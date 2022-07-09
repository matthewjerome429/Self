package com.cathaypacific.mmbbizrule.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BookingPropertiesBusiness;
import com.cathaypacific.mmbbizrule.dto.request.bookingcustomizedinfo.BookingCustomizedInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.memberselfbookings.SelfBookingsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingCustomizedInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.journey.JoruneyResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberselfbookings.SelfBookingsResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Booking Properties API"}, description = "Booking Properties API")
@RestController
@RequestMapping(path = "/v1")
public class BookingPropertiesController {
	
	
	@Autowired
	private BookingPropertiesBusiness bookingPropertiesBusiness;
	
	@GetMapping("/booking/properties")
	@ApiOperation(value = "Get Flight booking properties by rloc", response = BookingPropertiesDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = false, dataType = "string", paramType = "header", defaultValue = "")
	})
    @CheckLoginInfo
	public BookingPropertiesDTO getBookingProperties(@RequestParam String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return bookingPropertiesBusiness.getBookingProperties(loginInfo, rloc);
	}
	
	@GetMapping("/booking/properties/commerce")
	@ApiOperation(value = "Get Flight booking properties by rloc for E-commerce", response = BookingCommercePropertiesDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = false, dataType = "string", paramType = "header", defaultValue = "")
	})
    @CheckLoginInfo
	public BookingCommercePropertiesDTO getBookingCommerceProperties(@RequestParam String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return bookingPropertiesBusiness.getBookingCommerceProperties(loginInfo, rloc);
	}
	
	@GetMapping("/booking/{oneARloc}/journeys")
	@ApiOperation(value = "Get Journeys by 1A rloc", response = JoruneyResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "oneARloc", value = "booking rloc, please use 1A rloc.", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = false, dataType = "string", paramType = "header", defaultValue = "")
	})
    @CheckLoginInfo
	public JoruneyResponseDTO getBookingJourneys(@PathVariable String oneARloc) throws BusinessBaseException {
		return bookingPropertiesBusiness.getBookingJourneys(oneARloc);
	}
	
	@PostMapping("/internal/customizedInfo/booking")
	@ApiOperation(value = "Get booking customized info", response = BookingCustomizedInfoResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
    @CheckLoginInfo
	public BookingCustomizedInfoResponseDTO getBookingCustomizedInfo(@RequestBody BookingCustomizedInfoRequestDTO request) throws BusinessBaseException {
		if (StringUtils.isEmpty(request.getEticket()) && StringUtils.isEmpty(request.getRloc())) {
			throw new UnexpectedException("Unable to get booking customized info - e-ticket/rloc are both empty",
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		return bookingPropertiesBusiness.getBookingCustomizedInfo(request);
	}

	@PostMapping("/internal/customizedInfo/member/selfBookings")
	@ApiOperation(value = "Get member self booking list by member id", response = SelfBookingsResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""), 
		@ApiImplicitParam(name = "includeCompanionbooking", value = "Whether the bookings obtained from the PNR search interface include companion bookings.", required = false, dataType = "boolean", paramType = "query", defaultValue = "false")})
	@CheckLoginInfo
	public SelfBookingsResponseDTO getSelfBookingListForMember(@RequestBody SelfBookingsRequestDTO requestDTO,@Param("includeCompanionbooking") boolean includeCompanionbooking)
			throws BusinessBaseException {
		return bookingPropertiesBusiness.getSelfBookingList(requestDTO,includeCompanionbooking);
	}
}
