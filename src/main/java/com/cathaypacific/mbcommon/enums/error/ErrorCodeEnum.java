package com.cathaypacific.mbcommon.enums.error;

public enum ErrorCodeEnum {

	ERR_SYSTEM("E20000001",ErrorTypeEnum.SYSERROR,"ERR_SYSTEM_001","Unkonw technology error."),
	ERR_BUSSINESS_UNKNOW("E23000001",ErrorTypeEnum.SYSERROR,"ERR_MMB_100","Unkonw business error."),
	ERR_NO_UPCOMING_CXKA_FLIGHT("E23000002",ErrorTypeEnum.BUSERROR,"ERR_MMB_100","Cannot find any upcoming sector operated or marketed by CX/KA"),
	ERR_HTTP_READ_TIME_OUT("E23000005", ErrorTypeEnum.SYSERROR, "", "Http socket timed out."),
	ERR_PREFIX_SOAP_FAULT("E21",ErrorTypeEnum.BUSERROR,"ERR_MMB_100","Soap fault error."),
	ERR_JDBC("E20100002",ErrorTypeEnum.SYSERROR,"ERR_SYSTEM_001","DB error."),
	ERR_TOKEN_INVALID("E23100000",ErrorTypeEnum.BUSERROR,"ERR_MMB_100","Invalid token."),
	ERR_NOT_AUTHORIZED("E23100001",ErrorTypeEnum.BUSERROR,"ERR_MMB_100","No permission of request booking/event."),
	ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN("E23Z00113",ErrorTypeEnum.BUSERROR,"ERR_MMB_100","Cannot find request booking."),
	@Deprecated
	/**
	 * unlockend has bean removed form MMB
	 */
	ERR_UNLOCK_INVALID_TICKET("E23Z00100",ErrorTypeEnum.BUSERROR,"",null),
	@Deprecated
	/**
	 * unlockend has bean removed form MMB
	 */
	ERR_UNLOCK_INFANT_TICKET("E23Z00101",ErrorTypeEnum.BUSERROR,"",null),
	ERR_SEAT_MAP_NOT_FOUND("E23100007", ErrorTypeEnum.BUSERROR,"","Cannot found seat map."),
	ERR_LOGIN_NAME_NOT_MATCH("E23Z00121",ErrorTypeEnum.BUSERROR,"ERR_MMB_020","Cannot find request passenger in booking."),
	ERR_MULTI_PAX_FOUND("E23Z00122",ErrorTypeEnum.REMINDER,"ERR_MMB_020","MULTI passenger matched."),
	ERR_INFANT_TICKET_LOGIN("E23Z00123",ErrorTypeEnum.BUSERROR,"ERR_MMB_020","Cannot parser booking with infant ticket."),
	ERR_INF_WITHOUT_PARENT("E23Z00124", ErrorTypeEnum.BUSERROR, "","Invalid infant found in booking."),
	ERR_MINI_PNR_LOGIN("E23Z00125", ErrorTypeEnum.BUSERROR, "", "Block mini(PNR) booking."),
	
