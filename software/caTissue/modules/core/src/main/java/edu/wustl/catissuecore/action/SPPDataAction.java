/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.action;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.common.dynamicextensions.ui.util.Constants;
import edu.wustl.catissuecore.bizlogic.SPPComboDataBizLogic;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 *
 * @author atul_kaushal
 * @version
 */
public class SPPDataAction extends SecureAction
{

	@Override
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String limit = request.getParameter("limit");
		String query = request.getParameter("query");
		String start = request.getParameter("start");
		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);

		JSONArray jsonArray = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();

		Integer total = limitFetch + startFetch;

		List spp = getSPPValues(query);

		List<NameValueBean> sppBean = new ArrayList<NameValueBean>();
		populateQuerySpecificNameValueBeansList(sppBean, spp, query);
		mainJsonObject.put("totalCount", sppBean.size());
		request.setAttribute(Constants.SELECTED_VALUES, sppBean);

		for (int i = startFetch; i < total && i < sppBean.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

			if (query == null
					|| sppBean.get(i).getName().toLowerCase(locale).contains(
							query.toLowerCase(locale)) || query.length() == 0)
			{
				jsonObject.put("id", sppBean.get(i).getValue());
				jsonObject.put("field", sppBean.get(i).getName());
				jsonArray.put(jsonObject);
			}
		}

		mainJsonObject.put("row", jsonArray);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(mainJsonObject.toString());

		return null;
	}

	/**
	 * returns the user list present in the system
	 * @param query
	 * @throws BizLogicException
	 */
	private List getSPPValues(String query) throws BizLogicException
	{
		SPPComboDataBizLogic sppComboDataBizObj = new SPPComboDataBizLogic();
		return sppComboDataBizObj.getSPPNameList(query,false);
	}

	/**
	 * This method populates name value beans list as per query,
	 * i.e. word typed into the auto-complete drop-down text field.
	 * @param querySpecificNVBeans
	 * @param users
	 * @param query
	 */
	private void populateQuerySpecificNameValueBeansList(List<NameValueBean> querySpecificNVBeans,
			List users, String query)
	{
		Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

		for (Object obj : users)
		{
			NameValueBean nvb = (NameValueBean) obj;

			if (nvb.getName().toLowerCase(locale).contains(query.toLowerCase(locale)))
			{
				querySpecificNVBeans.add(nvb);
			}
		}
	}

}

