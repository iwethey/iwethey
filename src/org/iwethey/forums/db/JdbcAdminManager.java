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

package org.iwethey.forums.db;

import org.iwethey.forums.domain.AdminManager;
import org.iwethey.forums.domain.Quote;
import org.iwethey.forums.domain.OldQuote;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Query;

import org.springframework.jdbc.object.SqlFunction;

/**
 * <p>Implements the database interface for system functions.</p>
 *
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class JdbcAdminManager extends HibernateSupportExtensions implements AdminManager
{
    /** Logger for this class and subclasses */
    private final Log logger = LogFactory.getLog(getClass());

	/** Data source as set by the application initialization. */
    private DataSource mDatasource;

	/** Picks the quote-of-the-page. */
	private Random mLRPDChooser = new Random();

	/** Retrieves a specific quote by number from the database. */
	SqlFunction mLRPDRetriever = null;

	/** Counts the quotes in the database. */
	private static final String COUNT_LRPDS_SQL =
	"select count(*) as lrpdcount from Quote";

	/** Retrieves a specific quote by number for a board from the database. */
	private static final String GET_LRPD_SQL =
	"select quote from Quote limit 1 offset ?";

	/** 
	 * <p>Retrieve the number of quotes in the database.</p>
	 */
    public int getLRPDCount()
	{
		Query query = getSession().createQuery(COUNT_LRPDS_SQL);
		Number result = (Number) query.uniqueResult();

		return result.intValue();
	}

	/**
	 * <p>Retrieve a random quote from the database.</p>
	 */
    public String getLRPD()
	{
		int count = getLRPDCount();

		if (count == 0)
		{
			return "Yes, we have no bananas.";
		}

		int next = mLRPDChooser.nextInt(count);

		return (String) mLRPDRetriever.runGeneric(new Object[] { new Integer(next) } );
	}

	/**
	 * <p>Retrieve a specific quote from the database.</p>
	 *
	 * @param id The id of the quote to retrieve.
	 */
	public Quote getQuote(int id)
	{
		return (Quote) retrieveObjectById(id, Quote.class);
	}

	/**
	 * <p>Save a Quote to the database.</p>
	 *
	 * @param q The Quote to save.
	 */
	public void saveQuote(Quote q)
	{
		Date now = new Date();

		if (q.getCreated() == null)
		{
			q.setCreated(now);
		}

		if (q.isApproved() && q.getApprovedDate() == null) {
			q.setApprovedDate(now);
		}

		getHibernateTemplate().setFlushMode(getHibernateTemplate().FLUSH_ALWAYS);
		getHibernateTemplate().saveOrUpdate(q);
	}

	/**
	 * <p>Remove a Quote from the system.</p>
	 *
	 * @param q The Quote object to be removed.
	 */
	public void removeQuote(Quote q)
	{
		getHibernateTemplate().delete(q);
	}

	/**
	 * <p>Remove a Quote from the system.</p>
	 *
	 * @param id The id of the Quote object to be removed.
	 */
	public void removeQuote(int id)
	{
		Quote q = this.getQuote(id);
		getHibernateTemplate().update(q);
	}

	/**
	 * <p>Retrieve all random quotes from the database.</p>
	 */
    public List<Quote> getAllLRPDs()
	{
		return getHibernateTemplate().find("from Quote order by id asc");
	}

	/**
	 * <p>Retrieve all (old-style) quotes from the database.</p>
	 */
    public List<OldQuote> getAllOldLRPDs()
	{
		return getHibernateTemplate().find("from OldQuote");
	}

	/**
	 * Get the current system-wide notice, if any.
	 * <p>
	 */
	public String getNotice()
	{
		return null;
	}

	/**
	 * Set the datasource to use for this database interface.
	 * <p>
	 * @param ds The datasource as determined in the application context.
	 */
    public void setDataSource(DataSource ds)
	{
		mDatasource = ds;

		mLRPDRetriever = new SqlFunction(ds, GET_LRPD_SQL, new int[] {java.sql.Types.INTEGER}, String.class);
		mLRPDRetriever.compile();
	}
}
