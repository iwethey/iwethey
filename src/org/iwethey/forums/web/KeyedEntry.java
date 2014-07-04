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
 * Holds the information for a format-keyed entry, including
 * the label key and any args for the format string.
 * <p>
 * The message string represented by the label key can include
 * parameters. These are set using the addArg method.
 * <p>
 * $Id: KeyedEntry.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class KeyedEntry
{
	/** The key to use for the messages store. */
	private String label = null;

	/** A list of the arguments for the message string, if any. */
	private List args = null;

	/**
	 * Blank constructor for bean use.
	 * <p>
	 */
	public KeyedEntry() { };

	/**
	 * Construct a new entry with the given label key.
	 * <p>
	 * @param label The key to use for looking up the label for this link in the message store.
	 */
	public KeyedEntry(String label)
	{
		this.label = label;
	};
   
	/**
	 * Add an argument for substitution into the message string
	 * indicated by the label.
	 * <p>
	 * @param arg The object to insert into the message string.
	 */
	public void addArg(Object arg)
	{
		if (args == null)
		{
			args = new ArrayList();
		}

		args.add(arg);
	}

	public List getArgs() { return args; }
	public void setArgs(List args) { this.args = args; }

	public void setLabel(String label) { this.label = label; }
	public String getLabel() { return label; }
}
