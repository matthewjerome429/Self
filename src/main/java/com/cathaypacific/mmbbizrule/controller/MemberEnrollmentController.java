package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.MemberEnrollmentBusiness;
import com.cathaypacific.mmbbizrule.dto.request.ruenrollment.ActiveRuEnrolRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.ruenrollment.ActiveRuEnrolResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Member Enrollment APIs"}, value = "Member Enrollment APIs")
@RestController
@RequestMapping(path = "/v1/member")
public class MemberEnrollmentController {
	
	@Autowired
	private MemberEnrollmentBusiness memberEnrollmentBusiness;
	
	@PostMapping("/ru/enrol")
	@ApiOperation(value = "Enrol RU account", response = ActiveRuEnrolResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public ActiveRuEnrolResponseDTO enrolActiveRuAccount(@RequestBody @Validated ActiveRuEnrolRequestDTO request, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return memberEnrollmentBusiness.enrolActiveRuAccount(request, loginInfo);
	}

}
