package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.RequestAssistanceBusiness;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.UpdateAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance.CancelAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistanceDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.UpdateAssistanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.cancelassistance.CancelAssistanceResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = {"Special Service Controller"})
@RequestMapping(path = "/v1")
public class SpecialServiceController {
	
	@Autowired
	private RequestAssistanceBusiness requestAssistanceBusiness;
	
	@GetMapping("/assistance/{rloc}")
	@ApiOperation(value = "Get special service informations", response = RequestAssistanceDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc, please use 1A rloc.", required = true, dataType = "string", paramType = "path", defaultValue = "")
		})
	@CheckLoginInfo
	public RequestAssistanceDTO getSpecialServiceInfo(@PathVariable String rloc, @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException {
		return requestAssistanceBusiness.getRequestAssistanceInfo(rloc, loginInfo);
	}
	
	@PutMapping("/assistance/update")
	@ApiOperation(value = "Update assistance", response = UpdateAssistanceResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="UpdateAssistanceRequestDTO.rloc", argIndex = 0)
	public UpdateAssistanceResponseDTO updateAssistance(@RequestBody @Validated UpdateAssistanceRequestDTO updateAssistanceRequestDTO , @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException{
		return requestAssistanceBusiness.updateAssistance(updateAssistanceRequestDTO, loginInfo);
	}
	
	@PostMapping("/assistance/cancel")
	@ApiOperation(value = "Cancel assistance", response = CancelAssistanceResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public CancelAssistanceResponseDTO cancelAssistance(@RequestBody @Validated CancelAssistanceRequestDTO cancelAssistanceRequestDTO, @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException {
		return requestAssistanceBusiness.cancelAssistance(cancelAssistanceRequestDTO, loginInfo);
	}
}
