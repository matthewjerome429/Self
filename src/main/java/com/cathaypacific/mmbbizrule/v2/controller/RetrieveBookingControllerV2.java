package com.cathaypacific.mmbbizrule.v2.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.handler.LocaleHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.mmbbizrule.v2.business.RetrieveBookingBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking.RetrievePnrByETicketRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking.RetrievePnrByEncryptedRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking.RetrievePnrByRlocRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.ReceivePnrByEticketResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.ReceivePnrByRlocResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.RefreshBookingResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.RetrieveBookingBaseResponseDTOV2;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Zilong Bu on 11/22/2017.
 */
@Api(tags = {"PNR APIs"}, value = "PNR APIs")
@RestController
@RequestMapping(path = "/v2")
public class RetrieveBookingControllerV2 {
	
	@Autowired
	private RetrieveBookingBusinessV2 retrieveBookingBusiness;
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	
	@Autowired
	private LocaleHelper localeHelper;
	
	//@Cacheable(value="byrloc", key="#requestDTO.rloc")
	//@Cacheable(value="RetrievePnrController.getPnrByRloc", keyGenerator="keyGenerator")
	@GetMapping("/booking/byrloc")
	@ApiOperation(value = "Get pnr by rloc", notes="Rereieve", response = ReceivePnrByRlocResponseDTOV2.class, produces = "application/json",tags={"MB Login Api"})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		    @ApiImplicitParam(name = "retrieveDcs", value = "Retrieve DCS identifier, will ignore RES(PNR) and OJ if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		    @ApiImplicitParam(name = "retrieveRes", value = "Retrieve RES(pnr), will ignore RES(PNR) if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		    @ApiImplicitParam(name = "retrieveOJ", value = "Retrieve OJ, will ignore OJ if it is false, please note will return error in case this flag is false and request rloc is package rloc.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true")
	})
	public ReceivePnrByRlocResponseDTOV2 loginByRloc(@ApiIgnore @Validated RetrievePnrByRlocRequestDTOV2 requestDTO,@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws BusinessBaseException{
		requestDTO.setRloc(StringUtils.upperCase(requestDTO.getRloc()));
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		bookingBuildRequired.setBaggageAllowances(false);
		bookingBuildRequired.setPreSelectedMeal(true);
		ReceivePnrByRlocResponseDTOV2 response = retrieveBookingBusiness.bookingLoginByReference(requestDTO.getRloc(), requestDTO.getFamilyName(),
				requestDTO.getGivenName(), mmbToken, bookingBuildRequired);
		if(response != null) {
			response.setRequireLocalConsentPage(localeHelper.localeConsentPageRequired(MMBUtil.getCurrentAcceptLanguage()));			
		}	
		return response;
	}
	
	//@Cacheable(value="byrloc", key="#requestDTO.eticket")
	//@Cacheable(value="RetrievePnrController.getPnrByEticket", keyGenerator="keyGenerator")
	@GetMapping("/booking/byeticket")
	@ApiOperation(value = "Get pnr by eticket", response = ReceivePnrByEticketResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eticket", value = "Eticket number", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		    @ApiImplicitParam(name = "retrieveDcs", value = "Retrieve DCS identifier, will ignore RES(PNR) and OJ if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		    @ApiImplicitParam(name = "retrieveRes", value = "Retrieve RES(pnr), will ignore RES(PNR) if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		    @ApiImplicitParam(name = "retrieveOJ", value = "Retrieve OJ, will ignore OJ if it is false, please note will return error in case this flag is false and request rloc is package rloc.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true")
	})
	public ReceivePnrByEticketResponseDTOV2 loginByEtick(@ApiIgnore @Validated RetrievePnrByETicketRequestDTOV2 requestDTO,@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws BusinessBaseException  {
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		bookingBuildRequired.setBaggageAllowances(false);
		bookingBuildRequired.setPreSelectedMeal(true);
		ReceivePnrByEticketResponseDTOV2 response = retrieveBookingBusiness.bookingLoginByEticket(requestDTO.getFamilyName(), requestDTO.getGivenName(),
				requestDTO.getEticket(), mmbToken, bookingBuildRequired);
		if(response != null) {
			response.setRequireLocalConsentPage(localeHelper.localeConsentPageRequired(MMBUtil.getCurrentAcceptLanguage()));			
		}
		return response;
		
	}
	
	
	@GetMapping("/booking/{rloc}")
	@ApiOperation(value = "Refresh booking", response = RefreshBookingResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
			 @ApiImplicitParam(name = "retrieveDcs", value = "Retrieve DCS identifier, will ignore RES(PNR) and OJ if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
			    @ApiImplicitParam(name = "retrieveRes", value = "Retrieve RES(pnr), will ignore RES(PNR) if it is false.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
			    @ApiImplicitParam(name = "retrieveOJ", value = "Retrieve OJ, will ignore OJ if it is false, please note will return error in case this flag is false and request rloc is package rloc.", required = false, dataType = "boolean", paramType = "query", defaultValue = "true")
			
	})
	@CheckLoginInfo
	public RefreshBookingResponseDTOV2 refreshBookingByRloc(RequiredInfoDTO request,@PathVariable String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException{
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		bookingBuildRequired.setBaggageAllowances(false);
		bookingBuildRequired.setPreSelectedMeal(true);
		return retrieveBookingBusiness.refreshBooking(loginInfo, rloc, bookingBuildRequired);
	}
	
	@GetMapping("/booking/byEncryptedLink")
	@ApiOperation(value = "Get PNR by encrypted details", response = RetrieveBookingBaseResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "en"),
		@ApiImplicitParam(name = "Access-Channel", value = "The channel this request come from", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
		@ApiImplicitParam(name = "appCode", value = "The channel this request come from", required = false, dataType = "string", paramType = "query", defaultValue = "MMB"),
	})
	public RetrieveBookingBaseResponseDTOV2 loginByEncryptedLink(String encryptedDetails
			, @ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken, String appCode) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		String decryptedStr = encryptionHelper.decryptMessage(encryptedDetails, EncryptionHelper.Encoding.BASE64URL, MMBUtil.getCurrentAccessChannel(),appCode!=null?appCode:MMBUtil.getCurrentAppCode());
		
		RetrievePnrByEncryptedRequestDTOV2 requestDTO  = mapper.readValue(decryptedStr, RetrievePnrByEncryptedRequestDTOV2.class);
		
		if(StringUtils.isNotBlank(requestDTO.getEticket())) {
			RetrievePnrByETicketRequestDTOV2 etRequest = new RetrievePnrByETicketRequestDTOV2();
			etRequest.setEticket(requestDTO.getEticket());
			etRequest.setFamilyName(requestDTO.getFamilyName());
			etRequest.setGivenName(requestDTO.getGivenName());
			
			return loginByEtick(etRequest, mmbToken);
			
		}else if(StringUtils.isNotBlank(requestDTO.getRloc())) {
			RetrievePnrByRlocRequestDTOV2 rlocRequest = new RetrievePnrByRlocRequestDTOV2();
			rlocRequest.setRloc(requestDTO.getRloc());
			rlocRequest.setFamilyName(requestDTO.getFamilyName());
			rlocRequest.setGivenName(requestDTO.getGivenName());
			
			return loginByRloc(rlocRequest, mmbToken);
		}else {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW);
			throw new UnexpectedException("Can't find neither rloc or eticket in the request.", errorInfo);
		}
	}
	
	@PostMapping("/booking/byPNR")
	@ApiOperation(value = "Get PNR Details", response = ReceivePnrByRlocResponseDTOV2.class, produces = "application/json")
	public ReceivePnrByRlocResponseDTOV2 updatePassengerDetails(@RequestBody PNRReply pnrBody, 
													  String rloc, 
													  String givenName, 
													  String familyName,
													  @ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken ) throws BusinessBaseException{

		return retrieveBookingBusiness.bookingByPNR(pnrBody, 
				   rloc, 
				   familyName, 
				   givenName,  
				   mmbToken, 
				   new BookingBuildRequired());
	}
}
