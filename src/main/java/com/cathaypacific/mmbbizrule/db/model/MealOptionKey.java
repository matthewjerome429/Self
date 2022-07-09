package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Created by shane.tian.xia on 12/14/2017.
 */
@Embeddable
public class MealOptionKey implements Serializable {

	public MealOptionKey(String appCode, String origin, String destination, String cabinClass, String carrierCode) {
		super();
		this.appCode = appCode;
		this.origin = origin;
		this.destination = destination;
		this.cabinClass = cabinClass;
		this.carrierCode = carrierCode;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 218242263594600258L;

	@NotNull
    @Column(name="app_code", length = 5)
    private String appCode;

	@NotNull
    @Column(name = "origin")
    private String origin;

    @NotNull
    @Column(name = "destination")
    private String destination;

    @NotNull
    @Column(name = "cabin_class")
    private String cabinClass;

    @NotNull
    @Column(name = "carrier_code")
    private String carrierCode;
    
    public MealOptionKey(){

    }

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

}
