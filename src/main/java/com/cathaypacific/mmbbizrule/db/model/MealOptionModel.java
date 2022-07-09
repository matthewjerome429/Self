package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.cathaypacific.mmbbizrule.constant.MealOption;

/**
 * Created by shane.tian.xia on 12/14/2017.
 */
@Entity
@Table(name="tb_meal_ineligibility")
@IdClass(MealOptionKey.class)
public class MealOptionModel {

    @Id
    @NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;

    @NotNull
    @Column(name = "meal_option")
    @Enumerated(EnumType.STRING)
    private MealOption mealOption;


    @Id
    @NotNull
    @Column(name = "origin")
    private String origin;

    @Id
    @NotNull
    @Column(name = "destination")
    private String destination;

    @Id
    @NotNull
    @Column(name = "cabin_class")
    private String cabinClass;
    
    @Id
    @NotNull
    @Column(name = "carrier_code")
    private String carrierCode;
    
    @NotNull
    @Column(name = "last_update_source")
    private String lastUpdateSource;

    @NotNull
    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

    public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

    public MealOption getMealOption() {
		return mealOption;
	}

	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
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

	public String getLastUpdateSource() {
        return lastUpdateSource;
    }

    public void setLastUpdateSource(String lastUpdateSource) {
        this.lastUpdateSource = lastUpdateSource;
    }

    public Date getLastUpdateTimeStamp() {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }
}
