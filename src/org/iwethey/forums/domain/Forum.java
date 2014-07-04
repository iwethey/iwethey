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

import java.io.Serializable;

import java.util.Date;

/**
 * Represents a Forum. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: Forum.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class Forum implements Serializable, Cloneable
{
	/** The unique ID of this forum. */
	private int id = 0;

	/** The board containing this forum. */
	private Board board = null;

	/** The category containing this forum. */
	private Category category = null;

	/** The order position of this forum. */
	private int order = -1;

	/** The number of posts in this forum. */
	private int postCount = 0;

	/** The number of threads in this forum. */
	private int threadCount = 0;

	/** The last update time/date of this forum. */
	private Date lastUpdated = null;

	/** The display name of this forum. */
	private String displayName = null;

	/** The description of this forum. */
	private String description = null;

	/** The header image. */
	private String headerImage = null;

	/** The user that created this forum. */
	private User createdBy = null;

	/** The creation time/date of this forum. */
	private Date created = null;

	/** Blank constructor for bean use. */
	public Forum() { }

	/**
	 * Create this forum from another forum, copying all fields.
	 * <p>
	 * @param forum The forum to copy from.
	 */
	public Forum(Forum forum)
	{
		setFromForum(forum);
	}

	/**
	 * Create this forum, with the given category, order, name, description, and user.
	 * <p>
	 * @param category The forum's category.
	 * @param order The forum's display order.
	 * @param name The forum's display name.
	 * @param desc The forum's description.
	 * @param image The forum's header image.
	 * @param createdBy The user that created this forum.
	 */
	public Forum(Category category, int order, String name, String desc, String image, User createdBy)
	{
		this.board = category.getBoard();
		this.category = category;
		this.order = order;
		this.displayName = name;
		this.description = desc;
		this.headerImage = image;
		this.createdBy = createdBy;
		this.created = new Date();
	}

	/** 
	 * Set all of the properties and members of this forum to those
	 * of the given forum.
	 * <p>
	 * @param forum The forum to copy values from.
	 */
	public void setFromForum(Forum forum)
	{
		assert forum != null;
		setBoard(forum.getBoard());
		setCategory(forum.getCategory());
		setOrder(forum.getOrder());
		setPostCount(forum.getPostCount());
		setThreadCount(forum.getThreadCount());
		setLastUpdated(forum.getLastUpdated());
		setDisplayName(forum.getDisplayName());
		setDescription(forum.getDescription());
		setHeaderImage(forum.getHeaderImage());
		setCreatedBy(forum.getCreatedBy());
		setCreated(forum.getCreated());
	}

	@Override
	public Object clone()
	{
		return new Forum(this);
	}

	public boolean isNew(Date when)
	{
		if (when == null || this.lastUpdated == null)
		{
			return true;
		}

		return this.lastUpdated.compareTo(when) > 0;
	}

	public boolean isNew(User user)
	{
		return isNew(user.getForumMark(this.id));
	}

	public void setId(int id) { this.id = id; }
	public int getId() { return this.id; }

	public void setBoard(Board board) { this.board = board; }
	public Board getBoard() { return this.board; }

	public void setCategory(Category category) { this.category = category; }
	public Category getCategory() { return this.category; }

	public void setOrder(int order) { this.order = order; }
	public int getOrder() { return this.order; }

	public void setPostCount(int postCount) { this.postCount = postCount; }
	public int getPostCount() { return this.postCount; }

	public void setThreadCount(int threadCount) { this.threadCount = threadCount; }
	public int getThreadCount() { return this.threadCount; }

	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
	public Date getLastUpdated() { return this.lastUpdated; }

	public void setDisplayName(String name) { this.displayName = name; }
	public String getDisplayName() { return this.displayName; }

	public void setDescription(String name) { this.description = name; }
	public String getDescription() { return this.description; }

	public void setHeaderImage(String image) { this.headerImage = image; }
	public String getHeaderImage() { return this.headerImage; }

	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	public User getCreatedBy() { return this.createdBy; }
	public void setCreated(Date created) { this.created = created; }
	public Date getCreated() { return this.created; }

}
