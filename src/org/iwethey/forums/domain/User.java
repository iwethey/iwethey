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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a User. Used for form backing objects and data
 * storage.
 * <p>
 * $Id: User.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class User implements Serializable, Cloneable
{
    protected static final Log logger = LogFactory.getLog(User.class);

	private int mId = 0;
	
	/** The user's nickname (username). */
    private String mNickname = null;

	/** The password (only non-null when the user is being saved). */
	private String mUnencryptedPassword = null;

	/** The encrypted password. */
	private String mEncryptedPassword = null;

	/** The verification password (only non-null when the user is being saved). */
	private String mPasswordCheck = null;

	/** The date the user was created. */
	private Date mCreated = null;

	/** The date the user was last active in the system. */
	private Date mLastPresent = null;

	/** The user configuration and preference properties. */
	private Map mProperties = null;

	/** Read timestamps by forum ID. */
	private Map mForumRead = null;

	/** Is the User a forum Administrator? */
	private boolean admin = false;

	/** Blank constructor for bean user. */
	public User() { }

	/**
	 * Create this user from another user, copying all fields.
	 * <p>
	 * @param user The user to copy from.
	 */
	public User(User user)
	{
		setFromUser(user);
	}

	/**
	 * Create this user, using the given name.
	 * <p>
	 * @param nickname The user's system ID (username).
	 */
	public User(String nickname)
	{
		this.mNickname = nickname;
	}

	/**
	 * Create this user, using the given name and password.
	 * <p>
	 * @param nickname The user's system ID (username).
	 * @param password The unencrypted password used for login verification.
	 */
	public User(String nickname, String password)
	{
		this(nickname);
		setUnencryptedPassword(password);
	}

	public void setId(int id) { mId = id; }
	public int getId() { return mId; }

	public void setNickname(String nickname) { mNickname = nickname; }
	public String getNickname() { return mNickname; }

	public void setUnencryptedPassword(String password)
	{
		mUnencryptedPassword = password;

		if (password != null && !password.equals(""))
		{
			mEncryptedPassword = encrypt(password);
		}
	}
	public String getUnencryptedPassword() { return mUnencryptedPassword; }

	public void setEncryptedPassword(String password) { mEncryptedPassword = password; }
	public String getEncryptedPassword() { return mEncryptedPassword; }

	public void setPasswordCheck(String passwordCheck) { mPasswordCheck = passwordCheck; }
	public String getPasswordCheck() { return mPasswordCheck; }

	public void setCreated(Date created) { mCreated = created; }
	public Date getCreated() { return mCreated; }

	public void setLastPresent(Date lastPresent) { mLastPresent = lastPresent; }
	public Date getLastPresent() { return mLastPresent; }

	public void setForumRead(Map read) { mForumRead = read; }
	public Map getForumRead() { return mForumRead; }

	public Date getForumMark(Forum f)
	{
		return getForumMark(f.getId());
	}

	public Date getForumMark(int id)
	{
		if (mForumRead == null)
		{
			return null;
		}

		return (Date) mForumRead.get(new Integer(id));
	}

	public void markForumRead(Forum f)
	{
		markForumRead(f, new Date());
	}

	public void markForumRead(Forum f, Date now)
	{
		if (f == null)
		{
			return;
		}

		if (mForumRead == null)
		{
			mForumRead = new HashMap();
		}

		if (now == null)
		{
			now = new Date();
		}

		mForumRead.put(new Integer(f.getId()), now);
	}

	public void markForumsRead(Collection forums)
	{
		Date now = new Date();

		markForumsRead(forums, now);
	}

	public void markForumsRead(Collection forums, Date now)
	{
		if (forums == null) {
			return;
		}

		Iterator iter = forums.iterator();
		while (iter.hasNext()) {
			markForumRead((Forum) iter.next(), now);
		}
	}

	/**
	 * Set a single property on this user. The property will not actually
	 * be saved until save() is called.
	 * <p>
	 * @param name The name of the property to set.
	 * @param value The value of the property to set.
	 */
	public void setProperty(String name, String value)
	{
		getProperties().put(name, value);
	}

	/**
	 * Set a single boolean property on this user. The property will not actually
	 * be saved until save() is called.
	 * <p>
	 * @param name The name of the property to set.
	 * @param value The value of the property to set.
	 */
	public void setProperty(String name, boolean value)
	{
		setProperty(name, value ? "1" : "0");
	}

	/**
	 * Set a single integer property on this user. The property will not actually
	 * be saved until save() is called.
	 * <p>
	 * @param name The name of the property to set.
	 * @param value The value of the property to set.
	 */
	public void setProperty(String name, int value)
	{
		setProperty(name, Integer.toString(value));
	}

	/**
	 * Add a map of properties to this user's properties.
	 * <p>
	 * @param props the name/value pairs to add to this user's properties.
	 */
	public void addProperties(Map props)
	{
		getProperties().putAll(props);
	}

	/**
	 * Get the properties of this user.
	 * <p>
	 * @return The properties of this user. This is not a clone(), so do not modify!
	 */
	public Map getProperties()
	{
		if (mProperties == null)
		{
			mProperties = new HashMap();
		}

		return mProperties;
	}

	/**
	 * Set the properties of this user.
	 * <p>
	 * @return The properties of this user. This is not a clone(), so do not modify!
	 */
	public void setProperties(Map props)
	{
		mProperties = props;
	}

	/** 
	 * Get a single property from this user.
	 * <p>
	 * @param name The name of the property to get.
	 * @param defaultValue The value to return if the property has not been set.
	 * @return The property's value if it exists, otherwise the defaultValue.
	 */
	public String getProperty(String name, String defaultValue)
	{
		String val = (String) getProperties().get(name);

		if (val == null)
		{
			return defaultValue;
		}

		return val;
	}

	/** 
	 * Get a single property from this user.
	 * <p>
	 * @param name The name of the property to get.
	 * @return The property's value if it exists, otherwise null.
	 */
	public String getProperty(String name)
	{
		return getProperty(name, null);
	}

	/** 
	 * Get a single property from this user as a boolean. 
	 * <p>
	 * @param name The name of the property to get.
	 * @param defaultValue The value to return if the property has not been set.
	 * @return True if the property is "1", false otherwise, defaultValue if the property has not been set.
	 */
	public boolean getPropertyBoolean(String name, boolean defaultValue)
	{
		String val = (String) getProperties().get(name);

		if (val == null)
		{
			return defaultValue;
		}

		return val.startsWith("1");
	}

	/** 
	 * Get a single property from this user as a boolean. 
	 * <p>
	 * @param name The name of the property to get.
	 * @return True if the property is "1", false otherwise.
	 */
	public boolean getPropertyBoolean(String name)
	{
		return getPropertyBoolean(name, false);
	}

	/** 
	 * Get a single property from this user as an integer. 
	 * <p>
	 * @param name The name of the property to get.
	 * @param defaultValue The value to return if the property has not been set.
	 * @return The property as an integer if it is set, otherwise the defaultValue.
	 */
	public int getPropertyInt(String name, int defaultValue)
	{
		String val = (String) getProperties().get(name);

		if (val == null)
		{
			return defaultValue;
		}

		return Integer.parseInt(val);
	}

	/** 
	 * Get a single property from this user as an integer. 
	 * <p>
	 * @param name The name of the property to get.
	 * @return The property as an integer if it is set, otherwise 0.
	 */
	public int getPropertyInt(String name)
	{
		return getPropertyInt(name, 0);
	}

	/** 
	 * Set all of the properties and members of this user to those
	 * of the given user.
	 * <p>
	 * @param user The user to copy values from.
	 */
	public void setFromUser(User user)
	{
		assert user != null;

		setId(user.getId());
		setNickname(user.getNickname());
		setUnencryptedPassword(user.getUnencryptedPassword());
		setEncryptedPassword(user.getEncryptedPassword());
		setPasswordCheck(user.getPasswordCheck());
		setCreated(user.getCreated());
		setLastPresent(user.getLastPresent());
		setProperties(user.getProperties());
	}

	public void setFullName(String fullName) { setProperty("fullName", fullName); }
	public String getFullName() { return getProperty("fullName"); }

	public void setEmail(String email) { setProperty("email", email); }
	public String getEmail() { return getProperty("email"); }

	public void setShowInActives(boolean showInActives) { setProperty("showInActives", showInActives); }
	public boolean getShowInActives() { return getPropertyBoolean("showInActives", true); }

	public void setUseCss(boolean useCss) { setProperty("useCss", useCss); }
	public boolean getUseCss() { return getPropertyBoolean("useCss", true); }

	public void setSortByLastModified(boolean sortByLastModified) { setProperty("sortByLastModified", sortByLastModified); }
	public boolean getSortByLastModified() { return getPropertyBoolean("sortByLastModified", true); }

	public void setShowNewPostsOnly(boolean showNewPostsOnly) { setProperty("showNewPostsOnly", showNewPostsOnly); }
	public boolean getShowNewPostsOnly() { return getPropertyBoolean("showNewPostsOnly", true); }

	public void setShowThreadsCollapsed(boolean showThreadsCollapsed) { setProperty("showThreadsCollapsed", showThreadsCollapsed); }
	public boolean getShowThreadsCollapsed() { return getPropertyBoolean("showThreadsCollapsed", false); }

	public void setShowRepliesCollapsed(boolean showRepliesCollapsed) { setProperty("showRepliesCollapsed", showRepliesCollapsed); }
	public boolean getShowRepliesCollapsed() { return getPropertyBoolean("showRepliesCollapsed", false); }

	public void setShowPersonalPictures(boolean showPersonalPictures) { setProperty("showPersonalPictures", showPersonalPictures); }
	public boolean getShowPersonalPictures() { return getPropertyBoolean("showPersonalPictures", true); }

	public void setIndentationFormat(String indentationFormat) { setProperty("indentationFormat", indentationFormat); }
	public String getIndentationFormat() { return getProperty("indentationFormat", "nbsp"); }

	public void setHierarchyPosition(String hierarchyPosition) { setProperty("hierarchyPosition", hierarchyPosition); }
	public String getHierarchyPosition() { return getProperty("hierarchyPosition", "after"); }

	public void setShowPostIds(boolean showPostIds) { setProperty("showPostIds", showPostIds); }
	public boolean getShowPostIds() { return getPropertyBoolean("showPostIds", true); }

	public void setShowThreadReplyLinks(boolean showThreadReplyLinks) { setProperty("showThreadReplyLinks", showThreadReplyLinks); }
	public boolean getShowThreadReplyLinks() { return getPropertyBoolean("showThreadReplyLinks", true); }

	public void setShowMarkAllRead(boolean showMarkAllRead) { setProperty("showMarkAllRead", showMarkAllRead); }
	public boolean getShowMarkAllRead() { return getPropertyBoolean("showMarkAllRead", true); }

	public void setShowJumpTags(boolean showJumpTags) { setProperty("showJumpTags", showJumpTags); }
	public boolean getShowJumpTags() { return getPropertyBoolean("showJumpTags", true); }

	public void setFont(String font) { setProperty("font", font); }
	public String getFont() { return getProperty("font"); }

	public void setLinkFormat(String linkFormat) { setProperty("linkFormat", linkFormat); }
	public String getLinkFormat() { return getProperty("linkFormat", "newwindow"); }

	public void setImageFormat(String imageFormat) { setProperty("imageFormat", imageFormat); }
	public String getImageFormat() { return getProperty("imageFormat", "inline"); }

	public void setTimezone(String timezone) { setProperty("timezone", timezone); }
	public String getTimezone() { return getProperty("timezone", "US/Michigan"); }

	public void setForumBatchSize(int forumBatchSize) { setProperty("forumBatchSize", forumBatchSize); }
	public int getForumBatchSize() { return getPropertyInt("forumBatchSize", 10); }

	public void setForumControlLocation(String forumControlLocation) { setProperty("forumControlLocation", forumControlLocation); }
	public String getForumControlLocation() { return getProperty("forumControlLocation", "top"); }

	public void setShowForumMiniControl(boolean showForumMiniControl) { setProperty("showForumMiniControl", showForumMiniControl); }
	public boolean getShowForumMiniControl() { return getPropertyBoolean("showForumMiniControl", true); }

	public void setShowNewForumsOnly(boolean showNewForumsOnly) { setProperty("showNewForumsOnly", showNewForumsOnly); }
	public boolean getShowNewForumsOnly() { return getPropertyBoolean("showNewForumsOnly", true); }

	public void setInvertLpms(boolean invertLpms) { setProperty("invertLpms", invertLpms); }
	public boolean getInvertLpms() { return getPropertyBoolean("invertLpms", false); }

	public void setUseDialect(String dialect) { setProperty("dialect", dialect); }
	public String getUseDialect() { return getProperty("dialect", "normal"); }

	public void setSignature(String signature) { setProperty("signature", signature); }
	public String getSignature() { return getProperty("signature"); }

	public void setPersonalPictureUrl(String personalPictureUrl) { setProperty("personalPictureUrl", personalPictureUrl); }
	public String getPersonalPictureUrl() { return getProperty("personalPictureUrl"); }

	public void setPreviewByDefault(boolean previewByDefault) { setProperty("previewByDefault", previewByDefault); }
	public boolean getPreviewByDefault() { return getPropertyBoolean("previewByDefault", true); }

	public void setAutofillSubject(boolean autofillSubject) { setProperty("autoFillSubject", autofillSubject); }
	public boolean getAutofillSubject() { return getPropertyBoolean("autoFillSubject", true); }

	public void setConvertNewlines(boolean convertNewlines) { setProperty("convertNewlines", convertNewlines); }
	public boolean getConvertNewlines() { return getPropertyBoolean("convertNewlines", true); }

	public void setConvertHtml(boolean convertHtml) { setProperty("convertHtml", convertHtml); }
	public boolean getConvertHtml() { return getPropertyBoolean("convertHtml", true); }

	public void setConvertLinks(boolean convertLinks) { setProperty("convertLinks", convertLinks); }
	public boolean getConvertLinks() { return getPropertyBoolean("convertLinks", true); }

	public void setConvertCodes(boolean convertCodes) { setProperty("convertCodes", convertCodes); }
	public boolean getConvertCodes() { return getPropertyBoolean("convertCodes", true); }

	public void setDefaultBoard(int defaultBoard) { setProperty("defaultBoard", defaultBoard); }
	public int getDefaultBoard() { return getPropertyInt("defaultBoard", 0); }

	public void setAdmin(boolean admin) { this.admin = admin; }
	public boolean isAdmin() { return this.admin; }

	@Override
	public Object clone()
	{
		return new User(this);
	}

    private static final char mHexChars[] =
	{
    	'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
	};

	/**
	 * Encrypt a string using MD5.
	 * <p>
	 * @param buffer The string to encrypt.
	 * @return A hex-encoded encrypted string.
	 */
    public String encrypt(String buffer)
	{
		MessageDigest crypt = null;

		try
		{
			crypt = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e1)
		{
			LogFactory.getLog(getClass()).error("No MD5 implementation!");
			return null;
		}

		crypt.update(buffer.getBytes());

		byte[] bytes = crypt.digest();

		StringBuffer hex = new StringBuffer(2 * bytes.length);

		for (int i = 0; i < bytes.length; i++)
		{
			byte bch = bytes[i];

			hex.append(mHexChars[(bch & 0xF0) >> 4]);
			hex.append(mHexChars[bch & 0x0F]);
		}

		return hex.toString();
	}

	public boolean isAnonymous()
	{
		return mId == 0;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("Nickname = [").append(getNickname()).append("]");

		return buf.toString();
	}
}
