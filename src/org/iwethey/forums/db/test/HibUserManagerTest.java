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

package org.iwethey.forums.db.test;

import org.iwethey.forums.db.*;
import org.iwethey.forums.domain.*;

import java.util.*;

/**
 * Tests the database user manager.
 * <p>
 * $Id: HibUserManagerTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibUserManagerTest extends HibernateTestHarness
{
    private UserManager mMgr;

    public void setUp() throws Exception
	{
		super.setUp();
		mMgr = (UserManager) context.getBean("userManager");
	}

    public void testGetUserList()
	{
		List l = mMgr.getUserList();
		User mu1 = (User) l.get(0);
		assertEquals("spork1", "ut_spork1", mu1.getNickname());
		User mu2 = (User) l.get(1);
		assertEquals("spork2", "ut_spork2", mu2.getNickname());
		User mu3 = (User) l.get(2);
		assertEquals("spork3", "ut_spork3", mu3.getNickname());
	}

    public void testGetUserByNickname()
	{
		assertTrue("exists", mMgr.getExists("ut_spork2"));

		User mu1 = mMgr.getUserByNickname("ut_spork2");
		assertEquals("spork", "ut_spork2", mu1.getNickname());

		assertTrue("exists", mMgr.getExists(mu1));
	}

    public void testGetUserById()
	{
		assertTrue("exists", mMgr.getExists("ut_spork2"));

		User mu1 = mMgr.getUserById(-2);
		assertEquals("spork", "ut_spork2", mu1.getNickname());
	}

	public void testUserCount()
	{
		assertEquals("count", 4, mMgr.getUserCount());
		assertEquals("count", 3, mMgr.getActiveUserCount());
	}

	public void testGetExists()
	{
		assertTrue(mMgr.getExists("ut_spork2"));

		User u = new User();
		u.setNickname("ut_spork3");
		assertTrue(mMgr.getExists(u));
	}

    public void testCreateUser()
	{
		User mu1 = mMgr.getUserByNickname("ut_spork42");
		assertNull("not there yet", mu1);

		mu1 = new User("ut_spork42", "fumble");
		mMgr.createNewUser(mu1);

		mu1 = mMgr.getUserByNickname("ut_spork42");
		assertEquals("spork42", "ut_spork42", mu1.getNickname());

		mMgr.removeUser(mu1);

		mu1 = mMgr.getUserByNickname("ut_spork42");
		assertNull("not there again", mu1);

		mu1 = new User("ut_spork42", "fumble");
		mMgr.saveUser(mu1);

		mu1 = mMgr.getUserByNickname("ut_spork42");
		assertEquals("spork42 2", "ut_spork42", mu1.getNickname());

		mMgr.removeUser(mu1);

		mu1 = mMgr.getUserByNickname("ut_spork42");
		assertNull("not there again 2", mu1);
	}

	public void testSavePassword()
	{
		User mu1 = mMgr.getUserByNickname("ut_spork3");
		mu1.setUnencryptedPassword("itchy100");
		mMgr.saveUser(mu1);

		assertTrue("Password changed", mMgr.login(mu1));

		mu1.setUnencryptedPassword("itchy3");
		mMgr.saveUser(mu1);

		assertTrue("Password changed", mMgr.login(mu1));
	}

	public void testProperties()
	{
		User mu1 = mMgr.getUserByNickname("ut_spork1");
		assertEquals("p1", "spork", mu1.getProperty("p1"));
		assertEquals("p2", "goomba", mu1.getProperty("p2"));

		mu1 = mMgr.getUserByNickname("ut_spork2");
		assertEquals("p1", "blancmange", mu1.getProperty("p1"));
		assertEquals("p2", "seasponge", mu1.getProperty("p2"));

		mu1 = mMgr.getUserByNickname("ut_spork3");
		assertEquals("p1", "lemur", mu1.getProperty("p1"));
		assertEquals("p2", "ntmfac", mu1.getProperty("p2"));

		assertNull("p3 null", mu1.getProperty("p3"));
		assertEquals("p3", "ntmfac", mu1.getProperty("p3", "ntmfac"));

		mu1.setProperty("p3", "cbod!");
		assertEquals("p3", "cbod!", mu1.getProperty("p3"));
		assertEquals("p3", "cbod!", mu1.getProperty("p3", "ntmfac"));

		mMgr.saveUser(mu1);
		mu1 = mMgr.getUserByNickname("ut_spork3");
		assertEquals("p3", "cbod!", mu1.getProperty("p3"));

		mu1.setProperty("p3", null);
		mMgr.saveUser(mu1);
		mu1 = mMgr.getUserByNickname("ut_spork3");
		assertNull("p3 null", mu1.getProperty("p3"));
	}

	public void testLogin()
	{
		User u = new User("ut_spork1", "no way");
		assertTrue("bad login", !mMgr.login(u));

		u = new User("ut_spork1", "itchy1");
		assertTrue("good login", mMgr.login(u));
	}

	public void testForumRead()
	{
		User mu1 = mMgr.getUserById(-1);
		Date mark = mu1.getForumMark(-2);
		long past = mark.getTime();
		long now = (new Date()).getTime();
		assertTrue(now - (15 * 60 * 1000) > past);
	}

	public void testAdmin()
	{
		User mu4 = mMgr.getUserById(-4);
		assertTrue(mu4.isAdmin());
	}
}
