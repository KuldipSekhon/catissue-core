//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.03 at 06:02:46 PM IST 
//


package edu.wustl.catissuecore.jaxb.domain;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpecimenEventsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecimenEventsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CollectionEvent" type="{}CollectionEventType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "SpecimenEventsType", propOrder = {
    "collectionEvent"
})
public class SpecimenEventsType {

    @XmlElement(name = "CollectionEvent")
    protected CollectionEventType collectionEvent;

    /**
     * Gets the value of the collectionEvent property.
     * 
     * @return
     *     possible object is
     *     {@link CollectionEventType }
     *     
     */
    public CollectionEventType getCollectionEvent() {
        return collectionEvent;
    }

    /**
     * Sets the value of the collectionEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollectionEventType }
     *     
     */
    public void setCollectionEvent(CollectionEventType value) {
        this.collectionEvent = value;
    }

}
