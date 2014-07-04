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

import org.iwethey.forums.domain.Board;
import org.iwethey.forums.domain.BoardManager;
import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.ForumManager;
import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.PostManager;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;

import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.NavigationEntry;
import org.iwethey.forums.web.SelectEntry;

import org.iwethey.forums.web.board.BoardController;
import org.iwethey.forums.web.forum.ForumController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import org.springframework.web.bind.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.web.util.WebUtils;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shared code for creating new posts.
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: PostCreation.java 79 2005-05-09 23:47:19Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class PostCreation extends AbstractFormController implements ControllerAttributes
{
	public final static int POST_SUBJECT_LENGTH = 60;

    protected final Log logger = LogFactory.getLog(getClass());

	private PostManager mPostManager = null;
	private ForumManager mForumManager = null;
	private BoardManager mBoardManager = null;
	private UserManager mUserManager = null;

	public PostCreation() {}

	/**
	 * Places the navigation entries in the model for the header to access.
	 * <p>
	 * @param request The servlet request object.
	 * @param command The command object created and populated from the form.
	 * @param errors Any errors discovered during validation.
	 * @return The information to place in the model.
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors)
		throws Exception
		{
			Map model = new HashMap();

			Forum forum = getForum(request);
			model.put("forum", forum);

			model.put("subjectLength", new Integer(POST_SUBJECT_LENGTH));

			List navigation = new ArrayList();
			navigation.add(BoardController.createNavigationEntry(forum.getBoard()));
			navigation.add(ForumController.createNavigationEntry(forum));
			model.put("navigation", navigation);

			return model;
		}

	/**
	 * Allow each subclass to get the current forum in a context-specific way.
	 * <p>
	 * @param request The servlet request object.
	 */
	protected abstract Forum getForum(HttpServletRequest request) throws Exception;


	/**
	 * Process a submitted form by saving the post.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a Post object).
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
								 Object command, BindException errors)
		throws Exception
		{
			Post post = (Post) command;
			post.setLastUpdated(new Date());

			mPostManager.savePost(post);

			return new ModelAndView(new RedirectView("../forum/show.iwt?forumid=" + post.getForum().getId()));
		}

	/**
	 * Check to see if there are form errors. If so, show the form again, otherwise
	 * process the submission.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a User object).
	 * @param errors The Spring errors object.
	 */
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
												 Object command, BindException errors) throws Exception
	{
		boolean preview = RequestUtils.getBooleanParameter(request, "preview", false);
		String submitBtnVal = RequestUtils.getStringParameter(request, "SUBMIT");
		boolean ignorePreview = "Save without Preview".equals(submitBtnVal);

		if (ignorePreview)
		{
			preview = false;
		}

		if (errors.hasErrors() || preview)
		{
			ModelAndView mv = showForm(request, response, errors);
			mv.addObject("preview", new Boolean(preview));
			return mv;
		}
		else
		{
			return onSubmit(request, response, command, errors);
		}
	}

	/**
	 * Extra binding. Spring does not automatically convert booleans
	 * with false values.
	 * <p>
	 * @param request The servlet request object.
	 * @param command The form backing store object (a User object).
	 */
	protected void onBind(HttpServletRequest request, Object command)
			throws Exception
		{
			Post p = (Post) command;

			p.setConvertNewlines(RequestUtils.getBooleanParameter(request, "convertNewlines", false));
			p.setConvertLinks(RequestUtils.getBooleanParameter(request, "convertLinks", false));
			p.setConvertCodes(RequestUtils.getBooleanParameter(request, "convertCodes", false));
			p.setConvertHtml(RequestUtils.getBooleanParameter(request, "convertHtml", false));
		}

	public void setPostManager(PostManager pm) { mPostManager = pm; }
	public PostManager getPostManager() { return mPostManager; }

	public void setUserManager(UserManager um) { mUserManager = um; }
	public UserManager getUserManager() { return mUserManager; }

	public void setForumManager(ForumManager fm) { mForumManager = fm; }
	public ForumManager getForumManager() { return mForumManager; }

	public void setBoardManager(BoardManager bm) { mBoardManager = bm; }
	public BoardManager getBoardManager() { return mBoardManager; }
}
