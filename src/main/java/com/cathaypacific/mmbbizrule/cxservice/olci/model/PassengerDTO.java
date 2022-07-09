package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.util.List;

public class PassengerDTO extends BaseResponseDTO{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2563195857186751330L;

	/** Passenger Unique Customer ID, UCI */
    private String uniqueCustomerId;

    /** Passenger Title */
    private String title;

    /** Passenger FamilyName */
    private String familyName;

    /** Passenger GivenName */
    private String givenName;

    /** Passenger Gender*/
    private String gender;
    
    /** passenger checkin status**/
    private boolean checkInAccepted ;
    
    private List<FlightDTO> flights;

    public String getUniqueCustomerId() {
        return uniqueCustomerId;
    }

    public void setUniqueCustomerId(String uniqueCustomerId) {
        this.uniqueCustomerId = uniqueCustomerId;
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

    public boolean isCheckInAccepted() {
        return checkInAccepted;
    }

    public void setCheckInAccepted(boolean checkInAccepted) {
        this.checkInAccepted = checkInAccepted;
    }

	public List<FlightDTO> getFlights() {
		return flights;
	}

	public void setFlights(List<FlightDTO> flights) {
		this.flights = flights;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
