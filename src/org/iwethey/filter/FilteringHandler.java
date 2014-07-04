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

package org.iwethey.filter;

/**
 * Handler for simply filtering output; unnecessary unless your passthrough
 * handler escapes output (as the HTML escaper does).
 * <p>
 * $Id: FilteringHandler.java 45 2004-12-04 23:15:56Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class FilteringHandler extends AbstractKeyedHandler
{
	public FilteringHandler(String filterPattern)
		{
			setKey(filterPattern);
		}

	public FilteringHandler()
		{
		}

	public void process(FilterContext ctx, String text)
		{
			// This will never be called, since this is a filtering pattern.
		}

	public boolean isEscapedPattern()
		{
			return true;
		}
} 
