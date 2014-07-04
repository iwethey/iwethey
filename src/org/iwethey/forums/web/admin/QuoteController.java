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

package org.iwethey.forums.web.admin;

import org.iwethey.forums.domain.AdminManager;
import org.iwethey.forums.domain.Quote;
import org.iwethey.forums.web.ControllerAttributes;

import org.springframework.web.bind.RequestUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Handles general Admin URLs.</p>
 *
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class QuoteController extends SimpleFormController implements ControllerAttributes
{
	private AdminManager mAdminManager = null;

	/**
	 * <p>Create a new QuoteController.</p>
	 */
	public QuoteController() {}

	/**
	 * <p>Places the Quotes in the model.</p>
	 *
	 * @param request The servlet request object.
	 * @return The information to place in the model.
	 */
	@Override
	protected Map referenceData(HttpServletRequest request)
		throws Exception
	{
		Map model = new HashMap();

		model.put("lrpds", mAdminManager.getAllLRPDs());

		return model;
	}

	/**
	 * <p>Load a blank Quote object for the form backing.</p>
	 *
	 * @param request The servlet request object.
	 * @return A prepopulated Quote object for use in the JSP form.
	 */
	@Override
    protected Object formBackingObject(HttpServletRequest request)
		throws ServletException
	{
		return new Quote();
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors)
		throws Exception
	{
		return super.showForm(request, response, errors);
	}

	/**
	 * <p>Process a submitted form by creating the Quote.</p>
	 *
	 * @param request The servlet request object.
	 * @param response The servlet response object.
	 * @param command The form backing store object (a Quote object).
	 * @param errors The Spring errors object.
	 * @return The model and view.
	 */
	@Override
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
								 Object command, BindException errors)
		throws ServletException, Exception
	{
		Map model = new HashMap();

		Quote quote = (Quote) command;

		int id = RequestUtils.getIntParameter(request, "id", 0);
		if (id > 0)
		{
			Quote existing = this.mAdminManager.getQuote(id);

			existing.setQuote(quote.getQuote());

			this.mAdminManager.saveQuote(existing);

			return new ModelAndView("admin/lrpd/edit");
		}
		else
		{
			this.mAdminManager.saveQuote(quote);
		}

		model.putAll(referenceData(request));
		return new ModelAndView("/admin/lrpd/show", model);
	}

	public void setAdminManager(AdminManager mgr) { mAdminManager = mgr; }
	public AdminManager getAdminManager() { return mAdminManager; }
}
