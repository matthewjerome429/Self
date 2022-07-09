package com.cathaypacific.mmbbizrule.model.booking.detail;

public enum SeatSelectionIneligibleReason {
	
	/**
	 * not operated by CX KA
	 */
	NON_CXKA,
	
	/**
	 * with disability
	 */
	DISABILITY,
	
	/**
	 * open to check-in
	 */
	CHECK_IN_OPEN,
	
	/**
	 * stand by
	 */
	STAND_BY,
	
	/**
	 * group booking
	 */
	GROUP_BOOKING,
	
	/**
	 * staff booking
	 */
	STAFF_BOOKING,
	
	/**
	 * ineligible cabin
	 */
	INELIGIBLE_CABIN,
	
	/**
	 * segment waitlisted
	 */
	SEGMENT_WAITLISTED,
	
	/** grmb **/
	MICE_GRMB,
	
	/** inhibit change seat according to olci response **/
    OLCI_INHIBIT_CHANGESEAT;

}
