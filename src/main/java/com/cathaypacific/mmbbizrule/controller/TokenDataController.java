package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mmbbizrule.business.TokenDataBusiness;
import com.cathaypacific.mmbbizrule.dto.request.tokendata.store.StoreTokenDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.tokendata.transfer.TransferTokenDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.tokendata.store.StoreTokenDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.tokendata.transfer.TransferTokenDataResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Token Data APIs"}, value = "Token Data APIs")
@RestController
@RequestMapping(path = "/v1/tokendata")
public class TokenDataController {
	
	@Autowired
	private TokenDataBusiness tokenDataBusiness;
	
	@PostMapping("/store")
	@ApiOperation(value = "Store token data", response = StoreTokenDataResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public StoreTokenDataResponseDTO storeTokenData(@RequestHeader(name = "MMB-Token") String mmbToken, @RequestBody @Validated StoreTokenDataRequestDTO request) {
		return tokenDataBusiness.storeTokenData(mmbToken, request.getRloc());
	}
	
	@PutMapping("/transfer")
	@ApiOperation(value = "Transfer token data to current token using the key", response = TransferTokenDataResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public TransferTokenDataResponseDTO transferTokenData(@RequestHeader(name = "MMB-Token") String mmbToken, @RequestBody @Validated TransferTokenDataRequestDTO request) {
		return tokenDataBusiness.transferTokenData(mmbToken, request.getKey());
	}
}
