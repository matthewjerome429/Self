package com.cathaypacific.mmbbizrule.dto.request.email;

public enum RemarkTypeEnum {
	
	REMARK_TRPE_RECIPIENT("RecipientID"),
	REMARK_TRPE_BOOKING("BookingType"),
	REMARK_TYPE_NOTIFICATION("NotificationType"),
	REMARK_TYPE_ADCMSG("ADCMsg");
	private String type;
	
	private RemarkTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
