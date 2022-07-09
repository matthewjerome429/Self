package com.cathaypacific.mmbbizrule.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.OfficialReceiptBusiness;
import com.cathaypacific.mmbbizrule.dto.response.officialreceipt.OfficialReceiptResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = {"Official Receipt"})
@RequestMapping(path = "/v1")
public class OfficialReceiptController {
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private OfficialReceiptBusiness officialReceiptBusiness;
	
	@GetMapping("/officialreceipt/{oneARloc}/passengers")
	@ApiOperation(value = "Get official receipt passegner informations", response = OfficialReceiptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "oneARloc", value = "booking rloc, please use 1A rloc.", required = true, dataType = "string", paramType = "path", defaultValue = "")
		})
	@CheckLoginInfo
	public OfficialReceiptResponseDTO retrievePassengers(@PathVariable String oneARloc, @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException {
		return officialReceiptBusiness.retrievePassengers(oneARloc, loginInfo);
	}
	
	@GetMapping("/officialreceipt/{oneARloc}/download/{passengerId}")
	@ApiOperation(value = "download official receipt by passenger id", produces = "application/pdf")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "oneARloc", value = "booking rloc, please use 1A rloc.", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "passengerId", value = "passenger id", required = true, dataType = "string", paramType = "path", defaultValue = "")
		})
	@CheckLoginInfo
	public void download(@PathVariable(value = "oneARloc", required = true) String oneARloc, @PathVariable(value = "passengerId", required = true) String passengerId,
			@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @RequestHeader("Accept-Language") String acceptLanguage, HttpServletResponse response) throws IOException, BusinessBaseException {
		
		Locale locale = null;
		if (StringUtils.isNotEmpty(acceptLanguage)) {
			String[] languageLocales = acceptLanguage.split("-");
			String language = languageLocales[0];
			String country = languageLocales[1];
			locale = new Locale(language, country);
		} else {
			locale = new Locale("en", "HK");
		}
		
		// retrieve pnrBooking
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
		
		ByteArrayOutputStream pdfOutputStream = (ByteArrayOutputStream) officialReceiptBusiness.generateOfficialReceiptPDF(pnrBooking, passengerId, loginInfo, locale);

		response.setHeader("Content-type", "application/pdf");
		response.addHeader("Content-Disposition", "inline;filename=" + officialReceiptBusiness.generateOfficialReceiptFileName(pnrBooking, passengerId, locale));
		
		pdfOutputStream.writeTo(response.getOutputStream());
		pdfOutputStream.close();
	}
	
}
