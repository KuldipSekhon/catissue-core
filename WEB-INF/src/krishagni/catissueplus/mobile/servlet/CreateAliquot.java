package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.bizlogic.AliquotBizLogic;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import krishagni.catissueplus.mobile.dto.AliquotsDetailsDTO;
import edu.wustl.common.exception.ApplicationException;

public class CreateAliquot  extends HttpServlet  {
	/**
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}
	
	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{


		JSONObject returnedJObject= new JSONObject();
		try{
			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			AliquotsDetailsDTO aliquotDetailObj = getAliqoutDetail(request);
			AliquotBizLogic bizLogic = new AliquotBizLogic();
			if(aliquotDetailObj.isBasedOnCP()){
				bizLogic.createAliquotSpecimenBasedOnCp(aliquotDetailObj, sessionDataBean);
			} else {
				bizLogic.createAliquotSpecimen(aliquotDetailObj,sessionDataBean);
			}
			returnedJObject.put("success", "true");
			
		}catch(ApplicationException e){
			e.getMsgValues();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
    	
		response.getWriter().write(returnedJObject.toString());


	}
	
	public AliquotsDetailsDTO getAliqoutDetail(HttpServletRequest request){
		AliquotsDetailsDTO obj = new AliquotsDetailsDTO();
		obj.setContainerName(request.getParameter("containerName"));
		obj.setParentSpecimenLabel( request.getParameter("specimenLable"));
		if(request.getParameter("basedOnCp") == null || "no".equals(request.getParameter("basedOnCp"))){
			obj.setBasedOnCP(false);
		}else{
			obj.setBasedOnCP(true);
		}
		
		if(request.getParameter("noOfAliquots")!=null){
			obj.setNoOfAliquots(Integer.parseInt(request.getParameter("noOfAliquots")));
		}else{
			obj.setNoOfAliquots(0);
		}
		if(request.getParameter("quantity") != null && !"".equals(request.getParameter("quantity").trim())){
			obj.setQuantityPerAliquot(Double.parseDouble(request.getParameter("quantity")));
		}else{
			obj.setQuantityPerAliquot(0.0);
		}
		
		obj.setStartingStoragePositionX(request.getParameter("positionX"));
		obj.setCreatedDate(Calendar.getInstance().getTime());
		obj.setStartingStoragePositionY(request.getParameter("positionY"));
		if("true".equals(request.getParameter("disposeCheck"))){
			obj.setDisposeParentCheck(true);
		}else{
			obj.setDisposeParentCheck(false);
		}
		return obj;
	}
		
}