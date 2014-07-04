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

import org.iwethey.crypt.UnixCrypt;

import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;

import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the database interface for user functions.
 * <p>
 * $Id: HibUserManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibUserManager extends HibernateSupportExtensions implements UserManager
{
    /** Logger for this class and subclasses */
    private static final Log logger = LogFactory.getLog(HibUserManager.class);

	/**
	 * Get a list of all users in the database.
	 * <p>
	 * @return A list of User objects.
	 */
    public List getUserList()
	{
		return getHibernateTemplate().loadAll(User.class);
	}

	/**
	 * Get a user by nickname.
	 * <p>
	 * @param nickname The nickname to use to find the user.
	 * @return The user with the given nickname, or null if none. 
	 */
    public User getUserByNickname(String nickname)
	{
		List l = getHibernateTemplate().find("from User user where user.nickname = ?", nickname);

		if (l.size() == 1) {
			return (User) l.get(0);
		}

		return null;
	}

	/**
	 * Get a user by id.
	 * <p>
	 * @param id The id to use to find the user.
	 * @return The user with the given id, or null if none. 
	 */
    public User getUserById(int id)
	{
		try
		{
			return (User) getHibernateTemplate().get(User.class, new Integer(id));
		}
		catch (HibernateObjectRetrievalFailureException e)
		{
			return null;
		}
	}

	/**
	 * Verify a user login. Encrypts the password using MD5 and attempts to
	 * match it to that of the user with the given nickname.
	 * <p>
	 * @param user The User object containing the nickname and password to check.
	 * @return True if the nickname/password combo is valid, false otherwise.
	 */
    public boolean login(User user)
	{
		List l = getHibernateTemplate().find(
			"from User user where user.nickname = ? and user.encryptedPassword = ?",
			new Object[] { user.getNickname(), user.getEncryptedPassword() }
		);

		if (l.size() == 1)
		{
			return true;
		}

		// Check to see if this was a converted user login from Way Back When We Used Crypt.
		byte[] salt = user.getNickname().getBytes();
		String oldStylee = UnixCrypt.crypt(salt, user.getUnencryptedPassword());

		logger.info("SALT: " + salt + " oldStylee: " + oldStylee);

		l = getHibernateTemplate().find(
			"from User user where user.nickname = ? and user.encryptedPassword = ?",
			new Object[] { user.getNickname(), oldStylee }
		);

		if (l.size() != 1)
		{
			return false;
		}

		// Backfill it so we don't have to do this again, eh?
		User found = (User) l.get(0);
		found.setEncryptedPassword(user.getEncryptedPassword());
		saveUser(found);
		//		2009-09-27 13:02:31,362 INFO [org.iwethey.forums.db.HibUserManager] - <SALT: [B@3f9b39 oldStylee: adMsuds.QfuMM>

		return true;
	}

	/**
	 * Create a new user. Uses the nickname and password only. To insert an entire
	 * user with properties, use the saveUser method.
	 * <p>
	 * @param user The User object containing the information for insertion.
	 * @see #saveUser(User)
	 */
    public int createNewUser(User user)
	{
		user.setCreated(new Date());
		return ((Integer)getHibernateTemplate().save(user)).intValue();
	}

	/**
	 * Save a user. If the user does not exist, it is created first. If the password has been
	 * set in the User object, then the password will be updated in the database as well. Finally,
	 * the properties are saved.
	 * <p>
	 * @param user The User object containing the information for saving.
	 */
	public void saveUser(User user)
	{
		getHibernateTemplate().saveOrUpdate(user);
	}

	/**
	 * Save a user. If the user does not exist, nothing is saved.
	 * <p>
	 * @param user The User object containing the information for saving.
	 */
	public void saveUserAttributes(User user)
	{
		if (!user.isAnonymous()) {
			getHibernateTemplate().saveOrUpdate(user);
		}
	}

	/**
	 * Remove a user from the database. The nickname from the user object is used.
	 * <p>
	 * @param user The User object containing the nickname for deletion.
	 */
    public void removeUser(User user)
	{
		if (!user.isAnonymous()) {
			getHibernateTemplate().delete(user);
		}
	}

	/**
	 * Counts the total number of users in the database.
	 * <p>
	 * @return The number of users found.
	 */
	public int getUserCount()
	{
		return single("select count(*) from User user").intValue();
	}

	/**
	 * Counts the number of active users in the database.
	 * <p>
	 * @return The number of users active in the last 10 minutes.
	 */
	public int getActiveUserCount()
	{
		return single("select count(*) from User user where (CURRENT_TIMESTAMP - last_present) < '10 minutes'").intValue();
	}

	/**
	 * Get the active users.
	 * <p>
	 * @return The users active in the last 10 minutes.
	 */
	public List<User> getActiveUsers()
	{
		return getHibernateTemplate().find("from User where (CURRENT_TIMESTAMP - last_present) < '10 minutes' order by lastPresent asc");
	}

	/**
	 * Checks the nickname for existence in the database.
	 * <p>
	 * @param nickname The nickname of the user to search for.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean getExists(String nickname)
	{
		return single("select count(*) from User user where nickname = ?", nickname).intValue() > 0;
	}

	/**
	 * Checks the nickname of the User object for existence in the database.
	 * <p>
	 * @param user The user to search for.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean getExists(User user)
	{
		return getExists(user.getNickname());
	}
}
