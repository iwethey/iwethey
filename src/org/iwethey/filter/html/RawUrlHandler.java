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

import org.iwethey.filter.FilterContext;
import org.iwethey.filter.AbstractKeyedHandler;

import java.util.HashMap;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Accepts non-nesting newline values.
 * <p>
 * $Id: RawUrlHandler.java 72 2004-12-12 19:45:51Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class RawUrlHandler extends AbstractKeyedHandler
{
	private UrlFormatter mFormatter = null;

	/**
	 * Matches a standard URL. This is not meant to be comprehensive.
	 * The matcher will avoid including punctuation at the end of a URL;
	 * this is not foolproof by any means. The intent is to allow the
	 * use of raw URLs in the middle of normal text.
	 */
	private Pattern mUrlPattern = Pattern.compile(
		"//"                             + // separator
		"("                              + // start the host name
		"[\\w\\.\\-_]+"                  + // hostname, can't end in . or /
		"[^\\s\\.,\"\\?!:;\\]\\)\\</]"   + // host can't end in puncuation or a space or a <
		"(?::[\\d]+)?"                   + // optional :port number
		")"                              + // end the host name
		"("                              + // start the path
		"/(?:[^\\s\\<\"]+"               + // path, starts with /, then some number of non-space not including < or "
		"[^\\s\\.,\"\\?!:;\\]\\)\\<])?"  + // path can't end in puncuation or a space or a < or ]
		")?");                             // end the path, which is optional

	public RawUrlHandler(String protocol, UrlFormatter formatter)
		{
			if (protocol.endsWith(":"))
				{
					setKey(protocol.substring(0, protocol.length() - 1));
				}
			else
				{
					setKey(protocol);
				}

			if (formatter == null)
				{
					formatter = new StandardUrlFormatter();
				}

			mFormatter = formatter;
		}

	public RawUrlHandler(String protocol)
		{
			this(protocol, null);
		}

	public String getKey()
		{
			return super.getKey() + ":";
		}

	public final void process(FilterContext ctx, String text)
		{
			String remaining = ctx.getRemaining();
			Matcher m = mUrlPattern.matcher(remaining);

			if (m.lookingAt())
				{
					String protocol = super.getKey();
					String host = m.group(1);
					String path = m.group(2);

					ctx.addResults(mFormatter.formatUrl(protocol, host, path));

					ctx.skip(m.end());
				}
			else
				{
					ctx.abortResults();
				}
		}
} 
