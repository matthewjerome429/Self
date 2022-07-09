package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.RebookMapping;

public class FlightBookingSummary {
	
	private String loginRloc;
	
	private String oneARloc;
	
	private String displayRloc;
	
	private String gdsRloc;
	
	private String spnr;
	
	private String createDate;//ddmmyy
	
	private String officeId;
	
	private List<SegmentSummary> details;
	
	private List<Passenger> passengers;
	
	private boolean flightOnly;
	
	private boolean ibeBooking;
	private boolean trpBooking;
	private boolean appBooking;
	private boolean gdsBooking;
	private boolean groupBooking;
	private boolean redBooking;	//redemption booking
	private boolean gccBooking;
	private boolean staffBooking;
	private boolean isRedUpgrade;
	
	private boolean displayOnly;
	
	private boolean packageBooking;
	
	private boolean inEods;
	
	/**
	 * Linked booking
	 */
	private boolean tempLinkedBooking;
	
	//this only used for v1 
	@Deprecated
	private boolean canCheckIn;
	
	private List<PassengerSegmentSummary> passengerSegments;
	
	private String encryptedRloc;
	
	private boolean onHoldBooking;
	
	private boolean canIssueTicket;
	
	private boolean issueTicketsExisting;
	
	private boolean adtk;
	
	private boolean pcc;
	
	private TicketIssueInfoSummary ticketIssueInfo;
	
	private boolean tktl;
	
	private boolean tkxl;
	
	/**for PNR  of item in FE element**/
	private List<String> bookingDwCode;
	
	/** the relationship between cancelled segmentIds and accept segmentIds */
    private List<RebookMapping> rebookMapping;
    
    /** UMNR link display logic*/
    private UMFormInfoSummary umFormInfo;
    /** member logged in is not traveling*/
	private boolean companionBooking;
    
	/** OLCI journey info */
	private List<Journey> cprJourneys;
	
	/** is mice booking **/
	private boolean miceBooking;
	
	public Passenger getPrimaryPassenger() {
		if(CollectionUtils.isEmpty(passengers)) {
			return null;
		}
		return passengers.stream().filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
	}
	

	public String getLoginRloc() {
		return loginRloc;
	}

	public void setLoginRloc(String loginRloc) {
		this.loginRloc = loginRloc;
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

	public String getSpnr() {
		return spnr;
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public List<SegmentSummary> getDetails() {
		return details;
	}

	public void setDetails(List<SegmentSummary> details) {
		this.details = details;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public boolean isFlightOnly() {
		return flightOnly;
	}

	public void setFlightOnly(boolean flightOnly) {
		this.flightOnly = flightOnly;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
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

	public boolean isGroupBooking() {
		return groupBooking;
	}

	public void setGroupBooking(boolean groupBooking) {
		this.groupBooking = groupBooking;
	}

	public boolean isRedBooking() {
		return redBooking;
	}

	public void setRedBooking(boolean redBooking) {
		this.redBooking = redBooking;
	}

	public boolean isGccBooking() {
		return gccBooking;
	}

	public void setGccBooking(boolean gccBooking) {
		this.gccBooking = gccBooking;
	}

	public boolean isPackageBooking() {
		return packageBooking;
	}

	public void setPackageBooking(boolean packageBooking) {
		this.packageBooking = packageBooking;
	}

	public boolean isStaffBooking() {
		return staffBooking;
	}

	public void setStaffBooking(boolean staffBooking) {
		this.staffBooking = staffBooking;
	}

	public boolean isRedUpgrade() {
		return isRedUpgrade;
	}

	public void setRedUpgrade(boolean isRedUpgrade) {
		this.isRedUpgrade = isRedUpgrade;
	}

	public boolean isInEods() {
		return inEods;
	}

	public void setInEods(boolean inEods) {
		this.inEods = inEods;
	}

	public boolean isTempLinkedBooking() {
		return tempLinkedBooking;
	}

	public void setTempLinkedBooking(boolean tempLinkedBooking) {
		this.tempLinkedBooking = tempLinkedBooking;
	}
	//this only used for v1 
	@Deprecated
	public boolean isCanCheckIn() {
		return canCheckIn;
	}
	//this only used for v1 
	@Deprecated
	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public List<PassengerSegmentSummary> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentSummary> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public String getEncryptedRloc() {
		return encryptedRloc;
	}

	public void setEncryptedRloc(String encryptedRloc) {
		this.encryptedRloc = encryptedRloc;
	}

	public boolean isOnHoldBooking() {
		return onHoldBooking;
	}

	public void setOnHoldBooking(boolean onHoldBooking) {
		this.onHoldBooking = onHoldBooking;
	}

	public boolean isCanIssueTicket() {
		return canIssueTicket;
	}

	public void setCanIssueTicket(boolean canIssueTicket) {
		this.canIssueTicket = canIssueTicket;
	}

	public boolean isIssueTicketsExisting() {
		return issueTicketsExisting;
	}

	public void setIssueTicketsExisting(boolean issueTicketsExisting) {
		this.issueTicketsExisting = issueTicketsExisting;
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

	public TicketIssueInfoSummary getTicketIssueInfo() {
		return ticketIssueInfo;
	}

	public void setTicketIssueInfo(TicketIssueInfoSummary ticketIssueInfo) {
		this.ticketIssueInfo = ticketIssueInfo;
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

	public List<RebookMapping> getRebookMapping() {
		return rebookMapping;
	}

	public void setRebookMapping(List<RebookMapping> rebookMapping) {
		this.rebookMapping = rebookMapping;
	}

	public List<String> getBookingDwCode() {
		return bookingDwCode;
	}

	public void setBookingDwCode(List<String> bookingDwCode) {
		this.bookingDwCode = bookingDwCode;
	}

	public UMFormInfoSummary getUmFormInfo() {
		return umFormInfo;
	}

	public void setUmFormInfo(UMFormInfoSummary umFormInfo) {
		this.umFormInfo = umFormInfo;
	}

	public boolean isCompanionBooking() {
		return companionBooking;
	}

	public void setCompanionBooking(boolean companionBooking) {
		this.companionBooking = companionBooking;
	}

	public List<Journey> getCprJourneys() {
		return cprJourneys;
	}

	public void setCprJourneys(List<Journey> cprJourneys) {
		this.cprJourneys = cprJourneys;
	}

	public String getDisplayRloc() {
		return displayRloc;
	}

	public void setDisplayRloc(String displayRloc) {
		this.displayRloc = displayRloc;
	}


    public boolean isMiceBooking() {
        return miceBooking;
    }


    public void setMiceBooking(boolean miceBooking) {
        this.miceBooking = miceBooking;
    }
 
}
