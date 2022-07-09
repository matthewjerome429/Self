package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_special_meal_rule")
@IdClass(SpecialMealKey.class)
public class SpecialMealModel {
	@Id
    @NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;

	@Id
    @NotNull
    @Column(name = "meal_type")
    private String mealType;

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
    
    @NotNull
    @Column(name = "enable")
    private boolean enable;
    
    @NotNull
    @Column(name = "consent")
    private boolean consent;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SpecialMealModel [appCode=");
		builder.append(appCode);
		builder.append(", mealType=");
		builder.append(mealType);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", destination=");
		builder.append(destination);
		builder.append(", cabinClass=");
		builder.append(cabinClass);
		builder.append(", carrierCode=");
		builder.append(carrierCode);
		builder.append(", lastUpdateSource=");
		builder.append(lastUpdateSource);
		builder.append(", lastUpdateTimeStamp=");
		builder.append(lastUpdateTimeStamp);
		builder.append(", enable=");
		builder.append(enable);
		builder.append(", consent=");
		builder.append(consent);
		builder.append("]");
		return builder.toString();
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
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

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
