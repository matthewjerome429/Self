package com.cathaypacific.mbcommon.token;

public enum TokenCacheKeyEnum {
	
	LOGININFO("login_info"),
	/**TODO will remove from sprint 20*/
	@Deprecated
	UNLOCKEDPAX("unlocked_pax"),
	
	MASK_INFO("mask_info"),
	
	VALIDFLIGHTRLOCS("valid_flight_rlocs"),
	
	AEP_PRODUCTS("aep_products"),
	
	AEP_PRICES("aep_prices"),
	
	AEP_CART("aep_cart"),
	
	AEP_CART_TIMESTAMP("aep_cart_timestamp"),
	
	AEP_ORDER("aep_order"),
	
	AEP_PAYMENT("aep_payment"),
	
	AEP_PRICES_RECORD("aep_prices_record"),
	
	RLOC_MAPPING("rloc_mapping"),
	
	OLCI_SESSION_COOKIE("olci_session_cookie"),
	
	LIKEND_BOOKING("member_temp_linked_booking"),
	
	ADC_MESSAGE("adc_message"),
	
	INTER_ACTIVE_ERROR("inter_active_error"),
	
	OLSS_BOOKING_MODEL("olss_booking"),
	
	GET_CPR_SUCCESS("got_cpr"),
	
	CPR_JOURNEY_ID("cpr_journey_id"),
	
	/**
	 * below is token level cache keys, please note token cache key start with "cache" !!!!!!!!!!!!!!!!!
	 */
	PNR("cache_PNR_Retrieve"),
	
	PROFILE_SUMMARY("cache_profile_summary"),
	
	PROFILE_PREFERENCE("cache_profile_preference"),
	
	PROFILE_SOCIAL_ACCOUNT("cache_social_account"),
	
	ASIA_MILES("cache_asia_miles"),
	
	PROFILE_PREFERENCE_V2("cache_profile_preference_v2"),
	
	TICKET_PROCESS("cache_ticket_process"),
	
	DP_ELIGIBILITY_JOURNEY("cache_dp_eligibility_journey"),
	/**
	 *token level cache keys end, please note token cache key start with "cache" !!!!!!!!!!!!!!!!!
	 */

	TEMP_SEAT("temp_seat"),
	
	/** the seat payment saved in cache */
	SEAT_PAYMENT("cache_seat_payment"),

    PRESELECTEDMEAL_ELIGIBILITY("cache_preselectedmeal_eligibility"),
    
    // single menu
    PRESELECTEDMEAL_DESCRIPTION("cache_preselectedmeal_description"),

    // full menu
    PRESELECTEDMEAL_DETAIL("cache_preselectedmeal_detail");
	
	private String key;
	
	private TokenCacheKeyEnum(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
}
