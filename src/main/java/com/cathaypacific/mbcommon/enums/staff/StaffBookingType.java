package com.cathaypacific.mbcommon.enums.staff;

import java.util.Objects;

/**
 * Concession Booking Type
 * 
 * @author zilong.bu
 *
 */
public enum StaffBookingType {

	INDUSTRY_DISCOUNT("ID"), AGENCY_DISCOUNT("AD"), DONATION_WINNER("GE"), INAUGURAL_JOURNALISTS("IG"),
	TOUR_CONDUCTOR("CG");

	private String code;

	private StaffBookingType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static StaffBookingType codeOf(String code){
		for (StaffBookingType staffBookingType :StaffBookingType.values()) {
			if(Objects.equals(staffBookingType.getCode(), code)){
				return staffBookingType;
			}
		}
		return null;
	}
}
