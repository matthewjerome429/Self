package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDetail extends BaseResponse {

    private String carrierCode;

    private String flightNumber;

    private String flightDate;

    private String bookingClass;

    private List<FlightDetails> flightDetails;

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

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public List<FlightDetails> getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(List<FlightDetails> flightDetails) {
        this.flightDetails = flightDetails;
    }

    public void addFlightDetails(FlightDetails flightDetail) {
        if (CollectionUtils.isEmpty(this.flightDetails)) {
            this.flightDetails = new ArrayList<>();
        }
        this.flightDetails.add(flightDetail);
    }
}
