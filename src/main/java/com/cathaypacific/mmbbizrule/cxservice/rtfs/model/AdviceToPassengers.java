package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;

public class AdviceToPassengers implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = -7263011126599885490L;

	/** The message. */
	private String message;

	/** The arrival departure. */
	private String arrivalDeparture;

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param inMessage
	 *            the message to set
	 */
	public final void setMessage(final String inMessage) {
		this.message = inMessage;
	}

	/**
	 * Gets the arrival departure.
	 *
	 * @return the arrivalDeparture
	 */
	public final String getArrivalDeparture() {
		return arrivalDeparture;
	}

	/**
	 * Sets the arrival departure.
	 *
	 * @param inArrivalDeparture
	 *            the arrivalDeparture to set
	 */
	public final void setArrivalDeparture(final String inArrivalDeparture) {
		this.arrivalDeparture = inArrivalDeparture;
	}
}
