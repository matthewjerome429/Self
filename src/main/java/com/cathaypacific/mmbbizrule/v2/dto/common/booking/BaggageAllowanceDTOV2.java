package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class BaggageAllowanceDTOV2 implements Serializable {

	private static final long serialVersionUID = -5582509546988517984L;

	@ApiModelProperty(value = "Flag of all break down value has been retrieved", required = true)
	private boolean completed;
	
	@ApiModelProperty(value = "Flag to indicate if cabin baggage is not available due to the specified reasons", required = true)
	private boolean cabinBaggageUnavailable;

	private CheckInBaggageDTOV2 checkInBaggage;

	private CabinBaggageDTOV2 cabinBaggage;

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public CheckInBaggageDTOV2 getCheckInBaggage() {
		return checkInBaggage;
	}

	public CheckInBaggageDTOV2 findCheckInBaggage() {
		if (checkInBaggage == null) {
			checkInBaggage = new CheckInBaggageDTOV2();
		}
		return checkInBaggage;
	}

	public void setCheckInBaggage(CheckInBaggageDTOV2 checkInBaggage) {
		this.checkInBaggage = checkInBaggage;
	}

	public CabinBaggageDTOV2 getCabinBaggage() {
		return cabinBaggage;
	}

	public CabinBaggageDTOV2 findCabinBaggage() {
		if (cabinBaggage == null) {
			cabinBaggage = new CabinBaggageDTOV2();
		}
		return cabinBaggage;
	}

	public void setCabinBaggage(CabinBaggageDTOV2 cabinBaggage) {
		this.cabinBaggage = cabinBaggage;
	}

	public boolean isCabinBaggageUnavailable() {
		return cabinBaggageUnavailable;
	}

	public void setCabinBaggageUnavailable(boolean cabinBaggageUnavailable) {
		this.cabinBaggageUnavailable = cabinBaggageUnavailable;
	}

	
}
