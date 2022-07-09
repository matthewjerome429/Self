package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.AddBookingBusiness;
import com.cathaypacific.mmbbizrule.business.BookingLinkBusiness;
import com.cathaypacific.mmbbizrule.business.BookingSummaryBusiness;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.bookingsummary.LinkTempBookingRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.addbooking.AddBookingResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.BookingSummaryResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.LinkTempBookingResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
public class BookingSummaryController {

	@Autowired
	private BookingSummaryBusiness bookingSummaryBusiness;

	@Autowired
	private BookingLinkBusiness bookingLinkBusiness;
	
	@Autowired
	private AddBookingBusiness addBookingBusiness;
	
	@GetMapping("/member/bookings")
	@ApiOperation(value = "Retrieve Member Booking", response = BookingSummaryResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "lastRloc", value = "The last rloc in page.", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "pageSize", value = "The size of bookings in the response.", required = true, dataType = "string", paramType = "query", defaultValue = "5"),
	})
	@CheckLoginInfo(memberLoginRequired = true)
	public BookingSummaryResponseDTO getBookingListForMember(String lastRloc, String pageSize, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		long currentTimeMillis = System.currentTimeMillis();
		BookingBuildRequired required = new BookingBuildRequired();
		required.setSummaryPage(true);
		required.setBaggageAllowances(false);
		required.setPassenagerContactInfo(false);
		required.setMemberAward(false);
		required.setEmergencyContactInfo(false);
		required.setCountryOfResidence(false);
		required.setTravelDocument(false);
		required.setSeatSelection(false);
		required.setMealSelection(false);
		required.setRtfs(true);
		required.setCprCheck(true);
		required.setOperateInfoAndStops(true);
		required.setMiceBookingCheck(false);
		BookingSummaryResponseDTO response = bookingSummaryBusiness.getMemberBookings(loginInfo, lastRloc,  pageSize, required);
		long time = System.currentTimeMillis() - currentTimeMillis;
		response.setTime("Total time: " + time);
		return response;
	}
	
	@PutMapping("/member/bookings/temp")
	@ApiOperation(value = "Retrieve Member Booking", response = LinkTempBookingResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo(memberLoginRequired = true)
	public LinkTempBookingResponseDTO linkTempBookingToMember(@RequestBody @Validated LinkTempBookingRequestDTO requestDto, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		 
		return bookingLinkBusiness.linkBooking(loginInfo, requestDto.getRloc(), requestDto.getFamilyName(), requestDto.getGivenName());
	}
	
	@PutMapping("/member/bookings/add")
	@ApiOperation(value = "Add booking into member", response = AddBookingResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
	})
	@CheckLoginInfo(memberLoginRequired = true)
	public AddBookingResponseDTO addBooking(@RequestBody AddBookingRequestDTO requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		return addBookingBusiness.addBooking(loginInfo, requestDTO);
	}
	
}