package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FlightBookingDTO implements Serializable {

	private static final long serialVersionUID = -8630803675361326192L;
	
	private String spnr;
	
	private String oneARloc;
	private String gdsRloc;
	private String rloc;
	private String encryptedRloc;

	private Boolean canCheckIn;
	
	private Boolean flightOnly;
	
	private Boolean ibeBooking = false;
	private Boolean trpBooking = false;
	private Boolean appBooking = false;
	private Boolean gdsBooking = false;
	private Boolean gccBooking = false;
	private Boolean gdsGroupBooking = false;
	private Boolean redBooking = false;
	private Boolean staffBooking = false;
	private Boolean idBooking = false;

	private boolean hasInsurance = false;
	private boolean hasHotel = false;
	private boolean isRedUpgrade = false;

	/**for PNR  of item in FE element**/
	private List<String> bookingDwCodeList;
	private boolean canIssueTicketChecking;
	private boolean bookingOnhold;
	private boolean tktl;
	private String pos;
	private boolean tkxl;
	private boolean adtk;
	private boolean pcc;
	private boolean issueTicketsExisting;
	private boolean memberLogin;
	private boolean mandatoryContactInfo;
	private boolean companionBooking; //member logged in is not traveling
	private boolean profileCompanionBooking; //companion specified in member profile
	private String officeId;
	private String createDate;
	private String rpCityCode; // city code in RP office id
	private PassengerDTO passenger;
	private TicketIssueInfo ticketIssueInfo;
	private List<PassengerDTO> passengers;
	private List<SegmentDTO> segments;
	private List<PassengerSegmentDTO> passengerSegments;
	/** isExistBcode of current booking **/
	private Boolean isExistBcode;
	
	private Boolean packageBooking;
	
	private List<TicketPriceInfoDTO> ticketPriceInfo;
	
	private List<RebookMappingDTO> rebookMapping;

	private List<String> bookingWaiveReminders;
	
	private boolean issueSpecialServices;
	/**
	 * Linked booking
	 */
	private boolean tempLinkedBooking;
	
	@JsonIgnore
	public PassengerDTO getPrimaryPassenger() {
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
		
	public Boolean getCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public List<PassengerDTO> getPassengers() {
		if(passengers == null) {
			passengers = new ArrayList<>();
		}
		return passengers;
	}

	public void setPassengers(List<PassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public List<SegmentDTO> getSegments() {
		if(segments == null) {
			segments = new ArrayList<>();
		}
		return segments;
	}

	public void setSegments(List<SegmentDTO> segments) {
		this.segments = segments;
	}

	public List<PassengerSegmentDTO> getPassengerSegments() {
		if(passengerSegments == null) {
			passengerSegments = new ArrayList<>();
		}
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
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

	public boolean isMandatoryContactInfo() {
		return mandatoryContactInfo;
	}

	public void setMandatoryContactInfo(boolean mandatoryContactInfo) {
		this.mandatoryContactInfo = mandatoryContactInfo;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public PassengerDTO getPassenger() {
		return passenger;
	}

	public void setPassenger(PassengerDTO passenger) {
		this.passenger = passenger;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public boolean isProfileCompanionBooking() {
		return profileCompanionBooking;
	}
	public void setProfileCompanionBooking(boolean profileCompanionBooking) {
		this.profileCompanionBooking = profileCompanionBooking;
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

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
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

	public List<TicketPriceInfoDTO> getTicketPriceInfo() {
		return ticketPriceInfo;
	}

	public void setTicketPriceInfo(List<TicketPriceInfoDTO> ticketPriceInfo) {
		this.ticketPriceInfo = ticketPriceInfo;
	}

	public List<RebookMappingDTO> getRebookMapping() {
		return rebookMapping;
	}

	public void setRebookMapping(List<RebookMappingDTO> rebookMapping) {
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
}
