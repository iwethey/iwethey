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

/**
 * Accepts non-nesting newline values.
 * <p>
 * $Id: NewlineHandler.java 36 2004-12-02 22:28:38Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewlineHandler extends AbstractKeyedHandler
{
	public NewlineHandler()
		{
			setKey("\n");
		}

	public void process(FilterContext ctx, String text)
		{
			ctx.addResults("<br>");
			ctx.incrementLine();
		}
} 
