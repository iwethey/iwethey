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

import java.util.List;

import org.iwethey.forums.db.HibForumManager;
import org.iwethey.forums.domain.*;

/**
 * Tests the forum database manager.
 * <p>
 * $Id: HibForumManagerTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibForumManagerTest extends HibernateTestHarness
{
    private HibForumManager mMgr;
    private UserManager mUserMgr;
    private BoardManager mBoardMgr;
    private CategoryManager mCategoryMgr;

    public void setUp()
		throws Exception
		{
			super.setUp();
			mMgr = (HibForumManager) context.getBean("forumManager");
			mUserMgr = (UserManager) context.getBean("userManager");
			mBoardMgr = (BoardManager) context.getBean("boardManager");
			mCategoryMgr = (CategoryManager) context.getBean("categoryManager");
		}

	public void testGetForum()
		{
			Forum f = mMgr.getForumById(-1);

			assertEquals("id", -1, f.getId());
			assertEquals("board id", -1, f.getBoard().getId());
			assertEquals("category id", -1, f.getCategory().getId());
			assertEquals("order", 1, f.getOrder());
			assertEquals("posts", 100, f.getPostCount());
			assertEquals("name", "Tines", f.getDisplayName());
			assertEquals("desc", "Tines: long or short?", f.getDescription());
			assertEquals("created by", "ut_spork1", f.getCreatedBy().getNickname());
		}

	public void testGetThreads()
		{
			Forum f = mMgr.getForumById(-2);

			List threads = mMgr.getThreadPage(f, 0, 3, true, null);
			assertEquals(3, threads.size());
			assertEquals(-1, ((Post)threads.get(0)).getId());
			assertEquals(-3, ((Post)threads.get(1)).getId());
			assertEquals(-5, ((Post)threads.get(2)).getId());

			threads = mMgr.getThreadPage(f, 3, 3, true, null);
			assertEquals(3, threads.size());
			assertEquals(-7, ((Post)threads.get(0)).getId());
			assertEquals(-9, ((Post)threads.get(1)).getId());
			assertEquals(-11, ((Post)threads.get(2)).getId());

			threads = mMgr.getThreadPage(f, 6, 3, true, null);
			assertEquals(3, threads.size());
			assertEquals(-13, ((Post)threads.get(0)).getId());
			assertEquals(-15, ((Post)threads.get(1)).getId());
			assertEquals(-17, ((Post)threads.get(2)).getId());

			threads = mMgr.getThreadPage(f, 9, 3, true, null);
			assertEquals(2, threads.size());
			assertEquals(-19, ((Post)threads.get(0)).getId());
			assertEquals(-21, ((Post)threads.get(1)).getId());
		}

	public void testCreateForum()
		{
			User u = mUserMgr.getUserById(-2);
			Category c = mCategoryMgr.getCategoryById(-3);

			Forum f = new Forum(c, 3, "Moo", "Moo de Doo", "", u);

			int id = mMgr.createForum(f);

			f = mMgr.getForumById(id);

			assertEquals("board id", -2, f.getBoard().getId());
			assertEquals("category id", -3, f.getCategory().getId());
			assertEquals("order", 3, f.getOrder());
			assertEquals("posts", 0, f.getPostCount());
			assertEquals("name", "Moo", f.getDisplayName());
			assertEquals("desc", "Moo de Doo", f.getDescription());
			assertEquals("created by", "ut_spork2", f.getCreatedBy().getNickname());

			mMgr.removeForum(f);
			f = mMgr.getForumById(id);
			assertNull(f);
		}
/*

	public void testSaveForum()
		{
			User u = mUserMgr.getUserById(-3);
			Board b = mBoardMgr.getBoardById(-1);

			Forum c = new Forum(b, 4, "Grumble Gus", u);

			mMgr.saveForum(c);

			int id = f.getId();
			c = mMgr.getForumById(f.getId());

			assertEquals("id", id, f.getId());
			assertEquals("board id", -1, f.getBoard().getId());
			assertEquals("name", "Grumble Gus", f.getDisplayName());
			assertEquals("order", 4, f.getOrder());
			assertEquals("created by", "ut_spork3", f.getCreatedBy().getNickname());

			f.setDisplayName("Flink");

			mMgr.saveForum(c);

			c = mMgr.getForumById(f.getId());

			assertEquals("id", id, f.getId());
			assertEquals("board id", -1, f.getBoard().getId());
			assertEquals("name", "Flink", f.getDisplayName());
			assertEquals("order", 4, f.getOrder());
			assertEquals("created by", "ut_spork3", f.getCreatedBy().getNickname());

			mMgr.removeForum(c);
			c = mMgr.getForumById(id);
			assertNull(c);
		}
*/
}
