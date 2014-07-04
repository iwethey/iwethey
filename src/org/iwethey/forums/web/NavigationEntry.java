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

package org.iwethey.forums.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the information for a navigation bar entry, including
 * the label key, the uri for the link, and whether this entry
 * represents the current page or not.
 * <p>
 * The message string represented by the label key can include
 * parameters. These are set using the addArg method.
 * <p>
 * $Id: NavigationEntry.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NavigationEntry extends KeyedEntry
{
	/** The uri to use for the href of the link. */
	private String mUri = null;

	/** If this entry represents the current page, true. */
	private boolean mCurrent = false;

	/**
	 * Blank constructor for bean use.
	 * <p>
	 */
	public NavigationEntry() { };

	/**
	 * Construct a new entry with the given label key and uri.
	 * <p>
	 * @param label The key to use for looking up the label for this link in the message store.
	 * @param uri The uri to place in the href field of the link.
	 */
	public NavigationEntry(String label, String uri)
		{
			this(label, uri, false);
		};
   
	/**
	 * Construct a new entry with the given label key and uri, and
	 * indicating if this entry represents the current page or not.
	 * <p>
	 * @param label The key to use for looking up the label for this link in the message store.
	 * @param uri The uri to place in the href field of the link.
	 * @param current If true, this entry represents the current page being rendered.
	 */
	public NavigationEntry(String label, String uri, boolean current)
		{
			super(label);

			mUri = uri;
			mCurrent = current;
		};

	public void setUri(String uri) { mUri = uri; }
	public String getUri() { return mUri; }

	public void setCurrent() { mCurrent = true; }
	public boolean isCurrent() { return mCurrent; }
}
