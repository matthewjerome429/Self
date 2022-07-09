package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.MemberProfileBusiness;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.ProfileTravelDocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.SocialAccountResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.ProfileTravelDocDTO;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Zilong Bu on 12/23/2017.
 */
@RestController
public class MemberProfileController {

	@Autowired
	private MemberProfileBusiness memberProfileBusiness;

	@GetMapping("/v1/member/profilesummary")
	@ApiOperation(value = "Member profile", response = ProfilePersonInfo.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""), })
	@CheckLoginInfo
	public ProfilePersonInfo retrieveMemberProfileSummary(@ApiIgnore @LoginInfoPara LoginInfo loginInfo)
			throws BusinessBaseException {

		return memberProfileBusiness.retrieveMemberProfileSummary(loginInfo.getMemberId(),loginInfo);

	}

	@GetMapping("/v1/member/availabletraveldocs")
	@ApiOperation(value = "Member profile travel docs", response = ProfileTravelDocResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "The MMB memberId", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "origin", value = "The origin flight", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "destination", value = "The destination flight", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "requirePriDocs", value = "require primary docs", required = false, dataType = "boolean", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "requireSecDocs", value = "require secondary docs", required = false, dataType = "boolean", paramType = "query", defaultValue = ""), })
	@CheckLoginInfo
	public ProfileTravelDocResponseDTO getMemberProfileTravelDocs(
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo, String memberId, String origin,
			String destination, boolean requirePriDocs, boolean requireSecDocs) throws BusinessBaseException {


		return memberProfileBusiness.retrieveMemberProfileTravelDoc(loginInfo.getMemberId(), origin, destination, requirePriDocs,
				requireSecDocs,loginInfo);
	}
	
	@GetMapping("/v1/member/socialaccounts")
	@ApiOperation(value = "Member profile social account", response = SocialAccountResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "The MMB memberId", required = true, dataType = "string", paramType = "query", defaultValue = "")})
	@CheckLoginInfo
	public SocialAccountResponseDTO retrieveSocialAccount(@ApiIgnore @LoginInfoPara LoginInfo loginInfo, String memberId) {
		return memberProfileBusiness.retrieveSocialAccount(memberId);
	}
	
	@GetMapping("/v2/member/availabletraveldocs")
	@ApiOperation(value = "Member profile travel docs Version 2", response = ProfileTravelDocDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "The MMB memberId", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "origin", value = "The origin flight", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "destination", value = "The destination flight", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "requirePriDocs", value = "require primary docs", required = false, dataType = "boolean", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "requireSecDocs", value = "require secondary docs", required = false, dataType = "boolean", paramType = "query", defaultValue = ""), })
	@CheckLoginInfo
	public ProfileTravelDocDTO getMemberProfileTravelDocsV2(
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo, String memberId, String origin,
			String destination, boolean requirePriDocs, boolean requireSecDocs) throws BusinessBaseException {
		return memberProfileBusiness.retrieveMemberProfileTravelDocV2(loginInfo.getMemberId(), origin, destination, requirePriDocs,
				requireSecDocs,loginInfo);
	}

}
