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

import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.PostManager;

import org.iwethey.forums.web.ControllerAttributes;

import org.springframework.web.bind.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles general User URLs.
 * <p>
 * $Id: PostController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class PostController extends MultiActionController implements ControllerAttributes
{
	private PostManager mPostManager = null;

	/**
	 * Toggle expanded history settings.
	 * <p>
	 */
	public ModelAndView expandHistory(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			int id = RequestUtils.getRequiredIntParameter(request, "postid");
			Post post = getPostManager().getPostById(id);
			int index = RequestUtils.getRequiredIntParameter(request, "index");

			ExpandedHistory expanded = ExpandedHistory.getExpandedHistory(request);
			expanded.toggle(post, index);

			return new ModelAndView(new RedirectView(request.getHeader("referer")));
		}

	/**
	 * Expand all history entries.
	 * <p>
	 */
	public ModelAndView expandAllHistory(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			int id = RequestUtils.getRequiredIntParameter(request, "postid");
			Post post = getPostManager().getPostById(id);

			List entries = post.getEditHistory();
			if (entries != null)
				{
					ExpandedHistory expanded = ExpandedHistory.getExpandedHistory(request);

					for (int index=0; index < entries.size(); index++)
						{
							expanded.expand(post, index);
						}
				}

			return new ModelAndView(new RedirectView(request.getHeader("referer")));
		}

	/**
	 * Expand all history entries.
	 * <p>
	 */
	public ModelAndView collapseAllHistory(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			int id = RequestUtils.getRequiredIntParameter(request, "postid");
			Post post = getPostManager().getPostById(id);

			List entries = post.getEditHistory();
			if (entries != null)
				{
					ExpandedHistory expanded = ExpandedHistory.getExpandedHistory(request);

					for (int index=0; index < entries.size(); index++)
						{
							expanded.collapse(post, index);
						}
				}

			return new ModelAndView(new RedirectView(request.getHeader("referer")));
		}

	public void setPostManager(PostManager pm) { mPostManager = pm; }
	public PostManager getPostManager() { return mPostManager; }
}
