/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
/**
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
/**
 * The Class FlightInformationType.
 *
 * @author h.kumar.pamidi
 */
public class FlightInformationType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 462028642397549142L;
    /** The flt idnt. */
    private FlightIdentifierType fltIdnt;
    /** The carrier code. */
    private String carrierCode;
    /** The dep date. */
    private String depDate;
    /** The flt orig. */
    private String fltOrig;
    /** The flt dest. */
    private String fltDest;
    /**
     * Gets the flt idnt.
     *
     * @return the fltIdnt
     */
    public FlightIdentifierType getFltIdnt() {
        return this.fltIdnt;
    }
    /**
     * Sets the flt idnt.
     *
     * @param inFltIdnt
     *            the new flt idnt
     */
    public void setFltIdnt(final FlightIdentifierType inFltIdnt) {
        this.fltIdnt = inFltIdnt;
    }
    /**
     * Gets the carrier code.
     *
     * @return the carrierCode
     */
    public String getCarrierCode() {
        return this.carrierCode;
    }
    /**
     * Gets the dep date.
     *
     * @return the depDate
     */
    public String getDepDate() {
        return this.depDate;
    }
    /**
     * Gets the flt orig.
     *
     * @return the fltOrig
     */
    public String getFltOrig() {
        return this.fltOrig;
    }
    /**
     * Gets the flt dest.
     *
     * @return the fltDest
     */
    public String getFltDest() {
        return this.fltDest;
    }
    /**
     * Sets the carrier code.
     *
     * @param inCarrierCode
     *            the new carrier code
     */
    public void setCarrierCode(final String inCarrierCode) {
        this.carrierCode = inCarrierCode;
    }
    /**
     * Sets the dep date.
     *
     * @param inDepDate
     *            the new dep date
     */
    public void setDepDate(final String inDepDate) {
        this.depDate = inDepDate;
    }
    /**
     * Sets the flt orig.
     *
     * @param inFltOrig
     *            the new flt orig
     */
    public void setFltOrig(final String inFltOrig) {
        this.fltOrig = inFltOrig;
    }
    /**
     * Sets the flt dest.
     *
     * @param inFltDest
     *            the new flt dest
     */
    public void setFltDest(final String inFltDest) {
        this.fltDest = inFltDest;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FlightInformationType [fltIdnt=");
        builder.append(this.fltIdnt);
        builder.append(", carrierCode=");
        builder.append(this.carrierCode);
        builder.append(", depDate=");
        builder.append(this.depDate);
        builder.append(", fltOrig=");
        builder.append(this.fltOrig);
        builder.append(", fltDest=");
        builder.append(this.fltDest);
        builder.append(']');
        return builder.toString();
    }
}
