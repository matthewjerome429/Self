package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.RequestAssistanceBusiness;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.common.booking.FQTVInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.PaxConsentDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentsDeleteRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.SegmentConsentDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.SsrInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.UpdateAssistanceInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.UpdateAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance.CancelAssistanceInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance.CancelAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentInfoRecordDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistanceDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistancePassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistancePassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistanceSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.UpdateAssistanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.cancelassistance.CancelAssistanceResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.AddSsrBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.model.SsrInfoModel;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.model.SsrUpdateModel;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.ConsentInfoService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class RequestAssistanceBusinessImpl implements RequestAssistanceBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(RequestAssistanceBusinessImpl.class);
	
    @Autowired
    private BizRuleConfig bizRuleConfig;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Autowired
	private AddSsrBuilder addSsrBuilder;
	
	@Autowired
	private OneAWSClient oneAWSClient;
	
	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Autowired
	private RetrievePnrBusiness retrievePnrService;
	
	@Autowired
	private ConsentInfoService consentInfoService;
	
	@Override
	public RequestAssistanceDTO getRequestAssistanceInfo(String rloc, LoginInfo loginInfo) throws BusinessBaseException {

		return convertToSpecialServiceDTO(retrievePnrByRloc(rloc, loginInfo, initializeRequired()));
	}
	

	@Override
	public UpdateAssistanceResponseDTO updateAssistance(UpdateAssistanceRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {

		/** Get booking */
		Booking booking = retrievePnrByRloc(requestDTO.getRloc(), loginInfo, initializeRequired());
		verifyUpdateAssistanceRequest(requestDTO.getUpdateAssistanceInfo(), requestDTO.getCancelInfos(), booking);

		List<Integer> consentRecordIds;
		try {
			ConsentAddRequestDTO saveConsentRequest = buildSaveCommonConsentInfoRequestDTO(requestDTO);
			ConsentCommonRecordResponseDTO saveConsentResponse = retrievePnrService.saveConsentCommon(
					saveConsentRequest, loginInfo, requestDTO.getRloc(), MMBUtil.getCurrentAcceptLanguage());
			consentRecordIds = saveConsentResponse.getConsentInfoRecords().stream().map(
					ConsentInfoRecordDTO::getId).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(String.format("Save consent => failure! booking[%s]", requestDTO.getRloc()), e);
			return new UpdateAssistanceResponseDTO(false);
		}
		
		try {
			Session session = null;
			if (requestDTO.getCancelInfos() != null) { // If choose apply all and need to cancel it
				session = new Session();
				/** Get list of qulifier IDs SSR which should be removed from 1A */
				List<String> qulifierIds = getCancelSsrQulifierIds(booking, requestDTO.getCancelInfos());
				removeAssistanceSSR(booking.getOneARloc(), qulifierIds, session);
				session.setStatus(SessionStatus.END.getStatus()); // Close session
			}
			// Call oneA to update assistance
			updateAssistanceSSR(requestDTO, session);
		} catch(OneAErrorsException e) {
			logger.error(String.format("Update Assistance => failure! booking[%s] 1A error!", requestDTO.getRloc()), e);
			
			consentInfoService.deleteConsentCommons(buildDeleteCommonConsentInfoRequestDTO(consentRecordIds));
			
			UpdateAssistanceResponseDTO updateAssistanceResponseDTO = new UpdateAssistanceResponseDTO(false);
			updateAssistanceResponseDTO.setErrors(e.getOneAErrors());
			return updateAssistanceResponseDTO;
		} catch(Exception e) {
			logger.error(String.format("Update Assistance => failure! booking[%s]", requestDTO.getRloc()), e);
			
			consentInfoService.deleteConsentCommons(buildDeleteCommonConsentInfoRequestDTO(consentRecordIds));
			
			return new UpdateAssistanceResponseDTO(false);
		}
		logger.info(String.format("Update | Assistance Request | Rloc | %s", requestDTO.getRloc()), true);
		
		tagging(requestDTO);
		
		return new UpdateAssistanceResponseDTO(true);
	}
	
	/**
	 * OLSS-6335 & OLSS-7536 tagging
	 * 
	 * @param requestDTO
	 */
	private void tagging(UpdateAssistanceRequestDTO requestDTO) {
		try {
			List<String> requestCodes = requestDTO.getUpdateAssistanceInfo().stream().map(UpdateAssistanceInfoDTO::getUpdateSsrList)
					.flatMap(List::stream).map(SsrInfoDTO::getSsrCode).distinct().collect(Collectors.toList());
			if(requestCodes.stream().anyMatch(MMBBizruleConstants.SPECIAL_SERVICE_MOBILITY_CODE::contains)) {
				logger.info(String.format("Mobility Service Requested | RLOC | %s", requestDTO.getRloc()), true);
			}
			
			if(requestCodes.stream().anyMatch(MMBBizruleConstants.SPECIAL_SERVICE_VISUAL_CODE::contains)) {
				logger.info(String.format("Visual Service Requested | RLOC | %s", requestDTO.getRloc()), true);
			}
			
			if(requestCodes.stream().anyMatch(MMBBizruleConstants.SPECIAL_SERVICE_HEARING_CODE::contains)) {
				logger.info(String.format("Hearing Service Requested | RLOC | %s", requestDTO.getRloc()), true);
			}
			
			if(requestCodes.stream().anyMatch(MMBBizruleConstants.SPECIAL_SERVICE_AIRPORT_ASSISTANCE_CODE::contains)) {
				logger.info(String.format("Airport Assistance Requested | RLOC | %s", requestDTO.getRloc()), true);
			}
		} catch (Exception e) {
			logger.error("Tagging error!", e);
		}
		
	}

	private ConsentAddRequestDTO buildSaveCommonConsentInfoRequestDTO(UpdateAssistanceRequestDTO updateAssistanceRequestDTO) {
				
		List<UpdateAssistanceInfoDTO> updateAssistanceInfoDTOs = updateAssistanceRequestDTO.getUpdateAssistanceInfo();
		
		List<String> passengerIds = updateAssistanceInfoDTOs.stream().map(
				UpdateAssistanceInfoDTO::getPassengerId).distinct().collect(Collectors.toList());
		
		List<PaxConsentDTO> passengerConsentDTOs = passengerIds.stream().map(passengerId -> {
			List<UpdateAssistanceInfoDTO> passengerUpdateAssistanceInfoDTOs = 
					updateAssistanceInfoDTOs.stream().filter(infoDTO -> passengerId.equals(infoDTO.getPassengerId())
				).collect(Collectors.toList());
			return buildPaxConsentDTO(passengerId, passengerUpdateAssistanceInfoDTOs);
		}).collect(Collectors.toList());
		
		ConsentAddRequestDTO saveConsentInfoRequest = new ConsentAddRequestDTO();
		saveConsentInfoRequest.setRloc(saveConsentInfoRequest.getRloc());
		saveConsentInfoRequest.setPassengers(passengerConsentDTOs);		
		return saveConsentInfoRequest;
	}
	
	private PaxConsentDTO buildPaxConsentDTO(String passengerId, List<UpdateAssistanceInfoDTO> updateAssistanceInfoDTOs) {
		
		List<String> segmentIds = updateAssistanceInfoDTOs.stream().map(
				UpdateAssistanceInfoDTO::getSegmentId).distinct().collect(Collectors.toList());
		
		List<SegmentConsentDTO> segmentConsentDTOs = segmentIds.stream().map(segmentId -> {
			List<UpdateAssistanceInfoDTO> segmentUpdateAssistanceInfoDTOs = 
					updateAssistanceInfoDTOs.stream().filter(infoDTO -> segmentId.equals(infoDTO.getSegmentId())
				).collect(Collectors.toList());
			return buildSegmentConsentDTO(segmentId, segmentUpdateAssistanceInfoDTOs);
		}).collect(Collectors.toList());
		
		PaxConsentDTO passengerConsentDTO = new PaxConsentDTO();
		passengerConsentDTO.setPassengerId(passengerId);
		passengerConsentDTO.setSegments(segmentConsentDTOs);
		return passengerConsentDTO;
	}
	
	private SegmentConsentDTO buildSegmentConsentDTO(String segmentId, List<UpdateAssistanceInfoDTO> updateAssistanceInfoDTOs) {
		
		List<String> ssrCodeList = updateAssistanceInfoDTOs.stream().map(
				UpdateAssistanceInfoDTO::getUpdateSsrList
			).flatMap(List::stream).map(SsrInfoDTO::getSsrCode).collect(Collectors.toList());
		
		SegmentConsentDTO segmentConsentDTO = new SegmentConsentDTO();
		segmentConsentDTO.setSegmentId(segmentId);
		segmentConsentDTO.setSsrList(ssrCodeList);
		return segmentConsentDTO;
	}
	
	private ConsentsDeleteRequestDTO buildDeleteCommonConsentInfoRequestDTO(List<Integer> ids) {
		ConsentsDeleteRequestDTO requestDTO = new ConsentsDeleteRequestDTO();
		requestDTO.setIds(ids);
		return requestDTO;
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
		// build oneA request without cache
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRRetrieve request = builder.buildRlocRequest(rloc);
		PNRReply pnrReplay	= oneAWSClient.retrievePnr(request);
		RetrievePnrBooking retrievePnrBooking = pnrResponseParser.paserResponse(pnrReplay);

		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		return bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
	}
	
	/**
	 * Convert booking model to special service info model
	 * @param booking
	 * @return
	 */
	private RequestAssistanceDTO convertToSpecialServiceDTO(Booking booking) {
		RequestAssistanceDTO specialServiceInfoDTO = new RequestAssistanceDTO();
		
		specialServiceInfoDTO.setPassengers(buildSpecialServicePassenger(booking));
		specialServiceInfoDTO.setSegments(buildSpecialServiceSegment(booking));
		specialServiceInfoDTO.setPassengerSegments(buildSpecialServicePassengerSegment(booking));
		
		return specialServiceInfoDTO;
	}
	
	/**
	 * build special service passenger model
	 * @param passengers
	 * @param specialServicePassengerDTOs
	 */
	private List<RequestAssistancePassengerDTO> buildSpecialServicePassenger(Booking booking) {
		if (CollectionUtils.isEmpty(booking.getPassengers())) {
			return new ArrayList<>();
		}
		List<RequestAssistancePassengerDTO> specialServicePassengerDTOs = new ArrayList<>();
		for (Passenger passenger: booking.getPassengers()) {				
			RequestAssistancePassengerDTO specialServicePassenger = new RequestAssistancePassengerDTO();
			specialServicePassenger.setFamilyName(passenger.getFamilyName());
			specialServicePassenger.setGivenName(passenger.getGivenName());
			specialServicePassenger.setCompanion(passenger.isCompanion());
			specialServicePassenger.setLoginMember(passenger.getLoginMember());
			specialServicePassenger.setParentId(passenger.getParentId());
			specialServicePassenger.setPassengerId(passenger.getPassengerId());
			specialServicePassenger.setPassengerType(passenger.getPassengerType());
			specialServicePassenger.setPrimaryPassenger(passenger.isPrimaryPassenger());
			specialServicePassenger.setTitle(passenger.getTitle());
			specialServicePassengerDTOs.add(specialServicePassenger);
		}
		return specialServicePassengerDTOs;
	}
	
	/**
	 * build special service segment model
	 * @param segments
	 * @param specialServiceSegmentDTOs
	 */
	private List<RequestAssistanceSegmentDTO> buildSpecialServiceSegment(Booking booking) {
		if (CollectionUtils.isEmpty(booking.getSegments())) {
			return new ArrayList<>();
		}
		List<RequestAssistanceSegmentDTO> specialServiceSegmentDTOs = new ArrayList<>();
		for (Segment segment: booking.getSegments()) {
			RequestAssistanceSegmentDTO specialServiceSegment = new RequestAssistanceSegmentDTO();
			specialServiceSegment.setSegmentId(segment.getSegmentID());
			specialServiceSegment.setAirCraftType(segment.getAirCraftType());
			specialServiceSegment.setDestPort(segment.getDestPort());
			specialServiceSegment.setDestTerminal(segment.getDestTerminal());
			specialServiceSegment.setIsFlown(segment.isFlown());
			specialServiceSegment.setMarketCompany(segment.getMarketCompany());
			specialServiceSegment.setMarketSegmentNumber(segment.getMarketSegmentNumber());
			specialServiceSegment.setOperateCompany(segment.getOperateCompany());
			specialServiceSegment.setOperateSegmentNumber(segment.getOperateSegmentNumber());
			specialServiceSegment.setUnConfirmedOperateInfo(segment.isUnConfirmedOperateInfo());
			specialServiceSegment.setOriginPort(segment.getOriginPort());
			specialServiceSegment.setOriginTerminal(segment.getOriginTerminal());
			specialServiceSegment.setStatus(segment.getSegmentStatus().getStatus());
			buildFortyEightFlag(segment, specialServiceSegment);
			specialServiceSegmentDTOs.add(specialServiceSegment);
		}
		return specialServiceSegmentDTOs;
	}
	
	/**
	 * build special service passengerSegment model
	 * @param passengerSegments
	 * @param specialServiceSegmentDTOs
	 */
	private List<RequestAssistancePassengerSegmentDTO> buildSpecialServicePassengerSegment(Booking booking) {
		if (CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return new ArrayList<>();
		}
		List<RequestAssistancePassengerSegmentDTO> specialServicePassengerSegmentDTOs = new ArrayList<>();
		for (PassengerSegment passengerSegment: booking.getPassengerSegments()) {
			RequestAssistancePassengerSegmentDTO specialServicePassengerSegment = new RequestAssistancePassengerSegmentDTO();
			specialServicePassengerSegment.setMemberAward(passengerSegment.getMemberAward());
			specialServicePassengerSegment.setPassengerId(passengerSegment.getPassengerId());
			specialServicePassengerSegment.setSegmentId(passengerSegment.getSegmentId());
			if(!CollectionUtils.isEmpty(passengerSegment.getSpecialServices())){				
				specialServicePassengerSegment.setSpecialServices(BookingBuildUtil.removeDubplicateSepcialService(passengerSegment.getSpecialServices()));
			}
			specialServicePassengerSegment.setEticket(passengerSegment.getEticketNumber());
			specialServicePassengerSegment.setCheckedIn(passengerSegment.isCheckedIn());
			if(passengerSegment.getSpecialServices() != null) {				
				specialServicePassengerSegment.setHasMobilityAssist(passengerSegment.getSpecialServices().stream().anyMatch(specialService-> Arrays.asList(OneAConstants.MOBILITY_ASSIST_ARRAY).contains(specialService.getCode())));
			}
			
			FQTVInfo fqtvInfo = passengerSegment.getFqtvInfo();
			FQTVInfoDTO fqtvInfoDTO = specialServicePassengerSegment.findFQTVInfo();
			if(fqtvInfo != null && !StringUtils.isEmpty(fqtvInfo.getMembershipNumber())){
				fqtvInfoDTO.setCompanyId(fqtvInfo.getCompanyId());
				fqtvInfoDTO.setMembershipNumber(fqtvInfo.getMembershipNumber());
				fqtvInfoDTO.setTierLevel(fqtvInfo.getTierLevel());
				fqtvInfoDTO.setTopTier(fqtvInfo.isTopTier());
				fqtvInfoDTO.setProductLevel(fqtvInfo.isProductLevel());
				fqtvInfoDTO.setIsAM(fqtvInfo.getAm());
				fqtvInfoDTO.setIsMPO(fqtvInfo.getMpo());
			}
			specialServicePassengerSegmentDTOs.add(specialServicePassengerSegment);
		}
		return specialServicePassengerSegmentDTOs;
	}
	
	/**
	 * initialize BookingBuildRequired to build booking for add-booking function.
	 * 
	 * @return BookingBuildRequired
	 */
	private BookingBuildRequired initializeRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setCprCheck(true);
		required.setCountryOfResidence(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setMemberAward(true);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setRtfs(true);
		required.setSeatSelection(false);
		required.setTravelDocument(false);
		return required;
	}

	@Override
	public CancelAssistanceResponseDTO cancelAssistance(CancelAssistanceRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {
		/** Get booking */
		Booking booking = retrievePnrByRloc(requestDTO.getRloc(), loginInfo, initializeRequired());
		
		/** Get list of qulifier IDs SSR which should be removed from 1A */
		List<String> qulifierIds = getCancelSsrQulifierIds(booking, requestDTO.getCancelInfos());
		
		/**
		 * Due to 1 more passengers could share to same qulifierId in 1A PNR, get all this informations,
		 * need to add back this informations, so here get this information for adding later.
		 * */
		List<SsrUpdateModel> ssrAddModels = buildAddBackSsrModels(booking.getPassengerSegments(), qulifierIds, requestDTO);
		
		/** 
		 * Cancel assistance steps:
		 * 1. Remove assistance SSR elements by qulifierIds.
		 * 2. Adding back SSR if shared the same qulifierIds.
		 * */
		try {
			Session session = null;
			if(!CollectionUtils.isEmpty(ssrAddModels)) {
				session = new Session();
			}
			removeAssistanceSSR(booking.getOneARloc(), qulifierIds, session);
			
			if(session != null) {
				session.setStatus(SessionStatus.END.getStatus());
				addSpecialServiceSSR(booking.getOneARloc(), session, ssrAddModels);
			}
		} catch(Exception e) {
			logger.error(String.format("Cancel Assistance => failure! remove SSR by qulifierIds:%s in booking[%s] failure!", qulifierIds.toString(), booking.getOneARloc()), e);
			return new CancelAssistanceResponseDTO();
		}
		
		return new CancelAssistanceResponseDTO(true);
	}
	
	/**
	 * Build SsrUpdateModels which will be added back into PNR
	 * 
	 * @param passengerSegments
	 * @param cancelledQulifierIds
	 * @param requestDTO
	 * @return
	 */
	private List<SsrUpdateModel> buildAddBackSsrModels(List<PassengerSegment> passengerSegments,
			List<String> cancelledQulifierIds, CancelAssistanceRequestDTO requestDTO) {
		if(CollectionUtils.isEmpty(passengerSegments) || CollectionUtils.isEmpty(cancelledQulifierIds)
				|| requestDTO == null || CollectionUtils.isEmpty(requestDTO.getCancelInfos())) {
			return Collections.emptyList();
		}
		
		List<SsrUpdateModel> ssrAddModels = new ArrayList<>();
		List<CancelAssistanceInfoDTO> requestCancelInfos = requestDTO.getCancelInfos();
		for(PassengerSegment passengerSegment : passengerSegments) {
			if(passengerSegment == null || CollectionUtils.isEmpty(passengerSegment.getSpecialServices())) {
				continue;
			}
			
			String passengerId = passengerSegment.getPassengerId();
			String segmentId = passengerSegment.getSegmentId();
			
			for(SpecialService specialService : passengerSegment.getSpecialServices()) {
				if(specialService == null) {
					continue;
				}
				if(cancelledQulifierIds.contains(String.valueOf(specialService.getQulifierId())) && !idsInCancelRequest(passengerId, segmentId, requestCancelInfos)) {
					ssrAddModels.add(buildSsrAddModel(specialService, passengerId, segmentId));
				}
			}
		}
		return ssrAddModels;
	}
	
	/**
	 * Build ssrUpdateModel
	 * 
	 * @param specialService
	 * @param passengerId
	 * @param segmentId
	 * @return
	 */
	private SsrUpdateModel buildSsrAddModel(SpecialService specialService, String passengerId, String segmentId) {
		SsrUpdateModel ssrUpdateModel = new SsrUpdateModel();
		
		ssrUpdateModel.setPassengerId(passengerId);
		ssrUpdateModel.setSegmentId(segmentId);
		
		List<SsrInfoModel> ssrInfoModels = new ArrayList<>();
		SsrInfoModel ssrInfoModel = new SsrInfoModel();
		ssrInfoModel.setFreeText(specialService.getFreeText());
		ssrInfoModel.setSsrCode(specialService.getCode());
		ssrInfoModels.add(ssrInfoModel);
		ssrUpdateModel.setUpdateSsrList(ssrInfoModels);
		
		return ssrUpdateModel;
	}


	/**
	 * PassengerId & segmentId is in cancel request or not.
	 * 
	 * @param passengerId
	 * @param segmentId
	 * @param requestCancelInfos
	 * @return
	 */
	private boolean idsInCancelRequest(String passengerId, String segmentId, List<CancelAssistanceInfoDTO> requestCancelInfos) {
		return requestCancelInfos.stream().anyMatch(info -> Objects.equals(info.getPassengerId(), passengerId)
				&& Objects.equals(info.getSegmentId(), segmentId));
	}

	/**
	 * Remove assistance SSR by qulifierIds
	 * 
	 * @param rloc
	 * @param qulifierIds
	 * @param session
	 * @throws BusinessBaseException 
	 */
	private void removeAssistanceSSR(String rloc, List<String> qulifierIds, Session session) throws BusinessBaseException {
		Map<String, List<String>> deleteMap = new HashMap<>();
		deleteMap.put(OneAConstants.OT_QUALIFIER, qulifierIds);
		
		deletePnrService.deletePnrWithoutParser(rloc, deleteMap, session);
	}

	/**
	 * Get list of qulifier IDs SSR which should be removed from 1A
	 * Checking:
	 * 1. passenger and segment must be found in booking
	 * 2. passenger must have special service in the segment
	 * 
	 * @param booking
	 * @param cancelInfos
	 * @return
	 * @throws ExpectedException 
	 */
	private List<String> getCancelSsrQulifierIds(Booking booking, List<CancelAssistanceInfoDTO> cancelInfos) throws ExpectedException {
		List<String> qulifierIds = new ArrayList<>();

		String rloc = booking.getOneARloc();	
		for(CancelAssistanceInfoDTO cancelInfo : cancelInfos) {
			String passengerId = cancelInfo.getPassengerId();
			String segmentId = cancelInfo.getSegmentId();
			
			//1. passenger and segment must be found in booking
			PassengerSegment passegnerSegment = booking.getPassengerSegments().stream().filter(ps -> ps != null 
					&& passengerId.equals(ps.getPassengerId())
					&& segmentId.equals(ps.getSegmentId())).findFirst().orElse(null);
			if(passegnerSegment == null) {
				throw new ExpectedException(String.format("Cancel Assistance => failure! No passener[%s] with related segment[%s] found in booking[%s]",
						passengerId, segmentId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_CANCEL_NO_PASSENGER_SEGMENT_FOUND));
			}
			
			//2. passenger must have special service in the segment
			List<SpecialService> specialServices = passegnerSegment.getSpecialServices();
			if(CollectionUtils.isEmpty(specialServices)) {
				throw new ExpectedException(String.format("Cancel Assistance => failure! passener[%s] don't have any special services at segment[%s] in booking[%s]",
						passengerId, segmentId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_CANCEL_NO_ASSISTANCE_FOUND));
			}
			
			qulifierIds.addAll(getAndCheckQulifierIds(cancelInfo.getSpecialServiceCodes(), specialServices, passengerId, segmentId, rloc));
		}
		return qulifierIds;
	}


	/**
	 * Get 1 passenger's special service SSR qulifier IDs(should be cancelled) in 1 segment
	 * Checking: 
	 * 1. code must be exist in booking
	 * 2. code's status must be confirm("CF")
	 * 
	 * @param cancelCodes
	 * @param specialServices
	 * @param passengerId
	 * @param segmentId
	 * @param rloc
	 * @return
	 * @throws ExpectedException
	 */
	private List<String> getAndCheckQulifierIds(List<String> cancelCodes, List<SpecialService> specialServices,
			String passengerId, String segmentId, String rloc) throws ExpectedException {
		List<String> qulifierIds = new ArrayList<>();
		for(String cancelCode : cancelCodes) {
			if(StringUtils.isEmpty(cancelCode)) {
				continue;
			}
			
			//1. code must be exist in booking
			List<SpecialService> services = specialServices.stream().filter(s -> s != null && cancelCode.equals(s.getCode())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(services)) {
				throw new ExpectedException(String.format("Cancel Assistance => failure! passener[%s] don't have specialServiceCode[%s] at segment[%s] in booking[%s]",
						passengerId, cancelCode, segmentId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_CANCEL_INVALID_SPECIAL_CODE));
			}
			
			//2. code's status must be confirm("CF")
			/*if(!MMBBizruleConstants.SPECIAL_SERVICE_STATUS_CONFIRM.equalsIgnoreCase(specialService.getStatus())) {
				throw new UnexpectedException(String.format("Cancel Assistance => failure! passener's[%s] specialServiceCode's[%s] status[%s] is not confirmed at segment[%s] in booking[%s]",
						passengerId, cancelCode, specialService.getStatus(), segmentId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_CANCEL_INVALID_SPECIAL_CODE));
			}*/
			
			for(SpecialService specialService : services) {
				qulifierIds.add(String.valueOf(specialService.getQulifierId()));		
			}
		}
		
		return qulifierIds.stream().distinct().collect(Collectors.toList());
	}

	private void verifyUpdateAssistanceRequest(List<UpdateAssistanceInfoDTO> updateAssistanceInfoDTOs, List<CancelAssistanceInfoDTO> cancelAssistanceInfoDTOs, Booking booking) throws ExpectedException {
		
		for(UpdateAssistanceInfoDTO updateAssistanceInfo: updateAssistanceInfoDTOs) {		
			String passengerId = updateAssistanceInfo.getPassengerId();
			String segmentId = updateAssistanceInfo.getSegmentId();
			
			//1. passenger and segment must be found in booking
			PassengerSegment passegnerSegment = booking.getPassengerSegments().stream().filter(ps -> ps != null 
					&& passengerId.equals(ps.getPassengerId())
					&& segmentId.equals(ps.getSegmentId())).findFirst().orElse(null);
			if(passegnerSegment == null) {
				throw new ExpectedException(String.format("Update Assistance => failure! No passener[%s] with related segment[%s] found in booking[%s]",
						passengerId, segmentId, booking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_UPDATE_INVALID_PARAMETERS_RECEIVED));
			}
			
			//2. passenger shouldn't have this special service in the segment
			List<String> mobilityList = Arrays.asList(OneAConstants.MOBILITY_ASSIST_ARRAY);
			List<String> assistList = Arrays.asList(OneAConstants.ASSIST_COLLECTION);
			if(CollectionUtils.isEmpty(passegnerSegment.getSpecialServices())) {
				continue;
			}

			handleConflictSSR(cancelAssistanceInfoDTOs, mobilityList, assistList, updateAssistanceInfo, passegnerSegment);
			
		}
		
	}


	public void handleConflictSSR(List<CancelAssistanceInfoDTO> cancelAssistanceInfoDTOs, List<String> mobilityList,
			List<String> assistList, UpdateAssistanceInfoDTO updateAssistanceInfo, PassengerSegment passegnerSegment)
			throws ExpectedException {
		List<SpecialService> specialServices = BookingBuildUtil.removeDubplicateSepcialService(passegnerSegment.getSpecialServices());
		Iterator<SsrInfoDTO> ssrIterator = updateAssistanceInfo.getUpdateSsrList().iterator();
		
		while (ssrIterator.hasNext()) {
			SsrInfoDTO ssrInfoDTO = ssrIterator.next();
			List<String> assistArray = specialServices.stream()
					.filter(service -> assistList.contains(service.getCode())).map(SpecialService::getCode)
					.collect(Collectors.toList());
			// If update mobility assistance and PNR has already had mobility special service, remove this update SSR code
			if (mobilityList.contains(ssrInfoDTO.getSsrCode())
					&& specialServices.stream().anyMatch(service -> mobilityList.contains(service.getCode()))) {
				ssrIterator.remove();
			} 	
			// There is SSR conflict--between visual & hearing & mobility. Then find cancel info
			else if (!assistArray.isEmpty() && !OneAConstants.MAAS.equals(ssrInfoDTO.getSsrCode())) {
				CancelAssistanceInfoDTO cancelAssistanceInfoDTO = Optional.ofNullable(cancelAssistanceInfoDTOs)
						.orElse(Collections.emptyList()).stream()
						.filter(cancelInfo -> cancelInfo.getPassengerId().equals(passegnerSegment.getPassengerId())
								&& cancelInfo.getSegmentId().equals(passegnerSegment.getSegmentId()))
						.findFirst().orElse(null);
				// If we won't cancel them, throw exception
				if (cancelAssistanceInfoDTO == null || !assistArray.stream()
						.allMatch(assist -> cancelAssistanceInfoDTO.getSpecialServiceCodes().contains(assist))) {
					throw new ExpectedException(String.format("Update Assistance => failure! Assistance conflict in passener[%s] in segment[%s]",
									passegnerSegment.getPassengerId(), passegnerSegment.getSegmentId()),new ErrorInfo(ErrorCodeEnum.ERR_ASSISTANCE_UPDATE_ASSISTANCE_ALREADY_EXIST));
				}
			} 
			// If already has MAAS in special service, remove update MAAS
			else if (OneAConstants.MAAS.equals(ssrInfoDTO.getSsrCode())
					&& specialServices.stream().anyMatch(service -> OneAConstants.MAAS.equals(service.getCode()))) {
				ssrIterator.remove();
			}
		}
	}
	
	/**
	 * Check if departure time is within check in open window, 24 hours or 90 minutes
	 * @param segment
	 * @param openWindow
	 */
	private void buildFortyEightFlag(Segment segment, RequestAssistanceSegmentDTO specialServiceSegment) {
		if(segment.getDepartureTime() != null && !StringUtils.isEmpty(segment.getDepartureTime().getTime())) {
			try {
				Date std = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getScheduledTime(), segment.getDepartureTime().getTimeZoneOffset());
				Date fortyEightDate = new Date();
				Date currentDate = new Date();
				currentDate.setTime(System.currentTimeMillis());
                fortyEightDate.setTime(System.currentTimeMillis() + bizRuleConfig.getCutOffTimeForRequestAssistance());
				
				if((std.after(currentDate) && std.before(fortyEightDate)) || std.compareTo(fortyEightDate) == 0) {
					specialServiceSegment.setWithinFortyEightHrs(true);
				} else {
					specialServiceSegment.setWithinFortyEightHrs(false);
				}
			} catch (ParseException e) {
				logger.error("Error to convert departure time");
			}
		}
	}
	
	/**
	 * Update assistance SSR
	 * @param requestDTO
	 * @param session
	 * @throws BusinessBaseException
	 */
	private void updateAssistanceSSR(UpdateAssistanceRequestDTO requestDTO, Session session) throws BusinessBaseException {
		List<SsrUpdateModel> ssrUpdateModels = buildSsrUpdateModel(requestDTO);
		addSpecialServiceSSR(requestDTO.getRloc(), session, ssrUpdateModels);
	}
	
	/**
	 * Adding sepecialService SSR into 1A PNR
	 * 
	 * @param rloc
	 * @param session
	 * @param ssrUpdateModels
	 * @throws ExpectedException
	 * @throws BusinessBaseException
	 */
	private void addSpecialServiceSSR(String rloc, Session session, List<SsrUpdateModel> ssrAddModels) throws BusinessBaseException {
		PNRAddMultiElements request = addSsrBuilder.buildRequest(rloc, ssrAddModels, session);
		addPnrElementsInvokeService.addMutiElements(request, session);
	}

	/**
	 * Build requestDTO to update SSR model
	 * Null values are excluded in DTO model
	 * @param requestDTO
	 */
	public List<SsrUpdateModel> buildSsrUpdateModel(UpdateAssistanceRequestDTO requestDTO) {
		List<SsrUpdateModel> ssrUpdateModels = new ArrayList<>();
		for(UpdateAssistanceInfoDTO updateAssistanceInfoDTO: requestDTO.getUpdateAssistanceInfo()) {
			SsrUpdateModel ssrUpdateModel = new SsrUpdateModel();
			List<SsrInfoModel> ssrInfoModels = new ArrayList<>();
			ssrUpdateModel.setPassengerId(updateAssistanceInfoDTO.getPassengerId());
			ssrUpdateModel.setSegmentId(updateAssistanceInfoDTO.getSegmentId());
			ssrUpdateModel.setUpdateSsrList(ssrInfoModels);
			for(SsrInfoDTO ssrInfoDTO: updateAssistanceInfoDTO.getUpdateSsrList()) {
				SsrInfoModel ssrInfo = new SsrInfoModel();
				ssrInfo.setFreeText(ssrInfoDTO.getAdditionalInfo());
				ssrInfo.setSsrCode(ssrInfoDTO.getSsrCode());
				ssrInfoModels.add(ssrInfo);
			}
			ssrUpdateModels.add(ssrUpdateModel);
		}
		return ssrUpdateModels;
	}
}
