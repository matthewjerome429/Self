package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.dto.common.booking.BookingOrderDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightStatusInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.UpgradeInfo;

import io.swagger.annotations.ApiModelProperty;

public class SegmentDTOV2 extends BookingOrderDTO implements Serializable{
	
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
    private DepartureArrivalTimeDTOV2 departureTime;
    
    /** The detailed arrival time. */
    private DepartureArrivalTimeDTOV2 arrivalTime; 
    
    /** original flight Number */
    private String operateSegmentNumber;
    
    /** code share flight Number */
    private String marketSegmentNumber;
    
    /** original flight Provider */
    private String operateCompany;
    
	  /** the operateSegmentNumber and operateCompany confirmed flag, it is true if invoke air_flightinfo failed */
    @ApiModelProperty(value = "if true the phone information is from olss contact.", required = false)
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

	@ApiModelProperty(value = "the number of the flight stops.", required = false)
	private Integer numberOfStops;

	@ApiModelProperty(value = "if true the flight is flowned.", required = false)
	private Boolean isFlown;

	/** If it is a international transit */
	@ApiModelProperty(value = "if true the flight need international transit.", required = false)
	private Boolean transit;
	
	@ApiModelProperty(value = "the api of version", required = false)
	private String apiVersion;
    
	private List<String> usablePrimaryTavelDocs;

	private List<String> usableSecondaryTavelDocs;
	
	private SegmentDisplayDTOV2 display;
	
	/** Segment Check In Mandatory */
	private SegmentCheckInMandatoryDTOV2 mandatory;
	
	@ApiModelProperty(value = "the reminder message.", required = false)
	private String reminder;
	
	private boolean pcc;
	
	private boolean adtk;
	
	@ApiModelProperty(value = "if true the flight is open to checked in.", required = true)
	private boolean openToCheckIn;
	
	@ApiModelProperty(value = "if true the flight is post checkin window, this only for this flow instead of the journey.", required = true)
	private boolean postCheckIn;
	
	@ApiModelProperty(value = "if true the flight whith in 24 hours.", required = true)
	private boolean withinTwentyFourHrs;
	
	@ApiModelProperty(value = "if true the flight whith in 90 minutes.", required = true)
	private boolean withinNinetyMins;
	
	@ApiModelProperty(value = "the remaining time of the flight can check in", required = true)
	private String checkInRemainingTime;

	/** Train case for displaying pick up number or not  */
	@ApiModelProperty(value = "the train case for displaying pick up number or not", required = false)
	private String trainCase;
	
	@ApiModelProperty(value = "if true the cabin class is upgraded", required = true)
	private boolean cabinClassUpgraded;

	@ApiModelProperty(value = "if true the flight has checked baggage", required = true)
	private boolean hasCheckedBaggage;

	private RebookInfoDTOV2 rebookInfo;

	private AdditionalOperatorInfo additionalOperatorinfo;

	private UpgradeInfo upgradeInfo;
	
	@ApiModelProperty(value = "the real-time flight status from the rtfs interface", required = true)
	private RtfsFlightStatusInfo rtfsFlightStatusInfo;
	
	@ApiModelProperty(value = "summary of Real Time Flight Status, without filter out", required = true)
	private RtfsFlightSummaryDTOV2 rtfsSummary;
	
	/** has available wifi on flight */
	private boolean hasAvailableWifi;

	/** the number of gate **/
	private String gateNumber;
	
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
	public DepartureArrivalTimeDTOV2 getDepartureTime() {
		return departureTime;
	}
	public DepartureArrivalTimeDTOV2 findDepartureTime() {
		if(departureTime == null) {
			departureTime = new DepartureArrivalTimeDTOV2();
		}
		return departureTime;
	}
	public void setDepartureTime(DepartureArrivalTimeDTOV2 departureTime) {
		this.departureTime = departureTime;
	}
	public DepartureArrivalTimeDTOV2 findArrivalTime() {
		if(arrivalTime == null) {
			arrivalTime = new DepartureArrivalTimeDTOV2();
		}
		return arrivalTime;
	}
	public DepartureArrivalTimeDTOV2 getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(DepartureArrivalTimeDTOV2 arrivalTime) {
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
		return usablePrimaryTavelDocs;
	}
	public void setUsablePrimaryTavelDocs(List<String> usablePrimaryTavelDocs) {
		this.usablePrimaryTavelDocs = usablePrimaryTavelDocs;
	}
	public List<String> findUsablePrimaryTavelDocs() {
		if (this.usablePrimaryTavelDocs == null) {
			this.usablePrimaryTavelDocs = new ArrayList<>();
		}
		return usablePrimaryTavelDocs;
	}
	public List<String> getUsableSecondaryTavelDocs() {
		return usableSecondaryTavelDocs;
	}
	public List<String> findUsableSecondaryTavelDocs() {
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
	public SegmentDisplayDTOV2 getDisplay() {
		return display;
	}
	public SegmentDisplayDTOV2 findDisplay() {
		if(display == null) {
			display = new SegmentDisplayDTOV2();
		}
		return display;
	}
	public void setDisplay(SegmentDisplayDTOV2 display) {
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
	public RebookInfoDTOV2 getRebookInfo() {
		return rebookInfo;
	}
	public void setRebookInfo(RebookInfoDTOV2 rebookInfo) {
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
	public RtfsFlightSummaryDTOV2 getRtfsSummary() {
		return rtfsSummary;
	}
	public void setRtfsSummary(RtfsFlightSummaryDTOV2 rtfsSummary) {
		this.rtfsSummary = rtfsSummary;
	}
	public boolean isUnConfirmedOperateInfo() {
		return unConfirmedOperateInfo;
	}
	public void setUnConfirmedOperateInfo(boolean unConfirmedOperateInfo) {
		this.unConfirmedOperateInfo = unConfirmedOperateInfo;
	}
	public boolean isPostCheckIn() {
		return postCheckIn;
	}
	public void setPostCheckIn(boolean postCheckIn) {
		this.postCheckIn = postCheckIn;
	}
	public SegmentCheckInMandatoryDTOV2 getMandatory() {
		return mandatory;
	}
	public void setMandatory(SegmentCheckInMandatoryDTOV2 mandatory) {
		this.mandatory = mandatory;
	}
	public SegmentCheckInMandatoryDTOV2 findMandatory() {
		if (mandatory == null) {
			mandatory = new SegmentCheckInMandatoryDTOV2();
		}
		return mandatory;
	}
	public boolean isHasAvailableWifi() {
		return hasAvailableWifi;
	}
	public void setHasAvailableWifi(boolean hasAvailableWifi) {
		this.hasAvailableWifi = hasAvailableWifi;
	}

	public String getGateNumber() {
		return gateNumber;
	}

	public void setGateNumber(String gateNumber) {
		this.gateNumber = gateNumber;
	}
}
