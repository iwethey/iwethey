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

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * Creates the URL for various operations.
 * <p>
 * $Id: URLTag.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public abstract class URLTag extends TagSupport
{
	private String mVar = getVarDefault();

	public void setVar(String var) { mVar = var; }
	public String getVar() { return mVar; }

	public abstract String getVarDefault();
	public abstract String retrievePartial(Object var);

	public int doStartTag()
		throws JspTagException, JspException
		{
			Object val = null;

			if (mVar.indexOf("$") > -1)
				{
					val = ExpressionEvaluatorManager.evaluate("thing", mVar, Object.class, this, pageContext);
				}
			else
				{
					val = pageContext.findAttribute(mVar);
				}

			String partial = retrievePartial(val);

			HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse resp = (HttpServletResponse) pageContext.getResponse();

			try
				{
					pageContext.getOut().print(resp.encodeURL(req.getContextPath() + partial));
				}
			catch (IOException e)
				{
					throw new JspTagException("URLTag: " + e.getMessage());
				}

			return SKIP_BODY;
		}

	public int doEndTag()
		{
			mVar = getVarDefault();
			return EVAL_PAGE;
		}
}
