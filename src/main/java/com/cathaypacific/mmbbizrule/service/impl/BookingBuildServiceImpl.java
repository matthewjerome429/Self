package com.cathaypacific.mmbbizrule.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mbcommon.enums.booking.ContactInfoTypeEnum;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.seat.XLFOCReasonEnum;
import com.cathaypacific.mbcommon.enums.ssrskstatus.SsrStatusEnum;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeBidStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.model.common.TempSeat;
import com.cathaypacific.mbcommon.model.common.seatpayment.PaymentInfo;
import com.cathaypacific.mbcommon.model.common.seatpayment.SeatPayment;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.token.TokenLockKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.constant.AEPConstants;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.constant.MealOption;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.aep.service.AEPService;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.util.BaggageAllowanceErrorUtil;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.business.MealPreSelectEligibilityBusiness;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PreSelectedMealPassengerSegment;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.MemberAwardRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.SectorDetailRecordInRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.UserInformation;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.MemberAwardResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.SectorDetailRecordInResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.service.MemberAwardService;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.novatti.service.NovattiService;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeService;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.SectorDTO;
import com.cathaypacific.mmbbizrule.db.dao.ASREnableCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.AdditionalOperatorInfoDAO;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.MealIneligibilityDAO;
import com.cathaypacific.mmbbizrule.db.dao.RedemptionSubclassCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.RedemptionTPOSCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.SeatRuleDao;
import com.cathaypacific.mmbbizrule.db.dao.SpecialServiceDAO;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TBSsrSkMappingDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbFlightHaulDAO;
import com.cathaypacific.mmbbizrule.db.model.AdditionalOperatorInfoKey;
import com.cathaypacific.mmbbizrule.db.model.AdditionalOperatorInfoModel;
import com.cathaypacific.mmbbizrule.db.model.AsrEnableCheck;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.db.model.MealOptionKey;
import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassCheck;
import com.cathaypacific.mmbbizrule.db.model.RedemptionTPOSCheck;
import com.cathaypacific.mmbbizrule.db.model.SeatRuleModel;
import com.cathaypacific.mmbbizrule.db.model.SpecialServiceModel;
import com.cathaypacific.mmbbizrule.db.model.StatusManagementModel;
import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMapping;
import com.cathaypacific.mmbbizrule.db.model.TbFlightHaul;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;
import com.cathaypacific.mmbbizrule.dto.common.booking.PhoneInfoDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.BookingBuildValidationHelpr;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.AirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingPackageInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.AtciCancelledSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.ClaimedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Email;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedBaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.RebookMapping;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatPreference;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelectionIneligibleReason;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.SharedWaiverBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialMeal;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialSeatEligibility;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.model.booking.detail.UpgradeInfo;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.model.profile.Contact;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAddressDetails;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBidUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingPackageInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAtciCancelledSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDob;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmrContactInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFa;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrLoungeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookMapping;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeat;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatPreference;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrUpgradeProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessBaggageAllowance;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFareDetail;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessPricingInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessSysProvider;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService.BaggageAllowanceInfo;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.SeatRuleService;
import com.cathaypacific.mmbbizrule.service.UMNREFormBuildService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.TicketReminderUtil;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper.Encoding;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocList;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocOD;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

@Service
public class BookingBuildServiceImpl implements BookingBuildService {

	private static LogAgent logger = LogAgent.getLogAgent(BookingBuildServiceImpl.class);
	
	@Autowired
	private AirportTimeZoneService airportTimeZoneService;

	@Autowired
	private BookingStatusDAO bookingStatusDAO;

	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Autowired
	private CabinClassDAO cabinClassDAO;
	
	@Autowired
	private SeatRuleDao seatRuleDao;
	
	@Autowired
	private TBSsrSkMappingDAO tBSsrSkMappingDAO;
	
	@Autowired
	private TbTravelDocOdCacheHelper tbTravelDocOdCacheHelper;
	
	@Autowired
	private TbTravelDocListCacheHelper travelDocListCacheHelper;

	@Autowired
	private MemberAwardService memberAwardService;

	@Autowired
	private AirFlightInfoService airFlightInfoService;
	
	@Autowired
	private AEMService aemService;
	
	@Autowired
	private NationalityCodeService nationalityCodeService;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private MealIneligibilityDAO mealIneligibilityDao;
	
	@Autowired
	private RedemptionTPOSCheckDAO redemptionTPOSCheckDAO;
	
	@Autowired
	private RedemptionSubclassCheckDAO redemptionSubclassCheckDAO;
	
	@Autowired
	private ASREnableCheckDAO asrEnableCheckDAO;

	@Autowired
	private AdditionalOperatorInfoDAO additionalOperatorInfoDAO;

	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	
	@Value("${flight.flown.limithours}")
	private Integer limithours;
	
	@Value("${givenName.maxCharacterToMatch}")
	private Integer shortCompareSize;
	
	@Value("${mmb.flight.passed.time}")
	private long flightPassedTime;
	
	@Autowired
	private OLCIServiceV2 olciServiceV2;
	
	@Autowired
	private PnrCprMergeHelper pnrCprMergeHelper;
	
	@Autowired
	private OLCIConfig olciConfig;
	
	@Autowired
	private BookingStatusConfig bookingStatusCOnfig;
	
	@Autowired
	private SeatRuleService seatRuleService;

	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Autowired
	private AEPService aepService;
	
	@Autowired
	private BaggageAllowanceBuildService baggageAllowanceBuildService;

	@Autowired
	private StatusManagementDAO statusManagementDAO;

	@Autowired
	private SpecialServiceDAO specialServiceDAO;
	
	@Autowired
	private BookingBuildValidationHelpr bookingBuildValidationHelpr;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Autowired
	private TbFlightHaulDAO tbFlightHaulDAO;
	
	@Autowired
	private NovattiService novattiService;
	
	@Autowired
	private UMNREFormBuildService umnreFormBuildService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	@Autowired
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;

    @Autowired
    private MealPreSelectEligibilityBusiness mealPreSelectEligibilityBusiness;
    
    @Autowired
    private BookEligibilityConfig bookEligibilityConfig;

	@Override
	@LogPerformance(message="Time required to build booking.")
	public Booking buildBooking(RetrievePnrBooking pnrBooking, LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException {
		//generate cache key by para 
		//String cacheKey = EncodeUtil.encoderByMd5UTF8(pnrBooking,loginInfo);
		//Booking booking = getFromCache(cacheKey, pnrBooking.getOneARloc(), required);
	//	if(booking != null){
		//	return booking;
		//}
		
		//check booking before parser
		checkBooingBeforePaser(pnrBooking,loginInfo,required);

		List<TravelDocList> travelDocList = travelDocListCacheHelper.findAll();
		Map<String, String> priMap = getVersionMap(travelDocListCacheHelper.findVersionByTypeGroupByCode(TBConstants.TRAVEL_DOC_PRIMARY));
		Map<String, String> secMap = getVersionMap(travelDocListCacheHelper.findVersionByTypeGroupByCode(TBConstants.TRAVEL_DOC_SECONDARY));
		
		TicketProcessInfo ticketProcessInfo = getTicketProcessInfoForEticket(pnrBooking);

		@SuppressWarnings("unchecked")
		List<TempSeat> tempSeats = mbTokenCacheLockRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, pnrBooking.getOneARloc(), ArrayList.class);

		Booking booking = new Booking();
		booking.setGotPnr(true);
		booking.setLoginRloc(loginInfo.getLoginRloc());
		// build booking
		buildBookingInfo(loginInfo, booking, pnrBooking);
		// build segments
		booking.setSegments(buildFlightInfo(pnrBooking, required));

		// Redemption Booking 
		if(required.operateInfoAndStops()) {
			booking.setRedemptionBooking(redemptionBookingChecking(pnrBooking.getSegments()));
			checkCxIncompleteRedemptionBooking(booking,pnrBooking);
		}
		// build passenger
		List<Passenger> passengers = buildPassengerInfo(booking, pnrBooking, travelDocList, priMap, secMap, loginInfo, required);
		// open mice checking, mice will only return login pax
        if (required.isMiceBookingCheck() && booking.isMiceBooking()) {
            passengers = miceChecking(pnrBooking, passengers);
        }
		booking.setPassengers(passengers);
		
		// TODO Require baggage allowance is kept for retrieve booking V1 API
		// Prepare AEP products for baggage allowance.
		Future<AEPProductsResponse> futureAEPProductsResponse = null;
		if (required.baggageAllowances() && !MMBBizruleConstants.ACCESS_CHANNEL_WEB.equals(MMBUtil.getCurrentAccessChannel())) {
			futureAEPProductsResponse = aepService.asyncGetBookingProducts(booking.getOneARloc(), booking.getPos());
		}
		
		// Call MPO profile service for checking whether on Member Holiday
		Future<Map<String, ProfilePersonInfo>> futureCheckMemberHoliday = null;
		if(required.requireFqtvHolidayCheck()) {
			futureCheckMemberHoliday = asyncCheckMemberHoliday(booking, pnrBooking, loginInfo);
		}
		
		Future<LoginResponseDTO> futurePassengerCheckInInfo = null;
		if(required.isCprCheck()) {
			logger.info(String.format("buildBooking -> get CPR[%s] from OLCI by useSession = %s",
					booking != null ? booking.getOneARloc() : null, required.isUseCprSession()));
			//get passenger check in info
			futurePassengerCheckInInfo = asyncGetPassengerCheckInInfos(loginInfo, booking, required.isUseCprSession(), required.isFilerMergePNR());			
		}

		// Check upgrade progress status
		Future<Map<String, String>> futureUpgradeProgressStatus = null;
		if((required.isBookableUpgradeStatusCheck())) {
			futureUpgradeProgressStatus = asyncGetUpgradeProgressStatus(pnrBooking);
		}
		
		//build passenger segment info
		buildPassengerSegmentInfo(booking, pnrBooking, travelDocList, priMap, secMap, required, ticketProcessInfo,
				futureAEPProductsResponse, futureCheckMemberHoliday, futureUpgradeProgressStatus, tempSeats);

		// build pre selected meal
		Future<List<PreSelectedMealPassengerSegment>> futurePreSelectedMealPassengerSegments = null;
		if(BooleanUtils.isTrue(bookEligibilityConfig.isEnablePreselectedMeal()) && BooleanUtils.isTrue(required.isPreSelectedMeal())) {
		    futurePreSelectedMealPassengerSegments = mealPreSelectEligibilityBusiness.asyncGetPreSelectedMealInfo(booking);
		}

		booking.setBookingDwCode(pnrBooking.getBookingDwCode());

		//build TicketIssueInfo
		booking.setTicketIssueInfo(getEarliestTicketIssueInfo(pnrBooking));	
		//build skList
		booking.setSkList(pnrBooking.getSkList());
		// build ssrList
		booking.setSsrList(pnrBooking.getSsrList());
		//build ticketList
		booking.setTicketList(pnrBooking.getTicketList());
		// build invalid OTs
		booking.setInvalidOts(pnrBooking.getInvalidOts());
		
		// build FRBK SK
		booking.setFrbkSK(pnrBooking.isFrbkSK());

		booking.setBookingWaiveReminders(buildWaiveReminderSSRSK(pnrBooking));
		
		//build redemption
		if(required.operateInfoAndStops()) {
			buildRedemptionConfirmedBooking(booking, pnrBooking, loginInfo);
		}
		
		//build corporateBooking
		booking.setCorporateBooking(pnrBooking.isCorporateBooking());
		
		TicketReminderUtil.buildReminderInfos(booking);
		
		if(futurePassengerCheckInInfo != null) {
			try{
				LoginResponseDTO cprResponse = futurePassengerCheckInInfo.get();
				String journeyId = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(),
						TokenCacheKeyEnum.CPR_JOURNEY_ID, booking.getOneARloc(), String.class);
				required.setJourneyId(journeyId);
				pnrCprMergeHelper.mergeCprToBookingModel(booking, cprResponse, required);
			} catch (Exception e){
				logger.error("Fail to build passenger Check In Infos from CPR", e);
			}
			
		}

        // build seat selection
		// seat selection need checkin status, so we move seatSelection after call cpr
        if (required.seatSelection()) {
            buildSeatSelection(booking, pnrBooking, tempSeats);
        }

		// build seat by temp seat stored in cache
		buildSeatByTempSeat(booking, tempSeats);
		
		/** if user try to update a purchased seat to another seat of same type, 1A may not be able to add payment for the new seat on time,
		 *   so we save the payment in cache while updating and transfer the payment to seat manually */
		// transfer seat payment by seatPayments stored in cache
		transferSeatPayment(booking);
		
		booking.setMandatoryContactInfo(judgeMandatoryContactInfo(booking,loginInfo));	
		//storeBookingToCache(booking,cacheKey,required);

        // parse preselected meal info
        if (futurePreSelectedMealPassengerSegments != null) {
            try {
                buildPreSelectedMealToBooking(booking, futurePreSelectedMealPassengerSegments, required);
            } catch (Exception e) {
                logger.error("Fail to pare preselected meal to booking", e);
            }
        }
        // 1.remove meal for unidentified preselect meal
        // 2.if no preselect meal list then set PreSelectMealEligibility false
        removeMealForUnidentifiedPreselectMeal(booking);
        // add debug log
        logger.debug("Booking info after pnr cpr merged: " + new Gson().toJson(booking));
		return booking;

	}

	/**
     * remove meal for unidentified preselect meal and set PreSelectMealEligibility
     * false
     * 
     * @param booking
     */
    private void removeMealForUnidentifiedPreselectMeal(Booking booking) {
        if (null != booking && !CollectionUtils.isEmpty(booking.getPassengerSegments())) {
            booking.getPassengerSegments().stream().forEach(ps -> {
//                https://cathaypacific-prod.atlassian.net/browse/OLSS-7921
//                MealDetail mealDetail = ps.getMeal();
//                if (null != mealDetail && null != mealDetail.getMealCode() && mealDetail.isPreSelectedMeal()) {
//                    if (null == mealDetail.getDishName()) {
//                        ps.setMeal(null);
//                        logger.debug(String.format("existing pre-selected meal code in PNR cannot map to any response meal info, treat as unidentified meal.passengerId: %s, segmentId: %s", ps.getPassengerId(), ps.getSegmentId()));
//                    }
//                }
                MealSelection mealSelection = ps.getMealSelection();
                if (null != mealSelection && CollectionUtils.isEmpty(mealSelection.getPreSelectMeals())) {
                    mealSelection.setPreSelectMealEligibility(false);
                    logger.debug(String.format("Cause no response of full menu, set PreSelectMealEligibility false.passengerId: %s, segmentId: %s", ps.getPassengerId(), ps.getSegmentId()));
                }
            });
        }}

    /**
     * @param booking
     * @param futurePreSelectedMealPassengerSegments
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void buildPreSelectedMealToBooking(Booking booking,
            Future<List<PreSelectedMealPassengerSegment>> futurePreSelectedMealPassengerSegments,
            BookingBuildRequired required) throws InterruptedException, ExecutionException {
        List<PreSelectedMealPassengerSegment> preSelectedMealPassengerSegments = futurePreSelectedMealPassengerSegments
                .get();
        if (!CollectionUtils.isEmpty(preSelectedMealPassengerSegments)) {
            logger.debug(String.format("preSelectedMealInfo: %s", new Gson().toJson(preSelectedMealPassengerSegments)));
            preSelectedMealPassengerSegments.stream().forEach(psmps -> {
                PassengerSegment passengerSegment = booking.getPassengerSegments().stream()
                        .filter(ps -> StringUtils.equals(psmps.getPassengerId(), ps.getPassengerId())
                                && StringUtils.equals(psmps.getSegmentId(), ps.getSegmentId()))
                        .findFirst().orElse(null);
                if (null != passengerSegment) {
                    // handle meal selection
                    if (null != psmps.getMealSelection() && required.mealSelection()) {
                        passengerSegment.findMealSelection()
                                .setPreSelectMealEligibility(psmps.getMealSelection().isPreSelectMealEligibility());
                        passengerSegment.findMealSelection()
                                .setPreSelectMeals(psmps.getMealSelection().getPreSelectMeals());
                        passengerSegment.findMealSelection()
                                .setPreSelectMealEligibility(psmps.getMealSelection().isPreSelectMealEligibility());
                    }
                    // handle meal detail
                    if (null != psmps.getMeal()) {
                        passengerSegment.setMeal(psmps.getMeal());
                    }
                }
            });
        }
    }

    /**
     * @param pnrBooking
     * @param passengers
     * @return
     * @throws ExpectedException
     */
    private List<Passenger> miceChecking(RetrievePnrBooking pnrBooking, List<Passenger> passengers)
            throws ExpectedException {
        // only return logined pax
        Passenger loginPassenger = passengers.stream()
                .filter(passenger -> null != passenger && BooleanUtils.isTrue(passenger.isPrimaryPassenger())).findFirst().orElse(null);
        // find corresponding pnr pax
        RetrievePnrPassenger pnrPassenger = pnrBooking.getPassengers().stream()
                .filter(pnrPax -> null != pnrPax && BooleanUtils.isTrue(pnrPax.isPrimaryPassenger())).findFirst().orElse(null);
        if (null == pnrPassenger || null == loginPassenger) {
            logger.warn("no match with whole name exception");
            throw new ExpectedException(
                    String.format("Cannot match pax name,request data[ rloc:%s]", pnrBooking.getOneARloc()),
                    new ErrorInfo(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH));
        }
        // check has ticket issued or not
        if (!BookingBuildUtil.paxHasIssuedTicket(pnrPassenger, pnrBooking)) {
            loginPassenger.setTickedUnissued(true);
        }
        // grmc can not login
        if (BookingBuildUtil.isMiceGRMC(pnrBooking.getSkList())) {
            loginPassenger.setGrmc(true);
        }
        List<Passenger> loginPax = new ArrayList<>();
        loginPax.add(loginPassenger);
        return loginPax;
    }
	
	/**
	 * transfer seat payment
	 * @param booking
	 * @param seatPayments
	 */
	private void transferSeatPayment(Booking booking) {
		@SuppressWarnings("unchecked")
		List<SeatPayment> seatPayments = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.SEAT_PAYMENT, booking.getOneARloc(), ArrayList.class); 

		if (CollectionUtils.isEmpty(seatPayments)) {
			return;
		}
		
		boolean paymentInfoUpdated = false;
		List<SeatPayment> deleteList = new ArrayList<>();
		for (SeatPayment seatPayment : seatPayments) {
			PassengerSegment passengerSegment = booking.getPassengerSegments().stream().filter(ps -> !StringUtils.isEmpty(ps.getPassengerId()) && !StringUtils.isEmpty(ps.getSegmentId()) && ps.getPassengerId().equals(seatPayment.getPassengerId()) && ps.getSegmentId().equals(seatPayment.getSegmentId())).findFirst().orElse(null);
		
			if (passengerSegment == null || passengerSegment.getSeat() == null) {
				seatPayments.remove(seatPayment);
				continue;
			}
		
			SeatDetail seat = passengerSegment.getSeat();
			if (!StringUtils.isEmpty(seat.getSeatNo()) && seat.getSeatNo().equals(seatPayment.getSeatNo()) && !BooleanUtils.isTrue(seat.isPaid())) {
				seat.setPaid(true);
				if (seatPayment.getPaymentInfo() != null) {
					PaymentInfo paymentInfo = seatPayment.getPaymentInfo();
					RetrievePnrPaymentInfo pnrPayment = new RetrievePnrPaymentInfo();
					pnrPayment.setAmount(paymentInfo.getAmount());
					pnrPayment.setCurrency(paymentInfo.getCurrency());
					pnrPayment.setDate(paymentInfo.getDate());
					pnrPayment.setOfficeId(paymentInfo.getOfficeId());
					pnrPayment.setTicket(paymentInfo.getTicket());
					seat.setPaymentInfo(pnrPayment);
				}
			} else { // if the seat is not matched or the seat already paid which means 1A has added the payment, remove the seat payment from cache
				deleteList.add(seatPayment);
				paymentInfoUpdated = true;
			}
		}
		
		if (paymentInfoUpdated) {
			seatPayments.removeAll(deleteList);
			if (CollectionUtils.isEmpty(seatPayments)) {
				mbTokenCacheRepository.delete(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.SEAT_PAYMENT, booking.getOneARloc());
			} else {
				mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.SEAT_PAYMENT, booking.getOneARloc(), seatPayments);
			}
		}
		
	}
	
	/**
	 * build seat by tempSeat in cache
	 * @param booking 
	 * @param tempSeats 
	 */
	private void buildSeatByTempSeat(Booking booking, List<TempSeat> tempSeats) {
		if (CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		
		// since the original seat will be overwrite by temp seat, build original seat first
		booking.getPassengerSegments().stream().filter(ps -> ps.getSeat() != null).forEach(ps -> {
			try {
				ps.setOriginalSeat((SeatDetail)ps.getSeat().clone());
			} catch (CloneNotSupportedException e) {
				logger.warn(String.format("build original seat failed, rloc: %s, passenger id: %s, segment id: %s", booking.getOneARloc(), ps.getPassengerId(), ps.getSegmentId()));
			}
		});
				
		if (CollectionUtils.isEmpty(tempSeats)) {
			return;
		}
		
		for (TempSeat tempSeat : tempSeats) {
			PassengerSegment passengerSegment = booking.getPassengerSegments().stream().filter(ps -> !StringUtils.isEmpty(ps.getPassengerId()) && !StringUtils.isEmpty(ps.getSegmentId()) 
					&& ps.getPassengerId().equals(tempSeat.getPassengerId()) && ps.getSegmentId().equals(tempSeat.getSegmentId())).findFirst().orElse(null);
			
			if (passengerSegment == null || StringUtils.isEmpty(tempSeat.getJourneyId()) || (StringUtils.isEmpty(tempSeat.getSeatNo()) && StringUtils.isEmpty(tempSeat.getSeatPreference()))) {
				continue;
			}

			if (!StringUtils.isEmpty(tempSeat.getSeatNo())) {
				SeatDetail seatDetail = new SeatDetail();
				seatDetail.setSeatNo(tempSeat.getSeatNo());
				seatDetail.setExlSeat(tempSeat.isExlSeat());
				// if original seat is paid, set "paid" as true
				SeatDetail originalSeat = passengerSegment.getOriginalSeat();
				if (originalSeat != null && BooleanUtils.isTrue(originalSeat.isPaid())
						&& (BooleanUtils.isTrue(originalSeat.isExlSeat()) || (BooleanUtils.isTrue(originalSeat.isAsrSeat()) && !BooleanUtils.isTrue(seatDetail.isExlSeat())))) {
					seatDetail.setPaid(true);
				}
				seatDetail.setTempSeat(true);
				passengerSegment.setSeat(seatDetail);
				passengerSegment.setExtraSeats(null);
			} else {
				SeatPreference seatPreference = new SeatPreference();
				seatPreference.setPreferenceCode(tempSeat.getSeatPreference());
				seatPreference.setTempPreference(true);
				passengerSegment.setPreference(seatPreference);
			}	
		}
		
	}

	/**
	 * 
	 * @param pnrBooking
	 * @return
	 */
	private Future<Map<String, String>> asyncGetUpgradeProgressStatus(RetrievePnrBooking pnrBooking) {
		if(CollectionUtils.isEmpty(pnrBooking.getPassengerSegments())){
			return null;
		}
		Set<String> entitlementIds = new HashSet<>();
		for (RetrievePnrPassengerSegment pnrPassengerSegment : pnrBooking.getPassengerSegments()){
			RetrievePnrUpgradeProcessInfo upgradeInfo = pnrPassengerSegment.getUpgradeProcessInfo();
			if(upgradeInfo == null || BooleanUtils.isTrue(upgradeInfo.getConfirmed())){
				continue;
			}
			if(StringUtils.isNotBlank(upgradeInfo.getEntitlementId())){
				entitlementIds.add(upgradeInfo.getEntitlementId());
			}
		}
		
		if(CollectionUtils.isEmpty(entitlementIds)){
			return null;
		}
		
		return novattiService.asyncGetUpgradeProgressStatus(entitlementIds);
	}
	
	/**
	 * get Earliest Ticket Issue Info
	 * @author jiajian.guo
	 * @param timeZoneMap2 
	 * @param airPortList 
	 */
	private TicketIssueInfo getEarliestTicketIssueInfo(RetrievePnrBooking pnrBooking) {
		List<TicketIssueInfo> ticketIssueInfos = buildTicketIssueInfoList(pnrBooking);

		TicketIssueInfo result = null;
		if (!CollectionUtils.isEmpty(ticketIssueInfos)) {
			for (TicketIssueInfo ticketIssueInfo : ticketIssueInfos) {
				if (OneAConstants.TICKET_INDICATOR_XL.equals(ticketIssueInfo.getIndicator())
						|| OneAConstants.TICKET_INDICATOR_TL.equals(ticketIssueInfo.getIndicator())) {
					result = result == null ? ticketIssueInfo : compareDeadLineDate(result, ticketIssueInfo);
				}
			}
		}
		return result;
	}
	
	/**
	 * build ticketIssueInfo List
	 * @param timeZone
	 * @param pnrBooking
	 * @return List<TicketIssueInfo>
	 */
	private List<TicketIssueInfo> buildTicketIssueInfoList(RetrievePnrBooking pnrBooking) {
		if(CollectionUtils.isEmpty(pnrBooking.getTicketList())){
			return Collections.emptyList();
		}
		
		List<TicketIssueInfo> ticketIssueInfos =new ArrayList<>();
		for (RetrievePnrTicket ticket : pnrBooking.getTicketList()) {
			String ticketTimezone = getTimezoneByOfficeId(ticket.getOfficeId());
			TicketIssueInfo ticketIssueInfo = new TicketIssueInfo();
			ticketIssueInfo.setIndicator(ticket.getIndicator());
			ticketIssueInfo.setDate(ticket.getDate());
			ticketIssueInfo.setTime(ticket.getTime());
			ticketIssueInfo.setOfficeId(ticket.getOfficeId());
			ticketIssueInfo.setTimeZoneOffset(ticketTimezone);
			ticketIssueInfos.add(ticketIssueInfo);
		}
		return ticketIssueInfos;
	}
	
	/**
	 * Compare the dead line date
	 * @param result
	 * @param ticketIssueInfo
	 */
	private TicketIssueInfo compareDeadLineDate(TicketIssueInfo result, TicketIssueInfo ticketIssueInfo) {
		TicketIssueInfo compareResult = null;
		if(StringUtils.isEmpty(result.getTimeZoneOffset()) && !StringUtils.isEmpty(ticketIssueInfo.getTimeZoneOffset())) {
			compareResult = ticketIssueInfo;
		} else if(!StringUtils.isEmpty(result.getTimeZoneOffset()) && !StringUtils.isEmpty(ticketIssueInfo.getTimeZoneOffset())) {
			String dateString = ticketIssueInfo.getDate() + (StringUtils.isEmpty(ticketIssueInfo.getTime()) ? "0000" : ticketIssueInfo.getTime());
			String restulDate = result.getDate() + (StringUtils.isEmpty(result.getTime()) ? "0000" : result.getTime());
			long diffTime = 0L;
			try {
				diffTime = DateUtil.getStrToGMTDate(TicketReminderConstant.TIME_FORMAT, result.getTimeZoneOffset(), restulDate).getTime()
					- DateUtil.getStrToGMTDate(TicketReminderConstant.TIME_FORMAT, ticketIssueInfo.getTimeZoneOffset(), dateString).getTime();
			} catch (ParseException e) {
				logger.error("Parser dparture time error.", e);
			}
			
			compareResult = Long.compare(diffTime, 0L) >= 0 ? ticketIssueInfo : result;
		} else {
			compareResult = result;
		}
		return compareResult;
	}

	private Future<Map<String, ProfilePersonInfo>> asyncCheckMemberHoliday(Booking booking, RetrievePnrBooking pnrBooking, LoginInfo loginInfo) {
		List<String> mpos= bizRuleConfig.getCxkaTierLevel();
		Set<String> memberIds = new HashSet<>();
		for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrBooking.getPassengerSegments()) {
			if(booking.getSegments().stream().noneMatch(segment->Objects.equals(segment.getSegmentID(), pnrPassengerSegment.getSegmentId()))
					|| booking.getPassengers().stream().noneMatch(pax -> Objects.equals(pax.getPassengerId(), pnrPassengerSegment.getPassengerId()))){
				continue;
			}
			List<RetrievePnrFFPInfo> pnrFQTVInfos = pnrPassengerSegment.getFQTVInfos();
			//If FQTV info in product level is empty, get it from customer level
			if(CollectionUtils.isEmpty(pnrFQTVInfos)){
				String marketCompany = Optional.ofNullable(pnrBooking.getSegments()).orElse(new ArrayList<>()).stream()
						.filter(seg -> ObjectUtils.equals(seg.getSegmentID(), pnrPassengerSegment.getSegmentId())).map(RetrievePnrSegment :: getMarketCompany)
						.findFirst().orElse(null);
				RetrievePnrFFPInfo pnrFQTVInfo = getCusLvlFQTVInfo(pnrPassengerSegment.getPassengerId(), pnrBooking.getPassengers(), marketCompany);
				if(pnrFQTVInfo != null && mpos.contains(pnrFQTVInfo.getTierLevel())){
					memberIds.add(pnrFQTVInfo.getFfpMembershipNumber());
				}
			} else {
				//get the CX FQTV first, if there is no CX FQTV, get the first other FQTV
				RetrievePnrFFPInfo retrievePnrFQTVInfo = pnrFQTVInfos.stream().filter(fqtv -> OneAConstants.CX_COMPANY.equals(fqtv.getFfpCompany())).findFirst()
						.orElse(pnrFQTVInfos.stream().findFirst().orElse(null));
				if(retrievePnrFQTVInfo != null && mpos.contains(retrievePnrFQTVInfo.getTierLevel())) {
					memberIds.add(retrievePnrFQTVInfo.getFfpMembershipNumber());
				}	
		    }
		}
		return retrieveProfileService.asyncCheckMemberHoliday(memberIds, loginInfo.getMmbToken());
	}

	/**
	 * Check incomplete redemption booking
	 * @param pnrBooking
	 * @return
	 * @throws ExpectedException 
	 */
	public void checkCxIncompleteRedemptionBooking(Booking booking, RetrievePnrBooking pnrBooking) throws ExpectedException {
		// incomplete Redemption Booking Check
		if (BookingBuildUtil.isCxIncompleteRedemptionBooking(booking, pnrBooking)) {
			throw new ExpectedException(
					String.format("Redemption booking [%s] is incomplete. deny login/filter out.", pnrBooking.getOneARloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_INCOMPLETE_REDEMPTION_BOOKING), HttpStatus.OK);
		}
	}

		 
	/**
	 * check booking before parser
	 * @param pnrBooking
	 * @throws ExpectedException
	 */
	private void checkBooingBeforePaser(RetrievePnrBooking pnrBooking,LoginInfo loginInfo,BookingBuildRequired required) throws ExpectedException{
		
		boolean bookingSummaryListRequest= LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()) && required.isSummaryPage();
		
		// -- has valid segments 
		if (pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getSegments())) {
			throw new ExpectedException("Cannot find any flight in booking, may be the booking cancelled", new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		//-- has passenger
		if (CollectionUtils.isEmpty(pnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find any passenger in booking,rlco [%s]", pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		//-- block mini PNR login(1.IBE office ID 2.No ticket issued 3.AP AMADEUS-H exists)
		if (bookingBuildHelper.isIBEBooking(pnrBooking.getOfficeId()) && !pnrBooking.isTicketRecordExist() && pnrBooking.isApAmadeusHExist()){
			logger.info(String.format("MINI PNR | Blocked | RLOC | %s", pnrBooking.getOneARloc()), true);
			throw new ExpectedException(String.format("Block mini PNR login, rloc [%s]", pnrBooking.getOneARloc()),new ErrorInfo(ErrorCodeEnum.ERR_MINI_PNR_LOGIN));
		}
		//-- check invalid pri of staff booking
		if(BookingBuildUtil.hasInvalidPri(pnrBooking) && !bookingSummaryListRequest){
			throw new ExpectedException("Invalid staff booking priority(01) found in non memebr flow.,", new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_INVALID_STAFF_PRI));
		}
		//has infat without parend id 
		if(pnrBooking.getPassengers().stream().anyMatch(pax->OneAConstants.PASSENGER_TYPE_INF.equals(pax.getPassengerType()) && Objects.isNull(pax.getParentId()))){
			throw new ExpectedException("Invalid booking found, cannot find parent id, may be has invalid SSR INFT in this booking.", new ErrorInfo(ErrorCodeEnum.ERR_INF_WITHOUT_PARENT));
		}
		//group booking
		if(groupClosed(pnrBooking, required) && !bookingSummaryListRequest){
			if (MMBUtil.APPCODE_MMB.equals(MMBUtil.getCurrentAppCode())) {
				throw new ExpectedException(String.format("Group/Mice booking:%s", pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_STAFF_GROUP_BOOKING_NOT_SUPPORT));
			} else {
				logger.info(String.format("Booking Type | MICE | App Code | %s | Allow to retrieve booking information", MMBUtil.getCurrentAppCode()));
				if (MMBUtil.APPCODE_MMB_OLCI_LOGIN.equals(MMBUtil.getCurrentAppCode())) {
					MMBUtil.setAsMMBAppCode(MMBUtil.APPCODE_MMB);
					logger.info(String.format("Finish all the checking of app code 'MMB_OLCI_LOGIN'. Convert to app code 'MMB'."));
				}
			}
		}
		//-- remove fqtv for id booking 
		bookingBuildValidationHelpr.removeAllFqtvForIDBooking(pnrBooking);
	}
	
	/**
	 * gourp type open checking according to channel
	 * for mobile mice not open
	 * @param pnrBooking
	 * @param required 
	 * @return true -> means block
	 */
    private boolean groupClosed(RetrievePnrBooking pnrBooking, BookingBuildRequired required) {
        if (required.isMiceBookingCheck()) {
            // web channel will block non mice
            return BookingBuildUtil.isNonMiceGroupBooking(pnrBooking.getSkList(), pnrBooking.getSsrList());
        } else {
            return BookingBuildUtil.isGroupBooking(pnrBooking.getSsrList())
                    || BookingBuildUtil.isMiceBooking(pnrBooking.getSkList());
        }
    }

	/**
	 * get office city timezone by officeId
	 * 
	 * @param officeId
	 * @return
	 */
	private String getTimezoneByOfficeId(String officeId) {
		// retrieve airPort List by cityCode
		List<String> airPortList = aemService.airPortListByCityCode(StringUtils.left(officeId, TicketReminderConstant.OFFLICE_CODE_LEN));
		String timezone = StringUtils.EMPTY;
		try{
			if (!CollectionUtils.isEmpty(airPortList)) {
				timezone = airportTimeZoneService.getAirPortTimeZoneOffset(airPortList.get(0));
			}
		} catch (Exception e){
			logger.warn(String.format("Cannot find available timezone for airPort:%s", airPortList.get(0)), e);
		}
		return timezone;
	}
	
	/**
	 * get office city code by officeId
	 * 
	 * @param officeId
	 * @return
	 */
	private String getCityCodeByOfficeId(String officeId) {
		if (StringUtils.isEmpty(officeId) || officeId.length() < 3) {
			return null;
		}
		return StringUtils.left(officeId, TicketReminderConstant.OFFLICE_CODE_LEN);
	}
	
	/**
	 * Get AEP preferred POS value.
	 * If country is India, use port code of RP office ID.
	 * 
	 * @param pnrBooking
	 * @return
	 */
	private String getPosForAEP(RetrievePnrBooking pnrBooking) {
		if (AEPConstants.POS_INDIA.equalsIgnoreCase(pnrBooking.getCreationPos())) {
			String officeCityCode = getCityCodeByOfficeId(pnrBooking.getOfficeId());
			if (AEPConstants.POS_INDIA_BLR.equalsIgnoreCase(officeCityCode) ||
					AEPConstants.POS_INDIA_CCU.equalsIgnoreCase(officeCityCode)) {
				return officeCityCode;
			}
		}
		
		return pnrBooking.getCreationPos();
	}
	
	private boolean judgeMandatoryContactInfo(Booking booking, LoginInfo loginInfo) {
		
		// the booking itinerary is flown or not contain any upcoming CX/KA operating sector
		if (allFlightIsFlown(booking) || !hasNotFlownOperatedByCXKA(booking)) {
			return false;
		}
		
		List<Passenger> passengers = booking.getPassengers();
		if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
			String userType = loginInfo.getUserType();
			
			Passenger primaryPax = passengers.stream().filter(
					pax -> pax.isPrimaryPassenger() != null && pax.isPrimaryPassenger()
			).findFirst().orElse(null);

			/*
			 * If AM / MPO login, flag is false
			 * If RU + no /XX, flag is true
			 * If companion booking + no /XX, flag is true
			 */
			if((MMBConstants.AM_MEMBER.equals(userType) || MMBConstants.MPO_MEMBER.equals(userType))) {
				return false;
			}else if(MMBConstants.RU_MEMBER.equals(userType) && primaryPax != null && !hasXXContactInfo(primaryPax)) {
				return true;
			}else if(primaryPax != null && BooleanUtils.isTrue(primaryPax.isCompanion()) && !hasXXContactInfo(primaryPax)) {
				return true;
			}
			
			return false;
		}else{	
			//any passenger in booking with OLSS indicator at the end of CTCM and CTCE, no mandatory contact info is required
			if(hasXXContactInfoFromBooking(booking)) {
				return false;
			}
			
			//any passenger in booking with "validated" CX/KA FQTV, no mandatory contact info is required
			List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
			for(PassengerSegment passengerSegment:passengerSegments) {
				FQTVInfo fqtvInfo = passengerSegment.getFqtvInfo();
				if(fqtvInfo != null && (OneAConstants.COMPANY_CX.equals(fqtvInfo.getCompanyId()) || OneAConstants.COMPANY_KA.equals(fqtvInfo.getCompanyId()))) {
					return false;
				}
			}
			
			//non-GDS bookings with tickets issued by CX or KA office id, no mandatory contact info is required
			String officeId = booking.getOfficeId();
			if(officeId != null && (officeId.contains("CX0") || officeId.contains("KA0"))) {
				return false;
			   }
			}
			return true;
	
      }
	
	/**
	 * booking all flight is flown.
	 * 
	 * @param booking
	 * @return boolean
	 */
	private boolean allFlightIsFlown(Booking booking) {
		if (booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return false;
		}
		return booking.getSegments().stream().allMatch(segment -> BooleanUtils.isTrue(segment.isFlown()));
	}

	/**
	 * booking has sector is operated by operators other than CX and KA.
	 * 
	 * @param booking
	 * @return boolean
	 */
	private boolean hasNotFlownOperatedByCXKA(Booking booking) {
		if (booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return false;
		}
		return booking.getSegments().stream().filter(
					segment -> BooleanUtils.isNotTrue(segment.isFlown())
				).anyMatch(
					segment -> OneAConstants.COMPANY_CX.equals(segment.getOperateCompany())
						|| OneAConstants.COMPANY_KA.equals(segment.getOperateCompany()));
	}

	
		private boolean hasXXContactInfoFromBooking(Booking booking) {
			List<Passenger> passengers = booking.getPassengers();
			for(Passenger passenger:passengers) {
				ContactInfo contactInfo = passenger.getContactInfo();
				if(contactInfo != null && contactInfo.getPhoneInfo() != null && contactInfo.getEmail() != null) {
					boolean olssPhone = contactInfo.getPhoneInfo().isOlssContact();
					boolean olssEmail = contactInfo.getEmail().isOlssContact();
					if(olssPhone && olssEmail) {
						return true;
					}
				}
			}
			return false;
		}
		

	   private boolean hasXXContactInfo(Passenger passenger) {
			//any passenger in booking with OLSS indicator at the end of CTCM and CTCE, no mandatory contact info is required
				ContactInfo contactInfo = passenger.getContactInfo();
				if(contactInfo != null && contactInfo.getPhoneInfo() != null && contactInfo.getEmail() != null) {
					boolean olssPhone = contactInfo.getPhoneInfo().isOlssContact();
					boolean olssEmail = contactInfo.getEmail().isOlssContact();
					if(olssPhone && olssEmail) {
						return true;
					}
				}
			return false;
		}


	/**
	 * 
	 * @Description build baggage allowance
	 * @param
	 * @return void
	 * @author haiwei.jia
	 */
	private void buildBaggageAllowance(Booking booking, RetrievePnrBooking pnrBooking, TicketProcessInfo ticketProcessInfo,
			Future<BaggageAllowanceInfo> futureBaggageAllowanceInfo) {
		// build check-in baggage
		buildCheckInBaggage(booking, pnrBooking, ticketProcessInfo);

		/*
		 * Build cabin baggage and check in baggage (member, limit).
		 */		
		UnaryOperator<String> logMessageBuilder = errorCode -> {
			if (errorCode != null && errorCode.startsWith(BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_PREFIX)) {
				if (BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_CONNECTION.equals(errorCode)) {
					return "It can’t get the baggage allowance info due to connection error";
				} else if (BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_UNKNOWN.equals(errorCode)) {
					return "It can’t get the baggage allowance info due to unsupport scenario [unknown]";
				} else {
					return "It can’t get the baggage allowance info due to unsupport scenario [" + errorCode + "]";
				}
			} else {
				return "Failed to get baggage allowance info.";
			}
		};
		
		try {
			baggageAllowanceBuildService.populateBaggageAllowance(booking, futureBaggageAllowanceInfo);
		} catch (UnexpectedException ex) {
			String logMessage = logMessageBuilder.apply(ex.getErrorInfo().getErrorCode());
			logger.warn(logMessage, ex);
		} catch (ExpectedException ex) {
			String logMessage = logMessageBuilder.apply(ex.getErrorInfo().getErrorCode());
			logger.warn(logMessage, ex);
		} catch (Exception ex) {
			String logMessage = logMessageBuilder.apply(null);
			logger.warn(logMessage, ex);
		}
		
		
	}

	/**
	 * 
	 * @Description build check-in baggage
	 * @param booking
	 * @param pnrPassengerSegments
	 * @return void
	 * @author haiwei.jia
	 */
	private void buildCheckInBaggage(Booking booking, RetrievePnrBooking pnrBooking, TicketProcessInfo ticketProcessInfo) {
		if (pnrBooking == null) {
			return;
		}
		// build check-in standard baggage
		buildCheckInStandardBaggage(booking, pnrBooking.getPassengerSegments(), ticketProcessInfo);

		// build waiver baggage
		buildWaiverBaggage(booking, pnrBooking);
		
		// build purchased baggage
		buildPurchasedBaggage(booking, pnrBooking);
	}

	/**
	 * 
	* @Description build purchased baggage
	* @param booking
	* @param pnrBooking
	* @return void
	* @author haiwei.jia
	 */
	private void buildPurchasedBaggage(Booking booking, RetrievePnrBooking pnrBooking) {
		
		
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || pnrBooking == null
				|| CollectionUtils.isEmpty(pnrBooking.getSsrList())) {
			return;
		}
		
		for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
			BaggageUnitEnum unit = getCorrectBaggageType(passengerSegment);
			
			
			RetrievePnrPassengerSegment	pnrPs = pnrBooking.getPassengerSegments().stream().filter(pnrps->StringUtils.equals(pnrps.getPassengerId(), passengerSegment.getPassengerId())&&StringUtils.equals(pnrps.getSegmentId(),passengerSegment.getSegmentId())).findFirst().orElse(null);
			if(pnrPs == null || pnrPs.getBaggages() ==null ||pnrPs.getBaggages().isEmpty()){
				continue;
			}
			
			Segment segment = getSegmentById(booking.getSegments(), passengerSegment.getSegmentId());
			String operatingCompany = segment == null ? null : segment.getOperateCompany();
			String marketingCompany = segment == null ? null : segment.getMarketCompany();
			
			if (unit == null) {
				unit = pnrPs.getBaggages().get(0).getPcAmount() == null ? BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT: BaggageUnitEnum.BAGGAGE_PIECE_UNIT;
			}
			
			List<PurchasedBaggageDetail> purchasedBaggageDetailList = null;
			if (BaggageUnitEnum.BAGGAGE_PIECE_UNIT.equals(unit)) {
				purchasedBaggageDetailList = pnrPs.getBaggages().stream().filter(bag -> bag.getPcAmount() != null
						&& !StringUtils.isEmpty(bag.getCompanyId())
						&& (bag.getCompanyId().equals(marketingCompany) || bag.getCompanyId().equals(operatingCompany)))
						.map(bag -> {
							PurchasedBaggageDetail purchasedBaggage = new PurchasedBaggageDetail();
							purchasedBaggage.setAmount(bag.getPcAmount());
							purchasedBaggage.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
							purchasedBaggage.setPaymentInfo(bag.getPaymentInfo());
							return purchasedBaggage;
						}).collect(Collectors.toList());
			} else if (BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT.equals(unit)) {

				purchasedBaggageDetailList = pnrPs.getBaggages().stream().filter(bag -> bag.getWeightAmount() != null
						&& bag.getPcAmount() == null && !StringUtils.isEmpty(bag.getCompanyId())
						&& (bag.getCompanyId().equals(marketingCompany) || bag.getCompanyId().equals(operatingCompany)))
						.map(bag -> {
							PurchasedBaggageDetail purchasedBaggage = new PurchasedBaggageDetail();
							purchasedBaggage.setAmount(bag.getWeightAmount());
							purchasedBaggage.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
							purchasedBaggage.setPaymentInfo(bag.getPaymentInfo());
							return purchasedBaggage;
						}).collect(Collectors.toList());

			}
			if(!CollectionUtils.isEmpty(purchasedBaggageDetailList)){
				passengerSegment.findBaggageAllowance().findCheckInBaggage().setPurchasedBaggages(purchasedBaggageDetailList);
			}
			
		}
		
		
	}
	
	/**
	 * Get baggage type of stand baggage 
	 * @param passengerSegment
	 * @return
	 */
	private BaggageUnitEnum getCorrectBaggageType(PassengerSegment passengerSegment) {
		if (passengerSegment.getBaggageAllowance() != null
				&& passengerSegment.getBaggageAllowance().getCheckInBaggage() != null
				&& passengerSegment.getBaggageAllowance().getCheckInBaggage().getStandardBaggage() != null) {
			return passengerSegment.getBaggageAllowance().getCheckInBaggage().getStandardBaggage().getUnit();
		}
		return null;
	}

	/**
	 * 
	 * @Description build waiver baggage
	 * @param booking
	 * @param pnrBooking
	 * @return void
	 * @author haiwei.jia
	 */
	private void buildWaiverBaggage(Booking booking, RetrievePnrBooking pnrBooking) {
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || pnrBooking == null
				|| CollectionUtils.isEmpty(pnrBooking.getSkList())) {
			return;
		}
		
		for(RetrievePnrDataElements sk : pnrBooking.getSkList()){
			if (sk != null && OneAConstants.SK_TYPE_BAGW.equals(sk.getType())
					&& (OneAConstants.COMPANY_CX.equals(sk.getCompanyId()) || OneAConstants.COMPANY_KA.equals(sk.getCompanyId()))
					&& !StringUtils.isEmpty(sk.getPassengerId())
					&& !StringUtils.isEmpty(sk.getSegmentId())
					&& !StringUtils.isEmpty(sk.getFreeText())) {
				StatusManagementModel statusManagementModel = statusManagementDAO.findMostMatchedStatus(MMBConstants.APP_CODE, OneAConstants.SK_SEGMENT, sk.getType(), sk.getStatus());
				// check status of the SK
				if(statusManagementModel == null || !SsrStatusEnum.CONFIRMED.getCode().equals(statusManagementModel.getMmbStatus())) {
					logger.info(String.format("unconfirmed baggage found in booking:%s", pnrBooking.getOneARloc()));
					continue;
				}

				String[] strGroups = sk.getFreeText().split(OneAConstants.SEPARATOR_IN_FREETEXT);
				// String of baggage, e.g. "10K","2P"
				String baggageStr = strGroups[0];
				// baggage type, i.e. "EACH" or "TOTAL"
				String type = strGroups[2];
				
				// amount of baggage
				BigInteger amount = FreeTextUtil.getAmountFromBaggageStr(baggageStr);
				// unit of baggage
				String unitStr = FreeTextUtil.getUnitFromBaggageStr(baggageStr);
				BaggageUnitEnum unitEnm = null;
				if(OneAConstants.BAGGAGE_UNIT_K.equals(unitStr)){
					unitEnm = BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT;
				} else if(OneAConstants.BAGGAGE_UNIT_P.equals(unitStr)){
					unitEnm = BaggageUnitEnum.BAGGAGE_PIECE_UNIT;
				}
				
				// populate the waiver baggage info to booking
				populateWaiverBaggageToBooking(amount, unitEnm, type, sk, booking, pnrBooking.getSkList());
			}
		}

	}

	/**
	 * 
	* @Description populate the  waiver baggage to booking
	* @param amount
	* @param unit
	* @param type
	* @param passengerId
	* @param segmentId
	* @return void
	* @author haiwei.jia
	 */
	private void populateWaiverBaggageToBooking(BigInteger amount, BaggageUnitEnum unit, String type, RetrievePnrDataElements sk,
			Booking booking, List<RetrievePnrDataElements> skList) {	
		if (booking == null || sk == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| StringUtils.isEmpty(sk.getPassengerId())
				|| StringUtils.isEmpty(sk.getSegmentId())
				|| amount == null
				|| unit == null) {
			return;
		}
		
		String passengerId = sk.getPassengerId();
		String segmentId = sk.getSegmentId();
		String companyId = sk.getCompanyId();
		BigInteger skQulifierId = sk.getQulifierId();
		
		// find segment by segmentId
		Segment segment = booking.getSegments().stream()
				.filter(seg -> seg != null && segmentId.equals(seg.getSegmentID()))
				.findFirst().orElse(null);
		
		// find passengerSegment by passengerId and segmentId
		PassengerSegment passengerSegment = booking.getPassengerSegments().stream()
				.filter(ps -> ps != null && passengerId.equals(ps.getPassengerId()) && segmentId.equals(ps.getSegmentId()))
				.findFirst().orElse(null);
		
		// companyId and operating company of the segment should be the same
		if(segment == null || passengerSegment == null || !(companyId.equals(segment.getOperateCompany())||companyId.equals(segment.getMarketCompany())) 
				   || !(segment.getOperateCompany().equals(OneAConstants.COMPANY_CX)||segment.getOperateCompany().equals(OneAConstants.COMPANY_KA))){
			return;
		}
		
		if(OneAConstants.BAGW_TYPE_EACH.equals(type)){
			// populate individual waiver baggage
			populateIndivWaiverBaggage(amount, unit, passengerSegment);
		}else if(OneAConstants.BAGW_TYPE_TOTL.equals(type) && !CollectionUtils.isEmpty(skList)){
			// number of SK whose qulifierId equals skQulifierId
			long sameQulifierIdCount = skList.stream()
					.filter(currentSk -> currentSk != null && currentSk.getQulifierId() != null && currentSk.getQulifierId().equals(skQulifierId))
					.count();
			if(sameQulifierIdCount == 1){
				populateIndivWaiverBaggage(amount, unit, passengerSegment);
			} else if (sameQulifierIdCount > 1){
				populateSharedWaiverBaggage(amount, unit, skQulifierId, passengerSegment);
			}
		}
	}

	/**
	 * 
	* @Description populate shared waiver baggage
	* @param
	* @return void
	* @author haiwei.jia
	 */
	private void populateSharedWaiverBaggage(BigInteger amount, BaggageUnitEnum unit, BigInteger skQulifierId, PassengerSegment passengerSegment) {
		// the unit of shared waiver baggage should be the same with which of check-in standard baggage
		if (!isInCorrectBaggageSystem(unit, passengerSegment)){
			logger.warn(String.format("Baggage system of shared waiver baggage is not correct, passengerId: %s, segmentId: %s",
					passengerSegment.getPassengerId(), passengerSegment.getSegmentId()));
			return;
		}
		
		SharedWaiverBaggage sharedWaiverBaggage = new SharedWaiverBaggage();
		sharedWaiverBaggage.setGroupId(skQulifierId);
		sharedWaiverBaggage.findTotalBaggage().setAmount(amount);
		sharedWaiverBaggage.findTotalBaggage().setUnit(unit);
		
		passengerSegment.findBaggageAllowance().findCheckInBaggage().findSharedWaiverBaggages().add(sharedWaiverBaggage);
	}

	/**
	 * 
	* @Description populate individual waiver baggage
	* @param amount
	* @param unit
	* @param passengerSegment
	* @return void
	* @author haiwei.jia
	 */
	private void populateIndivWaiverBaggage(BigInteger amount, BaggageUnitEnum unit, PassengerSegment passengerSegment) {
		// the unit of waiver baggage should be the same with which of check-in standard baggage
		if (!isInCorrectBaggageSystem(unit, passengerSegment)){
			logger.warn(String.format("Baggage system of waiver baggage is not correct, passengerId: %s, segmentId: %s",
					passengerSegment.getPassengerId(), passengerSegment.getSegmentId()));
			return;
		}
		
		// amount of waiver baggage in passengerSegment
		BigInteger amountInPassengerSegment = passengerSegment.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().findAmount();
		// unit of waiver baggage in passengerSegment
		BaggageUnitEnum unitInPassengerSegment = passengerSegment.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().getUnit();

		// if the unit of the waiver baggage in passengerSegment is empty, set the value
		if(unitInPassengerSegment == null){
			passengerSegment.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setAmount(amount);
			passengerSegment.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setUnit(unit);
		} else if(unit.equals(unitInPassengerSegment)){// else if the unit is the same, add the amount up
			passengerSegment.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setAmount(amountInPassengerSegment.add(amount));
		}
	}

	/**
	 * 
	* @Description judge if the input unit equals the unit of standard baggage in passengerSegment
	* @param unit
	* @param passengerSegment
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean isInCorrectBaggageSystem(BaggageUnitEnum unit, PassengerSegment passengerSegment) {
		// if the unit of check-in standard baggage is not null, the input unit should be the same with it 
		return passengerSegment.findBaggageAllowance().findCheckInBaggage().findStandardBaggage().getUnit() == null
				|| passengerSegment.getBaggageAllowance().getCheckInBaggage().getStandardBaggage().getUnit().equals(unit);
	}
	
 

	/**
	 * 
	 * @Description build standard baggage
	 * @param booking
	 * @param pnrPassengerSegments
	 * @return void
	 * @author haiwei.jia
	 */
	private void buildCheckInStandardBaggage(Booking booking, List<RetrievePnrPassengerSegment> pnrPassengerSegments, TicketProcessInfo ticketProcessInfo) {
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments()) || CollectionUtils.isEmpty(pnrPassengerSegments)) {
			return;
		}

		if (ticketProcessInfo == null || CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())) {
			return;
		}

		// all detailInfos in ticketProcessInfo
		List<TicketProcessDetailGroup> detailInfos = new ArrayList<>();
		for (TicketProcessDocGroup infoGroup : ticketProcessInfo.getDocGroups()) {
			if (infoGroup.getDetailInfos() != null) {
				detailInfos.addAll(infoGroup.getDetailInfos());
			}
		}

		if (CollectionUtils.isEmpty(detailInfos)) {
			return;
		}

		// populateBaggageAllowance
		populateBaggageAllowance(booking, detailInfos);
	}

	/**
	 * get TicketProcessInfo for e-ticket
	 * 
	 * @param pnrPassengerSegments
	 * @return
	 */
	private TicketProcessInfo getTicketProcessInfoForEticket(RetrievePnrBooking pnrBooking) {
		if(pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengerSegments())){
			return null;
		}
		// e-tickets in pnrPassengerSegments
		List<String> etickets = new ArrayList<>();
		for (RetrievePnrPassengerSegment pnrPassengerSegment : pnrBooking.getPassengerSegments()) {
			if (pnrPassengerSegment != null && !CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets())) {
				etickets.addAll(pnrPassengerSegment.getEtickets().stream().map(RetrievePnrEticket::getTicketNumber).collect(Collectors.toList()));
			}
		}

		// remove the same e-ticket
		etickets = etickets.stream().distinct().collect(Collectors.toList());
		TicketProcessInfo ticketProcessInfo = null;
		try {
			ticketProcessInfo = ticketProcessInvokeService.getTicketProcessInfo(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, etickets);
			if (logger.isDebugEnabled()) {
				logger.debug("ticketProcessInfo json:"+new Gson().toJson(ticketProcessInfo));
			}
		} catch (SoapFaultException e) {
			logger.warn("failed to retrieve info by eticket while building baggage allowance");
			return null;
		}
		return ticketProcessInfo;
	}

	/**
	 * 
	 * @Description populate baggage allowance from flightInfos to booking
	 * @param booking
	 * @param detailInfos
	 * @return void
	 * @author haiwei.jia
	 */
	private void populateBaggageAllowance(Booking booking, List<TicketProcessDetailGroup> detailInfos) {
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments()) || CollectionUtils.isEmpty(detailInfos)) {
			return;
		}

		for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
			if (passengerSegment == null || StringUtils.isEmpty(passengerSegment.getEticketNumber())) {
				continue;
			}

			Segment segment = getSegmentById(booking.getSegments(), passengerSegment.getSegmentId());
			if (segment == null || segment.getDepartureTime() == null) {
				continue;
			}

			// flight info related to the passengerSegment by e-ticket
			List<TicketProcessCouponGroup> couponInfos = new ArrayList<>();

			for (TicketProcessDetailGroup detailInfo : detailInfos) {
				if (detailInfo != null && detailInfo.getCouponGroups() != null && passengerSegment.getEticketNumber().equals(detailInfo.getEticket())) {
					couponInfos.addAll(detailInfo.getCouponGroups());
				}
			}

			if (CollectionUtils.isEmpty(couponInfos)) {
				continue;
			}

			String marketCompany = segment.getMarketCompany();
			String marketSegmentNumber = segment.getMarketSegmentNumber();
			String originPort = segment.getOriginPort();
			String destPort = segment.getDestPort();
			String pnrTime = segment.getDepartureTime().getPnrTime();

