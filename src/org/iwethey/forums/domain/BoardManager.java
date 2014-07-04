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

package org.iwethey.forums.domain;

import java.util.List;

/**
 * Describes the database interface for board functions.
 * <p>
 * $Id: BoardManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface BoardManager
{
	/** Retrieve all boards.
	 * <p>
	 */
    public List getBoards();

	/**
	 * <p>Retrieve all boards with summary information.</p>
	 */
    public List<BoardSummary> getBoardSummary();

	/** Retrieve a board by ID.
	 * <p>
	 * @param board The board to retrieve.
	 */
    public Board getBoardById(int board);

	/** Save a board. If the board does not exist, it is created first.
	 * <p>
	 * @param board The board to save.
	 */
    public void saveBoard(Board board);

	/** Create a new board.
	 * <p>
	 * @param board The board to save.
	 * @return The board ID of the new board.
	 */
    public int createBoard(Board board);

	/** Remove a board by ID.
	 * <p>
	 * @param board The board to remove.
	 */
    public void removeBoard(Board board);
}
