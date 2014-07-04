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

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.web.NavigationEntry;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * Manages the new post (response) form.
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: ReplyPostController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ReplyPostController extends PostCreation
{
	public static String replyURI(Post post)
	{
		return "/post/reply.iwt?postid=" + post.getId();
	}

	/**
	 * Places the navigation entries in the model for the header to access.
	 * <p>
	 * @param request The servlet request object.
	 * @param command The command object created and populated from the form.
	 * @param errors Any errors discovered during validation.
	 * @return The information to place in the model.
	 */
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception
	{
		Map model = super.referenceData(request, command, errors);

		int responseId = RequestUtils.getRequiredIntParameter(request, "postid");
		Post post = getPostManager().getPostById(responseId);
		model.put("replyTo", post);

		List navigation = (List) model.get("navigation");
		NavigationEntry navEntry = new NavigationEntry("reply.post", "/post/reply");
		navEntry.addArg(post.getSubject());
		navigation.add(navEntry);

		return model;
	}

	/**
	 * Responses send the post ID in the request parameters.
	 * <p>
	 * @param request The servlet request object.
	 */
	protected Forum getForum(HttpServletRequest request) throws Exception
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
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception
	{
		return showForm(request, errors, "/post/reply");
	}

	/**
	 * Load a blank user object for the form backing. If the nickname
	 * has been entered already (but the password was wrong) place
	 * it in the Post object.
	 * <p>
	 * @param request The servlet request object.
	 * @return A prepopulated Post object for use in the JSP form.
	 */
	@Override
    protected Object formBackingObject(HttpServletRequest request) throws ServletException
	{
		int responseId = RequestUtils.getRequiredIntParameter(request, "postid");
		Post respondTo = getPostManager().getPostById(responseId);

		Integer id = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);
		User user = getUserManager().getUserById(id.intValue());
		Forum forum = respondTo.getForum();

		Post post = new Post();
		post.setParent(respondTo);
		post.setForum(forum);
		post.setThread(respondTo.getThread());
		post.setCreatedBy(user);
		post.setOriginalSignature(user.getSignature());

		if (user.getAutofillSubject())
		{
			String subject = respondTo.getSubject();

			if (!(subject.startsWith("Re: ") || subject.startsWith("re: ")))
			{
				int end = subject.length();

				if (end > POST_SUBJECT_LENGTH - 4)
				{
					end = POST_SUBJECT_LENGTH - 4;
				}

				subject = "Re: " + subject.substring(0, end);
			}

			post.setSubject(subject);
		}

		return post;
	}
}
