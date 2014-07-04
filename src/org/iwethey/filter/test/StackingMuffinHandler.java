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
 * Stacks muffins. Oh, and it also exercises the filter stacking
 * and so on.
 * <p>
 * $Id: StackingMuffinHandler.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class StackingMuffinHandler extends AbstractFilterStackingHandler
{
	private String mDog = null;

	public StackingMuffinHandler()
		{
			this("poodle");
		}

	public StackingMuffinHandler(String dog)
		{
			super("muffin");

			mDog = dog;

			Filter f = new Filter(true);

			// This lets the popping handler check to make sure the right filter is popped.
			f.addHandler(new PoppingBlancmangeHandler(f, this));

			setStackedFilter(f);
		}

	public Map processForStack(FilterContext ctx, String text)
		{
			ctx.addResults(text + "s are cosmic!");
			Map ret = new HashMap();
			ret.put("foodle", "is a poodle");
			return ret;
		}

	public void onPop(FilterContext ctx)
		{
			super.onPop(ctx);

			Filter peeked = ctx.peekFilter();
			if (peeked == getStackedFilter())
				{
					throw new FilterException("Peeked filter is the wrong one.");
				}
		}
} 
