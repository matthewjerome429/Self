package com.cathaypacific.mmbbizrule.v2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.RegulatoryCheckBusiness;
import com.cathaypacific.mmbbizrule.dto.request.checkin.regulatorycheck.RegcheckRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckResponseDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

//@Api(tags = {"Check-in related Operations"})
@RestController
@RequestMapping(path = "/v2/checkin")
public class RegulatoryCheckController {
	
	@Autowired
	private RegulatoryCheckBusiness regulatoryCheckBusiness;
	
	@PutMapping("/regulatorycheck")
	@ApiOperation(value = "Regulatorycheck check for the journry, please note this api may update booking(copy travel), you can re-retrieve booking to get the lastest booking info. ", response = RegCheckResponseDTO.class, produces = "application/json",tags= "Check-in related Operations")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
		})
	@CheckLoginInfo
	public RegCheckResponseDTO regulatoryCheck(@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @RequestBody RegcheckRequestDTO requestDTO) throws BusinessBaseException {
		 
		 return regulatoryCheckBusiness.regulatoryCheck(requestDTO,loginInfo);
	}
	
	@GetMapping("/adcmessage")
	@ApiOperation(value = "get adc message from redis cache", response = AdcMessageDTO.class, produces = "application/json",tags= "Check-in related Operations")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
	})
	@CheckLoginInfo
	public List<AdcMessageDTO> getAdcMessage(String rloc, @LoginInfoPara @ApiIgnore LoginInfo loginInfo){
		 return regulatoryCheckBusiness.getAdcMessageFromCache(rloc, loginInfo);
	}
}
