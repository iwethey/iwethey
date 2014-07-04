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

import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.User;

import java.io.IOException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

/**
 * Iterates a list of forums. Sets the 'forum' and 'forumIsNew' variables
 * in the page context.
 * <p>
 * $Id: ForumNewTag.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ForumNewTag extends TagSupport
{
	private String mForumVar = "forum";

	public void setVar(String var) { mForumVar = var; }
	public String getVar() { return mForumVar; }

	public int doStartTag()
		{
			Forum forum = (Forum) pageContext.findAttribute(mForumVar);
			User user = (User) pageContext.findAttribute("user");

			boolean isNew = forum.isNew(user);

			if (forum == null
				|| (user.getShowNewForumsOnly() && !isNew))
				return SKIP_BODY;

			pageContext.setAttribute("forumIsNew", new Boolean(isNew));

			return EVAL_BODY_AGAIN;
		}

	public int doEndTag()
		throws JspException
		{
			mForumVar = "forum";
			return super.doEndTag();
		}
}
