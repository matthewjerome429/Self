package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.AcceptFlightRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.ReBookingAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.ProtectFlightInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.RebookAcceptResponseDTO;

public interface ReBookingBusiness {
	
	/**
	 * accept re-booking protection
	 * 
	 * @param requestDTO
	 * @param accessChannel
	 * @return RebookAcceptResponseDTO
	 * @throws BusinessBaseException
	 */
	public RebookAcceptResponseDTO accept(ReBookingAcceptRequestDTO requestDTO, String accessChannel) throws BusinessBaseException;
	
	/**
	 * get protect flight informations
	 * 
	 * @param rloc
	 * @return ProtectFlightInfoResponseDTO
	 * @throws BusinessBaseException
	 */
	public ProtectFlightInfoResponseDTO getProtectFlightInfo(String rloc) throws BusinessBaseException;
	
	/**
	 * accept re-booking protection flight
	 * 
	 * @param requestDTO
	 * @param accessChannel
	 * @return RebookAcceptResponseDTO
	 * @throws BusinessBaseException
	 */
	public RebookAcceptResponseDTO acceptFlight(AcceptFlightRequestDTO requestDTO, String accessChannel) throws BusinessBaseException;

	RebookAcceptResponseDTO testSendBP(String acceptedSegmentId, String rloc, String familyName, String givenName)
			throws BusinessBaseException;
	
}
