package com.cathaypacific.mmbbizrule.model.booking.summary;

import com.cathaypacific.mbcommon.enums.booking.BookingSources;

public class TempLinkedBooking {
	
	/** 1A rloc or oj rloc  */
	private String rloc;
	
	/** primary passenger id */
	private String primaryPassengerId;
	
    /** The family name. */
    private String familyName;
    
    /** The given name. */
    private String givenName;
    
    /** The booking sources, Member profile, Temp add to EODS, email  link etc. */
    private BookingSources bookingSources;
    

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getPrimaryPassengerId() {
		return primaryPassengerId;
	}

	public void setPrimaryPassengerId(String primaryPassengerId) {
		this.primaryPassengerId = primaryPassengerId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public BookingSources getBookingSources() {
		return bookingSources;
	}

	public void setBookingSources(BookingSources bookingSources) {
		this.bookingSources = bookingSources;
	}
	
}
