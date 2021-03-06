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
// Generated on: 2011.05.30 at 01:53:00 PM GMT+05:30
//


package edu.wustl.catissuecore.metadata;

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

    private final static QName _StaticMetaData_QNAME = new QName("", "staticMetaData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StaticMetaDataType }
     *
     */
    public StaticMetaDataType createStaticMetaDataType() {
        return new StaticMetaDataType();
    }

    /**
     * Create an instance of {@link AssociationType }
     *
     */
    public AssociationType createAssociationType() {
        return new AssociationType();
    }

    /**
     * Create an instance of {@link AssociationsType }
     *
     */
    public AssociationsType createAssociationsType() {
        return new AssociationsType();
    }

    /**
     * Create an instance of {@link AttributeType }
     *
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link EntityType }
     *
     */
    public EntityType createEntityType() {
        return new EntityType();
    }

    /**
     * Create an instance of {@link EntitiesType }
     *
     */
    public EntitiesType createEntitiesType() {
        return new EntitiesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaticMetaDataType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "staticMetaData")
    public JAXBElement<StaticMetaDataType> createStaticMetaData(StaticMetaDataType value) {
        return new JAXBElement<StaticMetaDataType>(_StaticMetaData_QNAME, StaticMetaDataType.class, null, value);
    }

}
