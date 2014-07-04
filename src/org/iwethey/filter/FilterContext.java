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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Holds the filter results as the filter is occurring.
 * <p>
 * $Id: FilterContext.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class FilterContext
{
	private String mInput = null;
	private Matcher mMatcher = null;
	private Filter mFilter = null;
	private boolean mInsensitive = false;
	private int mLastEnd = 0;
	private int mMatchPos = 0;

	private final StringBuffer mResults = new StringBuffer();
	private final Stack mHandlers = new Stack();
	private final Stack mFilters = new Stack();
	private final HashMap mMatcherCache = new HashMap();

	private String mMatched = null;
	private String mSkipped = null;
	private String mRemaining = null;

	private int mLine;

	public FilterContext() {}

	public FilterContext(String str, Filter filter)
		{
			setup(str, filter);
		}

	private void setup(String str, Filter filter)
		{
			mInput = str;
			pushFilter(filter);
		}

	public FilterResults filter(String str, Filter filter)
		{
			setup(str, filter);
			return filter();
		}

	public FilterResults filter()
		{
			FilterResults ret = null;

			if (mInput == null)
				{
					ret = new FilterResults("");
				}
			else
				{
					mLine = 1;

					while (findMore())
						{
							mFilter.getPlaintextHandler().process(this, mSkipped);

							Handler hand = mFilter.getHandler(mInsensitive ? mMatched.toLowerCase() : mMatched);

							if (hand != null)
								{
									if (hand instanceof StackableHandler)
										{
											StackableHandler shand = (StackableHandler) hand;
											Map interim = shand.processForStack(this, mMatched);
											if (shand.shouldStack(this, interim))
												{
													pushHandler(shand, interim);
												}
										}
									else
										{
											hand.process(this, mMatched);
										}
								}
							else
								{
									// No handler matched, which meant it was just being used as a
									// filter, not a processor.
									addResults(mMatched);
								}
						}

					mFilter.getPlaintextHandler().process(this, mSkipped);

					ret = getResults();
				}

			return ret;
		}

	public boolean findMore()
		{
			boolean matched = mMatcher.find(mLastEnd);

			if (matched)
				{
					mSkipped = mInput.substring(mLastEnd, mMatcher.start());

					mMatchPos = mMatcher.start();
					mLastEnd = mMatcher.end();
					mMatched = mMatcher.group();

					mRemaining = mInput.substring(mMatcher.end());
				}
			else
				{
					mSkipped = mInput.substring(mLastEnd);
					if (mSkipped.length() > 0)
						{
							mMatched = "";
						}
					mRemaining = mSkipped;
				}

			return matched;
		}


	public int getMatchPosition()
		{
			return mMatchPos;
		}

	public String getMatched()
		{
			return mMatched;
		}

	public String getSkipped()
		{
			return mSkipped;
		}

	public String getRemaining()
		{
			return mRemaining;
		}

	public void addResults(String add)
		{
			mResults.append(add);
		}

	public void abortResults()
		{
			mFilter.getPlaintextHandler().process(this, mMatched);
		}

	public void skip(int amount)
		{
			mLastEnd += amount;
		}

	public void incrementLine() { mLine++; }
	public void incrementLine(int amt) { mLine += amt; }

	public int getLine() { return mLine; }

	public FilterResults getResults()
		{
			return new FilterResults(mResults.toString());
		}

	public void pushHandler(HandlerStackEntry entry)
		{
			entry.setPosition(getMatchPosition());
			mHandlers.push(entry);
			entry.getHandler().onPush(this);
		}

	public void pushHandler(StackableHandler handler)
		{
			pushHandler(handler, null);
		}

	public void pushHandler(StackableHandler handler, Map interim)
		{
			pushHandler(new HandlerStackEntry(handler, interim, getMatchPosition()));
		}

	public StackableHandler popHandler()
		{
			HandlerStackEntry entry = popHandlerEntry();
			return entry.getHandler();
		}

	public HandlerStackEntry popHandlerEntry()
		{
			if (mHandlers.isEmpty())
				{
					throw new FilterException("Mismatched tag.");
				}

			HandlerStackEntry entry = (HandlerStackEntry) mHandlers.pop();
			entry.onPopInternal(this);

			return entry;
		}

	public StackableHandler peekHandler()
		{
			if (mHandlers.isEmpty())
				{
					throw new FilterException("Handler stack is empty.");
				}

			HandlerStackEntry entry = (HandlerStackEntry) mHandlers.peek();
			return entry.getHandler();
		}

	private void setFilter(Filter filter)
		{
			mFilter = filter;
			mMatcher = filter.getMatcher(mInput);
			mInsensitive = ((mMatcher.pattern().flags() & Pattern.CASE_INSENSITIVE) != 0);
		}

	public void pushFilter(Filter filter)
		{
			mFilters.push(filter);

			setFilter(filter);
			mMatcherCache.put(filter, mMatcher);
		}

	public Filter popFilter()
		{
			if (mFilters.isEmpty())
				{
					throw new FilterException("Filter stack is empty.");
				}

			Filter old = (Filter) mFilters.pop();

			setFilter(peekFilter());

			return old;
		}

	public Filter peekFilter()
		{
			if (mFilters.isEmpty())
				{
					throw new FilterException("Filter stack is empty.");
				}

			return (Filter) mFilters.peek();
		}
}

