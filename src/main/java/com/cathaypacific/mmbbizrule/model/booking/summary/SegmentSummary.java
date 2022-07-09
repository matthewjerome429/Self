package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.text.ParseException;
import java.util.Date;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;

public class SegmentSummary extends SectorSummaryBase {
	
	private static final long serialVersionUID = 5248394713155073036L;

	private String segmentId;
	
	/** The origin. */
	private String originPort;

	/** The destination. */
	private String destPort;

	/** The detailed departure time. */
	private DepartureArrivalTime departureTime;

	/** The detailed arrival time. */
	private DepartureArrivalTime arrivalTime;

	/** original flight Number */
	private String operateSegmentNumber;

	/** code share flight Number */
	private String marketSegmentNumber;

	/** original flight Provider */
	private String operateCompany;

	/** code share flight Provider */
	private String marketCompany;

	private SegmentStatus segmentStatus;

	/** type of segment LCH,BUS or TRN */
	private String airCraftType;

	private Integer numberOfStops;
	
    /** The Cabin Class in booking of flight*/
    private String cabinClass;
    
    /** The Sub Class in booking of flight*/
    private String subClass;
    
	private boolean canCheckIn;
	
	private boolean checkedIn;
	
	private boolean openToCheckIn;
	
	private boolean postCheckIn;
	
	private boolean adtk;
	
	private boolean pcc;
	
	private RetrievePnrRebookInfo rebookInfo;
	
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

	public DepartureArrivalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DepartureArrivalTime departureTime) {
		this.departureTime = departureTime;
	}

	public DepartureArrivalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DepartureArrivalTime arrivalTime) {
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

	public boolean isFlown() {
		if(segmentStatus != null) {
			return segmentStatus.isFlown();
		}
		return false;
	}

	public SegmentStatus getSegmentStatus() {
		return segmentStatus;
	}

	public void setSegmentStatus(SegmentStatus segmentStatus) {
		this.segmentStatus = segmentStatus;
	}

	public String getAirCraftType() {
		return airCraftType;
	}

	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}

	public Integer getNumberOfStops() {
		return numberOfStops;
	}

	public void setNumberOfStops(Integer numberOfStops) {
		this.numberOfStops = numberOfStops;
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

	public Date getGMTArrivalDate() {
		Date arrivalDate = null;
		try {
			arrivalDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, arrivalTime.getTime(), arrivalTime.getTimeZoneOffset());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return arrivalDate;
	}

	public RetrievePnrRebookInfo getRebookInfo() {
		return rebookInfo;
	}

	public void setRebookInfo(RetrievePnrRebookInfo rebookInfo) {
		this.rebookInfo = rebookInfo;
	}

	public boolean isPostCheckIn() {
		return postCheckIn;
	}

	public void setPostCheckIn(boolean postCheckIn) {
		this.postCheckIn = postCheckIn;
	}
	
}