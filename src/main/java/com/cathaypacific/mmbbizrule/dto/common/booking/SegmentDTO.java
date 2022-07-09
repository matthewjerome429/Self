package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightStatusInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.UpgradeInfo;

public class SegmentDTO extends BookingOrderDTO implements Serializable{
	
	private static final long serialVersionUID = 3758722354116070364L;

	/** The segment id. */
    private String segmentId;
    
    /** The flight number. */
    private String flightNumber;
    
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
    
	/** the operateSegmentNumber and operateCompany confirmed flag, it is true if invoke air_flightinfo failed */
	private boolean unConfirmedOperateInfo;
	
    /** The Cabin Class in booking of market */
    private String marketCabinClass;

    /** The sub Class in booking of market*/
    private String marketSubClass;

    /** code share flight Provider */
    private String marketCompany;
    
    /** The Cabin Class in booking of flight*/
    private String cabinClass;
    
    /** The Sub Class in booking of flight*/
    private String subClass;
    
    private FlightStatusEnum status;
    
    private String originTerminal;
    
    private String destTerminal;

	private String airCraftType;

	private String[] stops;

	private String totalDuration;

	private Integer numberOfStops;

	private Boolean isFlown;

	/** If it is a international transit */
	private Boolean transit;
	
	private String apiVersion;
    
	private List<String> usablePrimaryTavelDocs;

	private List<String> usableSecondaryTavelDocs;
	
	private SegmentDisplayDTO display;
	
	private String reminder;
	
	private boolean pcc;
	
	private boolean adtk;
	
	private boolean canCheckIn;
	
	private boolean checkedIn;
	
	private boolean openToCheckIn;
	
	private boolean withinTwentyFourHrs;
	
	private boolean withinNinetyMins;
	
	private String checkInRemainingTime;

	/** Train case for displaying pick up number or not  */
	private String trainCase;
	
	private boolean cabinClassUpgraded;

	private boolean hasCheckedBaggage;

	private RebookInfoDTO rebookInfo;

	private AdditionalOperatorInfo additionalOperatorinfo;

	private UpgradeInfo upgradeInfo;
	
	private RtfsFlightStatusInfo rtfsFlightStatusInfo;
	
	/** has available wifi on flight */
	private boolean hasAvailableWifi;
	
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
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
	public DepartureArrivalTimeDTO getDepartureTime() {
		return departureTime;
	}
	public DepartureArrivalTimeDTO findDepartureTime() {
		if(departureTime == null) {
			departureTime = new DepartureArrivalTimeDTO();
		}
		return departureTime;
	}
	public void setDepartureTime(DepartureArrivalTimeDTO departureTime) {
		this.departureTime = departureTime;
	}
	public DepartureArrivalTimeDTO findArrivalTime() {
		if(arrivalTime == null) {
			arrivalTime = new DepartureArrivalTimeDTO();
		}
		return arrivalTime;
	}
	public DepartureArrivalTimeDTO getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(DepartureArrivalTimeDTO arrivalTime) {
		this.arrivalTime = arrivalTime;
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
	public Boolean getIsFlown() {
		return isFlown;
	}
	public void setIsFlown(Boolean isFlown) {
		this.isFlown = isFlown;
	}
	public String getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}
	public Integer getNumberOfStops() {
		return numberOfStops;
	}
	public void setNumberOfStops(Integer numberOfStops) {
		this.numberOfStops = numberOfStops;
	}
	public Boolean getTransit() {
		return transit;
	}
	public void setTransit(Boolean transit) {
		this.transit = transit;
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
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public SegmentDisplayDTO getDisplay() {
		return display;
	}
	public SegmentDisplayDTO findDisplay() {
		if(display == null) {
			display = new SegmentDisplayDTO();
		}
		return display;
	}
	public void setDisplay(SegmentDisplayDTO display) {
		this.display = display;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}	
	public String getTrainCase() {
		return trainCase;
	}
	public boolean isPcc() {
		return pcc;
	}
	public void setTrainCase(String trainCase) {
		this.trainCase = trainCase;
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
	public boolean isOpenToCheckIn() {
		return openToCheckIn;
	}
	public void setOpenToCheckIn(boolean openToCheckIn) {
		this.openToCheckIn = openToCheckIn;
	}
	public boolean isWithinTwentyFourHrs() {
		return withinTwentyFourHrs;
	}
	public void setWithinTwentyFourHrs(boolean withinTwentyFourHrs) {
		this.withinTwentyFourHrs = withinTwentyFourHrs;
	}
	public boolean isWithinNinetyMins() {
		return withinNinetyMins;
	}
	public void setWithinNinetyMins(boolean withinNinetyMins) {
		this.withinNinetyMins = withinNinetyMins;
	}
	public boolean isCheckedIn() {
		return checkedIn;
	}
	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	public String getCheckInRemainingTime() {
		return checkInRemainingTime;
	}
	public void setCheckInRemainingTime(String checkInRemainingTime) {
		this.checkInRemainingTime = checkInRemainingTime;
	}
	public FlightStatusEnum getStatus() {
		return status;
	}
	public void setStatus(FlightStatusEnum status) {
		this.status = status;
	}
	public boolean isCabinClassUpgraded() {
		return cabinClassUpgraded;
	}
	public void setCabinClassUpgraded(boolean cabinClassUpgraded) {
		this.cabinClassUpgraded = cabinClassUpgraded;
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
	public RebookInfoDTO getRebookInfo() {
		return rebookInfo;
	}
	public void setRebookInfo(RebookInfoDTO rebookInfo) {
		this.rebookInfo = rebookInfo;
	}
	public AdditionalOperatorInfo getAdditionalOperatorinfo() {
		return additionalOperatorinfo;
	}
	public void setAdditionalOperatorinfo(AdditionalOperatorInfo additionalOperatorinfo) {
		this.additionalOperatorinfo = additionalOperatorinfo;
	}
	public boolean isHasCheckedBaggage() {
		return hasCheckedBaggage;
	}

	public void setHasCheckedBaggage(boolean hasCheckedBaggage) {
		this.hasCheckedBaggage = hasCheckedBaggage;
	}
	public UpgradeInfo getUpgradeInfo() {
		return upgradeInfo;
	}
	public void setUpgradeInfo(UpgradeInfo upgradeInfo) {
		this.upgradeInfo = upgradeInfo;
	}
	public RtfsFlightStatusInfo getRtfsFlightStatusInfo() {
		return rtfsFlightStatusInfo;
	}
	public void setRtfsFlightStatusInfo(RtfsFlightStatusInfo rtfsFlightStatusInfo) {
		this.rtfsFlightStatusInfo = rtfsFlightStatusInfo;
	}
	public boolean isUnConfirmedOperateInfo() {
		return unConfirmedOperateInfo;
	}
	public void setUnConfirmedOperateInfo(boolean unConfirmedOperateInfo) {
		this.unConfirmedOperateInfo = unConfirmedOperateInfo;
	}
	public boolean isHasAvailableWifi() {
		return hasAvailableWifi;
	}
	public void setHasAvailableWifi(boolean hasAvailableWifi) {
		this.hasAvailableWifi = hasAvailableWifi;
	}
	
}
