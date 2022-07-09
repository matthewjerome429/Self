package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class SpecialMealKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8418401861621036389L;
	
	@NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;

	@NotNull
	@Column(name = "meal_type")
	private String mealType;
	
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

	public SpecialMealKey(String appCode, String mealType, String origin, String destination, String cabinClass,
			String carrierCode) {
		super();
		this.appCode = appCode;
		this.mealType = mealType;
		this.origin = origin;
		this.destination = destination;
		this.cabinClass = cabinClass;
		this.carrierCode = carrierCode;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SpecialMealKey [appCode=");
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
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
		result = prime * result + ((cabinClass == null) ? 0 : cabinClass.hashCode());
		result = prime * result + ((carrierCode == null) ? 0 : carrierCode.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((mealType == null) ? 0 : mealType.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpecialMealKey other = (SpecialMealKey) obj;
		if (appCode == null) {
			if (other.appCode != null)
				return false;
		} else if (!appCode.equals(other.appCode))
			return false;
		if (cabinClass == null) {
			if (other.cabinClass != null)
				return false;
		} else if (!cabinClass.equals(other.cabinClass))
			return false;
		if (carrierCode == null) {
			if (other.carrierCode != null)
				return false;
		} else if (!carrierCode.equals(other.carrierCode))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (mealType == null) {
			if (other.mealType != null)
				return false;
		} else if (!mealType.equals(other.mealType))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}
    
}
