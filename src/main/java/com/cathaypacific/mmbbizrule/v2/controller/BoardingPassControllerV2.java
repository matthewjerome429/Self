package com.cathaypacific.mmbbizrule.v2.controller;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BoardingPassBusiness;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SPBPRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendEmailRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendSMSRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SPBPResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendEmailResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendSMSResponseDTOV2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Boarding Pass related Operations"}, value = "self print, send sms, send email in Check-in flow")
@RestController
@RequestMapping(path = "/v2/boardingpass")
public class BoardingPassControllerV2 {

	@Autowired
	private BoardingPassBusiness boardingPassBusiness;

	@PostMapping("/spbp")
	@ApiOperation(value = "self print boarding pass", response = SPBPResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public SPBPResponseDTOV2 selfPrintBP(@LoginInfoPara @ApiIgnore LoginInfo loginInfo,
			@RequestBody @Validated SPBPRequestDTOV2 requestDTO) throws BusinessBaseException {
		return boardingPassBusiness.selfPrintBP(loginInfo, requestDTO);
	}

	@PostMapping("/sendBPEmail")
	@ApiOperation(value = "send Boarding Pass Email", response = SendEmailResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public SendEmailResponseDTOV2 sendBPEmail(@LoginInfoPara @ApiIgnore LoginInfo loginInfo,
			@RequestBody @Validated SendEmailRequestDTOV2 requestDTO) throws BusinessBaseException {
		return boardingPassBusiness.sendBPEmail(loginInfo, requestDTO);
	}

	@PostMapping("/sendBPSms")
	@ApiOperation(value = "send Boarding Pass SMS", response = SendSMSResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public SendSMSResponseDTOV2 sendBPSms(@LoginInfoPara @ApiIgnore LoginInfo loginInfo,
			@RequestBody @Validated SendSMSRequestDTOV2 requestDTO) throws BusinessBaseException {
		return boardingPassBusiness.sendBPSms(loginInfo, requestDTO);
	}

	@GetMapping("/{locale}/applewallet/{applePassNumber}/{rloc}")
	@ApiOperation(value = "get apple wallet file of boarding pass by applePassNumber", produces = "application/vnd.apple.pkpass")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "1A rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
			@ApiImplicitParam(name = "applePassNumber", value = "applePassNumber", required = true, dataType = "string", paramType = "path", defaultValue = ""),
			@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public void getAppleWalletBP(@PathVariable(value = "applePassNumber", required = true) String applePassNumber,
			@PathVariable(value = "rloc", required = true) String rloc,
			@PathVariable(value = "locale", required = true) String locale, HttpServletResponse response)
			throws Exception {
		InputStream applePass = boardingPassBusiness.getBPAppleWallet(locale, applePassNumber, rloc);
		byte[] passFileBytes = IOUtils.toByteArray(applePass);
		if (passFileBytes == null || passFileBytes.length == 0) {
			throw new UnexpectedException("generate apple boarding pass file failure!",
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		writePkPass(passFileBytes, response);
		response.flushBuffer();
		applePass.close();
	}

	private void writePkPass(byte[] boardingPassBytes, HttpServletResponse httpResponse) throws Exception {
		httpResponse.setHeader(MMBConstants.HTTP_HEADER_KEY_CONTENT_DISPOSITION,
				MMBConstants.HTTP_HEADER_VALUE_CONTENT_DISPOSITION_PKPASS);
		httpResponse.setContentType(MMBConstants.HTTP_CONTENT_TYPE_PKPASS);

		OutputStream output = httpResponse.getOutputStream();
		output.write(boardingPassBytes);
		output.flush();
		output.close();
	}

}
