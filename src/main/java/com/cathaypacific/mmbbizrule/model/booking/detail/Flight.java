package com.cathaypacific.mmbbizrule.model.booking.detail;

public class Flight{

	/** The carrier code. */
	private String carrierCode;

	/** The flight number. */
	private String flightNumber;

	/**
	 * Gets the carrier code.
	 *
	 * @return the carrierCode
	 */
	public final String getCarrierCode() {
		return carrierCode;
	}

	/**
	 * Sets the carrier code.
	 *
	 * @param inCarrierCode
	 *            the carrierCode to set
	 */
	public final void setCarrierCode(final String inCarrierCode) {
		this.carrierCode = inCarrierCode;
	}

	/**
	 * Gets the flight number.
	 *
	 * @return the flightNumber
	 */
	public final String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * Sets the flight number.
	 *
	 * @param inFlightNumber
	 *            the flightNumber to set
	 */
	public final void setFlightNumber(final String inFlightNumber) {
		this.flightNumber = inFlightNumber;
	}
}
