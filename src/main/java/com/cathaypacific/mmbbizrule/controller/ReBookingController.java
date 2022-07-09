package com.cathaypacific.mmbbizrule.controller;

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
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.ReBookingBusiness;
import com.cathaypacific.mmbbizrule.business.RescheduleEligibleBusiness;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.CancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.AcceptFlightRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.ReBookingAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.ProtectFlightInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.RebookAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.reschedule.BookingRescheduleResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"ReBooking"}, value = "Rebooking API")
@RestController
@RequestMapping(path = "/v1")
public class ReBookingController {
	
	@Autowired
	private ReBookingBusiness reBookingBusiness;
	@Autowired
	private RescheduleEligibleBusiness rescheduleEligibleBusiness;
	
	@PostMapping("/rebooking/accept")
	@ApiOperation(value = "accept rebooking", response = RebookAcceptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = false, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public RebookAcceptResponseDTO acceptRebook(@RequestBody @Validated ReBookingAcceptRequestDTO requestDTO ) throws BusinessBaseException {
		//TODO should use appcode instead of access channel
		return reBookingBusiness.accept(requestDTO, MMBUtil.getCurrentAccessChannel());
	}
	
	@PostMapping("/rebooking/acceptFlight")
	@ApiOperation(value = "accept rebooking without token", response = RebookAcceptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB")
	})
	public RebookAcceptResponseDTO acceptFlight(@RequestBody @Validated AcceptFlightRequestDTO requestDTO) throws BusinessBaseException {
		//TODO should use appcode instead of access channel
		return reBookingBusiness.acceptFlight(requestDTO, MMBUtil.getCurrentAccessChannel());
	}
	
	@GetMapping("/rebooking/protectFlightInfo/{rloc}")
	@ApiOperation(value = "get rebook protect flight info", response = ProtectFlightInfoResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB")
	})
	public ProtectFlightInfoResponseDTO getProtectFlightInfo(@PathVariable(required = true) String rloc) throws BusinessBaseException {
		return reBookingBusiness.getProtectFlightInfo(rloc);
	}
	
	/* REMOVE TEST CODE BEFORE GO LIVE R5.0
	@GetMapping("/test/sendBP")
	@ApiOperation(value = "get rebook protect flight info", response = RebookAcceptResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB")
	})
	public RebookAcceptResponseDTO testSendBP(String onerloc, String givenName, String familyName, String acceptedSegmentId) throws BusinessBaseException {
		return reBookingBusiness.testSendBP(acceptedSegmentId, onerloc, familyName, givenName);
	}
	*/
	
	@GetMapping("/reschedule/{rloc}")
	@ApiOperation(value = "First get the flight change option availability, and if the availability is true, get the deeplink of chatbot.", response = BookingRescheduleResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "en_HK"),
	})
	@CheckLoginInfo
	public BookingRescheduleResponseDTO rescheduleEligibleByRloc(@PathVariable("rloc") String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
	    return  rescheduleEligibleBusiness.rescheduleEligibleByRloc(rloc,loginInfo,MMBUtil.getCurrentAcceptLanguage());
	}
	
}
