package com.cathaypacific.mmbbizrule.dto.response.specialservice;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;

public class RequestAssistanceSegmentDTO implements Serializable {

	private static final long serialVersionUID = 677412176065114013L;

	/** The segment id. */
    private String segmentId;
    
    /** The origin. */
    private String originPort;
    
    /** The destination. */
    private String destPort;
    
    /** original flight Number */
    private String operateSegmentNumber;
    
    /** code share flight Number */
    private String marketSegmentNumber;
    
    /** original flight Provider */
    private String operateCompany;
    
	/** the operateSegmentNumber and operateCompany confirmed flag, it is true if invoke air_flightinfo failed */
	private boolean unConfirmedOperateInfo;

    /** code share flight Provider */
    private String marketCompany;
    
    private FlightStatusEnum status;
    
    private String originTerminal;
    
    private String destTerminal;

	private String airCraftType;

	private Boolean isFlown;
	
	private boolean withinFortyEightHrs;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getOperateSegmentNumber() {
		return operateSegmentNumber;
	}

	public void setOperateSegmentNumber(String operateSegmentNumber) {
		this.operateSegmentNumber = operateSegmentNumber;
	}

	public String getMarketSegmentNumber() {
		return marketSegmentNumber;
	}

	public void setMarketSegmentNumber(String marketSegmentNumber) {
		this.marketSegmentNumber = marketSegmentNumber;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public boolean isUnConfirmedOperateInfo() {
		return unConfirmedOperateInfo;
	}

	public void setUnConfirmedOperateInfo(boolean unConfirmedOperateInfo) {
		this.unConfirmedOperateInfo = unConfirmedOperateInfo;
	}

	public String getMarketCompany() {
		return marketCompany;
	}

	public void setMarketCompany(String marketCompany) {
		this.marketCompany = marketCompany;
	}

	public FlightStatusEnum getStatus() {
		return status;
	}

	public void setStatus(FlightStatusEnum status) {
		this.status = status;
	}

	public String getOriginTerminal() {
		return originTerminal;
	}

	public void setOriginTerminal(String originTerminal) {
		this.originTerminal = originTerminal;
	}

	public String getDestTerminal() {
		return destTerminal;
	}

	public void setDestTerminal(String destTerminal) {
		this.destTerminal = destTerminal;
	}

	public String getAirCraftType() {
		return airCraftType;
	}

	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}

	public Boolean getIsFlown() {
		return isFlown;
	}

	public void setIsFlown(Boolean isFlown) {
		this.isFlown = isFlown;
	}

	public boolean isWithinFortyEightHrs() {
		return withinFortyEightHrs;
	}

	public void setWithinFortyEightHrs(boolean withinFortyEightHrs) {
		this.withinFortyEightHrs = withinFortyEightHrs;
	}
	
}
