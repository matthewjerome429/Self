package com.cathaypacific.mmbbizrule.business;

import java.io.InputStream;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SPBPRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendEmailRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendSMSRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SPBPResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendEmailResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendSMSResponseDTOV2;

public interface BoardingPassBusiness {
	
	/**
	 * Download Self-print boarding pass
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public SPBPResponseDTOV2 selfPrintBP(LoginInfo loginInfo, SPBPRequestDTOV2 requestDTO) throws BusinessBaseException;
	
	/**
	 * send boarding pass email
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public SendEmailResponseDTOV2 sendBPEmail(LoginInfo loginInfo, SendEmailRequestDTOV2 requestDTO) throws BusinessBaseException;
	
	/**
	 * send boarding pass sms
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public SendSMSResponseDTOV2 sendBPSms(LoginInfo loginInfo, SendSMSRequestDTOV2 requestDTO) throws BusinessBaseException;
	
	/**
	 * get apple pass file stream
	 * @param locale
	 * @param applePassNumber
	 * @param rloc
	 * @return
	 * @throws Exception
	 */
	public InputStream getBPAppleWallet(String locale, String applePassNumber, String rloc) throws Exception;
	
}
