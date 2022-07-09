package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;

public class BookingCustomizedInfoResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = -6711192300097411678L;

	/** 1A RLOC */
	private String oneARloc;
	
	/** OJ RLOC, seven letter */
	private String spnr;
	
	/** passenger customized info*/
	private List<PassengeCustomizedInfoDTO> passengers;
	
	/** segment customized info */
	private List<SegmentCustomizedInfoDTO> segments;
	
	/** passengerSegment customized info */
	private List<PassengerSegmentCustomizedInfoDTO> passengerSegments;
	
	/** is staff booking */
	private Boolean staffBooking;
	
	/** is group booking */
	private Boolean groupBooking;
	
	/** is redemption booking */
	private Boolean redemptionBooking;
	
	/** booking package info */
    private BookingPackageCustomizedInfoDTO bookingPackageInfo;
    
	private UpgradeProgressStatus bookableUpgradeProgressStatus;
	
	private String pos;
	
	private String posForAep;
	
	private String createDate;
	
	private List<String> tposList;
	
	/** journeys from CPR */
	private List<JourneyCustomizedInfoDTO> cprJourneys;

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

	public List<PassengeCustomizedInfoDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengeCustomizedInfoDTO> passengers) {
		this.passengers = passengers;
	}

	public List<SegmentCustomizedInfoDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentCustomizedInfoDTO> segments) {
		this.segments = segments;
	}

	public List<PassengerSegmentCustomizedInfoDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentCustomizedInfoDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public Boolean getStaffBooking() {
		return staffBooking;
	}

	public void setStaffBooking(Boolean staffBooking) {
		this.staffBooking = staffBooking;
	}

	public Boolean getGroupBooking() {
		return groupBooking;
	}

	public void setGroupBooking(Boolean groupBooking) {
		this.groupBooking = groupBooking;
	}

	public Boolean getRedemptionBooking() {
		return redemptionBooking;
	}

	public void setRedemptionBooking(Boolean redemptionBooking) {
		this.redemptionBooking = redemptionBooking;
	}

	public BookingPackageCustomizedInfoDTO getBookingPackageInfo() {
		return bookingPackageInfo;
	}

	public void setBookingPackageInfo(BookingPackageCustomizedInfoDTO bookingPackageInfo) {
		this.bookingPackageInfo = bookingPackageInfo;
	}

	public UpgradeProgressStatus getBookableUpgradeProgressStatus() {
		return bookableUpgradeProgressStatus;
	}

	public void setBookableUpgradeProgressStatus(UpgradeProgressStatus bookableUpgradeProgressStatus) {
		this.bookableUpgradeProgressStatus = bookableUpgradeProgressStatus;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public List<JourneyCustomizedInfoDTO> getCprJourneys() {
		return cprJourneys;
	}

	public void setCprJourneys(List<JourneyCustomizedInfoDTO> cprJourneys) {
		this.cprJourneys = cprJourneys;
	}

	public List<String> getTposList() {
		return tposList;
	}

	public void setTposList(List<String> tposList) {
		this.tposList = tposList;
	}
	
	
	
}
