package com.cathaypacific.mmbbizrule.controller.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.business.token.TokenManagementBusiness;
import com.cathaypacific.mmbbizrule.dto.request.session.cacheclear.CacheClearRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.session.OperateResultDTO;
import com.cathaypacific.mmbbizrule.dto.response.session.SessionDurationDTO;
import com.cathaypacific.mmbbizrule.dto.response.session.cacheclear.CacheClearResultDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * this controller is used to get system parameters
 * @author fengfeng.jiang
 *
 */

@Api(tags = {"Session APIs"})
@RestController
@RequestMapping(path = "/v1/session")
public class TokenManagementController {

	@Autowired
	private TokenManagementBusiness tokenManagementBusiness;
 
	
	@GetMapping("/expiration")
	@ApiOperation(value = "Get session expiration time", response = SessionDurationDTO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header")})
	public SessionDurationDTO expiration(){
		SessionDurationDTO sessionDuration = new SessionDurationDTO();
		sessionDuration.setTokenDurationSeconds(tokenManagementBusiness.getExpirationTime());
		return sessionDuration;
	}
	
	@GetMapping("/expirationtime/refresh")
	@ApiOperation(value = "Refresh session expiration time", response = OperateResultDTO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header")})
	public OperateResultDTO refresh(){
		OperateResultDTO result = new OperateResultDTO();
		result.setSuccess(true);
		return result;
	}
	
	@GetMapping("/delete")
	@ApiOperation(value = "Clear user session", response = OperateResultDTO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header")})
	public OperateResultDTO delete(@RequestHeader(name = MMBConstants.HEADER_KEY_MMB_TOKEN_ID,required=false) String mmbToken){
		OperateResultDTO result = new OperateResultDTO();
		
		result.setSuccess(tokenManagementBusiness.delete(mmbToken));
		return result;
	}
	
	@GetMapping("/cache/refresh")
	@ApiOperation(value = "Delete all cache of current session", response = OperateResultDTO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header")})
	public OperateResultDTO cacheRefresh(@RequestHeader(name = MMBConstants.HEADER_KEY_MMB_TOKEN_ID,required=true) String mmbToken ){
		OperateResultDTO result = new OperateResultDTO();
		
		result.setSuccess(tokenManagementBusiness.deleteCache(mmbToken));
		return result;
	}
	
	@PutMapping("/cache/clear")
	@ApiOperation(value = "Clear caches of current session", response = CacheClearResultDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header")})
	public CacheClearResultDTO cacheClear(@RequestHeader(name = MMBConstants.HEADER_KEY_MMB_TOKEN_ID,required=true) String mmbToken, @RequestBody CacheClearRequestDTO request) {
		return tokenManagementBusiness.clearCache(mmbToken, request);
	}
}
