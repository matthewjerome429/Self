package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.business.OneAOperationBusiness;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddSkRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.oneaoperation.OneAOperationResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"1A Operations"}, description = "1A Operation APIs")
@RestController
@RequestMapping(path = "/v1/onea")
public class OneAController {
	
	@Autowired
	private OneAOperationBusiness oneAOperationBusiness;
	
	@PostMapping("/addsk")
	@ApiOperation(value = "add SK element", response = OneAOperationResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = false, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public OneAOperationResponseDTO addSkElements(@RequestBody AddSkRequestDTO request) {
		return oneAOperationBusiness.addSkElements(request);
	}
	
	@PostMapping("/addrm")
	@ApiOperation(value = "add RM element", response = OneAOperationResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = false, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public OneAOperationResponseDTO addRmElements(@RequestBody AddRmRequestDTO request) {
		return oneAOperationBusiness.addRmElements(request);
	}

}
