package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FlightBookingDTOV2 implements Serializable {

	private static final long serialVersionUID = -8630803675361326192L;
	
	@ApiModelProperty(value = "spnr of the booking reference, it is empty if the booking not package booking, the length usually is 7.", required = false, example = "AOX2QZ4")
	private String spnr;
	@ApiModelProperty(value = "1A booking reference of the booking", required = true, example = "MX4768")
	private String oneARloc;
	@ApiModelProperty(value = "GDS(non 1A) booking reference of the booking", required = true, example = "MX4768")
	private String gdsRloc;
	
	@ApiModelProperty(value = "The display rloc for frontend, ", required = true, example = "MX4768")
	private String displayRloc;

	@ApiModelProperty(value = "Same as displayRloc, please don't use this field anymore, will remove it this version later", required = true, example = "MX4768")
	@Deprecated
	private String rloc;
	
	@ApiModelProperty(value = "The encrypted 1A rloc.", required = true)
	private String encryptedRloc;
	
	@ApiModelProperty(value = "booking can checkIn or not")
	private Boolean canCheckIn = false;
	
	@ApiModelProperty(value = "It is true if this is flight only booking.", required = false)
	private Boolean flightOnly;
	
	@ApiModelProperty(value = "IBE booking flag.", required = true)
	private Boolean ibeBooking = false;
	
	@ApiModelProperty(value = "trp booking flag.", required = true)
	private Boolean trpBooking = false;
	@ApiModelProperty(value = "app booking flag.", required = true)
	private Boolean appBooking = false;
	@ApiModelProperty(value = "gds booking flag.", required = true)
	private Boolean gdsBooking = false;
	@ApiModelProperty(value = "gcc booking flag.", required = true)
	private Boolean gccBooking = false;
	@ApiModelProperty(value = "gds Group booking flag.", required = true)
	private Boolean gdsGroupBooking = false;
	
	@ApiModelProperty(value = "redemption Group booking flag.", required = true)
	private Boolean redBooking = false;
	
	@ApiModelProperty(value = "staffBooking booking flag.", required = true)
	private Boolean staffBooking = false;
	
	@ApiModelProperty(value = "ID(one kind of staff Booking) idBooking booking flag.", required = true)
	private Boolean idBooking = false;
	
	@ApiModelProperty(value = "has insurance booking flag.", required = true)
	private boolean hasInsurance = false;
	
	@ApiModelProperty(value = "has hotel booking flag.", required = true)
	private boolean hasHotel = false;
	
	@ApiModelProperty(value = "It is true if the booking is redemption upgrade booking.", required = true)
	private boolean isRedUpgrade = false;

	/**for PNR  of item in FE element**/
	private List<String> bookingDwCodeList;
	@ApiModelProperty(value = "onhold booking flag.", required = true)
	private boolean canIssueTicketChecking;
	@ApiModelProperty(value = "onhold booking flag.", required = true)
	private boolean bookingOnhold;
	private boolean tktl;
	private String pos;
	private boolean tkxl;
	private boolean adtk;
	private boolean pcc;
	@ApiModelProperty(value = "It is true the booking is exist issue tickets.", required = false)
	private boolean issueTicketsExisting;

	@ApiModelProperty(value = "It is true the booking is member login.", required = true)
	private boolean memberLogin;
	@ApiModelProperty(value = "has mandatoryContactInfo flag.", required = true)
	private Boolean mandatoryContactInfo;
	@ApiModelProperty(value = "companion booking flag.", required = true)
	private boolean companionBooking;
	@ApiModelProperty(value = "companion specified in member profile flag.", required = true)
	private boolean profileCompanionBooking; //companion specified in member profile
	private String officeId;
	private String createDate;
	@ApiModelProperty(value = "the city code in RP office id.", required = false)
	private String rpCityCode; // city code in RP office id
	@ApiModelProperty(value = "ticketIssueInfo of the booking", required = false)
	private TicketIssueInfo ticketIssueInfo;
	private List<PassengerDTOV2> passengers;
	private List<SegmentDTOV2> segments;
	private List<PassengerSegmentDTOV2> passengerSegments;
	/** isExistBcode of current booking **/
	@ApiModelProperty(value = "It is true the booking is exist Bcode.", required = false)
	private Boolean isExistBcode;

	@ApiModelProperty(value = "package booking flag.", required = false)
	private Boolean packageBooking;
	@ApiModelProperty(value = "ticketPriceInfo of the booking", required = false)
	private List<TicketPriceInfoDTOV2> ticketPriceInfo;

	@ApiModelProperty(value = "rebookMapping of the booking", required = false)
	private List<RebookMappingDTOV2> rebookMapping;

	@ApiModelProperty(value = "waive reminders of the booking.", required = false)
	private List<String> bookingWaiveReminders;

	@ApiModelProperty(value = "has issue specialServices flag.", required = true)
	private boolean issueSpecialServices;
	/**
	 * Linked booking
	 */
	@ApiModelProperty(value = "has tempLinkedBooking flag.", required = true)
	private boolean tempLinkedBooking;

	/** journeys from CPR */
	@ApiModelProperty(value = "cprJourneys of booking.", required = false)
	private List<JourneyDTOV2> cprJourneys;

	/** The flag of receive pnr success*/
	@ApiModelProperty(value = "receive pnr success flag.", required = true)
	private boolean gotPnr = false;

	/** The flag of receive cpr success*/
	@ApiModelProperty(value = "receive cpr success flag.", required = true)
	private boolean gotCpr = false;

	@ApiModelProperty(value = "is mice booking", required = true)
	private boolean miceBooking;
	
	@ApiModelProperty(value = "merged booking rlocs from OLCI")
	private List<String> mergedRlocs;

	private List<ErrorInfo> cprErrors;

	/** The flag to indicate whether have FRBK SK*/
	@ApiModelProperty(value = "is special(FRBK) fare rule booking.", required = false)
	private Boolean freeRebooking;
	
	private List<AtciCancelledSegmentDTOV2> atciCancelledSegments;
	
	@JsonIgnore
	public PassengerDTOV2 getPrimaryPassenger() {
		if(CollectionUtils.isEmpty(passengers)) {
			return null;
		}
		return passengers.stream().filter(pax -> BooleanUtils.isTrue(pax.getPrimaryPassenger())).findFirst().orElse(null);
	}
	public boolean isCanIssueTicketChecking() {
		return canIssueTicketChecking;
	}

	public void setCanIssueTicketChecking(boolean canIssueTicketChecking) {
		this.canIssueTicketChecking = canIssueTicketChecking;
	}

	public List<PassengerDTOV2> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengerDTOV2> passengers) {
		this.passengers = passengers;
	}

	public List<PassengerDTOV2> findPassengers() {
		if(passengers == null) {
			passengers = new ArrayList<>();
		}
		return passengers;
	}

	public List<SegmentDTOV2> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentDTOV2> segments) {
		this.segments = segments;
	}

	public List<SegmentDTOV2> findSegments() {
		if(segments == null) {
			segments = new ArrayList<>();
		}
		return segments;
	}

	public List<PassengerSegmentDTOV2> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentDTOV2> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public List<PassengerSegmentDTOV2> findPassengerSegments() {
		if(passengerSegments == null) {
			passengerSegments = new ArrayList<>();
		}
		return passengerSegments;
	}

	public boolean isBookingOnhold() {
		return bookingOnhold;
	}

	public void setBookingOnhold(boolean bookingOnhold) {
		this.bookingOnhold = bookingOnhold;
	}

	public boolean isTktl() {
		return tktl;
	}

	public void setTktl(boolean tktl) {
		this.tktl = tktl;
	}

	public boolean isTkxl() {
		return tkxl;
	}

	public void setTkxl(boolean tkxl) {
		this.tkxl = tkxl;
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

	public boolean isIssueTicketsExisting() {
		return issueTicketsExisting;
	}

	public void setIssueTicketsExisting(boolean issueTicketsExisting) {
		this.issueTicketsExisting = issueTicketsExisting;
	}

	public TicketIssueInfo getTicketIssueInfo() {
		return ticketIssueInfo;
	}

	public void setTicketIssueInfo(TicketIssueInfo ticketIssueInfo) {
		this.ticketIssueInfo = ticketIssueInfo;
	}

	public boolean isMemberLogin() {
		return memberLogin;
	}

	public void setMemberLogin(boolean memberLogin) {
		this.memberLogin = memberLogin;
	}

	public Boolean getMandatoryContactInfo() {
		return mandatoryContactInfo;
	}
	public void setMandatoryContactInfo(Boolean mandatoryContactInfo) {
		this.mandatoryContactInfo = mandatoryContactInfo;
	}
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public boolean isCompanionBooking() {
		return companionBooking;
	}

	public void setCompanionBooking(boolean companionBooking) {
		this.companionBooking = companionBooking;
	}

	public Boolean isFlightOnly() {
		return flightOnly;
	}

	public void setFlightOnly(Boolean flightOnly) {
		this.flightOnly = flightOnly;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRpCityCode() {
		return rpCityCode;
	}
	public void setRpCityCode(String rpCityCode) {
		this.rpCityCode = rpCityCode;
	}
	public Boolean getIsExistBcode() {
		return isExistBcode;
	}

	public void setIsExistBcode(Boolean isExistBcode) {
		this.isExistBcode = isExistBcode;
	}

	public String getSpnr() {
		return spnr;
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public String getGdsRloc() {
		return gdsRloc;
	}

	public void setGdsRloc(String gdsRloc) {
		this.gdsRloc = gdsRloc;
	}


	public String getEncryptedRloc() {
		return encryptedRloc;
	}

	public void setEncryptedRloc(String encryptedRloc) {
		this.encryptedRloc = encryptedRloc;
	}

	public Boolean isIbeBooking() {
		return ibeBooking;
	}

	public void setIbeBooking(Boolean ibeBooking) {
		this.ibeBooking = ibeBooking;
	}

	public Boolean isTrpBooking() {
		return trpBooking;
	}

	public void setTrpBooking(Boolean trpBooking) {
		this.trpBooking = trpBooking;
	}

	public Boolean isAppBooking() {
		return appBooking;
	}

	public void setAppBooking(Boolean appBooking) {
		this.appBooking = appBooking;
	}

	public Boolean isGdsBooking() {
		return gdsBooking;
	}

	public void setGdsBooking(Boolean gdsBooking) {
		this.gdsBooking = gdsBooking;
	}

	public Boolean isGccBooking() {
		return gccBooking;
	}

	public void setGccBooking(Boolean gccBooking) {
		this.gccBooking = gccBooking;
	}

	public Boolean isGdsGroupBooking() {
		return gdsGroupBooking;
	}

	public void setGdsGroupBooking(Boolean gdsGroupBooking) {
		this.gdsGroupBooking = gdsGroupBooking;
	}

	public Boolean isRedBooking() {
		return redBooking;
	}

	public void setRedBooking(Boolean redBooking) {
		this.redBooking = redBooking;
	}

	public Boolean isPackageBooking() {
		return packageBooking;
	}

	public void setPackageBooking(Boolean packageBooking) {
		this.packageBooking = packageBooking;
	}

	public Boolean getStaffBooking() {
		return staffBooking;
	}

	public void setStaffBooking(Boolean staffBooking) {
		this.staffBooking = staffBooking;
	}

	public Boolean isIdBooking() {
		return idBooking;
	}
	public void setIdBooking(Boolean idBooking) {
		this.idBooking = idBooking;
	}
	public boolean isHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(boolean hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public List<String> getBookingDwCodeList() {
		return bookingDwCodeList;
	}

	public void setBookingDwCodeList(List<String> bookingDwCodeList) {
		this.bookingDwCodeList = bookingDwCodeList;
	}

	public List<TicketPriceInfoDTOV2> getTicketPriceInfo() {
		return ticketPriceInfo;
	}

	public void setTicketPriceInfo(List<TicketPriceInfoDTOV2> ticketPriceInfo) {
		this.ticketPriceInfo = ticketPriceInfo;
	}

	public List<RebookMappingDTOV2> getRebookMapping() {
		return rebookMapping;
	}

	public void setRebookMapping(List<RebookMappingDTOV2> rebookMapping) {
		this.rebookMapping = rebookMapping;
	}

	public boolean isHasHotel() {
		return hasHotel;
	}

	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}

	public List<String> getBookingWaiveReminders() {
		return bookingWaiveReminders;
	}

	public void setBookingWaiveReminders(List<String> bookingWaiveReminders) {
		this.bookingWaiveReminders = bookingWaiveReminders;
	}
	public boolean isRedUpgrade() {
		return isRedUpgrade;
	}
	public void setRedUpgrade(boolean isRedUpgrade) {
		this.isRedUpgrade = isRedUpgrade;
	}
	public boolean isIssueSpecialServices() {
		return issueSpecialServices;
	}

	public void setIssueSpecialServices(boolean issueSpecialServices) {
		this.issueSpecialServices = issueSpecialServices;
	}
	public boolean isTempLinkedBooking() {
		return tempLinkedBooking;
	}
	public void setTempLinkedBooking(boolean tempLinkedBooking) {
		this.tempLinkedBooking = tempLinkedBooking;
	}
	public List<JourneyDTOV2> getCprJourneys() {
		return cprJourneys;
	}
	public void setCprJourneys(List<JourneyDTOV2> cprJourneys) {
		this.cprJourneys = cprJourneys;
	}
	public List<JourneyDTOV2> findCprJourneys() {
		if(cprJourneys == null) {
			cprJourneys = new ArrayList<>();
		}
		return cprJourneys;
	}
	public boolean isGotPnr() {
		return gotPnr;
	}
	public void setGotPnr(boolean gotPnr) {
		this.gotPnr = gotPnr;
	}
	public boolean isGotCpr() {
		return gotCpr;
	}
	public void setGotCpr(boolean gotCpr) {
		this.gotCpr = gotCpr;
	}
	public Boolean getCanCheckIn() {
		return canCheckIn;
	}
	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public String getDisplayRloc() {
		return displayRloc;
	}
	public void setDisplayRloc(String displayRloc) {
		this.displayRloc = displayRloc;
	}

	public String getRloc(){
		return displayRloc;
	}
	public boolean isProfileCompanionBooking() {
		return profileCompanionBooking;
	}
	public void setProfileCompanionBooking(boolean profileCompanionBooking) {
		this.profileCompanionBooking = profileCompanionBooking;
	}

	public boolean getMiceBooking() {
		return miceBooking;
	}

	public void setMiceBooking(boolean miceBooking) {
		this.miceBooking = miceBooking;
	}
	public List<String> getMergedRlocs() {
		return mergedRlocs;
	}
	public void setMergedRlocs(List<String> mergedRlocs) {
		this.mergedRlocs = mergedRlocs;
	}

	public List<ErrorInfo> getCprErrors() {
		return cprErrors;
	}

	public void setCprErrors(List<ErrorInfo> cprErrors) {
		this.cprErrors = cprErrors;
	}
	public Boolean getFreeRebooking() {
		return freeRebooking;
	}
	public void setFreeRebooking(Boolean freeRebooking) {
		this.freeRebooking = freeRebooking;
	}
	public List<AtciCancelledSegmentDTOV2> getAtciCancelledSegments() {
		return atciCancelledSegments;
	}
	public void setAtciCancelledSegments(List<AtciCancelledSegmentDTOV2> atciCancelledSegments) {
		this.atciCancelledSegments = atciCancelledSegments;
	}
	
}
