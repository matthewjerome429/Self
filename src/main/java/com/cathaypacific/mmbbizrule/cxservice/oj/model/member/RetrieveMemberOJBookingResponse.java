package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ManageBooking"
})
public class RetrieveMemberOJBookingResponse implements Serializable {

	private static final long serialVersionUID = 3440695466306384196L;
	
	@JsonProperty("ManageBooking")
	private ManageBooking manageBooking;

	public ManageBooking getManageBooking() {
		return manageBooking;
	}

	public void setManageBooking(ManageBooking manageBooking) {
		this.manageBooking = manageBooking;
	}

}
