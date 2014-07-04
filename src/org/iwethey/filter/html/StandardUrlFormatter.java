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

package org.iwethey.filter.html;

/**
 * Formats URLs as anchor tags.
 * <p>
 * $Id: StandardUrlFormatter.java 49 2004-12-06 02:15:40Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class StandardUrlFormatter implements UrlFormatter
{
	public String formatUrl(String protocol, String host, String path)
		{
			return formatUrl(protocol, host, path, null);
		}

	public String formatUrl(String protocol, String host, String path, String text)
		{
			StringBuffer buf = new StringBuffer("<a href=\"");
			
			buf.append(protocol);
			buf.append("://");
			buf.append(host);

			if (path != null)
				{
					buf.append(path);
				}

			if (text == null)
				{
					text = buf.substring(9);
				}

			int len = text.length();
			if (len > 40)
				{
					text = text.substring(0, 20) + "..." + text.substring(len - 17, len);
				}

			buf.append("\">");

			buf.append(text);

			buf.append("</a>");

			return buf.toString();
		}
} 
