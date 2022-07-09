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
import com.cathaypacific.mmbbizrule.business.TicketEmailBusiness;
import com.cathaypacific.mmbbizrule.dto.request.email.SendEmailRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.SendEmailReponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Send Email"}, description = "Send Email API")
@RestController
@RequestMapping(path = "/v1")
public class TicketEmailController {
	
	@Autowired
	private TicketEmailBusiness emailBusinessImpl;
	
	@PostMapping("/sendticketemail")
	@ApiOperation(value = "send eticket email", response = SendEmailReponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public SendEmailReponseDTO sendTicketEmail(@RequestBody @Validated SendEmailRequestDTO requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return emailBusinessImpl.sendTicketEmail(requestDTO, loginInfo);
	}
	
}
