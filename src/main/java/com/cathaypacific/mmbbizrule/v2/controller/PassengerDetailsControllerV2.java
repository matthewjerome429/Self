package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeService;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.v2.business.PassengerDetailsBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails.PassengerDetailsResponseDTOV2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Zilong Bu on 01/03/2018.
 */
@Api(tags = {"PNR APIs"}, description = "PNR APIs")
@RestController
@RequestMapping(path = "/v2")
public class PassengerDetailsControllerV2 {
	
	private static LogAgent logger = LogAgent.getLogAgent(PassengerDetailsControllerV2.class);
	
	@Autowired
	private PassengerDetailsBusinessV2 passengerDetailsBusinessV2;
	
	@Autowired
	private NationalityCodeService nationalityCodeService;
	 
	@PutMapping("/passengerdetails")
	@ApiOperation(value = "Update passneger info", response = PassengerDetailsResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="requestDTO.rloc", argIndex = 0)
	public PassengerDetailsResponseDTOV2 updatePassengerDetails(@RequestBody @Validated UpdatePassengerDetailsRequestDTOV2 requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException{
		// OLSSMMB-16763: Don't remove me!!!
		// --- begin ---
		try {
			logger.info(String.format("Update | Passenger Info | Rloc | %s | Update Type | %s", requestDTO.getRloc(), requestDTO.getUpdateType()), true);
		} catch(Exception e) {

		}
		// --- end ---
		/** SUPPORT BACKWARD 	COMPATIBLE: CONVERT COUNTRY CODE TO COUNTRY NUMBER **/
		try {
			if (requestDTO.getPhoneInfo() != null) {
				String phoneCountryNumber = requestDTO.getPhoneInfo().getPhoneCountryNumber();	// 3-Digit
				String countyCode = requestDTO.getPhoneInfo().getCountryCode();	// 3-Letter
				
				// Convert country code to phoneCountryNumber
				if (StringUtils.isEmpty(phoneCountryNumber) && countyCode != null && countyCode.length() == 3) {
					String newCountryCode = nationalityCodeService.findTwoCountryCodeByThreeCountryCode(countyCode);
					String convertedCountryNumber = BizRulesUtil.convertIso2CountryCodeToPhoneCountryNumber(newCountryCode);
					requestDTO.getPhoneInfo().setPhoneCountryNumber(convertedCountryNumber);
					logger.info(String.format("Converted country code %s to phone country number %s", countyCode, convertedCountryNumber));
				}
				
				// Check if still null
				if (StringUtils.isEmpty(requestDTO.getPhoneInfo().getPhoneCountryNumber())) {
					throw new UnexpectedException("PhoneCountryNumber is still null after convertion", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
				}
			}
		} catch (Exception e) {
			logger.error("Failed to execute countyCode convertion", e);
		}
		/**
		 * END OF HOT FIX
		 */
		
		return passengerDetailsBusinessV2.updatePaxDetails(loginInfo, requestDTO, new BookingBuildRequired());
	}
	
	
}
