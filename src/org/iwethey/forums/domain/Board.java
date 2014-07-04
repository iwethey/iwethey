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
 * Represents a Board. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: Board.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class Board implements Serializable, Cloneable
{
	/** The unique ID of this board. */
	private int mId = 0;

	/** The display name of this board. */
	private String mDisplayName = null;

	/** The long description of this board. */
	private String mDescription = null;

	/** The image location for this board. */
	private String mImage = null;

	/** The user that created this board. */
	private User mCreatedBy = null;

	/** The creation time/date of this board. */
	private Date mCreated = null;

	/** The categories for this board, sorted by category_order. */
	private Set mCategories = null;

	/** Blank constructor for bean use. */
	public Board() { }

	/**
	 * Create this board from another board, copying all fields.
	 * <p>
	 * @param board The board to copy from.
	 */
	public Board(Board board)
	{
		setFromBoard(board);
	}

	/**
	 * Create this board, using the given name, description, image, and creator.
	 * <p>
	 * @param name The board's display name.
	 * @param desc The board's description.
	 * @param image The board's image, if any.
	 * @param createdBy The user that created this board.
	 */
	public Board(String name, String desc, String image, User createdBy)
	{
		mDisplayName = name;
		mDescription = desc;
		mImage = image;
		mCreatedBy = createdBy;
		mCreated = new Date();
	}

	/** 
	 * Set all of the properties and members of this board to those
	 * of the given board.
	 * <p>
	 * @param board The board to copy values from.
	 */
	public void setFromBoard(Board board)
	{
		assert board != null;
		setDisplayName(board.getDisplayName());
		setDescription(board.getDescription());
		setImage(board.getImage());
		setCreatedBy(board.getCreatedBy());
		setCreated(board.getCreated());
	}

	@Override
	public Object clone()
	{
		return new Board(this);
	}

	public void setId(int id) { mId = id; }
	public int getId() { return mId; }

	public void setDisplayName(String name) { mDisplayName = name; }
	public String getDisplayName() { return mDisplayName; }

	public void setDescription(String desc) { mDescription = desc; }
	public String getDescription() { return mDescription; }

	public void setImage(String image) { mImage = image; }
	public String getImage() { return mImage; }

	public void setCreatedBy(User createdBy) { mCreatedBy = createdBy; }
	public User getCreatedBy() { return mCreatedBy; }

	public void setCreated(Date created) { mCreated = created; }
	public Date getCreated() { return mCreated; }

	public void setCategories(Set cats) { mCategories = cats; }
	public Set getCategories() { return mCategories; }
}
