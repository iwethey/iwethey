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
 * $Id: WebBoardListTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class WebBoardListTest extends TestCase
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

	public void testBoards()
		throws Exception
		{
			WebConversation wc = new WebConversation();
			WebResponse resp = wc.getResponse( TOP_URL + "main.iwt" );
			String text = resp.getText();

			int spork = text.indexOf("Spork");
			int blanc = text.indexOf("Blancmange");

			assertTrue(spork > -1);
			assertTrue(blanc > -1);
			assertTrue(spork > blanc);
		}
}
