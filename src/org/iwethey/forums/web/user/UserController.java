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

package org.iwethey.forums.web.user;

import org.iwethey.forums.domain.PostManager;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;

import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.NavigationEntry;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import org.springframework.web.bind.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles general User URLs.
 * <p>
 * $Id: UserController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class UserController extends MultiActionController implements ControllerAttributes
{
	/** The User data access object. */
	private UserManager userManager;
	/** The Post data access object. */
	private PostManager postManager;

	/**
	 * Display a user's information and post history.
	 * <p>
	 * @param request Current HTTP request object.
	 * @param response Current HTTP response object.
	 */
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		HashMap model = new HashMap();
		Integer id = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);

		if (id == null)
		{
			id = new Integer(0);
		}

		int did = RequestUtils.getIntParameter(request, "userid", id.intValue());

		User user = userManager.getUserById(did);
		model.put("displayUser", user);

		List navigation = new ArrayList();

		NavigationEntry nav = new NavigationEntry("show.user", "/user/show.iwt", true);

		nav.addArg(request.getAttribute("username"));
		navigation.add(nav);
		model.put("navigation", navigation);

		model.put("posts", postManager.getPostsByUser(user));
		
		return new ModelAndView("user/show", model);
	}

	/**
	 * Display all users in a statistics chart.
	 * <p>
	 * @param request Current HTTP request object.
	 * @param response Current HTTP response object.
	 */
	public ModelAndView statistics(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		HashMap model = new HashMap();
		List navigation = new ArrayList();
		NavigationEntry nav = new NavigationEntry("user.statistics", "/user/statistics.iwt", true);

		navigation.add(nav);
		model.put("navigation", navigation);

		return new ModelAndView("user/statistics", model);
	}

	/**
	 * Display all users in a active users chart.
	 * <p>
	 * @param request Current HTTP request object.
	 * @param response Current HTTP response object.
	 */
	public ModelAndView active(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		HashMap model = new HashMap();
		List navigation = new ArrayList();
		NavigationEntry nav = new NavigationEntry("user.active", "/user/active.iwt", true);

		navigation.add(nav);

		model.put("navigation", navigation);
		List<User> activeUsers = userManager.getActiveUsers();
		
		for (User u : activeUsers)
		{
			if (!u.getShowInActives())
			{
				u.setNickname("UQC");
			}
		}
		
		model.put("userList", userManager.getActiveUsers());

		return new ModelAndView("user/active", model);
	}

	public static String toggleURI(String toggle)
	{
		return "/user/toggle.iwt?toggle=" + toggle;
	}

	/**
	 * Toggle binary settings. Requires a request parameter of "toggle" 
	 * that contains the name of the property to toggle.
	 */
	public ModelAndView toggle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = (User) request.getAttribute(USER_ATTRIBUTE);

		String propName = RequestUtils.getStringParameter(request, "toggle", null);
		if (user != null && propName != null && !propName.equals(""))
		{
			BeanWrapper wrap = new BeanWrapperImpl(user);
			Boolean val = (Boolean) wrap.getPropertyValue(propName);

			wrap.setPropertyValue(propName, new Boolean(!val.booleanValue()));
			userManager.saveUserAttributes(user);
		}

		return new ModelAndView(new RedirectView(request.getHeader("referer")));
	}

	public void setUserManager(UserManager mgr) { userManager = mgr; }
	public UserManager getUserManager() { return userManager; }

	public void setPostManager(PostManager mgr) { postManager = mgr; }
	public PostManager getPostManager() { return postManager; }
}
