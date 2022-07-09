package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;
import java.util.Date;

import com.cathaypacific.mmbbizrule.constant.FlightStatusConstants;

public class FlightStatusRequestDTO implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -8200966055242135436L;

	/** The carrier code. */
	private String carrierCode;

	/** The flight number. */
	private String flightNumber;

	/** The travel date. */
	private Date travelDate;

	/** The origin. */
	private String origin;

	/** The destination. */
	private String destination;

	/** The locale. */
	private String locale = FlightStatusConstants.REQUEST_DEFAULT_LOCALE;

	/** The departure arrival. */
	private String departureArrival;

	/** The is flight messaging. */
	private Boolean isFlightMessaging = Boolean.FALSE;

	/**
	 * Gets the carrier code.
	 *
	 * @return the carrier code
	 */
	public final String getCarrierCode() {
		return carrierCode;
	}

	/**
	 * Sets the carrier code.
	 *
	 * @param carrierCode
	 *            the new carrier code
	 */
	public final void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	/**
	 * Gets the flight number.
	 *
	 * @return the flight number
	 */
	public final String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * Sets the flight number.
	 *
	 * @param flightNumber
	 *            the new flight number
	 */
	public final void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	/**
	 * Gets the travel date.
	 *
	 * @return the travel date
	 */
	public final Date getTravelDate() {
		return travelDate;
	}

	/**
	 * Sets the travel date.
	 *
	 * @param travelDate
	 *            the new travel date
	 */
	public final void setTravelDate(Date travelDate) {
		this.travelDate = travelDate;
	}

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public final String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 *
	 * @param origin
	 *            the new origin
	 */
	public final void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public final String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination
	 *            the new destination
	 */
	public final void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public final String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale
	 *            the new locale
	 */
	public final void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Gets the departure arrival.
	 *
	 * @return the departure arrival
	 */
	public final String getDepartureArrival() {
		return departureArrival;
	}

	/**
	 * Sets the departure arrival.
	 *
	 * @param departureArrial
	 *            the new departure arrival
	 */
	public final void setDepartureArrival(String departureArrial) {
		this.departureArrival = departureArrial;
	}

	/**
	 * Gets the checks if is flight messaging.
	 *
	 * @return the checks if is flight messaging
	 */
	public Boolean isFlightMessaging() {
		return isFlightMessaging;
	}

	/**
	 * Sets the checks if is flight messaging.
	 *
	 * @param isFlightMessaging
	 *            the new checks if is flight messaging
	 */
	public void setFlightMessaging(Boolean isFlightMessaging) {
		this.isFlightMessaging = isFlightMessaging;
	}

}
