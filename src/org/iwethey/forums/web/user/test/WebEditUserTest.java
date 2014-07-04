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

package org.iwethey.forums.web.user.test;

import com.meterware.httpunit.*;

import junit.framework.TestCase;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import java.util.Properties;

/**
 * Exercises the edit functionality of the web interface.
 * <p>
 * $Id: WebEditUserTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class WebEditUserTest extends TestCase
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

	public void testEditGeneral()
		throws Exception
		{
			// Login
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			assertNotNull("Form not null", form);
			form.setParameter("nickname", "ut_spork3");
			form.setParameter("unencryptedPassword", "itchy3");
			resp = form.submit();
			String text = resp.getText();

			assertTrue("good login 1", text.indexOf("Logged in as") > -1);
			assertTrue("good login 2", text.indexOf("ut_spork3") > -1);

			// Edit form
			resp = resp.getLinkWith("edit").click();
			text = resp.getText();

			assertTrue("Got general form", text.indexOf("fullName") > -1);

			form = resp.getFormWithID("editform");
			form.setParameter("fullName", "Sporky Spig");
			form.setParameter("email", "spork@bondo.tld");
			form.setParameter("unencryptedPassword", "itchy-3");
			form.setParameter("passwordCheck", "itchy-3");
			resp = form.submit();

			// Check edits
			form = resp.getFormWithID("editform");

			assertEquals("fullName", "Sporky Spig", form.getParameterValue("fullName"));
			assertEquals("email", "spork@bondo.tld", form.getParameterValue("email"));
			text = resp.getText();

			// So now test the changed password
			resp = resp.getLinkWith("logout").click();
			link = resp.getLinkWith("login");
			assertNotNull("logged out", link);

			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			form = resp.getFormWithID("loginform");
			assertNotNull("Form not null", form);
			form.setParameter("nickname", "ut_spork3");
			form.setParameter("unencryptedPassword", "itchy-3");
			resp = form.submit();
			text = resp.getText();

			assertTrue("good login 3", text.indexOf("Logged in as") > -1);
			assertTrue("good login 4", text.indexOf("ut_spork3") > -1);

			resp = resp.getLinkWith("edit").click();
			text = resp.getText();

			assertTrue("Got general form", text.indexOf("fullName") > -1);
			form = resp.getFormWithID("editform");

			assertNotNull("Form not null", form);
			assertEquals("fullName", "Sporky Spig", form.getParameterValue("fullName"));
			assertEquals("email", "spork@bondo.tld", form.getParameterValue("email"));

			// Change the password back
			form.setParameter("unencryptedPassword", "itchy3");
			form.setParameter("passwordCheck", "itchy3");
			resp = form.submit();

			form = resp.getFormWithID("editform");
			assertNull("showInActives", form.getParameterValue("showInActives"));
			form.setParameter("showInActives", "true");
			resp = form.submit();

			// This should detect problems with caching user objects
			form = resp.getFormWithID("editform");
			assertEquals("showInActives", "true", form.getParameterValue("showInActives"));
			resp = resp.getLinkWith("Display").click();
			resp = resp.getLinkWith("General").click();

			form = resp.getFormWithID("editform");
			assertEquals("showInActives", "true", form.getParameterValue("showInActives"));

			// Remove it and check the results
			form.removeParameter("showInActives");
			resp = form.submit();

			form = resp.getFormWithID("editform");
			assertNull("showInActives", form.getParameterValue("showInActives"));
			form.removeParameter("showInActives");
			resp = form.submit();

			// So now test the changed password
			resp = resp.getLinkWith("logout").click();
			link = resp.getLinkWith("login");
			assertNotNull("logged out", link);

			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			form = resp.getFormWithID("loginform");
			assertNotNull("Form not null", form);
			form.setParameter("nickname", "ut_spork3");
			form.setParameter("unencryptedPassword", "itchy3");
			resp = form.submit();
			text = resp.getText();

			assertTrue("good login 5", text.indexOf("Logged in as") > -1);
			assertTrue("good login 6", text.indexOf("ut_spork3") > -1);


		}

	/**
	 * Tests a bug that causes preference data to be overwritten
	 * after saving a different preference page.
	 */
	public void testEditDisplayBug()
		throws Exception
		{
			// Login
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			assertNotNull("Form not null", form);
			form.setParameter("nickname", "ut_spork2");
			form.setParameter("unencryptedPassword", "itchy2");
			resp = form.submit();
			String text = resp.getText();

			assertTrue("good login 1", text.indexOf("Logged in as") > -1);
			assertTrue("good login 2", text.indexOf("ut_spork2") > -1);

			// Edit form
			resp = resp.getLinkWith("edit").click();
			text = resp.getText();

			assertTrue("Got general form", text.indexOf("fullName") > -1);

			form = resp.getFormWithID("editform");
			assertNull("showInActives", form.getParameterValue("showInActives"));
			form.setParameter("showInActives", "true");
			resp = form.submit();

			// This should detect problems with caching user objects
			form = resp.getFormWithID("editform");
			assertEquals("showInActives", "true", form.getParameterValue("showInActives"));

			resp = resp.getLinkWith("Display").click();
			form = resp.getFormWithID("editform");
			form.setParameter("useCss", "true");
			resp = form.submit();

			resp = resp.getLinkWith("General").click();

			form = resp.getFormWithID("editform");
			assertEquals("showInActives", "true", form.getParameterValue("showInActives"));

			form.removeParameter("showInActives");
			resp = form.submit();

			resp = resp.getLinkWith("Display").click();
			form = resp.getFormWithID("editform");
			assertEquals("useCss", "true", form.getParameterValue("useCss"));
		}
}


