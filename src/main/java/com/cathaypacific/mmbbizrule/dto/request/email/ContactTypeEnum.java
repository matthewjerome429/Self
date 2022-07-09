package com.cathaypacific.mmbbizrule.dto.request.email;

public enum ContactTypeEnum {
	
	CONTACT_TYPE_EMAIL("EMAIL"),
	CONTACT_TYPE_SMS("SMS"),
	CONTACT_TYPE_PUSH("PUSH");
    
	private String type;
	
	private ContactTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