	ERR_MLC_TOKEN_INVALID("1002",ErrorTypeEnum.BUSERROR,"", "Invalid mlc token."),
	ERR_ALL_FLIGHT_FLOWN_BEFOR_LIMIT_TIME("E23Z00151",ErrorTypeEnum.BUSERROR,"ERR_MMB_019", "All flight flown and not in MMB login window."),
	ERR_ALL_HOTEL_EXPIRED_BEFOR_LIMIT_TIME("E23Z00153",ErrorTypeEnum.BUSERROR,"", "All hotel/event expired."),
	ERR_FILTER_INELIGIBLE_BOOKING_STATUS("E23Z00221",ErrorTypeEnum.BUSERROR,"ERR_MMB_019", "Invalid flight status."),
	WARN_MEMBER_BOOKING_NOTFOUND("W23100003",ErrorTypeEnum.WARNING,"", "No booking of member."),
	WARN_SPNR_BOOKING_NOTFOUND("E23100004",ErrorTypeEnum.WARNING,"", "The package booking not found."),
	/**R1 not support staff/group/mice booking*/
	ERR_STAFF_GROUP_BOOKING_NOT_SUPPORT("E23Z00152",ErrorTypeEnum.BUSERROR,"ERR_MMB_015", "Group booking."),
	ERR_INCOMPLETE_REDEMPTION_BOOKING("E23Z00114",ErrorTypeEnum.BUSERROR,"ERR_MMB_021", "Incomplete redemption."),
	ERR_SEATOCCUPIED("E23Z00115",ErrorTypeEnum.BUSERROR,"", "The seat occupied."),
	ERR_INELIGIBLE_TO_UPDATE_SEAT("E23Z00116",ErrorTypeEnum.BUSERROR,"", "Ineligible to update seat."),
    /** mice without ticket can not login */
    ERR_MICE_BOOKING_WITHOUT_TICKET("E23Z00154",ErrorTypeEnum.BUSERROR,"", "mice without ticket can not login"),
    /** GRMC can not login **/
    ERR_MICE_BOOKING_GRMC_DENY("E23Z00155",ErrorTypeEnum.BUSERROR,"", "GRMC can not login"),
    /** GRMC can not login **/
    ERR_ADDBOOKING_MICE_NAME_MISMATCH("E23Z00156",ErrorTypeEnum.BUSERROR,"", "to be added mice booking name is not matched with profile name"),
	
	/**error airlineCode*/
	ERR_AIRLINECODE("E23000003",ErrorTypeEnum.BUSERROR,"", "Cannot retrieve CX or KA airline"),
	
	/**Non-six digits Rloc*/
	ERR_NON_SIXDIGITS_RLOC("E00000004", ErrorTypeEnum.BUSERROR,"", "Not flight sixdigits rloc"),
	
	/**These messages is only used in back end*/
	WARN_1A_CONN_FAILED("W23100100", ErrorTypeEnum.Inner, "", "OneA connect failed"),
	WARN_OJ_CONN_FAILED("W23100101", ErrorTypeEnum.Inner, "", "OJ connect failed"),
	WARN_EODS_CONN_FAILED("W23100102", ErrorTypeEnum.Inner, "", "EODS connect failed"),

	/** cancel booking error code**/
	WARN_BLOCK_CANNEL_GROUP("W23C00001",ErrorTypeEnum.WARNING,"", "group/mice/staff booking cannot cancel booking"),
	WARN_BLOCK_CANNEL_NOTDIRECT("W23C00002",ErrorTypeEnum.WARNING,"", "Not cancel-booking-white-list booking and not member redemention non fqtu booking"),
	WARN_BLOCK_CANNEL_NOTFLIHTONLY("W23C00003",ErrorTypeEnum.WARNING,"", "Not flight only booking"),
	WARN_BLOCK_CANNEL_CREATETIME("W23C00004",ErrorTypeEnum.WARNING,"", "Booking not in create time 30 mins"),
	WARN_BLOCK_CANNEL_FQUG("W23C00005",ErrorTypeEnum.WARNING,"", null),
	WARN_BLOCK_CANNEL_CHECKEDIN("W23C00006",ErrorTypeEnum.WARNING,"", "Checked in booking cannot cancel booking"),
	WARN_BLOCK_CANNEL_FLOWN("W23C00007",ErrorTypeEnum.WARNING,"", "Booking contains flown segment"),
	WARN_BLOCK_CANNEL_UPGRADE("W23C00008",ErrorTypeEnum.WARNING,"", "Booking has fqtu"),
	WARN_BLOCK_CANNEL_REDEMPTION("W23C00009",ErrorTypeEnum.WARNING,"", "Redemption booking and not member login"),
	WARN_BLOCK_CANNEL_ONHOLD_NON_US("W23C00010",ErrorTypeEnum.WARNING,"", "On hold booking and not USA site"),
	WARN_BLOCK_CANNEL_ONHOLD_US("W23C00011",ErrorTypeEnum.WARNING,"", "On hold booking and USA site"),
	
