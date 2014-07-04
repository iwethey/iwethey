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
import java.util.Set;

/**
 * Represents a Category. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: Category.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class Category implements Serializable, Cloneable
{
	/** The unique ID of this category. */
	private int mId = 0;

	/** The board containing this category. */
	private Board mBoard = null;

	/** The order position of this category. */
	private int mOrder = -1;

	/** The display name of this category. */
	private String mDisplayName = null;

	/** The user that created this category. */
	private User mCreatedBy = null;

	/** The creation time/date of this category. */
	private Date mCreated = null;

	/** The forums that belong to this category. */
	private Set mForums = null;

	/** Blank constructor for bean use. */
	public Category() { }

	/**
	 * Create this category from another category, copying all fields.
	 * <p>
	 * @param category The category to copy from.
	 */
	public Category(Category category)
	{
		setFromCategory(category);
	}

	/**
	 * Create this category, with the given name, description, image, and creator.
	 * <p>
	 * @param board The Board this category belongs to.
	 * @param order The category's display order.
	 * @param name The category's display name.
	 * @param createdBy The user that created this category.
	 */
	public Category(Board board, int order, String name, User createdBy)
	{
		mBoard = board;
		mOrder = order;
		mDisplayName = name;
		mCreatedBy = createdBy;
		mCreated = new Date();
	}

	/** 
	 * Set all of the properties and members of this category to those
	 * of the given category.
	 * <p>
	 * @param category The category to copy values from.
	 */
	public void setFromCategory(Category category)
	{
		assert category != null;
		setBoard(category.getBoard());
		setOrder(category.getOrder());
		setDisplayName(category.getDisplayName());
		setCreatedBy(category.getCreatedBy());
		setCreated(category.getCreated());
	}

	@Override
	public Object clone()
	{
		return new Category(this);
	}

	public void setId(int id) { mId = id; }
	public int getId() { return mId; }

	public void setBoard(Board board) { mBoard = board; }
	public Board getBoard() { return mBoard; }

	public void setOrder(int order) { mOrder = order; }
	public int getOrder() { return mOrder; }

	public void setDisplayName(String name) { mDisplayName = name; }
	public String getDisplayName() { return mDisplayName; }

	public void setCreatedBy(User createdBy) { mCreatedBy = createdBy; }
	public User getCreatedBy() { return mCreatedBy; }

	public void setCreated(Date created) { mCreated = created; }
	public Date getCreated() { return mCreated; }

	public void setForums(Set forums) { mForums = forums; }
	public Set getForums() { return mForums; }
}
