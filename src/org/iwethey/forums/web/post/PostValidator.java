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

import org.iwethey.filter.ConstantHandler;
import org.iwethey.filter.Filter;
import org.iwethey.filter.FilterException;
import org.iwethey.filter.FilterResults;
import org.iwethey.filter.Handler;

import org.iwethey.filter.html.HtmlEscapeHandler;
import org.iwethey.filter.html.HtmlHelper;
import org.iwethey.filter.html.NewlineHandler;
import org.iwethey.filter.html.PassthroughEscapeHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iwethey.forums.domain.Post;
import org.iwethey.forums.domain.User;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Validates the information from the new/edit post form.
 * <p>
 * $Id: PostValidator.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class PostValidator implements Validator
{
	Map mFilters = new HashMap();
	Handler mPassthroughHandler = new PassthroughEscapeHandler();

	public PostValidator()
		{
		}

	private Filter getFilter(Post post)
		{
			boolean newlines = post.isConvertNewlines();
			boolean html = post.isConvertHtml();
			boolean links = post.isConvertLinks();
			boolean codes = post.isConvertCodes();

			String key = "" + newlines + html + links + codes;
			Filter filter = (Filter) mFilters.get(key);

			if (filter == null)
				{
					filter = new Filter();

					// Standard handlers
					filter.addHandler(new HtmlEscapeHandler());

					if (newlines)
						{
							filter.addHandler(new NewlineHandler());
						}

					if (html)
						{
							HtmlHelper.addCommonInlineConstantTags(filter);
							HtmlHelper.addCommonBlockConstantTags(filter);
						}

					if (links)
						{
							HtmlHelper.addRawUrlHandlers(filter);
						}

					if (codes)
						{
						}

					filter.setPlaintextHandler(mPassthroughHandler);

				}

			return filter;
		}

	/**
	 * Checks objects for form backing compatibility. Only supports Post objects.
	 * <p>
	 */
    public boolean supports(Class cl)
		{
			return Post.class.isAssignableFrom(cl);
		}

	/**
	 * Validate the information on the form.
	 * <p>
	 * @param obj The form backing object to validate.
	 * @param errors The errors object for error messages.
	 */
    public void validate(Object obj, Errors errors)
		{
			Post p = (Post) obj;

			if (p != null)
				{
					String subject = p.getSubject();
					if (subject == null || subject.equals(""))
					{
						errors.rejectValue("subject", "error.post.subject.empty", null, "Subject required.");
					}

					Filter filter = getFilter(p);

					FilterResults res = null;

					try
					{
						res = filter.filter(p.getOriginalContent());
						p.setContent(res.getText());
					}
					catch (FilterException fe)
					{
						errors.rejectValue("originalContent", "error.filter.improper.html");
					}

					try
					{
						res = filter.filter(p.getOriginalSignature());
						p.setSignature(res.getText());
					}
					catch (FilterException fe)
					{
						errors.rejectValue("originalSignature", "error.filter.improper.html");
					}
				}

        }
}
