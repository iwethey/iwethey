/**
   Copyright 2004 Scott Anderson

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.iwethey.forums.web.forum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.ForumManager;
import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.PostManager;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;
import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.KeyedEntry;
import org.iwethey.forums.web.NavigationEntry;
import org.iwethey.forums.web.board.BoardController;
import org.iwethey.forums.web.post.NewPostController;
import org.iwethey.forums.web.user.UserController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Manages the model for displaying a forum.
 * <p>
 * $Id: ForumController.java 76 2005-02-03 02:39:45Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ForumController extends MultiActionController implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	private ForumManager forumManager = null;
	private PostManager postManager = null;
	private UserManager userManager = null;

	public static NavigationEntry createNavigationEntry(Forum forum)
	{
		NavigationEntry nav = new NavigationEntry("show.forum", forumURI(forum), true);
		nav.addArg(forum.getDisplayName());
		return nav;
	}

	/**
	 * Creates the URI for linking to a particular forum.
	 */
	public static String forumURI(Forum forum)
	{
		return "/forum/show.iwt?forumid=" + forum.getId();
	}

	/**
	 * Creates the URI for marking a particular forum read.
	 */
	public static String markReadURI(Forum forum)
	{
		return "/forum/markRead.iwt?forumid=" + forum.getId() + "&markstamp=" + (new Date()).getTime();
	}

	/**
	 *  Prepare the model to show a single forum.
	 */
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HashMap model = new HashMap();

		int forumId = RequestUtils.getIntParameter(request, "forumid", 1);
		Forum forum = forumManager.getForumById(forumId);

		boolean showAll = true;
		boolean showChildren = true;

		User user = (User) request.getAttribute(USER_ATTRIBUTE);

		Date mark = user.getShowNewPostsOnly() ? user.getForumMark(forumId) : null;

		// HTML title
		KeyedEntry title = new KeyedEntry("title.forum");
		title.addArg(forum.getDisplayName());
		model.put("title", title);

		// Navigation bar
		List navigation = new ArrayList();
		navigation.add(BoardController.createNavigationEntry(forum.getBoard()));
		navigation.add(createNavigationEntry(forum));
		model.put("navigation", navigation);

		// Command bar
		List cmd = new ArrayList();

		// Use the time from when the forum was selected.
		if (!user.isAnonymous()) {
			cmd.add(new NavigationEntry("mark.forum.read", ForumController.markReadURI(forum)));
		}

		if (user.getShowNewPostsOnly())
		{
			cmd.add(new NavigationEntry("show.all.threads", UserController.toggleURI("showNewPostsOnly")));
			showAll = false;
		}
		else
		{
			cmd.add(new NavigationEntry("show.new.threads.only", UserController.toggleURI("showNewPostsOnly")));
		}

		if (user.getShowThreadsCollapsed())
		{
			cmd.add(new NavigationEntry("show.threads.expanded", UserController.toggleURI("showThreadsCollapsed")));
			showChildren = false;
		}
		else
		{
			cmd.add(new NavigationEntry("show.threads.collapsed", UserController.toggleURI("showThreadsCollapsed")));
		}

		if (!user.isAnonymous())
		{
			cmd.add(new NavigationEntry("add.topic", NewPostController.newURI(forum)));
		}

		model.put("commands", cmd);

		// PAGING
		int pageNumber = RequestUtils.getIntParameter(request, "pageNumber", 1);
		int batchSize = user.getForumBatchSize();

		List threads = forumManager.getThreadPage(forum,
												   (batchSize * (pageNumber - 1)), batchSize,
												   user.getSortByLastModified(),
												   mark);

		int count = forumManager.getThreadCount(forum, mark).intValue();
		model.put("itemCount", new Integer(count));
		model.put("pageNumber", new Integer(pageNumber));
		model.put("pageBreak", new Integer(PAGE_BREAK));
		model.put("pagerSize", new Integer(PAGER_SIZE));

		int pageCount = count / batchSize;
		if (count % batchSize != 0)
		{
			pageCount++;
		}
		model.put("pageCount", new Integer(pageCount));

		if (pageCount > PAGER_SIZE)
		{
			int start = pageNumber - PAGE_BREAK + 2;
			start = (start < 2 ? 2 : start);
			model.put("pageBarStart", new Integer(start));

			int end = start + PAGER_SIZE - 3;
			end = (end > (pageCount - 1) ? pageCount - 1 : end);
			model.put("pageBarEnd", new Integer(end));
		}
		else
		{
			model.put("pageBarStart", new Integer(2));
			model.put("pageBarEnd", new Integer(pageCount - 1));
		}

		// Populate the children for each thread.
		boolean getChildren = !user.getShowThreadsCollapsed();
		Date latest = null;

		for (Iterator i = threads.iterator(); i.hasNext(); )
		{
			Post p = (Post) i.next();

			Date last = p.getLastUpdated();
			if (latest == null || latest.compareTo(last) < 0)
			{
				latest = last;
			}

			if (getChildren) {
				postManager.getChildren(p);
			}
			else
			{
				p.setChildren(null);
			}
		}

		if (forum.getHeaderImage() != null)
		{
			request.setAttribute(HEADER_IMAGE_ATTRIBUTE, "/images/" + forum.getHeaderImage());
		}

		model.put("threads", threads);
		model.put("forum", forum);
		model.put("board", forum.getBoard());

		model.put("lastRead", user.getForumMark(forum));

		return new ModelAndView("forum/show", model);
	}

	/**
	 *  Mark a forum read.
	 */
	public ModelAndView markRead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int forumId = RequestUtils.getRequiredIntParameter(request, "forumid");
		Forum forum = forumManager.getForumById(forumId);

		User user = (User) request.getAttribute(USER_ATTRIBUTE);

		Date now = new Date(RequestUtils.getRequiredLongParameter(request, "markstamp"));
		user.markForumRead(forum, now);
		userManager.saveUserAttributes(user);

		return new ModelAndView(new RedirectView("../board/show.iwt?boardid=" + forum.getBoard().getId()));
	}

	public void setForumManager(ForumManager mgr) { forumManager = mgr; }
	public ForumManager getForumManager() { return forumManager; }

	public void setPostManager(PostManager mgr) { postManager = mgr; }
	public PostManager getPostManager() { return postManager; }

	public void setUserManager(UserManager mgr) { userManager = mgr; }
	public UserManager getUserManager() { return userManager; }
}
