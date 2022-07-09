package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.BookingPropertiesBusiness;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.dto.common.CustomizedRequiredInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.bookingcustomizedinfo.BookingCustomizedInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.memberselfbookings.SelfBookingsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingCustomizedInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingPackageCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.CprJourneyPassengerSegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.CprJourneySegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.DepartureArrivalTimeCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.FQTVCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.JourneyCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.PassengeCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.PassengerSegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.PurchasedLoungeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.SeatCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.SeatSelectionCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.SegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.UpgradeCustomizedInfo;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.journey.JoruneyResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.journey.JourneySegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.journey.JourneySummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberselfbookings.SelfBookingsResponseDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingPackageInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.ClaimedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneySegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialSeatEligibility;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.BookingPropertiesBuildService;
import com.cathaypacific.mmbbizrule.service.OneABookingSummaryService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.google.common.collect.Lists;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class BookingPropertiesBusinessImpl implements BookingPropertiesBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(BookingPropertiesBusinessImpl.class);
	
	@Autowired
	private BookingPropertiesBuildService bookingPropertiesBuildService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private JourneyCalculateHelper journeyCalculateHelper;
	
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Autowired
	private OneABookingSummaryService oneABookingSummaryService;
	
	@Autowired
	private OJBookingService ojBookingService;
	
	@Override
	public BookingPropertiesDTO getBookingProperties(LoginInfo loginInfo, String rloc) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo,buildBookingPropertiesRequired());
		
		return bookingPropertiesBuildService.buildBookingProperties(booking);
	}
	
	@Override
	public BookingCommercePropertiesDTO getBookingCommerceProperties(LoginInfo loginInfo, String rloc) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo,
				buildBookingPropertiesRequiredForCommerce());
		
		return bookingPropertiesBuildService.buildBookingCommerceProperties(booking);
	}
	
	/**
	 * build required info
	 * @return
	 */
	private BookingBuildRequired buildBookingPropertiesRequired(){
		BookingBuildRequired required =new BookingBuildRequired();
		required.setMemberAward(false);
		required.setSeatSelection(false);
		required.setCprCheck(false);
		//Booking Properties get WaiverBaggage from sk list, so no need build BaggageAllowances 
		required.setBaggageAllowances(false);
		required.setCountryOfResidence(false);
		required.setMealSelection(false);
		required.setPassenagerContactInfo(false);
		required.setEmergencyContactInfo(false);
		required.setCprCheck(true);
		required.setFqtvHolidayCheck(true);
		return required;
	}
	
	/**
	 * build required info
	 * @return
	 */
	private BookingBuildRequired buildBookingPropertiesRequiredForCommerce(){
		BookingBuildRequired required =new BookingBuildRequired();
		required.setMemberAward(false);
		required.setSeatSelection(false);
		//Booking Properties get WaiverBaggage from sk list, so no need build BaggageAllowances 
		required.setBaggageAllowances(false);
		required.setCountryOfResidence(false);
		required.setMealSelection(false);
		required.setPassenagerContactInfo(false);
		required.setEmergencyContactInfo(false);
		required.setCprCheck(false);
		required.setFqtvHolidayCheck(false);
		return required;
	}
	
	@Override
	public JoruneyResponseDTO getBookingJourneys(String oneARloc) throws BusinessBaseException{
		JoruneyResponseDTO result = new JoruneyResponseDTO();

		List<JourneySummary> journeySummaries = journeyCalculateHelper.calculateJourneyFromDpEligibility(oneARloc);
		
		// Covert to DTO
		List<JourneySummaryDTO> journeySummaryDTOs = Lists.newArrayList();
		for (JourneySummary journeySummary: journeySummaries) {
			JourneySummaryDTO journeySummaryDTO = new JourneySummaryDTO();
			journeySummaryDTO.setJourneyId(journeySummary.getJourneyId());
			for (JourneySegment journeySegment: journeySummary.getSegments()) {
				JourneySegmentDTO journeySegmentDTO = new JourneySegmentDTO();
				journeySegmentDTO.setSegmentId(journeySegment.getSegmentId());
				journeySummaryDTO.addSegment(journeySegmentDTO);
			}
			journeySummaryDTOs.add(journeySummaryDTO);
		}
		
		result.setJourneys(journeySummaryDTOs);
		return result;
	}

	@Override
	public BookingCustomizedInfoResponseDTO getBookingCustomizedInfo(BookingCustomizedInfoRequestDTO requestDTO)
			throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		// if rloc in request is null, get RLOC by e-ticket
		if (StringUtils.isEmpty(rloc)) {
			rloc = ticketProcessInvokeService.getRlocByEticket(requestDTO.getEticket());
			if (StringUtils.isEmpty(rloc)) {
				throw new ExpectedException(String.format("Unable to get booking customized info - Cannot find rloc by eticket:%s", requestDTO.getEticket()),
						new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
			}
		} else if(rloc.length() == 7) {
			rloc = getOneARlocFromOJ(rloc, requestDTO);
		}
		
		// retrieve pnrBooking 
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);	
		
		if (pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		
		// new a loginInfo for the following logic
		LoginInfo loginInfo = new LoginInfo();
		
		// name identification 
		if (!StringUtils.isEmpty(requestDTO.getFamilyName()) && !StringUtils.isEmpty(requestDTO.getGivenName())) {
			loginInfo.setLoginFamilyName(requestDTO.getFamilyName());
			loginInfo.setLoginGivenName(requestDTO.getGivenName());
			loginInfo.setLoginType(LoginInfo.LOGINTYPE_RLOC);
			loginInfo.setMmbToken("");
			
			// identify primary passenger
			identifyPrimaryPassenger(requestDTO, pnrBooking, loginInfo);
	
			// check all passengers whether them have any FQTV/FQTR matched with the memberId
			checkPaxMemberInfoMatched(pnrBooking, requestDTO.getMemberId());
		} else if(!StringUtils.isEmpty(requestDTO.getMemberId())) {
			loginInfo.setMemberId(requestDTO.getMemberId());
			loginInfo.setOriginalRuMemberId(requestDTO.getOriginalRuMemberId());
			loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
			loginInfo.setMmbToken("");
			
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		} else {
			throw new ExpectedException("Unable to get booking customized info - memberId & Name both null",
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		// init bookingBuildRequired
		initBookingBuildRequiredWithFalseValue(bookingBuildRequired);
		// build BookingBuildRequired by RequiredInfo in request
		buildBookingBuildRequiredByRequiredInfo(requestDTO.getRequiredInfo(), bookingBuildRequired);
		
		// please note that this build only build the necessary part of booking for now, if anything more is required in the future, please change the "required" 
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, bookingBuildRequired);
	
		return convertToBookingCustomizedInfoDTO(booking, requestDTO.getRequiredInfo());
	}

	/**
	 * identify primary passenger
	 * @param requestDTO
	 * @param pnrBooking
	 * @param loginInfo
	 * @throws BusinessBaseException
	 */
	private void identifyPrimaryPassenger(BookingCustomizedInfoRequestDTO requestDTO,
			RetrievePnrBooking pnrBooking, LoginInfo loginInfo) throws BusinessBaseException {
		// if identification by RLOC failed because of same name error, use e-ticket to identify primary passenger
		try {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
			logger.info(String.format("Use mock login info to build booking, mock login type: %s, familyNam: %s, givenName: %s", LoginInfo.LOGINTYPE_RLOC, requestDTO.getFamilyName(), requestDTO.getGivenName()));
		} catch (ExpectedException e) {
			if (e.getErrorInfo() != null && ErrorCodeEnum.ERR_MULTI_PAX_FOUND.getCode().equals(e.getErrorInfo().getErrorCode()) && !StringUtils.isEmpty(requestDTO.getEticket())) {
				loginInfo.setLoginFamilyName(requestDTO.getFamilyName());
				loginInfo.setLoginGivenName(requestDTO.getGivenName());
				loginInfo.setLoginEticket(requestDTO.getEticket());
				loginInfo.setLoginType(LoginInfo.LOGINTYPE_ETICKET);
				loginInfo.setMmbToken("");
				
				paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
				logger.info(String.format("Use mock login info to build booking, mock login type: %s, familyNam: %s, givenName: %s", LoginInfo.LOGINTYPE_ETICKET, requestDTO.getFamilyName(), requestDTO.getGivenName()));
			} else {
				throw e;
			}
		}
	}

	/**
	 * get 1A RLOC from OJ service by ojRloc
	 * @param rloc
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException 
	 */
	private String getOneARlocFromOJ(String ojRloc, BookingCustomizedInfoRequestDTO requestDTO) throws BusinessBaseException {
		OJBooking ojBooking = ojBookingService.getBooking(requestDTO.getGivenName(), requestDTO.getFamilyName(), ojRloc);
		
		if (ojBooking == null || ojBooking.getFlightBooking() == null) {
			throw new ExpectedException(String.format("Unable to get customized info - Cannot find OJBooking by rloc from ojSErvice:%s", ojRloc),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		
		return ojBooking.getFlightBooking().getBookingReference();
	}

	/**
	 * convert Booking to BookingPropertiesDTO
	 * @param booking
	 * @param customizedRequiredInfoDTO 
	 * @return BookingPropertiesDTO
	 */
	private BookingCustomizedInfoResponseDTO convertToBookingCustomizedInfoDTO(Booking booking, CustomizedRequiredInfoDTO customizedRequiredInfo) {	
		BookingCustomizedInfoResponseDTO bookingCustomizedInfoResponseDTO = new BookingCustomizedInfoResponseDTO();
		if (booking == null) {
			return bookingCustomizedInfoResponseDTO;
		}
		
		bookingCustomizedInfoResponseDTO.setOneARloc(booking.getOneARloc());
		bookingCustomizedInfoResponseDTO.setSpnr(booking.getSpnr());
		bookingCustomizedInfoResponseDTO.setGroupBooking(booking.isGroupBooking());
		bookingCustomizedInfoResponseDTO.setStaffBooking(booking.isStaffBooking());
		bookingCustomizedInfoResponseDTO.setRedemptionBooking(booking.isRedemptionBooking());
		bookingCustomizedInfoResponseDTO.setPos(booking.getPos());
		bookingCustomizedInfoResponseDTO.setPosForAep(booking.getPosForAep());
		bookingCustomizedInfoResponseDTO.setCreateDate(booking.getCreateDate());
		bookingCustomizedInfoResponseDTO.setTposList(booking.getTposList());
		
		BookingPackageInfo packageInfo = booking.getBookingPackageInfo();
		if (packageInfo != null) {
			BookingPackageCustomizedInfoDTO bookingPackageInfoDTO = new BookingPackageCustomizedInfoDTO();
			bookingPackageInfoDTO.setHasEvent(packageInfo.isHasEvent());
			bookingPackageInfoDTO.setHasFlight(packageInfo.isHasFlight());
			bookingPackageInfoDTO.setHasHotel(packageInfo.isHasHotel());
			bookingPackageInfoDTO.setMobBooking(packageInfo.isMobBooking());
			bookingPackageInfoDTO.setNdcBooking(packageInfo.isNdcBooking());
			bookingCustomizedInfoResponseDTO.setBookingPackageInfo(bookingPackageInfoDTO);
		}
				
		convertPassengerCustomizedInfos(booking, bookingCustomizedInfoResponseDTO);
		convertSegmentCustomizedInfos(booking, bookingCustomizedInfoResponseDTO);
		convertPassengerSegmentCustomizedInfos(booking, bookingCustomizedInfoResponseDTO, customizedRequiredInfo);
		
		if (customizedRequiredInfo.getCprCheck()) {
			convertJourneyCustomizedInfo(booking, bookingCustomizedInfoResponseDTO);
		}
		return bookingCustomizedInfoResponseDTO;
	}

	/**
	 * convert JourneyCustomizedInfo
	 * 
	 * @param booking
	 * @param bookingCustomizedInfoResponseDTO
	 * @param customizedRequiredInfo
	 */
	private void convertJourneyCustomizedInfo(Booking booking, BookingCustomizedInfoResponseDTO bookingCustomizedInfoResponseDTO) {
		if (CollectionUtils.isEmpty(booking.getCprJourneys())) {
			return;
		}
		
		List<JourneyCustomizedInfoDTO> journeyCustomizedInfoDTOs = new ArrayList<>();
		for (Journey cprJourney : booking.getCprJourneys()) {
			JourneyCustomizedInfoDTO journeyCustomizedInfoDTO = new JourneyCustomizedInfoDTO();
			if (!CollectionUtils.isEmpty(cprJourney.getErrors())) {
				journeyCustomizedInfoDTO.setErrors(cprJourney.getErrors());
			}
			journeyCustomizedInfoDTO.setJourneyId(cprJourney.getJourneyId());
			journeyCustomizedInfoDTO.setOpenToCheckIn(cprJourney.isOpenToCheckIn());
			journeyCustomizedInfoDTO.setCanCheckIn(cprJourney.isCanCheckIn());
			journeyCustomizedInfoDTO.setBeforeCheckIn(cprJourney.isBeforeCheckIn());
			journeyCustomizedInfoDTO.setPriorityCheckInEligible(cprJourney.getPriorityCheckInEligible());
			journeyCustomizedInfoDTO.setEnabledPriorityCheckIn(cprJourney.isEnabledPriorityCheckIn());
			journeyCustomizedInfoDTO.setCanCheckInAfterUpgrade(cprJourney.isCanCheckInAfterUPgrade());
			convertJourneySegmentCustomizedInfoDTO(cprJourney, journeyCustomizedInfoDTO);
			convertJourneyPassengerSegmentCustomizedInfoDTO(cprJourney, journeyCustomizedInfoDTO);
			journeyCustomizedInfoDTOs.add(journeyCustomizedInfoDTO);
		}
		bookingCustomizedInfoResponseDTO.setCprJourneys(journeyCustomizedInfoDTOs);

	}

	/**
	 * convertJourneySegmentCustomizedInfoDTOs
	 * @param cprJourney
	 * @param journeyCustomizedInfoDTO
	 */
	private void convertJourneySegmentCustomizedInfoDTO(Journey cprJourney,
			JourneyCustomizedInfoDTO journeyCustomizedInfoDTO) {
		if (CollectionUtils.isEmpty(cprJourney.getSegments())) {
			return;
		}
		
		List<CprJourneySegmentCustomizedInfoDTO> cprJourneySegDTOs = new ArrayList<>();
		for (CprJourneySegment cprJourneySeg : cprJourney.getSegments()) {
			CprJourneySegmentCustomizedInfoDTO cprJourneySegDTO = new CprJourneySegmentCustomizedInfoDTO();
			cprJourneySegDTO.setSegmentId(cprJourneySeg.getSegmentId());
			cprJourneySegDTO.setCanCheckIn(cprJourneySeg.getCanCheckIn());
			cprJourneySegDTOs.add(cprJourneySegDTO);
		}
		journeyCustomizedInfoDTO.setSegments(cprJourneySegDTOs);
	}

	/**
	 * convert journeyCustomizedInfoDTOs
	 * @param cprJourney
	 * @param journeyCustomizedInfoDTO
	 */
	private void convertJourneyPassengerSegmentCustomizedInfoDTO(Journey cprJourney, JourneyCustomizedInfoDTO journeyCustomizedInfoDTO) {
		if (CollectionUtils.isEmpty(cprJourney.getPassengerSegments())) {
			return;
		}
		
		List<CprJourneyPassengerSegmentCustomizedInfoDTO> cprJourneyPsDTOs = new ArrayList<>();
		for (CprJourneyPassengerSegment cprJourneyPs : cprJourney.getPassengerSegments()) {
			CprJourneyPassengerSegmentCustomizedInfoDTO cprJourneyPsDTO = new CprJourneyPassengerSegmentCustomizedInfoDTO();
			cprJourneyPsDTO.setCprProductIdentifierDID(cprJourneyPs.getCprProductIdentifierDID());
			cprJourneyPsDTO.setPassengerId(cprJourneyPs.getPassengerId());
			cprJourneyPsDTO.setSegmentId(cprJourneyPs.getSegmentId());
			cprJourneyPsDTO.setCanCheckIn(cprJourneyPs.getCanCheckIn());
			cprJourneyPsDTO.setCheckedIn(cprJourneyPs.getCheckedIn());
			cprJourneyPsDTOs.add(cprJourneyPsDTO);
		}
		journeyCustomizedInfoDTO.setPassengerSegments(cprJourneyPsDTOs);
	}

	/**
	 * convert passengerSegmentCustomizedInfos
	 * @param booking
	 * @param bookingCustomizedInfoResponseDTO
	 * @param customizedRequiredInfo 
	 */
	private void convertPassengerSegmentCustomizedInfos(Booking booking,
			BookingCustomizedInfoResponseDTO bookingCustomizedInfoResponseDTO, CustomizedRequiredInfoDTO customizedRequiredInfo) {
		if (booking != null && !CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			List<PassengerSegmentCustomizedInfoDTO> passengerSegmentCustomizedInfoDTOs = new ArrayList<>();
			for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
				PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO = new PassengerSegmentCustomizedInfoDTO();
				passengerSegmentCustomizedInfoDTO.setPassengerId(passengerSegment.getPassengerId());
				passengerSegmentCustomizedInfoDTO.setSegmentId(passengerSegment.getSegmentId());
				passengerSegmentCustomizedInfoDTO.setEticketNumber(passengerSegment.getEticketNumber());
				passengerSegmentCustomizedInfoDTO.setBookableUpgradeStatus(passengerSegment.getUpgradeProgressStatus());

				convertFqtvCustomizedInfo(passengerSegmentCustomizedInfoDTO, passengerSegment.getFqtvInfo());
				convertClaimedLounge(passengerSegmentCustomizedInfoDTO, passengerSegment.getClaimedLounge());
				convertPurchaseLounge(passengerSegmentCustomizedInfoDTO, passengerSegment.getPurchasedLounge());
				
				if (customizedRequiredInfo != null && customizedRequiredInfo.getSeatInfo()) {
					convertMmbSeatSelection(passengerSegmentCustomizedInfoDTO, passengerSegment.getMmbSeatSelection());
					convertOlciSeatSelection(passengerSegmentCustomizedInfoDTO, passengerSegment.getOlciSeatSelection());
					convertSeat(passengerSegment, passengerSegmentCustomizedInfoDTO, booking.getCprJourneys());
				}
	
				passengerSegmentCustomizedInfoDTOs.add(passengerSegmentCustomizedInfoDTO);
			}
			bookingCustomizedInfoResponseDTO.setPassengerSegments(passengerSegmentCustomizedInfoDTOs);
		}
	}
	
	/**
	 * convert OLCi seat Selection
	 * @param passengerSegmentCustomizedInfoDTO
	 * @param mmbSeatSelection
	 */
	private void convertOlciSeatSelection(PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO,
			SeatSelection seatSelection) {
		if (seatSelection != null) {
			SeatSelectionCustomizedInfoDTO seatSelectionDTO = new SeatSelectionCustomizedInfoDTO();	
			seatSelectionDTO.setEligible(seatSelection.isEligible());
			seatSelectionDTO.setIneligibleReason(seatSelection.getIneligibleReason());
			seatSelectionDTO.setLowRBD(seatSelection.isLowRBD());
			seatSelectionDTO.setDisabilities(seatSelection.getDisabilities());
			seatSelectionDTO.setPaidASR(seatSelection.isPaidASR());
			
			if(BooleanUtils.isTrue(seatSelection.isEligible())) {
				seatSelectionDTO.setAsrFOC(seatSelection.isAsrFOC());
				seatSelectionDTO.setXlFOC(BooleanUtils.isTrue(seatSelection.isXlFOC()));
				seatSelectionDTO.setXlFOCReason(seatSelection.getXlFOCReason());
			
				//set special seat eligibility
				SpecialSeatEligibility specialSeatEligibility = seatSelection.getSpecialSeatEligibility();
				if(specialSeatEligibility != null){
					seatSelectionDTO.findSpecialSeatEligibility().setExlSeat(specialSeatEligibility.getExlSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setAsrSeat(specialSeatEligibility.getAsrSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setExitRowSeat(specialSeatEligibility.getExitRowSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setBcstSeat(specialSeatEligibility.getBcstSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setUmSeat(specialSeatEligibility.getUmSeat());
				}
			}
			passengerSegmentCustomizedInfoDTO.setOlciSeatSelection(seatSelectionDTO);
		}
	}

	private void convertSeat(PassengerSegment passengerSegment,
			PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO, List<Journey> cprJourneys) {
		SeatDetail seat = passengerSegment.getSeat();
		if (seat != null) {
			SeatCustomizedInfoDTO seatDTO = new SeatCustomizedInfoDTO();
			seatDTO.setAsrSeat(BooleanUtils.isTrue(seat.isAsrSeat()));
			seatDTO.setExlSeat(BooleanUtils.isTrue(seat.isExlSeat()));
			// if the seat is from OLCI, windowSeat & AisleSeat may be null, so don't use BooleanUtils
			seatDTO.setWindowSeat(seat.isWindowSeat());
			seatDTO.setAisleSeat(seat.isAisleSeat());
			seatDTO.setFreeSeat(BooleanUtils.isTrue(isFreeSeat(seat, passengerSegment, cprJourneys)));
			seatDTO.setPaid(BooleanUtils.isTrue(seat.isPaid()));
			seatDTO.setSeatNo(seat.getSeatNo());
			seatDTO.setStatus(seat.getStatus());
			seatDTO.setPaymentStatus(seat.getPaymentStatus());
			seatDTO.setFromDCS(seat.isFromDCS());
			passengerSegmentCustomizedInfoDTO.setSeat(seatDTO);
		}
		
		List<SeatDetail> extraSeats = passengerSegment.getExtraSeats();
		if (!CollectionUtils.isEmpty(extraSeats)) {
			List<SeatCustomizedInfoDTO> extraSeatDTOs = new ArrayList<>();
			for (SeatDetail extraSeat : extraSeats) {
				SeatCustomizedInfoDTO extraSeatDTO = new SeatCustomizedInfoDTO();
				extraSeatDTO.setAsrSeat(BooleanUtils.isTrue(extraSeat.isAsrSeat()));
				extraSeatDTO.setExlSeat(BooleanUtils.isTrue(extraSeat.isExlSeat()));
				// if the seat is from OLCI, windowSeat & AisleSeat may be null, so don't use BooleanUtils
				extraSeatDTO.setWindowSeat(extraSeat.isWindowSeat());
				extraSeatDTO.setAisleSeat(extraSeat.isAisleSeat());
				extraSeatDTO.setFreeSeat(BooleanUtils.isTrue(isFreeSeat(extraSeat, passengerSegment, cprJourneys)));
				extraSeatDTO.setPaid(BooleanUtils.isTrue(extraSeat.isPaid()));
				extraSeatDTO.setSeatNo(extraSeat.getSeatNo());
				extraSeatDTO.setPaymentStatus(extraSeat.getPaymentStatus());
				extraSeatDTO.setStatus(extraSeat.getStatus());
				extraSeatDTOs.add(extraSeatDTO);
			}
			passengerSegmentCustomizedInfoDTO.setExtraSeats(extraSeatDTOs);
		}
	}

	private void convertMmbSeatSelection(PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO,
			SeatSelection seatSelection) {
		if (seatSelection != null) {
			SeatSelectionCustomizedInfoDTO seatSelectionDTO = new SeatSelectionCustomizedInfoDTO();				
			seatSelectionDTO.setEligible(seatSelection.isEligible());
			seatSelectionDTO.setIneligibleReason(seatSelection.getIneligibleReason());
			seatSelectionDTO.setLowRBD(seatSelection.isLowRBD());
			seatSelectionDTO.setDisabilities(seatSelection.getDisabilities());
			seatSelectionDTO.setPaidASR(seatSelection.isPaidASR());
			
			if(BooleanUtils.isTrue(seatSelection.isEligible())) {
				seatSelectionDTO.setAsrFOC(seatSelection.isAsrFOC());
				seatSelectionDTO.setSelectedAsrFOC(seatSelection.isSelectedAsrFOC());
				seatSelectionDTO.setXlFOC(BooleanUtils.isTrue(seatSelection.isXlFOC()));
				seatSelectionDTO.setXlFOCReason(seatSelection.getXlFOCReason());
			
				//set special seat eligibility
				SpecialSeatEligibility specialSeatEligibility = seatSelection.getSpecialSeatEligibility();
				if(specialSeatEligibility != null){
					seatSelectionDTO.findSpecialSeatEligibility().setExlSeat(specialSeatEligibility.getExlSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setAsrSeat(specialSeatEligibility.getAsrSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setExitRowSeat(specialSeatEligibility.getExitRowSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setBcstSeat(specialSeatEligibility.getBcstSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setUmSeat(specialSeatEligibility.getUmSeat());
				}
			}
			passengerSegmentCustomizedInfoDTO.setSeatSelection(seatSelectionDTO);
		}
	}

	private void convertClaimedLounge(PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO,
			ClaimedLounge claimedLounge) {
		if (claimedLounge != null) {
			ClaimedLoungeDTO claimedLoungeDTO = new ClaimedLoungeDTO();
			claimedLoungeDTO.setType(claimedLounge.getType());
			claimedLoungeDTO.setTier(claimedLounge.getTier());
			passengerSegmentCustomizedInfoDTO.setClaimedLounge(claimedLoungeDTO);
		}
	}
	
	private void convertPurchaseLounge(PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO,
			PurchasedLounge purchasedLounge) {
		if(purchasedLounge != null) {
			PurchasedLoungeDTO purchasedLoungeDTO = new PurchasedLoungeDTO();
			purchasedLoungeDTO.setType(purchasedLounge.getType());
			purchasedLoungeDTO.setTier(purchasedLounge.getTier());
			passengerSegmentCustomizedInfoDTO.setPurchasedLounge(purchasedLoungeDTO);
		}
	}

	private void convertFqtvCustomizedInfo(PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO,
			FQTVInfo fqtvInfo) {
		if (fqtvInfo != null) {
			FQTVCustomizedInfoDTO fqtvCustomizedInfoDTO = new FQTVCustomizedInfoDTO();
			fqtvCustomizedInfoDTO.setCompanyId(fqtvInfo.getCompanyId());
			fqtvCustomizedInfoDTO.setIsAM(BooleanUtils.isTrue(fqtvInfo.getAm()));
			fqtvCustomizedInfoDTO.setIsMPO(BooleanUtils.isTrue(fqtvInfo.getMpo()));
			fqtvCustomizedInfoDTO.setMembershipNumber(fqtvInfo.getMembershipNumber());
			fqtvCustomizedInfoDTO.setTierLevel(fqtvInfo.getTierLevel());
			fqtvCustomizedInfoDTO.setTopTier(BooleanUtils.isTrue(fqtvInfo.isTopTier()));
			fqtvCustomizedInfoDTO.setHolidayStartDate(fqtvInfo.getHolidayStartDate());
			fqtvCustomizedInfoDTO.setHolidayEndDate(fqtvInfo.getHolidayEndDate());
			fqtvCustomizedInfoDTO.setOnHoliday(fqtvInfo.getOnHoliday());
			passengerSegmentCustomizedInfoDTO.setFqtvInfo(fqtvCustomizedInfoDTO);
		}
	}
	
	/**
	 * check if the seat is a free seat
	 * @param seatDetail
	 * @param passengerSegment
	 * @param cprJourneys 
	 * @return
	 */
	private Boolean isFreeSeat(SeatDetail seatDetail, PassengerSegment passengerSegment, List<Journey> cprJourneys) {
		if(seatDetail == null || passengerSegment == null || passengerSegment.getMmbSeatSelection() == null) {
			return false;
		}
		
		SeatSelection seatSelection;
		// if segment enter check in time, use OLCI seat selection
		if (isCprInfoOfSegmentGot(passengerSegment.getSegmentId(), cprJourneys)) {
			seatSelection = passengerSegment.getOlciSeatSelection();
		} else {
			seatSelection = passengerSegment.getMmbSeatSelection();
		}

		return (BooleanUtils.isTrue(seatDetail.isAsrSeat()) && (seatSelection.isAsrFOC() || seatSelection.isSelectedAsrFOC()))
				|| (BooleanUtils.isTrue(seatDetail.isExlSeat()) && BooleanUtils.isTrue(seatSelection.isXlFOC()))
				|| (!BooleanUtils.isTrue(seatDetail.isAsrSeat()) && !BooleanUtils.isTrue(seatDetail.isExlSeat()));
	}
	
	/**
	 * got cpr segment info of the segment
	 * @param segmentId
	 * @param cprJourneys
	 * @return boolean
	 */
	private boolean isCprInfoOfSegmentGot(String segmentId, List<Journey> cprJourneys) {
		if (CollectionUtils.isEmpty(cprJourneys) || StringUtils.isEmpty(segmentId)) {
			return false;
		}

		return cprJourneys.stream().anyMatch(journey -> !CollectionUtils.isEmpty(journey.getSegments())
				&& journey.getSegments().stream().anyMatch(seg -> segmentId.equals(seg.getSegmentId())));
	}

	/**
	 * convert segmentCustomizedInfos
	 * @param booking
	 * @param bookingCustomizedInfoResponseDTO
	 */
	private void convertSegmentCustomizedInfos(Booking booking,
			BookingCustomizedInfoResponseDTO bookingCustomizedInfoResponseDTO) {
		if (booking != null && !CollectionUtils.isEmpty(booking.getSegments())) {
			List<SegmentCustomizedInfoDTO> segmentCustomizedInfoDTOs = new ArrayList<>();
			for (Segment segment : booking.getSegments()) {
				SegmentCustomizedInfoDTO segmentCustomizedInfoDTO = new SegmentCustomizedInfoDTO();
				segmentCustomizedInfoDTO.setCabinClass(segment.getCabinClass());
				segmentCustomizedInfoDTO.setSubClass(segment.getSubClass());
				segmentCustomizedInfoDTO.setMarketCabinClass(segment.getMarketCabinClass());
				segmentCustomizedInfoDTO.setMarketSubClass(segment.getMarketSubClass());
				segmentCustomizedInfoDTO.setOriginPort(segment.getOriginPort());
				segmentCustomizedInfoDTO.setDestPort(segment.getDestPort());
				segmentCustomizedInfoDTO.setMarketCompany(segment.getMarketCompany());
				segmentCustomizedInfoDTO.setMarketSegmentNumber(segment.getMarketSegmentNumber());
				segmentCustomizedInfoDTO.setOperateCompany(segment.getOperateCompany());
				segmentCustomizedInfoDTO.setOperateSegmentNumber(segment.getOperateSegmentNumber());
				segmentCustomizedInfoDTO.setHaulType(segment.getHaulType());
				segmentCustomizedInfoDTO.setAirCraftType(segment.getAirCraftType());
				segmentCustomizedInfoDTO.setSegmentId(segment.getSegmentID());
				segmentCustomizedInfoDTO.setCheckedIn(segment.isCheckedIn());
				segmentCustomizedInfoDTO.setPostCheckIn(segment.isPostCheckIn());
				segmentCustomizedInfoDTO.setOpenToCheckIn(segment.isOpenToCheckIn());

				DepartureArrivalTime departureTime = segment.getDepartureTime();
				DepartureArrivalTime arrivalTime = segment.getArrivalTime();
				if (departureTime != null) {
					DepartureArrivalTimeCustomizedInfoDTO departureTimeDTO = new DepartureArrivalTimeCustomizedInfoDTO();
					departureTimeDTO.setPnrTime(departureTime.getPnrTime());
					departureTimeDTO.setRtfsActualTime(departureTime.getRtfsActualTime());
					departureTimeDTO.setRtfsEstimatedTime(departureTime.getRtfsEstimatedTime());
					departureTimeDTO.setRtfsScheduledTime(departureTime.getRtfsScheduledTime());
					departureTimeDTO.setTimeZoneOffset(departureTime.getTimeZoneOffset());
					segmentCustomizedInfoDTO.setDepartureTime(departureTimeDTO);
				}

				if (arrivalTime != null) {
					DepartureArrivalTimeCustomizedInfoDTO arrivalTimeDTO = new DepartureArrivalTimeCustomizedInfoDTO();
					arrivalTimeDTO.setPnrTime(arrivalTime.getPnrTime());
					arrivalTimeDTO.setRtfsActualTime(arrivalTime.getRtfsActualTime());
					arrivalTimeDTO.setRtfsEstimatedTime(arrivalTime.getRtfsEstimatedTime());
					arrivalTimeDTO.setRtfsScheduledTime(arrivalTime.getRtfsScheduledTime());
					arrivalTimeDTO.setTimeZoneOffset(arrivalTime.getTimeZoneOffset());
					segmentCustomizedInfoDTO.setArrivalTime(arrivalTimeDTO);
				}
				
				SegmentStatus segmentStatus = segment.getSegmentStatus();
				if (segmentStatus != null) {
					segmentCustomizedInfoDTO.setStatus(segmentStatus.getStatus());
					segmentCustomizedInfoDTO.setFlown(segmentStatus.isFlown());
					segmentCustomizedInfoDTO.setDisable(segmentStatus.isDisable());
				}
				
				convertUpgradeInfo(segmentCustomizedInfoDTO, segment);
				
				segmentCustomizedInfoDTOs.add(segmentCustomizedInfoDTO);
			}

			bookingCustomizedInfoResponseDTO.setSegments(segmentCustomizedInfoDTOs);
		}
	}
	
	/**
	 * convert Upgrade Info
	 * @param segmentCustomizedInfoDTO
	 * @param segment
	 */
	private void convertUpgradeInfo(SegmentCustomizedInfoDTO segmentCustomizedInfoDTO, Segment segment ){
		if (segment.getUpgradeInfo() != null) {
			UpgradeCustomizedInfo upgradeCustomizedInfo = new UpgradeCustomizedInfo();
			segmentCustomizedInfoDTO.setUpgradeInfo(upgradeCustomizedInfo);
			upgradeCustomizedInfo.setFromCabinClass(segment.getUpgradeInfo().getFromCabinClass());
			upgradeCustomizedInfo.setFromSubClass(segment.getUpgradeInfo().getFromSubClass());
			upgradeCustomizedInfo.setToCabinClass(segment.getUpgradeInfo().getToCabinClass());
			upgradeCustomizedInfo.setToSubClass(segment.getUpgradeInfo().getToSubClass());
			if(segment.getUpgradeInfo().getRedUpgradeInfo()!=null){
				upgradeCustomizedInfo.setUpgradeRedStatus(segment.getUpgradeInfo().getRedUpgradeInfo().getStatus());
			}
			
			if(segment.getUpgradeInfo().getBidUpgradeInfo()!=null){
				upgradeCustomizedInfo.setUpgradeBidStatus(segment.getUpgradeInfo().getBidUpgradeInfo().getStatus());
			}
			
			if(segment.getUpgradeInfo().getBookableUpgradeInfo()!=null){
				upgradeCustomizedInfo.setBookableUpgradeStatus(segment.getUpgradeInfo().getBookableUpgradeInfo().getStatus());
			}
		}
	}

	/**
	 * convert passengerCustomizedInfos
	 * @param booking
	 * @param bookingCustomizedInfoResponseDTO
	 */
	private void convertPassengerCustomizedInfos(Booking booking,
			BookingCustomizedInfoResponseDTO bookingCustomizedInfoResponseDTO) {
		if (booking != null && !CollectionUtils.isEmpty(booking.getPassengers())) {
			List<PassengeCustomizedInfoDTO> passengeCustomizedInfoDTOs = new ArrayList<>();
			for (Passenger passenger : booking.getPassengers()) {
				PassengeCustomizedInfoDTO passengeCustomizedInfoDTO = new PassengeCustomizedInfoDTO();
				passengeCustomizedInfoDTO.setFamilyName(passenger.getFamilyName());
				passengeCustomizedInfoDTO.setGivenName(passenger.getGivenName());
				passengeCustomizedInfoDTO.setParentId(passenger.getParentId());
				passengeCustomizedInfoDTO.setPassengerId(passenger.getPassengerId());
				passengeCustomizedInfoDTO.setPassengerType(passenger.getPassengerType());
				passengeCustomizedInfoDTO.setTitle(passenger.getTitle());
				passengeCustomizedInfoDTO.setLoginFFPMatched(passenger.isLoginFFPMatched());
				passengeCustomizedInfoDTO.setUnaccompaniedMinor(passenger.isUnaccompaniedMinor());
				passengeCustomizedInfoDTOs.add(passengeCustomizedInfoDTO);
			}
			bookingCustomizedInfoResponseDTO.setPassengers(passengeCustomizedInfoDTOs);
		}
	}

	/**
	 * check all passengers whether them have any FQTV/FQTR matched with the memberId
	 * @param pnrBooking
	 */
	private void checkPaxMemberInfoMatched(RetrievePnrBooking pnrBooking, String memberId) {
		if (pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengers()) || StringUtils.isEmpty(memberId)) {
			return;
		}
		
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		
		for (RetrievePnrPassenger pnrPassenger : pnrPassengers) {
			// check any customer level member info matched
			if (checkMemberIdMatched(pnrPassenger.getFQTVInfos(), memberId)
					|| checkMemberIdMatched(pnrPassenger.getFQTRInfos(), memberId)) {
				pnrPassenger.setLoginFFPMatched(true);
				continue;
			}
			
			// check any product level member info matched
			pnrPassenger.setLoginFFPMatched(Optional.ofNullable(pnrBooking.getPassengerSegments())
					.orElse(new ArrayList<>()).stream()
					.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
							&& ps.getPassengerId().equals(pnrPassenger.getPassengerID())
							&& (!CollectionUtils.isEmpty(ps.getFQTRInfos())
									|| !CollectionUtils.isEmpty(ps.getFQTVInfos())))
					.anyMatch(ps -> checkMemberIdMatched(ps.getFQTVInfos(), memberId)
							|| checkMemberIdMatched(ps.getFQTRInfos(), memberId)));
		}
		
	}

	/**
	 * check if member id matched with one of the info in the list
	 * @param memberInfos
	 * @param memberId
	 * @return boolean
	 */
	private boolean checkMemberIdMatched(List<RetrievePnrFFPInfo> memberInfos, String memberId) {
		if (CollectionUtils.isEmpty(memberInfos) || StringUtils.isEmpty(memberId)) {
			return false;
		}
		
		return memberInfos.stream().anyMatch(info -> memberId.equals(info.getFfpMembershipNumber()));
	}

	/**
	 * build BookingBuildRequired by RequiredInfo
	 * @param requiredInfo
	 * @param bookingBuildRequired
	 */
	private void buildBookingBuildRequiredByRequiredInfo(CustomizedRequiredInfoDTO requiredInfo,
			BookingBuildRequired bookingBuildRequired) {
		if (requiredInfo != null) {
			bookingBuildRequired.setCprCheck(requiredInfo.getCprCheck());
			bookingBuildRequired.setFqtvHolidayCheck(requiredInfo.getMemberHolidayCheck());
			bookingBuildRequired.setRtfs(requiredInfo.getRtfsTime());
			bookingBuildRequired.setBookableUpgradeStatusCheck(requiredInfo.isBookableUpgradeStatusCheck());
			bookingBuildRequired.setOperateInfoAndStops(true);
			bookingBuildRequired.setSeatSelection(requiredInfo.getSeatInfo());
		}
	}

	/**
	 * init BookingBuildRequired with false value
	 * @param bookingBuildRequired 
	 */
	private void initBookingBuildRequiredWithFalseValue(BookingBuildRequired bookingBuildRequired) {
		if (bookingBuildRequired == null) {
			return;
		}
		bookingBuildRequired.setBaggageAllowances(false);
		bookingBuildRequired.setRtfs(false);
		bookingBuildRequired.setCprCheck(false);
		bookingBuildRequired.setOperateInfoAndStops(false);
		bookingBuildRequired.setPassenagerContactInfo(false);
		bookingBuildRequired.setMemberAward(false);
		bookingBuildRequired.setEmergencyContactInfo(false);
		bookingBuildRequired.setCountryOfResidence(false);
		bookingBuildRequired.setTravelDocument(false);
		bookingBuildRequired.setMealSelection(false);
		bookingBuildRequired.setSeatSelection(false);
		bookingBuildRequired.setSummaryPage(false);
	}
	
	@Override
	public SelfBookingsResponseDTO getSelfBookingList(SelfBookingsRequestDTO requestDTO,boolean includeCompanionbooking) throws BusinessBaseException {
		SelfBookingsResponseDTO responseDTO = new SelfBookingsResponseDTO();

		long currentTimeMillis = System.currentTimeMillis();

		Future<List<FlightBookingSummaryConvertBean>> asyncOneA = oneABookingSummaryService
				.asyncGetOneABookingList(requestDTO.getMemberId());

		List<FlightBookingSummaryConvertBean> oneABookings;
		try {
			oneABookings = asyncOneA.get();
		} catch (Exception e) {
			throw new UnexpectedException(String.format("Cannot get 1A booking list by memberId :%s", requestDTO),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
		}

		// new loginInfo for following logic
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		loginInfo.setMemberId(requestDTO.getMemberId());
		loginInfo.setMmbToken("");

		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		// init bookingBuildRequired
		initBookingBuildRequiredWithFalseValue(bookingBuildRequired);
		// build BookingBuildRequired by RequiredInfo in request
		buildBookingBuildRequiredByRequiredInfo(requestDTO.getRequiredInfo(), bookingBuildRequired);

		List<Future<Booking>> selfBookingFutures = new ArrayList<>();
		for (FlightBookingSummaryConvertBean oneABooking : oneABookings) {
			selfBookingFutures
					.add(oneABookingSummaryService.asyncGetSelfBookings(oneABooking, loginInfo, bookingBuildRequired));
		}
		
		// filter bookings ==> exclude companion bookings
		if(!includeCompanionbooking){
			filterBookings(selfBookingFutures);
		}
		
		
		List<BookingCustomizedInfoResponseDTO> selfBookingDTOs = new ArrayList<>();
		for (Future<Booking> selfBookingFuture : selfBookingFutures) {
			try {
				Booking selfBooking = selfBookingFuture.get();
				if (selfBooking != null) {
					selfBookingDTOs.add(convertToBookingCustomizedInfoDTO(selfBooking, requestDTO.getRequiredInfo()));
				}
			} catch (Exception e) {
				logger.warn("Error to retrieve OneA booking", e);
			}
		}

		// sort bookings by the departure date of the first segment
		sortBookings(selfBookingDTOs);
		
		long time = System.currentTimeMillis() - currentTimeMillis;
		logger.info(String.format("member self booking count:[%s], build self booking list spend time(ms):[%s]",
				selfBookingDTOs.size(), time));

		if (!CollectionUtils.isEmpty(selfBookingDTOs)) {
			responseDTO.setBookings(selfBookingDTOs);
		}

		return responseDTO;
	}

	private List<Future<Booking>> filterBookings(List<Future<Booking>> bookings){
		if(CollectionUtils.isEmpty(bookings)){
			return null;
		}
		return bookings.stream().filter(futureBooking -> {
			try {
				Booking booking = futureBooking.get();
				if (booking == null) {
					return false;
				}
				return checkCompanionBooking(booking);
			} catch (Exception e) {
				logger.warn("Error to get booking", e);
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	/**
	 * sort bookings by the departure date of the first segment
	 * @param selfBookingDTOs
	 */
	private void sortBookings(List<BookingCustomizedInfoResponseDTO> selfBookingDTOs) {
		if (CollectionUtils.isEmpty(selfBookingDTOs)) {
			return;
		}
		
		selfBookingDTOs.sort((booking1, booking2) -> {
			SegmentCustomizedInfoDTO firstSegOfBookig1 = getFirstAvlSegmentInBooking(booking1);
			SegmentCustomizedInfoDTO firstSegOfBookig2 = getFirstAvlSegmentInBooking(booking2);
			
			Date date1 = getSegmentDepartureDate(firstSegOfBookig1);
			Date date2 = getSegmentDepartureDate(firstSegOfBookig2);
			
			// the result of compare by RLOC
			int rlocCompareResult;
			if (StringUtils.isEmpty(booking1.getOneARloc())) {
				rlocCompareResult = 1;
			} else if (StringUtils.isEmpty(booking2.getOneARloc())) {
				rlocCompareResult = -1;
			} else {
				rlocCompareResult = booking1.getOneARloc().compareTo(booking2.getOneARloc());
			}
			
			if (date1 == null && date2 == null) {
				// if dates are both empty, compare by RLOC
				return rlocCompareResult;
			}
			else if (date1 == null) {
				return -1;
			} else if (date2 == null) {
				return 1;
			} else {
				int dateCompareResult = getSegmentDepartureDate(firstSegOfBookig1)
						.compareTo(getSegmentDepartureDate(firstSegOfBookig2));
				if (dateCompareResult != 0) {
					return dateCompareResult;
				} else {
					// if date is the same, compare by RLOC
					return rlocCompareResult;
				}
			}
		});

	}

	/**
	 * get the departure date of the segment
	 * @param firstSegOfBookig1
	 * @return
	 */
	private Date getSegmentDepartureDate(SegmentCustomizedInfoDTO segment) {
		if (segment == null || segment.getDepartureTime() == null
				|| StringUtils.isEmpty(segment.getDepartureTime().getPnrTime())
				|| StringUtils.isEmpty(segment.getDepartureTime().getTimeZoneOffset())) {
			return null;
		}		
		DepartureArrivalTimeCustomizedInfoDTO departureTime = segment.getDepartureTime();
		
		try {
			return DateUtil.getStrToDate(DepartureArrivalTimeCustomizedInfoDTO.TIME_FORMAT, departureTime.getPnrTime(), departureTime.getTimeZoneOffset());
		} catch (ParseException e) {
			logger.error("Failed to parse departure time", e);
			return null;
		}
	}
/**
	 * get first available segment of the booking
	 * @param booking1
	 * @return
	 */
	private SegmentCustomizedInfoDTO getFirstAvlSegmentInBooking(BookingCustomizedInfoResponseDTO booking) {
		if (booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return null;
		}
		return booking.getSegments().stream()
				// filter out unavailable segments
				.filter(seg -> seg.getDepartureTime() != null && !StringUtils.isEmpty(seg.getDepartureTime().getPnrTime())
						&& !StringUtils.isEmpty(seg.getDepartureTime().getTimeZoneOffset()) && !BooleanUtils.isTrue(seg.getFlown()) && !BooleanUtils.isTrue(seg.getDisable()))
				// sort by departure time
				.sorted((seg1, seg2) -> {
					DepartureArrivalTimeCustomizedInfoDTO departureTime1 = seg1.getDepartureTime();
					DepartureArrivalTimeCustomizedInfoDTO departureTime2 = seg2.getDepartureTime();

					Date departureDate1;
					Date departureDate2;
					try {
						departureDate1 = DateUtil.getStrToDate(DepartureArrivalTimeCustomizedInfoDTO.TIME_FORMAT, departureTime1.getPnrTime(),
								departureTime1.getTimeZoneOffset());
					} catch (ParseException e) {
						logger.error("Failed to parse departure time", e);
						return 1;
					}

					try {
						departureDate2 = DateUtil.getStrToDate(DepartureArrivalTimeCustomizedInfoDTO.TIME_FORMAT, departureTime2.getPnrTime(),
								departureTime2.getTimeZoneOffset());
					} catch (ParseException e) {
						logger.error("Failed to parse departure time", e);
						return -1;
					}

					return departureDate1.compareTo(departureDate2);
				}).findFirst().orElse(null);
	}
	
	/**
	 * check whether it is a companion booking
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private boolean checkCompanionBooking(Booking booking) throws BusinessBaseException {
		
		// If all of the passengers are not login member, it is a companion booking
		return Optional.ofNullable(booking.getPassengers()).orElse(new ArrayList<Passenger>()).stream()
				.allMatch(passenger -> BooleanUtils.isNotTrue(passenger.isLoginFFPMatched()));
	}
}
