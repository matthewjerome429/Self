package com.cathaypacific.mmbbizrule.model.booking.detail;

public class SpecialSeatEligibility {
	
	/** eligible to select extra leg room seat */
	private Boolean exlSeat;
	
	/** eligible to select regular seat */
	private Boolean asrSeat;
	
	/** eligible to select extra emergency exit row seat */
	private Boolean exitRowSeat;
	
	/** eligible to select bulkhead seat with baby bassinet */
	private Boolean bcstSeat;
	
	/** eligible to select unaccompanied minor seat */
	private Boolean umSeat;

	public SpecialSeatEligibility() {
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
