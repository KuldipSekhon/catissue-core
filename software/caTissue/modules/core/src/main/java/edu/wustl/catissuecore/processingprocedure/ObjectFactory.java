/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.08.04 at 10:54:25 AM GMT+05:30
//


package edu.wustl.catissuecore.processingprocedure;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the generated package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SPP_QNAME = new QName("", "SPP");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SPPType }
     *
     */
    public SPPType createSPPType() {
        return new SPPType();
    }

    /**
     * Create an instance of {@link EventType }
     *
     */
    public EventType createEventType() {
        return new EventType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SPPType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "SPP")
    public JAXBElement<SPPType> createSPP(SPPType value) {
        return new JAXBElement<SPPType>(_SPP_QNAME, SPPType.class, null, value);
    }

}
