package com.cathaypacific.mmbbizrule.oneaservice.pnr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.ssrskstatus.SsrStatusEnum;
import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeBidStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeRedStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeType;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.NonAirSegmentDao;
import com.cathaypacific.mmbbizrule.db.dao.PassengerTypeDAO;
import com.cathaypacific.mmbbizrule.db.dao.SpecialMealDAO;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbSsrTypeDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.db.model.StatusManagementModel;
import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModel;
import com.cathaypacific.mmbbizrule.model.booking.detail.AtciCancelledSegment;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.util.OneAErrorParserUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.DataElement;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAddressDetails;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrApContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrApEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBaggage;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBidUpgradeElement;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBidUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookableUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAtciCancelledSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElementsOtherData;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDob;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmrContactInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFa;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFe;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrLoungeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrOnHoldInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookMapping;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRedUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeat;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatPreference;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSsrInfant;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicketPriceInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrUpgradeProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.NameIdentficationUtil;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.AdditionalProductDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.AdditionalProductTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.DateAndTimeInformationType32722S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.FrequentTravellerIdentificationCodeType74327S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.InteractiveFreeTextType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.LongFreeTextType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.MiscellaneousRemarkType151C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.MiscellaneousRemarksType211S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.MonetaryInformationDetailsTypeI121351C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.MonetaryInformationTypeI79012S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.OriginatorDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.DataElementsMaster;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.DataElementsMaster.DataElementsIndiv;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.DataElementsMaster.DataElementsIndiv.SeatPaxInfo;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.OriginDestinationDetails;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.OriginDestinationDetails.ItineraryInfo;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.PnrHeader;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.PricingRecordGroup;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.TravellerInfo;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.TravellerInfo.PassengerData;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.POSGroupType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PPQRdataType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PPQRdataType.DocumentDetailsGroup;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PPQRdataType.DocumentDetailsGroup.CouponDetailsGroup;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PriorityDetailsType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferenceInfoType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferenceInformationType65487S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferenceInformationTypeI79009S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferencingDetailsType111975C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferencingDetailsType127526C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReferencingDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.SpecialRequirementsDataDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.SpecialRequirementsTypeDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TicketElementType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TicketInformationType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TravelProductInformationTypeI193100S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TravellerDetailsType260694C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TravellerSurnameInformationType260693C;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.UserPreferencesType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class PnrResponseParser {
	
	private static LogAgent logger = LogAgent.getLogAgent(PnrResponseParser.class);
	
	private static final String ONEA_COMPANY_ID="1A";
	
	private static final String PASSENGER_ID_QUALIFIER="PT";
	
	private static final String SEGMENT_ID_QUALIFIER="ST";
	
	private static final String EMAIL_TYPE_CTCE="CTCE";
	
	private static final String EMAIL_TYPE_APE="APE";

	private static final String PHONE_TYPE_CTCM="CTCM";
	
	private static final String PHONE_TYPE_APM="APM";
	
	private static final String PHONE_TYPE_APB="APB";
	
	private static final String PHONE_TYPE_APH="APH";

	private static final String FQTV_TYPE="FQTV";
	
	private static final String FQTR_TYPE="FQTR";
	
	private static final String FQTU_TYPE="FQTU";
	
	private static final String PGUG_TYPE="PGUG";
	
	private static final String FQUG_TYPE="FQUG";
	
	private static final String STAFF_TYPE="STFD";
	
	private static final String FLAC_TYPE="FLAC";
	
	private static final String BLAC_TYPE="BLAC";
	
	private static final String LGAB_TYPE="LGAB";
	
	private static final String LGAF_TYPE="LGAF";
	
	private static final String BKUG_TYPE="BKUG";

	private static final String COMPANY_CX="CX";

	private static final String COMPANY_KA="KA";
	
	public static final String ETICKET_PASSENGER_TYPE_PAX="PAX";
	
	public static final String PASSENGER_TYPE_INF="INF";
	
	public static final String PASSENGER_TYPE_INS="INS";

	private static final String PASSENGER_TYPE_ADT="ADT";
	
	private static final String PASSENGER_TYPE_CHD="CHD";
	
	private static final String EXTRA_SEAT_CBBG = "CBBG";
	
	private static final String SSR_TYPE_OBAG = "OBAG";
	
	private static final String SSR_TYPE_ABAG = "ABAG";
	
	private static final String SSR_TYPE_BBAG = "BBAG";
	
	private static final String SSR_TYPE_CBAG = "CBAG";
	
	private static final String EXTRA_SEAT_EXST = "EXST";
	
	/** Segment Name Constants */
	private static final String SEGMENT_NAME_SSR="SSR";
	
	private static final String SEGMENT_NAME_SK="SK";
	
	private static final String SEGMENT_NAME_FA = "FA";

	private static final String SEGMENT_NAME_FH = "FH";

	private static final String SEGMENT_NAME_FHA = "FHA";
	
	private static final String SEGMENT_NAME_FHD = "FHD";

	private static final String SEGMENT_NAME_FHE = "FHE";

	private static final String SEGMENT_NAME_FHM = "FHM";
	
	public static final String SEGMENT_NAME_TK = "TK";
	
	private static final String SEGMENT_NAME_RM = "RM";
	
	private static final String SEGMENT_NAME_AP = "AP";
	
	private static final String SEGMENT_NAME_FP = "FP";
	
	private static final String SEGMENT_NAME_FE = "FE";
	
	private static final String SEGMENT_NAME_OSI = "OS";
	
	public static final String SEPARATOR_IN_FREETEXT = "/";

	public static final String APP_CODE = "MMB";
	
	/** Travel Document */
	public static final String SSR_TYPE_TD = "TD";
	
	public static final String SSR_TYPE_DOCS = "DOCS";
	
	public static final String SSR_TYPE_DOCO = "DOCO";
	
	public static final String SSR_TYPE_DOCO_FREETEXT_INFANT = "I";
	
	public static final String PASSENGER_INFANT_ID_SUFFIX = "I";
	
	public static final String INFANT_GENDER_SUFFIX = "I";
	
	public static final String REFERENCE_QUALIFIER_OT = "OT";
	
	/** ssr type of emergency contact in DB */
	public static final String SSR_TYPE_EC = "EC";
	
	/** ssr type of destination address in DB */
	public static final String SSR_TYPE_DA = "DA";
	
	/** ssr type of residence address in DB */
	public static final String SSR_TYPE_RA = "RA";
	
	/** ssr type of residence address in DB */
	public static final String SSR_TYPE_INFANT = "INFT";
	
	/** ssr type of residence address in DB */
	public static final String SSR_TYPE_CHILD = "CHLD";
	
	/** ssr type of residence address in DB */
	public static final String SSR_TYPE_UM = "UMNR";
		
	/** type D in free text */
	private static final String TYPE_IN_FREE_TEXT_D = "D";
	
	/** type R in free text */
	private static final String TYPE_IN_FREE_TEXT_R = "R";
	
	/** baby meal type */
	private static final String BABY_MEAL_TYPE = "BBML";
	
	/** destination address type */
	private static final String ADDRESS_TYPE_DES = "DES";
	
	/** residence address type */
	private static final String ADDRESS_TYPE_RES = "RES";
	
	/** seat no ssr type */
	public static final String SSR_TYPE_SEATNO = "RQST";
	
	/** seat preference ssr type non smoking */
	public static final String SSR_TYPE_NON_SMOKING = "NSST";

	/** seat preference ssr type non smoking bulkhead */
	public static final String SSR_TYPE_NON_SMOKING_BULKHEAD = "NSSB";

	/** seat preference ssr type non smoking aisle*/
	public static final String SSR_TYPE_NON_SMOKING_AISLE = "NSSA";

	/** seat preference ssr type non smoking window*/
	public static final String SSR_TYPE_NON_SMOKING_WINDOW = "NSSW";
	
	public static final List<String> SEAT_SSR = Collections.unmodifiableList(Arrays.asList(SSR_TYPE_SEATNO,SSR_TYPE_NON_SMOKING,
			SSR_TYPE_NON_SMOKING_BULKHEAD, SSR_TYPE_NON_SMOKING_AISLE, SSR_TYPE_NON_SMOKING_WINDOW));

	private static final String PRIMARY_TRAVEL_DOCUMENT = "PT";
	
	private static final String SECONDARY_TRAVEL_DOCUMENT = "ST";
	
	private static final String OTHER_TRAVEL_DOCUMENT = "OT";
	
	private static final String KTN = "KTN";
	
	private static final String REDRESS = "REDRESS";
	
	public static final String SK_TYPE_XLMR = "XLMR";
	
	public static final String SK_TYPE_FRBK = "FRBK";
	
	/** APx contact type in PNR */
	private static final String APX_CONTACT_TYPE_APE = "P02";
	
	private static final String APX_CONTACT_TYPE_APM = "7";
	
	private static final String APX_CONTACT_TYPE_APB = "3";
	
	private static final String APX_CONTACT_TYPE_APH = "4";
	
	/** OSI Insurance */
	public static final String OSI_INSURANCE = "INS";
	
	private static final List<String> VALID_SEGMENT_NAME = Collections.unmodifiableList(Arrays.asList("AIR"));
	
	private static final String INVALID_SEGMENT_PROCESSING = "N";
	
	private static final List<String> ET_SEGMENT_NAME_LIST = Collections.unmodifiableList(Arrays.asList(SEGMENT_NAME_FA,SEGMENT_NAME_FH,SEGMENT_NAME_FHA,SEGMENT_NAME_FHE,SEGMENT_NAME_FHM));
	
	//--------------data emlemnt mapping key start-----------------
	private static final String DATAELEMENT_MAPPING_KEY_FQTU="mapkey_fqtu";
	private static final String DATAELEMENT_MAPPING_KEY_PGUG="mapkey_pgug";
	private static final String DATAELEMENT_MAPPING_KEY_FQUG="mapkey_fqug";
	private static final String DATAELEMENT_MAPPING_KEY_STAFF="mapkey_staff";
	private static final String DATAELEMENT_MAPPING_KEY_FLAC="mapkey_flac";
	private static final String DATAELEMENT_MAPPING_KEY_BLAC="mapkey_blac";
	private static final String DATAELEMENT_MAPPING_KEY_LGAF="mapkey_lgaf";
	private static final String DATAELEMENT_MAPPING_KEY_LGAB="mapkey_lgab";
	private static final String DATAELEMENT_MAPPING_KEY_BKUG="mapkey_bkug";
	private static final String DATAELEMENT_MAPPING_KEY_INFT="mapkey_inft";
	private static final String DATAELEMENT_MAPPING_KEY_ONHOLD="mapkey_onhold";
	private static final String DATAELEMENT_MAPPING_KEY_MBRENTITLEMENT="mapkey_mbrentitlement";
	//--------------data emlemnt mapping key end-----------------
	
	/** The prefix of flight protection RM cancel freeText */
	public static final String PROTECTION_RM_FREETEXT_PREFIX = "NOTIFLY";
	
	private static final String AMADEUS_H = "AMADEUS-H";
	
	/** separator number in protection notifly info freeText */
	public static final int SEPARATOR_NUMBER_IN_NOTIFLY_INFO_FREETEXT = 6;
	
	private static final String[] AIRPORT_UPGRADE_SSR_TYPES = new String[] {OneAConstants.SSR_TYPE_AUGW, OneAConstants.SSR_TYPE_AUGJ, OneAConstants.SSR_TYPE_AUGR, OneAConstants.SSR_TYPE_AUGF};
	
	/** FQUG free text delimiter */
	@Autowired
	private StatusManagementDAO statusManagementDAO;
	
	@Autowired
	private NonAirSegmentDao nonAirSegmentDao;

	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	@Autowired
	private TbSsrTypeDAO tbSsrTypeDAO;
	
	@Autowired
	private SpecialMealDAO specialMealDao;
	
	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Autowired
	private PassengerTypeDAO passengerTypeDAO;
	
	@Autowired
    private BookEligibilityConfig bookEligibilityConfig;

	public RetrievePnrBooking paserResponse(PNRReply pnrReply){
		if(pnrReply == null){
			return null;
		}
		// A temp data for parse infant meal
		Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp = Maps.newLinkedHashMap();
		
		RetrievePnrBooking booking = new RetrievePnrBooking();
		//base booking info 
		parseRloc(booking, pnrReply);

		//parser the dataElementMapping
		Map<String,List<DataElement>> dataElementMapping =  parserDataElement(pnrReply);

		//parser booking create info office ID and create date
		parserCreateInfo(pnrReply, booking);
		
		//parser onHoleInfo
		parserOnHoldInfo(booking,dataElementMapping);
		
		//parse ticket price info
		parseTicketPriceInfo(pnrReply, booking);

		//get pos
		parserPos(pnrReply, booking);
		
		//get pos of creation
		parserCreationPos(pnrReply, booking);

		//parser ssr sk list
		parserSsrSkList(pnrReply, booking);
		//Passenger Info  
		parserPassengerInfo(pnrReply, booking,dataElementMapping);
		//Segment Info
		parserSegment(pnrReply, booking,dataElementMapping);
		//generate passengerSegments
		generatePassengerSegment(booking.getPassengers(), booking.getSegments(), booking.getPassengerSegments());	
		//passenger and segment relative info
		parserPassengerSegment(pnrReply, booking, babyMealMapTemp, dataElementMapping);
		//parserInfantInfo including travelDocs 
		parserInfantInfo(booking, babyMealMapTemp);
		//parse remark
		parserRemark(pnrReply, booking);
		//parse Ticket list
		parserTicketList(pnrReply, booking);
		//parse APx contact info
		parserApContactInfo(pnrReply, booking);
		//parse Fa
		parserFaList(pnrReply, booking);

		//parse FE
		parserFeList(pnrReply, booking);
		//parse FHD
		parserFhdList(pnrReply, booking);
		//parse corporateBooking
		parserCorporateBooking(pnrReply, booking);
		//parse OSI
		parserOSI(pnrReply, booking);
		//parser re-book info
		parserReBookInfo(booking);

		//parser dwcode
		parserDWCode(booking);
		
		//parser tpos
		parserTPOS(booking);
		
		//parse package booking information including spnr
		booking.setBookingPackageInfo(BookingBuildUtil.buildBookingPackageInfo(booking.getSkList(), booking.getRemarkList()));
		
		//parser FRBK SK
		parserFRBKSK(pnrReply, booking);
		
		return booking;
	}
	
	private void parserTPOS(RetrievePnrBooking booking) {
		booking.setTposList(BookingBuildUtil.getListOfTPOS(booking));
	}
	
	/**
	 * parse onea rloc and GDS rloc
	 * @param booking
	 * @param pnrReply
	 */
	private void parseRloc(RetrievePnrBooking booking, PNRReply pnrReply) {
		// base booking info
		if (!CollectionUtils.isEmpty(pnrReply.getPnrHeader()) && !CollectionUtils.isEmpty(pnrReply.getPnrHeader().get(0).getReservationInfo().getReservation())) {
			for (PnrHeader pnrHeader : pnrReply.getPnrHeader()) {
				if (CollectionUtils.isEmpty(pnrHeader.getReservationInfo().getReservation())) {
					return;
				}
				boolean isOneARloc = ONEA_COMPANY_ID.equals(pnrHeader.getReservationInfo().getReservation().get(0).getCompanyId());
				if (booking.getOneARloc() == null && isOneARloc) {
					booking.setOneARloc(pnrHeader.getReservationInfo().getReservation().get(0).getControlNumber());
				} else if (booking.getGdsRloc() == null && !isOneARloc) {
					booking.setGdsRloc(pnrHeader.getReservationInfo().getReservation().get(0).getControlNumber());
				}
			}
			// we should always set 1A rloc
			if(booking.getOneARloc() == null){
				booking.setOneARloc(pnrReply.getPnrHeader().get(0).getReservationInfo().getReservation().get(0).getControlNumber());
			}	
		}
	}
	/**
	 * parse FHD element
	 * @param pnrReply
	 * @param booking
	 */
	private void parserFhdList(PNRReply pnrReply, RetrievePnrBooking booking) {
		List<RetrievePnrDataElements> fhdList = Lists.newArrayList();
		
		// Null checking for parameters
		if(pnrReply == null || pnrReply.getDataElementsMaster() == null || CollectionUtils.isEmpty(pnrReply.getDataElementsMaster().getDataElementsIndiv()) || booking == null) {
			return;
		}
		
		// Loop through all data elements to check if FHD segment exist
		for(DataElementsIndiv dataElementsIndiv: pnrReply.getDataElementsMaster().getDataElementsIndiv()) {
			ElementManagementSegmentType elementManagementData = dataElementsIndiv.getElementManagementData();
			
			// Null checking for elementManagementData
			if(elementManagementData == null) {
				continue;
			}
			
			// Only need FHD segment
			String segmentName = elementManagementData.getSegmentName();
			if(segmentName == null || !SEGMENT_NAME_FHD.equals(segmentName)) {
				continue;
			}
			
			RetrievePnrDataElements retrievePnrFhd = new RetrievePnrDataElements();
			retrievePnrFhd.setSegmentName(SEGMENT_NAME_FHD);
			
			// Set longFreeText
			if (CollectionUtils.isEmpty(dataElementsIndiv.getOtherDataFreetext()) || dataElementsIndiv.getReferenceForDataElement() == null || CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				continue;
			}
		
			// parse longFreeText
			parserLongFreeText(retrievePnrFhd, dataElementsIndiv.getOtherDataFreetext());
			
			// Set for passengerId
			List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
			
			parserPassengerIdForFhd(retrievePnrFhd, references);
		
			// Create Fa to PNRBooking
			if (retrievePnrFhd.getPassengerId() != null && !CollectionUtils.isEmpty(retrievePnrFhd.getOtherDataList())) {
				fhdList.add(retrievePnrFhd);
			}
		}
		
		booking.setFhdList(fhdList);	
	}

	/**
	 * parse passengerId for FHD
	 * @param retrievePnrFhd
	 * @param references
	 */
	private void parserPassengerIdForFhd(RetrievePnrDataElements retrievePnrFhd,
			List<ReferencingDetailsType111975C> references) {
		if(retrievePnrFhd == null || CollectionUtils.isEmpty(references)) {
			return;
		}
		
		for(int i = 0; i < references.size(); i++) {
			ReferencingDetailsType111975C reference = references.get(i);
			if(OneAConstants.PT_QUALIFIER.equals(reference.getQualifier())) {
				retrievePnrFhd.setPassengerId(reference.getNumber());
				break;
			}
		}
	}

	/**
	 * parse longFreeText for FHD
	 * @param retrievePnrFhd
	 * @param longFreeTextTypes
	 */
	private void parserLongFreeText(RetrievePnrDataElements retrievePnrFhd, List<LongFreeTextType> longFreeTextTypes) {
		if(retrievePnrFhd != null && !CollectionUtils.isEmpty(longFreeTextTypes) ) {
			for(LongFreeTextType longFreeText : longFreeTextTypes) {
				if(!StringUtils.isEmpty(longFreeText.getLongFreetext())) {
					RetrievePnrDataElementsOtherData otherData = new RetrievePnrDataElementsOtherData();
					otherData.setFreeText(longFreeText.getLongFreetext());
					retrievePnrFhd.findOtherDataList().add(otherData);
				}
			}
		}
	}

	private void parserOnHoldInfo(RetrievePnrBooking booking, Map<String,List<DataElement>> dataElementMapping ){
		
		RetrievePnrOnHoldInfo	onHoldInfo = (RetrievePnrOnHoldInfo)Optional.ofNullable(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_ONHOLD)).filter(CollectionUtils::isNotEmpty).map(eleList->eleList.get(0)).orElse(null);
		booking.setOnHoldRemarkInfo(onHoldInfo);
	}
	/**
	 * parser re-book info under segments
	 * @param booking
	 */
	private void parserReBookInfo(RetrievePnrBooking booking) {
		parserNormalReBookInfo(booking);
        // parse rebooking info for GDS booking whose UN flight has been cancelled
        parserRebookInfoForGdsBooking(booking);
	}
	
	/**
	 * parse normal rebook info
	 * @param booking
	 */
	private void parserNormalReBookInfo(RetrievePnrBooking booking) {
		List<RetrievePnrRemark> remarks = booking.getRemarkList();
		List<RetrievePnrSegment> segments = booking.getSegments();
		if(CollectionUtils.isEmpty(remarks) || CollectionUtils.isEmpty(segments)) {
			return;
		}
		parserCancelledReBookInfo(remarks, segments);
		parserAcceptedReBookInfo(remarks, segments);
		parserRebookMapping(booking);
	}

	/**
	 * parser the relationship between cancelled segmentIds and accept segmentIds
	 * @param booking
	 */
	private void parserRebookMapping(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return;
		}
		
		Map<String, List<String>> map = new HashMap<>();
		for(RetrievePnrSegment segment : booking.getSegments()) {
			if(segment == null || segment.getRebookInfo() == null 
					|| !BooleanUtils.isTrue(segment.getRebookInfo().isRebooked())
					|| !CollectionUtils.isNotEmpty(segment.getRebookInfo().getNewBookedSegmentIds())) {
				continue;
			}
			
			String key = StringUtils.collectionToDelimitedString(segment.getRebookInfo().getNewBookedSegmentIds(), ",");
			List<String> cancelledSegmentIds = map.get(key);
			if(CollectionUtils.isEmpty(cancelledSegmentIds)) {
				List<String> calSegmentIds = new ArrayList<>();
				calSegmentIds.add(segment.getSegmentID());
				map.put(key, calSegmentIds);
			} else {
				cancelledSegmentIds.add(segment.getSegmentID());
			}
		}
		
		List<RetrievePnrRebookMapping> rebookMapping = new ArrayList<>();
		for(Entry<String, List<String>> entry : map.entrySet()) {
			RetrievePnrRebookMapping mapping = new RetrievePnrRebookMapping();
			List<String> cancelledSegmentIds = entry.getValue();
			List<String> acceptSegmentIds = Arrays.asList(entry.getKey().split(","));
			Collections.sort(cancelledSegmentIds);
			Collections.sort(acceptSegmentIds);
			mapping.setCancelledSegmentIds(cancelledSegmentIds);
			mapping.setAcceptSegmentIds(acceptSegmentIds);
			rebookMapping.add(mapping);
		}
		
		booking.setRebookMapping(rebookMapping);
	}

	/**
	 * parser accepted segment re-book information
	 * @param remarks
	 * @param segments
	 */
	private void parserAcceptedReBookInfo(List<RetrievePnrRemark> remarks, List<RetrievePnrSegment> segments) {
		List<String[]> reBookRemarks = getReBookAcceptedFreeText(remarks);
		parserAcceptedSegmentReBookInfo(segments, reBookRemarks);
	}

	/**
	 * parser cancelled segment re-book information
	 * @param remarks
	 * @param segments
	 */
	private void parserCancelledReBookInfo(List<RetrievePnrRemark> remarks, List<RetrievePnrSegment> segments) {
		List<String[]> reBookRemarks = getReBookCancelledFreeText(remarks);
		parserCancelledSegmentReBookInfo(segments, reBookRemarks);
	}

	/**
	 * parser accepted segment re-book information
	 * @param segments
	 * @param reBookRemarks
	 */
	private void parserAcceptedSegmentReBookInfo(List<RetrievePnrSegment> segments, List<String[]> reBookRemarks) {
		for(String[] reBookRemark : reBookRemarks) {
			String acceptedSegments = reBookRemark[2];
			if(StringUtils.isEmpty(acceptedSegments)) {
				continue;
			}
			List<String> acceptedSegmentNumbers = Arrays.asList(acceptedSegments.split(" "));
			if(CollectionUtils.isEmpty(acceptedSegmentNumbers)) {
				continue;
			}
			List<String> acceptedSegmentIds = checkReBookAcceptSegment(segments, acceptedSegmentNumbers);
			String familyName = reBookRemark[0];
			String givenName = reBookRemark[1];
			setAcceptedReBookInfo(segments, acceptedSegmentIds, familyName, givenName);
		}
	}

	/**
	 * parser cancelled segment re-book information
	 * @param segments
	 * @param reBookRemarks
	 */
	private void parserCancelledSegmentReBookInfo(List<RetrievePnrSegment> segments, List<String[]> reBookRemarks) {
		for(String[] reBookRemark : reBookRemarks) {
			List<String> cancelledSegmentNumbers = Arrays.asList(reBookRemark[0].split(" "));
			List<String> newBookedSegmentNumbers = Arrays.asList(reBookRemark[1].split(" "));
			if(CollectionUtils.isEmpty(cancelledSegmentNumbers) || CollectionUtils.isEmpty(newBookedSegmentNumbers)) {
				continue;
			}
			List<String> newBookedSegmentIds = checkReBookSegment(segments, cancelledSegmentNumbers, newBookedSegmentNumbers);
			setCancelledReBookInfo(segments, cancelledSegmentNumbers, newBookedSegmentIds);
		}
	}

	/**
	 * check all segment from ACCEPT freeText are valid or not,
	 * if valid then return accepted segmentIds.
	 *
	 * @param segments
	 * @param acceptedSegmentNumbers
	 * @return List<String>
	 */
	private List<String> checkReBookAcceptSegment(List<RetrievePnrSegment> segments, List<String> acceptedSegmentNumbers) {
		int vaildCount = 0;
		List<String> acceptedSegmentIds = new ArrayList<>();
		for(RetrievePnrSegment segment : segments) {
			String segmentNumber = segment.getMarketCompany() + segment.getMarketSegmentNumber();
			if(!CollectionUtils.isEmpty(segment.getStatus())
					&& segment.getStatus().contains(OneAConstants.SEGMENTSTATUS_HK)
					&& acceptedSegmentNumbers.contains(segmentNumber)) {
				vaildCount++;
				acceptedSegmentIds.add(segment.getSegmentID());
			}
		}

		if(acceptedSegmentNumbers.size() == vaildCount) {
			return acceptedSegmentIds;
		}
		return Collections.emptyList();
	}

	/**
	 * check all segment from NOTIFLY freeText are valid or not,
	 * if valid then return new booked segmentIds.
	 *
	 * @param segments
	 * @param cancelledSegmentNumbers
	 * @param newBookedSegmentNumbers
	 * @return List<String>
	 */
	private List<String> checkReBookSegment(List<RetrievePnrSegment> segments, List<String> cancelledSegmentNumbers, List<String> newBookedSegmentNumbers) {
		int vaildCount = 0;
		List<String> newBookedSegmentIds = new ArrayList<>();
		for(RetrievePnrSegment segment : segments) {
			String segmentNumber = segment.getMarketCompany() + segment.getMarketSegmentNumber();
			if(!CollectionUtils.isEmpty(segment.getStatus())
					&& segment.getStatus().contains(OneAConstants.SEGMENTSTATUS_CANCEL)
					&& cancelledSegmentNumbers.contains(segmentNumber)) {
				vaildCount++;
			} else if(!CollectionUtils.isEmpty(segment.getStatus())
					&& segment.getStatus().contains(OneAConstants.SEGMENTSTATUS_TK)
					&& newBookedSegmentNumbers.contains(segmentNumber)) {
				vaildCount++;
				newBookedSegmentIds.add(segment.getSegmentID());
			}
		}
		if(cancelledSegmentNumbers.size() + newBookedSegmentNumbers.size() == vaildCount) {
			return newBookedSegmentIds;
		}
		return Collections.emptyList();
	}

	/**
	 * set accepted re-book information in segment
	 * @param segments
	 * @param acceptedSegmentIds
	 * @param familyName
	 * @param givenName
	 */
	private void setAcceptedReBookInfo(List<RetrievePnrSegment> segments, List<String> acceptedSegmentIds, String familyName, String givenName) {
		if(CollectionUtils.isEmpty(acceptedSegmentIds)) {
			return;
		}
		for(RetrievePnrSegment segment : segments) {
			if(acceptedSegmentIds.contains(segment.getSegmentID())) {
				RetrievePnrRebookInfo retrievePnrReBookInfo = new RetrievePnrRebookInfo();
				retrievePnrReBookInfo.setAccepted(true);
				retrievePnrReBookInfo.setAcceptFamilyName(familyName);
				retrievePnrReBookInfo.setAcceptGivenName(givenName);
				retrievePnrReBookInfo.setNewBookedSegmentIds(acceptedSegmentIds);
				segment.setRebookInfo(retrievePnrReBookInfo);
			}
		}
	}

	/**
	 * set cancelled re-book information in segment
	 * @param segments
	 * @param cancelledSegmentNumbers
	 * @param newBookedSegmentNumbers
	 */
	private void setCancelledReBookInfo(List<RetrievePnrSegment> segments, List<String> cancelledSegmentNumbers, List<String> newBookedSegmentIds) {
		if(CollectionUtils.isEmpty(newBookedSegmentIds)) {
			return;
		}
		for(RetrievePnrSegment segment : segments) {
			String segmentNumber = segment.getMarketCompany() + segment.getMarketSegmentNumber();
			if(!CollectionUtils.isEmpty(segment.getStatus())
					&& segment.getStatus().contains(OneAConstants.SEGMENTSTATUS_CANCEL)
					&& cancelledSegmentNumbers.contains(segmentNumber)) {
				RetrievePnrRebookInfo retrievePnrReBookInfo = new RetrievePnrRebookInfo();
				retrievePnrReBookInfo.setRebooked(true);
				retrievePnrReBookInfo.setNewBookedSegmentIds(newBookedSegmentIds);
				segment.setRebookInfo(retrievePnrReBookInfo);
			}
		}
	}

	/**
	 * get re-book accepted freeText from remark list parsed before
	 *
	 * @param pnrRemarks
	 * @return List<String[]>
	 */
	private List<String[]> getReBookAcceptedFreeText(List<RetrievePnrRemark> pnrRemarks) {
		List<String[]> rebookInfoList = new ArrayList<>();
		for(RetrievePnrRemark pnrRemark : pnrRemarks) {
			if(pnrRemark == null 
					|| !OneAConstants.REMARK_TYPE_RM.equals(pnrRemark.getType())
					|| StringUtils.isEmpty(pnrRemark.getFreeText())) {
				continue;
			}
			
			String[] rebookAcceptInfos = FreeTextUtil.getRebookAcceptInfoFromFreeText(pnrRemark.getFreeText());
			if(rebookAcceptInfos != null) {
				rebookInfoList.add(rebookAcceptInfos);
			}
		}
		return rebookInfoList;
	}
	
	/**
	 * parse rebooking info for GDS booking whose UN flight has been cancelled
	 * @param pnrBooking 
	 */
    private void parserRebookInfoForGdsBooking(RetrievePnrBooking booking) {
		if (booking == null || StringUtils.isEmpty(booking.getOfficeId()) || !BookingBuildUtil.is1AGDSBooking(booking.getOfficeId())
				|| BookingBuildUtil.isGroupBooking(booking.getSsrList())
				|| CollectionUtils.isEmpty(booking.getSegments()) || CollectionUtils.isEmpty(booking.getRemarkList())) {
			return;
		}
		int cancelledSegmentId = 1;
		List<String[]> reBookCancelledInfos = getReBookCancelledDetailInfo(booking.getRemarkList());
		List<RetrievePnrAtciCancelledSegment> atciCancelledSegments = new ArrayList<>();
		
		for(String[] reBookCancelledInfo : reBookCancelledInfos) {
			List<String> cancelledSegmentNumbers = Arrays.asList(reBookCancelledInfo[0].split(" "));
			List<String> cancelledSegmentDepartureDates = Arrays.asList(reBookCancelledInfo[1].split(" "));
			String cancelledSegmentOrigin = reBookCancelledInfo[2];
			String cancelledSegmentDestination = reBookCancelledInfo[3];	
			List<String> newBookedSegmentNumbers = Arrays.asList(reBookCancelledInfo[4].split(" "));
			List<String> newBookedSegmentDepartureDates = Arrays.asList(reBookCancelledInfo[5].split(" "));
			if (CollectionUtils.isEmpty(cancelledSegmentNumbers) || CollectionUtils.isEmpty(newBookedSegmentNumbers)
					|| CollectionUtils.isEmpty(cancelledSegmentDepartureDates)
					|| CollectionUtils.isEmpty(newBookedSegmentDepartureDates)
					|| StringUtils.isEmpty(cancelledSegmentOrigin)
					|| StringUtils.isEmpty(cancelledSegmentDestination)) {
				continue;
			}
			// if the UN segment is removed but TK segment exists, then build rebook info by RM
			if (checkUNSegmentRemoved(booking.getOneARloc(), booking.getSegments(), cancelledSegmentNumbers, cancelledSegmentDepartureDates, newBookedSegmentNumbers, newBookedSegmentDepartureDates)) {
				List<String> newBookedSegmentIds = getSegmentIdsBySegmentNumbers(newBookedSegmentNumbers, booking.getSegments());
				List<String> cancelledSegmentIds = new ArrayList<>();
				/** remove previous rebook mapping
				 * 		for cases that pax rebooked several times, there may be mulitple RM in PNR, e.g. rebook flight A -> flight B -> flight C -> flight B
				 * 		then there will be RM A -> B, RM B -> C, RM  C -> B, For RM A -> B and RM C -> B, RM A -> B should be removed (no need to consider 
				 * 		C -> B because for this case, at the moment, PNR should only contain flight B)
				 */
				removePreviousRebookMapping(booking.findAtciRebookMapping(), newBookedSegmentIds, atciCancelledSegments);
				for (int i = 0; i < cancelledSegmentNumbers.size(); i++) {
					RetrievePnrAtciCancelledSegment atciCancelledSegment = new RetrievePnrAtciCancelledSegment();
					atciCancelledSegment.setSegmentId(cancelledSegmentId++ + MMBBizruleConstants.CANCELLED_SEGMENT_SUFFIX);
					cancelledSegmentIds.add(atciCancelledSegment.getSegmentId());
					atciCancelledSegment.setOperateCompany(cancelledSegmentNumbers.get(i).substring(0, 2));
					atciCancelledSegment.setOperateFlightNumber(cancelledSegmentNumbers.get(i).substring(2, cancelledSegmentNumbers.get(i).length()));
					atciCancelledSegment.setOriginPort(cancelledSegmentOrigin);
					atciCancelledSegment.setDestPort(cancelledSegmentDestination);
					// parse departure date for ATCI cancelled segments
					parseDepartureDateForCancelledSegments(cancelledSegmentDepartureDates.get(i), booking.getSegments(), atciCancelledSegment, booking.getOneARloc());
					
					RetrievePnrRebookInfo rebookInfo = new RetrievePnrRebookInfo();
					rebookInfo.setRebooked(true);
					rebookInfo.setNewBookedSegmentIds(newBookedSegmentIds);
					atciCancelledSegment.setRebookInfo(rebookInfo);
					atciCancelledSegments.add(atciCancelledSegment);
				}
				
				RetrievePnrRebookMapping rebookMapping = new RetrievePnrRebookMapping();
				rebookMapping.setAcceptSegmentIds(newBookedSegmentIds);
				rebookMapping.setCancelledSegmentIds(cancelledSegmentIds);
				booking.findAtciRebookMapping().add(rebookMapping);
			}
			booking.setAtciCancelledSegments(atciCancelledSegments);

		}
	}
    
	/**
	 * parse departure date for cancelled segments
	 * 	The date comes from RM, format is "MM-DD", so need decide the year, the rule is parse the date to a time that is nearest to the first TK flight
	 * @param unDepartureDateStr - format DDMMM
	 * @param pnrSegments 
	 * @param departureDate
	 * @param originPort 
	 * @param oneARloc 
	 * @return date string, format YYYY-MM-DD
	 */
	private void parseDepartureDateForCancelledSegments(String unDepartureDateStr, List<RetrievePnrSegment> pnrSegments, RetrievePnrAtciCancelledSegment pnrCancelledSegment, String oneARloc) {
		if (StringUtils.isEmpty(unDepartureDateStr)) {
			return;
		}
		
		Date departureDateOfFirstTkSegment = getDateOfFirstTkSegment(pnrSegments, pnrCancelledSegment);
		// if can't find departure date of the TK flight, use current year
		if (departureDateOfFirstTkSegment == null) {
			// current year, format YYYY
			String currentYear = DateUtil.getCurrentCal2Str(DateUtil.DATE_PATTERN_YYYY);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(AtciCancelledSegment.TIME_FORMAT);
			try {
				// set value
				pnrCancelledSegment.setDepartureDate(dateFormat.format(createDateByDateStrAndYear(unDepartureDateStr, DateUtil.DATE_PATTERN_DDMMM, currentYear)));
			} catch (ParseException e) {
				logger.error(String.format("parse UN date failed for UN date: %s and year: %s", unDepartureDateStr, currentYear), e);
			}
		} else {
			try {
				// year of TK sement, format YYYY
				String yearOfTkSegment = DateUtil.getDate2Str(DateUtil.DATE_PATTERN_YYYY, departureDateOfFirstTkSegment);

				/**
				 * e,g. unDepartureDateStr is "01-01", departureDateOfFirstTkSegment is
				 * "2019-12-31", then compare date1: 2019-01-01 with date2: 2020-01-01,
				 * 2019-01-01 is 364 days before departureDateOfFirstTkSegment, and 2020-01-01
				 * is 1 day after departureDateOfFirstTkSegment, 2020-01-01 is closer to TK's
				 * date, so the date of UN should be 2020-01-01
				 */
				Date unDepartureDate1 = createDateByDateStrAndYear(unDepartureDateStr,
						RetrievePnrAtciCancelledSegment.TIME_FORMAT, yearOfTkSegment);

				Date unDepartureDate2 = createDateByDateStrAndYear(unDepartureDateStr,
						RetrievePnrAtciCancelledSegment.TIME_FORMAT,
						unDepartureDate1.compareTo(departureDateOfFirstTkSegment) < 0
								? String.valueOf(Integer.valueOf(yearOfTkSegment) + 1)
								: String.valueOf(Integer.valueOf(yearOfTkSegment) - 1));
				Date unDepartureDate = Math
						.abs(unDepartureDate1.getTime() - departureDateOfFirstTkSegment.getTime()) < Math
								.abs(unDepartureDate2.getTime() - departureDateOfFirstTkSegment.getTime())
										? unDepartureDate1
										: unDepartureDate2;
				// set value
				pnrCancelledSegment.setDepartureDate(DateUtil.getDate2Str(AtciCancelledSegment.TIME_FORMAT, unDepartureDate));
			} catch (NumberFormatException | ParseException e) {
				logger.error(String.format("parse UN date failed for UN date: %s and TK date: %s", unDepartureDateStr, DateUtil.getDate2Str(DateUtil.DATE_PATTERN_YYYY_MM_DD, departureDateOfFirstTkSegment)), e);
			}
		}
	}
	
	/**
	 * create date by dateStr and year
	 * 	set the year of the "dateStr" to the input "year", then return the new date
	 * @param departureDateStr
	 * @param dateStrFormat
	 * @param currentYear - format YYYY
	 * @return Date
	 * @throws ParseException
	 */
	private Date createDateByDateStrAndYear(String dateStr, String dateStrFormat, String currentYear) throws ParseException {
		Date departureDate = DateUtil.getStrToDate(dateStrFormat, dateStr);
		Calendar departureDateCalendar = Calendar.getInstance();
		departureDateCalendar.setTime(departureDate);
		departureDateCalendar.set(Calendar.YEAR, Integer.valueOf(currentYear));
		return departureDateCalendar.getTime();
	}

	/**
	 * get the departure date of the first TK segment
	 * @param pnrSegments
	 * @param pnrCancelledSegment
	 */
	private Date getDateOfFirstTkSegment(List<RetrievePnrSegment> pnrSegments,
			RetrievePnrAtciCancelledSegment pnrCancelledSegment) {
		RetrievePnrRebookInfo rebookinfo = pnrCancelledSegment.getRebookInfo();
		// first TK segment
		RetrievePnrSegment firstTkSegment = null;
		if (rebookinfo != null && !CollectionUtils.isEmpty(rebookinfo.getNewBookedSegmentIds())) {
			firstTkSegment = Optional.ofNullable(pnrSegments).orElse(Collections.emptyList()).stream()
					.filter(seg -> rebookinfo.getNewBookedSegmentIds().contains(seg.getSegmentID())).findFirst()
					.orElse(null);
		}
		// departure date of the first matched TK flight
		String dateStrOfFirstTkSegment = firstTkSegment == null || firstTkSegment.getDepartureTime() == null ? null : firstTkSegment.getDepartureTime().getPnrTime();
		try {
			return DateUtil.getStrToDate(RetrievePnrDepartureArrivalTime.TIME_FORMAT, dateStrOfFirstTkSegment);
		} catch (ParseException e) {
			logger.error(String.format("Parse date failed, date string: %s", dateStrOfFirstTkSegment), e);
			return null;
		}
	}
	
	/**
	 * remove previous rebook mapping
	 * 		for cases that pax rebooks several times, there may be multiple RM in PNR, e.g. rebook flight A -> flight B -> flight C -> flight B
	 * 		then there will be RM A -> B, RM B -> C, RM  C -> B, for RM A -> B and RM C -> B, RM A -> B should be removed (no need to consider 
	 * 		B -> C because for this case, at the moment, PNR should only contain TK flight B)
	 * @param atciRebookMappings
	 * @param newBookedSegmentIds
	 * @param cancelledSegments
	 */
	private void removePreviousRebookMapping(List<RetrievePnrRebookMapping> atciRebookMappings,
			List<String> newBookedSegmentIds, List<RetrievePnrAtciCancelledSegment> cancelledSegments) {
		if (CollectionUtils.isEmpty(atciRebookMappings) || CollectionUtils.isEmpty(newBookedSegmentIds)) {
			return;
		}
		
		List<RetrievePnrRebookMapping> previousMappings = atciRebookMappings.stream().filter(mapping -> !CollectionUtils.isEmpty(mapping.getAcceptSegmentIds()) && !CollectionUtils.isEmpty(mapping.getCancelledSegmentIds())
				// any newSegmentId exists in acceptSegmentIds of the mapping
				&& newBookedSegmentIds.stream().anyMatch(id -> mapping.getAcceptSegmentIds().contains(id))).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(previousMappings)) {
			List<String> cancelledIdsOfPreviousMappings = new ArrayList<>();
			previousMappings.stream().forEach(mapping -> cancelledIdsOfPreviousMappings.addAll(mapping.getCancelledSegmentIds()));
			
			for (String cancelledIdOfPreviousMappings : cancelledIdsOfPreviousMappings) {
				// remove previous cancelled segments
				cancelledSegments.removeAll(cancelledSegments.stream()
						.filter(seg -> Objects.equals(seg.getSegmentId(), cancelledIdOfPreviousMappings))
						.collect(Collectors.toList()));
			}
			// remove previous mapping
			atciRebookMappings.removeAll(previousMappings);
		}
	}

	/**
     * check rebook segments of GDS booking
     * 	TK segment should exist and UN segment should have been removed
	 * @param rloc 
     * @param segments
     * @param cancelledSegmentNumbers
     * @param newBookedSegmentNumbers
	 * @param newBookedSegmentDepartureDates 
	 * @param newBookedSegmentNumbers2 
     * @return boolean
     */
	private boolean checkUNSegmentRemoved(String rloc, List<RetrievePnrSegment> segments, List<String> cancelledSegmentNumbers,
			List<String> cancelledSegmentDepartureDates, List<String> newBookedSegmentNumbers, List<String> newBookedSegmentDepartureDates) {
		if (cancelledSegmentNumbers.size() != cancelledSegmentDepartureDates.size()
				|| newBookedSegmentNumbers.size() != newBookedSegmentDepartureDates.size()) {
			logger.info(String.format("RM NOTIFLY wrong format, parse failed, booking: %s", rloc));
			return false;
		}
		
		// if any of the cancelled flight exists in PNR, return false
		for (int i = 0; i < cancelledSegmentNumbers.size(); i++) {
			if (getSegmentByNumberAndDate(cancelledSegmentNumbers.get(i), cancelledSegmentDepartureDates.get(i), segments) != null) {
				return false;
			}
		}
		
		// if any of the TK flight doesn't exists in PNR, return false
		for (int i = 0; i < newBookedSegmentNumbers.size(); i++) {
			RetrievePnrSegment newBookedSegment = getSegmentByNumberAndDate(newBookedSegmentNumbers.get(i), newBookedSegmentDepartureDates.get(i), segments);
			if (newBookedSegment == null || CollectionUtils.isEmpty(newBookedSegment.getStatus())
					|| !newBookedSegment.getStatus().contains(OneAConstants.SEGMENTSTATUS_TK)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * get segment status by number
	 * @param inputSegmentNumber
	 * @param inputSegmentDate format: DDMMM
	 * @param segments
	 * @return RetrievePnrSegment
	 */
	private RetrievePnrSegment getSegmentByNumberAndDate(String inputSegmentNumber, String inputSegmentDate, List<RetrievePnrSegment> segments) {
		for(RetrievePnrSegment segment : segments) {
			if (segment.getDepartureTime() == null || segment.getDepartureTime().getPnrTime() == null) {
				continue;
			}
			String segmentNumber = segment.getMarketCompany() + segment.getMarketSegmentNumber();
			String segmentDate = DateUtil.convertDateFormat(segment.getDepartureTime().getPnrTime(), RetrievePnrDepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_DDMMM);
			if (!StringUtils.isEmpty(segmentNumber) && segmentNumber.equalsIgnoreCase(inputSegmentNumber)
					&& !StringUtils.isEmpty(segmentDate) && segmentDate.equalsIgnoreCase(inputSegmentDate)) {
				return segment;
			}
		}
		return null;
	}
	
    /**
     * get segment ids by segment numbers
     * @param newBookedSegmentNumbers
     * @param segments
     * @return List<String>
     */
    private List<String> getSegmentIdsBySegmentNumbers(List<String> newBookedSegmentNumbers,
			List<RetrievePnrSegment> segments) {
		return segments.stream()
				.filter(seg -> newBookedSegmentNumbers.contains(seg.getMarketCompany() + seg.getMarketSegmentNumber()))
				.map(RetrievePnrSegment :: getSegmentID).distinct().collect(Collectors.toList());
	}
	
	/**
	 * get re-book cancelled freeText from remark list parsed before
	 *	each rebookInfo contains:
	 *	index 0:cancelledFlights[format "KA734 CX880"],
	 *	index 1:newBookFlights [format "KA734 CX880"]
	 * @param pnrRemarks
	 * @return List<String[]>
	 */
	private List<String[]> getReBookCancelledFreeText(List<RetrievePnrRemark> pnrRemarks) {
		List<String[]> rebookInfoList = new ArrayList<>();
		// string builder for the RM freeText
		StringBuilder rmFreetextStrBuilder = new StringBuilder();
		for(RetrievePnrRemark pnrRemark : pnrRemarks) {
			if(pnrRemark == null 
					|| !OneAConstants.REMARK_TYPE_RM.equals(pnrRemark.getType())
					|| StringUtils.isEmpty(pnrRemark.getFreeText())) {
				continue;
			}
			
			/**
			 * for this kind of RM, one piece of info can be retrieved from freeTexts in
			 * multiple RMs, so keep reading the freeText until meet the 6th "/"
			 */
			if (pnrRemark.getFreeText() != null) {
				// if meet the prefix of the freeText, clear the builder
				if (pnrRemark.getFreeText().startsWith(PROTECTION_RM_FREETEXT_PREFIX)) {
					rmFreetextStrBuilder.setLength(0);
				}
				rmFreetextStrBuilder.append(pnrRemark.getFreeText());
			}
			
			// count the "/" in the freeText
			int separatorCount = BizRulesUtil.countSubstring(SEPARATOR_IN_FREETEXT, rmFreetextStrBuilder.toString());
			// if there're 6 or more "/" in the freeText, parse the freeText
			if (separatorCount >= SEPARATOR_NUMBER_IN_NOTIFLY_INFO_FREETEXT) {
				String[] rebookNotiflyInfos = FreeTextUtil.getRebookNotiflyInfoFromFreeText(rmFreetextStrBuilder.toString());
				if(rebookNotiflyInfos != null) {
					rebookInfoList.add(rebookNotiflyInfos);		
				}
				// clear the builder
				rmFreetextStrBuilder.setLength(0);
			}
		}
		return rebookInfoList;
	}
	
	/**
	 * get re-book cancelled info from remark list
	 *	each rebookInfo contains:
	 *	index 0:cancelledFlights[format "KA734 CX880"], 
	 * 	index 1:cancelled flight dates[format "12MAY 12MAY"], 
	 * 	index 2:cancelled flight origin port[format "KUL"], 
	 *  index 3:cancelled flight destination port[format "LAX"], 
	 *  index 4:new book flights[format "KA734 CX880"],
	 *  index 5:new booking flight dates[format "12MAY 12MAY"]
	 * @param pnrRemarks
	 * @return List<String[]>
	 */
	private List<String[]> getReBookCancelledDetailInfo(List<RetrievePnrRemark> pnrRemarks) {
		List<String[]> rebookInfoList = new ArrayList<>();
		// string builder for the RM freeText
		StringBuilder rmFreetextStrBuilder = new StringBuilder();
		for(RetrievePnrRemark pnrRemark : pnrRemarks) {
			if(pnrRemark == null 
					|| !OneAConstants.REMARK_TYPE_RM.equals(pnrRemark.getType())
					|| StringUtils.isEmpty(pnrRemark.getFreeText())) {
				continue;
			}
			
			/**
			 * for this kind of RM, one piece of info can be retrieved from freeTexts in
			 * multiple RMs, so keep reading the freeText until meet the 6th "/"
			 */
			if (pnrRemark.getFreeText() != null) {
				// if meet the prefix of the freeText, clear the builder
				if (pnrRemark.getFreeText().startsWith(PnrResponseParser.PROTECTION_RM_FREETEXT_PREFIX)) {
					rmFreetextStrBuilder.setLength(0);
				}
				rmFreetextStrBuilder.append(pnrRemark.getFreeText());
			}
			
			// count the "/" in the freeText
			int separatorCount = BizRulesUtil.countSubstring(PnrResponseParser.SEPARATOR_IN_FREETEXT, rmFreetextStrBuilder.toString());
			// if there're 6 or more "/" in the freeText, parse the freeText
			if (separatorCount >= PnrResponseParser.SEPARATOR_NUMBER_IN_NOTIFLY_INFO_FREETEXT) {
				String[] rebookNotiflyInfos = FreeTextUtil.getRebookNotiflyDetailInfoFromFreeText(rmFreetextStrBuilder.toString());
				if(rebookNotiflyInfos != null) {
					rebookInfoList.add(rebookNotiflyInfos);		
				}
				// clear the builder
				rmFreetextStrBuilder.setLength(0);
			}
		}
		return rebookInfoList;
	}

	/**
	 * parse OSI
	 * @param pnrReply
	 * @param booking
	 */
	private void parserOSI(PNRReply pnrReply, RetrievePnrBooking booking) {
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> allDataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		
		List<DataElementsIndiv> osiDataElementsIndivs = allDataElementsIndivs.stream().filter(dataElementsIndiv->dataElementsIndiv.getElementManagementData().getSegmentName() != null
				&& SEGMENT_NAME_OSI.equals(dataElementsIndiv.getElementManagementData().getSegmentName())
				&& !CollectionUtils.isEmpty(dataElementsIndiv.getOtherDataFreetext())).collect(Collectors.toList());
		
		for (DataElementsIndiv dataElementsIndiv : osiDataElementsIndivs) {
			//ssr segment name, eg. SSR,SK
			String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			
			List<LongFreeTextType> otherFreeText = dataElementsIndiv.getOtherDataFreetext();
			if(StringUtils.isEmpty(otherFreeText)) {
				continue;
			}
			
			BigInteger qulifierId = null;
			if(dataElementsIndiv.getElementManagementData().getReference() != null) {
				qulifierId =dataElementsIndiv.getElementManagementData().getReference().getNumber();
			}
			List<ReferencingDetailsType111975C> references =  new ArrayList<>();
			if(dataElementsIndiv.getReferenceForDataElement() != null){
				references = dataElementsIndiv.getReferenceForDataElement().getReference();
			}
			List<RetrievePnrDataElements> osiList = buildDataElementsStructure(references);
			populateOSIToBooking(osiList, segmentName, qulifierId, otherFreeText, booking);
				
		}	
	}

	/**
	 * populate OSI to booking
	 * @param osiList
	 * @param segmentName
	 * @param ssrType
	 * @param freeText
	 * @param qulifierId
	 * @param companyId
	 * @param status
	 * @param booking
	 */
	private void populateOSIToBooking(List<RetrievePnrDataElements> osiList, String segmentName, BigInteger qulifierId, List<LongFreeTextType> otherFreeTexts, RetrievePnrBooking booking) {
		if(CollectionUtils.isEmpty(osiList)){
			return;
		}
		
		for (RetrievePnrDataElements osiDetail : osiList) {
			osiDetail.setSegmentName(segmentName);
			osiDetail.setQulifierId(qulifierId);
			List<RetrievePnrDataElementsOtherData> otherDataList = new ArrayList<>();
			for(LongFreeTextType otherFreeText : otherFreeTexts) {
				RetrievePnrDataElementsOtherData otherData = new RetrievePnrDataElementsOtherData();
				if(otherFreeText.getFreetextDetail() != null) {
					otherData.setCompanyId(otherFreeText.getFreetextDetail().getCompanyId());
					otherData.setType(otherFreeText.getFreetextDetail().getType());
				}
				if(otherFreeText.getLongFreetext() != null) {
					otherData.setFreeText(otherFreeText.getLongFreetext());
				}
				otherDataList.add(otherData);
			}
			osiDetail.setOtherDataList(otherDataList);
			booking.findOsiList().add(osiDetail);
		}	
	}

	/**
	 * parse corporateBooking which is identified by BCODE freeText of FP/FE
	 * @param pnrReply
	 * @param booking
	 */
	private void parserCorporateBooking(PNRReply pnrReply, RetrievePnrBooking booking) {
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for (DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(dataElementsIndiv == null || dataElementsIndiv.getElementManagementData() == null 
					|| CollectionUtils.isEmpty(dataElementsIndiv.getOtherDataFreetext())){
				continue;
			}
			
			// if segment name is FP/FE and BCODE freeText matched, set the value and return
			if ((SEGMENT_NAME_FP.equals(dataElementsIndiv.getElementManagementData().getSegmentName())
					|| SEGMENT_NAME_FE.equals(dataElementsIndiv.getElementManagementData().getSegmentName()))
					&& elementContainsBCODEFreetext(dataElementsIndiv)) {
				booking.setCorporateBooking(true);
				return;
			}
		}
	}

	/**
	 * check if there is freeText with BCODE in the dataElementsIndiv
	 * @param dataElementsIndiv
	 * @return boolean
	 */
	private boolean elementContainsBCODEFreetext(DataElementsIndiv dataElementsIndiv) {
		for(LongFreeTextType dataFreetext : dataElementsIndiv.getOtherDataFreetext()){
			String freeText = dataFreetext.getLongFreetext();
			if(FreeTextUtil.isBCODEFreetext(freeText)){ // freeText matched
				return true;
			} 
		}
		return false;
	}

	/**
	 * parser booking create information
	 * @param pnrReply
	 * @param booking
	 */
	private void parserCreateInfo(PNRReply pnrReply, RetrievePnrBooking booking) {
		RetrievePnrBookingCerateInfo bookingCerateInfo = new RetrievePnrBookingCerateInfo();
		if(pnrReply.getSecurityInformation() != null || pnrReply.getSecurityInformation().getResponsibilityInformation() != null) {
			bookingCerateInfo.setRpOfficeId(pnrReply.getSecurityInformation().getResponsibilityInformation().getOfficeId());
		}
		if(pnrReply.getSecurityInformation() != null && pnrReply.getSecurityInformation().getSecondRpInformation() != null){
			bookingCerateInfo.setCreateDate(pnrReply.getSecurityInformation().getSecondRpInformation().getCreationDate());
			bookingCerateInfo.setCreateTime(pnrReply.getSecurityInformation().getSecondRpInformation().getCreationTime());
		}
		booking.setBookingCreateInfo(bookingCerateInfo);
	}

	/**
	 * parse ticket price info
	 * @param pnrReply
	 * @param booking
	 */
	private void parseTicketPriceInfo(PNRReply pnrReply, RetrievePnrBooking booking) {
		PricingRecordGroup pricingRecordGroup = pnrReply.getPricingRecordGroup();
		if (pricingRecordGroup == null) {
			logger.info("Ticket Price - PricingRecordGroup not found.");
			return;
		}
		
		List<PPQRdataType> ppqRdataTypes = pricingRecordGroup.getProductPricingQuotationRecord();
		if (CollectionUtils.isEmpty(ppqRdataTypes)) {
			logger.info("Ticket Price - ProductPricingQuotationRecord not found.");
			return;
		}

		for (PPQRdataType ppqRdataType: ppqRdataTypes) {
			
			// Parse passenger ids
			List<ReferenceInformationType65487S> passengerTattoos = ppqRdataType.getPassengerTattoos();
			if (CollectionUtils.isEmpty(passengerTattoos)) {
				logger.info("Ticket Price - ProductPricingQuotationRecord not found.");
				return;
			}
			List<String> paxIds = Lists.newArrayList();
			for (ReferenceInformationType65487S passengerTattoo: passengerTattoos) {
				ReferencingDetailsTypeI passengerReference = passengerTattoo.getPassengerReference();
				if (passengerReference == null) {
					logger.info("Ticket Price - passengerTattoo not found.");
					return;
				}
				paxIds.add(passengerReference.getValue());
			}
			
			
			DocumentDetailsGroup documentDetailsGroup = ppqRdataType.getDocumentDetailsGroup();
			if (documentDetailsGroup == null) {
				logger.info("Ticket Price - DocumentDetailsGroup not found.");
				return;
			}
			
			// Parse segment ids
			List<CouponDetailsGroup> couponDetailsGroups = documentDetailsGroup.getCouponDetailsGroup();
			if (CollectionUtils.isEmpty(couponDetailsGroups)) {
				logger.info("Ticket Price - couponDetailsGroups not found.");
				return;
			}
			List<String> segmentIds = Lists.newArrayList();
			for (CouponDetailsGroup couponDetailsGroup: couponDetailsGroups) {
				ReferenceInformationTypeI79009S productId = couponDetailsGroup.getProductId();
				if (productId == null) {
					logger.info("Ticket Price - productId not found.");
					return;
				}
				
				ReferencingDetailsTypeI referencingDetailsTypeI = productId.getReferenceDetails();
				if (referencingDetailsTypeI == null) {
					logger.info("Ticket Price - referencingDetailsTypeI not found.");
					return;
				}
				segmentIds.add(referencingDetailsTypeI.getValue());
			}
			
			// Parse ticket price and currency
			MonetaryInformationTypeI79012S totalFare = documentDetailsGroup.getTotalFare();
			if (totalFare == null) {
				logger.info("Ticket Price - TotalFare not found.");
				return;
			}
			MonetaryInformationDetailsTypeI121351C monetaryDetails = totalFare.getMonetaryDetails();
			if (monetaryDetails == null) {
				logger.info("Ticket Price - MonetaryDetails not found.");
				return;
			}
			float ticketPrice = -1;
			try {
				ticketPrice = Float.parseFloat(monetaryDetails.getAmount());
			} catch (Exception e) {
				logger.error("Failed to parse ticket price: " + monetaryDetails.getAmount() + " to float.", e);
			}
			String currency = monetaryDetails.getCurrency();
			
			// Assign to booking
			if (!StringUtils.isEmpty(currency) && ticketPrice != -1 &&
				!CollectionUtils.isEmpty(paxIds) && !CollectionUtils.isEmpty(segmentIds)) {
				// Ticket price info
				RetrievePnrTicketPriceInfo retrievePnrTicketPriceInfo = new RetrievePnrTicketPriceInfo();
				retrievePnrTicketPriceInfo.setCurrency(currency);
				retrievePnrTicketPriceInfo.setPrice(ticketPrice);
				retrievePnrTicketPriceInfo.setPaxIds(paxIds);
				retrievePnrTicketPriceInfo.setSegmentIds(segmentIds);
				booking.findTicketPriceInfo().add(retrievePnrTicketPriceInfo);
			}
		}
	}

	/**
	 * 
	* @Description parser APM/APB/APH/APE contact info
	* @param pnrReply
	* @param booking
	* @return void
	* @author haiwei.jia
	 */
	private void parserApContactInfo(PNRReply pnrReply, RetrievePnrBooking booking) {
		if (pnrReply == null || pnrReply.getDataElementsMaster() == null
				|| CollectionUtils.isEmpty(pnrReply.getDataElementsMaster().getDataElementsIndiv()) || booking == null) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = pnrReply.getDataElementsMaster().getDataElementsIndiv();
		for (int j = 0; j < dataElementsIndivs.size(); j ++) {
			DataElementsIndiv dataElementsIndiv = dataElementsIndivs.get(j);
			if (dataElementsIndiv == null 
					|| dataElementsIndiv.getElementManagementData() == null
					|| dataElementsIndiv.getElementManagementData().getReference() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getOtherDataFreetext())) {
				continue;
			}
			//OT number
			BigInteger qualifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
			//segment name
			String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			
			if(StringUtils.isEmpty(segmentName) 
					|| !SEGMENT_NAME_AP.equals(segmentName) 
					|| dataElementsIndiv.getOtherDataFreetext().get(0) == null
					|| dataElementsIndiv.getOtherDataFreetext().get(0).getFreetextDetail() == null){
				continue;
			}
			
			String type = dataElementsIndiv.getOtherDataFreetext().get(0).getFreetextDetail().getType();
			String freeText = dataElementsIndiv.getOtherDataFreetext().get(0).getLongFreetext();
			if(StringUtils.isEmpty(type) || StringUtils.isEmpty(freeText)){
				continue;
			}
			// check if there is AP AMADEUS-H in PNR as mini-PNR criteria
			if(AMADEUS_H.equalsIgnoreCase(freeText)) {
				booking.setApAmadeusHExist(true);
			}
			
			List<String> passengerIds = new ArrayList<>();
			if(dataElementsIndiv.getReferenceForDataElement() != null 
					&& !CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())){
				passengerIds = getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER);	
			}
			
			//set APx contact
			setApContact(booking, qualifierId, type, freeText, passengerIds);
		}
	}

	/**
	 * 
	* @Description set APx contact
	* @param booking
	* @param qualifierId
	* @param type
	* @param freeText
	* @return boolean
	* @author haiwei.jia
	 */
	private void setApContact(RetrievePnrBooking booking, BigInteger qualifierId, String type, String freeText, List<String> passengerIds) {
		boolean withSuffix =  BizRulesUtil.contactIsValidated(freeText);
		//APE contact info
		if(APX_CONTACT_TYPE_APE.equals(type)){
			//get formated text
			String text = getFormatedText(freeText, withSuffix, EMAIL_TYPE_APE); 
			if(!StringUtils.isEmpty(text)){
				RetrievePnrApEmail email = new RetrievePnrApEmail();
				email.setEmail(text);
				email.setQualifierId(qualifierId);
				email.setType(EMAIL_TYPE_APE);
				email.setOlssContact(withSuffix);
				email.setPassengerIds(passengerIds);
				booking.findApEmails().add(email);
			}
		}
		//APM/APH/APB contact info
		else if (APX_CONTACT_TYPE_APM.equals(type) 
				|| APX_CONTACT_TYPE_APH.equals(type)
				|| APX_CONTACT_TYPE_APB.equals(type)) {
			String phoneType;
			if(APX_CONTACT_TYPE_APM.equals(type)){
				phoneType = PHONE_TYPE_APM;
			}
			else if(APX_CONTACT_TYPE_APB.equals(type)){
				phoneType = PHONE_TYPE_APB;
			}
			else{
				phoneType = PHONE_TYPE_APH;
			}
			String text = getFormatedText(freeText, withSuffix, phoneType); 
			if(!StringUtils.isEmpty(text)){
				RetrievePnrApContactPhone contactPhone = new RetrievePnrApContactPhone();
				contactPhone.setPhoneNumber(BizRulesUtil.formatMobileNumber(text.trim()));
				contactPhone.setQualifierId(qualifierId);
				contactPhone.setType(phoneType);
				contactPhone.setOlssContact(withSuffix);
				contactPhone.setPassengerIds(passengerIds);
				booking.findApContactPhones().add(contactPhone);
			}
		}
	}

	/**
	 * 
	* @Description generate passegerSegments by passengers and segments
	* @param passengers
	* @param segments
	* @param passengerSegments
	* @return void
	* @author haiwei.jia
	 */
	private void generatePassengerSegment(List<RetrievePnrPassenger> passengers, List<RetrievePnrSegment> segments, List<RetrievePnrPassengerSegment> passengerSegments) {
		for(RetrievePnrPassenger passenger : passengers){
			for(RetrievePnrSegment segment : segments){
				if(passenger == null || segment == null){
					continue;
				}
				String passengerId = passenger.getPassengerID();
				String segmentId = segment.getSegmentID();
				if (!StringUtils.isEmpty(passengerId) && !StringUtils.isEmpty(segmentId)
						&& getPassengerSegmentByIds(passengerSegments, passengerId, segmentId) == null) {
					RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
					passengerSegment.setPassengerId(passengerId);
					passengerSegment.setSegmentId(segmentId);
					passengerSegments.add(passengerSegment);
				}
					
			}
		}
		
	}

	/**
	 * parser Segment
	 * @param pnrReply
	 * @param booking
	 * @param dataElementMapping
	 */
	private void parserSegment(PNRReply pnrReply, RetrievePnrBooking booking,Map<String,List<DataElement>> dataElementMapping) {
		//flight base Info
		parserSegmentInfo(pnrReply.getOriginDestinationDetails(), booking,dataElementMapping);
		//Parser segment reminder(train)
		parserSegmentReminder(pnrReply.getDataElementsMaster(), booking.getSegments());	
		//upgrade info parser
		parserUpgradeInfo(pnrReply.getDataElementsMaster(),booking,dataElementMapping);
	}

	/**
	 * parser booking upgrade info, redemption upgrade and bid upgrade
	 * @param dataElementMaster
	 * @param booking
	 */
	private void parserUpgradeInfo(DataElementsMaster dataElementMaster, RetrievePnrBooking booking,Map<String,List<DataElement>> dataElementMapping){
		//1) parser Redemption upgrade info
		parserRedUpgrade(booking);
		//2) parser Bid upgrade info
		parserBidUpgrade(booking, dataElementMapping);
		//3) parser Bookable upgrade info
		parserBookableUpgrade(booking, dataElementMapping);
	}

	/**
	 * parser bookable upgrade
	 * @param booking
	 * @param dataElementMapping
	 */
	private void parserBookableUpgrade(RetrievePnrBooking booking, Map<String, List<DataElement>> dataElementMapping) {
		List<RetrievePnrSegment> pnrSegments = booking.getSegments();
		if(CollectionUtils.isEmpty(pnrSegments) || CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BKUG))) {
			return;
		}
		List<RetrievePnrUpgradeProcessInfo> bkugInfos = new ArrayList<>();
		for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BKUG)){
			RetrievePnrUpgradeProcessInfo info = (RetrievePnrUpgradeProcessInfo) element;
			bkugInfos.add(info);
		}
		if(pnrSegments.size() == 1) {
			RetrievePnrSegment pnrSegment = pnrSegments.get(0);
			RetrievePnrUpgradeInfo upgradeInfo = new RetrievePnrUpgradeInfo();
			upgradeInfo.setUpgradeType(UpgradeType.BOOKABLEUPGRADE);
			upgradeInfo.setFromSubClass(bkugInfos.get(0).getFromSubclass());
			upgradeInfo.setToSubClass(pnrSegment.getSubClass());
			RetrievePnrBookableUpgradeInfo bookableUpgradeInfo = new RetrievePnrBookableUpgradeInfo();
			bookableUpgradeInfo.setStatus(UpgradeProgressStatus.CONFIRMED);
			upgradeInfo.setBookableUpgradeInfo(bookableUpgradeInfo);
			pnrSegment.setUpgradeInfo(upgradeInfo);
			return;
		}
		
		for(RetrievePnrSegment pnrSegment : pnrSegments){
			RetrievePnrUpgradeProcessInfo upgradeProcessInfo = bkugInfos.stream().filter(bkug -> CollectionUtils.isNotEmpty(bkug.getReferenceStList()) && bkug.getReferenceStList().contains(pnrSegment.getSegmentID()))
			.findFirst().orElse(null);
			if(upgradeProcessInfo != null){
				RetrievePnrUpgradeInfo upgradeInfo = new RetrievePnrUpgradeInfo();
				upgradeInfo.setUpgradeType(UpgradeType.BOOKABLEUPGRADE);
				upgradeInfo.setFromSubClass(bkugInfos.get(0).getFromSubclass());
				upgradeInfo.setToSubClass(pnrSegment.getSubClass());
				RetrievePnrBookableUpgradeInfo bookableUpgradeInfo = new RetrievePnrBookableUpgradeInfo();
				bookableUpgradeInfo.setStatus(UpgradeProgressStatus.CONFIRMED);
				upgradeInfo.setBookableUpgradeInfo(bookableUpgradeInfo);
				pnrSegment.setUpgradeInfo(upgradeInfo);
			}
		}
		
	}
	
	/**
	 * parser the bid upgrade info
	 * @param booking
	 * @param dataElementMapping
	 */
	private void parserBidUpgrade(RetrievePnrBooking booking, Map<String, List<DataElement>> dataElementMapping) {
		List<RetrievePnrSegment> pnrSegments = booking.getSegments();
		if(CollectionUtils.isEmpty(pnrSegments)) {
			return;
		}
		
		List<DataElement> fqugs = dataElementMapping.get(DATAELEMENT_MAPPING_KEY_FQUG);
		List<DataElement> pgugs = dataElementMapping.get(DATAELEMENT_MAPPING_KEY_PGUG);
		
		if(CollectionUtils.isEmpty(fqugs) && CollectionUtils.isEmpty(pgugs)) {
			return;
		}
		
		if(pnrSegments.size() == 1) {
			RetrievePnrSegment pnrSegment = pnrSegments.get(0);
			if(CollectionUtils.isNotEmpty(fqugs)) {
				setPnrBidUpgradeInfo(pnrSegment, fqugs.get(0), UpgradeBidStatus.SUCCESS);
			} else if(CollectionUtils.isNotEmpty(pgugs)) {
				setPnrBidUpgradeInfo(pnrSegment, pgugs.get(0), UpgradeBidStatus.REQUEST);
			}
			return;
		}
		
		for(RetrievePnrSegment pnrSegment : pnrSegments) {
			DataElement fqugElement = CollectionUtils.isEmpty(fqugs) ? null : fqugs.stream()
					.filter(fqug -> CollectionUtils.isNotEmpty(fqug.getReferenceStList()) && fqug.getReferenceStList().contains(pnrSegment.getSegmentID()))
					.findFirst().orElse(null);
			if(fqugElement != null) {
				setPnrBidUpgradeInfo(pnrSegment, fqugElement, UpgradeBidStatus.SUCCESS);
			} else if(CollectionUtils.isNotEmpty(pgugs)){
				DataElement pgugElement = pgugs.stream()
						.filter(pgug -> CollectionUtils.isNotEmpty(pgug.getReferenceStList()) && pgug.getReferenceStList().contains(pnrSegment.getSegmentID()))
						.findFirst().orElse(null);
				setPnrBidUpgradeInfo(pnrSegment, pgugElement, UpgradeBidStatus.REQUEST);
			}
		}
		
	}

	/**
	 * set pnrBidUpgradeInfo
	 * @param pnrSegment
	 * @param dataElement
	 * @param status 
	 */
	private void setPnrBidUpgradeInfo(RetrievePnrSegment pnrSegment, DataElement dataElement, UpgradeBidStatus status) {
		if(dataElement == null) {
			return;
		}
		
		RetrievePnrBidUpgradeElement fqugElement = (RetrievePnrBidUpgradeElement) dataElement;
		
		RetrievePnrUpgradeInfo upgradeInfo = pnrSegment.getUpgradeInfo();
		if(upgradeInfo == null) {
			upgradeInfo = new RetrievePnrUpgradeInfo();
			upgradeInfo.setUpgradeType(UpgradeType.BID);
			pnrSegment.setUpgradeInfo(upgradeInfo);
		}
		upgradeInfo.setFromSubClass(fqugElement.getFromSubClass());
		upgradeInfo.setToSubClass(fqugElement.getToSubClass());
		
		RetrievePnrBidUpgradeInfo bidUpgradeInfo = new RetrievePnrBidUpgradeInfo();
		bidUpgradeInfo.setStatus(status);
		upgradeInfo.setBidUpgradeInfo(bidUpgradeInfo);
	}

	/**
	 * parser the red upgrade info and remove red duplicate segment
	 * @param booking
	 * @param dataElementMapping
	 */
	private void parserRedUpgrade(RetrievePnrBooking booking){
		List<RetrievePnrSegment> redSegments = booking.getSegments().stream().filter(seg->seg.getFqtu()!=null).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(redSegments)){
			return;
		}
		//loop the redSegments to check the red upgrade status
		for (RetrievePnrSegment redSegment : redSegments) {
			RetrievePnrUpgradeInfo upgradeInfo = new RetrievePnrUpgradeInfo();
			upgradeInfo.setToSubClass(redSegment.getSubClass());
			upgradeInfo.setUpgradeType(UpgradeType.REDEMPTION);
			RetrievePnrRedUpgradeInfo redUp= new RetrievePnrRedUpgradeInfo();
			upgradeInfo.setRedUpgradeInfo(redUp);
			
			boolean confirmedStatus = !Collections.disjoint(OneAConstants.SSR_SK_CONFIRMED_STATUS, redSegment.getStatus());
			if(redSegment.getStatus() != null && confirmedStatus){
				redUp.setStatus(UpgradeRedStatus.CONFIRMED);
				redSegment.setUpgradeInfo(upgradeInfo);
			}else {
				//find the Original segment
				String key = redSegment.getDepartureTime().getPnrTime()+redSegment.getMarketCompany()+redSegment.getMarketSegmentNumber();
				RetrievePnrSegment originalSegment = booking.getSegments().stream().filter(oSeg -> (key.equals(
						oSeg.getDepartureTime().getPnrTime() + oSeg.getMarketCompany() + oSeg.getMarketSegmentNumber()) && !Objects.equals(redSegment.getSegmentID(), oSeg.getSegmentID())))
						.findFirst().orElse(null);
				//if cannot find confirmed segment, show all segment
				if(originalSegment != null && !Collections.disjoint(OneAConstants.SSR_SK_CONFIRMED_STATUS, originalSegment.getStatus())){
					upgradeInfo.setFromSubClass(originalSegment.getSubClass());
					booking.getSegments().remove(redSegment);
					redUp.setStatus(UpgradeRedStatus.WAITLIST);
					originalSegment.setUpgradeInfo(upgradeInfo);
				} 
			}
		}
	}

	/**
	 * Parser PNR element dataElementsMaster->dataElementsIndiv
	 * @param dataElementMaster
	 * @return
	 */
	private Map<String,List<DataElement>> parserDataElement(PNRReply pnrReply){
		
		DataElementsMaster dataElementMaster = pnrReply.getDataElementsMaster();
		
		Map<String, List<DataElement>> dataElementMap = new HashMap<>();
		if (dataElementMaster == null) {
			return dataElementMap;
		}
		//parser fqtu, PGUG, FQUG
		for(DataElementsIndiv dataElementsIndiv : dataElementMaster.getDataElementsIndiv()){
			DataElement dataElement = null;
			if(SEGMENT_NAME_SSR.equals(dataElementsIndiv.getElementManagementData().getSegmentName())
					|| SEGMENT_NAME_SK.equals(dataElementsIndiv.getElementManagementData().getSegmentName())){
				String ssrType = Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getType).orElse("");
				switch (ssrType) {
				case FQTU_TYPE:
					dataElement = parserFQTU(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_FQTU, dataElement);
					break;
				case PGUG_TYPE:
					dataElement = parserBidUpgradeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_PGUG, dataElement);
					break;
				case FQUG_TYPE:
					dataElement = parserBidUpgradeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_FQUG, dataElement);
					break;
				case STAFF_TYPE:
					dataElement = parserStaffInfo(dataElementsIndiv, pnrReply);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_STAFF, dataElement);
					break;
				case FLAC_TYPE:
					dataElement = parserClaimLoungeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_FLAC, dataElement);
					break;
				case BLAC_TYPE:
					dataElement = parserClaimLoungeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_BLAC, dataElement);
					break;
				case LGAB_TYPE:
					dataElement = parserPurchaseLoungeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_LGAB, dataElement);
					break;
				case LGAF_TYPE:
					dataElement = parserPurchaseLoungeInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_LGAF, dataElement);
					break;
				case BKUG_TYPE:
					dataElement = parserUpgradeProcessInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_BKUG, dataElement);
					break;
				case SSR_TYPE_INFANT:
					dataElement = parserInfantInfo(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_INFT, dataElement);
					break;
				default:
					break;
				}
			} else if (SEGMENT_NAME_RM.equals(dataElementsIndiv.getElementManagementData().getSegmentName())) {
				DataElement tempDataElement = parserOnHoldInfo(dataElementsIndiv);
				if (tempDataElement != null) {
					dataElement = tempDataElement;
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_ONHOLD, dataElement);
				} else {
					dataElement = parserMbrEntitlement(dataElementsIndiv);
					addElementToDataElementMap(dataElementMap, DATAELEMENT_MAPPING_KEY_MBRENTITLEMENT, dataElement);
				}
			}

			//parser reference info
		    parserReferenceInfoToDataElement(dataElementsIndiv, dataElement);
		}
		return dataElementMap;
	}
	
	/**
	 * 
	 * @param dataElementsIndiv
	 * @return RetrievePnrAirportUpgradeInfo
	 */
	private RetrievePnrUpgradeProcessInfo parserMbrEntitlement(DataElementsIndiv dataElementsIndiv) {
		String entitlementId = Optional.ofNullable(dataElementsIndiv.getMiscellaneousRemarks())
				.map(MiscellaneousRemarksType211S::getRemarks).map(MiscellaneousRemarkType151C::getFreetext)
				.map(FreeTextUtil::parserMbrEntitlementText).orElse(null);
		if (!StringUtils.isEmpty(entitlementId)){
			RetrievePnrUpgradeProcessInfo upgradeInfo = new RetrievePnrUpgradeProcessInfo();
			upgradeInfo.setEntitlementId(entitlementId);
			return upgradeInfo;
		}
		return null;
	}

	/**
	 * 
	 * @param dataElementsIndiv
	 * @return RetrievePnrUpgradeProcessInfo
	 */
	private RetrievePnrUpgradeProcessInfo parserUpgradeProcessInfo(DataElementsIndiv dataElementsIndiv) {
		List<String> freeTexts = Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getFreeText).orElse(null);
		if(CollectionUtils.isEmpty(freeTexts) || StringUtils.isEmpty(freeTexts.get(0).trim()) ) {
			return null;			
		}
		String[] fields = freeTexts.get(0).split(SEPARATOR_IN_FREETEXT, -1);
		if(fields.length != 5){
			logger.info("illegal free text for Airport Upgrade Info: " + freeTexts.get(0));
			return null;
		}
		RetrievePnrUpgradeProcessInfo upgradeInfo = new RetrievePnrUpgradeProcessInfo();
		upgradeInfo.setEntitlementId(fields[2]);
		upgradeInfo.setFromSubclass(fields[4]);
		upgradeInfo.setConfirmed(Boolean.TRUE);
		
		return upgradeInfo;
	}

	/**
	 * 
	 * @param dataElementsIndiv
	 * @return RetrievePnrSsrInfant
	 */
	private RetrievePnrSsrInfant parserInfantInfo(DataElementsIndiv dataElementsIndiv) {
		List<String> freeTexts = Optional
				.ofNullable(dataElementsIndiv.getServiceRequest())
				.map(SpecialRequirementsDetailsTypeI::getSsr)
				.map(SpecialRequirementsTypeDetailsTypeI::getFreeText)
				.orElse(null);
		if (CollectionUtils.isEmpty(freeTexts) || StringUtils.isEmpty(freeTexts.get(0).trim())) {
			return null;
		}

		String freeText = freeTexts.get(0);

		RetrievePnrSsrInfant infant;

		if (FreeTextUtil.isOccupyInfant(freeText)) {
			infant = new RetrievePnrSsrInfant();
			infant.setOccupySeat(true);
		} else {
			infant = new RetrievePnrSsrInfant();
			infant.setFamilyName(FreeTextUtil.getFamilyNameFromSsrInfantFreeText(freeText));
			infant.setGivenName(FreeTextUtil.getGivenNameFromSsrInfantFreeText(freeText));
			String date = FreeTextUtil.getBirthDateFromSsrInfantFreeText(freeText);
			if(!StringUtils.isEmpty(date)){
				infant.setBirthDateYear(DateUtil.convertDateFormat(date, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_YYYY));
				infant.setBirthDateMonth(DateUtil.convertDateFormat(date, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_MM));
				infant.setBirthDateDay(DateUtil.convertDateFormat(date, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_DD));
			}
		}
		return infant;
	}

	/**
	 * 
	 * @param dataElementsIndiv
	 * @return RetrievePnrLoungeInfo
	 */
	private RetrievePnrLoungeInfo parserClaimLoungeInfo(DataElementsIndiv dataElementsIndiv) {
		List<String> freeTexts = Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getFreeText).orElse(null);
		if(CollectionUtils.isEmpty(freeTexts) || StringUtils.isEmpty(freeTexts.get(0)) ) {
			return null;			
		}
		
		RetrievePnrLoungeInfo lounge = new RetrievePnrLoungeInfo();
		
		String[] fields = freeTexts.get(0).split(SEPARATOR_IN_FREETEXT, -1);
		if(fields.length != 4){
			logger.info("illegal free text for lounge pass info: " + freeTexts.get(0));
			return null;
		}
		lounge.setTier(fields[0]);
		lounge.setGuid(fields[1]);
		lounge.setEntitlementId(fields[2]);
		lounge.setPassengerType(fields[3]);
		Optional.ofNullable(dataElementsIndiv.getReferenceForDataElement()).map(ReferenceInfoType::getReference).orElse(Collections.emptyList()).stream().forEach(ref->{
			if(SEGMENT_ID_QUALIFIER.equals(ref.getQualifier())){
				lounge.setSegmentId(ref.getNumber());
			}else if(PASSENGER_ID_QUALIFIER.equals(ref.getQualifier())){
				lounge.setPassengerId(ref.getNumber());
			}
		});
		return lounge;
	}
	
	/**
	 * 
	 * @param dataElementsIndiv
	 * @return RetrievePnrLoungeInfo
	 */
	private RetrievePnrLoungeInfo parserPurchaseLoungeInfo(DataElementsIndiv dataElementsIndiv) {
		List<String> freeTexts = Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getFreeText).orElse(null);
		RetrievePnrLoungeInfo lounge = new RetrievePnrLoungeInfo();
		
		if(!CollectionUtils.isEmpty(freeTexts) && !StringUtils.isEmpty(freeTexts.get(0)) ) {
			String[] fields = freeTexts.get(0).split(SEPARATOR_IN_FREETEXT, -1);
			if(fields.length != 4){
				logger.info("illegal free text for lounge pass info: " + freeTexts.get(0));
				return null;
			}
			lounge.setTier(fields[0]);
			lounge.setGuid(fields[1]);
			lounge.setEntitlementId(fields[2]);
			lounge.setPassengerType(fields[3]);		
		}
		
		Optional.ofNullable(dataElementsIndiv.getReferenceForDataElement()).map(ReferenceInfoType::getReference).orElse(Collections.emptyList()).stream().forEach(ref->{
			if(SEGMENT_ID_QUALIFIER.equals(ref.getQualifier())){
				lounge.setSegmentId(ref.getNumber());
			}else if(PASSENGER_ID_QUALIFIER.equals(ref.getQualifier())){
				lounge.setPassengerId(ref.getNumber());
			}
		});
		
		BigInteger qulifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
		lounge.setQulifierId(qulifierId);
		
		return lounge;
	}

	/**
	 * check if any on hold info
	 * @param dataElementsIndiv
	 * @return
	 */
	private DataElement parserOnHoldInfo(DataElementsIndiv dataElementsIndiv) {

		String[] parsedOnholdInfo = Optional.ofNullable(dataElementsIndiv.getMiscellaneousRemarks())
				.map(MiscellaneousRemarksType211S::getRemarks).map(MiscellaneousRemarkType151C::getFreetext)
				.map(FreeTextUtil::parserOnHoldText).filter(strs -> !ArrayUtils.isEmpty(strs))
				.orElse(ArrayUtils.EMPTY_STRING_ARRAY);

		if (parsedOnholdInfo.length > 0) {
			RetrievePnrOnHoldInfo onholdInfo = new RetrievePnrOnHoldInfo();
			onholdInfo.setHasOnHoldRemark(true);
			if (parsedOnholdInfo[0] != null) {
				onholdInfo.setAmount(new BigDecimal(parsedOnholdInfo[0]));
			}
			onholdInfo.setCurrency(parsedOnholdInfo[1]);

			return onholdInfo;
		}

		return null;

	}
	
	/**
	 * Parser staff information
	 * @param dataElementsIndiv
	 * @return
	 */
	private DataElement parserStaffInfo(DataElementsIndiv dataElementsIndiv, PNRReply pnrReply) {

		final List<String> pids = new ArrayList<>();
		RetrievePnrStaffDetail staffDetails = new RetrievePnrStaffDetail();
		// one passenger check
		if (pnrReply.getTravellerInfo().size() != 1 && dataElementsIndiv.getReferenceForDataElement() != null) {
			pids.addAll(getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(),
					PASSENGER_ID_QUALIFIER));
		}

		// get OS element
		List<String> freeTextList = pnrReply.getDataElementsMaster().getDataElementsIndiv().stream().filter(de -> {
			if (!SEGMENT_NAME_OSI.equals(de.getElementManagementData().getSegmentName())) {
				return false;
			}
			if (CollectionUtils.isEmpty(pids) || de.getReferenceForDataElement() == null) {
				return true;
			}
			List<String> opids = getReferenceNumbers(de.getReferenceForDataElement().getReference(),
					PASSENGER_ID_QUALIFIER);
			return CollectionUtils.isEmpty(opids) || CollectionUtils.containsAny(pids,opids);
		}).flatMap(de -> de.getOtherDataFreetext().stream()).map(LongFreeTextType::getLongFreetext)
				.collect(Collectors.toList());
		String pri = null;
		if(!dataElementsIndiv.getServiceRequest().getSsr().getFreeText().isEmpty()){
			String[] skAry = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().stream().map(FreeTextUtil::getStaffSKInfoFromFreeText).filter(strAry->strAry.length>0).findFirst().orElse(null);
			pri= skAry != null ? skAry[0]:pri;
		}
	
		for (String freeText : freeTextList) {
			String[] parserdOS = FreeTextUtil.getStaffOSInfoFromFreeText(freeText);
			if (parserdOS.length > 0) {
				staffDetails.setPriority(pri);
				staffDetails.setType(StaffBookingType.codeOf(parserdOS[0]));
				break;
			}

		}
		
		// parse staff id
		staffDetails.setStaffId(FreeTextUtil.getStaffIdFromFreeText(freeTextList));
		return staffDetails;
	}
	/**
	 * parser PGUG/FQUG, only check the PGUG/FQUG which status is HK
	 * @param dataElementsIndiv
	 * @return DataElement
	 */
	private DataElement parserBidUpgradeInfo(DataElementsIndiv dataElementsIndiv) {
		if(OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())){
			return parserBidUpgradeElement(dataElementsIndiv);
		}
		return null;
	}
	
	/**
	 * parser PGUG/FQUG freeText
	 * @param dataElementsIndiv
	 * @return DataElement
	 */
	private DataElement parserBidUpgradeElement(DataElementsIndiv dataElementsIndiv) {
		List<String> freeTexts = Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getFreeText).orElse(null);
		if(CollectionUtils.isEmpty(freeTexts) || StringUtils.isEmpty(freeTexts.get(0)) ) {
			return null;			
		}
		
		RetrievePnrBidUpgradeElement element = new RetrievePnrBidUpgradeElement();
		
		String[] fields = freeTexts.get(0).split(SEPARATOR_IN_FREETEXT, -1);
		for(int i = 0; i < fields.length; i++) {
			String field = fields[i];
			switch (i) {
			case 0:
				element.setToSubClass(field);//[to be]upgraded sub class
				break;
			case 2:
				element.setFromSubClass(field);//original subclass
				break;
			default:
				break;
			}
		}
		return element;
	}

	/**
	 * Add element to the dataElementMap
	 * @param dataElementMap
	 * @param key
	 * @param dataElement
	 */
	private void addElementToDataElementMap(Map<String, List<DataElement>> dataElementMap,String key,DataElement dataElement){
		if(dataElement!=null){
			dataElementMap.computeIfAbsent(key, k->new ArrayList<>()).add(dataElement);
		}
	}
	/**
	 * Parser
	 * @param dataElementsIndiv
	 * @param dataElement
	 */
	private void parserReferenceInfoToDataElement(DataElementsIndiv dataElementsIndiv,DataElement dataElement){
		if(dataElement == null){
			return;
		}
		Optional.ofNullable(dataElementsIndiv.getReferenceForDataElement()).map(ReferenceInfoType::getReference).orElse(Collections.emptyList()).stream().forEach(ref->{
			if(SEGMENT_ID_QUALIFIER.equals(ref.getQualifier())){
				dataElement.addReferenceSt(ref.getNumber());
			}else if(PASSENGER_ID_QUALIFIER.equals(ref.getQualifier())){
				dataElement.addReferencePt(ref.getNumber());
			}else if(REFERENCE_QUALIFIER_OT.equals(ref.getQualifier())){
				dataElement.addReferenceOt(ref.getNumber());
			}
		});
	}

	/**
	 * parser FQTU, only check the fqtu which status is HK
	 * @param dataElementsIndiv
	 * @return
	 */
	private RetrievePnrFFPInfo parserFQTU(DataElementsIndiv dataElementsIndiv){
		if(OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())){
			return parserFFPInfo(dataElementsIndiv);
		}
		return null;

	}

	/**
	 * only valid FFP(fqtu,fqtv,fqtr) can be parsered
	 * @param dataElementsIndiv
	 * @return
	 */
	private RetrievePnrFFPInfo parserFFPInfo(DataElementsIndiv dataElementsIndiv) {

		String membershipNumber = Optional.ofNullable(dataElementsIndiv.getFrequentFlyerInformationGroup())
				.map(fig -> fig.getFrequentTravellerInfo().getFrequentTraveler().getMembershipNumber())
				.orElse(null);

		if (dataElementsIndiv == null || dataElementsIndiv.getServiceRequest() == null
				|| StringUtils.isEmpty(membershipNumber)) {
			return null;
		}
		List<String> cxKaTierLevels =  bizRuleConfig.getCxkaTierLevel();
		List<String> amTierLevels = bizRuleConfig.getAmTierLevel();
		List<String> topTiers = bizRuleConfig.getTopTier();
		
		RetrievePnrFFPInfo ffp = new RetrievePnrFFPInfo();
		ffp.setCompanyId(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId());
		ffp.setFfpMembershipNumber(membershipNumber);
		//no need to check null because checked empty of member ship number in the top of this method
		ffp.setFfpCompany(dataElementsIndiv.getFrequentFlyerInformationGroup().getFrequentTravellerInfo().getFrequentTraveler().getCompany());
		ffp.setTierLevel(getTierLevel(dataElementsIndiv.getFrequentFlyerInformationGroup().getFrequentTravellerInfo().getPriorityDetails(), ffp.getFfpCompany(), cxKaTierLevels, amTierLevels, topTiers));
		ffp.setQualifierId(Optional.ofNullable(dataElementsIndiv.getElementManagementData().getReference()).map(ReferencingDetailsType127526C::getNumber).orElse(null));
		ffp.setTopTier(topTiers.contains(ffp.getTierLevel()));
		ffp.setStatus(Optional.ofNullable(dataElementsIndiv.getServiceRequest()).map(SpecialRequirementsDetailsTypeI::getSsr).map(SpecialRequirementsTypeDetailsTypeI::getStatus).orElse(""));
		return ffp;
	}

	/**
	 * Get tier level
	 * @param priorityDetails
	 * @param fqtvCompany
	 * @param cxKaTierLevels
	 * @param amTierLevels
	 * @param topTiers
	 * @return
	 */
	private String getTierLevel(List<PriorityDetailsType> priorityDetails, String fqtvCompany, List<String> cxKaTierLevels, List<String> amTierLevels,
			List<String> topTiers) {
		 String tierLevel = "";
		if(CollectionUtils.isEmpty(priorityDetails)){
			 return tierLevel;
		 }
		 if (fqtvCompany.equals(COMPANY_CX) || fqtvCompany.equals(COMPANY_KA)) {
			// get MPO top tier level
			tierLevel = priorityDetails.stream()
					.filter(detail -> cxKaTierLevels.contains(detail.getTierLevel()) && topTiers.contains(detail.getTierLevel()))
					.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);

			// if tierLevel is empty, get MPO tier level
			if(StringUtils.isEmpty(tierLevel)){
				tierLevel = priorityDetails.stream()
						.filter(detail -> cxKaTierLevels.contains(detail.getTierLevel()))
						.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);
			}
			// if tierLevel is empty, get AM tier level
			if (StringUtils.isEmpty(tierLevel)) {
				tierLevel = priorityDetails.stream()
						.filter(detail -> detail != null && amTierLevels.contains(detail.getTierLevel()))
						.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);
			}

		 }
			//get one word tier
			if(StringUtils.isEmpty(tierLevel)){
				tierLevel = priorityDetails.stream()
						.filter(detail -> topTiers.contains(detail.getTierLevel()))
						.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);
			}

			if(StringUtils.isEmpty(tierLevel)){
				tierLevel = priorityDetails.get(0).getTierLevel();
			}

		return tierLevel;
	}








	/**
	 * Parser segment reminder(train)
	 * @param dataElementsMaster
	 * @param segments
	 */
	private void parserSegmentReminder(DataElementsMaster dataElementsMaster, List<RetrievePnrSegment> segments) {
		if(CollectionUtils.isEmpty(segments) || dataElementsMaster == null
				|| CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		for(RetrievePnrSegment segment : segments) {
			if(OneAConstants.EQUIPMENT_TRN.equals(segment.getAirCraftType())){
				List<String> reminders = new ArrayList<>();
				List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
				for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
					if(dataElementsIndiv == null || !SEGMENT_NAME_SSR.equals(dataElementsIndiv.getElementManagementData().getSegmentName())
							|| dataElementsIndiv.getServiceRequest() == null
							|| !OneAConstants.OTHS.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())
							|| CollectionUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText())) {
						continue;
					}
					reminders.add(org.apache.commons.lang.StringUtils.join(dataElementsIndiv.getServiceRequest().getSsr().getFreeText(), ""));
				}
				segment.setTrainReminder(org.apache.commons.lang.StringUtils.join(reminders, " "));
			}
		}
	}
	
	/**
	 * 
	 * @Description parser ssr and sk types
	 * @param pnrReply
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 */
	private void parserFRBKSK(PNRReply pnrReply, RetrievePnrBooking booking) {
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> allDataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		
		List<DataElementsIndiv> ssrSkDataElementsIndivs = allDataElementsIndivs.stream().filter(dataElementsIndiv->dataElementsIndiv.getElementManagementData().getSegmentName() != null
				&& dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_SK)
				&& dataElementsIndiv.getServiceRequest() != null).collect(Collectors.toList());
		
		for (DataElementsIndiv dataElementsIndiv : ssrSkDataElementsIndivs) {
			
			SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
			if(SK_TYPE_FRBK.equals(ssr.getType())) {
				booking.setFrbkSK(true);
				break;
			}
			
				
		}	
	}

	/**
	 * 
	 * @Description parser ssr and sk types
	 * @param pnrReply
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 */
	private void parserSsrSkList(PNRReply pnrReply, RetrievePnrBooking booking) {
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> allDataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		
		List<DataElementsIndiv> ssrSkDataElementsIndivs = allDataElementsIndivs.stream().filter(dataElementsIndiv->dataElementsIndiv.getElementManagementData().getSegmentName() != null
				&&(dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_SSR) || dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_SK))
				&& dataElementsIndiv.getServiceRequest() != null).collect(Collectors.toList());
		
		for (DataElementsIndiv dataElementsIndiv : ssrSkDataElementsIndivs) {
			//ssr segment name, eg. SSR,SK
			String ssrSegmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			
			SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
			if(StringUtils.isEmpty(ssr.getType())) {
				continue;
			}
			//ssr type, eg. CBBG,XLWR
			String ssrType = ssr.getType();
			String companyId=ssr.getCompanyId();
			String status = ssr.getStatus();
			String freeText = null;
			if(!CollectionUtils.isEmpty(ssr.getFreeText())) {
				freeText = ssr.getFreeText().get(0);
			}
			
			if(OneAConstants.MAAS.equals(ssrType) && MMBBizruleConstants.NON_AIR_SECTOR_CARRIER_CODE_DEUTSCHE_BAHN.equals(companyId)) {
				continue;
			}
			
			BigInteger qulifierId = null;
			if(dataElementsIndiv.getElementManagementData().getReference() != null) {
				qulifierId =dataElementsIndiv.getElementManagementData().getReference().getNumber();
			}
			List<ReferencingDetailsType111975C> references =  new ArrayList<>();
			if(dataElementsIndiv.getReferenceForDataElement() != null){
				references = dataElementsIndiv.getReferenceForDataElement().getReference();
			}
			List<RetrievePnrDataElements> ssrSkList = buildDataElementsStructure(references);
			populateSSrskDeatilInfoToBooking(ssrSkList, ssrSegmentName, ssrType,  freeText, qulifierId, companyId, status, booking);
				
		}	
	}
	
	/**
	 * Populate the ssr sk details info and add them to booking model
	 * @param ssrSkList
	 * @param ssrSegmentName
	 * @param ssrType
	 * @param freeText
	 * @param qulifierId
	 * @param booking
	 */
	private void populateSSrskDeatilInfoToBooking(List<RetrievePnrDataElements> ssrSkList,String ssrSegmentName,String ssrType,String freeText,BigInteger qulifierId,String companyId,String status,RetrievePnrBooking booking){
		if(CollectionUtils.isEmpty(ssrSkList)){
			return;
		}
		for (RetrievePnrDataElements pnrSsrSkDetail : ssrSkList) {
			pnrSsrSkDetail.setType(ssrType);
			pnrSsrSkDetail.setCompanyId(companyId);
			pnrSsrSkDetail.setStatus(status);
			pnrSsrSkDetail.setFreeText(freeText);
			// TKTL or TKXL or ADTK checking
			pnrSsrSkDetail.setSegmentName(ssrSegmentName);
			pnrSsrSkDetail.setQulifierId(qulifierId);
			
			if (SEGMENT_NAME_SSR.equals(ssrSegmentName)) {
				booking.findSsrList().add(pnrSsrSkDetail);
			} else if (SEGMENT_NAME_SK.equals(ssrSegmentName)) {
				booking.findSkList().add(pnrSsrSkDetail);
			}
		}
		
	}
	
	private void createRetrievePnrDataElement(List<RetrievePnrDataElements> dataElementList,String passengerId,String segmentId){
		RetrievePnrDataElements dataElement = new RetrievePnrDataElements();
		dataElement.setSegmentId(segmentId);
		dataElement.setPassengerId(passengerId);
		dataElementList.add(dataElement); 
	}
	
	/**
	 * Build ssr sk list, 
	 * @param references
	 * @return
	 */
	private List<RetrievePnrDataElements> buildDataElementsStructure( List<ReferencingDetailsType111975C> references) {
		
		List<RetrievePnrDataElements> dataElementList=new ArrayList<>();
		if (CollectionUtils.isEmpty(references)) {
			createRetrievePnrDataElement(dataElementList,null, null);
		} else {
			// pts and sts in references
			List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
			List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);

			int ptCount = passengerIds.size();
			int stCount = segmentIds.size();
			if (ptCount == 0 && stCount == 0) {
				return dataElementList;
			}

			// if stCount=0 , add ssr/sk type to list in passenger;
			if (stCount == 0) {
				passengerIds.stream().forEach(pt -> createRetrievePnrDataElement(dataElementList,pt, null));
			}
			// if ptCount=0, add ssr/sk type to list in segment
			else if (ptCount == 0) {
				segmentIds.stream().forEach(st -> createRetrievePnrDataElement(dataElementList, null, st));
			}
			// if ptCount!=0 and stCount!=0, add ssr/sk type to list in
			// passengerSegment
			else {
				passengerIds.stream()
						.forEach(pt -> segmentIds.stream().forEach(st -> createRetrievePnrDataElement(dataElementList, pt, st)));
			}
		}
		return dataElementList;
	}
	
	private void parserPassengerSegment(PNRReply pnrReply, RetrievePnrBooking booking, Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp, Map<String, List<DataElement>> dataElementMapping) {
		parserFFPInfos(pnrReply.getDataElementsMaster(), booking);
		parserTravelDocAndTS(pnrReply.getDataElementsMaster(), booking);
		parserPassengerSegmentInfo(pnrReply, booking, babyMealMapTemp);
		removeInvalidPassengerSegment(booking);
		parserPickUpNumber(pnrReply.getDataElementsMaster(), booking);
		parserClaimedLounges(dataElementMapping, booking);
		parserLoungePayment(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking.getPassengerSegments());
		parserAirportUpgradeInfo(pnrReply, booking);
		parserUpgradeProcessStatus(dataElementMapping, booking);
	}
	
	/**
	 * 
	 * @param dataElementMapping
	 * @param booking
	 */
	private void parserUpgradeProcessStatus(Map<String, List<DataElement>> dataElementMapping,
			RetrievePnrBooking booking) {
		if(CollectionUtils.isEmpty(booking.getPassengerSegments()) || (CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BKUG)) 
				&& CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_MBRENTITLEMENT)))) {
			return;
		}
		List<RetrievePnrUpgradeProcessInfo> bkugInfos = new ArrayList<>();
		List<RetrievePnrUpgradeProcessInfo> mbrInfos = new ArrayList<>();
		parserPnrUpgradeProcessInfo(bkugInfos, mbrInfos, dataElementMapping);
		if(booking.getPassengerSegments().size() == 1){
			parserUpgradeProcessInfoForOnlyOnePassengerSegment(booking, bkugInfos, mbrInfos);
			return;
		}
		parserUpgradeProcessInfoToPassengerSegments(booking, bkugInfos, mbrInfos);
	}

	/**
	 * 
	 * @param booking
	 * @param bkugInfos
	 * @param mbrInfos
	 */
	private void parserUpgradeProcessInfoToPassengerSegments(RetrievePnrBooking booking,
			List<RetrievePnrUpgradeProcessInfo> bkugInfos, List<RetrievePnrUpgradeProcessInfo> mbrInfos) {
		for (RetrievePnrPassengerSegment passengerSegment : booking.getPassengerSegments()){
			if(!CollectionUtils.isEmpty(bkugInfos)){
				RetrievePnrUpgradeProcessInfo bkugInfo = bkugInfos.stream().filter(info -> !CollectionUtils.isEmpty(info.getReferenceStList()) 
						&& info.getReferenceStList().contains(passengerSegment.getSegmentId())).findFirst().orElse(null);
				if(bkugInfo != null){
					passengerSegment.setUpgradeProcessInfo(bkugInfo);
					continue;
				}
			}
			
			if(!CollectionUtils.isEmpty(mbrInfos)){
				RetrievePnrUpgradeProcessInfo mbrInfo = mbrInfos.stream().filter(info -> !CollectionUtils.isEmpty(info.getReferencePtList()) && info.getReferencePtList().contains(passengerSegment.getPassengerId())
						&& !CollectionUtils.isEmpty(info.getReferenceStList()) && info.getReferenceStList().contains(passengerSegment.getSegmentId())).findFirst().orElse(null);;
				if(mbrInfo != null){
					passengerSegment.setUpgradeProcessInfo(mbrInfo);
				}
			}
		}
	}

	/**
	 * 
	 * @param booking
	 * @param bkugInfos
	 * @param mbrInfos
	 */
	private void parserUpgradeProcessInfoForOnlyOnePassengerSegment(RetrievePnrBooking booking,
			List<RetrievePnrUpgradeProcessInfo> bkugInfos, List<RetrievePnrUpgradeProcessInfo> mbrInfos) {
		if(!CollectionUtils.isEmpty(bkugInfos)){
			booking.getPassengerSegments().get(0).setUpgradeProcessInfo(bkugInfos.get(0));
			return;
		}
		if(!CollectionUtils.isEmpty(mbrInfos)){
			booking.getPassengerSegments().get(0).setUpgradeProcessInfo(mbrInfos.get(0));
		}
		
	}

	/**
	 * 
	 * @param bkugInfos
	 * @param mbrInfos
	 * @param dataElementMapping
	 */
	private void parserPnrUpgradeProcessInfo(List<RetrievePnrUpgradeProcessInfo> bkugInfos,
			List<RetrievePnrUpgradeProcessInfo> mbrInfos, Map<String, List<DataElement>> dataElementMapping) {
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BKUG))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BKUG)){
				RetrievePnrUpgradeProcessInfo info = (RetrievePnrUpgradeProcessInfo) element;
				bkugInfos.add(info);
			}
		}
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_MBRENTITLEMENT))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_MBRENTITLEMENT)){
				RetrievePnrUpgradeProcessInfo info = (RetrievePnrUpgradeProcessInfo) element;
				mbrInfos.add(info);
			}
		}
	}

	/**
	 * Build AirportUpgradeInfo for each passegnerSegment
	 * @param pnrReply 
	 * @param booking
	 */
	private void parserAirportUpgradeInfo(PNRReply pnrReply, RetrievePnrBooking booking) {
		parseAirportUpgradeSsrType(pnrReply, booking);
		parseAirportUpgradeNewCabinClass(booking);
	}
	

	/**
	 * Parse new CabinClass for airportUpgradeInfo
	 * 
	 * @param booking
	 */
	private void parseAirportUpgradeNewCabinClass(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		
		for(RetrievePnrPassengerSegment passengerSegment : booking.getPassengerSegments()) {
			if(passengerSegment == null || passengerSegment.getAirportUpgradeInfo() == null
					|| StringUtils.isEmpty(passengerSegment.getAirportUpgradeInfo().getUpgradeSsrType())) {
				continue;
			}
			setAirportUpgradeNewCabinClass(passengerSegment.getAirportUpgradeInfo());
		}
	}

	/**
	 * Set new cabinClass into airportUpgradeInfo
	 * Mapping of ssrType-cabinClass: AUGW-W, AUGJ-J, AUGR-J, AUGF-F
	 * 
	 * @param airportUpgradeInfo
	 */
	private void setAirportUpgradeNewCabinClass(RetrievePnrAirportUpgradeInfo airportUpgradeInfo) {
		String ssrType = airportUpgradeInfo.getUpgradeSsrType();
		String newCabinClass = null;
		if(OneAConstants.SSR_TYPE_AUGF.equals(ssrType)) {
			newCabinClass = OneAConstants.CABIN_CLASS_FIRST;
		} else if(OneAConstants.SSR_TYPE_AUGR.equals(ssrType)) {
			newCabinClass = OneAConstants.CABIN_CLASS_BUSINESS;
		} else if(OneAConstants.SSR_TYPE_AUGJ.equals(ssrType)) {
			newCabinClass = OneAConstants.CABIN_CLASS_BUSINESS;
		} else if(OneAConstants.SSR_TYPE_AUGW.equals(ssrType)) {
			newCabinClass = OneAConstants.CABIN_CLASS_PREMIUM_ECONOMY;
		}
		airportUpgradeInfo.setNewCabinClass(newCabinClass);
	}

	/**
	 * Parse SsrType[AUGF, AUGR, AUGJ, AUGW] for airportUpgradeInfo
	 * 
	 * @param pnrReply 
	 * @param booking
	 */
	private void parseAirportUpgradeSsrType(PNRReply pnrReply, RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments())) {
			return;
		}
		List<RetrievePnrPassengerSegment> passegnerSegments = booking.getPassengerSegments();
		List<RetrievePnrSegment> segments = booking.getSegments();
		
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		
		List<DataElementsIndiv> dataElementsIndivs = getDataElementsBySsrNameAndTypeAndStatus(dataElementsMaster.getDataElementsIndiv(), SEGMENT_NAME_SSR, OneAConstants.SEGMENTSTATUS_HK, AIRPORT_UPGRADE_SSR_TYPES);
		
		for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(dataElementsIndiv == null) {
				continue;
			}
			String upgradeSsrType = dataElementsIndiv.getServiceRequest().getSsr().getType();
			setUpgradeSsrType2PassengerSegments(getAirportUpgradeMatchedPassengerSegments(passegnerSegments, segments, dataElementsIndiv), upgradeSsrType);
		}
	}
	
	/**
	 * Get matched passengerSegments which can be set airport upgrade ssrType.
	 * 
	 * @param passegnerSegments
	 * @param segments
	 * @param dataElementsIndiv
	 * @return
	 */
	private List<RetrievePnrPassengerSegment> getAirportUpgradeMatchedPassengerSegments(List<RetrievePnrPassengerSegment> passegnerSegments, List<RetrievePnrSegment> segments, DataElementsIndiv dataElementsIndiv) {
		if(dataElementsIndiv.getReferenceForDataElement() == null 
				|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
			//if can't find any ST/PT under dataElementsIndiv, add into all passegnerSegment.
			return passegnerSegments;
		}
		
		List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		int ptCount = passengerIds.size();
		int stCount = segmentIds.size();
		
		List<RetrievePnrPassengerSegment> matchedPassengerSegments = null;
		if(stCount == 0) {
			if(ptCount == 0) {//NO ST/PT, all passegnerSegments matched
				matchedPassengerSegments = passegnerSegments;
			} else if(segments.size() == 1) {//NO ST, but have PT and booking have only 1 segment, PT related passegnerSegments matched
				matchedPassengerSegments = getPassengerSegmentsByPassengerIds(passegnerSegments, passengerIds);
			}
		} else {
			if(ptCount == 0) {//NO PT, but have ST, ST related passegnerSegments matched
				matchedPassengerSegments = getPassengerSegmentsBySegmentIds(passegnerSegments, segmentIds);
			} else {//have PT & ST, both related passegnerSegments matched
				matchedPassengerSegments = getPassengerSegmentsByIds(passegnerSegments, passengerIds, segmentIds);
			}
		}
		return matchedPassengerSegments;
	}
	
	/**
	 * Get list of passengerSegments by passengerIds and segmentIds
	 * 
	 * @param passegnerSegments
	 * @param passengerIds
	 * @param segmentIds
	 * @return
	 */
	private List<RetrievePnrPassengerSegment> getPassengerSegmentsByIds(List<RetrievePnrPassengerSegment> passegnerSegments,
			List<String> passengerIds, List<String> segmentIds) {
		if(CollectionUtils.isEmpty(passegnerSegments) 
				|| CollectionUtils.isEmpty(passengerIds)
				|| CollectionUtils.isEmpty(segmentIds)) {
			return Collections.emptyList();
		}
		
		List<RetrievePnrPassengerSegment> results = new ArrayList<>();
		for(String segmentId : segmentIds) {
			for(String passengerId : passengerIds) {
				RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passegnerSegments, passengerId, segmentId);
				if(passengerSegment != null) {
					results.add(passengerSegment);
				}
			}
		}
		
		return results;
	}

	/**
	 * Set upgrade ssrType into list of passengerSegments
	 * 
	 * @param passengerSegments
	 * @param upgradeSsrType
	 */
	private void setUpgradeSsrType2PassengerSegments(List<RetrievePnrPassengerSegment> passengerSegments, String upgradeSsrType) {
		if(CollectionUtils.isEmpty(passengerSegments)) {
			return;
		}
		passengerSegments.stream().forEach(ps -> setAirportUpgradeSsrType(ps, upgradeSsrType));
	}
	
	/**
	 * Set airportUpgradeSsrType into airportUpgradeInfo under passengerSegment
	 * 
	 * @param passengerSegment
	 * @param upgradeSsrType
	 */
	private void setAirportUpgradeSsrType(RetrievePnrPassengerSegment passengerSegment, String upgradeSsrType) {
		if(StringUtils.isEmpty(passengerSegment) || StringUtils.isEmpty(upgradeSsrType)) {
			return;
		}
		
		RetrievePnrAirportUpgradeInfo airportUpgradeInfo = passengerSegment.getAirportUpgradeInfo();
		
		/** 
		 * order DESC: AUGF -> AUGR -> AUGJ -> AUGW 
		 * eg. 
		 * 	when value already is AUGF, do nothing;
		 *  if not AUGF, but new value is AUGF, then replace the value with new value(AUGF)
		 * */
		if(airportUpgradeInfo == null || StringUtils.isEmpty(airportUpgradeInfo.getUpgradeSsrType())) {
			passengerSegment.findAirportUpgradeInfo().setUpgradeSsrType(upgradeSsrType);
		} else if(OneAConstants.SSR_TYPE_AUGF.equals(upgradeSsrType)) {
			airportUpgradeInfo.setUpgradeSsrType(upgradeSsrType);
		} else if(OneAConstants.SSR_TYPE_AUGR.equals(upgradeSsrType)) {
			airportUpgradeInfo.setUpgradeSsrType(upgradeSsrType);
		}else if(OneAConstants.SSR_TYPE_AUGJ.equals(upgradeSsrType)) {
			airportUpgradeInfo.setUpgradeSsrType(upgradeSsrType);
		}else {
			airportUpgradeInfo.setUpgradeSsrType(upgradeSsrType);
		}
	}

	/**
	 * get list of passengerSegments by list of passengerIds
	 * 
	 * @param passegnerSegments
	 * @param passengerIds
	 * @return
	 */
	private List<RetrievePnrPassengerSegment> getPassengerSegmentsByPassengerIds(List<RetrievePnrPassengerSegment> passegnerSegments, List<String> passengerIds) {
		if(CollectionUtils.isEmpty(passegnerSegments) || CollectionUtils.isEmpty(passengerIds)) {
			return Collections.emptyList();
		}
		return passegnerSegments.stream().filter(ps -> passengerIds.contains(ps.getPassengerId())).collect(Collectors.toList());
	}
	
	/**
	 * get list of passengerSegments by list of segmentIds
	 * 
	 * @param passegnerSegments
	 * @param segmentIds
	 * @return
	 */
	private List<RetrievePnrPassengerSegment> getPassengerSegmentsBySegmentIds(List<RetrievePnrPassengerSegment> passegnerSegments, List<String> segmentIds) {
		if(CollectionUtils.isEmpty(passegnerSegments) || CollectionUtils.isEmpty(segmentIds)) {
			return Collections.emptyList();
		}
		return passegnerSegments.stream().filter(ps -> segmentIds.contains(ps.getSegmentId())).collect(Collectors.toList());
	}
	
	/**
	 * Get list of DataElementsIndivs by ssrSegmentName, status and list of ssrTypes
	 * order by qualifierNumber DESC.
	 * 
	 * @param dataElementsIndivs
	 * @param ssrSegmentName
	 * @param status
	 * @param ssrType
	 * @return
	 */
	private List<DataElementsIndiv> getDataElementsBySsrNameAndTypeAndStatus(List<DataElementsIndiv> dataElementsIndivs, String ssrSegmentName, String status, String... ssrType) {
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return Collections.emptyList();
		}
		
		return dataElementsIndivs.stream().filter(dataElementsIndiv -> dataElementContainsTypeAndStatus(dataElementsIndiv, ssrSegmentName, status, ssrType))
				.sorted((d1, d2)-> compareByQualifierNumber(d2, d1)).collect(Collectors.toList());
	}
	
	/**
	 * Compare DataElementsIndivs by qualifierNumber
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	private int compareByQualifierNumber(DataElementsIndiv d1, DataElementsIndiv d2) {
		BigInteger qualifierNumber1 = getQualifierNumber(d1);
		BigInteger qualifierNumber2 = getQualifierNumber(d2);
		
		if(qualifierNumber1 == null && qualifierNumber2 == null) {
			return 0;
		} else if(qualifierNumber1 == null) {
			return -1;
		} else if(qualifierNumber2 == null) {
			return 1;
		} else {
			return qualifierNumber1.compareTo(qualifierNumber2);
		}
	}
	
	/**
	 * Get qualifierNumber from dataElementsIndiv
	 * 
	 * @param dataElementsIndiv
	 * @return
	 */
	private BigInteger getQualifierNumber(DataElementsIndiv dataElementsIndiv) {
		if(dataElementsIndiv == null || dataElementsIndiv.getElementManagementData().getReference() == null) {
			return null;
		}
		
		return dataElementsIndiv.getElementManagementData().getReference().getNumber();
	}
	

	/**
	 * Judge DataElementsIndiv contains ssrSegmentName, status or list of ssrTypes.
	 * ssrSegmentName, status and ssrType are all optional.
	 * 
	 * @param dataElementsIndiv
	 * @param ssrSegmentName
	 * @param status
	 * @param ssrType
	 * @return
	 */
	private boolean dataElementContainsTypeAndStatus(DataElementsIndiv dataElementsIndiv, String ssrSegmentName, String status, String... ssrType) {
		if(dataElementsIndiv == null) {
			return false;
		}
		
		if(!StringUtils.isEmpty(ssrSegmentName) && !ssrSegmentName.equals(dataElementsIndiv.getElementManagementData().getSegmentName())) {
			return false;
		}
		
		if(!StringUtils.isEmpty(status) &&
				(dataElementsIndiv.getServiceRequest() == null || !status.equals(dataElementsIndiv.getServiceRequest().getSsr().getStatus()))) {
			return false;
		}
		
		if(ssrType != null) {
			if(dataElementsIndiv.getServiceRequest() == null) {
				return false;
			}
			List<String> ssrTypes = Arrays.asList(ssrType);
			if(!ssrTypes.contains(dataElementsIndiv.getServiceRequest().getSsr().getType())) {
				return false;
			}
		}
		
		return true;
	}

	private void parserClaimedLounges(Map<String, List<DataElement>> dataElementMapping, RetrievePnrBooking booking) {
		if (CollectionUtils.isEmpty(booking.getPassengerSegments()) || (CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_FLAC))
				&& CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BLAC))
				&& CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAB))
				&& CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAF)))) {
			return;
		}
		List<RetrievePnrLoungeInfo> flacLoungeInfos = new ArrayList<>();
		List<RetrievePnrLoungeInfo> blacLoungeInfos = new ArrayList<>();
		List<RetrievePnrLoungeInfo> lgafLoungeInfos = new ArrayList<>();
		List<RetrievePnrLoungeInfo> lgabLoungeInfos = new ArrayList<>();
		parserRetrievePnrLoungeInfos(dataElementMapping, flacLoungeInfos, blacLoungeInfos, lgafLoungeInfos, lgabLoungeInfos);
		if(booking.getPassengerSegments().size() == 1){
			parserForOnlyOnePassengerSegment(booking, flacLoungeInfos, blacLoungeInfos, lgafLoungeInfos, lgabLoungeInfos);
			return;
		}
		parserClaimedLoungesToPassengerSegements(booking, flacLoungeInfos, blacLoungeInfos, lgafLoungeInfos, lgabLoungeInfos);
	}

	private void parserForOnlyOnePassengerSegment(RetrievePnrBooking booking,
			List<RetrievePnrLoungeInfo> flacLoungeInfos, List<RetrievePnrLoungeInfo> blacLoungeInfos,
			List<RetrievePnrLoungeInfo> lgafLoungeInfos, List<RetrievePnrLoungeInfo> lgabLoungeInfos) {
		if(!CollectionUtils.isEmpty(flacLoungeInfos)){
			booking.getPassengerSegments().get(0).addClaimedLounge(flacLoungeInfos.get(0));
		}
		
		if(!CollectionUtils.isEmpty(blacLoungeInfos)){
			booking.getPassengerSegments().get(0).addClaimedLounge(blacLoungeInfos.get(0));
		}
		
		if(!CollectionUtils.isEmpty(lgafLoungeInfos)){
			booking.getPassengerSegments().get(0).addPurchasedLounge(lgafLoungeInfos.get(0));
		}
		
		if(!CollectionUtils.isEmpty(lgabLoungeInfos)){
			booking.getPassengerSegments().get(0).addPurchasedLounge(lgabLoungeInfos.get(0));
		}
	}

	private void parserClaimedLoungesToPassengerSegements(RetrievePnrBooking booking,
			List<RetrievePnrLoungeInfo> flacLoungeInfos, List<RetrievePnrLoungeInfo> blacLoungeInfos,
			List<RetrievePnrLoungeInfo> lgafLoungeInfos, List<RetrievePnrLoungeInfo> lgabLoungeInfos) {
		for (RetrievePnrPassengerSegment passengerSegment : booking.getPassengerSegments()){
			if(!CollectionUtils.isEmpty(flacLoungeInfos)){
				RetrievePnrLoungeInfo retrievePnrLoungeInfo = getMatchedLoungeInfo(flacLoungeInfos, passengerSegment);
				if(retrievePnrLoungeInfo != null){
					passengerSegment.addClaimedLounge(retrievePnrLoungeInfo);
				}
			}
			
			if(!CollectionUtils.isEmpty(blacLoungeInfos)){
				RetrievePnrLoungeInfo retrievePnrLoungeInfo = getMatchedLoungeInfo(blacLoungeInfos, passengerSegment);
				if(retrievePnrLoungeInfo != null){
					passengerSegment.addClaimedLounge(retrievePnrLoungeInfo);
				}
			}
			
			if(!CollectionUtils.isEmpty(lgafLoungeInfos)){
				RetrievePnrLoungeInfo retrievePnrLoungeInfo = getMatchedLoungeInfo(lgafLoungeInfos, passengerSegment);
				if(retrievePnrLoungeInfo != null){
					passengerSegment.addPurchasedLounge(retrievePnrLoungeInfo);
				}
			}
			
			if(!CollectionUtils.isEmpty(lgabLoungeInfos)){
				RetrievePnrLoungeInfo retrievePnrLoungeInfo = getMatchedLoungeInfo(lgabLoungeInfos, passengerSegment);
				if(retrievePnrLoungeInfo != null){
					passengerSegment.addPurchasedLounge(retrievePnrLoungeInfo);
				}
			}
		}
	}

	private void parserRetrievePnrLoungeInfos(Map<String, List<DataElement>> dataElementMapping,
			List<RetrievePnrLoungeInfo> flacLoungeInfos, List<RetrievePnrLoungeInfo> blacLoungeInfos,
			List<RetrievePnrLoungeInfo> lgafLoungeInfos, List<RetrievePnrLoungeInfo> lgabLoungeInfos) {
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_FLAC))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_FLAC)){
				RetrievePnrLoungeInfo ele = (RetrievePnrLoungeInfo) element;
				ele.setType(FLAC_TYPE);
				flacLoungeInfos.add(ele);
			}
		}
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BLAC))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_BLAC)){
				RetrievePnrLoungeInfo ele = (RetrievePnrLoungeInfo) element;
				ele.setType(BLAC_TYPE);
				blacLoungeInfos.add(ele);
			}
		}
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAB))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAB)){
				RetrievePnrLoungeInfo ele = (RetrievePnrLoungeInfo) element;
				ele.setType(BLAC_TYPE);
				lgabLoungeInfos.add(ele);
			}
		}
		if(!CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAF))){
			for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_LGAF)){
				RetrievePnrLoungeInfo ele = (RetrievePnrLoungeInfo) element;
				ele.setType(FLAC_TYPE);
				lgafLoungeInfos.add(ele);
			}
		}
	}

	private RetrievePnrLoungeInfo getMatchedLoungeInfo(List<RetrievePnrLoungeInfo> loungeInfos,
			RetrievePnrPassengerSegment passengerSegment) {
		return loungeInfos.stream()
				.filter(info -> passengerSegment.getSegmentId().equals(info.getSegmentId()) && passengerSegment.getPassengerId().equals(info.getPassengerId()))
				.findFirst().orElse(null);
	}

	private void parserFFPInfos(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {

		if (dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();

		for (int i = 0; i < dataElementsIndivs.size(); i++) {
			if(dataElementsIndivs.get(i).getFrequentFlyerInformationGroup() != null && dataElementsIndivs.get(i).getFrequentFlyerInformationGroup().getFrequentTravellerInfo() != null){
				FrequentTravellerIdentificationCodeType74327S frequentTravellerInfo = dataElementsIndivs.get(i)
						.getFrequentFlyerInformationGroup().getFrequentTravellerInfo();
				// if companyId is CX or KA, validate the status and validate ssr type is fqtv or fqtr or fqtu
				if(ssrParseJudgment(dataElementsIndivs.get(i),frequentTravellerInfo)){
					continue;
				}
				SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndivs.get(i).getServiceRequest().getSsr();
				ffpInfosHandling(dataElementsIndivs.get(i),dataElementsMaster,booking,frequentTravellerInfo,ssr);
			}

		  }
		}




	private boolean ssrParseJudgment(DataElementsIndiv dataElementsIndiv, FrequentTravellerIdentificationCodeType74327S frequentTravellerInfo) {

		if(dataElementsIndiv.getElementManagementData().getSegmentName() == null
				|| !dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_SSR)
				|| dataElementsIndiv.getReferenceForDataElement() == null
				|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())
				|| dataElementsIndiv.getServiceRequest() == null){
			return true;
		}
		SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
		if (StringUtils.isEmpty(ssr.getType()) ||!(ssr.getType().equals(FQTV_TYPE)||ssr.getType().equals(FQTU_TYPE)
				|| ssr.getType().equals(FQTR_TYPE))|| StringUtils.isEmpty(ssr.getStatus()) || StringUtils.isEmpty(ssr.getCompanyId())) {
			return true;
		}
		// if companyId is CX or KA, validate the status
		if ((COMPANY_CX.equals(ssr.getCompanyId()) || COMPANY_KA.equals(ssr.getCompanyId()))
				&& (StringUtils.isEmpty(ssr.getStatus()) || !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(ssr.getStatus()))) {
			return true;
		}

		return StringUtils.isEmpty(frequentTravellerInfo.getFrequentTraveler().getMembershipNumber());
	}

	private void ffpInfosHandling(DataElementsIndiv dataElementsIndiv, DataElementsMaster dataElementsMaster, RetrievePnrBooking booking,
			FrequentTravellerIdentificationCodeType74327S frequentTravellerInfo,
			SpecialRequirementsTypeDetailsTypeI ssr) {

		List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement()
				.getReference();

		// get OT number
		ReferencingDetailsType127526C reference = dataElementsIndiv.getElementManagementData()
				.getReference();

		BigInteger otNumber = null;
		if (reference != null && reference.getQualifier().equals(REFERENCE_QUALIFIER_OT)) {
			otNumber = reference.getNumber();
		}
		ffpParseHandling(ssr, frequentTravellerInfo, references, booking, otNumber);

		if (FQTV_TYPE.equals(ssr.getType())) {
		parserFQTVForDMP(dataElementsMaster, booking);
		}

	}

	/**
	 * replace "DM" with "DMP" when ssrType="DMP" exist in 1A response.
	 * @param dataElementsMaster
	 * @param booking
	 */
	private void parserFQTVForDMP(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();

		List<DataElementsIndiv> dataElementsIndivs = new ArrayList<>();
		List<DataElementsIndiv> list = dataElementsMaster.getDataElementsIndiv();
		for(DataElementsIndiv dataElementsIndiv : list) {
			if(dataElementsIndiv.getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())
					|| dataElementsIndiv.getServiceRequest() == null) {
				continue;
			}

			SpecialRequirementsDetailsTypeI serviceRequest = dataElementsIndiv.getServiceRequest();
			SpecialRequirementsTypeDetailsTypeI ssr = serviceRequest.getSsr();
			if(StringUtils.isEmpty(ssr.getType()) || !ssr.getType().equals(OneAConstants.DMP)
					|| StringUtils.isEmpty(ssr.getStatus()) || !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(ssr.getStatus())
					|| StringUtils.isEmpty(ssr.getCompanyId())) {
				continue;
			}
			dataElementsIndivs.add(dataElementsIndiv);
		}

		for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
			List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
			List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
			if(CollectionUtils.isEmpty(segmentIds) && !CollectionUtils.isEmpty(passengerIds)) {
				for(String passengerId : passengerIds) {
					RetrievePnrPassenger passenger = getPassengerById(passengers, passengerId);
					if(passenger != null) {
						replaceDMWithDMP(passenger.getFQTVInfos());
					}
				}
			}else if(!CollectionUtils.isEmpty(segmentIds) && !CollectionUtils.isEmpty(passengerIds)) {
				for(String passengerId : passengerIds){
					for(String segmentId : segmentIds) {
						RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, passengerId, segmentId);
						if(passengerSegment != null) {
							replaceDMWithDMP(passengerSegment.getFQTVInfos());
						}
					}
				}
			}

		}
	}

	private void ffpParseHandling(SpecialRequirementsTypeDetailsTypeI ssr, FrequentTravellerIdentificationCodeType74327S frequentTravellerInfo,
			List<ReferencingDetailsType111975C> references, RetrievePnrBooking booking, BigInteger otNumber) {
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		List<RetrievePnrPassenger> passengers = booking.getPassengers();

		String companyId = ssr.getCompanyId();
		String ffpCompany = frequentTravellerInfo.getFrequentTraveler().getCompany();
		String ffpMembershipNumber = frequentTravellerInfo.getFrequentTraveler().getMembershipNumber();
		List<PriorityDetailsType> priorityDetails = frequentTravellerInfo.getPriorityDetails();

		RetrievePnrFFPInfo ffpInfo = getValidFFPInfo(priorityDetails, companyId, ffpCompany, ffpMembershipNumber, otNumber);

		if(!StringUtils.isEmpty(ssr.getStatus()) && ffpInfo!=null){
			ffpInfo.setStatus(ssr.getStatus());
		}

		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		int stCount = segmentIds.size();
		for(String pt : passengerIds) {
			if(stCount == 0) {
				//Confirm the current passenger by passenger id and add ffp info
				addFFPInfosInPassengers(passengers,pt,ffpInfo,ssr);
			} else {
				for(String st : segmentIds) {
					//Confirm the current passengersegment by segment id and add ffp info
					addFFPInfosInPnrPassengerSegment(passengerSegments,st,pt,ffpInfo,ssr);

				}
			}
		}

	}

	private void addFFPInfosInPnrPassengerSegment(List<RetrievePnrPassengerSegment> passengerSegments, String st,
			String pt, RetrievePnrFFPInfo ffpInfo, SpecialRequirementsTypeDetailsTypeI ssr) {

		RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, pt, st);
		if(passengerSegment == null) {
			passengerSegment = new RetrievePnrPassengerSegment();
			passengerSegment.setPassengerId(pt);
			passengerSegment.setSegmentId(st);
			// Determine ssr type and set the corresponding ffp info in PassengerSegment
			setFFPInfosInPassengerSegment(passengerSegment,ffpInfo,ssr);
			passengerSegments.add(passengerSegment);
		} else {
			// Determine ssr type and set the corresponding ffp info in PassengerSegment
			setFFPInfosInPassengerSegment(passengerSegment,ffpInfo,ssr);
		}

	}

	private void addFFPInfosInPassengers(List<RetrievePnrPassenger> passengers, String pt, RetrievePnrFFPInfo ffpInfo, SpecialRequirementsTypeDetailsTypeI ssr) {

		RetrievePnrPassenger passenger = getPassengerById(passengers, pt);
		List<RetrievePnrFFPInfo> ffpInfos;
		if(passenger == null) {
			passenger = new RetrievePnrPassenger();
			ffpInfos = new ArrayList<>();
			if(ffpInfo != null){
				ffpInfos.add(ffpInfo);
			}
			// Determine ssr type and set the corresponding ffp info in Passenger
			setFFPInfosInPassenger(passenger,ffpInfos,ssr);
			passengers.add(passenger);
		} else {
			ffpInfos = passenger.getFQTVInfos();
			if(!ffpInfos.contains(ffpInfo) && ffpInfo != null){
				ffpInfos.add(ffpInfo);
			}
		}

	}

	private void setFFPInfosInPassengerSegment(RetrievePnrPassengerSegment passengerSegment, RetrievePnrFFPInfo ffpInfo,
			SpecialRequirementsTypeDetailsTypeI ssr) {
		if (FQTV_TYPE.equals(ssr.getType()) && ffpInfo != null) {
			passengerSegment.getFQTVInfos().add(ffpInfo);
		}

		if (FQTR_TYPE.equals(ssr.getType()) && ffpInfo != null) {
			passengerSegment.getFQTRInfos().add(ffpInfo);
		}
	}

	private void setFFPInfosInPassenger(RetrievePnrPassenger passenger, List<RetrievePnrFFPInfo> ffpInfos,
			SpecialRequirementsTypeDetailsTypeI ssr) {
		if (FQTV_TYPE.equals(ssr.getType())) {
			passenger.setFQTVInfos(ffpInfos);
		}

		if (FQTR_TYPE.equals(ssr.getType())) {
			passenger.setFQTRInfos(ffpInfos);
		}
	}

	/**
	 * get validated FQTVInfo
	 * @param priorityDetails
	 * @param fqtvCompany
	 * @param fqtvMembershipNumber
	 */
	/**
	 */
	private RetrievePnrFFPInfo getValidFFPInfo(List<PriorityDetailsType> priorityDetails, String companyId, String fqtvCompany,
			String fqtvMembershipNumber, BigInteger otNumber) {
		RetrievePnrFFPInfo ffpInfo = null;
		//if company is CX or KA, tier level should be validated
		if (fqtvCompany.equals(COMPANY_CX) || fqtvCompany.equals(COMPANY_KA)) {
			ffpInfo = getCxKaFQTVInfo(priorityDetails, companyId, fqtvCompany, fqtvMembershipNumber, otNumber);
		} else {
			List<String> topTiers = bizRuleConfig.getTopTier();
			String tierLevel = "";
			if(!CollectionUtils.isEmpty(priorityDetails)){
				for(int i = 0; i < priorityDetails.size(); i++){
					if(topTiers.contains(priorityDetails.get(i).getTierLevel())){
						tierLevel = priorityDetails.get(i).getTierLevel();
						break;
					}
				}
				if(StringUtils.isEmpty(tierLevel)){
					tierLevel = priorityDetails.get(0).getTierLevel();
				}
			}
			ffpInfo = new RetrievePnrFFPInfo();
			ffpInfo.setCompanyId(companyId);
			ffpInfo.setFfpCompany(fqtvCompany);
			ffpInfo.setFfpMembershipNumber(fqtvMembershipNumber);
			ffpInfo.setTopTier(topTiers.contains(tierLevel));
			ffpInfo.setQualifierId(otNumber);
			ffpInfo.setTierLevel(tierLevel);
		}
		return ffpInfo;
	}

	/**
	 * Include FQTV,FQTU,FQTR information  
	 * @param pnrReply
	 * @param booking
	 */
	private void parserFrequentFlyers(PNRReply pnrReply, RetrievePnrBooking booking){
		if(pnrReply.getDataElementsMaster() == null || CollectionUtils.isEmpty(pnrReply.getDataElementsMaster().getDataElementsIndiv())){
			return;
		}
		
		List<DataElementsIndiv> dataElementsIndivs = pnrReply.getDataElementsMaster().getDataElementsIndiv();

		for (DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(!isFrequentFlyerElement(dataElementsIndiv)){
				continue;
			}
			SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
			if(FQTV_TYPE.equals(ssr.getType())){
				
			}
		}
			 
	}
	
	/**
	 * check the data elements div is Frequent Flyer element
	 * @param dataElementsIndiv
	 * @return
	 */
	private boolean isFrequentFlyerElement(DataElementsIndiv dataElementsIndiv) {
		// element empty check
		if (!SEGMENT_NAME_SSR.equals(dataElementsIndiv.getElementManagementData().getSegmentName())
				|| dataElementsIndiv.getReferenceForDataElement() == null
				|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())
				|| dataElementsIndiv.getServiceRequest() == null) {

			return false;
		}
		// Frequent Flyer Info empty check
		SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
		if ((!FQTU_TYPE.equals(ssr.getType()) && !FQTV_TYPE.equals(ssr.getType()) && !FQTR_TYPE.equals(ssr.getType()))
				|| StringUtils.isEmpty(ssr.getStatus()) 
				|| StringUtils.isEmpty(ssr.getCompanyId())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Parser pick up number
	 * @param dataElementsMaster
	 * @param booking
	 */
	private void parserPickUpNumber(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(dataElementsIndiv.getServiceRequest() == null 
					|| dataElementsIndiv.getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())
					|| !OneAConstants.MAAS.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| CollectionUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText())
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText().get(0))
					|| (!OneAConstants.HK_STATUS.equals(dataElementsIndiv.getServiceRequest().getSsr().getStatus()) 
							&& !OneAConstants.KK_STATUS.equals(dataElementsIndiv.getServiceRequest().getSsr().getStatus()) 
							&& !OneAConstants.KL_STATUS.equals(dataElementsIndiv.getServiceRequest().getSsr().getStatus()))) {
				continue;
			}
			
			List<String> pickUpNumbers = dataElementsIndiv.getServiceRequest().getSsr().getFreeText();
			String pickUpNumber = pickUpNumbers.get(0);

			Pattern p = Pattern.compile("\\d{1,}");
	        Matcher m = p.matcher(pickUpNumber);
	        if(m.find()) {
	        	pickUpNumber = m.group();
	        }
			
			List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
			List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
			List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
			setPickUpNumber(booking, pickUpNumber, segmentIds, passengerIds);
		}
	}

	/**
	 * set pick up number
	 * @param booking
	 * @param pickUpNumber
	 * @param segmentIds
	 * @param passengerIds
	 */
	private void setPickUpNumber(RetrievePnrBooking booking, String pickUpNumber, List<String> segmentIds, List<String> passengerIds) {
		for(String segmentId : segmentIds) {
			for(String passengerId : passengerIds) {
				RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
				if(passengerSegment == null) {
					continue;
				}
				passengerSegment.setPickUpNumber(pickUpNumber);
			}
		}
	}

	/**
	 * 
	* @Description remove passengerSegment whose passenger or segment doesn't exist in booking
	* @param booking
	* @return void
	* @author haiwei.jia
	 */
	private void removeInvalidPassengerSegment(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())){
			return;
		}
		
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrSegment> segments = booking.getSegments();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		
		if(CollectionUtils.isEmpty(passengers) || CollectionUtils.isEmpty(segments)){
			booking.setPassengerSegments(Collections.emptyList());
		}
		//invalid passengerSegments
		List<RetrievePnrPassengerSegment> invalidPassengerSegments = new ArrayList<>();
		
		for(int i = 0; i < passengerSegments.size(); i++){
			boolean passengerExist = false;
			boolean segmentExist = false;
			for(RetrievePnrPassenger passenger : passengers){
				if(passenger != null 
						&& !StringUtils.isEmpty(passenger.getPassengerID())
						&& passenger.getPassengerID().equals(passengerSegments.get(i).getPassengerId())){
					passengerExist = true;
					break;
				}
			}
			for(RetrievePnrSegment segment : segments){
				if(segment != null 
						&& !StringUtils.isEmpty(segment.getSegmentID())
						&& segment.getSegmentID().equals(passengerSegments.get(i).getSegmentId())){
					segmentExist = true;
					break;
				}
			}
			if(!passengerExist || !segmentExist){
				invalidPassengerSegments.add(passengerSegments.get(i));
			}
		}
		
		for(RetrievePnrPassengerSegment invalidPassengerSegment : invalidPassengerSegments){
			passengerSegments.remove(invalidPassengerSegment);
		}
	}

	private void parserInfantInfo(RetrievePnrBooking booking, Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp) {
		//parser infant travel documents
		parserPassengerInfantTravelDoc(booking);
		parserPassengerSegmentInfantTD(booking);
		
		//parser infant address
		parserPassengerInfantAddress(booking);
		parserPassengerSegmentInfantAddress(booking);
		
		//parse meal info
		parserPassengerSegmentInfantML(booking, babyMealMapTemp);
	}
	
	/**
	 * @Description parser infant destination address and residence address in passengerSegment(product level)
	 * @param booking
	 */
	private void parserPassengerSegmentInfantAddress(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers())){
			return;
		}
		//populate 
		for(RetrievePnrPassengerSegment passengerSegment : booking.getPassengerSegments()){			
			//the passenger related to the passengerSegment
			RetrievePnrPassenger passenger = getPassengerById(booking.getPassengers(), passengerSegment.getPassengerId());
			if(passenger == null || PASSENGER_TYPE_INF.equals(passenger.getPassengerType())){
				continue;
			}
			//destination address
			List<RetrievePnrAddressDetails> desAddresses = passengerSegment.getDesAddresses();
			parserPassengerSegmentInfantAddress(booking, passengerSegment, desAddresses, ADDRESS_TYPE_DES);
			
			//residence address
			List<RetrievePnrAddressDetails> resAddresses = passengerSegment.getResAddresses();
			parserPassengerSegmentInfantAddress(booking, passengerSegment, resAddresses, ADDRESS_TYPE_RES);
		}
	}

	/**
	 * Parser PassengerSegment Infant Address
	 * @param booking
	 * @param passengerSegment
	 * @param addresses
	 */
	private void parserPassengerSegmentInfantAddress(RetrievePnrBooking booking, RetrievePnrPassengerSegment passengerSegment,
			List<RetrievePnrAddressDetails> addresses, String addressType) {
		if(!CollectionUtils.isEmpty(addresses)){
			//populate address to infant passengerSegment
			for(int i = 0; i < addresses.size(); i++){
				RetrievePnrAddressDetails address = addresses.get(i);
				if(address == null){
					continue;
				}
				if(address.isInfAddress()){
					populateAddressToInfPassengerSegment(address, booking, passengerSegment.getPassengerId(), passengerSegment.getSegmentId(), addressType);
					addresses.remove(i);
				}
			}
		}
	}

	/**
	 * 
	 * @Description parser infant destination address and residence address in passenger(customer level)
	 * @param booking
	 * @return 
	 * @author haiwei.jia
	 */
	private void parserPassengerInfantAddress(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers())){
			return;
		}
		//populate customer level address
		for(RetrievePnrPassenger passenger : booking.getPassengers()){
			//only populate infant address from parent to infant, if it is a infant passenger, continue
			if(PASSENGER_TYPE_INF.equals(passenger.getPassengerType())){
				continue;
			}
			//destination address
			List<RetrievePnrAddressDetails> desAddresses = passenger.getDesAddresses();
			parserPassengerInfantAddress(booking, passenger, desAddresses, ADDRESS_TYPE_DES);
			
			//residence address
			List<RetrievePnrAddressDetails> resAddresses = passenger.getResAddresses();
			parserPassengerInfantAddress(booking, passenger, resAddresses, ADDRESS_TYPE_RES);
		}

	}

	/**
	 * Parser Passenger Infant Address
	 * @param booking
	 * @param passenger
	 * @param addresses
	 */
	private void parserPassengerInfantAddress(RetrievePnrBooking booking, RetrievePnrPassenger passenger,
			List<RetrievePnrAddressDetails> addresses, String addressType) {
		if(!CollectionUtils.isEmpty(addresses)){
			//populate address to infant passenger
			for(int i = 0; i < addresses.size(); i++){
				RetrievePnrAddressDetails address = addresses.get(i);
				if(address == null){
					continue;
				}
				if(address.isInfAddress()){
					populateAddressToInfPassenger(address, booking.getPassengers(), passenger.getPassengerID(), addressType);
					addresses.remove(i);
				}
			}
		}
	}

	/**
	 * 
	 * @Description populate destination and residence address details from parent passengerSegment to infant passengerSegment
	 * @param addressDetails
	 * @param booking
	 * @param parentId
	 * @param segmentId
	 * @param addressType - "DES" or "RES"
	 * @param newInfantPassengers - the infant passengers created while populating the address
	 * @param newInfantPassengerSegments - the infant passengerSegments created while populating the address
	 * @return void
	 * @author haiwei.jia
	 * @return 
	 */
	private void populateAddressToInfPassengerSegment(RetrievePnrAddressDetails addressDetails,
			RetrievePnrBooking booking, String parentId, String segmentId, String addressType) {
		// get infant passenger by parent id
		List<RetrievePnrPassenger> infantPassengers = getPassengersByParentId(booking.getPassengers(), parentId);

		// if infant passegner is empty, do nothing
		if (CollectionUtils.isEmpty(infantPassengers)) {
			return;
		}

		// get infant passengerSegment by passenger id and segment id
		RetrievePnrPassengerSegment infantPassengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(),
				parentId + PASSENGER_INFANT_ID_SUFFIX, segmentId);
		// if infantPassengerSegment is null, do nothing
		if (infantPassengerSegment == null) {
			return;
		} else {
			RetrievePnrAddressDetails address = new RetrievePnrAddressDetails();
			BeanUtils.copyProperties(addressDetails, address);
			address.setInfAddress(addressDetails.isInfAddress());
			if (ADDRESS_TYPE_DES.equals(addressType)) {
				infantPassengerSegment.getDesAddresses().add(address);
			} else {
				infantPassengerSegment.getResAddresses().add(address);
			}
		}
	}

	/**
	 * 
	 * @Description populate destination and residence address details from parent passenger to infant passenger
	 * @param addressDetails
	 * @param passengers
	 * @param parentId
	 * @param addressType - "DES" or "RES"
	 * @return void
	 * @author haiwei.jia
	 */
	private void populateAddressToInfPassenger(RetrievePnrAddressDetails addressDetails,
			List<RetrievePnrPassenger> passengers, String parentId, String addressType) {
		List<RetrievePnrPassenger> infantPassengers = getPassengersByParentId(passengers, parentId);
		if (CollectionUtils.isEmpty(infantPassengers)) {
			return;
		} 
		
		RetrievePnrAddressDetails address = new RetrievePnrAddressDetails();
		BeanUtils.copyProperties(addressDetails, address);
		address.setInfAddress(addressDetails.isInfAddress());
		if (ADDRESS_TYPE_DES.equals(addressType)) {
			infantPassengers.get(0).getDesAddresses().add(address);
		} else {
			infantPassengers.get(0).getResAddresses().add(address);
		}
	}

	/**
	 * 
	 * @Description parser infant meal
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 * @param babyMealMapTemp 
	 */
	private void parserPassengerSegmentInfantML(RetrievePnrBooking booking, Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp) {
		for(RetrievePnrPassengerSegment passengerSegment : booking.getPassengerSegments()){
			//passenger id and segment id of passengerSegment
			String passengerId = passengerSegment.getPassengerId();
			String segmentId = passengerSegment.getSegmentId();
			if(babyMealMapTemp.get(passengerSegment) == null) {
				continue;
			}
			
			//if there is a BBML in meal, add the meal to infant's passengerSegment
			//get infant passenger by parent id
			RetrievePnrPassenger infant = null;
			RetrievePnrPassengerSegment infantpassengerSegment = null;
			List<RetrievePnrPassenger> infantPassengers = getPassengersByParentId(booking.getPassengers(), passengerId);
			if(CollectionUtils.isEmpty(infantPassengers)){
				// individual infants
				infant = getPassengerById(booking.getPassengers(), passengerId);
				infantpassengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
			}else {
				// infants with adult
				infant = infantPassengers.get(0);
				infantpassengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), infant.getPassengerID(), segmentId);
			}
			
			if(infantpassengerSegment == null) {
				infantpassengerSegment = new RetrievePnrPassengerSegment();
				infantpassengerSegment.setPassengerId(infant.getPassengerID());
				infantpassengerSegment.setSegmentId(segmentId);
				infantpassengerSegment.setMeal(babyMealMapTemp.get(passengerSegment));
				booking.getPassengerSegments().add(infantpassengerSegment);
			} else {
				infantpassengerSegment.setMeal(babyMealMapTemp.get(passengerSegment));
			}
			babyMealMapTemp.remove(passengerSegment);
		}
			
		
		
	}

	/**
	 * add infant TravelDoc(abbreviation: TD) info into passengerSegment,
	 * remove infant TravelDoc in non-infant from passengerSegment
	 * @param booking
	 */
	private void parserPassengerSegmentInfantTD(RetrievePnrBooking booking) {
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		int noInfantSize = passengerSegments.size();
		for(int i = 0; i < noInfantSize; i++) {
			String passengerId = passengerSegments.get(i).getPassengerId();
			String segmentId = passengerSegments.get(i).getSegmentId();
			List<RetrievePnrPassenger> infants = getPassengersByParentId(passengers, passengerId);
			if(CollectionUtils.isEmpty(infants)) {
				continue;
			}
			
			//primary travelDocs
			List<RetrievePnrTravelDoc> priTravelDocs = passengerSegments.get(i).getPriTravelDocs();
			List<RetrievePnrTravelDoc> infantPriTravelDocs = new ArrayList<>();
			parserInfantPassengerSegmentTD(priTravelDocs, infantPriTravelDocs);
			
			//secondary travelDocs
			List<RetrievePnrTravelDoc> secTravelDocs = passengerSegments.get(i).getSecTravelDocs();
			List<RetrievePnrTravelDoc> infantSecTravelDocs = new ArrayList<>();
			parserInfantPassengerSegmentTD(secTravelDocs, infantSecTravelDocs);
			
			//other travelDocs
			List<RetrievePnrTravelDoc> othTravelDocs = passengerSegments.get(i).getOthTravelDocs();
			List<RetrievePnrTravelDoc> infantOthTravelDocs = new ArrayList<>();
			parserInfantPassengerSegmentTD(othTravelDocs, infantOthTravelDocs);
			
			//KTN
			List<RetrievePnrTravelDoc> ktns = passengerSegments.get(i).getKtns();
			List<RetrievePnrTravelDoc> infantKTNs = new ArrayList<>();
			parserInfantPassengerSegmentTD(ktns, infantKTNs);
			
			//redress
			List<RetrievePnrTravelDoc> redresses = passengerSegments.get(i).getRedresses();
			List<RetrievePnrTravelDoc> infantRedresses = new ArrayList<>();
			parserInfantPassengerSegmentTD(redresses, infantRedresses);
			
			RetrievePnrPassenger infant = infants.get(0);
			RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, infant.getPassengerID(), segmentId);
			if(passengerSegment == null) {
				passengerSegment = new RetrievePnrPassengerSegment();
				passengerSegment.setPassengerId(infant.getPassengerID());
				passengerSegment.setSegmentId(segmentId);
				passengerSegment.setPriTravelDocs(infantPriTravelDocs);
				passengerSegment.setSecTravelDocs(infantSecTravelDocs);
				passengerSegment.setOthTravelDocs(infantOthTravelDocs);
				passengerSegment.setKtns(infantKTNs);
				passengerSegment.setRedresses(infantRedresses);
				passengerSegments.add(passengerSegment);
			} else {
				passengerSegment.setPriTravelDocs(infantPriTravelDocs);
				passengerSegment.setSecTravelDocs(infantSecTravelDocs);
				passengerSegment.setOthTravelDocs(infantOthTravelDocs);
				passengerSegment.setKtns(infantKTNs);
				passengerSegment.setRedresses(infantRedresses);
			}
		}
	}

	/**
	 * move primary/secondary/other travelDocs, KTNs and redresses from adult to infant and remove it from adult
	 * @param priTravelDocs
	 * @param infantPriTravelDocs
	 */
	private void parserInfantPassengerSegmentTD(List<RetrievePnrTravelDoc> priTravelDocs, List<RetrievePnrTravelDoc> infantPriTravelDocs) {
		for(int j = 0; j < priTravelDocs.size(); j++) {
			RetrievePnrTravelDoc retrievePnrTravelDoc = priTravelDocs.get(j);
			if(retrievePnrTravelDoc != null && retrievePnrTravelDoc.isInfant()) {
				infantPriTravelDocs.add(retrievePnrTravelDoc);
				priTravelDocs.remove(j);
				j--;
			}
		}
	}
	
	/**
	 * parser infant TravelDoc info in passenger
	 * @param booking
	 */
	private void parserPassengerInfantTravelDoc(RetrievePnrBooking booking) {
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		for(RetrievePnrPassenger passenger : passengers) {
			String parentId = passenger.getParentId();
			if(StringUtils.isEmpty(parentId)) {
				continue;
			}
			//primary travelDocs
			List<RetrievePnrTravelDoc> priTravelDocs = passenger.getPriTravelDocs();
			parserPassengerInfantTravelDoc(passengers, parentId, priTravelDocs, PRIMARY_TRAVEL_DOCUMENT);
			
			//secondary travelDocs
			List<RetrievePnrTravelDoc> secTravelDocs = passenger.getSecTravelDocs();
			parserPassengerInfantTravelDoc(passengers, parentId, secTravelDocs, SECONDARY_TRAVEL_DOCUMENT);
			
			//other travelDocs
			List<RetrievePnrTravelDoc> othTravelDocs = passenger.getOthTravelDocs();
			parserPassengerInfantTravelDoc(passengers, parentId, othTravelDocs, OTHER_TRAVEL_DOCUMENT);
			
			//KTN
			List<RetrievePnrTravelDoc> ktns = passenger.getKtns();
			parserPassengerInfantTravelDoc(passengers, parentId, ktns, KTN);
			
			//redress
			List<RetrievePnrTravelDoc> redresses = passenger.getRedresses();
			parserPassengerInfantTravelDoc(passengers, parentId, redresses, REDRESS);
		}
	}

	/**
	 * move primary/secondary/other travelDocs, KTNs and redresses from adult to infant and remove it from adult 
	 * @param passengers
	 * @param parentId
	 * @param pnrTravelDocs
	 */
	private void parserPassengerInfantTravelDoc(List<RetrievePnrPassenger> passengers, String parentId, List<RetrievePnrTravelDoc> pnrTravelDocs, String flag) {
		for(RetrievePnrPassenger passenger : passengers) {
			if(parentId.equals(passenger.getPassengerID())) {
				List<RetrievePnrTravelDoc> pnrTravelDocs1 = new ArrayList<>();
				if(PRIMARY_TRAVEL_DOCUMENT.equals(flag)) {
					pnrTravelDocs1 = passenger.getPriTravelDocs();
				} else if (SECONDARY_TRAVEL_DOCUMENT.equals(flag)) {
					pnrTravelDocs1 = passenger.getSecTravelDocs();
				} else if(OTHER_TRAVEL_DOCUMENT.equals(flag)){
					pnrTravelDocs1 = passenger.getOthTravelDocs();
				}else if (KTN.equals(flag)) {
					pnrTravelDocs1 = passenger.getKtns();
				} else if (REDRESS.equals(flag)) {
					pnrTravelDocs1 = passenger.getRedresses();
				}
				for(int i = 0; i < pnrTravelDocs1.size(); i++) {
					if(pnrTravelDocs1.get(i) == null) {
						continue;
					}
					if(pnrTravelDocs1.get(i).isInfant()) {
						pnrTravelDocs.add(pnrTravelDocs1.get(i));
						pnrTravelDocs1.remove(i);
						i--;
					}
				}
			}
		}
	}



	/**
	 * replace "DM" with "DMP" in FQTV
	 * @param retrievePnrFQTVInfos
	 */
	private void replaceDMWithDMP(List<RetrievePnrFFPInfo> retrievePnrFQTVInfos) {
		RetrievePnrFFPInfo retrievePnrFQTVInfo = retrievePnrFQTVInfos.stream().filter(fqtv -> fqtv.getTierLevel()!=null && fqtv.getTierLevel().equals(OneAConstants.FQTV_TIER_LEVEL_DM)).findFirst().orElse(null);
		if(retrievePnrFQTVInfo != null) {
			List<String> topTiers = bizRuleConfig.getTopTier();
			retrievePnrFQTVInfo.setTierLevel(OneAConstants.FQTV_TIER_LEVEL_DMP);
			retrievePnrFQTVInfo.setTopTier(topTiers.contains(OneAConstants.FQTV_TIER_LEVEL_DMP));
		}
	}

	/**
	 * get list of reference.number by qulifierFlag = qualifier
	 * @param references
	 * @param qualifierFlag
	 */
	private List<String> getReferenceNumbers(List<ReferencingDetailsType111975C> references, String qualifierFlag) {
		return references.stream().filter(ref -> Objects.equals(ref.getQualifier(), qualifierFlag))
				.map(ReferencingDetailsType111975C::getNumber).collect(Collectors.toList());

	}

	private List<String> getReferenceNumbersForPT(List<ReferencingDetailsType111975C> references,List<RetrievePnrPassenger> paxList){
		// Proceed default process to get PT number list
		List<String> paxNumLists = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		
		// PNR can haven't PT qualifier if the SSR is for all paxs. Check if this special handling is needed and add pax id to it.
		if(shouldUseNoPTQualifierHandling(references) && paxNumLists.isEmpty()) {
			for(RetrievePnrPassenger pnrPax: paxList) {
				if(!StringUtils.isEmpty(pnrPax.getParentId())) {	// this pax is infant, use parent id
					if (!paxNumLists.contains(pnrPax.getParentId())) {
						paxNumLists.add(pnrPax.getParentId());
					}
				}else {
					if (!paxNumLists.contains(pnrPax.getPassengerID())) {
						paxNumLists.add(pnrPax.getPassengerID());		// otherwise, use pax own id
					}
				}
				
			}
		}
		return paxNumLists;
	}
	
	private boolean hasQualifierFlag(List<ReferencingDetailsType111975C> references, String qualifierFlag) {
		for(ReferencingDetailsType111975C reference : references) {
			String qualifier = reference.getQualifier();
			if(qualifierFlag.equals(qualifier)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean shouldUseNoPTQualifierHandling(List<ReferencingDetailsType111975C> references) {
		return !hasQualifierFlag(references, PASSENGER_ID_QUALIFIER);
	}

	/**
	 * parser PassengerSegment Info
	 * 
	 * @param pnrReply
	 * @param booking
	 * @param babyMealMapTemp 
	 */
	private void parserPassengerSegmentInfo(PNRReply pnrReply, RetrievePnrBooking booking, Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp) {
		if(pnrReply == null){
			return;
		}
		
		if(pnrReply.getDataElementsMaster() != null && !CollectionUtils.isEmpty(pnrReply.getDataElementsMaster().getDataElementsIndiv())){
			parserETicket(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking);
			// parse seat
			parserSeat(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking);
			//handle extra seat
			handleExtraSeat(pnrReply, booking);
			// parse seat payment
			parserSeatPayment(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking.getPassengerSegments());
			// parse meal
			parserMeal(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking, babyMealMapTemp);
			
			// parse parserBaggag BaggagePayment
			parserBaggageAndBaggagePayment(pnrReply.getDataElementsMaster().getDataElementsIndiv(), booking.getPassengerSegments(), booking.getOneARloc());
		}
	}
	
	/**
	 * parser baggage info and baggage payment info
	 * @param dataElementsIndivs
	 * @param passengerSegments
	 * @param rloc
	 */
	private void parserBaggageAndBaggagePayment(List<DataElementsIndiv> dataElementsIndivs,List<RetrievePnrPassengerSegment> passengerSegments, String rloc){
		for (RetrievePnrPassengerSegment passengerSegment : passengerSegments) {
			
			for (DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
				if(dataElementsIndiv.getReferenceForDataElement() == null
						|| dataElementsIndiv.getServiceRequest() == null
						|| !getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER).contains(passengerSegment.getPassengerId())
						|| !getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), SEGMENT_ID_QUALIFIER).contains(passengerSegment.getSegmentId())
						|| (!COMPANY_CX.equals(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId()) && !COMPANY_KA.equals(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId()))
						|| dataElementsIndiv.getElementManagementData().getReference() == null
						// not SSR OBAG/ABAG/BBAG/CBAG
						|| !(isSsrOfType(dataElementsIndiv, SSR_TYPE_OBAG)
								|| isSsrOfType(dataElementsIndiv, SSR_TYPE_ABAG)
								|| isSsrOfType(dataElementsIndiv, SSR_TYPE_BBAG)
								|| isSsrOfType(dataElementsIndiv, SSR_TYPE_CBAG))) {
						
						continue;
					}
				String status = dataElementsIndiv.getServiceRequest().getSsr().getStatus();
				String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
				String ssrType = dataElementsIndiv.getServiceRequest().getSsr().getType();
				
				StatusManagementModel statusManagementModel = statusManagementDAO.findMostMatchedStatus(MMBConstants.APP_CODE, segmentName, ssrType, status);
				// check if the status is confirmed status
				if (statusManagementModel == null || !SsrStatusEnum.CONFIRMED.getCode().equals(statusManagementModel.getMmbStatus())) {
					logger.info(String.format("unconfirmed baggage found in booking:%s", rloc));
					continue;
				}

				if(isSsrOfType(dataElementsIndiv,SSR_TYPE_OBAG)){
					String amoutStr = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().stream()
							.map(text->FreeTextUtil.getWeightAmountFromPurchasedBaggageFreeText(text)).filter(amout->!StringUtils.isEmpty(amout)).findFirst().orElse("");
					BigInteger amount = new BigInteger(StringUtils.isEmpty(amoutStr) ? "0" : amoutStr);
					RetrievePnrBaggage baggage = new RetrievePnrBaggage();
					baggage.setWeightAmount(amount);
					baggage.setCompanyId(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId());
					baggage.setPaymentInfo(getPaymentInfoFromFreeText(dataElementsIndivs,  passengerSegment.getPassengerId(), null , dataElementsIndiv.getElementManagementData().getReference().getNumber()));
					if(baggage.getPaymentInfo() != null) {
						passengerSegment.addBaggage(baggage);
					} else {
						logger.info(String.format("unpaid purchased baggage found in booking:%s", rloc));
					}
				}else if(isSsrOfType(dataElementsIndiv,SSR_TYPE_ABAG)
						||isSsrOfType(dataElementsIndiv,SSR_TYPE_BBAG)
						||isSsrOfType(dataElementsIndiv,SSR_TYPE_CBAG)){
					String amoutStr = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().stream()
							.map(text->FreeTextUtil.getPieceAmountFromPurchasedBaggageFreeText(text)).filter(amout->!StringUtils.isEmpty(amout)).findFirst().orElse("");
					BigInteger amount = new BigInteger(StringUtils.isEmpty(amoutStr) ? "0" : amoutStr);
					RetrievePnrBaggage baggage = new RetrievePnrBaggage();
					baggage.setPcAmount(amount);
					baggage.setCompanyId(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId());
					baggage.setPaymentInfo(getPaymentInfoFromFreeText(dataElementsIndivs, passengerSegment.getPassengerId(), null , dataElementsIndiv.getElementManagementData().getReference().getNumber()));
					if(baggage.getPaymentInfo() != null) {
						passengerSegment.addBaggage(baggage);
					} else {
						logger.info(String.format("unpaid purchased baggage found in booking:%s", rloc));
					}
				}
			}
		}
		
	}
	
	/**
	 * Check if is the type of ssr
	 * @param dataElementsIndiv
	 * @param ssrType
	 * @return
	 */
	private boolean isSsrOfType(DataElementsIndiv dataElementsIndiv, String ssrType) {
		if (dataElementsIndiv == null
				|| !SEGMENT_NAME_SSR.equals(dataElementsIndiv.getElementManagementData().getSegmentName())) {
			return false;
		}
		if (dataElementsIndiv.getServiceRequest() == null
				|| !ssrType.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())) {
			return false;
		}

		return true;

	}
	/**
	 * 
	* @Description parse seat payment
	* @param dataElementsIndiv
	* @param passengerSegments
	* @return void
	* @author haiwei.jia
	 */
	private void parserSeatPayment(List<DataElementsIndiv> dataElementsIndivs,
			List<RetrievePnrPassengerSegment> passengerSegments) {

		for (RetrievePnrPassengerSegment passengerSegment : passengerSegments) {
			if (passengerSegment.getSeat() != null) {
				
				if (passengerSegment.getSeat().getSeatDetail() != null) {
					
					RetrievePnrPaymentInfo paymentInfo = getPaymentInfoFromFreeText(dataElementsIndivs, passengerSegment.getSeat().getSeatDetail().getCrossRef(), null , passengerSegment.getSeat().getQulifierId());
					
					if (paymentInfo != null) {
						passengerSegment.getSeat().getSeatDetail().setPaid(true);
						passengerSegment.getSeat().getSeatDetail().setPaymentInfo(paymentInfo);
					}

				}
				if (passengerSegment.getSeat().getExtraSeats() != null) {
					passengerSegment.getSeat().getExtraSeats().forEach(ex -> {
						RetrievePnrPaymentInfo paymentInfo = getPaymentInfoFromFreeText(dataElementsIndivs, ex.getCrossRef(), null, passengerSegment.getSeat().getQulifierId());
						if(paymentInfo!=null){
							ex.setPaid(true);
							ex.setPaymentInfo(paymentInfo);
						}
					
					});
				}
			}
		}

	}
	
	private void parserLoungePayment(List<DataElementsIndiv> dataElementsIndivs,
			List<RetrievePnrPassengerSegment> passengerSegments) {

		for (RetrievePnrPassengerSegment passengerSegment : passengerSegments) {
			if (passengerSegment.getPurchasedLounges() != null) {

				for (RetrievePnrLoungeInfo loungeInfo : passengerSegment.getPurchasedLounges()) {

					RetrievePnrPaymentInfo paymentInfo = getPaymentInfoFromFreeText(dataElementsIndivs,
							loungeInfo.getPassengerId(), null, loungeInfo.getQulifierId());
					loungeInfo.setPaymentInfo(paymentInfo);
				}
			}
		}
	}
	
	/**
	 * Get payment inforom
	 * @param dataElementsIndivs
	 * @param passengerId
	 * @param segmentId
	 * @param otNumber
	 * @return
	 */
	private RetrievePnrPaymentInfo getPaymentInfoFromFreeText(List<DataElementsIndiv> dataElementsIndivs,String passengerId,String segmentId, BigInteger otNumber ) {
		
		RetrievePnrPaymentInfo paymentInfo = null;
		
		 for (DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(dataElementsIndiv.getElementManagementData().getSegmentName() == null
					//segmentName is not FA or FHD
					|| (!dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_FA) && !dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_FHD))
					|| dataElementsIndiv.getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				continue;
			}
			List<ReferencingDetailsType111975C> paymentReferences = dataElementsIndiv.getReferenceForDataElement().getReference();
			
			boolean otMatched = false;
			boolean paymentMultipleItems = false;
			
			if(otNumber != null){
				List<String> ots = getReferenceNumbers(paymentReferences, REFERENCE_QUALIFIER_OT);
				otMatched = ots.contains(otNumber.toString());
				paymentMultipleItems = ots.size() > 1;
			}
			
			if(!otMatched
					||(!StringUtils.isEmpty(passengerId) && !getReferenceNumbers(paymentReferences, PASSENGER_ID_QUALIFIER).contains(passengerId))
					|| (!StringUtils.isEmpty(segmentId)&&!getReferenceNumbers(paymentReferences, SEGMENT_ID_QUALIFIER).contains(segmentId))){
				continue;
			}
			
			 String[] paymentArray = dataElementsIndiv.getOtherDataFreetext().stream().map(longText->FreeTextUtil.getPaymentInfoFromFreeText(longText.getLongFreetext())).filter(payInfo->payInfo!=null).findFirst().orElse(null);
			
			 if(paymentArray != null){
				 paymentInfo = new RetrievePnrPaymentInfo(); 
				 paymentInfo.setCurrency(paymentArray[0]);
				 //don't display Amount if payment for Multiple items
				 if(!paymentMultipleItems && !StringUtils.isEmpty(paymentArray[1])){
					 paymentInfo.setAmount(new BigDecimal(paymentArray[1]));
				 }
				 paymentInfo.setDate(paymentArray[2]);
				 paymentInfo.setOfficeId(paymentArray[3]);
				 paymentInfo.setTicket(paymentArray[4]);
				 
				 if(dataElementsIndiv.getElementManagementData().getReference()!=null){
					 paymentInfo.setQualifierId(dataElementsIndiv.getElementManagementData().getReference().getNumber().toString());
				 }
				break;
			 }
		}
		return paymentInfo;
	}
	/**
	 * 
	* @Description populate seat payment info
	* @param references
	* @return void
	* @author haiwei.jia
	 */
	/*private void populateSeatPaymentInfo(PNRReply pnrReply, RetrievePnrBooking booking) {
		if(pnrReply.getDataElementsMaster()==null){
			return;
		}
		for (DataElementsIndiv dataElementsIndiv : pnrReply.getDataElementsMaster().getDataElementsIndiv()) {
			if(dataElementsIndiv.getElementManagementData().getSegmentName() == null
					//segmentName is not FA or FHD
					|| (!dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_FA) && !dataElementsIndiv.getElementManagementData().getSegmentName().equals(SEGMENT_NAME_FHD))
					|| dataElementsIndiv.getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				continue;
			}
			
			 String[] paymentInfo = dataElementsIndiv.getOtherDataFreetext().stream().map(longText->FreeTextUtil.getPaymentInfoFromFreeText(longText.getLongFreetext())).filter(payInfo->payInfo!=null).findFirst().orElse(null);
			 RetrievePnrPaymentInfo
 
		}
	}*/
	
	/**
	 * parse e-Ticket info
	 * 
	 * @param dataElementsIndiv
	 * @param passengerSegments
	 */
	private void parserETicket(List<DataElementsIndiv> dataElementsIndivList, RetrievePnrBooking booking) {
		if(CollectionUtils.isEmpty(dataElementsIndivList)) {
			return;
		}
		 
		// parse e ticket
		for (DataElementsIndiv dataElementsIndiv : dataElementsIndivList) {
			if (dataElementsIndiv.getReferenceForDataElement() != null
					&& CollectionUtils.isNotEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())
					&& ET_SEGMENT_NAME_LIST.contains(dataElementsIndiv.getElementManagementData().getSegmentName())
					&& !StringUtils.isEmpty(dataElementsIndiv.getOtherDataFreetext().get(0).getLongFreetext())) {
				
				String longFreeText = dataElementsIndiv.getOtherDataFreetext().get(0).getLongFreetext();
				
				Object[] eTicketInfos = FreeTextUtil.getETicketInfoFromFreeText(longFreeText);
				if(eTicketInfos == null) {// if not match eticket format, skip to next element
					continue;
				}
				booking.setTicketRecordExist(true);// set ticket record flag to true is found matched Record, this for mini pnr check only now.
				String passengerType = (String) eTicketInfos[2];
				BigInteger lineNumber = dataElementsIndiv.getElementManagementData().getLineNumber();
				List<RetrievePnrEticket> etickets = buildEticket(eTicketInfos, lineNumber);
				
				List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
				setETicketValue(references, etickets, booking, passengerType);
			}
		}
	}
	
	/**
	 * @param eTicketInfos
	 * @param lineNumber
	 * @return List<RetrievePnrEticket>s
	 */
	private List<RetrievePnrEticket> buildEticket(Object[] eTicketInfos, BigInteger lineNumber) {
		if(eTicketInfos == null) {
			return null;
		}
		
		List<RetrievePnrEticket> etickets = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<String> eTicketNumbers = (List<String>) eTicketInfos[0];		
		for(String number : eTicketNumbers) {
			RetrievePnrEticket pnrEticket = new RetrievePnrEticket();
			pnrEticket.setTicketNumber(number);
			pnrEticket.setLineNumber(String.valueOf(lineNumber));
			pnrEticket.setDate((String) eTicketInfos[1]);
			pnrEticket.setPassengerType((String) eTicketInfos[2]);
			pnrEticket.setCurrency((String) eTicketInfos[3]);
			pnrEticket.setAmount((String) eTicketInfos[4]);
			etickets.add(pnrEticket);
		}
		return etickets;
	}

	/**
	 * parse seat info
	 * 
	 * @param dataElementsIndivList
	 * @param passengerSegments
	 */
	private void parserSeat(List<DataElementsIndiv> dataElementsIndivList, RetrievePnrBooking booking) {
		if (CollectionUtils.isEmpty(dataElementsIndivList)) {
			return;
		}
		for (DataElementsIndiv dataElementsIndiv : dataElementsIndivList) {
			if(dataElementsIndiv == null || dataElementsIndiv.getServiceRequest() == null
					|| dataElementsIndiv.getReferenceForDataElement() == null
					|| dataElementsIndiv.getServiceRequest().getSsr()== null) {
				continue;
			}
			
			//check SSR type
			String ssrType = dataElementsIndiv.getServiceRequest().getSsr().getType();
			if(!SEAT_SSR.contains(ssrType)) {
				continue;
			}
			
			// Status
			String status = dataElementsIndiv.getServiceRequest().getSsr().getStatus();

			List<SpecialRequirementsDataDetailsTypeI> ssrbList = dataElementsIndiv.getServiceRequest().getSsrb();
			List<SeatPaxInfo> seatPaxInfoList = new ArrayList<>();
			if(!CollectionUtils.isEmpty(dataElementsIndiv.getSeatPaxInfo())){
				seatPaxInfoList = dataElementsIndiv.getSeatPaxInfo();
			}

			//ssrb must not be empty
			if(CollectionUtils.isEmpty(ssrbList)){
				continue;
			}
			
			//references must not be empty
			List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
			if(CollectionUtils.isEmpty(references)){
				continue;
			}
			
			//get seat info
			BigInteger qulifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
			if(qulifierId == null){
				continue;
			}
			if(SSR_TYPE_SEATNO.equals(ssrType)) {
				List<RetrievePnrSeatDetail> pnrSeats = getSeatInfoFromPnr(ssrbList, seatPaxInfoList, status);
				
				if (CollectionUtils.isEmpty(pnrSeats)) {
					continue;
				}
				processSeat(booking, references, pnrSeats, null, qulifierId);
			}else if(SEAT_SSR.contains(ssrType)) {
				//if SSR type is "NSST" but with no seat preference,ignore this
				SpecialRequirementsDataDetailsTypeI ssrb = ssrbList.get(0);
				List<String> seatTypeList = ssrb.getSeatType();
				String seatNo = ssrb.getData();
				
				if(!StringUtils.isEmpty(seatNo)) {
					List<RetrievePnrSeatDetail> pnrSeats = getSeatInfoFromPnr(ssrbList, seatPaxInfoList, status);
					if (CollectionUtils.isEmpty(pnrSeats)) {
						continue;
					}
					processSeat(booking, references, pnrSeats, null, qulifierId);
				}else if(!CollectionUtils.isEmpty(seatTypeList)) {
					RetrievePnrSeatPreference preference = new RetrievePnrSeatPreference();
					String seatPreference = getPreferenceFromSeatTypeList(seatTypeList);
					preference.setPreferenceCode(seatPreference);
					preference.setStatus(status);
					preference.setSpeicalPreference(seatPreference==null?true:false);
					processSeat(booking, references, null, preference, qulifierId);
				}
			}
			
		}
	}
	
	/**
	 * get seat preference
	 * @param seatTypeList
	 * @return
	 */
	private String getPreferenceFromSeatTypeList(List<String> seatTypeList) {
		String preference = "";
		if(!CollectionUtils.isEmpty(seatTypeList)) {
			for(int i=seatTypeList.size()-1;i>=0;i--) {
				if("N".equals(seatTypeList.get(i))) {
					seatTypeList.remove(i);
				}
			}
			
			if(seatTypeList.size()==1) {
				preference = seatTypeList.get(0);
				if(!"A".equals(preference) && !"W".equals(preference)) {
					preference = null;
				}
			}else if(seatTypeList.size()==2 && (!seatTypeList.contains("A") || !seatTypeList.contains("W"))) {
				preference = null;
			}else if(seatTypeList.size()>2) {
				preference = null;
			}
		}
		
		return preference;
	}

	/**
	 * 
	* @Description get seat infomation from pnr using ssrbList and seatPaxInfoList
	* @param ssrbList
	* @param seatPaxInfoList
	* @return List<RetrievePnrSeatDetail>
	* @author haiwei.jia
	 */
	private List<RetrievePnrSeatDetail> getSeatInfoFromPnr(List<SpecialRequirementsDataDetailsTypeI> ssrbList,
			List<SeatPaxInfo> seatPaxInfoList, String seatStatus) {
		List<RetrievePnrSeatDetail> pnrSeats = new ArrayList<>();
		for (SpecialRequirementsDataDetailsTypeI ssrb : ssrbList) {
			if(ssrb == null) {
				continue;
			}
			
			//if SSR type is "RQST" but with no seat number,ignore this
			String seatNo = ssrb.getData();
			String crossRef = ssrb.getCrossRef();
			if(StringUtils.isEmpty(seatNo)) {
				continue;
			}
			List<String> characteristics = new ArrayList<>();
			String indicator = null;
			//get seat characteristic
			for(SeatPaxInfo seatPaxInfo : seatPaxInfoList){
				if(seatPaxInfo == null){
					continue;
				}
				// get characteristics and indicator from the paxInfo which shares the same crossRef number with ssrb			   
				if(!StringUtils.isEmpty(crossRef) && 
					seatPaxInfo.getCrossRef() != null && 
					seatPaxInfo.getCrossRef().getReference() != null && 
					PASSENGER_ID_QUALIFIER.equals(seatPaxInfo.getCrossRef().getReference().get(0).getQualifier()) && 
					crossRef.equals(seatPaxInfo.getCrossRef().getReference().get(0).getNumber())
				){
					// characteristics
					if(characteristics.isEmpty() &&
						seatPaxInfo.getSeatPaxDetails() != null &&
						seatPaxInfo.getSeatPaxDetails().getGenericDetails() != null &&
						!CollectionUtils.isEmpty(seatPaxInfo.getSeatPaxDetails().getGenericDetails().getSeatCharacteristic())
					){
						//the characteristics of the seat
						characteristics = seatPaxInfo.getSeatPaxDetails().getGenericDetails().getSeatCharacteristic();
					}
					
					// seat pax indicator
					if(indicator == null &&
						seatPaxInfo.getSeatPaxIndicator() != null &&
						seatPaxInfo.getSeatPaxIndicator().getStatusDetails() != null
					) {
						indicator = seatPaxInfo.getSeatPaxIndicator().getStatusDetails().getIndicator();
					}
				}
				
				
				
			}
			RetrievePnrSeatDetail pnrSeat = new RetrievePnrSeatDetail();
			pnrSeat.setSeatNo(seatNo);
			pnrSeat.setSeatCharacteristics(characteristics);
			pnrSeat.setIndicator(indicator);
			pnrSeat.setStatus(seatStatus);
			pnrSeat.setCrossRef(crossRef);
			pnrSeats.add(pnrSeat);
		}
		return pnrSeats;
	}

	/**
	 * parser Meal Info
	 * @param babyMealMapTemp 
	 * 
	 * @param dataElementsIndiv
	 * @param passengerSegments
	 */
	private void parserMeal(List<DataElementsIndiv> dataElementsIndivList, RetrievePnrBooking booking, Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp) {
	 
		// parse meal
		for (DataElementsIndiv dataElementsIndiv : dataElementsIndivList) {
			if(dataElementsIndiv == null || dataElementsIndiv.getServiceRequest() == null
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| dataElementsIndiv.getReferenceForDataElement() == null) {
				continue;
			}
			
			SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
			
			
			String ssrType = ssr.getType().trim();
			List<String> mealTypes = tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE);
			if (mealTypes.contains(ssrType)) {
				List<ReferencingDetailsType111975C> references = dataElementsIndiv.getReferenceForDataElement().getReference();
				if (!CollectionUtils.isEmpty(references)) {
					String qualifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber().toString();
					int quantity = ssr.getQuantity().intValue();
					String companyId = ssr.getCompanyId();
					String mealStatus = ssr.getStatus();
					
					RetrievePnrMeal meal = new RetrievePnrMeal();
					// try to parse preselected meal
					String preSelectedMealCode = retrievePreSelectedMealCode(ssr);
                    if (BooleanUtils.isTrue(bookEligibilityConfig.isEnablePreselectedMeal())
                            && !StringUtils.isEmpty(preSelectedMealCode)) {
                        // handle preselected meal in pnr
					    handlePreSelectedMealInPnr(booking, ssr, references, qualifierId, quantity, companyId,
                                mealStatus, meal, preSelectedMealCode);
					}else {
					    // handle special meal in pnr
					    handleSpecialMealInPnr(booking, babyMealMapTemp, ssr, ssrType, references, qualifierId,
                                quantity, companyId, mealStatus, meal);  
					}
				}
			}
		}
	}

    /**
     * @param booking
     * @param babyMealMapTemp
     * @param ssr
     * @param ssrType
     * @param references
     * @param qualifierId
     * @param quantity
     * @param companyId
     * @param mealStatus
     * @param meal
     */
    private void handleSpecialMealInPnr(RetrievePnrBooking booking,
            Map<RetrievePnrPassengerSegment, RetrievePnrMeal> babyMealMapTemp, SpecialRequirementsTypeDetailsTypeI ssr,
            String ssrType, List<ReferencingDetailsType111975C> references, String qualifierId, int quantity,
            String companyId, String mealStatus, RetrievePnrMeal meal) {
        // try to parse special meal
        String originalMealCode = parseOriginalMealCode(ssr);
        List<String> specialMealTypes = new ArrayList<>();
        try{
            specialMealTypes = specialMealDao.findMealListByAppCode(PnrResponseParser.APP_CODE);
            
            /**
             *  Fix - OLSSMMB-16616
             *  From day 1, both BBML and CHML are controlled in code.
             */
            specialMealTypes.add(MealConstants.BABY_MEAL_CODE);
            specialMealTypes.add(MealConstants.CHILD_MEAL_CODE);
        }catch(Exception ex) {
            logger.info("Meal list is not found in the table.");
        }
        if (specialMealTypes != null &&
                (!MealConstants.SPECIAL_MEAL_CODE.equals(ssr.getType()) || specialMealTypes.contains(originalMealCode))) {
            meal.setMealCode(originalMealCode);
            meal.setQulifierId(qualifierId);
            meal.setCompanyId(companyId);
            meal.setQuantity(quantity);
            meal.setStatus(mealStatus);
            meal.setFreeTexts(ssr.getFreeText());
            
            // If this meal is for infant, set it to a map and retrieve it for the infant object.
            if(BABY_MEAL_TYPE.equals(ssrType) ||    // If BBML
              (
                MealConstants.SPECIAL_MEAL_CODE.equals(ssrType) &&      // If CHML for infant
                !CollectionUtils.isEmpty(ssr.getFreeText()) &&
                ssr.getFreeText().contains(MealConstants.SPML_CHML_FREETEXT)
              )
             ) {
                List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
                List<String> passengerIds = getReferenceNumbersForPT(references,booking.getPassengers());   // this id should be normal pax id (not infant id)
   
                // One SSR can refer to multi-passengers / multi-segments
                for(String segmentId: segmentIds) {
                    for(String passengerId: passengerIds) {
                        RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
                        babyMealMapTemp.put(passengerSegment, meal);
                    }
                }
            }else {
                setMealValue(references, meal, booking);
            }
        }else {
            logger.info(originalMealCode + " does not match any meal type in the table.");
        }
    }

    /**
     * @param booking
     * @param ssr
     * @param references
     * @param qualifierId
     * @param quantity
     * @param companyId
     * @param mealStatus
     * @param meal
     * @param preSelectedMealCode
     */
    private void handlePreSelectedMealInPnr(RetrievePnrBooking booking, SpecialRequirementsTypeDetailsTypeI ssr,
            List<ReferencingDetailsType111975C> references, String qualifierId, int quantity, String companyId,
            String mealStatus, RetrievePnrMeal meal, String preSelectedMealCode) {
        meal.setMealCode(preSelectedMealCode);
        meal.setQulifierId(qualifierId);
        meal.setCompanyId(companyId);
        meal.setQuantity(quantity);
        meal.setStatus(mealStatus);
        meal.setFreeTexts(ssr.getFreeText());
        meal.setPreSelectedMeal(true);
        setMealValue(references, meal, booking);
    }
	
	
	/**
	 * retrieve preselected meal code
	 * @param ssr
	 * @return
	 */
	private String retrievePreSelectedMealCode(SpecialRequirementsTypeDetailsTypeI ssr) {
        String mealCode = ssr.getType();
        // if special meal
        if (MealConstants.SPECIAL_MEAL_CODE.equals(mealCode)) {
            String freeText = ssr.getFreeText().stream()
                    .filter(ft -> !StringUtils.isEmpty(FreeTextUtil.getPreSelectedMealFromFreeText(ft)))
                    .findFirst().orElse(null);
            if (!StringUtils.isEmpty(freeText)) {
                return FreeTextUtil.getPreSelectedMealFromFreeText(freeText);
            }
        }
        return null;
    }

    /**
	 * is pre selected meal or not
	 * @param ssr
	 * @return
	 */
	private boolean isPreSelectedMeal(SpecialRequirementsTypeDetailsTypeI ssr) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
	 * Convert 1A meal type to original meal code
	 * E.g. SPML_NOBEEF, etc.
	 * @param mealCode
	 * @return
	 */
	private String parseOriginalMealCode(SpecialRequirementsTypeDetailsTypeI ssr) {
		String mealType = ssr.getType();
		List<String> freeTexts = ssr.getFreeText();
		
		// if special meal
		if(MealConstants.SPECIAL_MEAL_CODE.equals(mealType)) {
			for(String freeText: freeTexts) {
				
				switch (freeText) {
				case MealConstants.SPML_BBML_FREETEXT:
					return MealConstants.BABY_MEAL_CODE;
				case MealConstants.SPML_CHML_FREETEXT:
					return MealConstants.CHILD_MEAL_CODE;
				case MealConstants.SPML_NOBEEF_FREETEXT:
				case MealConstants.SPML_NOPORK_FREETEXT:
					return mealType + MealConstants.SPECIAL_MEAL_CODE_SEPARATOR + freeText;
				case MealConstants.SPML_LIQUID_MEAL_FREETEXT:
					return MealConstants.SPML_LIQUID_MEAL_CODE;
				default:
					break;
				}
			}
			
			// Consider new SPML maybe added to database. This can handle newly added SPML.
			if(freeTexts.size() > 0) {
				return mealType + MealConstants.SPECIAL_MEAL_CODE_SEPARATOR + freeTexts.get(0);
			}
		}
		
		return mealType;
		
	}
	
	/**
	 * parser Travel Document info & KTN & redress
	 * @param dataElementsMaster
	 * @param booking
	 */
	private void parserTravelDocAndTS(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		
		for (int i = 0; i < dataElementsIndivs.size(); i++) {
			if(dataElementsIndivs.get(i).getElementManagementData().getSegmentName() == null
					|| !dataElementsIndivs.get(i).getElementManagementData().getSegmentName().equals(SEGMENT_NAME_SSR)
					|| dataElementsIndivs.get(i).getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndivs.get(i).getReferenceForDataElement().getReference())
					|| dataElementsIndivs.get(i).getServiceRequest() == null) {
				continue;
			}
			
			ReferencingDetailsType127526C reference = dataElementsIndivs.get(i).getElementManagementData().getReference();
			BigInteger number = null;
			if(reference != null && reference.getQualifier().equals(REFERENCE_QUALIFIER_OT)) {
				number = reference.getNumber();
			}
			List<String> avaliableTravelDocSsrTypes = tbSsrTypeDAO
					.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
							TBConstants.SSR_TRAVEL_DOC_AVAILABLE)
					.stream().map(TbSsrTypeModel::getValue).collect(Collectors.toList());
			SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndivs.get(i).getServiceRequest().getSsr();
			if(StringUtils.isEmpty(ssr.getType()) || !avaliableTravelDocSsrTypes.contains(ssr.getType())
					|| StringUtils.isEmpty(ssr.getStatus()) || !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(ssr.getStatus())
					|| CollectionUtils.isEmpty(ssr.getFreeText())) {
				continue;
			}
			
			String freeText = "";
			List<String> ftList = ssr.getFreeText();
			for(String ft:ftList) {
				freeText += ft;
			}
			String companyId = ssr.getCompanyId();
			
			List<ReferencingDetailsType111975C> references = dataElementsIndivs.get(i).getReferenceForDataElement().getReference();
			List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
			List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
			int stCount = segmentIds.size();
			for(String pt : passengerIds) {
				if(stCount == 0) {
					RetrievePnrPassenger passenger = getPassengerById(passengers, pt);
					if(passenger == null) {
						passenger = new RetrievePnrPassenger();
						passenger.setPassengerID(pt);
						parserTavelDocFreeText(passenger.getKtns(), passenger.getRedresses(), passenger.getPriTravelDocs(),
								passenger.getSecTravelDocs(), passenger.getOthTravelDocs(), freeText, companyId, ssr.getType(), number);

						passengers.add(passenger);
					} else {
						parserTavelDocFreeText(passenger.getKtns(), passenger.getRedresses(), passenger.getPriTravelDocs(),
								passenger.getSecTravelDocs(), passenger.getOthTravelDocs(), freeText, companyId, ssr.getType(), number);
					}
				} else {
					for(String st : segmentIds) {
						RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, pt, st);
						if(passengerSegment == null) {
							passengerSegment = new RetrievePnrPassengerSegment();
							passengerSegment.setPassengerId(pt);
							passengerSegment.setSegmentId(st);
							parserTavelDocFreeText(passengerSegment.getKtns(), passengerSegment.getRedresses(), passengerSegment.getPriTravelDocs(),
									passengerSegment.getSecTravelDocs(), passengerSegment.getOthTravelDocs(), freeText, companyId, ssr.getType(), number);
							passengerSegments.add(passengerSegment);
						} else {
							parserTavelDocFreeText(passengerSegment.getKtns(), passengerSegment.getRedresses(), passengerSegment.getPriTravelDocs(),
									passengerSegment.getSecTravelDocs(), passengerSegment.getOthTravelDocs(), freeText, companyId, ssr.getType(), number);
						}						
					}
				}
			}
		}
		
		Comparator<RetrievePnrTravelDoc> comparator = new Comparator<RetrievePnrTravelDoc>() {
			@Override
			public int compare(RetrievePnrTravelDoc travelDoc1, RetrievePnrTravelDoc travelDoc2) {
				int ot1 = travelDoc1.getQualifierId().intValue();
				int ot2 = travelDoc2.getQualifierId().intValue();
				return ot2 - ot1;
			}
		};
		
		for(RetrievePnrPassenger passenger:passengers) {
			Collections.sort(passenger.getPriTravelDocs(), comparator);
			Collections.sort(passenger.getSecTravelDocs(), comparator);
		}
		
		for(RetrievePnrPassengerSegment passengerSegment:passengerSegments) {
			Collections.sort(passengerSegment.getPriTravelDocs(), comparator);
			Collections.sort(passengerSegment.getSecTravelDocs(), comparator);
		}
	}
	
	/**
	 * parser Travel Document freeText format
	 * @param redresses 
	 * @param ktns 
	 * @param ptds	list of primary travelDoc
	 * @param stds	list of secondary travelDoc
	 * @param freeText	
	 * @param companyId
	 * @param primaryTypes
	 * @param secondaryTypes 
	 * @param ssrType 
	 * @param number 
	 */
	private void parserTavelDocFreeText(List<RetrievePnrTravelDoc> ktns, List<RetrievePnrTravelDoc> redresses, List<RetrievePnrTravelDoc> ptds,
			List<RetrievePnrTravelDoc> stds, List<RetrievePnrTravelDoc> otds, String freeText, String companyId, String ssrType, BigInteger number) {
		String[] fields = freeText.split(SEPARATOR_IN_FREETEXT, -1);
		RetrievePnrTravelDoc td = null;
		if(ssrType.equals(SSR_TYPE_DOCS)) {
			if(fields.length < 9) {
				return;
			}
			td = parserTravelDocFreeTextForDOCS(companyId, fields, number);			
		} else if (ssrType.equals(SSR_TYPE_DOCO)) {
			if(fields.length < 6) {
				return;
			}
			td = parserTravelDocFreeTextForDOCO(companyId, fields, number);
		}
		List<String> primaryTypes = bizRuleConfig.getTdPrimaryTypes();
		List<String> secondaryTypes = bizRuleConfig.getTdSecondaryTypes();
		if(td == null) {
			
		} else if(OneAConstants.TRAVEL_DOCUMENT_TYPE_K.equals(td.getTravelDocumentType())) {
			ktns.add(td);
		} else if(OneAConstants.TRAVEL_DOCUMENT_TYPE_R.equals(td.getTravelDocumentType())) {
			redresses.add(td);
		} else if(primaryTypes.contains(td.getTravelDocumentType())) {
        	ptds.add(td);
        } else if(secondaryTypes.contains(td.getTravelDocumentType())){
        	stds.add(td);
        }else {
        	otds.add(td);
        }
	}

	/**
	 * parser TravelDoc FreeText For DOCO
	 * @param companyId
	 * @param fields
	 * @param number
	 * @return
	 */
	private RetrievePnrTravelDoc parserTravelDocFreeTextForDOCO(String companyId, String[] fields, BigInteger number) {
		//format [extra FreeText]/[Visa Doc Type]/[Document No.]/[Place of Issue]/[Date of Issue]/[Applicable Country]/[is infant or not]
		RetrievePnrTravelDoc td = new RetrievePnrTravelDoc();
		for(int i = 0; i < fields.length; i++) {
			String field = fields[i];
			switch(i) {
			case 0:
				td.setExtraFreeText(field);
				break;
			case 1:
				td.setTravelDocumentType(field);//doc type
				break;
			case 2:
				td.setTravelDocumentNumber(field);//doc number
				break;
			case 3:
				td.setNationality(field);//nationality
				break;
			case 4:
				String expiryDate = field;//date of expire
				if(!StringUtils.isEmpty(expiryDate)) {
					td.setExpiryDateYear(DateUtil.convertDateFormat(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_YYYY));
					td.setExpiryDateMonth(DateUtil.convertDateFormat(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_MM));
					td.setExpiryDateDay(DateUtil.convertDateFormat(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_DD));        	
				}			
				break;
			case 5:
				td.setCountryOfIssuance(field);//issuing country
				break;
			case 6:
				if(SSR_TYPE_DOCO_FREETEXT_INFANT.equals(field)) {
					td.setInfant(true);					
				}
				break;
			default:
				break;
			}
		}
		td.setSsrType(SSR_TYPE_DOCO);
		td.setQualifierId(number);
		return td;
	}

	/**
	 * parser TravelDoc FreeText For DOCS
	 * @param companyId
	 * @param fields
	 * @param number
	 * @return
	 */
	private RetrievePnrTravelDoc parserTravelDocFreeTextForDOCS(String companyId, String[] fields, BigInteger number) {
		//format [Document Type]/[Country of Issue]/[Travel Document Number]/[Document Nationality]/[DOB]/[Gender]/[Document Expiry Date(format DDMONYY)]/[Document Family Name]/[Document Given Name]
		RetrievePnrTravelDoc td = new RetrievePnrTravelDoc();      
        for(int i = 0; i < fields.length; i++) {
			String field = fields[i];
			switch(i) {
			case 0:
				td.setTravelDocumentType(field);//doc type
				break;
			case 1:
				td.setCountryOfIssuance(field);//issuing country
				break;
			case 2:
				td.setTravelDocumentNumber(field);//doc number
				break;
			case 3:
				td.setNationality(field);//nationality			
				break;
			case 4:
				String birthDate = field;//date of birth
				td.setBirthDateYear(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_YYYY));
				td.setBirthDateMonth(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_MM));
		        td.setBirthDateDay(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_DD));
				break;
			case 5:
				td.setInfant(bizRuleConfig.getTdInfantGenders().contains(field));
				td.setGender(removeInfantSuffixOfGender(field));//gender
				break;
			case 6:
				String expiryDate = field;//date of expire
		        if(!StringUtils.isEmpty(expiryDate)) {
		        	td.setExpiryDateYear(DateUtil.convertDateFormatForExpiryDate(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_YYYY));
		        	td.setExpiryDateMonth(DateUtil.convertDateFormatForExpiryDate(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_MM));
		        	td.setExpiryDateDay(DateUtil.convertDateFormatForExpiryDate(expiryDate, DateUtil.DATE_PATTERN_DDMMMYY, DateUtil.DATE_PATTERN_DD));        	
		        }
				break;
			case 7:
				td.setFamilyName(field);
				break;
			case 8:
				String givenName = field;
		        for(int j = 9; j < fields.length; j++) {
		        	// ignore the last H in text
		        	if(j == fields.length-1 && "H".equals(fields[j])){
		        		continue;
		        	}
		        	givenName = givenName + " " + fields[j];
		        }
		        td.setGivenName(givenName);
				break;
			default:
				break;	
			}
		}    
        td.setCompanyId(companyId);
        td.setSsrType(SSR_TYPE_DOCS);
        td.setQualifierId(number);
		return td;
	}

	/**
	 * remove the infant suffix "I" in the gender String
	 * @param field
	 * @return
	 */
	private String removeInfantSuffixOfGender(String gender) {
		if (StringUtils.isEmpty(gender) || gender.length() == 1) {
			return gender;
		}
		String suffix = gender.substring(gender.length()-1, gender.length());
		if (INFANT_GENDER_SUFFIX.equals(suffix)){
			gender = gender.substring(0, gender.length()-1);
		}
		return gender;
	}

	/**
	 * get FQTVInfo in list of tierLevel(cxkaTierLevels, oneworldTierLevels, amTierLevels)
	 * @param priorityDetails
	 * @param fqtvCompany
	 * @param fqtvMembershipNumber
	 * @param otNumber
	 * @param fqtvInfo
	 * @param tierLevels
	 */ 
	private RetrievePnrFFPInfo getCxKaFQTVInfo(List<PriorityDetailsType> priorityDetails, String companyId, String fqtvCompany,
			String fqtvMembershipNumber, BigInteger otNumber) {
		RetrievePnrFFPInfo fqtvInfo = null;
		if(CollectionUtils.isEmpty(priorityDetails)){
			fqtvInfo = new RetrievePnrFFPInfo();
			fqtvInfo.setCompanyId(companyId);
			fqtvInfo.setFfpCompany(fqtvCompany);
			fqtvInfo.setFfpMembershipNumber(fqtvMembershipNumber);
			fqtvInfo.setTopTier(false);
			fqtvInfo.setQualifierId(otNumber);
			fqtvInfo.setTierLevel("");
		}
		else{
			List<String> cxKaTierLevels =  bizRuleConfig.getCxkaTierLevel();
			List<String> amTierLevels = bizRuleConfig.getAmTierLevel();
			List<String> topTiers = bizRuleConfig.getTopTier();
			// get MPO top tier level
			String tierLevel = priorityDetails.stream()
					.filter(detail -> detail != null && cxKaTierLevels.contains(detail.getTierLevel())
							&& topTiers.contains(detail.getTierLevel()))
					.map(PriorityDetailsType :: getTierLevel).findFirst()
					.orElse(null);
			
			// if tierLevel is empty, get MPO tier level
			if(StringUtils.isEmpty(tierLevel)){
				tierLevel = priorityDetails.stream()
						.filter(detail -> detail != null && cxKaTierLevels.contains(detail.getTierLevel()))
						.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);
			}
			
			// if tierLevel is empty, get AM tier level
			if(StringUtils.isEmpty(tierLevel)){
				tierLevel = priorityDetails.stream()
						.filter(detail -> detail != null && amTierLevels.contains(detail.getTierLevel()))
						.map(PriorityDetailsType::getTierLevel).findFirst().orElse(null);
			}

			if (tierLevel != null) {
				fqtvInfo = new RetrievePnrFFPInfo();
				fqtvInfo.setCompanyId(companyId);
				fqtvInfo.setFfpCompany(fqtvCompany);
				fqtvInfo.setFfpMembershipNumber(fqtvMembershipNumber);
				fqtvInfo.setTopTier(topTiers.contains(tierLevel));
				fqtvInfo.setQualifierId(otNumber);
				fqtvInfo.setTierLevel(tierLevel);
			}else {
				fqtvInfo = new RetrievePnrFFPInfo();
				fqtvInfo.setCompanyId(companyId);
				fqtvInfo.setFfpCompany(fqtvCompany);
				fqtvInfo.setFfpMembershipNumber(fqtvMembershipNumber);
				fqtvInfo.setTopTier(false);
				fqtvInfo.setQualifierId(otNumber);
				fqtvInfo.setTierLevel("");
			}
		}
		return fqtvInfo;
	}
	
	/**
	 * get PassengerSegment by passengerId and segmentId
	 * @param passengerSegments
	 * @param passengerId
	 * @param segmentId
	 */
	public static RetrievePnrPassengerSegment getPassengerSegmentByIds(List<RetrievePnrPassengerSegment> passengerSegments, String passengerId, String segmentId){
		for (int i = 0; i < passengerSegments.size(); i++) {
			if(passengerSegments.get(i).getPassengerId().equals(passengerId) && passengerSegments.get(i).getSegmentId().equals(segmentId)) {
				return passengerSegments.get(i);
			}
		}
		return null;
	}
	
	/**
	 * get PassengerSegment by passengerId
	 * @param passengerSegments
	 * @param passengerId
	 * @param segmentId
	 */
	public static List<RetrievePnrPassengerSegment> getPassengerSegmentByIds(List<RetrievePnrPassengerSegment> passengerSegments, String passengerId){
		List<RetrievePnrPassengerSegment> results = new ArrayList<>();
		for (int i = 0; i < passengerSegments.size(); i++) {
			if(passengerSegments.get(i).getPassengerId().equals(passengerId)) {
				results.add(passengerSegments.get(i));
			}
		}
		return results;
	}
	
	/**
	 * 
	* @Description get PassengerSegment by passengerId
	* @param passengerSegments
	* @param segmentId
	* @return List<RetrievePnrPassengerSegment>
	* @author haiwei.jia
	 */
	public static List<RetrievePnrPassengerSegment> getPassengerSegmentBySegmentId(List<RetrievePnrPassengerSegment> passengerSegments, String segmentId){
		if(CollectionUtils.isEmpty(passengerSegments)){
			return new ArrayList<>();
		}
		
		List<RetrievePnrPassengerSegment> results = new ArrayList<RetrievePnrPassengerSegment>();
		for (int i = 0; i < passengerSegments.size(); i++) {
			if(passengerSegments.get(i).getSegmentId().equals(segmentId)) {
				results.add(passengerSegments.get(i));
			}
		}
		return results;
	}
	
	/**
	 * get Passenger by passengerId
	 * @param passengers
	 * @param passengerId
	 */
	public static RetrievePnrPassenger getPassengerById(List<RetrievePnrPassenger> passengers ,String passengerId) {
		for (int i = 0; i < passengers.size(); i++) {
			if(passengers.get(i).getPassengerID() != null && passengers.get(i).getPassengerID().equals(passengerId)) {
				return passengers.get(i);
			}
		}
		return null;
	}
	
	/**
	 * get Segment by segmentId
	 * @param passengers
	 * @param passengerId
	 */
	public static RetrievePnrSegment getSegmentById(List<RetrievePnrSegment> segments ,String segmentId) {
		for (int i = 0; i < segments.size(); i++) {
			if(segments.get(i).getSegmentID() != null && segments.get(i).getSegmentID().equals(segmentId)) {
				return segments.get(i);
			}
		}
		return null;
	}
	
	/**
	 * get Passengers by parentId
	 * @param passengers
	 * @param parentId
	 */
	private List<RetrievePnrPassenger> getPassengersByParentId(List<RetrievePnrPassenger> passengers ,String parentId) {
		List<RetrievePnrPassenger> retrievePnrPassengers = new ArrayList<>();
		for(RetrievePnrPassenger passenger : passengers) {
			String passengerId = passenger.getParentId();
			if(!StringUtils.isEmpty(passengerId) && passengerId.equals(parentId)) {
				retrievePnrPassengers.add(passenger);
			}
		}
		return retrievePnrPassengers;
	}

	/**
	 *  Parser flight.
	 * @param originDestinationDetailsList
	 * @param booking
	 */
	private void parserSegmentInfo(List<OriginDestinationDetails> originDestinationDetailsList,RetrievePnrBooking booking,Map<String,List<DataElement>> dataElementMapping){
		if(CollectionUtils.isEmpty(originDestinationDetailsList)){
			booking.setSegments(Collections.emptyList());
			return;
		}
		List<RetrievePnrSegment> segments = new ArrayList<>();
		List<ItineraryInfo> validSegments = originDestinationDetailsList.stream().filter(odl -> !CollectionUtils.isEmpty(odl.getItineraryInfo()))
				.flatMap(odd -> odd.getItineraryInfo().stream()).filter(this::isValidSegment)
				.collect(Collectors.toList());
		 
			for (ItineraryInfo itineraryInfo : validSegments) {
				RetrievePnrSegment segment = new RetrievePnrSegment();
				segment.setSegmentID(getIdByQualifier(itineraryInfo.getElementManagementItinerary(),SEGMENT_ID_QUALIFIER));
				segment.setLineNumber(String.valueOf(itineraryInfo.getElementManagementItinerary().getLineNumber()));
				parserArrivalTime(itineraryInfo,segment);
				parserDepartureTime(itineraryInfo,segment);
				//flight base info 
				parserSegmentBaseInfo(itineraryInfo,segment,booking.getSkList());
				segment.setFqtu((RetrievePnrFFPInfo)Optional.ofNullable(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_FQTU)).orElse(Collections.emptyList())
						.stream().filter(de -> (de.getReferenceStList() != null && de.getReferenceStList().contains(segment.getSegmentID()))||validSegments.size()==1)
						.findFirst().orElse(null));
				segments.add(segment);
		}
		booking.setSegments(segments);
	}
		/**
	 * check the flight is valid
	 * @param itineraryInfo
	 * @return
	 */
	private boolean isValidSegment(ItineraryInfo itineraryInfo){
		
		if(itineraryInfo.getTravelProduct() == null ||itineraryInfo.getTravelProduct().getProductDetails() == null ){
			logger.warn("Cannot find flight infomation from the product element(1A response), will igmore this segment.");
			return false;
		}
		
		if (itineraryInfo.getElementManagementItinerary().getSegmentName() == null || !VALID_SEGMENT_NAME
				.contains(itineraryInfo.getElementManagementItinerary().getSegmentName().toUpperCase())) {
			logger.info(String.format("Invalid segment name found, will ignore it, segment name:[%s]",
					itineraryInfo.getElementManagementItinerary().getSegmentName()));
			return false;
		}
		
		/*
		 * For Information (ARNK) Segments with a specific date entered, the
		 * system sorts segments by date
		 * 
		 * For Information (ARNK) Segment without specific dates, the system
		 * adds ARNK segments. Between open-jaw segments. e.g. PNR with flight
		 * segments for HKGSIN and BKKHKG, an ARNK segment is added between SIN
		 * and BKK.
		 * 
		 * ARNK (Arrival not known) The arrival city of the previous segment
		 * does not match the departure city of the next segment in the
		 * itinerary, or the arrival is by unspecified transport. Information
		 * segments are not mandatory, but they do ensure that the PNR contains
		 * as much detail as possible concerning the passenger's itinerary.
		 * 
		 */	
		if( OneAConstants.SEGMENTS_IDENTIFICATION_ARNK.equals(itineraryInfo.getTravelProduct().getProductDetails().getIdentification())){
			logger.info("ARNK Segment found, will ignore it");
			return false;
		}
		if(Objects.equals(INVALID_SEGMENT_PROCESSING, Optional.ofNullable(itineraryInfo.getTravelProduct()).map(TravelProductInformationTypeI193100S::getProcessingIndicator).orElse(null))){
			logger.info("Invalid Processing Indicator, will igmore this segment");
			return false;
		}
		
		if ( itineraryInfo.getTravelProduct().getBoardpointDetail() == null
				|| itineraryInfo.getTravelProduct().getOffpointDetail() == null
				|| StringUtils.isEmpty(itineraryInfo.getTravelProduct().getBoardpointDetail().getCityCode())
				|| StringUtils.isEmpty(itineraryInfo.getTravelProduct().getOffpointDetail().getCityCode())) {
			logger.warn("Found invalid ItineraryInfo(segment element in pnr).");
			return false;
		}
		return true;
	}
	/**
	 * Parser segment base info.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserSegmentBaseInfo(ItineraryInfo itineraryInfo, RetrievePnrSegment segment, List<RetrievePnrDataElements> sks) {
		if(sks == null) {
			sks = Lists.newArrayList();
		}
		
		if (itineraryInfo.getTravelProduct() != null) {
			// flight operate, market company
			if (itineraryInfo.getTravelProduct().getCompanyDetail() != null) {
				// operating info
				//segment.setOperateCompany(itineraryInfo.getTravelProduct().getCompanyDetail().getIdentification());
				segment.setMarketCompany(itineraryInfo.getTravelProduct().getCompanyDetail().getIdentification());
			}
			// flight operate, market company
			if (itineraryInfo.getTravelProduct().getProductDetails() != null) {
				segment.setSubClass(itineraryInfo.getTravelProduct().getProductDetails().getClassOfService());
				segment.setMarketSubClass(itineraryInfo.getTravelProduct().getProductDetails().getClassOfService());
				segment.setMarketSegmentNumber(itineraryInfo.getTravelProduct().getProductDetails().getIdentification());
			}
			// origin port
			if (itineraryInfo.getTravelProduct().getBoardpointDetail() != null) {
				segment.setOriginPort(itineraryInfo.getTravelProduct().getBoardpointDetail().getCityCode());
			}
			// dest port
			if (itineraryInfo.getTravelProduct().getOffpointDetail() != null) {
				segment.setDestPort(itineraryInfo.getTravelProduct().getOffpointDetail().getCityCode());
			}
			// originTerminal
			if (itineraryInfo.getFlightDetail() != null
					&& itineraryInfo.getFlightDetail().getDepartureInformation() != null) {
				segment.setOriginTerminal(
						itineraryInfo.getFlightDetail().getDepartureInformation().getDepartTerminal());
			}
			// destTerminal
			if (itineraryInfo.getFlightDetail() != null
					&& itineraryInfo.getFlightDetail().getArrivalStationInfo() != null) {
				segment.setDestTerminal(itineraryInfo.getFlightDetail().getArrivalStationInfo().getTerminal());
			}
			//segment status
			if(itineraryInfo.getRelatedProduct() != null && !CollectionUtils.isEmpty(itineraryInfo.getRelatedProduct().getStatus())){
				//TODO need to confirm with hing use get(0) is correct.
				segment.setStatus(itineraryInfo.getRelatedProduct().getStatus());
			}
			String equipment = Optional.ofNullable(itineraryInfo.getFlightDetail()).map(AdditionalProductDetailsTypeI::getProductDetails).map(AdditionalProductTypeI::getEquipment).orElse(null);
			//aircraft type
			if(StringUtils.hasText(equipment)){
				equipment = OneAConstants.EQUIPMENT_TRS.equals(equipment)?OneAConstants.EQUIPMENT_TRN:equipment;//TODO replace TRS by by TRN 
				segment.setAirCraftType(equipment);
			}else{
				//non air cannot find operate company in pnr, so using market, DB will match OD first.
				String airType = nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, segment.getOriginPort(), segment.getDestPort(), segment.getMarketCompany());
				if(StringUtils.isEmpty(airType)){
					logger.warn(String.format("Cannot find segment type from pnr and non air table for segment:[%s] OD:[%s-%s]", segment.getMarketCompany()+segment.getMarketSegmentNumber(),segment.getOriginPort(),segment.getDestPort()));
				}else{
					segment.setAirCraftType(airType);
				}
				
				
			}
		}
		//OperateCompany & OperateSegmentNumber
		if(!CollectionUtils.isEmpty(itineraryInfo.getItineraryfreeFormText())){
			setPnrOperateCompanyAndSegmentNumber(segment, itineraryInfo.getItineraryfreeFormText());
		}
	}
	
	/**
	 * Parser segment arrival time.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserArrivalTime(ItineraryInfo itineraryInfo, RetrievePnrSegment segment){
		RetrievePnrDepartureArrivalTime arrivalTime = new RetrievePnrDepartureArrivalTime();
		if(itineraryInfo.getTravelProduct() != null && itineraryInfo.getTravelProduct().getProduct() != null){
			if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getArrDate())){
				return;
			}else if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getArrTime())){
				arrivalTime.setPnrTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getArrDate(), DateUtil.DATE_PATTERN_DDMMYY, RetrievePnrDepartureArrivalTime.TIME_FORMAT));
			}else{
				arrivalTime.setPnrTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getArrDate()+itineraryInfo.getTravelProduct().getProduct().getArrTime(), DateUtil.DATE_PATTERN_DDMMYYHHMM, RetrievePnrDepartureArrivalTime.TIME_FORMAT));
			}
			segment.setArrivalTime(arrivalTime);
		}
	}
	
	/**
	 * Parser segment departure time.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserDepartureTime(ItineraryInfo itineraryInfo, RetrievePnrSegment segment){
		RetrievePnrDepartureArrivalTime departureTime = new RetrievePnrDepartureArrivalTime();	
		if(itineraryInfo.getTravelProduct() != null && itineraryInfo.getTravelProduct().getProduct() != null){
			if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getDepDate())){
				return;
			}else if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getDepTime())){
				departureTime.setPnrTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getDepDate(), DateUtil.DATE_PATTERN_DDMMYY, RetrievePnrDepartureArrivalTime.TIME_FORMAT));
			}else{
				departureTime.setPnrTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getDepDate()+itineraryInfo.getTravelProduct().getProduct().getDepTime(), DateUtil.DATE_PATTERN_DDMMYYHHMM, RetrievePnrDepartureArrivalTime.TIME_FORMAT));
			}
			segment.setDepartureTime(departureTime);
		}
	}
	
	/**
	 * Parser Passenger detail Info
	 * @param pnrReply
	 * @param booking
	 */
	private void parserPassengerInfo(PNRReply pnrReply,RetrievePnrBooking booking,Map<String,List<DataElement>> dataElementMapping ){
		paeserPaxIdAndNameInfo(pnrReply.getTravellerInfo(), booking, dataElementMapping);
		parserPaxInfoDetail(pnrReply.getDataElementsMaster(), booking);
		parserStaffInfo(booking,dataElementMapping);
		parseFpElement(pnrReply.getDataElementsMaster(), booking);
	}
	
	/**
	 * Parse FP element for per passengers
	 * 
	 * @param dataElementsMaster
	 * @param booking
	 */
	private void parseFpElement(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> vaildFPElements = dataElementsMaster.getDataElementsIndiv().stream().filter(div -> SEGMENT_NAME_FP.equals(div.getElementManagementData().getSegmentName()) 
				&& CollectionUtils.isNotEmpty(div.getOtherDataFreetext())
				&& div.getOtherDataFreetext().get(0) != null
				&& !StringUtils.isEmpty(div.getOtherDataFreetext().get(0).getLongFreetext()))
				.collect(Collectors.toList());
		if(CollectionUtils.isEmpty(vaildFPElements)) {
			return;
		}
		
		/** if without any PTs and contains PT are exist in FPs at the same time, all invalidated */
		if(!checkFpElementCounts(vaildFPElements)) {
			return;
		}
		
		for(DataElementsIndiv vaildFPElement : vaildFPElements) {
			if(vaildFPElement == null) {
				continue;
			}
			
			String fpLongFreeText = vaildFPElement.getOtherDataFreetext().get(0).getLongFreetext();

			// if no any PT found in FP element, using first element set all passengers' fpLongFreeText
			if(vaildFPElement.getReferenceForDataElement() == null || CollectionUtils.isEmpty(vaildFPElement.getReferenceForDataElement().getReference())) {
				booking.getPassengers().stream().forEach(pax -> pax.getFpLongFreetexts().add(fpLongFreeText));
				return;
			}
			
			List<String> ptNumbers = getReferenceNumbers(vaildFPElement.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER);
			
			// if no any PT found in FP element, using first element set all passengers' fpLongFreeText
			if(CollectionUtils.isEmpty(ptNumbers)) {
				booking.getPassengers().stream().forEach(pax -> pax.getFpLongFreetexts().add(fpLongFreeText));
				return;
			}
			
			for(String ptNumber : ptNumbers) {
				if(fpLongFreeText.startsWith("INF")) {// TO handle "INF CCVI/XXXXXXXXXXXX1111/1020/HKD641/N65655"
					List<RetrievePnrPassenger> infantPassengers = getPassengersByParentId(booking.getPassengers(), ptNumber);
					infantPassengers.stream().filter(pax -> pax != null).forEach(pax -> pax.getFpLongFreetexts().add(fpLongFreeText));
					continue;
				} 
				RetrievePnrPassenger passenger = getPassengerById(booking.getPassengers(), ptNumber);					
				// if passenger not be found or passenger fpFreeLongText already set, skip this passenger
				if(passenger != null) {
					passenger.getFpLongFreetexts().add(fpLongFreeText);
				}		
			}
		}
	}
		
	/**
	 * if without any PTs and contains PT are exist in FPs at the same time, all invalidated
	 * 
	 * @param dataElementsIndivs
	 * @return
	 */
	private boolean checkFpElementCounts(List<DataElementsIndiv> dataElementsIndivs) {
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return false;
		}
		
		/** if without any PTs and contains PT are exist in FPs at the same time, all invalidated */
		boolean noPtExist = dataElementsIndivs.stream().anyMatch(div -> div.getReferenceForDataElement() == null 
				|| CollectionUtils.isEmpty(div.getReferenceForDataElement().getReference()) 
				|| CollectionUtils.isEmpty(getReferenceNumbers(div.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER)));
		boolean havePt = dataElementsIndivs.stream().anyMatch(div -> div.getReferenceForDataElement() != null 
				&& !CollectionUtils.isEmpty(div.getReferenceForDataElement().getReference()) 
				&& !CollectionUtils.isEmpty(getReferenceNumbers(div.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER)));
		
		return !(noPtExist && havePt);
	}

	/**
	 * parserStaffInfo form dataElementMapping
	 * @param booking
	 * @param dataElementMapping
	 */
	private void parserStaffInfo(RetrievePnrBooking booking, Map<String, List<DataElement>> dataElementMapping) {
		if (CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_STAFF))) {
			return;
		}
		for (RetrievePnrPassenger pnrPax : booking.getPassengers()) {
			List<DataElement> dataElements = dataElementMapping.get(DATAELEMENT_MAPPING_KEY_STAFF);
			RetrievePnrStaffDetail staffDetail = (RetrievePnrStaffDetail) dataElements
					.stream()
					.filter(de -> booking.getPassengers().size() == 1 || CollectionUtils.isEmpty(de.getReferencePtList())
							|| (!CollectionUtils.isEmpty(de.getReferencePtList()) && de.getReferencePtList().contains(pnrPax.getPassengerID())))
					.findFirst()
					.orElse(null);
			pnrPax.setStaffDetail(staffDetail);
		}
	}
	/**
	 * Parser Passenger id & name
	 * @param travellerInfos
	 * @param booking
	 * @param dataElementMapping 
	 */
	private void paeserPaxIdAndNameInfo(List<TravellerInfo> travellerInfos, RetrievePnrBooking booking, Map<String, List<DataElement>> dataElementMapping) {
		if(CollectionUtils.isEmpty(travellerInfos)){
			return ;
		}
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		for (TravellerInfo travellerInfo : travellerInfos){
			//id
			String passengerId = getIdByQualifier(travellerInfo.getElementManagementPassenger(),PASSENGER_ID_QUALIFIER);
			if(StringUtils.isEmpty(passengerId)){
				continue;
			}
			//name info & infant's info
			List<RetrievePnrPassenger> paxsRetrievedById = parserPassengerNameInfo(travellerInfo.getPassengerData(), passengerId,booking.getSsrList());
			
			if(!CollectionUtils.isEmpty(paxsRetrievedById)){
				passengers.addAll(paxsRetrievedById);
			}
		}
		parserSsrInfant(passengers, dataElementMapping);
		assignParentidForNonParentInf(passengers);
		booking.setPassengers(passengers);
	}

	/**
	 * 
	 * @param passengers
	 * @param dataElementMapping
	 */
	private void parserSsrInfant(List<RetrievePnrPassenger> passengers, Map<String, List<DataElement>> dataElementMapping) {
		if(CollectionUtils.isEmpty(dataElementMapping.get(DATAELEMENT_MAPPING_KEY_INFT))){
			return;
		}
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream().map(ConstantData::getValue).collect(Collectors.toList());
		for(DataElement element: dataElementMapping.get(DATAELEMENT_MAPPING_KEY_INFT)){
			RetrievePnrSsrInfant infant = (RetrievePnrSsrInfant) element;
			
			RetrievePnrPassenger matchedPax =  getMatchedPassenger(passengers, element.getReferencePtList(), true);
			
			addSsrInfant(passengers,infant, matchedPax,nameTitleList);
		}
	}

	/**
	 * Get matched passenger
	 * @param passengers
	 * @param releatedPassengerIds
	 * @param matchTheOneIfOnlyOnePassenger
	 * @return
	 */
	private RetrievePnrPassenger getMatchedPassenger(List<RetrievePnrPassenger> passengers, List<String> releatedPassengerIds, boolean matchTheOneIfOnlyOnePassenger){
	
		if(passengers.size() == 1 && matchTheOneIfOnlyOnePassenger){
			return passengers.get(0);
		}
		
		if(releatedPassengerIds != null){
			return passengers.stream().filter(pax->releatedPassengerIds.contains(pax.getPassengerID())).findFirst().orElse(null);
		}
		
		return null;
		
	} 
	/**
	 * 
	 * @param passengers
	 * @param parentIds
	 * @param infant
	 * @param passenger
	 */
	private void addSsrInfant(List<RetrievePnrPassenger> passengers,
			RetrievePnrSsrInfant infant, RetrievePnrPassenger idMatchedpassenger, List<String> nameTitleList) {

		// ignore the ssr if cannot find related passenger
		if (idMatchedpassenger == null) {
			logger.warn("Infant cannot find related passengerId, ignore this INFT SSR");
			return;
		}
		// Occupy case--INS
		if (infant.isOccupySeat()) {
			idMatchedpassenger.setPassengerType(PASSENGER_TYPE_INS);
			// non Occupy case--INF
		} else {
			// check the matched passenger has infant
			RetrievePnrPassenger matchedPaxInfant = passengers.stream().filter(pax -> Objects.equals(idMatchedpassenger.getPassengerID(), pax.getParentId())).findFirst().orElse(null);
			 if (matchedPaxInfant == null) {
				boolean nameMatched = Objects.equals(idMatchedpassenger.getFamilyName(), infant.getFamilyName())
						&& NameIdentficationUtil.compareWithoutTitle(idMatchedpassenger.getGivenName(), infant.getGivenName(), nameTitleList);
				if (nameMatched) {
					if (!PASSENGER_TYPE_INS.equals(idMatchedpassenger.getPassengerType())) {
						idMatchedpassenger.setPassengerType(PASSENGER_TYPE_INF);
					} else {
						logger.warn("Found non-occupy seat SSR related INS passseger, will ignore this SSR.");
					}
				} else {
					RetrievePnrPassenger infantPax = new RetrievePnrPassenger();
					infantPax.setPassengerType(PASSENGER_TYPE_INF);
					infantPax.setFamilyName(infant.getFamilyName());
					infantPax.setGivenName(infant.getGivenName());
					RetrievePnrDob dob = new RetrievePnrDob();
					dob.setBirthDateDay(infant.getBirthDateDay());
					dob.setBirthDateMonth(infant.getBirthDateMonth());
					dob.setBirthDateYear(infant.getBirthDateYear());
					infantPax.setDob(dob);
					infantPax.setParentId(idMatchedpassenger.getPassengerID());
					infantPax.setPassengerID(idMatchedpassenger.getPassengerID() + PASSENGER_INFANT_ID_SUFFIX);
					passengers.add(infantPax);
				}
			}else{
				logger.info("SSR INFT releated passenger has infant already, will ingnor the SSR.");
			}
			
		}
	}
	/**
	 * Parser Passenger(including infant without seat) name & type info
	 * @param passengerDatas
	 * @param passenger
	 */
	private List<RetrievePnrPassenger> parserPassengerNameInfo(List<PassengerData> passengerDatas, String passengerId,List<RetrievePnrDataElements> ssrList){
		if(CollectionUtils.isEmpty(passengerDatas)) {
			return Collections.emptyList();
		}
		boolean hasChldSsr = false;
		if(CollectionUtils.isNotEmpty(ssrList)){
			 hasChldSsr = ssrList.stream().anyMatch(ssr->SSR_TYPE_CHILD.equals(ssr.getType()) && passengerId.equals(ssr.getPassengerId()));
		}
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		for(PassengerData passengerData : passengerDatas){
			TravellerSurnameInformationType260693C traveller = passengerData.getTravellerInformation().getTraveller();
			String surname = traveller != null ? traveller.getSurname() : null;
			List<TravellerDetailsType260694C> list = passengerData.getTravellerInformation().getPassenger();
			//the flag of Multiple pax in same elment, usually inft with adt togther 
			boolean hasMultiplePaxSamePt=passengerDatas.size() > 1 || list.size() > 1;
			for(int i = 0; i < list.size(); i++) {
				RetrievePnrPassenger passenger = new RetrievePnrPassenger();
				passenger.setGivenName(list.get(i).getFirstName());
				passenger.setFamilyName(surname);
				String paxType = getPassegerTypeByPnePaXTypeAndSsrInfo(list.get(i).getType(), hasChldSsr);
				passenger.setPassengerType(paxType);
				//only 1A response INF need special pax id 
				if(PASSENGER_TYPE_INF.equals(list.get(i).getType()) && hasMultiplePaxSamePt){
					passenger.setPassengerID(passengerId + PASSENGER_INFANT_ID_SUFFIX);
					passenger.setParentId(passengerId);
				}else{
					passenger.setPassengerID(passengerId);
				}
				// if there is more than one pax in the list and type of the passenger is ADT, don't parse the DOB because it belongs to the infant pax
				if (list.size() == 1 || !PASSENGER_TYPE_ADT.equals(passenger.getPassengerType())) {
					parserDOB(passenger, passengerDatas.get(0).getDateOfBirth());
				}
				passengers.add(passenger);
			}
		}
		return passengers;
	}
	/**
	 * assign the hasnot parent inf to ADT, keep parentid empty if there no valid ADT to assign infant
	 * @param passengers
	 */
	private void assignParentidForNonParentInf(List<RetrievePnrPassenger> passengers){
	
		List<RetrievePnrPassenger> nonParentPaxs = passengers.stream().filter(pax->PASSENGER_TYPE_INF.equals(pax.getPassengerType()) && StringUtils.isEmpty(pax.getParentId())).collect(Collectors.toList());
		
		if(CollectionUtils.isNotEmpty(nonParentPaxs)){
			List<String> parentIds = passengers.stream().filter(pax->!StringUtils.isEmpty(pax.getParentId())).map(RetrievePnrPassenger::getParentId).collect(Collectors.toList());
			nonParentPaxs.stream().forEach(nonParentPax->{
				RetrievePnrPassenger vlidAssignInfantPax = passengers.stream().filter(pax->PASSENGER_TYPE_ADT.equals(pax.getPassengerType())&&!parentIds.contains(pax.getPassengerID())).findFirst().orElse(null);
				if(vlidAssignInfantPax!=null){
					parentIds.add(vlidAssignInfantPax.getPassengerID());
					nonParentPax.setParentId(vlidAssignInfantPax.getPassengerID());
				}else{
					logger.warn("Infant cannot find valid ADT passenger to assign parent id");
				}
			});
		}
	}
	
	
	
	/**
	 * get PassegerType By PnePaXType And SsrInfo
	 * 
	 * @param pnrPaxType
	 * @param hasChldSsr
	 * @return
	 */
	private String getPassegerTypeByPnePaXTypeAndSsrInfo(String pnrPaxType, boolean hasChldSsr){
		
		if (!StringUtils.isEmpty(pnrPaxType)){
			pnrPaxType = passengerTypeDAO.getPaxType(MMBConstants.APP_CODE, pnrPaxType);
		}
		
		pnrPaxType = StringUtils.isEmpty(pnrPaxType) ? PASSENGER_TYPE_ADT : pnrPaxType;
		
		if(hasChldSsr && PASSENGER_TYPE_ADT.equals(pnrPaxType)){
			pnrPaxType = PASSENGER_TYPE_CHD;
		}
		
		return pnrPaxType;
	}
	/**
	 * 
	* @Description parse DOB to passenger from dateOfBirth in travellerInfo
	* @param passenger
	* @param dateOfBirth
	* @return void
	* @author haiwei.jia
	 */
	private void parserDOB(RetrievePnrPassenger passenger, DateAndTimeInformationType32722S dateOfBirth) {
		if (dateOfBirth == null || dateOfBirth.getDateAndTimeDetails() == null
				|| StringUtils.isEmpty(dateOfBirth.getDateAndTimeDetails().getDate())){
			return;
		}
		String birthDate = dateOfBirth.getDateAndTimeDetails().getDate();
		passenger.findDob().setBirthDateYear(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_YYYY));
		passenger.findDob().setBirthDateMonth(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_MM));
		passenger.findDob().setBirthDateDay(DateUtil.convertDateFormatForDob(birthDate, DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_DD));
	}
	
	/**
	 * handle the "CBBG" and "EXST"
	 * 
	 * @param pnrReply
	 * @param booking
	 * @author haiwei.jia
	 */
	private void handleExtraSeat(PNRReply pnrReply, RetrievePnrBooking booking){
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		
		// extra seat passenger list
		List<RetrievePnrPassenger> extraSeatPassengers = new ArrayList<>();
		// extra seat passenger segment list
		List<RetrievePnrPassengerSegment> extraSeatPassengerSegments = new ArrayList<>();
		
		for (RetrievePnrPassenger passenger : passengers) {
			// number of the passenger whose family name is the same with passenger' family name
			long sameFamilyNamecount = passengers.stream()
					.filter(pax -> pax != null && !StringUtils.isEmpty(pax.getFamilyName()) && pax.getFamilyName().equals(passenger.getFamilyName())).count();

			/** if passenger's given name is "CBBG" or "EXST", and exist a passenger 
			   who has same family name with passenger, do following logic */ 
			if ((EXTRA_SEAT_CBBG.equals(passenger.getGivenName()) || EXTRA_SEAT_EXST.equals(passenger.getGivenName()))
					&& sameFamilyNamecount > 1) {
				// check extra seat SSR
				extraSeatSsrCheck(pnrReply, booking, extraSeatPassengers, extraSeatPassengerSegments,
						passenger);
			}
		}
		//remove the extra seat passengers
		for(RetrievePnrPassenger passenger : extraSeatPassengers){
			passengers.remove(passenger);
		}
		
		//remove the extra seat passengerSegments
		for(RetrievePnrPassengerSegment passengerSegment : extraSeatPassengerSegments){
			passengerSegments.remove(passengerSegment);
		}
	}

	/**
	 * 
	* @Description check extra seat SSR, if find, populate the seat to owner 
	* @param pnrReply
	* @param passengerSegments
	* @param extraSeatPassengers
	* @param extraSeatPassengerSegments
	* @param passenger
	* @return void
	* @author haiwei.jia
	 */
	private void extraSeatSsrCheck(PNRReply pnrReply, RetrievePnrBooking booking,
			List<RetrievePnrPassenger> extraSeatPassengers,
			List<RetrievePnrPassengerSegment> extraSeatPassengerSegments, RetrievePnrPassenger passenger) {
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		//the passengerSegment list of passenger
		List<RetrievePnrPassengerSegment> passengerSegmentsOfPax = getPassengerSegmentByIds(passengerSegments,
				passenger.getPassengerID());
		
		for (RetrievePnrPassengerSegment passengerSegmentOfPax : passengerSegmentsOfPax) {
			if (passengerSegmentsOfPax == null) {
				continue;
			}
			String segmentId = passengerSegmentOfPax.getSegmentId();
			// find PT number in extra seat SSR 
			String passengerId = getPtNoOfExtraSeatSsr(pnrReply, passenger.getGivenName(), segmentId);
			if (!StringUtils.isEmpty(passengerId) && !StringUtils.isEmpty(segmentId)) {
				// populate extra seat to owner
				populateExtraSeatToOwner(booking, passengerSegmentOfPax, segmentId, passengerId);
				extraSeatPassengerSegments.add(passengerSegmentOfPax);
				if(!extraSeatPassengers.contains(passenger)){
					extraSeatPassengers.add(passenger);
				}
			}
		}
	}

	/**
	 * 
	* @Description populate extra seat to owner
	* @param passengerSegments
	* @param extraSeatPassengerSegment
	* @param segmentId
	* @param passengerId
	* @return void
	* @author haiwei.jia
	 */
	private void populateExtraSeatToOwner(RetrievePnrBooking booking,
			RetrievePnrPassengerSegment extraSeatPassengerSegment, String segmentId, String passengerId) {
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		List<RetrievePnrSeatDetail> extraSeats = new ArrayList<>();
		BigInteger qulifierId = null;
		
		// add seat of extraSeatPassenger into extraSeats list
		if(extraSeatPassengerSegment.getSeat() !=null){
			qulifierId = extraSeatPassengerSegment.getSeat().getQulifierId();
			if(qulifierId == null){
				return;
			}
			if(extraSeatPassengerSegment.getSeat().getSeatDetail() != null){
				extraSeats.add(extraSeatPassengerSegment.getSeat().getSeatDetail());
			}
			if(!CollectionUtils.isEmpty(extraSeatPassengerSegment.getSeat().getExtraSeats())){
				extraSeats.addAll(extraSeatPassengerSegment.getSeat().getExtraSeats());
			}
		}
		if(!CollectionUtils.isEmpty(extraSeats)){
			RetrievePnrPassengerSegment passengerSegmentOfOwner = getPassengerSegmentByIds(passengerSegments, passengerId, segmentId);
			if(passengerSegmentOfOwner == null){
				passengerSegmentOfOwner = new RetrievePnrPassengerSegment();
				passengerSegmentOfOwner.setPassengerId(passengerId);
				passengerSegmentOfOwner.setSegmentId(segmentId);
				passengerSegments.add(passengerSegmentOfOwner);
			}
			
			if(passengerSegmentOfOwner.findSeat().getQulifierId() == null 
					|| passengerSegmentOfOwner.findSeat().getQulifierId().equals(qulifierId)){
				populateExtraSeat(extraSeats, qulifierId, passengerSegmentOfOwner);
			} else if(passengerSegmentOfOwner.findSeat().getQulifierId().compareTo(qulifierId) < 0){
				booking.findInvalidOts().add(passengerSegmentOfOwner.findSeat().getQulifierId());
				passengerSegmentOfOwner.setSeat(new RetrievePnrSeat());
				populateExtraSeat(extraSeats, qulifierId, passengerSegmentOfOwner);
			} else{
				booking.findInvalidOts().add(qulifierId);
			}
			
		}
	}

	/**
	 * 
	* @Description populate extra seat to owner's passengerSegment
	* @param extraSeats
	* @param qulifierId
	* @param passengerSegmentOfOwner
	* @return void
	* @author haiwei.jia
	 */
	private void populateExtraSeat(List<RetrievePnrSeatDetail> extraSeats, BigInteger qulifierId,
			RetrievePnrPassengerSegment passengerSegmentOfOwner) {
		// if there is no qulifierId in owner's seat, set qulifiedId in
		if(passengerSegmentOfOwner.findSeat().getQulifierId() == null){
			passengerSegmentOfOwner.findSeat().setQulifierId(qulifierId);
		}
		// if no seatDetail in owner's seat, set with first seat in seatsOfPax
		if(passengerSegmentOfOwner.getSeat().getSeatDetail() == null){
			passengerSegmentOfOwner.getSeat().setSeatDetail(extraSeats.get(0));
			extraSeats.remove(0);
		}
		if(!CollectionUtils.isEmpty(extraSeats)){
			passengerSegmentOfOwner.findSeat().findExtraSeats().addAll(extraSeats);
		}
	}
	
	/**
	 * Get PT number of the extra seat SSR (CBBG/EXST) 
	 * whose segment name is same with extraSeatType and ST is same with SegmentId
	 * 
	 * @param pnrReply
	 * @param extraSeatType
	 * @param segmentId
	 * @return String
	 * @author haiwei.jia
	 */
	private String getPtNoOfExtraSeatSsr(PNRReply pnrReply,String extraSeatType, String segmentId){
		if (pnrReply == null || pnrReply.getDataElementsMaster() == null 
				|| StringUtils.isEmpty(extraSeatType)
				|| StringUtils.isEmpty(segmentId)) {
			return null;
		}
		for(DataElementsIndiv dataElementsIndiv : pnrReply.getDataElementsMaster().getDataElementsIndiv()){
			if(dataElementsIndiv.getServiceRequest() == null 
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| dataElementsIndiv.getReferenceForDataElement() == null
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				continue;
			}
			String ssrType = dataElementsIndiv.getServiceRequest().getSsr().getType();		
			List<String> ptNumbers = getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER);
			List<String> stNumbers = getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), SEGMENT_ID_QUALIFIER);
			if(extraSeatType.equals(ssrType) && segmentId.equals(stNumbers.get(0))){
				return ptNumbers.get(0);
			}
		}	
		return null;
	}
	
	/**
	 * Parser Passenger mobileNumber & email and more detail info
	 * @param dataElementsMaster
	 * @param booking
	 */
	private void parserPaxInfoDetail(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())){
			return ;
		}
		//email
		parserCTCContactInfo(dataElementsMaster, booking, EMAIL_TYPE_CTCE);
		//mobileNumber
		parserCTCContactInfo(dataElementsMaster, booking, PHONE_TYPE_CTCM);
		
		//TODO parser AP contact info
		//parserAPContactInfo(dataElementsMaster, booking, phoneTypeMap);
		
		//emergency contact info
		parserEmrContactInfo(dataElementsMaster,booking.getPassengers());
		//destination address
		parserDesAddress(dataElementsMaster,booking);
		//residence address
		parserResAddress(dataElementsMaster,booking);
	}

	/**
	 * 
	* @Description parse residence address
	* @param dataElementsMaster
	* @param booking
	* @return void
	* @author haiwei.jia
	 */
	private void parserResAddress(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for (int i = 0; i < dataElementsIndivs.size(); i ++) {
			DataElementsIndiv dataElementsIndiv = dataElementsIndivs.get(i);
			if(dataElementsIndiv == null || dataElementsIndiv.getReferenceForDataElement() == null 
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference()) 
					|| dataElementsIndiv.getElementManagementData() == null
					|| dataElementsIndiv.getServiceRequest() == null){
				continue;
			}
			
			List<String> residenceAddrSsrTypes = tbSsrTypeDAO
					.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA).stream()
					.map(TbSsrTypeModel::getValue).collect(Collectors.toList());
			String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			if(StringUtils.isEmpty(segmentName) || !segmentName.equals(SEGMENT_NAME_SSR) 
					|| dataElementsIndiv.getServiceRequest().getSsr() == null
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					|| !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					||  StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| !residenceAddrSsrTypes.contains(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| CollectionUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText())){
				continue;
			}
			
			String freeText = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().get(0);
			if(StringUtils.isEmpty(freeText)
					|| !TYPE_IN_FREE_TEXT_R.equals(FreeTextUtil.getFreeTextTypeFromAddressDetailsFreeText(freeText))){
				continue;
			}
			
			//get residence address from freetext
			RetrievePnrAddressDetails resAddress = getAddressDetails(dataElementsIndiv);
			
			List<ReferencingDetailsType111975C> references = dataElementsIndivs.get(i).getReferenceForDataElement().getReference();			
			//set residence address value in booking
			setResAddressValue(resAddress, references, booking);
		}
	}

	/**
	 * 
	 * @Description parse destination address
	 * @param dataElementsMaster
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 */
	private void parserDesAddress(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for (int i = 0; i < dataElementsIndivs.size(); i ++) {
			DataElementsIndiv dataElementsIndiv = dataElementsIndivs.get(i);
			if(dataElementsIndiv == null || dataElementsIndiv.getReferenceForDataElement() == null 
					|| CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference()) 
					|| dataElementsIndiv.getElementManagementData() == null
					|| dataElementsIndiv.getServiceRequest() == null){
				continue;
			}
			
			List<String> destinationAddrSsrTypes = tbSsrTypeDAO
					.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA).stream()
					.map(TbSsrTypeModel::getValue).collect(Collectors.toList());
			String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			if(StringUtils.isEmpty(segmentName) || !segmentName.equals(SEGMENT_NAME_SSR) 
					|| dataElementsIndiv.getServiceRequest().getSsr() == null
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					|| !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					||  StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| !destinationAddrSsrTypes.contains(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| CollectionUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText())){
				continue;
			}
			
			String freeText = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().get(0);
			if(StringUtils.isEmpty(freeText)
					|| !TYPE_IN_FREE_TEXT_D.equals(FreeTextUtil.getFreeTextTypeFromAddressDetailsFreeText(freeText))){
				continue;
			}
			
			//get destinaton address from freetext
			RetrievePnrAddressDetails desAddress = getAddressDetails(dataElementsIndiv);
			
			List<ReferencingDetailsType111975C> references = dataElementsIndivs.get(i).getReferenceForDataElement().getReference();			
			//set destination address value in booking
			setDesAddressValue(desAddress, references, booking);
		}
	}

	/**
	 * 
	 * @Description get address details from freetext
	 * @param freeText
	 * @return RetrievePnrDesAddress
	 * @author haiwei.jia
	 */
	private RetrievePnrAddressDetails getAddressDetails(DataElementsIndiv dataElementsIndiv) {
		String freeText = dataElementsIndiv.getServiceRequest().getSsr().getFreeText().get(0);
		BigInteger qualifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
		
		RetrievePnrAddressDetails addressDetails = new RetrievePnrAddressDetails();
		if(!StringUtils.isEmpty(freeText)){
			addressDetails.setStreet(FreeTextUtil.getStreetNameFromAddressDetailsFreeText(freeText));
			addressDetails.setCity(FreeTextUtil.getCityFromAddressDetailsFreeText(freeText));
			addressDetails.setCountry(FreeTextUtil.getCountryFromAddressDetailsFreeText(freeText));
			addressDetails.setStateCode(FreeTextUtil.getStateCodeFromAddressDetailsFreeText(freeText));
			addressDetails.setZipCode(FreeTextUtil.getZipCodeFromAddressDetailsFreeText(freeText));
			addressDetails.setQualifierId(qualifierId);
			//judge if this address is for infant
			if(StringUtils.isEmpty(FreeTextUtil.getInfIndicatorFromAddressDetailsFreeText(freeText))){
				addressDetails.setInfAddress(false);
			}
			else{
				addressDetails.setInfAddress(true);
			}
		}
		return addressDetails;
	}
	
	/**
	 * 
	 * @Description set residence address in booking
	 * @param resAddress
	 * @param references
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 */
	private void setResAddressValue(RetrievePnrAddressDetails resAddress, List<ReferencingDetailsType111975C> references, RetrievePnrBooking booking) {
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		int stCount = segmentIds.size();
		for(String pt : passengerIds) {
			//if is custom level information, set it in passenger
			if(stCount == 0) {
				RetrievePnrPassenger passenger = getPassengerById(passengers, pt);

				if(passenger == null) {
					passenger = new RetrievePnrPassenger();
					passenger.setParentId(pt);
					passenger.getResAddresses().add(resAddress);
					passengers.add(passenger);
				} else {
					passenger.getResAddresses().add(resAddress);
				}
			}
			//if is product level information, set it in passengerSegment
			else {
				for(String st : segmentIds) {
					RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, pt, st);
					if(passengerSegment == null) {
						passengerSegment = new RetrievePnrPassengerSegment();
						passengerSegment.setPassengerId(pt);
						passengerSegment.setSegmentId(st);
						passengerSegment.getResAddresses().add(resAddress);
						passengerSegments.add(passengerSegment);
					} else {
						passengerSegment.getResAddresses().add(resAddress);
					}
				}
			}	
		}
		
	}
	
	/**
	 * 
	 * @Description set destination address in booking
	 * @param desAddress
	 * @param references
	 * @param booking
	 * @return void
	 * @author haiwei.jia
	 */
	private void setDesAddressValue(RetrievePnrAddressDetails desAddress, List<ReferencingDetailsType111975C> references, RetrievePnrBooking booking) {
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = booking.getPassengerSegments();
		int stCount = segmentIds.size();
		for(String pt : passengerIds) {
			//if is custom level information, set it in passenger
			if(stCount == 0) {
				RetrievePnrPassenger passenger = getPassengerById(passengers, pt);
				if(passenger == null) {
					passenger = new RetrievePnrPassenger();
					passenger.setParentId(pt);
					passenger.getDesAddresses().add(desAddress);
					passengers.add(passenger);
				} else {
					passenger.getDesAddresses().add(desAddress);
				}
			}
			//if is product level information, set it in passengersegment
			else {
				for(String st : segmentIds) {
					RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(passengerSegments, pt, st);
					if(passengerSegment == null) {
						passengerSegment = new RetrievePnrPassengerSegment();
						passengerSegment.setPassengerId(pt);
						passengerSegment.setSegmentId(st);
						passengerSegment.getDesAddresses().add(desAddress);
						passengerSegments.add(passengerSegment);
					} else {
						passengerSegment.getDesAddresses().add(desAddress);
					}
				}
			}	
		}
		
	}

	/**
	 * 
	 * @Description parse emergency contact info 
	 * @param dataElementsMaster
	 * @param passengers
	 * @author haiwei.jia
	 */
	private void parserEmrContactInfo(DataElementsMaster dataElementsMaster,
			List<RetrievePnrPassenger> passengers) {
		if(dataElementsMaster == null || CollectionUtils.isEmpty(dataElementsMaster.getDataElementsIndiv())) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for (int i = 0; i < dataElementsIndivs.size(); i ++) {
			DataElementsIndiv dataElementsIndiv = dataElementsIndivs.get(i);
			if(dataElementsIndiv == null
					|| dataElementsIndiv.getElementManagementData() == null
					|| dataElementsIndiv.getServiceRequest() == null){
				continue;
			}
			
			List<String> emrContactSsrTypes = tbSsrTypeDAO
					.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC).stream()
					.map(TbSsrTypeModel::getValue).collect(Collectors.toList());
			String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
			if(StringUtils.isEmpty(segmentName) || !segmentName.equals(SEGMENT_NAME_SSR)
					|| dataElementsIndiv.getServiceRequest().getSsr() == null
					|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					|| !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
					||  StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| !emrContactSsrTypes.contains(dataElementsIndiv.getServiceRequest().getSsr().getType())
					|| CollectionUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getFreeText())){
				continue;
			}
			
			//get emergency contact info from ssr
			RetrievePnrEmrContactInfo emrContactInfo = getEmrContactInfo(dataElementsIndiv);
			
			List<ReferencingDetailsType111975C> references = new ArrayList<>();
			if (dataElementsIndiv.getReferenceForDataElement() != null && !CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				references = dataElementsIndiv.getReferenceForDataElement().getReference();
			}
			List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
			
			// if the PCTC has no pax association and there's only one pax in the booking, prepopulate the emr contact to the pax
			if (CollectionUtils.isEmpty(passengerIds) && passengers != null && passengers.size() == 1) {
				passengers.get(0).getEmrContactInfos().add(emrContactInfo);
			} 
			// else prepopulate the emr contact to associated pax
			else {
				//set emergency contact info for every pt
				for(String pt : passengerIds){
					RetrievePnrPassenger passenger = getPassengerById(passengers, pt);
					if(passenger == null){
						passenger = new RetrievePnrPassenger();
						passenger.setPassengerID(pt);
						passenger.getEmrContactInfos().add(emrContactInfo);
						passengers.add(passenger);
					} else{
						passenger.getEmrContactInfos().add(emrContactInfo);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description get emergency contact info from SSR
	 * @param ssr
	 * @return RetrievePnrEmrContactInfo
	 * @author haiwei.jia
	 */
	private RetrievePnrEmrContactInfo getEmrContactInfo(DataElementsIndiv dataElementsIndiv) {
		SpecialRequirementsTypeDetailsTypeI ssr = dataElementsIndiv.getServiceRequest().getSsr();
		BigInteger qualifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
		
		RetrievePnrEmrContactInfo emrContactInfo = new RetrievePnrEmrContactInfo();
		if(!StringUtils.isEmpty(ssr.getFreeText().get(0))){
			String freeText = ssr.getFreeText().get(0);
			emrContactInfo.setName(FreeTextUtil.getContactNameFromEmrContactFreeText(freeText));
			emrContactInfo.setCountryCode(FreeTextUtil.getCountryCodeFromEmrContactFreeText(freeText));
			emrContactInfo.setPhoneNumber(FreeTextUtil.getPhoneNumberFromEmrContactFreeText(freeText));
			emrContactInfo.setQualifierId(qualifierId);
		}
		return emrContactInfo;
	}

	/**
	 * Parser Passenger CTC mobileNumber & email by ssrType
	 * @param dataElementsMaster
	 * @param booking
	 * @param ssrType - EMAIL_TYPE, MOBILE_TYPE
	 */
	private void parserCTCContactInfo(DataElementsMaster dataElementsMaster, RetrievePnrBooking booking, String ssrType) {
		List<RetrievePnrPassenger> passengers = booking.getPassengers();
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		for (int i = 0; i < passengers.size(); i++) {
			RetrievePnrPassenger passenger = passengers.get(i);
			String passengerId = passenger.getPassengerID();
			if(StringUtils.isEmpty(passengerId)) {
				continue;
			}
			
			for (int j = 0; j < dataElementsIndivs.size(); j ++) {
				DataElementsIndiv dataElementsIndiv = dataElementsIndivs.get(j);
				if(dataElementsIndiv == null 
						|| dataElementsIndiv.getElementManagementData() == null
						|| dataElementsIndiv.getServiceRequest() == null){
					continue;
				}
				List<ReferencingDetailsType111975C> references;
				if (dataElementsIndiv.getReferenceForDataElement() != null
						&& !CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
					references = dataElementsIndiv.getReferenceForDataElement().getReference();
				} else {
					references = new ArrayList<>();
				}
				List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
				/** only when the SSR contains passengerId of the passenger
				 * 		or when the SSR has no passengerId and there's only one passenger in this booking
				 * 	will the contact info be prepopulated to the passenger
				 */
				if(!referenceExist(references, PASSENGER_ID_QUALIFIER, passengerId) 
						&& !(passengers.size() == 1 && passengerIds.isEmpty())){
					continue;
				}
				String segmentName = dataElementsIndiv.getElementManagementData().getSegmentName();
				if(StringUtils.isEmpty(segmentName) || !segmentName.equals(SEGMENT_NAME_SSR) 
						|| dataElementsIndiv.getServiceRequest().getSsr() == null
						|| StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
						|| !OneAConstants.SSR_SK_CONFIRMED_STATUS.contains(dataElementsIndiv.getServiceRequest().getSsr().getStatus())
						||  StringUtils.isEmpty(dataElementsIndiv.getServiceRequest().getSsr().getType())
						/**
						* OLSSMMB-19407 Fix - Don't check CX/KA
						* || (!COMPANY_CX.equals(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId()) && !COMPANY_KA.equals(dataElementsIndiv.getServiceRequest().getSsr().getCompanyId()))
						**/
						|| !dataElementsIndiv.getServiceRequest().getSsr().getType().equals(ssrType)){
					//TO DO  Here If may need to add companyId(CX) filter
					continue;
				}
				
				String companyId = dataElementsIndiv.getServiceRequest().getSsr().getCompanyId();
				List<String> freeTexts = dataElementsIndiv.getServiceRequest().getSsr().getFreeText();
				String freeText = freeTexts.stream().filter(str -> !StringUtils.isEmpty(str)).findFirst().orElse(null);
				if(StringUtils.isEmpty(freeText)){
					continue;
				}
				
				BigInteger qualifierId = dataElementsIndiv.getElementManagementData().getReference().getNumber();
				boolean withSuffix =  BizRulesUtil.contactIsValidated(freeText);
				
				//get formated text
				String text = getFormatedText(freeText, withSuffix, ssrType); 
				if(StringUtils.isEmpty(text)){
					continue;
				}
				if(ssrType.equals(EMAIL_TYPE_CTCE)) {
					RetrievePnrEmail email = new RetrievePnrEmail();
					email.setEmail(text);
					email.setQualifierId(qualifierId);
					email.setType(EMAIL_TYPE_CTCE);
					email.setOlssContact(withSuffix);
					email.setCompanyId(companyId);
					passenger.getEmails().add(email);
				}else if(ssrType.equals(PHONE_TYPE_CTCM) && !StringUtils.isEmpty(text)){
					RetrievePnrContactPhone contactPhone = new RetrievePnrContactPhone();
					contactPhone.setPhoneNumber(BizRulesUtil.formatMobileNumber(text.trim()));
					contactPhone.setQualifierId(qualifierId);
					contactPhone.setType(PHONE_TYPE_CTCM);
					contactPhone.setOlssContact(withSuffix);
					contactPhone.setCompanyId(companyId);
					passenger.getContactPhones().add(contactPhone);				
				}
			}
		}
	}
	
	/**
	 * 
	 * @Description get formated text of email/phone from freeText
	 * @param freeText
	 * @param withSuffix
	 * @param type
	 * @return String
	 * @author haiwei.jia
	 */
	private String getFormatedText(String freeText, boolean withSuffix, String type) {
		if(EMAIL_TYPE_CTCE.equals(type) || EMAIL_TYPE_APE.equals(type)){
			if(!StringUtils.isEmpty(freeText)) {
				freeText = BizRulesUtil.formatEmail(BizRulesUtil.convertEmailToDisplay(freeText));		
			}
		}else if(PHONE_TYPE_CTCM.equals(type) || PHONE_TYPE_APM.equals(type) || PHONE_TYPE_APB.equals(type) || PHONE_TYPE_APH.equals(type)){
			// remove suffix
			freeText = withSuffix ? BizRulesUtil.convertContactFormat(freeText) : freeText;
			// remove special characters
			freeText = BizRulesUtil.removeSpecialCharactersFromStr(freeText);
		}
		return freeText;
	}
	
	/**
	 * Determine if the references contains qualifier & passengerId
	 * @param references
	 * @param qualifier PASSENGER_ID_QUALIFIER
	 * @param passengerId
	 */
	private boolean referenceExist(List<ReferencingDetailsType111975C> references, String qualifier, String passengerId){
		boolean bool = false;
		for(int i = 0; i < references.size(); i ++){
			if(!StringUtils.isEmpty(references.get(i).getQualifier()) && !StringUtils.isEmpty(references.get(i).getNumber()) 
					&& references.get(i).getQualifier().equals(qualifier) && references.get(i).getNumber().equals(passengerId)){
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	/**
	 * Get the number by qualifier
	 * @param elementManagementSegmentType
	 * @return
	 */
	private String getIdByQualifier(ElementManagementSegmentType elementManagementSegmentType,String qualifier){
		if(null != elementManagementSegmentType.getReference() && qualifier.equals(elementManagementSegmentType.getReference().getQualifier())){
			return elementManagementSegmentType.getReference().getNumber().toString();
		}
		return null;
	}

	/**
	 * set e-ticket
	 * 
	 * @param references
	 * @param eTickets
	 * @param booking
	 * @param passengerType
	 * @author haiwei.jia
	 */
	private void setETicketValue(List<ReferencingDetailsType111975C> references, List<RetrievePnrEticket> eTickets, RetrievePnrBooking booking, String passengerType) {
		if(CollectionUtils.isEmpty(eTickets)) {
			return;
		}
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		if(CollectionUtils.isEmpty(segmentIds) && !CollectionUtils.isEmpty(booking.getSegments()) && booking.getSegments().size() ==1) {
			segmentIds.add(booking.getSegments().get(0).getSegmentID());
			logger.info("Can not found reference bumbers from response, and there is only on segment, using this segment id to set E ticket value.");
		}
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		if (passengerIds.size() == 1 && !CollectionUtils.isEmpty(segmentIds)) {
			String passengerId = passengerIds.get(0);
			//if the passenger is an infant, get its passengerId
			if(PASSENGER_TYPE_INF.equals(passengerType)){
				String currentPaxId = passengerId;
				//get the passengerId of the infant
				passengerId = booking.getPassengers().stream().filter(pax -> currentPaxId.equals(pax.getParentId()))
						.map(RetrievePnrPassenger :: getPassengerID).findFirst().orElse(null);
			}
			if(StringUtils.isEmpty(passengerId)){
				return;
			}
			for(String segmentId : segmentIds){
				RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
				if (passengerSegment == null) {
					passengerSegment = new RetrievePnrPassengerSegment();
					passengerSegment.setPassengerId(passengerId);
					passengerSegment.setSegmentId(segmentId);
					passengerSegment.getEtickets().addAll(eTickets);
					booking.getPassengerSegments().add(passengerSegment);
				} else {
					passengerSegment.getEtickets().addAll(eTickets);
				}
			}
		}
	}

	/**
	 * set seat
	 * 
	 * @param references
	 * @param seats
	 * @param booking
	 * @author haiwei.jia
	 */
	private void processSeat(RetrievePnrBooking booking, List<ReferencingDetailsType111975C> references,
			List<RetrievePnrSeatDetail> pnrSeats, RetrievePnrSeatPreference preference, BigInteger qulifierId) {
		if(booking == null || CollectionUtils.isEmpty(references) || qulifierId == null){
			return;
		}
		
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbers(references, PASSENGER_ID_QUALIFIER);
		
		if(!CollectionUtils.isEmpty(pnrSeats)) {
			if(!CollectionUtils.isEmpty(segmentIds)){
				for(String segmentId : segmentIds){
					for(RetrievePnrSeatDetail pnrSeat : pnrSeats){
						RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), pnrSeat.getCrossRef(), segmentId);
						if (passengerSegment == null) {
							passengerSegment = new RetrievePnrPassengerSegment();
							passengerSegment.setPassengerId(pnrSeat.getCrossRef());
							passengerSegment.setSegmentId(segmentId);
							passengerSegment.findSeat().setSeatDetail(pnrSeat);
							passengerSegment.findSeat().setQulifierId(qulifierId);
							booking.getPassengerSegments().add(passengerSegment);
						} else {
							populateSeatToPs(qulifierId, pnrSeat, passengerSegment, booking);
						}
					}
				}
			}
		}else if(preference != null) {
			RetrievePnrSeat seat = new RetrievePnrSeat();
			seat.setQulifierId(qulifierId);
			seat.setPreference(preference);
			String passengerId = getPassengerIdOfSeatInfo(booking, passengerIds);
			if(StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(segmentIds) || StringUtils.isEmpty(segmentIds.get(0))){
				return;
			}
			setSeat(seat, passengerId, segmentIds.get(0), booking);
		}
	}

	/**
	 * 
	* @Description populate seat to passengerSegment
	* @param qulifierId
	* @param pnrSeat
	* @param passengerSegment
	* @return void
	* @author haiwei.jia
	 */
	private void populateSeatToPs(BigInteger qulifierId, RetrievePnrSeatDetail pnrSeat,
			RetrievePnrPassengerSegment passengerSegment, RetrievePnrBooking booking) {
		if(passengerSegment.findSeat().getQulifierId() == null || qulifierId.equals(passengerSegment.findSeat().getQulifierId())){
			if(passengerSegment.findSeat().getSeatDetail() == null){
				passengerSegment.findSeat().setSeatDetail(pnrSeat);
				passengerSegment.findSeat().setQulifierId(qulifierId);
			}
			else{
				passengerSegment.findSeat().findExtraSeats().add(pnrSeat);
				if(StringUtils.isEmpty(passengerSegment.findSeat().getQulifierId())){
					passengerSegment.findSeat().setQulifierId(qulifierId);
				}
			}
		} else if(passengerSegment.findSeat().getSeatDetail() == null || qulifierId.compareTo(passengerSegment.findSeat().getQulifierId()) > 0){
			booking.findInvalidOts().add(passengerSegment.findSeat().getQulifierId());
			RetrievePnrSeat seat = new RetrievePnrSeat();	
			seat.setSeatDetail(pnrSeat);
			seat.setQulifierId(qulifierId);
			passengerSegment.setSeat(seat);
		} else{
			booking.findInvalidOts().add(qulifierId);
		}
	}

	/**
	 *  
	* @Description get passenger id of the passenger the seat info related to
	* @param booking
	* @param passengerIds
	* @return String
	* @author haiwei.jia
	 */
	private String getPassengerIdOfSeatInfo(RetrievePnrBooking booking, List<String> passengerIdsInPnr) {
		if(booking == null || CollectionUtils.isEmpty(passengerIdsInPnr)){
			return null;
		}
		/** the seat passenger id does not exist in the booking's
		passengers,so if the passengerIdInPnr equals to the passenger id
		in booking, then it is the passenger id which is needed */
		for(String passengerIdInPnr : passengerIdsInPnr){
			for(RetrievePnrPassenger passengerInBooking : booking.getPassengers()){
				if(passengerInBooking.getPassengerID().equals(passengerIdInPnr)){
					return passengerIdInPnr;
				}
			}
		}
		return null;
	}

	/**
	 * set seat
	 * 
	 * @param seat
	 * @param passengerId
	 * @param segmentId
	 * @param passengerSegments
	 * @author haiwei.jia
	 */
	private void setSeat(RetrievePnrSeat seat, String passengerId, String segmentId,
			RetrievePnrBooking booking) {
		RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
		if (passengerSegment == null) {
			passengerSegment = new RetrievePnrPassengerSegment();
			passengerSegment.setPassengerId(passengerId);
			passengerSegment.setSegmentId(segmentId);
			passengerSegment.setSeat(seat);
			booking.getPassengerSegments().add(passengerSegment);
		} else {
			if(passengerSegment.findSeat().getQulifierId() != null){
				if(passengerSegment.findSeat().getSeatDetail() == null && passengerSegment.findSeat().getQulifierId().compareTo(seat.getQulifierId()) < 0){
					booking.findInvalidOts().add(passengerSegment.findSeat().getQulifierId());
					passengerSegment.setSeat(seat);
				} else{		
					booking.findInvalidOts().add(seat.getQulifierId());
				}
			} else{
				passengerSegment.setSeat(seat);
			}
		}
	}
	
	/**
	 * set mealType
	 * 
	 * @param references
	 * @param ssrType
	 * @author haiwei.jia
	 */
	private void setMealValue(List<ReferencingDetailsType111975C> references, RetrievePnrMeal meal, RetrievePnrBooking booking) {	
		List<String> segmentIds = getReferenceNumbers(references, SEGMENT_ID_QUALIFIER);
		List<String> passengerIds = getReferenceNumbersForPT(references,booking.getPassengers());
		if (!segmentIds.isEmpty() && !passengerIds.isEmpty()) {
			
			// One SSR can refer to multi-passengers / multi-segments
			for(String segmentId: segmentIds) {
				for(String passengerId: passengerIds) {
					RetrievePnrPassengerSegment passengerSegment = getPassengerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
					if (passengerSegment != null) {
						passengerSegment.setMeal(meal);
					}
				}
			}
		}
	}

	/**
	 * set PnrOperateCompany and pnrOperateSegmentNumber
	 * @param segment
	 * @param freeTextTypeList
	 */
	private void setPnrOperateCompanyAndSegmentNumber(RetrievePnrSegment segment, List<InteractiveFreeTextType> freeTextTypeList){
		String regex = "(\\bOPERATED BY\\b)([\\D]+)([0]*)(([\\d]+))([\\D]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher;
		for(InteractiveFreeTextType item : freeTextTypeList){
			if(!CollectionUtils.isEmpty(item.getFreeText())){
				for(String freeText : item.getFreeText()){
					matcher = pattern.matcher(freeText);
					if(matcher.find()){
						segment.setPnrOperateCompany(matcher.group(2).trim());
						segment.setPnrOperateSegmentNumber(matcher.group(4).trim());
						//if operating company exist,Replace operating company subclass with marketing company
						segment.setSubClass(matcher.group(6).trim());
						break;
					}
				}
			}
		}
	}
	
	private void parserRemark(PNRReply pnrReply, RetrievePnrBooking booking) {
		List<RetrievePnrRemark> remarkList = Lists.newArrayList();
		
		// Null checking for parameters
		if(pnrReply == null || booking == null) {
			return;
		}
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return;
		}
		
		// Loop through all data elements to check if TK segment exist
		for(DataElementsIndiv dataElementsIndiv: dataElementsIndivs) {
			ElementManagementSegmentType elementManagementData = dataElementsIndiv.getElementManagementData();
			
			// Null checking for elementManagementData
			if(elementManagementData == null) {
				continue;
			}
			
			// Only need RM segment
			String segmentName = elementManagementData.getSegmentName();
			if(segmentName == null || !SEGMENT_NAME_RM.equals(segmentName)) {
				continue;
			}

			// Null checking for remark element
			MiscellaneousRemarksType211S miscellaneousRemarks = dataElementsIndiv.getMiscellaneousRemarks();
			if(miscellaneousRemarks == null) {
				continue;
			}
			MiscellaneousRemarkType151C remark = miscellaneousRemarks.getRemarks();
			if(remark == null) {
				continue;
			}
			
			List<String> passengerIds = new ArrayList<>();
			List<String> segmentIds = new ArrayList<>();
			if(dataElementsIndiv.getReferenceForDataElement() != null && !CollectionUtils.isEmpty(dataElementsIndiv.getReferenceForDataElement().getReference())) {
				passengerIds = getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), PASSENGER_ID_QUALIFIER);
				segmentIds = getReferenceNumbers(dataElementsIndiv.getReferenceForDataElement().getReference(), SEGMENT_ID_QUALIFIER);
			}		
			
			// Create remark to PNRBooking
			RetrievePnrRemark retrievePnrRemark = new RetrievePnrRemark();
			remarkList.add(retrievePnrRemark);
			
			// Add OT about RM
			if(OneAConstants.OT_QUALIFIER.equals(elementManagementData.getReference().getQualifier())) {				
				retrievePnrRemark.setOtQualifierNumber(elementManagementData.getReference().getNumber().toString());
			}
			
			retrievePnrRemark.setType(remark.getType());
			retrievePnrRemark.setCategory(remark.getCategory());
			retrievePnrRemark.setFreeText(remark.getFreetext());
			if(!StringUtils.isEmpty(passengerIds)) {
				retrievePnrRemark.setPassengerIds(passengerIds);
			}
			if(!StringUtils.isEmpty(segmentIds)) {
				retrievePnrRemark.setSegmentIds(segmentIds);
			}
		}
		
		booking.setRemarkList(remarkList);
	}
	
	private void parserTicketList(PNRReply pnrReply, RetrievePnrBooking booking) {
		List<RetrievePnrTicket> ticketList = Lists.newArrayList();
		
		// Null checking for parameters
		if(pnrReply == null || booking == null) {
			return;
		}
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return;
		}
		
		// Loop through all data elements to check if TK segment exist
		for(DataElementsIndiv dataElementsIndiv: dataElementsIndivs) {
			ElementManagementSegmentType elementManagementData = dataElementsIndiv.getElementManagementData();
			
			// Null checking for elementManagementData
			if(elementManagementData == null) {
				continue;
			}
			
			// Only need TK segment
			String segmentName = elementManagementData.getSegmentName();
			if(segmentName == null || !SEGMENT_NAME_TK.equals(segmentName)) {
				continue;
			}

			// Null checking for ticketElement
			TicketElementType ticketElement = dataElementsIndiv.getTicketElement();
			if(ticketElement == null) {
				continue;
			}
			TicketInformationType ticket = ticketElement.getTicket();
			if(ticket == null) {
				continue;
			}
			
			// Create Ticker to PNRBooking
			RetrievePnrTicket retrievePnrTicket = new RetrievePnrTicket();
			ticketList.add(retrievePnrTicket);
			
			retrievePnrTicket.setIndicator(ticket.getIndicator());
			retrievePnrTicket.setDate(ticket.getDate());
			retrievePnrTicket.setTime(ticket.getTime());
			retrievePnrTicket.setOfficeId(ticket.getOfficeId());
		}
		
		booking.setTicketList(ticketList);
	}

	public static List<OneAError> getAllErrors(PNRReply pnrReply) {
		if(pnrReply == null){
			return Collections.emptyList();
		}
		List<OneAError> errorDetails =new ArrayList<>();
		//PNR_Reply/pnr:generalErrorInfo
		OneAErrorParserUtil.parserOneAErrorFromGeneralErrorInfo(errorDetails,pnrReply.getGeneralErrorInfo());
		
		//PNR_Reply/pnr:travellerInfo/pnr:nameError/pnr:nameErrorInformation/pnr:errorDetail
		OneAErrorParserUtil.parserOneAErrorFromTravellerInfo(errorDetails,pnrReply.getTravellerInfo());
		
		//PNR_Reply/pnr:originDestinationDetails/pnr:itineraryInfo/pnr:typicalCarData/pnr:errorWarning/pnr:applicationError/pnr:errorDetails and //PNR_Reply/pnr:originDestinationDetails/pnr:itineraryInfo/pnr:errorInfo/pnr:errorInformation/pnr:errorDetail
		OneAErrorParserUtil.parserOneAErrorFromOriginDestinationDetails(errorDetails,pnrReply.getOriginDestinationDetails());
			
		//pnr:PNR_Reply/pnr:dataElementsMaster/pnr:dataElementsIndiv/pnr:elementErrorInformation/pnr:errorInformation/pnr:errorDetail and //PNR_Reply/pnr:dataElementsMaster/pnr:dataElementsIndiv/pnr:structuredFop/pnr:paymentModule/pnr:mopDetailedData/pnr:creditCardDetailedData/pnr:transactionStatus/pnr:errorOrWarningCodeDetails/pnr:errorDetails			
		if(pnrReply.getDataElementsMaster()!=null){
			OneAErrorParserUtil.parserOneAErrorFromDataElementsIndiv(errorDetails,pnrReply.getDataElementsMaster().getDataElementsIndiv());
		}
		return errorDetails;
	}
	
	private void parserFaList(PNRReply pnrReply, RetrievePnrBooking booking) {
		List<RetrievePnrFa> faList = Lists.newArrayList();
		
		// Null checking for parameters
		if(pnrReply == null || booking == null) {
			return;
		}
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return;
		}
		
		// Loop through all data elements to check if FA segment exist
		for(DataElementsIndiv dataElementsIndiv: dataElementsIndivs) {
			ElementManagementSegmentType elementManagementData = dataElementsIndiv.getElementManagementData();
			
			// Null checking for elementManagementData
			if(elementManagementData == null) {
				continue;
			}
			
			// Only need FA segment
			String segmentName = elementManagementData.getSegmentName();
			if(segmentName == null || !SEGMENT_NAME_FA.equals(segmentName)) {
				continue;
			}
			
			RetrievePnrFa retrievePnrFa = new RetrievePnrFa();

			// Set longFreeText
			List<LongFreeTextType> longFreeTextTypes = dataElementsIndiv.getOtherDataFreetext();
			if(longFreeTextTypes == null || longFreeTextTypes.isEmpty()) {
				continue;
			}
			
			if(!CollectionUtils.isEmpty(longFreeTextTypes) && longFreeTextTypes.get(0) != null) {
				retrievePnrFa.setLongFreeText(longFreeTextTypes.get(0).getLongFreetext());
			}
			
			// Set for passengerId
			ReferenceInfoType referenceInfoType = dataElementsIndiv.getReferenceForDataElement();
			if(referenceInfoType == null) {
				continue;
			}
			List<ReferencingDetailsType111975C> references = referenceInfoType.getReference();
			if(references == null) {
				continue;
			}
			for(int i = 0; i < references.size(); i++) {
				ReferencingDetailsType111975C reference = references.get(i);
				if(OneAConstants.PT_QUALIFIER.equals(reference.getQualifier())) {
					retrievePnrFa.setPassengerId(reference.getNumber());
					break;
				}
			}
		
			// Create Fa to PNRBooking
			if (retrievePnrFa.getPassengerId() != null && retrievePnrFa.getLongFreeText() != null) {
				faList.add(retrievePnrFa);
			}
		}
		
		booking.setFaList(faList);
	}

	private void parserFeList(PNRReply pnrReply, RetrievePnrBooking booking){
		List<RetrievePnrFe> feList = Lists.newArrayList();

		// Null checking for parameters
		if(pnrReply == null || booking == null) {
			return;
		}
		DataElementsMaster dataElementsMaster = pnrReply.getDataElementsMaster();
		if(dataElementsMaster == null) {
			return;
		}
		List<DataElementsIndiv> dataElementsIndivs = dataElementsMaster.getDataElementsIndiv();
		if(CollectionUtils.isEmpty(dataElementsIndivs)) {
			return;
		}

		// Loop through all data elements to check if FA segment exist
		for(DataElementsIndiv dataElementsIndiv: dataElementsIndivs) {
			ElementManagementSegmentType elementManagementData = dataElementsIndiv.getElementManagementData();

			// Null checking for elementManagementData
			if(elementManagementData == null) {
				continue;
			}

			// Only need FA segment
			String segmentName = elementManagementData.getSegmentName();
			if(segmentName == null || !SEGMENT_NAME_FE.equals(segmentName)) {
				continue;
			}

			RetrievePnrFe retrievePnrFe = new RetrievePnrFe();

			// Set longFreeText
			List<LongFreeTextType> longFreeTextTypes = dataElementsIndiv.getOtherDataFreetext();
			if(longFreeTextTypes == null || longFreeTextTypes.isEmpty()) {
				continue;
			}

			if(!CollectionUtils.isEmpty(longFreeTextTypes) && longFreeTextTypes.get(0) != null) {
				retrievePnrFe.setLongFreeText(longFreeTextTypes.get(0).getLongFreetext());
				feList.add(retrievePnrFe);
			}

//			// Set for passengerId
//			ReferenceInfoType referenceInfoType = dataElementsIndiv.getReferenceForDataElement();
//			if(referenceInfoType == null) {
//				continue;
//			}
//			List<ReferencingDetailsType111975C> references = referenceInfoType.getReference();
//			if(references == null) {
//				continue;
//			}
//			for(int i = 0; i < references.size(); i++) {
//				ReferencingDetailsType111975C reference = references.get(i);
//				if(OneAConstants.PT_QUALIFIER.equals(reference.getQualifier())) {
//					retrievePnrFe.setPassengerId(reference.getNumber());
//					break;
//				}
//			}
//
//			// Create Fa to PNRBooking
//			if (retrievePnrFe.getPassengerId() != null && retrievePnrFe.getLongFreeText() != null) {
//				feList.add(retrievePnrFe);
//			}
		}

		booking.setFeList(feList);
	}

	/**
	 * 
	* @Description parser pos
	* @param pnrReply
	* @param booking
	* @return void
	* @author jiajian.guo
	 */
	private void parserPos(PNRReply pnrReply, RetrievePnrBooking booking) {
		POSGroupType pOSGroupType = pnrReply.getSbrPOSDetails();
		if(pOSGroupType == null) {
			return;
		}
		UserPreferencesType sbrPreferences=pOSGroupType.getSbrPreferences();
		if(sbrPreferences == null) {
			return;
		}
		OriginatorDetailsTypeI userPreferences=sbrPreferences.getUserPreferences();
		if(userPreferences != null) {	
			booking.setPos(userPreferences.getCodedCountry());
		}
		
	}
	
	private void parserCreationPos(PNRReply pnrReply, RetrievePnrBooking booking) {
		POSGroupType pOSGroupType = pnrReply.getSbrCreationPosDetails();
		if(pOSGroupType == null) {
			return;
		}
		UserPreferencesType sbrPreferences=pOSGroupType.getSbrPreferences();
		if(sbrPreferences == null) {
			return;
		}
		OriginatorDetailsTypeI userPreferences=sbrPreferences.getUserPreferences();
		if(userPreferences != null) {	
			booking.setCreationPos(userPreferences.getCodedCountry());
		}
		
	}

	private void parserDWCode(RetrievePnrBooking booking){
		List<RetrievePnrFe> retrievePnrFes = booking.getFeList();
		List<String> bookingDwCode =new ArrayList<>();
		if(CollectionUtils.isNotEmpty(retrievePnrFes)){
			retrievePnrFes.stream().forEach(retrievePnrFe ->{
				if(!FreeTextUtil.getDwCodeFromFreeText(retrievePnrFe.getLongFreeText()).isEmpty()){
					bookingDwCode.add(FreeTextUtil.getDwCodeFromFreeText(retrievePnrFe.getLongFreeText()));
			    }
		});
		}
		booking.setBookingDwCode(bookingDwCode);
	}
	
	/**
	 * get all <segmentName/>:TK, <qualifier/>: OT numbers
	 * 
	 * @param pnrReply
	 * @return
	 */
	public List<String> getAllTkOTnumbers(PNRReply pnrReply) {
		if(pnrReply == null 
				|| pnrReply.getDataElementsMaster() == null
				|| CollectionUtils.isEmpty(pnrReply.getDataElementsMaster().getDataElementsIndiv())){
			return Collections.emptyList();
		}
		
		List<DataElementsIndiv> dataElementsIndivs = pnrReply.getDataElementsMaster().getDataElementsIndiv();
		return dataElementsIndivs.stream().map(DataElementsIndiv::getElementManagementData)
				.filter(e -> e.getReference() != null && SEGMENT_NAME_TK.equals(e.getSegmentName()) && OneAConstants.OT_QUALIFIER.equals(e.getReference().getQualifier()))
				.map(ElementManagementSegmentType::getReference)
				.map(ReferencingDetailsType127526C::getNumber)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.distinct().collect(Collectors.toList());
	}
	
	/**
	 * retrieve staff id from pnr
	 * @param retrievePnrBooking
	 * @return
	 */
    public String parseStaffId(RetrievePnrBooking retrievePnrBooking) {
        return retrievePnrBooking.getPassengers().stream()
                .filter(passenger -> null != passenger && null != passenger.getStaffDetail())
                .map(passenger -> passenger.getStaffDetail()).filter(staff -> !StringUtils.isEmpty(staff.getStaffId()))
                .map(staff -> staff.getStaffId()).findFirst().orElse(null);
    }
	
}