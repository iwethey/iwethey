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

package org.iwethey.filter.html.test;

import org.iwethey.filter.html.*;

/**
 * Formats URLs as MooRLs.
 * <p>
 * $Id: MooUrlFormatter.java 50 2004-12-06 03:40:54Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class MooUrlFormatter implements UrlFormatter
{
	public String formatUrl(String protocol, String host, String path)
		{
			return formatUrl(protocol, host, path, null);
		}

	public String formatUrl(String protocol, String host, String path, String text)
		{
			return "MOO:" + host + (path == null ? "" : path);
		}
} 
