
package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "SuperPNR_ID",
    "BookingStatus",
    "StartDate",
    "EndDate",
    "Product",
    "Documents",
    "ContactDetails"
})
public class OJBookingResponseDTO implements Serializable {

	private static final long serialVersionUID = -2052496095895063791L;
	
	@JsonProperty("SuperPNR_ID")
    private String superPNRID;
	
    @JsonProperty("BookingStatus")
    private String bookingStatus;
    
    @JsonProperty("StartDate")
    private String startDate;
    
    @JsonProperty("EndDate")
    private String endDate;   
    
    @JsonProperty("Product")
    private List<Product> product;
    
    @JsonProperty("Documents")
    private Documents documents;
    
    @JsonProperty("ContactDetails")
    private ContactDetails contactDetails;
    
    private NameInput nameInput;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("SuperPNR_ID")
    public String getSuperPNRID() {
        return superPNRID;
    }

    @JsonProperty("SuperPNR_ID")
    public void setSuperPNRID(String superPNRID) {
        this.superPNRID = superPNRID;
    }

    @JsonProperty("BookingStatus")
    public String getBookingStatus() {
        return bookingStatus;
    }

    @JsonProperty("BookingStatus")
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    
    @JsonProperty("StartDate")
    public String getStartDate() {
		return startDate;
	}

    @JsonProperty("StartDate")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

    @JsonProperty("EndDate")
	public String getEndDate() {
		return endDate;
	}

    @JsonProperty("EndDate")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@JsonProperty("Product")
    public List<Product> getProduct() {
        return product;
    }

    @JsonProperty("Product")
    public void setProduct(List<Product> product) {
        this.product = product;
    }

    @JsonProperty("Documents")
    public Documents getDocuments() {
        return documents;
    }

    @JsonProperty("Documents")
    public void setDocuments(Documents documents) {
        this.documents = documents;
    }
    
    @JsonProperty("ContactDetails")
    public ContactDetails getContactDetails() {
		return contactDetails;
	}

    @JsonProperty("ContactDetails")
	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public NameInput getNameInput() {
		return nameInput;
	}

	public void setNameInput(NameInput nameInput) {
		this.nameInput = nameInput;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
