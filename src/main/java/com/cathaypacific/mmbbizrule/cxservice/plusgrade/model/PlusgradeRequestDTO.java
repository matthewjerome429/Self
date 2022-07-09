package com.cathaypacific.mmbbizrule.cxservice.plusgrade.model;

import java.io.Serializable;
import java.util.List;

public class PlusgradeRequestDTO implements Serializable {

	private static final long serialVersionUID = 6521472055156331484L;
	
	private String apiKey;
	private String firstName;
	private String lastName;
	private String loyaltyLevel;
	private String loyaltyCompany;
	private String loyaltyNumber;
	private String email;
	private String mobilePhone;
	private String pax;
	private String currency;
	private String language;
	private String pointOfSale;
	private String pnr;
	private boolean infant;
	private List<PlusgradeRequestFlightDTO> flights;
	private List<PlusgradeRequestTicketDTO> tickets;

	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLoyaltyLevel() {
		return loyaltyLevel;
	}
	public void setLoyaltyLevel(String loyaltyLevel) {
		this.loyaltyLevel = loyaltyLevel;
	}
	public String getLoyaltyCompany() {
		return loyaltyCompany;
	}
	public void setLoyaltyCompany(String loyaltyCompany) {
		this.loyaltyCompany = loyaltyCompany;
	}
	public String getLoyaltyNumber() {
		return loyaltyNumber;
	}
	public void setLoyaltyNumber(String loyaltyNumber) {
		this.loyaltyNumber = loyaltyNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}	
	public String getPax() {
		return pax;
	}
	public void setPax(String pax) {
		this.pax = pax;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPointOfSale() {
		return pointOfSale;
	}
	public void setPointOfSale(String pointOfSale) {
		this.pointOfSale = pointOfSale;
	}
	public String getPnr() {
		return pnr;
	}
	public void setPnr(String pnr) {
		this.pnr = pnr;
	}
	public boolean isInfant() {
		return infant;
	}
	public void setInfant(boolean infant) {
		this.infant = infant;
	}
	public List<PlusgradeRequestFlightDTO> getFlights() {
		return flights;
	}
	public void setFlights(List<PlusgradeRequestFlightDTO> flights) {
		this.flights = flights;
	}
	public List<PlusgradeRequestTicketDTO> getTickets() {
		return tickets;
	}
	public void setTickets(List<PlusgradeRequestTicketDTO> tickets) {
		this.tickets = tickets;
	}
	@Override
	public String toString() {
		return "PlusgradeRequestDTO [apiKey=" + apiKey + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", loyaltyLevel=" + loyaltyLevel + ", loyaltyCompany=" + loyaltyCompany + ", loyaltyNumber="
				+ loyaltyNumber + ", email=" + email + ", mobilePhone=" + mobilePhone + ", pax=" + pax + ", currency="
				+ currency + ", language=" + language + ", pointOfSale=" + pointOfSale + ", pnr=" + pnr + ", infant="
				+ infant + ", flights=" + flights + ", tickets=" + tickets + "]";
	}
}	
