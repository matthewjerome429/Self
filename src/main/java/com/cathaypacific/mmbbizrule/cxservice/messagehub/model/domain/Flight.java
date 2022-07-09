package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain;

public class Flight {

	private String qualifier;
	
	private int number;
	
	private String origin;
	
	private String destination;
	
	private String depDateTime;
	
	private String arrDateTime;
	
	private String mktCarrier;
	
	private String mktFlightNo;
	
	private String mktBookingClass;

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepDateTime() {
		return depDateTime;
	}

	public void setDepDateTime(String depDateTime) {
		this.depDateTime = depDateTime;
	}

	public String getArrDateTime() {
		return arrDateTime;
	}

	public void setArrDateTime(String arrDateTime) {
		this.arrDateTime = arrDateTime;
	}

	public String getMktCarrier() {
		return mktCarrier;
	}

	public void setMktCarrier(String mktCarrier) {
		this.mktCarrier = mktCarrier;
	}

	public String getMktFlightNo() {
		return mktFlightNo;
	}

	public void setMktFlightNo(String mktFlightNo) {
		this.mktFlightNo = mktFlightNo;
	}

	public String getMktBookingClass() {
		return mktBookingClass;
	}

	public void setMktBookingClass(String mktBookingClass) {
		this.mktBookingClass = mktBookingClass;
	}
	
}
