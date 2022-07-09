package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;

public class SegmentSummaryDTO extends SummaryDTO {

	private static final long serialVersionUID = -810964042157934057L;

	private String segmentId;
	
	/** The origin. */
	private String originPort;
	
	/** The destination. */
	private String destPort;
	
	/** Segment market Company, only market info for summary interface */
	private String marketCompany;
	
	/** Segment market segment number , only market info for summary interface */
	private String marketSegmentNumber;
	
	/** Segment status*/
	private FlightStatusEnum status;
	
	/** Departure Time */
	private DepartureArrivalTimeDTO departureTime;
	
	private DepartureArrivalTimeDTO arrivalTime;
	
	private String operateSegmentNumber;
	
	private String operateCompany;
	
	private String airCraftType;
	
	private String cabinClass;
	
	private String subClass;
	
	private Boolean flown; 
	
	private boolean canCheckIn;
	
	private boolean checkedIn;
	
	private boolean openToCheckIn;
	
	private boolean postToCheckIn;
	
	private boolean adtk;
	
	private boolean pcc;
	
	private RetrievePnrRebookInfo rebookInfo;
	
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
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
	public String getOperateCompany() {
		return operateCompany;
	}
	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}
	public String getAirCraftType() {
		return airCraftType;
	}
	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}
	public String getCabinClass() {
		return cabinClass;
	}
	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}
	public String getSubClass() {
		return subClass;
	}
	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getMarketCompany() {
		return marketCompany;
	}
	public void setMarketCompany(String marketCompany) {
		this.marketCompany = marketCompany;
	}
	public String getMarketSegmentNumber() {
		return marketSegmentNumber;
	}
	public void setMarketSegmentNumber(String marketSegmentNumber) {
		this.marketSegmentNumber = marketSegmentNumber;
	} 
	public FlightStatusEnum getStatus() {
		return status;
	}
	public void setStatus(FlightStatusEnum status) {
		this.status = status;
	}
	public DepartureArrivalTimeDTO getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(DepartureArrivalTimeDTO departureTime) {
		this.departureTime = departureTime;
	}
	public Boolean getFlown() {
		return flown;
	}
	public void setFlown(Boolean flown) {
		this.flown = flown;
	}
	public boolean isCanCheckIn() {
		return canCheckIn;
	}
	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}
	public boolean isCheckedIn() {
		return checkedIn;
	}
	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	public boolean isOpenToCheckIn() {
		return openToCheckIn;
	}
	public void setOpenToCheckIn(boolean openToCheckIn) {
		this.openToCheckIn = openToCheckIn;
	}
	public boolean isAdtk() {
		return adtk;
	}
	public void setAdtk(boolean adtk) {
		this.adtk = adtk;
	}
	public boolean isPcc() {
		return pcc;
	}
	public void setPcc(boolean pcc) {
		this.pcc = pcc;
	}
	public RetrievePnrRebookInfo getRebookInfo() {
		return rebookInfo;
	}
	public void setRebookInfo(RetrievePnrRebookInfo rebookInfo) {
		this.rebookInfo = rebookInfo;
	}
	public boolean isPostToCheckIn() {
		return postToCheckIn;
	}
	public void setPostToCheckIn(boolean postToCheckIn) {
		this.postToCheckIn = postToCheckIn;
	}
	
}
