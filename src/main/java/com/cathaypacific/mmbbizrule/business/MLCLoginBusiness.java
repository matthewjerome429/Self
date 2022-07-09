package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.response.mmbtoken.MMBTokenResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.verifytoken.VerifytokenResponseDTO;

public interface MLCLoginBusiness {

	/**
	 * Verify MLC token by calling MLC web-service.
	 * 
	 * @param domain
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	public VerifytokenResponseDTO verifyMLCToken(final String mlcToken, final String mmbToken, final String acceptLanguage) throws BusinessBaseException;

	/**
	 * create loginInfo for member and return mmbtoken
	 * 
	 * @param mmbToken
	 * @param memberId
	 * @return MMBTokenResponseDTO
	 * @throws BusinessBaseException 
	 */
	public MMBTokenResponseDTO getMMBTokenWithMemberId(String mmbToken, String memberId) throws BusinessBaseException;

}