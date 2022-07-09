package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.commonapi.ChangeFlightEligibleBusiness;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightEligibleResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightResponseDTO;
import com.cathaypacific.mmbbizrule.dto.request.changeflight.ChangeFlightDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.changeflight.ChangeFlightDataResponseDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
public class ChangeFlightEligibleController {
	
	@Autowired
	private ChangeFlightEligibleBusiness changeFlightEligibleBusiness;
	
	@GetMapping("/changeflighteligible/{rloc}")
	@ApiOperation(value = "check change flight eligibility", response = ChangeFlightResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public ChangeFlightResponseDTO changeFlightEligibleByRloc(@PathVariable("rloc") String rloc,@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return changeFlightEligibleBusiness.changeFlightEligibleByRloc(rloc,loginInfo);
	}
	
	@PostMapping("/changeflightdata")
	@ApiOperation(value = " retrieve e change flight data", response = ChangeFlightDataResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public ChangeFlightDataResponseDTO getChangeFlightDataByRloc(@RequestBody ChangeFlightDataRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return changeFlightEligibleBusiness.getChangeFlightData(requestDTO, loginInfo);
	}
	
}
