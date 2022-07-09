package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BaggageBusiness;
import com.cathaypacific.mmbbizrule.dto.request.baggage.BaggageAllowanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.baggage.ExtraBaggageRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageAllowanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.ExtraBaggageResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = { "Baggage" }, value = "Booking Info With Baggage")
@RestController
@RequestMapping(path = "/v1")
public class BaggageController {
	
	@Autowired
	private BaggageBusiness baggageBusiness;
	
	@GetMapping("/baggage/baggageallowance")
	@ApiOperation(value = "Get baggage allowance info of booking with check in baggage and cabin baggage",
			response = BaggageAllowanceResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "Booking reference", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
			})
	@CheckLoginInfo
	public BaggageAllowanceResponseDTO getBaggageAllowance(@ApiIgnore @Validated BaggageAllowanceRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {

		return baggageBusiness.getBaggageAllowance(requestDTO, loginInfo);
	}

	@GetMapping("/baggage/extrabaggage")
	@ApiOperation(value = "Get extra baggage info of booking with eligible product or ineligible reason",
			response = ExtraBaggageResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "Booking reference", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
			})
	@CheckLoginInfo
	public ExtraBaggageResponseDTO getExtraBaggage(@ApiIgnore @Validated ExtraBaggageRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {

		return baggageBusiness.getExtraBaggage(requestDTO, loginInfo);
	}

}
