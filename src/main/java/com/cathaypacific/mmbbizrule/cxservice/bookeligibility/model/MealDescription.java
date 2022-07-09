package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDescription extends BaseResponse {

    private String carrierCode;

    private String flightNumber;

    private String flightDate;

    private String origin;

    private String destination;

    private String servicePscode;

    private String dishPscode;

    private DishName dishName;

    private List<String> tag;

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

    public DishName getDishName() {
        return dishName;
    }

    public void setDishName(DishName dishName) {
        this.dishName = dishName;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public void addTag(String t) {
        if (CollectionUtils.isEmpty(this.tag)) {
            this.tag = new ArrayList<>();
        }
        this.tag.add(t);
    }
}
