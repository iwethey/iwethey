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

import java.util.Date;
import java.util.List;

/**
 * Describes the database interface for forum functions.
 * <p>
 * $Id: ForumManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface ForumManager
{
	/** Retrieve a forum by ID.
	 * <p>
	 * @param forum The forum to retrieve.
	 */
    public Forum getForumById(int forum);
	
	/**
	 * Get a page of threads for a forum.
	 * <p>
	 * @param forum The forum to retrieve.
	 * @param start The thread index to start with.
	 * @param count The number of threads on the page.
	 * @param sortByLastModified If false, sorted by creation date, otherwise by last modified date.
	 * @param after The data after which threads should be considered new. If null, show all threads.
	 */
	public List getThreadPage(Forum forum, int start, int count, boolean sortByLastModified, Date after);

	/**
	 * Get a count of threads for a forum.
	 * <p>
	 * @param forum The forum to retrieve.
	 * @param after The data after which threads should be considered new. If null, show all threads.
	 */
	public Integer getThreadCount(Forum forum, Date after);

	/** Save a forum. If the forum does not exist, it is created first.
	 * <p>
	 * @param forum The forum to save.
	 */
    public void saveForum(Forum forum);

	/** Create a new forum.
	 * <p>
	 * @param forum The forum to save.
	 * @return The forum ID of the new forum.
	 */
    public int createForum(Forum forum);

	/** Remove a forum by ID.
	 * <p>
	 * @param forum The forum to remove.
	 */
    public void removeForum(Forum forum);
}
