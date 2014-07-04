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
 * Handles a filterable item in the text stream. Keyed by the pattern
 * the handler matches.
 * <p>
 * $Id: AbstractKeyedHandler.java 45 2004-12-04 23:15:56Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class AbstractKeyedHandler extends AbstractHandler implements KeyedHandler
{
	private String mKey = null;
	private String mPattern = null;

	public String getKey() { return mKey; }
	public void setKey(String key) { mKey = key; }

	public void setPattern(String pat) { mPattern = pat; }
	public String getPattern()
		{
			return mPattern == null ? getKey() : mPattern;
		}

	public boolean isEscapedPattern()
		{
			return false;
		}
} 
