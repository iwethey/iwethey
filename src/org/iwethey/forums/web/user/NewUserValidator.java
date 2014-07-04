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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Validates the information from the create user form.
 * <p>
 * $Id: NewUserValidator.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewUserValidator extends LoginValidator
{
    protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Validate the information on the form. Also checks the User Manager
	 * to see if the given user name already exists.
	 * <p>
	 * @param obj The form backing object to validate.
	 * @param errors The errors object for error messages.
	 */
    public void validate(Object obj, Errors errors)
		{
			User u = (User) obj;

			if (u != null)
				{
					checkNickname(u, errors);
					checkPassword(u, errors);
					checkPasswordCheck(u, errors);

					if (errors.getErrorCount() == 0)
						{
							if (getUserManager().getUserByNickname(u.getNickname()) != null)
								{
									errors.reject("error.existing.login",
												  new Object[] { u.getNickname() },
												  "User already exists. Please choose another name.");
									u.setNickname(null);
								}
						}
				}
        }

	/**
	 * Check the user verification password for errors. Checks for empty or 
	 * non-matching passwords. The two passwords must match for data-entry
	 * validation.
	 * <p>
	 * @param u The user object to check.
	 * @param errors The error object for recording errors.
	 */
	protected void checkPasswordCheck(User u, Errors errors)
		{
			String passwordCheck = u.getPasswordCheck();

			if (passwordCheck == null || passwordCheck.length() == 0)
				{
					errors.rejectValue("passwordCheck", "error.password.empty", null, "User password (again) required.");
				}
			else if (passwordCheck != null && !passwordCheck.equals(u.getUnencryptedPassword()))
				{
					errors.rejectValue("passwordCheck", "error.password.differs", null, "Must match password.");
				}
		}
}
