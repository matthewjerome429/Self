/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The Class PassengerCheckInInfo.
 */
public class PassengerCheckInInfo implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2669304706467557734L;
    /** The passenger details. */
    private List<PassengerInfoType> passengerDetails;
    /** The flight status check in info. */
    private FlightStatusCheckInInfo flightStatusCheckInInfo;
    /** The data origin. */
    private String source;
    /** The error code. */
    private String brlErrorCode;
    
    /** P2 ============start============= */
    /** been used to store carrierCode */
    private String carrierCode;
    /** been used to store fltNo */
    private String fltNo;
    /** been used to store flightDate */
    private Date flightDate;
    /** been used to store RLOC from BRL */
    private String rlocInfo;
    /** P2 ============end=============== */
    private String journeyID;
    
    
    // =======add ICEMObil CR#3=============//
    /** disallowExitRowForSSR */
    private boolean disallowExitRowForSSR ;
    
    
    
    /**
	 * @return the disallowExitRowForSSR
	 */
	public boolean isDisallowExitRowForSSR() {
		return disallowExitRowForSSR;
	}
	/**
	 * @param disallowExitRowForSSR the disallowExitRowForSSR to set
	 */
	public void setDisallowExitRowForSSR(boolean disallowExitRowForSSR) {
		this.disallowExitRowForSSR = disallowExitRowForSSR;
	}
	/**
	 * @return the journeyID
	 */
	public String getJourneyID() {
		return journeyID;
	}
	/**
	 * @param journeyID the journeyID to set
	 */
	public void setJourneyID(String journeyID) {
		this.journeyID = journeyID;
	}
	/**
     * Gets the passenger details.
     *
     * @return the passengerDetails
     */
    public List<PassengerInfoType> getPassengerDetails() {
        return passengerDetails;
    }
    /**
     * Sets the passenger details.
     *
     * @param inPassengerDetails the new passenger details
     */
    public void setPassengerDetails(final List<PassengerInfoType> inPassengerDetails) {
        this.passengerDetails = inPassengerDetails;
    }
    /**
     * Gets the flight status check in info.
     *
     * @return the flightStatusCheckInInfo
     */
    public FlightStatusCheckInInfo getFlightStatusCheckInInfo() {
        return flightStatusCheckInInfo;
    }
    /**
     * Sets the flight status check in info.
     *
     * @param inFlightStatusCheckInInfo the new flight status check in info
     */
    public void setFlightStatusCheckInInfo(final FlightStatusCheckInInfo inFlightStatusCheckInInfo) {
        this.flightStatusCheckInInfo = inFlightStatusCheckInInfo;
    }
    /**
     * Gets the origin of data.
     *
     * @return data origin
     */
    public String getSource() {
        return source;
    }
    /**
     * Sets the origin of data.
     *
     * @param data origin
     */
    public void setSource(String source) {
        this.source = source;
    }
     /**
     * Gets error code.
     *
     * @return error code
     */
    public String getBrlErrorCode() {
        return brlErrorCode;
    }
    /**
     * Sets the error code.
     *
     * @param error code
     */
	public void setBrlErrorCode(String brlErrorCode) {
		this.brlErrorCode = brlErrorCode;
	}
    
	/**
	 * @return the carrierCode
	 */
	public String getCarrierCode() {
		return carrierCode;
	}
	/**
	 * @param carrierCode the carrierCode to set
	 */
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	/**
	 * @return the fltNo
	 */
	public String getFltNo() {
		return fltNo;
	}
	/**
	 * @param fltNo the fltNo to set
	 */
	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}
	/**
	 * @return the flightDate
	 */
	public Date getFlightDate() {
		return flightDate;
	}
	/**
	 * @param flightDate the flightDate to set
	 */
	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}
	
	/**
	 * @return the rlocInfo
	 */
	public String getRlocInfo() {
		return rlocInfo;
	}
	/**
	 * @param rlocInfo the rlocInfo to set
	 */
	public void setRlocInfo(String rlocInfo) {
		this.rlocInfo = rlocInfo;
	}
	public String toString() {
		return new ToStringBuilder(this)
			.append("source", this.source)
			.append("brlErrorCode", this.brlErrorCode)
			.append("flightStatusCheckInInfo", this.flightStatusCheckInInfo)
			.append("passengerDetails", this.passengerDetails)
			.append("rloc",this.rlocInfo)
			.toString();
	}
}
