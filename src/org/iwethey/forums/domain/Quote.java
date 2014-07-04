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
public class Quote implements Serializable
{
    protected final Log logger = LogFactory.getLog(getClass());

	/** The unique ID of this Quote. */
	private int id = 0;

	/** The Board that this Quote belongs to. */
	private Board board = null;

	/** The actual Quote. */
	private String quote = "";

	/** Whether or not an Admin has approved this quote. */
	private boolean approved = false;

	/** The user that approved this Quote. */
	private User approvedBy = null;

	/** The approval time/date of this Quote. */
	private Date approvedDate = null;

	/** The user that created this Quote. */
	private User createdBy = null;

	/** The creation time/date of this Quote. */
	private Date created = null;

	/** Blank constructor for bean use. */
	public Quote() { }

	public void setId(int id) { this.id = id; }
	public int getId() { return id; }

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
	 * @return the approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}


	/**
	 * @return the approvedBy
	 */
	public User getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return the approvedDate
	 */
	public Date getApprovedDate() {
		return approvedDate;
	}

	/**
	 * @param approvedDate the approvedDate to set
	 */
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	/**
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
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
			"\n\tid                  = " + id + "\n" +
			"\tBoard               = " + (board == null ? "null" : ("" + board.getId())) + "\n" +
			"\tQuote               = " + quote + "\n" +
			"\tCreatedBy           = " + (createdBy == null ? "null" : ("" + createdBy.getId())) + "\n" +
			"\tCreated             = " + created + "\n";
	}
}
