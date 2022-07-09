package com.cathaypacific.mmbbizrule.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OneAConstants {

	
	// -----------For Build CPR Start --------------------

	/** Passenger Type: Infant Flag In CPR */
	
	
	/** Valid FFP identifier */
	public static final String VALID_FFP_IDENTIFIER = "OK";
	
	/** Boarding time */
	public static final String BOARDING_TIME = "BDT";
	
	/** Ticket document details type, E for ET */
	public static final String TICKET_DOCUMENT_TYPE_E = "E";
	
	/** Ticket status - ASS means Associated */
	public static final String TICKET_STATUS_ASS = "ASS";
	
	/** Service type. K means this is a SK element */
	public static final String SERVICE_TYPE_K = "K";

	/** Lounge access indicators */
	public static final String SERVICE_LOUNGE_FLAC = "FLAC";
	public static final String SERVICE_LOUNGE_BLAC = "BLAC";
	public static final String PURCHASE_LOUNGE_LGAB="LGAB";
	public static final String PURCHASE_LOUNGE_LGAF="LGAF";
	public static final String SERVICE_LOUNGE_LOT = "LOT";
	public static final String SERVICE_LOUNGE_LOE = "LOE";
	public static final String SERVICE_LOUNGE_ACTION_1 = "1";
	public static final String SERVICE_LOUNGE_ACTION_D = "D";
	/** Attribute type: Lounge Qualifier */
	public static final String SERVICE_LOUNGE_LQ = "LQ";
	/** Attribute description of lounge qualifier: first */
	public static final String SERVICE_LOUNGE_D1ST = "D1ST";
	/** Attribute description of lounge qualifier: business */
	public static final String SERVICE_LOUNGE_DBUS = "DBUS";
	/** Attribute type: Order Description */
	public static final String SERVICE_LOUNGE_OD = "OD";
	/** Attribute description of order description: first */
	public static final String SERVICE_LOUNGE_FIRST = "First";
	/** Attribute description of order description: business */
	public static final String SERVICE_LOUNGE_BUSINESS = "Business";

	/** SSR Code type A of a customer level service */
	public static final String CUSTOMER_LEVEL_SERVICE_SSR_CODE_GRMA = "GRMA";
	
	/** SSR Code type B of a customer level service */
	public static final String CUSTOMER_LEVEL_SERVICE_SSR_CODE_GRMB = "GRMB";
	
	/** SSR Code type C of a customer level service */
	public static final String CUSTOMER_LEVEL_SERVICE_SSR_CODE_GRMC = "GRMC";
	
	/** SSR Code type unaccompanied Minor */
	public static final String UNACCOMPANIED_MINOR_SSR_CODE_UMNR = "UMNR";
	
	/** Staff atrribute type, NA means Not Applicable */
	public static final String STAFF_ATTRIBUTE_TYPE_NA = "NA";
	
	/** Staff atrribute type, SBY means Subject to loading */
	public static final String STAFF_ATTRIBUTE_TYPE_SBY = "SBY";
	
	/** Staff atrribute type, BKB means No subject to loading */
	public static final String STAFF_ATTRIBUTE_TYPE_BKB = "BKB";
	
	/** revenueIntegrity stop over */
	public static final String REVENUE_INTEGRITY_STC = "STC";
	
	/** revenueIntegrity Out-of-sequence */
	public static final String REVENUE_INTEGRITY_SCS = "SCS";
	
	/** selection indicator ILK (link indicator) */
	public static final String SELECTION_INDICATOR_ILK = "ILK";
	
	/** CTCM : mobile number */
	public static final String CUSTOMER_LEVEL_SERVICE_SSR_CODE_CTCM = "CTCM";
	
	/** CTCE : email address */
	public static final String CUSTOMER_LEVEL_SERVICE_SSR_CODE_CTCE = "CTCE";
	
	/** Service type: special service requirement */
	public static final String SERVICE_TYPE_S = "S";
	
	/** Service action: confirmed */
	public static final String SERVICE_ACTION_MCF = "MCF";
	
	/** Service action: guaranteed */
	public static final String CUSTOMER_LEVEL_SERVICE_ACTION_MGD = "MGD";
	
	/** Emergency Contact Information */
	public static final String DOCUMENT_IDENTIFICATION_EME = "EME";
	
	/** Status indicator: VSP (Data View Permission) */
	public static final String STATUS_INDICATOR_VSP = "VSP";
	
	/** StatusInformation: N (Not Restricted) */
	public static final String STATUS_ACTION_N = "N";
	
	/** statusInformation:  F (failed)*/
	public static final String STATUS_INFORMATION_F = "F";
	
	/** statusInformation:   R (Required)*/
	public static final String STATUS_INFORMATION_R = "R";
	
	/**DCSETK_CheckRevenueIntegrityReply/customerLevel/productLevel/revenueIntegrity/booleanResultIndicator/responseType = R (Response)*/
	public static final String REVENUE_INTEGRITY_CHECK_RESPONSE_TRPE_R = "R";
	
	/** DCSETK_CheckRevenueIntegrityReply/customerLevel/productLevel/revenueIntegrity/booleanResultIndicator/statusCode = F (Failed)*/
	public static final String REVENUE_INTEGRITY_CHECK_RESPONSE_STATUSCODE_F = "F";
	
	/** Status indicator: Customer Acceptance Accepted */
	public static final String STATUS_INDICATOR_CUSTOMER_ACCEPTANCE_ACCEPTED = "CAC";
	public static final String STATUS_INDICATOR_CUSTOMER_ACCEPTANCE_NOT_ACCEPTED = "CNA";
	public static final String STATUS_INDICATOR_CUSTOMER_ACCEPTANCE_REJECTED = "CRJ";
	public static final String STATUS_INDICATOR_CUSTOMER_ACCEPTANCE_STANDBY = "CST";
	public static final String STATUS_INDICATOR_CUSTOMER_ACCEPTANCE_BOARDED = "BED";
	
	/** Seat Characteristic Mapping */
	public static final String SEAT_CHARACTERISTIC_OVER_WING = "OW";
	public static final String SEAT_CHARACTERISTIC_AISLE_SEAT = "A";
	public static final String SEAT_CHARACTERISTIC_CENTRE_SEAT = "9";
	public static final String SEAT_CHARACTERISTIC_RIGHT_SIDE = "RS";
	public static final String SEAT_CHARACTERISTIC_WINDOW_SEAT = "W";
	public static final String SEAT_CHARACTERISTIC_EXIT_SEAT = "E";
	public static final String SEAT_CHARACTERISTIC_CHARGEABLE = "CH";
	public static final String SEAT_CHARACTERISTIC_LEG_SPACE = "L";
	public static final String SEAT_CHARACTERISTIC_HANDICAPPED = "H";
	public static final String SEAT_CHARACTERISTIC_PREFERENTIAL = "O";
	public static final String SEAT_CHARACTERISTIC_BABY_BASSINET = "B";
	public static final String SEAT_CHARACTERISTIC_UNACCOMPANIED_MINOR = "U";
	public static final String SEAT_CHARACTERISTIC_WINDOW_WITHOUT_WINDOW_SEAT = "1W";
	public static final String SEAT_CHARACTERISTIC_WINDOW_AISLE_SEAT = "WA";
	
	/** Service identifier: SI */
	public static final String SERVICE_IDENTIFIER = "SI";
	
	/** OA RLOC flag*/
	public static final String ATTRIBUTETYPE_OAP="OAP";
	
	public static final String ATTRIBUTETYPE_CSP="CSP";
	
	public static final String ATTRIBUTETYPE_SGP="SGP";
	
	/** Comment Related Constants */
	public static final String COMMENT_TYPE_GATE = "GTE";
	public static final String COMMENT_DOCUMENT_CHECK = "DCHK";
	
	public static final String TSA_RESPONSE = "AQQ";

	public static final String ONEA_COMPANY = "1A";
	
	/**passenger title */
	public static final String NAME_TITLE_TYPE_IN_DB = "TITLE";
	
	/**Company id */
	public static final String COMPANY_CX = "CX";
	public static final String COMPANY_KA = "KA";
	
	/**segment status */
	public static final String SEGMENTSTATUS_FLOWN = "B";
	public static final String SEGMENTSTATUS_CANCEL = "UN";
	public static final String SEGMENTSTATUS_TK = "TK";
	public static final String SEGMENTSTATUS_HK = "HK";
	
	/** XX indicator */
	public static final String XX_INDICATOR = "XX";
	/** OT qualifier */
	public static final String OT_QUALIFIER = "OT";
	/** PT qualifier */
	public static final String PT_QUALIFIER = "PT";
	/** ST qualifier */
	public static final String ST_QUALIFIER = "ST";
	/** SSR segment */
	public static final String SSR_SEGMENT = "SSR";
	/** STR segment */
	public static final String STR_SEGMENT = "STR";
	/** SK segment */
	public static final String SK_SEGMENT = "SK";
	/** RM segment */
	public static final String RM_SEGMENT = "RM";
	/** HK status */
	public static final String HK_STATUS = "HK";
	/** KK status */
	public static final String KK_STATUS = "KK";
	/** KL status */
	public static final String KL_STATUS = "KL";
	/** RR status */
	public static final String RR_STATUS = "RR";
	/** TK status */
	public static final String TK_STATUS = "TK";
	/** UC status */
	public static final String UC_STATUS = "UC";
	/** HX status */
	public static final String HX_STATUS = "HX";
	/** CX company */
	public static final String CX_COMPANY = "CX";
	/** MAAS */
	public static final String MAAS = "MAAS";
	/** OTHS */
	public static final String OTHS = "OTHS";
	/** DOCS */
	public static final String DOCS = "DOCS";
	/** DOCO */
	public static final String DOCO = "DOCO";
	/** DOCA */
	public static final String DOCA = "DOCA";
	/** CTCM */
	public static final String CTCM = "CTCM";
	/** CTCE */
	public static final String CTCE = "CTCE";	
	/** CTCE */
	public static final String APE = "APE";
	/** CTCE */
	public static final String APM = "APM";
	/** CTCE */
	public static final String APH = "APH";
	/** CTCE */
	public static final String APB = "APB";
	/** PCTC */
	public static final String PCTC = "PCTC";
	/** CXMB */
	public static final String CXMB = "CXMB";
	/** DMP */
	public static final String DMP = "DMP";
	/** P22 type */
	public static final String P22_TYPE = "P22";
	/** DOCS format */
	public static final String TRAVEL_DOCUMENT_DOCS_FREETEXT_FORMAT = "%s-%s-%s-%s-%s-%s-%s-%s-%s";
	/** DOCO format */
	public static final String TRAVEL_DOCUMENT_DOCO_FREETEXT_FORMAT = "%s/%s/%s/%s/%s/%s";
	/** DOCO format */
	public static final String TRAVEL_DOCUMENT_DOCO_FREETEXT_INFANT_FORMAT = "%s/%s/%s/%s/%s/%s/%s";
	/** DOCA[R] format */
	public static final String COUNTRY_OF_RESIDENCE_FREETEXT_FORMAT = "R-%s-%s-%s-%s-%s";
	/** DOCA[R] infant format */
	public static final String COUNTRY_OF_RESIDENCE_FREETEXT_INFANT_FORMAT = "R-%s-%s-%s-%s-%s-I";
	/** DOCA[D] format */
	public static final String DESTINATION_ADDRESS_FREETEXT_FORMAT = "D-%s-%s-%s-%s-%s";
	/** DOCA[D] infant format */
	public static final String DESTINATION_ADDRESS_FREETEXT_INFANT_FORMAT = "D-%s-%s-%s-%s-%s-I";
	/** FQTV type */
	public static final String SSR_TYPE_FQTV = "FQTV";
	
	public static final String FQTV_TIER_LEVEL_DM = "DM";
	
	public static final String FQTV_TIER_LEVEL_DMP = "DMP";
	
	/** sbr type for mice/group booking in pnr **/
	public static final String SBR_TYPE_GROUP_MICE_BOOKING = "GRP";
	
	public static final String TRAVEL_DOCUMENT_TYPE_VISA = "V";
	public static final String TRAVEL_DOCUMENT_TYPE_K = "K";
	public static final String TRAVEL_DOCUMENT_TYPE_R = "R";
	public static final String TRAVEL_DOCUMENT_TYPE_PRIMARY = "P";
	public static final String TRAVEL_DOCUMENT_TYPE_SECONDARY = "S";

	/** Travel cabin economy class */
	public static final String CABIN_CLASS_ECONOMY = "Y";
	public static final String CABIN_CLASS_PREMIUM_ECONOMY = "W";
	public static final String CABIN_CLASS_FIRST = "F";
	public static final String CABIN_CLASS_BUSINESS = "J";
	
	public static final String PASSENGER_TYPE_INF = "INF";
	public static final String PASSENGER_TYPE_INS = "INS";
	public static final String PASSENGER_TYPE_DEFAULT = "ADT";
	public static final String PASSENGER_TYPE_CHILD = "CHD";
	public static final String PASSENGER_TYPE_BSCT = "BSCT";
	public static final String PASSENGER_TYPE_UNACCOMPANIED_MINOR = "UM";
	public static final String PASSENGER_UNACCOMPANIED_MINOR_UNN = "UNN";
	public static final String PASSENGER_UNACCOMPANIED_MINOR_UAM = "UAM";
	
	/** SK type */
	public static final String SK_TYPE_XLWR = "XLWR";
	public static final String SK_TYPE_XLMR = "XLMR";
	public static final String SK_TYPE_XLGR = "XLGR";
	public static final String SK_TYPE_ADTK = "ADTK";
	public static final String SK_TYPE_STFS = "STFS";
	public static final String SK_TYPE_STFD = "STFD";
	public static final String SK_TYPE_BAGW = "BAGW";
	public static final String SK_TYPE_CUST = "CUST";
	public static final String SK_TYPE_ASWR = "ASWR";
	
	
	public static final String SK_TYPE_SPNR = "SPNR";
	public static final String SK_TYPE_SPNR_FO = "FO";
	
	public static final String SK_TYPE_SPNR_BOOKINGTRPE_MOB = "MOB";
	public static final String SK_TYPE_SPNR_BOOKINGTRPE_NDC = "NDC";
	
	/**The booking has flight if found FLT in spnr*/
	public static final String SK_TYPE_SPNR_FLT = "FLT";
	/**The booking has hotel if found HTL in spnr*/
	public static final String SK_TYPE_SPNR_HTL = "HTL";
	/**The booking has event if found XXBEVT in spnr*/
	public static final String SK_TYPE_SPNR_XXBEVT = "XXBEVT";
	
	/** SSR type:mice Group booking GRMA*/
	public static final String SK_TYPE_GRMA  = "GRMA";
	/** SSR type:mice GRMBg*/
	public static final String SK_TYPE_GRMB  = "GRMB";
	/** SSR type:mice GRMC*/
	public static final String SK_TYPE_GRMC  = "GRMC";
	
	/** SSR type: BKUG*/
	public static final String SK_TYPE_BKUG  = "BKUG";
	
	
	
	/** SSR type:Group booking*/
	public static final String SSR_TYPE_GRPF  = "GRPF";
	
	/** SSR type:Upgrade Bid won segment*/
	public static final String SSR_TYPE_FQUG  = "FQUG";
	
	/** Staff sk identifier*/

	public static final List<String> STAFF_SK_IDENTIFIER_LIST = Collections.unmodifiableList(Arrays.asList(SK_TYPE_STFS,SK_TYPE_STFD));

	/** Staff sk identifier*/
	public static final List<String> MICE_SK_IDENTIFIER_LIST = Collections.unmodifiableList(Arrays.asList(SK_TYPE_GRMA,SK_TYPE_GRMB,SK_TYPE_GRMC));
	
	public static final String SK_TYPE_XLMR_SUBCLASS_MCO = "MCO";
	/** sengment identifications*/
	public static final String SEGMENTS_IDENTIFICATION_ARNK = "ARNK";
	
	/** TK Indicator */
	public static final String TICKET_INDICATOR_XL = "XL";
	public static final String TICKET_INDICATOR_TL = "TL";
	public static final String TICKET_INDICATOR_OK = "OK";
	
	public static final String TICKET_FREETEXT_ETCX = "ETCX";
	public static final String TICKET_FREETEXT_ETKA = "ETKA";
	public static final String TICKET_FREETEXT_ET = "ET";
	
	/** On hold static remark */
	public static final String REMARK_ON_HOLD = "STATUS:ON HOLD";
	
	public static final String REMARK_TYPE_RM = "RM";
	public static final String REMARK_CATEGORY_T = "T";
	public static final String REMARK_CX_HOT = "CX_HOT";
	public static final String REMARK_CX_EVT = "CX_EVT";
	
	/** Segment airCraftType */
	public static final String EQUIPMENT_TRN = "TRN";
	public static final String EQUIPMENT_TRS = "TRS";
	public static final String EQUIPMENT_LCH = "LCH";
	public static final String EQUIPMENT_BUS = "BUS";
	public static final String EQUIPMENT_A320 = "320";

	/** Middle character in booking office ID */
	public static final String OFFICE_ID_CX = "CX";
	public static final String OFFICE_ID_KA = "KA";
	public static final String OFFICE_ID_CX0 = "CX0";
	public static final String OFFICE_ID_KA0 = "KA0";
	
	/** IBE booking office ID - Last set of characters in booking office ID */
	public static final String OFFICE_ID_IBE_CX08AA = "CX08AA";
	public static final String OFFICE_ID_IBE_CX08CP = "CX08CP";
	public static final String OFFICE_ID_IBE_KA08AA = "KA08AA";
	public static final String OFFICE_ID_IBE_KA08CP = "KA08CP";
	public static final String OFFICE_ID_IBE_CX08KA = "CX08KA";
	public static final String OFFICE_ID_IBE_CX08KP = "CX08KP";
	public static final String OFFICE_ID_IBE_CX08HK = "CX08HK";
	public static final String OFFICE_ID_IBE_CX08HP = "CX08HP";
	public static final String OFFICE_ID_IBE_KA08HK = "KA08HK";
	public static final String OFFICE_ID_IBE_KA08HP = "KA08HP";
	public static final String OFFICE_ID_IBE_BJSCX08HK = "BJSCX08HK";
	public static final String OFFICE_ID_IBE_BJSCX08HP = "BJSCX08HP";
	public static final String OFFICE_ID_IBE_TYOCX08JP = "TYOCX08JP";
	
	/** NDC booking office ID - Last set of characters in booking office ID */
	public static final String OFFICE_ID_NDC_CX06DC = "CX06DC";
	public static final String OFFICE_ID_NDC_KA06DC = "KA06DC";
	
	/** GCC booking office ID - Last set of characters in booking office ID */
	public static final String OFFICE_ID_GCC_CX0100 = "CX0100";
	public static final String OFFICE_ID_GCC_CX0101 = "CX0101";
	public static final String OFFICE_ID_GCC_CX0102 = "CX0102";
	public static final String OFFICE_ID_GCC_KA0100 = "KA0100";
	public static final String OFFICE_ID_GCC_KA0101 = "KA0101";
	public static final String OFFICE_ID_GCC_KA0102 = "KA0102";
	public static final String OFFICE_ID_GCC_CX0001_0012 = "CX0001-0012";
	public static final String OFFICE_ID_GCC_CX00AT = "CX00AT";
	public static final String OFFICE_ID_GCC_CX00CM = "CX00CM";
	public static final String OFFICE_ID_GCC_KA0001_0012 = "KA0001-0012";
	public static final String OFFICE_ID_GCC_KA00AT = "KA00AT";
	public static final String OFFICE_ID_GCC_KA00CM = "KA00CM";
	
	public static final String PAX_TYPE_INFANT = "IN";
	
	public static final String TPOS = "TPOS";
	public static final String L04 = "L04";
	
	/** Paid ASR */
	public static final String PAID_ASR_INDICATOR = "C0";
	public static final String PAID_ASR_SEAT_CHARACTERISTIC = "CH";
	public static final String PAID_ASR_FREE_TEXT = "DTCX";
	
	/** Separator "/" */
	public static final String SEPARATOR_IN_FREETEXT = "/";
	
	/** Type of SK BAGW */
	public static final String BAGW_TYPE_EACH = "EACH";
	public static final String BAGW_TYPE_TOTL = "TOTL";
	
	/** SSR type check-in purchased baggage */
	public static final String SSR_TYPE_OBAG  = "OBAG";
	public static final String SSR_TYPE_ABAG  = "ABAG";
	public static final String SSR_TYPE_BBAG  = "BBAG";
	public static final String SSR_TYPE_CBAG  = "CBAG";
	
	/** Baggage allowance unit */
	public static final String BAGGAGE_UNIT_K  = "K";
	public static final String BAGGAGE_UNIT_P  = "P";

	/** E-ticket prefix */
	public static final String CX_ETICKET_PREFIX  = "160";
	public static final String KA_ETICKET_PREFIX  = "043";
	
	/** Delimiter in CUST free text */
	public static final String DELIMITER_IN_CUST_FREETEXT  = ":";
	
	/** Prefix of OSI freeText */
	public static final String OSI_FREETEXT_PREFIX_AD  = "AD";
	
	public static final String OSI_FREETEXT_PREFIX_ID  = "ID";
	
	public static final String ASR_RBD_X  = "X";
	
	/** Wheel chair SSR type */
	public static final String SSR_TYPE_WCHR  = "WCHR";
	public static final String SSR_TYPE_WCHS  = "WCHS";

	public static final String SEGMENT_FE_WAIVE = "WAIVE";
	
	public static final List<String> SSR_SK_CONFIRMED_STATUS= Collections.unmodifiableList(Arrays.asList(TK_STATUS,KK_STATUS,HK_STATUS)) ;
	
	/** the remark free text of voluntary seat change from EXL to non EXL seat */
	public static final String REMARK_FREETEXT_OF_VOLUNTARY_CHANGE_FROM_EXL_TO_NORMAL = "NON REFUND OF EXL SEAT CHARGE DUE TO VOLUNTARY CHANGE FROM EXL TO NON EXL SEAT AS REQUESTED BY PAX";
	
	/** the messageFunction for 1A Ticket_ProcessEDoc call to get e-ticket info */
	public static final String TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET = "131";
	
	/** the messageFunction for 1A Ticket_ProcessEDoc call to get purchase history */
	public static final String TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_PURCHASE_HISTORY = "791";
	
	/** display groups */
	public static final String DISPLAY_GROUP_EC = "EC";
	public static final String DISPLAY_GROUP_DA = "DA";
	public static final String DISPLAY_GROUP_CR = "CR";
	public static final String DISPLAY_GROUP_KN = "KN";
	public static final String DISPLAY_GROUP_RN = "RN";
	public static final String DISPLAY_GROUP_PI = "PI";
	public static final String DISPLAY_GROUP_EI = "EI";
	
	/** gender */
	public static final String GENDER_ADULT_MALE = "M";
	public static final String GENDER_ADULT_FEMALE = "F";
	public static final String GENDER_INFANT_MALE = "MI";
	public static final String GENDER_INFANT_FEMALE = "FI";
	public static final String GENDER_INFANT_FLAG = "I";
	
	public static final String TICKET_COUPON_STATUS_AL = "AL";
	public static final String TICKET_COUPON_STATUS_I = "I";
	public static final String TICKET_COUPON_STATUS_RF = "RF";
	
	/** SSR type  airport paid upgrade */
	public static final String SSR_TYPE_AUGW  = "AUGW";
	public static final String SSR_TYPE_AUGJ  = "AUGJ";
	public static final String SSR_TYPE_AUGR  = "AUGR";
	public static final String SSR_TYPE_AUGF  = "AUGF";
	
	/** FP freeText */
	public static final String FP_FOP_PREFIX_IBE = "IBE";
	public static final String FP_FOP_PREFIX_CC = "CC";
	public static final String FP_FOP_CASH = "CASH";
	public static final String FP_FOP_CHQ = "CHQ";
	
	// Mobility assistance SSR 
	public static final String[] MOBILITY_ASSIST_ARRAY  = {"WCHR","WCHC","WCHS","WCLB","WCBD","WCBW","WCMP","WCOB"};
	public static final String[] ASSIST_COLLECTION  = {"BLND","DEAF","WCHR","WCHC","WCHS","WCLB","WCBD","WCBW","WCMP","WCOB"};
	
	// UMNR EForm parental lock remark freeText
	public static final String UMNR_EFORM_PARENTAL_LOCK_RM_FREETEXT = "MMB UM IFE Parental Lock";
	
	//SSR TYPE CRID
	public static final String SSR_TYPE_CRID = "CRID";
	
	// -----------For Build CPR End --------------------
	private OneAConstants() {
		// Empty constructor.
	}
	
}
