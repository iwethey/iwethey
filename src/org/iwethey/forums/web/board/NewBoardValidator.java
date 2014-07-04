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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Validates the information from the create board form.
 * <p>
 * $Id: NewBoardValidator.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class NewBoardValidator
{
	/**
	 * Validate the information on the form. Also checks the Board Manager
	 * to see if the given board name already exists.
	 * <p>
	 * @param obj The form backing object to validate.
	 * @param errors The errors object for error messages.
	 */
    public void validate(Object obj, Errors errors)
		{
			Board u = (Board) obj;

			if (u != null)
				{
				}
        }
}
