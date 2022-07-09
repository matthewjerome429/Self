package com.cathaypacific.mmbbizrule.dto.request.email;

public enum TemplateTypeEnum {
	
	EMAIL_TEMPLATE_TYPE_GENERIC("DynamicWaiver"),
	EMAIL_TEMPLATE_TYPE_NON_GENERIC("DynamicWaiver"),
	EMAIL_TEMPLATE_TYPE_REBOOK_CONFIRM("RebookConfirm"),
	EMAIL_TEMPLATE_TYPE_CANCEL_BOOKING("CancelBookingConfirm");

	private String type;
	
	private TemplateTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
