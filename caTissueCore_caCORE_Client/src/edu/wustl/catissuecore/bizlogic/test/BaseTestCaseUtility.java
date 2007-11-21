package edu.wustl.catissuecore.bizlogic.test;



import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class BaseTestCaseUtility {
	
	public static CollectionProtocol initCollectionProtocol()
	{
	    CollectionProtocol collectionProtocol = new CollectionProtocol();
	    
	    Collection consentTierColl = new HashSet();
		ConsentTier c1 = new ConsentTier();
		c1.setStatement("Consent for aids research");
		consentTierColl.add(c1);
		ConsentTier c2 = new ConsentTier();
		c2.setStatement("Consent for cancer research");
		consentTierColl.add(c2);		
		ConsentTier c3 = new ConsentTier();
		c3.setStatement("Consent for Tb research");
		consentTierColl.add(c3);
		
		collectionProtocol.setConsentTierCollection(consentTierColl);
		collectionProtocol.setAliquotInSameContainer(new Boolean(true));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777");
		collectionProtocol.setTitle("coll proto"+UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setShortTitle("pc"+UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setEnrollment(2);
		
		System.out.println("reached setUp");
		
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/2003", Utility.datePattern("08/15/1975")));
		
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}

		Collection collectionProtocolEventList = new LinkedHashSet();
		CollectionProtocolEvent collectionProtocolEvent = null;
		for(int specimenEventCount = 0 ;specimenEventCount<2 ;specimenEventCount++)
		{
			collectionProtocolEvent= new CollectionProtocolEvent();
			setCollectionProtocolEvent(collectionProtocolEvent);
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			collectionProtocolEventList.add(collectionProtocolEvent);
		}
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList);

		User principalInvestigator = new User();		
		principalInvestigator.setId(new Long("1"));		
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
		//User protocolCordinator = new User();
		//protocolCordinator.setId(new Long("2"));
		
		Collection protocolCordinatorCollection = new HashSet();
		//protocolCordinatorCollection.add(protocolCordinator);
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		
		return collectionProtocol;
		
	}
	
	private static void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));
		collectionProtocolEvent.setCollectionPointLabel("Pre Study" + UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocolEvent.setClinicalStatus("Operative");		
		SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = new SpecimenCollectionRequirementGroup();
		specimenCollectionRequirementGroup.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimenCollectionRequirementGroup.setClinicalDiagnosis("Abdominal fibromatosis");
		specimenCollectionRequirementGroup.setClinicalStatus("Operative");
		collectionProtocolEvent.setRequiredCollectionSpecimenGroup(specimenCollectionRequirementGroup);
		
		Collection specimenCollection =null;
		CollectionProtocolEventBean cpEventBean = new CollectionProtocolEventBean();
		SpecimenRequirementBean specimenRequirementBean = createSpecimenBean();
		
		cpEventBean.addSpecimenRequirementBean(specimenRequirementBean);
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();
		if (specimenMap!=null && !specimenMap.isEmpty())
		{
			specimenCollection =edu.wustl.catissuecore.util.CollectionProtocolUtil.getSpecimens(
					specimenMap.values()
					,null, specimenCollectionRequirementGroup);	
		}
		specimenCollectionRequirementGroup.setSpecimenCollection(specimenCollection);
	}
	
	private static SpecimenRequirementBean createSpecimenBean()
	{
		SpecimenRequirementBean specimenRequirementBean = createSpecimen();
		
		Map aliquotSpecimenMap = (Map)getChildSpecimenMap("Aliquot");
		Map deriveSpecimenMap = (Map)getChildSpecimenMap("Derived");
		specimenRequirementBean.setAliquotSpecimenCollection((LinkedHashMap)aliquotSpecimenMap);
		specimenRequirementBean.setDeriveSpecimenCollection((LinkedHashMap)deriveSpecimenMap);
		return specimenRequirementBean;
	}
	private static SpecimenRequirementBean createSpecimen()
	{
		SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
		specimenRequirementBean.setParentName("Specimen_E1");
		specimenRequirementBean.setUniqueIdentifier("E1_S0");
		specimenRequirementBean.setDisplayName("Specimen_E1_S0");
		specimenRequirementBean.setLineage("New");
		specimenRequirementBean.setClassName("Tissue");
		specimenRequirementBean.setType("Fixed Tissue");
		specimenRequirementBean.setTissueSite("Accessory sinus, NOS");
		specimenRequirementBean.setTissueSide("Left");
		specimenRequirementBean.setPathologicalStatus("Malignant, Invasive");
		specimenRequirementBean.setConcentration("0");
		specimenRequirementBean.setQuantity("10");
		specimenRequirementBean.setStorageContainerForSpecimen("Auto");
	
		//Collected and received events
		specimenRequirementBean.setCollectionEventUserId(1);
		specimenRequirementBean.setReceivedEventUserId(1);
		specimenRequirementBean.setCollectionEventContainer("Heparin Vacutainer");
		specimenRequirementBean.setReceivedEventReceivedQuality("Cauterized");
		specimenRequirementBean.setCollectionEventCollectionProcedure("Lavage");
		
		//Aliquot
		specimenRequirementBean.setNoOfAliquots("2");
		specimenRequirementBean.setQuantityPerAliquot("1");
		specimenRequirementBean.setStorageContainerForAliquotSpecimem("Auto");
		
		specimenRequirementBean.setNoOfDeriveSpecimen(1);
		specimenRequirementBean.setDeriveSpecimen(null);
		return specimenRequirementBean;
	}

	private static Map getChildSpecimenMap(String lineage)
	{
		String noOfAliquotes = "2";
		String quantityPerAliquot = "1";
		Map aliquotMap = new LinkedHashMap();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);
		Double parentQuantity = Double.parseDouble("10");
		Double aliquotQuantity=0D;
		if(quantityPerAliquot==null||quantityPerAliquot.equals(""))
		{
			aliquotQuantity = parentQuantity/aliquotCount;
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		}
		else
		{
			aliquotQuantity=Double.parseDouble(quantityPerAliquot); ;
			parentQuantity = parentQuantity - (aliquotQuantity*aliquotCount);
		}
		System.out.println(aliquotCount+":aliquotCount");
		for(int iCount=1; iCount<= 2; iCount++)
		{
			SpecimenRequirementBean specimenRequirementBean = createSpecimen();
			specimenRequirementBean.setParentName("Specimen_E1_S0");
			specimenRequirementBean.setUniqueIdentifier("E1_S0_A1");
			specimenRequirementBean.setDisplayName("Specimen_E1_S0_A1");
			specimenRequirementBean.setLineage(lineage);
			if(quantityPerAliquot==null || quantityPerAliquot.equals(""))
			{
				quantityPerAliquot="0";
			}
			specimenRequirementBean.setQuantity(quantityPerAliquot);
			specimenRequirementBean.setNoOfAliquots(null);
			specimenRequirementBean.setQuantityPerAliquot(null);
			specimenRequirementBean.setStorageContainerForAliquotSpecimem(null);
			specimenRequirementBean.setStorageContainerForSpecimen("Auto");
			specimenRequirementBean.setDeriveSpecimen(null);
			aliquotMap.put(iCount, specimenRequirementBean);
		}
		System.out.println(aliquotMap.size()+":aliquotMap.size()");
		return aliquotMap;
	}

	
	public static void updateCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		collectionProtocol.setIrbIdentifier("abcdef");
		collectionProtocol.setShortTitle("PC1"+UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active"); //Active
		//collectionProtocol.setEndDate(null);
		collectionProtocol.setTitle("cp updated title" + UniqueKeyGeneratorUtil.getUniqueKey());
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	public static CollectionProtocolEvent initCollectionProtocolEvent()
	{
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();		
		collectionProtocolEvent.setClinicalStatus("New Diagnosis");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));		
	 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		specimenRequirementCollection.add(specimenRequirement);
		
