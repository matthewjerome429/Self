package com.cathaypacific.mmbbizrule.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.MLCLoginBusiness;
import com.cathaypacific.mmbbizrule.constant.MLCConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.mlc.model.MLCSession;
import com.cathaypacific.mmbbizrule.cxservice.mlc.service.MLCService;
import com.cathaypacific.mmbbizrule.dto.response.mmbtoken.MMBTokenResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.verifytoken.VerifytokenResponseDTO;
import com.cathaypacific.mmbbizrule.handler.LocaleHelper;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.util.LoginInfoUtil;

@Service
public class MLCLoginBusinessImpl implements MLCLoginBusiness{

	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrBusinessImpl.class);

	@Autowired
	private MLCService mlcService;
	
	@Autowired
	private LocaleHelper localeHelper;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	public VerifytokenResponseDTO verifyMLCToken(final String mlcToken, final String mmbToken, String acceptLanguage)
			throws BusinessBaseException {

		VerifytokenResponseDTO response = new VerifytokenResponseDTO();
		response.setRequireLocalConsentPage(localeHelper.localeConsentPageRequired(MMBUtil.getCurrentAcceptLanguage()));
		
		MLCSession mlcSession = null;
		try {

			mlcSession = mlcService.verifyToken(mlcToken);
		} catch (Exception e) {
			throw new UnexpectedException("MLC token verify error.", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW),e);
		}
		if (mlcSession!=null && mlcSession.getStatusCode() == MLCConstants.STATUS_CODE_VALID_TOKEN) {
			logger.info("verify mcl token success!");
			response.setMmbToken(mmbToken);
			response.setLoginSuccess(true);
			storeLoginInfoToRedisForMember(mmbToken, mlcSession);
		} else {
			logger.info("[Member Login Failed]verify mcl token failed! mlc token:"+mlcToken);
			throw new ExpectedException("Invalid MLC token:" + mlcToken,
					new ErrorInfo(ErrorCodeEnum.ERR_MLC_TOKEN_INVALID));
		}

		return response;
	}
	
	private void storeLoginInfoToRedisForMember(String token, MLCSession mlcSession) throws BusinessBaseException {
		LoginInfo loginInfo = LoginInfoUtil.createLoginInfoForMember(token, mlcSession.getSession().getMemID(), mlcSession.getSession().getUserType(), null);
		if (!MMBConstants.RU_MEMBER.equals(loginInfo.getUserType())) {
			loginInfo.setOriginalRuMemberId(getOriginalRuMemberId(token, loginInfo.getMemberId()));
		}
		mbTokenCacheRepository.add(token, TokenCacheKeyEnum.LOGININFO, null, loginInfo);
		logger.info("storeLoginInfoToRedisForMember", "[Member Login Success]Stored login info to redis for member", "Member login", loginInfo, token);
	}

	@Override
	public MMBTokenResponseDTO getMMBTokenWithMemberId(String mmbToken, String memberId) throws BusinessBaseException {
		MMBTokenResponseDTO response = new MMBTokenResponseDTO();
		storeLoginInfoToRedisForMember(mmbToken, memberId);
		response.setMmbToken(mmbToken);
		return response;
	}

	private void storeLoginInfoToRedisForMember(String mmbToken, String memberId) throws BusinessBaseException {
		
		String userType = getUserTypeByMemberId(mmbToken, memberId);
		LoginInfo loginInfo; 
		if(MMBConstants.RU_MEMBER.equals(userType)){
			loginInfo = LoginInfoUtil.createLoginInfoForMember(mmbToken, memberId, userType, null);
		}else{
			loginInfo = LoginInfoUtil.createLoginInfoForMember(mmbToken, memberId, userType, getOriginalRuMemberId(mmbToken, memberId));
		}
	
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.LOGININFO, null,loginInfo);
		logger.info("storeLoginInfoToRedisForMember", "[Member Login Success]Stored login info to redis for member", "Member login", loginInfo, mmbToken);
	}

	/**
	 * Get Original Ru MemberId
	 * @param mmbToken
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException
	 */
	private String getOriginalRuMemberId(String mmbToken, String memberId) throws BusinessBaseException {
		try {
			ProfilePreference profilePreference = retrieveProfileService.retrievePreference(memberId, mmbToken);
			if (profilePreference != null) {
				return profilePreference.getOriginalMemberId();
			}
		} catch (Exception e) {
			logger.info("Failed to retrieve profile for member:" + memberId);
			throw new UnexpectedException("MemberID verify error.", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
		}
		return null;

	}
	
	private String getUserTypeByMemberId(String mmbToken, String memberId)
			throws BusinessBaseException {
		ProfilePersonInfo personInfo = null;
		try {
			personInfo = retrieveProfileService.retrievePersonInfo(memberId, mmbToken);
		} catch (Exception e) {
			logger.info("Failed to retrieve profile for member:"+memberId);
			throw new UnexpectedException("MemberID verify error.", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM),e);
		}
		String userType = null;
		if (null != personInfo && StringUtils.isNotEmpty(personInfo.getTier())){
			if(MMBConstants.RU_MEMBER.equalsIgnoreCase(personInfo.getTier())){
				userType = MMBConstants.RU_MEMBER;
			} else if (MMBConstants.AM_MEMBER.equalsIgnoreCase(personInfo.getTier())){
				userType = MMBConstants.AM_MEMBER;
			} else {
				userType = MMBConstants.MPO_MEMBER;
			}
		} else {
			logger.info("Member profile is not found for memberId:"+memberId);
			throw new ExpectedException("Invalid memberId:" + memberId,
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		return userType;
	}
}