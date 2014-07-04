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
 * Handler that stacks a new filter on the context when pushed, and
 * pops the filter when popped.
 * <p>
 * $Id: AbstractFilterStackingHandler.java 47 2004-12-05 13:11:48Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class AbstractFilterStackingHandler extends AbstractStackableHandler
{
	private Filter mStackedFilter;

	public AbstractFilterStackingHandler(Filter stacked)
		{
			this(null, stacked);
		}

	public AbstractFilterStackingHandler(String key, Filter stacked)
		{
			setKey(key);
			setStackedFilter(stacked);
		}

	public AbstractFilterStackingHandler(String key)
		{
			this(key, null);
		}

	public void setStackedFilter(Filter stacked)
		{
			mStackedFilter = stacked;
		}

	public Filter getStackedFilter()
		{
			return mStackedFilter;
		}

	public void onPush(FilterContext ctx)
		{
			if (mStackedFilter != null)
				{
					ctx.pushFilter(mStackedFilter);
				}
		}

	public void onPop(FilterContext ctx)
		{
			if (mStackedFilter != null)
				{
					if (mStackedFilter != ctx.popFilter())
						{
							throw new FilterException("Popped filter does not match the filter this instance pushed.");
						}
				}
		}
} 
