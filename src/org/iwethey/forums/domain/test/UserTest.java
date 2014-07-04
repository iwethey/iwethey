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

package org.iwethey.forums.domain.test;

import org.iwethey.forums.domain.*;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/** 
 * Tests the User class.
 * <p>
 * $Id: UserTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class UserTest extends TestCase
{
	public void testProperties()
		{
			User mu1 = new User();

			mu1.setProperty("p1", "spork");
			assertEquals("p1", "spork", mu1.getProperty("p1"));

			Map props = new HashMap();
			props.put("p2", "foodle");
			mu1.setProperties(props);

			assertEquals("p2", "foodle", mu1.getProperty("p2"));

			mu1.setProperty("p3", true);
			assertEquals("p3", "1", mu1.getProperty("p3"));
			assertTrue("p3", mu1.getPropertyBoolean("p3"));
			assertTrue("p3", !mu1.getPropertyBoolean("p1"));
			assertTrue("p3", !mu1.getPropertyBoolean("p4"));

			mu1.setProperty("p3", 42);
			assertEquals("p3", "42", mu1.getProperty("p3"));
			assertEquals("p3", 42, mu1.getPropertyInt("p3"));
			
		}

	public void testCreateFrom()
		{
			User u = new User("nickname", "password");
			u.setId(42);
			u.setPasswordCheck("passwordCheck");
			Date ts = new Date();
			u.setCreated(ts);
			u.setLastPresent(ts);
			u.setProperty("foo", "spork");
			u.setProperty("moo", "blancmange");

			User mu = new User(u);
			assertEquals("id", 42, mu.getId());
			assertEquals("nickname", "nickname", mu.getNickname());
			assertEquals("password", "password", mu.getUnencryptedPassword());
			assertEquals("passwordCheck", "passwordCheck", mu.getPasswordCheck());
			assertEquals("created", ts, mu.getCreated());
			assertEquals("lastPresent", ts, mu.getLastPresent());
			assertEquals("foo", "spork", mu.getProperty("foo"));
			assertEquals("moo", "blancmange", mu.getProperty("moo"));
		}
}
