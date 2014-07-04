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

import org.iwethey.filter.AbstractStackableHandler;
import org.iwethey.filter.Filter;
import org.iwethey.filter.FilterContext;

/**
 * For simple tags with no attributes, like &lt;b&gt; and the like.
 * <p>
 * $Id: SimpleConstantTagHandler.java 48 2004-12-05 16:49:09Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class SimpleConstantTagHandler extends AbstractStackableHandler
{
	public SimpleConstantTagHandler(String tag)
		{
			setKey("<" + tag + ">");
		}

	/**
	 * Convenience constructor; adds a matching close tag handler to
	 * the filter.
	 * <p>
	 * @param tag The tag name for this constant tag.
	 * @param filter The filter to add the matching close tag to.
	 */
	public SimpleConstantTagHandler(String tag, Filter filter)
		{
			this(tag);

			filter.addHandler(new SimpleConstantTagCloseHandler(tag));
		}

	public void process(FilterContext ctx, String text)
		{
			ctx.addResults(text);
		}
} 
