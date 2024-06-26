//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.30 at 01:32:48 PM IST 
//


package com.emerson.bpm.dsl.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RulesIDContextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RulesIDContextType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/RulkesDSLSchema}ContextType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ruleContext" type="{http://www.example.org/RulkesDSLSchema}RuleComplexType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RulesIDContextType", propOrder = {
    "ruleContext"
})
public class RulesIDContextType
    extends ContextType
{

    @XmlElement(required = true)
    protected List<RuleComplexType> ruleContext;

    /**
     * Gets the value of the ruleContext property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ruleContext property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRuleContext().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RuleComplexType }
     * 
     * 
     */
    public List<RuleComplexType> getRuleContext() {
        if (ruleContext == null) {
            ruleContext = new ArrayList<RuleComplexType>();
        }
        return this.ruleContext;
    }

}
