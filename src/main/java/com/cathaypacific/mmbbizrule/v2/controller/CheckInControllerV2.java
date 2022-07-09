package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.CheckInBusiness;
import com.cathaypacific.mmbbizrule.dto.request.checkin.accept.CheckInAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.checkin.cancel.CancelCheckInRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel.CanCancelCheckInDetailResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInResponseDTOV2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Check-in related Operations"}, value = "Accept, Cancel, canCancel")
@RestController
@RequestMapping(path = "/v2/checkin")
public class CheckInControllerV2 {
	
	@Autowired
	private CheckInBusiness checkInBusiness;
	
	@PostMapping("/accept")
	@ApiOperation(value = "Do check in Operation", response = CheckInAcceptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
		})
	@CheckLoginInfo
	public CheckInAcceptResponseDTO accept(@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @Validated @RequestBody CheckInAcceptRequestDTO requestDTO) throws BusinessBaseException {
		return checkInBusiness.accept(loginInfo, requestDTO);
	}
	
	@GetMapping("/cancancel/{rloc}")
	@ApiOperation(value = "Get list of passenger with check-in infos", response = CanCancelCheckInDetailResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "journeyId", value = "journey id", required = true, dataType = "string", paramType = "query", defaultValue = "")
		})
	@CheckLoginInfo
	public CanCancelCheckInDetailResponseDTO getCanCancelCheckInDetails(@PathVariable(value = "rloc", required = true) String rloc,
			@RequestParam(value = "journeyId", required = true) String journeyId, @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException {
		return checkInBusiness.canCancel(loginInfo, rloc, journeyId);
	}
	
	@PostMapping("/cancel")
	@ApiOperation(value = "Do cancel check in Operation", response = CheckInAcceptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
		})
	@CheckLoginInfo
	public CancelCheckInResponseDTOV2 cancel(@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @Validated @RequestBody CancelCheckInRequestDTOV2 requestDTO) throws BusinessBaseException {
		return checkInBusiness.cancel(loginInfo, requestDTO);
	}
	
}
