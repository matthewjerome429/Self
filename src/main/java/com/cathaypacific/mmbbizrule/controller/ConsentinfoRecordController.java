package com.cathaypacific.mmbbizrule.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentDeleteRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentsDeleteRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrAndConsentRecordRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentsDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.service.impl.ConsentInfoServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
public class ConsentinfoRecordController {

	@Autowired
	private RetrievePnrBusiness retrievePnrService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private ConsentInfoServiceImpl consentInfoService;
	
	private static LogAgent logger = LogAgent.getLogAgent(ConsentinfoRecordController.class);
	
	@GetMapping("/booking/consentinforecord")
	@ApiOperation(value = " consent info Record", response = ConsentInfoRecordResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "consentBoxCheck", value = "consent Box Check", dataType = "boolean" , paramType = "query", defaultValue = "false"),
	    @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public ConsentInfoRecordResponseDTO consentInfoRecording(@ApiIgnore @Validated RetrievePnrAndConsentRecordRequestDTO requestDTO,@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {

		@SuppressWarnings("unchecked")
		List<String> rlocs = mbTokenCacheRepository.get(loginInfo.getMmbToken(), TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null,
				ArrayList.class);
		
		if(CollectionUtils.isEmpty(rlocs)) {
			logger.info("No rloc info found, no need to log ");
			return new ConsentInfoRecordResponseDTO();
		}
		// get the first rloc
		String rloc = rlocs.get(0);

		return retrievePnrService.consentInfoRecord(loginInfo, rloc, MMBUtil.getCurrentAcceptLanguage());
	}
	
	@PutMapping("/common/consentinforecord")
	@ApiOperation(value = "common consent api", response = ConsentCommonRecordResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public ConsentCommonRecordResponseDTO commonConsentInfoRecording(@RequestBody @Validated ConsentAddRequestDTO requestDTO,@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {

		@SuppressWarnings("unchecked")
		List<String> rlocs = mbTokenCacheRepository.get(loginInfo.getMmbToken(), TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null,
				ArrayList.class);		
		if(CollectionUtils.isEmpty(rlocs) || !rlocs.contains(requestDTO.getRloc())) {
			logger.info("No rloc info found, no need to log ");
			return new ConsentCommonRecordResponseDTO();
		}
		// get the first rloc
		

		return retrievePnrService.saveConsentCommon(requestDTO, loginInfo, requestDTO.getRloc(), MMBUtil.getCurrentAcceptLanguage());
	}
	
	@DeleteMapping("/delete/consent")
	@ApiOperation(value = "Delete consent API", response = ConsentDeleteResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
	})
	@CheckLoginInfo
	public ConsentDeleteResponseDTO deleteConsent(@RequestBody @Validated ConsentDeleteRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return consentInfoService.deleteConsentCommon(requestDTO.getId());
	}
	
	@DeleteMapping("/delete/consents")
	@ApiOperation(value = "Delete consent list API", response = ConsentDeleteResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
	})
	@CheckLoginInfo
	public ConsentsDeleteResponseDTO deleteConsents(@RequestBody @Validated ConsentsDeleteRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return consentInfoService.deleteConsentCommons(requestDTO);
	}
	
}
