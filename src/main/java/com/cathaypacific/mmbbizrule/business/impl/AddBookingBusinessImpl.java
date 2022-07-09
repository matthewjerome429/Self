package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.AddBookingBusiness;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.BookingAddTypeEnum;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.addbooking.AddBookingResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.service.OneAAddBookingService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class AddBookingBusinessImpl implements AddBookingBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(AddBookingBusinessImpl.class);
	
	@Autowired
	private BizRuleConfig bizRuleConfig;

	@Autowired
	private OJBookingService ojBookingService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private OneAAddBookingService oneAAddBookingService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;
	
	@Override
	public AddBookingResponseDTO addBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException {
		if (requestDTO == null || (StringUtils.isEmpty(requestDTO.getEticket()) && StringUtils.isEmpty(requestDTO.getRloc()))) {
			throw new ExpectedException("Unable to add booking - input e-ticket/rloc are both empty",
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_ETICKET_RLOC_BOTH_NULL));
		}
		
		if(StringUtils.isNotEmpty(requestDTO.getRloc())) {
			return addBookingByRloc(loginInfo, requestDTO);
		} else {
			return addBookingByET(loginInfo, requestDTO);
		}
	}
	
	/**
	 * add booking by e-ticket
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	private AddBookingResponseDTO addBookingByET(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException {
		String rloc = ticketProcessInvokeService.getRlocByEticket(requestDTO.getEticket());
		if (StringUtils.isEmpty(rloc)) {
			throw new ExpectedException(String.format("Unable to add booking - Cannot find rloc by eticket:%s", requestDTO.getEticket()),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
		}
		
		requestDTO.setRloc(rloc);
		return addBookingByRloc(loginInfo, requestDTO);
	}

	/**
	 * add booking by RLOC
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	private AddBookingResponseDTO addBookingByRloc(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		String oneARloc = getOneARloc(rloc, loginInfo);
		
		// check whether booking is already showing or not
		checkBookingShowing(loginInfo, rloc, oneARloc);

		// get booking by RLOC
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
		if (retrievePnrBooking == null) {
			throw new ExpectedException(String.format("Unable to add booking - Cannot find booking by rloc:%s", oneARloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
		}
		
		// check whether name matches or not
		checknameMatch(loginInfo, requestDTO, retrievePnrBooking);
		
		// check whether the booking is a staff AD ticket/MICE Type C/group booking or not
		checkStaffMICEGroupBooking(retrievePnrBooking, rloc);
 
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, initializeAddBookingRequired());
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(booking.getSegments())) {
			throw new ExpectedException(String.format("Unable to add booking - booking:%s is invalid[booking is null/ have no pax or segments]", oneARloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_UNAVAILABLE_BOOKING));
		}

		// check whether the booking is available to add
		checkAddBookingAvailability(booking, oneARloc, loginInfo, retrievePnrBooking);
		
		String primaryPassengerId = booking.getPassengers().stream()
				.filter(pax -> pax != null && BooleanUtils.isTrue(pax.isPrimaryPassenger()))
				.map(Passenger::getPassengerId).findFirst().orElse(null);
		if(primaryPassengerId == null) {
			throw new ExpectedException(String.format("Unable to add booking - Name not matched in booking:%s for member:%s", requestDTO.getRloc(), loginInfo.getMemberId()),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NAME_MISMATCHED));
		}
		
		
		// if membershipNumber is not empty, then MPO exist in the booking for the login member
		String membershipNumber = getMPObyPaxId(booking, requestDTO, loginInfo, primaryPassengerId);
		if(StringUtils.isNotEmpty(membershipNumber)) {
			AddBookingResponseDTO response = new AddBookingResponseDTO();
			response.setSuccess(false);
			response.setMpoExist(true);
			response.setMpoMemberId(membershipNumber);
			return response;
		}
		
		// segmentIds of primary passenger
		List<String> segmentIds = booking.getPassengerSegments().stream()
				.filter(ps -> ps != null && !StringUtils.isEmpty(ps.getSegmentId()) && primaryPassengerId.equals(ps.getPassengerId())) 
				.map(PassengerSegment::getSegmentId).distinct().collect(Collectors.toList());
		
		if(MMBUtil.isRuLogin(loginInfo) || !checkMatchedMemberName(loginInfo, requestDTO, retrievePnrBooking, primaryPassengerId)) {		
			return addBookingThroughCUST(primaryPassengerId, booking, retrievePnrBooking, loginInfo, rloc, segmentIds);
		} else {
			return addBookingForAMMPOMember(primaryPassengerId, booking, retrievePnrBooking, loginInfo, rloc, segmentIds);
		}
		
	}
	
	/**
	 * check the request name matched to profile, use paxNameIdentificationService checking to make sure this handling align to name identification always. 
	 * @param loginInfo
	 * @param requestDTO
	 * @param retrievePnrBooking
	 * @param primaryPassengerId
	 * @return
	 */
	private boolean checkMatchedMemberName(LoginInfo loginInfo, AddBookingRequestDTO requestDTO, RetrievePnrBooking retrievePnrBooking, String primaryPassengerId) {
		if (StringUtils.isEmpty(requestDTO.getFamilyName()) || StringUtils.isEmpty(requestDTO.getGivenName())) {
			return true;// it should matched profile name if the name in request is empty
		} else {
			ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
			// 1)Clean primary passenger
			retrievePnrBooking.getPassengers().stream().forEach(pax -> pax.setPrimaryPassenger(false));
			// 2)Check member profile name match
			try {
				paxNameIdentificationService.primaryPaxIdentificationForRloc(profilePersonInfo.getFamilyName(), profilePersonInfo.getGivenName(),
						retrievePnrBooking);
			} catch (Exception e) {
				// ignore any exception if check
				logger.info("Add booking member profile name match failed, will try to add by cust", e.getMessage());
			}
			// profile matched primary passenger id
			String profilePrimaryPaxId = retrievePnrBooking
					.getPassengers()
					.stream()
					.filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger()))
					.map(RetrievePnrPassenger::getPassengerID)
					.findFirst()
					.orElse(null);
			// 3) reset primary passenger
			retrievePnrBooking.getPassengers().stream().forEach(pax -> {
				if (Objects.equals(primaryPassengerId, pax.getPassengerID())) {
					pax.setPrimaryPassenger(true);
				} else {
					pax.setPrimaryPassenger(false);
				}
			});
			return profilePrimaryPaxId != null && Objects.equals(profilePrimaryPaxId, primaryPassengerId);
		}

	}
	/**
	 * add booking for AM/MPO member
	 * If FQTV don't exist, add into EODS by CUST
	 * If FQTV exist:
	 * 		1. contains CX, throw exception
	 * 		2. don't contain CX, add into 1A by FQTV, 
	 * 		   if failure, then try to add by CUST.
	 * 
	 * @param primaryPassegnerId
	 * @param booking
	 * @param pnrBooking
	 * @param loginInfo
	 * @param rloc
	 * @param segmentIds
	 * @return
	 * @throws BusinessBaseException
	 */
	private AddBookingResponseDTO addBookingForAMMPOMember(String primaryPassengerId, Booking booking,
			RetrievePnrBooking pnrBooking, LoginInfo loginInfo, String rloc, List<String> segmentIds) throws BusinessBaseException {
		List<FQTVInfo> fqtvs = getFQTVsByPaxId(primaryPassengerId, booking);
		if(CollectionUtils.isEmpty(fqtvs)) {
			try {
				return addBookingThroughFQTV(primaryPassengerId, pnrBooking, loginInfo, rloc, segmentIds);
			} catch(Exception e) {
				logger.info(String.format("Fail to add booking:%s by adding FQTV, will try to add by adding SK", rloc));
				return addBookingThroughCUST(primaryPassengerId, booking, pnrBooking, loginInfo, rloc, segmentIds);
			}
		} else {
			boolean cxFQTVExist = fqtvs.stream().anyMatch(fqtv -> BooleanUtils.isTrue(fqtv.getAm()) || BooleanUtils.isTrue(fqtv.getMpo()));
			if(cxFQTVExist) {
				throw new ExpectedException(String.format("Unable to add booking - AM/MPO FQTV already exist in booking:%s", rloc),
						new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_UNAVAILABLE_BOOKING));
			} else {
				return addBookingThroughCUST(primaryPassengerId, booking, pnrBooking, loginInfo, rloc, segmentIds);
			}
		}
	}
	
	/**
	 * add booking into 1A by FQTV
	 * 
	 * @param primaryPassegnerId
	 * @param pnrBooking
	 * @param loginInfo
	 * @param rloc
	 * @param segmentIds
	 * @return
	 * @throws BusinessBaseException
	 */
	private AddBookingResponseDTO addBookingThroughFQTV(String primaryPassengerId, RetrievePnrBooking pnrBooking,
			LoginInfo loginInfo, String rloc, List<String> segmentIds) throws BusinessBaseException {
		oneAAddBookingService.addBookingByFQTV(rloc, primaryPassengerId, segmentIds, loginInfo.getMemberId(), pnrBooking);

		AddBookingResponseDTO response = new AddBookingResponseDTO();
		response.setSuccess(true);
		response.setRloc(rloc);
		response.setAddType(BookingAddTypeEnum.ONEA);
		response.setCompanionBooking(false); // add booking through FQTV should be self booking
		logger.info(String.format("Add Booking | Type | FQTV | Rloc | %s", rloc), true);
		return response;
	}

	/**
	 * add booking into EODS by CUST
	 * @param primaryPassengerId 
	 * 
	 * @param booking
	 * @param pnrBooking
	 * @param loginInfo
	 * @param rloc
	 * @param segmentIds
	 * @return
	 * @throws BusinessBaseException
	 */
	private AddBookingResponseDTO addBookingThroughCUST(String primaryPassengerId, Booking booking, RetrievePnrBooking pnrBooking,
			LoginInfo loginInfo, String rloc, List<String> segmentIds) throws BusinessBaseException {
		// operating company id(CX/KA) list of primary passenger
		List<String> operatingCompanys = booking.getSegments().stream()
				.filter(seg -> seg != null && segmentIds.contains(seg.getSegmentID())
				&& (OneAConstants.COMPANY_CX.equals(seg.getOperateCompany()) || OneAConstants.COMPANY_KA.equals(seg.getOperateCompany())))
				.map(Segment::getOperateCompany).distinct().collect(Collectors.toList());
		
		try {
			checkCUSTExistByMemberId(loginInfo.getMemberId(), operatingCompanys, pnrBooking.getSkList(), pnrBooking.getOneARloc());
		} catch (Exception e) {
			// Add to tempLinked booking even the cust existd, because 1A sync to eods has issue most time in test env.
			addBookingToTempList(booking, primaryPassengerId, loginInfo);
			throw e;
		}
		
		oneAAddBookingService.addBookingBySK(booking.getOneARloc(), loginInfo.getMemberId(), operatingCompanys);
		
		// to add booking to redis for display in session
		addBookingToTempList(booking, primaryPassengerId, loginInfo);
		
		//check the login user name for identify companion booking 
		Boolean companionBooking = checkCompanionBooking(loginInfo, pnrBooking);
		
		AddBookingResponseDTO response = new AddBookingResponseDTO();
		response.setSuccess(true);
		response.setRloc(booking.getOneARloc());
		response.setAddType(BookingAddTypeEnum.EODS);
		response.setCompanionBooking(companionBooking);
		logger.info(String.format("Add Booking | Type | SK Cust | Rquest Rloc[%s] | oneARloc[%s]", rloc, booking.getOneARloc()), true);
		return response;
	}

	/**
	 * add booking into TempList for display 
	 * caused by can't get the booking added by CUST from EODS by memberId immediately.
	 * 
	 * @param booking
	 * @param primaryPassengerId
	 * @param loginInfo
	 */
	private void addBookingToTempList(Booking booking, String primaryPassengerId, LoginInfo loginInfo) {
		Passenger primaryPax = booking.getPassengers().stream()
				.filter(pax -> pax != null &&  BooleanUtils.isTrue(pax.isPrimaryPassenger()) && primaryPassengerId.equals(pax.getPassengerId())).findFirst().orElse(null);
		
		if(primaryPax == null) {
			primaryPax = booking.getPassengers().stream()
					.filter(pax -> pax != null && pax.getParentId() == null).findFirst().orElse(new Passenger());
		}
		
		tempLinkedBookingRepository.addEodsBooking(booking.getOneARloc(), primaryPax.getFamilyName(), primaryPax.getGivenName(), primaryPassengerId, loginInfo.getMmbToken());
	}

	/**
	 * check CUST element already exist or not in 1A by memberId
	 * 
	 * @param memberId
	 * @param operatingCompanys
	 * @param skList
	 * @param oneARloc
	 * @throws BusinessBaseException
	 */
	private void checkCUSTExistByMemberId(String memberId, List<String> operatingCompanys,
			List<RetrievePnrDataElements> skList, String oneARloc) throws BusinessBaseException {
		if(CollectionUtils.isEmpty(skList)) {
			return;
		}
		
		for(RetrievePnrDataElements element : skList) {
			if(element != null && OneAConstants.SK_SEGMENT.equals(element.getSegmentName()) 
					&& OneAConstants.SK_TYPE_CUST.equals(element.getType()) 
					&& !StringUtils.isEmpty(element.getFreeText())) {
				String[] arr = element.getFreeText().split(OneAConstants.DELIMITER_IN_CUST_FREETEXT);
				String memberIdInCUST = StringUtils.isNotEmpty(arr[0]) ? arr[0] : null;
				
				if(memberId.equals(memberIdInCUST) && operatingCompanys.contains(element.getCompanyId())) {
					throw new ExpectedException(String.format("Unable to add booking - SK CUST already exist in booking:%s ", oneARloc),
							new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_BOOKING_ALREADY_SHOWING));
				}
			}
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
		return booking.getPassengerSegments().stream().filter(ps -> ps != null && passengerId.equals(ps.getPassengerId()) 
				&& ps.getFqtvInfo() != null && !StringUtils.isEmpty(ps.getFqtvInfo().getMembershipNumber()))
				.map(PassengerSegment::getFqtvInfo).collect(Collectors.toList());
	}

	/**
	 * check MPO exist in the booking for the current member only
	 * 
	 * @param booking
	 * @param requestDTO
	 * @param loginInfo
	 * @param loginPassengerId
	 * @return String : the AM/MPO membershipNumber
	 */
	private String getMPObyPaxId(Booking booking, AddBookingRequestDTO requestDTO, LoginInfo loginInfo, String loginPassengerId) {
		/**
		 * skip AM/MPO check 3 cases:
		 * 1. flag "ignoreMPOCheck" from request is true;
		 * 2. input familyName and givenName are both not empty;
		 * 3. the member is RU member
		 */
		if(BooleanUtils.isTrue(requestDTO.getIgnoreMPOCheck()) 
				|| (StringUtils.isNotEmpty(requestDTO.getFamilyName()) && StringUtils.isNotEmpty(requestDTO.getGivenName()))
				|| MMBUtil.isRuLogin(loginInfo)) {
			return null;
		}

		return booking.getPassengerSegments().stream()
				.filter(ps -> ps != null && loginPassengerId.equals(ps.getPassengerId()) && ps.getFqtvInfo() != null)
				.map(PassengerSegment::getFqtvInfo)
				.filter(fqtv -> bizRuleConfig.getAmTierLevel().contains(fqtv.getTierLevel())
						|| bizRuleConfig.getCxkaTierLevel().contains(fqtv.getTierLevel()))
				.map(FQTVInfo::getMembershipNumber).findFirst().orElse(null);
	}

	/**
	 * get oneARloc(flight RLOC) by input RLOC
	 * 
	 * @param rloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	private String getOneARloc(String rloc, LoginInfo loginInfo) throws BusinessBaseException {
		String oneARloc = null;
		if (rloc.length() == 7) {
			oneARloc = getOneARlocFromOJ(rloc, loginInfo);
			if (StringUtils.isEmpty(oneARloc)) {
				throw new ExpectedException(String.format("Unable to add booking - No flight Rloc found by rloc from ojService:%s", rloc),
						new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_HOTEL_BOOKING_NOT_SUPPORT));
			}
		} else {
			oneARloc = rloc;
		}
		
		return oneARloc;
	}

	/**
	 * check name match, 
	 * the mean while, paxNameIdentificationService will set primary passenger
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @param booking
	 * @throws BusinessBaseException
	 */
	private void checknameMatch(LoginInfo loginInfo, AddBookingRequestDTO requestDTO, RetrievePnrBooking booking) throws BusinessBaseException {
		String familyName = requestDTO.getFamilyName();
		String givenName = requestDTO.getGivenName();
		if(StringUtils.isEmpty(familyName) && StringUtils.isEmpty(givenName)) {
			ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
			familyName = profilePersonInfo.getFamilyName();
			givenName = profilePersonInfo.getGivenName();
		}
		
		try {
			if(StringUtils.isNotEmpty(requestDTO.getEticket())) {
				paxNameIdentificationService.primaryPaxIdentificationForETicket(familyName, givenName, requestDTO.getEticket(), booking);
			} else {
				paxNameIdentificationService.primaryPaxIdentificationForRloc(familyName, givenName, booking);
			}			
		} catch(ExpectedException e) {
			if(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH.getCode().equals(e.getErrorInfo().getErrorCode())) {
			    if(BookingBuildUtil.isMiceBooking(booking.getSkList())) {
			        throw new ExpectedException(String.format("Cannot match pax name, request data[familyName:%s givenName:%s rloc:%s] for mice booking", familyName, givenName, booking.getOneARloc()),
	                        new ErrorInfo(ErrorCodeEnum.ERR_ADDBOOKING_MICE_NAME_MISMATCH));
			    }else {
			        throw new ExpectedException(String.format("Cannot match pax name, request data[familyName:%s givenName:%s rloc:%s]", familyName, givenName, booking.getOneARloc()),
	                        new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NAME_MISMATCHED)); 
			    }
				
			} else if(ErrorCodeEnum.ERR_INFANT_TICKET_LOGIN.getCode().equals(e.getErrorInfo().getErrorCode())) {
				throw new ExpectedException(String.format("input eticket:[%s] is infant eticket", requestDTO.getEticket()),
						new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_INVAILD_INFANT_ETICKET));
			} else {
				throw e;
			}
		}
	}

	/**
	 * initialize BookingBuildRequired to build booking for add-booking function.
	 * 
	 * @return BookingBuildRequired
	 */
	private BookingBuildRequired initializeAddBookingRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setCprCheck(false);
		required.setCountryOfResidence(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setMemberAward(false);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setRtfs(true);
		required.setSeatSelection(false);
		required.setTravelDocument(false);
		return required;
	}

	/**
	 * check flight's status contains UC or HX
	 * 
	 * @param booking
	 * @return boolean
	 */
	private boolean statusContainsUcHx(RetrievePnrBooking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return false;
		}
		return booking.getSegments().stream().anyMatch(seg -> !CollectionUtils.isEmpty(seg.getStatus()) && 
				(seg.getStatus().contains(OneAConstants.UC_STATUS) || seg.getStatus().contains(OneAConstants.HX_STATUS)));
	}

	/**
	 * get PNR RLOC from OJ
	 * 
	 * @param ojRloc
	 * @param loginInfo
	 * @return String
	 * @throws BusinessBaseException
	 */
	private String getOneARlocFromOJ(String ojRloc, LoginInfo loginInfo) throws BusinessBaseException {
		LinkedHashMap<String, String> nameMap = getNameMapByMemberId(loginInfo);
		if(CollectionUtils.isEmpty(nameMap)) {
			throw new ExpectedException(String.format("Unable to add booking - No passenger name found in profile of member:%s", loginInfo.getMemberId()),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
		}
		
		OJBooking ojBooking = null;
		for(Entry<String, String> entry : nameMap.entrySet()) {
			ojBooking = ojBookingService.getBooking(entry.getKey(), entry.getValue(), ojRloc);
			if(ojBooking != null) {
				break;
			}
		}

		if (ojBooking == null) {
			throw new ExpectedException(String.format("Unable to add booking - Cannot find OJBooking by rloc from ojSErvice:%s", ojRloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
		}

		if(ojBooking.getFlightBooking() == null){
			return null;
		}
		return ojBooking.getFlightBooking().getBookingReference();
	}

	/**
	 * get nameMap by memberId from ProfilePersonInfo/ProfilePreference;
	 * nameMap format: key: givenName, value: familyName;
	 * used to call OJService to get oneARloc.
	 * 
	 * @param loginInfo
	 * @return LinkedHashMap<String, String>
	 */
	private LinkedHashMap<String, String> getNameMapByMemberId(LoginInfo loginInfo) {
		LinkedHashMap<String, String> nameMap = new LinkedHashMap<>();
		
		// set member profilePerson names
		ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
		if(profilePersonInfo != null 
				&& StringUtils.isNotEmpty(profilePersonInfo.getGivenName())
				&& StringUtils.isNotEmpty(profilePersonInfo.getFamilyName())) {
			nameMap.put(profilePersonInfo.getGivenName(), profilePersonInfo.getFamilyName());
		}
		
		// set member companion names
		ProfilePreference profilePreference = retrieveProfileService.retrievePreference(loginInfo.getMemberId(), null);
		setCompanionNames(profilePreference, nameMap);
		
		return nameMap;
	}

	/**
	 * put companion names into nameMap from profilePreference
	 * 
	 * @param profilePreference
	 * @param nameMap
	 */
	private void setCompanionNames(ProfilePreference profilePreference, LinkedHashMap<String, String> nameMap) {
		if(profilePreference == null || CollectionUtils.isEmpty(profilePreference.getTravelCompanions())) {
			return;
		}
		
		for(TravelCompanion companion : profilePreference.getTravelCompanions()) {
			if(companion.getTravelDocument() != null 
					&& StringUtils.isNotEmpty(companion.getTravelDocument().getGivenName()) 
					&& StringUtils.isNotEmpty(companion.getTravelDocument().getFamilyName())) {
				nameMap.put(companion.getTravelDocument().getGivenName(), companion.getTravelDocument().getFamilyName());
			}
		}
	}

	/**
	 * check whether the booking is able to add or not, add condition:
	 * 1. booking's last flight sector has departed less than 3 days;
	 * 2. booking contains at least one sector both operated and marketed by CX/KA;
	 * 3. booking doesn't contain unavailable segments(status: UC/HX);
	 * 
	 * @param booking
	 * @param rloc
	 * @param loginInfo
	 * @param retrievePnrBooking
	 * @throws BusinessBaseException
	 */
	private void checkAddBookingAvailability(Booking booking, String rloc, LoginInfo loginInfo,
			RetrievePnrBooking retrievePnrBooking) throws BusinessBaseException {
		
		// check if booking's last flight sector has departed more than 3 days ago
		checkSegmentDepartureTime(booking, rloc);

		// check if booking include at least one sector both operated and marketed by CX/KA
		checkCxKaFlightExist(booking, rloc);

		// check if check if booking contains unavailable segments(UC/HX)
		checkUnavailableSegment(retrievePnrBooking, rloc);

	}

	/**
	 * check if booking contains unavailable segment
	 * 
	 * @param includeUCOrHXFlight
	 * @param rloc
	 */
	private void checkUnavailableSegment(RetrievePnrBooking retrievePnrBooking, String rloc) throws BusinessBaseException {
		if (statusContainsUcHx(retrievePnrBooking)) {
			throw new ExpectedException(String.format("Unable to add booking - Unavailable segment[UC/HX segment] found in booking:%s ", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_INCLUDE_NOT_AVAILABLE_FLIGHT));
		}
	}

	/**
	 * check if booking include at least one sector both operated and marketed by CX/KA
	 * 
	 * @param booking
	 * @param rloc
	 */
	private void checkCxKaFlightExist(Booking booking, String rloc) throws BusinessBaseException {
		if (booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			throw new ExpectedException(String.format("Unable to add booking - No segment found in booking:%s ", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_UNAVAILABLE_BOOKING));
		}

		// find the segment which is operated by CX/KA and marketed by CX/KA
		Segment segment = booking.getSegments().stream()
				.filter(seg -> seg != null
						&& (OneAConstants.COMPANY_CX.equals(seg.getOperateCompany()) || OneAConstants.COMPANY_KA.equals(seg.getOperateCompany()))
						&& (OneAConstants.COMPANY_CX.equals(seg.getMarketCompany()) || OneAConstants.COMPANY_KA.equals(seg.getMarketCompany())))
				.findFirst().orElse(null);

		// if no segment found, throw exception
		if (segment == null) {
			throw new ExpectedException(String.format("Unable to add booking - booking:%s does not include any sector which is both operated and marketed by CX/KA", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NO_CXKA_FLIGHT_FOUND));
		}
	}

	/**
	 * check booking's last flight sector has departed more than 3 days ago or not
	 * 
	 * @param booking
	 */
	private void checkSegmentDepartureTime(Booking booking, String rloc) throws BusinessBaseException {
		if (booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return;
		}
		List<Segment> sortedSegments = BookingBuildUtil.sortSegmentsByPnrTime(booking.getSegments());
		// last segment
		Segment lastSegment = sortedSegments.get(sortedSegments.size() - 1);
		// if segment is invalid, throw exception
		if (lastSegment == null || lastSegment.getDepartureTime() == null
				|| StringUtils.isEmpty(lastSegment.getDepartureTime().getTime())
				|| StringUtils.isEmpty(lastSegment.getDepartureTime().getTimeZoneOffset())) {
			throw new ExpectedException(String.format("Unable to add booking - Segment departure time missing in booking:%s ", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_UNAVAILABLE_BOOKING));
		}

		Date departureTimeDate;
		// get departure date
		try {
			departureTimeDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
					lastSegment.getDepartureTime().getTime(), lastSegment.getDepartureTime().getTimeZoneOffset());
		} catch (ParseException e) {
			throw new ExpectedException(String.format("Unable to add booking - Fail to parse departure time for segment:%s in booking:%s ", 
					lastSegment.getSegmentID(), rloc), new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_UNAVAILABLE_BOOKING), e);
		}
		// add three days to departure time
		Calendar departureTimeCalendar = Calendar.getInstance();
		departureTimeCalendar.setTime(departureTimeDate);
		departureTimeCalendar.add(Calendar.DAY_OF_YEAR, 3);

		// if booking's last flight sector has departed more than 3 days ago, throw exception
		if (departureTimeCalendar.before(DateUtil.getGMTTime())) {
			throw new ExpectedException(String.format("Unable to add booking - booking:%s last flight sector has departed more than 3 days ago", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_LAST_FLIGHT_DEPARTED_3_DAYS_AGO));
		}
	}

	/**
	 * check the booking is a staff AD ticket/MICE Type C/group booking,
	 * if yes, then throw exception
	 * 
	 * @param booking
	 * @throws BusinessBaseException
	 */
	private void checkStaffMICEGroupBooking(RetrievePnrBooking booking, String rloc) throws BusinessBaseException {
		if(booking == null) {
			return;
		}
		// special handling for thirdy party
		// web channel non mice is not allowed
		// other channel gourp(mice/non mice) is not allowed
        boolean nonMicegroupBooking = MMBBizruleConstants.ACCESS_CHANNEL_WEB
                .equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())
                        ? BookingBuildUtil.isNonMiceGroupBooking(booking.getSkList(), booking.getSsrList())
                        : BookingBuildUtil.isGroupBooking(booking.getSsrList());
		boolean staffBooking = false;
		// staff booking check
		if(!CollectionUtils.isEmpty(booking.getOsiList())) {
			for(RetrievePnrDataElements osi : booking.getOsiList()) {
				if (!CollectionUtils.isEmpty(osi.getOtherDataList()) && osi.getOtherDataList().stream()
						.anyMatch(data -> !StringUtils.isEmpty(data.getFreeText())
								&& data.getFreeText().startsWith(OneAConstants.OSI_FREETEXT_PREFIX_AD)
								&& OneAConstants.COMPANY_CX.equals(data.getCompanyId()))) {
					staffBooking = true;
					break;
				}
			}
		}
		
		if(nonMicegroupBooking || staffBooking || BooleanUtils.isTrue(booking.isIDBooking())) {
			throw new ExpectedException(String.format("Unable to add booking - Booking:%s is a staff_AD / non mice group booking", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_STAFF_MICE_GROUP_BOOKING_NOT_SUPPORT));
		}
	}

	/**
	 * check booking is already exist under this member or not
	 * 
	 * @param loginInfo
	 * @param inputRloc
	 * @param oneARloc 
	 * @throws BusinessBaseException
	 */
	private void checkBookingShowing(LoginInfo loginInfo, String inputRloc, String oneARloc) throws BusinessBaseException {
		@SuppressWarnings("unchecked")
		List<String> rlocs = mbTokenCacheRepository.get(loginInfo.getMmbToken(), TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, ArrayList.class);
		
		if(!CollectionUtils.isEmpty(rlocs) && (rlocs.contains(inputRloc) || rlocs.contains(oneARloc))) {
			throw new ExpectedException(
					String.format("Unable to add booking - Booking:[inputRloc:%s] [oneARloc:%s] is already exist under the member:%s", inputRloc, oneARloc, loginInfo.getMemberId()),
					new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_BOOKING_ALREADY_SHOWING));
		}
	}
	
	/**
	 * check whether it is a companion booking
	 * @param loginInfo
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private boolean checkCompanionBooking(LoginInfo loginInfo, RetrievePnrBooking booking) throws BusinessBaseException {
		paxNameIdentificationService.primaryPaxIdentificationForMember(loginInfo, booking);

		// If all of the passengers are not login member, it is a companion booking
		return Optional.ofNullable(booking.getPassengers()).orElse(new ArrayList<RetrievePnrPassenger>()).stream()
				.allMatch(passenger -> BooleanUtils.isNotTrue(passenger.isLoginMember()));
	}

}
