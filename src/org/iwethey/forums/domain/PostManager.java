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

package org.iwethey.forums.domain;

import java.util.List;

/**
 * Implements the database interface for post functions.
 * <p>
 * $Id: PostManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface PostManager
{
	/**
	 * Retrieve a post by ID.
	 * <p>
	 * @param post The post to retrieve.
	 */
    public Post getPostById(int post);

	/**
	 * <p>Retrieve all posts by a User ID.</p>
	 * 
	 * @param user The User who created all the Posts to retrieve.
	 */
	public List<Post> getPostsByUser(User user);
	
	/**
	 * Retrieve the children (replies) of a post. Sets them on the
	 * Sets them on the post itself as well. 
	 * <p>
	 * @param post The post to retrieve the replies for.
	 */
	public List getChildren(Post post);

	/**
	 * Save a post. If the post does not exist, it is created first.
	 * <p>
	 * @param post The post to save.
	 * @return The ID of the new post.
	 */
    public void savePost(Post post);

	/**
	 * Create a new post.
	 * <p>
	 * @param post The post to save.
	 */
    public int createPost(Post post);

	/**
	 * Remove a post.
	 * <p>
	 * @param post The post to remove.
	 */
    public void removePost(Post post);
}
