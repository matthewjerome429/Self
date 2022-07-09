package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirActivityRecord {

    private String source;

    private String promotionCode;

    private String flightType;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String activityDate;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String creditingDate;

    private String airlineCode;

    private String flightNumber;

    private String bookingClass;

    private String flownClass;

    private String origin;

    private String destination;

    private String travelDebitCredit;

    private String awardTotalPoints;

    private String awardBasicPoints;

    private String awardBonusPoints;

    private String suspenseStatus;

    private String totalMpoClubPts;

    private String basicMpoClubPts;

    private String bonusMpoClubPts;

    private String rejectShortDescription;

    private String rejectCode;

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(final String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(final String flightType) {
        this.flightType = flightType;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(final String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(final String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(final String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getFlownClass() {
        return flownClass;
    }

    public void setFlownClass(final String flownClass) {
        this.flownClass = flownClass;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public String getTravelDebitCredit() {
        return travelDebitCredit;
    }

    public void setTravelDebitCredit(final String travelDebitCredit) {
        this.travelDebitCredit = travelDebitCredit;
    }

    public String getAwardTotalPoints() {
        return awardTotalPoints;
    }

    public void setAwardTotalPoints(final String awardTotalPoints) {
        this.awardTotalPoints = awardTotalPoints;
    }

    public String getAwardBasicPoints() {
        return awardBasicPoints;
    }

    public void setAwardBasicPoints(final String awardBasicPoints) {
        this.awardBasicPoints = awardBasicPoints;
    }

    public String getAwardBonusPoints() {
        return awardBonusPoints;
    }

    public void setAwardBonusPoints(final String awardBonusPoints) {
        this.awardBonusPoints = awardBonusPoints;
    }

    public String getSuspenseStatus() {
        return suspenseStatus;
    }

    public void setSuspenseStatus(final String suspenseStatus) {
        this.suspenseStatus = suspenseStatus;
    }

    public String getTotalMpoClubPts() {
        return totalMpoClubPts;
    }

    public void setTotalMpoClubPts(final String totalMpoClubPts) {
        this.totalMpoClubPts = totalMpoClubPts;
    }

    public String getBasicMpoClubPts() {
        return basicMpoClubPts;
    }

    public void setBasicMpoClubPts(final String basicMpoClubPts) {
        this.basicMpoClubPts = basicMpoClubPts;
    }

    public String getBonusMpoClubPts() {
        return bonusMpoClubPts;
    }

    public void setBonusMpoClubPts(final String bonusMpoClubPts) {
        this.bonusMpoClubPts = bonusMpoClubPts;
    }

    public String getRejectShortDescription() {
        return rejectShortDescription;
    }

    public void setRejectShortDescription(final String rejectShortDescription) {
        this.rejectShortDescription = rejectShortDescription;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(final String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getCreditingDate() {
		return creditingDate;
	}

	public void setCreditingDate(String creditingDate) {
		this.creditingDate = creditingDate;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AirActivityRecord{");
        sb.append("source='").append(source).append('\'');
        sb.append(", promotionCode='").append(promotionCode).append('\'');
        sb.append(", flightType='").append(flightType).append('\'');
        sb.append(", activityDate='").append(activityDate).append('\'');
        sb.append(", creditingDate='").append(creditingDate).append('\'');
        sb.append(", airlineCode='").append(airlineCode).append('\'');
        sb.append(", flightNumber='").append(flightNumber).append('\'');
        sb.append(", bookingClass='").append(bookingClass).append('\'');
        sb.append(", flownClass='").append(flownClass).append('\'');
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", travelDebitCredit='").append(travelDebitCredit).append('\'');
        sb.append(", awardTotalPoints='").append(awardTotalPoints).append('\'');
        sb.append(", awardBasicPoints='").append(awardBasicPoints).append('\'');
        sb.append(", awardBonusPoints='").append(awardBonusPoints).append('\'');
        sb.append(", suspenseStatus='").append(suspenseStatus).append('\'');
        sb.append(", totalMpoClubPts='").append(totalMpoClubPts).append('\'');
        sb.append(", basicMpoClubPts='").append(basicMpoClubPts).append('\'');
        sb.append(", bonusMpoClubPts='").append(bonusMpoClubPts).append('\'');
        sb.append(", rejectShortDescription='").append(rejectShortDescription).append('\'');
        sb.append(", rejectCode='").append(rejectCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
