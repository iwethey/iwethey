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
 * Tests the board database manager.
 * <p>
 * $Id: HibBoardManagerTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibBoardManagerTest extends HibernateTestHarness
{
    private HibBoardManager mMgr;

    public void setUp()
		throws Exception
		{
			super.setUp();
			mMgr = (HibBoardManager) context.getBean("boardManager");
		}

	public void testGetAllBoards()
		{
			List boards = mMgr.getBoards();
			
			assertTrue(boards.size() >= 2);

			Board b = (Board) boards.get(0);
			assertEquals("id", -2, b.getId());
			assertEquals("name", "Blancmange", b.getDisplayName());
			assertEquals("desc", "Board de la Blancmange", b.getDescription());
			assertNull("image", b.getImage());
			assertEquals("created by", "ut_spork2", b.getCreatedBy().getNickname());

			b = (Board) boards.get(1);
			assertEquals("id", -1, b.getId());
			assertEquals("name", "Spork", b.getDisplayName());
			assertEquals("desc", "Board de la Spork", b.getDescription());
			assertEquals("image", "spork.png", b.getImage());
			assertEquals("created by", "ut_spork1", b.getCreatedBy().getNickname());
		}

	public void testGetBoard()
		{
			Board b = mMgr.getBoardById(-1);
			
			assertEquals("id", -1, b.getId());
			assertEquals("name", "Spork", b.getDisplayName());
			assertEquals("desc", "Board de la Spork", b.getDescription());
			assertEquals("image", "spork.png", b.getImage());
			assertEquals("created by", "ut_spork1", b.getCreatedBy().getNickname());
		}

	public void testCreateBoard()
		{
			User u = new User();
			u.setId(-2);
			u.setNickname("ut_spork2");
			Board b = new Board("Boardo", "Boardo Board, Bud", "boardo.png", u);
			
			int id = mMgr.createBoard(b);

			b = mMgr.getBoardById(id);

			assertEquals("id", id, b.getId());
			assertEquals("name", "Boardo", b.getDisplayName());
			assertEquals("desc", "Boardo Board, Bud", b.getDescription());
			assertEquals("image", "boardo.png", b.getImage());
			assertEquals("created by", "ut_spork2", b.getCreatedBy().getNickname());

			mMgr.removeBoard(b);
			b = mMgr.getBoardById(id);
			assertNull(b);
		}

	public void testSaveBoard()
		{
			User u = new User();
			u.setId(-3);
			u.setNickname("ut_spork3");
			Board b = new Board("Boardo 2", "Boardo Board 2, Bud", "boardo2.png", u);
			
			mMgr.saveBoard(b);

			int id = b.getId();
			b = mMgr.getBoardById(b.getId());

			assertEquals("id", id, b.getId());
			assertEquals("name", "Boardo 2", b.getDisplayName());
			assertEquals("desc", "Boardo Board 2, Bud", b.getDescription());
			assertEquals("image", "boardo2.png", b.getImage());
			assertEquals("created by", "ut_spork3", b.getCreatedBy().getNickname());

			b.setDisplayName("Boardo 3");
			
			mMgr.saveBoard(b);

			b = mMgr.getBoardById(b.getId());

			assertEquals("id", id, b.getId());
			assertEquals("name", "Boardo 3", b.getDisplayName());
			assertEquals("desc", "Boardo Board 2, Bud", b.getDescription());
			assertEquals("image", "boardo2.png", b.getImage());
			assertEquals("created by", "ut_spork3", b.getCreatedBy().getNickname());

			mMgr.removeBoard(b);
			b = mMgr.getBoardById(id);
			assertNull(b);
		}

}
