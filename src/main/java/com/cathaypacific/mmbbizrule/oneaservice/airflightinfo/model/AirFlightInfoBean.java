package com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model;

import java.math.BigDecimal;
import java.util.List;
import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;

/**
 * Created by shane.tian.xia on 12/26/2017.
 */
public class AirFlightInfoBean {

    private String carrierCode;
    private String flightNumber;
    private BigDecimal numberOfStops;
    private String totalDuration;
    private List<String> stops;
    private boolean isStopOverFlight;
    private AdditionalOperatorInfo additionalOperatorInfo;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public BigDecimal getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(BigDecimal numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public boolean isStopOverFlight() {
        return isStopOverFlight;
    }

    public void setStopOverFlight(boolean stopOverFlight) {
        isStopOverFlight = stopOverFlight;
    }

	public AdditionalOperatorInfo getAdditionalOperatorInfo() {
		return additionalOperatorInfo;
	}

	public void setAdditionalOperatorInfo(AdditionalOperatorInfo additionalOperatorInfo) {
		this.additionalOperatorInfo = additionalOperatorInfo;
	}
    
}
