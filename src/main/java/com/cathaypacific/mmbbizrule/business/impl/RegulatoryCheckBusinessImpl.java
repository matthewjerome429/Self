package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.RegulatoryCheckBusiness;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.RegulatoryCountryDAO;
import com.cathaypacific.mmbbizrule.db.model.RegulatoryCountryMapping;
import com.cathaypacific.mmbbizrule.dto.request.checkin.regulatorycheck.RegcheckRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckCprJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckError;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckPassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckResponseDTO;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO;
import com.cathaypacific.mmbbizrule.v2.handler.CacheHelper;
import com.cathaypacific.olciconsumer.model.request.PassengerInfoDTO;
import com.cathaypacific.olciconsumer.model.request.regulatorycheck.RegulatoryCheckRequestDTO;
import com.cathaypacific.olciconsumer.model.response.AdcMessageKey;
import com.cathaypacific.olciconsumer.model.response.ErrorInfo;
import com.cathaypacific.olciconsumer.model.response.FlightDTO;
import com.cathaypacific.olciconsumer.model.response.PassengerDTO;
import com.cathaypacific.olciconsumer.model.response.regulatorycheck.RegulatoryCheckResponseDTO;
import com.cathaypacific.olciconsumer.service.client.impl.OlciClientImpl;

