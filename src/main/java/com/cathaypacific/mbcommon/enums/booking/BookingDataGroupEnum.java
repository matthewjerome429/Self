package com.cathaypacific.mbcommon.enums.booking;

/**
 * TODO will remove lock logic from sprint 19
 * @author zilong.bu
 *
 */
@Deprecated
public enum BookingDataGroupEnum {

	PERSONAL_INFO("PI"),
	CONTACT_INFO("CI"),
	CONTACT_INFO_M("CIM"),
	CONTACT_INFO_E("CIE"),
	EMERGENCY_CONTACT("EC"),
	FREQUENTY_FLYER("FF"),
	PRIMARY_TRAVRLDOC("PT"),
	SECONDARY_TRAVRLDOC("ST"),
	DESTINAITION_ADDRESS("DA"),
	KTN_REDRESS("TS"),
	CONTACT_MOBILE("CM"),
	COUNTRY_OF_RESIDENCE("COR"),
	KTN("KTN"),
	REDRESS("REDRESS");
	
	private String code;
	private BookingDataGroupEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}

