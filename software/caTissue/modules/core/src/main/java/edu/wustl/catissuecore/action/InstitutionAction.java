/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 * <p>Title: InstitutionAction Class>
 * <p>Description:	This class initializes the fields in the Institution.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 * 
 */
public class InstitutionAction extends SecureAction {

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * drop down fields in Institution.jsp Page.
	 * 
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return value for ActionForward object
	 */
	Logger logger = Logger.getCommonLogger(InstitutionAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);
saveToken(request);
		request.setAttribute("operationAdd", Constants.ADD);
		request.setAttribute("operationEdit", Constants.EDIT);

		final InstitutionForm institutionForm = (InstitutionForm) form;
		institutionForm.setOperation(operation);
		// Sets the operation attribute to be used in the Add/Edit Institution
		// Page.
		String formName = Constants.INSTITUTION_ADD_ACTION;
		final String submittedFor = (String) request
				.getAttribute(Constants.SUBMITTED_FOR);
		institutionForm.setSubmittedFor(submittedFor);

		if (operation.equals(Constants.EDIT)) {
			formName = Constants.INSTITUTION_EDIT_ACTION;

		} else
		// Add
		{
			formName = Constants.INSTITUTION_ADD_ACTION;

		}
		request.setAttribute("formName", formName);

		if (!Constants.INSTITUTION_ADD_ACTION.equals(formName)
				&& (institutionForm.getId() != 0)) {
			final IFactory factory = AbstractFactoryConfig.getInstance()
					.getBizLogicFactory();
			final DefaultBizLogic bizLogic = (DefaultBizLogic) factory
					.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			Institution institution = (Institution) bizLogic.retrieve(
					Institution.class.getName(), institutionForm.getId());
			institutionForm.setAllValues(institution);
		}

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}

}