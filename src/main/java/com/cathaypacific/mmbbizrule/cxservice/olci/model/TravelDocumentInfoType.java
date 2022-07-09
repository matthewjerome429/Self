/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Class TravelDocumentInfoType.
 */
public class TravelDocumentInfoType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6855859006831540739L;
    /** The last name. */
    private String lastName;
    /** The first name. */
    private String firstName;
    /** The middle name. */
    private String middleName;
    /** The document info type list. */
    private ArrayList<DocumentInfoType> documentInfoTypeList = new ArrayList<>();
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
     * @param inLastName the new last name
     */
    public void setLastName(final String inLastName) {
        this.lastName = inLastName;
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
     * @param inFirstName the new first name
     */
    public void setFirstName(final String inFirstName) {
        this.firstName = inFirstName;
    }
    /**
     * Gets the middle name.
     *
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }
    /**
     * Sets the middle name.
     *
     * @param inMiddleName the new middle name
     */
    public void setMiddleName(final String inMiddleName) {
        this.middleName = inMiddleName;
    }
    /**
     * Gets the document info type list.
     *
     * @return the documentInfoTypeList
     */
    public ArrayList<DocumentInfoType> getDocumentInfoTypeList() {
        return documentInfoTypeList;
    }
    /**
     * Sets the document info type list.
     *
     * @param inDocumentInfoTypeList the new document info type list
     */
    public void setDocumentInfoTypeList(final ArrayList<DocumentInfoType> inDocumentInfoTypeList) {
        this.documentInfoTypeList = inDocumentInfoTypeList;
    }
}
