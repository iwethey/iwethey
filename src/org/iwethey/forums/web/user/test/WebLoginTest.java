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
 * Exercises the login functionality of the web interface.
 * <p>
 * $Id: WebLoginTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class WebLoginTest extends TestCase
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

	public void testLogin()
		throws Exception
		{
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );

			WebLink link = resp.getLinkWith("login");
			resp = link.click();
			assertTrue("Got form", resp.getText().indexOf("Password") > -1);

			WebForm form = resp.getFormWithID("loginform");
			assertTrue("Form not null", form != null);
			assertTrue("Nickname 1", form.getParameterValue("nickname").equals(""));
			assertTrue("Password 1", form.getParameterValue("unencryptedPassword").equals(""));

			resp = form.submit(form.getSubmitButton("SUBMIT"));
			String text = resp.getText();

			assertTrue("empty nickname 1", text.indexOf("User nickname required") > -1);
			assertTrue("empty password 1", text.indexOf("User password required") > -1);

			form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork16");
			form.setParameter("unencryptedPassword", "itchy1");
			resp = form.submit();
			text = resp.getText();

			assertTrue("bad nickname 1", text.indexOf("User nickname does not exist") > -1);

			form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork1");
			form.setParameter("unencryptedPassword", "itchy13");
			resp = form.submit();
			text = resp.getText();

			assertTrue("bad password 1", text.indexOf("User nickname does not exist") > -1);

			form = resp.getFormWithID("loginform");
			form.setParameter("nickname", "ut_spork1");
			form.setParameter("unencryptedPassword", "itchy1");
			resp = form.submit();
			text = resp.getText();

			assertTrue("good login 1", text.indexOf("Logged in as") > -1);
			assertTrue("good login 2", text.indexOf("ut_spork1") > -1);

			link = resp.getLinkWith("logout");
			resp = link.click();
			link = resp.getLinkWith("login");
			assertNotNull("logged out", link);
		}
}
