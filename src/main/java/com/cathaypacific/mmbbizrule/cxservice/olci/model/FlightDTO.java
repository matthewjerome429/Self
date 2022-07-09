package com.cathaypacific.mmbbizrule.cxservice.olci.model;

public class FlightDTO {

	private String productIdentifierDID;
	
	private String operateFlightNumber;
	
	private String operateCompany;
	
	private String marketFlightNumber;
	
	private String marketingCompany;
	
	private ADTimeDTO departureTime;
	
	private ADTimeDTO arrivalTime;
	
	private String originPort;
	
	private String destPort;

	public String getProductIdentifierDID() {
		return productIdentifierDID;
	}

	public void setProductIdentifierDID(String productIdentifierDID) {
		this.productIdentifierDID = productIdentifierDID;
	}

	public String getOperateFlightNumber() {
		return operateFlightNumber;
	}

	public void setOperateFlightNumber(String operateFlightNumber) {
		this.operateFlightNumber = operateFlightNumber;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public String getMarketFlightNumber() {
		return marketFlightNumber;
	}

	public void setMarketFlightNumber(String marketFlightNumber) {
		this.marketFlightNumber = marketFlightNumber;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}

	public ADTimeDTO getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(ADTimeDTO departureTime) {
		this.departureTime = departureTime;
	}

	public ADTimeDTO getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(ADTimeDTO arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	
}
