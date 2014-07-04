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

import java.util.Map;

/**
 * Handler that can be stacked in the filter context.
 * <p>
 * $Id: AbstractStackableHandler.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class AbstractStackableHandler extends AbstractKeyedHandler implements StackableHandler
{
	/**
	 * This allows subclasses that don't care about context to simply
	 * implement process(FilterContext, String) instead.
	 * <p>
	 */
	public Map processForStack(FilterContext ctx, String text)
		{
			process(ctx, text);
			return null;
		}

	/**
	 * This isn't abstract in order to work with processForContext.
	 * <p>
	 */
	public void process(FilterContext ctx, String text) {}

	public void onPush(FilterContext ctx) {  }
	public void onPop(FilterContext ctx) {  }
	public boolean shouldStack(FilterContext ctx, Map interim) { return true; }
} 
