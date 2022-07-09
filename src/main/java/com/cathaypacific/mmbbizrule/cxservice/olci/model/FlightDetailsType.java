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
 * The Class FlightDetailsType.
 */
public class FlightDetailsType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 608748698839534491L;
    /** The flt status. */
    private String fltStatus;
    /** The flight date. */
    private String flightDate;
    /** The origin. */
    private String origin;
    /** The destination. */
    private String destination;
    /** The flight identifier type. */
    private FlightIdentifierType flightIdentifierType;
    /** The fltinfo. */
    private FlightInformationType fltinfo;
    /** The fltinfo. */
    private String flightDateTime;

    /**CR451 The e-ticket number*/
    private String eticketNumber;
    /**
     * @return eticket Number
     */
    public String getEticketNumber() {
        return eticketNumber;
    }
    /**
     * @param _eticketNumber eticketNumber
     */
    public void setEticketNumber(final String _eticketNumber) {
        this.eticketNumber = _eticketNumber;
    }
    /**CR451 end*/
    /**
     * Sets the flt status.
     *
     * @param inFltStatus
     *            the new flt status
     */
    public void setFltStatus(final String inFltStatus) {
        this.fltStatus = inFltStatus;
    }
    /**
     * Gets the flt status.
     *
     * @return the flt status
     */
    public String getFltStatus() {
        return this.fltStatus;
    }
    /**
     * Gets the fltinfo.
     *
     * @return the fltinfo
     */
    public FlightInformationType getFltinfo() {
        return this.fltinfo;
    }
    /**
     * Sets the fltinfo.
     *
     * @param inFltinfo
     *            the new fltinfo
     */
    public void setFltinfo(final FlightInformationType inFltinfo) {
        this.fltinfo = inFltinfo;
    }
    /**
     * Gets the flight date.
     *
     * @return the flightDate
     */
    public String getFlightDate() {
        return this.flightDate;
    }
    /**
     * Sets the flight date.
     *
     * @param inFlightDate
     *            the new flight date
     */
    public void setFlightDate(final String inFlightDate) {
        this.flightDate = inFlightDate;
    }
    /**
     * Gets the origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return this.origin;
    }
    /**
     * Sets the origin.
     *
     * @param inOrigin
     *            the new origin
     */
    public void setOrigin(final String inOrigin) {
        this.origin = inOrigin;
    }
    /**
     * Gets the destination.
     *
     * @return the destination
     */
    public String getDestination() {
        return this.destination;
    }
    /**
     * Sets the destination.
     *
     * @param inDestination
     *            the new destination
     */
    public void setDestination(final String inDestination) {
        this.destination = inDestination;
    }
    /**
     * Sets the flight identifier type.
     *
     * @param inFlightIdentifierType
     *            the new flight identifier type
     */
    public void setFlightIdentifierType(
            final FlightIdentifierType inFlightIdentifierType) {
        this.flightIdentifierType = inFlightIdentifierType;
    }
    /**
     * Gets the flight identifier type.
     *
     * @return the flightIdentifierType
     */
    public FlightIdentifierType getFlightIdentifierType() {
        return this.flightIdentifierType;
    }

    public String getFlightDateTime() {
        return flightDateTime;
    }

    public void setFlightDateTime(String flightDateTime) {
        this.flightDateTime = flightDateTime;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("fltStatus", fltStatus).append("flightDate", flightDate)
                .append("origin", origin).append("destination", destination)
                .append("flightIdentifierType", flightIdentifierType)
                .append("fltinfo", fltinfo)
                .append("flightDateTime", flightDateTime)
                .append("eticketNumber", eticketNumber);
        return builder.toString();
    }


}
