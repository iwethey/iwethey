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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Log the user out by clearing the user name from the session.
 * <p>
 * $Id: LogoutController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class LogoutController extends AbstractController implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Remove the user name from the session to indicate that
	 * the user is no longer logged in.
	 * <p>
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			HttpSession sess = request.getSession(false);
			if (sess != null)
				{
					sess.removeAttribute(USER_ID_ATTRIBUTE);
				}

			return new ModelAndView(new RedirectView("../main.iwt"));
		}
}
