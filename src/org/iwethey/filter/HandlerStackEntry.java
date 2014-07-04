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
 * Holds the position of a handler in the stack.
 * <p>
 * $Id: HandlerStackEntry.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HandlerStackEntry
{
	private StackableHandler mHandler = null;
	private Map mInterim = null;
	private int mPosition = 0;

	public HandlerStackEntry(StackableHandler handler, Map interim)
		{
			if (handler == null)
				{
					throw new FilterException("Handler may not be null.");
				}

			mHandler = handler;
			mInterim = interim;
		}

	public HandlerStackEntry(StackableHandler handler)
		{
			this(handler, null);
		}

	public HandlerStackEntry(StackableHandler handler, Map ctx, int pos)
		{
			this(handler, ctx);
			mPosition = pos;
		}

	public HandlerStackEntry(StackableHandler handler, int pos)
		{
			this(handler, null, pos);
		}

	public StackableHandler getHandler() { return mHandler; }

	public Map getInterim() { return mInterim; }

	public void setPosition(int pos)
		{
			mPosition = pos;
		}
	public int getPosition() { return mPosition; }

	protected void onPop(FilterContext ctx) {}

	final void onPopInternal(FilterContext ctx)
		{
			onPop(ctx);

			mHandler.onPop(ctx);
		}

}

