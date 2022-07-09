/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
import java.util.List;
/**
 * The Class PassengerInfoType.
 */
public class PassengerInfoType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3826149863191714432L;
    /** The personal info. */
    /** The travel documents. */
    private PersonalInfoType personalInfo;
    private TravelDocumentInfoType travelDocuments;
    /** The nationality. */
    private String nationality;
    /** The ffp id. */
    private String ffpId;
    /** The ffp number. */
    private String ffpNumber;
    /** The special request. */
    private SpecialRequestType specialRequest;
    /** The cabin class. */
    private String cabinClass;
    /** The sub class. */
    private String subClass;
    /** The check in indicator. */
    private String checkInIndicator;
    /** The boarding pass indicator. */
    private String boardingPassIndicator;
    /** The boarding sequence. */
    private String boardingSequence;
    /** The group indicator. */
    private IndicatorType groupIndicator;
    /** The staff indicator. */
    private String staffIndicator;
    /** The seat num. */
    private String seatNum;
    private boolean extraLegRoomSeat;
    private boolean seatPaid;
    /** The tier type. */
    private String tierType;
    /** The one world tier code. */
    private String oneWorldTierCode;
    /** The cancel check in count. */
    private String cancelCheckInCount;

    /** The departure gate info.  */
    private String departureGateInfo ;
    
    /** CM Lounge **/
    private Lounge cmLounge;
    
    private boolean displayOnly;

    private boolean hasCheckedBaggage;
    
    /** reverse checkin carrier **/
	private String reverseCheckinCarrier;
    
    /** CPR transit */
    private Boolean isTransit;
    
    /** ssr list, serviceType = S, actionCode= MCF */
    private List<String> ssrList;
    
    /** sk list,serviceType = K, actionCode= MCF */
    private List<String> skList;

	public Boolean getIsTransit() {
		return isTransit;
	}

	public void setIsTransit(Boolean isTransit) {
		this.isTransit = isTransit;
	}

	/**
     * Get departure gate info.
     * @return departureGateInfo type of String
     */
    public String getDepartureGateInfo() {
        return departureGateInfo;
    }

    /**
     * set departure gate info.
     * @param departureGateInfo type of String
     */
    public void setDepartureGateInfo(final String _departureGateInfo) {
        this.departureGateInfo = _departureGateInfo;
    }

    /**
     * Gets the personal info.
     *
     * @return the personal info
     */
    public PersonalInfoType getPersonalInfo() {
        return personalInfo;
    }
    /**
     * Sets the personal info.
     *
     * @param inPersonalInfo
     *            the new personal info
     */
    public void setPersonalInfo(final PersonalInfoType inPersonalInfo) {
        this.personalInfo = inPersonalInfo;
    }
    /**
     * Gets the travel documents.
     *
     * @return the travel documents
     */
    public TravelDocumentInfoType getTravelDocuments() {
        return travelDocuments;
    }
    /**
     * Sets the travel documents.
     *
     * @param inTravelDocuments
     *            the new travel documents
     */
    public void setTravelDocuments(final TravelDocumentInfoType inTravelDocuments) {
        this.travelDocuments = inTravelDocuments;
    }
    /**
     * Gets the nationality.
     *
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }
    /**
     * Sets the nationality.
     *
     * @param inNationality
     *            the new nationality
     */
    public void setNationality(final String inNationality) {
        this.nationality = inNationality;
    }
    /**
     * Gets the ffp id.
     *
     * @return the ffp id
     */
    public String getFfpId() {
        return ffpId;
    }
    /**
     * Sets the ffp id.
     *
     * @param inFFPId
     *            the new ffp id
     */
    public void setFfpId(final String inFFPId) {
        this.ffpId = inFFPId;
    }
    /**
     * Gets the ffp number.
     *
     * @return the ffp number
     */
    public String getFfpNumber() {
        return ffpNumber;
    }
    /**
     * Sets the ffp number.
     *
     * @param inFFPNumber
     *            the new ffp number
     */
    public void setFfpNumber(final String inFFPNumber) {
        this.ffpNumber = inFFPNumber;
    }
    /**
     * Gets the cabin class.
     *
     * @return the cabin class
     */
    public String getCabinClass() {
        return cabinClass;
    }
    /**
     * Sets the cabin class.
     *
     * @param inCabinClass
     *            the new cabin class
     */
    public void setCabinClass(final String inCabinClass) {
        this.cabinClass = inCabinClass;
    }
    /**
     * Gets the sub class.
     *
     * @return the sub class
     */
    public String getSubClass() {
        return subClass;
    }
    /**
     * Sets the sub class.
     *
     * @param inSubClass
     *            the new sub class
     */
    public void setSubClass(final String inSubClass) {
        this.subClass = inSubClass;
    }
    /**
     * Gets the check in indicator.
     *
     * @return the check in indicator
     */
    public String getCheckInIndicator() {
        return checkInIndicator;
    }
    /**
     * Sets the check in indicator.
     *
     * @param inCheckInIndicator
     *            the new check in indicator
     */
    public void setCheckInIndicator(final String inCheckInIndicator) {
        this.checkInIndicator = inCheckInIndicator;
    }
    /**
     * Gets the boarding pass indicator.
     *
     * @return the boarding pass indicator
     */
    public String getBoardingPassIndicator() {
        return boardingPassIndicator;
    }
    /**
     * Sets the boarding pass indicator.
     *
     * @param inBoardingPassIndicator
     *            the new boarding pass indicator
     */
    public void setBoardingPassIndicator(final String inBoardingPassIndicator) {
        this.boardingPassIndicator = inBoardingPassIndicator;
    }
    /**
     * Gets the boarding sequence.
     *
     * @return the boarding sequence
     */
    public String getBoardingSequence() {
        return boardingSequence;
    }
    /**
     * Sets the boarding sequence.
     *
     * @param inBoardingSequence
     *            the new boarding sequence
     */
    public void setBoardingSequence(final String inBoardingSequence) {
        this.boardingSequence = inBoardingSequence;
    }
    /**
     * Gets the staff indicator.
     *
     * @return the staff indicator
     */
    public String getStaffIndicator() {
        return staffIndicator;
    }
    /**
     * Sets the staff indicator.
     *
     * @param inSaffIndicator
     *            the new staff indicator
     */
    public void setStaffIndicator(final String inSaffIndicator) {
        this.staffIndicator = inSaffIndicator;
    }
    /**
     * Gets the seat num.
     *
     * @return the seat num
     */
    public String getSeatNum() {
        return seatNum;
    }
    /**
     * Sets the seat num.
     *
     * @param inSeatNum
     *            the new seat num
     */
    public void setSeatNum(final String inSeatNum) {
        this.seatNum = inSeatNum;
    }
    /**
     * Gets the tier type.
     *
     * @return the tier type
     */
    public String getTierType() {
        return tierType;
    }
    /**
     * Sets the tier type.
     *
     * @param inTierType
     *            the new tier type
     */
    public void setTierType(final String inTierType) {
        this.tierType = inTierType;
    }
    /**
     * Gets the one world tier code.
     *
     * @return the one world tier code
     */
    public String getOneWorldTierCode() {
        return oneWorldTierCode;
    }
    /**
     * Sets the one world tier code.
     *
     * @param inOneWorldTierCode
     *            the new one world tier code
     */
    public void setOneWorldTierCode(final String inOneWorldTierCode) {
        this.oneWorldTierCode = inOneWorldTierCode;
    }
    /**
     * Gets the cancel check in count.
     *
     * @return the cancel check in count
     */
    public String getCancelCheckInCount() {
        return cancelCheckInCount;
    }
    /**
     * Sets the cancel check in count.
     *
     * @param inCancelCheckInCount
     *            the new cancel check in count
     */
    public void setCancelCheckInCount(final String inCancelCheckInCount) {
        this.cancelCheckInCount = inCancelCheckInCount;
    }
    /**
     * Sets the special request.
     *
     * @param inSpecialRequest
     *            the specialRequest to set
     */
    public void setSpecialRequest(final SpecialRequestType inSpecialRequest) {
        this.specialRequest = inSpecialRequest;
    }
    /**
     * Gets the special request.
     *
     * @return the specialRequest
     */
    public SpecialRequestType getSpecialRequest() {
        return specialRequest;
    }
    /**
     * Sets the group indicator.
     *
     * @param inGroupIndicator
     *            the groupIndicator to set
     */
    public void setGroupIndicator(final IndicatorType inGroupIndicator) {
        this.groupIndicator = inGroupIndicator;
    }
    
    /**
	 * @return the extraLegRoomSeat
	 */
	public boolean isExtraLegRoomSeat() {
		return extraLegRoomSeat;
	}
	/**
	 * @param extraLegRoomSeat the extraLegRoomSeat to set
	 */
	public void setExtraLegRoomSeat(boolean extraLegRoomSeat) {
		this.extraLegRoomSeat = extraLegRoomSeat;
	}

	/**
	 * @return the seatPaid
	 */
	public boolean isSeatPaid() {
		return seatPaid;
	}

	/**
	 * @param seatPaid the seatPaid to set
	 */
	public void setSeatPaid(boolean seatPaid) {
		this.seatPaid = seatPaid;
	}

	/**
     * Gets the group indicator.
     *
     * @return the groupIndicator
     */
    public IndicatorType getGroupIndicator() {
        return groupIndicator;
    }
    
    public Lounge getCmLounge() {
		return cmLounge;
	}

	public void setCmLounge(Lounge cmLounge) {
		this.cmLounge = cmLounge;
	}

	/**
	 * @return the displayOnly
	 */
	public boolean isDisplayOnly() {
		return displayOnly;
	}

	/**
	 * @param displayOnly the displayOnly to set
	 */
	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}

    public boolean isHasCheckedBaggage() {
        return hasCheckedBaggage;
    }

    public void setHasCheckedBaggage(boolean hasCheckedBaggage) {
        this.hasCheckedBaggage = hasCheckedBaggage;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
	@Override
	public String toString() {
		return "PassengerInfoType [personalInfo=" + personalInfo
				+ ", travelDocuments=" + travelDocuments + ", nationality="
				+ nationality + ", ffpId=" + ffpId + ", ffpNumber=" + ffpNumber
				+ ", specialRequest=" + specialRequest + ", cabinClass="
				+ cabinClass + ", subClass=" + subClass + ", checkInIndicator="
				+ checkInIndicator + ", boardingPassIndicator="
				+ boardingPassIndicator + ", boardingSequence="
				+ boardingSequence + ", groupIndicator=" + groupIndicator
				+ ", staffIndicator=" + staffIndicator + ", seatNum=" + seatNum
				+ ", tierType=" + tierType + ", oneWorldTierCode="
				+ oneWorldTierCode + ", cancelCheckInCount="
				+ cancelCheckInCount + ", departureGateInfo="
				+ departureGateInfo + "]";
	}

	public String getReverseCheckinCarrier() {
		return reverseCheckinCarrier;
	}

	public void setReverseCheckinCarrier(String reverseCheckinCarrier) {
		this.reverseCheckinCarrier = reverseCheckinCarrier;
	}

	public List<String> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<String> ssrList) {
		this.ssrList = ssrList;
	}

	public List<String> getSkList() {
		return skList;
	}

	public void setSkList(List<String> skList) {
		this.skList = skList;
	}

}
