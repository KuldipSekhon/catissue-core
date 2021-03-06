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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.CommonLoginInfoUtility;
import edu.wustl.catissuecore.util.SSOcaTissueCommonLoginUtility;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;

/**
 * <p>
 * Title:
 * </p>
 *<p>
 * Description:
 * </p>
 *<p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 *<p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 *
 * @author Aarti Sharma
 *@version 1.0
 */
public class LoginAction extends XSSSupportedAction
{

    /**
     * Common Logger for Login Action.
     */
    private static final Logger LOGGER = Logger.getCommonLogger(LoginAction.class);

    /**
     * Overrides the execute method of Action class.
     *
     * @param mapping
     *            object of ActionMapping
     * @param form
     *            object of ActionForm
     * @param request
     *            object of HttpServletRequest
     * @param response
     *            object of HttpServletResponse
     * @return value for ActionForward object
     */
    @Override
    public ActionForward executeXSS(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response)
    {
        String forwardTo = Constants.FAILURE;
        CommonLoginInfoUtility infoUtility = new CommonLoginInfoUtility();
    	String referer=request.getHeader("referer");
    	boolean invalidRequest=false;

		if (form == null)
		{
			LoginAction.LOGGER.debug("Form is Null");
			forwardTo = Constants.FAILURE;
		}
		else
		{
			try
			{
				cleanSession(request);
				LoginAction.LOGGER.info("Inside Login Action, Just before validation");
			   if(request.getRequestURL()!=null)
				{
					CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());
				}
				String logInURL=CommonServiceLocator.getInstance().getAppURL()+"/Login.do";
				String logOutURL=CommonServiceLocator.getInstance().getAppURL()+"/Logout.do";
				String reDirectHomeURL=CommonServiceLocator.getInstance().getAppURL()+"/RedirectHome.do";
				if(!logOutURL.equals(referer) && !logInURL.equals(referer) && !reDirectHomeURL.equals(referer))
				{
					if(referer != null)
 					{
						invalidRequest=true;
						throw new Exception();
					}
				}

				if (isRequestFromClinportal(request))
				{
					forwardTo = Constants.SUCCESS;
				}
				else
				{
					SSOcaTissueCommonLoginUtility loginUtility = new SSOcaTissueCommonLoginUtility();
					LoginCredentials loginCredentials = new LoginCredentials();
					loginCredentials.setLoginName(((LoginForm)form).getLoginName());
					loginCredentials.setPassword(((LoginForm)form).getPassword());
					LoginAction.LOGGER.info("Inside Login Action, Just before authentication");
					loginCredentials = loginUtility.processUserAuthentication(loginCredentials, request, ((LoginForm)form).getLoginName());
					LoginAction.LOGGER.info("Inside Login Action, Just before authorization");
					infoUtility = loginUtility.processUserAuthorization(loginCredentials, ((LoginForm)form).getLoginName(), request);
					if(infoUtility.getActionErrors() != null)
					{
						saveErrors(request, infoUtility.getActionErrors());
					}
					if(infoUtility.getActionMessages() != null)
					{
						saveMessages(request, infoUtility.getActionMessages());
					}
				}
			}
			catch (final Exception ex)
			{
				String message = "";
				try{
					message = CatissueLoginProcessor.auditLogin(false, ((LoginForm)form).getLoginName(), request.getRemoteAddr());
					}catch(CatissueException e)
					{LoginAction.LOGGER.error("Exception while auditing: " + e.getMessage(), e);}
				LoginAction.LOGGER.error("Exception: " + ex.getMessage(), ex);
				cleanSession(request);
				if(Validator.isEmpty(message))
					handleError(request, "errors.invalid","username or password");
				else
					handleError(request, "error.account.locked","");
				forwardTo = Constants.FAILURE;

				infoUtility.setForwardTo(forwardTo);
			}
		}
        return getActionForward(mapping, form, infoUtility);
    }

	private ActionForward getActionForward(final ActionMapping mapping,
			final ActionForm form, CommonLoginInfoUtility infoUtility) {
		ActionForward actionForward = null;
        if(infoUtility.getForwardTo().equals("GridGrouperUser"))
        {
        	ActionForward ggForwardTemplate = mapping
			.findForward("GridGrouperUser");
        	actionForward = new ActionForward(
			ggForwardTemplate.getName(),
			ggForwardTemplate.getPath()+((LoginForm)form).getLoginName(),
			ggForwardTemplate.getRedirect(),
			ggForwardTemplate.getContextRelative());
        }
        else
        {
        	actionForward = mapping.findForward(infoUtility.getForwardTo());
        }
        return actionForward;
	}

    private boolean isRequestFromClinportal(final HttpServletRequest request)
    {
        return request.getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL) != null
                && !(CDMSIntegrationConstants.DOUBLE_QUOTE.equals(request
                        .getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL)))
                && Boolean.valueOf(request.getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL));
    }

    /**
     * This method will clean session.
     *
     * @param request
     *            object of HttpServletRequest
     */
    private void cleanSession(final HttpServletRequest request)
    {
        final HttpSession prevSession = request.getSession();
        if (prevSession != null)
        {
            prevSession.invalidate();
        }
    }

    /**
     *
     * @param request
     *            : request
     * @param errorKey
     *            : errorKey
     */
    private void handleError(final HttpServletRequest request, final String errorKey,String errorMsg)
    {
        final ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,errorMsg));
        // Report any errors we have discovered
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
        }
    }
}