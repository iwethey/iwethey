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
 * Tests the Post class.
 * <p>
 * $Id: PostTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class PostTest extends TestCase
{
	public void testCreateFrom()
		{
			User u = new User();
			u.setId(-1);
			u.setNickname("ut_spork1");

			Forum f = new Forum();
			f.setId(-1);

			Post p = new Post(f, null, null, "subject", "sporks", "siggy spork", u, true, false, true, false);
			Post pc = new Post(f, p, p, "subject", "sporks", "siggy spork", u, true, false, true, false);

			Post mp = new Post(pc);
			assertEquals("forum", -1, mp.getForum().getId());
			assertEquals("parent", p, mp.getParent());
			assertEquals("thread", p, mp.getThread());
			assertEquals("level", 2, mp.getLevel());
			assertEquals("subject", "subject", mp.getSubject());
			assertEquals("content", "sporks", mp.getOriginalContent());
			assertEquals("sig", "siggy spork", mp.getOriginalSignature());
			assertTrue("newlines", mp.isConvertNewlines());
			assertFalse("links", mp.isConvertLinks());
			assertTrue("codes", mp.isConvertCodes());
			assertFalse("html", mp.isConvertHtml());
			assertEquals("created by", u.getNickname(), mp.getCreatedBy().getNickname());
			assertEquals("created", pc.getCreated(), mp.getCreated());
		}
}