	//Cancel/refund booking fail, but cpnStatus changed to refund success. 
	ERR_CANCAL_FAIL_BUT_TICKET_STATUS_SUCCESS("E23C00012",ErrorTypeEnum.BUSERROR,"", "Booking cannot be cancel and retry not work"),
	
	/** cancel booking refund error code */
	ERR_BLOCK_REFUND_TICKETING_DATE_NOTFOUND("E20220001", ErrorTypeEnum.BUSERROR, "", "Cancel booking ticketIssuedDate is empty"),
	ERR_BLOCK_REFUND_TICKET_ISSUED_INVAILD_TIME("E20220002", ErrorTypeEnum.BUSERROR, "", "Cancelbooking flow not within 24hrs after ticket is issued"),
	ERR_BLOCK_REFUND_7_DAYS_LESS_THAN_FIRST_SEGMENT_STD("E20220003", ErrorTypeEnum.BUSERROR, "", "Cancel booking flow < 7 days from STD of 1st sector"),
	ERR_BLOCK_REFUND_VAILD_TICKET_COUPON_STATUS_NOTFOUND("E20220004", ErrorTypeEnum.BUSERROR, "", "ET coupon status not marked as I or AL"),
	ERR_BLOCK_REFUND_NOREDEMPTION_OR_REDEMPTION_HAVE_FQTU("E20220005", ErrorTypeEnum.BUSERROR, "", "Cancel booking is redemption but has no FQTU"),
	ERR_BLOCK_REFUND_VAILD_BOOKINGSITE_NOTFOUND("E20220006", ErrorTypeEnum.BUSERROR, "", "Cancel booking has no OSI YY BOOKING SITE US-CX element"),
	ERR_BLOCK_REFUND_BOOKING_IS_ONHLOD("E20220007", ErrorTypeEnum.BUSERROR, "", "Cancel booking is onHold."),
	ERR_BLOCK_REFUND_REQUESTEREMAIL_NOTFOUND("E20220008", ErrorTypeEnum.BUSERROR, "", "Cancel booking has requester email is empty."),
	
	/** Error code of add booking */
	ERR_BOOKINGADD_NOBOOKINGFOUND("E23A00000", ErrorTypeEnum.BUSERROR, "", "Cannot find rloc by eticket"),
	ERR_BOOKINGADD_NAME_MISMATCHED("E23A00001", ErrorTypeEnum.BUSERROR, "", "Name not matched"),
	ERR_BOOKINGADD_INVALID_STAFF_PRI("E23A00011", ErrorTypeEnum.BUSERROR, "", "Invalid staff booking priority(01) found"),
	
	ERR_BOOKINGADD_BOOKING_ALREADY_SHOWING("E23A00002", ErrorTypeEnum.BUSERROR, "", "Booking already exist"),
	ERR_BOOKINGADD_STAFF_MICE_GROUP_BOOKING_NOT_SUPPORT("E23A00003", ErrorTypeEnum.BUSERROR, "", "Booking is a staff_AD / MICE_type_C / group booking"),
	ERR_BOOKINGADD_LAST_FLIGHT_DEPARTED_3_DAYS_AGO("E23A00004", ErrorTypeEnum.BUSERROR, "", "Last flight sector has departed more than 3 days"),
	ERR_BOOKINGADD_NO_CXKA_FLIGHT_FOUND("E23A00005", ErrorTypeEnum.BUSERROR, "", "No CX/KA flight found"),
	ERR_BOOKINGADD_INCLUDE_NOT_AVAILABLE_FLIGHT("E23A00006", ErrorTypeEnum.BUSERROR, "", "Unavailable segment[UC/HX segment] found in booking"),
	ERR_BOOKINGADD_NO_TRAVEL_DOC_IN_MEMBER_PROFILE("E23A00007", ErrorTypeEnum.BUSERROR, "", "No travel document in member profile"),
	ERR_BOOKINGADD_HOTEL_BOOKING_NOT_SUPPORT("E23A00008", ErrorTypeEnum.BUSERROR, "", "No flight rloc in this booking"),
	ERR_BOOKINGADD_UNAVAILABLE_BOOKING("E23A00009", ErrorTypeEnum.BUSERROR, "", "Booking has no pax or segments"),
	ERR_BOOKINGADD_ETICKET_RLOC_BOTH_NULL("E23A00010", ErrorTypeEnum.BUSERROR, "", "E-ticket/rloc are both empty"),
	ERR_BOOKINGADD_INVAILD_INFANT_ETICKET("E23A00012", ErrorTypeEnum.BUSERROR, "", "Input e-ticket is a infant eticket"),
	
