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

package org.iwethey.forums.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iwethey.forums.domain.AdminManager;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * <p>Loads the model with information for the header and footer.</p>
 *
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class HeaderInterceptor extends HandlerInterceptorAdapter implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	private UserManager mUserManager = null;
	private AdminManager mAdminManager = null;
	private MessageSource mMessageSource = null;

	/**
	 * Load the request attributes with the User object (if authenticated)
	 * and start time for the page for audit purposes.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param handler The request handler processing this request.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		Date now = new Date();
		request.setAttribute("now", now);

		long start = now.getTime();
		request.setAttribute("start", new Long(start));

		Integer id = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);

		User user = null;

		if (id == null)
		{
			user = (User) WebUtils.getSessionAttribute(request, USER_ATTRIBUTE);

			if (user == null)
			{
				user = new User("Anonymous");
				WebUtils.setSessionAttribute(request, USER_ATTRIBUTE, user);
			}
		}
		else
		{
			user = mUserManager.getUserById(id.intValue());
			user.setLastPresent(new Date());
			mUserManager.saveUserAttributes(user);
		}

		request.setAttribute("username", user.getNickname());
		request.setAttribute(USER_ATTRIBUTE, user);

			System.out.println("Local Address  = [" + request.getLocalAddr() + "]");
			System.out.println("Local Name     = [" + request.getLocalName() + "]");
			System.out.println("Remote Address = [" + request.getRemoteAddr() + "]");
			System.out.println("Remote Host    = [" + request.getRemoteHost() + "]");
			System.out.println("Remote Port    = [" + request.getRemotePort() + "]");
			System.out.println("Remote User    = [" + request.getRemoteUser() + "]");
			System.out.println("Context Path   = [" + request.getContextPath() + "]");
			System.out.println("====================");

			Cookie[] cookies = request.getCookies();
			if (cookies != null)
			{
				for (int i = 0; i < cookies.length; i++)
				{
					Cookie cookie = cookies[i];

					System.out.println("Cookie Domain = [" + cookie.getDomain() + "]");
					System.out.println("Cookie Name   = [" + cookie.getName() + "]");
					System.out.println("Cookie Value  = [" + cookie.getValue() + "]");
					System.out.println("Cookie Expire = [" + cookie.getMaxAge() + "]");
					System.out.println("====================");

					if ("iwt_cookie".equals(cookie.getName())) {
						cookie.setMaxAge(1000 * 60 * 60 * 24 * 30 * 6);
						response.addCookie(cookie);
					}
				}
			}
			else
			{
				System.out.println("No cookies were found in the request");
			}

			Cookie newCookie = new Cookie("iwt_cookie", "harrr2!");
			newCookie.setPath(request.getContextPath());
			newCookie.setDomain(request.getLocalName());
			newCookie.setMaxAge(1000 * 60 * 60 * 24 * 30 * 6);
			response.addCookie(newCookie);

		request.setAttribute(HEADER_IMAGE_ATTRIBUTE, "/images/iwethey-lrpd-small.png");

		return true;
	}

	/**
	 * Load the model with the information needed for the header JSP,
	 * including user statistics, the quote-of-the-page, and the
	 * navigation bar links. Sets the last URI attribute in the session.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param handler The request handler processing this request.
	 * @param mv The ModelAndView created by the controller wrapped by this interceptor.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
		throws Exception
	{
		String currentUri = request.getRequestURI();
		if (! (
				currentUri.indexOf("toggle") >= 0
				|| currentUri.indexOf("logout") >= 0
				|| currentUri.indexOf("mark") >= 0
				) )
		{
			String query = request.getQueryString();
			if (query != null)
			{
				currentUri = currentUri + "?" + query;
			}
			WebUtils.setSessionAttribute(request, LAST_URI_ATTRIBUTE, currentUri);
		}

		View view = mv.getView();
		if (view != null && view instanceof RedirectView)
		{
			return;
		}

		// Copy the stuff set above on the request into the model.
		// Some view engines (Velocity as an example) use the model differently.
		mv.addObject("now", request.getAttribute("now"));
		mv.addObject("start", request.getAttribute("start"));
		mv.addObject(USER_ATTRIBUTE, request.getAttribute(USER_ATTRIBUTE));
		mv.addObject("username", request.getAttribute("username"));

		mv.addObject("userCount", new Integer(mUserManager.getUserCount()));
		int actives = mUserManager.getActiveUserCount();
		mv.addObject("activeUserCount", new Integer(actives));
		mv.addObject("activeSingle", Boolean.valueOf(actives == 1));

		mv.addObject("lph", new Integer(3));
		mv.addObject("mpl", new Integer(60/3));

		mv.addObject("notice", mAdminManager.getNotice());

		mv.addObject("lrpd", mAdminManager.getLRPD());

		List nav = null;
		Map model = mv.getModel();
		if (model != null)
		{
			nav = (List) model.get("navigation");
		}
		if (nav == null)
		{
			nav = new ArrayList();
		}

		nav.add(0, new NavigationEntry("home", "/main.iwt"));

		mv.addObject("navigation", nav);

		//mv.addObject("ctx", new ContextTool(request, response, RequestContextUtils.getLocale(request), mMessageSource));
	}

	public void setUserManager(UserManager mgr) { mUserManager = mgr; }
	public UserManager getUserManager() { return mUserManager; }

	public void setAdminManager(AdminManager mgr) { mAdminManager = mgr; }
	public AdminManager getAdminManager() { return mAdminManager; }

	public void setMessageSource(MessageSource source) { mMessageSource = source; }
	public MessageSource getMessageSource() { return mMessageSource; }
}
