package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class MealDetailRequestInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8360876630026595451L;
    private String carrierCode;
    private String flightNumber;
    private String flightDate;
    private String origin;
    private String destination;
    private String bookingClass;

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

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

}
