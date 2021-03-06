/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CaTissueWritableAppService extends WritableApplicationService {

	String getCaTissueServerProperty(String key) throws ApplicationException;
	String getVisitInformationURL(Object urlInformationObject) throws ApplicationException;
	String getSpecimenCollectionGroupURL(Object urlInformationObject) throws ApplicationException;
	String getSpecimenCollectionGroupLabel(Object scg) throws ApplicationException;
	Object registerParticipant(Object object, Long cpid) throws ApplicationException;
	Object getVisitRelatedEncounteredDate(Map<String, Long> map) throws ApplicationException;
	List<Object> getCaTissueLocalParticipantMatchingObects(Object object, Set<Long> cpIdSet) throws ApplicationException;
	List<Object> getParticipantMatchingObects(Object object) throws ApplicationException;
    public SDKQueryResult executeQuery(SDKQuery query, String gridId) throws ApplicationException;
    public <E> java.util.List<E> executeQuery(CQLQuery query, String gridId) throws ApplicationException;
    public <E> java.util.List<E> executeQuery(HQLCriteria criteria, String gridId) throws ApplicationException;

}
