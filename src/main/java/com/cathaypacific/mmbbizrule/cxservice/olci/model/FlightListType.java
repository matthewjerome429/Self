/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The Class FlightListType.
 */
public class FlightListType implements Serializable {
      /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4541606545908458950L;
    /** The flt details. */
    private List<FlightDetailsType> fltDetails;
    /**
     * Gets the flt details.
     *
     * @return the fltDetails
     */
    public List<FlightDetailsType> getFltDetails() {
        return fltDetails;
    }
    /**
     * Sets the flt details.
     *
     * @param inFltDetails the new flt details
     */
    public void setFltDetails(final List<FlightDetailsType> inFltDetails) {
        this.fltDetails = inFltDetails;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("fltDetails", fltDetails);
        return builder.toString();
    }


}
