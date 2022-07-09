package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrOnHoldInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicketPriceInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;


public class Booking{

	/** The flag of receive pnr success*/
	private boolean gotPnr = false;
	
	/** The flag of receive cpr success*/
	private boolean gotCpr = false;
	
	private RetrievePnrBookingCerateInfo bookingCreateInfo;//create date
	private List<RetrievePnrTicketPriceInfo> ticketPriceInfo;
	
	private String oneARloc;//1A rloc
	
	private String gdsRloc;//GDS rloc

	private String loginRloc;//the login rloc of this seesion, only used for non member login, e.g. rloc/ticket loign
	
	private String spnr;//spnr,used for package booking.
	
	private String officeTimezone;//office city timezone
	private String rpCityCode; // city code in RP office id
	private String encryptedRloc; // The rloc encrypted by the provided key file
	private Boolean canCheckIn;
	private String pos;
	private String posForAep;
	private boolean redemptionBooking;
	private boolean canIssueTicket;
	
	private boolean bookingOnhold;
	private boolean tktl;
	private boolean tkxl;
	private boolean adtk;
	private boolean pcc;
	private boolean hasIssuedTicket;
	private boolean hasIssuedAllTickets;

	private boolean nonMiceGroupBooking;
	
	private boolean ibeBooking;
	private boolean trpBooking;
	private boolean appBooking;
	private boolean gdsBooking;
	private boolean gccBooking;
	private boolean miceBooking;
	private boolean ndcBooking;

	private boolean hasFqtu;
	private boolean hasBkug;

	private List<ErrorInfo> cprErrors;

	/**for PNR  of item in FE element**/
	private List<String> bookingDwCode;
	
	private List<String> bookingWaiveReminders;

	private TicketIssueInfo ticketIssueInfo;
	private List<Passenger> passengers;
	private List<Segment> segments;
	private List<PassengerSegment> passengerSegments;	
    /** ssr list */
    private List<RetrievePnrDataElements> ssrList;
    /** sk list */
    private List<RetrievePnrDataElements> skList;
    /** tk list */
    private List<RetrievePnrTicket> ticketList;
    /** Invalid OTs */
    private List<BigInteger> invalidOts;
    
    private List<String> tposList;
    
    private BookingPackageInfo bookingPackageInfo;
    /** Is corporate booking */
    private Boolean corporateBooking;
    
    private Boolean mandatoryContactInfo;
    
    private boolean hasInsurance;
    
    private boolean hasHotel;

    private boolean hasEvent;

    private boolean issueSpecialServices;

    /** OSI booking site info from OSI freeText, e.g."BOOKING SITE US-CX" */
    private OSIBookingSite osiBookingSite;
    
    /**the on Hold remark, please note the booking may be not on hold even has this filed*/
    private RetrievePnrOnHoldInfo onHoldRemarkInfo; 
    
    /** the relationship between cancelled segmentIds and accept segmentIds */
    private List<RebookMapping> rebookMapping;
    
	/**
	 * Linked booking
	 */
	private boolean tempLinkedBooking;
	
	/** booking base information is built from CPR */
	private boolean basedOnCPR;
	
	/** journeys from CPR */
	private List<Journey> cprJourneys;
	
	/** CPR staff booking */
	private boolean cprStaffBooking;
	
	/** merged booking gotten from OLCI*/
	private List<String> mergedRlocs;
	
	private boolean frbkSK;
	
	/** removed segment while rebook, build by RM */
	private List<AtciCancelledSegment> atciCancelledSegments;
	
	/** for GDS booking, the cancelled segment is removed, use the mocked cancelled segment id for mapping */
	private List<RebookMapping> atciRebookMapping;
	
	public String getEncryptedRloc() {
		return encryptedRloc;
	}

	public void setEncryptedRloc(String encryptedRloc) {
		this.encryptedRloc = encryptedRloc;
	}
	
