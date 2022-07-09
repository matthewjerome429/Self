package com.cathaypacific.mmbbizrule.model.booking.detail;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;

public class SegmentStatus {
	
	private String pnrStatus;
    
    /** flight status*/
    private FlightStatusEnum status;
    
    /** booking is display only */
    private boolean disable;
    
    private boolean flown;

	public String getPnrStatus() {
		return pnrStatus;
	}

	public void setPnrStatus(String pnrStatus) {
		this.pnrStatus = pnrStatus;
	}

	public FlightStatusEnum getStatus() {
		return status;
	}

	public void setStatus(FlightStatusEnum status) {
		this.status = status;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isFlown() {
		return flown;
	}
	public void setFlown(boolean flown) {
		this.flown = flown;
	}
	

}
