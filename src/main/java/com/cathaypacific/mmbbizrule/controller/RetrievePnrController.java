package com.cathaypacific.mmbbizrule.controller;

import java.text.ParseException;

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
import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrByETicketRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrByEncryptedRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrByRlocRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.unlock.PaxInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.unlock.UnlockPaxInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByEticketResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.RefreshBookingResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.RetrieveBookingBaseResponseDTO;
import com.cathaypacific.mmbbizrule.handler.LocaleHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
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
@RequestMapping(path = "/v1")
public class RetrievePnrController {
	
	@Autowired
	private RetrievePnrBusiness retrievePnrService;
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	
	@Autowired
	private LocaleHelper localeHelper;
	
	//@Cacheable(value="byrloc", key="#requestDTO.rloc")
	//@Cacheable(value="RetrievePnrController.getPnrByRloc", keyGenerator="keyGenerator")
	@GetMapping("/booking/byrloc")
	@ApiOperation(value = "Get pnr by rloc", response = ReceivePnrByRlocResponseDTO.class, produces = "application/json",tags={"MB Login Api"})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public ReceivePnrByRlocResponseDTO loginByRloc(@ApiIgnore @Validated RetrievePnrByRlocRequestDTO requestDTO,@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws Exception {
		
		requestDTO.setRloc(StringUtils.upperCase(requestDTO.getRloc()));
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		// mob and vera no need to do mice or link checking
        if (CommonConstants.CHAT_BOX_APP_CODE.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())
                || OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_MOB.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())) {
            bookingBuildRequired.setMiceBookingCheck(false);
            bookingBuildRequired.setLinkBookingCheck(false);
        }
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		ReceivePnrByRlocResponseDTO response = retrievePnrService.bookingLoginByReference(requestDTO.getRloc(), requestDTO.getFamilyName(),
				requestDTO.getGivenName(), mmbToken, bookingBuildRequired);
		if(response != null) {
			response.setRequireLocalConsentPage(localeHelper.localeConsentPageRequired(MMBUtil.getCurrentAcceptLanguage()));			
		}
		return response;
	}
	
	//@Cacheable(value="byrloc", key="#requestDTO.eticket")
	//@Cacheable(value="RetrievePnrController.getPnrByEticket", keyGenerator="keyGenerator")
	@GetMapping("/booking/byeticket")
	@ApiOperation(value = "Get pnr by eticket", response = ReceivePnrByEticketResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eticket", value = "Eticket number", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public ReceivePnrByEticketResponseDTO loginByEtick(@ApiIgnore @Validated RetrievePnrByETicketRequestDTO requestDTO,@ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken) throws Exception{
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		// mob and vera no need to do mice or link checking
        if (CommonConstants.CHAT_BOX_APP_CODE.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())
                || OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_MOB.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())) {
            bookingBuildRequired.setMiceBookingCheck(false);
            bookingBuildRequired.setLinkBookingCheck(false);
        }
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		ReceivePnrByEticketResponseDTO response = retrievePnrService.bookingLoginByEticket(requestDTO.getFamilyName(), requestDTO.getGivenName(),
				requestDTO.getEticket(), mmbToken, bookingBuildRequired);
		if(response != null) {
			response.setRequireLocalConsentPage(localeHelper.localeConsentPageRequired(MMBUtil.getCurrentAcceptLanguage()));			
		}
		return response;
		
	}
	
	@GetMapping("/booking/{rloc}")
	@ApiOperation(value = "Refresh booking", response = RefreshBookingResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "path", defaultValue = ""),
			@ApiImplicitParam(name = "requirePassenger", value = "Require Passenger", dataType = "boolean", paramType = "query", defaultValue = "false"),
			@ApiImplicitParam(name = "requireFlight", value = "Require Passenger", dataType = "boolean", paramType = "query", defaultValue = "false"),
			@ApiImplicitParam(name = "requireTravelDoc", value = "Require TravelDoc", dataType = "boolean", paramType = "query", defaultValue = "false")
	})
	//@CheckRloc(rlocPath="rloc", argIndex = 1)
	@CheckLoginInfo
	public RefreshBookingResponseDTO refreshBookingByRloc(RequiredInfoDTO request,@PathVariable String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException, ParseException{
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		// mob and vera no need to do mice or link checking
        if (CommonConstants.CHAT_BOX_APP_CODE.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())
                || OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_MOB.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())) {
            bookingBuildRequired.setMiceBookingCheck(false);
            bookingBuildRequired.setLinkBookingCheck(false);
        }
		bookingBuildRequired.setBookableUpgradeStatusCheck(true);
		return retrievePnrService.refreshBooking(loginInfo, rloc, bookingBuildRequired);
	}
	
	@GetMapping("/booking/unlockbyeticket")
	@ApiOperation(value = "unlock passenger by eticket,this method is invalid in this version, it always return empty.", response = BaseResponseDTO.class, produces = "application/json")
	@Deprecated
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "eticket", value = "Eticket number", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "rloc", value = "booking rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "passengerId", value = "passenger id", required = true, dataType = "string", paramType = "query", defaultValue = "")})
	@CheckLoginInfo
	public BaseResponseDTO unlockUserInfoByEticket(@ApiIgnore @LoginInfoPara LoginInfo loginInfo, String eticket, String rloc, String passengerId) throws BusinessBaseException, ParseException{
		return unlockUserInfoByEtickets(new UnlockPaxInfoDTO(rloc, new PaxInfoDTO(passengerId, eticket)), loginInfo);
	}
	
	@PostMapping("/booking/unlockbyetickets")
	@ApiOperation(value = "unlock passengers by etickets, this method is invalid in this version, it always return empty.", response = BaseResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	@Deprecated
	public BaseResponseDTO unlockUserInfoByEtickets(@RequestBody @Validated UnlockPaxInfoDTO unlockPaxInfoDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException, ParseException {
		return null;
	}
	
	@GetMapping("/booking/byEncryptedLink")
	@ApiOperation(value = "Get PNR by encrypted details", response = RetrieveBookingBaseResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = "en"),
		@ApiImplicitParam(name = "Access-Channel", value = "The channel this request come from", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
		@ApiImplicitParam(name = "appCode", value = "The channel this request come from", required = false, dataType = "string", paramType = "query", defaultValue = "MMB"),
	})
	public RetrieveBookingBaseResponseDTO loginByEncryptedLink(String encryptedDetails
			, @ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken, String appCode) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		String decryptedStr = encryptionHelper.decryptMessage(encryptedDetails, EncryptionHelper.Encoding.BASE64URL, MMBUtil.getCurrentAccessChannel(),appCode!=null?appCode:MMBUtil.getCurrentAppCode());
		
		RetrievePnrByEncryptedRequestDTO requestDTO  = mapper.readValue(decryptedStr, RetrievePnrByEncryptedRequestDTO.class);
		
		if(StringUtils.isNotBlank(requestDTO.getEticket())) {
			RetrievePnrByETicketRequestDTO etRequest = new RetrievePnrByETicketRequestDTO();
			etRequest.setEticket(requestDTO.getEticket());
			etRequest.setFamilyName(requestDTO.getFamilyName());
			etRequest.setGivenName(requestDTO.getGivenName());
			
			return loginByEtick(etRequest, mmbToken);
			
		}else if(StringUtils.isNotBlank(requestDTO.getRloc())) {
			RetrievePnrByRlocRequestDTO rlocRequest = new RetrievePnrByRlocRequestDTO();
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
	@ApiOperation(value = "Get PNR Details", response = ReceivePnrByRlocResponseDTO.class, produces = "application/json")
	public ReceivePnrByRlocResponseDTO updatePassengerDetails(@RequestBody PNRReply pnrBody, 
													  String rloc, 
													  String givenName, 
													  String familyName,
													  @ApiIgnore @RequestAttribute(value=MMBConstants.HEADER_KEY_MMB_TOKEN_ID) String mmbToken ) throws Exception{
		
		ReceivePnrByRlocResponseDTO response = retrievePnrService.bookingByPNR(pnrBody, 
																			   rloc, 
																			   familyName, 
																			   givenName,  
																			   mmbToken, 
																			   new BookingBuildRequired());

		return response;
	}
	
	
}
