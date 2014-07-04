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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Represents a Quote (commonly called a LRPDism or Lerpadism). Used for form backing objects and data
 * storage.</p>
 *
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class OldQuote implements Serializable
{
    protected final Log logger = LogFactory.getLog(getClass());

	/** The Board that this Quote belongs to. */
	private Board board = null;

	/** The actual Quote. */
	private String quote = "";

	/** The user that created this Quote. */
	private String createdBy = "";

	/** The creation time/date of this Quote. */
	private Date created = null;

	/** Blank constructor for bean use. */
	public OldQuote() { }

	public Board getBoard() { return board; }
	public void setBoard(Board board) { this.board = board; }

	public void logMe(Log logger)
	{
		logger.error("POST: " + toString());
	}

	/**
	 * @return the quote
	 */
	public String getQuote() {
		return quote;
	}

	/**
	 * @param quote the quote to set
	 */
	public void setQuote(String quote) {
		this.quote = quote;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString()
	{
		return
			"\n\tBoard               = " + (board == null ? "null" : ("" + board.getId())) + "\n" +
			"\tQuote               = " + quote + "\n" +
			"\tCreatedBy           = " + createdBy + "\n" +
			"\tCreated             = " + created + "\n";
	}
}
