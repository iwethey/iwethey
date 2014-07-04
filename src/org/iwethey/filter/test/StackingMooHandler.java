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

/**
 * Counts newlines.
 * <p>
 * $Id: StackingMooHandler.java 44 2004-12-03 23:30:47Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class StackingMooHandler extends AbstractStackableHandler
{
	public StackingMooHandler()
		{
			setKey("moo");
		}

	public void process(FilterContext ctx, String text)
		{
			ctx.addResults(text + " on you");
		}

	public void onPush(FilterContext ctx)
		{
			StackableHandler moo = ctx.peekHandler();
			if (moo != this)
				{
					throw new FilterException("Peeking moo filter not on stack.");
				}

			moo = ctx.popHandler();
			if (moo != this)
				{
					throw new FilterException("Popping moo filter not on stack.");
				}

			super.onPush(ctx);
		}
} 
