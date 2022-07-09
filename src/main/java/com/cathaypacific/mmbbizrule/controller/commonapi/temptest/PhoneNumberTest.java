package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/* REMOVE TEST CODE BEFORE GO LIVE R5.0
@Api(tags = {" Temp For Test"} , description= "just for test, will delete before go live")
@RestController
@RequestMapping(path = "/v1/common")
*/
public class PhoneNumberTest {

	@GetMapping("/phonetest/{phoneNumber}")
	@ApiOperation(value = "Retrieve traveldoc types by api version.", produces = "application/json")
	public PhoneNumTestResponseDTO phoneNumberTest(@PathVariable String phoneNumber) {

		PhoneNumTestResponseDTO result = new PhoneNumTestResponseDTO();

		String formattedPhoneNumber = BizRulesUtil
				.formatMobileNumber(BizRulesUtil.removeSpecialCharactersFromStr(phoneNumber));
		result.setFormattedNumber(formattedPhoneNumber);

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber phoneNumberObj;

		try {
			
			phoneNumberObj = phoneUtil.parse("+" + formattedPhoneNumber, null);
			//PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
			// PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
			System.out.println(phoneUtil.isPossibleNumber(phoneNumberObj));
			result.setValidPhoneNumber(phoneUtil.isValidNumberForRegion(phoneNumberObj, "HKG"));
			//System.out.println(phoneUtil.isPossibleNumberForType(phoneNumberObj, PhoneNumberUtil.PhoneNumberType.MOBILE));
			//result.
			result.setCountryNumber(String.valueOf(phoneNumberObj.getCountryCode()));
			String iso2CountryCode = phoneUtil.getRegionCodeForNumber(phoneNumberObj);
			if (StringUtils.isNotBlank(iso2CountryCode) && iso2CountryCode.length() == 2) {
				result.setIos2CountryCode(iso2CountryCode);
				Locale locale = new Locale("", iso2CountryCode);
				result.setIos3CountryCode(locale.getISO3Country());
			}
			
			if(StringUtils.isNotBlank(result.getCountryNumber())){
				result.setPhoneNumber(formattedPhoneNumber.substring(result.getCountryNumber().length()));
			}else{
				result.setPhoneNumber(formattedPhoneNumber);
			}
			result.setType(phoneUtil.getNumberType(phoneNumberObj).name());
			System.out.println(phoneUtil.getNumberType(phoneNumberObj).name());
		} catch (NumberParseException e) {
			result.setValidPhoneNumber(false);
			result.setPhoneNumber(formattedPhoneNumber);
		}

		return result;

	}
	
	public static void main(String[] args) {
		PhoneNumberTest test = new PhoneNumberTest();
		test.phoneNumberTest("13014531110");
		 
	}
}
