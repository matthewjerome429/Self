package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.v2.business.impl.BookingSummaryBusinessImplV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.BookingSummaryResponseDTOV2;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v2")
public class BookingSummaryControllerV2 {

	@Autowired
	private BookingSummaryBusinessImplV2 bookingSummaryBusinessV2;

	
	@GetMapping("/member/bookings")
	@ApiOperation(value = "Retrieve Member Booking", response = BookingSummaryResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "lastRloc", value = "The last rloc in page.", required = false, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "pageSize", value = "The size of bookings in the response.", required = true, dataType = "string", paramType = "query", defaultValue = "5"),
	})
	@CheckLoginInfo(memberLoginRequired = true)
	public BookingSummaryResponseDTOV2 getBookingListForMember(String lastRloc, String pageSize, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
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
		required.setMiceBookingCheck(true);
		 
		return bookingSummaryBusinessV2.getMemberBookings(loginInfo, required);
	}
	 
}