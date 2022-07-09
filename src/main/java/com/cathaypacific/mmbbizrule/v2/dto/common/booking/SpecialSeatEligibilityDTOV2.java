package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import io.swagger.annotations.ApiModelProperty;

public class SpecialSeatEligibilityDTOV2 {
	
	/** eligible to select extra leg room seat */
	@ApiModelProperty("if the passenger is eligible to select or buy extra legroom seat")
	private Boolean exlSeat;
	
	/** eligible to select regular seat */
	@ApiModelProperty("if the passenger is eligible to select or buy asr seat")
	private Boolean asrSeat;
	
	/** eligible to select extra emergency exit row seat */
	@ApiModelProperty("if the passenger is eligible to select exit row seat")
	private Boolean exitRowSeat;
	
	/** eligible to select bulkhead seat with baby bassinet */
	@ApiModelProperty("if the passenger is eligible to select BCST seat")
	private Boolean bcstSeat;
	
	/** eligible to select unaccompanied minor seat */
	@ApiModelProperty("if the passenger is eligible to select UM seat")
	private Boolean umSeat;
	
	public SpecialSeatEligibilityDTOV2() {
		this.exlSeat = false;
		this.exitRowSeat = false;
		this.bcstSeat = false;
		this.umSeat = false;
		this.asrSeat = false;
	}

	public Boolean getExlSeat() {
		return exlSeat;
	}

	public void setExlSeat(Boolean exlSeat) {
		this.exlSeat = exlSeat;
	}

	public Boolean getExitRowSeat() {
		return exitRowSeat;
	}

	public void setExitRowSeat(Boolean exitRowSeat) {
		this.exitRowSeat = exitRowSeat;
	}

	public Boolean getBcstSeat() {
		return bcstSeat;
	}

	public void setBcstSeat(Boolean bcstSeat) {
		this.bcstSeat = bcstSeat;
	}

	public Boolean getUmSeat() {
		return umSeat;
	}

	public void setUmSeat(Boolean umSeat) {
		this.umSeat = umSeat;
	}

	public Boolean getAsrSeat() {
		return asrSeat;
	}

	public void setAsrSeat(Boolean asrSeat) {
		this.asrSeat = asrSeat;
	}

}