	/** Error code of External invoke: sever error aep */
	ERR_AEP_CONNECTION("E24AEP001", ErrorTypeEnum.BUSERROR, "", "Cannot connect aes service"),
	
	/** Error code of External invoke: IBE error*/
	ERR_IBE_TICKET_REFUND_FAIL("E24IBE001", ErrorTypeEnum.BUSERROR, "", "IBE response null or error"),
	
	/** Error code of change flight*/
	ERR_FLIGHT_ONLY("E00000004",ErrorTypeEnum.BUSERROR,"", "Flight only"),

	/** Error code of OLCI cancel checkin*/
	WARN_CANCEL_CHECKIN_NO_ELIGIBILITY("W23Z00128",ErrorTypeEnum.BUSERROR,"", "Cancel check in not eligible"),
	
	/** Error code of purchase history */
	ERR_PURCHASEHISTORY_NO_HISTORY_FOUND("E23P00001", ErrorTypeEnum.BUSERROR, "", "No purchase history found in booking"),


	/** Error code of 15 below */
	ERR_15BLOW_GETTOKEN("E15B00001",ErrorTypeEnum.BUSERROR,"", "15Blow get token error"),
	ERR_15BLOW_SENTCALSELEMAIL("E15B00002",ErrorTypeEnum.BUSERROR,"", "15Blow send email error"),
	
	/** Error code of update entitlement through INT031*/
	ERR_NOVATTI_UPDATE_ENTITLEMENT("E25A00001", ErrorTypeEnum.BUSERROR, "", "Call INT031 to lock entitilements failure"),
	/** Error code of retrieve entitlement through INT031*/
	ERR_NOVATTI_RETRIEVE_ENTITLEMENT("E25A00009", ErrorTypeEnum.BUSERROR, "", "NovattiService retrieve EntitlementId error"),
	/** Error code of retrieve membership entitlement through INT031*/
	ERR_NOVATTI_RETRIEVE_MEMBERSHIP_ENTITLEMENT("E25A00021", ErrorTypeEnum.BUSERROR, "", "NovattiService retrieve Membership Entitlement failure"),
	/** try to roll back entitlement failure*/
	ERR_NOVATTI_ROLLBACK_ENTITLEMENT_FAIL("E25A00019", ErrorTypeEnum.BUSERROR, "", "Entitlement roll back error"),
	/** Error code of claim lounge*/
	ERR_CLAIM_LOUNGE_NOT_ENOUGH_ENTITLEMENT("E25A00002", ErrorTypeEnum.BUSERROR, "", "Claim lounge not enough entitlements"),
	ERR_CLAIM_LOUNGE_BOOKING_NOT_AVAILABLE("E25A00003", ErrorTypeEnum.BUSERROR, "", "Some passengers cannot claim lounge"),
	ERR_CLAIM_LOUNGE_GET_ENTITLEMENT_FAILURE("E25A00004", ErrorTypeEnum.BUSERROR, "", "Get entitlements from EODS failure"),
	ERR_CLAIM_LOUNGE_GET_BOOKING_FAILURE("E25A00005", ErrorTypeEnum.BUSERROR, "", "Get benefitsBooking failure"),
	ERR_CLAIM_LOUNGE_NO_BENEFITS_BOOKING_FOUND("E25A00006", ErrorTypeEnum.BUSERROR, "", "No benefitsBooking found from BizRule"),
	ERR_CLAIM_LOUNGE_PASSENGER_NO_FOUND_IN_BOOKING("E25A00007", ErrorTypeEnum.BUSERROR, "", "No passenger found in benefitsBooking"),
	ERR_CLAIM_LOUNGE_FLIGHT_NO_FOUND_IN_BOOKING("E25A00008", ErrorTypeEnum.BUSERROR, "", "No flight found in benefitsBooking"),
	ERR_CLAIM_LOUNGE_ADD_SK_FAIL("E25A00020", ErrorTypeEnum.BUSERROR, "", "Add SK failure"),
	
