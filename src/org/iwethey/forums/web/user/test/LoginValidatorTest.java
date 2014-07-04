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

package org.iwethey.forums.web.user.test;

import org.iwethey.forums.db.*;
import org.iwethey.forums.db.test.*;
import org.iwethey.forums.domain.*;
import org.iwethey.forums.web.user.*;

import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.validation.*;

import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Tests the LoginValidator object.
 * <p>
 * $Id: LoginValidatorTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class LoginValidatorTest extends TestCase
{
    private LoginValidator mVal;

    public void setUp()
		throws Exception
		{
			ApplicationContext mContext = new ClassPathXmlApplicationContext("test-context.xml");
			mVal = new LoginValidator();
			mVal.setUserManager((UserManager) mContext.getBean("userManager"));
		}

	public void testSupports()
		{
			assertTrue(mVal.supports(User.class));
		}

	public void testValidateNull()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");

			mVal.validate(null, errors);
			assertEquals("null 1", 1, errors.getFieldErrorCount("nickname"));
			assertEquals("null 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("nickname");
			assertEquals("null code 1", "error.nickname.empty", err.getCode());
			err = errors.getFieldError("unencryptedPassword");
			assertEquals("null code 2", "error.password.empty", err.getCode());
		}

	public void testValidateNullNickname()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");

			mVal.validate(u, errors);
			assertEquals("null nickname 1", 1, errors.getFieldErrorCount("nickname"));
			assertEquals("null nickname 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("nickname");
			assertEquals("null nickname code", "error.nickname.empty", err.getCode());
			err = errors.getFieldError("unencryptedPassword");
			assertEquals("null password code", "error.password.empty", err.getCode());
		}

	public void testValidateEmptyNickname()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("");

			mVal.validate(u, errors);
			assertEquals("empty nickname 1", 1, errors.getFieldErrorCount("nickname"));
			assertEquals("empty nickname 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("nickname");
			assertEquals("empty nickname code", "error.nickname.empty", err.getCode());
			err = errors.getFieldError("unencryptedPassword");
			assertEquals("null password code", "error.password.empty", err.getCode());
		}

	public void testValidateShortNickname()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("a");

			mVal.validate(u, errors);
			assertEquals("short nickname 1", 1, errors.getFieldErrorCount("nickname"));
			assertEquals("short nickname 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("nickname");
			assertEquals("short nickname code", "error.nickname.length", err.getCode());
			err = errors.getFieldError("unencryptedPassword");
			assertEquals("null password code", "error.password.empty", err.getCode());
		}

	public void testValidateNullPassword()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("spork");

			mVal.validate(u, errors);
			assertEquals("null password 1", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("null password 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("unencryptedPassword");
			assertEquals("null password code", "error.password.empty", err.getCode());
		}

	public void testValidateEmptyPassword()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("spork");
			u.setUnencryptedPassword("");

			mVal.validate(u, errors);
			assertEquals("empty password 1", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("empty password 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("unencryptedPassword");
			assertEquals("empty password code", "error.password.empty", err.getCode());
		}

	public void testValidateShortPassword()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("spork");
			u.setUnencryptedPassword("spork");

			mVal.validate(u, errors);
			assertEquals("short password 1", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("short password 2", 1, errors.getFieldErrorCount("unencryptedPassword"));
			FieldError err = errors.getFieldError("unencryptedPassword");
			assertEquals("short password code", "error.password.length", err.getCode());
		}


	public void testValidateBadLogin()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("spork");
			u.setUnencryptedPassword("blancmange");

			mVal.validate(u, errors);
			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("unencryptedPassword", 0, errors.getFieldErrorCount("unencryptedPassword"));
			assertEquals("bad login", 1, errors.getGlobalErrorCount());

			ObjectError err = errors.getGlobalError();
			assertEquals("global", "error.invalid.login", err.getCode());
		}

	public void testValidateGoodLogin()
		{
			User u = new User();
			BindException errors = new BindException(u, "nickname");
			u.setNickname("ut_spork1");
			u.setUnencryptedPassword("itchy1");

			mVal.validate(u, errors);
			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("unencryptedPassword", 0, errors.getFieldErrorCount("unencryptedPassword"));
			assertEquals("good login", 0, errors.getGlobalErrorCount());
		}
}
