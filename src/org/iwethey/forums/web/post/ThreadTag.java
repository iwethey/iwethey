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

import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.User;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

/**
 * Iterates a thread. Sets the 'post' and 'postIsNew' variables
 * in the page context.
 * <p>
 * $Id: ThreadTag.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class ThreadTag extends TagSupport
{
	private String threadVar = "thread";
	private Post thread = null;
	private Iterator posts = null;
	private Date mark = null;
	private boolean odd = true;
	private boolean content = false;

	private static final Logger logger = Logger.getLogger(ThreadTag.class);

	public void setVar(String var) { this.threadVar = var; }
	public String getVar() { return threadVar; }

	public void setContent(boolean content) { this.content = content; }
	public boolean getContent() { return content; }

	/**
	 * Start the iteration with the thread parent itself.
	 */
	@Override
	public int doStartTag()
	{
		thread = (Post) pageContext.findAttribute(threadVar);
		Forum forum = (Forum) pageContext.findAttribute("forum");
		User user = (User) pageContext.findAttribute("user");

		if (thread == null)
		{
			return SKIP_BODY;
		}

		boolean isNew = false;

		mark = user.getForumMark(forum);
		Date threadUpdate = thread.getLastUpdated();

		isNew = mark == null
			|| threadUpdate == null
			|| mark.compareTo(threadUpdate) < 0
			|| (thread.getThread().getId() == thread.getId()
				&& (!content)
				&& user.getShowThreadsCollapsed()
				&& mark.compareTo(thread.getChildrenLastUpdated()) < 0
				);

		odd = true;

		pageContext.setAttribute("odd", odd ? "odd" : "even");
		pageContext.setAttribute("even", odd ? "even" : "odd");
		pageContext.setAttribute("post", thread);
		pageContext.setAttribute("postIsNew", new Boolean(isNew));

		return EVAL_BODY_AGAIN;
	}

	/**
	 * Continue the iteration with each reply to the thread in turn.
	 */
	@Override
	public int doAfterBody()
	{
		User user = (User) pageContext.findAttribute("user");

		if (content && user.getShowRepliesCollapsed())
		{
			return SKIP_BODY;
		}

		if (posts == null)
		{
			List children = thread.getChildren();
			//logger.info("children " + children);

			if (children == null)
			{
				return SKIP_BODY;
			}
			posts = children.iterator();
		}

		if (posts.hasNext())
		{
			Post post = (Post) posts.next();
			Forum forum = (Forum) pageContext.findAttribute("forum");

			boolean isNew = false;

			isNew = (mark == null) || (mark.compareTo(post.getLastUpdated()) < 0);

			odd = !odd;
			pageContext.setAttribute("odd", odd ? "odd" : "even");
			pageContext.setAttribute("even", odd ? "even" : "odd");
			pageContext.setAttribute("postIsNew", new Boolean(isNew));
			pageContext.setAttribute("post", post);

			return EVAL_BODY_AGAIN;
		}
		else
		{
			posts = null;
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() throws JspException
	{
		threadVar = "thread";
		thread = null;
		posts = null;
		mark = null;
		odd = true;
		content = false;

		return super.doEndTag();
	}
}
