package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.BookingAdditionalInfoBusiness;
import com.cathaypacific.mmbbizrule.dto.request.bookingproperties.additional.BookingAdditionalInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.BookingAdditionalInfoResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Booking additionalInfos API"}, description = "Booking additionalInfos API")
@RestController
@RequestMapping(path = "/v1")
public class BookingAdditionalInfoController {
	
	@Autowired
	private BookingAdditionalInfoBusiness bookingAdditionalInfoBusiness;
	
	@GetMapping("/booking/{oneARloc}/additionalInfos")
	@ApiOperation(value = "Get Journeys by 1A rloc",  response = BookingAdditionalInfoResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "oneARloc", value = "booking rloc, please use 1A rloc.", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "plusgrade", value = "booking rloc", required = false, dataType = "boolean", paramType = "query", defaultValue = "false"),
	})
    @CheckLoginInfo
	public BookingAdditionalInfoResponseDTO getBookingAdditionalInfos(@PathVariable String oneARloc,BookingAdditionalInfoRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
				
		return	bookingAdditionalInfoBusiness.getBookingAdditional(loginInfo, oneARloc, MMBUtil.getCurrentAcceptLanguage(), requestDTO);
	}
	

	
}
