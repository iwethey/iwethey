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

import org.iwethey.filter.ConstantHandler;
import org.iwethey.filter.Filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Accepts non-nesting newline values.
 * <p>
 * $Id: HtmlHelper.java 74 2004-12-14 04:34:10Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HtmlHelper
{
	public static void addCommonInlineConstantTags(Filter filter)
		{
			addCommonConstantTags(filter,
								  new String[] {
									  "b",
									  "big",
									  "code",
									  "em",
									  "i",
									  "small",
									  "strike",
									  "strong",
									  "sub",
									  "sup",
									  "tt",
									  "u"
								  });
		}

	public static void addCommonBlockConstantTags(Filter filter)
		{
			// Some of these need to be made into more complex tags, like ol, ul, etc.
			addCommonConstantTags(filter,
								  new String[] {
									  "blockquote",
									  "center",
									  "dd",
									  "dl",
									  "dt",
									  "h1",
									  "h2",
									  "h3",
									  "h4",
									  "h5",
									  "h6",
									  "li",
									  "ol",
									  "pre",
									  "ul"
								  });

			filter.addHandler(new ConstantHandler("<br>"));
			filter.addHandler(new ConstantHandler("<br/>"));

			filter.addHandler(new ConstantHandler("<hr>"));
			filter.addHandler(new ConstantHandler("<hr/>"));

			filter.addHandler(new AmpersandEscapeHandler());

		}

	private static void addCommonConstantTags(Filter filter, String[] tags)
		{
			int i = tags.length;
			while (i-- != 0)
				{
					filter.addHandler(new SimpleConstantTagHandler(tags[i], filter));
				}
		}

	public static void addRawUrlHandlers(Filter filter)
		{
			addRawUrlHandlers(filter, null);
		}

	public static void addRawUrlHandlers(Filter filter, UrlFormatter formatter)
		{
			filter.addHandler(new RawUrlHandler("http", formatter));
			filter.addHandler(new RawUrlHandler("https", formatter));
			filter.addHandler(new RawUrlHandler("ftp", formatter));
			filter.addHandler(new RawUrlHandler("gopher", formatter));
			filter.addHandler(new RawUrlHandler("telnet", formatter));
		}
} 
