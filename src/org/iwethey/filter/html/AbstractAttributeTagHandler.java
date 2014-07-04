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

import org.iwethey.filter.AbstractKeyedHandler;
import org.iwethey.filter.Filter;
import org.iwethey.filter.FilterContext;
import org.iwethey.filter.FilterException;

import java.util.Map;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For simple tags with no attributes, like &lt;b&gt; and the like.
 * <p>
 * $Id: SimpleConstantTagHandler.java 48 2004-12-05 16:49:09Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class AbstractAttributeTagHandler extends AbstractKeyedHandler
{
	private Set mAttributes = null;
	private Set mRequired = null;
	private Map mValidators = null;
	private AttributeTagFormatter mFormatter = null;

	private Pattern mFinder = Pattern.compile(
		"(?:" +
		"\\s+" +
		"([\\w\\d]+)" +             // attribute name
		"\\s*=\\s*(" +              // = some value
		"[^\\s>\\\"\\'][^\\s>]*|" + // unquoted values
		"\\\"[^\\\"]*\\\"|" +       // double quoted values
		"\\'[^\\']*\\'|" +          // single quoted values
		")" + 
		"|" +
		"\\s*(/+)>" +               // end of tag
		")");

	public AbstractAttributeTagHandler(String tag, Set attributes)
		{
			this(tag, attributes, (AttributeTagFormatter) null);
		}

	public AbstractAttributeTagHandler(String tag, Set attributes, Filter filter)
		{
			this(tag, attributes, filter, null);
		}

	public AbstractAttributeTagHandler(String tag, Set attributes, AttributeTagFormatter format)
		{
			setKey("<" + tag + " ");

			if (attributes == null)
				{
					throw new FilterException("Attributes may not be null.");
				}
			mAttributes = attributes;

			if (format == null)
				{
					format = new StandardAttributeTagFormatter(tag);
				}
			else
				{
					mFormatter = format;
				}
		}

	public AbstractAttributeTagHandler(String tag, Set attributes, Filter filter, AttributeTagFormatter format)
		{
			this(tag, attributes, format);
			filter.addHandler(new SimpleConstantTagCloseHandler(tag));
		}

	public void process(FilterContext ctx, String text)
		{
			
		}
} 
