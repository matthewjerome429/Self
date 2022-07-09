package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SegmentCustomizedInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 6864617691419485140L;

	/** segment id */
	private String segmentId;

	/** air craft type   */
	private String airCraftType;
	
	/** arrival time */
	private DepartureArrivalTimeCustomizedInfoDTO arrivalTime;
	
	/** departure time */
	private DepartureArrivalTimeCustomizedInfoDTO departureTime;
	
	/** cabin class */
	private String cabinClass;
	
	/** cabin subclass */
	private String subClass;
	
	 /** The Cabin Class in booking of market */
    private String marketCabinClass;

    /** The sub Class in booking of market*/
    private String marketSubClass;
	
	/** origin port */
	private String originPort;
	
	/** destination port */
	private String destPort;
	
	/** marketing company */
	private String marketCompany ;
	
	/** marketing segment number */
	private String marketSegmentNumber;
	
	/** operating company */
	private String operateCompany;
	
	/** operating segment number */
	private String operateSegmentNumber;
	
	/** segment status */
	private FlightStatusEnum status;
	
	/** segment is flown */
	private Boolean flown;
	
    /** booking is display only */
    private Boolean disable;
	
	/** segment haul type */
	private String haulType;
	
	/** upgrade info */
	private UpgradeCustomizedInfo upgradeInfo;
	
	/** check in status */
	private boolean checkedIn;
	
	/** post check-in window */
	private boolean postCheckIn;
	
	/** is open to check-in */
	private boolean openToCheckIn;
	
	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public DepartureArrivalTimeCustomizedInfoDTO getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DepartureArrivalTimeCustomizedInfoDTO arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public DepartureArrivalTimeCustomizedInfoDTO getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DepartureArrivalTimeCustomizedInfoDTO departureTime) {
		this.departureTime = departureTime;
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

	public String getMarketCabinClass() {
		return marketCabinClass;
	}

	public void setMarketCabinClass(String marketCabinClass) {
		this.marketCabinClass = marketCabinClass;
	}

	public String getMarketSubClass() {
		return marketSubClass;
	}

	public void setMarketSubClass(String marketSubClass) {
		this.marketSubClass = marketSubClass;
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

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public String getOperateSegmentNumber() {
		return operateSegmentNumber;
	}

	public void setOperateSegmentNumber(String operateSegmentNumber) {
		this.operateSegmentNumber = operateSegmentNumber;
	}

	public FlightStatusEnum getStatus() {
		return status;
	}

	public void setStatus(FlightStatusEnum status) {
		this.status = status;
	}

	public Boolean getFlown() {
		return flown;
	}

	public void setFlown(Boolean flown) {
		this.flown = flown;
	}

	@JsonIgnore
	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String getHaulType() {
		return haulType;
	}

	public void setHaulType(String haulType) {
		this.haulType = haulType;
	}

	public String getAirCraftType() {
		return airCraftType;
	}

	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}

	public UpgradeCustomizedInfo getUpgradeInfo() {
		return upgradeInfo;
	}

	public void setUpgradeInfo(UpgradeCustomizedInfo upgradeInfo) {
		this.upgradeInfo = upgradeInfo;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public boolean isPostCheckIn() {
		return postCheckIn;
	}

	public void setPostCheckIn(boolean postCheckIn) {
		this.postCheckIn = postCheckIn;
	}

	public boolean isOpenToCheckIn() {
		return openToCheckIn;
	}

	public void setOpenToCheckIn(boolean openToCheckIn) {
		this.openToCheckIn = openToCheckIn;
	}


}
