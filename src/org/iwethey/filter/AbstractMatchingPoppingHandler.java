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
 * Handler that pops a matching handler from the context. Useful for
 * tags with closing tags.
 * <p>
 * $Id: AbstractMatchingPoppingHandler.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class AbstractMatchingPoppingHandler extends AbstractKeyedHandler
{
	private String mMatchingKey;

	public AbstractMatchingPoppingHandler(String key, String matching)
		{
			setKey(key);
			setMatchingKey(matching);
		}

	public void setMatchingKey(String matching)
		{
			mMatchingKey = matching;
		}

	public String getMatchingKey()
		{
			return mMatchingKey;
		}

	protected abstract void processInternal(FilterContext ctx, String text);

	protected void processPopped(FilterContext ctx, HandlerStackEntry entry) {}

	public final void process(FilterContext ctx, String text)
		{
			processInternal(ctx, text);

			HandlerStackEntry entry = ctx.popHandlerEntry();
			StackableHandler popped = entry.getHandler();

			if (!mMatchingKey.equals(popped.getKey()))
				{
					throw new FilterException("Popped handler does not match the key for this handler: got \"" + popped.getKey() + "\" but was expecting \"" + mMatchingKey + "\".");
				}

			processPopped(ctx, entry);
		}
} 
