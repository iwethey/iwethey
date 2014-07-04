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

import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.ForumManager;

import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Implements the database interface for forum functions.
 * <p>
 * $Id: HibForumManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibForumManager extends HibernateSupportExtensions implements ForumManager
{
    /** Logger for this class and subclasses */
    private final Logger logger = Logger.getLogger(HibForumManager.class);

	/** Retrieve all categories.
	 * <p>
	 */
    public List getForums()
		{
			return getHibernateTemplate().loadAll(Forum.class);
		}

	/** Retrieve a forum by ID.
	 * <p>
	 * @param forum The forum to retrieve.
	 */
    public Forum getForumById(int forum)
		{
			try
				{
					return (Forum) getHibernateTemplate().get(Forum.class, new Integer(forum));
				}
			catch (HibernateObjectRetrievalFailureException e)
				{
					return null;
				}
		}

	/**
	 * Get a page of threads for a forum.
	 * <p>
	 * @param forum The forum to retrieve.
	 * @param start The thread index to start with.
	 * @param count The number of threads on the page.
	 * @param sortByLastModified If true, sorted by creation date, otherwise by last modified date.
	 * @param after The data after which threads should be considered new. If null, show all threads.
	 */
	public List getThreadPage(Forum forum, int start, int count, boolean sortByLastModified, Date after)
		{
			if (after == null)
				{
					return findByPagedQuery((sortByLastModified ? "org.iwethey.threads" : "org.iwethey.threads.by.updated"),
											start, count, "forum", forum);
				}
			else
				{
					return findByPagedQuery((sortByLastModified ? "org.iwethey.new.threads" : "org.iwethey.new.threads.by.updated"),
											start, count,
											new String[] { "forum", "mark"},
											new Object[] { forum, after} );
					/* I have no idea what the hell I thought this was accomplishing.
					return findByPagedQuery((sortByLastModified ? "org.iwethey.new.threads" : "org.iwethey.new.threads.by.updated"),
											start, count,
											new String[] { "forum", "mark", "lastUpdated" },
											new Object[] { forum, after, forum.getLastUpdated() } );
					*/
				}
		}

	/**
	 * Get a count of threads for a forum.
	 * <p>
	 * @param forum The forum to retrieve.
	 * @param after The data after which threads should be considered new. If null, show all threads.
	 */
	public Integer getThreadCount(Forum forum, Date after)
		{
			if (after == null)
				{
					return forum.getThreadCount();
				}
			else
				{
					return single("org.iwethey.new.threads.count",
								  new String[] { "forum", "mark" },
								  new Object[] { forum, after } );
					/* I have no idea what the hell I thought this was accomplishing.
					return single("org.iwethey.new.threads.count",
								  new String[] { "forum", "mark", "lastUpdated" },
								  new Object[] { forum, after, forum.getLastUpdated() } );
					*/
				}
		}

	/** Save a forum. If the forum does not exist, it is created first.
	 * <p>
	 * @param forum The forum to save.
	 * @return The ID of the new forum.
	 */
    public void saveForum(Forum forum)
		{
			getHibernateTemplate().saveOrUpdate(forum);
		}

	/** Create a new forum.
	 * <p>
	 * @param forum The forum to save.
	 */
    public int createForum(Forum forum)
		{
			getHibernateTemplate().save(forum);
			return forum.getId();
		}

	/** Remove a forum.
	 * <p>
	 * @param forum The forum to remove.
	 */
    public void removeForum(Forum forum)
		{
			getHibernateTemplate().delete(forum);
		}
}
