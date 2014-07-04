/**
   Copyright 2004-2010 Scott Anderson and Mike Vitale

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

package org.iwethey.forums.web.admin;

import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;
import org.iwethey.forums.web.ControllerAttributes;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Check to see that the user has been authenticated as an admin
 * for pages that require an administrator to access.</p>
 *
 * <p>If the user is not authenticated, redirect to the login form.</p>
 *
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class AdminInterceptor extends HandlerInterceptorAdapter implements ControllerAttributes
{
	private UserManager mUserManager = null;

	/**
	 * <p>Check the session for an authenticated admin user. If none, redirect to
	 * the login page.</p>
	 *
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param handler The request handler processing this request.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		Integer id = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);

		if (id == null)
		{
			String uri = request.getServletPath();
			String query = request.getQueryString();

			ModelAndView mv = new ModelAndView(new RedirectView("../user/login.iwt"));

			throw new ModelAndViewDefiningException(mv);
		}
		else
		{
			User u = mUserManager.getUserById(id);

			return u.isAdmin();
		}
	}

	public void setUserManager(UserManager mgr) { mUserManager = mgr; }
	public UserManager getUserManager() { return mUserManager; }
}
