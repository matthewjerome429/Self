package com.cathaypacific.mmbbizrule.cxservice.olci.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.OLCIConnector;
import com.cathaypacific.mmbbizrule.cxservice.olci.constant.OLCIconstant;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightDetailsType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightIdentifierType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightInformationType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightListType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightStatusCheckInInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightStatusInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Journey;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.JourneyDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Passenger;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerCheckInInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerInfoType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PersonalInfoType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.RetrieveJourneyResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.request.CancelCheckInRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.CancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.NonMemberLoginResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.OLCIService;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.dto.response.cancelcheckin.BookingCancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.util.HttpResponse;
import com.cathaypacific.mmbbizrule.util.JourneyUtil;
import com.cathaypacific.mmbbizrule.util.NameIdentficationUtil;
import com.cathaypacific.mmbbizrule.util.SessionUtil;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendBPRequestDTO;
import com.cathaypacific.olciconsumer.service.client.OlciClient;

@Service
public class OLCIServiceImpl implements OLCIService{
	
	private static final LogAgent logger = LogAgent.getLogAgent(OLCIServiceImpl.class);
	
	@Autowired
	private OLCIConnector olciConnector;
	
	@Autowired
	private ConstantDataDAO constantDataDAO;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Value("${givenName.maxCharacterToMatch}")
	private Integer shortCompareSize;
	
	@Autowired
	private OlciClient olciClient;
	
	@Async
	@Override
	public Future<List<PassengerCheckInInfo>> asyncGetBRLPaxDetails(LoginInfo loginInfo, Booking booking) {
		return new AsyncResult<>(this.getBRLPaxDetails(loginInfo, booking));
	}


	@Override
	@Async
	public CancelCheckInResponseDTO cancelCheckInWithOLCI(CancelCheckInRequestDTO cancelCheckInRequestDTO,String cookie) {
		return olciConnector.cancelCheckInWithOLCI(cancelCheckInRequestDTO,cookie);
	}

	@Override
	public BookingCancelCheckInResponseDTO cancelCheckInByLogin(LoginInfo loginInfo,String rloc) throws BusinessBaseException, InterruptedException {
		BookingCancelCheckInResponseDTO responseDTO = new BookingCancelCheckInResponseDTO();
		responseDTO.setSuccess(true);
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		String familyName=StringUtils.EMPTY;
		String givenName=StringUtils.EMPTY;
		if(retrievePnrBooking!=null && !retrievePnrBooking.getPassengers().isEmpty()){
			familyName = retrievePnrBooking.getPassengers().get(0).getFamilyName();
			givenName = retrievePnrBooking.getPassengers().get(0).getGivenName();
		}
		/**first login to OLCI by NonMemberLogin*/
		// OLCI creates new session everytime
		ResponseEntity<NonMemberLoginResponseDTO> responseEntity = olciConnector.nonMemberLoginWithOLCI(rloc,givenName,familyName);

		NonMemberLoginResponseDTO response = responseEntity.getBody();
		if(!CollectionUtils.isEmpty(response.getErrors())){
			responseDTO.setSuccess(false);
			responseDTO.addError(new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.WARN_CANCEL_CHECKIN_NO_ELIGIBILITY));
			return responseDTO;
		}
		String cookie = SessionUtil.getCookie(responseEntity);

