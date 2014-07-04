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
 * <p>Represents a Board. Used for the main page.</p>
 *
 * <p>$Id:$</p>
 *
 * <p>@author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)</p>
 */
public class BoardSummary implements Serializable
{
	/** The unique ID of this board. */
	private int id = 0;

	/** The display name of this board. */
	private String displayName = null;

	/** The long description of this board. */
	private String description = null;

	/** The image location for this board. */
	private String image = null;

	/** The number of posts this board contains. */
	private int postCount = 0;

	/** The timestamp of the last post to this board. */
	private Date lastUpdated = null;

	/** Blank constructor for bean use. */
	public BoardSummary() { }

	public void setId(int id) { this.id = id; }
	public int getId() { return id; }

	public void setDisplayName(String name) { this.displayName = name; }
	public String getDisplayName() { return displayName; }

	public void setDescription(String desc) { this.description = desc; }
	public String getDescription() { return description; }

	public void setImage(String image) { this.image = image; }
	public String getImage() { return image; }

	public void setPostCount(int postCount) { this.postCount = postCount; }
	public int getPostCount() { return postCount; }

	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
	public Date getLastUpdated() { return lastUpdated; }
}
