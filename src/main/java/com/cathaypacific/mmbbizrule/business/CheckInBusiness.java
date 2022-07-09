package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.checkin.accept.CheckInAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.checkin.cancel.CancelCheckInRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel.CanCancelCheckInDetailResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInResponseDTOV2;

public interface CheckInBusiness {
	
	/**
	 * Do check in accept
	 * Allocate seat and accept check in
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public CheckInAcceptResponseDTO accept(LoginInfo loginInfo, CheckInAcceptRequestDTO requestDTO) throws BusinessBaseException;
	
	/**
	 * Get can cancel check-in related booking details
	 * 
	 * @param loginInfo
	 * @param rloc
	 * @param journeyId
	 * @return
	 * @throws BusinessBaseException
	 */
	public CanCancelCheckInDetailResponseDTO canCancel(LoginInfo loginInfo, String rloc, String journeyId) throws BusinessBaseException;
	
	/**
	 * Do cancel check-in
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public CancelCheckInResponseDTOV2 cancel(LoginInfo loginInfo, CancelCheckInRequestDTOV2 requestDTO) throws BusinessBaseException;
	
}
