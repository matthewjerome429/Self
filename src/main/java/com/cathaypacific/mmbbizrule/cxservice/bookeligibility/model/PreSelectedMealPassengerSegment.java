package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import com.cathaypacific.mmbbizrule.model.booking.detail.MealDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealSelection;

public class PreSelectedMealPassengerSegment {

    private String passengerId;
    // E PETER
    private String firstname;
    // TEST
    private String lastname;
    private String segmentId;
    // flight (470)
    private String flightNumber;
    // flightDate (2019-12-02)
    private String flightDate;
    // STD in local time (YYYY-MM-DD HH:mm:ss)
    private String stdLocal;
    // STD in GMT+0 (2019-12-02T01:45:00Z)
    private String stdGmt;
    // origin airport (HKG)
    private String origin;
    // destination airport (TPE)
    private String destination;

    private String cabinClass;
    // CX
    private String carrierCode;

    private MealDetail meal;

    private MealSelection mealSelection;

    private boolean flown;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public MealDetail getMeal() {
        return meal;
    }

    public void setMeal(MealDetail meal) {
        this.meal = meal;
    }

    public MealSelection getMealSelection() {
        return mealSelection;
    }

    public void setMealSelection(MealSelection mealSelection) {
        this.mealSelection = mealSelection;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lasename) {
        this.lastname = lasename;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getStdLocal() {
        return stdLocal;
    }

    public void setStdLocal(String stdLocal) {
        this.stdLocal = stdLocal;
    }

    public String getStdGmt() {
        return stdGmt;
    }

    public void setStdGmt(String stdGmt) {
        this.stdGmt = stdGmt;
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

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

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

    public boolean isFlown() {
        return flown;
    }

    public void setFlown(boolean flown) {
        this.flown = flown;
    }

}
