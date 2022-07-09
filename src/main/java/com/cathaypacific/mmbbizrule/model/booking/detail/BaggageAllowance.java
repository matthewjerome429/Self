package com.cathaypacific.mmbbizrule.model.booking.detail;

public class BaggageAllowance {

	/**
	 * 	If baggage allowance API doesn't return data of matched member tier, completed flag is false as default value.<br>
	 *  If baggage allowance API returns data of matched member tier, set completed flag to true as initiated value.<br>
	 *  If any break down baggage allowance is not found, set completed flag to false as final value.
	 */
	private boolean completed;
	
	/**
	 * The cabin baggage must be unavailable if 
	 * (1) group/MICE/staff booking and
	 * (2) ticket stock not 160/043 and
	 * (3) flight within STD-24hrs
	 * 
	 * This indicator is used in front-end behavior.
	 * Please note that if not fulfill one of them, doesn't mean cabin baggage is available.
	 * In simple word,
	 *  (1) if cabinBaggageUnavailable is true, means cabinBaggage is null
	 *  (2) if cabinBaggageUnavailable is false, means expected cabinBaggage is not null, but it still can be null. 
	 */
	private boolean cabinBaggageUnavailable;

	private CheckInBaggage checkInBaggage;

	private CabinBaggage cabinBaggage;

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public CheckInBaggage getCheckInBaggage() {
		return checkInBaggage;
	}

	public CheckInBaggage findCheckInBaggage() {
		if (checkInBaggage == null) {
			checkInBaggage = new CheckInBaggage();
		}
		return checkInBaggage;
	}

	public void setCheckInBaggage(CheckInBaggage checkInBaggage) {
		this.checkInBaggage = checkInBaggage;
	}

	public CabinBaggage getCabinBaggage() {
		return cabinBaggage;
	}

	public CabinBaggage findCabinBaggage() {
		if (cabinBaggage == null) {
			cabinBaggage = new CabinBaggage();
		}
		return cabinBaggage;
	}

	public void setCabinBaggage(CabinBaggage cabinBaggage) {
		this.cabinBaggage = cabinBaggage;
	}

	public boolean isCabinBaggageUnavailable() {
		return cabinBaggageUnavailable;
	}

	public void setCabinBaggageUnavailable(boolean cabinBaggageUnavailable) {
		this.cabinBaggageUnavailable = cabinBaggageUnavailable;
	}

	
}
