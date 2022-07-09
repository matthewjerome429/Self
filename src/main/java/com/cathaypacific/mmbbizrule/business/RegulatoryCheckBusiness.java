package com.cathaypacific.mmbbizrule.business;

import java.util.List;
import java.util.Map;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.checkin.regulatorycheck.RegcheckRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckCprJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO;
import com.cathaypacific.olciconsumer.model.response.ErrorInfo;
import com.cathaypacific.olciconsumer.model.response.PassengerDTO;

public interface RegulatoryCheckBusiness {

	/**
	 * regulatory check of booking, call olci to do regulatory check
	 * @param requestDTO
	 * @throws BusinessBaseException 
	 */
	public RegCheckResponseDTO regulatoryCheck(RegcheckRequestDTO requestDTO,LoginInfo loginInfo) throws BusinessBaseException;
	
	/**
	 * Get adc message from redis
	 * @param rloc
	 * @param loginInfo
	 * @return
	 */
	public List<AdcMessageDTO> getAdcMessageFromCache(String rloc, LoginInfo loginInfo);

	/**
	 * build Error To MapModel
	 * @param passengers
	 * @param booking
	 * @return
	 */
	Map<String, List<ErrorInfo>> buildErrorToMapModel(List<PassengerDTO> passengers, Booking booking);

	/**
	 * build Adc Message For Cache
	 * @param passengers
	 * @param jounrey
	 * @return
	 */
	List<AdcMessage> buildAdcMessageForCache(List<PassengerDTO> passengers, Journey jounrey);

	/**
	 * build RegCheckCprJourneyDTO
	 * @param regInterActiveErrorMapping
	 * @param adcMessageList
	 * @param jounrey
	 * @return
	 */
	RegCheckCprJourneyDTO buildRegCheckCprJourneyDTO(Map<String, List<ErrorInfo>> regInterActiveErrorMapping,
			List<AdcMessage> adcMessageList, Journey jounrey);
}
