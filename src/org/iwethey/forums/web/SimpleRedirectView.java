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

import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple object to redirect to a page without including query information
 * like the Spring RedirectView does.
 * <p>
 * $Id: SimpleRedirectView.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class SimpleRedirectView extends RedirectView
{
	/**
	 * Empty constructor for bean use.
	 * <p>
	 */
	public SimpleRedirectView() {}

	/**
	 * Construct a new redirect view to the given URL.
	 * <p>
	 * @param url The URL to redirect the browser to.
	 */
	public SimpleRedirectView(String url)
		{
			super(url);
		}

	/**
	 * Construct a new redirect view to the given relative URL.
	 * <p>
	 * @param url The URL to redirect the browser to.
	 * @param contextRelative If true, the URL is treated as a relative URI.
	 */
	public SimpleRedirectView(String url, boolean contextRelative)
		{
			super(url, contextRelative);
		}

	/**
	 * Overrides the superclass method to return a blank map instead
	 * of the query parameters.
	 * <p>
	 * @param model The current model.
	 * @return An empty HashMap.
	 */
	public Map queryProperties(Map model)
		{
			return new HashMap();
		}
}

