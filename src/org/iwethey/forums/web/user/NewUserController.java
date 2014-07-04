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

import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;

import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.NavigationEntry;
import org.iwethey.forums.web.SelectEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.BindException;

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
 * Manages the user creation form. 
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: NewUserController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewUserController extends SimpleFormController implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	private UserManager mUserManager = null;
	private ArrayList mNavEntries = new ArrayList();

	/**
	 * Create a new NewUserController and set up the navigation entries.
	 * <p>
	 */
	public NewUserController()
		{
			mNavEntries.add(new NavigationEntry("create.user", "/user/new.iwt", true));
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
			model.put("navigation", mNavEntries.clone());
			return model;
		}

	/**
	 * Process a submitted form by creating the user and placing the user name in the session.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a User object).
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
								 Object command, BindException errors)
		{
			User user = (User) command;

			int id = mUserManager.createNewUser(user);

			HttpSession sess = request.getSession();
			sess.setAttribute(USER_ID_ATTRIBUTE, new Integer(id));

			return new ModelAndView(new RedirectView(getSuccessView()));
		}

	/**
	 * Load a blank user object for the form backing. If the nickname
	 * or password have been entered already (but the password or verify
	 * password was wrong) place them in the User object.
	 * <p>
	 * @param request The servlet request object.
	 * @return A prepopulated User object for use in the JSP form.
	 */
    protected Object formBackingObject(HttpServletRequest request)
		throws ServletException
		{
			User user = new User();
			user.setNickname(request.getParameter("nickname"));
			user.setUnencryptedPassword(request.getParameter("password"));

			return user;
		}

	public void setUserManager(UserManager mgr) { mUserManager = mgr; }
	public UserManager getUserManager() { return mUserManager; }
}
