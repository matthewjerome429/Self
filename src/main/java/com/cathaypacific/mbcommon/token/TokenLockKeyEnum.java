package com.cathaypacific.mbcommon.token;

public enum TokenLockKeyEnum {

	AEP_PRODUCTS("aep_products"),
	
	AEP_CART("aep_cart"),
	
	MMB_TEMP_SEAT("mmb_temp_seat");
	
	private String key;

	private TokenLockKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
}
