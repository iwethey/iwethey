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
 * Tests the Board class.
 * <p>
 * $Id: BoardTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class BoardTest extends TestCase
{
	public void testCreateFrom()
		{
			User u = new User();
			u.setNickname("ut_spork1");
			Board b = new Board("board", "spork", "spork.png", u);;

			Board mb = new Board(b);
			assertEquals("displayName", "board", mb.getDisplayName());
			assertEquals("description", "spork", mb.getDescription());
			assertEquals("image", "spork.png", mb.getImage());
			assertEquals("created by", u.getNickname(), mb.getCreatedBy().getNickname());
		}
}
