/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
/**
 * The Class SpecialRequestType.
 */
public class SpecialRequestType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5012901777736137496L;
    /** The special meal. */
    private String specialMeal;
    /** The e ticket. */
    private IndicatorType eTicket;
    /** The wheelchair. */
    private IndicatorType wheelchair;
    /** The meet and assist. */
    private IndicatorType meetAndAssist;
    /** The basinet indicator. */
    private IndicatorType basinetIndicator;
    /** The meda. */
    private IndicatorType meda;
    /** The stretcher. */
    private IndicatorType stretcher;
    /** The security check. */
    private IndicatorType securityCheck;
    /** The apay indicator. */
    private IndicatorType apayIndicator;
    /** The un accompanied minor. */
    private IndicatorType unAccompaniedMinor;
    /** The wchc. */
    private IndicatorType wchc;
    /** The wchs. */
    private IndicatorType wchs;
    /** The others. */
    private String others;
    /**
     * Gets the value of the specialMeal property.
     *
     * @return the special meal
     * possible object is
     * {@link String }
     */
    public String getSpecialMeal() {
        return specialMeal;
    }
    /**
     * Sets the value of the specialMeal property.
     *
     * @param inValue the new special meal
     * {@link String }
     */
    public void setSpecialMeal(final String inValue) {
        this.specialMeal = inValue;
    }
    /**
     * Gets the value of the eTicket property.
     *
     * @return the e ticket
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getETicket() {
        return eTicket;
    }
    /**
     * Sets the value of the eTicket property.
     *
     * @param inValue the new e ticket
     * {@link IndicatorType }
     */
    public void setETicket(final IndicatorType inValue) {
        this.eTicket = inValue;
    }
    /**
     * Gets the value of the wheelchair property.
     *
     * @return the wheelchair
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getWheelchair() {
        return wheelchair;
    }
    /**
     * Sets the value of the wheelchair property.
     *
     * @param inValue the new wheelchair
     * {@link IndicatorType }
     */
    public void setWheelchair(final IndicatorType inValue) {
        this.wheelchair = inValue;
    }
    /**
     * Gets the value of the meetAndAssist property.
     *
     * @return the meet and assist
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getMeetAndAssist() {
        return meetAndAssist;
    }
    /**
     * Sets the value of the meetAndAssist property.
     *
     * @param inValue the new meet and assist
     * {@link IndicatorType }
     */
    public void setMeetAndAssist(final IndicatorType inValue) {
        this.meetAndAssist = inValue;
    }
    /**
     * Gets the value of the basinetIndicator property.
     *
     * @return the basinet indicator
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getBasinetIndicator() {
        return basinetIndicator;
    }
    /**
     * Sets the value of the basinetIndicator property.
     *
     * @param inValue the new basinet indicator
     * {@link IndicatorType }
     */
    public void setBasinetIndicator(final IndicatorType inValue) {
        this.basinetIndicator = inValue;
    }
    /**
     * Gets the value of the meda property.
     *
     * @return the mEDA
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getMEDA() {
        return meda;
    }
    /**
     * Sets the value of the meda property.
     *
     * @param inValue the new mEDA
     * {@link IndicatorType }
     */
    public void setMEDA(final IndicatorType inValue) {
        this.meda = inValue;
    }
    /**
     * Gets the value of the stretcher property.
     *
     * @return the stretcher
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getStretcher() {
        return stretcher;
    }
    /**
     * Sets the value of the stretcher property.
     *
     * @param inValue the new stretcher
     * {@link IndicatorType }
     */
    public void setStretcher(final IndicatorType inValue) {
        this.stretcher = inValue;
    }
    /**
     * Gets the value of the securityCheck property.
     *
     * @return the security check
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getSecurityCheck() {
        return securityCheck;
    }
    /**
     * Sets the value of the securityCheck property.
     *
     * @param inValue the new security check
     * {@link IndicatorType }
     */
    public void setSecurityCheck(final IndicatorType inValue) {
        this.securityCheck = inValue;
    }
    /**
     * Gets the value of the apayIndicator property.
     *
     * @return the aPAY indicator
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getAPAYIndicator() {
        return apayIndicator;
    }
    /**
     * Sets the value of the apayIndicator property.
     *
     * @param inValue the new aPAY indicator
     * {@link IndicatorType }
     */
    public void setAPAYIndicator(final IndicatorType inValue) {
        this.apayIndicator = inValue;
    }
    /**
     * Gets the value of the unAccompaniedMinor property.
     *
     * @return the un accompanied minor
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getUnAccompaniedMinor() {
        return unAccompaniedMinor;
    }
    /**
     * Sets the value of the unAccompaniedMinor property.
     *
     * @param inValue the new un accompanied minor
     * {@link IndicatorType }
     */
    public void setUnAccompaniedMinor(final IndicatorType inValue) {
        this.unAccompaniedMinor = inValue;
    }
    /**
     * Gets the value of the wchc property.
     *
     * @return the wCHC
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getWCHC() {
        return wchc;
    }
    /**
     * Sets the value of the wchc property.
     *
     * @param inValue the new wCHC
     * {@link IndicatorType }
     */
    public void setWCHC(final IndicatorType inValue) {
        this.wchc = inValue;
    }
    /**
     * Gets the value of the wchs property.
     *
     * @return the wCHS
     * possible object is
     * {@link IndicatorType }
     */
    public IndicatorType getWCHS() {
        return wchs;
    }
    /**
     * Sets the value of the wchs property.
     *
     * @param inValue the new wCHS
     * {@link IndicatorType }
     */
    public void setWCHS(final IndicatorType inValue) {
        this.wchs = inValue;
    }
    /**
     * Gets the value of the others property.
     *
     * @return the others
     * possible object is
     * {@link String }
     */
    public String getOthers() {
        return others;
    }
    /**
     * Sets the value of the others property.
     *
     * @param inValue the new others
     * {@link String }
     */
    public void setOthers(final String inValue) {
        this.others = inValue;
    }
}
