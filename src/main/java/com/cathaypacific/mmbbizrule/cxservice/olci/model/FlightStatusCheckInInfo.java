/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The Class FlightStatusCheckInInfo.
 */
public class FlightStatusCheckInInfo implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4944657402053063381L;
    /** The flight list info type. */
    private FlightListType flightListInfoType;
    /**
     * Sets the flight list info type.
     *
     * @param inFlightListInfoType the new flight list info type
     */
    public void setFlightListInfoType(final FlightListType inFlightListInfoType) {
        this.flightListInfoType = inFlightListInfoType;
    }
    /**
     * Gets the flight list info type.
     *
     * @return the flight list info type
     */
    public FlightListType getFlightListInfoType() {
        return flightListInfoType;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("flightListInfoType", flightListInfoType);
        return builder.toString();
    }
}
