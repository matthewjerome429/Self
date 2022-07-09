package com.cathaypacific.mmbbizrule.dto.common.booking;

import io.swagger.annotations.ApiModelProperty;

public class BaggageAllowanceDTO {

	@ApiModelProperty(value = "Flag of all break down value has been retrieved", required = true)
	private boolean completed;
	
	@ApiModelProperty(value = "Flag to indicate if cabin baggage is not available due to the specified reasons", required = true)
	private boolean cabinBaggageUnavailable;

	private CheckInBaggageDTO checkInBaggage;

	private CabinBaggageDTO cabinBaggage;

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public CheckInBaggageDTO getCheckInBaggage() {
		return checkInBaggage;
	}

	public CheckInBaggageDTO findCheckInBaggage() {
		if (checkInBaggage == null) {
			checkInBaggage = new CheckInBaggageDTO();
		}
		return checkInBaggage;
	}

	public void setCheckInBaggage(CheckInBaggageDTO checkInBaggage) {
		this.checkInBaggage = checkInBaggage;
	}

	public CabinBaggageDTO getCabinBaggage() {
		return cabinBaggage;
	}

	public CabinBaggageDTO findCabinBaggage() {
		if (cabinBaggage == null) {
			cabinBaggage = new CabinBaggageDTO();
		}
		return cabinBaggage;
	}

	public void setCabinBaggage(CabinBaggageDTO cabinBaggage) {
		this.cabinBaggage = cabinBaggage;
	}

	public boolean isCabinBaggageUnavailable() {
		return cabinBaggageUnavailable;
	}

	public void setCabinBaggageUnavailable(boolean cabinBaggageUnavailable) {
		this.cabinBaggageUnavailable = cabinBaggageUnavailable;
	}

	
}
