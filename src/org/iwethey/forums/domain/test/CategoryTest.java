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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/** 
 * Tests the Category class.
 * <p>
 * $Id: CategoryTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class CategoryTest extends TestCase
{
	public void testCreateFrom()
		{
			User u = new User();
			u.setId(-1);
			u.setNickname("ut_spork1");

			Board b = new Board();
			b.setId(-1);

			Category c = new Category(b, 42, "category", u);;

			Category mc = new Category(c);
			assertEquals("board", -1, mc.getBoard().getId());
			assertEquals("order", 42, mc.getOrder());
			assertEquals("displayName", "category", mc.getDisplayName());
			assertEquals("created by", u.getNickname(), mc.getCreatedBy().getNickname());
		}
}
