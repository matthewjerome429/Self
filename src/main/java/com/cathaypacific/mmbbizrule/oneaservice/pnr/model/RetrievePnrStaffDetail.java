package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;

public class RetrievePnrStaffDetail extends DataElement{
	
	private StaffBookingType type;
	
	private String priority;
	
	private String staffId;

	public StaffBookingType getType() {
		return type;
	}

	public void setType(StaffBookingType type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
	
}
