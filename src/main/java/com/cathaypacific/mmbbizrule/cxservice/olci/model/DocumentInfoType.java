/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.cxservice.olci.model;
import java.io.Serializable;
/**
 * The Class DocumentInfoType.
 */
public class DocumentInfoType implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4950496860633371547L;
    /** The document number. */
    private String documentNumber;
    /** The document type. */
    private String documentType;
    /** The expiry date. */
    private String expiryDate;
    /** The country of issue. */
    private String countryOfIssue;
    /**
     * Gets the document number.
     *
     * @return the documentNumber
     */
    public String getDocumentNumber() {
        return documentNumber;
    }
    /**
     * Sets the document number.
     *
     * @param inDocumentNumber the new document number
     */
    public void setDocumentNumber(final String inDocumentNumber) {
        this.documentNumber = inDocumentNumber;
    }
    /**
     * Gets the document type.
     *
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }
    /**
     * Sets the document type.
     *
     * @param inDocumentType the new document type
     */
    public void setDocumentType(final String inDocumentType) {
        this.documentType = inDocumentType;
    }
    /**
     * Gets the expiry date.
     *
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }
    /**
     * Sets the expiry date.
     *
     * @param inExpiryDate the new expiry date
     */
    public void setExpiryDate(final String inExpiryDate) {
        this.expiryDate = inExpiryDate;
    }
    /**
     * Gets the country of issue.
     *
     * @return the countryOfIssue
     */
    public String getCountryOfIssue() {
        return countryOfIssue;
    }
    /**
     * Sets the country of issue.
     *
     * @param inCountryOfIssue the new country of issue
     */
    public void setCountryOfIssue(final String inCountryOfIssue) {
        this.countryOfIssue = inCountryOfIssue;
    }
}
