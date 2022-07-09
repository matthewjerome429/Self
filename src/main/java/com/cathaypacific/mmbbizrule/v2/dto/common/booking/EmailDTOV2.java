package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;


public class EmailDTOV2 implements Serializable{

	private static final long serialVersionUID = -1713008644403267811L;

	/** email address */
	private String email;
	
	/** type of this information */
	private String type; 
	
	/** Whether it is olss contact */
	@ApiModelProperty(value = "if true the email information is from olss contact.", required = false)
	private boolean olssContact;
	@ApiModelProperty(value = "if true the email is masked.", required = false)
	private Boolean emailMasked;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public Boolean isEmpty() {
		
		if(StringUtils.isEmpty(email) && StringUtils.isEmpty(type)) {
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isOlssContact() {
		return olssContact;
	}

	public void setOlssContact(boolean olssContact) {
		this.olssContact = olssContact;
	}

	public Boolean getEmailMasked() {
		return emailMasked;
	}

	public void setEmailMasked(Boolean emailMasked) {
		this.emailMasked = emailMasked;
	}
	
}
