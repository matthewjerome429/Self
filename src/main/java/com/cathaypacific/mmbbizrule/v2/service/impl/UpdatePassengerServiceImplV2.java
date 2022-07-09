package com.cathaypacific.mmbbizrule.v2.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.RegulatoryCheckBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OLCIConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.controller.RetrievePnrController;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckPassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.service.AddPassengerInfoService;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneyPassengerSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneySegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateAdultSegmentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateBasicSegmentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateCountryOfResidenceDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateDestinationAddressDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateDestinationDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateEmailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateEmergencyContactDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateFFPInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateInfantDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePhoneInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateTravelDocDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateTsDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails.PassengerDetailsResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.CacheHelper;
import com.cathaypacific.mmbbizrule.v2.oneaservice.UpdatePassengerRequestBuilderV2;
import com.cathaypacific.mmbbizrule.v2.service.UpdatePassengerServiceV2;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateAddressDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateDestinationDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmailDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmailDTO.Mode;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateFlightDetailDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateMobileDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengerDetailDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengersDetailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateResidenceDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateTravelDocumentDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateTsDTO;
import com.cathaypacific.olciconsumer.model.response.DateDTO;
import com.cathaypacific.olciconsumer.model.response.PassengerDTO;
import com.cathaypacific.olciconsumer.model.response.updatepassenger.UpdatePassengersDetailResponseDTO;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.google.gson.Gson;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:42:46 PM
 * @version V1.0
 */
