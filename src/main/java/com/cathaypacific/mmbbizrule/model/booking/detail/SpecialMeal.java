package com.cathaypacific.mmbbizrule.model.booking.detail;

public class SpecialMeal {
	
	private String type;
	
	private boolean consent;

	public SpecialMeal() {
		super();
	}

	/**
	 * This constructor used for SQL entity mapping
	 * 
	 * @param type
	 * @param consent
	 */
	public SpecialMeal(String type, byte consent) {
		super();
		this.type = type;
		this.consent = "1".equals(Byte.toString(consent));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isConsent() {
		return consent;
	}

	public void setConsent(boolean consent) {
		this.consent = consent;
	}
	
}
