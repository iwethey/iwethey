/**
   Copyright 2004-2010 Scott Anderson and Mike Vitale

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
 * <p>Describes the database interface for system functions.</p>
 *
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public interface AdminManager
{
	/**
	 * <p>Retrieve the number of quotes in the database.</p>
	 */
    public int getLRPDCount();

	/**
	 * <p>Retrieve a random quotes from the database.</p>
	 */
    public String getLRPD();

	/**
	 * <p>Retrieve a specific quote from the database.</p>
	 *
	 * @param id The id of the quote to retrieve.
	 */
	public Quote getQuote(int id);

	/**
	 * <p>Save a Quote to the database.</p>
	 *
	 * @param q The Quote to save.
	 */
	public void saveQuote(Quote q);

	/**
	 * <p>Remove a Quote from the system.</p>
	 *
	 * @param q The Quote object to be removed.
	 */
	public void removeQuote(Quote q);

	/**
	 * <p>Remove a Quote from the system.</p>
	 *
	 * @param id The id of the Quote object to be removed.
	 */
	public void removeQuote(int id);

	/**
	 * <p>Retrieve all quotes from the database.</p>
	 */
    public List<Quote> getAllLRPDs();

	/**
	 * <p>Retrieve all (old-style) quotes from the database.</p>
	 */
    public List<OldQuote> getAllOldLRPDs();

	/**
	 * Get the current system-wide notice, if any.
	 * <p>
	 */
	public String getNotice();
}