@Service
public class UpdatePassengerServiceImplV2 implements UpdatePassengerServiceV2{
	
	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrController.class);
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Autowired
	private AddPassengerInfoService addPassengerInfoService;
	
	@Autowired
	private OLCIServiceV2 olciServiceV2;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private RegulatoryCheckBusiness regulatoryCheckBusiness;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private CacheHelper cacheHelper;
	
	@Value("#{'${olci.error.type.interactive.nonVisa}'.split(',')}")
	private List<String> regNonVisaTypeList;
	
	@Value("#{'${olci.error.type.interactive.visa}'.split(',')}")
	private List<String> regVisaTypeList;
	
	@Override
	public RetrievePnrBooking updatePassenger(UpdatePassengerDetailsRequestDTOV2 requestDTO, RetrievePnrBooking pnrBooking, Booking booking,
			TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus, PassengerDetailsResponseDTOV2 response) throws BusinessBaseException{
		logger.debug(String.format("request json:{%s}",new Gson().toJson(requestDTO)));
		
		RetrievePnrBooking updateResult = null;
		//Update to CPR
		if (checkUpdateCPR(requestDTO, booking) && !checkNeedUpdateFFP(requestDTO, booking.getSegments())) {	
			UpdatePassengersDetailResponseDTO olciResponse = olciServiceV2.updatePassengerDetails(buildCPRUpdatePassenger(requestDTO, booking), booking.getOneARloc(), null);
			//OLSS-6822 pass the olci error code to front-end if the error type is S or D 
			if (olciResponse.getErrors() != null) {
				handleOLCIErrors(requestDTO.getPassengerId(), response, olciResponse);
			} 
//				handleOLCIPassengerLevelErrors(response, requestDTO, olciResponse.getPassengers(), booking);
			if(olciResponse.getPassengers() != null){
				handleRegulatoryCheck(olciResponse, requestDTO, booking, response);
			}
			updateResult = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		} else {			
			// if need to transfer FFP from customer level to product level
			boolean needTransferFFP = checkNeedUpdateFFP(requestDTO, booking.getSegments())
					&& checkPaxOnlyHasCusLevelFfpInBooking(requestDTO.getPassengerId(), booking);
			
			boolean needTransferTravelDoc = checkNeedUpdateTravelDoc(requestDTO, booking.getSegments())
					&& checkAnySegmentNeedTravelDocTransfer(requestDTO, booking);
			
			if (!needTransferFFP && !needTransferTravelDoc) { // if don't need to transfer FFP & travel doc, just update the info		
				updateResult = updateWithoutTransfer(requestDTO, pnrBooking, booking);
			} else { // if need to transfer, first update with transfer, if 1A error detected, then try to update without transfer, and then transfer for segments one by one
				// need to transfer FFP & travel doc from customer level to product level, set the flag
				ffpTransferStatus.setNeedTransfer(needTransferFFP);
				travelDocTransferStatus.setNeedTransfer(needTransferTravelDoc);
				updateResult = updateWithTransfer(requestDTO, pnrBooking, booking, ffpTransferStatus, travelDocTransferStatus);
			}
		}
		
		return updateResult;
	}
	
	/**
	 * handleRegulatoryCheck
	 * @param olciResponse
	 * @param requestDTO
	 * @param booking
	 * @param response
	 */
	private void handleRegulatoryCheck(UpdatePassengersDetailResponseDTO olciResponse,
			UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking, PassengerDetailsResponseDTOV2 response) {
		Journey journey = Optional.ofNullable(booking.getCprJourneys()).orElse(Collections.emptyList()).stream()
				.filter(jny->Objects.equals(requestDTO.getJourneyId(), jny.getJourneyId())).findFirst().orElse(null);
		if(journey == null || CollectionUtils.isEmpty(journey.getPassengerSegments()) || StringUtils.equalsIgnoreCase(requestDTO.getUpdateType(), MMBBizruleConstants.UPDATE_TYPE_CONTACT_INFO)) {
			return;
		}
		//build update passengerSegments
		List<CprJourneyPassengerSegment> cprPassengerSegments = journey.getPassengerSegments().stream().filter(ps -> anyMatch(ps, requestDTO)).collect(Collectors.toList());
		List<AdcMessage> adcMessages = regulatoryCheckBusiness.buildAdcMessageForCache(olciResponse.getPassengers(), journey);
		updateAdcMessagesToCache(adcMessages, requestDTO.getRloc(), cprPassengerSegments);
		Map<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> interactiveErrorMap = regulatoryCheckBusiness.buildErrorToMapModel(olciResponse.getPassengers(), booking);
		updateInteractiveErrorToCache(interactiveErrorMap, requestDTO.getRloc(), cprPassengerSegments);
		buildInteractiveErrorResponose(cacheHelper.getInteractiveErrorFromCache(requestDTO.getRloc()), cacheHelper.getAdcMessageFromCache(requestDTO.getRloc()), journey, response);
		// filter out errors if exist in regulatoryCheck 
		filterOutDuplicateErrors(response);
		
		// OLSS-7536 tagging
		try {
			tagging(booking, interactiveErrorMap);			
		} catch(Exception e) {
			logger.error("Tagging error!", e);
		}
	}

	/**
	 * OLSS-7536 tagging
	 * @param booking
	 * @param interactiveErrorMap
	 */
	private void tagging(Booking booking, Map<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> interactiveErrorMap) {
		if(CollectionUtils.isEmpty(interactiveErrorMap) || booking == null) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(!CollectionUtils.isEmpty(regVisaTypeList) && interactiveErrorMap.entrySet().stream().map(Entry::getValue)
				.filter(list -> !CollectionUtils.isEmpty(list)).flatMap(List::stream).anyMatch(e -> e != null && regVisaTypeList.contains(e.getFieldName()))) {
			sb.append("| VISA Related ");
		}
		
		if(!CollectionUtils.isEmpty(regNonVisaTypeList) && interactiveErrorMap.entrySet().stream().map(Entry::getValue)
				.filter(list -> !CollectionUtils.isEmpty(list)).flatMap(List::stream).anyMatch(e -> e != null && regNonVisaTypeList.contains(e.getFieldName()))) {
			sb.append("| NON VISA ");
		}
		
		logger.info(String.format("Travel Doc Update %s| Interactive Error | RLOC | %s", sb.toString(), booking.getOneARloc()), true);
	}

	/**
	 * any Match
	 * @param ps
	 * @param requestDTO
	 * @return
	 */
	private boolean anyMatch(CprJourneyPassengerSegment cprPassengerSegment, UpdatePassengerDetailsRequestDTOV2 requestDTO) {
		
		if(!CollectionUtils.isEmpty(requestDTO.getSegments()) && requestDTO.getSegments().stream()
				.anyMatch(seg -> Objects.equals(requestDTO.getPassengerId(), cprPassengerSegment.getPassengerId())
						&& Objects.equals(seg.getSegmentId(), cprPassengerSegment.getSegmentId()))) {
			return true;
		}
		return requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments()) &&
				requestDTO.getInfant().getSegments().stream().anyMatch(seg -> Objects.equals(requestDTO.getInfant().getPassengerId(), cprPassengerSegment.getPassengerId())
						&& Objects.equals(seg.getSegmentId(), cprPassengerSegment.getSegmentId()));
	}

	/**
	 * filter Out Duplicate Errors
	 * @param response
	 */
	private void filterOutDuplicateErrors(PassengerDetailsResponseDTOV2 response) {
		if(!CollectionUtils.isEmpty(response.getErrors()) && response.getRegulatoryCheck() != null && !CollectionUtils.isEmpty(response.getRegulatoryCheck().getPassengerSegments())) {
			List<ErrorInfo> list = response.getErrors().stream().filter(err -> !anyMatchInRegulatoryCheck(err, response.getRegulatoryCheck().getPassengerSegments())).collect(Collectors.toList());
			response.setErrors(list);
		}
	}

	/**
	 * any Match In Regulatory Check
	 * @param err
	 * @param passengerSegments
	 * @return
	 */
	private boolean anyMatchInRegulatoryCheck(ErrorInfo err, List<RegCheckPassengerSegmentDTO> passengerSegments) {
		for(RegCheckPassengerSegmentDTO  regPassengerSegment : passengerSegments) {
			if(!CollectionUtils.isEmpty(regPassengerSegment.getErrors()) && regPassengerSegment.getErrors().stream().anyMatch(error -> Objects.equals(err.getCode(), error.getCode()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * build Interactive Error Responose
	 * @param interactiveErrorMap
	 * @param adcMessages
	 * @param journey
	 * @param response
	 */
	private void buildInteractiveErrorResponose(
			Map<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> interactiveErrorMap,
			List<AdcMessage> adcMessages, Journey journey, PassengerDetailsResponseDTOV2 response) {
		response.setRegulatoryCheck(regulatoryCheckBusiness.buildRegCheckCprJourneyDTO(interactiveErrorMap, adcMessages, journey));
	}

	/**
	 * update Interactive Error To Cache
	 * @param map
	 * @param rloc
	 * @param cprPassengerSegments 
	 */
	private void updateInteractiveErrorToCache(Map<String,List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> map, String rloc, List<CprJourneyPassengerSegment> cprPassengerSegments) {
		Map<String,List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> interactiveErrorFromCache = cacheHelper.getInteractiveErrorFromCache(rloc);
		if(CollectionUtils.isEmpty(interactiveErrorFromCache)) {
			if (!CollectionUtils.isEmpty(map)) {
				mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.INTER_ACTIVE_ERROR, rloc, map);
			}
			return;
		}
		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.INTER_ACTIVE_ERROR, rloc, mergeInteractiveError(map, interactiveErrorFromCache, cprPassengerSegments));
	}
	
	/**
	 * merge Interactive Error
	 * @param map
	 * @param interactiveErrorFromCache
	 * @param cprPassengerSegments 
	 */
	private Map<String,List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> mergeInteractiveError(Map<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> map,
			Map<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> interactiveErrorFromCache, List<CprJourneyPassengerSegment> cprPassengerSegments) {
		for(CprJourneyPassengerSegment cprJourneyPassengerSegment : cprPassengerSegments) {
			interactiveErrorFromCache.remove(cprJourneyPassengerSegment.getCprProductIdentifierDID());
		}
		if(!CollectionUtils.isEmpty(map)) {
			for(Map.Entry<String, List<com.cathaypacific.olciconsumer.model.response.ErrorInfo>> entry : map.entrySet()) {
				interactiveErrorFromCache.put(entry.getKey(), entry.getValue());
			}
		}
		return interactiveErrorFromCache;
	}

	/**
	 * update AdcMessages To Cache
	 * @param adcMessages
	 * @param rloc
	 * @param cprPassengerSegments 
	 */
	private void updateAdcMessagesToCache(List<AdcMessage> adcMessages, String rloc, List<CprJourneyPassengerSegment> cprPassengerSegments) {
		List<AdcMessage> adcMessagesFromCache = cacheHelper.getAdcMessageFromCache(rloc);
		if (CollectionUtils.isEmpty(adcMessagesFromCache)) {
			if (!CollectionUtils.isEmpty(adcMessages)) {
				mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.ADC_MESSAGE, rloc, adcMessages);
			}
			return;
		}
		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.ADC_MESSAGE, rloc, mergeAdcMessages(adcMessages, adcMessagesFromCache, cprPassengerSegments));
	}

	/**
	 * merge AdcMessages
	 * @param adcMessages
	 * @param adcMessagesFromCache
	 * @param cprPassengerSegments 
	 * @return 
	 */
	private List<AdcMessage> mergeAdcMessages(List<AdcMessage> adcMessages, List<AdcMessage> adcMessagesFromCache, List<CprJourneyPassengerSegment> cprPassengerSegments) {
		if (!CollectionUtils.isEmpty(adcMessages)) {
			adcMessagesFromCache = adcMessagesFromCache.stream().filter(adcMsg -> cprPassengerSegments.stream().noneMatch(cprPS -> Objects.equals(adcMsg.getPassengerId(), cprPS.getPassengerId()) && Objects.equals(adcMsg.getCprUniqueCustomerId(), cprPS.getCprUniqueCustomerId()) 
						&& Objects.equals(adcMsg.getSegmentId(), cprPS.getSegmentId()) && Objects.equals(adcMsg.getCprProductIdentifierDID(), cprPS.getCprProductIdentifierDID()))).collect(Collectors.toList());
			for(AdcMessage adcMsg : adcMessages) {
				adcMessagesFromCache.add(adcMsg); 
			}
		}
		return adcMessagesFromCache;
	}

	private boolean checkUpdateCPR(UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking) {
		
		/** 1. if CPR journey is available(no matter journey is valid or not) and request only contains contact info, update to CPR
		 * 	This is because when journey is available, we would retrieve contact from CPR, so we should update contact to CPR in case PNR can't sync to CPR immediately
		 */
		if (onlyUpdateContact(requestDTO) && cprContactAvailable(requestDTO.getPassengerId(), booking)) {
			return true;
		}
		
		/*
		 *  2. If no valid CPR journey, do not need to update to CPR.
		 *  Valid journey:
		 *  (1) Returned from OLCI.
		 *  (2) Open to check in.
		 *  (3) Not checked in yet.
		 */
		Predicate<Journey> isValidCprJourney = jny -> {
			if (!jny.isOpenToCheckIn()) {
				return false;
			}
			return jny.getPassengerSegments().stream().filter(
					ps -> Objects.equals(ps.getPassengerId(), requestDTO.getPassengerId())
				).noneMatch(CprJourneyPassengerSegment::getCheckedIn);
		};
		if (BooleanUtils.isNotTrue(booking.isGotCpr()) ||
				CollectionUtils.isEmpty(booking.getCprJourneys()) ||
				booking.getCprJourneys().stream().noneMatch(isValidCprJourney)) {
			
			return false;
		}
		// 3. If request has journeyId and this journey is open to check in, update to CPR
		if (!StringUtils.isEmpty(requestDTO.getJourneyId())) {
			return booking.getCprJourneys().stream().anyMatch(journey -> journey.getJourneyId().equals(requestDTO.getJourneyId()) 
					&& journey.isOpenToCheckIn());
		}
		// 4. If request only has segmentId, first check this segment is open to check in, second check all this segment is in one journey.
		boolean hasRequestAdultSegment = !CollectionUtils.isEmpty(requestDTO.getSegments());
		boolean hasRequestInfantSegment = requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments());
		if (hasRequestAdultSegment || hasRequestInfantSegment) {
			List<UpdateBasicSegmentInfoDTOV2> requestSegments = new ArrayList<>();
			if (hasRequestAdultSegment) {
				requestSegments.addAll(requestDTO.getSegments());
			}
			if (hasRequestInfantSegment) {
				requestSegments.addAll(requestDTO.getInfant().getSegments());
			}
			List<String> requestSegmentIds = new ArrayList<>();
			for(UpdateBasicSegmentInfoDTOV2 segment: requestSegments) {
				requestSegmentIds.add(segment.getSegmentId());
				if (booking.getSegments().stream().anyMatch(bookingSegment -> bookingSegment.getSegmentID().equals(segment.getSegmentId())
						&& !bookingSegment.isOpenToCheckIn())) {
					return false;
				} 
			}
			
			List<String> journeyList = booking.getCprJourneys().stream().filter(journey -> journey.getSegments().stream()
					.anyMatch(segment -> requestSegmentIds.contains(segment.getSegmentId()))).map(Journey::getJourneyId).distinct().collect(Collectors.toList());
		
			// All segment in one journey.
			if(journeyList.size() == 1) {
				requestDTO.setJourneyId(journeyList.get(0));
			}
			return journeyList.size() == 1;
		}
		return true;
	}

	/**
	 * check there's only Contact that is needed to be updated
	 * @param requestDTO
	 * @return boolean
	 */
	private boolean onlyUpdateContact(UpdatePassengerDetailsRequestDTOV2 requestDTO) {
		// need to update contact
		boolean contactNeedUpdate = requestDTO.getEmail() != null || requestDTO.getPhoneInfo() != null;
		// don't need to update other info
		boolean otherInfoNoNeedUpdate = requestDTO.getDestination() == null && requestDTO.getEmergencyContact() == null
				&& isEmptyInfant(requestDTO.getInfant()) && requestDTO.getKtn() == null
				&& requestDTO.getRedress() == null
				&& (CollectionUtils.isEmpty(requestDTO.getSegments()) || requestDTO.getSegments().stream()
						.allMatch(seg -> seg.getCountryOfResidence() == null && seg.getFfpInfo() == null
								&& seg.getPrimaryTravelDoc() == null && seg.getSecondaryTravelDoc() == null));
		return contactNeedUpdate && otherInfoNoNeedUpdate;
	}
	
	/**
	 * check if the infant is empty
	 * @param infant
	 * @return
	 */
	private boolean isEmptyInfant(UpdateInfantDTOV2 infant) {
		return infant == null
				|| infant.getDestination() == null && infant.getKtn() == null && infant.getRedress() == null
						&& (CollectionUtils.isEmpty(infant.getSegments())
								|| infant.getSegments().stream().allMatch(seg -> seg.getCountryOfResidence() == null
										&& seg.getPrimaryTravelDoc() == null && seg.getSecondaryTravelDoc() == null));
	}

	/**
	 * 
	 * @param passengerId
	 * @param booking
	 * @return boolean
	 */
	private boolean cprContactAvailable(String passengerId, Booking booking) {
		if (StringUtils.isEmpty(passengerId) || booking == null || CollectionUtils.isEmpty(booking.getPassengers())) {
			return false;
		}
		
		Passenger passenger = booking.getPassengers().stream().filter(pax -> passengerId.equals(pax.getPassengerId())).findFirst().orElse(null);
		// if corresponding cprPassenger is found, means Contact is available from CPR(no matter the contact is empty or not)
		return passenger != null && passenger.isCorrespondingCprPassengerFound();
	}

	private void handleOLCIErrors(String passengerId, PassengerDetailsResponseDTOV2 response,
			UpdatePassengersDetailResponseDTO olciResponse) {
		List<ErrorInfo> errors = new ArrayList<>();
		for (com.cathaypacific.olciconsumer.model.response.ErrorInfo error : olciResponse.getErrors()) {
			if (OLCIConstants.CPR_ERROR_INFO_TYPE_S.equalsIgnoreCase(error.getType())
					|| OLCIConstants.CPR_ERROR_INFO_TYPE_D.equalsIgnoreCase(error.getType())) {
				ErrorInfo errorInfo = new ErrorInfo();
//				errorInfo.setCode(error.getErrorCode());
				errorInfo.setErrorCode(error.getErrorCode());
				errorInfo.setPassengerId(passengerId);
				errorInfo.setFieldName(error.getFieldName());
				if(OLCIConstants.CPR_ERROR_INFO_TYPE_S.equalsIgnoreCase(error.getType())){
					errorInfo.setType(ErrorTypeEnum.SYSERROR);
				}else if(OLCIConstants.CPR_ERROR_INFO_TYPE_D.equalsIgnoreCase(error.getType())){
					errorInfo.setType(ErrorTypeEnum.BUSERROR);
				}
				errors.add(errorInfo);
			}
		}
		response.setErrors(errors);
	}

	/**
	 * handle OLCI Passenger Level Errors
	 * @param response
	 * @param requestDTO 
	 * @param passengers
	 * @param booking
	 */
	private void handleOLCIPassengerLevelErrors(PassengerDetailsResponseDTOV2 response, UpdatePassengerDetailsRequestDTOV2 requestDTO, List<PassengerDTO> passengers,
			Booking booking) {
		if(CollectionUtils.isEmpty(booking.getCprJourneys())) {
			return;
		}
		Journey updateJourney = booking.getCprJourneys().stream().filter(journey -> journey.getJourneyId().equals(requestDTO.getJourneyId())).findFirst().orElse(null);
		if (updateJourney == null || CollectionUtils.isEmpty(updateJourney.getPassengers())) {
			return;
		}
		List<ErrorInfo> errors = new ArrayList<>();
		for (PassengerDTO passenger : passengers) {
			addErrors(updateJourney, errors, passenger);
		}
		response.setErrors(errors);
	}

	/**
	 * @param updateJourney
	 * @param errors
	 * @param passenger
	 */
	private void addErrors(Journey updateJourney, List<ErrorInfo> errors, PassengerDTO passenger) {
		if (!CollectionUtils.isEmpty(passenger.getErrors())) {
			CprJourneyPassenger cprJourneyPassenger = updateJourney.getPassengers().stream().filter(pax -> Objects.equals(passenger.getUniqueCustomerId(), pax.getCprUniqueCustomerId())).findFirst().orElse(null);
			if (cprJourneyPassenger == null) {
				return;
			}
			for(com.cathaypacific.olciconsumer.model.response.ErrorInfo error : passenger.getErrors()) {
				if(error.getErrorCode().contains(ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE) || error.getErrorCode().contains(ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
						|| error.getErrorCode().equalsIgnoreCase(OLCIConstants.WAR_ADC_FOUND) || error.getErrorCode().equalsIgnoreCase(OLCIConstants.ERR_ADC_FOUND) || error.getErrorCode().equalsIgnoreCase(OLCIConstants.WAR_ADC_BP_NOTALLOW)) {
					continue;
				}
				ErrorInfo errorInfo = new ErrorInfo();
				errorInfo.setCode(error.getErrorCode());
				errorInfo.setErrorCode(error.getErrorCode());
				errorInfo.setPassengerId(cprJourneyPassenger.getPassengerId());
				errorInfo.setFieldName(error.getFieldName());
				errors.add(errorInfo);
			}
		}
	}
	
	/**
	 * check if need to update travel doc
	 * @param requestDTO
	 * @param list 
	 * @return boolean
	 */
	private boolean checkNeedUpdateTravelDoc(UpdatePassengerDetailsRequestDTOV2 requestDTO, List<Segment> segments) {
		if (requestDTO == null || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		return (!CollectionUtils.isEmpty(requestDTO.getSegments()) 
				&& requestDTO.getSegments().stream().anyMatch(seg -> (seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null) && !segmentPassedCheckInOpenTime(seg.getSegmentId(), segments)))
				|| (requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())
						&& requestDTO.getInfant().getSegments().stream().anyMatch(seg -> (seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null) && !segmentPassedCheckInOpenTime(seg.getSegmentId(), segments)));
	}
	
	/**
	 * 
	 * check if the segment has entered check in time and after
	 * @param segmentId
	 * @return boolean
	 */
	private boolean segmentPassedCheckInOpenTime(String segmentId, List<Segment> segments) {
		Segment seg = segments.stream().filter(s -> s != null && !StringUtils.isEmpty(s.getSegmentID()) && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return seg.isOpenToCheckIn() || seg.isWithinNinetyMins() || seg.isFlown();
	}

	/**
	 * check if there's any segment need to transfer travel doc to product level
	 * @param requestDTO
	 * @param booking 
	 * @return boolean
	 */
	private boolean checkAnySegmentNeedTravelDocTransfer(UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking) {
		if (requestDTO == null || booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(booking.getSegments())) {
			return false;
		}
		
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			Passenger passenger = booking.getPassengers().stream().filter(pax -> requestDTO.getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(null);
			// if passenger have any primary travel doc in customer level
			boolean cusLevelPriTravelDocExist = passenger == null ? false : !CollectionUtils.isEmpty(passenger.getPriTravelDocs());
			// if passenger have any secondary travel doc in customer level
			boolean cusLevelSecTravelDocExist = passenger == null ? false : !CollectionUtils.isEmpty(passenger.getSecTravelDocs());
			List<PassengerSegment> psListOfThePassenger = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			
			for (PassengerSegment ps : psListOfThePassenger) {
				// if the passengerSegment don't have any travel doc and there's travel doc in customer level and this travel doc is not updated and the sector hasn't passed check-in open time, then need to transfer to product level
				if (((ps.getPriTravelDoc() == null && cusLevelPriTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), true))
						|| (ps.getSecTravelDoc() == null && cusLevelSecTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), false)))
						&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
					return true;
				}
			}
		}
		
		if (requestDTO.getInfant() != null && !StringUtils.isEmpty(requestDTO.getInfant().getPassengerId()) && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			Passenger infPassenger = booking.getPassengers().stream().filter(pax -> requestDTO.getInfant().getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(null);
			// if passenger have any primary travel doc in customer level
			boolean cusLevelPriTravelDocExist = infPassenger == null ? false : !CollectionUtils.isEmpty(infPassenger.getPriTravelDocs());
			// if passenger have any secondary travel doc in customer level
			boolean cusLevelSecTravelDocExist = infPassenger == null ? false : !CollectionUtils.isEmpty(infPassenger.getSecTravelDocs());
			List<PassengerSegment> psListOfThePassenger = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getInfant().getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			
			for (PassengerSegment ps : psListOfThePassenger) {
				// if the passengerSegment don't have any travel doc and there's travel doc in customer level and this travel doc is not updated and the sector hasn't passed check-in open time, then need to transfer to product level
				if (((ps.getPriTravelDoc() == null && cusLevelPriTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), true))
						|| (ps.getSecTravelDoc() == null && cusLevelSecTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), false)))
						&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * check if the travel doc is updated
	 * @param segments
	 * @param segmentId
	 * @param isPrimary
	 * @return boolean
	 */
	private boolean checkIsTravelDocUpdated(List<? extends UpdateBasicSegmentInfoDTOV2> segments, String segmentId, boolean isPrimary) {
		if (isPrimary) {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getPrimaryTravelDoc() != null);
		} else {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getSecondaryTravelDoc() != null);
		}
	}

	/**
	 * update the pax info without transfer
	 * @param requestDTO
	 * @param pnrBooking
	 * @param booking
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	private RetrievePnrBooking updateWithoutTransfer(UpdatePassengerDetailsRequestDTOV2 requestDTO,
			RetrievePnrBooking pnrBooking, Booking booking)
			throws BusinessBaseException {
		RetrievePnrBooking updateResult;
		
		UpdatePassengerRequestBuilderV2 builder = new UpdatePassengerRequestBuilderV2();
		//Store mapping OT id which need to be deleted 
		Map<String, List<String>> deleteMap = new HashMap<>();
		PNRAddMultiElements request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, false, false);
		//delete PNR
		Session session = deleteElements(requestDTO, deleteMap, request);
		updateResult = addPassengerInfoService.addPassengerInfo(request, session);

		return updateResult;	
		
	}
	
	private UpdatePassengersDetailRequestDTO buildCPRUpdatePassenger(UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking) throws ExpectedException {
		UpdatePassengersDetailRequestDTO cprUpdateRequest = new UpdatePassengersDetailRequestDTO();	
		List<String> passengerIdList = new ArrayList<>();
		passengerIdList.add(requestDTO.getPassengerId());
		
		// OLSSMMB-23046 - Set default journey ID for contact info update
		if(CollectionUtils.isEmpty(requestDTO.getSegments()) &&
			(
				// Means mandatory contact info
				(requestDTO != null && requestDTO.getPhoneInfo() != null && requestDTO.getPhoneInfo().getConvertToOlssContactInfo() != null)
			)
		) {
			String journeyId = booking.getCprJourneys().stream()
					.filter(Journey::isOpenToCheckIn)
					.map(Journey::getJourneyId)
					.findFirst()
					.orElseThrow(() -> new ExpectedException("Update CPR contact but no journey is open to check in",
							new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW)));
			requestDTO.setJourneyId(journeyId);
		}
		
		// Set JourneyId, if no journeyId in request, find first openToCheckIn journey
		if (!StringUtils.isEmpty(requestDTO.getJourneyId())) {			
			cprUpdateRequest.setJourneyId(requestDTO.getJourneyId());
		} else {
			cprUpdateRequest.setJourneyId(CollectionUtils.isEmpty(booking.getCprJourneys()) ? "" : booking.getCprJourneys().stream().filter(Journey::isOpenToCheckIn)
					.map(Journey::getJourneyId).findFirst().orElse(""));
		}
		
		// If apply to other passenger, add passengerId list
		if ((requestDTO.getEmail() != null && requestDTO.getEmail().getApplyPassengerId() != null)) {
			for(String passengerId: requestDTO.getEmail().getApplyPassengerId()) {
				passengerIdList.add(passengerId);
			}
		} else if((requestDTO.getPhoneInfo() != null && requestDTO.getPhoneInfo().getApplyPassengerId() != null)) {
			for(String passengerId: requestDTO.getPhoneInfo().getApplyPassengerId()) {
				passengerIdList.add(passengerId);
			}
		} 
		// Make sure no duplicate passenger id
		passengerIdList.stream().distinct().collect(Collectors.toList());
		cprUpdateRequest.setPassengers(buildPassenger(requestDTO, booking, passengerIdList, cprUpdateRequest.getJourneyId()));
		return cprUpdateRequest;
		
	}

	private List<UpdatePassengerDetailDTO> buildPassenger(UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking, List<String> passengerIdList, String journeyId) throws ExpectedException {
		List<UpdatePassengerDetailDTO> updatePassengerList = new ArrayList<>();
		
		UpdateEmailDTO addEmail = buildCprEmailModel(requestDTO.getEmail());	
		UpdateMobileDTO addPhoneNumer = buildCprPhoneNumberModel(requestDTO.getPhoneInfo());
		
		for(String passengerId: passengerIdList) {		
			UpdatePassengerDetailDTO passengerDetail = new UpdatePassengerDetailDTO();	
			// Use first journey to get passenger customer id, cause requestDTO may not contain journey id
			String customerId = null;
			if(!CollectionUtils.isEmpty(booking.getCprJourneys()) && !StringUtils.isEmpty(journeyId)) {
				Journey updateJourney = booking.getCprJourneys().stream().filter(journey -> Objects.equals(journeyId, journey.getJourneyId())).findFirst().orElse(null);
				if(updateJourney != null && !CollectionUtils.isEmpty(updateJourney.getPassengers())) {
					customerId = updateJourney.getPassengers().stream().filter(pax -> Objects.equals(pax.getPassengerId(), passengerId))
							.map(CprJourneyPassenger::getCprUniqueCustomerId).findFirst().orElse("");
				}
			}
			if (StringUtils.isEmpty(customerId)) {
				throw new ExpectedException("Find passenger unique customer id failed",
						new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}
			passengerDetail.setUniqueCustomerId(customerId);
			if (Objects.equals(passengerId, requestDTO.getPassengerId())) {
				// add gender, dateOfBirth, ktn, redress, primaryTravelDocument & secondaryTravelDocument
				populateTravelDocRelated(requestDTO.getSegments(), requestDTO.getPassengerId(), requestDTO.getKtn(), requestDTO.getRedress(), passengerDetail, booking ,journeyId);
				// add destination address
				addDestinationAddress(requestDTO.getDestination(), passengerDetail);
				// add emergency contact
				addEmergencyContact(requestDTO.getEmergencyContact(), passengerDetail);
			}
			// add contact info
			addContactInfo(addEmail, addPhoneNumer, passengerDetail);
			updatePassengerList.add(passengerDetail);
		}
		// build infant passenger detail
		addInfantPassengerDetail(requestDTO, booking, updatePassengerList, journeyId);
		// filter out empty passengerDetail
		return updatePassengerList.stream().filter(pd -> passengerDetailNotEmptyCheck(pd)).collect(Collectors.toList());
	}

	/**
	 * @param pd
	 * @return
	 */
	private boolean passengerDetailNotEmptyCheck(UpdatePassengerDetailDTO pd) {
		return pd != null && (!StringUtils.isEmpty(pd.getGender()) || pd.getDateOfBirth() != null || pd.getDestination() != null
				|| pd.getEmail() != null || pd.getEmergencyContact() != null || !CollectionUtils.isEmpty(pd.getFlights()) || pd.getMobile() != null || pd.getResidence() != null);
	}

	/**
	 * @param addEmail
	 * @param addPhoneNumer
	 * @param passengerDetail
	 */
	private void addContactInfo(UpdateEmailDTO addEmail, UpdateMobileDTO addPhoneNumer,
			UpdatePassengerDetailDTO passengerDetail) {
		if(addEmail != null) {
			passengerDetail.setEmail(addEmail);
		}
		if(addPhoneNumer != null) {
			passengerDetail.setMobile(addPhoneNumer);
		}
	}

	/**
	 * add Infant Passenger Detail
	 * @param requestDTO
	 * @param booking
	 * @param updatePassengerList
	 * @throws ExpectedException 
	 */
	private void addInfantPassengerDetail(UpdatePassengerDetailsRequestDTOV2 requestDTO, Booking booking,
			List<UpdatePassengerDetailDTO> updatePassengerList, String journeyId) throws ExpectedException {
		if(requestDTO.getInfant() == null || StringUtils.isEmpty(requestDTO.getInfant().getPassengerId())) {
			return;
		}
		String customerId = null;
		if(!CollectionUtils.isEmpty(booking.getCprJourneys()) && !StringUtils.isEmpty(journeyId)) {
			Journey updateJourney = booking.getCprJourneys().stream().filter(journey -> Objects.equals(journeyId, journey.getJourneyId())).findFirst().orElse(null);
			if(updateJourney != null && !CollectionUtils.isEmpty(updateJourney.getPassengers())) {
				customerId = updateJourney.getPassengers().stream().filter(pax -> Objects.equals(pax.getPassengerId(), requestDTO.getInfant().getPassengerId()))
						.map(CprJourneyPassenger::getCprUniqueCustomerId).findFirst().orElse("");
			}
		}
		if(StringUtils.isEmpty(customerId)) {
			throw new ExpectedException("Find infant passenger unique customer id failed",
					new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		UpdatePassengerDetailDTO passengerDetail = new UpdatePassengerDetailDTO();
		passengerDetail.setUniqueCustomerId(customerId);
		addDestinationAddress(requestDTO.getInfant().getDestination(), passengerDetail);
		populateTravelDocRelated(requestDTO.getInfant().getSegments(), requestDTO.getInfant().getPassengerId(), requestDTO.getInfant().getKtn(), requestDTO.getInfant().getRedress(), passengerDetail, booking, journeyId);
		updatePassengerList.add(passengerDetail);
	}

	/**
	 * add Emergency Contact
	 * @param emergencyContact
	 * @param passengerDetail
	 */
	private void addEmergencyContact(UpdateEmergencyContactDTOV2 emergencyContact,
			UpdatePassengerDetailDTO passengerDetail) {
		if (emergencyContact == null) {
			return;
		}
		com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmergencyContactDTO olciEmergencyContact = new com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmergencyContactDTO();
		if (emergencyContact.isBlank()) {
			olciEmergencyContact.setMode(com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmergencyContactDTO.Mode.REMOVE);
		} else {
			olciEmergencyContact.setMode(com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateEmergencyContactDTO.Mode.OVERWRITE);
			olciEmergencyContact.setEmergencyContactCountryCode(emergencyContact.getCountryCode());
			olciEmergencyContact.setEmergencyContactCountryNumber(emergencyContact.getPhoneCountryNumber());
			olciEmergencyContact.setEmergencyContactNumber(emergencyContact.getPhoneNo());
			olciEmergencyContact.setEmergencyContactPerson(emergencyContact.getContactName());
		}
		passengerDetail.setEmergencyContact(olciEmergencyContact);
	}

	/**
	 * add Destination Address
	 * @param destination
	 * @param passengerDetail
	 */
	private void addDestinationAddress(UpdateDestinationDTOV2 destination,
			UpdatePassengerDetailDTO passengerDetail) {
		if (destination == null) {
			return;
		}
		UpdateDestinationDTO updateDestination = new UpdateDestinationDTO();
		if (!destination.isTransit() && destination.getDestinationAddress().isBlank()) {
			updateDestination.setMode(UpdateDestinationDTO.Mode.REMOVE);
		} else {
			updateDestination.setMode(UpdateDestinationDTO.Mode.OVERWRITE);
			updateDestination.setTransit(destination.isTransit());
			
			UpdateDestinationAddressDTOV2 address = destination.getDestinationAddress();
			if (address != null && !address.isBlank()) {
				UpdateAddressDTO updateAddress = new UpdateAddressDTO();
				updateAddress.setCity(address.getCity());
				updateAddress.setCountryCode(MMBBizruleConstants.COUNTRY_CODE_USA_THREE);
				updateAddress.setStateCode(address.getStateCode());
				updateAddress.setStateName(address.getStateName());
				updateAddress.setStreet(address.getStreetName());
				updateAddress.setZipCode(address.getZipCode());
				updateDestination.setDestinationAddress(updateAddress);	
			}
		}
		passengerDetail.setDestination(updateDestination);
	}

	/**
	 * populateTravelDocRelated:add gender, dateOfBirth, ktn, primaryTravelDocument & secondaryTravelDocument
	 * @param requestDTO
	 * @param passengerDetail
	 * @param booking 
	 * @param journeyId 
	 */
	private void populateTravelDocRelated(List< ? extends UpdateBasicSegmentInfoDTOV2> list, String passengerId, UpdateTsDTOV2 updateKtn,
			UpdateTsDTOV2 updateRedress, UpdatePassengerDetailDTO passengerDetail, Booking booking, String journeyId) throws ExpectedException {
		// add empty flight list with productIdentifierDID and productIdentifierJID in UpdatePassengerDetailDTO
		passengerDetail.setFlights(buildFlightsList(booking, passengerId, journeyId));
		// start to populate travel doc related
		if(!CollectionUtils.isEmpty(list)) {
			// add dob, gender and country of residence
			addDobAndGenderAndCor(list, passengerDetail);
			// add primary & secondary TravelDocument
			addPrimaryAndSecondaryTravelDoc(list, passengerId, passengerDetail, booking);
		}
		// add ktn & redress
		if(updateKtn != null) {
			UpdateTsDTO ktn = new UpdateTsDTO();
			if(updateKtn.isBlank()) {
				ktn.setMode(UpdateTsDTO.Mode.REMOVE);
			}else {
				ktn.setMode(UpdateTsDTO.Mode.OVERWRITE);
				ktn.setNumber(updateKtn.getNumber());
			}
			addKtn(passengerDetail.getFlights(), ktn);
		}
		if(updateRedress != null) {
			UpdateTsDTO redress = new UpdateTsDTO();
			if(updateRedress.isBlank()) {
				redress.setMode(UpdateTsDTO.Mode.REMOVE);
			}else {
				redress.setMode(UpdateTsDTO.Mode.OVERWRITE);
				redress.setNumber(updateRedress.getNumber());
			}
			addRedress(passengerDetail.getFlights(), redress);
		}
		// filter out empty flight
		List<UpdateFlightDetailDTO> updateFlights = passengerDetail.getFlights().stream().filter(flight -> flight != null && (flight.getPrimaryTravelDocument() != null || flight.getSecondaryTravelDocument() != null
				|| flight.getKtn() != null || flight.getRedress() != null)).collect(Collectors.toList());
		passengerDetail.setFlights(updateFlights);
	}

	/**
	 * @param requestDTO
	 * @param passengerDetail
	 * @param booking
	 * @throws ExpectedException
	 */
	private void addPrimaryAndSecondaryTravelDoc(List< ? extends UpdateBasicSegmentInfoDTOV2> list, String passengerId,
			UpdatePassengerDetailDTO passengerDetail, Booking booking) throws ExpectedException {
		for(UpdateBasicSegmentInfoDTOV2 segmentInfo : list) {
			if(!CollectionUtils.isEmpty(booking.getPassengerSegments())) {
				PassengerSegment passengerSegment = booking.getPassengerSegments().stream().filter(ps -> Objects.equals(ps.getPassengerId(), passengerId) 
						&& Objects.equals(ps.getSegmentId(), segmentInfo.getSegmentId())).findFirst().orElse(null);
				if (passengerSegment == null || StringUtils.isEmpty(passengerSegment.getCprProductIdentifierDID())) {
					throw new ExpectedException("Find cpr Product Identifier did failed",
							new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
				}
				UpdateFlightDetailDTO flightDetail = passengerDetail.getFlights().stream()
						.filter(flight -> Objects.equals(flight.getProductIdentifierDID(), passengerSegment.getCprProductIdentifierDID())
								&& Objects.equals(flight.getProductIdentifierJID(), passengerSegment.getCprProductIdentifierJID())).findFirst().orElse(null);
				addPrimaryTravelDoc(segmentInfo, flightDetail);
				addSecondaryTravelDoc(segmentInfo, flightDetail);
			}
		}
	}

	/**
	 * @param segmentInfo
	 * @param flightDetail
	 */
	private void addSecondaryTravelDoc(UpdateBasicSegmentInfoDTOV2 segmentInfo, UpdateFlightDetailDTO flightDetail) {
		
		if(segmentInfo.getSecondaryTravelDoc() != null && flightDetail != null) {
			UpdateTravelDocumentDTO updateSecondaryTravelDocumentDTO = new UpdateTravelDocumentDTO();
			if (segmentInfo.getSecondaryTravelDoc().isBlank()) {
				updateSecondaryTravelDocumentDTO.setMode(UpdateTravelDocumentDTO.Mode.REMOVE);
			} else {
				if(segmentInfo.getSecondaryTravelDoc().getDateOfExpire() != null) {
					DateDTO secondaryExpiryDate = new DateDTO();
					secondaryExpiryDate.setDate(segmentInfo.getSecondaryTravelDoc().getDateOfExpire());
					updateSecondaryTravelDocumentDTO.setExpiryDate(secondaryExpiryDate);
				}
				updateSecondaryTravelDocumentDTO.setIssueCountry(segmentInfo.getSecondaryTravelDoc().getCountryOfIssuance());
				updateSecondaryTravelDocumentDTO.setMode(UpdateTravelDocumentDTO.Mode.OVERWRITE);
				updateSecondaryTravelDocumentDTO.setNationality(segmentInfo.getSecondaryTravelDoc().getNationality());
				updateSecondaryTravelDocumentDTO.setNumber(segmentInfo.getSecondaryTravelDoc().getTravelDocumentNumber());
				updateSecondaryTravelDocumentDTO.setType(segmentInfo.getSecondaryTravelDoc().getTravelDocumentType());
				updateSecondaryTravelDocumentDTO.setRegulatoryFirstName(segmentInfo.getSecondaryTravelDoc().getGivenName());
				updateSecondaryTravelDocumentDTO.setRegulatorySurname(segmentInfo.getSecondaryTravelDoc().getFamilyName());
			}
			flightDetail.setSecondaryTravelDocument(updateSecondaryTravelDocumentDTO);
		}
	}

	/**
	 * @param segmentInfo
	 * @param flightDetail
	 */
	private void addPrimaryTravelDoc(UpdateBasicSegmentInfoDTOV2 segmentInfo, UpdateFlightDetailDTO flightDetail) {
		if(segmentInfo.getPrimaryTravelDoc() != null && flightDetail != null) {
			UpdateTravelDocumentDTO updatePrimaryTravelDocumentDTO = new UpdateTravelDocumentDTO();
			DateDTO primaryExpiryDate = new DateDTO();
			primaryExpiryDate.setDate(segmentInfo.getPrimaryTravelDoc().getDateOfExpire());
			updatePrimaryTravelDocumentDTO.setExpiryDate(primaryExpiryDate);
			updatePrimaryTravelDocumentDTO.setIssueCountry(segmentInfo.getPrimaryTravelDoc().getCountryOfIssuance());
			updatePrimaryTravelDocumentDTO.setMode(UpdateTravelDocumentDTO.Mode.OVERWRITE);
			updatePrimaryTravelDocumentDTO.setNationality(segmentInfo.getPrimaryTravelDoc().getNationality());
			updatePrimaryTravelDocumentDTO.setNumber(segmentInfo.getPrimaryTravelDoc().getTravelDocumentNumber());
			updatePrimaryTravelDocumentDTO.setType(segmentInfo.getPrimaryTravelDoc().getTravelDocumentType());
			updatePrimaryTravelDocumentDTO.setRegulatoryFirstName(segmentInfo.getPrimaryTravelDoc().getGivenName());
			updatePrimaryTravelDocumentDTO.setRegulatorySurname(segmentInfo.getPrimaryTravelDoc().getFamilyName());
			flightDetail.setPrimaryTravelDocument(updatePrimaryTravelDocumentDTO);
		}
	}


	/**
	 * add Dob And Gender And Cor
	 * @param list
	 * @param passengerDetail
	 */
	private void addDobAndGenderAndCor(List< ? extends UpdateBasicSegmentInfoDTOV2> list,
			UpdatePassengerDetailDTO passengerDetail) {
		UpdateBasicSegmentInfoDTOV2 segmentWithPriTravelDoc = list.stream().filter(segInfo -> segInfo.getPrimaryTravelDoc() != null).findFirst().orElse(null);
		if(segmentWithPriTravelDoc == null) {
			UpdateBasicSegmentInfoDTOV2 infoDTO = list.stream().filter(segInfo -> segInfo.getSecondaryTravelDoc() != null).findFirst().orElse(null);
			if(infoDTO != null && !OneAConstants.TRAVEL_DOCUMENT_TYPE_VISA.equalsIgnoreCase(infoDTO.getSecondaryTravelDoc().getTravelDocumentType()) &&
					!infoDTO.getSecondaryTravelDoc().isBlank()) {
				DateDTO date = new DateDTO();
			    date.setDate(infoDTO.getSecondaryTravelDoc().getDateOfBirth());
			    passengerDetail.setDateOfBirth(date);
			    passengerDetail.setGender(infoDTO.getSecondaryTravelDoc().getGender());
			}
		} else {
			DateDTO date = new DateDTO();
			date.setDate(segmentWithPriTravelDoc.getPrimaryTravelDoc().getDateOfBirth());
			passengerDetail.setDateOfBirth(date);
			passengerDetail.setGender(segmentWithPriTravelDoc.getPrimaryTravelDoc().getGender());
		}
		
		String countryOfResidence =	list.stream()
				.map(UpdateBasicSegmentInfoDTOV2::getCountryOfResidence).filter(Objects::nonNull)
				.map(UpdateCountryOfResidenceDTOV2::getCountryCode).filter(StringUtils::isNotEmpty)
				.findFirst().orElse(null);
		
		if (StringUtils.isNotEmpty(countryOfResidence)) {
			UpdateResidenceDTO residence = new UpdateResidenceDTO();
			residence.setMode(UpdateResidenceDTO.Mode.OVERWRITE);
			residence.setResidenceCountry(countryOfResidence);
			passengerDetail.setResidence(residence);
		}
	}

	/**
	 * add Redress
	 * @param flights
	 * @param redress
	 */
	private void addRedress(List<UpdateFlightDetailDTO> flights, UpdateTsDTO redress) {
		for (UpdateFlightDetailDTO flight : flights) {
			flight.setRedress(redress);
		}
	}

	/**
	 * add Ktn
	 * @param flights
	 * @param ktn
	 */
	private void addKtn(List<UpdateFlightDetailDTO> flights, UpdateTsDTO ktn) {
		for (UpdateFlightDetailDTO flight : flights) {
			flight.setKtn(ktn);
		}
	}

	/**
	 * build Flights List
	 * @param passengerSegments
	 * @param string 
	 * @return
	 */
	private List<UpdateFlightDetailDTO> buildFlightsList(Booking booking, String passengerId, String journeyId) {
		if (!CollectionUtils.isEmpty(booking.getCprJourneys())) {
			Journey olciJourney = booking.getCprJourneys().stream()
					.filter(journey -> Objects.equals(journey.getJourneyId(), journeyId)).findFirst().orElse(null);
			if (olciJourney != null && !CollectionUtils.isEmpty(olciJourney.getPassengerSegments())) {
				List<CprJourneyPassengerSegment> cprPassengerSegments = olciJourney.getPassengerSegments().stream()
						.filter(ps -> ps != null && ps.getCanCheckIn() && !ps.getCheckedIn())
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(cprPassengerSegments)) {
					List<UpdateFlightDetailDTO> flights = new ArrayList<>();
					for (CprJourneyPassengerSegment passengerSegment : cprPassengerSegments) {
						if (Objects.equals(passengerId, passengerSegment.getPassengerId())) {
							UpdateFlightDetailDTO flight = new UpdateFlightDetailDTO();
							flight.setProductIdentifierDID(passengerSegment.getCprProductIdentifierDID());
							flight.setProductIdentifierJID(passengerSegment.getCprProductIdentifierJID());
							flights.add(flight);
						}
					}
					return flights;
				}
			}
		}
		return new ArrayList<>();
	}

	private UpdateEmailDTO buildCprEmailModel(UpdateEmailDTOV2 email) {
		if (email != null) {			
			UpdateEmailDTO updateEmail = new UpdateEmailDTO();
			updateEmail.setMode(Mode.OVERWRITE);
			updateEmail.setEmailAddress(email.getEmail());
			return updateEmail;
		}
		return null;
	}

	private UpdateMobileDTO buildCprPhoneNumberModel(UpdatePhoneInfoDTOV2 phoneNumer) {
		if (phoneNumer != null) {			
			UpdateMobileDTO updateMobile = new UpdateMobileDTO();
			updateMobile.setMode(com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdateMobileDTO.Mode.OVERWRITE);
			updateMobile.setMobileCountryNumber(phoneNumer.getPhoneCountryNumber());
			updateMobile.setMobileNumber(phoneNumer.getPhoneNo());
			updateMobile.setMobileCountryCode(phoneNumer.getCountryCode());
			return updateMobile;
		}
		return null;
	}


	/**
	 * update the pax info with transfer
	 * @param requestDTO
	 * @param pnrBooking
	 * @param booking
	 * @param ffpTransferStatus
	 * @param travelDocTransferStatus 
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	private RetrievePnrBooking updateWithTransfer(UpdatePassengerDetailsRequestDTOV2 requestDTO,
			RetrievePnrBooking pnrBooking, Booking booking, TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus) throws BusinessBaseException {
		UpdatePassengerRequestBuilderV2 builder = new UpdatePassengerRequestBuilderV2();	
		//Store mapping OT id which need to be deleted 
		Map<String, List<String>> deleteMap = new HashMap<>();	
		Session session;
		RetrievePnrBooking updateResult;
		PNRAddMultiElements request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, BooleanUtils.isTrue(ffpTransferStatus.isNeedTransfer()), BooleanUtils.isTrue(travelDocTransferStatus.isNeedTransfer()));
		try { // try update with transfer
			//delete PNR
			session = deleteElements(requestDTO, deleteMap, request);
			updateResult = addPassengerInfoService.addPassengerInfo(request, session);
		} catch (OneAErrorsException e) { // if update fails
			logger.warn(String.format("update passenger info with transfer failed for booking: %s, try to update and transfer separately", requestDTO.getRloc()), e);
			deleteMap.clear();
			builder = new UpdatePassengerRequestBuilderV2();
			// try update without transfer first
			request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, false, false);
			//delete PNR
			session = deleteElements(requestDTO, deleteMap, request);
			updateResult = addPassengerInfoService.addPassengerInfo(request, session);
		
			// mock request list for segment those need to transfer FFP
			List<UpdatePassengerDetailsRequestDTOV2> ffpTransferRequestList = mockFfpTransferRequestList(booking, requestDTO);	
			// transfer FFP one by one 
			for (UpdatePassengerDetailsRequestDTOV2 ffpTransferRequest : ffpTransferRequestList) {
				builder = new UpdatePassengerRequestBuilderV2();
				try {
					request = builder.buildRequest(ffpTransferRequest, updateResult, booking, deleteMap, false, false);
					updateResult = addPassengerInfoService.addPassengerInfo(request, null);
				} catch (Exception e2) {
					// there is FFP transfer failed, set the flag
					ffpTransferStatus.setAnyTransferFailed(true);
					logger.warn(String.format("Transfer FFP failed for rloc: %s, passengerId: %s, segmentId: %s", ffpTransferRequest.getRloc(), ffpTransferRequest.getPassengerId(),
							CollectionUtils.isEmpty(ffpTransferRequest.getSegments()) ? "": ffpTransferRequest.getSegments().get(0).getSegmentId()), e2);
				}
			}
			
			// mock request list for segment those need to transfer travel doc
			List<UpdatePassengerDetailsRequestDTOV2> travelDocTransferRequestList = mockTravelDocTransferRequestList(booking, requestDTO);	
			// transfer travel doc one by one 
			for (UpdatePassengerDetailsRequestDTOV2 travelDocTransferRequest : travelDocTransferRequestList) {
				builder = new UpdatePassengerRequestBuilderV2();
				try {
					request = builder.buildRequest(travelDocTransferRequest, updateResult, booking, deleteMap, false, false);
					updateResult = addPassengerInfoService.addPassengerInfo(request, null);
				} catch (Exception e2) {
					// there is travel doc transfer failed, set the flag
					travelDocTransferStatus.setAnyTransferFailed(true);
					logger.warn(String.format("Transfer travel doc failed for rloc: %s, passengerId: %s, segmentId: %s", travelDocTransferRequest.getRloc(), travelDocTransferRequest.getPassengerId(),
							CollectionUtils.isEmpty(travelDocTransferRequest.getSegments()) ? "": travelDocTransferRequest.getSegments().get(0).getSegmentId()), e2);
				}
			}
		}
		return updateResult;
	}
	
	/**
	 * mock request list for the transfer of travel doc
	 * @param booking
	 * @param requestDTO
	 * @return List<UpdatePassengerDetailsRequestDTO>
	 */
	private List<UpdatePassengerDetailsRequestDTOV2> mockTravelDocTransferRequestList(Booking booking,
			UpdatePassengerDetailsRequestDTOV2 requestDTO) {
		List<UpdatePassengerDetailsRequestDTOV2> mockRequestDTOs = new ArrayList<>();
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getPassengers())
				|| requestDTO == null) {
			return mockRequestDTOs;
		}
		
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			// passengerSegment of the pax in request
			List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
					.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
							&& ps.getPassengerId().equals(requestDTO.getPassengerId()))
					.collect(Collectors.toList());
			
			Passenger pax = booking.getPassengers().stream().filter(p -> requestDTO.getPassengerId().equals(p.getPassengerId())).findFirst().orElse(new Passenger());
			// customer level primary travel doc
			TravelDoc cusLevelPriTravelDoc = !CollectionUtils.isEmpty(pax.getPriTravelDocs()) ? pax.getPriTravelDocs().get(0) : null;
			// customer level secondary travel doc
			TravelDoc cusLevelSecTravelDoc = !CollectionUtils.isEmpty(pax.getSecTravelDocs()) ? pax.getSecTravelDocs().get(0) : null;
			for (PassengerSegment ps : psListOfThePax) {
				// mock for primary travel doc
				mockTravelDocRequestForAdultPax(booking, requestDTO, mockRequestDTOs, cusLevelPriTravelDoc, ps, true);
				// mock for secondary travel doc
				mockTravelDocRequestForAdultPax(booking, requestDTO, mockRequestDTOs, cusLevelSecTravelDoc, ps, false);
			}
		}
		
		if (requestDTO.getInfant() != null && !StringUtils.isEmpty(requestDTO.getInfant().getPassengerId()) && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			// passengerSegment of the pax in request
			List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
					.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
							&& ps.getPassengerId().equals(requestDTO.getInfant().getPassengerId()))
					.collect(Collectors.toList());	

			Passenger pax = booking.getPassengers().stream().filter(p -> requestDTO.getInfant().getPassengerId().equals(p.getPassengerId())).findFirst().orElse(new Passenger());
			// customer level primary travel doc
			TravelDoc cusLevelPriTravelDoc = !CollectionUtils.isEmpty(pax.getPriTravelDocs()) ? pax.getPriTravelDocs().get(0) : null;
			// customer level secondary travel doc
			TravelDoc cusLevelSecTravelDoc = !CollectionUtils.isEmpty(pax.getSecTravelDocs()) ? pax.getSecTravelDocs().get(0) : null;
			
			for (PassengerSegment ps : psListOfThePax) {
				// mock for primary travel doc
				mockTravelDocRequestForInfantPax(booking, requestDTO, mockRequestDTOs, cusLevelPriTravelDoc, ps, true);
				// mock for secondary travel doc
				mockTravelDocRequestForInfantPax(booking, requestDTO, mockRequestDTOs, cusLevelSecTravelDoc, ps, false);
			}
		}
		
		return mockRequestDTOs;
	}

	/**
	 * mock travel doc transfer request for infant passenger
	 * @param booking
	 * @param requestDTO
	 * @param mockRequestDTOs
	 * @param cusLevelTravelDoc
	 * @param passengerSegment
	 * @param isPrimary
	 */
	private void mockTravelDocRequestForInfantPax(Booking booking, UpdatePassengerDetailsRequestDTOV2 requestDTO,
			List<UpdatePassengerDetailsRequestDTOV2> mockRequestDTOs, TravelDoc cusLevelTravelDoc, PassengerSegment ps,
			boolean isPrimary) {
		if (!checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), isPrimary)
				&& ps.getPriTravelDoc() == null && cusLevelTravelDoc != null
				&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
			UpdatePassengerDetailsRequestDTOV2 mockRequestDTO = new UpdatePassengerDetailsRequestDTOV2();
			
			mockRequestDTO.setRloc(requestDTO.getRloc());
			mockRequestDTO.setPassengerId(ps.getPassengerId());
			UpdateInfantDTOV2 infantDTO = new UpdateInfantDTOV2();
			List<UpdateBasicSegmentInfoDTOV2> mockSegmentDTOs = new ArrayList<>();
			UpdateBasicSegmentInfoDTOV2 mockSegmentDTO = new UpdateBasicSegmentInfoDTOV2();
			mockSegmentDTO.setSegmentId(ps.getSegmentId());
			
			if (StringUtils.isNotEmpty(cusLevelTravelDoc.getCountryOfResidence())) {
				UpdateCountryOfResidenceDTOV2 countryOfResidenceDTO = new UpdateCountryOfResidenceDTOV2();
				countryOfResidenceDTO.setCountryCode(cusLevelTravelDoc.getCountryOfResidence());
				mockSegmentDTO.setCountryOfResidence(countryOfResidenceDTO);
			}
			
			UpdateTravelDocDTOV2 travelDocDTO = new UpdateTravelDocDTOV2();
			travelDocDTO.setCountryOfIssuance(cusLevelTravelDoc.getCountryOfIssuance());
			travelDocDTO.setDateOfBirth(cusLevelTravelDoc.getBirthDateYear()+"-"+cusLevelTravelDoc.getBirthDateMonth()+"-"+cusLevelTravelDoc.getBirthDateDay());
			travelDocDTO.setDateOfExpire(cusLevelTravelDoc.getExpiryDateYear()+"-"+cusLevelTravelDoc.getExpiryDateMonth()+"-"+cusLevelTravelDoc.getExpiryDateDay());
			travelDocDTO.setFamilyName(cusLevelTravelDoc.getFamilyName());
			travelDocDTO.setGivenName(cusLevelTravelDoc.getGivenName());
			travelDocDTO.setGender(cusLevelTravelDoc.getGender());
			travelDocDTO.setNationality(cusLevelTravelDoc.getNationality());
			travelDocDTO.setTravelDocumentNumber(cusLevelTravelDoc.getTravelDocumentNumber());
			travelDocDTO.setTravelDocumentType(cusLevelTravelDoc.getTravelDocumentType());
			
			if (isPrimary) {
				mockSegmentDTO.setPrimaryTravelDoc(travelDocDTO);
			} else {
				mockSegmentDTO.setSecondaryTravelDoc(travelDocDTO);
			}
			mockSegmentDTOs.add(mockSegmentDTO);
			infantDTO.setSegments(mockSegmentDTOs);
			mockRequestDTO.setInfant(infantDTO);
			mockRequestDTOs.add(mockRequestDTO);
		}
	}
	
	/**
	 * mock travel doc transfer request for adult passenger
	 * @param booking
	 * @param requestDTO
	 * @param mockRequestDTOs
	 * @param cusLevelTravelDoc
	 * @param passengerSegment
	 * @param isPrimary
	 */
	private void mockTravelDocRequestForAdultPax(Booking booking, UpdatePassengerDetailsRequestDTOV2 requestDTO,
			List<UpdatePassengerDetailsRequestDTOV2> mockRequestDTOs, TravelDoc cusLevelTravelDoc, PassengerSegment ps,
			boolean isPrimary) {
		if (!checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), isPrimary)
				&& ps.getPriTravelDoc() == null && cusLevelTravelDoc != null
				&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
			UpdatePassengerDetailsRequestDTOV2 mockRequestDTO = new UpdatePassengerDetailsRequestDTOV2();
			
			mockRequestDTO.setRloc(requestDTO.getRloc());
			mockRequestDTO.setPassengerId(ps.getPassengerId());
			
			List<UpdateAdultSegmentInfoDTOV2> mockSegmentDTOs = new ArrayList<>();
			UpdateAdultSegmentInfoDTOV2 mockSegmentDTO = new UpdateAdultSegmentInfoDTOV2();
			mockSegmentDTO.setSegmentId(ps.getSegmentId());
			
			if (StringUtils.isNotEmpty(cusLevelTravelDoc.getCountryOfResidence())) {
				UpdateCountryOfResidenceDTOV2 countryOfResidenceDTO = new UpdateCountryOfResidenceDTOV2();
				countryOfResidenceDTO.setCountryCode(cusLevelTravelDoc.getCountryOfResidence());
				mockSegmentDTO.setCountryOfResidence(countryOfResidenceDTO);
			}
			
			UpdateTravelDocDTOV2 travelDocDTO = new UpdateTravelDocDTOV2();	
			travelDocDTO.setCountryOfIssuance(cusLevelTravelDoc.getCountryOfIssuance());
			travelDocDTO.setDateOfBirth(cusLevelTravelDoc.getBirthDateYear()+"-"+cusLevelTravelDoc.getBirthDateMonth()+"-"+cusLevelTravelDoc.getBirthDateDay());
			travelDocDTO.setDateOfExpire(cusLevelTravelDoc.getExpiryDateYear()+"-"+cusLevelTravelDoc.getExpiryDateMonth()+"-"+cusLevelTravelDoc.getExpiryDateDay());
			travelDocDTO.setFamilyName(cusLevelTravelDoc.getFamilyName());
			travelDocDTO.setGivenName(cusLevelTravelDoc.getGivenName());
			travelDocDTO.setGender(cusLevelTravelDoc.getGender());
			travelDocDTO.setNationality(cusLevelTravelDoc.getNationality());
			travelDocDTO.setTravelDocumentNumber(cusLevelTravelDoc.getTravelDocumentNumber());
			travelDocDTO.setTravelDocumentType(cusLevelTravelDoc.getTravelDocumentType());
			
			if (isPrimary) {
				mockSegmentDTO.setPrimaryTravelDoc(travelDocDTO);
			} else {
				mockSegmentDTO.setSecondaryTravelDoc(travelDocDTO);
			}
			mockSegmentDTOs.add(mockSegmentDTO);
			mockRequestDTO.setSegments(mockSegmentDTOs);
			mockRequestDTOs.add(mockRequestDTO);
		}
	}

	/**
	 * check if the pax only have customer level ffp in the booking
	 * @param passengerId
	 * @param booking
	 * @return boolean
	 */
	private boolean checkPaxOnlyHasCusLevelFfpInBooking(String passengerId, Booking booking) {
		if (StringUtils.isEmpty(passengerId) || booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return false;
		}
		
		boolean hasCusLevelFfp = booking.getPassengerSegments().stream().anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && ps.getFqtvInfo() != null && !ps.getFqtvInfo().isBlank() && !BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel()));
		boolean hasProLevelFfp = booking.getPassengerSegments().stream().anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && ps.getFqtvInfo() != null && !ps.getFqtvInfo().isBlank() && BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel()));
		return hasCusLevelFfp && !hasProLevelFfp;
	}
	
	/**
	 * @Description check if the segment is flown
	 * @param segmentId
	 * @return
	 */
	private boolean isSegmentFlown(String segmentId, List<Segment> segments) {
		Segment seg = segments.stream().filter(s -> s != null && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return BooleanUtils.isTrue(seg.isFlown());
	}
	
	/**
	 * check if request contains update of FFP
	 * @param requestDTO
	 * @return
	 */
	private boolean checkNeedUpdateFFP(UpdatePassengerDetailsRequestDTOV2 requestDTO, List<Segment> segments) {
		 if (requestDTO == null || CollectionUtils.isEmpty(requestDTO.getSegments()) || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		return requestDTO.getSegments().stream().anyMatch(seg -> seg.getFfpInfo() != null && !isSegmentFlown(seg.getSegmentId(), segments));
	}

	/**
	 * mock request list for the transfer of FFP
	 * @param pnrBooking
	 * @param booking 
	 * @param requestDTO
	 * @return List<UpdatePassengerDetailsRequestDTOV2>
	 */
	private List<UpdatePassengerDetailsRequestDTOV2> mockFfpTransferRequestList(Booking booking, UpdatePassengerDetailsRequestDTOV2 requestDTO) {
		List<UpdatePassengerDetailsRequestDTOV2> mockRequestDTOs = new ArrayList<>();
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| requestDTO == null
				|| CollectionUtils.isEmpty(requestDTO.getSegments())) {
			return mockRequestDTOs;
		}
		// passengerSegment of the pax in request
		List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
				.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
						&& ps.getPassengerId().equals(requestDTO.getPassengerId()))
				.collect(Collectors.toList());
		for (PassengerSegment ps : psListOfThePax) {
			if (!checkFfpOfPsUpdated(ps.getPassengerId(), ps.getSegmentId(), requestDTO) && ps.getFqtvInfo() != null
					&& !ps.getFqtvInfo().isBlank() && !BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel())
					&& !isSegmentFlown(ps.getSegmentId(), booking.getSegments())) {
				// if FFP of the passengerSegment is from customer level, and FFP of this passengerSegment is not updated in requestDTO and segment is not flown, mock request for this passengerSegment for later transfer
				UpdatePassengerDetailsRequestDTOV2 mockRequestDTO = new UpdatePassengerDetailsRequestDTOV2();
				
				mockRequestDTO.setRloc(requestDTO.getRloc());
				mockRequestDTO.setPassengerId(ps.getPassengerId());
				
				List<UpdateAdultSegmentInfoDTOV2> mockSegmentDTOs = new ArrayList<>();
				UpdateAdultSegmentInfoDTOV2 mockSegmentDTO = new UpdateAdultSegmentInfoDTOV2();
				UpdateFFPInfoDTOV2 mockFfpDTO = new UpdateFFPInfoDTOV2();
				
				mockFfpDTO.setCompanyId(ps.getFqtvInfo().getCompanyId());
				mockFfpDTO.setMembershipNumber(ps.getFqtvInfo().getMembershipNumber());
				
				mockSegmentDTO.setFfpInfo(mockFfpDTO);
				mockSegmentDTO.setSegmentId(ps.getSegmentId());
				
				mockSegmentDTOs.add(mockSegmentDTO);
				mockRequestDTO.setSegments(mockSegmentDTOs);
				
				mockRequestDTOs.add(mockRequestDTO);
			}
		}
		
		return mockRequestDTOs;
	}

	/**
	 * check if the FFP of the passengerSegment is updated in the requestDTO
	 * @param passengerId
	 * @param segmentId
	 * @param requestDTO
	 * @return boolean
	 */
	private boolean checkFfpOfPsUpdated(String passengerId, String segmentId,
			UpdatePassengerDetailsRequestDTOV2 requestDTO) {
		if (StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)) {
			return false;
		}
		return passengerId.equals(requestDTO.getPassengerId()) && requestDTO.getSegments().stream().anyMatch(seg -> seg.getFfpInfo() != null && segmentId.equals(seg.getSegmentId()));
	}

	private Session deleteElements(UpdatePassengerDetailsRequestDTOV2 requestDTO, Map<String, List<String>> deleteMap,
			PNRAddMultiElements request) throws BusinessBaseException {
		Session session;
		if(!deleteMap.isEmpty()) {
			session = new Session();//set begin transaction
			
			Map<String, List<String>> map = buildDeleteOtMap(deleteMap);
			
			RetrievePnrBooking booking = deletePnrService.deletePnr(requestDTO.getRloc(), map, session);
			//check onea error for delete response
			session = booking.getSession();
			session.setStatus(SessionStatus.END.getStatus());//set end transaction
			request.getReservationInfo().getReservation().setControlNumber("");
		}else {
			session = null;
		}
		return session;
	}
	
	/**
	 * build OT map for 1A delete
	 * @param deleteMap
	 * @return map
	 */
	private Map<String, List<String>> buildDeleteOtMap(Map<String, List<String>> deleteMap) {
		Map<String, List<String>> map = new HashMap<>();
		List<String> values = new ArrayList<>();
		for(Entry<String, List<String>> entry : deleteMap.entrySet()) {
			values.addAll(entry.getValue());
		}
		map.put(OneAConstants.OT_QUALIFIER, values);
		return map;
	}
	
}