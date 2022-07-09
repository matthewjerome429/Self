package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

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
    "Amount",
    "BookingDate",
    "StartDate",
    "EndDate",
    "Product"
})
public class OJBookingDTO implements Serializable{
	private static final long serialVersionUID = -4569735654544478410L;
	
	@JsonProperty("SuperPNR_ID")
	private String superPNRID;
	
	@JsonProperty("BookingStatus")
	private String bookingStatus;
	
	@JsonProperty("Amount")
	private String amount;
	
	@JsonProperty("BookingDate")
	private String bookingDate;
	
	@JsonProperty("StartDate")
	private String startDate;
	
	@JsonProperty("EndDate")
	private String endDate;
	
	@JsonProperty("Product")
	private List<Product> products;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public String getSuperPNRID() {
		return superPNRID;
	}

	public void setSuperPNRID(String superPNRID) {
		this.superPNRID = superPNRID;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
