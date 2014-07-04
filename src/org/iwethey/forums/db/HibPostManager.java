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

package org.iwethey.forums.db;

import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.PostManager;
import org.iwethey.forums.domain.User;

import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the database interface for post functions.
 * <p>
 * $Id: HibPostManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibPostManager extends HibernateSupportExtensions implements PostManager
{
    /** Logger for this class and subclasses */
    private final Log logger = LogFactory.getLog(getClass());

	/**
	 * Retrieve a post by ID.
	 * <p>
	 * @param post The post to retrieve.
	 */
    public Post getPostById(int post)
	{
		try
		{
			return (Post) getHibernateTemplate().get(Post.class, new Integer(post));
		}
		catch (HibernateObjectRetrievalFailureException e)
		{
			return null;
		}
	}

	/**
	 * <p>Retrieve all posts by a User ID.</p>
	 * 
	 * @param userId The User ID who created all the Posts to retrieve.
	 */
	public List<Post> getPostsByUser(User user)
	{
		return (List<Post>) getHibernateTemplate().find("from Post where createdBy = ? order by created desc", user);
	}

	/**
	 * Retrieve the children (replies) of a post. Sets them on the
	 * Sets them on the post itself as well. 
	 * <p>
	 * @param post The post to retrieve the replies for.
	 */
	public List getChildren(Post post)
	{
		Post thread = post.getThread();
		Integer threadId = null;
		if (thread == null)
		{
			threadId = new Integer(post.getId());
		}
		else
		{
			threadId = new Integer(thread.getId());
		}

		Integer leftId = new Integer(post.getLeftId());
		Integer rightId = new Integer(post.getRightId());

		post.setChildren(getHibernateTemplate().find(
							 "from Post post where post.thread = ? and post.leftId > ? and post.leftId < ? order by post_left_id",
							 new Object[] { threadId, leftId, rightId }
							 ));

		return post.getChildren();
	}

	/**
	 * Save a post. If the post does not exist, it is created first.
	 * <p>
	 * @param post The post to save.
	 * @return The ID of the new post.
	 */
    public void savePost(Post post)
	{
		getHibernateTemplate().saveOrUpdate(post);
	}

	/**
	 * Create a new post.
	 * <p>
	 * @param post The post to save.
	 */
    public int createPost(Post post)
	{
		getHibernateTemplate().save(post);
		return post.getId();
	}

	/**
	 * Remove a post.
	 * <p>
	 * @param post The post to remove.
	 */
    public void removePost(Post post)
	{
		getHibernateTemplate().delete(post);
	}
}
