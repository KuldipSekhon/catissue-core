/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.cagrid.test;
import edu.wustl.catissuecore.domain.util.PropertiesLoader;
import junit.framework.TestCase;

/**
 * @author Ion C. Olaru
 *         Date: 8/29/11 - 2:48 PM
 */
public class PropertiesLoaderTest extends TestCase {
    public void testSuperUser() {
        assertEquals("admin@admin.com", PropertiesLoader.getCaTissueSuperUserUsername());
        assertEquals("Aa_111111", PropertiesLoader.getCaTissueSuperUserPassword());
    }
}
