package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.summary.UMFormInfoSummary;

public class FlightBookingSummaryDTO implements Serializable{

	private static final long serialVersionUID = 94287449872347924L;

	private String rloc;
	
	private String oneARloc;
	
	private String spnr;
	
	private String createDate;//ddmmyy
	
	private String officeId;
	
	private List<SegmentSummaryDTO> details;
	
	private List<Passenger> passengers;
	
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
	
	private boolean canCheckIn;
	
	private List<PassengerSegmentSummaryDTO> passengerSegments;
	
	private String encryptedRloc;
	
	private boolean onHoldBooking;
	
	private boolean canIssueTicket;
	
	private boolean issueTicketsExisting;
	
	private boolean adtk;
	
	private boolean pcc;
	
	private TicketIssueInfoSummaryDTO ticketIssueInfo;
	
	private boolean tktl;

	private boolean tkxl;
	
	private List<RebookMappingDTO> rebookMapping;
	
	/**for PNR  of item in FE element**/
	private List<String> bookingDwCodeList;
	
	/** UMNR link display logic*/
    private UMFormInfoSummary umFormInfo;
  /** member logged in is not traveling*/
  private boolean companionBooking;
  
	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
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

	public List<SegmentSummaryDTO> getDetails() {
		return details;
	}

	public void setDetails(List<SegmentSummaryDTO> details) {
		this.details = details;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
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

	public boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public List<PassengerSegmentSummaryDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentSummaryDTO> passengerSegments) {
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

	public TicketIssueInfoSummaryDTO getTicketIssueInfo() {
		return ticketIssueInfo;
	}

	public void setTicketIssueInfo(TicketIssueInfoSummaryDTO ticketIssueInfo) {
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

	public List<RebookMappingDTO> getRebookMapping() {
		return rebookMapping;
	}

	public void setRebookMapping(List<RebookMappingDTO> rebookMapping) {
		this.rebookMapping = rebookMapping;
	}

	public List<String> getBookingDwCodeList() {
		return bookingDwCodeList;
	}

	public void setBookingDwCodeList(List<String> bookingDwCodeList) {
		this.bookingDwCodeList = bookingDwCodeList;
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
	
}
