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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a Post. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: Post.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class Post implements Serializable, Cloneable
{
    protected final Log logger = LogFactory.getLog(getClass());

	/** The unique ID of this post. */
	private int mId = 0;

	/** The forum containing this post. */
	private Forum mForum = null;

	/** The parent post. */
	private Post mParent = null;

	/** The thread head post. */
	private Post mThread = null;

	/** The left ID of this post. Used for the nested set tree. */
	private int mLeftId = 0;

	/** The right ID of this post. Used for the nested set tree. */
	private int mRightId = 0;

	private int mReplyCount = 0;

	/** The level of this post in the hierarchy. */
	private int mLevel = 1;

	/** The subject of the post. */
	private String mSubject = null;

	/** The filtered content of the post. */
	private String mContent = null;

	/** The original content of the post. */
	private String mOriginalContent = null;

	/** The filtered signature of the post. */
	private String mSignature = null;

	/** The original signature of the post. */
	private String mOriginalSignature = null;

	/** The lock status of the post. */
	private boolean mLocked = false;

	/** Whether convert newlines should be used for this post. */
	private boolean mConvertNewlines = true;

	/** Whether convert links should be used for this post. */
	private boolean mConvertLinks = true;

	/** Whether convert codes should be used for this post. */
	private boolean mConvertCodes = true;

	/** Whether convert html should be used for this post. */
	private boolean mConvertHtml = true;

	/** The user that created this post. */
	private User mCreatedBy = null;

	/** The creation time/date of this post. */
	private Date mCreated = null;

	/** The last update time/date of this post. */
	private Date mLastUpdated = null;

	/** The last update time/date of this post's children. */
	private Date mChildrenLastUpdated = null;

	private List mChildren = null;

	private List mEditHistory = null;

	/** Blank constructor for bean use. */
	public Post() { }

	/**
	 * Create this post from another post, copying all fields.
	 * <p>
	 * @param post The post to copy from.
	 */
	public Post(Post post)
	{
		setFromPost(post);
	}

	/**
	 * Create this post, with the given category, order, name, description, and user.
	 * <p>
	 * @param forum The post's forum.
	 * @param parent The post's parent post, if any.
	 * @param thread The post's thread head post, if any.
	 * @param subject The post's subject.
	 * @param content The post's content.
	 * @param sig The post's signature.
	 * @param createdBy The user that created this post.
	 * @param newlines Whether newlines should be converted for this post.
	 * @param links Whether links should be converted for this post.
	 * @param codes Whether wee codes should be converted for this post.
	 * @param html Whether HTML should be converted for this post.
	 */
	public Post(Forum forum, Post parent, Post thread, String subject, String content, String sig, User createdBy,
				boolean newlines, boolean links, boolean codes, boolean html)
	{
		mForum = forum;
		mParent = parent;
		mThread = thread;
		if (parent != null)
		{
			mLevel = parent.getLevel() + 1;
		}
		mSubject = subject;
		mOriginalContent = content;
		mOriginalSignature = sig;
		mConvertNewlines = newlines;
		mConvertLinks = links;
		mConvertCodes = codes;
		mConvertHtml = html;
		mCreatedBy = createdBy;
		mCreated = new Date();
	}

	/** 
	 * Set all of the properties and members of this post to those
	 * of the given post.
	 * <p>
	 * @param post The post to copy values from.
	 */
	public void setFromPost(Post post)
	{
		assert post != null;
		setId(post.getId());
		setForum(post.getForum());
		setParent(post.getParent());
		setThread(post.getThread());
		setLeftId(post.getLeftId());
		setRightId(post.getRightId());
		setLevel(post.getLevel());
		setSubject(post.getSubject());
		setContent(post.getContent());
		setOriginalContent(post.getOriginalContent());
		setSignature(post.getSignature());
		setOriginalSignature(post.getOriginalSignature());
		setLocked(post.isLocked());
		setConvertNewlines(post.isConvertNewlines());
		setConvertLinks(post.isConvertLinks());
		setConvertCodes(post.isConvertCodes());
		setConvertHtml(post.isConvertHtml());
		setCreatedBy(post.getCreatedBy());
		setCreated(post.getCreated());
		setLastUpdated(post.getLastUpdated());
		setChildrenLastUpdated(post.getChildrenLastUpdated());
	}

	/** 
	 * Set all of the properties and members of this post to the
	 * edited properties of the given post.
	 * <p>
	 * @param post The post to copy values from.
	 */
	public void editFromPost(Post post, User editor)
	{
		assert post != null;

		PostHistory hist = new PostHistory(this, editor);

		setSubject(post.getSubject());
		setContent(post.getContent());
		setOriginalContent(post.getOriginalContent());
		setSignature(post.getSignature());
		setOriginalSignature(post.getOriginalSignature());
		setConvertNewlines(post.isConvertNewlines());
		setConvertLinks(post.isConvertLinks());
		setConvertCodes(post.isConvertCodes());
		setConvertHtml(post.isConvertHtml());

		addEditHistory(hist);
	}

	@Override
	public Object clone()
	{
		return new Post(this);
	}

	public boolean isNew(Date when)
	{
		return mLastUpdated.compareTo(when) > 0;
	}

	public void setId(int id) { mId = id; }
	public int getId() { return mId; }

	public Forum getForum() { return mForum; }
	public void setForum(Forum forum){ mForum = forum; }

	public Post getParent() { return mParent; }
	public void setParent(Post parent){ mParent = parent; }

	public Post getThread() { return mThread; }
	public void setThread(Post thread){ mThread = thread; }

	public int getLeftId() { return mLeftId; }
	public void setLeftId(int leftId){ mLeftId = leftId; }

	public int getRightId() { return mRightId; }
	public void setRightId(int rightId){ mRightId = rightId; }

	public int getReplyCount() { return mReplyCount; }
	public void setReplyCount(int replyCount){ mReplyCount = replyCount; }

	public int getLevel() { return mLevel; }
	public void setLevel(int level){ mLevel = level; }

	public String getSubject() { return mSubject; }
	public void setSubject(String subject){ mSubject = subject; }

	public String getContent() { return mContent; }
	public void setContent(String content){ mContent = content; }

	public String getOriginalContent() { return mOriginalContent; }
	public void setOriginalContent(String originalContent){ mOriginalContent = originalContent; }

	public String getSignature() { return mSignature; }
	public void setSignature(String signature){ mSignature = signature; }

	public String getOriginalSignature() { return mOriginalSignature; }
	public void setOriginalSignature(String originalSignature){ mOriginalSignature = originalSignature; }

	public boolean isLocked() { return mLocked; }
	public void setLocked(boolean locked){ mLocked = locked; }

	public boolean isConvertNewlines() { return mConvertNewlines; }
	public void setConvertNewlines(boolean convertNewlines){ mConvertNewlines = convertNewlines; }

	public boolean isConvertLinks() { return mConvertLinks; }
	public void setConvertLinks(boolean convertLinks){ mConvertLinks = convertLinks; }

	public boolean isConvertCodes() { return mConvertCodes; }
	public void setConvertCodes(boolean convertCodes){ mConvertCodes = convertCodes; }

	public boolean isConvertHtml() { return mConvertHtml; }
	public void setConvertHtml(boolean convertHtml){ mConvertHtml = convertHtml; }

	public void setCreatedBy(User createdBy) { mCreatedBy = createdBy; }
	public User getCreatedBy() { return mCreatedBy; }

	public void setCreated(Date created) { mCreated = created; }
	public Date getCreated() { return mCreated; }

	public void setLastUpdated(Date lastUpdated) { mLastUpdated = lastUpdated; }
	public Date getLastUpdated() { return mLastUpdated; }

	public void setChildrenLastUpdated(Date childrenLastUpdated) { mChildrenLastUpdated = childrenLastUpdated; }
	public Date getChildrenLastUpdated() { return mChildrenLastUpdated; }

	public void setChildren(List children) { mChildren = children; }
	public List getChildren() { return mChildren; }

	public void setEditHistory(List editHistory) { mEditHistory = editHistory; }
	public List getEditHistory() { return mEditHistory; }

	public void addEditHistory(PostHistory hist)
	{
		if (mEditHistory == null)
		{
			mEditHistory = new ArrayList();
		}

		mEditHistory.add(hist);
	}

	@Override
	public String toString()
	{
		return
			"\n\tmId                  = " + mId + "\n" +
			"\tmForum               = " + (mForum == null ? "null" : ("" + mForum.getId())) + "\n" +
			"\tmParent              = " + (mParent == null ? "null" : ("" + mParent.getId())) + "\n" +
			"\tmThread              = " + (mThread == null ? "null" : ("" + mThread.getId())) + "\n" +
			"\tmLeftId              = " + mLeftId + "\n" +
			"\tmRightId             = " + mRightId + "\n" +
			"\tmReplyCount          = " + mReplyCount + "\n" +
			"\tmLevel               = " + mLevel + "\n" +
			"\tmSubject             = " + mSubject + "\n" +
			"\tmContent             = " + mContent + "\n" +
			"\tmOriginalContent     = " + mOriginalContent + "\n" +
			"\tmSignature           = " + mSignature + "\n" +
			"\tmOriginalSignature   = " + mOriginalSignature + "\n" +
			"\tmLocked              = " + mLocked + "\n" +
			"\tmConvertNewlines     = " + mConvertNewlines + "\n" +
			"\tmConvertLinks        = " + mConvertLinks + "\n" +
			"\tmConvertCodes        = " + mConvertCodes + "\n" +
			"\tmConvertHtml         = " + mConvertHtml + "\n" +
			"\tmCreatedBy           = " + (mCreatedBy == null ? "null" : ("" + mCreatedBy.getId())) + "\n" +
			"\tmCreated             = " + mCreated + "\n" +
			"\tmLastUpdated         = " + mLastUpdated + "\n" +
			"\tmChildrenLastUpdated = " + mChildrenLastUpdated + "\n" +
			"\tmEditHistory         = \n" + mEditHistory + "\n";
	}

	public void logMe(Log logger)
	{
		logger.error("POST: " + toString());
	}
} 
