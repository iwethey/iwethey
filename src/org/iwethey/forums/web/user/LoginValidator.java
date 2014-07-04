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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Validates the information from the login form.
 * <p>
 * $Id: LoginValidator.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class LoginValidator implements Validator
{
    protected final Log logger = LogFactory.getLog(getClass());

	private static final int DEFAULT_MIN_NICKNAME = 3;
	private static final int DEFAULT_MIN_PASSWORD = 6;

	private int mMinNickname = DEFAULT_MIN_NICKNAME;
	private int mMinPassword = DEFAULT_MIN_PASSWORD;

	private UserManager mUserManager = null;

	/**
	 * Checks objects for form backing compatibility. Only supports User objects.
	 * <p>
	 */
    public boolean supports(Class cl)
		{
			return User.class.isAssignableFrom(cl);
		}

	/**
	 * Validate the information on the form. Also checks the User Manager
	 * to authenticate the user if there are no errors on the form.
	 * <p>
	 * @param obj The form backing object to validate.
	 * @param errors The errors object for error messages.
	 */
    public void validate(Object obj, Errors errors)
		{
			User u = (User) obj;

			if (u == null)
				{
					errors.rejectValue("nickname", "error.nickname.empty", null, "User nickname required.");
					errors.rejectValue("unencryptedPassword", "error.password.empty", null, "User password required.");
				}
			else
				{
					checkNickname(u, errors);
					checkPassword(u, errors);

					if (errors.getErrorCount() == 0)
						{
							if (!mUserManager.login(u))
								errors.reject("error.invalid.login",
											  "User nickname does not exist, or incorrect password.");
						}
				}
        }

	/**
	 * Check the user nickname for errors. Checks for empty or too-short names.
	 * <p>
	 * @param u The user object to check.
	 * @param errors The error object for recording errors.
	 */
    protected void checkNickname(User u, Errors errors)
		{
			String nickname = u.getNickname();

			if (nickname == null || nickname.length() == 0)
				{
					errors.rejectValue("nickname", "error.nickname.empty", null, "User nickname required.");
				}
			else if (nickname != null && nickname.length() < mMinNickname)
				{
					errors.rejectValue("nickname", "error.nickname.length",
									   new Object[] {new Integer(mMinNickname)},
									   "Nickname is too short.");
				}
		}				

	/**
	 * Check the user password for errors. Checks for empty or too-short passwords.
	 * <p>
	 * @param u The user object to check.
	 * @param errors The error object for recording errors.
	 */
    protected void checkPassword(User u, Errors errors)
		{
			String password = u.getUnencryptedPassword();

			if (password == null || password.length() == 0)
				{
					errors.rejectValue("unencryptedPassword", "error.password.empty", null, "User password required.");
				}
			else if (password != null && password.length() < mMinPassword)
				{
					errors.rejectValue("unencryptedPassword", "error.password.length",
									   new Object[] {new Integer(mMinPassword)}, "Password is too short.");
				}
        }

	public void setMinNickname(int len) { mMinNickname = len; }
	public int getMinNickname() { return mMinNickname; }

	public void setMinPassword(int len) { mMinPassword = len; }
	public int getMinPassword() { return mMinPassword; }

	public void setUserManager(UserManager mgr) { mUserManager = mgr; }
	public UserManager getUserManager() { return mUserManager; }
}
