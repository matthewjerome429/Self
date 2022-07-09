package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.BookingRefMapping;
import com.cathaypacific.mbcommon.model.common.Passenger;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OjConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.dto.common.booking.BookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.BookingOrderDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SegmentDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByEticketResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.RefreshBookingResponseDTO;
import com.cathaypacific.mmbbizrule.handler.DTOConverter;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.impl.ConsentInfoServiceImpl;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.LoginInfoUtil;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.common.base.Objects;

@Service
public class RetrievePnrBusinessImpl implements RetrievePnrBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrBusinessImpl.class);


	@Autowired
	private OJBookingService ojBookingService;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private DTOConverter dtoConverter;

	@Autowired
	private ConsentInfoServiceImpl consentInfoService;

	@Autowired
	private RetrieveProfileService retrieveProfileService;

	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;

	@Autowired
	private MaskHelper maskHelper;

	@Override
	public ReceivePnrByRlocResponseDTO bookingLoginByReference(String rloc, String familyName, String givenName,
			String unAuthMbToken, BookingBuildRequired required) throws BusinessBaseException {

		// build login before login success, will store to redis after login success.
		LoginInfo loginInfo = LoginInfoUtil.createLoginInfoForNonmember(rloc, familyName, givenName, null, LoginInfo.LOGINTYPE_RLOC, unAuthMbToken);

		ReceivePnrByRlocResponseDTO response = new ReceivePnrByRlocResponseDTO();
		try {

			BookingDTO bookingDTO = retrieveBookingDTOByROneARlocOrSpnr(loginInfo,rloc,required);

			// handle oneA or OJ error
			if(!bookingDTO.getRetrieveOneA()) {
				response.setRetrieveOneASuccess(false);
			} else if (!bookingDTO.getRetrieveOJ()) {
				response.setRetrieveOJSuccess(false);
			}

			response.setBooking(bookingDTO);

			// add login info to response
			response.setMmbToken(loginInfo.getMmbToken());
			response.setLoginSuccess(true);
			// save login info to redis

			mbTokenCacheRepository.add(loginInfo.getMmbToken(), TokenCacheKeyEnum.LOGININFO, null, loginInfo);

			logger.info("bookingLoginByReference", "[Rloc Login Success]Stored login info to redis for rloc login", "Rloc login", loginInfo, loginInfo.getMmbToken(), true);

			// save rloc to redis
			saveValidFlightRlocs(bookingDTO, unAuthMbToken);

			// save rlocMapping to redis
			saveRlocMapping(bookingDTO, response.getMmbToken());
			maskForFlightBookingDTO(bookingDTO);

		} catch (Exception e) {
			logger.info("bookingLoginByReference", "[Rloc Login Failed]", "Rloc login", loginInfo, loginInfo.getMmbToken());
			throw e;
		}
		return response;
	}


	@Override
	public ReceivePnrByRlocResponseDTO bookingByPNR(PNRReply pnr,String rloc, String familyName, String givenName,
			String unAuthMbToken, BookingBuildRequired required) throws BusinessBaseException {

		LoginInfo loginInfo = LoginInfoUtil.createLoginInfoForNonmember(rloc, familyName, givenName, null, LoginInfo.LOGINTYPE_RLOC, unAuthMbToken);
		ReceivePnrByRlocResponseDTO response = new ReceivePnrByRlocResponseDTO();
		BookingDTO bookingDTO = retrieveBookingDTOByPNR(pnr, loginInfo,required);

		// handle oneA or OJ error
		if (!bookingDTO.getRetrieveOneA()) {
			response.setRetrieveOneASuccess(false);
		} else if (!bookingDTO.getRetrieveOJ()) {
			response.setRetrieveOJSuccess(false);
		}

		response.setBooking(bookingDTO);

		// add login info to response
		response.setMmbToken(loginInfo.getMmbToken());
		response.setLoginSuccess(true);
		return response;
	}

	/**
	 * Eticket login flow
	 *
	 * @param familyName
	 * @param givenName
	 * @param eticket
	 * @return
	 * @throws Exception
	 */
	@Override
	public ReceivePnrByEticketResponseDTO bookingLoginByEticket(String familyName, String givenName, String eticket,
			String unAuthMbToken, BookingBuildRequired required) throws BusinessBaseException {
		// build login before login success, will store to redis after login success.
		LoginInfo loginInfo = LoginInfoUtil.createLoginInfoForNonmember(null, familyName, givenName, eticket, LoginInfo.LOGINTYPE_ETICKET, unAuthMbToken);
		ReceivePnrByEticketResponseDTO response = new ReceivePnrByEticketResponseDTO();

		String rloc = ticketProcessInvokeService.getRlocByEticket(eticket);

		if (StringUtils.isEmpty(rloc)) {
			throw new ExpectedException(String.format("Cannot find rloc by eticket:%s", eticket), new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}

		loginInfo.setLoginRloc(rloc);

		try {
			BookingDTO bookingDTO = retrieveBookingDTOByROneARlocOrSpnr(loginInfo,rloc,required);

			// store login info
			response.setMmbToken(unAuthMbToken);
			response.setLoginSuccess(true);

			// save login info to redis
			mbTokenCacheRepository.add(loginInfo.getMmbToken(), TokenCacheKeyEnum.LOGININFO, null, loginInfo);
			logger.info("bookingLoginByEticket", "[Eticket Login Success]Stored login info to redis for eticket login",
					"Eticket login", loginInfo, loginInfo.getMmbToken(), true);
			// save rloc to redis
			saveValidFlightRlocs(bookingDTO, unAuthMbToken);

			// save rlocMapping to redis
			saveRlocMapping(bookingDTO, response.getMmbToken());

			maskForFlightBookingDTO(bookingDTO);

			response.setBooking(bookingDTO);
		} catch (Exception e) {
			logger.info("bookingLoginByEticket", "[Eticket Login Failed]", "Eticket login", loginInfo, loginInfo.getMmbToken());
			throw e;
		}

		return response;
	}

	@Override
	public Booking retrieveFlightBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException {
		logger.info(String.format("Retrieve flight booking .login type Member, Rloc[%s]", rloc));
		return retrievePnrByRloc(rloc, loginInfo, required);
	}

	@Override
	public RefreshBookingResponseDTO refreshBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException, ParseException {
		RefreshBookingResponseDTO response = new RefreshBookingResponseDTO();
		BookingDTO booking = null;
		BookingRefMapping rlocMapping = getRlocMappingByRloc(rloc, loginInfo.getMmbToken());

		if(rlocMapping != null) {
			booking = refreshBookingByRlocMapping(loginInfo, rlocMapping, required);
		} else {
			logger.info(String.format("Refreshing booking. rloc[%s] ", rloc));
			booking = retrieveBookingDTOByROneARlocOrSpnr(loginInfo,rloc, required);
		}

		if(booking != null) {
			// handle oneA & OJ error
			if(BooleanUtils.isFalse(booking.getRetrieveOneA())) {
				response.setRetrieveOneASuccess(false);
			}
			if(BooleanUtils.isFalse(booking.getRetrieveOJ())) {
				response.setRetrieveOJSuccess(false);
			}
			response.setBooking(booking);
		}
		// save rloc mapping
		saveRlocMapping(response.getBooking(), loginInfo.getMmbToken());
		maskForFlightBookingDTO(booking);

		// OLSSMMB-16763: Don't remove me!!!
        // --- begin ---
		String bookingType = response.getBooking().getBookingType();
		logger.info(String.format("Retrieve Booking | Rloc | %s |  Booking Type | %s", rloc, bookingType), true);

		if(BooleanUtils.isTrue(response.getBooking().getStaffBooking())) {
            logger.info(String.format("Retrieve Booking | Rloc | %s |  Staff Booking", rloc), true);
        }
        // --- end ---
		return response;
	}

	/**
	 * refreshBooking by rloc Mapping through async calling 1A and OJ
	 *
	 * @param loginInfo
	 * @param rlocMapping
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	private BookingDTO refreshBookingByRlocMapping(LoginInfo loginInfo, BookingRefMapping rlocMapping, BookingBuildRequired required) throws BusinessBaseException {

		// OJ booking, async to get OJ booking
		Future<OJBooking> asyncOjBooking = null;
		if(StringUtils.isNotBlank(rlocMapping.getOjRloc())){
			asyncOjBooking  = asyncGetOjBooking(loginInfo, rlocMapping);
		}

		// OneA booking, sync to get 1A booking
		FlightBookingDTO flightBookingDTO = null;
		boolean ojbookingRetrieveFailed = false;

		if(StringUtils.isNotBlank(rlocMapping.getOneARloc())){
			flightBookingDTO = getOneABooking(loginInfo, rlocMapping, required);
		}

		OJBooking ojBooking = null;

		if (asyncOjBooking != null) {
			try {
				ojBooking = asyncOjBooking.get();
				if (ojBooking != null && OjConstants.CANCELLED.equals(ojBooking.getBookingStatus())) {
					logger.info(String.format("Booking found by ojRloc:%s from OJService cancelled", ojBooking.getBookingReference()));
					ojBooking = null;
				}
			} catch (Exception e) {
				ojbookingRetrieveFailed = true;
				logger.warn(String.format("Error to retrieve OneA booking by oneARloc: %s", rlocMapping.getOneARloc()), e);
			}
		}

		BookingDTO	bookingDTO = crossRetrieveBookingDTOFromOneaAndOj(loginInfo,required, flightBookingDTO, false, ojBooking, false);

		checkBooking(bookingDTO);

		if(bookingDTO == null){
			bookingDTO = new BookingDTO();
		}

		bookingDTO.setRetrieveOJ(!ojbookingRetrieveFailed);
		bookingDTO.setRetrieveOneA(true);//always set 1A as true, because will throw error if 1A down

		return bookingDTO;
	}

	/**
	 * async get ojBooking by OjRloc in rlocMapping
	 *
	 * @param loginInfo
	 * @param rlocMapping
	 * @return
	 * @throws BusinessBaseException
	 */
	private Future<OJBooking> asyncGetOjBooking(LoginInfo loginInfo, BookingRefMapping rlocMapping) throws BusinessBaseException {
		String givenName = loginInfo.getLoginGivenName();
		String familyName = loginInfo.getLoginFamilyName();

		Passenger primaryPax = rlocMapping.getPrimaryPax();
		if(primaryPax != null) {
			givenName = primaryPax.getGivenName();
			familyName = primaryPax.getFamilyName();
		}

		String ojRloc = rlocMapping.getOjRloc();
		
		/*if(OjConstants.CANCELLED.equals(ojBooking.getBookingStatus())) {
			logger.info(String.format("Booking found by ojRloc:%s from OJService cancelled", ojRloc));
			return new AsyncResult<>(null);
		}*/

		return ojBookingService.asyncGetBooking(givenName, familyName, ojRloc);
	}

	/**
	 * async get oneABooking by oneARloc in rlocMapping
	 *
	 * @param loginInfo
	 * @param rlocMapping
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	private FlightBookingDTO getOneABooking(LoginInfo loginInfo, BookingRefMapping rlocMapping, BookingBuildRequired required) throws BusinessBaseException {
		FlightBookingDTO  flightBooking = dtoConverter.convertToBookingDTO(retrievePnrByRloc(rlocMapping.getOneARloc(), loginInfo, required), loginInfo);
		setSegmentIdAndOrderDate(flightBooking);
		return flightBooking;
	}

	/**
	 * get rlocMapping by rloc
	 *
	 * @param rloc
	 * @param mmbToken
	 * @return BookingRefMapping
	 */
	private BookingRefMapping getRlocMappingByRloc(String rloc, String mmbToken) {
		if(StringUtils.isBlank(rloc)) {
			return null;
		}

		@SuppressWarnings("unchecked")
		List<BookingRefMapping> bookingRefMappings = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.RLOC_MAPPING, null, ArrayList.class);
		if(CollectionUtils.isEmpty(bookingRefMappings)) {
			return null;
		}

		return bookingRefMappings.stream().filter(rf->rloc.equals(rf.getOjRloc())||rloc.equals(rf.getOneARloc())).findFirst().orElse(null);
	}

	/**
	 * save RLOC mapping relationship to redis
	 *
	 * @param booking
	 * @param mbToken
	 */
	private void saveRlocMapping(BookingDTO booking, String mbToken) {
		// only add package booking to the rloc mapping
		if (booking == null ||(booking.getFlightBooking()!=null && BooleanUtils.isNotTrue(booking.getFlightBooking().isPackageBooking()))) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<BookingRefMapping> bookingRefMappings = mbTokenCacheRepository.get(mbToken, TokenCacheKeyEnum.RLOC_MAPPING,
				null, ArrayList.class);

		if (CollectionUtils.isEmpty(bookingRefMappings)) {
			bookingRefMappings = new ArrayList<>();
		}

		boolean existInRedis = bookingRefMappings.stream()
				.anyMatch((bkrf -> (booking.getOneARloc() != null && booking.getOneARloc().equals(bkrf.getOalRloc()))
						|| (booking.getOjRloc() != null && booking.getOjRloc().equals(bkrf.getOjRloc()))));

		if (!existInRedis) {
			BookingRefMapping bookingRefMapping = getNewRlocMapping(booking);
			bookingRefMappings.add(bookingRefMapping);
			mbTokenCacheRepository.add(mbToken, TokenCacheKeyEnum.RLOC_MAPPING, null, bookingRefMappings);
		}

	}

	/**
	 * new BookingRefMapping when this rlocMapping not in redis
	 *
	 * @param booking
	 * @return BookingRefMapping
	 */
	private BookingRefMapping getNewRlocMapping(BookingDTO booking) {

		FlightBookingDTO flightBooking = booking.getFlightBooking();
		BookingRefMapping bookingRefMapping = new BookingRefMapping();
		if (flightBooking != null) {
			bookingRefMapping.setOneARloc(flightBooking.getOneARloc());
			bookingRefMapping.setOalRloc(flightBooking.getGdsRloc());
			if (flightBooking.isPackageBooking()) {
				bookingRefMapping.setOjRloc(booking.getBookingReference());
			}
			bookingRefMapping.setPrimaryPax(getPrimaryPax(flightBooking));
		} else {
			bookingRefMapping.setOjRloc(booking.getBookingReference());
		}
		return bookingRefMapping;
	}

	/**
	 * get primary passenger in flightBooking
	 *
	 * @param flightBooking
	 * @return
	 */
	private Passenger getPrimaryPax(FlightBookingDTO flightBooking) {
		if(flightBooking == null || CollectionUtils.isEmpty(flightBooking.getPassengers())) {
			return null;
		}
		List<PassengerDTO> passengerDTOs = flightBooking.getPassengers();
		for(PassengerDTO passengerDTO : passengerDTOs) {
			if(passengerDTO == null) {
				continue;
			}
			if(BooleanUtils.isTrue(passengerDTO.getPrimaryPassenger())) {
				Passenger passenger = new Passenger();
				passenger.setPassengerId(passengerDTO.getPassengerId());
				passenger.setFamilyName(passengerDTO.getFamilyName());
				passenger.setGivenName(passengerDTO.getGivenName());
				return passenger;
			}
		}
		return null;
	}

	/**
	 * @param booking
	 * @throws ExpectedException
	 */
	private void checkBooking(BookingDTO booking) throws ExpectedException {
		if(booking == null) {
			return;
		}
		if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(booking.getBookingType())) {
			checkHotelBooking(booking);
		} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(booking.getBookingType())) {
			checkPackageBooking(booking);
		}
	}

	/**
	 * @param booking
	 * @throws ExpectedException
	 */
	private void checkPackageBooking(BookingDTO booking) throws ExpectedException {
		List<BookingOrderDTO> sectors = new ArrayList<>();
		if(booking.getFlightBooking() != null
				&& !CollectionUtils.isEmpty(booking.getFlightBooking().getSegments())) {
			sectors.addAll(booking.getFlightBooking().getSegments());
		}
		if(booking.getHotelBooking() != null
				&& !CollectionUtils.isEmpty(booking.getHotelBooking().getDetails())) {
			sectors.addAll(booking.getHotelBooking().getDetails());
		}
		if(booking.getEventBooking() != null
				&& !CollectionUtils.isEmpty(booking.getEventBooking().getDetails())) {
			sectors.addAll(booking.getEventBooking().getDetails());
		}
		List<BookingOrderDTO> bookingOrderDTOs = sortBookingOrders(sectors);
		if(checkoutThreeDaysAgo(bookingOrderDTOs.get(bookingOrderDTOs.size()-1).getOrderDate(), DateUtil.getGMTTime())) {
			throw new ExpectedException(String.format("All sectors end date before 3 days ago. oneARloc:[%s] ojRloc:[%s]", booking.getOneARloc(), booking.getOjRloc()), new ErrorInfo(ErrorCodeEnum.ERR_ALL_FLIGHT_FLOWN_BEFOR_LIMIT_TIME));
		}
	}

	/**
	 * @param booking
	 * @throws ExpectedException
	 */
	private void checkHotelBooking(BookingDTO booking) throws ExpectedException {
		if(booking.getHotelBooking() != null && BooleanUtils.isTrue(booking.getHotelBooking().isAllInVaild())) {
			throw new ExpectedException(String.format("All Hotel checkout date before 4 days ago. ojRloc:[%s]", booking.getOjRloc()), new ErrorInfo(ErrorCodeEnum.ERR_ALL_HOTEL_EXPIRED_BEFOR_LIMIT_TIME));
		}
	}

	@Override
	public ConsentInfoRecordResponseDTO consentInfoRecord(LoginInfo loginInfo, String rloc, String acceptLanguage) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		logger.debug(String.format("save Consent Info to db with Rloc [%s]", loginInfo.getLoginRloc()));
		return consentInfoService.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage);
	}

	@Override
	public ConsentCommonRecordResponseDTO saveConsentCommon(ConsentAddRequestDTO requestDTO, LoginInfo loginInfo, String rloc, String acceptLanguage) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		logger.debug(String.format("save Consent Info to db with Rloc [%s]", loginInfo.getLoginRloc()));
		return consentInfoService.saveConsentCommon(requestDTO, retrievePnrBooking, loginInfo, rloc, acceptLanguage);
	}
	/**
	 * retrieveBooking DTO By Reference, spnr or 1A rloc
	 *
	 * @param rloc
	 * @param familyName
	 * @param givenName
	 * @param unAuthMbToken
	 * @return
	 * @throws BusinessBaseException
	 * @throws ParseException
	 */
	private BookingDTO retrieveBookingDTOByROneARlocOrSpnr(LoginInfo loginInfo,String rloc, BookingBuildRequired required) throws BusinessBaseException {
		BookingDTO bookingDTO = null;

		if (BizRulesUtil.isSPNR(rloc)) {
			OJBooking ojBooking = retrieveOJBookingDTO(loginInfo, rloc, null);

			if(ojBooking == null) {
				throw new ExpectedException(String.format("OJ booking not found by spnr:[%s]  firstNmae:[%s] lastName:[%s]",
						loginInfo.getLoginRloc(), loginInfo.getLoginGivenName(), loginInfo.getLoginFamilyName()),
						new ErrorInfo(ErrorCodeEnum.ERR_SPNR_BOOKING_NOTFOUND), HttpStatus.OK);
			}
			bookingDTO = crossRetrieveBookingDTOFromOneaAndOj(loginInfo, required, null, true, ojBooking, false);
		} else {
			FlightBookingDTO flightBookingDTO = dtoConverter.convertToBookingDTO(retrievePnrByRloc(rloc, loginInfo, required), loginInfo);
			setSegmentIdAndOrderDate(flightBookingDTO);
			bookingDTO = crossRetrieveBookingDTOFromOneaAndOj(loginInfo, required, flightBookingDTO, false, null, true);
		}
		checkBooking(bookingDTO);
		return bookingDTO;
	}


	/**
	 * retrieveBooking DTO By Reference
	 *
	 * @param PNR
	 * @return
	 * @throws BusinessBaseException
	 * @throws ParseException
	 */
	private BookingDTO retrieveBookingDTOByPNR(PNRReply pnr,LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException{
		BookingDTO bookingDTO = null;

		FlightBookingDTO flightBookingDTO = dtoConverter.convertToBookingDTO(convertPnrReplyToBooking(pnr,loginInfo.getLoginRloc(), loginInfo, required), loginInfo);
		if (flightBookingDTO == null) {
			throw new ExpectedException("Cannot find booking by pnr",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		bookingDTO = crossRetrieveBookingDTOFromOneaAndOj(loginInfo, required, flightBookingDTO, false, null, false);

		return bookingDTO;
	}

	/**
	 * Check 1A booking by OJ spnr or check OJ booking by 1A rloc
	 * @param loginInfo
	 * @param maskValue
	 * @param required
	 * @param flightBookingDTO
	 * @param deepCheckOnea whether check 1A if flightBookingDTO is null but the the booking is package booking
	 * @param ojBooking
	 * @param deepCheckOj whether check open jaw if ojBooking is null but the the booking is package booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private BookingDTO crossRetrieveBookingDTOFromOneaAndOj(LoginInfo loginInfo, BookingBuildRequired required,
			FlightBookingDTO flightBookingDTO, boolean deepCheckOnea, OJBooking ojBooking, boolean deepCheckOj)
			throws BusinessBaseException {

		if(flightBookingDTO == null && ojBooking == null){
			return null;
		}


		boolean dataLeveloJCheckRequired =  false;
		//1) the booking always base on flight booking if exist
		if(flightBookingDTO != null ){
			dataLeveloJCheckRequired = BooleanUtils.isTrue(flightBookingDTO.isPackageBooking());
		}

		boolean dataLevelOneaCheckRequired =  false;
		//2) check open jaw booking
		if (ojBooking != null) {
			dataLevelOneaCheckRequired = MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(ojBooking.getBookingType())
					&& ojBooking.getFlightBooking() != null
					&& StringUtils.isNotEmpty(ojBooking.getFlightBooking().getBookingReference());
		}

		boolean retrievedOneAError = false;
		boolean retrievedOjError = false;

		//a1) call 1A to retrieve booking again if needed
		if(flightBookingDTO == null && dataLevelOneaCheckRequired && deepCheckOnea){
			try {
				flightBookingDTO =  dtoConverter.convertToBookingDTO(retrievePnrByRloc(ojBooking.getFlightBooking().getBookingReference(), loginInfo, required), loginInfo);
				setSegmentIdAndOrderDate(flightBookingDTO);
			} catch (Exception e) {
				logger.error(String.format("Error to retrieve OneA booking from Pnr_retive by rloc:[%s], familyName:[%s], givenName:[%s]",
						ojBooking.getFlightBooking().getBookingReference(), loginInfo.getLoginFamilyName(), loginInfo.getLoginGivenName()), e);
				retrievedOneAError = true ;
			}

		}

		//a2) call OJ to retrieve booking again if needed
		if(ojBooking == null && dataLeveloJCheckRequired && deepCheckOj){
			try {
				ojBooking = retrieveOJBookingDTO(loginInfo, flightBookingDTO.getSpnr(), flightBookingDTO);
			} catch (Exception e) {
				logger.error(String.format("Error to retrieve PackageBooking from OJService by rloc:[%s], familyName:[%s], givenName:[%s]",
						flightBookingDTO.getSpnr(), loginInfo.getLoginFamilyName(), loginInfo.getLoginGivenName()), e);
				retrievedOjError = true;
			}
		}

		// merge 1A and OJ booking to bookingDTO
		BookingDTO bookingDTO = mergeFlightBookingDTOAndOjBookingDTO(flightBookingDTO, ojBooking);
		if(bookingDTO != null){
			bookingDTO.setRetrieveOneA(!retrievedOneAError);
			bookingDTO.setRetrieveOJ(!retrievedOjError);
		}
		return bookingDTO;

	}

	/**
	 * merge ojbooking and flight booking dto to booking dto
	 * @param flightBookingDTO
	 * @param ojBooking
	 * @return
	 */
	private BookingDTO mergeFlightBookingDTOAndOjBookingDTO(FlightBookingDTO flightBookingDTO, OJBooking ojBooking) {

		if (flightBookingDTO == null && ojBooking == null) {
			return null;
		}

		BookingDTO bookingDTO = new BookingDTO();
		if (flightBookingDTO != null) {
			bookingDTO.setFlightBooking(flightBookingDTO);
			bookingDTO.setBookingReference(flightBookingDTO.getRloc());
			bookingDTO.setIbeBooking(flightBookingDTO.isIbeBooking());
			bookingDTO.setTrpBooking(flightBookingDTO.isTrpBooking());
			bookingDTO.setAppBooking(flightBookingDTO.isAppBooking());
			bookingDTO.setGdsBooking(flightBookingDTO.isGdsBooking());
			bookingDTO.setGccBooking(flightBookingDTO.isGccBooking());
			bookingDTO.setGdsGroupBooking(flightBookingDTO.isGdsGroupBooking());
			bookingDTO.setRedBooking(flightBookingDTO.isRedBooking());
			bookingDTO.setStaffBooking(flightBookingDTO.getStaffBooking());
			if (BooleanUtils.isTrue(flightBookingDTO.isFlightOnly())) {
				bookingDTO.setBookingType(MMBBizruleConstants.BOOKING_TYPE_FLIGHT);
				// always trust 1A response, so no need check OJ value if cannot find spnr in pnr even can find it in OJ
				return sortBookingDTO(bookingDTO);
			} else {
				bookingDTO.setBookingType(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
			}
		}

		if (ojBooking != null) {
			if (StringUtils.isEmpty(bookingDTO.getBookingType())) {
				bookingDTO.setBookingType(ojBooking.getBookingType());
			}
			bookingDTO.setBookingReference(ojBooking.getBookingReference());
			bookingDTO.setBookingDate(ojBooking.getBookingDate());
			bookingDTO.setHotelBooking(dtoConverter.covert2HotelBookingDTO(ojBooking));
			bookingDTO.setEventBooking(dtoConverter.covert2EventBookingDTO(ojBooking.getEventBooking()));
			bookingDTO.setDocuments(dtoConverter.covert2DocumentDTOs(ojBooking.getDocuments()));
			bookingDTO.setContactDetails(dtoConverter.covert2ContactDTOs(ojBooking.getContactDetails()));
			bookingDTO.setNameInput(ojBooking.getNameInput());
		}
		return sortBookingDTO(bookingDTO);

	}
	/**
	 * get OJ booking from open jaw
	 * @param loginInfo
	 * @param spnr
	 * @param flightBookingDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	private OJBooking retrieveOJBookingDTO(LoginInfo loginInfo, String spnr, FlightBookingDTO flightBookingDTO) throws BusinessBaseException{
		String familyName = null;
		String givenName = null;
		// always use flight booking primary passenger's name if can find
		if (flightBookingDTO != null && flightBookingDTO.getPrimaryPassenger() != null) {
			givenName = flightBookingDTO.getPrimaryPassenger().getGivenName();
			familyName = flightBookingDTO.getPrimaryPassenger().getFamilyName();

		//member login use name from profile
		}else if(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			TempLinkedBooking tempLinkedBooking = tempLinkedBookingRepository
					.getLinkedBookings(loginInfo.getMmbToken())
					.stream()
					.filter(linkedBooking -> Objects.equal(spnr, linkedBooking.getRloc()))
					.findFirst()
					.orElse(null);

			if (tempLinkedBooking != null) {
				familyName = tempLinkedBooking.getFamilyName();
				givenName = tempLinkedBooking.getGivenName();
			} else {
				ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
				familyName = profilePersonInfo.getFamilyName();
				givenName = profilePersonInfo.getGivenName();
			}
		//non member case
		}else{
			 givenName = loginInfo.getLoginGivenName();
			 familyName = loginInfo.getLoginFamilyName();
		}

		OJBooking ojBooking = ojBookingService.getBooking(givenName, familyName, spnr);

		if(ojBooking == null) {
			return null;
		}

		if(OjConstants.CANCELLED.equals(ojBooking.getBookingStatus())) {
			throw new ExpectedException(String.format("Booking found by rloc:%s from OJService cancelled", spnr),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}

		return ojBooking;
	}

	/**
	 * @param loginInfo
	 * @param maskValue
	 * @param flightBookingDTO
	 * @throws UnexpectedException
	 */
	private void maskForFlightBookingDTO(FlightBookingDTO flightBookingDTO){

		//mask user info
		maskHelper.mask(flightBookingDTO);
	}

	/**
	 * @param loginInfo
	 * @param maskValue
	 * @param flightBookingDTO
	 * @throws UnexpectedException
	 */
	private void maskForFlightBookingDTO(BookingDTO bookingDTO){

		//mask user info
		if(bookingDTO !=null && bookingDTO.getFlightBooking()!=null){
			this.maskForFlightBookingDTO(bookingDTO.getFlightBooking());
		}
	}

	/**
	 * @Description retrieve the booking from 1A by rloc
	 * @param rloc
	 * @param loginInfo
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	private Booking retrievePnrByRloc(String rloc, LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		return bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
	}


	/**
	 * @Description retrieve the booking from 1A by rloc
	 * @param rloc
	 * @param loginInfo
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	private Booking convertPnrReplyToBooking(PNRReply pnr,String rloc, LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.PNRReplyToBooking(pnr, rloc);
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		return bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
	}

	/**
	 * @param flightBookingDTO
	 */
	private void setSegmentIdAndOrderDate(FlightBookingDTO flightBookingDTO) {
		List<SegmentDTO> segments = flightBookingDTO.getSegments();
		for(SegmentDTO segment : segments) {
			segment.setId(MMBBizruleConstants.BOOKING_TYPE_FLIGHT + segment.getSegmentId());
			try {
				segment.setOrderDate(DateUtil.getStrToDate(DepartureArrivalTimeDTO.TIME_FORMAT, segment.getDepartureTime().getTime()));
			} catch (ParseException e) {
				logger.error("Paser segment departureTime failure for orderDate", e);
			}
		}
	}


	/**
	 * add order for booking(Package, hotel, event) sorting.
	 * @param bookingDTO
	 * @return
	 */
	private BookingDTO sortBookingDTO(BookingDTO bookingDTO) {
		if(bookingDTO == null) {
			return null;
		}
		List<BookingOrderDTO> orders = new ArrayList<>();
		if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingDTO.getBookingType())) {
			HotelBookingDTO hotelBookingDTO = bookingDTO.getHotelBooking();
			List<HotelDTO> details = hotelBookingDTO.getDetails();
			orders.addAll(details);
		} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingDTO.getBookingType())) {
			if(bookingDTO.getFlightBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getFlightBooking().getSegments())) {
				List<SegmentDTO> segments = bookingDTO.getFlightBooking().getSegments();
				orders.addAll(segments);
			}
			if(bookingDTO.getHotelBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getHotelBooking().getDetails())) {
				orders.addAll(bookingDTO.getHotelBooking().getDetails());
			}
			if(bookingDTO.getEventBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getEventBooking().getDetails())) {
				orders.addAll(bookingDTO.getEventBooking().getDetails());
			}
		} else if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingDTO.getBookingType())) {
			return bookingDTO;
		}

		return setBookingOrder(bookingDTO, sortBookingOrders(orders));
	}

	/**
	 * sort list of PackageBookingOrderDTO according to orderDate
	 * @param orders
	 * @return
	 */
	private List<BookingOrderDTO> sortBookingOrders(List<BookingOrderDTO> orders) {
		orders.sort((o1, o2) -> {
			if(o1.getOrderDate() == null && o2.getOrderDate() == null) {
				return 0;
			} else if(o1.getOrderDate() == null) {
				return -1;
			} else if (o2.getOrderDate() == null) {
				return 1;
			} else {
				return o1.getOrderDate().compareTo(o2.getOrderDate());
			}
		});
		return orders;
	}

	/**
	 * @param bookingDTO
	 * @param orders
	 */
	private BookingDTO setBookingOrder(BookingDTO bookingDTO, List<BookingOrderDTO> orders) {
		Map<String, Integer> map = new HashMap<>();
		for(int i = 0; i < orders.size(); i++) {
			BookingOrderDTO order = orders.get(i);
			map.put(order.getId(), i);
		}

		if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingDTO.getBookingType())) {
			HotelBookingDTO hotelBookingDTO = bookingDTO.getHotelBooking();
			List<HotelDTO> details = hotelBookingDTO.getDetails();
			for(HotelDTO detail : details) {
				detail.setOrder(map.get(detail.getId()));
			}
		} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingDTO.getBookingType())) {
			if(bookingDTO.getFlightBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getFlightBooking().getSegments())) {
				for(SegmentDTO segment : bookingDTO.getFlightBooking().getSegments()) {
					segment.setOrder(map.get(segment.getId()));
				}
			}
			if(bookingDTO.getHotelBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getHotelBooking().getDetails())) {
				for(HotelDTO detail : bookingDTO.getHotelBooking().getDetails()) {
					detail.setOrder(map.get(detail.getId()));
				}
			}
			if(bookingDTO.getEventBooking() != null && !CollectionUtils.isEmpty(bookingDTO.getEventBooking().getDetails())) {
				for(EventDTO detail : bookingDTO.getEventBooking().getDetails()) {
					detail.setOrder(map.get(detail.getId()));
				}
			}
		}
		return bookingDTO;
	}


	/**
	 * @Description check if the booking is checked out 3 days ago
	 * @param checkoutDate
	 * @param now
	 * @return
	 */
	private boolean checkoutThreeDaysAgo(Date checkoutDate, Date now) {
		long day = (now.getTime() - checkoutDate.getTime())/(24*60*60*1000);
		return day > 3;
	}

	/**
	 * save rlocs for flight booking
	 * @param booking
	 * @param mmbToken
	 */
	private void saveValidFlightRlocs(BookingDTO booking, String mmbToken) {
		if(booking != null && MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(booking.getBookingType())
				&& booking.getFlightBooking() != null) {
			FlightBookingDTO flightBooking = booking.getFlightBooking();
			List<String> vaildRlocs = new ArrayList<>();
			vaildRlocs.add(flightBooking.getOneARloc());
			if (!StringUtils.isEmpty(flightBooking.getSpnr())) {
				vaildRlocs.add(flightBooking.getSpnr());
			}
			mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, vaildRlocs);
		}
	}

}
