package com.cathaypacific.mbcommon.enums.booking;

public enum ContactInfoTypeEnum {
	
	APX_CONTACT_INFO("AP"),
	CTCX_CONTACT_INFO("CT"),
	MEMBER_PROFILE_CONTACT_INFO("PF");
	
	private String type;
	
	private ContactInfoTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
