package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Segment {
	
	/** The segment id. */
	private String segmentID;

	/** The origin. */
	private String originPort;
	/** The origin 3 letter country Code */
	private String originCountry;
	
	/** The destination. */
	private String destPort;
	/** The destination 3 letter country code  */
	private String destCountry;
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

	/** the operateSegmentNumber and operateCompany confirmed flag, it is true if invoke air_flightinfo failed */
	private boolean unConfirmedOperateInfo;
	
	/** code share flight Provider */
	private String marketCompany;

	/** The Cabin Class in booking of flight */
	private String cabinClass;

	/** The Sub Class in booking of flight */
	private String subClass;
	
	 /** The Cabin Class in booking of market */
    private String marketCabinClass;

    /** The sub Class in booking of market*/
    private String marketSubClass;

	private SegmentStatus segmentStatus;

	private String originTerminal;

	private String destTerminal;

	private String airCraftType;

	private String[] stops;

	private String totalDuration;

	private Integer numberOfStops;

	private String apiVersion;

	private List<String> usablePrimaryTavelDocs;

	private List<String> usableSecondaryTavelDocs;
	
	private boolean pcc;
	
	private boolean adtk;

	private String trainReminder;
	
	private String trainCase;
 
	/**
	 * check in window start limit time, default value is 2880(48h), 
	 */
	private int checkInWindowOpenTimeLimitMins = 2880;
	/**
	 * check in window end limit time, default value is 90, 
	 */
	private int checkInWindowEndTimeLimitMins = 90;
	/**
	 * the remaining time to open checkin, it should calculate by: system date - (std -checkInWindowOpenTimeLimitMins) , 
	 */
	private int checkInWindowOpenTimeRemainingMins;
	/**
	 * the remaining time to close checkin, it should calculate by: system date - (std -checkInWindowOpenTimeRemainingMins) , 
	 */
	private int checkInWindowEndTimeRemainingMins;
	/**
	 * the remaining time to departure in minute
	 */
	private int departRemainingMins;
	
	private boolean hasCheckedBaggge;

	/** display train pick up number or not*/
	private Boolean trainPNDisplay;

	private RetrievePnrRebookInfo rebookInfo;

	private AdditionalOperatorInfo additionalOperatorInfo;
	
	private UpgradeInfo upgradeInfo;
	
	private String haulType;

	private RtfsFlightStatusInfo rtfsFlightStatusInfo;
	
	private RtfsFlightSummary rtfsSummary;
	/**--------------------Cpr information start----------------------*/
    /** TODO Need to further check if 'canCheckIn' and 'openToCheckIn' are both required */
	/**Can check in flag, from OLCI*/
	private boolean canCheckIn;
	
	/**checked in flag, from OLCI*/
	private boolean checkedIn;
	
	/** has available wifi on flight */
	private boolean hasAvailableWifi;

	/** the number of gate **/
	private String gateNumber;

	public String getSegmentID() {
		return segmentID;
	}

	public void setSegmentID(String segmentID) {
		this.segmentID = segmentID;
	}

	public DepartureArrivalTime getDepartureTime() {
		return this.departureTime;
	}
	
	public DepartureArrivalTime findDepartureTime() {
		if (this.departureTime == null) {
			this.departureTime = new DepartureArrivalTime();
		}
		return this.departureTime;
	}

	public void setDepartureTime(DepartureArrivalTime departureTime) {
		this.departureTime = departureTime;
	}

	public DepartureArrivalTime getArrivalTime() {
		return this.arrivalTime;
	}
	
	public DepartureArrivalTime findArrivalTime() {
		if (this.arrivalTime == null) {
			this.arrivalTime = new DepartureArrivalTime();
		}
		return this.arrivalTime;
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

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
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

	public String getSubClass() {
		return subClass;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
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

	public String[] getStops() {
		return stops;
	}

	public void setStops(String[] stops) {
		this.stops = stops;
	}
	@JsonIgnore
	public Boolean isFlown() {
		if(segmentStatus != null) {
			return segmentStatus.isFlown();
		}
		return false;
	}


	public Integer getNumberOfStops() {
		return numberOfStops;
	}

	public void setNumberOfStops(Integer numberOfStops) {
		this.numberOfStops = numberOfStops;
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public List<String> getUsablePrimaryTavelDocs() {
		if (this.usablePrimaryTavelDocs == null) {
			this.usablePrimaryTavelDocs = new ArrayList<>();
		}
		return usablePrimaryTavelDocs;
	}

	public void setUsablePrimaryTavelDocs(List<String> usablePrimaryTavelDocs) {
		this.usablePrimaryTavelDocs = usablePrimaryTavelDocs;
	}

	public List<String> getUsableSecondaryTavelDocs() {
		if (this.usableSecondaryTavelDocs == null) {
			this.usableSecondaryTavelDocs = new ArrayList<>();
		}
		return usableSecondaryTavelDocs;
	}

	public void setUsableSecondaryTavelDocs(List<String> usableSecondaryTavelDocs) {
		this.usableSecondaryTavelDocs = usableSecondaryTavelDocs;
	}

	public String getTrainReminder() {
		return trainReminder;
	}

	public void setTrainReminder(String trainReminder) {
		this.trainReminder = trainReminder;
	}

	public String getTrainCase() {
		return trainCase;
	}

	public void setTrainCase(String trainCase) {
		this.trainCase = trainCase;
	}

	public Boolean isTrainPNDisplay() {
		return trainPNDisplay;
	}

	public void setTrainPNDisplay(Boolean trainPNDisplay) {
		this.trainPNDisplay = trainPNDisplay;
	}

	public boolean isPcc() {
		return pcc;
	}

	public void setPcc(boolean pcc) {
		this.pcc = pcc;
	}

	public boolean isAdtk() {
		return adtk;
	}

	public void setAdtk(boolean adtk) {
		this.adtk = adtk;
	}

	public boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}
	@JsonIgnore
	public boolean isOpenToCheckIn() {
		return checkInWindowOpenTimeRemainingMins < 0 && checkInWindowEndTimeRemainingMins > 0;
	}
	@JsonIgnore
	public boolean isBeforeCheckIn() {
		return checkInWindowOpenTimeRemainingMins < 0 && checkInWindowEndTimeRemainingMins > 0;
	}
	@JsonIgnore
	public boolean isPostCheckIn() {
		return  checkInWindowEndTimeRemainingMins <= 0;
	}
	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	@JsonIgnore
	public boolean isWithinTwentyFourHrs() {
		return departRemainingMins > 0 && departRemainingMins <= 1440;
	}

	@JsonIgnore
	public boolean isWithinNinetyMins() {
		return departRemainingMins > 0 && departRemainingMins <= 90;
	}
	@JsonIgnore
	public String getCheckInRemainingTime() {
		if(isOpenToCheckIn()) {
			return String.valueOf(checkInWindowEndTimeRemainingMins*60000);
		}else {
			return null;
		}
	}

	public SegmentStatus getSegmentStatus() {
		return segmentStatus;
	}

	public void setSegmentStatus(SegmentStatus segmentStatus) {
		this.segmentStatus = segmentStatus;
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

	public RetrievePnrRebookInfo getRebookInfo() {
		return rebookInfo;
	}

	public void setRebookInfo(RetrievePnrRebookInfo rebookInfo) {
		this.rebookInfo = rebookInfo;
	}

	public AdditionalOperatorInfo getAdditionalOperatorInfo() {
		return additionalOperatorInfo;
	}

	public void setAdditionalOperatorInfo(AdditionalOperatorInfo additionalOperatorInfo) {
		this.additionalOperatorInfo = additionalOperatorInfo;
	}

	public boolean isHasCheckedBaggge() {
		return hasCheckedBaggge;
	}

	public void setHasCheckedBaggge(boolean hasCheckedBaggge) {
		this.hasCheckedBaggge = hasCheckedBaggge;
	}

	public UpgradeInfo getUpgradeInfo() {
		return upgradeInfo;
	}

	public void setUpgradeInfo(UpgradeInfo upgradeInfo) {
		this.upgradeInfo = upgradeInfo;
	}

	@JsonIgnore
	public String getOriginalSubClass() {
		return this.upgradeInfo == null ? null:this.upgradeInfo.getFromSubClass();
	}

	public String getHaulType() {
		return haulType;
	}

	public void setHaulType(String haulType) {
		this.haulType = haulType;
	}
	public RtfsFlightStatusInfo getRtfsFlightStatusInfo() {
		return rtfsFlightStatusInfo;
	}

	public void setRtfsFlightStatusInfo(RtfsFlightStatusInfo rtfsFlightStatusInfo) {
		this.rtfsFlightStatusInfo = rtfsFlightStatusInfo;
	}

	public RtfsFlightSummary getRtfsSummary() {
		return rtfsSummary;
	}

	public void setRtfsSummary(RtfsFlightSummary rtfsSummary) {
		this.rtfsSummary = rtfsSummary;
	}

	public boolean isUnConfirmedOperateInfo() {
		return unConfirmedOperateInfo;
	}

	public void setUnConfirmedOperateInfo(boolean unConfirmedOperateInfo) {
		this.unConfirmedOperateInfo = unConfirmedOperateInfo;
	}

	public int getCheckInWindowOpenTimeLimitMins() {
		return checkInWindowOpenTimeLimitMins;
	}

	public void setCheckInWindowOpenTimeLimitMins(int checkInWindowOpenTimeLimitMins) {
		this.checkInWindowOpenTimeLimitMins = checkInWindowOpenTimeLimitMins;
	}

	public int getCheckInWindowEndTimeLimitMins() {
		return checkInWindowEndTimeLimitMins;
	}

	public void setCheckInWindowEndTimeLimitMins(int checkInWindowEndTimeLimitMins) {
		this.checkInWindowEndTimeLimitMins = checkInWindowEndTimeLimitMins;
	}

	public int getCheckInWindowOpenTimeRemainingMins() {
		return checkInWindowOpenTimeRemainingMins;
	}

	public void setCheckInWindowOpenTimeRemainingMins(int checkInWindowOpenTimeRemainingMins) {
		this.checkInWindowOpenTimeRemainingMins = checkInWindowOpenTimeRemainingMins;
	}

	public int getCheckInWindowEndTimeRemainingMins() {
		return checkInWindowEndTimeRemainingMins;
	}

	public void setCheckInWindowEndTimeRemainingMins(int checkInWindowEndTimeRemainingMins) {
		this.checkInWindowEndTimeRemainingMins = checkInWindowEndTimeRemainingMins;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public boolean isHasAvailableWifi() {
		return hasAvailableWifi;
	}

	public void setHasAvailableWifi(boolean hasAvailableWifi) {
		this.hasAvailableWifi = hasAvailableWifi;
	}

	public int getDepartRemainingMins() {
		return departRemainingMins;
	}

	public void setDepartRemainingMins(int departRemainingMins) {
		this.departRemainingMins = departRemainingMins;
	}

	public String getGateNumber() {
		return gateNumber;
	}

	public void setGateNumber(String gateNumber) {
		this.gateNumber = gateNumber;
	}
}