	ERR_REQUEST_MEMBER_ONHOLIDAY("E25A00010", ErrorTypeEnum.BUSERROR, "", "Member on holiday"),
	WARN_BOOKING_LOUNGE_GROUP_BOOKING("W25A00011", ErrorTypeEnum.WARNING, "", "Group booking cannot claim lounge"),
	WARN_BOOKING_LOUNGE_NO_TICKET("W25A00012", ErrorTypeEnum.WARNING, "", "Booking no e-ticket"),
	WARN_BOOKING_LOUNGE_PORT_NO_LOUNGE("W25A00013", ErrorTypeEnum.WARNING, "", "Port no lounge"),
	WARN_BOOKING_LOUNGE_NOT_IN_WINDOW("W25A00014", ErrorTypeEnum.WARNING, "", "Not in claim lounge window"),
	WARN_BOOKING_LOUNGE_NOT_TRAVEL_WITH_MEMBER("W25A00015", ErrorTypeEnum.WARNING, "", "Not travel with member"),
	WARN_BOOKING_LOUNGE_FLIGHT_FLOWN("W25A00016", ErrorTypeEnum.WARNING, "", "Flight flown"),
	WARN_BOOKING_LOUNGE_FLIGHT_NOT_CONFIRMED("W25A00017", ErrorTypeEnum.WARNING, "", "Flight not confirmed"),
	WARN_BOOKING_LOUNGE_NON_CXKA("W25A00018", ErrorTypeEnum.WARNING, "", "Flight not by CX/KA"),
	WARN_BOOKING_LOUNGE_PORT_NO_CLAIMABLE_LOUNGE("W25A00019", ErrorTypeEnum.WARNING, "", "Port no claimable lounge"),
	WARN_BOOKING_LOUNGE_PORT_NO_PURCHASABLE_LOUNGE("W25A00020", ErrorTypeEnum.WARNING, "", "Port no purchasable lounge"),
	WARN_BOOKING_LOUNGE_NON_AIR_SEGMENT("W25A00021", ErrorTypeEnum.WARNING, "", "Non-air segment"),
	
	/** Error code of claim upgrade*/
	ERR_CLAIM_UPGRADE_NOT_ENOUGH_ENTITLEMENT("E23BU0020", ErrorTypeEnum.BUSERROR, "", "Claim upgrade not enough entitlements"),
	ERR_CLAIM_UPGRADE_BOOKING_NOT_AVAILABLE("E23BU0021", ErrorTypeEnum.BUSERROR, "", "Some passengers cannot claim lounge"),
	ERR_CLAIM_UPGRADE_GET_ENTITLEMENT_FAILURE("E23BU0022", ErrorTypeEnum.BUSERROR, "", "Get entitlement failure"),
	ERR_CLAIM_UPGRADE_GET_BOOKING_FAILURE("E23BU0023", ErrorTypeEnum.BUSERROR, "", "Get bookableUpgradeBooking failure"),
	ERR_CLAIM_UPGRADE_NO_BENEFITS_BOOKING_FOUND("E23BU0024", ErrorTypeEnum.BUSERROR, "", "No bookableUpgradeBooking found from BizRule"),
	ERR_CLAIM_UPGRADE_PASSENGER_NO_FOUND_IN_BOOKING("E23BU0025", ErrorTypeEnum.BUSERROR, "", "Passenger can't be found in bookableUpgradeBooking"),
	ERR_CLAIM_UPGRADE_FLIGHT_NO_FOUND_IN_BOOKING("E23BU0026", ErrorTypeEnum.BUSERROR, "", "Claim lounge flight cannot find in booking"),
	
