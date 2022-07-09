package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import javax.validation.constraints.NotNull;

public class MealDescriptionRequestInfo {

    @NotNull
    private String carrierCode;

    @NotNull
    private String flightNumber;

    // format: DDMMYYYY
    @NotNull
    private String flightDate;

    @NotNull
    private String origin;

    @NotNull
    private String destination;

    @NotNull
    private String servicePscode;

    @NotNull
    private String dishPscode;

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

    public String getServicePscode() {
        return servicePscode;
    }

    public void setServicePscode(String servicePscode) {
        this.servicePscode = servicePscode;
    }

    public String getDishPscode() {
        return dishPscode;
    }

    public void setDishPscode(String dishPscode) {
        this.dishPscode = dishPscode;
    }

}
