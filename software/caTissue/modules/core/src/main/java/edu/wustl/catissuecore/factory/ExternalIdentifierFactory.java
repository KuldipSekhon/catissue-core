/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ExternalIdentifier;

public class ExternalIdentifierFactory implements InstanceFactory<ExternalIdentifier>
{
	private static ExternalIdentifierFactory externalIdentifierFactory;

	private ExternalIdentifierFactory()
	{
		super();
	}

	public static synchronized ExternalIdentifierFactory getInstance()
	{
		if(externalIdentifierFactory==null){
			externalIdentifierFactory = new ExternalIdentifierFactory();
		}
		return externalIdentifierFactory;
	}

	public ExternalIdentifier createClone(ExternalIdentifier obj)
	{
		ExternalIdentifier externalIdentifier = createObject();
		externalIdentifier.setName(obj.getName());
		externalIdentifier.setValue(obj.getValue());
		return externalIdentifier;
	}

	public ExternalIdentifier createObject()
	{
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		return externalIdentifier;
	}
	public void initDefaultValues(ExternalIdentifier obj)
	{
	}
}
