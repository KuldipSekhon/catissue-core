/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.service;

import edu.wustl.catissuecore.domain.util.Service;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

/**
 * @author Ion C. Olaru
 * @created by Introduce Toolkit version 1.4
 */
public class Catissue_cacoreImpl extends Catissue_cacoreImplBase {

	private static Logger log = Logger.getLogger(Catissue_cacoreImpl.class);

	public Catissue_cacoreImpl() throws RemoteException {
		super();
	}

	private QueryProcessingExceptionType getError(Exception e) {
		e.printStackTrace();
		gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType ex = new gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType();
		ex.setStackTrace(new StackTraceElement[0]);
		ex.setFaultString(e.getCause().toString());
		return ex;
	}

  public java.lang.Object insert(java.lang.Object object) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {

		log.debug(">>> INSERTING: " + object.getClass());
		Object ado = null;
		try {
			ado = getClient().insertWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		log.debug(">>> INSERTED: " + object.getClass());
		return ado;
	}

  public java.lang.Object update(java.lang.Object object) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
		Object ado = null;
		try {
			ado = getClient().updateWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		return ado;
	}

  public java.lang.Object disable(java.lang.Object object) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
		Object ado = null;
		WAPIClient client = null;
		try {
			client = getClient();
		} catch (Exception e) {
			throw getError(e);
		}
		try {
			ado = client.updateWsObjectStatus(object);
		} catch (Exception e) {
			throw getError(e);
		}
		if (ado == null)
			return object;
		try {
			ado = client.updateWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		return ado;
	}

	private WAPIClient getClient() throws Exception {
//		System.out.println("url: " + getConfiguration().getCql2QueryProcessorConfig_applicationHostName());
		return WAPIUtility.getClient(this.getConfiguration());
	}

}
