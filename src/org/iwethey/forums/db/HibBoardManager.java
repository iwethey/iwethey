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

package org.iwethey.forums.db;

import org.iwethey.forums.domain.Board;
import org.iwethey.forums.domain.BoardManager;
import org.iwethey.forums.domain.BoardSummary;
import org.iwethey.forums.domain.UserManager;

import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the database interface for board functions.
 * <p>
 * $Id: HibBoardManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibBoardManager extends HibernateSupportExtensions implements BoardManager
{
    /** Logger for this class and subclasses */
    private final Log logger = LogFactory.getLog(getClass());

	private UserManager userManager = null;

	/** Retrieve all boards.
	 * <p>
	 */
    public List getBoards()
	{
		return getHibernateTemplate().find("from Board board order by board.id");
	}

	/**
	 * <p>Retrieve all boards with summary information.</p>
	 */
    public List<BoardSummary> getBoardSummary()
	{
		return getHibernateTemplate().find("from BoardSummary bs order by bs.id");
	}

	/** Retrieve a board by ID.
	 * <p>
	 * @param board The board to retrieve.
	 */
    public Board getBoardById(int board)
	{
		try
		{
			return (Board) getHibernateTemplate().get(Board.class, new Integer(board));
		}
		catch (HibernateObjectRetrievalFailureException e)
		{
			return null;
		}
	}

	/** Save a board. If the board does not exist, it is created first.
	 * <p>
	 * @param board The board to save.
	 * @return The ID of the new board.
	 */
    public void saveBoard(Board board)
	{
		getHibernateTemplate().saveOrUpdate(board);
	}

	/** Create a new board.
	 * <p>
	 * @param board The board to save.
	 */
    public int createBoard(Board board)
	{
		getHibernateTemplate().save(board);
		return board.getId();
	}

	/** Remove a board.
	 * <p>
	 * @param board The board to remove.
	 */
    public void removeBoard(Board board)
	{
		getHibernateTemplate().delete(board);
	}

	public void setUserManager(UserManager mgr) { userManager = mgr; }
	public UserManager getUserManager() { return userManager; }
}
