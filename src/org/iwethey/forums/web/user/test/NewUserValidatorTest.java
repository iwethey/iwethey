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
 * Tests the NewUserValidator object.
 * <p>
 * $Id: NewUserValidatorTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewUserValidatorTest extends TestCase
{
    private NewUserValidator mVal;

    public void setUp()
		throws Exception
		{
			ApplicationContext mContext = new ClassPathXmlApplicationContext("test-context.xml");
			mVal = new NewUserValidator();
			mVal.setUserManager((UserManager) mContext.getBean("userManager"));
		}

	public void testSupports()
		{
			assertTrue(mVal.supports(User.class));
		}

	public void testValidateMissingCheck()
		{
			User u = new User();
			BindException errors = new BindException(u, "userInfo");
			u.setNickname("spork");
			u.setUnencryptedPassword("blancmange");
			u.setPasswordCheck("");

			mVal.validate(u, errors);
			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("password", 0, errors.getFieldErrorCount("password"));
			assertEquals("passwordCheck", 1, errors.getFieldErrorCount("passwordCheck"));

			FieldError err = errors.getFieldError("passwordCheck");
			assertEquals("null passwordCheck code", "error.password.empty", err.getCode());
		}

	public void testValidateDiffers()
		{
			User u = new User();
			BindException errors = new BindException(u, "userInfo");
			u.setNickname("spork");
			u.setUnencryptedPassword("blancmange");
			u.setPasswordCheck("sassy");

			mVal.validate(u, errors);
			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("password", 0, errors.getFieldErrorCount("password"));
			assertEquals("passwordCheck", 1, errors.getFieldErrorCount("passwordCheck"));

			FieldError err = errors.getFieldError("passwordCheck");
			assertEquals("differing passwordCheck code", "error.password.differs", err.getCode());
		}

	public void testValidateExists()
		{
			User u = new User();
			BindException errors = new BindException(u, "userInfo");
			u.setNickname("ut_spork1");
			u.setUnencryptedPassword("itchy1");
			u.setPasswordCheck("itchy1");

			mVal.validate(u, errors);
			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("password", 0, errors.getFieldErrorCount("password"));
			assertEquals("passwordCheck", 0, errors.getFieldErrorCount("passwordCheck"));

			ObjectError err = errors.getGlobalError();
			assertEquals("global", "error.existing.login", err.getCode());
		}

	public void testValidateGoodLogin()
		{
			User u = new User();
			BindException errors = new BindException(u, "userInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");

			mVal.validate(u, errors);

			assertEquals("nickname", 0, errors.getFieldErrorCount("nickname"));
			assertEquals("password", 0, errors.getFieldErrorCount("password"));
			assertEquals("passwordCheck", 0, errors.getFieldErrorCount("passwordCheck"));
			assertEquals("good login", 0, errors.getGlobalErrorCount());
		}
}
