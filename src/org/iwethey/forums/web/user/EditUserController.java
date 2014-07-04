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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iwethey.forums.domain.Board;
import org.iwethey.forums.domain.User;
import org.iwethey.forums.domain.UserManager;
import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.NavigationEntry;
import org.iwethey.forums.web.SelectEntry;
import org.iwethey.forums.web.board.BoardController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

/**
 * Manages the edit user forms. 
 * <p>
 * Provides validation and backing data.
 * <p>
 * $Id: EditUserController.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class EditUserController extends AbstractFormController implements ControllerAttributes
{
	/** Logging object for this class. */
    protected final Log logger = LogFactory.getLog(getClass());

	/** Storage interface for users. */
	private UserManager userManager = null;

	private BoardController boardController = null;

	/** Validation information. */
	private List allowedIndentationFormats = null;
	/** Validation information. */
	private List allowedHierarchyPositions = null;
	/** Validation information. */
	private List allowedLinkFormats = null;
	/** Validation information. */
	private List allowedImageFormats = null;
	/** Validation information. */
	private List allowedForumControlLocations = null;

	private List allowedTimeZones = null;

	/** Empty constructor for bean use. */
	public EditUserController() { }

	/**
	 * Copies validation information from the validator being used for this form.
	 * <p>
	 */
	private void setupValidator()
	{
		if (this.allowedIndentationFormats != null)
		{
			return;
		}

		EditUserValidator v = (EditUserValidator) getValidator();

		// Cache the select lists
		this.allowedIndentationFormats = loadList(v.getAllowedIndentationFormats(), "allowed.indentation.");
		this.allowedHierarchyPositions = loadList(v.getAllowedHierarchyPositions(), "allowed.hierarchy.");
		this.allowedLinkFormats = loadList(v.getAllowedLinkFormats(), "allowed.links.");
		this.allowedImageFormats = loadList(v.getAllowedImageFormats(), "allowed.image.");
		this.allowedForumControlLocations = loadList(v.getAllowedForumControlLocations(), "allowed.control.");
	}

	/**
	 * Load a list of select list entries with SelectEntry objects.
	 * <p>
	 * @param values The values allowed in the list.
	 * @param prepend The string to prepend to the values for indexing into the messages file.
	 * @return A list of SelectEntry objects with the allowed values inside.
	 */
	private List loadList(List values, String prepend)
	{
		List selects = new ArrayList();

		Iterator iter = values.iterator();
		while (iter.hasNext())
		{
			String val = (String) iter.next();
			selects.add(new SelectEntry(prepend + val, val));
		}

		return selects;
	}

	private List getAllowedTimeZones()
	{
		if (this.allowedTimeZones != null)
		{
			return this.allowedTimeZones;
		}

		this.allowedTimeZones = new ArrayList();

		String[] zones = TimeZone.getAvailableIDs();

		for (int i=0; i < zones.length; i++)
		{
			String zone = zones[i];
			if (zone.startsWith("Etc/")
				|| zone.startsWith("SystemV/")
				|| zone.indexOf("/") < 0
				)
			{
				continue;	
			}
			this.allowedTimeZones.add(new SelectEntry(zone, zone, false));
		}

		return this.allowedTimeZones;
	}

	/**
	 * Place data into the model for use by the JSP pages.
	 * <p>
	 * Includes navigation and control links, and validation information.
	 * <p>
	 * @param request The servlet request object.
	 * @param command The form backing store object (a User object).
	 * @param errors The Spring errors object.
	 * @return A map of name/value pairs for the model.
	 */
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception
	{
		Map model = new HashMap();
		List navigation = new ArrayList();

		NavigationEntry nav = new NavigationEntry("edit.user", "/user/edit.iwt", true);
		nav.addArg(request.getAttribute("username"));
		navigation.add(nav);
		model.put("navigation", navigation);

		String action = RequestUtils.getStringParameter(request, "action", "general");

		List cmd = new ArrayList();
		cmd.add(new NavigationEntry("edit.user.main", "/user/edit.iwt", action.equals("general")));
		cmd.add(new NavigationEntry("edit.user.display", "/user/edit.iwt?action=display", action.equals("display")));
		cmd.add(new NavigationEntry("edit.user.posting", "/user/edit.iwt?action=posting", action.equals("posting")));
		model.put("commands", cmd);

		setupValidator();

		model.put("allowedIndentationFormats", this.allowedIndentationFormats);
		model.put("allowedHierarchyPositions", this.allowedHierarchyPositions);
		model.put("allowedLinkFormats", this.allowedLinkFormats);
		model.put("allowedImageFormats", this.allowedImageFormats);
		model.put("allowedForumControlLocations", this.allowedForumControlLocations);
		model.put("allowedTimeZones", getAllowedTimeZones());

		if (action.equals("display"))
		{
			List boards = this.boardController.boardSelectList();
			boards.add(0, new SelectEntry("no.default.board", "0"));
			model.put("boards", boards);
		}

		return model;
	}

	/**
	 * Determine which edit form to show, setting the view appropriately.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception
	{
		String action = RequestUtils.getStringParameter(request, "action", "general");

		return showForm(request, errors, "/user/edit-" + action);
	}

	/**
	 * Process a submitted form by updating the user through the storage manager.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a User object).
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
								 Object command, BindException errors) throws Exception
	{
		User user = (User) command;
		this.userManager.saveUserAttributes(user);

		String action = RequestUtils.getStringParameter(request, "action", "general");
		ModelAndView mv = showForm(request, errors, "user/edit-" + action);
		mv.addObject("message", "settings.updated");

		return mv;
	}

	/**
	 * Extra validation and binding. Spring does not automatically convert booleans
	 * with false values.
	 * <p>
	 * @param request The servlet request object.
	 * @param command The form backing store object (a User object).
	 */
	@Override
	protected void onBind(HttpServletRequest request, Object command) throws Exception
	{
		User u = (User) command;
		String action = RequestUtils.getStringParameter(request, "action", "general");

		if (action.equals("general"))
		{
			u.setShowInActives(RequestUtils.getBooleanParameter(request, "showInActives", false));
		}
		else if (action.equals("display"))
		{
			u.setUseCss(RequestUtils.getBooleanParameter(request, "useCss", false));
			u.setShowNewPostsOnly(RequestUtils.getBooleanParameter(request, "showNewPostsOnly", false));
			u.setShowPersonalPictures(RequestUtils.getBooleanParameter(request, "showPersonalPictures", false));
			u.setSortByLastModified(RequestUtils.getBooleanParameter(request, "sortByLastModified", false));
			u.setShowPostIds(RequestUtils.getBooleanParameter(request, "showPostIds", false));
			u.setShowThreadReplyLinks(RequestUtils.getBooleanParameter(request, "showThreadReplyLinks", false));
			u.setShowJumpTags(RequestUtils.getBooleanParameter(request, "showJumpTags", false));
			u.setShowMarkAllRead(RequestUtils.getBooleanParameter(request, "showMarkAllRead", false));
			u.setShowForumMiniControl(RequestUtils.getBooleanParameter(request, "showForumMiniControl", false));
			u.setShowNewForumsOnly(RequestUtils.getBooleanParameter(request, "showNewForumsOnly", false));
			u.setInvertLpms(RequestUtils.getBooleanParameter(request, "invertLpms", false));
		}
		else if (action.equals("posting"))
		{
			u.setPreviewByDefault(RequestUtils.getBooleanParameter(request, "previewByDefault", false));
			u.setAutofillSubject(RequestUtils.getBooleanParameter(request, "autofillSubject", false));
			u.setConvertNewlines(RequestUtils.getBooleanParameter(request, "convertNewlines", false));
			u.setConvertHtml(RequestUtils.getBooleanParameter(request, "convertHtml", false));
			u.setConvertLinks(RequestUtils.getBooleanParameter(request, "convertLinks", false));
			u.setConvertCodes(RequestUtils.getBooleanParameter(request, "convertCodes", false));
		}
	}

	/**
	 * Check to see if there are form errors. If so, show the form again, otherwise
	 * process the submission.
	 * <p>
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a User object).
	 * @param errors The Spring errors object.
	 */
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
												 Object command, BindException errors) throws Exception
	{
		if (errors.hasErrors())
		{
			logger.warn("HAS ERRORS: " + errors);
			return showForm(request, response, errors);
		}
		else
		{
			return onSubmit(request, response, command, errors);
		}
	}

	/**
	 * Load the user object for the form backing by getting the User
	 * object from the request.
	 * <p>
	 * @param request The servlet request object.
	 * @return A prepopulated User object for use in the JSP form.
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException
	{
		User user = (User) request.getAttribute(USER_ATTRIBUTE);
		assert (user != null) : "USER_ATTRIBUTE is null";
		return user;
	}

	public void setUserManager(UserManager mgr) { this.userManager = mgr; }
	public UserManager getUserManager() { return this.userManager; }

	public void setBoardController(BoardController mgr) { this.boardController = mgr; }
	public BoardController getBoardController() { return this.boardController; }
}
