package com.cathaypacific.mmbbizrule.business.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.ContactInfoTypeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommunication.webservice.service.client.CancelBookingEmailClient;
import com.cathaypacific.mmbbizrule.business.BookingCancelBusiness;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.business.token.TokenManagementBusiness;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.IBERedirectParameterConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.request.CancelBookingEligibilityRequest;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response.CancelBookingEligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.PassengerInfoDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.RandomFieldMapDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.RequesterInfoDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.TicketRefundRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.service.IBEService;
import com.cathaypacific.mmbbizrule.cxservice.sentemail.service.FIFBelowService;
import com.cathaypacific.mmbbizrule.dto.request.bookingcancel.CancelBookingDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.ContactTypeEnum;
import com.cathaypacific.mmbbizrule.dto.request.email.FFPInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.FIFBelowRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxInfo;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.RemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.RemarkTypeEnum;
import com.cathaypacific.mmbbizrule.dto.request.email.SegmentDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.TemplateTypeEnum;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.BookingCancelResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.CancelBookingDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.BookingCancelCheckResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.CancelFlow;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.ETicket;
import com.cathaypacific.mmbbizrule.model.booking.detail.Email;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.OSIBookingSite;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.AddBookingBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrApEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.session.service.PnrSessionInvokService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol.service.TicketAirportControlService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.security.IBEEncryptionHelper;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.common.collect.Lists;

