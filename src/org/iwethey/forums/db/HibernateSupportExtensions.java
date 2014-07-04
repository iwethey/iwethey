package org.iwethey.forums.db;

import org.springframework.dao.DataAccessException;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;

import java.util.List;

/**
 * Implements extensions to the Spring Hibernate support class.
 * <p>
 * $Id: HibernateSupportExtensions.java 393 2007-06-02 13:42:28Z mike $
 * <p>
 * @author Scott Anderson (<a href="mailto:scottanderson@comcast.net">scottanderson@comcast.net</a>)
 */
public class HibernateSupportExtensions extends HibernateDaoSupport
{
	/**
	 * Convenience method for retrieving an arbitrary class by ID. Casting
	 * must be performed by the caller.
	 * <p>
	 * @param id Unique primary key ID for object to retrieve.
	 * @param objClass Class of object to retrieve.
	 * @return Object if found, null otherwise.
	 */
	protected Object retrieveObjectById(int id, Class objClass)
	{
		try
		{
			return getHibernateTemplate().get(objClass, new Integer(id));
		}
		catch (HibernateObjectRetrievalFailureException e)
		{
			return null;
		}
	}

	/**
	 * Convenience method for retrieving an arbitrary class by ID. Casting
	 * must be performed by the caller.
	 * <p>
	 * @param id Unique primary key ID for object to retrieve.
	 * @param objClass Class of object to retrieve.
	 * @return Object if found, null otherwise.
	 */
	protected Object retrieveObjectById(Serializable id, Class objClass)
	{
		try
		{
			return getHibernateTemplate().get(objClass, id);
		}
		catch (HibernateObjectRetrievalFailureException e)
		{
			return null;
		}
	}

	/**
	 * Perform a query that returns a single value.
	 * <p>
	 * @param queryStr The SQL query string to execute.
	 */
	public Integer single(final String queryStr)
		throws DataAccessException
	{
		return (Integer) getHibernateTemplate().execute(
														new HibernateCallback()
														{
															public Object doInHibernate(Session session)
																throws HibernateException
															{
																Query query = session.createQuery(queryStr);
																return query.iterate().next();
															}
														}
														);
	}

	/**
	 * Perform a query that returns a single value and takes
	 * a single value as argument.
	 * <p>
	 * @param queryStr The SQL query string to execute.
	 * @param arg The argument to pass to the query.
	 */
	public Integer single(final String queryStr, final Object arg)
		throws DataAccessException
	{
		return (Integer) getHibernateTemplate().execute(new HibernateCallback()
			{
				public Object doInHibernate(Session session)
					throws HibernateException
				{
					Query query = session.createQuery(queryStr);
					query.setParameter(0, arg);
					return query.iterate().next();
				}
			}
														);
	}

	/**
	 * Perform a named query that returns a single value and takes
	 * a single named value as argument.
	 * <p>
	 * @param queryName The named query to execute.
	 * @param argName The argument name to pass to the query.
	 * @param arg The argument to pass to the query.
	 */
	public Integer single(final String queryName, final String argName, final Object arg)
		throws DataAccessException
	{
		return (Integer) getHibernateTemplate().execute(new HibernateCallback()
			{
				public Object doInHibernate(Session session)
					throws HibernateException
				{
					Query query = getNamedQuery(session, queryName);
					query.setParameter(argName, arg);
					return query.iterate().next();
				}
			}
														);
	}

	/**
	 * Perform a named query that returns a single value and takes
	 * a set of named values as arguments.
	 * <p>
	 * @param queryName The named query to execute.
	 * @param argNames The argument names to pass to the query.
	 * @param args The arguments to pass to the query.
	 */
	public Integer single(final String queryName, final String[] argNames, final Object[] args)
		throws DataAccessException
	{
		return (Integer) getHibernateTemplate().execute(new HibernateCallback()
			{
				public Object doInHibernate(Session session)
					throws HibernateException
				{
					Query query = getNamedQuery(session, queryName);

					for (int i = 0; i < args.length; i++) {
						query.setParameter(argNames[i], args[i]);
					}

					return query.iterate().next();
				}
			}
														);
	}

	/**
	 * Proxy for getHibernateTemplate().getNamedQuery().
	 * <p>
	 */
	public Query getNamedQuery(Session session, String queryName)
		throws HibernateException
	{
		return session.getNamedQuery(queryName);
	}

