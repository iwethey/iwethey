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

package org.iwethey.filter.html;

import java.util.Map;

/**
 * Formats a tag with attributes.
 * <p>
 * $Id: SimpleConstantTagHandler.java 48 2004-12-05 16:49:09Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class StandardAttributeTagFormatter implements AttributeTagFormatter
{
	private String mTag = null;

	public StandardAttributeTagFormatter(String tag)
		{
			mTag = tag;
		}

	public String format(Map attributes)
		{
			return "";
		}
} 
