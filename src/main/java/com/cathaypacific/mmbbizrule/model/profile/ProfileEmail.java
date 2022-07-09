package com.cathaypacific.mmbbizrule.model.profile;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfileEmail {

	/** email address */
	private String email;
	
	/** type of this information */
	private String type; 
	
	/** Whether it is olss contact */
	private boolean olssContact;

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
	
}
