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
 * Holds the information for a select list entry, including
 * the label key, the value of the entry, and whether this entry
 * represents the current page or not.
 * <p>
 * The message string represented by the label key can include
 * parameters. These are set using the addArg method.
 * <p>
 * $Id: SelectEntry.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class SelectEntry
{
	/** The key to use for the messages store. */
	private String mLabel = null;

	/** The value to place in the dropdown. */
	private String mValue = null;

	/** A list of the arguments for the message string, if any. */
	private List mArgs = null;

	/** If true, translate the label using localization. */
	private boolean mTranslate = true;

	/**
	 * Blank constructor for bean use.
	 * <p>
	 */
	public SelectEntry() { };

	/**
	 * Construct a new entry with the given label key and value.
	 * <p>
	 * @param label The key to use for looking up the label for this link in the message store.
	 * @param value The value to place in the dropdown list.
	 */
	public SelectEntry(String label, String value)
		{
			mLabel = label;
			mValue = value;
		};
   
	/**
	 * Construct a new entry with the given label key, value, and translate flag.
	 * <p>
	 * @param label The key to use for looking up the label for this link in the message store.
	 * @param value The value to place in the dropdown list.
	 * @param translate If true, translate the label via locale.
	 */
	public SelectEntry(String label, String value, boolean translate)
		{
			this(label, value);
			mTranslate = translate;
		};
   
	/**
	 * Add an argument for substitution into the message string
	 * indicated by the label.
	 * <p>
	 * @param arg The object to insert into the message string.
	 */
	public void addArg(Object arg)
		{
			if (mArgs == null)
				mArgs = new ArrayList();

			mArgs.add(arg);
		}

	/**
	 * Retrieve the arguments to be substituted into the message string.
	 * <p>
	 * @return A List of Objects for use in the message string.
	 */
	public List getArgs() { return mArgs; }

	public void setLabel(String label) { mLabel = label; }
	public String getLabel() { return mLabel; }

	public void setValue(String value) { mValue = value; }
	public String getValue() { return mValue; }

	public void setTranslate(boolean translate) { mTranslate = translate; }
	public boolean getTranslate() { return mTranslate; }
}
