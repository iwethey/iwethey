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
 * Manages the new thread form.
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: NewPostController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewPostController extends PostCreation
{
	public static String newURI(Forum forum)
		{
			return "/post/new.iwt?forumid=" + forum.getId();
		}

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
			Map model = super.referenceData(request, command, errors);

			List navigation = (List) model.get("navigation");
			navigation.add(new NavigationEntry("new.post", "/post/new"));

			return model;
		}

	/**
	 * New threads send the forum ID right in the request parameters.
	 * <p>
	 * @param request The servlet request object.
	 */
	protected Forum getForum(HttpServletRequest request)
		throws Exception
		{
			return getForumManager().getForumById(RequestUtils.getIntParameter(request, "forumid", 0));
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
			return showForm(request, errors, "/post/new");
		}

	/**
	 * Load a blank post object for the form backing.
	 * <p>
	 * @param request The servlet request object.
	 * @return A prepopulated Post object for use in the JSP form.
	 */
    protected Object formBackingObject(HttpServletRequest request)
		throws ServletException
		{
			Integer id = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);
			User user = getUserManager().getUserById(id.intValue());
			Forum forum = getForumManager().getForumById(RequestUtils.getIntParameter(request, "forumid", 0));

			Post post = new Post();
			post.setParent(null);
			post.setForum(forum);
			post.setThread(post);
			post.setCreatedBy(user);
			post.setOriginalSignature(user.getSignature());
			return post;
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
			super.onBind(request, command);
			Post p = (Post) command;
			p.setCreated(new Date());
		}
}
