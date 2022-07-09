package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.v2.business.LinkBookingBusiness;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.AddLinkedBookingDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CheckLinkedBookingDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Link Booking Operations"})
@RestController
@RequestMapping(path = "/v2")
public class LinkBookingController {
	
	@Autowired
	private LinkBookingBusiness linkBookingBusiness;
	
	// use put method, because request is idempotent
	// no matter how many times we receive the same request, we will have the same response.
	@PutMapping("/staff/booking/check")
	@ApiOperation(value = "Check Linked Booking", response = CheckLinkedBookingDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
		})
	@CheckLoginInfo
	public CheckLinkedBookingDTO checkLinkedBooking(@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @RequestBody AddBookingRequestDTO requestDTO) throws BusinessBaseException {
		 return linkBookingBusiness.checkLinkedBooking(loginInfo, requestDTO);
	}
	
    @PostMapping("/staff/booking/add")
    @ApiOperation(value = "Add Linked Booking", response = AddLinkedBookingDTO.class, produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")
        })
    @CheckLoginInfo
    public AddLinkedBookingDTO addLinkedBooking(@LoginInfoPara @ApiIgnore LoginInfo loginInfo, @RequestBody AddBookingRequestDTO requestDTO) throws BusinessBaseException {
         return linkBookingBusiness.addLinkedBooking(loginInfo, requestDTO);
    }
	
}
