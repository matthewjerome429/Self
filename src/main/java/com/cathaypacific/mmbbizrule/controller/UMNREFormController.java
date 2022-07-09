package com.cathaypacific.mmbbizrule.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.UMNREFormBusiness;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnrformupdate.UmnrFormUpdateResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = {"UMNR EForm"})
@RequestMapping(path = "/v1")
public class UMNREFormController {
	
	@Autowired
	private UMNREFormBusiness umnrEFormBusiness;
	
	@GetMapping("/umnrEForm/{rloc}")
	@ApiOperation(
			value = "Retrieve all necessary UMNR e-form data including eligible segments and UMNR passengers with UMNR journeys.",
			response = UMNREFormResponseDTO.class,
			produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public UMNREFormResponseDTO retrieveUMNREFrom(@PathVariable("rloc") String rloc, @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException, ParseException {
		return umnrEFormBusiness.retrieveEFormData(rloc, loginInfo);
	}
	
	@PutMapping("/updateUmnrEForm")
	@ApiOperation(value = "Update UMNR E-Form", response = UmnrFormUpdateResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = false, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="requestDTO.rloc", argIndex = 0)
	public UmnrFormUpdateResponseDTO updatePassengerDetails(@RequestBody @Validated UmnrFormUpdateRequestDTO requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws UnexpectedException {
		return umnrEFormBusiness.updateUmnrEFormData(requestDTO, loginInfo);
	}
	
	@GetMapping("/umnrEFormPdf/{rloc}")
	@ApiOperation(
			value = "Generate UMNR EForm PDFs based on the specified passenger id",
			produces = "application/pdf")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "passengerId", value = "passenger id", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "umnrJourneyId", value = "umnr journey id", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@CheckLoginInfo
	public void generateUMNREFormPDF(@PathVariable("rloc") String rloc, String passengerId, String umnrJourneyId, @LoginInfoPara @ApiIgnore LoginInfo loginInfo, HttpServletResponse response) throws IOException, ParseException, BusinessBaseException {
		ByteArrayOutputStream pdfOutputStream = (ByteArrayOutputStream) umnrEFormBusiness.generateUMNREFormPDF(rloc, passengerId, umnrJourneyId, loginInfo);
		response.setHeader("Content-type", "application/pdf");
		pdfOutputStream.writeTo(response.getOutputStream());
		pdfOutputStream.close();
	}
}
