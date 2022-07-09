package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Embeddable
public class PassengerTypeKey implements Serializable{

	private static final long serialVersionUID = -4859004559896567832L;
	
	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "pnr_passenger_type")
	private String pnrPassengerType;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getPnrPassengerType() {
		return pnrPassengerType;
	}

	public void setPnrPassengerType(String pnrPassengerType) {
		this.pnrPassengerType = pnrPassengerType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
		result = prime * result + ((pnrPassengerType == null) ? 0 : pnrPassengerType.hashCode());
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
		PassengerTypeKey other = (PassengerTypeKey) obj;
		if (appCode == null) {
			if (other.appCode != null)
				return false;
		} else if (!appCode.equals(other.appCode))
			return false;
		if (pnrPassengerType == null) {
			if (other.pnrPassengerType != null)
				return false;
		} else if (!pnrPassengerType.equals(other.pnrPassengerType))
			return false;
		return true;
	}
	
}
