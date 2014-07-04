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

package org.iwethey.forums.web.post;

import java.io.IOException;
import java.util.ArrayList;
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
import org.iwethey.forums.web.forum.ForumController;
import org.iwethey.forums.web.user.UserController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Manages the model for displaying a post.
 * <p>
 * $Id: ShowPostController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ShowPostController extends AbstractController implements ControllerAttributes
{
    protected static final Log logger = LogFactory.getLog(ShowPostController.class);

	private ForumManager forumManager = null;
	private PostManager postManager = null;
	private UserManager userManager = null;

	public static NavigationEntry createNavigationEntry(Post post)
	{
		NavigationEntry nav = new NavigationEntry("show.post", "/post/show.iwt?postid=" + post.getId(), true);
		nav.addArg(post.getSubject());
		return nav;
	}

	/**
	 *  Prepare the model to show a single post.
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HashMap model = new HashMap();

		int postId = RequestUtils.getIntParameter(request, "postid", 1);
		Post post = postManager.getPostById(postId);
		if (post == null)
		{
			// do something here!
		}
		Forum forum = post.getForum();

		KeyedEntry title = new KeyedEntry("title.post");
		title.addArg(post.getSubject());
		model.put("title", title);

		List navigation = new ArrayList();
		navigation.add(BoardController.createNavigationEntry(forum.getBoard()));
		navigation.add(ForumController.createNavigationEntry(forum));
		navigation.add(createNavigationEntry(post));
		model.put("navigation", navigation);

		boolean showChildren = true;

		User user = (User) request.getAttribute(USER_ATTRIBUTE);

		List cmd = new ArrayList();

		if (user.getShowRepliesCollapsed())
		{
			cmd.add(new NavigationEntry("show.replies.expanded", UserController.toggleURI("showRepliesCollapsed")));
			showChildren = false;
		}
		else
		{
			cmd.add(new NavigationEntry("show.replies.collapsed", UserController.toggleURI("showRepliesCollapsed")));
		}

		if (!user.isAnonymous()) {
			cmd.add(new NavigationEntry("add.reply", ReplyPostController.replyURI(post)));
		}

		model.put("commands", cmd);

		List children = null;
		post.setChildren(null);

		Post thread = post.getThread();
		if (!user.getHierarchyPosition().equals("none"))
		{
			model.put("thread", thread);

			children = postManager.getChildren(thread);
		}

		if (showChildren)
		{
			if (children == null)
			{
				postManager.getChildren(post);
			}
			else if (post != thread && post.getChildren() == null)
			{
				List replies = new ArrayList();

				for (Iterator iter = children.iterator(); iter.hasNext(); )
				{
					Post p = (Post) iter.next();
					if (p.getLeftId() <= post.getLeftId())
					{
						continue;
					}
					if (p.getLeftId() < post.getRightId())
					{
						replies.add(p);
					}
					else
					{
						break;
					}
				}

				post.setChildren(replies);
			}
		}

		model.put("content", post);
		model.put("forum", forum);
		model.put("board", forum.getBoard());

		return new ModelAndView("post/show", model);
	}

	public void setForumManager(ForumManager mgr) { forumManager = mgr; }
	public ForumManager getForumManager() { return forumManager; }

	public void setPostManager(PostManager mgr) { postManager = mgr; }
	public PostManager getPostManager() { return postManager; }

	public void setUserManager(UserManager mgr) { userManager = mgr; }
	public UserManager getUserManager() { return userManager; }
}
