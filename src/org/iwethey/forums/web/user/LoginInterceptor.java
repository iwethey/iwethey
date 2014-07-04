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

import org.iwethey.forums.web.ControllerAttributes;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.springframework.web.servlet.view.RedirectView;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Check to see that the user has been authenticated for pages
 * that require an authenticated user to access.
 * <p>
 * If the user is not authenticated, redirect to the login form.
 * <p>
 * $Id: LoginInterceptor.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class LoginInterceptor extends HandlerInterceptorAdapter implements ControllerAttributes
{
	/**
	 * Check the session for an authenticated user name. If none, redirect to
	 * the login page.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param handler The request handler processing this request.
	 */
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
					return true;
				}
		}
}
