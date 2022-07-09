/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
/**
 * The Class ContactInfo.
 */
public class ContactInfo implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -498985593828906264L;
    /** The passenger contact. */
    private String passengerContact;
    /** name */
    private String contactName;
    /**countryCode*/
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * Gets the passenger contact.
     *
     * @return the passengerContact
     */
    public String getPassengerContact() {
        return passengerContact;
    }
    /**
     * Sets the passenger contact.
     *
     * @param inPassengerContact the new passenger contact
     */
    public void setPassengerContact(final String inPassengerContact) {
        this.passengerContact = inPassengerContact;
    }
}
