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

import junit.framework.TestCase;

import org.iwethey.forums.domain.Board;
import org.iwethey.forums.domain.Category;
import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.User;

/**
 * Tests the Forum class.
 * <p>
 * $Id: ForumTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ForumTest extends TestCase
{
	public void testCreateFrom()
		{
			User u = new User();
			u.setId(-1);
			u.setNickname("ut_spork1");

			Board b = new Board();
			b.setId(-1);

			Category c = new Category();
			c.setId(-1);
			c.setBoard(b);

			Forum f = new Forum(c, 42, "forum", "The Forum", "", u);;

			Forum mf = new Forum(f);
			assertEquals("board", -1, mf.getBoard().getId());
			assertEquals("category", -1, mf.getCategory().getId());
			assertEquals("order", 42, mf.getOrder());
			assertEquals("displayName", "forum", mf.getDisplayName());
			assertEquals("description", "The Forum", mf.getDescription());
			assertEquals("created by", u.getNickname(), mf.getCreatedBy().getNickname());
		}
}
