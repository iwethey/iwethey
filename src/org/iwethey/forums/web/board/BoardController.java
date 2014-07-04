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

package org.iwethey.forums.web.board;

import org.iwethey.forums.domain.Board;
import org.iwethey.forums.domain.BoardManager;
import org.iwethey.forums.domain.Category;
import org.iwethey.forums.domain.Forum;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;

import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.KeyedEntry;
import org.iwethey.forums.web.NavigationEntry;
import org.iwethey.forums.web.SelectEntry;

import org.iwethey.forums.web.user.UserController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.RequestUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the model for displaying a board.
 * <p>
 * $Id: BoardController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class BoardController extends MultiActionController implements ControllerAttributes
{
    protected final Log logger = LogFactory.getLog(getClass());

	private BoardManager boardManager = null;
	private UserManager userManager = null;

	public static NavigationEntry createNavigationEntry(Board board)
	{
		NavigationEntry nav = new NavigationEntry("show.board", "/board/show.iwt?boardid=" + board.getId(), true);
		nav.addArg(board.getDisplayName());
		nav.addArg(board.getDescription());

		return nav;
	}

	/**
	 * Get a list of SelectEntry objects for all boards.
	 * <p>
	 * @return A list of SelectEntry objects with the allowed values inside.
	 */
	public List boardSelectList()
	{
		List selects = new ArrayList();

		List boards = boardManager.getBoards();

		Iterator iter = boards.iterator();
		while (iter.hasNext())
		{
			Board board = (Board) iter.next();
			selects.add(new SelectEntry(board.getDisplayName(), String.valueOf(board.getId()), false ));
		}

		return selects;
	}

	/**
	 *  Prepare the model to show a single board.
	 */
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HashMap model = new HashMap();

		int boardId = RequestUtils.getIntParameter(request, "boardid", 1);
		Board board = boardManager.getBoardById(boardId);

		List navigation = new ArrayList();
		navigation.add(createNavigationEntry(board));
		model.put("navigation", navigation);

		boolean showAll = true;

		User user = (User) request.getAttribute(USER_ATTRIBUTE);

		List cmd = new ArrayList();
		if (!user.isAnonymous() && user.getShowMarkAllRead()) {
			cmd.add(new NavigationEntry("mark.all.read", "/board/markRead.iwt?boardid=" + boardId + "&markstamp=" + ((Date) request.getAttribute("now")).getTime()));
		}

		if (user.getShowNewForumsOnly())
		{
			cmd.add(new NavigationEntry("show.all.forums", UserController.toggleURI("showNewForumsOnly")));
			showAll = false;
		}
		else
		{
			cmd.add(new NavigationEntry("show.new.forums.only", UserController.toggleURI("showNewForumsOnly")));
		}

		model.put("commands", cmd);

		List displayCats = new ArrayList();
		Set cats = board.getCategories();
		boolean isNew = false;

		for (Iterator iter = cats.iterator(); iter.hasNext();)
		{
			Category cat = (Category) iter.next();

			for (Iterator fiter = cat.getForums().iterator(); fiter.hasNext();)
			{
				Forum f = (Forum) fiter.next();

				boolean forumNew = false;

				forumNew = f.isNew(user);

				if (forumNew) {
					isNew = true;
				}

				if (showAll || forumNew)
				{
					displayCats.add(cat);
					break;
				}
			}
		}

		KeyedEntry title = new KeyedEntry("title.board");
		title.addArg(board.getDisplayName());
		title.addArg(isNew ? "(new) " : "");
		model.put("title", title);

		model.put("categories", displayCats);
		model.put("board", board);

		return new ModelAndView("board/show", model);
	}

	/**
	 *  Mark all forums read.
	 */
	public ModelAndView markRead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int boardId = RequestUtils.getIntParameter(request, "boardid", 1);
		Board board = boardManager.getBoardById(boardId);
		User user = (User) request.getAttribute(USER_ATTRIBUTE);
		Set categories = board.getCategories();

		if (categories != null)
		{
			Iterator iter = categories.iterator();
			Date now = new Date(RequestUtils.getRequiredLongParameter(request, "markstamp"));

			while (iter.hasNext())
			{
				Category cat = (Category) iter.next();
				user.markForumsRead(cat.getForums(), now);
			}

			userManager.saveUserAttributes(user);
		}

		return new ModelAndView(new RedirectView("show.iwt?boardid=" + boardId));
	}

	public void setBoardManager(BoardManager mgr) { boardManager = mgr; }
	public BoardManager getBoardManager() { return boardManager; }

	public void setUserManager(UserManager mgr) { userManager = mgr; }
	public UserManager getUserManager() { return userManager; }
}