		/**next cancel check in*/
		if(!CollectionUtils.isEmpty(response.getJourneys())){
			if (response.getJourneys().stream().noneMatch(this::cancelEligibilityCheck)) {
				responseDTO.setSuccess(false);
				responseDTO.addError(new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.WARN_CANCEL_CHECKIN_NO_ELIGIBILITY));
				return responseDTO;
			}
			List<AsyncResult<CancelCheckInResponseDTO>> asyncResults =new ArrayList<>();
			response.getJourneys().stream().forEach(journeyDTO -> {
				if(cancelEligibilityCheck(journeyDTO)){
				   asyncResults.add(new AsyncResult<>(cancelCheckInWithOLCI(bulidCancelCheckInRequestDTO(journeyDTO), cookie)));
				}
			});
			asyncResults.stream().forEach(asyncResult -> {
				try {
					if(!checkCancelCheckInResponse(asyncResult.get())){
						responseDTO.setSuccess(false);
					}
				} catch (ExecutionException e) {
					logger.warn("not success cancel check in");
				}
			});
		}
		return responseDTO;
	}

	@Override
	public List<PassengerCheckInInfo> getBRLPaxDetails(LoginInfo loginInfo, Booking booking) {
	

		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger =
				Optional.ofNullable(booking.getPassengers()).orElse(Collections.emptyList()).stream().filter(
						pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())
					).findFirst().orElse(null);
		if(passenger==null){
			return Collections.emptyList();
		}
		
		List<PassengerCheckInInfo> passengerCheckInInfos = new ArrayList<>();
		HttpResponse<RetrieveJourneyResponseDTO> journeyResponse = new HttpResponse<>();
		
		String givenName = passenger.getGivenName();
		String familyName = passenger.getFamilyName();

		try {
			journeyResponse = olciConnector.getJourneyResponseByRLOC(booking.getOneARloc(), passenger.getGivenName(), passenger.getFamilyName());
			RetrieveJourneyResponseDTO journeyResponseDTO = journeyResponse.getValue();
		
			if (journeyResponseDTO != null && !CollectionUtils.isEmpty(journeyResponseDTO.getJourneys()) && !CollectionUtils.isEmpty(booking.getSegments())) {
				for (Journey identifiedJourney : journeyResponseDTO.getJourneys()) {
					setDisplayOnlyInfo(identifiedJourney);
					populatePassengerCheckInInfo(booking, identifiedJourney, givenName, familyName, passengerCheckInInfos);
				}
			}
		} catch (Exception e) {
			logger.error("Error invoking OLCI service API", e);
		}

		
		return passengerCheckInInfos;
	}
	
	private void populatePassengerCheckInInfo(Booking booking, Journey identifiedJourney, String givenName, String familyName, List<PassengerCheckInInfo> passengerCheckInInfos) {
		for(Segment segment : booking.getSegments()) {
			PassengerCheckInInfo passengerCheckInInfo = new PassengerCheckInInfo();
			if(!CollectionUtils.isEmpty(identifiedJourney.getPassengers())) {
				Flight flight = getFlightFromBooking(segment, identifiedJourney.getPassengers().get(0));
				if(flight != null) {
					populateJourneyDCSToPassengerCheckInInfo(passengerCheckInInfo, segment, identifiedJourney, flight);
					FlightStatusCheckInInfo flightStatusCheckInInfo = transFormJourneyDCSToFlightStatusCheckInInfo(segment, identifiedJourney, givenName, familyName);
					passengerCheckInInfo.setFlightStatusCheckInInfo(flightStatusCheckInInfo);
					passengerCheckInInfo.setSource(MMBBizruleConstants.OLCI_SOURCE);
					passengerCheckInInfo.setFlightDate(getDateTimeFromOLCI(flight.getDepartureTime().getScheduledTime()));
					passengerCheckInInfo.setFltNo(flight.getOperateFlightNumber());
					passengerCheckInInfo.setCarrierCode(flight.getOperateCompany());
					passengerCheckInInfos.add(passengerCheckInInfo);
				}
			}
		}
	}
	
	private Flight getFlightFromBooking(Segment segment, Passenger cpr) {
		Flight flightInBrlResponse = null;
		if(!CollectionUtils.isEmpty(cpr.getFlights())) {
			for(Object tempFlight : cpr.getFlights()) {
				Flight flight = (Flight) tempFlight;
				logCmFlight(flight);
				
				if(flight != null && StringUtils.equals(segment.getDepartureTime().getPnrTime(), flight.getDepartureTime().getScheduledTime())
						&& StringUtils.equals(segment.getOriginPort(), flight.getOriginPort()) 
						&& StringUtils.equals(segment.getDestPort(), flight.getDestPort())
						&& ((StringUtils.equals(segment.getOperateCompany(), flight.getOperateCompany()) && compareFlightNumber(segment.getMarketSegmentNumber(), flight.getOperateFlightNumber())) 
								|| (StringUtils.equals(segment.getMarketCompany(), flight.getMarketingCompany()) && compareFlightNumber(segment.getMarketSegmentNumber(), flight.getMarketFlightNumber())) )) {
					flightInBrlResponse = flight;
					break;
				}
				
			}
		}
		return flightInBrlResponse;
	} 

	private void logCmFlight(Flight flight) {
		if (flight != null) {
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("CM Flight: flight no: ").append(flight.getOperateCompany()).append(flight.getOperateFlightNumber()).append(" flight date: ").append(flight.getDepartureTime().getScheduledTime()).append(" origin: ").append(flight.getOriginPort()).append(" destination: ").append(flight.getDestPort());
			logger.debug(strBuilder.toString());
		}
	}
	
	private boolean compareFlightNumber(String lhfFlightNumber, String rhfFlightNumber) {
		if (StringUtils.length(lhfFlightNumber) == StringUtils.length(rhfFlightNumber)) {
			return StringUtils.equals(lhfFlightNumber, rhfFlightNumber);
		} else {
			return StringUtils.equals(StringUtils.leftPad(lhfFlightNumber, 4, "0"), StringUtils.leftPad(rhfFlightNumber, 4, "0"));
		}
	}
	
	protected PassengerCheckInInfo populateJourneyDCSToPassengerCheckInInfo(PassengerCheckInInfo passengerCheckInInfo, Segment segment, Journey journeyDCS, Flight flight) {
		List<PassengerInfoType> paxlist = new ArrayList<>();

		for (Passenger cpr : journeyDCS.getPassengers()) {
			PassengerInfoType paxInfoType = new PassengerInfoType();

			Flight flightInBrlResponse = getFlightFromBooking(segment, cpr);
			if (flightInBrlResponse == null) {
				continue;
			}

			// @Ben, From OLCI team:
			// Philip: journeys.passengers.flights.acceptanceStatus, CAC = accepted, CAN = not accepted, CST = standby, CRJ = Rejected
			paxInfoType.setCheckInIndicator("CAC".equalsIgnoreCase(flightInBrlResponse.getAcceptanceStatus()) ? "Y" : "N");
			paxInfoType.setHasCheckedBaggage(flightInBrlResponse.isHasCheckedBaggage());
			PersonalInfoType personInfoType = new PersonalInfoType();
			SimpleDateFormat sdf = DateUtil.getDateFormat(DateUtil.DATE_PATTERN_YYYY_MM_DD);
			if (cpr.getDateOfBirth() != null) {
				personInfoType.setDob(sdf.format(getDateFromOLCI(cpr.getDateOfBirth())));
			}
			personInfoType.setFirstName(cpr.getGivenName());
			personInfoType.setGender(cpr.getGender());
			personInfoType.setLastName(cpr.getFamilyName());
			personInfoType.setFamilyName(cpr.getFamilyName());
			paxInfoType.setPersonalInfo(personInfoType);
			paxInfoType.setDepartureGateInfo(flight.getDepartureGate());
			paxInfoType.setIsTransit(flight.getTransit());
			paxInfoType.setReverseCheckinCarrier(cpr.getReverseCheckinCarrier());

			if (flightInBrlResponse.getSeat() != null) {
				paxInfoType.setSeatNum(flightInBrlResponse.getSeat().getSeatNum());
				paxInfoType.setExtraLegRoomSeat(flightInBrlResponse.getSeat().isExtraLegRoomSeat());
				paxInfoType.setSeatPaid(flightInBrlResponse.getSeat().isPaid());
				paxInfoType.setCmLounge(flightInBrlResponse.getCmLounge());
			}
			paxInfoType.setSsrList(flightInBrlResponse.getSsrList());
			paxInfoType.setSkList(flightInBrlResponse.getSkList());
			
			populatePassengerDisplayOnlyFlag(journeyDCS, flightInBrlResponse, cpr, paxInfoType);
			paxlist.add(paxInfoType);
		}

		passengerCheckInInfo.setPassengerDetails(paxlist);
		return passengerCheckInInfo;
	}
	
	private void populatePassengerDisplayOnlyFlag( Journey journeyDCS, Flight flight, Passenger cpr, PassengerInfoType paxInfoType) {
		if(journeyDCS.isDisplayOnly()) {
			paxInfoType.setDisplayOnly(true);
		}
		
		if(flight.isDisplayOnly()) {
			paxInfoType.setDisplayOnly(true);
		}
		if(!journeyDCS.isDisplayOnly() && !flight.isDisplayOnly()) {
			paxInfoType.setDisplayOnly(cpr.isDisplayOnly());
		}
	}
	private FlightStatusCheckInInfo transFormJourneyDCSToFlightStatusCheckInInfo(Segment segment, Journey journeyDCS, String firstName, String lastName) {
		
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream().map(ConstantData::getValue).collect(Collectors.toList());
		FlightStatusCheckInInfo flightStatusCheckInInfo = new FlightStatusCheckInInfo();
		for (Passenger cpr : journeyDCS.getPassengers()) {
			Flight flight = getFlightFromBooking(segment, cpr);
			if (flight == null) {
				continue;
			}
			
			Map<String, String[]> paxMap = new HashMap<>();
			String[] nameArray = new String[] {cpr.getGivenName(),cpr.getFamilyName()};
			paxMap.put(cpr.getRloc(), nameArray);

				List<String> nameList = NameIdentficationUtil.nameIdentification(lastName, firstName, paxMap, nameTitleList, shortCompareSize);
				if(!CollectionUtils.isEmpty(nameList) && nameList.size() ==1) {
					List<FlightDetailsType> list = new ArrayList<>();
					FlightDetailsType flightDetailsType = new FlightDetailsType();
					flightDetailsType.setEticketNumber(flight.getAssocETicketNumber());
					// @Ben 09.01, logic changed for OLCI
					String generalFlightStatus = getGeneralFlightStatus(flight);
					if (generalFlightStatus != null) {
						flightDetailsType.setFltStatus(generalFlightStatus);
					} else {
						flightDetailsType.setFltStatus(getAcceptanceStatus(flight));
					}
					FlightInformationType flightInformationType = new FlightInformationType();
					flightInformationType.setCarrierCode(flight.getOperateCompany());
					FlightIdentifierType flightIdentifierType = new FlightIdentifierType();
					flightIdentifierType.setFltNo(flight.getOperateFlightNumber());
					flightInformationType.setFltIdnt(flightIdentifierType);
					flightInformationType.setFltOrig(flight.getOriginPort());
					flightInformationType.setFltDest(flight.getDestPort());
					flightDetailsType.setFltinfo(flightInformationType);
					list.add(flightDetailsType);
					FlightListType flt = new FlightListType();
					flt.setFltDetails(list);
					flightStatusCheckInInfo.setFlightListInfoType(flt);
				}
			 
		}
		return flightStatusCheckInInfo;
	}
	
	private String getGeneralFlightStatus(Flight flight) {
		if (flight == null) {
			return null;
		}
		if (flight.getFlightStatusList() == null) {
			return null;
		}
		if (flight.getFlightStatusList().isEmpty()) {
			return null;
		}
		for (FlightStatusInfo info : flight.getFlightStatusList()) {
			if ("GN".equals(info.getIndicator())) {
				return info.getAction();
			}
		}
		return null;
	}
	
	private String getAcceptanceStatus(Flight flight) {
		if (flight == null) {
			return null;
		}
		if (flight.getFlightStatusList() == null) {
			return null;
		}
		if (flight.getFlightStatusList().isEmpty()) {
			return null;
		}
		for (FlightStatusInfo info : flight.getFlightStatusList()) {
			if ("AC".equals(info.getIndicator())) {
				return info.getAction();
			}
		}
		return null;
	}
	
	private Date getDateFromOLCI(String olciDate) {
		try {
			return DateUtil.getStrToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD,olciDate);
		} catch (Exception e) {
			logger.warn("Date format error from OLCI: " + olciDate, e);
			return null;
		}
	}
	
	private Date getDateTimeFromOLCI(String olciDateTime) {
		try {
			return DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,olciDateTime);
		} catch (Exception e) {
			logger.warn("DateTime format error from OLCI: " + olciDateTime, e);
			return null;
		}
	}
	
	private void setDisplayOnlyInfo(Journey journey) {
		List<Passenger> passengers = journey.getPassengers();
		for (Passenger passenger : passengers) {
			passenger.setDisplayOnly(JourneyUtil.isDisplayOnly(passenger));
			populateOpenCloseTimeInfo(passenger, journey.isBetweenChekinFlightCloseTime());
			if (passenger.getMiceDetails() != null && JourneyUtil.isMiceNoSeatAssign(passenger)) {
					passenger.setDisplayOnly(true);

			}
			
			if(passenger.isInhibitAcceptanceByComment()){
				passenger.setDisplayOnly(true);
			}
			
			populateDisplayInfoForAllFlights(passenger);
			
			List<Flight> flights = getFlightsByCid(passenger.getUniqueCustomerId(), journey);
			for (Flight flight : flights) {
				if (null != flight.getRevenueIntegrityStatus() && OneAConstants.REVENUE_INTEGRITY_CHECK_RESPONSE_STATUSCODE_F
						.equals(flight.getRevenueIntegrityStatus().getStatusCode())) {
					journey.setDisplayOnly(true);
				}

			}
		}
	}
	
	
	private void populateDisplayInfoForAllFlights(Passenger passenger) {
		List<Flight> allFlights = JourneyUtil.getAllFlights(passenger);
		for (Flight flight : allFlights) {
			if (JourneyUtil.isDisplayOnly(flight)) {
				flight.setDisplayOnly(true);
			}
		}
	}
	/**
	 * get flights by cid
	 * @param uniqueCustomerId
	 * @param journey
	 * @return
	 */
	private static List<Flight> getFlightsByCid(String uniqueCustomerId,Journey journey){
		
		Passenger passenger = journey.getPassengers().stream().filter(pax->pax.getUniqueCustomerId().equals(uniqueCustomerId)).findFirst().orElse(null);
		if(null != passenger){
			return passenger.getFlights();
		}
		
		return Collections.emptyList();
		
	}
	
	/** 
	 * check Open Close Time Info
	 * @param passenger
	 * @param passengerDTO
	 * @param betweenChekinFlightCloseTime
	 */
	private static void populateOpenCloseTimeInfo(Passenger passenger, boolean betweenChekinFlightCloseTime){
		if(betweenChekinFlightCloseTime && !JourneyUtil.isAnyCheckInAccepted(passenger)){
			passenger.setDisplayOnly(true);
		}
	}


	private static CancelCheckInRequestDTO bulidCancelCheckInRequestDTO(JourneyDTO journeyDTO){
		CancelCheckInRequestDTO cancelCheckInRequestDTO =new CancelCheckInRequestDTO();
		List<String> uniqueCustomerIds =new ArrayList<>();
		if(!CollectionUtils.isEmpty(journeyDTO.getPassengers())){
			uniqueCustomerIds = journeyDTO.getPassengers().stream().filter(passenger ->
				passenger.isCheckInAccepted() && (CollectionUtils.isEmpty(passenger.getErrors())||(!CollectionUtils.isEmpty(passenger.getErrors())
						&& passenger.getErrors().stream().anyMatch(errorInfo ->
						!OLCIconstant.NOT_CANCEL_CHECKIN_ERRORCODE .contains(errorInfo.getErrorCode()))))
			).map(PassengerDTO::getUniqueCustomerId).collect(Collectors.toList());
		}
		if(!CollectionUtils.isEmpty(uniqueCustomerIds)){
			cancelCheckInRequestDTO.setJourneyId(journeyDTO.getJourneyId());
			cancelCheckInRequestDTO.setUniqueCustomerIds(uniqueCustomerIds);
		}
		return  cancelCheckInRequestDTO;
	}

	private boolean cancelEligibilityCheck(JourneyDTO journeyDTO){
		if(!CollectionUtils.isEmpty(journeyDTO.getPassengers())){
			return  journeyDTO.getPassengers().stream().anyMatch(passenger ->
					passenger.isCheckInAccepted() && (CollectionUtils.isEmpty(passenger.getErrors()) || (!CollectionUtils.isEmpty(passenger.getErrors())
							&& passenger.getErrors().stream().anyMatch(errorInfo ->
							!OLCIconstant.NOT_CANCEL_CHECKIN_ERRORCODE .contains(errorInfo.getErrorCode()))))
			);
		}
		return false;
	}

	private boolean checkCancelCheckInResponse(CancelCheckInResponseDTO cancelCheckInResponseDTO){
     if(cancelCheckInResponseDTO!=null && cancelCheckInResponseDTO.getJourney()!=null){
		return !CollectionUtils.isEmpty(cancelCheckInResponseDTO.getJourney().getPassengers()) &&
				cancelCheckInResponseDTO.getJourney().getPassengers().stream().allMatch(passengerDTO ->
						 !passengerDTO.isCheckInAccepted() && CollectionUtils.isEmpty(passengerDTO.getErrors()));
	 }
      return  false;
	}
	
	@Override
	public void sendJourneyBP(List<String> acceptSegmentIds, RetrievePnrBooking retrievePnrBooking, String rloc, String givenName, String familyName) {
		ResponseEntity<NonMemberLoginResponseDTO> responseEntity= olciConnector.nonMemberLoginWithOLCI(rloc,givenName,familyName);

		NonMemberLoginResponseDTO response = responseEntity.getBody();
		if (response == null || CollectionUtils.isEmpty(response.getJourneys())) {
			logger.warn(String.format("send journey boarding pass failed! rloc: %s, givenName: %s, familyName:%s",rloc,givenName,familyName));
			return;
		}
		
		response.getJourneys().forEach(journey -> {
			if (journey != null) {
				String journeyId = journey.getJourneyId();
				journey.getPassengers().stream().flatMap(passenger -> passenger.getFlights().stream())
						.forEach(flight -> sendAcceptedFlightBP(acceptSegmentIds, retrievePnrBooking, flight, journeyId));
			}
		});
	}
	
	private void sendAcceptedFlightBP(List<String> acceptSegmentIds, RetrievePnrBooking retrievePnrBooking, FlightDTO flight, String journeyId) {
		// check if the flight has accepted rebooking
		for(String acceptSegmentId : acceptSegmentIds) {
			for(RetrievePnrSegment segment : retrievePnrBooking.getSegments()) {
				boolean companyCodeMatch = StringUtils.isNotEmpty(segment.getPnrOperateCompany()) ?
						( segment.getPnrOperateCompany().equalsIgnoreCase(flight.getOperateCompany()) || segment.getPnrOperateCompany().equalsIgnoreCase(flight.getMarketingCompany())) :
						( segment.getMarketCompany().equalsIgnoreCase(flight.getOperateCompany()) || segment.getMarketCompany().equalsIgnoreCase(flight.getMarketingCompany()));
						
				boolean flightNumMatch = StringUtils.isNotEmpty(segment.getPnrOperateSegmentNumber()) ?
						( segment.getPnrOperateSegmentNumber().equalsIgnoreCase(flight.getOperateFlightNumber()) || segment.getPnrOperateSegmentNumber().equalsIgnoreCase(flight.getMarketFlightNumber())) :
							( segment.getMarketSegmentNumber().equalsIgnoreCase(flight.getOperateFlightNumber()) || segment.getMarketSegmentNumber().equalsIgnoreCase(flight.getMarketFlightNumber()));
				
				if(StringUtils.isNotEmpty(acceptSegmentId) && acceptSegmentId.equals(segment.getSegmentID())
					&& segment.getDepartureTime().getPnrTime().equalsIgnoreCase(flight.getDepartureTime().getCprScheduledTime())
					&& segment.getArrivalTime().getPnrTime().equalsIgnoreCase(flight.getArrivalTime().getCprScheduledTime())
					&& segment.getOriginPort().equalsIgnoreCase(flight.getOriginPort())
					&& segment.getDestPort().equalsIgnoreCase(flight.getDestPort())
					&& companyCodeMatch
					&& flightNumMatch
				) {
					String did = flight.getProductIdentifierDID();
					SendBPRequestDTO sendBPRequestDTO = new SendBPRequestDTO();
					sendBPRequestDTO.setDid(did);
					sendBPRequestDTO.setJourneyId(journeyId);
					sendBPRequestDTO.setSendEmail(true);
					sendBPRequestDTO.setSendSms(false);
					
					// TODO add async handling
					logger.info("call OLCI to send boarding pass after rebook with did:["+did+"] and journey id:["+journeyId+"]");
					olciClient.sendBPWithOLCI(sendBPRequestDTO, retrievePnrBooking.getOneARloc(), null);
				}
			}
		}
	}

}
