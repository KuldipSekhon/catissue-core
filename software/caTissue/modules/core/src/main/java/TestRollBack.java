/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.HibernateDAOImpl;

/*
 * Created on Aug 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestRollBack
{
    public static void main(String[] args) throws Exception
    {
    	System.out.println("Head");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(CommonServiceLocator.getInstance().getAppHome()+"\\WEB-INF\\src\\"+"ApplicationResources.properties");

		Logger.out.debug("here1234");

        Department dept = (Department)DomainInstanceFactory.getInstanceFactory(Department.class).createObject();//new Department();
        dept.setName("ABCD");

        HibernateDAO dao = new HibernateDAOImpl();
        dao.openSession(null);
        dao.insert(dept);
        dao.rollback();
        dao.closeSession();
    }
}
