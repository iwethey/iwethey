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

package org.iwethey.forums.web.user;

import java.util.List;

import org.iwethey.forums.domain.User;

import org.springframework.validation.Errors;

/**
 * <p>Validates the information from the edit user form.</p>
 * 
 * <p>$Id: EditUserValidator.java 55 2004-12-07 21:53:42Z anderson $</p>
 * 
 * <p>@author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)</p>
 */
public class EditUserValidator extends NewUserValidator
{
	private List allowedIndentationFormats = null;
	private List allowedHierarchyPositions = null;
	private List allowedLinkFormats = null;
	private List allowedImageFormats = null;
	private int minForumBatchSize = 1;
	private int maxForumBatchSize = 100;
	private List allowedForumControlLocations = null;

	/**
	 * Validate the information on the form.
	 * <p>
	 * @param obj The form backing object to validate.
	 * @param errors The errors object for error messages.
	 */
    public void validate(Object obj, Errors errors)
	{
		User u = (User) obj;

		if (u != null)
		{
			if (u.getUnencryptedPassword() != null && !u.getUnencryptedPassword().equals(""))
			{
				checkPassword(u, errors);
				checkPasswordCheck(u, errors);
			}

			String test = u.getIndentationFormat();
			if (test != null && allowedIndentationFormats != null && !allowedIndentationFormats.contains(test))
			{
				errors.rejectValue("indentationFormat", "error.indentation.format", null, "Invalid value.");
			}

			test = u.getHierarchyPosition();
			if (test != null && allowedHierarchyPositions != null && !allowedHierarchyPositions.contains(test))
			{
				errors.rejectValue("hierarchyPosition", "error.hierarchy.position", null, "Invalid value.");
			}

			test = u.getLinkFormat();
			if (test != null && allowedLinkFormats != null && !allowedLinkFormats.contains(test))
			{
				errors.rejectValue("linkFormat", "error.link.format", null, "Invalid value.");
			}

			test = u.getImageFormat();
			if (test != null && allowedImageFormats != null && !allowedImageFormats.contains(test))
			{
				errors.rejectValue("imageFormat", "error.image.format", null, "Invalid value.");
			}

			test = u.getForumControlLocation();
			if (test != null && allowedForumControlLocations != null && !allowedForumControlLocations.contains(test))
			{
				errors.rejectValue("forumControlLocation", "error.forum.control.location", null, "Invalid value.");
			}

			int batch = u.getForumBatchSize();
			if (batch < minForumBatchSize)
			{
				errors.rejectValue("forumBatchSize", "error.forum.batch.size.min",
								   new Object[] {new Integer(minForumBatchSize)}, "Invalid value.");
			}

			if (batch > maxForumBatchSize)
			{
				errors.rejectValue("forumBatchSize", "error.forum.batch.size.max",
								   new Object[] {new Integer(maxForumBatchSize)}, "Invalid value.");
			}
		}
	}

	public void setAllowedIndentationFormats(List arg) { allowedIndentationFormats = arg; }
	public List getAllowedIndentationFormats() { return allowedIndentationFormats; }

	public void setAllowedHierarchyPositions(List arg) { allowedHierarchyPositions = arg; }
	public List getAllowedHierarchyPositions() { return allowedHierarchyPositions; }

	public void setAllowedLinkFormats(List arg) { allowedLinkFormats = arg; }
	public List getAllowedLinkFormats() { return allowedLinkFormats; }

	public void setAllowedImageFormats(List arg) { allowedImageFormats = arg; }
	public List getAllowedImageFormats() { return allowedImageFormats; }

	public void setMinForumBatchSize(int arg) { minForumBatchSize = arg; }
	public int getMinForumBatchSize() { return minForumBatchSize; }

	public void setMaxForumBatchSize(int arg) { maxForumBatchSize = arg; }
	public int getMaxForumBatchSize() { return maxForumBatchSize; }

	public void setAllowedForumControlLocations(List arg) { allowedForumControlLocations = arg; }
	public List getAllowedForumControlLocations() { return allowedForumControlLocations; }
}