	/**
	 * Perform a paged query that returns a list of objects and takes
	 * a single value as argument.
	 * <p>
	 * @param queryName The saved query name to execute.
	 * @param first The index of the first result to return.
	 * @param count The number of results to return.
	 * @param arg The argument to pass to the query.
	 */
	public List findByPagedQuery(final String queryName, final int first, final int count, final Object arg)
		throws DataAccessException
	{
		return getHibernateTemplate().executeFind(
												  new HibernateCallback()
												  {
													  public Object doInHibernate(Session session)
														  throws HibernateException
													  {
														  Query query = getNamedQuery(session, queryName);

														  query.setFirstResult(first);
														  query.setMaxResults(count);

														  query.setParameter(0, arg);

														  return query.list();
													  }
												  }
												  );
	}

	/**
	 * Perform a paged query that returns a list of objects and takes
	 * two values as arguments.
	 * <p>
	 * @param queryName The saved query name to execute.
	 * @param first The index of the first result to return.
	 * @param count The number of results to return.
	 * @param arg1 The first argument to pass to the query.
	 * @param arg2 The second argument to pass to the query.
	 */
	public List findByPagedQuery(final String queryName, final int first, final int count, final Object arg1, final Object arg2)
		throws DataAccessException
	{
		return getHibernateTemplate().executeFind(
												  new HibernateCallback()
												  {
													  public Object doInHibernate(Session session)
														  throws HibernateException
													  {
														  Query query = getNamedQuery(session, queryName);

														  query.setFirstResult(first);
														  query.setMaxResults(count);

														  query.setParameter(0, arg1);
														  query.setParameter(1, arg2);

														  return query.list();
													  }
												  }
												  );
	}

	/**
	 * Perform a paged query that returns a list of objects and takes
	 * an array of values as arguments.
	 * <p>
	 * @param queryName The saved query name to execute.
	 * @param first The index of the first result to return.
	 * @param count The number of results to return.
	 * @param args The arguments to pass to the query.
	 */
	public List findByPagedQuery(final String queryName, final int first, final int count, final Object[] args)
		throws DataAccessException
	{
		return getHibernateTemplate().executeFind(
												  new HibernateCallback()
												  {
													  public Object doInHibernate(Session session)
														  throws HibernateException
													  {
														  Query query = getNamedQuery(session, queryName);

														  query.setFirstResult(first);
														  query.setMaxResults(count);

														  for (int i = 0; i < args.length; i++) {
															  query.setParameter(i, args[i]);
														  }

														  return query.list();
													  }
												  }
												  );
	}

	/**
	 * Perform a paged query with named parameters that returns a list of objects and takes
	 * an array of values as arguments.
	 * <p>
	 * @param queryName The saved query name to execute.
	 * @param first The index of the first result to return.
	 * @param count The number of results to return.
	 * @param names The argument names to pass to the query.
	 * @param args The arguments to pass to the query.
	 */
	public List findByPagedQuery(final String queryName,
								 final int first, final int count,
								 final String[] names, final Object[] args)
		throws DataAccessException
	{
		return getHibernateTemplate().executeFind(
												  new HibernateCallback()
												  {
													  public Object doInHibernate(Session session)
														  throws HibernateException
													  {
														  Query query = getNamedQuery(session, queryName);

														  query.setFirstResult(first);
														  query.setMaxResults(count);

														  for (int i = 0; i < args.length; i++) {
															  query.setParameter(names[i], args[i]);
														  }

														  return query.list();
													  }
												  }
												  );
	}

	/**
	 * Perform a paged query with named parameters that returns a list of objects and takes
	 * a single value as argument.
	 * <p>
	 * @param queryName The saved query name to execute.
	 * @param first The index of the first result to return.
	 * @param count The number of results to return.
	 * @param name The argument name to pass to the query.
	 * @param arg The argument to pass to the query.
	 */
	public List findByPagedQuery(final String queryName,
								 final int first, final int count,
								 final String name, final Object arg)
		throws DataAccessException
	{
		return getHibernateTemplate().executeFind(
												  new HibernateCallback()
												  {
													  public Object doInHibernate(Session session)
														  throws HibernateException
													  {
														  Query query = getNamedQuery(session, queryName);

														  query.setFirstResult(first);
														  query.setMaxResults(count);

														  query.setParameter(name, arg);

														  return query.list();
													  }
												  }
												  );
	}

}
