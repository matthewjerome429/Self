package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.oj.model.NameInput;
import com.cathaypacific.mmbbizrule.dto.common.booking.ContactDetailsDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DocumentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelBookingDTO;

import io.swagger.annotations.ApiModelProperty;

public class BookingDTOV2 implements Serializable {

	private static final long serialVersionUID = 1487190770597186580L;
	
	private String bookingType;
	
	@ApiModelProperty(value = "rloc or spnr.", required = true)
	private String bookingReference;
	
	private FlightBookingDTOV2 flightBooking;
	private HotelBookingDTO hotelBooking;
	private EventBookingDTO eventBooking;
	
	//TODO please use gotOneA and gotOJ
	private Boolean retrieveOneA = true;
	private Boolean retrieveOJ = true;
	
	//private Boolean ibeBooking = false;
	//private Boolean trpBooking = false;
	//private Boolean appBooking = false;
	//private Boolean gdsBooking = false;
	//private Boolean gccBooking = false;
	//private Boolean gdsGroupBooking = false;
	//private Boolean redBooking = false;
	//private Boolean staffBooking = false;

	private String bookingDate;

	private List<DocumentDTO> documents;
	
	private ContactDetailsDTO contactDetails;
	
	private NameInput nameInput;
	
	public BookingDTOV2() {
		super();
	}

	public BookingDTOV2(String bookingType) {
		super();
		this.bookingType = bookingType;
	}
	
	public String getOjRloc() {
		if(flightBooking != null) {
			return flightBooking.getSpnr();
		} else {
			return bookingReference;
		}
	}

	public String getOneARloc() {
		if(flightBooking != null) {
			return flightBooking.getOneARloc();
		} else {
			return null;
		}
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

	public Boolean getRetrieveOneA() {
		return retrieveOneA;
	}

	public void setRetrieveOneA(Boolean retrieveOneA) {
		this.retrieveOneA = retrieveOneA;
	}

	public Boolean getRetrieveOJ() {
		return retrieveOJ;
	}

	public void setRetrieveOJ(Boolean retrieveOJ) {
		this.retrieveOJ = retrieveOJ;
	}

	/*
	 * public Boolean isIbeBooking() { return ibeBooking; }
	 * 
	 * public void setIbeBooking(Boolean ibeBooking) { this.ibeBooking = ibeBooking;
	 * }
	 * 
	 * public Boolean isTrpBooking() { return trpBooking; }
	 * 
	 * public void setTrpBooking(Boolean trpBooking) { this.trpBooking = trpBooking;
	 * }
	 * 
	 * public Boolean isAppBooking() { return appBooking; }
	 * 
	 * public void setAppBooking(Boolean appBooking) { this.appBooking = appBooking;
	 * }
	 * 
	 * public Boolean isGdsBooking() { return gdsBooking; }
	 * 
	 * public void setGdsBooking(Boolean gdsBooking) { this.gdsBooking = gdsBooking;
	 * }
	 * 
	 * public Boolean isGccBooking() { return gccBooking; }
	 * 
	 * public void setGccBooking(Boolean gccBooking) { this.gccBooking = gccBooking;
	 * }
	 * 
	 * public Boolean isGdsGroupBooking() { return gdsGroupBooking; }
	 * 
	 * public void setGdsGroupBooking(Boolean gdsGroupBooking) {
	 * this.gdsGroupBooking = gdsGroupBooking; }
	 * 
	 * public Boolean isRedBooking() { return redBooking; }
	 * 
	 * public void setRedBooking(Boolean redBooking) { this.redBooking = redBooking;
	 * }
	 * 
	 * public void resetBookingChannel() { ibeBooking = null; trpBooking = null;
	 * appBooking = null; gdsBooking = null; gccBooking = null; gdsGroupBooking =
	 * null; redBooking = null; staffBooking = null; }
	 */
	public FlightBookingDTOV2 getFlightBooking() {
		return flightBooking;
	}

	public void setFlightBooking(FlightBookingDTOV2 flightBooking) {
		this.flightBooking = flightBooking;
	}

	public HotelBookingDTO getHotelBooking() {
		return hotelBooking;
	}

	public void setHotelBooking(HotelBookingDTO hotelBooking) {
		this.hotelBooking = hotelBooking;
	}

	public EventBookingDTO getEventBooking() {
		return eventBooking;
	}

	public void setEventBooking(EventBookingDTO eventBooking) {
		this.eventBooking = eventBooking;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public ContactDetailsDTO getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(ContactDetailsDTO contactDetails) {
		this.contactDetails = contactDetails;
	}

	/*
	 * public Boolean getStaffBooking() { return staffBooking; }
	 * 
	 * public void setStaffBooking(Boolean staffBooking) { this.staffBooking =
	 * staffBooking; }
	 */

	public NameInput getNameInput() {
		return nameInput;
	}

	public void setNameInput(NameInput nameInput) {
		this.nameInput = nameInput;
	}
	
	public boolean isTempLinkedBooking() {
		return flightBooking != null && flightBooking.isTempLinkedBooking();
	}

}
