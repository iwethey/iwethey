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

import java.util.List;

/**
 * Describes the database interface for user functions.
 * <p>
 * This can be implemented by anything: a database, LDAP,
 * CSV files, whatever.
 * <p>
 * $Id: UserManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface UserManager
{
	/**
	 * Get a list of all users in the system.
	 * <p>
	 * @return A list of User objects.
	 */
    public List getUserList();

	/**
	 * Get a user by nickname.
	 * <p>
	 * @param nickname The nickname to use to find the user.
	 * @return The user with the given nickname, or null if none. 
	 */
    public User getUserByNickname(String nickname);

	/**
	 * Get a user by id.
	 * <p>
	 * @param id The id to use to find the user.
	 * @return The user with the given nickname, or null if none. 
	 */
    public User getUserById(int id);

	/**
	 * Verify a user login. Encrypts the password using MD5 and attempts to
	 * match it to that of the user with the given nickname.
	 * <p>
	 * @param user The User object containing the nickname and password to check.
	 * @return True if the nickname/password combo is valid, false otherwise.
	 */
	public boolean login(User user);

	/**
	 * Create a new user. Uses the nickname and password only. To insert an entire
	 * user with properties, use the saveUser method.
	 * <p>
	 * @param user The User object containing the information for insertion.
	 * @return The id of the new user.
	 * @see #saveUser(User)
	 */
    public int createNewUser(User user);

	/**
	 * Save a user. If the user does not exist, it is created first. If the password has been
	 * set in the User object, then the password will be updated as well. Finally, the properties
	 * are saved.
	 * <p>
	 * @param user The User object containing the information for saving.
	 */
	public void saveUser(User user);

	/**
	 * Save a user. If the user does not exist, nothing is saved.
	 * <p>
	 * @param user The User object containing the information for saving.
	 */
	public void saveUserAttributes(User user);

	/**
	 * Remove a user from the system. The nickname from the user object is used.
	 * <p>
	 * @param user The User object containing the nickname for deletion.
	 */
    public void removeUser(User user);

	/**
	 * Counts the total number of users in the system.
	 * <p>
	 * @return The number of users found.
	 */
	public int getUserCount();

	/**
	 * Counts the number of active users in the system.
	 * <p>
	 * @return The number of users active in the last 10 minutes.
	 */
	public int getActiveUserCount();

	/**
	 * Get the active users.
	 * <p>
	 * @return The users active in the last 10 minutes.
	 */
	public List<User> getActiveUsers();

	/**
	 * Checks the nickname for existence in the system.
	 * <p>
	 * @param nickname The nickname of the user to search for.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean getExists(String nickname);

	/**
	 * Checks the nickname of the User object for existence in the system.
	 * <p>
	 * @param user The user to search for.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean getExists(User user);
}
