package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mmbbizrule.oneaservice.SessionEntity;
import com.google.common.collect.Lists;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

public class RetrievePnrBooking extends SessionEntity{


	private String oneARloc;//1A rloc
	private String gdsRloc;//GDS rloc
 
	/** Country code of Point Of Sale */
	private String pos;
	private String creationPos;
	private RetrievePnrBookingCerateInfo bookingCreateInfo;
	private List<RetrievePnrTicketPriceInfo> ticketPriceInfo;
	private List<RetrievePnrPassenger> passengers;
	private List<RetrievePnrSegment> segments;
	private List<RetrievePnrPassengerSegment> passengerSegments;
	
    /** ssr list */
    private List<RetrievePnrDataElements> ssrList;
    /** sk list */
    private List<RetrievePnrDataElements> skList;
    /** tk list */
    private List<RetrievePnrTicket> ticketList;
    /** rm list */
    private List<RetrievePnrRemark> remarkList;
    /** fa list */
    private List<RetrievePnrFa> faList;

    private List<RetrievePnrFe> feList;
    
    private List<RetrievePnrDataElements> fhdList;
    /** osi list */
    private List<RetrievePnrDataElements> osiList;
    
    /** APM/APB/APH list */
    private List<RetrievePnrApContactPhone> apContactPhones;
    /** APE list*/
    private List<RetrievePnrApEmail> apEmails;
    /** Invalid OTs */
    private List<BigInteger> invalidOts;
    /** Is corporate booking */
    private Boolean corporateBooking;
    /** the relationship between cancelled segmentIds and accept segmentIds */
    private List<RetrievePnrRebookMapping> rebookMapping;

    private List<String> bookingDwCode;

    private RetrievePnrOnHoldInfo onHoldRemarkInfo;
	
	private List<String> tposList;
	
	/** package related information */
	private RetrievePnrBookingPackageInfo bookingPackageInfo;
	
	/** Any ticket record exist in the pnr, this flag only check ticket record and ignore other rules,
	 *  e.g. if the eticket hasn't related to any segment or passenger, this flag is true still */
	private boolean ticketRecordExist;
	
	/** check if there is AP AMADEUS-H in PNR as mini-PNR criteria */
	private boolean apAmadeusHExist;
	
	/** indicate FRBK SK temporary solution should more to booking level SK if we have further booking level sk*/
	private boolean frbkSK;
	
	/** the removed segment in rebook flow, build by RM */
	private List<RetrievePnrAtciCancelledSegment> atciCancelledSegments;
	
	/** for GDS booking, the cancelled segment is removed, use the mocked cancelled segment id for mapping */
	private List<RetrievePnrRebookMapping> atciRebookMapping;
	
	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public String getOfficeId() {
		return bookingCreateInfo == null ? null:bookingCreateInfo.getRpOfficeId();
	}

	public List<RetrievePnrPassenger> getPassengers() {
		if(passengers == null) {
			passengers = new ArrayList<>();
		}
		return passengers;
	}

	public void setPassengers(List<RetrievePnrPassenger> passengers) {
		this.passengers = passengers;
	}

