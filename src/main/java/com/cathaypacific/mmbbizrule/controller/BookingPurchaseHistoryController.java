package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.business.BookingPurchaseHistoryBusiness;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PurchaseHistoryResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Booking Purchase History"}, description = "Booking Purchase History API")
@RestController
@RequestMapping(path = "/v1/purchasehistory")
public class BookingPurchaseHistoryController {
	@Autowired
	private BookingPurchaseHistoryBusiness bookingPurchaseHistoryBusiness;
	
	@GetMapping("/retrievehistory")
	@ApiOperation(value = "Retrieve purchase history", response = PurchaseHistoryResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "oneARloc", value = "One A Rloc", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@CheckLoginInfo
	@Validated
	public PurchaseHistoryResponseDTO retrievePurchaseHistory(@RequestParam("oneARloc")String oneARloc) throws BusinessBaseException {	
		return bookingPurchaseHistoryBusiness.retrieveHistroy(oneARloc);
	}
}
