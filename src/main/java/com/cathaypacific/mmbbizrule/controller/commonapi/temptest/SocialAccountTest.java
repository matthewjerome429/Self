package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.MemberProfileBusiness;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.socialaccount.ClsSocialAccountRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.v2.TravelDocumentRetrievalRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.ClsSocialAccountResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2.TravelDocumentRetrievalResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileServiceCacheHelper;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.ProfileTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.DTOConverterV2;
import com.cathaypacific.mmbbizrule.v2.handler.MaskHelperV2;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/* REMOVE TEST CODE BEFORE GO LIVE R5.0
@Api(tags = {" Temp For Test"} , description= "just for test, will delete before go live")
@RestController
@RequestMapping(path = "/v1/common")
*/
public class SocialAccountTest {
	
	@Autowired
	private MemberProfileBusiness memberProfileBusiness;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private OLCIServiceV2 olciServiceV2;
	
	@Autowired
	private PnrCprMergeHelper pnrCprMergeHelper;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private DTOConverterV2 dtoConverter;
	
	@Autowired
	private MaskHelperV2 maskHelper;
	
	@Autowired
	private  RetrieveProfileServiceCacheHelper retrieveProfileServiceCacheHelper;
	@GetMapping("/socialaccounttest")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberNumber", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "applicationName", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "correlationId", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wsseUser", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wssePassword", required = false, dataType = "string", paramType = "query", defaultValue = ""), })

	public String nocache(String  memberNumber, String applicationName, String correlationId, String wsseUser, String wssePassword) {
		ClsSocialAccountRequest request = new ClsSocialAccountRequest();
		request.setApplicationName(applicationName);
		request.setCorrelationId(correlationId);
		request.setMemberNumber(memberNumber);
		request.setWsseUser(wsseUser);
		request.setWssePassword(wssePassword);
		
		ClsSocialAccountResponse response = retrieveProfileServiceCacheHelper.retrieveSocialAccountDetails(request);	
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	@PostMapping("/profilev2")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberNumber", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "applicationName", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "correlationId", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wsseUser", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wssePassword", required = false, dataType = "string", paramType = "query", defaultValue = ""), })
	public String retrieveProfileV2(String memberId){
		TravelDocumentRetrievalRequest request = new TravelDocumentRetrievalRequest();
		request.setApplicationName(MMBUtil.getCurrentAppCode());
		request.setCorrelationId("");
		request.setMemberIdOrUsername(memberId);
		TravelDocumentRetrievalResponse response = retrieveProfileServiceCacheHelper.retrieveMemberProfileSummaryV2(request);
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	@PostMapping("/traveldocv2")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberNumber", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "applicationName", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "correlationId", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wsseUser", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "wssePassword", required = false, dataType = "string", paramType = "query", defaultValue = ""), })
	public ProfileTravelDocDTO retrieveTravelDocV2(String memberId,String origin,
			String destination, boolean requirePriDocs, boolean requireSecDocs){
		return memberProfileBusiness.retrieveMemberProfileTravelDocV2(memberId, origin, destination,requirePriDocs,requireSecDocs,new LoginInfo());
	}
	
	@PostMapping("/memberholiday")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberNumber", required = false, dataType = "string", paramType = "query", defaultValue = "")})
	public ProfilePersonInfo retrievePersonInfo(String memberId, String mbToken){
		return retrieveProfileService.retrievePersonInfo(memberId, mbToken);
	}
	
	@GetMapping("/olcibooking/byrloc")
	@ApiOperation(value = "Get pnr by rloc", response = ReceivePnrByRlocResponseDTO.class, produces = "application/json" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "member id", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public LoginResponseDTO retrieveOlciInfo(String rloc, String givenName, String familyName, String memberId){
		return olciServiceV2.retrieveCPRBooking(rloc, null,givenName, familyName, memberId, false, true);
	}
	
	@GetMapping("/cprbooking/byrloc")
	@ApiOperation(value = "Get booking based on CPR by rloc", response = Booking.class, produces = "application/json" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "member id", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public Booking getBookingBasedOnCPR(String rloc, String givenName, String familyName, String memberId) {
		LoginResponseDTO cpr = olciServiceV2.retrieveCPRBooking(rloc, null,givenName, familyName, memberId, false, true);
		return pnrCprMergeHelper.buildBookingModelByCpr(cpr);
	}
	
	@GetMapping("/pnrbooking/byrloc")
	@ApiOperation(value = "Get booking based on PNR merged CPR by rloc", response = Booking.class, produces = "application/json" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "member id", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public Booking getBookingBasedOnPnr(String rloc, String givenName, String familyName, String memberId, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, new BookingBuildRequired());
		LoginResponseDTO cpr = olciServiceV2.retrieveCPRBooking(rloc, null,givenName, familyName, memberId, false, true);
		pnrCprMergeHelper.mergeCprToBookingModel(booking, cpr, new BookingBuildRequired());
		return booking;
	}
	
	
	@GetMapping("/pnrbookingDTO/byrloc")
	@ApiOperation(value = "Get bookingDTO based on PNR merged CPR by rloc", response = Booking.class, produces = "application/json" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "member id", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	@CheckLoginInfo
	public FlightBookingDTOV2 getBookingDTOBasedOnPnr(String rloc, String givenName, String familyName, String memberId, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, new BookingBuildRequired());
		LoginResponseDTO cpr = olciServiceV2.retrieveCPRBooking(rloc, null, givenName, familyName, memberId, false, true);
		pnrCprMergeHelper.mergeCprToBookingModel(booking, cpr, new BookingBuildRequired());
		
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBookableUpgradeStatusCheck(true);
		
		FlightBookingDTOV2 bookingDTO = dtoConverter.convertToBookingDTO(booking, loginInfo);
		maskHelper.mask(bookingDTO);
		return bookingDTO;
	}
	
	@GetMapping("/cprbookingDTO/byrloc")
	@ApiOperation(value = "Get bookingDTO based on CPR by rloc", response = Booking.class, produces = "application/json" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "givenName", value = "Given name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "familyName", value = "Family name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "memberId", value = "member id", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		    @ApiImplicitParam(name = "Accept-Language", value = "Accept Language", required = true, dataType = "string", paramType = "header", defaultValue = ""),
	})
	public FlightBookingDTOV2 getBookingDTOBasedOnCPR(String rloc, String givenName, String familyName, String memberId, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException {
		LoginResponseDTO cpr = olciServiceV2.retrieveCPRBooking(rloc, null, givenName, familyName, memberId, false, true);
		Booking booking =pnrCprMergeHelper.buildBookingModelByCpr(cpr);
		
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBookableUpgradeStatusCheck(true);
		
		FlightBookingDTOV2 bookingDTO = dtoConverter.convertToBookingDTO(booking, loginInfo);
		maskHelper.mask(bookingDTO);
		return bookingDTO;
	}
}
