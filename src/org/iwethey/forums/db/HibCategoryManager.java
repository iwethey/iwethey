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

import org.iwethey.forums.domain.Category;
import org.iwethey.forums.domain.CategoryManager;

import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the database interface for category functions.
 * <p>
 * $Id: HibCategoryManager.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HibCategoryManager extends HibernateSupportExtensions implements CategoryManager
{
    /** Logger for this class and subclasses */
    private final Log logger = LogFactory.getLog(getClass());

	/** Retrieve all categories.
	 * <p>
	 */
    public List getCategories()
		{
			return getHibernateTemplate().loadAll(Category.class);
		}

	/** Retrieve a category by ID.
	 * <p>
	 * @param category The category to retrieve.
	 */
    public Category getCategoryById(int category)
		{
			try
				{
					return (Category) getHibernateTemplate().get(Category.class, new Integer(category));
				}
			catch (HibernateObjectRetrievalFailureException e)
				{
					return null;
				}
		}

	/** Save a category. If the category does not exist, it is created first.
	 * <p>
	 * @param category The category to save.
	 * @return The ID of the new category.
	 */
    public void saveCategory(Category category)
		{
			getHibernateTemplate().saveOrUpdate(category);
		}

	/** Create a new category.
	 * <p>
	 * @param category The category to save.
	 */
    public int createCategory(Category category)
		{
			getHibernateTemplate().save(category);
			return category.getId();
		}

	/** Remove a category.
	 * <p>
	 * @param category The category to remove.
	 */
    public void removeCategory(Category category)
		{
			getHibernateTemplate().delete(category);
		}
}