	ERR_BOOKABLE_UPGRADE_NO_BOOKING_FOUND("E23BU0030", ErrorTypeEnum.BUSERROR, "", "No Booking found"),
	ERR_BOOKABLE_UPGRADE_MEMBER_NOT_IN_BOOKING("E23BU0050", ErrorTypeEnum.BUSERROR, "", "The request booking not the memebr self booking"),
	ERR_BOOKABLE_UPGRADE_EMAIL_SENT_FAIL("E23BU0051", ErrorTypeEnum.BUSERROR, "", "Send claim upgrade email failure"),
	WARN_BOOKABLE_UPGRADE_GROUP_BOOKING("W23BU0031", ErrorTypeEnum.WARNING, "", "Group booking cannot upgrade"),
	WARN_BOOKABLE_UPGRADE_PACKAGE_BOOKING("W23BU0032", ErrorTypeEnum.WARNING, "", "Package booking cannot upgrade"),
	WARN_BOOKABLE_UPGRADE_CONCESSION_BOOKING("W23ABU0033", ErrorTypeEnum.WARNING, "", "Concession booking cannot upgrade"),
	WARN_BOOKABLE_UPGRADE_TICKET_NOT_ISSUED("W23BU0034", ErrorTypeEnum.WARNING, "", "Ticket not issued"),
	WARN_BOOKABLE_UPGRADE_REDEMPTION_BOOKING("W23BU0035", ErrorTypeEnum.WARNING, "", "Redemption booking"),
	WARN_BOOKABLE_UPGRADE_FLIGHT_FLOWN("W23BU0036", ErrorTypeEnum.WARNING, "", "Flight flown"),
	WARN_BOOKABLE_UPGRADE_NON_CXKA("W23BU0037", ErrorTypeEnum.WARNING, "", "Flight not by CX/KA"),
	WARN_BOOKABLE_UPGRADE_INVALID_SUBCALSS("W23BU0038", ErrorTypeEnum.WARNING, "", "Upgrade invalid subclass"),
	WARN_BOOKABLE_UPGRADE_NOT_IN_WINDOW("W23BU0039", ErrorTypeEnum.WARNING, "", "Not in upgrade window"),
	WARN_BOOKABLE_UPGRADE_GOT_TOP_CALSS("W23BU0040", ErrorTypeEnum.WARNING, "", "Booking upgrade got top class"),
	WARN_BOOKABLE_UPGRADE_STATUS_NOT_CONFIRMED("W23BU0041", ErrorTypeEnum.WARNING, "", "Flight status not confirmed"),
	WARN_BOOKABLE_UPGRADE_IN_REQUEST("W23BU0042", ErrorTypeEnum.WARNING, "", "Upgrade in request"),
	WARN_BOOKABLE_UPGRADE_UPGRADED("W23BU0043", ErrorTypeEnum.WARNING, "", "Bookable upgrade upgraded"),
	WARN_BOOKABLE_UPGRADE_NON_AIR_SEGMENT("W23BU0044", ErrorTypeEnum.WARNING, "", "Non-air segment"),
	
	ERR_RETRIEVE_PNR_NO_FOUND("E23RP0000", ErrorTypeEnum.BUSERROR, "", "Cannot find PNR by rloc"),
	
	ERR_ASSISTANCE_CANCEL_NO_PASSENGER_SEGMENT_FOUND("E23AS0001", ErrorTypeEnum.BUSERROR, "", "No related passenger and segment found in booking"),
	ERR_ASSISTANCE_CANCEL_NO_ASSISTANCE_FOUND("E23AS0002", ErrorTypeEnum.BUSERROR, "", "No assistance found"),
	ERR_ASSISTANCE_CANCEL_INVALID_SPECIAL_CODE("E23AS0003", ErrorTypeEnum.BUSERROR, "", "Request special code is not exist in booking under the passenger"),
	ERR_ASSISTANCE_UPDATE_INVALID_PARAMETERS_RECEIVED("E23AS004", ErrorTypeEnum.BUSERROR, "", "Invalid parameter reveived in update assistance"),
	ERR_ASSISTANCE_UPDATE_ASSISTANCE_ALREADY_EXIST("E23AS005", ErrorTypeEnum.BUSERROR, "", "Update assistance has already exist in PNR"),
	
