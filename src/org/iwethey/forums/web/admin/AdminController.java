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

import org.hibernate.stat.SecondLevelCacheStatistics;

import org.iwethey.forums.domain.AdminManager;
import org.iwethey.forums.domain.Quote;
import org.iwethey.forums.domain.UserManager;
import org.iwethey.forums.web.ControllerAttributes;
import org.iwethey.forums.web.json.FastJsonView;
import org.iwethey.forums.web.json.TerseSerializer;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Handles general Admin URLs.</p>
 *
 * @author Mike Vitale (<a href="mailto:mike@iwethey.org">mike@iwethey.org</a>)
 */
public class AdminController extends MultiActionController implements ControllerAttributes
{
	private AdminManager mAdminManager = null;
	private UserManager mUserManager = null;

	/**
	 * <p>Shows the default admin screen.</p>
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		return new ModelAndView("admin/main", null);
	}

	/**
	 * <p>Retrieve Hibernate cache stats.</p>
	 *
	 * @param request Current HTTP request object.
	 * @param response Current HTTP response object.
	 */
	public ModelAndView hibernateStats(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		Map model = new HashMap();
		//		SecondLevelCacheStatistics cacheStats = stats.getSecondLevelCacheStatistics("org.iwethey.forums.domain.User");

		//		model.put("userStats", stats.getSecondLevelCacheStatistics("org.iwethey.forums.domain.User"));
		return new ModelAndView("admin/hibernateStats", model);
	}

	/**
	 * <p>Retrieve all Quotes for editing view.</p>
	 *
	 * @param request Current HTTP request object.
	 * @param response Current HTTP response object.
	 */
	public ModelAndView allLrpds(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		List<Quote> quotes = mAdminManager.getAllLRPDs();
		Map model = new HashMap();
		model.put("quotes", quotes);

		Map config = new HashMap();
		config.put("json", model);
		config.put("serializer", new TerseSerializer());

		return new ModelAndView(new FastJsonView(), config);
	}

	public ModelAndView saveLrpd(HttpServletRequest request, HttpServletResponse response)
			throws ServletRequestBindingException
	{
		System.out.println("*****************");
		System.out.println("*** SAVE LRPD ***");
		System.out.println("*****************");
		int id = RequestUtils.getRequiredIntParameter(request, "id");
		boolean approved = RequestUtils.getRequiredBooleanParameter(request, "approved");
		String updatedQuote = RequestUtils.getRequiredStringParameter(request, "quote");


		Quote quote = mAdminManager.getQuote(id);

		quote.setApproved(approved);
		quote.setQuote(updatedQuote);
		if (quote.isApproved() && quote.getApprovedBy() == null)
		{
			Integer userId = (Integer) WebUtils.getSessionAttribute(request, USER_ID_ATTRIBUTE);
			quote.setApprovedBy(mUserManager.getUserById(userId));
		}

		if (quote.isApproved() && quote.getApprovedDate() == null)
		{
			quote.setApprovedDate(new Date());
		}

		System.out.println("Saving quote = [" + quote + "]");

		mAdminManager.saveQuote(quote);

		return new ModelAndView();
	}

	public void setAdminManager(AdminManager mgr) { mAdminManager = mgr; }
	public AdminManager getAdminManager() { return mAdminManager; }

	public void setUserManager(UserManager um) { mUserManager = um; }
	public UserManager getUserManager() { return mUserManager; }
}

