package com.cathaypacific.mbcommon.enums.booking;

public enum BookingSources {
	
	/**
	 * Add booking by CUST, the booking cannot return from eods immediately
	 */
	ADD_EODS_BOOKING_TEMP_LINK,
	
	/**
	 * Member profile, EODS or 1A 
	 */
	MEMBER_PROFILE,
	
	/**
	 * Linked booking, e.g. temp display booking
	 */
	EXTERNAL_LINK,
	
	/**
	 * After RU enrollment, add booking by CUST, the booking cannot return from eods immediately
	 */
	RU_ENROL;
	
}