	public List<RetrievePnrSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<RetrievePnrSegment> segments) {
		this.segments = segments;
	}

	public List<RetrievePnrPassengerSegment> getPassengerSegments() {
		if(passengerSegments == null) {
			passengerSegments = new ArrayList<>();
		}
		return passengerSegments;
	}

	public void setPassengerSegments(List<RetrievePnrPassengerSegment> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}
	public String getGdsRloc() {
		return gdsRloc;
	}

	public void setGdsRloc(String midRloc) {
		this.gdsRloc = midRloc;
	}

	public List<RetrievePnrDataElements> getSsrList() {
		return ssrList;
	}

	public List<RetrievePnrDataElements> findSsrList() {
		if(ssrList == null){
			ssrList = new ArrayList<>();
		}
		return ssrList;
	}
	
	public void setSsrList(List<RetrievePnrDataElements> ssrList) {
		this.ssrList = ssrList;
	}

	public List<RetrievePnrDataElements> getSkList() {
		return skList;
	}
	
	public List<RetrievePnrDataElements> findSkList() {
		if(skList == null){
			skList = new ArrayList<>();
		}
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

	public List<RetrievePnrRemark> getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(List<RetrievePnrRemark> remarkList) {
		this.remarkList = remarkList;
	}

	public List<RetrievePnrApContactPhone> getApContactPhones() {
		return apContactPhones;
	}
	
	public List<RetrievePnrApContactPhone> findApContactPhones() {
		if(apContactPhones == null){
			apContactPhones = new ArrayList<>();
		}
		return apContactPhones;
	}

	public void setApContactPhones(List<RetrievePnrApContactPhone> apContactPhones) {
		this.apContactPhones = apContactPhones;
	}

	public List<RetrievePnrApEmail> getApEmails() {
		return apEmails;
	}
	
	public List<RetrievePnrApEmail> findApEmails() {
		if(apEmails == null){
			apEmails = new ArrayList<>();
		}
		return apEmails;
	}

	public void setApEmails(List<RetrievePnrApEmail> apEmails) {
		this.apEmails = apEmails;
	}

	public String getSpnr() {
		if(bookingPackageInfo != null && BooleanUtils.isNotTrue(bookingPackageInfo.isFlightOnly())) {
			return bookingPackageInfo.getSpnr();
		}
		return null;
	}

	public List<RetrievePnrFa> getFaList() {
		return faList;
	}

	public void setFaList(List<RetrievePnrFa> faList) {
		this.faList = faList;
	}

	public List<RetrievePnrDataElements> getOsiList() {
		return osiList;
	}
	
	public List<RetrievePnrDataElements> findOsiList() {
		if(osiList == null) {
			osiList = new ArrayList<>();
		}
		return osiList;
	}

	public void setOsiList(List<RetrievePnrDataElements> osiList) {
		this.osiList = osiList;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public List<BigInteger> getInvalidOts() {
		return invalidOts;
	}
	
	public List<BigInteger> findInvalidOts() {
		if(invalidOts == null){
			invalidOts = new ArrayList<>();
		}
		return invalidOts;
	}

	public void setInvalidOts(List<BigInteger> invalidOts) {
		this.invalidOts = invalidOts;
	}

	public Boolean isCorporateBooking() {
		return corporateBooking;
	}

	public void setCorporateBooking(Boolean corporateBooking) {
		this.corporateBooking = corporateBooking;
	}

	public String getCreateDate() {
		return bookingCreateInfo == null ? null : bookingCreateInfo.getCreateDate();
	}

	public RetrievePnrBookingCerateInfo getBookingCreateInfo() {
		return bookingCreateInfo;
	}

	public void setBookingCreateInfo(RetrievePnrBookingCerateInfo bookingCreateInfo) {
		this.bookingCreateInfo = bookingCreateInfo;
	}

	public List<RetrievePnrTicketPriceInfo> findTicketPriceInfo() {
		if (ticketPriceInfo == null) {
			ticketPriceInfo = Lists.newArrayList();
		}
		return ticketPriceInfo;
	}
	public List<RetrievePnrTicketPriceInfo> getTicketPriceInfo() {
		return ticketPriceInfo;
	}

	public void setTicketPriceInfo(List<RetrievePnrTicketPriceInfo> ticketPriceInfo) {
		this.ticketPriceInfo = ticketPriceInfo;
	}

	public List<RetrievePnrFe> getFeList() {
		return feList;
	}

	public void setFeList(List<RetrievePnrFe> feList) {
		this.feList = feList;
	}

	public List<RetrievePnrDataElements> getFhdList() {
		return fhdList;
	}

	public void setFhdList(List<RetrievePnrDataElements> fhdList) {
		this.fhdList = fhdList;
	}

	public List<RetrievePnrRebookMapping> getRebookMapping() {
		return rebookMapping;
	}

	public void setRebookMapping(List<RetrievePnrRebookMapping> rebookMapping) {
		this.rebookMapping = rebookMapping;
	}

	public Boolean getCorporateBooking() {
		return corporateBooking;
	}

	public List<String> getBookingDwCode() {
		return bookingDwCode;
	}

	public void setBookingDwCode(List<String> bookingDwCode) {
		this.bookingDwCode = bookingDwCode;
	}
	
	public RetrievePnrOnHoldInfo getOnHoldRemarkInfo() {
		return onHoldRemarkInfo;
	}

	public void setOnHoldRemarkInfo(RetrievePnrOnHoldInfo onHoldRemarkInfo) {
		this.onHoldRemarkInfo = onHoldRemarkInfo;
	}

	public boolean isIDBooking() {
		return this.passengers != null && this.passengers.stream().anyMatch(p->p.getStaffDetail() != null && StaffBookingType.INDUSTRY_DISCOUNT.equals(p.getStaffDetail().getType()));
	}
	
	public boolean isADBooking() {
		return this.passengers != null && this.passengers.stream().anyMatch(p->p.getStaffDetail() != null && StaffBookingType.AGENCY_DISCOUNT.equals(p.getStaffDetail().getType()));
	}
	
	public boolean isStaffBooking() {
		return CollectionUtils.isNotEmpty(this.passengers)&&passengers.stream().anyMatch(pax->Objects.nonNull(pax.getStaffDetail()));
	}
	
	public RetrievePnrPassenger getPrimaryPassenger() {
		if(CollectionUtils.isEmpty(passengers)) {
			return null;
		}
		return passengers.stream().filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
	}

	public RetrievePnrBookingPackageInfo getBookingPackageInfo() {
		return bookingPackageInfo;
	}

	public void setBookingPackageInfo(RetrievePnrBookingPackageInfo bookingPackageInfo) {
		this.bookingPackageInfo = bookingPackageInfo;
	}

	public boolean isTicketRecordExist() {
		return ticketRecordExist;
	}

	public void setTicketRecordExist(boolean ticketRecordExist) {
		this.ticketRecordExist = ticketRecordExist;
	}

	public boolean isApAmadeusHExist() {
		return apAmadeusHExist;
	}

	public void setApAmadeusHExist(boolean apAmadeusHExist) {
		this.apAmadeusHExist = apAmadeusHExist;
	}

	
	public String getCreationPos() {
		return creationPos;
	}

	public void setCreationPos(String creationPos) {
		this.creationPos = creationPos;
	}

	public List<String> getTposList() {
		return tposList;
	}

	public void setTposList(List<String> tposList) {
		this.tposList = tposList;
	}

	public boolean isFrbkSK() {
		return frbkSK;
	}

	public void setFrbkSK(boolean frbkSK) {
		this.frbkSK = frbkSK;
	}

	public List<RetrievePnrAtciCancelledSegment> getAtciCancelledSegments() {
		return atciCancelledSegments;
	}

	public void setAtciCancelledSegments(List<RetrievePnrAtciCancelledSegment> atciCancelledSegments) {
		this.atciCancelledSegments = atciCancelledSegments;
	}

	public List<RetrievePnrRebookMapping> getAtciRebookMapping() {
		return atciRebookMapping;
	}
	
	public List<RetrievePnrRebookMapping> findAtciRebookMapping() {
		if (atciRebookMapping == null) {
			atciRebookMapping = new ArrayList<>();
		}
		return atciRebookMapping;
	}

	public void setAtciRebookMapping(List<RetrievePnrRebookMapping> atciRebookMapping) {
		this.atciRebookMapping = atciRebookMapping;
	}
	
}
