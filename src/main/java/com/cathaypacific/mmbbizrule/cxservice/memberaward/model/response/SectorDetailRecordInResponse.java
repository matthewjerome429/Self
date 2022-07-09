package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response;

public class SectorDetailRecordInResponse {
	private String tier;
	
	private String carrierCode;

	private String flightDate;
	
	private String origin;
	
	private String destination;
	
	private String flightNum;
	
	private long basicAwardMiles;
	
	private long basicClubPoints;
	
	private long bonusMiles;
	
	private long tierBonusMiles;
	
	private long bonusClubPoints;
	
	private String bookingClass;
	
	private String fareClassGroup;
	
	private String pointsClass;
	
	private String errorCode;
	
	private String errorMessage;

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
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

	public String getFlightNum() {
		return flightNum;
	}

	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}

	public long getBasicAwardMiles() {
		return basicAwardMiles;
	}

	public void setBasicAwardMiles(long basicAwardMiles) {
		this.basicAwardMiles = basicAwardMiles;
	}

	public long getBasicClubPoints() {
		return basicClubPoints;
	}

	public void setBasicClubPoints(long basicClubPoints) {
		this.basicClubPoints = basicClubPoints;
	}

	public long getBonusMiles() {
		return bonusMiles;
	}

	public void setBonusMiles(long bonusMiles) {
		this.bonusMiles = bonusMiles;
	}

	public long getTierBonusMiles() {
		return tierBonusMiles;
	}

	public void setTierBonusMiles(long tierBonusMiles) {
		this.tierBonusMiles = tierBonusMiles;
	}

	public long getBonusClubPoints() {
		return bonusClubPoints;
	}

	public void setBonusClubPoints(long bonusClubPoints) {
		this.bonusClubPoints = bonusClubPoints;
	}

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}

	public String getFareClassGroup() {
		return fareClassGroup;
	}

	public void setFareClassGroup(String fareClassGroup) {
		this.fareClassGroup = fareClassGroup;
	}

	public String getPointsClass() {
		return pointsClass;
	}

	public void setPointsClass(String pointsClass) {
		this.pointsClass = pointsClass;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