//		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		return collectionProtocolEvent;
		
	}
	
	public static User initUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("ddd"+ UniqueKeyGeneratorUtil.getUniqueKey()+"@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("Alabama");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");	 
		
		
		userObj.setAddress(address);
		
		Institution inst = new Institution();
		inst.setId(new Long(1));
		userObj.setInstitution(inst);
		
		Department department = new Department();
		department.setId(new Long(1));
		userObj.setDepartment(department);
		
		CancerResearchGroup cancerResearchGroup =  new CancerResearchGroup();
		cancerResearchGroup.setId(new Long(1));
		userObj.setCancerResearchGroup(cancerResearchGroup);
		
		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");
		userObj.setPageOf(Constants.PAGEOF_SIGNUP);

		return userObj;
	}
	
	public static User initUpdateUser(User userObj)
	{
		userObj.setEmailAddress("sup"+UniqueKeyGeneratorUtil.getUniqueKey()+"@sup.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("Alabama");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");	 
		
		
		userObj.setAddress(address);
		
		Institution inst = (Institution)TestCaseUtility.getObjectMap(Institution.class); 
		//new Institution();
		//inst.setId(new Long(1));
		
		userObj.setInstitution(inst);
		
		Department department = (Department)TestCaseUtility.getObjectMap(Department.class); 
		//new Department();
		//department.setId(new Long(1));
		userObj.setDepartment(department);
		
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)TestCaseUtility.getObjectMap(CancerResearchGroup.class);  
		//new CancerResearchGroup();
		//cancerResearchGroup.setId(new Long(1));
		userObj.setCancerResearchGroup(cancerResearchGroup);
		
		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");

		return userObj;
	}
	
	public static Department initDepartment()
	{
		Department dept =new Department();
		dept.setName("department name"+ UniqueKeyGeneratorUtil.getUniqueKey());
		return dept;
	}
	public static void updateDepartment(Department department)
	{
		department.setName("dt"+UniqueKeyGeneratorUtil.getUniqueKey());
	}
	
	public static SpecimenCollectionGroup initSpecimenCollectionGroup()
	{
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setCollectionStatus("Completed");
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class);
		//Site site = new Site();
		//site.setId(new Long(1));
		specimenCollectionGroup.setSpecimenCollectionSite(site);

		specimenCollectionGroup.setClinicalDiagnosis("Abdominal fibromatosis");
		specimenCollectionGroup.setClinicalStatus("Operative");
		specimenCollectionGroup.setActivityStatus("Active");

		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		collectionProtocolEvent.setId(new Long(1));
		specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocolEvent);

		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration)TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
		
		//new CollectionProtocolRegistration();
		//collectionProtocolRegistration.setId(new Long(3));
		Participant participant =(Participant)TestCaseUtility.getObjectMap(Participant.class); 
		//new Participant();
		//participant.setId(new Long(1));
		collectionProtocolRegistration.setParticipant(participant);
		
		CollectionProtocol collectionProt = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
		//new CollectionProtocol();
		//collectionProt.setId(new Long(1));
		
		collectionProtocolRegistration.setCollectionProtocol(collectionProt);
		//collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);

		specimenCollectionGroup.setName("Collection Protocol1_1_1.1.1");
		//Setting Consent Tier Status.
		Collection consentTierStatusCollection = new HashSet();
		
		ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(1));
		consentTierStatus.setConsentTier(consentTier);
		consentTierStatus.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus);
		
		ConsentTierStatus  consentTierStatus1 = new ConsentTierStatus();		
		ConsentTier consentTier1 = new ConsentTier();
		consentTier1.setId(new Long(2));
		consentTierStatus1.setConsentTier(consentTier1);
		consentTierStatus1.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus1);
		
		ConsentTierStatus  consentTierStatus2 = new ConsentTierStatus();		
		ConsentTier consentTier2 = new ConsentTier();
		consentTier2.setId(new Long(3));
		consentTierStatus2.setConsentTier(consentTier2);
		consentTierStatus2.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus2);
		
		specimenCollectionGroup.setConsentTierStatusCollection(consentTierStatusCollection);
		
		return specimenCollectionGroup;
	}
	
	public static SpecimenCollectionGroup updateSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class);
		//Site site = new Site();
		//site.setId(new Long(1));
		specimenCollectionGroup.setSpecimenCollectionSite(site);

		specimenCollectionGroup.setClinicalDiagnosis("Abdominal fibromatosis");
		specimenCollectionGroup.setClinicalStatus("Operative");
		specimenCollectionGroup.setActivityStatus("Active");

		CollectionProtocolEvent collectionProtocol = new CollectionProtocolEvent();
		collectionProtocol.setId(new Long(21));
		specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocol);

		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		Participant participant = (Participant)TestCaseUtility.getObjectMap(Participant.class); 
		//new Participant();
		//participant.setId(new Long(1));
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setId(new Long(5));
		CollectionProtocol collectionProt = new CollectionProtocol();
		collectionProt.setId(new Long(21));
		
		collectionProtocolRegistration.setCollectionProtocol(collectionProt);
		//collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);

		specimenCollectionGroup.setName("Collection Protocol1_1_1.1.1");

		
		//Setting Consent Tier Status.
		Collection consentTierStatusCollection = new HashSet();
		
		ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(21));
		consentTierStatus.setConsentTier(consentTier);
		consentTierStatus.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus);
		
		ConsentTierStatus  consentTierStatus1 = new ConsentTierStatus();		
		ConsentTier consentTier1 = new ConsentTier();
		consentTier1.setId(new Long(22));
		consentTierStatus1.setConsentTier(consentTier1);
		consentTierStatus1.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus1);
		
		ConsentTierStatus  consentTierStatus2 = new ConsentTierStatus();		
		ConsentTier consentTier2 = new ConsentTier();
		consentTier2.setId(new Long(23));
		consentTierStatus2.setConsentTier(consentTier2);
		consentTierStatus2.setStatus("Yes");
		consentTierStatusCollection.add(consentTierStatus2);
		
		specimenCollectionGroup.setConsentTierStatusCollection(consentTierStatusCollection);
		
		return specimenCollectionGroup;
	}
	
	public static Specimen initSpecimen()
	{
		MolecularSpecimen molecularSpecimen = new MolecularSpecimen();

		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(new Long(1));
		molecularSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);

		molecularSpecimen.setLabel("Specimen 12345");
		molecularSpecimen.setBarcode("");
		molecularSpecimen.setType("DNA");
		molecularSpecimen.setAvailable(new Boolean(true));
		molecularSpecimen.setActivityStatus("Active");

		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide("Left");
		specimenCharacteristics.setTissueSite("Placenta");
		molecularSpecimen.setSpecimenCharacteristics(specimenCharacteristics);

		molecularSpecimen.setPathologicalStatus("Malignant");

		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		// modified code here. chnged funcion name to setInitialQuantity(quantity) from setQuantity(quantity)
		molecularSpecimen.setInitialQuantity(quantity);

		molecularSpecimen.setConcentrationInMicrogramPerMicroliter(new Double(10));
	//	molecularSpecimen.setComments("");
		// Is virtually located
		molecularSpecimen.setStorageContainer(null);
		molecularSpecimen.setPositionDimensionOne(null);
		molecularSpecimen.setPositionDimensionTwo(null);
		

		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("Specimen 1 ext id");
		externalIdentifier.setValue("11");
		externalIdentifierCollection.add(externalIdentifier);
		molecularSpecimen.setExternalIdentifierCollection(externalIdentifierCollection);

		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
	//	collectionEventParameters.setComments("");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		//User user = new User();
		//user.setId(new Long(1));
	//	collectionEventParameters.setId(new Long(0));
		collectionEventParameters.setUser(user);
		try
		{
			collectionEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e1)
		{
			e1.printStackTrace();
		}
		collectionEventParameters.setContainer("No Additive Vacutainer");
		collectionEventParameters.setCollectionProcedure("Needle Core Biopsy");

		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setUser(user);
		//receivedEventParameters.setId(new Long(0));
		try
		{
			receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		receivedEventParameters.setReceivedQuality("acceptable");
	//	receivedEventParameters.setComments("");
		receivedEventParameters.setReceivedQuality("Cauterized");
		
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		molecularSpecimen.setSpecimenEventCollection(specimenEventCollection);

//		Biohazard biohazard = new Biohazard();
//		biohazard.setName("Biohazard1");
//		biohazard.setType("Toxic");
//		biohazard.setId(new Long(1));
//		Collection biohazardCollection = new HashSet();
//		biohazardCollection.add(biohazard);
//		molecularSpecimen.setBiohazardCollection(biohazardCollection);

		//Setting Consent Tier Response
		Collection consentTierStatusCollection = new HashSet();
		
		ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(21));
		consentTierStatus.setConsentTier(consentTier);
		consentTierStatus.setStatus("No");
		consentTierStatusCollection.add(consentTierStatus);
		
		ConsentTierStatus  consentTierStatus1 = new ConsentTierStatus();		
		ConsentTier consentTier1 = new ConsentTier();
		consentTier1.setId(new Long(22));
		consentTierStatus1.setConsentTier(consentTier1);
		consentTierStatus1.setStatus("No");
		consentTierStatusCollection.add(consentTierStatus1);
		
		ConsentTierStatus  consentTierStatus2 = new ConsentTierStatus();		
		ConsentTier consentTier2 = new ConsentTier();
		consentTier2.setId(new Long(23));
		consentTierStatus2.setConsentTier(consentTier2);
		consentTierStatus2.setStatus("No");
		consentTierStatusCollection.add(consentTierStatus2);
		
		molecularSpecimen.setConsentTierStatusCollection(consentTierStatusCollection);
		
		return molecularSpecimen;
	}
	
	public static CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant)
	{
		//Logger.configure("");
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		CollectionProtocol collectionProtocol =(CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
		//new CollectionProtocol();
		//collectionProtocol.setId(new Long(3));
		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

		collectionProtocolRegistration.setParticipant(participant);

		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Setting Consent Tier Responses.
		try
		{
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		//User user = new User();
		//user.setId(new Long(1));
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();
		while(consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier)consentTierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setConsentTier(consentTier);
			consentResponse.setResponse("Yes");
			consentTierResponseCollection.add(consentResponse);
		}
//		ConsentTierResponse r1 = new ConsentTierResponse();
//		ConsentTier consentTier = new ConsentTier();
//		consentTier.setId(new Long(1));
//		r1.setConsentTier(consentTier);
//		r1.setResponse("Yes");
//		consentTierResponseCollection.add(r1);
//		
//		ConsentTierResponse r2 = new ConsentTierResponse();
//		ConsentTier consentTier2 = new ConsentTier();
//		consentTier2.setId(new Long(2));
//		r2.setConsentTier(consentTier2);
//		r2.setResponse("Yes");
//		consentTierResponseCollection.add(r2);
//		
//		ConsentTierResponse r3 = new ConsentTierResponse();
//		ConsentTier consentTier3 = new ConsentTier();
//		consentTier3.setId(new Long(3));
//		r3.setConsentTier(consentTier3);
//		r3.setResponse("No");
//		consentTierResponseCollection.add(r3);
//		
		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);		
		SpecimenCollectionGroup specimenCollectionGroup = createSCG(collectionProtocolRegistration);
		
		Collection specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>(); 
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
		
		return collectionProtocolRegistration;
	}
	
	
	private static SpecimenCollectionGroup createSCG(CollectionProtocolRegistration collectionProtocolRegistration)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try 
		{
			Collection collectionProtocolEventCollection = collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection();
			Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			//User user = new User();
			//user.setId(new Long(1));
			while(collectionProtocolEventIterator.hasNext())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolEventIterator.next();
				SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = (SpecimenCollectionRequirementGroup) collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
				specimenCollectionGroup = new SpecimenCollectionGroup(specimenCollectionRequirementGroup);
				specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
				specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
				
				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
				
				Collection cloneSpecimenCollection = new LinkedHashSet();
				Collection specimenCollection = specimenCollectionRequirementGroup.getSpecimenCollection();
				if(specimenCollection != null && !specimenCollection.isEmpty())
				{
					Iterator itSpecimenCollection = specimenCollection.iterator();
					while(itSpecimenCollection.hasNext())
					{
						Specimen specimen = (Specimen)itSpecimenCollection.next();
						if(specimen.getLineage().equalsIgnoreCase("new"))
						{
							Specimen cloneSpecimen = getCloneSpecimen(specimenMap, specimen,null,specimenCollectionGroup,user);
							LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
							specimenLableGenerator.setLabel(cloneSpecimen);
							cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
							cloneSpecimenCollection.add(cloneSpecimen);
						}
					}
				}
				
				specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
			}
		}catch(Exception e)
		{
			
		}
			return specimenCollectionGroup;
	}
	
	private static Specimen getCloneSpecimen(Map<Specimen, List<Specimen>> specimenMap, Specimen specimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup, User user)
	{
		Collection childrenSpecimen = new LinkedHashSet<Specimen>(); 
		Specimen newSpecimen = specimen.createClone();
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(user.getId());
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
		if (newSpecimen.getParentSpecimen()== null)
		{
			specimenMap.put(newSpecimen, new ArrayList<Specimen>());
		}
    	else
    	{
    		specimenMap.put(newSpecimen, null);
    	}
		
		Collection childrenSpecimenCollection = specimen.getChildrenSpecimen();
    	if(childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		{
	    	Iterator it = childrenSpecimenCollection.iterator();
	    	while(it.hasNext())
	    	{
	    		Specimen childSpecimen = (Specimen)it.next();
	    		Specimen newchildSpecimen = getCloneSpecimen(specimenMap, childSpecimen,newSpecimen, specimenCollectionGroup, user);
	    		childrenSpecimen.add(newchildSpecimen);
	    		newSpecimen.setChildrenSpecimen(childrenSpecimen);
	    	}
		}

    	
    	return newSpecimen;
	}
	
	public Collection initConsentTier(boolean empty)
	{
//		Setting consent tiers for this protocol.
		Collection consentTierColl = new HashSet();
		if(!empty)
		{
			ConsentTier c1 = new ConsentTier();
			c1.setStatement("Consent for aids research");
			consentTierColl.add(c1);
			ConsentTier c2 = new ConsentTier();
			c2.setStatement("Consent for cancer research");
			consentTierColl.add(c2);		
			ConsentTier c3 = new ConsentTier();
			c3.setStatement("Consent for Tb research");
			consentTierColl.add(c3);	
		}
		return consentTierColl;
		
	
}
	public static Institution initInstitution()
	{
		Institution institutionObj = new Institution();
		institutionObj.setName("inst" + UniqueKeyGeneratorUtil.getUniqueKey());
		return institutionObj;
	}
	
	public static void updateInstitution(Institution institution)
	{
		institution.setName("inst"+UniqueKeyGeneratorUtil.getUniqueKey());
	}
	
	public static CancerResearchGroup initCancerResearchGrp()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("crgName" + UniqueKeyGeneratorUtil.getUniqueKey());
		return cancerResearchGroup;
	}
	
	public static void updateCancerResearchGrp(CancerResearchGroup cancerResearchGroup)
	{
		cancerResearchGroup.setName("crgName"+UniqueKeyGeneratorUtil.getUniqueKey());		
	}
	
	
	public static DistributionProtocol initDistributionProtocol()
	{
		DistributionProtocol distributionProtocol = new DistributionProtocol();

		//User principalInvestigator = initUser();
		User principalInvestigator =new User();
		principalInvestigator.setId(new Long("1"));
		
		/*	
		new User();
		principalInvestigator.setId(new Long(1));
		*/
		distributionProtocol.setPrincipalInvestigator(principalInvestigator);
		distributionProtocol.setTitle("DP"+ UniqueKeyGeneratorUtil.getUniqueKey());
		distributionProtocol.setShortTitle("DP1");
		distributionProtocol.setIrbIdentifier("52266");
		
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		distributionProtocol.setDescriptionURL("distribution protocol");
		distributionProtocol.setEnrollment(new Integer(10));

		SpecimenRequirement specimenRequirement = initSpecimenRequirement();
		/*	
		new SpecimenRequirement();
		specimenRequirement.setPathologyStatus("Malignant");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setSpecimenClass("Molecular");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		*/
			
		Collection specimenRequirementCollection = new HashSet();
		specimenRequirementCollection.add(specimenRequirement);
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);

		distributionProtocol.setActivityStatus("Active");
		return distributionProtocol;
	}
	
	public static SpecimenRequirement initSpecimenRequirement()
	{
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		return specimenRequirement;
	}
	
	public static void updateDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		User principalInvestigator = new User();
		principalInvestigator.setId(new Long(1));
		/*	
		new User();
		principalInvestigator.setId(new Long(1));
		*/
		//distributionProtocol.setPrincipalInvestigator(principalInvestigator);
		distributionProtocol.setTitle("DP"+ UniqueKeyGeneratorUtil.getUniqueKey());
		distributionProtocol.setShortTitle("DP"); //DP1
		distributionProtocol.setIrbIdentifier("11111");//55555
		
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1976", Utility
					.datePattern("08/15/1976"))); //08/15/1975
		}
		catch (ParseException e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
		
		distributionProtocol.setDescriptionURL("");
		distributionProtocol.setEnrollment(new Integer(20)); //10

		SpecimenRequirement specimenRequirement = initSpecimenRequirement();
		specimenRequirement.setPathologyStatus("Non-Malignant"); //Malignant
		specimenRequirement.setTissueSite("Anal canal"); //Placenta
		specimenRequirement.setSpecimenType("Bile"); //DNA
		specimenRequirement.setSpecimenClass("Fluid"); //Molecular
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(20)); //10
		specimenRequirement.setQuantity(quantity);
			
		Collection specimenRequirementCollection = new HashSet();
		specimenRequirementCollection.add(specimenRequirement);
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);

		distributionProtocol.setActivityStatus("Active"); //Active
	}
	
	public static Site initSite()
	{
		Site siteObj = new Site();
		User userObj = (User)TestCaseUtility.getObjectMap(User.class);
//		User userObj = new User();
//		userObj.setId(new Long(1));

		siteObj.setEmailAddress("admin@admin.com");
		siteObj.setName("sit" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Laboratory");
		siteObj.setActivityStatus("Active");
		siteObj.setCoordinator(userObj);
		
	 
		Address addressObj = new Address();
		addressObj.setCity("Saint Louis");
		addressObj.setCountry("United States");
		addressObj.setFaxNumber("555-55-5555");
		addressObj.setPhoneNumber("123678");
		addressObj.setState("Missouri");
		addressObj.setStreet("4939 Children's Place");
		addressObj.setZipCode("63110");
		siteObj.setAddress(addressObj);
		return siteObj;
	}
	
	public static void updateSite(Site siteObj)
	{
		siteObj.setEmailAddress("admin1@admin.com");
		siteObj.setName("updatedSite" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Repository");
		siteObj.setActivityStatus("Active");		
		siteObj.getAddress().setCity("Saint Louis1"); 
		siteObj.getAddress().setCountry("United States");
		siteObj.getAddress().setFaxNumber("777-77-77771");
		siteObj.getAddress().setPhoneNumber("1236781");
		siteObj.getAddress().setState("Missouri");
		siteObj.getAddress().setStreet("4939 Children's Place1");
		siteObj.getAddress().setZipCode("63111");		
	}
	
	public static StorageType initStorageType()
	{
		StorageType storageTypeObj = new StorageType();
		Capacity capacity = new Capacity();

		storageTypeObj.setName("st" + UniqueKeyGeneratorUtil.getUniqueKey());
		storageTypeObj.setDefaultTempratureInCentigrade(new Double(-30));
		storageTypeObj.setOneDimensionLabel("label 1");
		storageTypeObj.setTwoDimensionLabel("label 2");

		capacity.setOneDimensionCapacity(new Integer(3));
		capacity.setTwoDimensionCapacity(new Integer(3));
		storageTypeObj.setCapacity(capacity);		

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageTypeObj);

		storageTypeObj.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
		storageTypeObj.setActivityStatus("Active");

		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageTypeObj.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		return storageTypeObj;
	}
	

	public static void updateStorageType(StorageType updateStorageType)
	{		
		Capacity capacity = updateStorageType.getCapacity();
		
		updateStorageType.setDefaultTempratureInCentigrade(new Double(30));//-30
		updateStorageType.setOneDimensionLabel("Label-1"); //label 1
		updateStorageType.setTwoDimensionLabel("Label-2"); //label 2

		capacity.setOneDimensionCapacity(new Integer(2));//3
		capacity.setTwoDimensionCapacity(new Integer(2));//3
		updateStorageType.setCapacity(capacity);
			
	}
	
	
	public static Participant initParticipant()
	{
		Participant participant = new Participant();
		participant.setLastName("lname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("fname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setGender("Male Gender");
		participant.setEthnicity("Unknown");
		participant.setSexGenotype("XX");

		Collection raceCollection = new HashSet();
		raceCollection.add("White");
		raceCollection.add("Asian");
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");
		Logger.out.info("Participant added successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
		return participant;
	}
	public static Participant initParticipantWithCPR()
	{
		Participant participant = new Participant();
		participant.setLastName("lname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("fname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setGender("Male Gender");
		participant.setEthnicity("Unknown");
		participant.setSexGenotype("XX");

		Collection raceCollection = new HashSet();
		raceCollection.add("White");
		raceCollection.add("Asian");
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");
		Logger.out.info("Participant added successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
		Collection collectionProtocolRegistrationCollection = new HashSet();
		CollectionProtocolRegistration collectionProtocolRegistration = initCollectionProtocolRegistration(participant);
		collectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
		participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
		return participant;
	}
	
	public static void updateParticipant(Participant participant)
	{
		participant.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("frst" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setMiddleName("mdl" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		participant.setVitalStatus("Alive"); //Dead
		participant.setGender("Male Gender"); //
		participant.setSexGenotype(""); //XX

		Collection raceCollection = new HashSet();
		raceCollection.add("Black or African American"); //White
		raceCollection.add("Unknown"); //Asian
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active"); //Active
		participant.setEthnicity("Unknown"); //Hispanic or Latino

		Collection participantMedicalIdentifierCollection = new HashSet();
		participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		
	}
	
	public static StorageContainer initStorageContainer()
	{
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName("sc" + UniqueKeyGeneratorUtil.getUniqueKey());

		StorageType storageType =(StorageType)TestCaseUtility.getObjectMap(StorageType.class); 
		//new StorageType();
		//storageType.setId(new Long(3));
		storageContainer.setStorageType(storageType);
		
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class); 
		//new Site();
		//site.setId(new Long(1));
		
		storageContainer.setSite(site);

		Integer conts = new Integer(1);
		storageContainer.setNoOfContainers(conts);
		storageContainer.setTempratureInCentigrade(new Double(-30));
		storageContainer.setBarcode("barc" + UniqueKeyGeneratorUtil.getUniqueKey());

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(new Integer(1));
		capacity.setTwoDimensionCapacity(new Integer(2));
		storageContainer.setCapacity(capacity);

		CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
		//new CollectionProtocol();
		//collectionProtocol.setId(new Long(3));
		Collection collectionProtocolCollection = new HashSet();
		collectionProtocolCollection.add(collectionProtocol);
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageType);
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();		
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
/*		Container parent = new Container();
		parent.setId(new Long(2));
		storageContainer.setParent(parent);    
*/
		storageContainer.setPositionDimensionOne(new Integer(1));
		storageContainer.setPositionDimensionTwo(new Integer(2));

		storageContainer.setActivityStatus("Active");
		storageContainer.setFull(Boolean.valueOf(false));
		return storageContainer;
	}
	
	public static void updateStorageContainer(StorageContainer storageContainer)
	{	
		StorageType storageType =(StorageType)TestCaseUtility.getObjectMap(StorageType.class);
		//StorageType storageType = new StorageType();
		//storageType.setId(new Long(4));  //setId(1)
		storageContainer.setStorageType(storageType);
		
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class);
		//Site site = new Site();
		//site.setId(new Long(1));
		storageContainer.setSite(site);
		
		storageContainer.setTempratureInCentigrade(new Double(30)); //-30
		storageContainer.setBarcode("barc" + UniqueKeyGeneratorUtil.getUniqueKey());

		Capacity capacity = storageContainer.getCapacity();
		capacity.setOneDimensionCapacity(new Integer(1));
		capacity.setTwoDimensionCapacity(new Integer(2));
		storageContainer.setCapacity(capacity);

		CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
		//CollectionProtocol collectionProtocol =  new CollectionProtocol();
		//collectionProtocol.setId(new Long(3));
		
		Collection collectionProtocolCollection = new HashSet();
		collectionProtocolCollection.add(collectionProtocol);
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageType);
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();		
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
		storageContainer.setPositionDimensionOne(new Integer(1));
		storageContainer.setPositionDimensionTwo(new Integer(2));

		storageContainer.setActivityStatus("Active");
		storageContainer.setFull(Boolean.valueOf(false));		
	}
	
	public static Biohazard initBioHazard()
	{
		Biohazard bioHazard = new Biohazard();
		bioHazard.setComment("NueroToxicProtein");
		bioHazard.setName("bh" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioHazard.setType("Toxic");
		return bioHazard;
	}
	
	public static void updateBiohazard(Biohazard bioHazard)
	{
		bioHazard.setComment("Radioactive");
		bioHazard.setName("bh" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioHazard.setType("Radioactive"); //Toxic
	}
	
	public static OrderDetails initOrder()
    {           
          OrderDetails order = new OrderDetails();  
          order.setComment("Comment");
          
          //Obtain Distribution Protocol
          DistributionProtocol distributionProtocolObj = new DistributionProtocol();
          distributionProtocolObj.setId(new Long(2));
          
          /*DistributionProtocol distributionProtocol = new DistributionProtocol();
          distributionProtocol.setId(new Long(2));*/

          order.setDistributionProtocol(distributionProtocolObj);
          order.setName("Request1 ");
          order.setStatus("New");
          try
          {
                order.setRequestedDate(Utility.parseDate("04-02-1984", Constants.DATE_PATTERN_MM_DD_YYYY));
          }

          catch (ParseException e)
          {
                Logger.out.debug(""+e);
          }
          Collection orderItemCollection = new HashSet();       

          Specimen specimen = new Specimen();
          specimen.setId(new Long(1));

          ExistingSpecimenOrderItem exSpOrderItem = new ExistingSpecimenOrderItem();
          exSpOrderItem.setDescription("OrderDetails Item 1 of Order_Id ");
          exSpOrderItem.setStatus("New");           
          
          Quantity quantity = new Quantity();
          quantity.setValue(new Double(1));
          exSpOrderItem.setRequestedQuantity(quantity);
          exSpOrderItem.setSpecimen(specimen);
               
          orderItemCollection.add(exSpOrderItem);
          order.setOrderItemCollection(orderItemCollection);
          return order;
    }
	
	public static OrderDetails updateOrderDetails(OrderDetails orderObj)
	{
		orderObj.setComment("UpdatedComment");
		
		//Obtain Distribution Protocol
        DistributionProtocol distributionProtocolObj = new DistributionProtocol();
        distributionProtocolObj.setId(new Long(2));
		
		orderObj.setDistributionProtocol(distributionProtocolObj);
        orderObj.setName("Updated Request Name");
        orderObj.setStatus("Pending");
        try
        {
        	orderObj.setRequestedDate(Utility.parseDate("05-02-1984", Constants.DATE_PATTERN_MM_DD_YYYY));
        }
        catch (ParseException e)
        {
              Logger.out.debug(""+e);
        }
        Collection orderItemCollection = new HashSet(); 
        ExistingSpecimenOrderItem existingOrderItem =(ExistingSpecimenOrderItem) orderObj.getOrderItemCollection().iterator().next();
        existingOrderItem.setDescription("Updated OrderDetails Item 1 of Order_Id ");
        existingOrderItem.setStatus("Pending - Protocol Review");          
        existingOrderItem.setOrderDetails(orderObj);
       
        
        return orderObj;
	}
	
	public static Distribution initDistribution()
	{
		Distribution distribution = new Distribution();

		distribution.setActivityStatus("Active");

		Specimen specimen = new Specimen();
		specimen.setId(new Long(1));
		
		/*
		= new MolecularSpecimen();
	//	specimen.setBarcode("");
	//	specimen.setLabel("new label");
		specimen.setId(new Long(10));
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(15));
		specimen.setAvailableQuantity(quantity);
		*/
		
		
		
		DistributedItem distributedItem = new DistributedItem();
		distributedItem.setQuantity(new Double(1));
		distributedItem.setSpecimen(specimen);
		Collection distributedItemCollection = new HashSet();
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		
		DistributionProtocol distributionProtocol =  new DistributionProtocol();
		distributionProtocol.setId(new Long(2));
		distribution.setDistributionProtocol(distributionProtocol);
		Site toSite = (Site)TestCaseUtility.getObjectMap(Site.class);
		
		//Site toSite =  new Site();
		//toSite.setId(new Long(6));
		//toSite.setId(new Long("1000"));
		distribution.setToSite(toSite);
		/*	
		new Site();
		toSite.setId(new Long(1));
		distribution.setToSite(toSite);
		*/
		/*
		try
		{
			distribution.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		*/
		distribution.setComment("");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		//User user = new User();
		//user.setId(new Long(2));
		/*	
		new User();
		user.setId(new Long(1));
		*/
		distribution.setDistributedBy(user);
	 
		return distribution;
	}
	
	public static Distribution initDistribution(Specimen specimen)
	{
		Distribution distribution = new Distribution();

		distribution.setActivityStatus("Active");		
		DistributedItem distributedItem = new DistributedItem();
		distributedItem.setQuantity(new Double(2));
		distributedItem.setSpecimen(specimen);
		Collection distributedItemCollection = new HashSet();
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		
		DistributionProtocol distributionProtocol = new DistributionProtocol();		
		distributionProtocol.setId(new Long(2));
		distribution.setDistributionProtocol(distributionProtocol);
		
		Site toSite = (Site)TestCaseUtility.getObjectMap(Site.class);
		
		//Site toSite =  new Site();
		//toSite.setId(new Long(2));
		distribution.setToSite(toSite);	
		distribution.setComment("");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		//User user =  new User();
		//user.setId(new Long(2));
		distribution.setDistributedBy(user);
		return distribution;
	}
	
	public static Specimen initTissueSpecimen()
	{
		TissueSpecimen ts= new TissueSpecimen();
		ts.setLabel("TissueSpecimenXYZ"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setActivityStatus("Active");
		ts.setAvailable(true);
		ts.setBarcode("Barcode"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setType("Fixed Tissue Block");
		//ts.setAliqoutMap(arg0);
		ts.setPathologicalStatus("Malignant");
		
		
		try{
			ts.setCreatedOn(Utility.parseDate("08/15/2003", Utility.datePattern("08/15/1975")));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage()); 
			
		}
		
				
		SpecimenCharacteristics specimenCharacteristics =  new SpecimenCharacteristics();
		specimenCharacteristics.setId(new Long(1));
		specimenCharacteristics.setTissueSide("Left");
		specimenCharacteristics.setTissueSite("Placenta");
		ts.setSpecimenCharacteristics(specimenCharacteristics);
		
		
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(new Long(56));
		ts.setSpecimenCollectionGroup(scg);
		
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10.0));
		ts.setInitialQuantity(quantity);
		ts.setAvailableQuantity(quantity);
		
		
		ts.setStorageContainer(null); 
		ts.setPositionDimensionOne(null);
		ts.setPositionDimensionTwo(null);
		
		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
		externalIdentifier.setValue("11");
		externalIdentifier.setSpecimen(ts);
		externalIdentifierCollection.add(externalIdentifier);
		ts.setExternalIdentifierCollection(externalIdentifierCollection);
		
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		collectionEventParameters.setComment("");
		collectionEventParameters.setSpecimen(ts);
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		//User user = new User();
		//user.setId(new Long(1));
		collectionEventParameters.setUser(user);		
		try
		{
			collectionEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
					
		}
		catch (ParseException e1)
		{
			System.out.println(" exception in APIDemo");
			e1.printStackTrace();
		}
		
		collectionEventParameters.setContainer("No Additive Vacutainer");
		collectionEventParameters.setCollectionProcedure("Needle Core Biopsy");
		
		
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setUser(user);				
		try
		{
			System.out.println("--- Start ---- 10");
			receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			System.out.println("APIDemo");
			e.printStackTrace();
		}
		
		receivedEventParameters.setReceivedQuality("Acceptable");
		receivedEventParameters.setComment("");
		receivedEventParameters.setSpecimen(ts);
		
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		ts.setSpecimenEventCollection(specimenEventCollection);
		
		Biohazard biohazard =  new Biohazard();
		biohazard.setId(new Long(1));
		biohazard.setName("Bh"+UniqueKeyGeneratorUtil.getUniqueKey());
		biohazard.setType("Toxic");
		Collection biohazardCollection = new HashSet();
		biohazardCollection.add(biohazard);
		ts.setBiohazardCollection(biohazardCollection);
		System.out.println(" -------- end -----------");
		
		//Created on date is same as Collection Date
		
		ts.setCreatedOn(collectionEventParameters.getTimestamp());
		
		Collection consentTierStatusCollection = new HashSet();
		ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(1));
		consentTierStatus.setConsentTier(consentTier);
		consentTierStatus.setStatus("No");
		consentTierStatusCollection.add(consentTierStatus);
		ts.setConsentTierStatusCollection(consentTierStatusCollection);
        return ts;
	}
	public static SpecimenArrayType initSpecimenSpecimenArrayType()
	{
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setName("sat" + UniqueKeyGeneratorUtil.getUniqueKey());
		specimenArrayType.setSpecimenClass("Tissue");

		Collection specimenTypeCollection = new HashSet();
		specimenTypeCollection.add("Frozen Tissue Block");
		specimenTypeCollection.add("Fixed Tissue");
		specimenTypeCollection.add("Fixed Tissue Block");
		specimenTypeCollection.add("Fixed Tissue Slide");
		specimenTypeCollection.add("Fresh Tissue");		
		specimenArrayType.setSpecimenTypeCollection(specimenTypeCollection);
		
		specimenArrayType.setComment("");
		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(new Integer(4));
		capacity.setTwoDimensionCapacity(new Integer(4));
		specimenArrayType.setCapacity(capacity);
		return specimenArrayType;
	}
	public static SpecimenArrayType updateSpecimenSpecimenArrayType(SpecimenArrayType specimenArrayType)
	{
		specimenArrayType.setName("Updated SpecimenArray"+ UniqueKeyGeneratorUtil.getUniqueKey());
		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(new Integer(5));//4
		capacity.setTwoDimensionCapacity(new Integer(5));//4
		specimenArrayType.setCapacity(capacity);
		return specimenArrayType;	
	}
	
	public static SpecimenArray initSpecimenArray()
	{
		SpecimenArray specimenArray = new SpecimenArray();
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setId(new Long(9));
		specimenArray.setSpecimenArrayType(specimenArrayType);
		
		specimenArray.setBarcode("bar" + UniqueKeyGeneratorUtil.getUniqueKey());
		specimenArray.setName("sa" + UniqueKeyGeneratorUtil.getUniqueKey()); 
		
		User createdBy = (User)TestCaseUtility.getObjectMap(User.class);
//		User createdBy = new User();
//		createdBy.setId(new Long(1));
		specimenArray.setCreatedBy(createdBy);
		
		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(4);
		capacity.setTwoDimensionCapacity(4);
		specimenArray.setCapacity(capacity);
		
		specimenArray.setComment("");
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setId(new Long(1));	
		
		specimenArray.setStorageContainer(storageContainer);
		specimenArray.setPositionDimensionOne(new Integer(1));
		specimenArray.setPositionDimensionTwo(new Integer(1));
		
		Collection specimenArrayContentCollection = new HashSet();
		SpecimenArrayContent specimenArrayContent = new SpecimenArrayContent();
		
		Specimen specimen = new Specimen(); 
		specimen.setId(new Long(50));
		
		specimenArrayContent.setSpecimen(specimen);
		specimenArrayContent.setPositionDimensionOne(new Integer(1));
		specimenArrayContent.setPositionDimensionTwo(new Integer(1));
		
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(2));
		specimenArrayContent.setInitialQuantity(quantity);
		specimenArrayContentCollection.add(specimenArrayContent);
		specimenArray.setSpecimenArrayContentCollection(specimenArrayContentCollection);
		return specimenArray;	
	}
	
	public static IdentifiedSurgicalPathologyReport initIdentifiedSurgicalPathologyReport()
	{
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=new IdentifiedSurgicalPathologyReport();
		identifiedSurgicalPathologyReport.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		identifiedSurgicalPathologyReport.setCollectionDateTime(new Date());
		identifiedSurgicalPathologyReport.setIsFlagForReview(new Boolean(false));
		identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
		identifiedSurgicalPathologyReport.setReportSource((Site)TestCaseUtility.getObjectMap(Site.class));
		TextContent textContent=new TextContent();
		String data="[FINAL DIAGNOSIS]\n" +
				"This is the Final Diagnosis Text" +
				"\n\n[GROSS DESCRIPTION]" +
				"The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.";
	
		textContent.setData(data);
		textContent.setSurgicalPathologyReport(identifiedSurgicalPathologyReport);
		Set reportSectionCollection=new HashSet();
		ReportSection reportSection1=new ReportSection();
		reportSection1.setName("GDT");
		reportSection1.setDocumentFragment("The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.");
		reportSection1.setTextContent(textContent);
		
		ReportSection reportSection2=new ReportSection();
		reportSection2.setName("FIN");
		reportSection2.setDocumentFragment("This is the Final Diagnosis Text");
		reportSection2.setTextContent(textContent);
		
		reportSectionCollection.add(reportSection1);
		reportSectionCollection.add(reportSection2);
		
		textContent.setReportSectionCollection(reportSectionCollection);
		
		identifiedSurgicalPathologyReport.setTextContent(textContent);
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		specimenCollectionGroup.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());
		identifiedSurgicalPathologyReport.setSpecimenCollectionGroup(specimenCollectionGroup);
		return identifiedSurgicalPathologyReport;
	}
	
	public static IdentifiedSurgicalPathologyReport updateIdentifiedSurgicalPathologyReport(IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport)
	{
		identifiedSurgicalPathologyReport=(IdentifiedSurgicalPathologyReport)TestCaseUtility.getObjectMap(IdentifiedSurgicalPathologyReport.class);
		identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.DEIDENTIFIED);
		return identifiedSurgicalPathologyReport;
	}
	
	public static DeidentifiedSurgicalPathologyReport initDeIdentifiedSurgicalPathologyReport()
	{
		DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport=new DeidentifiedSurgicalPathologyReport();
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=(IdentifiedSurgicalPathologyReport)TestCaseUtility.getObjectMap(IdentifiedSurgicalPathologyReport.class);
		deidentifiedSurgicalPathologyReport.setIsFlagForReview(new Boolean(false));
		deidentifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.PENDING_FOR_XML);
		deidentifiedSurgicalPathologyReport.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		deidentifiedSurgicalPathologyReport.setSpecimenCollectionGroup(identifiedSurgicalPathologyReport.getSpecimenCollectionGroup());
		
		TextContent textContent = new TextContent();
		String data="Report is de-identified \n"+identifiedSurgicalPathologyReport.getTextContent().getData();
		textContent.setData(data);
		textContent.setSurgicalPathologyReport(deidentifiedSurgicalPathologyReport);
		
		deidentifiedSurgicalPathologyReport.setTextContent(textContent);
		
		return deidentifiedSurgicalPathologyReport;
	}
	
	public static DeidentifiedSurgicalPathologyReport updateIdentifiedSurgicalPathologyReport(DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
	{
		deidentifiedSurgicalPathologyReport=(DeidentifiedSurgicalPathologyReport)TestCaseUtility.getObjectMap(DeidentifiedSurgicalPathologyReport.class);
		deidentifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.CONCEPT_CODED);
		TextContent textContent=deidentifiedSurgicalPathologyReport.getTextContent();
		String data=textContent.getData();
		data+="Updated\n";
		
		return deidentifiedSurgicalPathologyReport;
	}
}