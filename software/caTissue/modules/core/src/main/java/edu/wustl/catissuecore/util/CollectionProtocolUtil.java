/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.CollectionProtocolUtility;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SpecimenEventsUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * The Class CollectionProtocolUtil.
 */
public class CollectionProtocolUtil
{

	static InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);

	/** The Constant MOLECULAR_SPECIMEN_CLASS. */
	private static final String MOLECULAR_SPECIMEN_CLASS = "Molecular";

	/** The event bean. */
	private LinkedHashMap<String, CollectionProtocolEventBean> eventBean =
		new LinkedHashMap<String, CollectionProtocolEventBean> ();

	/** The Constant STORAGE_TYPE_ARR. */
	private static final String STORAGE_TYPE_ARR[]= {"Virtual", "Auto","Manual"};

	/**
	 * Gets the storage type value.
	 *
	 * @param type the type
	 *
	 * @return the storage type value
	 */
	public static Integer getStorageTypeValue(String type)
	{
		int returnVal=Integer.valueOf(0);
		for (int ctr=0;ctr<STORAGE_TYPE_ARR.length;ctr++)
		{
			if(STORAGE_TYPE_ARR[ctr].equals(type))
			{
				returnVal=Integer.valueOf(ctr);
				break;
			}
		}

		return returnVal; 	//default considered as 'Virtual';
	}

	/**
	 * Gets the storage type value.
	 *
	 * @param type the type
	 *
	 * @return the storage type value
	 */
	public static String getStorageTypeValue(Integer type)
	{
		String storeArr;//STORAGE_TYPE_ARR[type.intValue()];
		if (type == null)
		{
			storeArr=STORAGE_TYPE_ARR[0]; //default considered as 'Virtual';
		}
		else
		{
			storeArr=STORAGE_TYPE_ARR[type.intValue()];
		}
		//if(type.intValue()>2) return storageTypeArr[1];
		return storeArr;
	}

	/**
	 * Gets the collection protocol bean.
	 *
	 * @param collectionProtocol the collection protocol
	 *
	 * @return the collection protocol bean
	 */
	public static CollectionProtocolBean getCollectionProtocolBean(CollectionProtocol collectionProtocol)
	{
		CollectionProtocolBean collectionProtocolBean;

		collectionProtocolBean = new CollectionProtocolBean();
		collectionProtocolBean.setConsentTierCounter(collectionProtocol.getConsentTierCollection().size());
		Long identifier = Long.valueOf(collectionProtocol.getId().longValue());
		collectionProtocolBean.setIdentifier(identifier);

		long[] coordinatorIds = null;
		coordinatorIds = getProtocolCordnateIds(collectionProtocol, coordinatorIds);

		collectionProtocolBean.setCoordinatorIds(coordinatorIds);

		/**For Clinical Diagnosis subset **/
		collectionProtocolBean.setClinicalDiagnosis(getClinicalDiagnosis(collectionProtocol));

		setBasicCPProps(collectionProtocol, collectionProtocolBean);
		if(collectionProtocol.getCollectionProtocolRegistrationCollection()==null)
		{
			collectionProtocol.setCollectionProtocolRegistrationCollection(new HashSet<CollectionProtocolRegistration>());
		}
		if(collectionProtocol.getCollectionProtocolRegistrationCollection().size() > 0)
		{
			collectionProtocolBean.setParticiapantReg(true);
		}

		collectionProtocolBean.setType(collectionProtocol.getType());
		collectionProtocolBean.setSequenceNumber(collectionProtocol.getSequenceNumber());
		if(collectionProtocol.getStudyCalendarEventPoint() != null)
		{
			collectionProtocolBean.setStudyCalendarEventPoint(collectionProtocol.getStudyCalendarEventPoint().toString());
		}

		if(collectionProtocol.getParentCollectionProtocol() != null)
		{
			collectionProtocolBean.setParentCollectionProtocolId(collectionProtocol.getParentCollectionProtocol().getId());
		}
		if (collectionProtocol.getRemoteId() !=null){
			collectionProtocolBean.setRemoteId(new Long(collectionProtocol.getRemoteId()).longValue());
		}
		if (collectionProtocol.getRemoteManagedFlag() !=null){
			collectionProtocolBean.setRemoteManagedFlag(new Boolean(collectionProtocol.getRemoteManagedFlag()).booleanValue());
		}

		if (collectionProtocol.getDirtyEditFlag() !=null){
			collectionProtocolBean.setDirtyEditFlag(new Boolean(collectionProtocol.getDirtyEditFlag()).booleanValue());
		}
		if (collectionProtocol.getRemoteId() !=null){
			collectionProtocolBean.setRemoteId(new Long(collectionProtocol.getRemoteId()).longValue());
		}
		if (collectionProtocol.getRemoteManagedFlag() !=null){
			collectionProtocolBean.setRemoteManagedFlag(new Boolean(collectionProtocol.getRemoteManagedFlag()).booleanValue());
		}

		if (collectionProtocol.getDirtyEditFlag() !=null){
			collectionProtocolBean.setDirtyEditFlag(new Boolean(collectionProtocol.getDirtyEditFlag()).booleanValue());
		}
		collectionProtocolBean.getGridPrivilegeList().addAll(collectionProtocol.getGridGrouperPrivileges());
		return collectionProtocolBean;
	}

	/**
	 * Sets the basic cp props.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param collectionProtocolBean the collection protocol bean
	 */
	private static void setBasicCPProps(CollectionProtocol collectionProtocol,
			CollectionProtocolBean collectionProtocolBean)
	{
		collectionProtocolBean.setPrincipalInvestigatorId(collectionProtocol.getPrincipalInvestigator().getId().longValue());
		if (collectionProtocol.getIrbSite()!=null && collectionProtocol.getIrbSite().getId()!=null)
			collectionProtocolBean.setIrbSiteId(collectionProtocol.getIrbSite().getId().longValue());
		Date date = collectionProtocol.getStartDate();
		collectionProtocolBean.setStartDate(edu.wustl.common.util.Utility.parseDateToString(date, Constants.DATE_FORMAT) );
		collectionProtocolBean.setDescriptionURL(collectionProtocol.getDescriptionURL());
		collectionProtocolBean.setUnsignedConsentURLName(collectionProtocol.getUnsignedConsentDocumentURL());
		setLabelFormatProps(collectionProtocol, collectionProtocolBean);
		if(collectionProtocol.getConsentsWaived()==null)
		{
			collectionProtocol.setConsentsWaived(false);
		}
		collectionProtocolBean.setConsentWaived (collectionProtocol.getConsentsWaived());//.booleanValue());
		collectionProtocolBean.setIrbID(collectionProtocol.getIrbIdentifier());
		collectionProtocolBean.setTitle(collectionProtocol.getTitle());
		collectionProtocolBean.setShortTitle(collectionProtocol.getShortTitle());
		collectionProtocolBean.setEnrollment(String.valueOf(collectionProtocol.getEnrollment()));
		collectionProtocolBean.setConsentValues(prepareConsentTierMap(collectionProtocol.getConsentTierCollection()));
		collectionProtocolBean.setActivityStatus(collectionProtocol.getActivityStatus());
		collectionProtocolBean.setAliqoutInSameContainer(collectionProtocol.getAliquotInSameContainer());
		String endDate = Utility.parseDateToString(collectionProtocol.getEndDate(),CommonServiceLocator.getInstance().getDatePattern());
		collectionProtocolBean.setEndDate(endDate);
	}

	/**
	 * Sets label format properties.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param collectionProtocolBean the collection protocol bean
	 */
	private static void setLabelFormatProps(CollectionProtocol collectionProtocol,
			CollectionProtocolBean collectionProtocolBean)
	{
		collectionProtocolBean.setLabelFormat(collectionProtocol.getSpecimenLabelFormat());
		collectionProtocolBean.setDerivativeLabelFormat(collectionProtocol.getDerivativeLabelFormat());
		collectionProtocolBean.setAliquotLabelFormat(collectionProtocol.getAliquotLabelFormat());
	}

	/**
	 * Gets the protocol cordnate ids.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param coordinatorIds the coordinator ids
	 *
	 * @return the protocol cordnate ids
	 */
	private static long[] getProtocolCordnateIds(CollectionProtocol collectionProtocol,
			long[] coordinatorIds)
	{
		Collection<User> userCollection = collectionProtocol.getCoordinatorCollection();
		if(userCollection != null)
		{
			coordinatorIds = new long[userCollection.size()];
			int counter=0;
			Iterator<User> iterator = userCollection.iterator();
			while(iterator.hasNext())
			{
				User user = (User)iterator.next();
				coordinatorIds[counter] = user.getId().longValue();
				counter++;
			}
		}
		return coordinatorIds;
	}

	/**
	 * Returns string array of clinical diagnosis.
	 *
	 * @param collectionProtocol the collection protocol
	 *
	 * @return the clinical diagnosis
	 */
	private static String[] getClinicalDiagnosis(CollectionProtocol collectionProtocol)
	{
		String[] clinicalDiagnosisArr = null;
		Collection<ClinicalDiagnosis> clinicDiagnosisCollection = collectionProtocol.getClinicalDiagnosisCollection();
		if(clinicDiagnosisCollection != null)
		{
			clinicalDiagnosisArr = new String[clinicDiagnosisCollection.size()];
			int index=0;
			Iterator<ClinicalDiagnosis> iterator = clinicDiagnosisCollection.iterator();
			while(iterator.hasNext())
			{
				ClinicalDiagnosis clinicalDiagnosis = iterator.next();
				clinicalDiagnosisArr[index] = clinicalDiagnosis.getName();
				index++;
			}
		}
		return clinicalDiagnosisArr;
	}


	/**
	 * Prepare consent tier map.
	 *
	 * @param consentTierColl the consent tier coll
	 *
	 * @return the map
	 */
	public static Map prepareConsentTierMap(Collection<ConsentTier> consentTierColl)
	{
		Map tempMap = new LinkedHashMap();//bug 8905
		List<ConsentTier> consentsList = new ArrayList<ConsentTier>();
		if(consentTierColl!=null)
		{
			consentsList.addAll(consentTierColl);//bug 8905
			Collections.sort(consentsList,new IdComparator());//bug 8905
			//Iterator consentTierCollIter = consentTierColl.iterator();
			Iterator<ConsentTier> consentTierCollIter = consentsList.iterator();//bug 8905
			int counter = 0;
			while(consentTierCollIter.hasNext())
			{
				ConsentTier consent = consentTierCollIter.next();
				String statement = "ConsentBean:"+counter+"_statement";
				String statementkey = "ConsentBean:"+counter+"_consentTierID";
				tempMap.put(statement, consent.getStatement());
				tempMap.put(statementkey, String.valueOf(consent.getId()));
				counter++;
			}
		}
		return tempMap;
	}

	/**
	 * Gets the collection protocol event bean.
	 *
	 * @param collectionProtocolEvent the collection protocol event
	 * @param counter the counter
	 * @param dao the dao
	 *
	 * @return the collection protocol event bean
	 *
	 * @throws DAOException the DAO exception
	 */
	public static CollectionProtocolEventBean getCollectionProtocolEventBean(
			CollectionProtocolEvent  collectionProtocolEvent, int counter, DAO dao) throws DAOException
			{
		CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
		eventBean.setId(collectionProtocolEvent.getId().longValue());
		if(collectionProtocolEvent.getStudyCalendarEventPoint() != null)
		{
			eventBean.setStudyCalenderEventPoint(collectionProtocolEvent.getStudyCalendarEventPoint().toString());
		}
		eventBean.setCollectionPointLabel(collectionProtocolEvent.getCollectionPointLabel());
		eventBean.setClinicalDiagnosis(collectionProtocolEvent.getClinicalDiagnosis());
		eventBean.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
		eventBean.setId(collectionProtocolEvent.getId().longValue());
		eventBean.setUniqueIdentifier("E"+ counter++);
		eventBean.setSpecimenCollRequirementGroupId(
				collectionProtocolEvent.getId().longValue());
		eventBean.setSpecimenRequirementbeanMap(
				getSpecimensMap(collectionProtocolEvent.getSpecimenRequirementCollection(),
						eventBean.getUniqueIdentifier()) );

		eventBean.setLabelFormat(collectionProtocolEvent.getLabelFormat());
		eventBean.setSpecimenProcessingProcedure(getSPP(collectionProtocolEvent));

		return eventBean;
			}

	private static String[] getSPP(CollectionProtocolEvent collectionProtocolEvent)
	{
		String[] sppArr = null;
		Collection<SpecimenProcessingProcedure> sppCollection = collectionProtocolEvent.getSppCollection();
		if(sppCollection != null)
		{
			sppArr = new String[sppCollection.size()];
			int index=0;
			Iterator<SpecimenProcessingProcedure> iterator = sppCollection.iterator();
			while(iterator.hasNext())
			{
				SpecimenProcessingProcedure spp = iterator.next();
				sppArr[index] = spp.getName();
				index++;
			}
		}
		return sppArr;
	}

	/**
	 * Gets the sorted cp event list.
	 *
	 * @param genericList the generic list
	 *
	 * @return the sorted cp event list
	 */
	public static List getSortedCPEventList(List genericList)
	{
		//Comparator to sort the List of Map chronologically.
		final Comparator identifierComparator = new Comparator()
		{
			public int compare(Object object1, Object object2)
			{
				Long identifier1 = null;
				Long identifier2 = null;

				if(object1 instanceof CollectionProtocolEvent)
				{
					identifier1 = ((CollectionProtocolEvent)object1).getId();
					identifier2 = ((CollectionProtocolEvent)object2).getId();
				}
				else if(object1 instanceof SpecimenRequirement)
				{
					identifier1 = ((SpecimenRequirement)object1).getId();
					identifier2 = ((SpecimenRequirement)object2).getId();
				}
				else if (object1 instanceof AbstractSpecimen)
				{
					identifier1 = ((AbstractSpecimen) object1).getId();
					identifier2 = ((AbstractSpecimen) object2).getId();
				}

				if(identifier1!= null && identifier2 != null)
				{
					return identifier1.compareTo(identifier2);
				}
				if(identifier1 ==null)
				{
					return -1;
				}
				if(identifier2 == null)
				{
					return 1;
				}
				return 0;
			}
		};
		Collections.sort(genericList, identifierComparator);
		return genericList;
	}

	/**
	 * Gets the specimens map.
	 *
	 * @param reqSpecimenCollection the req specimen collection
	 * @param parentUniqueId the parent unique id
	 *
	 * @return the specimens map
	 */
	public static LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection<SpecimenRequirement> reqSpecimenCollection,
			String parentUniqueId)
			{
		LinkedHashMap<String, GenericSpecimen> reqSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		List<SpecimenRequirement> reqSpecimenList = new LinkedList<SpecimenRequirement>(reqSpecimenCollection);
		getSortedCPEventList(reqSpecimenList);
		Iterator<SpecimenRequirement> specimenIterator = reqSpecimenList.iterator();
		int specCtr=0;
		while(specimenIterator.hasNext())
		{
			SpecimenRequirement reqSpecimen = specimenIterator.next();
			if (reqSpecimen.getParentSpecimen() == null)
			{
				SpecimenRequirementBean specBean =getSpecimenBean(reqSpecimen, null, parentUniqueId, specCtr++);
				reqSpecimenMap.put(specBean.getUniqueIdentifier(), specBean);
			}
		}
		return reqSpecimenMap;
			}

	/**
	 * Gets the child aliquots.
	 *
	 * @param reqSpecimen the req specimen
	 * @param parentuniqueId the parentunique id
	 * @param parentName the parent name
	 *
	 * @return the child aliquots
	 */
	private static LinkedHashMap<String, GenericSpecimen> getChildAliquots(SpecimenRequirement reqSpecimen,
			String parentuniqueId, String parentName)
			{
		Collection reqSpecimenChildren = reqSpecimen.getChildSpecimenCollection();
		List reqSpecimenList = new LinkedList<SpecimenRequirement>(reqSpecimenChildren);
		getSortedCPEventList(reqSpecimenList);
		Iterator<SpecimenRequirement> iterator = reqSpecimenList.iterator();
		LinkedHashMap<String, GenericSpecimen>  aliquotMap = new
		LinkedHashMap<String, GenericSpecimen> ();
		int aliqCtr =1;

		while(iterator.hasNext())
		{
			SpecimenRequirement childReqSpecimen = iterator.next();
			if(Constants.ALIQUOT.equals(childReqSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean = getSpecimenBean(
						childReqSpecimen, parentName, parentuniqueId, aliqCtr++);
				aliquotMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}
		return aliquotMap;
			}

	/**
	 * Gets the child derived.
	 *
	 * @param specimen the specimen
	 * @param parentuniqueId the parentunique id
	 * @param parentName the parent name
	 *
	 * @return the child derived
	 */
	private static LinkedHashMap<String, GenericSpecimen> getChildDerived(SpecimenRequirement specimen,
			String parentuniqueId, String parentName)
			{
		Collection specimenChildren = specimen.getChildSpecimenCollection();
		List specimenList = new LinkedList(specimenChildren);
		getSortedCPEventList(specimenList);
		Iterator<SpecimenRequirement> iterator = specimenList.iterator();
		LinkedHashMap<String, GenericSpecimen>  derivedMap = new
		LinkedHashMap<String, GenericSpecimen> ();
		int deriveCtr=1;
		while(iterator.hasNext())
		{
			SpecimenRequirement childReqSpecimen = iterator.next();
			if(Constants.DERIVED_SPECIMEN.equals(childReqSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean =
					getSpecimenBean(childReqSpecimen, parentName,
							parentuniqueId, deriveCtr++);
				derivedMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}


		return derivedMap;
			}

	/**
	 * Gets the unique id.
	 *
	 * @param lineage the lineage
	 * @param ctr the ctr
	 *
	 * @return the unique id
	 */
	private static String getUniqueId(String lineage, int ctr)
	{	String constantVal=null;
	if(Constants.NEW_SPECIMEN.equals(lineage))
	{
		constantVal=Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + ctr;
	}

	else if(Constants.DERIVED_SPECIMEN.equals(lineage))
	{
		constantVal=Constants.UNIQUE_IDENTIFIER_FOR_DERIVE + ctr;
	}
	else if(Constants.ALIQUOT.equals(lineage))
	{
		constantVal= Constants.UNIQUE_IDENTIFIER_FOR_ALIQUOT + ctr;
	}
	return constantVal;
	}

	/**
	 * Gets the specimen bean.
	 *
	 * @param reqSpecimen the req specimen
	 * @param parentName the parent name
	 * @param parentUniqueId the parent unique id
	 * @param specCtr the spec ctr
	 *
	 * @return the specimen bean
	 */
	private static SpecimenRequirementBean getSpecimenBean(SpecimenRequirement reqSpecimen, String parentName,
			String parentUniqueId, int specCtr)
	{

		SpecimenRequirementBean speRequirementBean = new SpecimenRequirementBean();
		speRequirementBean.setId(reqSpecimen.getId().longValue());
		speRequirementBean.setLineage(reqSpecimen.getLineage());
		speRequirementBean.setUniqueIdentifier(
				parentUniqueId + getUniqueId(reqSpecimen.getLineage(),specCtr));

		speRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN
				+ "_" + speRequirementBean.getUniqueIdentifier());

		speRequirementBean.setClassName(reqSpecimen.getSpecimenClass());
		speRequirementBean.setType(reqSpecimen.getSpecimenType());
		speRequirementBean.setId(reqSpecimen.getId().longValue());

		if(reqSpecimen.getCreationEvent()!=null)
		{
			List contList = null;
			try {
				contList = AppUtility.executeSQLQuery("select caption from dyextn_container where identifier="+reqSpecimen.getCreationEvent().getContainerId());
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String containerName= (String) ((List) contList.get(0)).get(0);
			speRequirementBean.setCreationEventForSpecimen(getCreationEventNameToDisplay(reqSpecimen.getId(), containerName));
		}
		else
		{
			speRequirementBean.setCreationEventForSpecimen("Not Specified");
		}
		if(reqSpecimen.getProcessingSPP()!=null)
		{
			speRequirementBean.setProcessingSPPForSpecimen(reqSpecimen.getProcessingSPP().getName());
		}
		else
		{
			speRequirementBean.setProcessingSPPForSpecimen("Not Specified");
		}
		SpecimenCharacteristics characteristics = reqSpecimen.getSpecimenCharacteristics();
		updateSpeRequirementBean(reqSpecimen, speRequirementBean,
				characteristics);

		Double quantity = reqSpecimen.getInitialQuantity();

		if(quantity != null)
		{
			speRequirementBean.setQuantity(String.valueOf(quantity));
		}

		if(reqSpecimen.getStorageType() != null)
		{
			speRequirementBean.setStorageContainerForSpecimen(reqSpecimen.getStorageType());
		}
		//setSpecimenEventParameters(reqSpecimen,speRequirementBean );

		setAliquotAndDerivedColl(reqSpecimen, parentName, speRequirementBean);
		speRequirementBean.setLabelFormat(reqSpecimen.getLabelFormat());
		return speRequirementBean;
	}

	/**
	 * set Specimen Requirements.
	 *
	 * @param reqSpecimen the req specimen
	 * @param speRequirementBean the spe requirement bean
	 * @param characteristics the characteristics
	 */
	private static void updateSpeRequirementBean(
			SpecimenRequirement reqSpecimen,
			SpecimenRequirementBean speRequirementBean,
			SpecimenCharacteristics characteristics)
	{
		if(characteristics != null)
		{
			speRequirementBean.setTissueSite(characteristics.getTissueSite());
			speRequirementBean.setTissueSide(characteristics.getTissueSide());
		}

		speRequirementBean.setSpecimenCharsId(reqSpecimen.getSpecimenCharacteristics().getId().longValue());
		speRequirementBean.setPathologicalStatus(reqSpecimen.getPathologicalStatus());

		if(MOLECULAR_SPECIMEN_CLASS.equals(reqSpecimen.getSpecimenClass()))
		{
			Double concentration = ((MolecularSpecimenRequirement)reqSpecimen).getConcentrationInMicrogramPerMicroliter();
			if (concentration != null)
			{
				speRequirementBean.setConcentration( String.valueOf(concentration.doubleValue()));
			}
		}
	}

	/**
	 * Sets the aliquot and derived coll.
	 *
	 * @param reqSpecimen the req specimen
	 * @param parentName the parent name
	 * @param speRequirementBean the spe requirement bean
	 */
	private static void setAliquotAndDerivedColl(
			SpecimenRequirement reqSpecimen, String parentName,
			SpecimenRequirementBean speRequirementBean)
	{
		speRequirementBean.setParentName(parentName);

		LinkedHashMap<String, GenericSpecimen> aliquotMap =
			getChildAliquots(reqSpecimen, speRequirementBean.getUniqueIdentifier(),speRequirementBean.getDisplayName());
		LinkedHashMap<String, GenericSpecimen> derivedMap =
			getChildDerived(reqSpecimen, speRequirementBean.getUniqueIdentifier(), speRequirementBean.getDisplayName());

		Collection aliquotCollection = aliquotMap.values();
		Collection derivedCollection = derivedMap.values();
		//added method
		setQuantityPerAliquot(speRequirementBean, aliquotCollection);

		speRequirementBean.setNoOfAliquots(String.valueOf(aliquotCollection.size()));
		speRequirementBean.setAliquotSpecimenCollection(aliquotMap);

		speRequirementBean.setDeriveSpecimenCollection(derivedMap);
		speRequirementBean.setNoOfDeriveSpecimen(derivedCollection.size());
		derivedMap = getDerviredObjectMap(derivedMap.values());
		speRequirementBean.setDeriveSpecimen(derivedMap);
		setDeriveQuantity(speRequirementBean, derivedCollection);

	}


	/**
	 * set specimen requirement bean by DerivedCollection.
	 *
	 * @param speRequirementBean the spe requirement bean
	 * @param derivedCollection the derived collection
	 */
	private static void setDeriveQuantity(
			SpecimenRequirementBean speRequirementBean,
			Collection derivedCollection)
	{
		if (derivedCollection != null && !derivedCollection.isEmpty())
		{
			Iterator iterator = derivedCollection.iterator();
			GenericSpecimen derivedSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setDeriveClassName(derivedSpecimen.getClassName());
			speRequirementBean.setDeriveType(derivedSpecimen.getType());
			speRequirementBean.setDeriveConcentration(derivedSpecimen.getConcentration());
			speRequirementBean.setDeriveQuantity(derivedSpecimen.getQuantity());
		}
	}

	/**
	 * set specimen requirement bean by AliquotCollection.
	 *
	 * @param speRequirementBean the spe requirement bean
	 * @param aliquotCollection the aliquot collection
	 */
	private static void setQuantityPerAliquot(
			SpecimenRequirementBean speRequirementBean,
			Collection aliquotCollection)
	{
		if (aliquotCollection != null && !aliquotCollection.isEmpty())
		{
			Iterator iterator = aliquotCollection.iterator();
			GenericSpecimen aliquotSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setStorageContainerForAliquotSpecimem(
					aliquotSpecimen.getStorageContainerForSpecimen() );
			speRequirementBean.setCreationEventForAliquot(aliquotSpecimen.getCreationEventForSpecimen());
			speRequirementBean.setProcessingSPPForAliquot(aliquotSpecimen.getProcessingSPPForSpecimen());
			speRequirementBean.setQuantityPerAliquot(aliquotSpecimen.getQuantity());
		}
	}

	/**
	 * Gets the dervired object map.
	 *
	 * @param derivedCollection the derived collection
	 *
	 * @return the dervired object map
	 */
	public static LinkedHashMap getDerviredObjectMap(Collection<GenericSpecimen> derivedCollection)
	{
		LinkedHashMap<String, String> derivedObjectMap = new LinkedHashMap<String, String> ();
		Iterator<GenericSpecimen> iterator = derivedCollection.iterator();
		int deriveCtr=1;
		while (iterator.hasNext())
		{
			SpecimenRequirementBean derivedSpecimen = (SpecimenRequirementBean) iterator.next();

			StringBuffer derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_id");
			derivedObjectMap.put(derivedSpecimenKey.toString(), String.valueOf(derivedSpecimen.getId()));

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_specimenClass" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getClassName());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_specimenType" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getType());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_storageLocation" );
			derivedObjectMap.put(derivedSpecimenKey.toString(),
					derivedSpecimen.getStorageContainerForSpecimen());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_quantity" );
			String quantity = derivedSpecimen.getQuantity();
			derivedObjectMap.put(derivedSpecimenKey.toString(), quantity);

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_concentration" );

			derivedObjectMap.put(derivedSpecimenKey.toString(),
					derivedSpecimen.getConcentration());
			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_unit" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), "");

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_creationEvent" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getCreationEventForSpecimen());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_processingSPP" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getProcessingSPPForSpecimen());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_labelFormat" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getLabelFormat());

			deriveCtr++;
		}
		return derivedObjectMap;
	}

	/**
	 * Gets the key base.
	 *
	 * @param deriveCtr the derive ctr
	 *
	 * @return the key base
	 */
	private static StringBuffer getKeyBase(int deriveCtr)
	{
		StringBuffer derivedSpecimenKey = new StringBuffer();
		derivedSpecimenKey.append("DeriveSpecimenBean:");
		derivedSpecimenKey.append(String.valueOf(deriveCtr));
		return derivedSpecimenKey;
	}

	/**
	 * Sets the specimen event parameters.
	 *
	 * @param reqSpecimen the req specimen
	 * @param specimenRequirementBean the specimen requirement bean
	 */
	/*private  static void setSpecimenEventParameters(SpecimenRequirement reqSpecimen, SpecimenRequirementBean specimenRequirementBean)
	{
		Collection<SpecimenEventParameters> eventsParametersColl = reqSpecimen.getSpecimenEventCollection();
		if(eventsParametersColl == null || eventsParametersColl.isEmpty())
		{
			return;
		}

		Iterator<SpecimenEventParameters> iter = eventsParametersColl.iterator();

		//		while(iter.hasNext())
		//		{
		//			setSpecimenEvents(specimenRequirementBean, iter);
		//		}

	}
*/
	/**
	 * Update session.
	 *
	 * @param request the request
	 * @param identifieer the identifieer
	 *
	 * @throws ApplicationException the application exception
	 */
	public static void updateSession(HttpServletRequest request,  Long identifieer)
	throws ApplicationException{

		List sessionCpList = new CollectionProtocolBizLogic().retrieveCP(identifieer);

		if (sessionCpList == null || sessionCpList.size()<2){

			throw new ApplicationException(ErrorKey.getErrorKey("errors.item"),null,"Fail to retrieve Collection protocol..");
		}

		HttpSession session = request.getSession();
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.removeAttribute(Constants.GRID_ROW_ID_OBJECT_BEAN_MAP);
		setPrivilegesForCP(identifieer,session);
		CollectionProtocolBean collectionProtocolBean =
			(CollectionProtocolBean)sessionCpList.get(0);
		collectionProtocolBean.setOperation("update");
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN,
				sessionCpList.get(0));
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP,
				sessionCpList.get(1));
		String cptitle = collectionProtocolBean.getTitle();
		String treeNode = "cpName_"+cptitle;
		session.setAttribute(Constants.TREE_NODE_ID, treeNode);
		session.setAttribute("tempKey", treeNode);
		session.setAttribute(Constants.GRID_ROW_ID_OBJECT_BEAN_MAP, collectionProtocolBean.getGridPrivilegeList());
	}

	/**
	 * Sets the privileges for cp.
	 *
	 * @param cpId the cp id
	 * @param session the session
	 */
	private static  void setPrivilegesForCP(Long cpId,HttpSession session)
	{
		Map<String,SiteUserRolePrivilegeBean> map = CaTissuePrivilegeUtility.getPrivilegesOnCP(cpId);
		session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP, map);

	}


	/**
	 * Gets the collection protocol event map.
	 *
	 * @param collectionProtocolEventColl the collection protocol event coll
	 * @param dao the dao
	 *
	 * @return the collection protocol event map
	 *
	 * @throws DAOException the DAO exception
	 */
	public static  LinkedHashMap<String, CollectionProtocolEventBean> getCollectionProtocolEventMap(
			Collection collectionProtocolEventColl, DAO dao) throws DAOException
			{
		Iterator iterator = collectionProtocolEventColl.iterator();
		LinkedHashMap<String, CollectionProtocolEventBean> eventMap =
			new LinkedHashMap<String, CollectionProtocolEventBean>();
		int ctr=1;
		while(iterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent=
				(CollectionProtocolEvent)iterator.next();

			CollectionProtocolEventBean eventBean =
				CollectionProtocolUtil.getCollectionProtocolEventBean(collectionProtocolEvent,ctr++,dao);
			eventMap.put(eventBean.getUniqueIdentifier(), eventBean);
		}
		return eventMap;
			}

	/**
	 * Populate collection protocol objects.
	 *
	 * @param request the request
	 *
	 * @return the collection protocol
	 *
	 * @throws Exception the exception
	 */
	public static CollectionProtocol populateCollectionProtocolObjects(HttpServletRequest request)
	throws Exception
	{

		HttpSession session = request.getSession();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = (LinkedHashMap) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		if (cpEventMap == null)
		{
			throw AppUtility.getApplicationException(null, "event.req", "");
		}
		CollectionProtocol collectionProtocol = createCollectionProtocolDomainObject(collectionProtocolBean);
		Collection<CollectionProtocolEvent> collectionProtocolEventList = new LinkedHashSet<CollectionProtocolEvent>();

		Collection<CollectionProtocolEventBean> collectionProtocolEventBeanColl = cpEventMap.values();
		if (collectionProtocolEventBeanColl != null)
		{

			Iterator<CollectionProtocolEventBean> cpEventIterator = collectionProtocolEventBeanColl.iterator();

			while (cpEventIterator.hasNext()) {

				CollectionProtocolEventBean cpEventBean = cpEventIterator
				.next();
				CollectionProtocolEvent collectionProtocolEvent = getCollectionProtocolEvent(cpEventBean);
				collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
				collectionProtocolEventList.add(collectionProtocolEvent);
			}
		}
		collectionProtocol
		.setCollectionProtocolEventCollection(collectionProtocolEventList);
		return collectionProtocol;
	}



	/**
	 * Creates collection protocol domain object from given collection protocol bean.
	 *
	 * @param cpBean the cp bean
	 *
	 * @return the collection protocol
	 *
	 * @throws Exception the exception
	 */
	private static CollectionProtocol createCollectionProtocolDomainObject(
			CollectionProtocolBean cpBean) throws Exception
			{

		InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
		CollectionProtocol collectionProtocol = cpInstFact.createObject();
		collectionProtocol.setId(cpBean.getIdentifier());
		collectionProtocol.setSpecimenLabelFormat(cpBean.getLabelFormat());
		collectionProtocol.setDerivativeLabelFormat(cpBean.getDerivativeLabelFormat());
		collectionProtocol.setAliquotLabelFormat(cpBean.getAliquotLabelFormat());
		collectionProtocol.setActivityStatus(cpBean.getActivityStatus());
		collectionProtocol.setConsentsWaived(cpBean.isConsentWaived());
		collectionProtocol.setAliquotInSameContainer(cpBean.isAliqoutInSameContainer());
		collectionProtocol.setConsentTierCollection(CollectionProtocolUtility.prepareConsentTierCollection(cpBean.getConsentValues()));
		Collection<User> coordinatorCollection = new LinkedHashSet<User>();
		Collection<Site> siteCollection = new LinkedHashSet<Site>();
		Collection<ClinicalDiagnosis> clinicalDiagnosisCollection = new LinkedHashSet<ClinicalDiagnosis>();

		setCoordinatorColl(collectionProtocol,
				coordinatorCollection, cpBean);

		/**For Clinical Diagnosis Subset **/
		setClinicalDiagnosis(collectionProtocol,
				clinicalDiagnosisCollection, cpBean);

		setSiteColl(collectionProtocol, siteCollection, cpBean);

		collectionProtocol.setDescriptionURL(cpBean.getDescriptionURL());
		Integer enrollmentNo=null;
		try{
			enrollmentNo = Integer.valueOf(cpBean.getEnrollment());
		}
		catch(NumberFormatException e)
		{
			//CollectionProtocolUtil.LOGGER.error(e.getMessage(), e);
			enrollmentNo = Integer.valueOf(0);
		}
		collectionProtocol.setEnrollment(enrollmentNo);

		User principalInvestigator = instFact.createObject();
		principalInvestigator.setId(Long.valueOf(cpBean
				.getPrincipalInvestigatorId()));
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		if (cpBean.getIrbSiteId() > 0) {
			Site irbSite = (Site) DomainInstanceFactory.getInstanceFactory(
					Site.class).createObject();
			irbSite.setId(Long.valueOf(cpBean.getIrbSiteId()));
			collectionProtocol.setIrbSite(irbSite);
		} else {
			collectionProtocol.setIrbSite(null);
		}

		collectionProtocol.setShortTitle(cpBean.getShortTitle());
		Date startDate = Utility.parseDate(cpBean.getStartDate(), Utility
				.datePattern(cpBean.getStartDate()));
		collectionProtocol.setStartDate(startDate);
		collectionProtocol.setTitle(cpBean.getTitle());
		collectionProtocol.setUnsignedConsentDocumentURL(cpBean
				.getUnsignedConsentURLName());
		collectionProtocol.setIrbIdentifier(cpBean.getIrbID());

		collectionProtocol.setType(cpBean.getType());
		collectionProtocol.setSequenceNumber(cpBean.getSequenceNumber());
		if(!Validator.isEmpty(cpBean.getStudyCalendarEventPoint()))
		{
			collectionProtocol.setStudyCalendarEventPoint(Double.valueOf(cpBean.getStudyCalendarEventPoint()));
		}

		if(cpBean.getParentCollectionProtocolId() != null && cpBean.getParentCollectionProtocolId() != 0 && !"".equals(cpBean.getParentCollectionProtocolId()))
		{
			collectionProtocol.setParentCollectionProtocol(getParentCollectionProtocol(cpBean.getParentCollectionProtocolId()));
		}

		collectionProtocol.setRemoteManagedFlag(cpBean.isRemoteManagedFlag());
		collectionProtocol.setRemoteId(cpBean.getRemoteId()!=0?cpBean.getRemoteId():null);
		collectionProtocol.setDirtyEditFlag(cpBean.isDirtyEditFlag());

		return collectionProtocol;
			}

	/**
	 * This method will be called to return the CollectionProtocol.
	 *
	 * @param parentCollectionProtocolId : parentCollectionProtocolId.
	 *
	 * @return the collectionProtocol.
	 *
	 * @throws ApplicationException ApplicationException.
	 */
	public static CollectionProtocol getParentCollectionProtocol(Long parentCollectionProtocolId) throws ApplicationException
	{
		CollectionProtocol collectionProtocol = null;
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			collectionProtocol = (CollectionProtocol)dao.retrieveById(CollectionProtocol.class.getName(), parentCollectionProtocolId);

		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}

		return collectionProtocol;
	}

	/**
	 * Sets the site coll.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param siteCollection the site collection
	 * @param cpBean the cp bean
	 */
	private static void setSiteColl(
			CollectionProtocol collectionProtocol,
			Collection<Site> siteCollection,CollectionProtocolBean cpBean)
	{
		long[] siteArr = cpBean.getSiteIds();
		if (siteArr != null)
		{
			InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
			for (int i = 0; i < siteArr.length; i++) {
				if (siteArr[i] != -1) {
					Site site = instFact.createObject();
					site.setId(Long.valueOf(siteArr[i]));
					siteCollection.add(site);
				}
			}
			collectionProtocol.setSiteCollection(siteCollection);
		}
	}

	/**
	 * Sets the coordinator coll.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param coordinatorCollection the coordinator collection
	 * @param cpBean the cp bean
	 */
	private static void setCoordinatorColl(
			CollectionProtocol collectionProtocol,
			Collection<User> coordinatorCollection, CollectionProtocolBean cpBean)
	{
		long[] coordinatorsArr = cpBean.getCoordinatorIds();
		if (coordinatorsArr != null)
		{
			for (int i = 0; i < coordinatorsArr.length; i++) {
				if (coordinatorsArr[i] >= 1) {
					User coordinator = instFact.createObject();
					coordinator.setId(Long.valueOf(coordinatorsArr[i]));
					coordinatorCollection.add(coordinator);
				}
			}
			collectionProtocol.setCoordinatorCollection(coordinatorCollection);
		}
	}

	/**
	 * Sets the clinical diagnosis.
	 *
	 * @param collectionProtocol the collection protocol
	 * @param clinicalDiagnosis the clinical diagnosis
	 * @param cpBean the cp bean
	 */
	private static void setClinicalDiagnosis(
			CollectionProtocol collectionProtocol,
			Collection<ClinicalDiagnosis> clinicalDiagnosis, CollectionProtocolBean cpBean)
	{
		String[] clinicalDiagnosisArr = cpBean.getClinicalDiagnosis();


		if (clinicalDiagnosisArr != null)
		{
			for (int i = 0; i < clinicalDiagnosisArr.length; i++) {
				if (!"".equals(clinicalDiagnosisArr[i]))
				{
					final ClinicalDiagnosis clinicalDiagnosisObj = (ClinicalDiagnosis)DomainInstanceFactory.getInstanceFactory(ClinicalDiagnosis.class).createObject();//new ClinicalDiagnosis();
					clinicalDiagnosisObj.setName(clinicalDiagnosisArr[i]);
					clinicalDiagnosisObj.setCollectionProtocol(collectionProtocol);
					clinicalDiagnosis.add(clinicalDiagnosisObj);
				}
			}
			collectionProtocol.setClinicalDiagnosisCollection(clinicalDiagnosis);
		}
	}

	/**
	 * This function used to create CollectionProtocolEvent domain object
	 * from given CollectionProtocolEventBean Object.
	 *
	 * @param cpEventBean the cp event bean
	 *
	 * @return CollectionProtocolEvent domain object.
	 */
	private static CollectionProtocolEvent getCollectionProtocolEvent(
			CollectionProtocolEventBean cpEventBean)
	{
		InstanceFactory<CollectionProtocolEvent> cpeInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolEvent.class);
		CollectionProtocolEvent collectionProtocolEvent = cpeInstFact.createObject();//new CollectionProtocolEvent();
		collectionProtocolEvent.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setCollectionPointLabel(cpEventBean.getCollectionPointLabel());
		if(!Validator.isEmpty(cpEventBean.getStudyCalenderEventPoint()))
		{
			collectionProtocolEvent.setStudyCalendarEventPoint(Double.valueOf(cpEventBean.getStudyCalenderEventPoint()));
		}
		collectionProtocolEvent.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		collectionProtocolEvent.setClinicalDiagnosis(cpEventBean.getClinicalDiagnosis());

		Collection<SpecimenProcessingProcedure> sppCollection = new LinkedHashSet<SpecimenProcessingProcedure>();
		setSPP(collectionProtocolEvent,sppCollection,cpEventBean);
		if (cpEventBean.getId()==-1)
		{
			collectionProtocolEvent.setId(null);
		}
		else
		{
			collectionProtocolEvent.setId(Long.valueOf(cpEventBean.getId()));
		}

		Collection specimenCollection =null;
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();

		if (specimenMap!=null && !specimenMap.isEmpty())
		{
			specimenCollection =getReqSpecimens(specimenMap.values()
					,null, collectionProtocolEvent);
		}

		collectionProtocolEvent.setSpecimenRequirementCollection(specimenCollection);
		collectionProtocolEvent.setLabelFormat(cpEventBean.getLabelFormat());

		return collectionProtocolEvent;
	}


	private static void setSPP(
			CollectionProtocolEvent collectionProtocolEvent,
			Collection<SpecimenProcessingProcedure> sppColl, CollectionProtocolEventBean cpEventBean)
	{
		String[] sppArr = cpEventBean.getSpecimenProcessingProcedure();


		if (sppArr != null)
		{
			for (int i = 0; i < sppArr.length; i++) {
				if(sppMap.get(sppArr[i])==null )
				{
					if (!"".equals(sppArr[i]))
					{
						List sppList=null;
						try {
							DAO dao = AppUtility.openDAOSession(null);
							String hql = "from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure where name='"+sppArr[i]+"'";
							sppList = dao.executeQuery(hql);
							AppUtility.closeDAOSession(dao);
						} catch (ApplicationException e) {
							e.printStackTrace();	}
						if(sppList!=null && !sppList.isEmpty())
						{
							sppColl.add((SpecimenProcessingProcedure) sppList.get(0));
						}
					}
				}
				else
				{
					sppColl.add(sppMap.get(sppArr[i]));
				}
			}
			collectionProtocolEvent.setSppCollection(sppColl);
		}
	}



	/**
	 * creates collection of Specimen domain objects.
	 *
	 * @param specimenRequirementBeanColl the specimen requirement bean coll
	 * @param parentSpecimen the parent specimen
	 * @param cpEvent the cp event
	 *
	 * @return the req specimens
	 */
	public static Collection getReqSpecimens(Collection specimenRequirementBeanColl,
			SpecimenRequirement parentSpecimen, CollectionProtocolEvent cpEvent )
	{
		Collection<SpecimenRequirement> reqSpecimenCollection = new LinkedHashSet<SpecimenRequirement>();
		Iterator iterator = specimenRequirementBeanColl.iterator();
		while(iterator.hasNext())
		{
			SpecimenRequirementBean specimenRequirementBean =
				(SpecimenRequirementBean)iterator.next();
			SpecimenRequirement reqSpecimen = getSpecimenDomainObject(specimenRequirementBean);
			reqSpecimen.setParentSpecimen(parentSpecimen);
			if (parentSpecimen == null)
			{
				InstanceFactory<SpecimenCharacteristics> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCharacteristics.class);
				SpecimenCharacteristics specimenCharacteristics =instFact.createObject();
				//new SpecimenCharacteristics();
				long identifier =specimenRequirementBean.getSpecimenCharsId();
				if(identifier != -1)
				{
					specimenCharacteristics.setId(Long.valueOf(identifier));
				}
				specimenCharacteristics.setTissueSide(
						specimenRequirementBean.getTissueSide());
				specimenCharacteristics.setTissueSite(
						specimenRequirementBean.getTissueSite());
				reqSpecimen.setCollectionProtocolEvent(cpEvent);
				reqSpecimen.setSpecimenCharacteristics(specimenCharacteristics);
				//Collected and received events
				/*if(reqSpecimen.getId()==null)
				{
					setSpecimenEvents(reqSpecimen, specimenRequirementBean);
				}*/
			}
			else
			{
				reqSpecimen.setSpecimenCharacteristics(
						parentSpecimen.getSpecimenCharacteristics());
				//bug no. 7489
				//Collected and received events
				/*if(specimenRequirementBean.getCollectionEventContainer()!=null && specimenRequirementBean.getReceivedEventReceivedQuality()!=null)
				{
					if(reqSpecimen.getId()==null)
					{
						setSpecimenEvents(reqSpecimen, specimenRequirementBean);
					}
				}
				else
				{*/
					reqSpecimen.setSpecimenEventCollection(
							parentSpecimen.getSpecimenEventCollection());
				//}
			}
			reqSpecimen.setLineage(specimenRequirementBean.getLineage());
			reqSpecimenCollection.add(reqSpecimen);
			Map aliquotColl = (LinkedHashMap)specimenRequirementBean.getAliquotSpecimenCollection();
			Collection childSpecimens = new HashSet();
			if(aliquotColl!=null && !aliquotColl.isEmpty())
			{
				Collection aliquotCollection= specimenRequirementBean.getAliquotSpecimenCollection().values();
				childSpecimens  =
					getReqSpecimens(aliquotCollection, reqSpecimen, cpEvent);
				reqSpecimenCollection.addAll(childSpecimens);
			}
			Map drivedColl = (LinkedHashMap)specimenRequirementBean.getDeriveSpecimenCollection();
			if(drivedColl!=null && !drivedColl.isEmpty())
			{
				Collection derivedCollection= specimenRequirementBean.getDeriveSpecimenCollection().values();

				Collection derivedSpecimens =
					getReqSpecimens(derivedCollection, reqSpecimen, cpEvent);
				if(childSpecimens == null || childSpecimens.isEmpty())
				{
					childSpecimens = derivedSpecimens;
				}
				else
				{
					childSpecimens.addAll(derivedSpecimens);
				}
				reqSpecimenCollection.addAll(childSpecimens);
			}
			reqSpecimen.setChildSpecimenCollection(childSpecimens);
		}
		return reqSpecimenCollection;
	}




	/**
	 * Sets the specimen events.
	 *
	 * @param reqSpecimen the req specimen
	 * @param specimenRequirementBean the specimen requirement bean
	 */
	/*private static  void setSpecimenEvents(SpecimenRequirement reqSpecimen, SpecimenRequirementBean specimenRequirementBean)
	{
		//seting collection event values
		Collection<SpecimenEventParameters> specimenEventCollection =
			new LinkedHashSet<SpecimenEventParameters>();
		//
		//		if(specimenRequirementBean.getCollectionEventContainer()!=null)
		//		{
		//			CollectionEventParameters collectionEvent = (CollectionEventParameters)DomainInstanceFactory.getInstanceFactory(CollectionEventParameters.class).createObject();//new CollectionEventParameters();
		//			collectionEvent.setCollectionProcedure(specimenRequirementBean.getCollectionEventCollectionProcedure());
		//			collectionEvent.setContainer(specimenRequirementBean.getCollectionEventContainer());
		//			User collectionEventUser = instFact.createObject();
		//			collectionEventUser.setId(Long.valueOf(specimenRequirementBean.getCollectionEventUserId()));
		//			collectionEvent.setUser(collectionEventUser);
		//			collectionEvent.setSpecimen(reqSpecimen);
		//			specimenEventCollection.add(collectionEvent);
		//		}

		//setting received event values

		//		if(specimenRequirementBean.getReceivedEventReceivedQuality()!=null)
		//		{
		//			ReceivedEventParameters receivedEvent = (ReceivedEventParameters)DomainInstanceFactory.getInstanceFactory(ReceivedEventParameters.class).createObject();//new ReceivedEventParameters();
		//			receivedEvent.setReceivedQuality(specimenRequirementBean.getReceivedEventReceivedQuality());
		//			User receivedEventUser = instFact.createObject() ;
		//			receivedEventUser.setId(Long.valueOf(specimenRequirementBean.getReceivedEventUserId()));
		//			receivedEvent.setUser(receivedEventUser);
		//			receivedEvent.setSpecimen(reqSpecimen);
		//			specimenEventCollection.add(receivedEvent);
		//		}

		reqSpecimen.setSpecimenEventCollection(specimenEventCollection);

	}
*/

	static Map<String,SpecimenProcessingProcedure> sppMap=new HashMap<String,SpecimenProcessingProcedure>();
	/**
	 * creates specimen domain object from given specimen requirement bean.
	 *
	 * @param specimenRequirementBean the specimen requirement bean
	 *
	 * @return the specimen domain object
	 * @throws ApplicationException
	 * @throws BizLogicException
	 */
	private static SpecimenRequirement getSpecimenDomainObject(SpecimenRequirementBean specimenRequirementBean)
	{
//		NewSpecimenForm form = new NewSpecimenForm();
		String specimenClass = specimenRequirementBean.getClassName();
		SpecimenRequirement reqSpecimen=null;
		try
		{
			if(specimenClass.equals("Tissue"))
			{
				InstanceFactory<TissueSpecimenRequirement> instFact = DomainInstanceFactory.getInstanceFactory(TissueSpecimenRequirement.class);
				reqSpecimen = instFact.createObject();
				//reqSpecimen = new TissueSpecimenRequirement();
			}
			else if(specimenClass.equals("Fluid"))
			{
				InstanceFactory<FluidSpecimenRequirement> instFact = DomainInstanceFactory.getInstanceFactory(FluidSpecimenRequirement.class);
				reqSpecimen = instFact.createObject();
				//reqSpecimen = new FluidSpecimenRequirement();
			}
			else if(specimenClass.equals("Cell"))
			{
				InstanceFactory<CellSpecimenRequirement> instFact = DomainInstanceFactory.getInstanceFactory(CellSpecimenRequirement.class);
				reqSpecimen = instFact.createObject();//new CellSpecimenRequirement();

			}
			else if(specimenClass.equals("Molecular"))
			{
				InstanceFactory<MolecularSpecimenRequirement> instFact = DomainInstanceFactory.getInstanceFactory(MolecularSpecimenRequirement.class);
				reqSpecimen = instFact.createObject();
				//reqSpecimen = new MolecularSpecimenRequirement();
			}
		}
		catch (Exception e1)
		{
			//CollectionProtocolUtil.LOGGER.error("Error in setting Section " +
			//		"header Priorities"+e1.getMessage(),e1);
			return null;
		}

		if (specimenRequirementBean.getId()==-1)
		{
			reqSpecimen.setId(null);
		}
		else
		{
			reqSpecimen.setId(Long.valueOf(specimenRequirementBean.getId()));
		}
		//		/reqSpecimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		reqSpecimen.setInitialQuantity(new Double(specimenRequirementBean.getQuantity()));
		reqSpecimen.setLineage(specimenRequirementBean.getLineage());
		reqSpecimen.setPathologicalStatus(specimenRequirementBean.getPathologicalStatus());
		reqSpecimen.setSpecimenType(specimenRequirementBean.getType());
		String storageType = specimenRequirementBean.getStorageContainerForSpecimen();
		if(specimenRequirementBean.getClassName().equalsIgnoreCase(Constants.MOLECULAR))
		{
			((MolecularSpecimenRequirement)reqSpecimen).setConcentrationInMicrogramPerMicroliter(new Double(specimenRequirementBean.getConcentration()));
		}
		reqSpecimen.setStorageType(storageType);
		reqSpecimen.setSpecimenClass(specimenRequirementBean.getClassName());
		reqSpecimen.setLabelFormat(specimenRequirementBean.getLabelFormat());
		if(specimenRequirementBean.getCreationEventForSpecimen()!=null && !specimenRequirementBean.getCreationEventForSpecimen().equalsIgnoreCase("Not Specified")
				&& !specimenRequirementBean.getCreationEventForSpecimen().equalsIgnoreCase("-- Select --"))
		{
			List<SpecimenProcessingProcedure> sppList=null;
			List<Action> actionList=null;
			Map<String,Long> map=new HashMap<String,Long>();
			new SPPBizLogic().getAllSPPEventFormNames(map);
			String[] sppWithEvent=specimenRequirementBean.getCreationEventForSpecimen().split(":");
			Long contId=null;
			if(sppWithEvent.length==3)
			{
				contId=map.get(sppWithEvent[2].trim());
				DAO dao =null;
				try {
					dao = AppUtility.openDAOSession(null);
					String hql = "from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure where name='"+sppWithEvent[0].trim()+"'";
					sppList = dao.executeQuery(hql);
					if(sppList!=null && !sppList.isEmpty())
					{
						Action action=null;
						SpecimenProcessingProcedure spp =sppList.get(0);
						Iterator<Action> iter=spp.getActionCollection().iterator();
						while(iter.hasNext())
						{
							action = iter.next();
							NameValueBean nvb=SpecimenEventsUtility.getDynamicEntity(action.getContainerId());
							if(nvb.getValue().equalsIgnoreCase(sppWithEvent[2].trim().replace(" ", ""))
									&&  action.getActionOrder().equals(Long.valueOf(sppWithEvent[1].trim())))
							{
								break;
							}
						}
						reqSpecimen.setCreationEvent(action);
					}
					AppUtility.closeDAOSession(dao);
				} catch (ApplicationException e) {
					e.printStackTrace();	} catch (DynamicExtensionsSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DynamicExtensionsApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						try {
							AppUtility.closeDAOSession(dao);
						} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			}
			else
			{
				contId=map.get(sppWithEvent[0].trim());
			}
			/*try {
				DAO dao = AppUtility.openDAOSession(null);
				String hql = "from edu.wustl.catissuecore.domain.processingprocedure.Action where containerId="+contId;
				actionList = dao.executeQuery(hql);
				AppUtility.closeDAOSession(dao);
			} catch (ApplicationException e) {
				e.printStackTrace();	}
			if(actionList!=null && !actionList.isEmpty()){
				reqSpecimen.setCreationEvent(actionList.get(0));
			}
			else
			{
				reqSpecimen.setCreationEvent(null);
			}*/
		}
		else
		{
			reqSpecimen.setCreationEvent(null);
		}
		if(!Validator.isEmpty(specimenRequirementBean.getProcessingSPPForSpecimen())&& !specimenRequirementBean.getProcessingSPPForSpecimen().equalsIgnoreCase("Not Specified")
				&& !specimenRequirementBean.getProcessingSPPForSpecimen().equalsIgnoreCase("-- Select --"))
		{
			if(sppMap.get(specimenRequirementBean.getProcessingSPPForSpecimen())==null)
			{
				List<SpecimenProcessingProcedure> sppList=null;
				try {
					DAO dao = AppUtility.openDAOSession(null);
					String hql = "from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure where name= \'"+specimenRequirementBean.getProcessingSPPForSpecimen()+"\'";
					sppList = dao.executeQuery(hql);
					AppUtility.closeDAOSession(dao);
					sppMap.put(specimenRequirementBean.getProcessingSPPForSpecimen(), sppList.get(0));

				}
				catch (ApplicationException e) {
					e.printStackTrace();
				}
			}
			reqSpecimen.setProcessingSPP(sppMap.get(specimenRequirementBean.getProcessingSPPForSpecimen()));
		}
		else
		{
			reqSpecimen.setProcessingSPP(null);
		}
		return reqSpecimen;
	}

	/**
	 * Gets the collection protocol for scg.
	 *
	 * @param identifier the identifier
	 *
	 * @return the collection protocol for scg
	 *
	 * @throws ApplicationException the application exception
	 */
	public static CollectionProtocol getCollectionProtocolForSCG(String identifier) throws ApplicationException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		String sourceObjectName =  SpecimenCollectionGroup.class.getName();
		String[] whereColName = {"id"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {Long.parseLong(identifier)};
		String [] selectColumnName = {"collectionProtocolRegistration.collectionProtocol"};
		List list = collectionProtocolBizLogic.retrieve(sourceObjectName,selectColumnName,whereColName,whereColCond,whereColVal,Constants.AND_JOIN_CONDITION);
		CollectionProtocol returnVal=null;
		if(list != null && !list.isEmpty())
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) list.get(0);
			returnVal= collectionProtocol;

		}
		return returnVal ;
	}
	//bug 8905
	/**
	 * This method is used to sort consents according to id.
	 *
	 * @param consentsMap the consents map
	 *
	 * @return sorted map
	 */
	public static Map sortConsentMap(Map consentsMap)
	{
		Set keys = consentsMap.keySet();
		List<String> idList = new ArrayList<String>();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext())
		{
			String key = (String)iterator.next();
			idList.add(key);
		}
		Collections.sort(idList,new IdComparator());
		Map idMap = new LinkedHashMap();
		Iterator idIterator = idList.iterator();
		while(idIterator.hasNext())
		{
			String identifier = (String)idIterator.next();
			idMap.put(identifier,consentsMap.get(identifier));
		}
		return idMap;
	}

	/**
	 * Update the Clinical Diagnosis value.
	 *
	 * @param request request
	 * @param collectionProtocolBean collectionProtocolBean
	 */
	public static  void updateClinicalDiagnosis(HttpServletRequest request,
			final CollectionProtocolBean collectionProtocolBean)
	{
		if(collectionProtocolBean != null)
		{
			Collection<NameValueBean> clinicalDiagnosisBean = new LinkedHashSet<NameValueBean>();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();

			Object[] clinicalDiagnosis =  collectionProtocolBean.getClinicalDiagnosis();
			if(clinicalDiagnosis != null)
			{
				for (int i=0 ;i<clinicalDiagnosis.length;i++)
				{
					NameValueBean nvb = new NameValueBean(clinicalDiagnosis[i],clinicalDiagnosis[i]) ;

					nvb.getName().toLowerCase(locale);

					clinicalDiagnosisBean.add(nvb);

				}
			}
			Collection<NameValueBean> clinicalDiagnosisBeans = new ArrayList<NameValueBean>();
			clinicalDiagnosisBeans.addAll(clinicalDiagnosisBean);
			request.setAttribute(edu.common.dynamicextensions.ui.util.Constants.SELECTED_VALUES, clinicalDiagnosisBeans);
		}

	}

	/**
	 * returns the errors.
	 *
	 * @param request gives the error key.
	 *
	 * @return errors.
	 */
	public static ActionErrors getActionErrors(HttpServletRequest request)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		return errors;
	}

	/**
	 * This method will return collectionProtocolId.
	 *
	 * @param specimenId - specimen id
	 * @param dao - DAO obj
	 *
	 * @return collectionProtocolId - collectionProtocolId
	 *
	 * @throws BizLogicException - BizLogicException
	 */
	public static String getCPIdFromSpecimen(String specimenId,DAO dao) throws BizLogicException
	{
		String collectionProtocolId = "";
		if (specimenId != null && !specimenId.trim().equals(""))
		{
			InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
			final Specimen specimen = instFact.createObject();//new Specimen();
			specimen.setId(Long.parseLong(specimenId));
			try
			{
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
				collectionProtocolId = newSpecimenBizLogic.getObjectId(dao, specimen);
				if (collectionProtocolId != null && !collectionProtocolId.trim().equals(""))
				{
					collectionProtocolId = collectionProtocolId.split("_")[1];
				}
			}
			catch (final ApplicationException appEx)
			{
				collectionProtocolId = "";
				throw new BizLogicException(appEx.getErrorKey(), appEx, appEx.getMsgValues());
			}
		}
		return collectionProtocolId;
	}

	public static String getCreationEventNameToDisplay(Long id, String creationEvent) {
		List sppNameList = null;
		String nameToReturn = null;
		//Action action=null;
		try {
			sppNameList = AppUtility.executeSQLQuery("SELECT SPP.NAME FROM CATISSUE_ACTION ACTION,CATISSUE_CP_REQ_SPECIMEN REQ,CATISSUE_SPP SPP"
					+" WHERE "
					+" REQ.IDENTIFIER="
					+id
					+" AND REQ.ACTION_IDENTIFIER=ACTION.IDENTIFIER"
					+" AND ACTION.SPP_IDENTIFIER = SPP.IDENTIFIER");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		if(sppNameList!=null && !sppNameList.isEmpty())
		{
			try {
				List actionOrderList = null;
				String sppName=(String)((List)sppNameList.get(0)).get(0);
				actionOrderList=AppUtility.executeSQLQuery("SELECT ACTION.ACTION_ORDER FROM CATISSUE_ACTION ACTION,CATISSUE_CP_REQ_SPECIMEN REQ"
						+" WHERE "
						+" REQ.IDENTIFIER="
						+id
						+" AND REQ.ACTION_IDENTIFIER=ACTION.IDENTIFIER");

				String actionOrder=(String)((List)actionOrderList.get(0)).get(0);
				nameToReturn = sppName+" : "+actionOrder+ " : "+creationEvent;
			} catch (ApplicationException e) {
				e.printStackTrace();
			}

		}
		return nameToReturn;
	}

}
