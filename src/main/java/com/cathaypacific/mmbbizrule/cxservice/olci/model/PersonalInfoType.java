/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class PersonalInfoType.
 */
public class PersonalInfoType implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6515486476160331088L;
	/** The family name. */
	private String familyName;;
	/** The last name. */
	private String lastName;
	/** The gender. */
	private String gender;
	/** The dob. */
	private String dob;
	/** The country of residence. */
	private String countryOfResidence;
	/** The usa address. */
	private AddressType usaAddress;
	/** The contact info. */
	private ContactInfo contactInfo;
	/** The first name. */
	private String firstName;

	/**
	 * Gets the last name.
	 *
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param inLastName
	 *            the new last name
	 */
	public void setLastName(final String inLastName) {
		this.lastName = inLastName;
	}

	/**
	 * Gets the usa address.
	 *
	 * @return the usaAddress
	 */
	public AddressType getUsaAddress() {
		return usaAddress;
	}

	/**
	 * Sets the usa address.
	 *
	 * @param inUsaAddress
	 *            the new usa address
	 */
	public void setUsaAddress(final AddressType inUsaAddress) {
		this.usaAddress = inUsaAddress;
	}

	/**
	 * Gets the contact info.
	 *
	 * @return the contactInfo
	 */
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	/**
	 * Sets the contact info.
	 *
	 * @param inContactInfo
	 *            the new contact info
	 */
	public void setContactInfo(final ContactInfo inContactInfo) {
		this.contactInfo = inContactInfo;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 *
	 * @param inGender
	 *            the new gender
	 */
	public void setGender(final String inGender) {
		this.gender = inGender;
	}

	/**
	 * Gets the dob.
	 *
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * Sets the dob.
	 *
	 * @param inDob
	 *            the new dob
	 */
	public void setDob(final String inDob) {
		this.dob = inDob;
	}

	/**
	 * Gets the country of residence.
	 *
	 * @return the countryOfResidence
	 */
	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	/**
	 * Sets the country of residence.
	 *
	 * @param inCountryOfResidence
	 *            the new country of residence
	 */
	public void setCountryOfResidence(final String inCountryOfResidence) {
		this.countryOfResidence = inCountryOfResidence;
	}

	/**
	 * Sets the family name.
	 *
	 * @param inFamilyName
	 *            the new family name
	 */
	public void setFamilyName(final String inFamilyName) {
		this.familyName = inFamilyName;
	}

	/**
	 * Gets the family name.
	 *
	 * @return the family name
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param theFirstName
	 *            the firstName to set
	 */
	public void setFirstName(final String theFirstName) {
		this.firstName = theFirstName;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("gender", gender);
		builder.append("dob", dob);
		builder.append("countryOfResidence", countryOfResidence);
		builder.append("usaAddress", usaAddress);
		builder.append("contactInfo", contactInfo);
		return builder.toString();
	}
}
