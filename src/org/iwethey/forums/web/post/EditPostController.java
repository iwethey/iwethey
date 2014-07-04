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

import org.springframework.web.bind.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the user login form. 
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: EditPostController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class EditPostController extends PostCreation
{
    /** Logger for this class and subclasses */
    private final Log logger = LogFactory.getLog(getClass());

	public static String editURI(Post post)
		{
			return "/post/edit.iwt?postid=" + post.getId();
		}

	/**
	 * Places the navigation entries in the model for the header to access.
	 * <p>
	 * @param request The servlet request object.
	 * @return The information to place in the model.
	 */
	protected Map referenceData(HttpServletRequest request)
		throws Exception
		{
			Map model = new HashMap();

			Forum forum = null;
			NavigationEntry currentEntry = null;

			int postId = RequestUtils.getIntParameter(request, "postid", 0);
			Post post = getPostManager().getPostById(postId);

			model.put("editing", post);

			forum = post.getForum();
			currentEntry = new NavigationEntry("edit.post", "/post/edit");

			List navigation = new ArrayList();
			navigation.add(BoardController.createNavigationEntry(forum.getBoard()));
			navigation.add(ForumController.createNavigationEntry(forum));
			navigation.add(currentEntry);
			model.put("navigation", navigation);

			return model;
		}

	/**
	 * Responses send the post ID in the request parameters.
	 * <p>
	 * @param request The servlet request object.
	 */
	protected Forum getForum(HttpServletRequest request)
		throws Exception
		{
			int responseId = RequestUtils.getRequiredIntParameter(request, "postid");
			Post post = getPostManager().getPostById(responseId);
			return post.getForum();
		}

	/**
	 * Determine which edit form to show, setting the view appropriately.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors)
		throws Exception
		{
			return showForm(request, errors, "/post/edit");
		}

	/**
	 * Process a submitted form by saving the post and history.
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
			Post newPost = (Post) command;

			Post oldPost = getPostManager().getPostById(newPost.getId());

			logger.error("COMMAND: " + newPost);
			logger.error("OLD    : " + oldPost);

			oldPost.editFromPost(newPost, (User) request.getAttribute(USER_ATTRIBUTE));

			logger.error("OLDNEW : " + oldPost);

			return super.onSubmit(request, response, oldPost, errors);
		}

	/**
	 * Load a blank user object for the form backing. If the nickname
	 * has been entered already (but the password was wrong) place
	 * it in the Post object.
	 * <p>
	 * @param request The servlet request object.
	 * @return A prepopulated Post object for use in the JSP form.
	 */
    protected Object formBackingObject(HttpServletRequest request)
		throws ServletException
		{
			Post post = null;

			int postId = RequestUtils.getIntParameter(request, "postid", 0);

			post = getPostManager().getPostById(postId);

			return post.clone();
		}
}
