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

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The Class FlightIdentifierType.
 *
 * @author s.a.nallamalli
 */
public class FlightIdentifierType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5575343113442974353L;
    /** The flt no. */
    private String fltNo;
    /** The flt sx. */
    private String fltSx;
    /** The carrier code*. */
    private String carrierCode;
     /**
      * Gets the flt no.
      *
      * @return the fltNo
      */
    public String getFltNo() {
        return fltNo;
    }
    /**
     * Sets the flt no.
     *
     * @param inFltNo the new flt no
     */
    public void setFltNo(final String inFltNo) {
        this.fltNo = inFltNo;
    }
    /**
     * Gets the flt sx.
     *
     * @return the fltSx
     */
    public String getFltSx() {
        return fltSx;
    }
    /**
     * Sets the flt sx.
     *
     * @param inFltSx the new flt sx
     */
    public void setFltSx(final String inFltSx) {
        this.fltSx = inFltSx;
    }
    /**
     * Gets the carrier code.
     *
     * @return the carrierCode
     */
    public String getCarrierCode() {
        return carrierCode;
    }
    /**
     * Sets the carrier code.
     *
     * @param inCarrierCode the new carrier code
     */
    public void setCarrierCode(final String inCarrierCode) {
        this.carrierCode = inCarrierCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fltSx", fltSx)
            .append("carrierCode", carrierCode)
            .append("fltNo", fltNo).toString();

    }
}