@Service
public class RegulatoryCheckBusinessImpl implements RegulatoryCheckBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(RegulatoryCheckBusinessImpl.class);
	
	@Autowired
	private OlciClientImpl olciClient;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private FlightBookingConverterHelper flightBookingConverterHelper;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private CacheHelper cacheHelper;
	
	@Autowired
	private RegulatoryCountryDAO regulatoryCountryDAO;
	
	@Value("#{'${olci.error.type.interactive.nonVisa}'.split(',')}")
	private List<String> regNonVisaTypeList;
	
	@Value("#{'${olci.error.type.interactive.visa}'.split(',')}")
	private List<String> regVisaTypeList;

	
	@Override
	public RegCheckResponseDTO regulatoryCheck(RegcheckRequestDTO requestDTO,LoginInfo loginInfo) throws BusinessBaseException {
		
		saveJourneyId(requestDTO.getJourneyId(), requestDTO.getRloc());
		
		/** Get RetrievePnrBooking */
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		
		Booking olssBooking = flightBookingConverterHelper.getFlightBooking(retrievePnrBooking, loginInfo, buildBookingRequiredForRegulatoryCheck(), true);
		Journey jounrey = Optional.ofNullable(olssBooking.getCprJourneys()).orElse(Collections.emptyList()).stream()
				.filter(journey->StringUtils.equals(requestDTO.getJourneyId(), journey.getJourneyId())).findFirst().orElse(null);
		if(jounrey == null){
			throw new ExpectedException(String.format("Cannot find request journy, please check the Journey Id[%s].",requestDTO.getJourneyId()), new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		RegulatoryCheckRequestDTO regulatoryRequest = buildRegulatoryCheckRequest(requestDTO,jounrey);
		if(CollectionUtils.isEmpty(regulatoryRequest.getPassengers())){
			throw new ExpectedException(String.format("Cannot find valid passenger to do reg check, may be all the passnger has been checked in,booking [%s]",requestDTO.getRloc()), new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		//build request of regulatory check
		ResponseEntity<RegulatoryCheckResponseDTO>  responseEntity = olciClient.regulatorycheck(regulatoryRequest, retrievePnrBooking.getOneARloc(), null);
		RegulatoryCheckResponseDTO regulatoryCheckResponse = responseEntity.getBody();
		//Check response
		checkOlciRegulatorycheckResult(regulatoryCheckResponse);
		
		// get adc message from reg check response
		List<AdcMessage> adcMessageList = buildAdcMessageForCache(regulatoryCheckResponse.getPassengers(), jounrey);
		
		//store the adc message to cache
		storeAdcMessageToCache(adcMessageList, requestDTO.getRloc());

		//build the error to a map
		Map<String,List<ErrorInfo>> regInterActiveErrorMapping = buildErrorToMapModel(regulatoryCheckResponse.getPassengers(), olssBooking);
		
		//store inter active error to cache
		storeInterActiveErrorToCache(regInterActiveErrorMapping, requestDTO.getRloc());
		
		return buildResponseDto(regInterActiveErrorMapping, adcMessageList, jounrey);
		
	}
	/**
	 * Check OLCI reg response, throw exception if only error in the response
	 * @param regulatoryCheckResponse
	 * @throws ExpectedException
	 */
	private void checkOlciRegulatorycheckResult(RegulatoryCheckResponseDTO regulatoryCheckResponse) throws ExpectedException{
		// check the error if cannot get passenger info from OLCI
		if(CollectionUtils.isEmpty(regulatoryCheckResponse.getPassengers()) && CollectionUtils.isNotEmpty(regulatoryCheckResponse.getErrors())){
			com.cathaypacific.mbcommon.dto.error.ErrorInfo error = new com.cathaypacific.mbcommon.dto.error.ErrorInfo();
			error.setErrorCode(regulatoryCheckResponse.getErrors().get(0).getErrorCode());
			error.setType(PnrCprMergeHelper.covertOlciErrorTypeToMmb(regulatoryCheckResponse.getErrors().get(0).getType()));
			throw new ExpectedException(String.format("Invoke OLCI to reg check failed, return error:%s", error.getErrorCode()), error); 
		}
	}
	/**
	 * Build response dto
	 * @param regInterActiveErrorMapping
	 * @param adcMessageList
	 * @param jounrey
	 * @return
	 */
	private RegCheckResponseDTO buildResponseDto(Map<String,List<ErrorInfo>> regInterActiveErrorMapping, List<AdcMessage> adcMessageList, Journey jounrey){
		RegCheckResponseDTO result  = new RegCheckResponseDTO();
		result.setCprJourney(buildRegCheckCprJourneyDTO(regInterActiveErrorMapping,
				adcMessageList, jounrey));
		return result;
	}
	/**
	 * Store journey ID
	 * 
	 * @param journeyId
	 * @param rloc
	 */
	private void saveJourneyId(String journeyId, String rloc) {
		
		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.CPR_JOURNEY_ID, rloc, journeyId);
	}
	/**
	 * @param regInterActiveErrorMapping
	 * @param adcMessageList
	 * @param jounrey
	 * @return
	 */
	@Override
	public RegCheckCprJourneyDTO buildRegCheckCprJourneyDTO(Map<String, List<ErrorInfo>> regInterActiveErrorMapping,
			List<AdcMessage> adcMessageList, Journey jounrey) {
		if(jounrey == null || CollectionUtils.isEmpty(jounrey.getPassengerSegments())) {
			return null;
		}
		RegCheckCprJourneyDTO regCheckCprJourney = new RegCheckCprJourneyDTO();
		regCheckCprJourney.setJourneyId(jounrey.getJourneyId());
		for (CprJourneyPassengerSegment cprJourneyPassengerSegment : jounrey.getPassengerSegments()) {
			RegCheckPassengerSegmentDTO regCheckpaxSeg = new RegCheckPassengerSegmentDTO();
			regCheckCprJourney.addPassengerSegments(regCheckpaxSeg);
			regCheckpaxSeg.setCprProductIdentifierDID(cprJourneyPassengerSegment.getCprProductIdentifierDID());
			regCheckpaxSeg.setSegmentId(cprJourneyPassengerSegment.getSegmentId());
			regCheckpaxSeg.setCprUniqueCustomerId(cprJourneyPassengerSegment.getCprUniqueCustomerId());
			regCheckpaxSeg.setPassengerId(cprJourneyPassengerSegment.getPassengerId());
			//adc message 
			if(CollectionUtils.isNotEmpty(adcMessageList)){
				regCheckpaxSeg.setAdcMessages(adcMessageList.stream()
						.filter(adc -> StringUtils.equals(adc.getCprProductIdentifierDID(),cprJourneyPassengerSegment.getCprProductIdentifierDID()))
						.map(adc -> convertAdcMessageModel(adc)).collect(Collectors.toList()));
			}
			//interactive error
			if(regInterActiveErrorMapping!=null && regInterActiveErrorMapping.containsKey(cprJourneyPassengerSegment.getCprProductIdentifierDID())){
				List<ErrorInfo> interactiveError = regInterActiveErrorMapping.get(cprJourneyPassengerSegment.getCprProductIdentifierDID());
				if(CollectionUtils.isNotEmpty(interactiveError)){
					regCheckCprJourney.setInterActiveError(true);
					interactiveError.forEach(error->{//all the errors in this list should be interactive error becouse do filter when build 
						 RegCheckError regError = new RegCheckError();
						 regError.setCode(error.getErrorCode());
						 regError.setFieldName(error.getFieldName());
						 regError.setType(PnrCprMergeHelper.covertOlciErrorTypeToMmb(error.getType()));
						 regError.setInterActiveError(true);
						 regError.setVisaRelated(regVisaTypeList.contains(error.getFieldName()));
						 regCheckpaxSeg.addError(regError);
					});
				}
			}
		}
		return regCheckCprJourney;
	}
	/**
	 * convert adc message to web model
	 * @param adcMessage
	 * @return
	 */
	private AdcMessageDTO convertAdcMessageModel(AdcMessage adcMessage){
		
		AdcMessageDTO adcMessageDto =  new AdcMessageDTO();
		adcMessageDto.setAdcCprMessage(adcMessage.getAdcCprMessage());
		adcMessageDto.setAdcMessageKeys(adcMessage.getAdcMessageKeys());
		return adcMessageDto;
	}

	@Override
	public Map<String,List<ErrorInfo>> buildErrorToMapModel(List<PassengerDTO> passengers,Booking olssBooking){
		
		//get all passenger level errors
		List<String> regTypeList = passengers.stream()
				.filter(pax -> CollectionUtils.isNotEmpty(pax.getErrors())).flatMap(pax -> pax.getErrors().stream())
				.filter(error -> isRegInterActiveError(error)).map(ErrorInfo::getFieldName)
				.collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(regTypeList)){
			return null;
		}
		
		
		Map<String, String> portCountryCodeMap = new HashMap<>();
		
		//port country code  map
		for(Segment segment : olssBooking.getSegments()) {
			portCountryCodeMap.put(segment.getDestPort(), convertIso2CountryCodeToIso3CountryCode(segment.getDestCountry()));
			portCountryCodeMap.put(segment.getOriginPort(), convertIso2CountryCodeToIso3CountryCode(segment.getOriginCountry()));
		}
		
		
		// get rules from DB
		List<RegulatoryCountryMapping>  regCountryList =  regulatoryCountryDAO.getByAppCodeAndTypes(MMBConstants.APP_CODE, regTypeList);
		
		//group the reg types and country code to a map
		Map<String,List<String>> regCountryCodeMapping = regCountryList.stream().collect(Collectors.groupingBy(RegulatoryCountryMapping::getRegType,Collectors.mapping(RegulatoryCountryMapping::getCountryCode, Collectors.toList())));
		
		Map<String,List<ErrorInfo>> didInterActiveErrorMap = new HashMap<>();
		//apply the error to the level
		for (PassengerDTO regulatoryPassenger : passengers) {
			if(CollectionUtils.isEmpty(regulatoryPassenger.getErrors())){
				continue;
			}
			
			for (ErrorInfo regulatoryError : regulatoryPassenger.getErrors()) {
				if(!isRegInterActiveError(regulatoryError)){
					continue;
				}
				buildToErrorMapping(regulatoryError, regulatoryPassenger, regCountryCodeMapping.get(regulatoryError.getFieldName()), portCountryCodeMap, didInterActiveErrorMap);
			}
			
		}
		return didInterActiveErrorMap;
	}
	
	/**
	 * convertIso2CountryCodeToIso3CountryCode
	 * @param countryCode
	 * @return
	 */
	private String convertIso2CountryCodeToIso3CountryCode(String countryCode) {
		Locale locale = new Locale("", countryCode);
		return locale.getISO3Country();
	}
	/**
	 * add the error to error mapping
	 * @param regulatoryError
	 * @param regulatoryPassenger
	 * @param segments
	 * @param regApplyCountryCodeList
	 * @param portCountryCodeMap
	 * @param resultMap
	 */
	private void buildToErrorMapping(ErrorInfo regulatoryError,PassengerDTO regulatoryPassenger,
			List<String> regApplyCountryCodeList, Map<String, String> portCountryCodeMap,Map<String,List<ErrorInfo>> resultMap ){
		
		boolean matchedOD = false;
		//first time add, only add the matched OD segment
		for(FlightDTO regulatoryFlight:regulatoryPassenger.getFlights()){
			if(CollectionUtils.isNotEmpty(regApplyCountryCodeList) 
					&& regApplyCountryCodeList.contains(TBConstants.TB_REGULATORY_COUNTRY_MAPPING_COUNTRYCODE_WILDCARD)
					|| regApplyCountryCodeList.contains(portCountryCodeMap.get(regulatoryFlight.getDestPort()))
					|| regApplyCountryCodeList.contains(portCountryCodeMap.get(regulatoryFlight.getOriginPort()))){
					matchedOD = true;
					resultMap.computeIfAbsent(regulatoryFlight.getProductIdentifierDID(), v->new ArrayList<>()).add(regulatoryError);
					}
		}
		
		for (FlightDTO regulatoryFlight : regulatoryPassenger.getFlights()) {
			//second time add, add to all segment if matched OD is false
			if (!matchedOD) {
				resultMap.computeIfAbsent(regulatoryFlight.getProductIdentifierDID(), v->new ArrayList<>())
						.add(regulatoryError);
			}
			
			// add the flight level interactive error
			if(CollectionUtils.isNotEmpty(regulatoryFlight.getErrors())){
				for (ErrorInfo regulatoryFlightLevleError : regulatoryFlight.getErrors()) {
					if(isRegInterActiveError(regulatoryFlightLevleError)){
						resultMap.computeIfAbsent(regulatoryFlight.getProductIdentifierDID(), v->new ArrayList<>())
						.add(regulatoryFlightLevleError);
					}
					
					
				}
			}
		}
	}
	
	//private String 
	/**
	 * check if it is inter active error
	 * @param errorInfo
	 * @return
	 */
	private boolean isRegInterActiveError(com.cathaypacific.olciconsumer.model.response.ErrorInfo errorInfo){
		return !StringUtils.isEmpty(errorInfo.getFieldName())&& (regNonVisaTypeList.contains(errorInfo.getFieldName())||regVisaTypeList.contains(errorInfo.getFieldName()));
		
	}
	/**
	 * store the adc message to redis
	 * @param adcMessageList
	 * @param rloc
	 */
	private void storeAdcMessageToCache(List<AdcMessage> adcMessageList, String rloc) {

		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.ADC_MESSAGE, rloc, adcMessageList);

	}
	/**
	 * store Inter Active Error To Cache
	 * @param map
	 * @param rloc
	 */
	private void storeInterActiveErrorToCache(Map<String,List<ErrorInfo>> map, String rloc) {
		
		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.INTER_ACTIVE_ERROR, rloc, map);
	}
	
	@Override
	public List<AdcMessage> buildAdcMessageForCache(List<PassengerDTO> passengers,Journey jounrey){
		if(CollectionUtils.isEmpty(passengers)){
			return null;
		}
	
		List<AdcMessage> adcList = null;
		
		for (PassengerDTO cprPassenger : passengers) {
			for (FlightDTO flight : cprPassenger.getFlights()) {
				if(CollectionUtils.isEmpty(flight.getAdcMessages())){
					continue;
				}
				if(adcList==null ){
					adcList = new ArrayList<>();
				}
				List<AdcMessage> singFlightAdcList = flight.getAdcMessages().stream().map(adcMessage->{
					return convertToAdcCacheModel(adcMessage, flight.getProductIdentifierDID(),jounrey);
				}).filter(adcMessage->adcMessage!=null).collect(Collectors.toList());
				adcList.addAll(singFlightAdcList);
			}
			 
		}
		//regulatoryCheckResponse.getPassengers()
		return adcList;
	}
	/**
	 * Convert to Adc message cahce model 
	 * @param cprAdcMessage
	 * @param uci
	 * @param did
	 * @return
	 */
	private AdcMessage convertToAdcCacheModel(com.cathaypacific.olciconsumer.model.response.AdcMessageDTO cprAdcMessage,String did,Journey jounrey){
		if(cprAdcMessage == null){
			return null;
		}
		AdcMessage cacheAdcMessage = new AdcMessage();
		cacheAdcMessage.setAdcCprMessage(cprAdcMessage.getAdcCprMessage());
		CprJourneyPassengerSegment cprPaxSeg = jounrey.getPassengerSegments().stream().filter(ps->StringUtils.equals(ps.getCprProductIdentifierDID(),did)).findFirst().orElse(null);
		if(cprPaxSeg == null){
			logger.warn(String.format("cannot find matched flight of the adc message, will ignore it,DID[%s], Message[%s]", did,cprAdcMessage.getAdcCprMessage()));
		} else {
			cacheAdcMessage.setCprProductIdentifierDID(cprPaxSeg.getCprProductIdentifierDID());
			cacheAdcMessage.setCprUniqueCustomerId(cprPaxSeg.getCprUniqueCustomerId());
			cacheAdcMessage.setSegmentId(cprPaxSeg.getSegmentId());
			cacheAdcMessage.setPassengerId(cprPaxSeg.getPassengerId());			
		}
		if(cprAdcMessage.getAdcMessageKeys()!=null){
			cacheAdcMessage.setAdcMessageKeys(cprAdcMessage.getAdcMessageKeys().stream().map(AdcMessageKey::getMessageCode).collect(Collectors.toList()));
		}
	 
		return cacheAdcMessage;
		
	}
	/**
	 * Build regulatory check request model
	 * @param requestDTO
	 * @param olssBooking
	 * @return
	 */
	private RegulatoryCheckRequestDTO buildRegulatoryCheckRequest(RegcheckRequestDTO requestDTO,Journey journey){
		
		RegulatoryCheckRequestDTO regulatoryCheckRequest = new RegulatoryCheckRequestDTO();
		regulatoryCheckRequest.setJourneyId(requestDTO.getJourneyId());
	 
		if(journey == null){
			return regulatoryCheckRequest;
		}
		//set passenger list
		List<PassengerInfoDTO> passengerInfoList = new ArrayList<>();
		
		regulatoryCheckRequest.setPassengers(passengerInfoList);
		//from request
		if(!CollectionUtils.isEmpty(requestDTO.getPassengerIds())){
			for (String passengerId : requestDTO.getPassengerIds()) {
				String uci = journey.getPassengers().stream().filter(pax->StringUtils.equals(pax.getPassengerId(), passengerId) && !pax.isCheckedIn()).map(CprJourneyPassenger::getCprUniqueCustomerId).findFirst().orElse(null);
				if(StringUtils.isAllEmpty(uci)){
					continue;
				}
				PassengerInfoDTO passengerInfo = new PassengerInfoDTO();
				passengerInfo.setUniqueCustomerId(uci);
				passengerInfoList.add(passengerInfo);
			}
		// all non checked in passenger
		}else{
			journey.getPassengers().stream().filter(pax->!pax.isCheckedIn()).forEach(pax->{
				PassengerInfoDTO passengerInfo = new PassengerInfoDTO();
				passengerInfo.setUniqueCustomerId(pax.getCprUniqueCustomerId());
				passengerInfoList.add(passengerInfo);
			});
		}
		
		return regulatoryCheckRequest;
		
	}
	
	/**
	 * Build required info
	 * @return
	 */
	private BookingBuildRequired buildBookingRequiredForRegulatoryCheck(){
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setRtfs(true);
		required.setCprCheck(true);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setMemberAward(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setCountryOfResidence(false);
		required.setSeatSelection(false);
		required.setUseCprSession(true);
		return required;
	}


	@Override
	public List<com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO> getAdcMessageFromCache(String rloc,
			LoginInfo loginInfo) {
		
		List<AdcMessage> adcMessages = cacheHelper.getAdcMessageFromCache(rloc);
		if (CollectionUtils.isEmpty(adcMessages)) {
			return null;
		}
		
		List<com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO> resultList = new ArrayList<>();
		for (AdcMessage message: adcMessages) {			
			com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO adcMessageDTO = new com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO();
			adcMessageDTO.setAdcCprMessage(message.getAdcCprMessage());
			adcMessageDTO.setAdcMessageKeys(message.getAdcMessageKeys());
			adcMessageDTO.setProductIdentifierDID(message.getCprProductIdentifierDID());
			adcMessageDTO.setUniqueCustomerId(message.getCprUniqueCustomerId());
			resultList.add(adcMessageDTO);
		}
		return resultList;
	}

}
