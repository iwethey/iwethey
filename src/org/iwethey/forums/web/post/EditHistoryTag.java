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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Iterates a post's edit history in proper order. Sets the following
 * variables in the page context for each history entry based on the session.
 * <ul>
 * <li>hist - The history entry</li>
 * <li>histIndex - The index of the history entry</li>
 * <li>expanded - Whether the history entry is expanded or not</li>
 * </ul>
 * Also sets the following variables based on whether collapsed or
 * expanded entries were found:
 * <ul>
 * <li>someExpanded - true if some expanded entries were found</li>
 * <li>someCollapsed - true if some collapsed entries were found</li>
 * </ul>
 * <p>
 * $Id: EditHistoryTag.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class EditHistoryTag extends TagSupport implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	private String mPostVar = "post";
	private ListIterator mHistoryEntries = null;

	private boolean mCollapsed = false;
	private boolean mExpanded = false;

	public void setVar(String var) { mPostVar = var; }
	public String getVar() { return mPostVar; }

	public int doStartTag()
		{
			mCollapsed = false;
			mExpanded = false;

			return iterate();
		}

	public int doAfterBody()
		{
			return iterate();
		}
	
	/**
	 * Iterate the history entries in reverse order. Indicate if
	 * the entry is expanded or collapsed.
	 * <p>
	 */
	private int iterate()
		{
			Post post = (Post) pageContext.findAttribute(mPostVar);

			if (mHistoryEntries == null)
				{
					if (post == null)
						return SKIP_BODY;

					List entries = post.getEditHistory();
					if (entries == null)
						return SKIP_BODY;

					mHistoryEntries = entries.listIterator(entries.size());
				}

			if (mHistoryEntries.hasPrevious())
				{
					int index = mHistoryEntries.previousIndex();
					PostHistory hist = (PostHistory) mHistoryEntries.previous();

					HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

					ExpandedHistory expanded = ExpandedHistory.getExpandedHistory(request);

					boolean isExpanded = expanded.isExpanded(post, index);
					if (isExpanded)
						mExpanded = true;
					else
						mCollapsed = true;

					pageContext.setAttribute("hist", hist);
					pageContext.setAttribute("expanded", new Boolean(isExpanded));
					pageContext.setAttribute("histIndex", new Integer(index));

					return EVAL_BODY_AGAIN;
				}
			else
				{
					pageContext.setAttribute("someCollapsed", new Boolean(mCollapsed));
					pageContext.setAttribute("someExpanded", new Boolean(mExpanded));
						
					mHistoryEntries = null;

					return SKIP_BODY;
				}
		}

	public int doEndTag()
		throws JspException
		{
			mPostVar = "post";
			mHistoryEntries = null;

			mCollapsed = false;
			mExpanded = false;

			return super.doEndTag();
		}
}
