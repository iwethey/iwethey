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

import junit.framework.TestCase;

/**
 * Tests the category database manager.
 * <p>
 * $Id: HibCategoryManagerTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibCategoryManagerTest extends HibernateTestHarness
{
    private HibCategoryManager mMgr;
    private UserManager mUserMgr;
    private BoardManager mBoardMgr;

    public void setUp()
		throws Exception
		{
			super.setUp();
			mMgr = (HibCategoryManager) context.getBean("categoryManager");
			mUserMgr = (UserManager) context.getBean("userManager");
			mBoardMgr = (BoardManager) context.getBean("boardManager");
		}

	public void testGetCategory()
		{
			Category c = mMgr.getCategoryById(-1);
			
			assertEquals("id", -1, c.getId());
			assertEquals("board id", -1, c.getBoard().getId());
			assertEquals("name", "General Sporks", c.getDisplayName());
			assertEquals("order", 1, c.getOrder());
			assertEquals("created by", "ut_spork1", c.getCreatedBy().getNickname());
		}

	public void testCreateCategory()
		{
			User u = mUserMgr.getUserById(-2);
			Board b = mBoardMgr.getBoardById(-2);

			Category c = new Category(b, 3, "Moo de Doo", u);
			
			int id = mMgr.createCategory(c);

			c = mMgr.getCategoryById(id);

			assertEquals("id", id, c.getId());
			assertEquals("board id", -2, c.getBoard().getId());
			assertEquals("name", "Moo de Doo", c.getDisplayName());
			assertEquals("order", 3, c.getOrder());
			assertEquals("created by", "ut_spork2", c.getCreatedBy().getNickname());

			mMgr.removeCategory(c);
			c = mMgr.getCategoryById(id);
			assertNull(c);
		}

	public void testSaveCategory()
		{
			User u = mUserMgr.getUserById(-3);
			Board b = mBoardMgr.getBoardById(-1);

			Category c = new Category(b, 4, "Grumble Gus", u);
			
			mMgr.saveCategory(c);

			int id = c.getId();
			c = mMgr.getCategoryById(c.getId());

			assertEquals("id", id, c.getId());
			assertEquals("board id", -1, c.getBoard().getId());
			assertEquals("name", "Grumble Gus", c.getDisplayName());
			assertEquals("order", 4, c.getOrder());
			assertEquals("created by", "ut_spork3", c.getCreatedBy().getNickname());

			c.setDisplayName("Flink");
			
			mMgr.saveCategory(c);

			c = mMgr.getCategoryById(c.getId());

			assertEquals("id", id, c.getId());
			assertEquals("board id", -1, c.getBoard().getId());
			assertEquals("name", "Flink", c.getDisplayName());
			assertEquals("order", 4, c.getOrder());
			assertEquals("created by", "ut_spork3", c.getCreatedBy().getNickname());

			mMgr.removeCategory(c);
			c = mMgr.getCategoryById(id);
			assertNull(c);
		}

}
