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

package org.iwethey.forums.db.test;

import org.iwethey.forums.db.JdbcAdminManager;
import org.iwethey.forums.domain.Quote;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>Tests the administration database manager.</p>
 *
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class JdbcAdminManagerTest extends TestCase
{
    private JdbcAdminManager manager;
	private ApplicationContext context = null;

	@Override
    public void setUp()
	{
		this.context = new ClassPathXmlApplicationContext("test-context.xml");
		manager = (JdbcAdminManager) this.context.getBean("adminManager");
	}

	public void testLRPD()
	{
		String lrpd = manager.getLRPD();

		assertTrue("spork", lrpd.equals("Feared.") || lrpd.equals("Your Spork God[tm] is HERE!"));
	}

	public void testGetAllLRPDs()
	{
		List<Quote> lrpds = manager.getAllLRPDs();

		for (Quote lrpd : lrpds) {
			assertTrue("spork", lrpd.getQuote().equals("Feared.") || lrpd.getQuote().equals("Your Spork God[tm] is HERE!"));
		}
	}
}
