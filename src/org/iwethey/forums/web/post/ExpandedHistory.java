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

package org.iwethey.forums.web.post;

import org.iwethey.forums.web.ControllerAttributes;

import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.PostHistory;

import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Holds the expanded history items for this user for the session.
 * <p>
 * $Id: ExpandedHistory.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ExpandedHistory extends HashMap implements ControllerAttributes
{
	/**
	 * Retrieve the expanded history settings from the user's session.
	 * <p>
	 * @param request The HTTP request for this session.
	 */
	public static ExpandedHistory getExpandedHistory(HttpServletRequest request)
		{
			ExpandedHistory expanded = (ExpandedHistory) WebUtils.getSessionAttribute(request, EXPANDED_HISTORY_ATTRIBUTE);
			if (expanded == null)
				{
					expanded = new ExpandedHistory();
					WebUtils.setSessionAttribute(request, EXPANDED_HISTORY_ATTRIBUTE, expanded);
				}

			return expanded;
		}

	/**
	 * Set a history entry's state to 'expanded'.
	 * <p>
	 * @param post The post to which the history entry belongs.
	 * @param index The index of the entry to expand.
	 */
	public void expand(Post post, int index)
		{
			Set postExpanded = getPostExpanded(new Integer(post.getId()));

			postExpanded.add(new Integer(index));
		}

	/**
	 * Set a history entry's state to 'collapsed'.
	 * <p>
	 * @param post The post to which the history entry belongs.
	 * @param index The index of the entry to collapse.
	 */
	public void collapse(Post post, int index)
		{
			Integer id = new Integer(post.getId());
			Set postExpanded = getPostExpanded(id);

			checkRemove(postExpanded, id, new Integer(index));
		}

	/**
	 * Toggle a history entry's state between 'expanded' and 'collapsed'.
	 * <p>
	 * @param post The post to which the history entry belongs.
	 * @param index The index of the entry to toggle.
	 */
	public void toggle(Post post, int index)
		{
			Integer id = new Integer(post.getId());
			Set postExpanded = getPostExpanded(id);

			Integer idx = new Integer(index);
			if (postExpanded.contains(idx))
				{
					checkRemove(postExpanded, id, idx);
				}
			else
				{
					postExpanded.add(idx);
				}
		}

	/**
	 * Check a history entry's state of expansion.
	 * <p>
	 * @param post The post to which the history entry belongs.
	 * @param index The index of the entry to check.
	 */
	public boolean isExpanded(Post post, int index)
		{
			Set postExpanded = getPostExpanded(new Integer(post.getId()));

			return postExpanded.contains(new Integer(index));
		}

	/**
	 * Get the set of expanded history entries for a particular post.
	 * <p>
	 * @param id The post ID to retrieve the entries for.
	 */
	private Set getPostExpanded(Integer id)
		{
			Set postExpanded = (Set) get(id);

			if (postExpanded == null)
				{
					postExpanded = new HashSet();
					put(id, postExpanded);
				}
			
			return postExpanded;
		}

	/**
	 * Remove an entry from the set of expanded entries. If there are
	 * no more expanded entries for this post, remove the set completely
	 * to clean it up.
	 * <p>
	 * @param postExpanded The set of currently expanded history entries.
	 * @param id The ID of the post being checked.
	 * @param index The index of the history entry to remove. 
	 */
	private void checkRemove(Set postExpanded, Integer id, Integer index)
		{
			postExpanded.remove(index);

			if (postExpanded.size() == 0)
				remove(id);
		}
}
