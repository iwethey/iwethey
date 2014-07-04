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
 * Describes the database interface for category functions.
 * <p>
 * $Id: CategoryManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public interface CategoryManager
{
	/** Retrieve a category by ID.
	 * <p>
	 * @param category The category to retrieve.
	 */
    public Category getCategoryById(int category);

	/** Save a category. If the category does not exist, it is created first.
	 * <p>
	 * @param category The category to save.
	 */
    public void saveCategory(Category category);

	/** Create a new category.
	 * <p>
	 * @param category The category to save.
	 * @return The category ID of the new category.
	 */
    public int createCategory(Category category);

	/** Remove a category by ID.
	 * <p>
	 * @param category The category to remove.
	 */
    public void removeCategory(Category category);
}
