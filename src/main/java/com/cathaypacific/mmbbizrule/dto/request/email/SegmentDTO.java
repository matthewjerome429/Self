package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer the flight segment fields for 15below
 *
 * @author fangfang.zhang
 */
public class SegmentDTO implements Serializable {

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = -3494330639304686355L;

	/** the segmentSeq. */
	@ApiModelProperty(value = "segmentSeq", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String segmentSeq;

	/** the carrierCode. */
	private String carrierCode;

	/** the flightNo. */
	private String flightNo;

	/** the departureDate. */
	private String departureDate;

	/** the departAirport. */
	private String departAirport;

	/** the arrivalDate. */
	private String arrivalDate;

	/** the arrivalAirport. */
	private String arrivalAirport;

	/** the bookingClass. */
	private String bookingClass;

	/** the cabinClass. */
	private String cabinClass;

	public String getSegmentSeq() {
		return segmentSeq;
	}

	public void setSegmentSeq(String segmentSeq) {
		this.segmentSeq = segmentSeq;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartAirport() {
		return departAirport;
	}

	public void setDepartAirport(String departAirport) {
		this.departAirport = departAirport;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	@Override
	public String toString() {
		return "FlightSegmentDTO [carrierCode=" + carrierCode + ", flightNo=" + flightNo + ", departureDate="
				+ departureDate + ", departAirport=" + departAirport + ", arrivalDate=" + arrivalDate + ", arrivalAirport="
				+ arrivalAirport + ", bookingClass=" + bookingClass + ", cabinClass=" + cabinClass + "]";
	}

}