//			String std = segment.getDepartureTime().getPnrTime()
		/*	Date stdDate = null
			try {
				stdDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, std);
			} catch (ParseException e) {
				logger.warn(String.format("Failed to conver string %s to date", std));
				continue;
			}*/

			for (TicketProcessCouponGroup flightInfo : couponInfos) {
				if (flightInfo == null || CollectionUtils.isEmpty(flightInfo.getFlightInfos())
						|| CollectionUtils.isEmpty(flightInfo.getBaggageAllowances())) {
					continue;
				}
				boolean matched = segmentInfoMatched(marketCompany, marketSegmentNumber, originPort, destPort, pnrTime, flightInfo);
				if (matched) {
					passengerSegment.findBaggageAllowance().findCheckInBaggage()
							.setStandardBaggage(getBaggageAllowanceValue(flightInfo.getBaggageAllowances()));
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @Description get value of baggage allowance from baggageAllowances
	 * @param baggageAllowances
	 * @return String
	 * @author haiwei.jia
	 */
	private BaggageDetail getBaggageAllowanceValue(List<TicketProcessBaggageAllowance> baggageAllowances) {
		BaggageDetail baggageDetail = null;
		for (TicketProcessBaggageAllowance allowance : baggageAllowances) {
			if (allowance.getNumber() == null || allowance.getUnit() == null) {
				continue;
			}
			if (baggageDetail == null) {
				baggageDetail = new BaggageDetail();
				baggageDetail.setAmount(allowance.getNumber());
				baggageDetail.setUnit(allowance.getUnit());
			} else if (allowance.getUnit().equals(baggageDetail.getUnit())) {
				baggageDetail.setAmount(baggageDetail.getAmount().add(allowance.getNumber()));
			} else {
				return null;
			}
		}
		return baggageDetail;
	}

	/**
	 * 
	* @Description if the segment info is matched with info in flightInfo
	* @param marketeCompany
	* @param marketSegmentNumber
	* @param originPort
	* @param destPort
	* @param pnrTime
	* @param couponInfo
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean segmentInfoMatched(String marketCompany, String marketSegmentNumber, String originPort,
			String destPort, String pnrTime, TicketProcessCouponGroup couponInfo) {
		if(StringUtils.isEmpty(marketCompany)
				|| StringUtils.isEmpty(marketSegmentNumber)
				|| StringUtils.isEmpty(originPort)
				|| StringUtils.isEmpty(destPort)
				|| StringUtils.isEmpty(pnrTime)
				|| couponInfo == null){
			return false;
		}
		
		String pnrTimeStr = DateUtil.convertDateFormat(pnrTime, DepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_DDMMYY);
		
		for(TicketProcessFlightInfo flightInfo : couponInfo.getFlightInfos()){
			if(flightInfo == null 
					|| flightInfo.getFlightDate() == null
					|| StringUtils.isEmpty(flightInfo.getFlightDate().getDepartureTime())
					|| StringUtils.isEmpty(flightInfo.getFlightDate().getDepartureDate())){
				continue;
			}
			// departure time in flightBascInfo, time format ddMMyyHHmm
			//remove departureTime check because it is not needed for ticket call
			/*String departureTimeStr = flightBasicInfo.getFlightDate().getDepartureDate()+flightBasicInfo.getFlightDate().getDepartureTime();
			Date departureTimeDate = null;
			
			try {
				departureTimeDate = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_DDMMYYHHMM, departureTimeStr);
			} catch (ParseException e) {
				logger.warn(String.format("Failed to conver string %s to date",departureTimeStr));
				continue;
			}*/
			
			if(marketCompany.equals(flightInfo.getMarketingCompany())
					&& marketSegmentNumber.equals(flightInfo.getFlightNumber())
					&& originPort.equals(flightInfo.getBoardPoint())
					&& destPort.equals(flightInfo.getOffpoint())
					&& flightInfo.getFlightDate().getDepartureDate().equals(pnrTimeStr)){
				return true;
			}
		}
		return false;
	}

	private void buildRedemptionConfirmedBooking(Booking booking, RetrievePnrBooking flightBooking, LoginInfo loginInfo) throws BusinessBaseException{
		if (flightBooking == null || CollectionUtils.isEmpty(flightBooking.getSegments()) || CollectionUtils.isEmpty(flightBooking.getPassengers())) {
			throw new UnexpectedException("Cannot find any flight in booking.", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		// Complete payment checking
		boolean canIssueTicket = issueTicketChecking(flightBooking, loginInfo, booking.isRedemptionBooking());
		booking.setCanIssueTicket(canIssueTicket);
	}
	
	private Map<String, String> getVersionMap(List<String> list) {
		Map<String, String> map = new HashMap<>();
		for(String str : list) {
			String[] strs = str.split("-");
			if(strs.length == 2) {
				map.put(strs[1], strs[0]);
			}
		}
		return map;
	}

	/**
	 * build passenger segment info 
	 * 
	 * @param booking
	 * @param pnrBooking
	 * @param travelDocList
	 * @param secMap 
	 * @param priMap 
	 * @param futureCheckMemberHoliday 
	 * @param futurePassengerCheckInInfo 
	 * @param futureUpgradeProgressStatus 
	 * @param tempSeats 
	 * @throws BusinessBaseException 
	 */
	private void buildPassengerSegmentInfo(Booking booking, RetrievePnrBooking pnrBooking, List<TravelDocList> travelDocList, Map<String, String> priMap,
			Map<String, String> secMap, BookingBuildRequired required, TicketProcessInfo ticketProcessInfo, Future<AEPProductsResponse> futureAEPProductsResponse, Future<Map<String, ProfilePersonInfo>> futureCheckMemberHoliday, Future<Map<String, String>> futureUpgradeProgressStatus, List<TempSeat> tempSeats) throws BusinessBaseException {
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
		logger.debug("Start buildPassengerSegmentInfo");
		List<SpecialServiceModel> specialServiceModels = specialServiceDAO.findAllByAppCodeAndAction(MMBConstants.APP_CODE,TBConstants.REMINDER_SPECIALSERVICE_KEY);
		List<StatusManagementModel> statusManagementModels = statusManagementDAO.findAllByAppCodeAndType(MMBConstants.APP_CODE,TBConstants.TB_STATUS_MANAGEMENT_TYPE_SSR);
		List<String> specials = specialServiceModels.stream().map(SpecialServiceModel::getReminderCode).collect(Collectors.toList());
		Map<FQTVInfo, List<RetrievePnrSegment>> fqtvSegmentsMap = new HashMap<>();
		for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrBooking.getPassengerSegments()) {
			if(booking.getSegments().stream().noneMatch(segment-> Objects.equals(segment.getSegmentID(), pnrPassengerSegment.getSegmentId()))
					|| booking.getPassengers().stream().noneMatch(pax -> Objects.equals(pax.getPassengerId(), pnrPassengerSegment.getPassengerId()))){
				continue;
			}
			PassengerSegment passengerSegment = new PassengerSegment();
			passengerSegment.setPassengerId(pnrPassengerSegment.getPassengerId());
			passengerSegment.setSegmentId(pnrPassengerSegment.getSegmentId());
			
			//Pick up number(for train)
			passengerSegment.setPickUpNumber(pnrPassengerSegment.getPickUpNumber());
			
			//build FQTV and MemberHodliay if needed
			buildFQTVInfo(pnrPassengerSegment, pnrBooking, passengerSegment, futureCheckMemberHoliday, required.requireFqtvHolidayCheck());

			//build FQTR
			buildFQTRInfo(pnrPassengerSegment, pnrBooking, passengerSegment);
			
			//build ClaimedLounge and PurchaseLounge
			buildLoungeAccess(booking, pnrPassengerSegment, passengerSegment);
			
			//build Special Services
			buildSpecialServices(statusManagementModels,specials,booking,pnrBooking, passengerSegment);

			//build Travel Document
			if(required.travelDocument()) {
				buildPassengerSegmentTravelDoc(pnrPassengerSegment, booking, passengerSegment, travelDocList, priMap, secMap);				
			}
			
			// build Country of Residence
			buildPassengerSegmentCountryOfResidence(pnrPassengerSegment, passengerSegment);
			
			// build e-ticket
			buildEticket(passengerSegment, pnrPassengerSegment, ticketProcessInfo, booking);
			
			
			// build e-ticket originator id
			buildOriginatorId(passengerSegment, ticketProcessInfo);
			
			
			// build selected meal
			passengerSegment.setMeal(buildSelectedMeal(pnrPassengerSegment.getMeal()));
			
			//build seat
			buildSeat(passengerSegment, pnrPassengerSegment, booking);		

			//combine the member FQTV info with segment
			RetrievePnrSegment pnrSegment = getPnrSegmentById(pnrBooking.getSegments(), pnrPassengerSegment.getSegmentId());
			if (StringUtils.isNotEmpty(passengerSegment.getFqtvInfo().getMembershipNumber()) && pnrSegment != null) {           
	            if (fqtvSegmentsMap.keySet().stream().noneMatch(fqtv->fqtv.equals(passengerSegment.getFqtvInfo()))) {
	               List<RetrievePnrSegment> pnrSegmentList = new ArrayList<>();
	               pnrSegmentList.add(pnrSegment);
	               fqtvSegmentsMap.put(passengerSegment.getFqtvInfo(), pnrSegmentList);
	            } else {
	               fqtvSegmentsMap.get(passengerSegment.getFqtvInfo()).add(pnrSegment);
	            }
	        }
			
			// build airport upgrade information
			buildAirportUpgradeInfo(passengerSegment, pnrPassengerSegment);
			buildUpgradeProcessStatus(passengerSegment, pnrPassengerSegment, futureUpgradeProgressStatus);
			passengerSegments.add(passengerSegment);
		}
		
		// Call baggage allowance API after passenger segment associations are built.
		Future<BaggageAllowanceInfo> futureBaggageAllowanceInfo = null;
		if (required.baggageAllowances() && !MMBBizruleConstants.ACCESS_CHANNEL_WEB.equals(MMBUtil.getCurrentAccessChannel())) {
			futureBaggageAllowanceInfo =
					baggageAllowanceBuildService.asyncRetrieveBaggageAllowanceInfo(booking, futureAEPProductsResponse);
		}
		
		//set the member award info
		if(required.operateInfoAndStops() && required.memberAward()) {
			buildMemberAward(passengerSegments, fqtvSegmentsMap, pnrBooking);
		}
		
		//build meal selection
		if(required.mealSelection()) {
			buildMealSelection(booking);
		}
		
		// build baggage allowance
		if(required.baggageAllowances() && !MMBBizruleConstants.ACCESS_CHANNEL_WEB.equals(MMBUtil.getCurrentAccessChannel())) {
			buildBaggageAllowance(booking, pnrBooking, ticketProcessInfo, futureBaggageAllowanceInfo);
		} else {
			// Always build check in baggage by PNR, no more external call.
			buildCheckInBaggage(booking, pnrBooking, ticketProcessInfo);
		}
		
		//build segment pickUpNumber case && display
		buildSegmentPickUpNumberCase(booking, pnrBooking);
		logger.debug("end buildPassengerSegmentInfo");
	}

	/**
	 * 
	 * @param passengerSegment
	 * @param pnrPassengerSegment
	 * @param futureUpgradeProgressStatus
	 */
	private void buildUpgradeProcessStatus(PassengerSegment passengerSegment,
			RetrievePnrPassengerSegment pnrPassengerSegment, Future<Map<String, String>> futureUpgradeProgressStatus) {
		if(pnrPassengerSegment == null || pnrPassengerSegment.getUpgradeProcessInfo() == null){
			return;
		}
		RetrievePnrUpgradeProcessInfo upgradeProcessInfo = pnrPassengerSegment.getUpgradeProcessInfo();
		if(upgradeProcessInfo.getConfirmed() != null && upgradeProcessInfo.getConfirmed()){
			passengerSegment.setUpgradeProgressStatus(UpgradeProgressStatus.CONFIRMED);
		} else {
			if(StringUtils.isNotBlank(upgradeProcessInfo.getEntitlementId()) && futureUpgradeProgressStatus != null){
				Map<String, String> map = null;
				try {
					map = futureUpgradeProgressStatus.get();
				} catch (Exception e) {
					logger.error("Fail to retrieve Upgrade Progress Status",e);
				}
				if(map != null){
					passengerSegment.setUpgradeProgressStatus(UpgradeProgressStatus.REQUESTED.name().equals(map.get(upgradeProcessInfo.getEntitlementId())) ? UpgradeProgressStatus.REQUESTED : null);
				}
			}
		}
	}

	/**
	 * Build airport upgrade information
	 * 
	 * @param passengerSegment
	 * @param pnrPassengerSegment
	 */
	private void buildAirportUpgradeInfo(PassengerSegment passengerSegment, RetrievePnrPassengerSegment pnrPassengerSegment) {
		if(pnrPassengerSegment == null || pnrPassengerSegment.getAirportUpgradeInfo() == null) {
			return;
		}
		RetrievePnrAirportUpgradeInfo pnrAirportUpgradeInfo = pnrPassengerSegment.getAirportUpgradeInfo();
		
		AirportUpgradeInfo airportUpgradeInfo = new AirportUpgradeInfo();
		airportUpgradeInfo.setNewCabinClass(pnrAirportUpgradeInfo.getNewCabinClass());
		
		passengerSegment.setAirportUpgradeInfo(airportUpgradeInfo);
	}

	/**
	 * Build claimed lounge and purchase lounge
	 * 
	 * @param booking
	 * @param pnrPassengerSegment
	 * @param passengerSegment
	 * @param futurePassengerCheckInInfo
	 * @param cprCheck
	 */
	private void buildLoungeAccess(Booking booking, RetrievePnrPassengerSegment pnrPassengerSegment, PassengerSegment passengerSegment) {
		 
		populateClaimedLounge(booking, pnrPassengerSegment, passengerSegment);
		populatePurchasedLounge(booking, pnrPassengerSegment, passengerSegment);
	}

	/**
	 * populate claimed lounge
	 * 
	 * @param booking
	 * @param pnrPassengerSegment
	 * @param passengerSegment
	 * @param passengerCheckInInfos
	 */
	private void populateClaimedLounge(Booking booking, RetrievePnrPassengerSegment pnrPassengerSegment,
			PassengerSegment passengerSegment) {

		Passenger passenger = booking.getPassengers().stream().filter(pax -> StringUtils.isNotEmpty(pax.getPassengerId()) && pax.getPassengerId().equals(passengerSegment.getPassengerId())).findFirst().orElse(null);
		Segment segment = booking.getSegments().stream().filter(se -> StringUtils.isNotEmpty(se.getSegmentID()) && se.getSegmentID().equals(passengerSegment.getSegmentId())).findFirst().orElse(null);
		if(passenger == null || segment == null){
			return;
		}
		
		if (!CollectionUtils.isEmpty(pnrPassengerSegment.getClaimedLounges())){
			RetrievePnrLoungeInfo retrievePnrLoungeInfo = pnrPassengerSegment.getClaimedLounges()
					.stream().filter(lounge -> LoungeClass.FIRST.code().equals(lounge.getType())).findFirst().orElse(pnrPassengerSegment.getClaimedLounges().get(0));
			ClaimedLounge claimedLounge = new ClaimedLounge();
			claimedLounge.setType(LoungeClass.parseCode(retrievePnrLoungeInfo.getType()));
			claimedLounge.setTier(retrievePnrLoungeInfo.getTier());
			claimedLounge.setGuid(retrievePnrLoungeInfo.getGuid());
			claimedLounge.setEntitlementId(retrievePnrLoungeInfo.getEntitlementId());
			claimedLounge.setPassengerType(retrievePnrLoungeInfo.getPassengerType());
			
			passengerSegment.setClaimedLounge(claimedLounge);
		}
		 
	}
	
	/**
	 * Populate purchase lounge, from OLCI first, then use pnr
	 * @param booking
	 * @param pnrPassengerSegment
	 * @param passengerSegment
	 * @param passengerCheckInInfos
	 */
	private void populatePurchasedLounge(Booking booking, RetrievePnrPassengerSegment pnrPassengerSegment, PassengerSegment passengerSegment) {
		Segment segment = booking.getSegments().stream().filter(se -> StringUtils.isNotEmpty(se.getSegmentID()) && se.getSegmentID().equals(passengerSegment.getSegmentId())).findFirst().orElse(null);
		Passenger passenger = booking.getPassengers().stream().filter(pax -> StringUtils.isNotEmpty(pax.getPassengerId()) && pax.getPassengerId().equals(passengerSegment.getPassengerId())).findFirst().orElse(null);
		if(passenger == null || segment == null){
			return;
		}

		if (!CollectionUtils.isEmpty(pnrPassengerSegment.getPurchasedLounges())){
			RetrievePnrLoungeInfo retrievePnrLoungeInfo = pnrPassengerSegment.getPurchasedLounges()
					.stream().filter(lounge -> LoungeClass.FIRST.code().equals(lounge.getType())).findFirst().orElse(pnrPassengerSegment.getPurchasedLounges().get(0));
			PurchasedLounge purchasedLounge = new PurchasedLounge();
			purchasedLounge.setType(LoungeClass.parseCode(retrievePnrLoungeInfo.getType()));
			purchasedLounge.setTier(retrievePnrLoungeInfo.getTier());
			purchasedLounge.setGuid(retrievePnrLoungeInfo.getGuid());
			purchasedLounge.setEntitlementId(retrievePnrLoungeInfo.getEntitlementId());
			purchasedLounge.setPassengerType(retrievePnrLoungeInfo.getPassengerType());
			purchasedLounge.setPaymentInfo(retrievePnrLoungeInfo.getPaymentInfo());
			passengerSegment.setPurchasedLounge(purchasedLounge);
		}
		
	}

	/***
	 * Multiple pax and multiple sector: SSR with pax and sector association
	 * Multiple pax and one sector: Possible to have SSR with pax association but without segment association
	 * One pax and multiple sectors: Possible to have SSR with segment association but without pax association
	 * One pax and one sector: Possible to have SSR without pax or segment association
	 * order by code
	 */
	private void buildSpecialServices(List<StatusManagementModel> statusManagementModels,List<String> specials,Booking booking, RetrievePnrBooking pnrBooking, PassengerSegment passengerSegment) {
		List<SpecialService> specialServices = new ArrayList<>();
		List<RetrievePnrDataElements> ssrList = pnrBooking.getSsrList();
		if (!CollectionUtils.isEmpty(ssrList) && !CollectionUtils.isEmpty(specials)) {
			boolean isManySeg = booking.getSegments().size() > 1;
			boolean isManyPax = booking.getPassengers().size() > 1;
			boolean manyToMany = isManySeg && isManyPax;
			boolean oneToOne = !isManySeg && !isManyPax;
			boolean manyPaxToOneSeg = !isManySeg && isManyPax;
			boolean manySegToOnePax = isManySeg && !isManyPax;

			ssrList.stream().forEach(retrievePnrDataElements -> {
				if (!specials.contains(retrievePnrDataElements.getType())) return;
				if (statusManagementModels.stream().noneMatch(statusManagementModel ->
						statusManagementModel.getStatusCode().equals(retrievePnrDataElements.getStatus()))) return;
				SpecialService specialService = new SpecialService();
				
				//one pax one segment
				if (oneToOne) {
					setSpecialServiceInfo(statusManagementModels, pnrBooking, passengerSegment, retrievePnrDataElements,
							specialService);
				}

				//multiple pax one segment
				if (manyPaxToOneSeg) {
					if (retrievePnrDataElements.getPassengerId() != null) {
						if (retrievePnrDataElements.getPassengerId().equals(passengerSegment.getPassengerId())) {
							setSpecialServiceInfo(statusManagementModels, pnrBooking, passengerSegment,
									retrievePnrDataElements, specialService);
						}
					} else {
						booking.setIssueSpecialServices(true);
						logger.warn(String.format("Invalid SRR associate, cannot find ST/PT of SSR[%s], rloc[%s]", retrievePnrDataElements.getType(), booking.getOneARloc()));
					}
				}

				//multiple segment one pax
				if (manySegToOnePax) {
					if (retrievePnrDataElements.getSegmentId() != null) {
						if (retrievePnrDataElements.getSegmentId().equals(passengerSegment.getSegmentId())) {
							setSpecialServiceInfo(statusManagementModels, pnrBooking, passengerSegment,
									retrievePnrDataElements, specialService);
						}
					} else {
						booking.setIssueSpecialServices(true);
						logger.warn(String.format("Invalid SRR associate, cannot find ST/PT of SSR[%s], rloc[%s]", retrievePnrDataElements.getType(), booking.getOneARloc()));
					}
				}

				if (manyToMany) {					
					if (retrievePnrDataElements.getPassengerId() != null && retrievePnrDataElements.getSegmentId() != null) {
						if (retrievePnrDataElements.getPassengerId().equals(passengerSegment.getPassengerId()) && retrievePnrDataElements.getSegmentId().equals(passengerSegment.getSegmentId())) {
							setSpecialServiceInfo(statusManagementModels, pnrBooking, passengerSegment,
									retrievePnrDataElements, specialService);
						}
					} else {
						booking.setIssueSpecialServices(true);
						logger.warn(String.format("Invalid SRR associate, cannot find ST/PT of SSR[%s], rloc[%s]", retrievePnrDataElements.getType(), booking.getOneARloc()));
					}
				}

				if(specialService.getCode() != null){
					specialServices.add(specialService);
				}
			});
			passengerSegment.setSpecialServices(specialServices.stream().sorted(Comparator.comparing(SpecialService::getCode)).collect(Collectors.toList()));
		}
	}

	public void setSpecialServiceInfo(List<StatusManagementModel> statusManagementModels, RetrievePnrBooking pnrBooking,
			PassengerSegment passengerSegment, RetrievePnrDataElements retrievePnrDataElements,
			SpecialService specialService) {
		specialService.setQulifierId(retrievePnrDataElements.getQulifierId());
		specialService.setCode(retrievePnrDataElements.getType());
		specialService.setFreeText(retrievePnrDataElements.getFreeText());
		specialService.setStatus(convertBookingStatus(statusManagementModels,retrievePnrDataElements.getStatus()));
		addUMNRAdditionalStatus(specialService, retrievePnrDataElements.getType(), retrievePnrDataElements.getPassengerId(), pnrBooking, passengerSegment);
	}

	private void addUMNRAdditionalStatus(SpecialService specialService, String ssrType, String paxId, RetrievePnrBooking pnrBooking, PassengerSegment passengerSegment) {
		if (ssrType.equals(OneAConstants.UNACCOMPANIED_MINOR_SSR_CODE_UMNR)) {
			RetrievePnrSegment segment = pnrBooking.getSegments().stream().filter(segmentInfo -> segmentInfo.getSegmentID().equals(passengerSegment.getSegmentId())).findFirst().orElse(null);
			boolean cxkaCompany = StringUtils.isNotEmpty(segment.getPnrOperateCompany()) ?
					(segment.getPnrOperateCompany().equalsIgnoreCase(OneAConstants.COMPANY_CX) || segment.getPnrOperateCompany().equalsIgnoreCase(OneAConstants.COMPANY_KA)) :
						(segment.getMarketCompany().equalsIgnoreCase(OneAConstants.COMPANY_CX) || segment.getMarketCompany().equalsIgnoreCase(OneAConstants.COMPANY_KA));
			if (cxkaCompany) {
				if (umnreFormBuildService.hasEFormRemark(paxId, pnrBooking)) {								
					specialService.setAdditionalStatus(MMBBizruleConstants.HAVE_UNACCOMPANIED_MINOR_EFORM); 
				} else {
					specialService.setAdditionalStatus(MMBBizruleConstants.NON_UNACCOMPANIED_MINOR_EFORM);
				}		
			}
		}
	}

	private String convertBookingStatus(List<StatusManagementModel> statusManagementModels,String status){
		return statusManagementModels.stream().filter(statusManagementModel ->
				statusManagementModel.getStatusCode().equals(status)).findFirst().get().getMmbStatus();
	}

	/**
	 * build e-ticket
	 * 
	 * @param pnrPassengerSegment
	 * @param ticketProcessInfo
	 * @param booking
	 */
	private void buildEticket(PassengerSegment passengerSegment, RetrievePnrPassengerSegment pnrPassengerSegment,
			TicketProcessInfo ticketProcessInfo, Booking booking) {
		if(CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets())){
			return;
		}
		
		buildCorrespondingEticket(passengerSegment, pnrPassengerSegment, ticketProcessInfo, booking);
	}
	
	/**
	 * build e-ticket originator id
	 * 
	 * @param pnrPassengerSegment
	 * @param ticketProcessInfo
	 */
	private void buildOriginatorId(PassengerSegment passengerSegment, TicketProcessInfo ticketProcessInfo) {
		
		if(ticketProcessInfo == null || CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())){
			return;
		}

		buildCorrespondingOriginatorId(passengerSegment,ticketProcessInfo);
		
	}
	
	
	
	/**
	 * build corresponding e-ticket originator id
	 * 
	 * @param passengerSegment
	 * @param ticketProcessInfo
	 */
	private void buildCorrespondingOriginatorId(PassengerSegment passengerSegment, TicketProcessInfo ticketProcessInfo) {
		
		if (ticketProcessInfo == null || CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())) {
			return;
		}
		

		for (TicketProcessDocGroup infoGroup : ticketProcessInfo.getDocGroups()) {
			if (CollectionUtils.isEmpty(infoGroup.getDetailInfos())) {
				continue;
			}
			

			for(TicketProcessDetailGroup detailInfo : infoGroup.getDetailInfos()){
				if (detailInfo == null || StringUtils.isEmpty(passengerSegment.getEticketNumber())) {
					continue;
				}
				else if (passengerSegment.getEticketNumber().equals(detailInfo.getEticket())) {
					
					if ((infoGroup.getOriginatorInfo()!= null) && (infoGroup.getOriginatorInfo().getOriginIdentification()!=null) && !(StringUtils.isEmpty(infoGroup.getOriginatorInfo().getOriginIdentification().getOriginatorId()))) {
							passengerSegment.setEticketOriginatorId(infoGroup.getOriginatorInfo().getOriginIdentification().getOriginatorId());
					}
				}
			}
		}
	}
	
	/**
	 * build corresponding e-ticket
	 * 
	 * @param passengerSegment
	 * @param pnrPassengerSegment
	 * @param ticketProcessInfo
	 * @param booking
	 */
	private void buildCorrespondingEticket(PassengerSegment passengerSegment, RetrievePnrPassengerSegment pnrPassengerSegment,
			TicketProcessInfo ticketProcessInfo, Booking booking) {
		// segment related to passengerSegment
		Segment segment = getSegmentById(booking.getSegments(), passengerSegment.getSegmentId());
		if (segment == null 
				|| segment.getDepartureTime() == null 
				|| ticketProcessInfo == null 
				|| CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())
				|| CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets())) {
			logger.info(
				String.format(
					"Skip to build eticket for passenger [%s] and segment [%s] because parameter is empty | "
						+ "Empty segment: %s |"
						+ "Empty departure time: %s |"
						+ "Empty ticket process info: %s |"
						+ "Empty doc groups: %s |"
						+ "Empty eTicket: %s",
					passengerSegment.getPassengerId(),
					passengerSegment.getSegmentId(),
					segment == null,
					(segment != null) ? segment.getDepartureTime() == null : true,
					ticketProcessInfo == null,
					(ticketProcessInfo != null) ? CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups()) : true,
					(pnrPassengerSegment != null) ? CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets()) : true	
				)
			);
			return;
		}
		
		// segment info
		String marketCompany = segment.getMarketCompany();
		String marketSegmentNumber = segment.getMarketSegmentNumber();
		String originPort = segment.getOriginPort();
		String destPort = segment.getDestPort();
		String pnrTime = segment.getDepartureTime().getPnrTime();
		
		for(RetrievePnrEticket eTicket : pnrPassengerSegment.getEtickets()) {
			if(eTicket == null || StringUtils.isEmpty(eTicket.getTicketNumber())) {
				logger.info(
					String.format(
						"Skip to build eticket for passenger [%s] with segment [%s] because eticket is empty", 
						passengerSegment.getPassengerId(),
						passengerSegment.getSegmentId()
					)
				);
				continue;
			}
			for(TicketProcessDocGroup infoGroup : ticketProcessInfo.getDocGroups()) {
				if (CollectionUtils.isEmpty(infoGroup.getDetailInfos())) {
					logger.info(
						String.format(
							"Passenger [%s] with segment [%s]'s details info of info group is empty. Skip this info group.", 
							passengerSegment.getPassengerId(),
							passengerSegment.getSegmentId()
						)
					);
					continue;
				}
				String ticketProductDate = infoGroup.getProductInfo() != null ? infoGroup.getProductInfo().getProductDate() : null;
				BigInteger iataNumber = Optional.ofNullable(infoGroup.getSysProvider()).orElse(new TicketProcessSysProvider()).getBookingIataNumber();
				for(TicketProcessDetailGroup detailInfo : infoGroup.getDetailInfos()){
					if(!eTicket.getTicketNumber().equals(detailInfo.getEticket())) {
						// Expected if-block - find out matched eticket info
						continue;
					}
					// Fix of OLSSMMB-21883 (System error occurred if missing couponGroup in ticket process)
					if (detailInfo.getCouponGroups() == null) {
						continue;
					}
					for (TicketProcessCouponGroup couponInfo : detailInfo.getCouponGroups()) {
						if (couponInfo == null || CollectionUtils.isEmpty(couponInfo.getFlightInfos())) {
							logger.info(
								String.format(
									"Passenger [%s] with segment [%s]'s flight info of coupon info is empty. Skip this coupon info.", 
									passengerSegment.getPassengerId(),
									passengerSegment.getSegmentId()
								)
							);
							continue;
						}
						if (segmentInfoMatched(marketCompany, marketSegmentNumber, originPort, destPort, pnrTime, couponInfo)) {
							passengerSegment.setEticketNumber(eTicket.getTicketNumber());
							passengerSegment.setEticketOriginatorId(infoGroup == null ? null : infoGroup.getOriginatorId());
							passengerSegment.findEticket().setCouponInfos(couponInfo.getCouponInfos());
							passengerSegment.findEticket().setDate(eTicket.getDate());
							passengerSegment.findEticket().setPassengerType(eTicket.getPassengerType());
							passengerSegment.findEticket().setProductDepartureDate(ticketProductDate);
							passengerSegment.findEticket().setCurrency(eTicket.getCurrency());
							passengerSegment.findEticket().setAmount(eTicket.getAmount());
							passengerSegment.findEticket().setIataNumber(iataNumber);
							passengerSegment.findEticket().setFareRateTariffClass(Optional.ofNullable(couponInfo.getPricingInfo()).orElse(new TicketProcessPricingInfo()).getRateTariffClass());
							
							// Set currency and amount from ticket process fare info.
							if (infoGroup != null && infoGroup.getFareInfo() != null) {
								TicketProcessFareDetail fareInfo = infoGroup.getFareInfo();
								passengerSegment.findEticket().setTicketProcessFareCurrency(fareInfo.getCurrency());
								passengerSegment.findEticket().setTicketProcessFareAmount(fareInfo.getAmount() == null ? null : fareInfo.getAmount().toString());
							}
							return;
						}
					}
				}

			}
		}
	}

	/**
	 * 
	 * @Description build segment pickUpNumber case && display
	 * @param booking
	 * @param pnrBooking
	 */
	private void buildSegmentPickUpNumberCase(Booking booking, RetrievePnrBooking pnrBooking) {
		List<Segment> segments = booking.getSegments();
		List<RetrievePnrPassengerSegment> pnrPassengerSegments = pnrBooking.getPassengerSegments();
		String officeId = pnrBooking.getOfficeId();
		for(Segment segment : segments) {
			if(!OneAConstants.EQUIPMENT_TRN.equals(segment.getAirCraftType())) {
				continue;
			}
			boolean empty = false;
			for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrPassengerSegments) {
				if(segment.getSegmentID().equals(pnrPassengerSegment.getSegmentId())
						&& StringUtils.isNotEmpty(pnrPassengerSegment.getPassengerId()) 
						&& !pnrPassengerSegment.getPassengerId().endsWith(PnrResponseParser.PASSENGER_INFANT_ID_SUFFIX)
						&& StringUtils.isEmpty(pnrPassengerSegment.getPickUpNumber())) {
					empty = true;
					break;
				}
			}
			if(empty) {
				segment.setTrainPNDisplay(false);
				segment.setTrainCase(bookingBuildHelper.getTrainCaseByOfficeId(officeId));
			} else {
				segment.setTrainPNDisplay(true);
				segment.setTrainCase(CommonConstants.TRAIN_CASE_CONFIRM);
			}
		}
	}

	private MealDetail buildSelectedMeal(RetrievePnrMeal pnrMeal) {
		MealDetail mealDetail = null;
		if(pnrMeal != null){
			mealDetail = new MealDetail();
			mealDetail.setCompanyId(pnrMeal.getCompanyId());
			// special handling for LIQUID DIET meal code
			if(pnrMeal.getMealCode().contains(MealConstants.SPML_LIQUID_MEAL_FREETEXT)) {
				mealDetail.setMealCode(MealConstants.SPML_LIQUID_MEAL_CODE);
			}else {
				mealDetail.setMealCode(pnrMeal.getMealCode());
			}
			
 			mealDetail.setQuantity(pnrMeal.getQuantity());
 			mealDetail.setStatus(pnrMeal.getStatus());
 			mealDetail.setPreSelectedMeal(pnrMeal.isPreSelectedMeal());
		}
		return mealDetail;
	}
	
	private void buildMealSelection(Booking booking){	
		booking.getPassengerSegments().stream().forEach( ps -> {
			try {
				Segment segment = booking.getSegments().stream()
						.filter( s -> s.getSegmentID().equalsIgnoreCase(ps.getSegmentId()) )
						.findFirst().orElseThrow( () -> new RuntimeException() );
			
				MealSelection ms = new MealSelection();
				MealOptionKey key = new MealOptionKey(MMBConstants.APP_CODE, segment.getOriginPort(),
						segment.getDestPort(), segment.getCabinClass(), segment.getOperateCompany());

				ms.setMealOption(mealIneligibilityDao.findOne(key).map(option -> option.getMealOption())
						.orElseGet(() -> MealOption.MA));
				
				List<SpecialMeal> specialMeals = bookingBuildHelper.getSpecialMeals(MMBConstants.APP_CODE, segment.getOperateCompany(),
						segment.getCabinClass(), segment.getOriginPort(), segment.getDestPort());
				
				//  All meals are available for concession booking EXCEPT fruit platter meal (FPML)
				if (booking.isStaffBooking()) {
					specialMeals = specialMeals.stream().filter(meal -> meal != null && !MealConstants.FRUIT_PLATTER_MEAL.equals(meal.getType())).collect(Collectors.toList());
				}
				ms.setSpecialMeals(specialMeals);
				
				ps.setMealSelection(ms);
			} catch (RuntimeException e) {
				logger.error("Segment not found in booking", e);
			}
		});
	}

	/**
	 * 
	* @Description build seat selection
	* @param booking
	* @param pnrBooking
	* @return void
	* @author haiwei.jia
	 * @param tempSeats 
	 */
	private void buildSeatSelection(Booking booking, RetrievePnrBooking pnrBooking, List<TempSeat> tempSeats) {
		buildMmbSeatSelection(booking, pnrBooking);
		buildOlciSeatSelection(booking, pnrBooking, tempSeats);
	}

	/**
	 * build OLCI seat selection
	 * @param booking
	 * @param pnrBooking
	 * @param tempSeats 
	 */
	private void buildOlciSeatSelection(Booking booking, RetrievePnrBooking pnrBooking, List<TempSeat> tempSeats) {
		boolean mpoTopTierIsExist = mpoTopTierExist(booking.getPassengerSegments());
		boolean childExistInBooking = booking.getPassengers().stream().anyMatch(p->
				p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_CHILD)||
						p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_INS)
		     );
		boolean infantExistInBooking = booking.getPassengers().stream().anyMatch(p->p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_INF));
		Boolean companionAsrFOCAll = false;
		
		List<String> oneWorldTiers = bizRuleConfig.getOneworldTierLevel();
		List<String> cabinClassMap = cabinClassDAO.findCabinClassByDescriptionAndAppCode(MMBConstants.APP_CODE);
		List<TBSsrSkMapping> asrFocTbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndAsrFOC(MMBConstants.APP_CODE,TBConstants.ASR_FOC_YES);
		List<SeatRuleModel> seatRules = seatRuleDao.findAllByAppCodeAndAsrFOC(MMBConstants.APP_CODE,TBConstants.ASR_FOC_YES);
		List<String> seatRule = seatRules.stream().map(SeatRuleModel::getValue).collect(Collectors.toList());
		List<String> asrFocSsrSks = asrFocTbSsrSkMappings.stream().map(TBSsrSkMapping::getSsrSkCode)
				.collect(Collectors.toList());
		for(PassengerSegment passengerSegment : booking.getPassengerSegments()){
			if(passengerSegment == null){
				continue;
			}	
			RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), passengerSegment.getPassengerId());
			RetrievePnrPassengerSegment pnrPassengerSegment = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), passengerSegment.getPassengerId(), passengerSegment.getSegmentId());
			Segment segment = pnrPassengerSegment != null? getSegmentById(booking.getSegments(), pnrPassengerSegment.getSegmentId()): new Segment();

			boolean withEligibleCabinClass = withEligibleCabinClass(passengerSegment, booking.getSegments());
			boolean lowRBD = isLowRBD(passengerSegment, booking.getSegments());
			boolean xlEligible = specialSkOfExlExist(passengerSegment, pnrBooking);
			boolean isTopTier = passengerSegment.getFqtvInfo() != null && BooleanUtils.isTrue(passengerSegment.getFqtvInfo().isTopTier());
			boolean ticketIssuedOrDMP = isTicketIssuedOrDMP(passengerSegment);
			boolean asrFOC = olciCanAsrFOC(passengerSegment, booking.getSegments(), pnrBooking, mpoTopTierIsExist,
					lowRBD, oneWorldTiers, cabinClassMap, asrFocSsrSks, seatRule);
			boolean exlFOC = canExlFOC(xlEligible, isTopTier, ticketIssuedOrDMP);
			boolean cxKaOperated = operatedByCxKa(passengerSegment, booking.getSegments());
			boolean isWithDisability = withDisability(passengerSegment, pnrBooking);
			boolean enableASRForRedemptionBooking = enableASRForRedemptionBooking(booking, pnrBooking, passengerSegment, true);
			CprJourneyPassengerSegment cprPassengerSegment = getCprPassengerSegment(booking, passengerSegment.getPassengerId(), passengerSegment.getSegmentId());
			boolean standBy = cprPassengerSegment != null && cprPassengerSegment.isCheckInStandBy();
			
			// the booking is a staff booking and without any condition that can make him/her eligible to select seat
			boolean ineligibleStaffBooking = booking.isStaffBooking() && (!passengerSegment.isCheckedIn() || standBy);
			// segment is wait listed
			boolean segmentWaitListed = isWaitListedSegment(booking.getSegments(), passengerSegment);
			// the cabin is eligible or pax has privilege to select seat
			boolean cabinEligibleOrHavePrivilege = isCabinEligibleOrHavePrivilege(mpoTopTierIsExist, childExistInBooking, infantExistInBooking, isOneWorldTier(pnrPassengerSegment, pnrPassenger), withEligibleCabinClass, enableASRForRedemptionBooking);
			
			SeatSelection seatSelection = new SeatSelection();
			seatSelection.setLowRBD(lowRBD);
			// Paid ASR
			if(BooleanUtils.isTrue(seatSelection.isLowRBD())) {
				seatSelection.setPaidASR(isPaidASR(pnrPassengerSegment, pnrBooking.getFaList()));
			}
			//eligible to select seat
			if (cxKaOperated && !isWithDisability && !booking.isGroupBooking() && !ineligibleStaffBooking
					&& !segmentWaitListed && cabinEligibleOrHavePrivilege && !BookingBuildUtil.isMiceGRMB(pnrBooking.getSkList()) && !passengerSegment.isInhibitChangeSeat()) {
				seatSelection.setEligible(true);
				// check whether the flight is eligible for displaying or submitting seat preference form
				seatSelection.setIsSeatPreferenceEligible(true);
				//set eligibility for special seat
				seatSelection.setSpecialSeatEligibility(checkSpecialSeatEligibility(seatSelection,passengerSegment, pnrBooking, booking, infantExistInBooking, childExistInBooking, asrFOC, exlFOC, tempSeats));
			}
			else {// ineligible to select seat
				seatSelection.setEligible(false);
				// check whether the flight is eligible for displaying or submitting seat preference form
				seatSelection.setIsSeatPreferenceEligible(segment == null || isSeatPreferenceEligible(segment.getOperateCompany()));
				// set ineligible reason
                setOlciIneligibleReason(cxKaOperated, isWithDisability, booking, standBy, ineligibleStaffBooking,
                        segmentWaitListed, cabinEligibleOrHavePrivilege,
                        BookingBuildUtil.isMiceGRMB(pnrBooking.getSkList()), passengerSegment.isInhibitChangeSeat(),
                        seatSelection);
				//populate disabilities to seatSelection
				populateDisabilities(passengerSegment, pnrBooking, seatSelection);
			}
			Boolean companionAsrFOC = populateCompanionAsrFOC(passengerSegment, pnrBooking);
			if(companionAsrFOC) {
				companionAsrFOCAll = companionAsrFOC;
			}
			seatSelection.setAsrFOC(asrFOC);
			//check and set eligibility for EXL FOC
			checkExlFOC(seatSelection, exlFOC, isTopTier);           
			// if AsrSeat and ExlSeat are false ,the eligible is false
			if(!BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getAsrSeat()) && !BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getExlSeat())){
            	seatSelection.setEligible(false);
            }
			passengerSegment.setOlciSeatSelection(seatSelection);
		}
		if(companionAsrFOCAll) {
			for(PassengerSegment passengerSegment : booking.getPassengerSegments()){
				if(passengerSegment == null || null == passengerSegment.getOlciSeatSelection()){
					continue;
				}
				if(passengerSegment.getOlciSeatSelection().isEligible()){
					passengerSegment.getOlciSeatSelection().setAsrFOC(true);
				}
			}
		}
	}

	/**
	 * set olci ineligible reason
	 * @param cxKaOperated
	 * @param isWithDisability
	 * @param standBy 
	 * @param booking 
	 * @param cabinEligibleOrHavePrivilege 
	 * @param segmentWaitListed 
	 * @param cabinEligibleOrHavePrivilege 
	 * @param seatSelection
	 */
	private void setOlciIneligibleReason(boolean cxKaOperated, boolean isWithDisability, Booking booking,
			boolean standBy, boolean ineligibleStaffBooking, boolean segmentWaitListed,
			boolean cabinEligibleOrHavePrivilege, boolean grmb, boolean inhibitChangeSeat, SeatSelection seatSelection) {
	    if(grmb){
            seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.MICE_GRMB);
        } else if (!cxKaOperated) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.NON_CXKA);
		} else if (booking.isStaffBooking() && standBy) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.STAND_BY);
		} else if (booking.isGroupBooking()) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.GROUP_BOOKING);
		} else if (ineligibleStaffBooking) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.STAFF_BOOKING);
		} else if (segmentWaitListed) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.SEGMENT_WAITLISTED);
		} else if (!cabinEligibleOrHavePrivilege) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.INELIGIBLE_CABIN);
		} else if (inhibitChangeSeat) {
            seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.OLCI_INHIBIT_CHANGESEAT);
        } else if (isWithDisability) { // this must be checked last to make sure when DISABILITY is return, all of other check has passed which is used in mbseatservice
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.DISABILITY);
		}
	}

	/**
	 * get cprPassengerSegment by passengerId and segmentId
	 * @param booking
	 * @param passengerId
	 * @param segmentId
	 * @return 
	 */
	private CprJourneyPassengerSegment getCprPassengerSegment(Booking booking, String passengerId, String segmentId) {
		if (booking == null || CollectionUtils.isEmpty(booking.getCprJourneys()) || StringUtils.isEmpty(passengerId)
				|| StringUtils.isEmpty(segmentId)) {
			return null;
		}
		
		return booking.getCprJourneys().stream().filter(j -> !CollectionUtils.isEmpty(j.getPassengerSegments()) && j.getPassengerSegments().stream().anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && segmentId.equals(ps.getSegmentId())))
				.map(j -> j.getPassengerSegments().stream().filter(ps -> passengerId.equals(ps.getPassengerId()) && segmentId.equals(ps.getSegmentId())).findFirst().orElse(null)).findFirst().orElse(null);
	}

	/**
	 * check if can select ASR seat for free according to OLCI rule
	 * @param passengerSegment
	 * @param segments
	 * @param pnrBooking
	 * @param mpoTopTierIsExist
	 * @param lowRBD 
	 * @param seatRule 
	 * @param asrFocSsrSks 
	 * @param cabinClassMap 
	 * @param oneWorldTiers 
	 * @return boolean
	 */
	private boolean olciCanAsrFOC(PassengerSegment passengerSegment, List<Segment> segments,
			RetrievePnrBooking pnrBooking, boolean mpoTopTierIsExist, boolean lowRBD, List<String> oneWorldTiers,
			List<String> cabinClassMap, List<String> asrFocSsrSks, List<String> seatRule) {
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();	
		// allow WCHR/WCHS/WCBD/WCBW/WCMP/WCOB to select regular seat for free
		if (hasEligibleWcSsrOfAsrFoc(passengerSegment, pnrBooking)) {
			return true;
		}
		
		// MPO Silver/ Gold/ Diamond/ Diamond Plus/Invitation
		if(mpoTopTierIsExist){
			return true;
		}
		// OW Ruby/ Sapphire/ Emerald
		if(bookingWithOneWorld(passengerSegment,oneWorldTiers)){
			return true;
		}
		// check RBD eligible
		if(checkRBDForOlciFoc(passengerSegment.getSegmentId(), segments, seatRule, pnrBooking, cabinClassMap, lowRBD)){
			return true;
		}
		// Pax is not entitled to wavier and Determine if SK ASWR exist,if exist return true
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())){
			for (RetrievePnrDataElements sk : pnrBooking.getSkList()) {
				if (passengerSegment.getSegmentId().equals(sk.getSegmentId()) && passengerSegment.getPassengerId().equals(sk.getPassengerId()) && asrFocSsrSks.contains(sk.getType())){
					return true;
				}
			 }		    
		}		
		// Not Family with infants/ children
		return checkFamilyWithInfants(pnrPassengers,pnrBooking);
	}

	/**
	 * build MMB seat selection
	 * @param booking
	 * @param pnrBooking
	 */
	private void buildMmbSeatSelection(Booking booking, RetrievePnrBooking pnrBooking) {
		boolean mpoTopTierIsExist = mpoTopTierExist(booking.getPassengerSegments());
		boolean childExistInBooking = booking.getPassengers().stream().anyMatch(p->
				p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_CHILD)||
						p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_INS)
		     );
		boolean infantExistInBooking = booking.getPassengers().stream().anyMatch(p->p.getPassengerType().equalsIgnoreCase(OneAConstants.PASSENGER_TYPE_INF));
		Boolean companionAsrFOCAll = false;
		
		List<String> oneWorldTiers = bizRuleConfig.getOneworldTierLevel();
		List<String> cabinClassMap = cabinClassDAO.findCabinClassByDescriptionAndAppCode(MMBConstants.APP_CODE);
		List<TBSsrSkMapping> asrFocTbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndAsrFOC(MMBConstants.APP_CODE,TBConstants.ASR_FOC_YES);
		List<String> asrFocSsrSks = asrFocTbSsrSkMappings.stream().map(TBSsrSkMapping::getSsrSkCode)
				.collect(Collectors.toList());
		List<TBSsrSkMapping> selectedAsrFocTbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndSelectedAsrFOC(MMBConstants.APP_CODE, TBConstants.SELECTED_ASR_FOC_YES);
		List<String> selectedAsrFocSsrSks = selectedAsrFocTbSsrSkMappings.stream().map(TBSsrSkMapping::getSsrSkCode)
				.collect(Collectors.toList());
		List<SeatRuleModel> seatRules = seatRuleDao.findAllByAppCodeAndAsrFOC(MMBConstants.APP_CODE,TBConstants.ASR_FOC_YES);
		List<String> seatRule = seatRules.stream().map(SeatRuleModel::getValue).collect(Collectors.toList());
		
		for(PassengerSegment passengerSegment : booking.getPassengerSegments()){
			if(passengerSegment == null){
				continue;
			}	
			RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), passengerSegment.getPassengerId());
			RetrievePnrPassengerSegment pnrPassengerSegment = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), passengerSegment.getPassengerId(), passengerSegment.getSegmentId());
			// for linked booking we can not find pnr info
			if(null == pnrPassenger || null == pnrPassengerSegment) {
			    continue;
			}

			Segment segment = getSegmentById(booking.getSegments(), pnrPassengerSegment.getSegmentId());
			// get the white listed TPOS list from the database
			List<AsrEnableCheck> whiteListedTPOSList = getTPOSListFromDB(pnrBooking);

			boolean withEligibleCabinClass = withEligibleCabinClass(passengerSegment, booking.getSegments());
			boolean lowRBD = isLowRBD(passengerSegment, booking.getSegments());
			boolean xlEligible = specialSkOfExlExist(passengerSegment, pnrBooking);
			boolean isTopTier = passengerSegment.getFqtvInfo() != null && BooleanUtils.isTrue(passengerSegment.getFqtvInfo().isTopTier());
			boolean ticketIssuedOrDMP = isTicketIssuedOrDMP(passengerSegment);
			boolean allSegmentsConfirmed = allSegmentsConfirmed(booking.getSegments());
			boolean asrFOC = mmbCanAsrFOC(passengerSegment, booking.getSegments(), pnrBooking, mpoTopTierIsExist,
					whiteListedTPOSList, oneWorldTiers, cabinClassMap, asrFocSsrSks, seatRule);
			boolean exlFOC = canExlFOC(xlEligible, isTopTier, ticketIssuedOrDMP);
			boolean cxKaOperated = operatedByCxKa(passengerSegment, booking.getSegments());
			boolean isWithDisability = withDisability(passengerSegment, pnrBooking);
			boolean enableASRForRedemptionBooking = enableASRForRedemptionBooking(booking, pnrBooking, passengerSegment, false);
			/** the selected ASR is free and can only change to same type of ASR freely
			 *  e.g. if there's SK ASMR found in the booking which means the seat is paid by miles, the selected ASR is free and
			 * 		passenger can change to seats of the same type
			 */
			boolean selectedAsrFoc = isSelectedAsrFoc(pnrBooking, selectedAsrFocSsrSks, passengerSegment.getPassengerId(), passengerSegment.getSegmentId());
			
			// the booking is a staff booking and without any condition that can make him/her eligible to select seat
			boolean ineligibleStaffBooking = booking.isStaffBooking() && !(allSegmentsConfirmed && (childExistInBooking || infantExistInBooking));
			// segment is wait listed
			boolean segmentWaitListed = isWaitListedSegment(booking.getSegments(), passengerSegment);
			// the cabin is eligible or pax has privilege to select seat
			boolean cabinEligibleOrHavePrivilege = isCabinEligibleOrHavePrivilege(mpoTopTierIsExist, childExistInBooking, infantExistInBooking, isOneWorldTier(pnrPassengerSegment, pnrPassenger), withEligibleCabinClass, enableASRForRedemptionBooking);
			// check whether the seat selection is available if it is a redemption booking
			boolean canSelectSeatForRedemption = booking.isRedemptionBooking() && (whiteListedTPOSList != null && whiteListedTPOSList.stream().allMatch(result -> result != null && result.isSeatSelection()));
			
			SeatSelection seatSelection = new SeatSelection();
			seatSelection.setLowRBD(lowRBD);
			// Paid ASR
			if(BooleanUtils.isTrue(seatSelection.isLowRBD())) {
				seatSelection.setPaidASR(isPaidASR(pnrPassengerSegment, pnrBooking.getFaList()));
			}
			//eligible to select seat
			if (cxKaOperated
					&& !isWithDisability
					&& !booking.isGroupBooking()
					&& !ineligibleStaffBooking && !segmentWaitListed
					&& cabinEligibleOrHavePrivilege
					&& !BookingBuildUtil.isMiceGRMB(pnrBooking.getSkList())
					&& (!booking.isRedemptionBooking() || canSelectSeatForRedemption)) {
				seatSelection.setEligible(true);

				seatSelection.setIsSeatPreferenceEligible(true);

				//set eligibility for special seat
				seatSelection.setSpecialSeatEligibility(checkSpecialSeatEligibility(seatSelection,passengerSegment, pnrBooking, booking, infantExistInBooking, childExistInBooking, asrFOC, exlFOC, null));
			}
			else {// ineligible to select seat
				seatSelection.setEligible(false);

				// check whether the flight is eligible for displaying or submitting seat preference form
				seatSelection.setIsSeatPreferenceEligible(segment == null || isSeatPreferenceEligible(segment.getOperateCompany()));

				// set ineligible reason
				setMmbIneligibleReason(cxKaOperated, isWithDisability, booking.isGroupBooking(),ineligibleStaffBooking, segmentWaitListed, cabinEligibleOrHavePrivilege,BookingBuildUtil.isMiceGRMB(booking.getSkList()), seatSelection);
				//populate disabilities to seatSelection
				populateDisabilities(passengerSegment, pnrBooking, seatSelection);
			}
			Boolean companionAsrFOC = populateCompanionAsrFOC(passengerSegment, pnrBooking);
			if(companionAsrFOC) {
				companionAsrFOCAll = companionAsrFOC;
			}
			seatSelection.setAsrFOC(asrFOC);
			seatSelection.setSelectedAsrFOC(selectedAsrFoc);
			//check and set eligibility for EXL FOC
			checkExlFOC(seatSelection, exlFOC, isTopTier);           
			// if AsrSeat and ExlSeat are false ,the eligible is false
			if(!BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getAsrSeat()) && !BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getExlSeat())){
            	seatSelection.setEligible(false);
            }
			passengerSegment.setMmbSeatSelection(seatSelection);
		}
		if(companionAsrFOCAll) {
			for(PassengerSegment passengerSegment : booking.getPassengerSegments()){
				if(passengerSegment == null || null == passengerSegment.getMmbSeatSelection()){
					continue;
				}
				if(passengerSegment.getMmbSeatSelection().isEligible()){
					passengerSegment.getMmbSeatSelection().setAsrFOC(true);
				}
			}
		}
	}

	/**
	 * check if the selected ASR should be free 
	 * 
	 * @param pnrBooking
	 * @param selectedAsrFocSsrSks 
	 * @param segmentId 
	 * @param passengerId 
	 * @return boolean
	 */
	private boolean isSelectedAsrFoc(RetrievePnrBooking pnrBooking, List<String> selectedAsrFocSsrSks, String passengerId, String segmentId) {
		if (pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getSkList()) || CollectionUtils.isEmpty(selectedAsrFocSsrSks)
				|| StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)) {
			return false;
		}

		for (RetrievePnrDataElements sk : pnrBooking.getSkList()) {
			if (passengerId.equals(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()) && selectedAsrFocSsrSks.contains(sk.getType())){
				return true;
			}
		}
		return false;
	}

	/**
	 * check if the cabin class is eligible or there's privilege of the passenger to select seat
	 * @param mpoTopTierIsExist
	 * @param childExistInBooking
	 * @param infantExistInBooking
	 * @param pnrPassenger
	 * @param pnrPassengerSegment
	 * @param withEligibleCabinClass
	 * @param enableASRForRedemptionBooking
	 * @return boolean
	 */
	private boolean isCabinEligibleOrHavePrivilege(boolean mpoTopTierIsExist, boolean childExistInBooking,
			boolean infantExistInBooking, boolean isOneWorldTier, boolean withEligibleCabinClass,
			boolean enableASRForRedemptionBooking) {
		return withEligibleCabinClass || mpoTopTierIsExist || isOneWorldTier || childExistInBooking
				|| infantExistInBooking || enableASRForRedemptionBooking;
	}

	/**
	 * set MMB ineligible reason
	 * @param cxKaOperated
	 * @param isWithDisability
	 * @param cabinEligibleOrHavePrivilege 
	 * @param segmentWaitListed 
	 * @param ineligibleStaffBooking 
	 * @param groupBooking 
	 * @param seatSelection
	 */
	private void setMmbIneligibleReason(boolean cxKaOperated, boolean isWithDisability, boolean groupBooking, boolean ineligibleStaffBooking, boolean segmentWaitListed, boolean cabinEligibleOrHavePrivilege, boolean grmb, SeatSelection seatSelection) {
	    if(grmb) {
            seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.MICE_GRMB);
        } else if (!cxKaOperated) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.NON_CXKA);
		} else if(groupBooking) { 
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.GROUP_BOOKING);
		} else if (ineligibleStaffBooking) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.STAFF_BOOKING);
		} else if (segmentWaitListed) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.SEGMENT_WAITLISTED);
		} else if (!cabinEligibleOrHavePrivilege) {
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.INELIGIBLE_CABIN);
		} else if(isWithDisability) { // this must be checked last to make sure when DISABILITY is return, all of other check has passed which is used in mbseatservice
			seatSelection.setIneligibleReason(SeatSelectionIneligibleReason.DISABILITY);
		}
	}

	/**
	 * check if the segment of the passengerSegment is waitListed
	 * @param segments
	 * @param passengerSegment
	 * @return boolean
	 */
	private boolean isWaitListedSegment(List<Segment> segments, PassengerSegment passengerSegment) {
		if(passengerSegment == null || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		// segment related to the passengerSegment
		Segment segment = segments.stream().filter(seg -> !StringUtils.isEmpty(seg.getSegmentID()) && seg.getSegmentID().equals(passengerSegment.getSegmentId())).findFirst().orElse(null);
		if(segment == null || segment.getSegmentStatus() == null || segment.getSegmentStatus().getStatus() == null) {
			return false;
		} else {
			return FlightStatusEnum.WAITLISTED.getCode().equals(segment.getSegmentStatus().getStatus().getCode());
		}
	}
	
	private boolean allSegmentsConfirmed(List<Segment> segments) {
        if (segments != null) {
            return  segments.stream().allMatch(segment ->segment.getSegmentStatus()!=null
                    && segment.getSegmentStatus().getStatus()!= null
                    && segment.getSegmentStatus().getStatus().equals(FlightStatusEnum.CONFIRMED)
            );
        }
        return false;
    }
	
	/**
	 * Ticket issued or is DMP member
	 * @param passengerSegment
	 * @return
	 */
	private boolean isTicketIssuedOrDMP(PassengerSegment passengerSegment) {
		if(passengerSegment == null){
			return false;
		}
		boolean isDMP = false;
		if(passengerSegment.getFqtvInfo() != null && OneAConstants.DMP.equals(passengerSegment.getFqtvInfo().getTierLevel())){
			isDMP = true;
		}

		return !StringUtils.isEmpty(passengerSegment.getEticketNumber()) || isDMP;
	}


	/**
	 * check whether the flight operation company can display and submit the seat preference form
	 * @param operationCompany
	 * @return
	 */
    public boolean isSeatPreferenceEligible(String operationCompany) {
    	if (operationCompany != null) {
    		return bizRuleConfig.getIneligibleSeatPreferenceAirlineCodes().stream().noneMatch(airlineCode -> airlineCode.equals(operationCompany));
		}
    	return false;
	}


	/**
	 * check if passenger can choose EXL for free and set value for xlFOC and xlFOCReason
	 * @param seatSelection
	 * @param passengerSegment
	 * @param xlEligible
	 * @param isTopTier
	 * @param hasAssociatedPayment
	 * @param ticketIssuedOrDMP
	 */
	private void checkExlFOC(SeatSelection seatSelection, boolean exlFOC, boolean isTopTier) {
		if (exlFOC) {
			seatSelection.setXlFOC(true);
			// set the reason why passenger can choose EXL for free
			if(isTopTier) {
				seatSelection.setXlFOCReason(XLFOCReasonEnum.IS_TOP_TIER.getReason());
			} else {
				seatSelection.setXlFOCReason(XLFOCReasonEnum.SPECIAL_SK_FOUND.getReason());
			}
		}
	}
	
	/**
	 * check if can choose EXL for free
	 * @param xlEligible
	 * @param isTopTier
	 * @param ticketIssuedOrDMP
	 * @return
	 */
	private boolean canExlFOC(boolean xlEligible, boolean isTopTier, boolean ticketIssuedOrDMP) {
		return (xlEligible || isTopTier) && ticketIssuedOrDMP;
	}

	/**
	 * check if the cabin subclass is low RBD
	 * @param passengerSegment
	 * @param segments
	 * @return boolean
	 */
	private boolean isLowRBD(PassengerSegment passengerSegment, List<Segment> segments) {
		if(passengerSegment == null || CollectionUtils.isEmpty(segments)){
			return false;
		}
		//the segment related to passegerSegment
		Segment segment = getSegmentById(segments, passengerSegment.getSegmentId());
		if(segment == null){
			return false;
		}
		
		//if cabin class is first class or business class, then return true
		if(MMBBizruleConstants.CABIN_CLASS_FIRST_CLASS.equals(segment.getCabinClass()) || MMBBizruleConstants.CABIN_CLASS_BUSINESS_CLASS.equals(segment.getCabinClass())){
			return false;
		} else{
			List<String> lowRBDs = seatRuleService.getLowRBD();
			if(CollectionUtils.isEmpty(lowRBDs)){
				return false;
			}
			
			return lowRBDs.contains(segment.getSubClass());
		}
	}

	/**
	 * check if can select ASR seat for free according to MMB rule
	 * @param passengerSegment
	 * @param segments
	 * @param pnrBooking
	 * @param mpoTopTierIsExist
	 * @param seatRule 
	 * @param asrFocSsrSks 
	 * @param cabinClassMap 
	 * @param oneWorldTiers 
	 * @return boolean
	 */
	private boolean mmbCanAsrFOC(PassengerSegment passengerSegment, List<Segment> segments,
			RetrievePnrBooking pnrBooking, boolean mpoTopTierIsExist, List<AsrEnableCheck> whiteListedTPOSList,
			List<String> oneWorldTiers, List<String> cabinClassMap, List<String> asrFocSsrSks,
			List<String> seatRule) {

		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();	
		// allow WCHR/WCHS/WCBD/WCBW/WCMP/WCOB to select regular seat for free
		if (hasEligibleWcSsrOfAsrFoc(passengerSegment, pnrBooking)) {
			return true;
		}
		
		// MPO Silver/ Gold/ Diamond/ Diamond Plus/Invitation
		if(mpoTopTierIsExist){
			return true;
		}
		// OW Ruby/ Sapphire/ Emerald
		if(bookingWithOneWorld(passengerSegment,oneWorldTiers)){
			return true;
		}
		// check RBD eligible
		if(checkRBDForFoc(passengerSegment.getSegmentId(), segments, seatRule, pnrBooking, cabinClassMap, whiteListedTPOSList)){
			return true;
		}

		// Pax is not entitled to wavier and Determine if SK ASWR exist,if exist return true
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())){
			for (RetrievePnrDataElements sk : pnrBooking.getSkList()) {
				if (passengerSegment.getSegmentId().equals(sk.getSegmentId()) && passengerSegment.getPassengerId().equals(sk.getPassengerId()) && asrFocSsrSks.contains(sk.getType())){
					return true;
				}
			 }		    
		}		
		// Not Family with infants/ children
		return checkFamilyWithInfants(pnrPassengers,pnrBooking);
	}

	private boolean bookingWithOneWorld(PassengerSegment passengerSegment, List<String> oneWorldTiers) {
		if(StringUtils.isEmpty(passengerSegment.getFqtvInfo().getTierLevel())){
			return false;		
		}		
		return passengerSegment.getFqtvInfo() != null
				&& oneWorldTiers.contains(passengerSegment.getFqtvInfo().getTierLevel());
	}
	
	private boolean checkRBDForFoc(String segmentID, List<Segment> segments, List<String> seatRule, RetrievePnrBooking pnrBooking, List<String> cabinClassMap, List<AsrEnableCheck> whiteListedTPOSList) {
		if(CollectionUtils.isEmpty(segments)){
			return false;
		}
		
		for (Segment segment : segments) {
			if ((segment.getSegmentID().equals(segmentID) && (seatRule.contains(segment.getSubClass()) || cabinClassMap.contains(segment.getCabinClass())))
					|| (OneAConstants.ASR_RBD_X.equals(segment.getSubClass()) && whiteListedTPOSList != null && whiteListedTPOSList.stream().allMatch(record -> record != null && record.isFoc()))) {
				return true;
			}
		 }		
		return false;
	}
	
	/**
	 * check if the RBD is eligble top select ASR for free according to OLCi rules
	 * @param segmentID
	 * @param segments
	 * @param seatRule
	 * @param pnrBooking
	 * @param cabinClassMap
	 * @param lowRBD 
	 * @return boolean
	 */
	private boolean checkRBDForOlciFoc(String segmentID, List<Segment> segments, List<String> seatRule, RetrievePnrBooking pnrBooking, List<String> cabinClassMap, boolean lowRBD) {
		if(CollectionUtils.isEmpty(segments)){
			return false;
		}
		
		// allow low RBD to select ASR for free in OLCI
		if (lowRBD) {
			return true;
		}
		
		for (Segment segment : segments) {
			if ((segment.getSegmentID().equals(segmentID) && (seatRule.contains(segment.getSubClass()) || cabinClassMap.contains(segment.getCabinClass()))) || (segment.getSubClass().equals(OneAConstants.ASR_RBD_X))) {
				return true;
			}
		 }		
		return false;
	}
	
	private boolean isOfficeIdAndTOPSFOC(RetrievePnrBooking pnrBooking) {
		List<String> skList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())) {
			for(RetrievePnrDataElements sk : pnrBooking.getSkList()) {
				if(OneAConstants.TPOS.equals(sk.getType())) {
					skList.add(sk.getFreeText());
				}
			}
		}
		String tposQuery = StringUtils.join(skList, ",");
		List<AsrEnableCheck> results = asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, pnrBooking.getOfficeId(), tposQuery);
		return Optional.ofNullable(results).orElse(Collections.emptyList()).stream().anyMatch(record -> record != null && record.isFoc());
	}


	private List<AsrEnableCheck> getTPOSListFromDB(RetrievePnrBooking pnrBooking) {
		List<String> skList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())) {
			for(RetrievePnrDataElements sk : pnrBooking.getSkList()) {
				if(OneAConstants.TPOS.equals(sk.getType())) {
					skList.add(sk.getFreeText());
				}
			}
		}
		String tposQuery = StringUtils.join(skList, ",");
		List<AsrEnableCheck> results = asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, pnrBooking.getOfficeId(), tposQuery);
		return Optional.ofNullable(results).orElse(Collections.emptyList());
	}

	private boolean checkFamilyWithInfants(List<RetrievePnrPassenger> pnrPassengers, RetrievePnrBooking pnrBooking) {
		if(CollectionUtils.isEmpty(pnrPassengers)){
			return false;
		}
		if(familyWithBSCT(pnrBooking)){
			return true;
		}
		for (RetrievePnrPassenger passenger : pnrPassengers) {
			if (passenger != null &&(OneAConstants.PASSENGER_TYPE_INF.equals(passenger.getPassengerType())
					|| OneAConstants.PASSENGER_TYPE_CHILD.equals(passenger.getPassengerType())
					|| OneAConstants.PASSENGER_TYPE_INS.equals(passenger.getPassengerType()))) {
				return true;
			}
		  }
		return false;
	}


	private boolean familyWithBSCT(RetrievePnrBooking pnrBooking) {
		if(CollectionUtils.isEmpty(pnrBooking.getSsrList())){
			return false;
		}
		for(RetrievePnrDataElements ssr:pnrBooking.getSsrList()){
			if(OneAConstants.PASSENGER_TYPE_BSCT.equals(ssr.getType())){
				return true;
			}
		}
		return false;
	}

	/**
	 * check if passenger is a WCHR/WCHS
	 * @param passengerSegment
	 * @param booking
	 * @return boolean
	 */
	private boolean isWCHROrWCHS(PassengerSegment passengerSegment, RetrievePnrBooking booking) {
		if(passengerSegment == null || StringUtils.isEmpty(passengerSegment.getPassengerId()) 
				|| StringUtils.isEmpty(passengerSegment.getSegmentId())
				|| booking == null || CollectionUtils.isEmpty(booking.getSsrList())) {
			return false;
		}
		// passenger id of the passenger
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		return booking.getSsrList().stream()
				.anyMatch(ssr -> ssr != null
						&& (OneAConstants.SSR_TYPE_WCHR.equals(ssr.getType())
								|| OneAConstants.SSR_TYPE_WCHS.equals(ssr.getType()))
						&& ((passengerId.equals(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))
								|| (passengerId.equals(ssr.getPassengerId()) && StringUtils.isEmpty(ssr.getSegmentId()))
								|| (StringUtils.isEmpty(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))));
	}
	
	/**
	 * check if has WCHR/WCHS/WCBD/WCBW/WCMP/WCOB which makes the pax eligible to select regular seat for free
	 * @param passengerSegment
	 * @param booking
	 * @return boolean
	 */
	private boolean hasEligibleWcSsrOfAsrFoc(PassengerSegment passengerSegment, RetrievePnrBooking booking) {
		if(passengerSegment == null || StringUtils.isEmpty(passengerSegment.getPassengerId()) 
				|| StringUtils.isEmpty(passengerSegment.getSegmentId()) 
				|| booking == null || CollectionUtils.isEmpty(booking.getSsrList())) {
			return false;
		}
		// passenger id of the passenger
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		
		return booking.getSsrList().stream()
				.anyMatch(ssr -> ssr != null
						&& MMBBizruleConstants.ELIGIBLE_WC_SSR_LIST_OF_ASR_FOC.contains(ssr.getType())
						&& ((passengerId.equals(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))
								|| (passengerId.equals(ssr.getPassengerId()) && StringUtils.isEmpty(ssr.getSegmentId()))
								|| (StringUtils.isEmpty(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))));
		}

	/**
	 * ASR enable check, check if the booking is redemption with subclass X AND the office id or tpos is configured in DB
	 * @param booking
	 * @return
	 */
	private boolean enableASRForRedemptionBooking(Booking booking, RetrievePnrBooking pnrBooking, PassengerSegment passengerSegment, boolean isOlciFlow) {
		boolean enableASRForRedemptionBooking = false;
		boolean redemptionBookingWithSubclassX = redemptionBookingWithSubclassX(booking, passengerSegment);
		if(redemptionBookingWithSubclassX) {
			List<String> skList = new ArrayList<>();
			if(!CollectionUtils.isEmpty(pnrBooking.getSkList())) {
				for(RetrievePnrDataElements sk : pnrBooking.getSkList()) {
					if(OneAConstants.TPOS.equals(sk.getType())) {
						skList.add(sk.getFreeText());
					}
				}
			}
			String tposQuery = StringUtils.join(skList, ",");
			List<AsrEnableCheck> results = asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, booking.getOfficeId(), tposQuery);
			enableASRForRedemptionBooking = !CollectionUtils.isEmpty(results) && results.stream().anyMatch(AsrEnableCheck::isSeatSelection);
		}
		passengerSegment.setEnableASRForRedemptionBooking(enableASRForRedemptionBooking);
		return isOlciFlow ? redemptionBookingWithSubclassX : enableASRForRedemptionBooking;
	}
	
	/**
	 * Check if the booking is redemption booking and with sub class X
	 * @param booking
	 * @return
	 */
	private boolean redemptionBookingWithSubclassX(Booking booking,PassengerSegment passengerSegment) {
		boolean subClassX = false;
		if(!CollectionUtils.isEmpty(booking.getSegments())) {
			for(Segment segment : booking.getSegments()) {
				if (StringUtils.equals(passengerSegment.getSegmentId(), segment.getSegmentID()) && MMBBizruleConstants.ASR_ENABLE_SUBCLASS_X.equals(segment.getSubClass())) {
					subClassX = true;
					break;
				}
			}
		}
		return subClassX && booking.isRedemptionBooking();
	}
	/**
	 * 
	* @Description judge if there is SK XLWR, SK XLMR or SK XLGR MCO related to the passengerSegment
	* @param passengerSegment
	* @param pnrBooking
	* @return Boolean
	* @author haiwei.jia
	 */
	private Boolean specialSkOfExlExist(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking) {
		if(passengerSegment == null || pnrBooking == null){
			return false;
		}
		
		//passenger id and segment id in the passengerSegment
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		
		//judge if there is SK XLWR, SK XLMR or SK XLGR MCO in pnrPassengerSegment or pnrPassenger or pnrSegment
		return haveSpecialSkOfExl(pnrBooking, passengerId, segmentId);
	}

	/**
	 * 
	* @Description judge if there is SK XLWR, SK XLMR or SK XLGR MCO in skList
	* @param skList
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean haveSpecialSkOfExl(RetrievePnrBooking pnrBooking, String passengerId, String segmentId) {
		if(pnrBooking == null || StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)|| CollectionUtils.isEmpty(pnrBooking.getSkList())){
			return false;
		}

		// find SKs by passengerId and segmentId
		List<RetrievePnrDataElements> sks = parseSKListByPassengerIdAndSegmentId(pnrBooking,passengerId,segmentId);
		
		if(CollectionUtils.isEmpty(sks)){
			return false;
		}
		
		for(RetrievePnrDataElements sk : sks){
			if(sk == null){
				continue;
			}
			if(OneAConstants.SK_TYPE_XLWR.equals(sk.getType()) 
					|| (OneAConstants.SK_TYPE_XLMR.equals(sk.getType()) && FreeTextUtil.isXLMRMCO(sk.getFreeText()))){
				return true;
			}
		}
		return false;
	}


	private List<RetrievePnrDataElements> parseSKListByPassengerIdAndSegmentId(RetrievePnrBooking pnrBooking, String passengerId, String segmentId) {
		
		return pnrBooking.getSkList().stream().filter(sk -> {
			if (sk == null) {
				return false;
			}
			return (StringUtils.isEmpty(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()))
					|| StringUtils.isEmpty(sk.getPassengerId()) && StringUtils.isEmpty(sk.getSegmentId())
					|| (passengerId.equals(sk.getPassengerId()) && StringUtils.isEmpty(sk.getSegmentId()))
					|| (passengerId.equals(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()));
		}).collect(Collectors.toList());
	}


	/**
	 * 
	* @Description populate disabilities to seatSelection
	* @param passengerSegment
	* @param pnrBooking
	* @param seatSelection
	* @return void
	* @author haiwei.jia
	 */
	private void populateDisabilities(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking,
			SeatSelection seatSelection) {
		if(passengerSegment == null || pnrBooking == null || seatSelection == null){
			return;
		}
		
		List<TBSsrSkMapping> tbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndSeat(MMBConstants.APP_CODE,TBConstants.SEAT_SELECTION_INHIBIT);
		if(CollectionUtils.isEmpty(tbSsrSkMappings)){
			return;
		}
		
		List<String> ineligibleSsrSks = tbSsrSkMappings.stream().filter(tbSsrSkMapping -> tbSsrSkMapping != null)
				.map(TBSsrSkMapping :: getSsrSkCode).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(ineligibleSsrSks)){
			return;
		}
		
		//passenger id and segment id in the passengerSegment
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		
		if(StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)){
			return;
		}
		
		List<RetrievePnrDataElements> relatedSsrSks = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(pnrBooking.getSsrList())){
			// add related Ssrs
			relatedSsrSks.addAll(pnrBooking.getSsrList().stream().filter(ssr -> {
				if (ssr == null) {
					return false;
				}
				return (StringUtils.isEmpty(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && StringUtils.isEmpty(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()));
			}).collect(Collectors.toList()));
		}
		
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())){
			//add related SKs
			relatedSsrSks.addAll(pnrBooking.getSkList().stream().filter(sk -> {
				if (sk == null) {
					return false;
				}
				return (StringUtils.isEmpty(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()))
						|| (passengerId.equals(sk.getPassengerId()) && StringUtils.isEmpty(sk.getSegmentId()))
						|| (passengerId.equals(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()));
			}).collect(Collectors.toList()));
		}
		
		if(CollectionUtils.isEmpty(relatedSsrSks)){
			return;
		}
		populateIneligibleSsrSk(relatedSsrSks, ineligibleSsrSks, seatSelection);
	}
	
	private Boolean populateCompanionAsrFOC(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking) {
		if(passengerSegment == null || pnrBooking == null){
			return false;
		}
		List<TBSsrSkMapping> tbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndCompanionAsrFOC(MMBConstants.APP_CODE,TBConstants.COMPANION_ASR_FOC_YES);
		if(CollectionUtils.isEmpty(tbSsrSkMappings)){
			return false;
		}
		List<String> companionAsrFOCSsr = tbSsrSkMappings.stream().filter(tbSsrSkMapping -> tbSsrSkMapping != null)
				.map(TBSsrSkMapping :: getSsrSkCode).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(companionAsrFOCSsr)){
			return false;
		}
		
		//passenger id and segment id in the passengerSegment
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		
		if(StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)){
			return false;
		}
		
		List<RetrievePnrDataElements> relatedSsr = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(pnrBooking.getSsrList())){
			// add related Ssrs
			relatedSsr.addAll(pnrBooking.getSsrList().stream().filter(ssr -> {
				if (ssr == null) {
					return false;
				}
				return (StringUtils.isEmpty(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && StringUtils.isEmpty(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()));
			}).collect(Collectors.toList()));
		}
		for(RetrievePnrDataElements ssr : relatedSsr){
			if(ssr == null){
				return false;
			}
			if (companionAsrFOCSsr.contains(ssr.getType())) {
				logger.info("companionAsrFOC ssr found: " + ssr.getType());
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description populate ineligible ssr and sk to disability list in seatSelection
	 * @param ssrList
	 * @param skList
	 * @param ineligibleSsrSks
	 * @param seatSelection
	 * @return void
	 * @author haiwei.jia
	 */
	private void populateIneligibleSsrSk(List<RetrievePnrDataElements> relatedSsrSks, List<String> ineligibleSsrSks,
			SeatSelection seatSelection) {
		if(CollectionUtils.isEmpty(ineligibleSsrSks) || seatSelection == null || CollectionUtils.isEmpty(relatedSsrSks)){
			return;
		}
		
		//populate ineligible ssr to seatSelection
		for(RetrievePnrDataElements ssrSk : relatedSsrSks){
			if(ssrSk == null){
				continue;
			} 
			if (ineligibleSsrSks.contains(ssrSk.getType())) {
				seatSelection.findDisabilities().add(ssrSk.getType());
			}
		}
	}

	/**
	 * 
	 * @Description judge if passengerSegment have eligible cabin class
	 * @param passengerSegment
	 * @param segments
	 * @return boolean
	 * @author haiwei.jia
	 */
	private boolean withEligibleCabinClass(PassengerSegment passengerSegment, List<Segment> segments) {
		if(passengerSegment == null || CollectionUtils.isEmpty(segments)){
			return false;
		}
		//the segment related to passegerSegment
		Segment segment = getSegmentById(segments, passengerSegment.getSegmentId());
		if(segment == null){
			return false;
		}
		
		//if cabin class is first class or business class, then return true
		if(MMBBizruleConstants.CABIN_CLASS_FIRST_CLASS.equals(segment.getCabinClass()) || MMBBizruleConstants.CABIN_CLASS_BUSINESS_CLASS.equals(segment.getCabinClass())){
			return true;
		} else{
			List<String> eligibeFareTypes = seatRuleService.getEligibleRBDForSeatSelection();
			if(CollectionUtils.isEmpty(eligibeFareTypes)){
				return false;
			}
			
			if(eligibeFareTypes.contains(segment.getSubClass())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Description determine whether productLevel or customerLevel tier contains oneWorld tier.
	 * @param pnrPassengerSegment
	 * @param pnrPassenger
	 * @return boolean
	 */
	private boolean isOneWorldTier(RetrievePnrPassengerSegment pnrPassengerSegment, RetrievePnrPassenger pnrPassenger) {
		List<String> oneWorldTiers = bizRuleConfig.getOneworldTierLevel();
		if(CollectionUtils.isEmpty(oneWorldTiers) || null == pnrPassengerSegment || null == pnrPassenger) {
			return false;
		}
        List<String> psTierLevel = CollectionUtils.isEmpty(pnrPassengerSegment.getFQTVInfos()) ? Collections.emptyList()
                : pnrPassengerSegment.getFQTVInfos().stream().filter(fqtv -> null != fqtv).map(RetrievePnrFFPInfo::getTierLevel)
                        .collect(Collectors.toList());
        List<String> pTierLevel = CollectionUtils.isEmpty(pnrPassenger.getFQTVInfos()) ? Collections.emptyList()
                : pnrPassenger.getFQTVInfos().stream().filter(fqtv -> null != fqtv).map(RetrievePnrFFPInfo::getTierLevel)
                        .collect(Collectors.toList());
        boolean bool = false;
		for(String owTier : oneWorldTiers) {
			if(psTierLevel.contains(owTier) || pTierLevel.contains(owTier)) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	/**
	 * 
	 * @Description judge if there is a MPO topTier member in booking
	 * @param passengerSegments
	 * @return boolean
	 * @author haiwei.jia
	 */
	private boolean mpoTopTierExist(List<PassengerSegment> passengerSegments) {
		List<String> topTiers = bizRuleConfig.getTopTier();
		List<String> mpoTiers = bizRuleConfig.getCxkaTierLevel();
		if(CollectionUtils.isEmpty(topTiers) || CollectionUtils.isEmpty(mpoTiers)) {
			return false;
		}
		
		//if 1 MPO topTier exists, then return true
		for(PassengerSegment passengerSegment : passengerSegments){
			if(passengerSegment.getFqtvInfo() != null 
					&& topTiers.contains(passengerSegment.getFqtvInfo().getTierLevel())
					&& mpoTiers.contains(passengerSegment.getFqtvInfo().getTierLevel())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	* @Description judge if the flight is operated by CX or KA
	* @param passengerSegment
	* @param segments
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean operatedByCxKa(PassengerSegment passengerSegment, List<Segment> segments) {
		if(CollectionUtils.isEmpty(segments) || passengerSegment == null){
			return false;
		}
		//the segment related to the passengerSegment
		Segment segment = segments.stream().filter(seg -> seg != null && seg.getSegmentID().equals(passengerSegment.getSegmentId())).findFirst().orElse(null);
		if(segment == null){
			return false;
		} 
		
		return OneAConstants.COMPANY_CX.equals(segment.getOperateCompany()) || OneAConstants.COMPANY_KA.equals(segment.getOperateCompany());
	}

	/**
	 * 
	* @Description judge if there is disability to select seat for the passengerSegment
	* @param passengerSegment
	* @param pnrBooking
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean withDisability(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking) {
		if(passengerSegment == null || pnrBooking == null){
			return false;
		}
		List<TBSsrSkMapping> tbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndSeat(MMBConstants.APP_CODE,TBConstants.SEAT_SELECTION_INHIBIT);
		
		return haveIneligibleSsrSk(passengerSegment, pnrBooking, tbSsrSkMappings);
	}

	/** 
	 * 
	* @Description judge if there is ineligible Ssr Sk of passengerSegment
	* @param passengerSegment
	* @param pnrBooking
	* @param tbSsrSkMappings
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean haveIneligibleSsrSk(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking, List<TBSsrSkMapping> tbSsrSkMappings) {
		if(CollectionUtils.isEmpty(tbSsrSkMappings)){
			return false;
		}
		
		List<String> ineligibleSsrSks = tbSsrSkMappings.stream().filter(tbSsrSkMapping -> tbSsrSkMapping != null)
				.map(TBSsrSkMapping :: getSsrSkCode).collect(Collectors.toList());
		
		//passenger id and segment id in the passengerSegment
		String passengerId = passengerSegment.getPassengerId();
		String segmentId = passengerSegment.getSegmentId();
		
		if(StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)){
			return false;
		}
		
		//the SSR and SK related to passengerSegment
		List<RetrievePnrDataElements> relatedSsrSks = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(pnrBooking.getSsrList())){
			//add related Ssr list
			relatedSsrSks.addAll(pnrBooking.getSsrList().stream().filter(ssr -> {
				if (ssr == null) {
					return false;
				}
				return (StringUtils.isEmpty(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && StringUtils.isEmpty(ssr.getSegmentId()))
						|| (passengerId.equals(ssr.getPassengerId()) && segmentId.equals(ssr.getSegmentId()));
			}).collect(Collectors.toList()));
		}
	
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())){
			//add related SK list
			relatedSsrSks.addAll(pnrBooking.getSkList().stream().filter(sk -> {
				if (sk == null) {
					return false;
				}
				return (StringUtils.isEmpty(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()))
						|| (passengerId.equals(sk.getPassengerId()) && StringUtils.isEmpty(sk.getSegmentId()))
						|| (passengerId.equals(sk.getPassengerId()) && segmentId.equals(sk.getSegmentId()));
			}).collect(Collectors.toList()));
		}

		if(CollectionUtils.isEmpty(ineligibleSsrSks) || CollectionUtils.isEmpty(relatedSsrSks)){
			return false;
		}
		
		for(RetrievePnrDataElements ssrSk : relatedSsrSks){
			if(ssrSk == null){
				continue;
			}
			if (ineligibleSsrSks.contains(ssrSk.getType())) {
				return true;
			}
		}
		return false;
	}


	private SpecialSeatEligibility checkSpecialSeatEligibility(SeatSelection seatSelection, PassengerSegment passengerSegment,
			RetrievePnrBooking pnrBooking, Booking booking, boolean infantExistInBooking, boolean childExistInBooking, boolean asrFOC, boolean exlFOC, List<TempSeat> tempSeats) {
		SpecialSeatEligibility ssEligibility = new SpecialSeatEligibility();
		RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(),
				passengerSegment.getPassengerId());
		if(eligibleToSelectExlSeat(passengerSegment, infantExistInBooking, childExistInBooking, pnrBooking, exlFOC, tempSeats)){
			ssEligibility.setExlSeat(true);
		}
		
		if(asrFOC || checkPaxIssuedTicket(passengerSegment.getEticketNumber())){
			ssEligibility.setAsrSeat(true);
		}
		
		if(null != pnrPassenger && eligibleToSelectUMSeat(pnrPassenger, pnrBooking)){
			ssEligibility.setUmSeat(true);
		}
		
		if(eligibleToSelectExitRowSeat(passengerSegment, pnrBooking)){
			ssEligibility.setExitRowSeat(true);
		}
		
		if(eligibleToSelectBCSTSeat(pnrBooking.getPassengers(), passengerSegment.getPassengerId())){
			ssEligibility.setBcstSeat(true);
		}
		
		return ssEligibility;
	}

	/**
	 * if Pax Issued Ticket Stock is 160/043 ,the mean that pax can purchase asr seat
	 * @param ticket
	 * @author jiajian.guo 
	 */
	private boolean checkPaxIssuedTicket(String ticket) {
		if (StringUtils.isEmpty(ticket)) {
			return false;
		}
		return OneAConstants.CX_ETICKET_PREFIX.equals(ticket.substring(0, 3))
				|| OneAConstants.KA_ETICKET_PREFIX.equals(ticket.substring(0, 3));
	}
	
	private boolean eligibleToSelectExlSeat(PassengerSegment passengerSegment, boolean infantExistInBooking, boolean childExistInBooking, RetrievePnrBooking pnrBooking, boolean exlFOC, List<TempSeat> tempSeats) {
		if(passengerSegment == null){
			return false;
		}
		boolean isDMP = false;
		if(passengerSegment.getFqtvInfo() != null && OneAConstants.DMP.equals(passengerSegment.getFqtvInfo().getTierLevel())){
			isDMP = true;
		}
		
		boolean ticketIssued = !StringUtils.isEmpty(passengerSegment.getEticketNumber());
		// if the eticket is 160/043
		boolean cxKaTicket = isCxKaTicket(passengerSegment.getEticketNumber());
		// can find remark of voluntary change from EXL seat to non EXL seat
		boolean voluntaryChangeRemarkFound = isVoluntaryChangeRemarkFound(pnrBooking, passengerSegment);
		// if pax has forfeited EXL according to temp seats
		boolean exlForfeited = isExlForfeited(passengerSegment, tempSeats);
		// have eligibility to choose EXL seat for free or buy EXL seat
		boolean exlEligible = checkEligibilityOfExl(passengerSegment, pnrBooking, infantExistInBooking, childExistInBooking, exlFOC, voluntaryChangeRemarkFound, exlForfeited);
		// check if pax can choose or buy EXL seat according to e-ticket, e.g. if pax is unissued and not DMP member, then he/she can't choose or buy any EXL seat
		boolean canSelectExlAccordingToET = eligibleToSelectExlAccordingToET(ticketIssued, cxKaTicket, exlFOC, isDMP);
		
		return exlEligible && canSelectExlAccordingToET;
	}
	
	/**
	 * check if the seat of the passegnerSegment has been forfeited according to tempSeats
	 * @param passengerSegment
	 * @param tempSeats
	 * @return boolean
	 */
	private boolean isExlForfeited(PassengerSegment passengerSegment, List<TempSeat> tempSeats) {
		if (CollectionUtils.isEmpty(tempSeats)) {
			return false;
		}
		return tempSeats.stream()
				.anyMatch(seat -> ObjectUtils.equals(seat.getPassengerId(), passengerSegment.getPassengerId())
						&& ObjectUtils.equals(seat.getSegmentId(), passengerSegment.getSegmentId())
						 && seat.isForfeited());
	}
	/**
	 * check if the pax is eligible to choose or buy EXL seat according to his/her e-ticket
	 * @param ticketIssued
	 * @param cxKaTicket
	 * @param voluntaryChangeRemarkFound
	 * @param exlFOC 
	 * @param isDMP 
	 * @return boolean
	 */
	private boolean eligibleToSelectExlAccordingToET(boolean ticketIssued, boolean cxKaTicket, boolean exlFOC, boolean isDMP) {
		return (ticketIssued && cxKaTicket) 
				|| (ticketIssued && !cxKaTicket && exlFOC)
				|| (!ticketIssued && isDMP && exlFOC);
	}
	
	/**
	 * check eligibility of EXL seat
	 * @param passengerSegment
	 * @param pnrBooking
	 * @param infantExistInBooking
	 * @param childExistInBooking
	 * @param exlFOC 
	 * @param voluntaryChangeRemarkFound 
	 * @param exlForfeited 
	 * @return
	 */
	private boolean checkEligibilityOfExl(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking, boolean infantExistInBooking, boolean childExistInBooking, boolean exlFOC, boolean voluntaryChangeRemarkFound, boolean exlForfeited) {
		return !isWCHROrWCHS(passengerSegment, pnrBooking)
				&& !infantExistInBooking && !childExistInBooking
				// if pax can't choose EXL seat for free and voluntary change remark found in the booking(which means the pax can't buy EXL seat) or EXL forfeited according to temp seats, then pax can't select EXL seat
				&& !(!exlFOC && (voluntaryChangeRemarkFound || exlForfeited));
	}
	
	/**
	 * check if the ticket is 160/043 ticket
	 * @param eticket
	 * @return
	 */
	private boolean isCxKaTicket(String eticket) {
		return !StringUtils.isEmpty(eticket) && (OneAConstants.CX_ETICKET_PREFIX.equals(eticket.substring(0, 3))
				|| OneAConstants.KA_ETICKET_PREFIX.equals(eticket.substring(0, 3)));
	}
	/**
	 * check if there is remark of voluntary change from EXL seat to non EXL seat found associated to the passengerSegment
	 * @param pnrBooking
	 * @param passengerSegment
	 * @return boolean
	 */
	private boolean isVoluntaryChangeRemarkFound(RetrievePnrBooking pnrBooking, PassengerSegment passengerSegment) {
		if(pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getRemarkList()) || passengerSegment == null) {
			return false;
		}
		return pnrBooking.getRemarkList().stream()
				.anyMatch(remark -> remark.getFreeText()
						.equals(OneAConstants.REMARK_FREETEXT_OF_VOLUNTARY_CHANGE_FROM_EXL_TO_NORMAL)
						&& !CollectionUtils.isEmpty(remark.getPassengerIds())
						&& !CollectionUtils.isEmpty(remark.getSegmentIds())
						&& remark.getPassengerIds().contains(passengerSegment.getPassengerId())
						&& remark.getSegmentIds().contains(passengerSegment.getSegmentId()));
	}
	
	private boolean eligibleToSelectExitRowSeat(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking){
		RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(),
				passengerSegment.getPassengerId());
		return null != pnrPassenger && !paxIsUnaccompaniedMinor(pnrPassenger, pnrBooking) 
				&& !paxWithInfant(pnrBooking.getPassengers(), passengerSegment.getPassengerId()) 
				&& !paxIsChild(pnrPassenger, pnrBooking) 
				&& !paxIsInfant(pnrPassenger, pnrBooking) 
				&& !paxWithDisabilitySSRForExitRowSeat(passengerSegment, pnrBooking);
	}
	
	private boolean eligibleToSelectUMSeat(RetrievePnrPassenger passenger, RetrievePnrBooking pnrBooking){
		return BookingBuildUtil.isUnaccompaniedMinor(pnrBooking, passenger.getPassengerID());
	}
	
	private boolean eligibleToSelectBCSTSeat(List<RetrievePnrPassenger> passengers, String passengerID){
		return paxWithInfant(passengers, passengerID);
	}
	
	private boolean paxWithDisabilitySSRForExitRowSeat(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking) {
		return withDisability(passengerSegment, pnrBooking) ||paxWithIneligibleSSRForSpecialSeat(passengerSegment, pnrBooking);
	}
	
	private boolean paxWithIneligibleSSRForSpecialSeat(PassengerSegment passengerSegment, RetrievePnrBooking pnrBooking) {
		List<TBSsrSkMapping> tbSsrSkMappings = tBSsrSkMappingDAO.findByAppCodeAndSpecialSeat(MMBConstants.APP_CODE,TBConstants.SPECIAL_SEAT_SELECTION_EXITROW);
		return haveIneligibleSsrSk(passengerSegment, pnrBooking, tbSsrSkMappings);
	}
	
	/**
	 * @param passenger
	 * @return true if passenger is an unaccompanied minor
	 */
	private boolean paxIsUnaccompaniedMinor(RetrievePnrPassenger passenger, RetrievePnrBooking pnrBooking){
		List<RetrievePnrDataElements> ssrList = pnrBooking.findSsrList();
		
		boolean paxHasSSRUMNR = ssrList.stream()
				.filter(ssr->ssr.getPassengerId()!=null)
				.filter(ssr->ssr.getPassengerId().equals(passenger.getPassengerID()))
				.anyMatch(ssr->ssr.getType().equalsIgnoreCase(PnrResponseParser.SSR_TYPE_UM));
		
		boolean paxTypeIsUM = false;
		
		if(StringUtils.isNotEmpty(passenger.getPassengerType())){
			paxTypeIsUM = passenger.getPassengerType().equals(OneAConstants.PASSENGER_TYPE_UNACCOMPANIED_MINOR);
			
		}
		return paxHasSSRUMNR || paxTypeIsUM;
	}
	
	/**
	 * @param passengers
	 * @param passengerID
	 * @return true if passenger is with infant
	 */
	private boolean paxWithInfant(List<RetrievePnrPassenger> passengers, String passengerID){
		//check if any infant passenger reference to this passenger as parent
		return passengers.stream()
				.anyMatch(p ->OneAConstants.PASSENGER_TYPE_INF.equals(p.getPassengerType()) && ObjectUtils.equals(passengerID, p.getParentId()));
	}
	
	/**
	 * @param passenger
	 * @return true if passenger is a child
	 */
	private boolean paxIsChild(RetrievePnrPassenger passenger, RetrievePnrBooking pnrBooking){
		List<RetrievePnrDataElements> ssrList = pnrBooking.findSsrList();
		
		boolean paxHasSSRCHD = ssrList.stream()
				.filter(ssr->ssr.getPassengerId()!=null)
				.filter(ssr->ssr.getPassengerId().equals(passenger.getPassengerID()))
				.anyMatch(ssr->ssr.getType().equalsIgnoreCase(PnrResponseParser.SSR_TYPE_CHILD));
		
		boolean paxTypeIsChild = false;
		
		if(StringUtils.isNotEmpty(passenger.getPassengerType())){
			paxTypeIsChild = passenger.getPassengerType().equals(OneAConstants.PASSENGER_TYPE_CHILD);
		}
		return paxHasSSRCHD && paxTypeIsChild;
	}
	
	private boolean paxIsInfant(RetrievePnrPassenger passenger, RetrievePnrBooking pnrBooking) {
		List<RetrievePnrDataElements> ssrList = pnrBooking.findSsrList();
		
		boolean paxHasSSRInf = ssrList.stream()
				.filter(ssr->ssr.getPassengerId()!=null)
				.filter(ssr->ssr.getPassengerId().equals(passenger.getPassengerID()))
				.anyMatch(ssr->ssr.getType().equalsIgnoreCase(PnrResponseParser.SSR_TYPE_INFANT));
		
		boolean paxTypeIsInfant = passenger.getPassengerType().equals(OneAConstants.PASSENGER_TYPE_INF);
		boolean paxTypeIsInfantWithSeat = passenger.getPassengerType().equals(OneAConstants.PASSENGER_TYPE_INS);
		
		return paxHasSSRInf && (paxTypeIsInfant || paxTypeIsInfantWithSeat);
	}
	

	/**
	 * 
	* @Description build seat info
	* @param passengerSegment
	* @param pnrPassengerSegment
	* @return void
	* @author haiwei.jia
	 */
	private void buildSeat(PassengerSegment passengerSegment, RetrievePnrPassengerSegment pnrPassengerSegment, Booking booking) {
		RetrievePnrSeat seat = pnrPassengerSegment.getSeat();
		if(seat != null){
			passengerSegment.setSeatQulifierId(seat.getQulifierId());
			RetrievePnrSeatDetail pnrSeatDetail = seat.getSeatDetail();
			//set seat
			if(pnrSeatDetail != null){
				passengerSegment.findSeat().setSeatNo(pnrSeatDetail.getSeatNo());
				passengerSegment.findSeat().setExlSeat(isExlSeat(pnrSeatDetail.getSeatCharacteristics(), pnrPassengerSegment.getSegmentId(), booking.getSegments()));
				passengerSegment.findSeat().setAsrSeat(isAsrSeat(pnrSeatDetail.getSeatCharacteristics(), pnrPassengerSegment.getSegmentId(), booking.getSegments()));
				passengerSegment.findSeat().setWindowSeat(isWindowSeat(pnrSeatDetail.getSeatCharacteristics(), pnrPassengerSegment.getSegmentId(), booking.getSegments()));
				passengerSegment.findSeat().setAisleSeat(isAisleSeat(pnrSeatDetail.getSeatCharacteristics(), pnrPassengerSegment.getSegmentId(), booking.getSegments()));
				passengerSegment.findSeat().setPaid(pnrSeatDetail.isPaid());
				passengerSegment.findSeat().setStatus(pnrSeatDetail.getStatus());	
				passengerSegment.findSeat().setPaymentInfo(pnrSeatDetail.getPaymentInfo());
				passengerSegment.findSeat().setCrossRef(pnrSeatDetail.getCrossRef());
			}
			
			//set preference
			RetrievePnrSeatPreference pnrSeatPreference = pnrPassengerSegment.getSeat().getPreference();
			if(pnrSeatPreference != null){
				passengerSegment.findPreference().setPreferenceCode(pnrPassengerSegment.getSeat().getPreference().getPreferenceCode());
				passengerSegment.findPreference().setStatus(pnrPassengerSegment.getSeat().getPreference().getStatus());
				passengerSegment.findPreference().setSpeicalPreference(pnrPassengerSegment.getSeat().getPreference().isSpeicalPreference());
			}
			
			//set extra seat
			List<RetrievePnrSeatDetail> pnrExtraSeats = pnrPassengerSegment.getSeat().getExtraSeats();
			if(!CollectionUtils.isEmpty(pnrExtraSeats)){
				for(RetrievePnrSeatDetail pnrExtraSeat : pnrExtraSeats){
					if(pnrExtraSeat != null){
						SeatDetail seatDetail = new SeatDetail();
						seatDetail.setSeatNo(pnrExtraSeat.getSeatNo());
						seatDetail.setExlSeat(isExlSeat(pnrExtraSeat.getSeatCharacteristics(), pnrPassengerSegment.getSegmentId(), booking.getSegments()));
						seatDetail.setPaid(pnrExtraSeat.isPaid());
						seatDetail.setStatus(pnrExtraSeat.getStatus());
						seatDetail.setPaymentInfo(pnrExtraSeat.getPaymentInfo());
						seatDetail.setCrossRef(pnrExtraSeat.getCrossRef());
						passengerSegment.findExtraSeats().add(seatDetail);
					}
				}
			}
		}
	}	
	
	/**
	 * is Aisle Seat
	 * @param seatCharacteristics
	 * @param segmentId
	 * @param segments
	 * @return
	 */
	private Boolean isAisleSeat(List<String> seatCharacteristics, String segmentId, List<Segment> segments) {
		if(CollectionUtils.isEmpty(seatCharacteristics) || StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)){
			return false;
		}
		Segment segment = getSegmentById(segments, segmentId);
		if(segment == null){
			return false;
		}	
		//cabin class of the passengerSegment
		String cabinClass = segment.getCabinClass();
		//if Seat characteristic of the seat is "Y" + "A", return true.
		return OneAConstants.CABIN_CLASS_ECONOMY.equals(cabinClass) && seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_AISLE_SEAT)
				&& !seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_LEG_SPACE);
	}
	
	/**
	 * is Window Seat
	 * @param seatCharacteristics
	 * @param segmentId
	 * @param segments
	 * @return
	 */
	private Boolean isWindowSeat(List<String> seatCharacteristics, String segmentId, List<Segment> segments) {
		if(CollectionUtils.isEmpty(seatCharacteristics) || StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)){
			return false;
		}
		Segment segment = getSegmentById(segments, segmentId);
		if(segment == null){
			return false;
		}	
		//cabin class of the passengerSegment
		String cabinClass = segment.getCabinClass();
		//if Seat characteristic of the seat is "Y" + "W", return true.
		// remove the "CH" check because for ASMR case, 1A may not return "CH" for selected ASR.
		return OneAConstants.CABIN_CLASS_ECONOMY.equals(cabinClass) && seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_WINDOW_SEAT)
				&& !seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_LEG_SPACE);
	}
	
	/**
	 * 
	* @Description judge if the seat is a extra seat by seat characteristics and cabin class
	* @param seatCharacteristics
	* @return boolean
	* @author haiwei.jia
	 */
	private boolean isExlSeat(List<String> seatCharacteristics, String segmentId, List<Segment> segments) {
		if(CollectionUtils.isEmpty(seatCharacteristics) || StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)){
			return false;
		}
		Segment segment = getSegmentById(segments, segmentId);
		if(segment == null){
			return false;
		}	
		//cabin class of the passengerSegment
		String cabinClass = segment.getCabinClass();
		//if Seat characteristic of the seat is "L" + "Y", return true.
		return seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_LEG_SPACE)
				&& OneAConstants.CABIN_CLASS_ECONOMY.equals(cabinClass);
	}
	
	/**
	 * @Description judge if the seat is a asr seat by seat characteristics and cabin class
	 * @param seatCharacteristics
	 * @param segmentId
	 * @param segments
	 * @return
	 */
	private boolean isAsrSeat(List<String> seatCharacteristics, String segmentId, List<Segment> segments) {
		if(CollectionUtils.isEmpty(seatCharacteristics) || StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)){
			return false;
		}
		Segment segment = getSegmentById(segments, segmentId);
		if(segment == null){
			return false;
		}	
		//cabin class of the passengerSegment
		String cabinClass = segment.getCabinClass();
		//if Seat characteristic of the seat is "CH" + "Y", return true.
		// remove the "CH" check because for ASMR case, 1A may not return "CH" for selected ASR.
		return seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_CHARGEABLE)
				&& OneAConstants.CABIN_CLASS_ECONOMY.equals(cabinClass) 
				&& !seatCharacteristics.contains(OneAConstants.SEAT_CHARACTERISTIC_LEG_SPACE);
	}
	
	/**
	 * 
	* @Description get segment by segment id from segments
	* @param segments
	* @param segmentId
	* @return Segment
	* @author haiwei.jia
	 */
	private Segment getSegmentById(List<Segment> segments, String segmentId) {
		if (CollectionUtils.isEmpty(segments) || StringUtils.isEmpty(segmentId)) {
			return null;
		}
		return segments.stream().filter(seg -> seg != null && segmentId.equals(seg.getSegmentID())).findFirst().orElse(null);
	}

	/**
	 * build passengerSegment TravelDoc
	 * 
	 * @param pnrPassengerSegment
	 * @param booking
	 * @param passengerSegment
	 * @param travelDocList
	 * @param secMap 
	 * @param priMap 
	 */
	private void buildPassengerSegmentTravelDoc(RetrievePnrPassengerSegment pnrPassengerSegment, Booking booking, PassengerSegment passengerSegment, List<TravelDocList> travelDocList, Map<String, String> priMap, Map<String, String> secMap) {
		List<String> apiVersions = getApiVersionsBySegmentIds(booking.getSegments(), pnrPassengerSegment.getSegmentId());
		String apiVersion = apiVersions.stream().filter(Objects::nonNull).findFirst().orElse(null);
		if(StringUtils.isEmpty(apiVersion)) {
			return;
		}
		//primary travelDoc		
		List<RetrievePnrTravelDoc> pnrPriTravelDocs = pnrPassengerSegment.getPriTravelDocs();
		RetrievePnrTravelDoc retrievePnrPriTravelDoc = null;
		for(RetrievePnrTravelDoc travelDoc : pnrPriTravelDocs) {
			String type = travelDoc.getTravelDocumentType();
			if(travelDocIsValidated(type, TBConstants.TRAVEL_DOC_PRIMARY, travelDocList, apiVersion)) {
				retrievePnrPriTravelDoc = travelDoc;
				break;
			}
		}
		if(retrievePnrPriTravelDoc != null) {
			buildTravelDoc(retrievePnrPriTravelDoc, passengerSegment.findPriTravelDoc(), priMap);			
			//add countryOfResidence to priTravelDoc
			addCountryOfResidenceToPriTravelDoc(pnrPassengerSegment.getResAddresses(), passengerSegment.findPriTravelDoc());
		}
		//secondary travelDoc
		List<RetrievePnrTravelDoc> pnrSecTravelDocs = pnrPassengerSegment.getSecTravelDocs();
		RetrievePnrTravelDoc retrievePnrSecTravelDoc = null;
		for(RetrievePnrTravelDoc travelDoc : pnrSecTravelDocs) {
			String type = travelDoc.getTravelDocumentType();
			if(travelDocIsValidated(type, TBConstants.TRAVEL_DOC_SECONDARY, travelDocList, apiVersion)) {
				retrievePnrSecTravelDoc = travelDoc;
				break;
			}
		}
		if(retrievePnrSecTravelDoc != null) {
			buildTravelDoc(retrievePnrSecTravelDoc, passengerSegment.findSecTravelDoc(), secMap);			
		}
	}
	
	private String getCountryCodeOfResidence(List<RetrievePnrAddressDetails> resAddresses) {
		if(CollectionUtils.isEmpty(resAddresses)){
			return null;
		}
		//get the largest OT residence address from product level
		Optional<RetrievePnrAddressDetails> pnrResAddress = resAddresses.stream()
				.sorted((resAddress1, resAddress2) -> resAddress2.getQualifierId().compareTo(resAddress1.getQualifierId()))
				.findFirst();
		if (pnrResAddress.isPresent()) {
			return getThreeCharsCountryCode(pnrResAddress.get().getCountry());
		} else {
			return null;
		}		
	}
	
	/**
	 * add countryOfResidence to primary travel document
	 * @param resAddresses
	 * @param travelDoc
	 */
	private void addCountryOfResidenceToPriTravelDoc(List<RetrievePnrAddressDetails> resAddresses,
			TravelDoc travelDoc) {

		travelDoc.setCountryOfResidence(getCountryCodeOfResidence(resAddresses));
	}
	
	/**
	 * build passenger segment countryOfResidence
	 * @param resAddresses
	 * @param travelDoc
	 */
	private void buildPassengerSegmentCountryOfResidence(RetrievePnrPassengerSegment pnrPassengerSegment, PassengerSegment passengerSegment) {
		
		List<RetrievePnrAddressDetails> resAddresses = pnrPassengerSegment.getResAddresses();
		passengerSegment.setCountryOfResidence(getCountryCodeOfResidence(resAddresses));
	}
	
	/**
	 * validate travelDoc is in the travelDocList or not by apiVersion & type & primaryOrSecondeary
	 * 
	 * @param type
	 * @param primaryOrSecondeary
	 * @param travelDocList
	 * @param apiVersion
	 */
	private boolean travelDocIsValidated(String type, String primaryOrSecondeary, List<TravelDocList> travelDocList, String... apiVersion) {
		boolean bool = false;
		if(apiVersion.length < 1 || StringUtils.isEmpty(type)) {
			return bool;
		}
		List<String> apiVersions = Arrays.asList(apiVersion);
		for(TravelDocList travelDoc: travelDocList) {
			if(apiVersions.contains(String.valueOf(travelDoc.getTravelDocVersion())) && type.equals(travelDoc.getTravelDocCode())
					&& primaryOrSecondeary.equals(travelDoc.getTravelDocType())) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	/**
	 * get apiVersions by segmentIds
	 * 
	 * @param segments
	 * @param segmentIds
	 */
	private List<String> getApiVersionsBySegmentIds(List<Segment> segments, String... segmentIds) {
		List<String> apiVersions = new ArrayList<>();
		List<String> ids = Arrays.asList(segmentIds);
		if(CollectionUtils.isEmpty(ids)) {
			return apiVersions;
		}
		for(Segment segment : segments) {
			if(ids.contains(segment.getSegmentID())) {
				apiVersions.add(segment.getApiVersion());
			}
		}
		return apiVersions;
	}

	/**
	 * build passenger TravelDoc
	 * 
	 * @param pnrPassenger
	 * @param passenger
	 * @param pnrBooking
	 * @param booking 
	 * @param secMap 
	 * @param priMap 
	 */
	private void buildPassengerTravelDoc(RetrievePnrPassenger pnrPassenger, Passenger passenger, RetrievePnrBooking pnrBooking, Booking booking, List<TravelDocList> travelDocList, Map<String, String> priMap, Map<String, String> secMap) {
		List<String> segmentIds = getSegmentIdsByPassengerId(pnrBooking.getPassengerSegments(), passenger.getPassengerId());
		List<String> apiVersions = getApiVersionsBySegmentIds(booking.getSegments(), segmentIds.toArray(new String[segmentIds.size()]));
		
		//primary TravelDoc
		List<TravelDoc> priTravelDocs = passenger.getPriTravelDocs();
		List<RetrievePnrTravelDoc> pnrPriTravelDocs = pnrPassenger.getPriTravelDocs();
		for(RetrievePnrTravelDoc pnrPriTravelDoc : pnrPriTravelDocs) {
			String type = pnrPriTravelDoc.getTravelDocumentType();
			if(travelDocIsValidated(type, TBConstants.TRAVEL_DOC_PRIMARY, travelDocList, apiVersions.toArray(new String[apiVersions.size()]))) {
				TravelDoc priTravelDoc = new TravelDoc();
				buildTravelDoc(pnrPriTravelDoc, priTravelDoc, priMap);
				addCountryOfResidenceToPriTravelDoc(pnrPassenger.getResAddresses(), priTravelDoc);
				priTravelDocs.add(priTravelDoc);
			}
		}
		
		//secondary TravelDoc
		List<TravelDoc> secTravelDocs = passenger.getSecTravelDocs();
		List<RetrievePnrTravelDoc> pnrSecTravelDocs = pnrPassenger.getSecTravelDocs();
		for(RetrievePnrTravelDoc pnrSecTravelDoc : pnrSecTravelDocs) {
			String type = pnrSecTravelDoc.getTravelDocumentType();
			if(travelDocIsValidated(type, TBConstants.TRAVEL_DOC_SECONDARY, travelDocList, apiVersions.toArray(new String[apiVersions.size()]))) {
				TravelDoc secTravelDoc = new TravelDoc();
				buildTravelDoc(pnrSecTravelDoc, secTravelDoc, secMap);
				secTravelDocs.add(secTravelDoc);
			}
		}
		
	}
	
	private void buildPassengerCountryOfResidence(RetrievePnrPassenger pnrPassenger, Passenger passenger) {
		List<RetrievePnrAddressDetails> resAddresses = pnrPassenger.getResAddresses();
		passenger.setCountryOfResidence(getCountryCodeOfResidence(resAddresses));
	}

	/**
	 * get list of segmentId by passengerId
	 * 
	 * @param retrievePnrPassengerSegments
	 * @param passengerId
	 */
	private List<String> getSegmentIdsByPassengerId(List<RetrievePnrPassengerSegment> retrievePnrPassengerSegments, String passengerId) {
		List<String> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(retrievePnrPassengerSegments) || StringUtils.isEmpty(passengerId)) {
			return list;
		}
		for(RetrievePnrPassengerSegment passengerSegment : retrievePnrPassengerSegments) {
			if(StringUtils.isNotEmpty(passengerSegment.getPassengerId()) && passengerSegment.getPassengerId().equals(passengerId)) {
				list.add(passengerSegment.getSegmentId());
			}
		}
		return list;
	}

	/**
	 * build passenger info
	 * 
	 * @param booking
	 * @param pnrBooking
	 * @param travelDocList
	 * @param secMap 
	 * @param priMap 
	 * @param required 
	 * 
	 */
	private List<Passenger> buildPassengerInfo(Booking booking, RetrievePnrBooking pnrBooking, List<TravelDocList> travelDocList,
			Map<String, String> priMap, Map<String, String> secMap, LoginInfo loginInfo, BookingBuildRequired required) {
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		List<Passenger> passengers = new ArrayList<>();
		for (RetrievePnrPassenger pnrPassenger : pnrPassengers) {
		    // if mice booking we will only build login pax
	        if(BooleanUtils.isTrue(booking.isMiceBooking()) && !BooleanUtils.isTrue(pnrPassenger.isPrimaryPassenger())) {
	            continue;
	        }
			Passenger passenger = new Passenger();
			passenger.setParentId(pnrPassenger.getParentId());
			passenger.setPassengerId(pnrPassenger.getPassengerID());
			passenger.setFamilyName(pnrPassenger.getFamilyName());
			passenger.setGivenName(pnrPassenger.getGivenName());
			passenger.setPassengerType(pnrPassenger.getPassengerType());
			passenger.setPrimaryPassenger(pnrPassenger.isPrimaryPassenger());
			passenger.setCompanion(pnrPassenger.isCompanion());
			passenger.setLoginFFPMatched(pnrPassenger.isLoginFFPMatched());
			passenger.setStaffDetail(pnrPassenger.getStaffDetail());
			passenger.setLoginMember(pnrPassenger.isLoginMember());
			passenger.setUnaccompaniedMinor(BookingBuildUtil.isUnaccompaniedMinor(pnrBooking, pnrPassenger.getPassengerID()));
			
			// passenger title
			buildPassengerTitle(passenger);
			// build DOB
			if(pnrPassenger != null && pnrPassenger.getDob() != null){
				RetrievePnrDob pnrPassengerDob = pnrPassenger.getDob();
				passenger.findDob().setBirthDateDay(pnrPassengerDob.getBirthDateDay());
				passenger.findDob().setBirthDateMonth(pnrPassengerDob.getBirthDateMonth());
				passenger.findDob().setBirthDateYear(pnrPassengerDob.getBirthDateYear());
			}
			
			// Travel Documents
			if(required.travelDocument()) {
				buildPassengerTravelDoc(pnrPassenger, passenger, pnrBooking, booking, travelDocList, priMap, secMap);				
			}
			
			// Country of Residence
			buildPassengerCountryOfResidence(pnrPassenger, passenger);
			
			// Contact Info
			if(required.operateInfoAndStops() && required.passenagerContactInfo()) {
				buildContactInfo(pnrPassenger, pnrBooking, passenger, loginInfo);
			}
			
			//the pnrPassengerSegments related to current passenger
			List<RetrievePnrPassengerSegment> pnrPassengerSegmentsOfPassenger = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), pnrPassenger.getPassengerID());
			
			//emergency contact info
			if(required.emergencyContactInfo()) {
				buildEmrContactInfo(pnrPassenger, passenger);				
			}
				
			//destination address
			buildDesAddress(pnrPassenger, pnrPassengerSegmentsOfPassenger, passenger);
			
			//KTN
			buildKTN(pnrPassenger, pnrPassengerSegmentsOfPassenger, passenger);
			
			//redress number
			buildRedressNo(pnrPassenger, pnrPassengerSegmentsOfPassenger, passenger);
			
			passengers.add(passenger);
		}				
		return passengers;
	}

	/**
	 * build redress number
	 * @param pnrPassenger
	 * @param pnrPassengerSegmentsOfPassenger
	 * @param passenger
	 * @param segments 
	 */
	private void buildRedressNo(RetrievePnrPassenger pnrPassenger,
			List<RetrievePnrPassengerSegment> pnrPassengerSegments, Passenger passenger) {
		if(CollectionUtils.isEmpty(pnrPassengerSegments) && CollectionUtils.isEmpty(pnrPassenger.getKtns())){
			return;
		}
		
		// all redress numbers
		List<RetrievePnrTravelDoc> allRedressNos;
		// add product level redress numbers
		allRedressNos = pnrPassengerSegments.stream()
				.filter(ps -> !CollectionUtils.isEmpty(ps.getRedresses())).map(RetrievePnrPassengerSegment :: getRedresses)
				.reduce(new ArrayList<>(), (all, item) -> {
					all.addAll(item);
					return all;
				});
		
		// add customer level redress numbers
		if (!CollectionUtils.isEmpty(pnrPassenger.getRedresses())) {
			allRedressNos.addAll(pnrPassenger.getRedresses());
		}
		
		if (!CollectionUtils.isEmpty(allRedressNos)) { 
			// get the largest OT redressNo
			RetrievePnrTravelDoc largestOtRedressNo = allRedressNos.stream().filter(redressNo -> redressNo.getQualifierId() != null)
					.sorted((redress1, redress2) -> redress2.getQualifierId().compareTo(redress1.getQualifierId()))
					.findFirst().orElse(null);
			
			if (largestOtRedressNo != null) {
				passenger.findRedress().setNumber(largestOtRedressNo.getTravelDocumentNumber());
				passenger.findRedress().setQualifierId(largestOtRedressNo.getQualifierId());
			}
		}
	}

	/**
	 * build KTN 
	 * @param pnrPassenger
	 * @param pnrPassengerSegmentsOfPassenger
	 * @param passenger
	 * @param segments 
	 */
	private void buildKTN(RetrievePnrPassenger pnrPassenger,
			List<RetrievePnrPassengerSegment> pnrPassengerSegments, Passenger passenger) {
		if(CollectionUtils.isEmpty(pnrPassengerSegments) && CollectionUtils.isEmpty(pnrPassenger.getKtns())){
			return;
		}
		// all ktns
		List<RetrievePnrTravelDoc> allKTNs;
		// product level KTNs
		allKTNs = pnrPassengerSegments.stream()
				.filter(ps -> !CollectionUtils.isEmpty(ps.getKtns())).map(RetrievePnrPassengerSegment :: getKtns)
				.reduce(new ArrayList<>(), (all, item) -> {
					all.addAll(item);
					return all;
				});
		// customer level ktns
		if (!CollectionUtils.isEmpty(pnrPassenger.getKtns())) {
			allKTNs.addAll(pnrPassenger.getKtns());
		}
		
		if (!CollectionUtils.isEmpty(allKTNs)) {
			// get the largest OT KTN 
			RetrievePnrTravelDoc largestOtKTN = allKTNs.stream().filter(ktn -> ktn.getQualifierId() != null)
					.sorted((ktn1, ktn2) -> ktn2.getQualifierId().compareTo(ktn1.getQualifierId()))
					.findFirst().orElse(null);
			
			if (largestOtKTN != null) {
				passenger.findKtn().setNumber(largestOtKTN.getTravelDocumentNumber());
				passenger.findKtn().setQualifierId(largestOtKTN.getQualifierId());
			}
		} 
	}

	/**
	 * 
	* @Description get three chars country code by countryCode
	* @param countryCode
	* @return String
	* @author haiwei.jia
	 */
	private String getThreeCharsCountryCode(String countryCode) {
		if(!StringUtils.isEmpty(countryCode) && countryCode.trim().length() == 2){
			countryCode = nationalityCodeService.findThreeCountryCodeByTwoCountryCode(countryCode.trim());
		}
		return countryCode;
	}

	/**
	 * 
	* @Description build emergency contact info
	* @param pnrPassenger
	* @param passenger
	* @return void
	* @author haiwei.jia
	 */
	private void buildEmrContactInfo(RetrievePnrPassenger pnrPassenger, Passenger passenger) {
		if(CollectionUtils.isEmpty(pnrPassenger.getEmrContactInfos())){
			return;
		}
		//get the largest OT
		Optional<RetrievePnrEmrContactInfo> pnrEmrContactInfo = pnrPassenger.getEmrContactInfos().stream()
				.sorted((emrContactInfo1, emrContactInfo2) -> emrContactInfo2.getQualifierId().compareTo(emrContactInfo1.getQualifierId()))
				.findFirst();
		if(pnrEmrContactInfo.isPresent()){
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			String countryNumber = String.valueOf(phoneUtil.getCountryCodeForRegion(pnrEmrContactInfo.get().getCountryCode()));
			// If invalid, prepopulate country number and phone number in phone number field
			if (bookingBuildHelper.isValidPhoneNumber(countryNumber, pnrEmrContactInfo.get().getPhoneNumber(), ContactType.EMR_CONTACT)) {
				passenger.findEmrContactInfo().setCountryCode(getThreeCharsCountryCode(pnrEmrContactInfo.get().getCountryCode()));
				passenger.findEmrContactInfo().setName(pnrEmrContactInfo.get().getName());
				passenger.findEmrContactInfo().setPhoneNumber(pnrEmrContactInfo.get().getPhoneNumber());
				try {
					passenger.findEmrContactInfo().setPhoneCountryNumber(countryNumber);
				} catch (Exception e) {
					logger.warn(String.format("failed to get phone country number of country code: %s", pnrEmrContactInfo.get().getCountryCode()));
				}		
			} else {
				passenger.findEmrContactInfo().setName(pnrEmrContactInfo.get().getName());
				passenger.findEmrContactInfo().setPhoneNumber(countryNumber + pnrEmrContactInfo.get().getPhoneNumber());	
			}
		}
		
	}
	
	/**
	 * 
	* @Description build destination address
	* @param pnrPassenger
	* @param pnrPassengerSegments
	* @param passenger
	* @return void
	* @author haiwei.jia
	 */
	private void buildDesAddress(RetrievePnrPassenger pnrPassenger, List<RetrievePnrPassengerSegment> pnrPassengerSegments, Passenger passenger) {
		if(CollectionUtils.isEmpty(pnrPassengerSegments) && CollectionUtils.isEmpty(pnrPassenger.getDesAddresses())){
			return;
		}
		
		// all destination addresses
		List<RetrievePnrAddressDetails> desAddresses = new ArrayList<>();
		// get product level addresses
		if(!CollectionUtils.isEmpty(pnrPassengerSegments)){
			for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrPassengerSegments){
				desAddresses.addAll(pnrPassengerSegment.getDesAddresses());
			}
		}
		// get customer level addresses
		if (!CollectionUtils.isEmpty(pnrPassenger.getDesAddresses())) {
			desAddresses.addAll(pnrPassenger.getDesAddresses());
		}
		
		//get the largest OT desaddress
		RetrievePnrAddressDetails desAddress = desAddresses.stream().filter(address -> address.getQualifierId() != null).sorted((desAddress1, desAddress2) -> {
			return desAddress2.getQualifierId().compareTo(desAddress1.getQualifierId());
		}).findFirst().orElse(null);
		
		if(desAddress != null){
			passenger.findDesAddress().setCity(desAddress.getCity());
			passenger.findDesAddress().setStateCode(desAddress.getStateCode());
			passenger.findDesAddress().setStreet(desAddress.getStreet());
			passenger.findDesAddress().setZipCode(desAddress.getZipCode());
		}
	}

	/**
	 * 
	* @Description build contact info
	* @param pnrPassenger
	* @param pnrBooking
	* @param passenger
	* @param loginInfo
	* @return void
	* @author haiwei.jia
	 */
	private void buildContactInfo(RetrievePnrPassenger pnrPassenger, RetrievePnrBooking pnrBooking, Passenger passenger, LoginInfo loginInfo) {
		if(pnrPassenger == null || pnrBooking == null){
			return;
		}	
		boolean haveCxFlight = false;
		boolean haveKaFlight = false;	
		
		//judge if the passenger has a flight of CX/KA
		for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrBooking.getPassengerSegments()){
			if(pnrPassengerSegment.getPassengerId().equals(pnrPassenger.getPassengerID())){
				RetrievePnrSegment pnrSegment = PnrResponseParser.getSegmentById(pnrBooking.getSegments(), pnrPassengerSegment.getSegmentId());
				if(OneAConstants.COMPANY_CX.equals(pnrSegment.getPnrOperateCompany())){
					haveCxFlight = true;
				}
				else if(OneAConstants.COMPANY_KA.equals(pnrSegment.getPnrOperateCompany())){
					haveKaFlight = true;
				}
			}
		}
		//build phone info
		buildContactPhoneInfo(pnrBooking, pnrPassenger, passenger, haveCxFlight, haveKaFlight, loginInfo);	
		
		//build email info
		buildEmailInfo(pnrBooking, pnrPassenger, passenger, haveCxFlight, haveKaFlight, loginInfo);	

		//build notification email info
		buildNotificationEmailInfo(pnrBooking, pnrPassenger, passenger, haveCxFlight, haveKaFlight);
	}
	
	/**
	 * 
	* @Description build contact email info
	* @param emails
	* @param passenger
	* @param haveCxFlight
	* @param haveKaFlight
	* @param loginInfo
	* @return void
	* @author haiwei.jia
	 */
	private void buildEmailInfo(RetrievePnrBooking pnrBooking, RetrievePnrPassenger pnrPassenger, Passenger passenger, boolean haveCxFlight,
			boolean haveKaFlight, LoginInfo loginInfo) {
		List<RetrievePnrEmail> pnrEmails = pnrPassenger.getEmails();
		
		//pnrEmails is empty, build email info by member profile
		if(CollectionUtils.isEmpty(pnrEmails) && CollectionUtils.isEmpty(pnrBooking.getApEmails())){
			buildEmailByMemProfile(passenger, loginInfo);
			return;
		}
		
		// build email from CTCE
		// CTCE emails
		List<RetrievePnrEmail> ctceEmails = getCTCEEmails(pnrEmails);

		RetrievePnrEmail email = null;
		// if the passenger has a CX flight, first get CX email, if find
		// nothing, then get KA email
		if (haveCxFlight) {
			// get the first CX email
			email = getFirstCxEmail(ctceEmails);
			if (email != null) {
				populateEmail(passenger, email);
				return;
			} else {// if no CX email found, get first KA email
				// get first KA email
				email = getFirstKaEmail(ctceEmails);
				if (email != null) {
					populateEmail(passenger, email);
					return;
				}
			}
		} else if (haveKaFlight) {// if the passenger don't have CX flight but have a KA flight, first get KA contact phone, if find nothing, then get CX contact phone
			email = getFirstKaEmail(ctceEmails);
			if (email != null) {
				populateEmail(passenger, email);
				return;
			}
			// if no KA contact found, get first CX contact
			else {
				// get first CX contact
				email = getFirstCxEmail(ctceEmails);
				if (email != null) {
					populateEmail(passenger, email);
					return;
				}
			}
		} else {
			// OLSSMMB-19407 Fix - Allow to retrieve interline contact info
			email = getFirstEmail(ctceEmails);
			if (email != null) {
				populateEmail(passenger, email);
				return;
			}
		}
		
		// build email from APE
		// AP email is not empty and there is only one passenger in the booking
		if (!CollectionUtils.isEmpty(pnrBooking.getApEmails()) && pnrBooking.getPassengers() != null && pnrBooking.getPassengers().size() == 1) {
			// get available APM contact first then APB & APH
			email = pnrBooking.getApEmails().stream()
					.filter(apEmail -> apEmail != null && !CollectionUtils.isEmpty(apEmail.getPassengerIds())
							&& apEmail.getPassengerIds().contains(passenger.getPassengerId()))
					.findFirst().orElse(pnrBooking.getApEmails().stream().filter(Objects::nonNull).findFirst()
							.orElse(null));
			if (email != null) {
				populateEmail(passenger, email);
				return;
			}
		}
		// AP email is not empty and there are more than one passengers in the booking 
		else if(!CollectionUtils.isEmpty(pnrBooking.getApEmails()) && pnrBooking.getPassengers() != null && pnrBooking.getPassengers().size() > 1) {
			// get available APM contact first then APB & APH
			email = pnrBooking.getApEmails().stream()
					.filter(apEmail -> apEmail != null && !CollectionUtils.isEmpty(apEmail.getPassengerIds())
							&& apEmail.getPassengerIds().contains(passenger.getPassengerId()))
					.findFirst().orElse(null);
			if (email != null) {
				populateEmail(passenger, email);
				return;
			}
		}
		
		//build email info by member profile
		buildEmailByMemProfile(passenger, loginInfo);	
	}
	
	
	/**
	 * Build email info which can be used to notify something to passenger by email
	 * Use CTCE first. If no CTCE, use APE.
	 * @param pnrBooking
	 * @param pnrPassenger
	 * @param passenger
	 * @param haveCxFlight
	 * @param haveKaFlight
	 * @param loginInfo
	 */
	private void buildNotificationEmailInfo(RetrievePnrBooking pnrBooking, RetrievePnrPassenger pnrPassenger, Passenger passenger, boolean haveCxFlight,
			boolean haveKaFlight) {
		List<RetrievePnrEmail> pnrEmails = pnrPassenger.getEmails();
		
		// Find CTCEs first
		List<RetrievePnrEmail> ctceEmails = getCTCEEmails(pnrEmails);
		if (ctceEmails != null && !ctceEmails.isEmpty()) {
			populateNotificationEmails(passenger, ctceEmails);
			return;
		}

		// If no CTCE, try to find APEs associated with pax
		if(!CollectionUtils.isEmpty(pnrBooking.getApEmails()) && pnrBooking.getPassengers() != null && pnrBooking.getPassengers().size() >= 1) {
			List<RetrievePnrEmail> apeEmails = pnrBooking.getApEmails().stream()
					.filter(apEmail -> apEmail != null && !CollectionUtils.isEmpty(apEmail.getPassengerIds())
							&& apEmail.getPassengerIds().contains(passenger.getPassengerId()))
					.collect(Collectors.toList());
			if (apeEmails != null && !apeEmails.isEmpty()) {
				populateNotificationEmails(passenger, apeEmails);
				return;
			}
		}

		// If no APE with pax, try to find APEs without pax
		if (!CollectionUtils.isEmpty(pnrBooking.getApEmails())) {
			List<RetrievePnrEmail> apeEmails = pnrBooking.getApEmails().stream().filter(
						apEmail -> apEmail != null && CollectionUtils.isEmpty(apEmail.getPassengerIds()))
					.collect(Collectors.toList());
			if (apeEmails != null && !apeEmails.isEmpty()) {
				populateNotificationEmails(passenger, apeEmails);
				return;
			}
		}
	}

	/**
	 * 
	* @Description build email by member profile
	* @param passenger
	* @param loginInfo
	* @return void
	* @author haiwei.jia
	 */
	private void buildEmailByMemProfile(Passenger passenger, LoginInfo loginInfo) {
		if(passenger == null || loginInfo == null || BooleanUtils.isNotTrue(passenger.getLoginMember())){
			return;
		}
		
		if(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()) && !StringUtils.isEmpty(loginInfo.getMemberId())){
			String emailAddress = getCustomerEmailInfoFromProfile(loginInfo.getMemberId(),loginInfo.getMmbToken());
			if(!StringUtils.isEmpty(emailAddress)){
				passenger.findContactInfo().findEmail().setEmailAddress(emailAddress);
				passenger.findContactInfo().findEmail().setType(ContactInfoTypeEnum.MEMBER_PROFILE_CONTACT_INFO.getType());
			}
		}
	}

	/**
	 * 
	* @Description build contact phone info
	* @param pnrContactPhones
	* @param passenger
	* @param haveCxFlight
	* @param haveKaFlight
	* @param loginInfo
	* @return void
	* @author haiwei.jia
	 */
	private void buildContactPhoneInfo(RetrievePnrBooking pnrBooking, RetrievePnrPassenger pnrPassenger, Passenger passenger,
			boolean haveCxFlight, boolean haveKaFlight, LoginInfo loginInfo) {
		List<RetrievePnrContactPhone> pnrContactPhones = pnrPassenger.getContactPhones();
		
		//if CTCx and APx contact is empty, build contact info by member profile
		if(CollectionUtils.isEmpty(pnrContactPhones) && CollectionUtils.isEmpty(pnrBooking.getApContactPhones())){
			buildContactByMemProfile(passenger, loginInfo);
			return;
		}
		
		// build contact from CTCx contact
		// CTCM contact phones
		List<RetrievePnrContactPhone> ctcmContactPhones = getCTCMContactPhones(pnrContactPhones);

		RetrievePnrContactPhone pnrContactPhone;
		// if the passenger has a CX flight, first get CX contact phone, if find nothing, then get KA contact phone
		if (haveCxFlight) {
			// get the first CX contact
			pnrContactPhone = getFirstCxContactPhone(ctcmContactPhones);
			if (pnrContactPhone != null) {
				populateContactPhone(passenger, pnrContactPhone);
				return;
			} else {// if no CX contact found, get first KA contact
				// get first KA contact
				pnrContactPhone = getFirstKaContactPhone(ctcmContactPhones);
				if (pnrContactPhone != null) {
					populateContactPhone(passenger, pnrContactPhone);
					return;
				}
			}
		} else if (haveKaFlight) {// if the passenger don't have CX flight but have a KA flight, first get KA contact phone, if find nothing, then get CX contact phone
			pnrContactPhone = getFirstKaContactPhone(ctcmContactPhones);
			if (pnrContactPhone != null) {
				populateContactPhone(passenger, pnrContactPhone);
				return;
			} else {// if no KA contact found, get first CX contact
				// get first CX contact
				pnrContactPhone = getFirstCxContactPhone(ctcmContactPhones);
				if (pnrContactPhone != null) {
					populateContactPhone(passenger, pnrContactPhone);
					return;
				}
			}
		} else {
			// OLSSMMB-19407 Fix - Allow to retrieve interline contact info
			pnrContactPhone = getFirstContactPhone(ctcmContactPhones);
			if (pnrContactPhone != null) {
				populateContactPhone(passenger, pnrContactPhone);
				return;
			}
		}
		
		//build contact info from APx contact
		// AP contact phones is not empty and there is only one passenger in the booking
		if(!CollectionUtils.isEmpty(pnrBooking.getApContactPhones()) && pnrBooking.getPassengers() != null && pnrBooking.getPassengers().size() == 1){
			// get available APM contact first then APB & APH, and on the basis of this logic, get APx which is associated to the passenger first
			pnrContactPhone = pnrBooking.getApContactPhones().stream()
					.filter(contact -> contact != null && OneAConstants.APM.equals(contact.getType())
							&& !CollectionUtils.isEmpty(contact.getPassengerIds())
							&& contact.getPassengerIds().contains(passenger.getPassengerId()))
					.findFirst()
					.orElse(pnrBooking.getApContactPhones().stream()
							.filter(contact -> contact != null && OneAConstants.APM.equals(contact.getType()))
							.findFirst()
							.orElse(pnrBooking.getApContactPhones().stream()
									.filter(contact -> contact != null
											&& !CollectionUtils.isEmpty(contact.getPassengerIds())
											&& contact.getPassengerIds().contains(passenger.getPassengerId()))
									.findFirst().orElse(pnrBooking.getApContactPhones().stream()
											.filter(contact -> contact != null).findFirst().orElse(null))));
			
			if(pnrContactPhone != null){
				populateContactPhone(passenger, pnrContactPhone);
				return;
			}
		}
		// AP contact phones is not empty and but there are more than one passengers in the booking
		else if(!CollectionUtils.isEmpty(pnrBooking.getApContactPhones()) && pnrBooking.getPassengers() != null && pnrBooking.getPassengers().size() > 1) {
			pnrContactPhone = pnrBooking.getApContactPhones().stream()
					.filter(contact -> contact != null && OneAConstants.APM.equals(contact.getType())
							&& !CollectionUtils.isEmpty(contact.getPassengerIds())
							&& contact.getPassengerIds().contains(passenger.getPassengerId()))
					.findFirst()
					.orElse(pnrBooking.getApContactPhones().stream()
							.filter(contact -> contact != null && !CollectionUtils.isEmpty(contact.getPassengerIds())
									&& contact.getPassengerIds().contains(passenger.getPassengerId()))
							.findFirst().orElse(null));
			if(pnrContactPhone != null){
				populateContactPhone(passenger, pnrContactPhone);
				return;
			}
		}
		
		//build contact info by member profile 
		buildContactByMemProfile(passenger, loginInfo);
	}

	/**
	 * 
	* @Description build contact by member profile
	* @param passenger
	* @param loginInfo
	* @return void
	* @author haiwei.jia
	 */
	private void buildContactByMemProfile(Passenger passenger, LoginInfo loginInfo) {
		if (passenger == null || loginInfo == null || BooleanUtils.isNotTrue(passenger.getLoginMember())) {
			return;
		}

		if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())
				&& !StringUtils.isEmpty(loginInfo.getMemberId())) {

			ProfilePersonInfo memberprofile = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(),
					loginInfo.getMmbToken());
			if (Optional.ofNullable(memberprofile).map(ProfilePersonInfo::getContact).map(Contact::getPhoneInfo)
					.orElse(null) != null) {

				passenger.findContactInfo().findPhoneInfo()
						.setCountryCode(memberprofile.getContact().getPhoneInfo().getCountryCode());
				passenger.findContactInfo().findPhoneInfo()
						.setPhoneCountryNumber(memberprofile.getContact().getPhoneInfo().getPhoneCountryNumber());
				passenger.findContactInfo().findPhoneInfo()
						.setPhoneNo(memberprofile.getContact().getPhoneInfo().getPhoneNo());
				passenger.findContactInfo().findPhoneInfo()
						.setType(ContactInfoTypeEnum.MEMBER_PROFILE_CONTACT_INFO.getType());
			}
		}
	}

	/**
	 * 
	* @Description populate pnrContactPhone to passenger
	* @param passenger
	* @param pnrContactPhone
	* @return void
	* @author haiwei.jia
	 */
	private void populateContactPhone(Passenger passenger, RetrievePnrContactPhone pnrContactPhone) {
		if (!pnrContactPhone.isEmpty()) {
			PhoneInfoDTO phoneInfo = BizRulesUtil.parserPhoneNumber(pnrContactPhone.getPhoneNumber());
			// If our validation failed, do not pre-populate anything 
			if (bookingBuildHelper.isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.CONTACT)) {
				passenger.findContactInfo().findPhoneInfo().setCountryCode(phoneInfo.getCountryCode());
				passenger.findContactInfo().findPhoneInfo().setPhoneCountryNumber(phoneInfo.getPhoneCountryNumber());
				passenger.findContactInfo().findPhoneInfo().setPhoneNo(phoneInfo.getPhoneNo());				
			} else {
				passenger.findContactInfo().findPhoneInfo().setPhoneNo(phoneInfo.getPhoneCountryNumber() + phoneInfo.getPhoneNo());				
			}
			
			passenger.findContactInfo().findPhoneInfo().setOlssContact(pnrContactPhone.isOlssContact());
			if (OneAConstants.CTCM.equals(pnrContactPhone.getType())) {
				passenger.findContactInfo().findPhoneInfo().setType(ContactInfoTypeEnum.CTCX_CONTACT_INFO.getType());
			} else if (OneAConstants.APM.equals(pnrContactPhone.getType())
					|| OneAConstants.APH.equals(pnrContactPhone.getType())
					|| OneAConstants.APB.equals(pnrContactPhone.getType())) {
				passenger.findContactInfo().findPhoneInfo().setType(ContactInfoTypeEnum.APX_CONTACT_INFO.getType());
			}
		}
	}
	
	/**
	 * 
	* @Description populate pnrEmail to passenger
	* @param passenger
	* @param pnrEmail
	* @param RetrievePnrEmail
	* @return void
	* @author haiwei.jia
	 */
	private void populateEmail(Passenger passenger, RetrievePnrEmail pnrEmail) {
		if (!pnrEmail.isEmpty()) {
			passenger.findContactInfo().findEmail().setEmailAddress(pnrEmail.getEmail());
			passenger.findContactInfo().findEmail().setOlssContact(pnrEmail.isOlssContact());
			if (OneAConstants.CTCE.equals(pnrEmail.getType())) {
				passenger.findContactInfo().findEmail().setType(ContactInfoTypeEnum.CTCX_CONTACT_INFO.getType());
			}
			else if (OneAConstants.APE.equals(pnrEmail.getType())){
				passenger.findContactInfo().findEmail().setType(ContactInfoTypeEnum.APX_CONTACT_INFO.getType());
			}
		}
	}

	/**
	 * Populate notification email
	 * @param passenger
	 * @param pnrEmail
	 */
	private void populateNotificationEmails(Passenger passenger, List<RetrievePnrEmail> pnrEmails) {
		if (pnrEmails != null && !pnrEmails.isEmpty()) {
			for(RetrievePnrEmail pnrEmail: pnrEmails) {
				Email email = new Email();
				email.setEmailAddress(pnrEmail.getEmail());
				email.setOlssContact(pnrEmail.isOlssContact());

				if (OneAConstants.CTCE.equals(pnrEmail.getType())) {
					email.setType(ContactInfoTypeEnum.CTCX_CONTACT_INFO.getType());
				}
				else if (OneAConstants.APE.equals(pnrEmail.getType())){
					email.setType(ContactInfoTypeEnum.APX_CONTACT_INFO.getType());
				}
				passenger.findContactInfo().findNotificationEmails().add(email);
			}
		}
	}

	/** 
	 * 
	 * @Description get first CX contact from pnrContactPhones
	 * @param pnrContactPhones
	 * @return RetrievePnrContactPhone
	 * @author haiwei.jia
	 */
	private RetrievePnrContactPhone getFirstCxContactPhone(List<RetrievePnrContactPhone> pnrContactPhones) {
		if(CollectionUtils.isEmpty(pnrContactPhones)){
			return null;
		}
		
		//get OLSS CX contact from contactPhones
		List<RetrievePnrContactPhone> olssCxContactPhones = getContactPhonesByCompanyId(getOlssContactPhones(pnrContactPhones), OneAConstants.CX_COMPANY);
		if(!CollectionUtils.isEmpty(olssCxContactPhones)){
			//get olss Cx Contact Phone with largest OT
			RetrievePnrContactPhone olssCxContactPhone = getContactPhoneWithLargestOt(olssCxContactPhones);
			if(olssCxContactPhone != null){
				return olssCxContactPhone;
			}
		}
		
		//get CX contact from contactPhones
		List<RetrievePnrContactPhone> cxContactPhones = getContactPhonesByCompanyId(pnrContactPhones,OneAConstants.CX_COMPANY);
		if(!CollectionUtils.isEmpty(cxContactPhones)){
			//get Cx Contact Phone with largest OT
			RetrievePnrContactPhone cxContactPhone = getContactPhoneWithLargestOt(cxContactPhones);
			if(cxContactPhone != null){
				return cxContactPhone;
			}
		}
		return null;
	}
	
	/** 
	 * 
	 * @Description get first contact from pnrContactPhones
	 * @param pnrContactPhones
	 * @return RetrievePnrContactPhone
	 */
	private RetrievePnrContactPhone getFirstContactPhone(List<RetrievePnrContactPhone> pnrContactPhones) {
		if(CollectionUtils.isEmpty(pnrContactPhones)){
			return null;
		}
		
		// Get contact phone with largest OT
		List<RetrievePnrContactPhone> contactPhones = getOlssContactPhones(pnrContactPhones);
		if(CollectionUtils.isEmpty(contactPhones)) {
			contactPhones = pnrContactPhones;
		}
		
		return getContactPhoneWithLargestOt(contactPhones);
	}

	/** 
	 * 
	 * @Description get first KA contact from pnrContactPhones
	 * @param pnrContactPhones
	 * @return RetrievePnrContactPhone
	 * @author haiwei.jia
	 */
	private RetrievePnrContactPhone getFirstKaContactPhone(List<RetrievePnrContactPhone> pnrContactPhones) {
		if(CollectionUtils.isEmpty(pnrContactPhones)){
			return null;
		}
		
		//get OLSS KA contact from contactPhones
		List<RetrievePnrContactPhone> olssKaContactPhones = getContactPhonesByCompanyId(getOlssContactPhones(pnrContactPhones), OneAConstants.COMPANY_KA);
		if(!CollectionUtils.isEmpty(olssKaContactPhones)){
			//get olss KA Contact Phone with largest OT
			RetrievePnrContactPhone olssKaContactPhone = getContactPhoneWithLargestOt(olssKaContactPhones);
			if(olssKaContactPhone != null){
				return olssKaContactPhone;
			}
		}
		
		//get KA contact from contactPhones
		List<RetrievePnrContactPhone> kaContactPhones = getContactPhonesByCompanyId(pnrContactPhones, OneAConstants.COMPANY_KA);
		if(!CollectionUtils.isEmpty(kaContactPhones)){
			//get KA Contact Phone with largest OT
			RetrievePnrContactPhone kaContactPhone = getContactPhoneWithLargestOt(kaContactPhones);
			if(kaContactPhone != null){
				return kaContactPhone;
			}
		}
		return null;
	}
	
	/** 
	 * 
	 * @Description get first email from pnrEmails
	 * @param pnrEmails
	 * @return RetrievePnrEmail
	 */
	private RetrievePnrEmail getFirstEmail(List<RetrievePnrEmail> pnrEmails) {
		if(CollectionUtils.isEmpty(pnrEmails)){
			return null;
		}
		
		List<RetrievePnrEmail> emails = getOlssEmails(pnrEmails);
		if (CollectionUtils.isEmpty(emails)) {
			emails = pnrEmails;
		}
		
		return getEmailWithLargestOt(pnrEmails);
	}
	
	/** 
	 * 
	 * @Description get first CX email from pnrEmails
	 * @param pnrEmails
	 * @return RetrievePnrEmail
	 * @author haiwei.jia
	 */
	private RetrievePnrEmail getFirstCxEmail(List<RetrievePnrEmail> pnrEmails) {
		if(CollectionUtils.isEmpty(pnrEmails)){
			return null;
		}
		
		//get OLSS CX email from pnrEmails
		List<RetrievePnrEmail> olssCxEmails = getEmailsByCompany(getOlssEmails(pnrEmails), OneAConstants.COMPANY_CX);
		if(!CollectionUtils.isEmpty(olssCxEmails)){
			//get OLSS CX email with largest OT
			RetrievePnrEmail olssCxEmail = getEmailWithLargestOt(olssCxEmails);
			if(olssCxEmail != null){
				return olssCxEmail;
			}
		}
		
		//get CX email from pnrEmails
		List<RetrievePnrEmail> cxEmails = getEmailsByCompany(pnrEmails, OneAConstants.COMPANY_CX);
		if(!CollectionUtils.isEmpty(cxEmails)){
			//get CX email with largest OT
			RetrievePnrEmail cxEmail = getEmailWithLargestOt(cxEmails);
			if(cxEmail != null){
				return cxEmail;
			}
		}
		return null;
	}
	
	/** 
	 * 
	 * @Description get first KA email from pnrEmails
	 * @param pnrEmails
	 * @return RetrievePnrEmail
	 * @author haiwei.jia
	 */
	private RetrievePnrEmail getFirstKaEmail(List<RetrievePnrEmail> pnrEmails) {
		if(CollectionUtils.isEmpty(pnrEmails)){
			return null;
		}
		
		//get OLSS KA email from pnrEmails
		List<RetrievePnrEmail> olssKaEmails = getEmailsByCompany(getOlssEmails(pnrEmails), OneAConstants.COMPANY_KA);
		if(!CollectionUtils.isEmpty(olssKaEmails)){
			//get OLSS CX email with largest OT
			RetrievePnrEmail olssKaEmail = getEmailWithLargestOt(olssKaEmails);
			if(olssKaEmail != null){
				return olssKaEmail;
			}
		}
		
		//get KA email from pnrEmails
		List<RetrievePnrEmail> kaEmails = getEmailsByCompany(pnrEmails, OneAConstants.COMPANY_KA);
		if(!CollectionUtils.isEmpty(kaEmails)){
			//get KA email with largest OT
			RetrievePnrEmail kaEmail = getEmailWithLargestOt(kaEmails);
			if(kaEmail != null){
				return kaEmail;
			}
		}
		return null;
	}

	/**
	 * 
	 * @Description find CTCM contact phones from pnrContactPhones
	 * @param pnrContactPhones
	 * @return List<RetrievePnrContactPhone>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrContactPhone> getCTCMContactPhones(List<RetrievePnrContactPhone> pnrContactPhones){
		if(CollectionUtils.isEmpty(pnrContactPhones)){
			return new ArrayList<>();
		}
		return pnrContactPhones.stream().filter(contactPhone -> OneAConstants.CTCM.equals(contactPhone.getType())).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @Description find olss contact phones from pnrContactPhones
	 * @param pnrContactPhones
	 * @return List<RetrievePnrContactPhone>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrContactPhone> getOlssContactPhones(List<RetrievePnrContactPhone> pnrContactPhones){
		if(CollectionUtils.isEmpty(pnrContactPhones)){
			return new ArrayList<>();
		}
		return pnrContactPhones.stream().filter(contactPhone -> contactPhone.isOlssContact()).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @Description find contact phones from pnrContactPhones by company id
	 * @param pnrContactPhones
	 * @return List<RetrievePnrContactPhone>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrContactPhone> getContactPhonesByCompanyId(List<RetrievePnrContactPhone> pnrContactPhones, String companyId) {
		if (CollectionUtils.isEmpty(pnrContactPhones) || StringUtils.isEmpty(companyId)) {
			return new ArrayList<>();
		}
		return pnrContactPhones.stream()
				.filter(contactPhone -> companyId.equals(contactPhone.getCompanyId()))
				.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @Description find contact phone with largest OT from pnrContactPhones
	 * @param pnrContactPhones
	 * @return List<RetrievePnrContactPhone>
	 * @author haiwei.jia
	 */
	private RetrievePnrContactPhone getContactPhoneWithLargestOt(List<RetrievePnrContactPhone> pnrContactPhones){
		if (CollectionUtils.isEmpty(pnrContactPhones)) {
			return null;
		}
		Optional<RetrievePnrContactPhone> pnrContactPhone = pnrContactPhones.stream().sorted((contactPhone1, contactPhone2) -> contactPhone2.getQualifierId().compareTo(contactPhone1.getQualifierId())).findFirst();
		if(pnrContactPhone.isPresent()){
			return pnrContactPhone.get();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @Description find CTCE email address from pnrEmails
	 * @param pnrEmails
	 * @return List<RetrievePnrEmail>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrEmail> getCTCEEmails(List<RetrievePnrEmail> pnrEmails){
		if(CollectionUtils.isEmpty(pnrEmails)){
			return new ArrayList<>();
		}
		return pnrEmails.stream().filter(email -> OneAConstants.CTCE.equals(email.getType())).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @Description find olss email address from pnrEmails
	 * @param pnrEmails
	 * @return List<RetrievePnrEmail>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrEmail> getOlssEmails(List<RetrievePnrEmail> pnrEmails){
		if(CollectionUtils.isEmpty(pnrEmails)){
			return new ArrayList<>();
		}
		return pnrEmails.stream().filter(RetrievePnrEmail :: isOlssContact).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @Description find email address from pnrEmails by company id
	 * @param pnrEmails
	 * @return List<RetrievePnrEmail>
	 * @author haiwei.jia
	 */
	private List<RetrievePnrEmail> getEmailsByCompany(List<RetrievePnrEmail> pnrEmails, String companyId) {
		if (CollectionUtils.isEmpty(pnrEmails) || StringUtils.isEmpty(companyId)) {
			return new ArrayList<>();
		}
		return pnrEmails.stream()
				.filter(email -> companyId.equals(email.getCompanyId()))
				.collect(Collectors.toList());
	}	
	
	/**
	 * 
	 * @Description find email address with largest OT from pnrEmails
	 * @param pnrEmails
	 * @return List<RetrievePnrEmail>
	 * @author haiwei.jia
	 */
	private RetrievePnrEmail getEmailWithLargestOt(List<RetrievePnrEmail> pnrEmails){
		if (CollectionUtils.isEmpty(pnrEmails)) {
			return null;
		}
		Optional<RetrievePnrEmail> pnrEmail = pnrEmails.stream().sorted((email1, email2) -> email2.getQualifierId().compareTo(email1.getQualifierId())).findFirst();
		if(pnrEmail.isPresent()){
			return pnrEmail.get();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @Description get customer email info from member profile by member id
	 * @param memberId
	 * @return String
	 * @author haiwei.jia
	 */
	private String getCustomerEmailInfoFromProfile(String memberId,String mbToken) {
		ProfilePersonInfo memberprofile = retrieveProfileService.retrievePersonInfo(memberId, mbToken);
		String memberEmail = null;
		if (memberprofile != null && memberprofile.getContact() != null  ) {
			memberEmail = memberprofile.getContact().getEmail().getEmail();

		}
		return memberEmail;
	}

	/**
	 * build passenger title
	 * 
	 * @param pnrPassenger
	 * @return
	 */
	private void buildPassengerTitle(Passenger passenger) {
		List<String> nameTitles = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		String givenName = passenger.getGivenName();
		
		String title = BookingBuildUtil.retrievePassengerTitle(givenName, nameTitles);
		passenger.setTitle(title);
		passenger.setGivenName(BookingBuildUtil.removeTitleFromGivenName(givenName, title));
	}

	/**
	 * build TravelDoc
	 * 
	 * @param pnrTravelDoc
	 * @param travelDoc
	 * @param map 
	 */
	private void buildTravelDoc(RetrievePnrTravelDoc pnrTravelDoc, TravelDoc travelDoc, Map<String, String> map) {
		if(pnrTravelDoc == null){
			return;
		}
		if(travelDoc == null) {
			travelDoc = new TravelDoc();
		}
		travelDoc.setBirthDateDay(pnrTravelDoc.getBirthDateDay());
		travelDoc.setBirthDateMonth(pnrTravelDoc.getBirthDateMonth());
		travelDoc.setBirthDateYear(pnrTravelDoc.getBirthDateYear());
		travelDoc.setCompanyId(pnrTravelDoc.getCompanyId());
		if(StringUtils.length(pnrTravelDoc.getCountryOfIssuance()) == 2){
			travelDoc.setCountryOfIssuance(nationalityCodeService.findThreeCountryCodeByTwoCountryCode(pnrTravelDoc.getCountryOfIssuance()));
		}else{
			travelDoc.setCountryOfIssuance(pnrTravelDoc.getCountryOfIssuance());
		}
		
		travelDoc.setExpiryDateDay(pnrTravelDoc.getExpiryDateDay());
		travelDoc.setExpiryDateMonth(pnrTravelDoc.getExpiryDateMonth());
		travelDoc.setExpiryDateYear(pnrTravelDoc.getExpiryDateYear());
		travelDoc.setFamilyName(pnrTravelDoc.getFamilyName());
		travelDoc.setGender(pnrTravelDoc.getGender());
		travelDoc.setGivenName(pnrTravelDoc.getGivenName());
		travelDoc.setInfant(pnrTravelDoc.isInfant());
		if(StringUtils.length(pnrTravelDoc.getNationality()) == 2){
			travelDoc.setNationality(nationalityCodeService.findThreeCountryCodeByTwoCountryCode(pnrTravelDoc.getNationality()));
		}else{
			travelDoc.setNationality(pnrTravelDoc.getNationality());
		}
		travelDoc.setTravelDocumentNumber(pnrTravelDoc.getTravelDocumentNumber());
		travelDoc.setTravelDocumentType(pnrTravelDoc.getTravelDocumentType());
		travelDoc.setQualifierId(pnrTravelDoc.getQualifierId());
	}
 
	/**
	 * build FQTVInfo
	 * @param requireFqtvHolidayCheck 
	 * @param futureCheckMemberHoliday 
	 * 
	 * @param pnrFQTVInfo
	 * @param fqtvInfo
	 */
	private void buildFQTVInfo(RetrievePnrPassengerSegment pnrPassengerSegment,RetrievePnrBooking pnrBooking,PassengerSegment passengerSegment, Future<Map<String, ProfilePersonInfo>> futureCheckMemberHoliday, boolean requireFqtvHolidayCheck) 
	 throws BusinessBaseException{
		List<String> mpos= bizRuleConfig.getCxkaTierLevel();
		List<String> ams = bizRuleConfig.getAmTierLevel();
		List<String> oneWorlds = bizRuleConfig.getOneworldTierLevel();
		List<RetrievePnrFFPInfo> pnrFQTVInfos = pnrPassengerSegment.getFQTVInfos();
		Map<String, ProfilePersonInfo> memberHolidayCheck = null;
		if(requireFqtvHolidayCheck){
			try {
				memberHolidayCheck = futureCheckMemberHoliday.get();
			} catch (Exception e) {
				logger.error("Fail to retrieve Member Holiday Check",e);
			}
		}
		//If FQTV info in product level is empty, get it from customer level
		if(CollectionUtils.isEmpty(pnrFQTVInfos)){
			String marketCompany = Optional.ofNullable(pnrBooking.getSegments()).orElse(new ArrayList<>()).stream()
					.filter(seg -> ObjectUtils.equals(seg.getSegmentID(), pnrPassengerSegment.getSegmentId())).map(RetrievePnrSegment :: getMarketCompany)
					.findFirst().orElse(null);
			RetrievePnrFFPInfo pnrFQTVInfo = getCusLvlFQTVInfo(pnrPassengerSegment.getPassengerId(), pnrBooking.getPassengers(), marketCompany);
			if(pnrFQTVInfo != null){
				FQTVInfo fqtvInfo = new FQTVInfo();
				fqtvInfo.setCompanyId(pnrFQTVInfo.getFfpCompany());
				fqtvInfo.setMembershipNumber(pnrFQTVInfo.getFfpMembershipNumber());
				fqtvInfo.setTierLevel(pnrFQTVInfo.getTierLevel());
				fqtvInfo.setTopTier(pnrFQTVInfo.isTopTier());
				fqtvInfo.setProductLevel(false);
				fqtvInfo.setAm(ams.contains(fqtvInfo.getTierLevel()));
				fqtvInfo.setMpo(mpos.contains(fqtvInfo.getTierLevel()));
				fqtvInfo.setOneWorld(oneWorlds.contains(fqtvInfo.getTierLevel()));
				fqtvInfo.setQualifierId(pnrFQTVInfo.getQualifierId());
				setFqtvHolidayCheck(requireFqtvHolidayCheck, memberHolidayCheck, pnrFQTVInfo.getFfpMembershipNumber(), fqtvInfo, fqtvInfo.getMpo());
				passengerSegment.setFqtvInfo(fqtvInfo);
			}
		} else {
			//get the CX FQTV first, if there is no CX FQTV, get the first other FQTV
			RetrievePnrFFPInfo retrievePnrFQTVInfo = pnrFQTVInfos.stream().filter(fqtv -> OneAConstants.CX_COMPANY.equals(fqtv.getFfpCompany())).findFirst()
					.orElse(pnrFQTVInfos.stream().findFirst().orElse(null));
			if(retrievePnrFQTVInfo != null) {
				FQTVInfo fqtvInfo = new FQTVInfo();
				fqtvInfo.setCompanyId(retrievePnrFQTVInfo.getFfpCompany());
				fqtvInfo.setMembershipNumber(retrievePnrFQTVInfo.getFfpMembershipNumber());
				fqtvInfo.setTierLevel(retrievePnrFQTVInfo.getTierLevel());
				fqtvInfo.setTopTier(retrievePnrFQTVInfo.isTopTier());
				fqtvInfo.setProductLevel(true);
				fqtvInfo.setAm(ams.contains(fqtvInfo.getTierLevel()));
				fqtvInfo.setMpo(mpos.contains(fqtvInfo.getTierLevel()));
				fqtvInfo.setOneWorld(oneWorlds.contains(fqtvInfo.getTierLevel()));
				setFqtvHolidayCheck(requireFqtvHolidayCheck, memberHolidayCheck, retrievePnrFQTVInfo.getFfpMembershipNumber(), fqtvInfo, fqtvInfo.getMpo());
				passengerSegment.setFqtvInfo(fqtvInfo);
			}			
		}
	}

	private void setFqtvHolidayCheck(boolean requireFqtvHolidayCheck, Map<String, ProfilePersonInfo> memberHolidayCheck,
			String membershipNumber, FQTVInfo fqtvInfo, Boolean isMPO) throws BusinessBaseException{
		if(requireFqtvHolidayCheck && !CollectionUtils.isEmpty(memberHolidayCheck)){
			if(null != memberHolidayCheck.get(membershipNumber)){
				fqtvInfo.setOnHoliday(memberHolidayCheck.get(membershipNumber).getOnHoliday());
				fqtvInfo.setHolidayStartDate(memberHolidayCheck.get(membershipNumber).getHolidayStartDate());
				fqtvInfo.setHolidayEndDate(memberHolidayCheck.get(membershipNumber).getHolidayEndDate());
			} else if (isMPO){
				throw new UnexpectedException("Fail to retrieve Member Holiday Check for Member:" + membershipNumber, new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
			}
		}
	}
	/**
	 * build FQTVInfo
	 * 
	 * @param pnrFQTVInfo
	 * @param fqtvInfo
	 *//*
	private void buildFQTUInfo(RetrievePnrPassengerSegment pnrPassengerSegment,RetrievePnrBooking pnrBooking,PassengerSegment passengerSegment) {
		List<RetrievePnrFQTUInfo> pnrFQTUInfos = pnrPassengerSegment.getFQTUInfos();
		//If FQTU info in product level is empty, get it from customer level
		if(CollectionUtils.isEmpty(pnrFQTUInfos)){
			RetrievePnrFQTVInfo pnrFQTVInfo = getCusLvlFQTVInfo(pnrPassengerSegment.getPassengerId(), pnrBooking.getPassengers());
			if(pnrFQTVInfo != null){
				FQTVInfo fqtvInfo = new FQTVInfo();
				fqtvInfo.setCompanyId(pnrFQTVInfo.getFqtvCompany());
				fqtvInfo.setMembershipNumber(pnrFQTVInfo.getFqtvMembershipNumber());
				fqtvInfo.setTierLevel(pnrFQTVInfo.getTierLevel());
				fqtvInfo.setTopTier(pnrFQTVInfo.isTopTier());
				fqtvInfo.setProductLevel(false);
				passengerSegment.setFQTVInfo(fqtvInfo);
			}
		} else {
			//get the CX FQTV first, if there is no CX FQTV, get the first other FQTV
			RetrievePnrFQTVInfo retrievePnrFQTVInfo = pnrFQTVInfos.stream().filter(fqtv -> OneAConstants.CX_COMPANY.equals(fqtv.getFqtvCompany())).findFirst()
					.orElse(pnrFQTVInfos.stream().findFirst().orElse(null));
			if(retrievePnrFQTVInfo != null) {
				FQTVInfo fqtvInfo = new FQTVInfo();
				fqtvInfo.setCompanyId(retrievePnrFQTVInfo.getFqtvCompany());
				fqtvInfo.setMembershipNumber(retrievePnrFQTVInfo.getFqtvMembershipNumber());
				fqtvInfo.setTierLevel(retrievePnrFQTVInfo.getTierLevel());
				fqtvInfo.setTopTier(retrievePnrFQTVInfo.isTopTier());
				fqtvInfo.setProductLevel(true);
				passengerSegment.setFQTVInfo(fqtvInfo);
			}			
		}
	}
*/
	
	/**
	 * build FQTRInfo
	 * @param requireFqtvHolidayCheck 
	 * @param futureCheckMemberHoliday 
	 * 
	 * @param pnrFQTVInfo
	 * @param fqtvInfo
	 */
	private void buildFQTRInfo(RetrievePnrPassengerSegment pnrPassengerSegment,RetrievePnrBooking pnrBooking,PassengerSegment passengerSegment) {
		List<String> mpos= bizRuleConfig.getCxkaTierLevel();
		List<String> ams = bizRuleConfig.getAmTierLevel();
		List<String> oneWorlds = bizRuleConfig.getOneworldTierLevel();
		List<RetrievePnrFFPInfo> pnrFQTRInfos = pnrPassengerSegment.getFQTRInfos();

		//If FQTV info in product level is empty, get it from customer level
		if(CollectionUtils.isEmpty(pnrFQTRInfos)){
			RetrievePnrFFPInfo pnrFQTRInfo = getCusLvlFQTRInfo(pnrPassengerSegment.getPassengerId(), pnrBooking.getPassengers());
			if(pnrFQTRInfo != null){
				FQTVInfo fqtrInfo = new FQTVInfo();
				fqtrInfo.setCompanyId(pnrFQTRInfo.getFfpCompany());
				fqtrInfo.setMembershipNumber(pnrFQTRInfo.getFfpMembershipNumber());
				fqtrInfo.setTierLevel(pnrFQTRInfo.getTierLevel());
				fqtrInfo.setTopTier(pnrFQTRInfo.isTopTier());
				fqtrInfo.setProductLevel(false);
				fqtrInfo.setAm(ams.contains(fqtrInfo.getTierLevel()));
				fqtrInfo.setMpo(mpos.contains(fqtrInfo.getTierLevel()));
				fqtrInfo.setOneWorld(oneWorlds.contains(fqtrInfo.getTierLevel()));
				fqtrInfo.setQualifierId(pnrFQTRInfo.getQualifierId());
				passengerSegment.setFqtrInfo(fqtrInfo);
			}
		} else {
			//get the CX FQTR first, if there is no CX FQTR, get the first other FQTR
			RetrievePnrFFPInfo retrievePnrFQTVInfo = pnrFQTRInfos.stream().filter(fqtr -> OneAConstants.CX_COMPANY.equals(fqtr.getFfpCompany())).findFirst()
					.orElse(pnrFQTRInfos.stream().findFirst().orElse(null));
			if(retrievePnrFQTVInfo != null) {
				FQTVInfo fqtrInfo = new FQTVInfo();
				fqtrInfo.setCompanyId(retrievePnrFQTVInfo.getFfpCompany());
				fqtrInfo.setMembershipNumber(retrievePnrFQTVInfo.getFfpMembershipNumber());
				fqtrInfo.setTierLevel(retrievePnrFQTVInfo.getTierLevel());
				fqtrInfo.setTopTier(retrievePnrFQTVInfo.isTopTier());
				fqtrInfo.setProductLevel(true);
				fqtrInfo.setAm(ams.contains(fqtrInfo.getTierLevel()));
				fqtrInfo.setMpo(mpos.contains(fqtrInfo.getTierLevel()));
				fqtrInfo.setOneWorld(oneWorlds.contains(fqtrInfo.getTierLevel()));
				passengerSegment.setFqtrInfo(fqtrInfo);
			}			
		}
	}
	
	/**
	 * 
	 * @Description get first order custom level FQTV info of the passenger
	 * @param passengerId
	 * @param passengers
	 * @return void
	 * @author haiwei.jia
	 * @param marketCompany 
	 */
	private RetrievePnrFFPInfo getCusLvlFQTVInfo(String passengerId, List<RetrievePnrPassenger> pnrPassengers, String marketCompany) {
		if (StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(pnrPassengers)) {
			return null;
		}
		//get passenger by passengerId
		RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrPassengers, passengerId);
		if(pnrPassenger == null || CollectionUtils.isEmpty(pnrPassenger.getFQTVInfos())){
			return null;
		}
		
		//custom level FQTV info list of passenger
		List<RetrievePnrFFPInfo> pnrFQTVInfos = pnrPassenger.getFQTVInfos();
		return pnrFQTVInfos.stream()
				.filter(fqtv -> fqtv != null && OneAConstants.CX_COMPANY.equals(fqtv.getFfpCompany()))
				// use the market company matched FQTV first
				.sorted((f1, f2) -> compareByCompanyMatching(f1, f2, marketCompany)).findFirst()
				.orElse(pnrFQTVInfos.stream().filter(fqtv -> fqtv != null)
						// use the market company matched FQTV first
						.sorted((f1, f2) -> compareByCompanyMatching(f1, f2, marketCompany)).findFirst().orElse(null));
	}
	
	/**
	 * Compare FQTV by company id
	 * @param f1
	 * @param f2
	 * @param marketCompany
	 * @return int
	 */
	private int compareByCompanyMatching(RetrievePnrFFPInfo f1, RetrievePnrFFPInfo f2, String marketCompany) {
		if (!StringUtils.isEmpty(f2.getCompanyId()) && f2.getCompanyId().equals(marketCompany)) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * @Description get first order custom level FQTR info of the passenger
	 * @param passengerId
	 * @param passengers
	 * @return void
	 * @author haiwei.jia
	 */
	private RetrievePnrFFPInfo getCusLvlFQTRInfo(String passengerId, List<RetrievePnrPassenger> pnrPassengers) {
		if (StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(pnrPassengers)) {
			return null;
		}
		//get passenger by passengerId
		RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrPassengers, passengerId);
		if(pnrPassenger == null || CollectionUtils.isEmpty(pnrPassenger.getFQTRInfos())){
			return null;
		}
		
		//custom level FQTR info list of passenger
		List<RetrievePnrFFPInfo> pnrFQTRInfos = pnrPassenger.getFQTRInfos();
		return pnrFQTRInfos.stream().filter(fqtr -> fqtr != null && OneAConstants.CX_COMPANY.equals(fqtr.getFfpCompany())).findFirst()
				.orElse(pnrFQTRInfos.stream().filter(fqtr -> fqtr != null).findFirst().orElse(null));
	}
	
	/**
	 * filter and build segment
	 * 
	 * @param pnrSegmentList
	 * @return
	 * @throws ExpectedException 
	 */
	private List<Segment> buildFlightInfo(RetrievePnrBooking pnrBooking, BookingBuildRequired required) throws BusinessBaseException {
		List<TravelDocOD> travelDocOds = tbTravelDocOdCacheHelper.findByAppCodeInAndStartDateBefore(buildAppCodeParam(MMBConstants.APP_CODE),new java.sql.Date(System.currentTimeMillis()));

		List<TravelDocList> travelDocList = travelDocListCacheHelper.findAll();
		Map<String, String> cabinClassMap = cabinClassDAO.findByAppCode(MMBConstants.APP_CODE).stream()
				.collect(Collectors.toMap(CabinClass::getSubclass, CabinClass::getBasicClass));
		List<Segment> segments = new ArrayList<>();
		// build segment info
		for (RetrievePnrSegment pnrSegment : pnrBooking.getSegments()) {
			Segment segment = new Segment();
			
			if(required.operateInfoAndStops()) {
				populateFromFlightInfo(segment, pnrSegment);
			}
			
			String originPortOffset = airportTimeZoneService.getAirPortTimeZoneOffset(pnrSegment.getOriginPort());
			if (StringUtils.isEmpty(originPortOffset)) {
				logger.warn(String.format("Cannot find available timezone for originPort:%s", pnrSegment.getOriginPort()));
			}
			
			String destPortOffset = airportTimeZoneService.getAirPortTimeZoneOffset(pnrSegment.getDestPort());
			if (StringUtils.isEmpty(destPortOffset)) {
				logger.warn(String.format("Cannot find available timezone for destPort:%s", pnrSegment.getDestPort()));
			}
			// build time
			segment.setArrivalTime(BookingBuildUtil.buildDepartArrivalTime(pnrSegment.getArrivalTime()));
			segment.setDepartureTime(BookingBuildUtil.buildDepartArrivalTime(pnrSegment.getDepartureTime()));
			// build time zone
			setTimeZone(originPortOffset, destPortOffset, segment);
			BookingStatus bookingStatus = bookingBuildHelper.getFirstAvailableStatus(pnrSegment.getStatus());
			
			String status = bookingStatus==null? null:bookingStatus.getStatusCode();
			if (StringUtils.isEmpty(status)) {
				logger.warn(String.format("Cannot find available status for flight:%s%s", pnrSegment.getPnrOperateCompany(), pnrSegment.getPnrOperateSegmentNumber()));
			}
			
			if (StringUtils.isEmpty(originPortOffset) || StringUtils.isEmpty(destPortOffset) || StringUtils.isEmpty(status)) {
				logger.warn(String.format("Cannot find time zone offset of segment:%s%s, will remove this segment from booking.", pnrSegment.getPnrOperateCompany(), pnrSegment.getPnrOperateSegmentNumber()));
				continue;
			}

			// copy segment info from pnrSegment to Segment
			segment.setAirCraftType(pnrSegment.getAirCraftType());
			segment.setCabinClass(getCabinClassBySubClass(cabinClassMap,pnrSegment.getSubClass()));
			segment.setSubClass(pnrSegment.getSubClass());
			segment.setMarketCabinClass(getCabinClassBySubClass(cabinClassMap,pnrSegment.getMarketSubClass()));
			segment.setMarketSubClass(pnrSegment.getMarketSubClass());
			segment.setDestPort(pnrSegment.getDestPort());
			segment.setDestTerminal(pnrSegment.getDestTerminal());
			segment.setOriginTerminal(pnrSegment.getOriginTerminal());
			segment.setOriginPort(pnrSegment.getOriginPort());
			segment.setSegmentID(pnrSegment.getSegmentID());
			segment.setOperateCompany(pnrSegment.getPnrOperateCompany());
			segment.setMarketCompany(pnrSegment.getMarketCompany());
			segment.setMarketSegmentNumber(pnrSegment.getMarketSegmentNumber());
			segment.setOperateSegmentNumber(pnrSegment.getPnrOperateSegmentNumber());
			//port country code
			segment.setDestCountry(getCountryCodeByAirPortCode(pnrSegment.getDestPort()));
			segment.setOriginCountry(getCountryCodeByAirPortCode(pnrSegment.getOriginPort()));
			//Train reminder
			segment.setTrainReminder(pnrSegment.getTrainReminder());
			//build segment apiVersion
			segment.setApiVersion(buildSegmentApiVersion(segment.getOriginCountry(), segment.getDestCountry(), travelDocOds));
			//set usable travel docs
			setUsableTavelDocs(segment, travelDocList);

			//re-book information
			segment.setRebookInfo(pnrSegment.getRebookInfo());
			segment.setUpgradeInfo(buildUpgradeInfo(cabinClassMap, pnrSegment, segment.getDepartureTime()));
			// get flightStatus from rtfs
			if (required.rtfs() && ("CX".equalsIgnoreCase(segment.getOperateCompany())
					|| "KA".equalsIgnoreCase(segment.getOperateCompany()))) {
				List<FlightStatusData> flightStatusDataList = bookingBuildHelper.getFlightStatus(segment);
				if (!CollectionUtils.isEmpty(flightStatusDataList)) {
					// build rtfs flightStatus
					bookingBuildHelper.populateRTFSStatus(segment, flightStatusDataList);
					// build rtfs detail time
					bookingBuildHelper.populateFlightDetailTime(segment, flightStatusDataList);
					// build rtfs summary, which is always returned without filter 
					bookingBuildHelper.populateRtfsSummary(segment, flightStatusDataList);
				  // build rtfs wifi
					populateFlightWifi(segment, flightStatusDataList);
					// build gateNumber in segment
					bookingBuildHelper.populateGateNumber(segment,flightStatusDataList);
				}
			}
			
			//segment status
			SegmentStatus segmentStatus = BookingBuildUtil.generateFlightStatus(segment.getDepartureTime(), pnrSegment.getStatus(), bookingStatusDAO.findAvailableStatus(MMBConstants.APP_CODE), bookingStatusCOnfig, flightPassedTime);
			segment.setSegmentStatus(segmentStatus);
			
			segment.setHaulType(getHaulType(segment));
			//check-in window info
			bookingBuildHelper.populateChekinWindow(segment, pnrBooking.isStaffBooking()); 
			segments.add(segment);
		}
		if (CollectionUtils.isEmpty(segments)) {
			throw new ExpectedException("All Segment status ineligible.", new ErrorInfo(ErrorCodeEnum.ERR_FILTER_INELIGIBLE_BOOKING_STATUS));
		}
		if(required.rtfs() && isWholeFlightsFlownThreeDays(segments)){
			throw new ExpectedException("All flight flown before 3 days ago.", new ErrorInfo(ErrorCodeEnum.ERR_ALL_FLIGHT_FLOWN_BEFOR_LIMIT_TIME));
		}
		return segments;
	}
	
	/**
	 * Build app code query parameters by adding wildcard option.
	 *
	 * @param appCode
	 * @return 
	 */
	private List<String> buildAppCodeParam (String appCode){
		List<String> paramList = new ArrayList<>();
		if(!StringUtils.isEmpty(appCode)){
			paramList.add(appCode);
		}
		paramList.add(TBConstants.APP_CODE_WILDCARD);
		return paramList;
	}
	
	/**
	 * get
	 * @param segment2
	 * @return
	 */
	private String getHaulType(Segment segment) {
		List<TbFlightHaul> tbFlightHauls = tbFlightHaulDAO.findAll();
		if (CollectionUtils.isEmpty(tbFlightHauls)) {
			return null;
		}
		String originPort = segment.getOriginPort();
		String destPort = segment.getDestPort();
		String operateCompany = segment.getOperateCompany();
		
		// optCx and optKa
		String optCx = OneAConstants.COMPANY_CX.equals(operateCompany) ? TBConstants.TB_FLIGHT_HAUL_OPT_CX_EXIST
				: TBConstants.TB_FLIGHT_HAUL_OPT_CX_NON_EXIST;	
		String optKa = OneAConstants.COMPANY_KA.equals(operateCompany) ? TBConstants.TB_FLIGHT_HAUL_OPT_KA_EXIST
				: TBConstants.TB_FLIGHT_HAUL_OPT_KA_NON_EXIST;
		
		return tbFlightHaulDAO.findHaulType(MMBConstants.APP_CODE, originPort, destPort, optCx, optKa);
	}

	/**
	 * build upgrade info
	 * @param cabinClassMap
	 * @param pnrSegment
	 * @param departureTime 
	 * @return
	 */
	private UpgradeInfo buildUpgradeInfo(Map<String, String> cabinClassMap,RetrievePnrSegment pnrSegment, DepartureArrivalTime departureTime){
		UpgradeInfo result = null;
		RetrievePnrUpgradeInfo pnrUpgradeInfo= pnrSegment.getUpgradeInfo(); 
		if (pnrSegment.getUpgradeInfo() != null) {
			result = new UpgradeInfo();
			result.setFromSubClass(pnrUpgradeInfo.getFromSubClass());
			result.setToSubClass(pnrUpgradeInfo.getToSubClass());
			result.setFromCabinClass(getCabinClassBySubClass(cabinClassMap, pnrUpgradeInfo.getFromSubClass()));
			result.setToCabinClass(getCabinClassBySubClass(cabinClassMap, pnrUpgradeInfo.getToSubClass()));
			result.setRedUpgradeInfo(pnrUpgradeInfo.getRedUpgradeInfo());
			result.setBidUpgradeInfo(buildBidUpgradeInfo(pnrUpgradeInfo.getBidUpgradeInfo(), departureTime));
			result.setBookableUpgradeInfo(pnrUpgradeInfo.getBookableUpgradeInfo());
			result.setUpgradeType(pnrUpgradeInfo.getUpgradeType());
		}
		
		return result;
	}

	/**
	 * build bid upgradeInfo
	 * @param bidUpgradeInfo
	 * @param departureTime 
	 * @return RetrievePnrBidUpgradeInfo
	 */
	private RetrievePnrBidUpgradeInfo buildBidUpgradeInfo(RetrievePnrBidUpgradeInfo bidUpgradeInfo, DepartureArrivalTime departureTime) {
		if(bidUpgradeInfo == null || bidUpgradeInfo.getStatus() == null 
				|| departureTime == null || StringUtils.isEmpty(departureTime.getScheduledTime())) {
			return null;
		}
		if(UpgradeBidStatus.REQUEST.getCode().equals(bidUpgradeInfo.getStatus().getCode())) {
			try {
				Date std = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, departureTime.getScheduledTime(), departureTime.getTimeZoneOffset());
				int hour = DateUtil.getDifferenceHours(std, new Date());
				if(hour - 48 <= 0) {
					return new RetrievePnrBidUpgradeInfo(UpgradeBidStatus.FAILURE);
				} else if(hour - 48 > 0 && hour - 50 <= 0) {
					return new RetrievePnrBidUpgradeInfo(UpgradeBidStatus.CALCULATE);
				}
			} catch (ParseException e) {
				logger.error(String.format("Error to convert departure time:[%s]", departureTime.getScheduledTime()));
			}
		}
		return bidUpgradeInfo;
	}


	/**
	 * 
	 * @Description populate operate company, stops and total duration info from AirFlightInfo service
	 * @param segment
	 * @param pnrSegment
	 * @return void
	 * @author haiwei.jia
	 * @throws ExpectedException 
	 */
	private void populateFromFlightInfo(Segment segment, RetrievePnrSegment pnrSegment) throws ExpectedException {
		if(pnrSegment.getDepartureTime() == null){
			return;
		}
			
		String departureTimeString = pnrSegment.getDepartureTime().getTime();
		AirFlightInfoBean airFlightInfoBean = airFlightInfoService.getAirFlightInfo(departureTimeString, pnrSegment.getOriginPort(),
				pnrSegment.getDestPort(), pnrSegment.getMarketCompany(), pnrSegment.getMarketSegmentNumber(), pnrSegment.getAirCraftType());
		/*if(airFlightInfoBean == null){
			//throw new ExpectedException("Cannot find air flight information", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW))
			return;
		}*/
		
		if(airFlightInfoBean == null && StringUtils.isEmpty(pnrSegment.getPnrOperateCompany())){
			segment.setUnConfirmedOperateInfo(true);
		}
		//set operate company info
		BookingBuildUtil.setOperateByCompanyAndFlightNumber(pnrSegment, airFlightInfoBean);
		//set stops and total duration info
		setStopsAndTotalDuration(segment, airFlightInfoBean);
		//set AdditionalOperatorInfo in Segment
		setAdditionalOperatorInfo(segment, airFlightInfoBean);

	}

	/**
	 * Set AdditionalOperatorInfo
	 * @param segment
	 * @param airFlightInfoBean
	 */
	private void setAdditionalOperatorInfo(Segment segment, AirFlightInfoBean airFlightInfoBean) {
		if (null != airFlightInfoBean && null != airFlightInfoBean.getAdditionalOperatorInfo() && StringUtils.isNotEmpty(airFlightInfoBean.getAdditionalOperatorInfo().getOperatorName())){
			AdditionalOperatorInfoModel model = additionalOperatorInfoDAO.findOne(new AdditionalOperatorInfoKey(MMBConstants.APP_CODE, airFlightInfoBean.getAdditionalOperatorInfo().getOperatorName()));
			AdditionalOperatorInfo additionalOperatorInfo = new AdditionalOperatorInfo();
			additionalOperatorInfo.setOperatorName(airFlightInfoBean.getAdditionalOperatorInfo().getOperatorName());
			if (null != model && StringUtils.isNotEmpty(model.getOperatorCode())){
				additionalOperatorInfo.setOperatorCode(model.getOperatorCode());
			}
			//no need to set this flag here because some time the DB has not additional information
			/*else{
				segment.setUnConfirmedOperateInfo(true);
			}*/
			segment.setAdditionalOperatorInfo(additionalOperatorInfo);
		}
	}

	/**
	 * populate usable travel docs 
	 * @param segment
	 * @param travelDocList
	 */
	private void setUsableTavelDocs(Segment segment, List<TravelDocList> travelDocList) {
		for (TravelDocList travelDoc : travelDocList) {
			// check api version 
			if (String.valueOf(travelDoc.getTravelDocVersion()).equals(segment.getApiVersion())) {
				// primary travle list
				if (TBConstants.TRAVEL_DOC_PRIMARY.equals(travelDoc.getTravelDocType())) {
					segment.getUsablePrimaryTavelDocs().add(travelDoc.getTravelDocCode());
				// secondary travle list
				} else if (TBConstants.TRAVEL_DOC_SECONDARY.equals(travelDoc.getTravelDocType())) {
					segment.getUsableSecondaryTavelDocs().add(travelDoc.getTravelDocCode());
				}
			}

		}
	}
	
	/**
	 * get counry code by air port code 
	 * @param portCode
	 * @return
	 */
	private String getCountryCodeByAirPortCode(String portCode){
		
		if(StringUtils.isNotEmpty(portCode) ){
			return aemService.getCountryCodeByPortCode(portCode);
		}
		return null;
	}
	/**
	 * get segment apiVersion by originPort & destPort
	 * TravelDocList
	 * @param originPort
	 * @param destPort
	 * @param travelDocOds
	 * @return segment apiVersion
	 */
	private String buildSegmentApiVersion(String originCountryCode, String destCountryCode, List<TravelDocOD> travelDocOds) {
		//dest >> orgin
		for(int i = 0; i < travelDocOds.size(); i++) {
			TravelDocOD travelDocOd = travelDocOds.get(i);
			String orgin = travelDocOd.getOrigin();
			String destination = travelDocOd.getDestination();
			if(StringUtils.equals(destination, destCountryCode) && (StringUtils.equals(orgin,originCountryCode) || TBConstants.TRAVEL_DOC_WILDCARD.equals(orgin))) {
				return String.valueOf(travelDocOd.getTravelDocVersion());
			}
		}
		//orgin >> dest
		for(int i = 0; i < travelDocOds.size(); i++) {
			TravelDocOD travelDocOd = travelDocOds.get(i);
			String orgin = travelDocOd.getOrigin();
			String destination = travelDocOd.getDestination();
			if(StringUtils.equals(orgin,originCountryCode) && (StringUtils.equals(destCountryCode,destination) || TBConstants.TRAVEL_DOC_WILDCARD.equals(destination))) {
				return String.valueOf(travelDocOd.getTravelDocVersion());
			}
		}
		//both value"**"
		for(int i = 0; i < travelDocOds.size(); i++) {
			TravelDocOD travelDocOd = travelDocOds.get(i);
			String orgin = travelDocOd.getOrigin();
			String destination = travelDocOd.getDestination();
			if(TBConstants.TRAVEL_DOC_WILDCARD.equals(destination) || TBConstants.TRAVEL_DOC_WILDCARD.equals(orgin)) {
				return String.valueOf(travelDocOd.getTravelDocVersion());
			}
		}
		
		return null;
	}

	/**
	 * get cabin class by subclass
	 * 
	 * @param cabinClassMap
	 * @param subClass
	 * @return cabin class
	 * @author haiwei.jia
	 */
	private String getCabinClassBySubClass(Map<String, String> cabinClassMap, String subClass) {
		//the map of cabin subclass and corresponding cabin class
		String result = null;
		if (StringUtils.isNotBlank(subClass)) {
			result = cabinClassMap.get(subClass);
		}
		return result;
	}

	/**
	 * Judge whether the whole flights is flown 3 days
	 * 
	 * @param segments
	 * @return boolean
	 */
	private boolean isWholeFlightsFlownThreeDays(List<Segment> segments) {
		ZonedDateTime localCurrentZoneTime = ZonedDateTime.now();
		for (Segment segment : segments) {
			if ((!segment.isFlown() && segment.getSegmentStatus() != null
					&& segment.getSegmentStatus().getStatus() != FlightStatusEnum.CANCELLED)
					|| !DateUtil.compareBetweenZonedDateTimeByHour(localCurrentZoneTime,
							segment.findDepartureTime().getTime(), segment.findDepartureTime().getTimeZoneOffset(),
							limithours)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * copy passenger info from pnr response model to booking model
	 * 
	 * @param loginInfo 
	 * @param booking
	 * @param pnrBooking
	 * @throws UnexpectedException 
	 * @throws ExpectedException 
	 */
	private void buildBookingInfo(LoginInfo loginInfo, Booking booking, RetrievePnrBooking pnrBooking) throws BusinessBaseException{
		booking.setOneARloc(pnrBooking.getOneARloc());
		booking.setGdsRloc(pnrBooking.getGdsRloc());
		booking.setOfficeTimezone(getTimezoneByOfficeId(pnrBooking.getOfficeId()));
		booking.setRpCityCode(getCityCodeByOfficeId(pnrBooking.getOfficeId()));
		booking.setEncryptedRloc(this.getEncryptedRloc(pnrBooking));
		booking.setPos(pnrBooking.getPos());
		booking.setPosForAep(getPosForAEP(pnrBooking));
		booking.setTposList(pnrBooking.getTposList());
		
		//build spnr
		booking.setSpnr(pnrBooking.getSpnr());
		
		if(pnrBooking.getBookingPackageInfo() != null) {
			RetrievePnrBookingPackageInfo pnrBookingPackageInfo = pnrBooking.getBookingPackageInfo();
			booking.setBookingPackageInfo(buildBookingPackageInfo(pnrBookingPackageInfo));
			booking.setTrpBooking(pnrBookingPackageInfo.isPackageBooking());//hotel/event only booking cannot find On 1A
			booking.setHasHotel(pnrBookingPackageInfo.isHasHotel());
			booking.setHasEvent(pnrBookingPackageInfo.isHasEvent());	
		}

		booking.setNonMiceGroupBooking(BookingBuildUtil.isNonMiceGroupBooking(pnrBooking.getSkList(),pnrBooking.getSsrList()));

		booking.setIbeBooking(bookingBuildHelper.isIBEBooking(pnrBooking.getOfficeId()));
		booking.setNdcBooking(bookingBuildHelper.isNDCBooking(pnrBooking.getOfficeId()));
		booking.setAppBooking(BookingBuildUtil.isAppBooking(pnrBooking.getSkList()));
		booking.setGdsBooking(BookingBuildUtil.isGDSBooking(pnrBooking.getOfficeId()));
		booking.setGccBooking(bookingBuildHelper.isGCCBooking(pnrBooking.getOfficeId()));
		booking.setMiceBooking(BookingBuildUtil.isMiceBooking(pnrBooking.getSkList()));
		booking.setBookingCreateInfo(pnrBooking.getBookingCreateInfo());
		booking.setHasFqtu(BookingBuildUtil.hasFqtu(pnrBooking));
		booking.setHasBkug(BookingBuildUtil.isBookingBKUG(pnrBooking.getSkList()));
		booking.setHasInsurance(BookingBuildUtil.hasInsurance(pnrBooking.getOsiList()));
		booking.setTicketPriceInfo(pnrBooking.getTicketPriceInfo());
		booking.setOsiBookingSite(BookingBuildUtil.getOsiBookingSite(pnrBooking));
		booking.setHasIssuedTicket(BookingBuildUtil.hasIssuedTicket(pnrBooking));
		booking.setHasIssuedAllTickets(BookingBuildUtil.hasIssuedAllTickets(pnrBooking));
		booking.setBookingOnhold(BookingBuildUtil.isBookingOnHold(pnrBooking));
		booking.setOnHoldRemarkInfo(pnrBooking.getOnHoldRemarkInfo());
		
		booking.setRebookMapping(buildRebookMapping(pnrBooking.getRebookMapping()));

		// GDS rebook mapping and cancelled flight
		booking.setAtciRebookMapping(buildRebookMapping(pnrBooking.getAtciRebookMapping()));
		booking.setAtciCancelledSegments(buildCancelledSegments(pnrBooking.getSegments(), pnrBooking.getAtciCancelledSegments(), pnrBooking.getOneARloc()));
		
		booking.setTempLinkedBooking(isLinkedBooking(loginInfo, pnrBooking));
		//R1 not support staff mice/group booking
//		if(booking.isStaffBooking()){
//			throw new ExpectedException(String.format("Staff booking:%s", pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_STAFF_GROUP_BOOKING_NOT_SUPPORT));
//		}

	}

	/**
	 * build cancelled segments
	 * @param pnrSegments 
	 * @param pnrAtciCancelledSegments
	 * @param oneARloc
	 * @return List<CancelledSegment>
	 */
	private List<AtciCancelledSegment> buildCancelledSegments(List<RetrievePnrSegment> pnrSegments, List<RetrievePnrAtciCancelledSegment> pnrAtciCancelledSegments, String oneARloc) {
		if (CollectionUtils.isEmpty(pnrAtciCancelledSegments)) {
			return null;
		}
		
		List<AtciCancelledSegment> atciCancelledSegments = new ArrayList<>();
		for (RetrievePnrAtciCancelledSegment pnrAtciCancelledSegment : pnrAtciCancelledSegments) {
			AtciCancelledSegment atciCancelledSegment = new AtciCancelledSegment();
			atciCancelledSegment.setOriginPort(pnrAtciCancelledSegment.getOriginPort());
			atciCancelledSegment.setDestPort(pnrAtciCancelledSegment.getDestPort());
			atciCancelledSegment.setOperateCompany(pnrAtciCancelledSegment.getOperateCompany());
			atciCancelledSegment.setOperateSegmentNumber(pnrAtciCancelledSegment.getOperateFlightNumber());
			atciCancelledSegment.setRebookInfo(pnrAtciCancelledSegment.getRebookInfo());
			atciCancelledSegment.setSegmentId(pnrAtciCancelledSegment.getSegmentId());
			atciCancelledSegment.setDepartureDate(pnrAtciCancelledSegment.getDepartureDate());
			atciCancelledSegments.add(atciCancelledSegment);
		}
		return atciCancelledSegments;
	}

	/**
	 * is linked booking
	 * 
	 * @param loginInfo
	 * @param booking
	 * @return
	 */
	private boolean isLinkedBooking(LoginInfo loginInfo, RetrievePnrBooking booking) {
		if(booking == null) {
			return false;
		}
		
		List<TempLinkedBooking> linkedBookings = linkTempBookingRepository.getLinkedBookings(loginInfo.getMmbToken());
		return linkedBookings.stream().anyMatch(lb -> lb != null && Objects.equals(booking.getOneARloc(), lb.getRloc()));
	}
	
	/**
	 * Build booking package info
	 * 
	 * @param pnrBookingPackageInfo
	 * @return
	 */
	private BookingPackageInfo buildBookingPackageInfo(RetrievePnrBookingPackageInfo pnrBookingPackageInfo) {
		if(pnrBookingPackageInfo == null) {
			return null;
		}
		
		BookingPackageInfo bookingPackageInfo = new BookingPackageInfo();
		bookingPackageInfo.setHasEvent(pnrBookingPackageInfo.isHasEvent());
		bookingPackageInfo.setHasFlight(pnrBookingPackageInfo.isHasFlight());
		bookingPackageInfo.setHasHotel(pnrBookingPackageInfo.isHasHotel());
		bookingPackageInfo.setMobBooking(pnrBookingPackageInfo.isMobBooking());
		bookingPackageInfo.setNdcBooking(pnrBookingPackageInfo.isNdcBooking());
		
		return bookingPackageInfo;
	}

	/**
	 * build rebookMapping
	 * 
	 * @param pnrRebookMappings
	 * @return List<RebookMapping>
	 */
	private List<RebookMapping> buildRebookMapping(List<RetrievePnrRebookMapping> pnrRebookMappings) {
		if(CollectionUtils.isEmpty(pnrRebookMappings)) {
			return null;
		}
		List<RebookMapping> rebookMappings = new ArrayList<>();
		for(RetrievePnrRebookMapping pnrRebookMapping : pnrRebookMappings) {
			RebookMapping rebookMapping = new RebookMapping();
			rebookMapping.setAcceptSegmentIds(pnrRebookMapping.getAcceptSegmentIds());
			rebookMapping.setCancelledSegmentIds(pnrRebookMapping.getCancelledSegmentIds());
			rebookMappings.add(rebookMapping);
		}
		return rebookMappings;
	}

	private String getEncryptedRloc(RetrievePnrBooking pnrBooking) throws UnexpectedException {
		return encryptionHelper.encryptMessage(pnrBooking.getOneARloc(), Encoding.BASE64, MMBBizruleConstants.IBE_KEY);
	}

	/**
	 * set time zone offset
	 * 
	 * @param originPortOffset
	 * @param destPortOffset
	 * @param segment
	 */
	public void setTimeZone(String originPortOffset, String destPortOffset, Segment segment) {

		if (segment.getArrivalTime() == null) {
			segment.setArrivalTime(new DepartureArrivalTime());
		}
		if (segment.getDepartureTime() == null) {
			segment.setDepartureTime(new DepartureArrivalTime());
		}
		segment.getArrivalTime().setTimeZoneOffset(destPortOffset);
		segment.getDepartureTime().setTimeZoneOffset(originPortOffset);
	}

	
	private void buildMemberAward(List<PassengerSegment> passengerSegments, Map<FQTVInfo, List<RetrievePnrSegment>> fqtvSegmentMap, RetrievePnrBooking pnrBooking) {
		Map<FQTVInfo, MemberAwardResponse> fqtvResponseMap = new HashMap<>();
		for(Entry<FQTVInfo, List<RetrievePnrSegment>> fqtvSegment: fqtvSegmentMap.entrySet()) { 
			try {
				MemberAwardResponse response = populateMemberAward(fqtvSegment.getValue(), fqtvSegment.getKey());
				if (response != null && ((response.getErrors() == null || response.getErrors().isEmpty()))){
					fqtvResponseMap.put(fqtvSegment.getKey(), response);
				}
			} catch (ParseException e) {
				logger.warn(String.format("Parse Date failed for member %s", fqtvSegment.getKey().getMembershipNumber()));
			}
		}
		for (PassengerSegment passengerSegment : passengerSegments) {			
			buildMemberAwardResponse(passengerSegment, fqtvResponseMap, pnrBooking);
		}
	}
	
	private void buildMemberAwardResponse(PassengerSegment passengerSegment, Map<FQTVInfo, MemberAwardResponse> fqtvResponseMap, RetrievePnrBooking pnrBooking) {
		if (passengerSegment.getFqtvInfo() != null) {
			if (fqtvResponseMap.get(passengerSegment.getFqtvInfo()) != null) {
				MemberAwardResponse response = fqtvResponseMap.get(passengerSegment.getFqtvInfo());
				RetrievePnrSegment pnrSegment = getPnrSegmentById(pnrBooking.getSegments(), passengerSegment.getSegmentId());
				if (pnrSegment != null) {
					try {
						Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, pnrSegment.getDepartureTime().getPnrTime());
						for (SectorDetailRecordInResponse responseDetail : response.getSectorDetailRecord()) {
							// if O/D, carrierCode, FlightDate are the same, set the member info for these passenger segment
							if (responseDetail.getOrigin().equals(pnrSegment.getOriginPort())
									&& responseDetail.getDestination().equals(pnrSegment.getDestPort())
									&& responseDetail.getCarrierCode().equals(StringUtils.isEmpty(pnrSegment.getMarketCompany()) ? pnrSegment.getPnrOperateCompany() : pnrSegment.getMarketCompany())
									&& responseDetail.getFlightDate().equals(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMYYYY, departureTime))) {
								passengerSegment.getMemberAward().setAsiaMiles(responseDetail.getBasicAwardMiles());
								passengerSegment.getMemberAward().setClubPoint(responseDetail.getBasicClubPoints());
								passengerSegment.getMemberAward().setTierBonus(responseDetail.getTierBonusMiles());
								passengerSegment.getMemberAward().setTierLevel(passengerSegment.getFqtvInfo().getTierLevel());
							}
						}
					} catch (ParseException e) {
						logger.warn(String.format("Parse Date failed for member %s", passengerSegment.getFqtvInfo().getMembershipNumber()));
					}
				}
			}
		} else {
			passengerSegment.getMemberAward().setAsiaMiles(-1);
			passengerSegment.getMemberAward().setClubPoint(-1);
			passengerSegment.getMemberAward().setTierBonus(-1);
			passengerSegment.getMemberAward().setTierLevel(passengerSegment.getFqtvInfo().getTierLevel());
		}
	}


	private MemberAwardResponse populateMemberAward(List<RetrievePnrSegment> pnrSegmentList, FQTVInfo fqtvInfo) throws ParseException {
		// if there's no FQTV or not a MPO/AM member, skip
		if(fqtvInfo == null 
				|| (!bizRuleConfig.getCxkaTierLevel().contains(fqtvInfo.getTierLevel())
						&& !bizRuleConfig.getAmTierLevel().contains(fqtvInfo.getTierLevel()))){
			return null;
		}
		MemberAwardRequest request = new MemberAwardRequest();
		request.setMemberNumber(fqtvInfo.getMembershipNumber());
		
		// set value for sectorDetailRecordList
		List<SectorDetailRecordInRequest> sectorDetailRecords = new ArrayList<>();
		
		for(RetrievePnrSegment pnrSegment: pnrSegmentList) {
			SectorDetailRecordInRequest sectorDetailRecord = new SectorDetailRecordInRequest();
			sectorDetailRecords.add(sectorDetailRecord);
			request.setSectorDetailRecord(sectorDetailRecords);
			
			// set value for sectorDetailRecord
			// For bookable upgrade, use markeing subclass
			if(pnrSegment.getUpgradeInfo() != null &&
				pnrSegment.getUpgradeInfo().getBookableUpgradeInfo() != null &&
				!StringUtils.isEmpty(pnrSegment.getMarketSubClass())
			) {
				sectorDetailRecord.setBookingClass(pnrSegment.getMarketSubClass());
			} 
			// For other upgrade, use original subclass
			else if (!StringUtils.isEmpty(pnrSegment.getOriginalSubClass())) {
				sectorDetailRecord.setBookingClass(pnrSegment.getOriginalSubClass());
			} 
			// For normal booking, use marketing first
			else if (!StringUtils.isEmpty(pnrSegment.getMarketSubClass())) {
				sectorDetailRecord.setBookingClass(pnrSegment.getMarketSubClass());
			} 
			// Then use operating subclass
			else {
				sectorDetailRecord.setBookingClass(pnrSegment.getSubClass());
			}
			
			//if market company and flight number is not null, use the market value, else use the operate value
			sectorDetailRecord.setCarrierCode(StringUtils.isEmpty(pnrSegment.getMarketCompany())
					? pnrSegment.getPnrOperateCompany() : pnrSegment.getMarketCompany());
			sectorDetailRecord.setFlightNum(StringUtils.isEmpty(pnrSegment.getMarketSegmentNumber())
					? pnrSegment.getPnrOperateSegmentNumber() : pnrSegment.getMarketSegmentNumber());
			
			//transform flight number to 4 digits
			int flightNumberLength = sectorDetailRecord.getFlightNum().length();
			for(int i = MMBBizruleConstants.FLIGHT_NUMEBR_STANDARD_LENGTH-flightNumberLength;i>0;i--){
				sectorDetailRecord.setFlightNum("0"+sectorDetailRecord.getFlightNum());
			}
			
			sectorDetailRecord.setDestination(pnrSegment.getDestPort());
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
					pnrSegment.getDepartureTime().getPnrTime());
			sectorDetailRecord.setFlightDate(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMYYYY, departureTime));
			sectorDetailRecord.setOrigin(pnrSegment.getOriginPort());
		}
		
		UserInformation userInformation = new UserInformation();
		request.setUserInformation(userInformation);
		
		// set value for userInformation
		userInformation.setMemberId(fqtvInfo.getMembershipNumber());
		
		return memberAwardService.getMemberAward(request);
	}
	
	
	/**
	 * get segment by segment id 
	 * 
	 * @param pnrSegments
	 * @param segmentId
	 * @return pnrSegment
	 * @author haiwei.jia
	 */
	private RetrievePnrSegment getPnrSegmentById(List<RetrievePnrSegment> pnrSegments, String segmentId) {
		if(CollectionUtils.isEmpty(pnrSegments) || StringUtils.isEmpty(segmentId)) {
			return null;
		}
		
		for (RetrievePnrSegment pnrSegment : pnrSegments) {
			if (pnrSegment.getSegmentID().equals(segmentId)) {
				return pnrSegment;
			}
		}
		return null;
	}

//	/**
//	 * Set PnrOperateCompany and PnrOperateSegmentNumber by Air_Flight_info
//	 * @param pnrSegment
//	 * @return
//	 */
//	private void setOperateByCompanyAndFlightNumber(RetrievePnrSegment pnrSegment, AirFlightInfoBean airFlightInfoBean){
//		if(pnrSegment == null){
//			return;
//		}	
//		if (StringUtils.isEmpty(pnrSegment.getPnrOperateCompany()) && airFlightInfoBean != null
//				&& StringUtils.isNotBlank(airFlightInfoBean.getCarrierCode())) {
//			pnrSegment.setPnrOperateCompany(airFlightInfoBean.getCarrierCode());
//			pnrSegment.setPnrOperateSegmentNumber(airFlightInfoBean.getFlightNumber());
//		}
//		if(StringUtils.isEmpty(pnrSegment.getPnrOperateCompany()) && StringUtils.isEmpty(pnrSegment.getPnrOperateSegmentNumber())){
//			pnrSegment.setPnrOperateCompany(pnrSegment.getMarketCompany());
//			pnrSegment.setPnrOperateSegmentNumber(pnrSegment.getMarketSegmentNumber());
//		}
//	}

	/**
	 * Set numberOfStops, Stops and total duration
	 * @param segment
	 * @param airFlightInfoBean
	 */
	private void setStopsAndTotalDuration(Segment segment, AirFlightInfoBean airFlightInfoBean){
		if(airFlightInfoBean == null){
			return;
		}
		if(airFlightInfoBean.isStopOverFlight()){
			segment.setNumberOfStops(airFlightInfoBean.getNumberOfStops().intValue());
			segment.setStops(airFlightInfoBean.getStops().toArray(new String[0]));
		}
		segment.setTotalDuration(airFlightInfoBean.getTotalDuration());
	}

	/**
	 * required CompletePayment Checking
	 * @param flightBookingDTO
	 * @param memberId
	 * @param redemptionBookingConfirmed
	 * @return
	 */
	private boolean issueTicketChecking(RetrievePnrBooking flightBooking, LoginInfo loginInfo, boolean redemptionBooking){
		
		boolean canIssueTicketChecking = true;
		
		if(!redemptionBooking){
			return false;
		}

		// 1. The booking does not contain UM passenger; 2. The booking does not contain infant
		boolean passengerTypeChecking = flightBooking.getPassengers().stream().anyMatch(passenger -> OneAConstants.PASSENGER_TYPE_INF.equals(passenger.getPassengerType()) || OneAConstants.PASSENGER_TYPE_INS.equals(passenger.getPassengerType()) ||
				OneAConstants.PASSENGER_TYPE_UNACCOMPANIED_MINOR.equals(passenger.getPassengerType()));
		if(passengerTypeChecking){
			return false;
		}
		
		//duplicated O/D in the booking checking
		boolean sameOriginAndDestinationChecking = checkSameOriginAndDestination(flightBooking.getSegments());
		if(sameOriginAndDestinationChecking){
			return false;
		}
		
		// segment status checking
		boolean segmentStatusChecking = checkSegmentStatus(flightBooking.getSegments());
  
		if(!segmentStatusChecking){
			return false;
		}
		
									
		//checking the existence of the FQTU element in the PNR
		boolean checkFQTUExisting = checkFQTUStatus(flightBooking);
		if(checkFQTUExisting){
			return false;
		}
		
		// checking member ID not equal to FQTR member number in PNR
		if(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
		 boolean checkFQTRMemberId	= checkFQTRStatus(flightBooking,loginInfo.getMemberId());
		 if(!checkFQTRMemberId){
			 return false;
		 }
		}
		
		// officeId checking: LVO requirement
		//List<String> issuingOffices = bizRuleConfig.getRedemptionIssuingOffice()
		//boolean officeIssueChecking = issueTicketOfficeChecking(flightBooking, issuingOffices) 
		boolean tposIssueChecking = issueTicketTPOSChecking(flightBooking);
		if(!tposIssueChecking) {
			return false;
		}
		
		// TKTL, TKXL or ADTK exists checking in the booking
		boolean indicatorChecking = checkExistingForRedemption(flightBooking);
  
		if(!indicatorChecking){
			return false;
		}
		
		return canIssueTicketChecking;
	}
	
	/**
	 * officeId checking: LVO requirement
	 * 
	 * @param flightBooking
	 * @param issuingOffices
	 * @return
	 */
//	private boolean issueTicketOfficeChecking(RetrievePnrBooking flightBooking, List<String> issuingOffices){
//		
//		boolean issueTicketOffice = false;
//		List<RetrievePnrTicket> ticketLists = flightBooking.getTicketList();
//		if(!CollectionUtils.isEmpty(ticketLists)){
//			for(RetrievePnrTicket ticketList : ticketLists){
//				String officeId = ticketList.getOfficeId();
//				if(!StringUtils.isEmpty(officeId) && issuingOffices.contains(officeId)){
//					issueTicketOffice = true;
//					break;
//				}
//			}
//		}
//		
//		return issueTicketOffice;
//	}
	
	private boolean issueTicketTPOSChecking(RetrievePnrBooking flightBooking) {
		if(!CollectionUtils.isEmpty(flightBooking.getSkList())) {
			if(redemptionTPOSCheckDAO.count() == 0) {
				return true;
			} else {
				List<String> tposList = redemptionTPOSCheckDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.TPOS).stream()
						.map(RedemptionTPOSCheck::getValue).collect(Collectors.toList());
				for(RetrievePnrDataElements sk : flightBooking.getSkList()) {
					if(OneAConstants.TPOS.equals(sk.getType()) && tposList.contains(sk.getFreeText())) {
						return true;
					}
				}
			}
		} 
		return false;
	}
		
	/**
	 * check FQTR memberid existing or not
	 * @param flightBooking
	 * @param memberId
	 * @return
	 */
	private boolean checkFQTRStatus(RetrievePnrBooking flightBooking, String memberId){

		boolean checkFQTRMemberIdExsiting = false;
		List<RetrievePnrPassenger> passengers = flightBooking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = flightBooking.getPassengerSegments();

		if (!CollectionUtils.isEmpty(passengerSegments)) {
			for (RetrievePnrPassengerSegment passengerSegment : passengerSegments) {
				checkFQTRMemberIdExsiting = checkFQTRMemberId(passengerSegment.getFQTRInfos(), memberId);
				if (checkFQTRMemberIdExsiting) {
					break;
				}
			}
		}

		if (!checkFQTRMemberIdExsiting) {
			for (RetrievePnrPassenger passenger : passengers) {
				checkFQTRMemberIdExsiting = checkFQTRMemberId(passenger.getFQTRInfos(), memberId);
				if (checkFQTRMemberIdExsiting) {
					break;
				}
			}
		}
		
		return checkFQTRMemberIdExsiting;
	}
	
	private boolean checkFQTRMemberId(List<RetrievePnrFFPInfo> fqtrInfos, String memberId){
		
		boolean checkFQTRMemberIdExisting = false;
		if(!CollectionUtils.isEmpty(fqtrInfos)){
			for(RetrievePnrFFPInfo FQTUInfo : fqtrInfos){
				if(FQTUInfo.getFfpMembershipNumber().equals(memberId)){
					checkFQTRMemberIdExisting = true;
					break;
				}
			}
		}
	
		return checkFQTRMemberIdExisting;
	}
	
	/**
	 * check FQTU element existing or not
	 * @param flightBooking
	 * @return
	 */
	private boolean checkFQTUStatus(RetrievePnrBooking flightBooking){
		
		return flightBooking.getSegments().stream().anyMatch(seg->seg.getFqtu()!=null);
	}
	
	
	
	/**
	 * TKXL or TKTL or ADTK exists checking lsit
	 * 
	 * @param flightBookingDTO
	 * @return
	 */
	private boolean checkExistingForRedemption(RetrievePnrBooking flightBooking){
		
		boolean checkExisting = false;
		List<RetrievePnrTicket> ticketLists = flightBooking.getTicketList();
		List<RetrievePnrDataElements> skLists = flightBooking.getSkList();
		List<RetrievePnrDataElements> srLists = flightBooking.getSsrList();
		
		if(!CollectionUtils.isEmpty(ticketLists)){
			
			for(RetrievePnrTicket ticket: ticketLists) {
				// Check if TKXL or TKTL exists
				String indicator = ticket.getIndicator();
				if(OneAConstants.TICKET_INDICATOR_XL.equals(indicator) || OneAConstants.TICKET_INDICATOR_TL.equals(indicator)) {
					checkExisting = true;
					break;
				}
			}
		}
		
		if(!checkExisting && !CollectionUtils.isEmpty(skLists)){
			checkExisting = ssrListChecking(skLists);
		}
		
		if(!checkExisting && !CollectionUtils.isEmpty(srLists)){
			checkExisting = ssrListChecking(srLists);
		}
		
		return checkExisting;

	}
	
	/**
	 * ssrList Checking
	 * @param skLists
	 * @return
	 */
	private boolean ssrListChecking(List<RetrievePnrDataElements> skLists){
		
		boolean ssrListChecking = false;
		for(RetrievePnrDataElements skList : skLists){
			String skType = skList.getType();
			if(OneAConstants.SK_TYPE_ADTK.equals(skType)){
				ssrListChecking = true;
				break;
			}
		}
		
		return ssrListChecking;
	}
	
	/**
	 * check Segment Status
	 * @param segmentDTOs
	 * @return
	 */
	private boolean checkSegmentStatus(List<RetrievePnrSegment> segments) {

		boolean segmentStatusChecking = false;
		boolean matchStatus = false;
		
		List<RetrievePnrSegment> filteredSegmentDTOs = segments.stream()
				.filter(segment -> segment.getMarketCompany().equals(OneAConstants.COMPANY_CX)
						|| segment.getMarketCompany().equals(OneAConstants.COMPANY_KA))
				.collect(Collectors.toList());

		// All segments have status confirmed
		if (!CollectionUtils.isEmpty(filteredSegmentDTOs)) {

			List<String> segmentStatus = bookingStatusCOnfig.getConfirmedList();
			
			for(RetrievePnrSegment segment : filteredSegmentDTOs){
				List<String> status = segment.getStatus();
				if(!CollectionUtils.isEmpty(status)){
					 matchStatus = status.stream().allMatch(segmentStatus::contains);
						if(!matchStatus){
							break;
						}
					}
				}
			}
			
			if (matchStatus) {
				segmentStatusChecking = true;
			}
			
			return segmentStatusChecking;
		}
		

	
	/**
	 * check Same Origin And Destination
	 * @param segments
	 * @return
	 */
	private boolean checkSameOriginAndDestination(List<RetrievePnrSegment> segments){
		
		boolean sameOriginAndDestination = false;
		Map<String, List<String>> originAndDestination = new HashMap<>();
		
		for(RetrievePnrSegment segment: segments) {
			List<String> destinationPort = originAndDestination.get(segment.getOriginPort());
			if(CollectionUtils.isEmpty(destinationPort)){
				destinationPort = new ArrayList<>();
				destinationPort.add(segment.getDestPort());
				originAndDestination.put(segment.getOriginPort(),destinationPort);
			} else {
				String destinationPort2 = segment.getDestPort();
				for(String destination: destinationPort){
					if(destination.equals(destinationPort2)){
						sameOriginAndDestination = true;
						break;
					}
				}
				destinationPort.add(destinationPort2);
				originAndDestination.put(segment.getOriginPort(),destinationPort);
			}
			
		}
		return sameOriginAndDestination;
	}
	
	/**
	 * redemption Booking checking
	 * @param flightBookingDTO
	 * @return
	 */
	private boolean redemptionBookingChecking(List<RetrievePnrSegment> segments) {
		boolean returnValue = false;
		for (RetrievePnrSegment segment : segments) {
			if (!StringUtils.isEmpty(segment.getSubClass())) {
				// find by operate company and subclass
				List<RedemptionSubclassCheck> redemptionBookingChecks = redemptionSubclassCheckDAO
						.findByAppCodeAndOperateCompanyAndSubclass(MMBConstants.APP_CODE, segment.getPnrOperateCompany(), segment.getSubClass());
				if (!CollectionUtils.isEmpty(redemptionBookingChecks)) {
					returnValue = true;
				} else {
					returnValue = false;
					break;
				}
			}
		}
		return returnValue;
	}
	
 

	/**
	 * Get segments that eligible for online check in 
	 * @param booking
	 * @return
	 */
	private boolean eligibleForCheckInSegmentExsits(Booking booking) {
		if(booking == null) {
			logger.info("eligibleForCheckInSegmentExsits -> false, the booking is null");
			return false;
		}
		
		boolean needToCallOLCIToRetrievePriorityCheckInfo = false;
		for(Segment segment : booking.getSegments()) {
			if(needToCallOLCIToRetrievePriorityCheckInfo) {
				logger.info(String.format("eligibleForCheckInSegmentExsits -> true, booking[%s] Retrieve OLCI information within D-72 hr", booking.getOneARloc()));
				return true;
			}
			
			// to check whether this segment is within priority check in checking window
			needToCallOLCIToRetrievePriorityCheckInfo = BooleanUtils.isNotTrue(segment.isFlown())
					&& (segment.getSegmentStatus() != null && !FlightStatusEnum.CANCELLED.equals(segment.getSegmentStatus().getStatus()))
					&& BizRulesUtil.isFlight(segment.getAirCraftType())
					&& isSegmentInPriorityCheckInWindow(segment);
		}
		
		if(!needToCallOLCIToRetrievePriorityCheckInfo) {
			logger.info(String.format("eligibleForCheckInSegmentExsits -> false, booking[%s] Retrieve OLCI information not within D-72 hr", booking.getOneARloc()));
		}

		return needToCallOLCIToRetrievePriorityCheckInfo;
	}
	
	/**
	 * Check if segment is inside check in window 
	 * @param segment
	 * @return
	 */
	private boolean isSegmentInCheckInWindow(Segment segment) {
		boolean isSegmentInChecinWindow = false;
		try {
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
			Date beforeTime = new Date();
			Date afterTime = new Date();
			beforeTime.setTime(System.currentTimeMillis());
			afterTime.setTime(System.currentTimeMillis() + olciConfig.getCheckInWindowUpper());
			if((departureTime.before(afterTime) && departureTime.after(beforeTime)) || departureTime.compareTo(afterTime) == 0) {
				isSegmentInChecinWindow = true;
			}
		} catch (ParseException e) {
			logger.error("Error to convert depaeture time");
		}
		
		return isSegmentInChecinWindow;
	}

	/**
	 * Check if segment is inside priority check in window
	 * @param segment
	 * @return
	 */
	private boolean isSegmentInPriorityCheckInWindow(Segment segment) {
		boolean isSegmentInPriorityCheckInWindow = false;
		try {
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
			Date beforeTime = new Date(System.currentTimeMillis());
			Date afterTime = new Date(System.currentTimeMillis() + olciConfig.getPriorityCheckInExamineTime());
			isSegmentInPriorityCheckInWindow = ((departureTime.before(afterTime) && departureTime.after(beforeTime)) || departureTime.compareTo(afterTime) == 0);
		} catch (ParseException e) {
			logger.error("Error to convert depaeture time");
		}

		return isSegmentInPriorityCheckInWindow;
	}
	
	
	/**
	 * get OlCI CPR information
	 * @param loginInfo
	 * @param booking
	 * @param useCprSession
	 * @return
	 */
	private Future<LoginResponseDTO> asyncGetPassengerCheckInInfos(LoginInfo loginInfo, Booking booking, boolean useCprSession, boolean filerMergePNR) {
		if(eligibleForCheckInSegmentExsits(booking)) {
			Passenger priPax = booking.getPassengers().stream().filter(pax->BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(booking.getPassengers().get(0));
			return olciServiceV2.asyncRetrieveCPRBooking(booking.getOneARloc(), loginInfo.getLoginEticket(), priPax.getGivenName(), priPax.getFamilyName(),  loginInfo.getMemberId(), useCprSession, filerMergePNR);
		} else {
			logger.info(String.format("asyncGetPassengerCheckInInfos -> No eligible check-in segments in booking[%s], no need to call OLCI to get CPR",
					booking != null ? booking.getOneARloc() : null));
			return null;
		}
	}
	
	
	
	private boolean isPaidASR(RetrievePnrPassengerSegment pnrPaxSegment, List<RetrievePnrFa> faList) {
		if (pnrPaxSegment == null || pnrPaxSegment.getSeat() == null || pnrPaxSegment.getSeat().getSeatDetail() == null) {
			return false;
		}
		RetrievePnrSeatDetail pnrSeatDetail = pnrPaxSegment.getSeat().getSeatDetail();
		
		if (pnrSeatDetail == null || pnrSeatDetail.getIndicator() == null || pnrSeatDetail.getSeatCharacteristics() == null) {
			return false;
		}
		boolean isPaidASRIndicator = OneAConstants.PAID_ASR_INDICATOR.equals(pnrSeatDetail.getIndicator());
		boolean isPaidASRSeatCharacteristic = pnrSeatDetail.getSeatCharacteristics().contains(OneAConstants.PAID_ASR_SEAT_CHARACTERISTIC);
		
		boolean isDTCXFa = false;
		for(RetrievePnrFa pnrFa: faList) {
			if(pnrFa.getPassengerId() != null && pnrFa.getPassengerId().equals(pnrPaxSegment.getPassengerId()) &&
			    pnrFa.getLongFreeText() != null && pnrFa.getLongFreeText().contains(OneAConstants.PAID_ASR_FREE_TEXT)) {
				isDTCXFa = true;
				break;
			}
		}
		return isPaidASRIndicator && isPaidASRSeatCharacteristic && isDTCXFa;
	}

	private List<String> buildWaiveReminderSSRSK(RetrievePnrBooking pnrBooking){
		List<String> codeList = new ArrayList<>();
		List<SpecialServiceModel> changeFlightReminderSSKSRModels = specialServiceDAO.findAllByAppCodeAndAction(MMBConstants.APP_CODE,TBConstants.BOOKINGWAIVE_REMINDER_FUNCTIONNAME);
		List<RetrievePnrDataElements> ssrList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pnrBooking.getSkList())){ssrList.addAll(pnrBooking.getSkList());}
		if(!CollectionUtils.isEmpty(pnrBooking.getSsrList())){ssrList.addAll(pnrBooking.getSsrList());}
		ssrList.stream().forEach(retrievePnrDataElements -> {
			if(changeFlightReminderSSKSRModels.stream().anyMatch(changeFlightReminderSSKSRModel ->
					changeFlightReminderSSKSRModel.getReminderCode().equals(retrievePnrDataElements.getType())  && !codeList.contains(retrievePnrDataElements.getType()))) {
				codeList.add(retrievePnrDataElements.getType());
			}
		});
		return  codeList;
	}
	
	/**
	 * populate WIFI to segment by flight status data.
	 * 
	 * @param segment
	 * @param flightStatusDataList
	 */
	public void populateFlightWifi(Segment segment, List<FlightStatusData> flightStatusDataList) {
		if (CollectionUtils.isEmpty(flightStatusDataList)) {
			logger.warn(String.format(
					"Flight [%s][%s] is not found in RTFS status, because it is not recently flight, pnrTime[%s]",
					segment.getOperateCompany(), segment.getOperateSegmentNumber(), segment.findDepartureTime().getPnrTime()));
			return;
		}
		/** according to flight's OriginPort and Departure time to match */
		FlightStatusData matchedFlightStatus = flightStatusDataList.stream().filter(fs -> BookingBuildUtil.isFlightStatusMatched(segment, fs)).findFirst().orElse(null);

		if (matchedFlightStatus == null || CollectionUtils.isEmpty(matchedFlightStatus.getSectors())) {
			logger.warn(
					String.format("Flight [%s][%s] is not found in RTFS status, base on originPort[%s]  and Departure time[%s] ",
							segment.getOperateCompany(), segment.getOperateSegmentNumber(), segment.getOriginPort(),
							segment.getDepartureTime()));
			segment.setHasAvailableWifi(false);
		}else {
			List<SectorDTO> sectorDTOArrayList = BookingBuildUtil.matchAirlineFromOriginToDestport(matchedFlightStatus.getSectors(), segment.getOriginPort(), segment.getDestPort());
			segment.setHasAvailableWifi(bookingBuildHelper.isAvailableWifiExist(sectorDTOArrayList));
		}
	}
}
