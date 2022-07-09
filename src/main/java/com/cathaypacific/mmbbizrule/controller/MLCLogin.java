package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.MLCLoginBusiness;
import com.cathaypacific.mmbbizrule.dto.response.mmbtoken.MMBTokenResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.verifytoken.VerifytokenResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Member login APIs"}, value = "Member login APIs")
@RestController
@RequestMapping(path = {"/v1", "/mb-bizrule/v1"})
public class MLCLogin {
	
	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrController.class);

	@Autowired
	private MLCLoginBusiness mlcLoginBusiness;
	
	
	@GetMapping(value = "/mlc/token/verify")
	@Deprecated
	@ApiOperation(value = "Verify mlc token, please note this api for MLC v1.0, no need verify token in MB backend in MLC v2.0",produces = "application/json", tags={"Deprecated Api"})
	public VerifytokenResponseDTO verifyAndRedirect( String mlcToken,@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws BusinessBaseException {
		logger.debug("verifytoken MLC token: " + mlcToken);
		return mlcLoginBusiness.verifyMLCToken(mlcToken,mmbToken, MMBUtil.getCurrentAcceptLanguage());
	}
	
	@GetMapping(value = "/mmbtoken")
	@ApiOperation(value = "Retrieve MMB token with memberId from header", response = MMBTokenResponseDTO.class, produces = "application/json", tags={"MB Login Api"})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-Authenticated-Userid", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
	})
	public MMBTokenResponseDTO getMMBTokenWithMemberId(@RequestHeader(name = "X-Authenticated-Userid", required = true) String memberId, 
			@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws BusinessBaseException {
		return mlcLoginBusiness.getMMBTokenWithMemberId(mmbToken, memberId);
	}
	
}
