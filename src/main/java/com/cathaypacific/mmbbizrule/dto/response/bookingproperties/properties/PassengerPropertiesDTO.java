package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class PassengerPropertiesDTO implements Serializable{
	
	private static final long serialVersionUID = -5806418996984531708L;

	/** passenger id */
	private String passengerId;
	
    /** The title. */
    private String title;
    
    /** The family name. */
    private String familyName;
    
    /** The given name. */
    private String givenName;
	
	/** passenger type is INF */
	private Boolean infant;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Boolean isInfant() {
		return infant;
	}

	public void setInfant(Boolean infant) {
		this.infant = infant;
	}
	
}
