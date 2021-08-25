//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.30 at 01:32:48 PM IST 
//


package com.emerson.bpm.dsl.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlphaNodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlphaNodeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ruleContext" type="{http://www.example.org/RulkesDSLSchema}RulesIDContextType"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comparator" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="alphaNode" type="{http://www.example.org/RulkesDSLSchema}AlphaNodeType"/&gt;
 *           &lt;element name="joinNode" type="{http://www.example.org/RulkesDSLSchema}JoinNodeType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlphaNodeType", propOrder = {
    "ruleContext",
    "name",
    "comparator",
    "alphaNode",
    "joinNode"
})
public class AlphaNodeType {

    @XmlElement(required = true)
    protected RulesIDContextType ruleContext;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String comparator;
    protected AlphaNodeType alphaNode;
    protected JoinNodeType joinNode;

    /**
     * Gets the value of the ruleContext property.
     * 
     * @return
     *     possible object is
     *     {@link RulesIDContextType }
     *     
     */
    public RulesIDContextType getRuleContext() {
        return ruleContext;
    }

    /**
     * Sets the value of the ruleContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link RulesIDContextType }
     *     
     */
    public void setRuleContext(RulesIDContextType value) {
        this.ruleContext = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the comparator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComparator() {
        return comparator;
    }

    /**
     * Sets the value of the comparator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComparator(String value) {
        this.comparator = value;
    }

    /**
     * Gets the value of the alphaNode property.
     * 
     * @return
     *     possible object is
     *     {@link AlphaNodeType }
     *     
     */
    public AlphaNodeType getAlphaNode() {
        return alphaNode;
    }

    /**
     * Sets the value of the alphaNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlphaNodeType }
     *     
     */
    public void setAlphaNode(AlphaNodeType value) {
        this.alphaNode = value;
    }

    /**
     * Gets the value of the joinNode property.
     * 
     * @return
     *     possible object is
     *     {@link JoinNodeType }
     *     
     */
    public JoinNodeType getJoinNode() {
        return joinNode;
    }

    /**
     * Sets the value of the joinNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JoinNodeType }
     *     
     */
    public void setJoinNode(JoinNodeType value) {
        this.joinNode = value;
    }

}