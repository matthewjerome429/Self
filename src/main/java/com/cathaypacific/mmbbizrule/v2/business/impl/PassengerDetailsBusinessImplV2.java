package com.cathaypacific.mmbbizrule.v2.business.impl;

import java.util.List;

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
import com.cathaypacific.mbcommon.model.common.TokenMaskInfo;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.handler.ValidateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.v2.business.PassengerDetailsBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails.PassengerDetailsResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.DTOConverterV2;
import com.cathaypacific.mmbbizrule.v2.handler.MaskHelperV2;
import com.cathaypacific.mmbbizrule.v2.service.UpdatePassengerServiceV2;
import com.cathaypacific.mmbbizrule.v2.service.UpdatePassengerServiceV2.TransferStatus;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;
/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info of PNR
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:52:11 PM
 * @version V1.0
 */
@Service
public class PassengerDetailsBusinessImplV2 implements PassengerDetailsBusinessV2 {
	private static LogAgent logger = LogAgent.getLogAgent(PassengerDetailsBusinessImplV2.class);
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private UpdatePassengerServiceV2 updatePassengerServiceV2;
	
	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private DTOConverterV2 dtoConverterV2;
	
	@Autowired
	private MaskHelper maskHelper;
	
	@Autowired
	private MaskHelperV2 maskHelperV2;
	
	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;

