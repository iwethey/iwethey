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
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

/**
 * Represents a Post's edit history. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: PostHistory.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class PostHistory implements Serializable
{
	/** The post this history belongs to. */
	private Post mParent = null;

	/** The subject of the post. */
	private String mSubject = null;

	/** The filtered content of the post. */
	private String mContent = null;

	/** The filtered signature of the post. */
	private String mSignature = null;

	/** The user that created this post. */
	private User mCreatedBy = null;

	/** The creation time/date of this post. */
	private Date mCreated = null;

	/** Blank constructor for bean use. */
	public PostHistory() { }

	/**
	 * Create this post history from a post, copying all fields.
	 * <p>
	 * @param post The post to copy from.
	 */
	public PostHistory(Post post, User editor)
		{
			setFromPost(post, editor);
		}

	/** 
	 * Set all of the properties and members of this post history to those
	 * of the given post.
	 * <p>
	 * @param post The post to copy values from.
	 */
	public void setFromPost(Post post, User editor)
		{
			assert post != null;
			setParent(post);
			setSubject(post.getSubject());
			setContent(post.getContent());
			setSignature(post.getSignature());
			setCreatedBy(editor);
			setCreated(new Date());
		}

	public Post getParent() { return mParent; }
	public void setParent(Post parent){ mParent = parent; }

	public String getSubject() { return mSubject; }
	public void setSubject(String subject){ mSubject = subject; }

	public String getContent() { return mContent; }
	public void setContent(String content){ mContent = content; }

	public String getSignature() { return mSignature; }
	public void setSignature(String signature){ mSignature = signature; }

	public void setCreatedBy(User createdBy) { mCreatedBy = createdBy; }
	public User getCreatedBy() { return mCreatedBy; }

	public void setCreated(Date created) { mCreated = created; }
	public Date getCreated() { return mCreated; }

	public String toString()
		{
			return
				"\t\tmParent              = " + (mParent == null ? "null" : ("" + mParent.getId())) + "\n" +
				"\t\tmSubject             = " + mSubject + "\n" +
				"\t\tmContent             = " + mContent + "\n" +
				"\t\tmSignature           = " + mSignature + "\n" +
				"\t\tmCreatedBy           = " + (mCreatedBy == null ? "null" : ("" + mCreatedBy.getId())) + "\n" +
				"\t\tmCreated             = " + mCreated + "\n";
		}
} 
