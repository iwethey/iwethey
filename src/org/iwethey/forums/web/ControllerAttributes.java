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

/**
 * Holds system-wide model names.
 * <p>
 * $Id: ControllerAttributes.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface ControllerAttributes
{
	/** Key for the current user object in the session and model. */
	public static final String USER_ATTRIBUTE = "user";

	public static final String USER_ID_ATTRIBUTE = "user_id";

	public static final String LAST_URI_ATTRIBUTE = "last_uri";

	public static final String EXPANDED_HISTORY_ATTRIBUTE = "expanded.history";

	public static final String HEADER_IMAGE_ATTRIBUTE = "header_image";

	public static final int PAGE_BREAK = 6;
	public static final int PAGER_SIZE = 20;
}
