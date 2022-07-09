package com.cathaypacific.mmbbizrule.dto.response.baggage;

public enum IneligibleReasonEnum {

	/**
	 * Default reason when no matched criteria.
	 */
	DEFAULT,

	/**
	 * Product is sold on offline on the passenger segment association.
	 */
	SELL_OFFLINE,

	/**
	 * Maximum product value has been purchased on the passenger segment association.
	 */
	MAX_REACHED,

	/**
	 * Group booking.
	 */
	GROUP_BOOKING,

	/**
	 * Staff booking.
	 */
	STAFF_BOOKING,

	/**
	 * Booking contains flight operated by neither CX nor KA.
	 */
	NON_CX_KA_OP_FLIGHT,

	/**
	 * Passenger has wavier baggage.
	 */
	WAVIER_BAGGAGE,
	
	/**
	 * Segment is waitlisted.
	 */
	WAITLIST,

	/**
	 * E-ticket is not issued on the passenger segment association.
	 */
	TICKET_NOT_ISSUED,

	/**
	 * E-ticket is neither CX nor KA on the passenger segment association.
	 */
	NON_CX_KA_ETICKET,

	/**
	 * Segment belongs to a journey that is too close to departure time (first segment within 24 hours).
	 */
	CLOSE_TO_DEPARTURE;
}