	/** Error Code of check-in acceptance*/
	ERR_CHECK_IN_ACCEPTANCE_FAIL("E23CA0001", ErrorTypeEnum.BUSERROR, "", "Check-in acceptance failure."),
	ERR_CHECK_IN_ACCEPTANCE_CPRJOURNEY_NO_FOUND("E23CA0002", ErrorTypeEnum.BUSERROR, "", "Booking is null or no cprJouney found in the booking"),
	ERR_CHECK_IN_ACCEPTANCE_REQUEST_JOURNEY_NO_FOUND("E23CA0003", ErrorTypeEnum.BUSERROR, "", "Request jouney not found in the booking"),
	ERR_CHECK_IN_ACCEPTANCE_REQUEST_PASSENGER_NO_FOUND("E23CA0004", ErrorTypeEnum.BUSERROR, "", "Request passenger not found in the jouenry"),
	ERR_CHECK_IN_ACCEPTANCE_VALID_SEGMENT_NO_FOUND("E23CA0005", ErrorTypeEnum.BUSERROR, "", "No can-check segments found in the booking"),
	
	/** Error code of cancel check in */
	ERR_CANCEL_CHECK_IN_JOURNEY_NOT_FOUND("E23CC0001", ErrorTypeEnum.BUSERROR, "", "Journey not found in booking"),
	ERR_CANCEL_CHECK_IN_PASSENGER_NOT_FOUND("E23CC0002", ErrorTypeEnum.BUSERROR, "", "Passenger not found in journey"),
	ERR_CANCEL_CHECK_IN_FAILURE("E23CC0003", ErrorTypeEnum.BUSERROR, "", "Passenger not found in journey"),
	
	/** Error code of member enrollment */
	ERR_MEMBERENROLLMENT_TECHNICALISSUE("E23E00000", ErrorTypeEnum.BUSERROR, "", "Enrollment failed because of technical issue"),
	
	/** Error code of package booking not found */
	ERR_SPNR_BOOKING_NOTFOUND("E23E00001",ErrorTypeEnum.BUSERROR,"", "Package booking not found."),
	
	/** Error code of current session of olci not found */
	ERR_BP_OLCISESSION_NOTFOUND("E23E00003",ErrorTypeEnum.BUSERROR,"", "OLCI session of not found."),
	
	/** Error code of generate apple pass file failure */
	ERR_BP_GENERATE_APPLEPASS_FAIL("E23E00004",ErrorTypeEnum.BUSERROR,"", "generate apple pass file failure."),
	
	/** Error code of send email failure */
	ERR_BP_EMAIL_SENT_FAIL("E23E00005",ErrorTypeEnum.BUSERROR,"", "send boarding pass email failure."),
	
	/** Error code of send SMS failure */
	ERR_BP_SMS_SENT_FAIL("E23E00006",ErrorTypeEnum.BUSERROR,"", "send boarding pass sms failure."),
	
	/** Error code of send LinkBooking */
    ERR_LINK_BOOKING_ID_NONID_FAIL("E23L00002",ErrorTypeEnum.BUSERROR,"", "non id staff booking can not be linked");

	
	
	private String code;
	
	private ErrorTypeEnum type;
	
	private String oldMmbCode;
	
	private String errorMsg;
	
	private ErrorCodeEnum(String code, ErrorTypeEnum type,String oldMmbCode,String errorMsg) {
		this.code = code;
		this.type = type;
		this.oldMmbCode = oldMmbCode;
		this.errorMsg = errorMsg;
	}
	
	public String getCode() {
		return this.code;
	}
	public ErrorTypeEnum getType(){
		return this.type;
	}

	public String getOldMmbCode() {
		return oldMmbCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}


}

