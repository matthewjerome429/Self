package com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;

public class CanCancelCheckInSegmentDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -6532465045505575901L;
	
	/** The segment id. */
	private String segmentId;
	
    /** The origin. */
    private String originPort;
    
    /** The destination. */
    private String destPort;
    
    /** The detailed departure time. */
    private DepartureArrivalTimeDTO departureTime;
    
    /** The detailed arrival time. */
    private DepartureArrivalTimeDTO arrivalTime; 
    
    /** original flight Number */
    private String operateSegmentNumber;
    
    /** code share flight Number */
    private String marketSegmentNumber;
    
    /** original flight Provider */
    private String operateCompany;
    
    /** code share flight Provider */
    private String marketCompany;
    
    private FlightStatusEnum status;
    
    private String airCraftType;
    
    private boolean isFlown;
    
    private boolean checkedIn;
    
    private boolean checkInStandBy;

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

	public DepartureArrivalTimeDTO getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DepartureArrivalTimeDTO departureTime) {
		this.departureTime = departureTime;
	}

	public DepartureArrivalTimeDTO getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DepartureArrivalTimeDTO arrivalTime) {
		this.arrivalTime = arrivalTime;
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

	public String getAirCraftType() {
		return airCraftType;
	}

	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}

	public boolean isFlown() {
		return isFlown;
	}

	public void setFlown(boolean isFlown) {
		this.isFlown = isFlown;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public boolean isCheckInStandBy() {
		return checkInStandBy;
	}

	public void setCheckInStandBy(boolean checkInStandBy) {
		this.checkInStandBy = checkInStandBy;
	}
    
}
