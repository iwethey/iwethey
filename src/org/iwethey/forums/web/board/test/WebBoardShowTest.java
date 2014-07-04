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

package org.iwethey.forums.web.board.test;

import com.meterware.httpunit.*;

import junit.framework.*;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import java.util.Properties;

/**
 * Exercises the board listing functionality of the web interface.
 * <p>
 * $Id: WebBoardShowTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class WebBoardShowTest extends TestCase
{
	public static String BASE_URL;
	public static String APP_NAME;
	public static String TOP_URL;

	static {
		try
		{
			Properties props = PropertiesLoaderUtils.loadAllProperties("runtime.properties");
			BASE_URL = props.getProperty("tomcat.baseurl");
			APP_NAME = props.getProperty("app.name");
			TOP_URL = BASE_URL + "/" + APP_NAME + "/";
		} catch (java.io.IOException ie) {
			System.out.println("Can't find runtime.properties.");
		}
	}

	public WebBoardShowTest(String name)
		{
			super(name);
		}

    public static Test suite()
    {
		TestSuite suite= new TestSuite("Show Board Tests");

		suite.addTest(new WebBoardShowTest("testShowAll"));
		suite.addTest(new WebBoardShowTest("testMarkAllRead"));

		return suite;
    }

	public void testShowAll()
		throws Exception
		{
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );
			String text = resp.getText();

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork1");
			form.setParameter("unencryptedPassword", "itchy1");
			resp = form.submit();

			WebLink[] links = resp.getLinks();
			link = null;
			for (int i = 0; i < links.length; i++)
				{
					if (links[i].getURLString().indexOf("boardid=-1") > -1)
						link = links[i];
				}
			assertNotNull(link);

			resp = link.click();

			assertNull(resp.getLinkWith("Tines"));
			assertNotNull(resp.getLinkWith("Chatter"));
			assertNull(resp.getLinkWith("History"));
			assertNull(resp.getLinkWith("Boston"));

			assertTrue(resp.getText().indexOf("New") > -1);

			link = resp.getLinkWith("Show All Forums");
			assertNotNull(link);

			resp = link.click();

			assertNotNull(resp.getLinkWith("Tines"));
			assertNotNull(resp.getLinkWith("Chatter"));
			assertNotNull(resp.getLinkWith("History"));
			assertNotNull(resp.getLinkWith("Boston"));

			link = resp.getLinkWith("Show New Forums Only");
			assertNotNull(link);

			resp = link.click();

			assertNull(resp.getLinkWith("Tines"));
			assertNotNull(resp.getLinkWith("Chatter"));
			assertNull(resp.getLinkWith("History"));
			assertNull(resp.getLinkWith("Boston"));
		}

	public void testMarkAllRead()
		throws Exception
		{
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );
			String text = resp.getText();

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork1");
			form.setParameter("unencryptedPassword", "itchy1");
			resp = form.submit();

			WebLink[] links = resp.getLinks();
			link = null;
			for (int i = 0; i < links.length; i++)
				{
					if (links[i].getURLString().indexOf("boardid=-1") > -1)
						link = links[i];
				}
			assertNotNull(link);

			resp = link.click();

			assertNull(resp.getLinkWith("Tines"));
			assertNotNull(resp.getLinkWith("Chatter"));
			assertNull(resp.getLinkWith("History"));
			assertNull(resp.getLinkWith("Boston"));

			link = resp.getLinkWith("Mark All Forums Read");
			assertNotNull(link);

			resp = link.click();

			assertNull(resp.getLinkWith("Tines"));
			assertNull(resp.getLinkWith("Chatter"));
			assertNull(resp.getLinkWith("History"));
			assertNull(resp.getLinkWith("Boston"));

			assertTrue(resp.getText().indexOf("So why not make one") > -1);
		}

	public void testShowMarkAllRead()
		throws Exception
		{
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );
			String text = resp.getText();

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork2");
			form.setParameter("unencryptedPassword", "itchy2");
			resp = form.submit();

			WebLink[] links = resp.getLinks();
			link = null;
			for (int i = 0; i < links.length; i++)
				{
					if (links[i].getURLString().indexOf("boardid=-1") > -1)
						link = links[i];
				}
			assertNotNull(link);

			resp = link.click();

			assertNull(resp.getLinkWith("Tines"));
			assertNotNull(resp.getLinkWith("Chatter"));
			assertNull(resp.getLinkWith("History"));
			assertNull(resp.getLinkWith("Boston"));

			link = resp.getLinkWith("Mark All Forums Read");
			assertNull(link);
		}
}
