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

import org.iwethey.forums.domain.BoardManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the model for the main page.
 * <p>
 * $Id: MainController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class MainController extends AbstractController
{
    protected final Log logger = LogFactory.getLog(getClass());

	private BoardManager boardManager = null;

	/**
	 *  
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			HashMap model = new HashMap();
			
			model.put("boards", boardManager.getBoardSummary());

			return new ModelAndView("main", model);
		}

	public void setBoardManager(BoardManager mgr) { boardManager = mgr; }
	public BoardManager getBoardManager() { return boardManager; }
}
