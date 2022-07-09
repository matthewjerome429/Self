package com.cathaypacific.mmbbizrule.controller;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BookingCancelBusiness;
import com.cathaypacific.mmbbizrule.dto.request.bookingcancel.BookingCancelRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.bookingcancel.CancelBookingDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.BookingCancelResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.CancelBookingDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.BookingCancelCheckResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.changeflight.ChangeFlightDataResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
@Api(tags = {"Booking Cancel"}, value="Booking cancel related apis.")
public class BookingCancelController {
 
	@Autowired
	private BookingCancelBusiness bookingCancelBusiness;
	
	@GetMapping("/booking/{rloc}/cancancel")
	@ApiOperation(value = "Check the booking can cancel.", response = BookingCancelCheckResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "skipIbeCheck", value = "check Ibe canccel flag", required = true, dataType = "boolean", paramType = "query", defaultValue = "false"),
	})
	@CheckLoginInfo
	public BookingCancelCheckResponseDTO checkBookingCanCancel(@PathVariable String rloc,@Param("skipIbeCheck") boolean skipIbeCheck, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return bookingCancelBusiness.checkBookingCanCancel(rloc, loginInfo,skipIbeCheck);
	}
	
	@PostMapping("/booking/cancel")
	@ApiOperation(value = "Cancel booking.", response = BookingCancelResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public BookingCancelResponseDTO cancelBooking(@RequestBody BookingCancelRequestDTO requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		
		String languageLocale = requestDTO.getLanguageLocale();
		Locale locale = null;
		if (StringUtils.isNotEmpty(languageLocale)) {
			String[] languageLocales = languageLocale.split("_");
			String language = languageLocales[0];
			String country = languageLocales[1];
			locale = new Locale(language, country);
		} else {
			locale = new Locale("en");
		}
		
		return bookingCancelBusiness.cancelBooking(requestDTO.getRloc(), requestDTO.isRefund(), loginInfo, locale);
	}

	@PostMapping("/booking/canceldata")
	@ApiOperation(value = " retrieve cancel booking encrypt data for IBE cancel booking", response = CancelBookingDataResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	public CancelBookingDataResponseDTO getCancelBookingDataByRloc(@RequestBody CancelBookingDataRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return bookingCancelBusiness.getCancelBookingData(requestDTO, loginInfo);
	}
}
