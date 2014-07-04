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

package org.iwethey.filter.test;

import org.iwethey.filter.*;

import java.util.*;

/**
 * Checks the filter and handler being popped; tests the
 * AbstractMatchingPoppingHandler.
 * <p>
 * $Id: PoppingBlancmangeHandler.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class PoppingBlancmangeHandler extends AbstractMatchingPoppingHandler
{
	Filter mStackedFilter = null;
	StackableHandler mStackedHandler = null;

	public PoppingBlancmangeHandler(Filter filt, StackableHandler hand)
		{
			super("blancmange", hand.getKey());
			mStackedFilter = filt;
			mStackedHandler = hand;
		}

	protected void processInternal(FilterContext ctx, String text)
		{
			ctx.addResults(text + "s are on the Hertzprung-Russel chart! Next to 'M'!");

			Filter peeked = ctx.peekFilter();
			if (peeked != mStackedFilter)
				{
					throw new FilterException("Peeked filter is the wrong one.");
				}

			StackableHandler hand = ctx.peekHandler();
			if (hand != mStackedHandler)
				{
					throw new FilterException("Peeked handler is the wrong one.");
				}
		}

	protected void processPopped(FilterContext ctx, HandlerStackEntry entry)
		{
			Map m = entry.getInterim();
			if (!(m.get("foodle").equals("is a poodle")))
				{
					throw new FilterException("Foodle isn't a poodle.");
				}
		}
} 
