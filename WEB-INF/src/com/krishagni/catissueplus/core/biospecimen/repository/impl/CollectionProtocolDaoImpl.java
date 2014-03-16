
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.util.global.Constants;

public class CollectionProtocolDaoImpl extends AbstractDao<CollectionProtocol> implements CollectionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocolSummary> getAllCollectionProtocols() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_CPS);
		query.setString("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE);

		List<Object[]> rows = query.list();
		List<CollectionProtocolSummary> cps = new ArrayList<CollectionProtocolSummary>();
		for (Object[] row : rows) {
			CollectionProtocolSummary cp = new CollectionProtocolSummary();
			cp.setId((Long) row[0]);
			cp.setShortTitle((String) row[1]);
			cp.setTitle((String) row[2]);

			cps.add(cp);
		}

		return cps;
	}

	@Override
	public CollectionProtocol getCollectionProtocol(Long cpId) {
		return (CollectionProtocol)sessionFactory.getCurrentSession().get(CollectionProtocol.class.getName(), cpId);
	}

	@Override
	public Collection<ConsentTier> getConsentTierCollection(Long cpId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionProtocolRegistration getCpr(Long cpId, String ppid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPpidUniqueForProtocol(Long cpId, String protocolParticipantIdentifier) {
		// TODO Auto-generated method stub
		return true;
	}

	private static final String FQN = CollectionProtocol.class.getName();

	private static final String GET_ALL_CPS = FQN + ".getAllProtocols";

}