@Service
public class BookingCancelBusinessImpl implements BookingCancelBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(BookingCancelBusinessImpl.class);
	
	private static final int US_CANCEL_DEADLINE_MIN = 24*60;
	
	private static final String REMARK_STRING_FORMAT_REFUND_FEE_WAIVER = "DW%s - refund fee waiver";
	
	private static final String REMARK_STRING_FORMAT_N0_REFUND_FEE_WAIVER = "DW%s - no refund fee waiver";
	
	private static final String REMARK_STRING_FORMAT_ONLINE_REFUND = "ONLINE REFUND";
	
	@Value("${booking.cancel.blockwindow.min}")
	private int bookingCancelBlockWindowMin;
	
	@Value("${booking.refund.blockwindow.hour}")
	private int bookingRefoundBlockwindowHour;
	
	@Value("${booking.refund.enableRequestRemark}")
	private boolean bookingRefundEnableRequestRemark;
	
	@Value("${ibe.cancelbooking.check.checkedin.errorcode}")
	private String ibeCancelBookingCheckedInErrorCode;
	
	@Value("${ibe.cancelbooking.check.active}")
	private boolean ibeCheckActive;
	
	@Autowired
	private RetrievePnrBusiness retrievePnrBusiness;
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	private TokenManagementBusiness sessionBusiness;
	
	@Autowired
	private IBEService ibeService;
	
	@Autowired
	CancelBookingEmailClient cancelBookingEmailClient;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Autowired
	private FIFBelowService fifBlowService;

	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Autowired
	private TicketAirportControlService ticketAirportControlService;
	
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private FlightBookingConverterHelper flightBookingConverterHelper;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private IBEEncryptionHelper ibeEncryptionHelper;
	
	@Autowired
	private PnrSessionInvokService pnrSessionInvokService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	@Override
	public BookingCancelCheckResponseDTO checkBookingCanCancel(String rloc, LoginInfo loginInfo,boolean skipIbeCheck)throws BusinessBaseException {
		
		BookingCancelCheckResponseDTO result = new BookingCancelCheckResponseDTO();
		if(!BizRulesUtil.isFlightRloc(rloc)){
			result.addBlocReason(new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_NOTFLIHTONLY));
			return result;
		}
		
		PNRReply pnrReply = pnrInvokeService.retrievePnrReplyByOneARloc(rloc);
		Booking booking = flightBookingConverterHelper.getFlightBooking(pnrInvokeService.PNRReplyToBooking(pnrReply, rloc), loginInfo, createBookingBuildRequiredForCancelBookingCheck(), true);
		
		//set any passenger checked in
		result.setHasCheckedInPassenger(BookingBuildUtil.anyPassengerCheckedIn(booking));
		result.setRedemptionBooking(booking.isRedemptionBooking());
		result.setBohBooking(booking.isBookingOnhold());
		String mmbToken = loginInfo == null ? null : loginInfo.getMmbToken();
		//check IBE first 
		boolean ibeEligibleCancel = ibeCheckActive && !skipIbeCheck && checkBookingCanCancelIbeFlow(booking, pnrReply, mmbToken);
		
		if(ibeEligibleCancel){
			result.setSuggestedCancelflow(CancelFlow.IBE);
			result.setCanCancel(true);
			result.setCanRefund(true);// always set true if IBE eligible to cancel 
		//check mmb cancel
		} else {
			List<ErrorInfo> errors = checkBookingCanCancelMbFlow(booking, loginInfo, false);
			if (CollectionUtils.isEmpty(errors)) {
				result.setCanCancel(true);
				result.setSuggestedCancelflow(CancelFlow.MMB);
				setMbRefoundFlagAndReasonsToResponse(booking, result);
			} else {
				result.setBlockReason(errors);
			}
		}
		// OLSSMMB-16763: Don't remove me!!!
		// --- begin ---
		logger.info(String.format("Cancel Flight | Rloc | %s | Eligibility | %s", rloc, result.isCanCancel() ? "Eligible": "Ineligible"), true);

		if(result.isCanCancel()) {
			logger.info(String.format("Cancel Flight | Rloc | %s | Cancel Booking Flow | %s | Booking Type | %s", rloc, result.getSuggestedCancelflow(), result.isRedemptionBooking()? "Redemption" : "Revenue"), true);
		}
		// --- end ---
		return result;
	}

	@Override
	public BookingCancelResponseDTO cancelBooking(String rloc, boolean refund, LoginInfo loginInfo, Locale locale) throws BusinessBaseException {
		BookingCancelResponseDTO response = new BookingCancelResponseDTO();
		response.setRequestedRefund(refund);
		if(!BizRulesUtil.isFlightRloc(rloc)){
			response.addError(new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_NOTFLIHTONLY));
			return response;
		}

		Booking booking = retrievePnrBusiness.retrieveFlightBooking(loginInfo, rloc, createBookingBuildRequiredForCancelBookingCheck());
		
		List<ErrorInfo> blockReasons = checkBookingCanCancelMbFlow(booking, loginInfo, true);
		if(CollectionUtils.isNotEmpty(blockReasons)) {
			response.setBlockReasons(blockReasons);
			return response;
		}
		//Do cancel & refund
		doCancelRefund(response, booking, refund, loginInfo, locale);
		
		if(CollectionUtils.isNotEmpty(response.getBlockReasons())) {
			return response;
		}
		
		if (response.isCancelled()) {
			// delete DW codes in cache
			deleteDwCodesCaches(loginInfo, booking.getOneARloc());
			//response.setRefunded(response.isRefunded())
			// send email
			sendEmailForCancelBooking(booking, locale, response,refund);
			// log out if non member login
			if (LoginInfo.LOGINTYPE_RLOC.equals(loginInfo.getLoginType()) || LoginInfo.LOGINTYPE_ETICKET.equals(loginInfo.getLoginType())) {
				response.setLoggedOut(sessionBusiness.delete(loginInfo.getMmbToken()));
			}
		}else{
			if(!booking.isRedemptionBooking()){
				logger.warn("Cancel RevenueBooking Fail.");
			}
			throw new UnexpectedException("booking cancel failed, Unexpected exception", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		return response;
	}
	
	/**
	 * delete DW codes in cache
	 * @param loginInfo
	 * @param oneARloc
	 */
	private void deleteDwCodesCaches(LoginInfo loginInfo, String rloc) {
		if (loginInfo != null && !StringUtils.isEmpty(loginInfo.getMmbToken()) && !StringUtils.isEmpty(rloc)) {
			mbTokenCacheRepository.delete(loginInfo.getMmbToken(), TokenCacheKeyEnum.DW_CODE, rloc);
		}
	}

	/**
	 * get DW codes
	 * @param booking
	 * @return List<String>
	 */
	private List<String> getDwCodes(Booking booking, LoginInfo loginInfo) {
		if (booking == null || booking.getBookingCreateInfo() == null
				|| StringUtils.isEmpty(booking.getBookingCreateInfo().getRpOfficeId())) {
			logger.warn(String.format("No officeId found in booking while cancelling booking, rloc: %s", booking == null ? null : booking.getOneARloc()));
			return Collections.emptyList();
		}
		
		PNRReply pnrReply = null;
		try {
			pnrReply = pnrInvokeService.retrievePnrReplyByOneARloc(booking.getOneARloc());
		} catch (BusinessBaseException e) {
			logger.error(String.format("retrieve PNR by rloc: %s failed while cancelling booking", booking.getOneARloc()), e);
			return Collections.emptyList();
		}
		String officeId = booking.getBookingCreateInfo().getRpOfficeId();
		String mmbToken = loginInfo == null ? null : loginInfo.getMmbToken();
		
		return bookingBuildHelper.getDwCodes(pnrReply, officeId, mmbToken, booking.getOneARloc());
	}

	/**
	 * Do cancel/refund
	 * 
	 * @param response
	 * @param booking
	 * @param refund
	 * @param loginInfo
	 * @param locale
	 * @throws UnexpectedException
	 */
	private void doCancelRefund(BookingCancelResponseDTO response, Booking booking, boolean refund, LoginInfo loginInfo, Locale locale) throws UnexpectedException {
		boolean bookingCancelled = false;
		boolean bookingRefunded = false;
		
		if(BooleanUtils.isTrue(refund)) {
			boolean isRedemptionBooking = booking.isRedemptionBooking();
			if(isRedemptionBooking) {
				List<ErrorInfo> refundCheckErrors = checkCanRefund(booking);
				if(CollectionUtils.isNotEmpty(refundCheckErrors)) {
					response.setBlockReasons(refundCheckErrors);
					return;
				}
				bookingCancelled = doCancel(booking, true);
				bookingRefunded = bookingCancelled;
			} else {
				List<String> dwCodes = getDwCodes(booking, loginInfo);
				bookingCancelled = doCancel(booking, false);
				try {
					bookingRefunded = bookingCancelled && doIbeRefund(booking, locale, dwCodes);
				} catch (UnexpectedException e) {
					if (e.getErrorInfo() != null
							&& (ErrorCodeEnum.ERR_BLOCK_REFUND_BOOKING_IS_ONHLOD.getCode().equals(e.getErrorInfo().getErrorCode())
									|| ErrorCodeEnum.ERR_BLOCK_REFUND_REQUESTEREMAIL_NOTFOUND.getCode()
											.equals(e.getErrorInfo().getErrorCode()))) {
						response.setSkipRefund(true);
					}
				}
			}
		} else {
			bookingCancelled = doCancel(booking, false);
		}
		if(!booking.isRedemptionBooking() && bookingCancelled){
			logger.info("Cancel RevenueBooking Success.");
		}
		if(!booking.isRedemptionBooking() && bookingRefunded){
			logger.info("Refund RevenueBooking Success.");
		}
		response.setCancelled(bookingCancelled);
		response.setRefunded(bookingRefunded);
	}

	/**
	 * Do refund through IBE
	 * 
	 * @param booking
	 * @param locale
	 * @param dwCodes 
	 * @return
	 */
	private boolean doIbeRefund(Booking booking, Locale locale, List<String> dwCodes) throws UnexpectedException{
		boolean refundSuccess = false;
		
		if(BooleanUtils.isTrue(booking.isBookingOnhold())){
			logger.warn(String.format("Refund RefundRevBooking Fail - BOH, rloc: %s", booking.getOneARloc()));
			throw new UnexpectedException("Refund RefundRevBooking Fail - BOH", new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_BOOKING_IS_ONHLOD));
		}
		
		TicketRefundRequestDTO request = buildTicketRefundRequest(booking, locale, dwCodes);
		if(request == null) {
			logger.warn(String.format("Refund RevenueBooking Fail - lack of contact info, rloc: %s", booking.getOneARloc()));
			throw new UnexpectedException("Refund RevenueBooking Fail - lack of contact info", new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_REQUESTEREMAIL_NOTFOUND));
		}
		
		try {
			refundSuccess = ibeService.submitTicketRefund(request);			
		} catch(Exception e) {
			logger.error(String.format("cancel booking ibe refund failure, rloc: %s", booking.getOneARloc()), e);
		}
		return refundSuccess;
	}

	/**
	 * build request for calling IBE ticketRefund
	 * 
	 * @param booking
	 * @param locale
	 * @param dwCodes 
	 * @return
	 * @throws UnexpectedException
	 */
	private TicketRefundRequestDTO buildTicketRefundRequest(Booking booking, Locale locale, List<String> dwCodes) throws UnexpectedException {
		TicketRefundRequestDTO request = new TicketRefundRequestDTO();
		
		List<Passenger> passengers = booking.getPassengers();
		Passenger primaryPassenger = passengers.stream().filter(pax -> pax != null && BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(passengers.get(0));
		
		/** build passengerInfos */
		request.setPassengerInfos(buildTicketRefundPassengerInfos(passengers, booking, dwCodes));
		
		/** build requesterInfo */
		RequesterInfoDTO requesterInfoDTO = new RequesterInfoDTO();
		requesterInfoDTO.setRequesterLastName(primaryPassenger.getFamilyName());
		requesterInfoDTO.setRequesterFirstName(primaryPassenger.getGivenName());
		
		String requesterEmail = getRequestEmail(primaryPassenger, booking.getOneARloc());
		if(StringUtils.isEmpty(requesterEmail)) {
			logger.warn(String.format("cancel booking[%s] IBE refund warning: requesterEmail is empty, familyName:%s givenName:%s, will skip the IBE refund",
					booking.getOneARloc(), requesterInfoDTO.getRequesterLastName(), requesterInfoDTO.getRequesterFirstName()));
			return null;
		} else {
			requesterInfoDTO.setRequesterEmail(requesterEmail);
			requesterInfoDTO.setReconfirmRequesterEmail(requesterEmail);			
		}
		request.setRequesterInfo(requesterInfoDTO);
		
		request.setSite("cx");
		request.setConsent("yes");
		request.setLang(locale.getLanguage());
		request.setCountryCode(locale.getCountry());
		request.setChannel(MMBConstants.APP_CODE);
		request.setRandomFieldMap(new RandomFieldMapDTO());
		return request;
	}

	private String getRequestEmail(Passenger primaryPassenger, String rloc) {
		String requestEmail = Optional.ofNullable(primaryPassenger.getContactInfo()).map(ContactInfo::getEmail).map(Email::getEmailAddress).orElse(null);
		
		/** if can't find requestEmail, try to get it from APE not related to any PT */
		if(StringUtils.isEmpty(requestEmail)) {
			logger.info(String.format("Try to get requesterEmail from APE not related to any PT, rloc:%s, passengerId:%s", rloc, primaryPassenger.getPassengerId()));
			try {
				RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
				RetrievePnrApEmail ape = Optional.ofNullable(pnrBooking.getApEmails()).orElse(Collections.emptyList()).stream().filter(e -> e != null && CollectionUtils.isEmpty(e.getPassengerIds()) && StringUtils.isNotEmpty(e.getEmail())).findFirst().orElse(null);
				requestEmail = ape != null ? ape.getEmail() : null;
			} catch (Exception e) {
				logger.error("get pnr booking failure when try to get APE email", e);
				requestEmail = null;
			}
		}
		
		return requestEmail;
	}

	/**
	 * Build passengerInfos in ticket refund request
	 * 
	 * @param passengers
	 * @param booking 
	 * @param dwCodes 
	 * @return
	 */
	private List<PassengerInfoDTO> buildTicketRefundPassengerInfos(List<Passenger> passengers, Booking booking, List<String> dwCodes) {
		String remarks;
		// if remark is enabled, build remark by DW codes, otherwise always return "ONLINE REFUND"
		if (bookingRefundEnableRequestRemark) {
			remarks = buildTicketRefundRemarksByDwCodes(dwCodes);
		} else {
			remarks = REMARK_STRING_FORMAT_ONLINE_REFUND;
		}
		
		List<PassengerInfoDTO> passengerInfoDTOs = new ArrayList<>();
		List<PassengerSegment> passegnerSegments = booking.getPassengerSegments();
		for(Passenger passenger : passengers) {
			List<String> ticketNumbers = passegnerSegments.stream()
					.filter(ps -> ps!=null && passenger.getPassengerId().equals(ps.getPassengerId()) && ps.getEticket() != null && StringUtils.isNotEmpty(ps.getEticket().getNumber()))
					.map(PassengerSegment::getEticket)
					.map(ETicket::getNumber)
					.collect(Collectors.toList());
			for(String ticketNumber : ticketNumbers) {
				// to filter the case: passenger has the same ticket in different segment.
				boolean existInRequest = passengerInfoDTOs.stream()
						.anyMatch(pi -> pi.geteTicketNumber().equals(ticketNumber) 
								&& pi.getPassengerFirstName().equals(passenger.getGivenName())
								&& pi.getPassengerLastName().equals(passenger.getFamilyName()));
				if(existInRequest) {
					continue;
				}
				
				PassengerInfoDTO passengerInfoDTO = new PassengerInfoDTO();
				passengerInfoDTO.setPassengerFirstName(passenger.getGivenName());
				passengerInfoDTO.setPassengerLastName(passenger.getFamilyName());
				passengerInfoDTO.seteTicketNumber(ticketNumber);
				passengerInfoDTO.setRemarks(remarks);
				
				passengerInfoDTOs.add(passengerInfoDTO);
			}
		}
		
		return passengerInfoDTOs;
	}

	/**
	 * build remarks by DW codes
	 * 		if DW8/9 found, return first DW8/9 code and corresponding text ("8" & "9" is configured in Jenkins)
	 * 			eg. dwcode is 3000, 8000, 9000 then “Remarks” is “DW8000 - refund fee waiver"
	 * 		else if no DW8/9 found but other codes found, return first code and corresponding text
	 * 			eg. dwcode is 2000, 3000 then “Remarks” is “DW2000 - no refund fee waiver”
	 * 		else no DW code found, return corresponding text
	 * 			eg. “Remarks” is “ONLINE REFUND"
	 * @param dwCodes
	 * @return String
	 */
	private String buildTicketRefundRemarksByDwCodes(List<String> dwCodes) {
		if (CollectionUtils.isEmpty(dwCodes)) { // no DW code found, return "ONLINE REFUND"
			return REMARK_STRING_FORMAT_ONLINE_REFUND;
		}
		// the DW code selected for the remark
		// search DW8/9 code first
		String dwCode = dwCodes.stream().filter(cdoe -> bizRuleConfig.getBookingRefundFeeWaiverDwCodes().stream().anyMatch(cdoe :: startsWith)).findFirst().orElse(null);
		if(!StringUtils.isEmpty(dwCode)) { // DW8/9 found, return "DWXXX - refund fee waiver"
			return String.format(REMARK_STRING_FORMAT_REFUND_FEE_WAIVER, dwCode);
		} else { // other DW code found, return "DWXXX - no refund fee waiver"
			dwCode = dwCodes.get(0);
			return String.format(REMARK_STRING_FORMAT_N0_REFUND_FEE_WAIVER, dwCode);
		}
	}

	/**
	 * Do cancel booking actions: [refund steps: 1 ~ 3, 5 ~ 6]
	 * when transaction2 failure, try it again.
	 * 
	 * if refund is false, just do cancel booking part
	 * 
	 * [Transaction 1]
	 * 1) check all cpnStatus are all open for update, if yes go to Step4 else go to Step2;
	 * 2) make all cpnStatus marked "AL" to open for updating;
	 * 3) update all PAX cpnStatus to refund.
	 * 
	 * [Transaction 2]
	 * 4) delete cancel booking element;
	 * 5) remove all TK elements;
	 * 6) add TK TL with queue office;
	 * 7) add RF element to identify who did cancelBooking actions
	 * 
	 * @param booking
	 * @param refund 
	 * @return cancelBooking success or not 
	 * @throws UnexpectedException 
	 */
	private boolean doCancel(Booking booking, boolean refund) throws UnexpectedException {
		String rloc = booking.getOneARloc();
		
		boolean cpnStatusProcessSuccess = checkAndUpdateTicketStatus(booking,false, refund);
		if(!cpnStatusProcessSuccess) {
			logger.error(String.format("cancel booking RLOC:[%s] failure in [transaction1] first time", booking.getOneARloc()));
			//retry update ticketStatus
			cpnStatusProcessSuccess = checkAndUpdateTicketStatus(booking, true, refund);
		}
		if(!cpnStatusProcessSuccess) {
			logger.error(String.format("cancel booking RLOC:[%s] failure in [transaction1] and retry not work", booking.getOneARloc()));
			return false;
		} else if(refund){
			logger.info(String.format("cancel booking RLOC:[%s] retried success in [transaction1]", booking.getOneARloc()));
		}
		
		boolean cancelRefundSuccess = cancelAndRefundInPnr(rloc, refund);
		
		if(!refund) {// if don't refund, just return the cancel result.
			return cancelRefundSuccess;
		}
		
		if(!cancelRefundSuccess) {//[Transaction 2] failure, try it again.
			logger.error(String.format("Redemption refund failed - PNR[%s] cannot be cancel and added to the queue", booking.getOneARloc()));
			cancelRefundSuccess = cancelAndRefundInPnr(rloc, refund);
		}
		
		if(!cancelRefundSuccess) {
			throw new UnexpectedException("Redemption refund failed - PNR[%s] cannot be cancel and retry not work", new ErrorInfo(ErrorCodeEnum.ERR_CANCAL_FAIL_BUT_TICKET_STATUS_SUCCESS));
		} else {
			logger.info(String.format("Redemption refund success - PNR[%s] can be cancel and retry works", booking.getOneARloc()));
		}
		
		return true;
	}
	
	/**
	 * [Transaction 2] (refund: 5,6)
	 * 4) delete cancel booking element;
	 * 5) remove all TK elements;
	 * 6) add TK TL with queue office;
	 * 7) add RF element to identify who did cancelBooking actions
	 * 
	 * @param rloc
	 * @param refund
	 * @return
	 */
	private boolean cancelAndRefundInPnr(String rloc, boolean refund) {
		// new session start
		Session session = new Session();
		
		PNRReply pnrReply = null;
		
		/** 4) delete cancel booking element */
		try {
			pnrReply = removeCancelledElement(rloc, session);
		} catch(Exception e) {
			logger.error(String.format("cancel booking RLOC:[%s] failure -> remove cancelled booking element failure! errorMessage: %s", rloc, e.getMessage()));
			return false;
		}
		
		if(refund) {
			/** 5) remove all TK elements */
			try {
				removeAllTkElements(rloc, session, pnrReply);
			} catch(Exception e) {
				logger.error(String.format("cancel booking RLOC:[%s] failure -> remove all TK elements failure! errorMessage: %s", rloc, e.getMessage()));
				return false;
			}
			
			/** 6) add TK TL with queue office */
			try {
				addTkTlElements(rloc, session);
			} catch(Exception e) {
				logger.error(String.format("cancel booking RLOC:[%s] failure -> add TK TL with queue office failure! errorMessage: %s", rloc, e.getMessage()));
				return false;
			}
		}
		
		// set session status END
		session.setStatus(SessionStatus.END.getStatus());
		
		/** 7) add RF element to identify who did cancelBooking actions*/
		try {
			addRfForCancelBooking(rloc, session);
		} catch(Exception e) {
			logger.error(String.format("cancel booking RLOC:[%s] failure -> add RF element failure! errorMessage: %s", rloc, e.getMessage()));
			return false;
		}
		
		return true;
	}

	/**
	 * When refund is true, [Transaction 1]
	 * 1) check all cpnStatus are all open for update, if yes go to Step4 else go to Step2;
	 * 2) make all cpnStatus marked "AL" to open for updating, any errors then close 1A session;
	 * 3) update all PAX cpnStatus to refund.
	 * 
	 * @param booking
	 * @param refund
	 * @throws Exception
	 */
	private boolean checkAndUpdateTicketStatus(Booking booking,boolean getAirPortControlForAllStatus, boolean refund) {
		if(!refund) {
			return true;
		}
		
		// get all tickets without infant in booking
		List<ETicket> etickets = BookingBuildUtil.getAllEticketsByPaxType(booking.getPassengerSegments(), PnrResponseParser.ETICKET_PASSENGER_TYPE_PAX);
		
		// new session start
		Session session = new Session();
		
		/** 1) check all cpnStatus are all open for update, if yes go to Step4 else go to Step2 */
		Map<String, List<String>> alTicketMap = null;
		try {
			// all tickets with cpnStatus = "AL"(under airportControl, need to get the control in Step2)
			alTicketMap = getAlTickets(etickets,getAirPortControlForAllStatus, session);
		} catch(Exception e) {
			/** 
			 * If any errors occurred when checking cpnStatus, just logging then going to Step4.
			 * If any cpnStatus is invalid[airport control] in step4, it will fail by 1A
			 * */
			logger.error(String.format("cancel booking RLOC:[%s] check cpnStatus failure -> check all cpnStatus are all open for update failure! it will continue next step", booking.getOneARloc()), e);
		}
		
		/** 2) make all cpnStatus marked "AL" to open for updating */
		try {
			if(alTicketMap != null && !alTicketMap.isEmpty()) {
				getAirPortControl(alTicketMap , session);
			}
		} catch(Exception e) {
			logger.error(String.format("cancel booking RLOC:[%s] failure -> get airportControl failure!", booking.getOneARloc()), e);
			// Any error occurs, close 1A session
			closeSession(session);
			return false;
		}
		
		/** 3) update all PAX cpnStatus to refund */
		try {
			updateCpnstatus2Refund(etickets, session);			
		} catch(Exception e) {
			logger.error(String.format("cancel booking RLOC:[%s] failure -> update all PAX cpnStatus to refund failure!", booking.getOneARloc()), e);
			return false;
		}
		
		return true;
	}

	/**
	 * Close 1A session
	 * 
	 * @param session
	 */
	private void closeSession(Session session) {
		try {
			pnrSessionInvokService.closeSessionOnly(session);
		} catch (Exception e) {
			logger.error("close session failure!" , e);
		}
	}

	/**
	 * get airport control
	 * 
	 * @param alTicketMap
	 * @param session
	 * @throws BusinessBaseException
	 */
	private void getAirPortControl(Map<String, List<String>> alTicketMap, Session session) throws BusinessBaseException {
		if(alTicketMap == null || alTicketMap.isEmpty()) {
			return;
		}
		
		for(String ticketNumber : alTicketMap.keySet()) {
			List<String> cpnNumbers = alTicketMap.get(ticketNumber);
			if(CollectionUtils.isEmpty(cpnNumbers)) {
				continue;
			}
			
			for(String cpnNumber : cpnNumbers) {
				ticketAirportControlService.getAirportControl(ticketNumber, cpnNumber, session);
			}
		}
	}

	/**
	 * get all cpnStatus = "AL" ticket(including ticketNumber & cpnNumbers)
	 * map format:
	 * 			key    - ticketNumber
	 * 			value  - list of cpnNumbers under ticketNumber(key)
	 * 
	 * @param etickets
	 * @param session
	 * @return
	 * @throws BusinessBaseException
	 */
	private Map<String, List<String>> getAlTickets(List<ETicket> etickets,boolean getAirPortControlForAllStatus, Session session) throws BusinessBaseException {
		Map<String, List<String>> ticketMap = new HashMap<>();		
		List<String> ticketNumbers = etickets.stream().map(ETicket::getNumber).distinct().collect(Collectors.toList());
		TicketProcessInfo ticketProcessInfo = ticketProcessInvokeService.getTicketProcessInfoWithoutCache(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, ticketNumbers, session);
		
		for(String ticketNumber : ticketNumbers) {
			if(StringUtils.isEmpty(ticketNumber)) {
				continue;
			}
			for(TicketProcessDocGroup docGroup : ticketProcessInfo.getDocGroups()) {
				if (CollectionUtils.isEmpty(docGroup.getDetailInfos())) {
					continue;
				}
				for(TicketProcessDetailGroup detailGroup : docGroup.getDetailInfos()) {
					if(detailGroup == null || !ticketNumber.equals(detailGroup.getEticket())) {
						continue;
					}
					for(TicketProcessCouponGroup couponGroup : detailGroup.getCouponGroups()) {
						if(couponGroup == null || CollectionUtils.isEmpty(couponGroup.getCouponInfos())) {
							continue;
						}
						for(TicketProcessCouponInfo couponInfo : couponGroup.getCouponInfos()) {
							if(couponInfo != null && (OneAConstants.TICKET_COUPON_STATUS_AL.equals(couponInfo.getCpnStatus())||getAirPortControlForAllStatus)) {
								putCpnNumber2TicketMap(ticketMap, ticketNumber, couponInfo.getCpnNumber());
							}
						}
					}
				}
			}
		}
		
		return ticketMap;
	}

	/**
	 * put cpnNumber into map by ticketNumber
	 * 
	 * @param ticketMap
	 * @param ticketNumber
	 * @param cpnNumber
	 */
	private void putCpnNumber2TicketMap(Map<String, List<String>> ticketMap, String ticketNumber, String cpnNumber) {
		if(ticketMap == null || StringUtils.isEmpty(ticketNumber) || StringUtils.isEmpty(cpnNumber)) {
			return;
		}
		
		List<String> cpnNumbers = ticketMap.get(ticketNumber);
		if(CollectionUtils.isEmpty(cpnNumbers)) {
			cpnNumbers = new ArrayList<>();
			cpnNumbers.add(cpnNumber);
			ticketMap.put(ticketNumber, cpnNumbers);
		} else {
			cpnNumbers.add(cpnNumber);
		}
	}

	/**
	 * update all PAX cpnStatus to refund
	 * 
	 * @param etickets
	 * @param session
	 * @throws BusinessBaseException
	 */
	private void updateCpnstatus2Refund(List<ETicket> etickets, Session session) throws BusinessBaseException {
		for(int j = 0; j < etickets.size(); j++) {
			ETicket eticket = etickets.get(j);
			if(eticket == null || StringUtils.isEmpty(eticket.getNumber()) || CollectionUtils.isEmpty(eticket.getCpnNumbers())) {
				continue;
			}
			List<String> cpnNumbers = eticket.getCpnNumbers();
			for(int i = 0; i < cpnNumbers.size(); i++) {
				if(j== etickets.size()-1 && i == cpnNumbers.size()-1) {
					// set session status END
					session.setStatus(SessionStatus.END.getStatus());
				}
				ticketAirportControlService.updateCpnstatus(OneAConstants.TICKET_COUPON_STATUS_RF, eticket.getNumber(), cpnNumbers.get(i), session);
			}
		}
	}

	/**
	 * remove all TK elements in PNR
	 * 
	 * @param rloc
	 * @param session
	 * @param pnrReply
	 * @throws BusinessBaseException
	 */
	private void removeAllTkElements(String rloc, Session session, PNRReply pnrReply) throws BusinessBaseException {
		List<String> otNumbers = pnrResponseParser.getAllTkOTnumbers(pnrReply);
		Map<String, List<String>> map = new HashMap<>();
		map.put(OneAConstants.OT_QUALIFIER, otNumbers);
		deletePnrService.deletePnrWithoutParser(rloc, map, session);
	}

	/**
	 * delete cancel booking element
	 * 
	 * @param rloc
	 * @param session
	 * @return 
	 * @throws BusinessBaseException
	 */
	private PNRReply removeCancelledElement(String rloc, Session session) throws BusinessBaseException {
		return deletePnrService.cancelBooking(rloc, session);
	}
	
	/**
	 * add TK TL with queue office
	 * 
	 * @param rloc
	 * @param session
	 * @throws BusinessBaseException
	 */
	private void addTkTlElements(String rloc, Session session) throws BusinessBaseException {
		AddBookingBuilder builder = new AddBookingBuilder();
		PNRAddMultiElements request = builder.buildTkTlRequest(rloc, session);
		addPnrElementsInvokeService.addMutiElements(request, session);
	}

	/**
	 * add RF element to identify who did cancelBooking actions
	 * 
	 * @param rloc
	 * @param session
	 * @throws BusinessBaseException
	 */
	private void addRfForCancelBooking(String rloc, Session session) throws BusinessBaseException {
		AddBookingBuilder builder = new AddBookingBuilder();
		PNRAddMultiElements request = builder.buildRFRequest(rloc, session);
		addPnrElementsInvokeService.addMutiElements(request, session);
	}

	/**
	 * canRefund conditions:
	 * 1. enter cancel booking flow BOH booking
	 * 2. User enters the cancel booking flow within 24hrs after ticket is issued
	 * 3. enters the cancel booking flow >= 7 days from STD of 1st sector in booking
	 * 4. ET coupon status is marked as "I" or "AL"
	 * 5. It is a redemption booking (Not redemption upgrade booking- check any FQTU existing)
	 * 6. There is OSI YY BOOKING SITE US-CX element in the booking
	 * 
	 * @param booking
	 * @return
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> checkCanRefund(Booking booking) throws UnexpectedException {
		List<ErrorInfo> errors = new ArrayList<>();
		if(booking == null) {
			throw new UnexpectedException("Cannot find booking infomation for cancel booking.", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** 1. enter cancel booking flow BOH booking */
		if(BooleanUtils.isTrue(booking.isBookingOnhold())){
			logger.info(String.format("EligibilityCheck RefundRevBooking Fail - BOH, booking RLOC:[%s]", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_BOOKING_IS_ONHLOD);
			errors.add(error);
			return errors;
		}
		
		if(BooleanUtils.isFalse(booking.isRedemptionBooking())) {
			return Collections.emptyList();
		}
		Date gmtDate = DateUtil.getGMTTime();
		
		/** 2. enter cancel booking flow within 24hrs after ticket is issued */
		if(!checkTicketIssuedTime(booking, gmtDate)) {
			logger.info(String.format("CancelBooking checkCanRefund: false, booking RLOC:[%s] enter cancelbooking flow not within 24hrs after ticket is issued", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_TICKET_ISSUED_INVAILD_TIME);
			errors.add(error);
		}
		
		/**3. enters the cancel booking flow >= 7 days from STD of 1st sector in booking */
		if(!checkDepartureDate(booking, gmtDate)) {
			logger.info(String.format("CancelBooking checkCanRefund: false, booking RLOC:[%s] cancel booking flow < 7 days from STD of 1st sector", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_7_DAYS_LESS_THAN_FIRST_SEGMENT_STD);
			errors.add(error);
		}
		
		/**4. ET coupon status is marked as "I" or "AL" */
		if(!checkTicketCouponStatus(booking)) {
			logger.info(String.format("CancelBooking checkCanRefund: false, booking RLOC:[%s] can't find ET coupon status marked as I or AL", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_VAILD_TICKET_COUPON_STATUS_NOTFOUND);
			errors.add(error);
		}
		
		/**5. check booking is redemption but has no FQTU */
		if(BooleanUtils.isFalse(booking.isRedemptionBooking()) || BooleanUtils.isTrue(booking.isHasFqtu())) {
			logger.info(String.format("CancelBooking checkCanRefund: false, booking RLOC:[%s] isRedemptionBooking:%s, hasFQTU:%s",
					booking.getOneARloc(), booking.isRedemptionBooking(), booking.isHasFqtu()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_NOREDEMPTION_OR_REDEMPTION_HAVE_FQTU);
			errors.add(error);
		}
		
		/**6. There is OSI YY BOOKING SITE US-CX element in the booking */
		if(!checkBookingSite(booking)) {
			logger.info(String.format("CancelBooking checkCanRefund: false, booking RLOC:[%s] have no OSI YY BOOKING SITE US-CX element", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_VAILD_BOOKINGSITE_NOTFOUND);
			errors.add(error);
		}
		
		return errors;
	}
	
	/**
	 * check ET coupon status is marked as "I" or "AL"
	 * 
	 * @param booking
	 * @return
	 */
	private boolean checkTicketCouponStatus(Booking booking) {
		return booking.getPassengerSegments().stream().map(PassengerSegment::getEticketCouponStatus)
				.anyMatch(s -> s.contains(OneAConstants.TICKET_COUPON_STATUS_AL) || s.contains(OneAConstants.TICKET_COUPON_STATUS_I));
	}

	/**
	 * enters the cancel booking flow >= 7 days from STD of 1st sector in booking
	 * 
	 * @param booking
	 * @param gmtDate
	 * @return
	 */
	private boolean checkDepartureDate(Booking booking, Date gmtDate) {
		Segment segment = Optional.ofNullable(booking.getSegments()).orElse(Collections.emptyList()).stream()
				.filter(s -> s.getDepartureTime() != null && s.getDepartureTime().getScheduledTime() != null)
				.findFirst().orElse(null);
		if(segment == null) {
			logger.warn(String.format("can't get vaildate departureTime from segments in booking: RLOC[%s]", booking.getOneARloc()));
			return false;
		}
		DepartureArrivalTime departureTime = segment.getDepartureTime();
		
		try {
			Date gmtSTD = DateUtil.getStrToGMTDate(DepartureArrivalTime.TIME_FORMAT, departureTime.getTimeZoneOffset(), departureTime.getScheduledTime());
			int diffHrs = DateUtil.getDifferenceHours(gmtSTD, gmtDate);
			return diffHrs >= 24*7;
		} catch (ParseException e) {
			logger.warn(String.format("convert STD:[%s] to GMT date failure for cancel booking: RLOC[%s] refund checking",
					departureTime.getScheduledTime(), booking.getOneARloc()));
			return false;
		}
	}

	/**
	 * check OSI YY BOOKING SITE US-CX element
	 * US: country, CX: company.
	 * 
	 * @param booking
	 * @return
	 */
	private boolean checkBookingSite(Booking booking) {
		OSIBookingSite bookingSite = booking.getOsiBookingSite();
		if(bookingSite == null) {
			return false;
		}
		return MMBBizruleConstants.COUNTRY_CODE_USA.equals(bookingSite.getCountry()) 
				&& OneAConstants.COMPANY_CX.equals(bookingSite.getCompany());
	}

	/**
	 * check whether cancel booking flow within 24hrs after ticket is issued
	 * 
	 * @param booking
	 * @param gmtDate 
	 * @return
	 * @throws UnexpectedException 
	 */
	private boolean checkTicketIssuedTime(Booking booking, Date gmtDate) throws UnexpectedException {
		if(BooleanUtils.isFalse(booking.isHasIssuedTicket())) {
			return false;
		}
		
		Date ticketIssuedDate = BookingBuildUtil.getEarliestTicketIssueDate(booking.getPassengerSegments(), booking.getOfficeTimezone());
		if(ticketIssuedDate == null) {
			throw new UnexpectedException(String.format("Gotten ticketIssuedDate is empty, booking RLOC:[%s] officeTimeOffset:[%s]", booking.getOneARloc(), booking.getOfficeTimezone()),
					new ErrorInfo(ErrorCodeEnum.ERR_BLOCK_REFUND_TICKETING_DATE_NOTFOUND));
		}
		
		logger.info(String.format("CancelBooking checkCanRefund => check ticket issued time, ticketIssuedDate: %s, currentDate: %s, booking office timeZone: %s", ticketIssuedDate, gmtDate, booking.getOfficeTimezone()));
		
		int diffHrs = DateUtil.getDifferenceHours(gmtDate, ticketIssuedDate);
		return diffHrs >= 0 && diffHrs <= bookingRefoundBlockwindowHour;
	}
	
	/**
	 * Check Ibe cancnel booking eligibility 
	 * @param booking
	 * @param loginInfo
	 * @return
	 * @throws UnexpectedException
	 */
	private boolean checkBookingCanCancelIbeFlow(Booking booking,PNRReply pnrReply, String mmbToken) {
		
		// check UN/TK segment in booking
		if (booking.getSegments().stream().anyMatch(seg -> OneAConstants.SEGMENTSTATUS_CANCEL.equals(seg.getSegmentStatus().getPnrStatus())
				|| OneAConstants.SEGMENTSTATUS_TK.equals(seg.getSegmentStatus().getPnrStatus()))) {
			logger.info("Cancel booking check, protection booking found, will ignore IBE check.");
			return false;
		}
		
		boolean dwBooking = bookingBuildHelper.anySectorUnderDynamicWaiverStartWithRefundFeeWaiverCode(pnrReply, booking.getBookingCreateInfo().getRpOfficeId(), mmbToken, booking.getOneARloc());
		if(dwBooking){
			logger.info("Dynamic waiver booking, will ignore IBE cancel/refund flow.");
			return false;
		}

		if(booking.isBookingOnhold()) {
			logger.info("BOH booking, will ignore IBE cancel/refund flow.");
			return false;
		}
		try {
			logger.info("Booking is NOT Dynamic waiver or BOH, continue to check cancel booking in IBE flow");
			CancelBookingEligibilityRequest request = new CancelBookingEligibilityRequest();
			request.setRloc(booking.getOneARloc());
			request.setCreationDate(getBookingCreationDataForIBE(booking.getBookingCreateInfo()));
			CancelBookingEligibilityResponse response = ibeService.checkCancelBookingEligibility(request);
			if (response != null) {
				if (response.isFlag()) {
					return response.isFlag();
				} else {
					// ignore the checked in error code if there only checkin error
					return CollectionUtils.isNotEmpty(response.getErrors()) && response.getErrors().size() == 1
							&& ibeCancelBookingCheckedInErrorCode.equalsIgnoreCase(response.getErrors().get(0).getCode())
							//defect OLSSMMB-19598, go to mb inflow if IBE return checkin error and mmb cannot find checked sector
							&& booking.getPassengerSegments().stream().anyMatch(PassengerSegment::isCheckedIn);
				}

			}

		} catch (Exception e) {
			logger.error("invok ibe cancel booking check failed.", e);
		}

		return false;

	}
	
	private String getBookingCreationDataForIBE(RetrievePnrBookingCerateInfo bookingCreateInfo){
		
		if (bookingCreateInfo != null && StringUtils.isNotEmpty(bookingCreateInfo.getCreateTime())
				&& StringUtils.isNotEmpty(bookingCreateInfo.getCreateDate())) {
			Date createDate = getBookingCreateDate(bookingCreateInfo);
			return DateUtil.getDate2Str(CancelBookingEligibilityRequest.CREATION_DATE_FORMAT, "+0800", createDate);
			 
		}
		return null;
		
	}
	/**
	 * check the booking if can cancel
	 * @param booking
	 * @param loginInfo
	 * @param checkInCheck
	 * @return
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> checkBookingCanCancelMbFlow(Booking booking, LoginInfo loginInfo, boolean checkInCheck) throws UnexpectedException {
		List<ErrorInfo> errors = new ArrayList<>();
		if (booking == null) {
			throw new UnexpectedException("Cannot find booking infomation for cancel booking.", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		if(booking.isGroupBooking() || booking.isMiceBooking() || booking.isStaffBooking()){
			logger.info(String.format("Cannot cancel booking of [%s], because group/mice/staff booking", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_GROUP);
			errors.add(error);
		// cancel booking in white list check. OLSSMMB-9702&&7238
		}
		if(!bookingBuildHelper.isCBWLBooking(booking.getOfficeId())){
			logger.info(String.format("Cannot cancel booking of [%s], because it is not CBWL(cancel booking white list) booking, and isn't member redemention non fqtu booking.", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_NOTDIRECT);
			errors.add(error);
		}
		
		/**
		 * RedemptionBooking-
		 *  - FQTV login member id 
		 *  - FQTR login member id
		 * */
		if(booking.isRedemptionBooking()) {
			if(!BookingBuildUtil.hasMemberBooking(booking,loginInfo) && !isFQTRMemberLogin(booking, loginInfo)) {
				logger.info(String.format("Cannot cancel booking of [%s], because it is redemption and not has member login.", booking.getOneARloc()));
				ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_REDEMPTION);
				errors.add(error);
			}
		}
		
		//defect OLSSMMB-7204, split redemption booking, direct booking check and upgrade booking
		if(booking.isHasFqtu()){
			logger.info(String.format("Cannot cancel booking of [%s], because the booking has fqtu.", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_UPGRADE);
			errors.add(error);
		}
		if (booking.getBookingPackageInfo() != null && !booking.getBookingPackageInfo().isFlightOnly()) {
			logger.info(String.format("Cannot cancel booking of [%s], because not flight only", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_NOTFLIHTONLY);
			errors.add(error);
		// create time check
		}
		if(!isUnissuedWaitlistedRedemption(booking) && !isInBookingCancelWindow(booking)){
			logger.info(String.format("Cannot cancel booking of [%s], because not in create time 30 mins", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_CREATETIME);
			errors.add(error);
		}
		
		//Upgrade Bid won segment check
		//Story olssmmb-7285, remove FQUG check
		/*if(BookingBuildUtil.isUpgradeBidWonSegment(booking.getSkList())){
			logger.info(String.format("Cannot cancel booking of [%s], Upgrade Bid won segment", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WRR_BLOCK_CANNEL_FQUG);
			errors.add(error);
		} */
		if (checkInCheck && BookingBuildUtil.anyPassengerCheckedIn(booking)){
			logger.info(String.format("Cannot cancel booking of [%s], the segment checked in.", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_CHECKEDIN);
			errors.add(error);
		}
		
		if(booking.getSegments().stream().allMatch(Segment::isFlown)){
			logger.info(String.format("Cannot cancel booking of [%s], the booking contains flown segment.", booking.getOneARloc()));
			ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_FLOWN);
			errors.add(error);
		}
		//check boh booking
		checkBOHBooking(booking, errors);
		
		return errors;
	}
	
	/**
	 * is FQTR member login or not.
	 * @param booking
	 * @param loginInfo
	 * @return
	 */
	private boolean isFQTRMemberLogin(Booking booking, LoginInfo loginInfo) {
		if(!LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			return false;
		}
		
		return BookingBuildUtil.isFQTRMemberID(booking, loginInfo.getMemberId());
	}
	
	/**
	 * Check boh Booking
	 * @param booking
	 * @param errors
	 */
	private void checkBOHBooking(Booking booking, List<ErrorInfo> errors){
		if(isDepositBOHBooking(booking)) {
			if(!MMBBizruleConstants.COUNTRY_CODE_USA.equals(Optional.ofNullable(booking.getOsiBookingSite()).orElse(new OSIBookingSite()).getCountry())){
				logger.info(String.format("Cannot cancel booking of [%s], the booking is on hold.", booking.getOneARloc()));
				ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_ONHOLD_NON_US);
				errors.add(error);
			}else if(!isInUSCancelWindow(booking)){
				logger.info(String.format("Cannot cancel booking of [%s], the on hold booking is us site, but not in 30 min to 24 hours .", booking.getOneARloc()));
				//In this case, should remove WRR_BLOCK_CANNEL_CREATETIME error because it is repetitive logic of 30mins
				ErrorInfo createdTimeError  = errors.stream().filter(er->ErrorCodeEnum.WARN_BLOCK_CANNEL_CREATETIME.getCode().equals(er.getErrorCode())).findFirst().orElse(null);
				if(createdTimeError!=null){
					errors.remove(createdTimeError);
				}
				ErrorInfo error = new ErrorInfo(ErrorCodeEnum.WARN_BLOCK_CANNEL_ONHOLD_US);
				errors.add(error);
			}
			
		}
	}
	/**
	 * check has deposit 
	 * @param booking
	 * @return
	 */
	private boolean isDepositBOHBooking(Booking booking) {
		return booking.isBookingOnhold() && booking.getOnHoldRemarkInfo() != null
				&& booking.getOnHoldRemarkInfo().getAmount() != null
				&& booking.getOnHoldRemarkInfo().getAmount().compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * check redemption waitlisted booking with unissued ticket
	 * @param booking
	 */
	private boolean isUnissuedWaitlistedRedemption(Booking booking) {
		return BooleanUtils.isNotTrue(booking.isHasIssuedTicket()) && booking.isRedemptionBooking()
				&& isWaitlistedBooking(booking);
	}
	
	/**
	 * check all sectors has waitListed of booking
	 * @param booking
	 * @return
	 */
	private boolean isWaitlistedBooking(Booking booking){
		if(CollectionUtils.isEmpty(booking.getSegments())){
			return false;
		}
		
		for (Segment segment : booking.getSegments()) {
			if (segment.getSegmentStatus() == null || 
					!FlightStatusEnum.WAITLISTED.getCode().equals(segment.getSegmentStatus().getStatus().getCode())) {
				return false;
			}
		}
		
		return true;
	}
	/**
	 * check can cancel booking of create time
	 * @param booking
	 * @return
	 * @throws ParseException 
	 */
	private boolean isInBookingCancelWindow(Booking booking) {

		Date createDate = getBookingCreateDate(booking.getBookingCreateInfo());
		if (createDate == null) {
			return false;
		}

		return createDate.before(DateUtils.addMinutes(Calendar.getInstance().getTime(), -bookingCancelBlockWindowMin));

	}
	
	/**
	 * check can cancel booking of create time
	 * @param booking
	 * @return
	 * @throws ParseException 
	 */
	private boolean isInUSCancelWindow(Booking booking) {

		Date createDate = getBookingCreateDate(booking.getBookingCreateInfo());
		if (createDate == null) {
			return false;
		}

		return createDate.before(DateUtils.addMinutes(Calendar.getInstance().getTime(), -bookingCancelBlockWindowMin))
				&& createDate.after(DateUtils.addMinutes(Calendar.getInstance().getTime(), -US_CANCEL_DEADLINE_MIN));

	}
	
	/**
	 * Get booking create time
	 * @param booking
	 * @return
	 * @throws ParseException
	 */
	private Date getBookingCreateDate(RetrievePnrBookingCerateInfo bookingCreateInfo) {
		
		Date createDate  = null;
		if (bookingCreateInfo == null
				|| StringUtils.isEmpty(bookingCreateInfo.getCreateTime())
				|| StringUtils.isEmpty(bookingCreateInfo.getCreateDate())) {
			return createDate;
		}
		
		try {
			 createDate = DateUtil.getStrToDate(
					RetrievePnrBookingCerateInfo.CREATE_DATE_FOMRAT + RetrievePnrBookingCerateInfo.CREATE_TIME_FOMRAT,
					bookingCreateInfo.getCreateDate() + bookingCreateInfo.getCreateTime(),
					RetrievePnrBookingCerateInfo.TIME_ZONE);
			 
		} catch (Exception e) {
			logger.error(String.format("passer pnr create time failed, pnr create date[%s],pnr create time[%s]",
					bookingCreateInfo.getCreateDate(), bookingCreateInfo.getCreateTime()));
		}
		return createDate;
	}
	/**
	 * Create booking build required for cancel booking
	 * @return
	 */
	private BookingBuildRequired createBookingBuildRequiredForCancelBookingCheck(){
		BookingBuildRequired bookingBuildRequired = new  BookingBuildRequired();
		
		bookingBuildRequired.setTravelDocument(false);
		bookingBuildRequired.setPassenagerContactInfo(true);
		bookingBuildRequired.setEmergencyContactInfo(false);
		bookingBuildRequired.setMealSelection(false);
		bookingBuildRequired.setBaggageAllowances(false);
		bookingBuildRequired.setMemberAward(false);
		
		return bookingBuildRequired;
		
	}
	
	/**
	 * send email for cancel booking sent by 15Blow
	 * 
	 * @param booking
	 * @param locale
	 * @return
	 */
	private void sendEmailForCancelBooking(Booking booking, Locale locale, BookingCancelResponseDTO cancelResponse,boolean refund){
		// send cancel booking email
		try {
			
			for(Passenger pax: booking.getPassengers()) {
				if(!isCanReceiveMail(pax.getContactInfo())){
					continue;
				}
				FIFBelowRequestDTO emailRequestDTO = this.convertToEmailRequest(booking,pax, locale,cancelResponse,refund);
				fifBlowService.sentEmail(emailRequestDTO);
			}
			
		} catch (Exception e) {
			logger.error("System exception. The email won't be sent out.", e);
		}
	}
	
	/**
	 * convert to email request
	 * 
	 * @param booking
	 * @param locale
	 * @param refund
	 */
	private FIFBelowRequestDTO convertToEmailRequest(Booking booking,Passenger pax, Locale locale,BookingCancelResponseDTO cancelResponse,boolean refund) {
		FIFBelowRequestDTO requestDTO = new FIFBelowRequestDTO();
		
		requestDTO.setLanguageCode(locale.getLanguage().toUpperCase());
		// Rloc
		requestDTO.setRloc(booking.getOneARloc());
		requestDTO.setTemplateType(TemplateTypeEnum.EMAIL_TEMPLATE_TYPE_CANCEL_BOOKING);
		
		// convert to paxContacts
		requestDTO.setPaxContacts(Lists.newArrayList());
		ContactInfo contactInfo = Optional.ofNullable(pax).orElseGet(Passenger::new).getContactInfo();
		if (contactInfo != null && CollectionUtils.isNotEmpty(contactInfo.getNotificationEmails()) && !contactInfo.getNotificationEmails().isEmpty()) {
			for (Email email: contactInfo.getNotificationEmails()) {
				PaxContactDTO recipientDTO = new PaxContactDTO();
				recipientDTO.setContactType(ContactTypeEnum.CONTACT_TYPE_EMAIL);
				recipientDTO.setContactDetail(email.getEmailAddress());
				if (email.getType().equals(ContactInfoTypeEnum.APX_CONTACT_INFO.getType()) 
						&& isExistInContactsAPE(requestDTO.getPaxContacts(), email.getEmailAddress())) {
					// For APE, no recipient name and skip create a new recipient if email already exists in other recipient
					continue;
				}

				requestDTO.getPaxContacts().add(recipientDTO);
			}
		}
		// convert to remarks
		convertToRemarks(requestDTO,booking,pax,cancelResponse,refund);
		// convert to segments
		convertToSegments(requestDTO,booking.getSegments());
		// convert to paxInfos
		convertToPaxInfos(requestDTO,booking.getPassengers(),booking);
		// convert to paxSegments
		convertToPaxSegments(requestDTO);
		
		return requestDTO;
	}
	
	
	/**
	 * convert booking segments to email segmentDTOs
	 * @param segments
	 * @return
	 */
	private static void convertToSegments(FIFBelowRequestDTO request,List<Segment> segments) {
		if(CollectionUtils.isEmpty(segments)) {
			return;
		}
		
		int seq = 1;
		for (Segment segment : segments) {
			SegmentDTO segmentDTO = new SegmentDTO();

			// STD time
			String arrivalTime = "";
			String departureTime = "";
			if (segment.getArrivalTime() != null) {
				arrivalTime = DateUtil.convertDateFormat(segment.getArrivalTime().getPnrTime(),
						DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATETIME_FORMAT_XML);
			}

			if (segment.getDepartureTime() != null) {
				departureTime = DateUtil.convertDateFormat(segment.getDepartureTime().getPnrTime(),
						DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATETIME_FORMAT_XML);
			}
			segmentDTO.setSegmentSeq(String.valueOf(seq));
			segmentDTO.setFlightNo(segment.getOperateSegmentNumber());
			segmentDTO.setArrivalDate(arrivalTime);
			segmentDTO.setDepartureDate(departureTime);
			segmentDTO.setCabinClass(segment.getCabinClass());
			if (!StringUtils.isEmpty(segment.getOriginalSubClass())) {
				segmentDTO.setBookingClass(segment.getOriginalSubClass());
			} else {
				segmentDTO.setBookingClass(segment.getSubClass());
			}

			segmentDTO.setCarrierCode(
					StringUtils.isEmpty(segment.getOperateCompany()) ? segment.getMarketCompany() : segment.getOperateCompany());
			segmentDTO.setFlightNo(StringUtils.isEmpty(segment.getOperateSegmentNumber())
					? segment.getMarketSegmentNumber()
					: segment.getOperateSegmentNumber());

			segmentDTO.setArrivalAirport(segment.getDestPort());
			segmentDTO.setDepartAirport(segment.getOriginPort());
			request.findSegments().add(segmentDTO);
			seq++;
		}
		
	}
	
	/**
	 * convert booking passengers to email paxInfos
	 * @param passengers
	 * @param booking
	 * @return paxInfos
	 */
	private void convertToPaxInfos(FIFBelowRequestDTO request,List<Passenger> passengers,Booking booking) {
		if(CollectionUtils.isEmpty(passengers)) {
			return ;
		}
		
		int seq=1;
		for(Passenger passenger : passengers) {
			PaxInfo paxDto = new PaxInfo();
			paxDto.setPaxSeq(String.valueOf(seq));
			paxDto.setFirstName(passenger.getGivenName());
			paxDto.setLastName(passenger.getFamilyName());
			paxDto.setTitle(passenger.getTitle());
			if(BooleanUtils.isTrue(passenger.getLoginMember())){
				List<FQTVInfo> fqtvInfos = getFQTVsByPaxId(passenger.getPassengerId(),booking);
				FQTVInfo fqtvInfo = fqtvInfos.stream().filter(info -> info != null 
						&& TicketReminderConstant.OPERATE_CX.equals(info.getCompanyId())).findFirst().orElse(new FQTVInfo());
				if(!StringUtils.isEmpty(fqtvInfo.getMembershipNumber())){
					FFPInfoDTO ffpInfo =new FFPInfoDTO();
					ffpInfo.setMemberTier(fqtvInfo.getTierLevel());
					ffpInfo.setMemberNo(fqtvInfo.getMembershipNumber());
					paxDto.setFfpInfo(ffpInfo);
				}
			}
			request.findPaxInfos().add(paxDto);
			seq++;
		}
	}
	
	/**
	 * convert booking passengers to email PaxSegments
	 * @param request
	 */
	private void convertToPaxSegments(FIFBelowRequestDTO request) {
		
		if(CollectionUtils.isEmpty(request.getSegments()) || CollectionUtils.isEmpty(request.getPaxInfos())){
			return;
		}
		
		for (SegmentDTO segmentDTO : request.getSegments()) {
			for (PaxInfo paxInfo : request.getPaxInfos()) {
				PaxSegmentDTO  paxSegment = new PaxSegmentDTO();
				paxSegment.setPaxSeq(paxInfo.getPaxSeq());
				paxSegment.setSegmentSeq(segmentDTO.getSegmentSeq());
				paxSegment.setBookingClass(segmentDTO.getBookingClass());
				request.findPaxSegments().add(paxSegment);
			}
		}
	}
	
	/**
	 * convert booking passengers to email remarks
	 * @param request
	 */
	private void convertToRemarks(FIFBelowRequestDTO request, Booking booking, Passenger pax,BookingCancelResponseDTO cancelResponse,boolean refund) {
		RemarkDTO remark = new RemarkDTO();
		// remark greet passenger
		int seq = getPassengerSeqById(pax.getPassengerId(), booking.getPassengers());
		if (seq > 0) {
			remark.setType(RemarkTypeEnum.REMARK_TRPE_RECIPIENT);
			remark.setRemark(String.valueOf(seq));
			request.findRemarks().add(remark);
		}
		remark = new RemarkDTO();
		// remark booking attributes
		if (BooleanUtils.isTrue(booking.isRedemptionBooking())) {
			remark.setType(RemarkTypeEnum.REMARK_TRPE_BOOKING);
			if (BooleanUtils.isTrue(cancelResponse.isRefunded())) {
				remark.setRemark(CommonConstants.EMAIL_REMARK_REFUND_REDEEM);
			} else {
				remark.setRemark(CommonConstants.EMAIL_REMARK_NONREFUND_REDEEM);
			}
			request.findRemarks().add(remark);
		} else {
			remark.setType(RemarkTypeEnum.REMARK_TRPE_BOOKING);
			if (BooleanUtils.isTrue(cancelResponse.isRefunded())) {
				remark.setRemark(CommonConstants.EMAIL_REMARK_AUTOREFUND_REVENUE);
			} else if(BooleanUtils.isNotTrue(refund)){
				remark.setRemark(CommonConstants.EMAIL_REMARK_NONREFUND_REVENUE);
			} else{
				remark.setRemark(CommonConstants.EMAIL_REMARK_FAILREFUND_REVENUE);
			}
			request.findRemarks().add(remark);
		}
	}
	
	/**
	 * get list of FQTV by passenger ID
	 * 
	 * @param passengerId
	 * @param booking
	 * @return
	 */
	private List<FQTVInfo> getFQTVsByPaxId(String passengerId, Booking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || StringUtils.isEmpty(passengerId)) {
			return Collections.emptyList();
		}
		return booking.getPassengerSegments().stream().filter(ps -> ps != null && StringUtils.equalsIgnoreCase(ps.getPassengerId(), passengerId)
				&& ps.getFqtvInfo() != null && !StringUtils.isEmpty(ps.getFqtvInfo().getMembershipNumber()))
				.map(PassengerSegment::getFqtvInfo).collect(Collectors.toList());
	}

	/**
	 * Get is exist in contacts APE
	 * 
	 * @param paxContacts
	 * @param emailAddress
	 * @return 
	 */
	private boolean isExistInContactsAPE(List<PaxContactDTO> paxContacts, String emailAddress) {
		if (emailAddress == null) {
			return false;
		}
		
		for(PaxContactDTO paxContact: paxContacts) {
			if (emailAddress.equals(paxContact.getContactDetail())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check passenger is CanReceiveMail
	 * @param passenger
	 * @return
	 */
	private boolean isCanReceiveMail(ContactInfo contactInfo) {
		if(contactInfo==null ||  CollectionUtils.isEmpty(contactInfo.getNotificationEmails())){
			return false;
		}
		
		return contactInfo.getNotificationEmails().stream().anyMatch(email -> !StringUtils.isEmpty(email.getType())
				&& !StringUtils.isEmpty(email.getEmailAddress()));
	}
	
	 /**
		* get passenger sequence by passenger ID
		* @param passengerId
		* @param booking
		* @return
		*/
		private int getPassengerSeqById(String passengerId, List<Passenger> passengers) {
			int seq=0;
			for (Passenger passenger : passengers) {
				if(StringUtils.equalsIgnoreCase(passenger.getPassengerId(), passengerId)){
					  seq++;
					  break;
				}
			}
			return seq;
		}
	/**
	 * set the refund flag and reasons to check can cancel response dto
	 * @param booking
	 * @param result
	 * @throws UnexpectedException
	 */
	private void setMbRefoundFlagAndReasonsToResponse(Booking booking,BookingCancelCheckResponseDTO result) throws UnexpectedException{
		try {
			List<ErrorInfo> refundCheckErrors = checkCanRefund(booking);
			if (CollectionUtils.isEmpty(refundCheckErrors)) {
				if(BooleanUtils.isNotTrue(booking.isRedemptionBooking())){
					logger.info("EligibilityCheck RefundRevBooking Success.");
				}
				result.setCanRefund(true);
			} else {
				result.setCantRefundReason(refundCheckErrors);
			}
		} catch (UnexpectedException e) {
			if (ErrorCodeEnum.ERR_BLOCK_REFUND_TICKETING_DATE_NOTFOUND.getCode().equals(e.getErrorInfo().getErrorCode())) {
				logger.info(e.getMessage());
				result.addCantRefundReason(e.getErrorInfo());
			} else {
				throw e;
			}
		}
	}

	@Override
	public CancelBookingDataResponseDTO getCancelBookingData(CancelBookingDataRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {

		
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getOneARloc());
		if (retrievePnrBooking == null) {
			throw new ExpectedException("Retrieve PNR failed", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		//check primary passenger
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ELIGIBILITY_CHECK, "TRUE");
		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.CREATION_DATE, getBookingCreationDataForIBE(retrievePnrBooking.getBookingCreateInfo()));

		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ENTRYLANGUAGE, requestDTO.getEntryLanguage());
		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ENTRYCOUNTRY, requestDTO.getEntryCountry());
		try {
			uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ENTRYPOINT, URLEncoder.encode(requestDTO.getEntryPoint(),"utf-8"));
			uriComponentsBuilder.queryParam(IBERedirectParameterConstants.RETURNURL, URLEncoder.encode(requestDTO.getReturnUrl(),"utf-8"));
			uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ERRORURL, URLEncoder.encode(requestDTO.getErrorUrl(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Url encoder failed.",e);
		}
		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.ONEA_RLOC, retrievePnrBooking.getOneARloc());
		uriComponentsBuilder.queryParam(IBERedirectParameterConstants.OFFICE_ID, retrievePnrBooking.getOfficeId());
		
		if(CollectionUtils.isNotEmpty( retrievePnrBooking.getSegments())) {
			uriComponentsBuilder.queryParam(String.format(IBERedirectParameterConstants.ORIGIN, 1), retrievePnrBooking.getSegments().get(0).getDestPort());
			uriComponentsBuilder.queryParam(IBERedirectParameterConstants.PAX_FIRSTNAME, retrievePnrBooking.getPassengers().get(0).getGivenName());
			uriComponentsBuilder.queryParam(IBERedirectParameterConstants.PAX_LASTNAME, retrievePnrBooking.getPassengers().get(0).getFamilyName());
		}
		
		String queryPars = uriComponentsBuilder.build().getQuery();
		logger.info("cancel booking query parameteres before encrypt:" + queryPars);
		String keyId = ibeEncryptionHelper.getActiveKeyId();
		String encryptedData = ibeEncryptionHelper.encryptMessage(queryPars, keyId);
		
		CancelBookingDataResponseDTO responseDTO = new CancelBookingDataResponseDTO();
		responseDTO.setAedKey(keyId);
		responseDTO.setEncryptedData(encryptedData);
		return responseDTO;
	}
}