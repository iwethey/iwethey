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

package org.iwethey.forums.web.forum;

import org.iwethey.forums.web.URLTag;

import org.iwethey.forums.domain.Forum;

/**
 * Creates the URL for marking a forum read.
 * <p>
 * $Id: MarkReadURLTag.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class MarkReadURLTag extends URLTag
{
	public String getVarDefault()
		{
			return "forum";
		}

	public String retrievePartial(Object forum)
		{
			return ForumController.markReadURI((Forum) forum);
		}
}
