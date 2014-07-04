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

package org.iwethey.forums.db.test;

import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Tests the board database manager.
 * <p>
 * $Id: HibernateTestHarness.java 653 2008-07-27 02:53:30Z mike $
 * <p>
 * @author Scott Anderson (<a href="mailto:scottanderson@comcast.net">scottanderson@comcast.net</a>)
 */
public class HibernateTestHarness extends TestCase
{
	protected ApplicationContext context = null;

	public HibernateTestHarness()
	{
		this.context = new ClassPathXmlApplicationContext("test-context.xml");
	}

    public void setUp()
		throws Exception
	{
		SessionFactory sf = (SessionFactory) this.context.getBean("sessionFactory");
		//Session s = SessionFactoryUtils.getSession(sessionFactory, true);
		// open and bind the session for this test thread.
		Session s = sf.openSession();
		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(s));
	}

	public void tearDown()
		throws Exception
	{
		SessionFactory sf = (SessionFactory) this.context.getBean("sessionFactory");
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sf);
		Session s = holder.getSession(); 
		s.flush();
		TransactionSynchronizationManager.unbindResource(sf);
		//SessionFactoryUtils.closeSessionIfNecessary(s, sf);
		SessionFactoryUtils.releaseSession(s, sf);
	}

	public Session getSession()
	{
		SessionFactory sf = (SessionFactory) this.context.getBean("sessionFactory");
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sf);
		return holder.getSession(); 
	}
}
