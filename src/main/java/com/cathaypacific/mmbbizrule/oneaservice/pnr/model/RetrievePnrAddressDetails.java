package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrAddressDetails {
	/** Street name */
	private String street;
	/** city */
	private String city;
	/** State code */
	private String stateCode;
	/** Zip code */
	private String zipCode;
	/** country */
	private String country;
	/** OT number in pnr */
	private BigInteger qualifierId;
	/** if this is a address of infant */
	private Boolean infAddress;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public BigInteger getQualifierId() {
		return qualifierId;
	}
	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	public Boolean isInfAddress() {
		return infAddress;
	}
	public void setInfAddress(Boolean infAddress) {
		this.infAddress = infAddress;
	}
	
}