	@Autowired
	private ValidateHelper validateHelper;

	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Override
	public PassengerDetailsResponseDTOV2 updatePaxDetails(LoginInfo loginInfo,UpdatePassengerDetailsRequestDTOV2 requestDTO, BookingBuildRequired required) throws BusinessBaseException {
		
		saveJourneyId(requestDTO.getJourneyId(), requestDTO.getRloc());
		
		PassengerDetailsResponseDTOV2 response = new PassengerDetailsResponseDTOV2();
		// unmask the request
		TokenMaskInfo tokenMaskInfo = unmaskBookingInfo(requestDTO);
		// after unmasking the fields, validate them
		List<ErrorInfo> errors = validateMaskFields(requestDTO);
		// if there's validation error, return the error
		if (!CollectionUtils.isEmpty(errors)) {
			response.setErrors(errors);
			return response;
		}
		
		/** 1.retrieve PNR **/
		String rloc = requestDTO.getRloc();
		// If got cpr in redis is false, do not make the cpr check
		if (!BooleanUtils.isTrue(mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), 
				TokenCacheKeyEnum.GET_CPR_SUCCESS, requestDTO.getRloc(), Boolean.class))) {
			required.setCprCheck(false);
		}
		
		// set useCprSession = true, will get cached CPR from OLCi service
		required.setUseCprSession(true);
		
		//set useCprSession = false, will update FFP
		if (StringUtils.equalsIgnoreCase(requestDTO.getUpdateType(), MMBBizruleConstants.UPDATE_TYPE_FFP)) {
			required.setUseCprSession(false);
		}
		
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
		FlightBookingDTOV2 flightBookingDTO;
		// check companies of segments
		checkSegmentCompanies(booking);
		
		// primary Passenger Identification By InFo and find primary pax for member login
		RetrievePnrPassenger primaryPax = primaryPaxCheck(loginInfo, retrievePnrBooking);
		
		TransferStatus ffpTransferStatus = new TransferStatus();
		TransferStatus travelDocTransferStatus = new TransferStatus();
		/** 2.update passenger **/
		retrievePnrBooking = updatePassengerServiceV2.updatePassenger(requestDTO, retrievePnrBooking, booking, ffpTransferStatus, travelDocTransferStatus, response);
		// If something goes wrong when update to OLCI, return error.
		if(retrievePnrBooking == null) {
			return response;
		}else if(StringUtils.equalsIgnoreCase(requestDTO.getUpdateType(), MMBBizruleConstants.UPDATE_TYPE_CONTACT_INFO) || CollectionUtils.isEmpty(response.getErrors())) {
			response.setErrors(null);
		}
		updateTokenMaskInfo(tokenMaskInfo, rloc);
		
		/** 3.build booking **/
		// primary Passenger identification for member login
		if (primaryPax != null){
			primaryPaxCheckForMemberLogin(rloc, primaryPax, loginInfo, retrievePnrBooking);
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		//build booking
		booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
		
		//convert to DTO V2
		flightBookingDTO = dtoConverterV2.convertToBookingDTO(booking, loginInfo);
		
		//mask data
        maskBookingInfo(loginInfo.getMmbToken(), flightBookingDTO);
			
		//set DTO to response
		response.setBooking(flightBookingDTO);
		response.findProductLevelTransfer().findFfp().setNeedTransfer(ffpTransferStatus.isNeedTransfer());
		response.findProductLevelTransfer().findFfp().setAnySectorFail(ffpTransferStatus.isAnyTransferFailed());
		response.findProductLevelTransfer().findTravelDoc().setNeedTransfer(travelDocTransferStatus.isNeedTransfer());
		response.findProductLevelTransfer().findTravelDoc().setAnySectorFail(travelDocTransferStatus.isAnyTransferFailed());
		
		return response;
	}
	

	/**
	 * Store journey ID to Redis
	 * 
	 * @param journeyId
	 * @param rloc
	 */
	private void saveJourneyId(String journeyId, String rloc) {
		
		if (StringUtils.isNotEmpty(journeyId)) {
			mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.CPR_JOURNEY_ID, rloc, journeyId);
		}
	}
	
	/**
	 * save token mask info into redis
	 * @param tokenMaskInfo
	 */
	private void updateTokenMaskInfo(TokenMaskInfo tokenMaskInfo, String rloc) {
		if (tokenMaskInfo != null && tokenMaskInfo.isMaskInfoUpdated()
				&& !CollectionUtils.isEmpty(tokenMaskInfo.getMaskInfos())) {
			maskHelper.updateTokenMaskInfo(tokenMaskInfo.getMaskInfos(), rloc, MMBUtil.getCurrentMMBToken());
		}
		
	}

	/**
	 * 
	 * @param requestDTO
	 * @throws ExpectedException 
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> validateMaskFields(UpdatePassengerDetailsRequestDTOV2 requestDTO) throws ExpectedException, UnexpectedException {
		return validateHelper.validate(requestDTO, MaskFieldGroup.class);
	}

	/**
	 * primary PaxCheck For MemberLogin
	 * @param rloc 
	 * @param primaryPax
	 * @param loginInfo
	 * @param retrievePnrBooking
	 */
	private void primaryPaxCheckForMemberLogin(String rloc, RetrievePnrPassenger primaryPax, LoginInfo loginInfo,
			RetrievePnrBooking retrievePnrBooking) {
		try {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		} catch (BusinessBaseException e) {
			tempLinkedBookingRepository.addUpdateFfpBooking(rloc, primaryPax.getFamilyName(), primaryPax.getGivenName(), primaryPax.getPassengerID(), loginInfo.getMmbToken());
			logger.warn("Member is trying to remove the membershipNumber: " + loginInfo.getMemberId());
		}
	}

	/**
	 * primary Passenger Identification By InFo and find primary pax for member login
	 * @param loginInfo
	 * @param retrievePnrBooking
	 * @return RetrievePnrPassenger
	 * @throws BusinessBaseException 
	 */
	private RetrievePnrPassenger primaryPaxCheck(LoginInfo loginInfo, RetrievePnrBooking retrievePnrBooking) throws BusinessBaseException {
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			return retrievePnrBooking.getPrimaryPassenger();
		}
		return null;
	}

	/**
	 * check companies of the segments, if there is no any upcoming sector operated or marketed by CX/KA, then cannot update and throw exception
	 * @param booking
	 * @throws BusinessBaseException 
	 */
	private void checkSegmentCompanies(Booking booking) throws BusinessBaseException {
		boolean bookingValid = false;
		if(booking != null && !CollectionUtils.isEmpty(booking.getSegments())) {
			// if the booking is valid (contains upcoming sector operated or marketed by CX/KA)
			bookingValid = booking.getSegments().stream().anyMatch(seg -> seg != null 
					&& !seg.isFlown()
					&& (OneAConstants.COMPANY_CX.equals(seg.getOperateCompany()) || OneAConstants.COMPANY_KA.equals(seg.getOperateCompany()) 
							|| OneAConstants.COMPANY_CX.equals(seg.getMarketCompany()) || OneAConstants.COMPANY_KA.equals(seg.getMarketCompany())));
		}
		
		if(!bookingValid) {
			throw new ExpectedException(
					String.format("There is no any upcoming sector operated or marketed by CX/KA in booking:%s, cannot update passenger info", booking == null ? null : booking.getOneARloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NO_UPCOMING_CXKA_FLIGHT), HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * mask booking dto
	 * @param rloc
	 * @param mmbToken
	 * @param flightBookingDTO
	 * @throws UnexpectedException 
	 */
    private void maskBookingInfo(String mmbToken, FlightBookingDTOV2 flightBookingDTO){
		// mask user info
    	maskHelperV2.mask(flightBookingDTO);
	}

	/**
	 * Unmask passenger details DTO.
	 * 
	 * @param updatePassengerDetailsRequestDTO
	 * @return TokenMaskInfo
	 */
	private TokenMaskInfo unmaskBookingInfo(UpdatePassengerDetailsRequestDTOV2 updatePassengerDetailsRequestDTO) {
		return maskHelperV2.unmask(updatePassengerDetailsRequestDTO);
	}

}
