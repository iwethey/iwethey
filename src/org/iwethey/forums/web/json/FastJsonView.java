/**
   Copyright 2004-2010 Scott Anderson and Mike Vitale

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

package org.iwethey.forums.web.json;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.Map;

/**
 * <p>Simple view to return a JSON value and possibly some text.</p>
 *
 * <p>@author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)</p>
 */
public class FastJsonView extends AbstractView
{
	private static final Logger logger = Logger.getLogger(FastJsonView.class);

	public FastJsonView() {}

	public void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
		throws ServletException
	{
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());

			Object data = model.get("json");

			Object serTmp = model.get("serializer");
			JacksonSerializer ser = null;
			if (serTmp != null && JacksonSerializer.class.isAssignableFrom(serTmp.getClass()))
			{
				ser = (JacksonSerializer) serTmp;
			}
			else
			{
				ser = new JacksonSerializer();
			}

			if (data != null)
			{
				ser.serialize(data, out);
			}

			out.flush();
		}
		catch (IOException ie)
		{
			throw new ServletException(ie);
		}
		catch (JsonException e)
		{
			throw new ServletException(e);
		}
	}
}
