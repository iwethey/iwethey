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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Contains a group of handlers to apply.
 * <p>
 * $Id: Filter.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class Filter
{
	private final Map mHandlers = new HashMap();

	private Handler mPlaintext = new PassthroughHandler();

	private ArrayList mKeys = new ArrayList();

	private Pattern mTags = null;

	private boolean mInsensitive = true;

	/** List of behaviors customizing this filter, such as "close block elements", etc.
	 * Behaviors are used by handlers to customize their internal behavior.
	 */
	private HashSet mBehaviors = null;

	/** List of traits to filter handlers, such as "html". */
	private HashSet mTraits = null;

	public Filter()
		{
		}

	public Filter(boolean insensitive)
		{
			mInsensitive = insensitive;
		}

	public Filter(Collection handlers)
		{
			addHandlers(handlers);
		}

	public Filter(Collection handlers, boolean insensitive)
		{
			this(insensitive);
			addHandlers(handlers);
		}

	public void setPlaintextHandler(Handler hand)
		{
			mPlaintext = hand;
		}

	public Handler getPlaintextHandler()
		{
			return mPlaintext;
		}

	/**
	 * Filter a string with this set of handlers.
	 * <p>
	 * @param str The string to filter with these handlers.
	 */
	public FilterResults filter(String str)
		{
			return (new FilterContext(str, this)).filter();
		}

	public KeyedHandler getHandler(String matched)
		{
			return (KeyedHandler) mHandlers.get(matched);
		}

	public Matcher getMatcher(String str)
		{
			if (mTags == null)
				{
					compilePattern();
				}

			return mTags.matcher(str);
		}

	public final void addHandler(KeyedHandler hand)
		{
			String key = hand.getKey();
			mKeys.add(key);
			mHandlers.put((mInsensitive ? key.toLowerCase() : key), hand);
		}

	public final void addHandlers(Collection handlers)
		{
			for (Iterator i = handlers.iterator(); i.hasNext(); )
				{
					addHandler((KeyedHandler) i.next());
				}
		}

	public final void addHandlers(Collection handlers, boolean insensitive)
		{
			mInsensitive = insensitive;
			addHandlers(handlers);
		}

	private void compilePattern()
		{
			Collections.sort(mKeys);
			Collections.reverse(mKeys);

			Pattern escaper = Pattern.compile("([^a-zA-Z0-9])");

			StringBuffer patBuf = new StringBuffer();
			for (Iterator i = mKeys.iterator(); i.hasNext(); )
				{
					String key = (String) i.next();

					KeyedHandler hand = (KeyedHandler) mHandlers.get(key);
					hand.postRegister(this);

					String pattern = hand.getPattern();

					if (hand.isEscapedPattern())
						{
							patBuf.append(pattern);
						}
					else
						{
							patBuf.append(escaper.matcher(pattern).replaceAll("\\\\$1"));
						}

					if (i.hasNext())
						{
							patBuf.append("|");
						}
				}

			mTags = Pattern.compile(patBuf.toString(), mInsensitive ? Pattern.CASE_INSENSITIVE : 0);
		}
} 