	public List<RetrievePnrDataElements> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<RetrievePnrDataElements> ssrList) {
		this.ssrList = ssrList;
	}

	public List<RetrievePnrDataElements> getSkList() {
		return skList;
	}

	public void setSkList(List<RetrievePnrDataElements> skList) {
		this.skList = skList;
	}

	public List<RetrievePnrTicket> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<RetrievePnrTicket> ticketList) {
		this.ticketList = ticketList;
	}


	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}
	@JsonIgnore
	public String getOfficeId() {
		return bookingCreateInfo == null ? null : bookingCreateInfo.getRpOfficeId();
	}

	public String getOfficeTimezone() {
		return officeTimezone;
	}

	public void setOfficeTimezone(String officeTimezone) {
		this.officeTimezone = officeTimezone;
	}

	public String getRpCityCode() {
		return rpCityCode;
	}

	public void setRpCityCode(String rpCityCode) {
		this.rpCityCode = rpCityCode;
	}

	public Boolean getCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public List<Passenger> getPassengers() {
		if(passengers == null) {
			passengers = new ArrayList<>();
		}
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public List<Segment> getSegments() {
		if(segments == null) {
			segments = new ArrayList<>();
		}
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public List<PassengerSegment> getPassengerSegments() {
		if(passengerSegments == null) {
			passengerSegments = new ArrayList<>();
		}
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegment> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}
	public String getGdsRloc() {
		return gdsRloc;
	}

	public void setGdsRloc(String midRloc) {
		this.gdsRloc = midRloc;
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
	public boolean isRedemptionBooking() {
		return redemptionBooking;
	}

	public void setRedemptionBooking(boolean redemptionBooking) {
		this.redemptionBooking = redemptionBooking;
	}

	public boolean isCanIssueTicket() {
		return canIssueTicket;
	}

	public void setCanIssueTicket(boolean canIssueTicket) {
		this.canIssueTicket = canIssueTicket;
	}

	public boolean isHasIssuedTicket() {
		return hasIssuedTicket;
	}

	public void setHasIssuedTicket(boolean hasIssuedTicket) {
		this.hasIssuedTicket = hasIssuedTicket;
	}

	public boolean isBookingOnhold() {
		return bookingOnhold;
	}

	public void setBookingOnhold(boolean bookingOnhold) {
		this.bookingOnhold = bookingOnhold;
	}

	public TicketIssueInfo getTicketIssueInfo() {
		return ticketIssueInfo;
	}

	public void setTicketIssueInfo(TicketIssueInfo ticketIssueInfo) {
		this.ticketIssueInfo = ticketIssueInfo;
	}

	public String getSpnr() {
		return spnr;
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}
	@JsonIgnore
	public boolean isGroupBooking() {
		return nonMiceGroupBooking;
	}

	public void setNonMiceGroupBooking(boolean nonMiceGroupBooking) {
		this.nonMiceGroupBooking = nonMiceGroupBooking;
	}
	@JsonIgnore
	public String getDisplayRloc() {
		if(bookingPackageInfo!=null && bookingPackageInfo.isPackageBooking()){
			return this.spnr;
		}else if (StringUtils.isNotEmpty(loginRloc)){
			return loginRloc;
		}else{
			return oneARloc;
		}
		
	}

	public String getLoginRloc() {
		return loginRloc;
	}

	public void setLoginRloc(String loginRloc) {
		this.loginRloc = loginRloc;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPosForAep() {
		return posForAep;
	}

	public void setPosForAep(String posForAep) {
		this.posForAep = posForAep;
	}

	public List<BigInteger> getInvalidOts() {
		return invalidOts;
	}

	public void setInvalidOts(List<BigInteger> invalidOts) {
		this.invalidOts = invalidOts;
	}

	public BookingPackageInfo getBookingPackageInfo() {
		return bookingPackageInfo;
	}

	public void setBookingPackageInfo(BookingPackageInfo bookingPackageInfo) {
		this.bookingPackageInfo = bookingPackageInfo;
	}

	public boolean isCorporateBooking() {
		return BooleanUtils.isTrue(corporateBooking);
	}

	public void setCorporateBooking(Boolean corporateBooking) {
		this.corporateBooking = corporateBooking;
	}

	public Boolean getMandatoryContactInfo() {
		return mandatoryContactInfo;
	}

	public void setMandatoryContactInfo(Boolean mandatoryContactInfo) {
		this.mandatoryContactInfo = mandatoryContactInfo;
	}
	@JsonIgnore
	public String getCreateDate() {
		return this.bookingCreateInfo == null ? null : bookingCreateInfo.getCreateDate();
	}

	public boolean isIbeBooking() {
		return ibeBooking;
	}

	public void setIbeBooking(boolean ibeBooking) {
		this.ibeBooking = ibeBooking;
	}


	public boolean isTrpBooking() {
		return trpBooking;
	}

	public void setTrpBooking(boolean trpBooking) {
		this.trpBooking = trpBooking;
	}

	public boolean isAppBooking() {
		return appBooking;
	}

	public void setAppBooking(boolean appBooking) {
		this.appBooking = appBooking;
	}

	public boolean isGdsBooking() {
		return gdsBooking;
	}

	public void setGdsBooking(boolean gdsBooking) {
		this.gdsBooking = gdsBooking;
	}

	public boolean isGccBooking() {
		return gccBooking;
	}

	public void setGccBooking(boolean gccBooking) {
		this.gccBooking = gccBooking;
	}

	public boolean isMiceBooking() {
		return miceBooking;
	}

	public void setMiceBooking(boolean miceBooking) {
		this.miceBooking = miceBooking;
	}

	public boolean isHasFqtu() {
		return hasFqtu;
	}

	public void setHasFqtu(boolean hasFqtu) {
		this.hasFqtu = hasFqtu;
	}

	public RetrievePnrBookingCerateInfo getBookingCreateInfo() {
		return bookingCreateInfo;
	}

	public void setBookingCreateInfo(RetrievePnrBookingCerateInfo bookingCreateInfo) {
		this.bookingCreateInfo = bookingCreateInfo;
	}

	public boolean isNdcBooking() {
		return ndcBooking;
	}

	public void setNdcBooking(boolean ndcBooking) {
		this.ndcBooking = ndcBooking;
	}

	public boolean isHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(boolean hasInsurance) {
		this.hasInsurance = hasInsurance;
	}
	
	public List<RetrievePnrTicketPriceInfo> getTicketPriceInfo() {
		return ticketPriceInfo;
	}

	public void setTicketPriceInfo(List<RetrievePnrTicketPriceInfo> ticketPriceInfo) {
		this.ticketPriceInfo = ticketPriceInfo;
	}

	public List<String> getBookingDwCode() {
		return bookingDwCode;
	}

	public void setBookingDwCode(List<String> bookingDwCode) {
		this.bookingDwCode = bookingDwCode;
	}

	public List<String> getBookingWaiveReminders() {
		return bookingWaiveReminders;
	}

	public void setBookingWaiveReminders(List<String> bookingWaiveReminders) {
		this.bookingWaiveReminders = bookingWaiveReminders;
	}

	public Boolean getCorporateBooking() {
		return corporateBooking;
	}

	public List<RebookMapping> getRebookMapping() {
		return rebookMapping;
	}
	
	public List<RebookMapping> findRebookMapping() {
		if (rebookMapping == null) {
			rebookMapping = new ArrayList<>();
		}
		return rebookMapping;
	}

	public void setRebookMapping(List<RebookMapping> rebookMapping) {
		this.rebookMapping = rebookMapping;
	}

	public boolean isHasHotel() {
		return hasHotel;
	}

	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}

	public boolean isHasEvent() {
		return hasEvent;
	}

	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}
 
	public boolean isIssueSpecialServices() {
		return issueSpecialServices;
	}

	public void setIssueSpecialServices(boolean issueSpecialServices) {
		this.issueSpecialServices = issueSpecialServices;
	}

	public boolean getHasBkug() {
		return hasBkug;
	}

	public void setHasBkug(boolean hasBkug) {
		this.hasBkug = hasBkug;
	}
	
	public OSIBookingSite getOsiBookingSite() {
		return osiBookingSite;
	}

	public void setOsiBookingSite(OSIBookingSite osiBookingSite) {
		this.osiBookingSite = osiBookingSite;
	}

	public RetrievePnrOnHoldInfo getOnHoldRemarkInfo() {
		return onHoldRemarkInfo;
	}

	public void setOnHoldRemarkInfo(RetrievePnrOnHoldInfo onHoldRemarkInfo) {
		this.onHoldRemarkInfo = onHoldRemarkInfo;
	}
	@JsonIgnore
	public boolean isIDBooking() {
		return Optional.ofNullable(this.passengers).orElseGet(Collections::emptyList).stream()
		.anyMatch(pax -> pax.getStaffDetail() != null && StaffBookingType.INDUSTRY_DISCOUNT.equals(pax.getStaffDetail().getType()));
	}
	@JsonIgnore
	public boolean isADBooking() {
		return Optional.ofNullable(this.passengers).orElseGet(Collections::emptyList).stream()
		.anyMatch(pax -> pax.getStaffDetail() != null && StaffBookingType.AGENCY_DISCOUNT.equals(pax.getStaffDetail().getType()));
	}
	@JsonIgnore
	public boolean isStaffBooking() {
		return (CollectionUtils.isNotEmpty(this.passengers)&&passengers.stream().anyMatch(pax->Objects.nonNull(pax.getStaffDetail())))
				|| isCprStaffBooking();
	}

	public boolean isTempLinkedBooking() {
		return tempLinkedBooking;
	}

	public void setTempLinkedBooking(boolean tempLinkedBooking) {
		this.tempLinkedBooking = tempLinkedBooking;
	}
	
	@JsonIgnore
	public boolean isHasLoginMember() {
		return Optional.ofNullable(this.passengers).orElseGet(Collections::emptyList).stream()
				.anyMatch(pax-> BooleanUtils.isTrue(pax.getLoginMember()));
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

	public boolean isBasedOnCPR() {
		return basedOnCPR;
	}

	public void setBasedOnCPR(boolean basedOnCPR) {
		this.basedOnCPR = basedOnCPR;
	}

	public List<Journey> getCprJourneys() {
		return cprJourneys;
	}

	public void setCprJourneys(List<Journey> cprJourneys) {
		this.cprJourneys = cprJourneys;
	}

	public boolean isCprStaffBooking() {
		return cprStaffBooking;
	}

	public void setCprStaffBooking(boolean cprStaffBooking) {
		this.cprStaffBooking = cprStaffBooking;
	}

	public boolean isHasIssuedAllTickets() {
		return hasIssuedAllTickets;
	}

	public void setHasIssuedAllTickets(boolean hasIssuedAllTickets) {
		this.hasIssuedAllTickets = hasIssuedAllTickets;
	}

	public List<String> getMergedRlocs() {
		return mergedRlocs;
	}

	public void setMergedRlocs(List<String> mergedRlocs) {
		this.mergedRlocs = mergedRlocs;
	}

	public List<String> getTposList() {
		return tposList;
	}

	public void setTposList(List<String> tposList) {
		this.tposList = tposList;
	}

	public List<ErrorInfo> getCprErrors() {
		return cprErrors;
	}

	public void setCprErrors(List<ErrorInfo> cprErrors) {
		this.cprErrors = cprErrors;
	}

	public boolean isFrbkSK() {
		return frbkSK;
	}

	public void setFrbkSK(boolean frbkSK) {
		this.frbkSK = frbkSK;
	}

	public List<AtciCancelledSegment> getAtciCancelledSegments() {
		return atciCancelledSegments;
	}

	public void setAtciCancelledSegments(List<AtciCancelledSegment> atciCancelledSegments) {
		this.atciCancelledSegments = atciCancelledSegments;
	}

	public List<RebookMapping> getAtciRebookMapping() {
		return atciRebookMapping;
	}
	
	public List<RebookMapping> findAtciRebookMapping() {
		if (atciRebookMapping == null) {
			atciRebookMapping = new ArrayList<>();
		}
		return atciRebookMapping;
	}

	public void setAtciRebookMapping(List<RebookMapping> atciRebookMapping) {
		this.atciRebookMapping = atciRebookMapping;
	}

}