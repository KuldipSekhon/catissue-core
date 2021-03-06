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

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author santhoshkumar_c
 * @author Alexander Zgursky
 */
public class MagetabExportCartAction extends QueryShoppingCartAction {

    /**
     * Overrides the executeSecureAction method of SecureAction class.
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
     * @return ActionForward : ActionForward
     */
    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
        List<Integer> chkBoxValues = this.getCheckboxValues(searchForm);

        final HttpSession session = request.getSession();
        final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
        final QueryShoppingCart cart = (QueryShoppingCart) session
                .getAttribute(Constants.QUERY_SHOPPING_CART);

        final List<List<String>> exportList = bizLogic.export(cart,
                chkBoxValues);
        List<String> columnList = exportList.get(0);

        int idColumnIndex = -1;
        for (int i = 0; i < columnList.size(); i++) {
            final String columnName = columnList.get(i).trim();
            if (columnName.equalsIgnoreCase("ID")
                    || columnName.equalsIgnoreCase("Id : Specimen")) {
                idColumnIndex = i;
                break;
            }
        }

        if (idColumnIndex <= 0) {
            throw new RuntimeException(
                    "Couldn't find primary key column in search results");
        }

        List<Long> specimenIds = new LinkedList<Long>();

        List<List<String>> dataList = exportList.subList(1, exportList.size());
        for (List<String> row : dataList) {
            specimenIds.add(Long.parseLong(row.get(idColumnIndex)));
        }

        MagetabExportWizardBean wizardBean = (MagetabExportWizardBean) session
                .getAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
        if (wizardBean == null) {
            wizardBean = new MagetabExportWizardBean();
            session.setAttribute(
                    MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN,
                    wizardBean);
        }
        wizardBean.init(specimenIds);
        return mapping.findForward("startWizard");

    }

}
