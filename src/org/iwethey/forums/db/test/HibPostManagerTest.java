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
 * Tests the post database manager.
 * <p>
 * $Id: HibPostManagerTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibPostManagerTest extends HibernateTestHarness
{
    private HibPostManager mMgr;
    private UserManager mUserMgr;
    private ForumManager mForumMgr;

    public void setUp() throws Exception
	{
		super.setUp();
		mMgr = (HibPostManager) context.getBean("postManager");
		mUserMgr = (UserManager) context.getBean("userManager");
		mForumMgr = (ForumManager) context.getBean("forumManager");
	}

	public void testGetPost()
	{
		Post p = mMgr.getPostById(-1);

		assertEquals("id", -1, p.getId());
		assertEquals("forum id", -2, p.getForum().getId());
		assertEquals("parent", null, p.getParent());
		assertEquals("thread", p, p.getThread());
		assertEquals("lept", 1, p.getLeftId());
		assertEquals("right", 14, p.getRightId());
		assertEquals("level", 1, p.getLevel());
		assertEquals("replies", 6, p.getReplyCount());
		assertEquals("subject", "Spork Subject 1", p.getSubject());
		assertEquals("content", "This is a post about sporks.", p.getContent());
		assertEquals("contentorig", "This is a post about sporks.", p.getOriginalContent());
		assertEquals("signature", "Spork Sig", p.getSignature());
		assertEquals("signatureorig", "Spork Sig", p.getOriginalSignature());
		assertTrue("locked", !p.isLocked());
		assertTrue("newlines", p.isConvertNewlines());
		assertTrue("links", p.isConvertLinks());
		assertTrue("codes", p.isConvertCodes());
		assertTrue("html", p.isConvertHtml());
		assertEquals("created by", "ut_spork1", p.getCreatedBy().getNickname());

		assertEquals(6, mMgr.getChildren(p).size());
		assertEquals(6, p.getChildren().size());

		Post child = (Post) mMgr.getChildren(p).get(0);
		assertEquals(3, mMgr.getChildren(child).size());
	}

	public void testCreatePost()
	{
		User u = mUserMgr.getUserById(-2);
		Forum f = mForumMgr.getForumById(-2);

		Post p = new Post(f, null, null, "Moo", "Moo de Doo", "sig", u, true, true, true, true);
		p.setContent("Moo de Doo");
		p.setSignature("sig");

		int id = mMgr.createPost(p);

		p = mMgr.getPostById(id);

		assertEquals("forum id", -2, p.getForum().getId());
		assertEquals("subject", "Moo", p.getSubject());
		assertEquals("contents orig", "Moo de Doo", p.getOriginalContent());
		assertEquals("sig orig", "sig", p.getOriginalSignature());
		assertEquals("contents", "Moo de Doo", p.getContent());
		assertEquals("sig", "sig", p.getSignature());
		assertEquals("created by", "ut_spork2", p.getCreatedBy().getNickname());
		assertTrue(p.isConvertNewlines());
		assertTrue(p.isConvertLinks());
		assertTrue(p.isConvertCodes());
		assertTrue(p.isConvertHtml());

		mMgr.removePost(p);
		p = mMgr.getPostById(id);
		assertNull(p);
	}
	
	public void testGetPostsByUser()
	{
		User u = mUserMgr.getUserById(-2);
		List<Post> posts = mMgr.getPostsByUser(u);
		
		assertEquals(16, posts.size());
	}
}
