package com.cathaypacific.mmbbizrule.util;

import static com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants.CABIN_CLASS_BUSINESS_CLASS;
import static com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants.CABIN_CLASS_ECON_CLASS;
import static com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants.CABIN_CLASS_FIRST_CLASS;
import static com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants.CABIN_CLASS_PEY_CLASS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.ContactInfoTypeEnum;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OLCIConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.common.booking.PhoneInfoDTO;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class BizRulesUtil {

    private static final String CONTACT_SUFFIX = "/XX";

    // regex of date of birth, format YYYY-MM-DD
    private static final String DATE_OF_BIRTH_REGEX = "(?<year>\\S{4})-(?<month>[0-9]{2})-(?<day>\\S{2})";
    private static final String DATE_OF_BIRTH_YEAR_GROUP = "year";
    private static final String DATE_OF_BIRTH_MONTH_GROUP = "month";
    private static final String DATE_OF_BIRTH_DAY_GROUP = "day";
    private static final Pattern DATE_OF_BIRTH_PATTERN = Pattern.compile(DATE_OF_BIRTH_REGEX);
    private static final String LANGUAGE_SPLITTER = "-";
    private static final String LANGUAGE_UNDERLINE = "_";
    
    private BizRulesUtil() {

    }

    /**
     * get year from date of birth string
     *
     * @param dateOfBirthString
     * @return String
     */
    public static String getYearFromDateOfBirth(String dateOfBirthString) {
        if (StringUtils.isEmpty(dateOfBirthString)) {
            return "";
        }

        Matcher matcher = DATE_OF_BIRTH_PATTERN.matcher(dateOfBirthString);

        //if no match, return ""
        if (!matcher.matches()) {
            return "";
        } else {
            return matcher.group(DATE_OF_BIRTH_YEAR_GROUP);
        }
    }

    /**
     * get year from date of birth string
     *
     * @param dateOfBirthString
     * @return String
     */
    public static String getMonthFromDateOfBirth(String dateOfBirthString) {
        if (StringUtils.isEmpty(dateOfBirthString)) {
            return "";
        }

        Matcher matcher = DATE_OF_BIRTH_PATTERN.matcher(dateOfBirthString);

        //if no match, return ""
        if (!matcher.matches()) {
            return "";
        } else {
            return matcher.group(DATE_OF_BIRTH_MONTH_GROUP);
        }
    }

    /**
     * get year from date of birth string
     *
     * @param dateOfBirthString
     * @return String
     */
    public static String getDayFromDateOfBirth(String dateOfBirthString) {
        if (StringUtils.isEmpty(dateOfBirthString)) {
            return "";
        }

        Matcher matcher = DATE_OF_BIRTH_PATTERN.matcher(dateOfBirthString);

        //if no match, return ""
        if (!matcher.matches()) {
            return "";
        } else {
            return matcher.group(DATE_OF_BIRTH_DAY_GROUP);
        }
    }

    /**
     * Convert from free text of contact to normal format.<br>
     * <br>
     * mobile: 85212345678/XX, Email: ABC//CATHAYPACIFIC.COM/XX<br>
     * To<br>
     * mobile: 85212345678, Email: ABC//CATHAYPACIFIC.COM
     *
     * @param contactText
     * @return normal format contact.
     */
    public static String convertContactFormat(String freeText) {
        String result = null;
        if (StringUtils.isNotEmpty(freeText)) {
            result = freeText.substring(0, freeText.indexOf(CONTACT_SUFFIX));
        }
        return result;
    }

    /**
     * Determine whether free text of contact (mobile, Email) is validated [ends with "/XX"].
     *
     * @param contactText
     * @return
     */
    public static boolean contactIsValidated(String contactText) {
        if (StringUtils.isEmpty(contactText)) {
            return false;
        } else {
            return contactText.contains(CONTACT_SUFFIX);
        }
    }

    /**
     * format email from freeText
     * \w[-.\w]*\@[-\w]+(\.\w+)+
     *
     * @param freeText
     * @return
     */
    public static String formatEmail(String freeText) {
        String res = "";
        Pattern p = Pattern.compile("\\w[-.\\w]*\\@[-\\w]+(\\.\\w+)+");
        Matcher m = p.matcher(freeText);
        if (m.find()) {
            res = m.group();
        }
        return res.toLowerCase();
    }

    /**
     * convert email replace:
     * "//"  --> "@" (AT SIGN)
     * ".."  --> "_" (UNDERSCORED)
     * "./"  --> "-" (DASH)
     *
     * @param email
     * @return
     */
    public static String convertEmailToDisplay(String email) {
        String result = null;
        if (!StringUtils.isEmpty(email)) {
            result = email.replace("//", "@").replace("..", "_").replace("./", "-");
        }
        return result;
    }

    /**
     * vaild mobile number
     * get the longest number from freeText
     *
     * @param freeText
     * @return
     */
    public static String formatMobileNumber(String freeText) {
        String res = "";
        if (StringUtils.isEmpty(freeText)) {
            return res;
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(freeText);
        while (matcher.find()) {
            if (res.length() < matcher.group().length()) {
                res = matcher.group();
            }
        }
        return res;
    }

    /**
     * split mobileNumber
     *
     * @param mobileNumber
     * @return String[], length = 2, String[0]=mobile country number;String[1] = mobile number without country code
     */
    public static String[] splitMobileNumber(String mobileNumber) {
        String[] result = new String[2];
        if (StringUtils.isEmpty(mobileNumber)) {
            return result;
        }
        String formatMobileNumber = mobileNumber;
        if (!mobileNumber.startsWith("+")) {
            formatMobileNumber = "+" + mobileNumber;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber numberProto;
        try {
            numberProto = phoneUtil.parse(formatMobileNumber, null);
            result[0] = String.valueOf(numberProto.getCountryCode());
            int phoneNumIndex = 0;
            if (!StringUtils.isEmpty(result[0])) {
                phoneNumIndex = formatMobileNumber.indexOf(result[0]) + result[0].length();
            }
            result[1] = formatMobileNumber.substring(phoneNumIndex);
        } catch (NumberParseException e) {
//			LOGGER.info("Canot parse MobileNumber:"+mobileNumber);
//			LOGGER.debug("Canot parse MobileNumber:"+mobileNumber,e);
        }
        return result;
    }

    /**
     * convert list of BigInteger to List of String
     *
     * @param list
     * @return
     */
    public static List<String> convertToStringList(List<BigInteger> list) {
        List<String> resultList = new ArrayList<>();
        for (BigInteger bit : list) {
            resultList.add(bit.toString());
        }
        return resultList;
    }

    /**
     * handle the null value of String
     *
     * @param String
     * @return
     */
    public static String nvl(String source) {
        return source == null ? "" : source;
    }

    /**
     * determine segment is train or not by airCraftType.
     *
     * @param airCraftType
     * @return
     */
    public static boolean isTrain(String airCraftType) {
        if (OneAConstants.EQUIPMENT_TRN.equalsIgnoreCase(airCraftType)) {
            return true;
        }
        return false;
    }

    /**
     * determine segment is buss or not by airCraftType.
     *
     * @param airCraftType
     * @return
     */
    public static boolean isBuss(String airCraftType) {
        return OneAConstants.EQUIPMENT_BUS.equalsIgnoreCase(airCraftType);
    }

    /**
     * determine segment is ferry or not by airCraftType.
     *
     * @param airCraftType
     * @return
     */
    public static boolean isFerry(String airCraftType) {
        return OneAConstants.EQUIPMENT_LCH.equalsIgnoreCase(airCraftType);
    }

    /**
     * determine segment is flight or not by airCraftType.
     *
     * @param airCraftType
     * @return
     */
    public static boolean isFlight(String airCraftType) {
        return !isBuss(airCraftType) && !isTrain(airCraftType) && !isFerry(airCraftType);
    }

    /**
     * remove special characters from str
     *
     * @param str
     * @return String
     */
    public static String removeSpecialCharactersFromStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String specialCharacters = MMBBizruleConstants.SPECIAL_CHARACTERS;
        for (int i = 0; i < specialCharacters.length(); i++) {
            str = str.replace(String.valueOf(specialCharacters.charAt(i)), "");
        }
        return str;
    }

    /**
     * Check is flight(amadeus) rloc.
     *
     * @param rloc
     * @return
     */
    public static boolean isFlightRloc(String rloc) {

        return !StringUtils.isEmpty(rloc) && rloc.length() == 6;

    }

    /**
     * Check is Spnr/OJ rloc.
     *
     * @param rloc
     * @return
     */
    public static boolean isSPNR(String rloc) {

        return !StringUtils.isEmpty(rloc) && rloc.length() == 7;

    }

    /**
     * parser phone number to PhoneInfoDTO
     *
     * @param memberPhone
     * @return PhoneInfoDTO
     */
    public static PhoneInfoDTO parserPhoneNumber(String phoneNumber) {
        PhoneInfoDTO phoneInfo = new PhoneInfoDTO();
        String formattedPhoneNumber = formatMobileNumber(removeSpecialCharactersFromStr(phoneNumber));
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber phoneNumberObj;
        phoneInfo.setType(ContactInfoTypeEnum.MEMBER_PROFILE_CONTACT_INFO.getType());
        try {
            phoneNumberObj = phoneUtil.parse("+" + formattedPhoneNumber, null);
            if (!phoneUtil.isValidNumber(phoneNumberObj)) {
                phoneInfo.setCountryCode(StringUtils.EMPTY);
                phoneInfo.setPhoneCountryNumber(StringUtils.EMPTY);
                phoneInfo.setPhoneNo(formattedPhoneNumber);
            } else {
                phoneInfo.setPhoneCountryNumber(String.valueOf(phoneNumberObj.getCountryCode()));
                String iso2CountryCode = phoneUtil.getRegionCodeForNumber(phoneNumberObj);

                if (StringUtils.isNotEmpty(iso2CountryCode) && iso2CountryCode.length() == 2) {
                    Locale locale = new Locale("", iso2CountryCode);
                    phoneInfo.setCountryCode(locale.getISO3Country());
                }

                if (StringUtils.isNotEmpty(phoneInfo.getCountryCode())
                        && StringUtils.isNotEmpty(phoneInfo.getPhoneCountryNumber())) {
                    phoneInfo.setPhoneNo(formattedPhoneNumber.substring(phoneInfo.getPhoneCountryNumber().length()));
                } else {
                    phoneInfo.setCountryCode(StringUtils.EMPTY);
                    phoneInfo.setPhoneCountryNumber(StringUtils.EMPTY);
                    phoneInfo.setPhoneNo(formattedPhoneNumber);
                }
            }
        } catch (NumberParseException e) {
            phoneInfo.setCountryCode(StringUtils.EMPTY);
            phoneInfo.setPhoneCountryNumber(StringUtils.EMPTY);
            phoneInfo.setPhoneNo(formattedPhoneNumber);
        }
        return phoneInfo;
    }

    /**
     * convertIso2CountryCodeToPhoneCountryNumber
     *
     * @param countryCode
     * @return PhoneCountryNumber
     */
    public static String convertIso2CountryCodeToPhoneCountryNumber(String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        return Integer.toString(phoneUtil.getCountryCodeForRegion(countryCode));
    }

    /**
     * convert PhoneNumber To Iso2CountryCode
     *
     * @param phoneNumber
     * @return
     */
    public static String convertPhoneNumberToIso2CountryCode(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber number;
        try {
            number = phoneUtil.parse("+" + phoneNumber, null);
            return phoneUtil.getRegionCodeForNumber(number);
        } catch (NumberParseException e) {
            // do nothing
        }
        return StringUtils.EMPTY;
    }

    /**
     * count subString in string
     *
     * @param subStr
     * @param string
     * @return int
     */
    public static int countSubstring(String subStr, String str) {
        return (str.length() - str.replace(subStr, "").length()) / subStr.length();
    }

    /**
     * convert CallingCode To Iso3CountryCode
     *
     * @param countryCallingCode
     * @return
     */
    public static String convertCallingCodeToIso3CountryCode(String countryCallingCode) {
        if (StringUtils.isEmpty(countryCallingCode)) {
            return null;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String regionCode = phoneUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCallingCode));
        if (StringUtils.isNotEmpty(regionCode) && regionCode.length() == 2) {
            Locale locale = new Locale("", regionCode);
            return locale.getISO3Country();
        }
        return regionCode;
    }

    // return the readable name with its cabin class
    public static String getDisplayedClassNameWithCabinClass(String subclass) {
        if(StringUtils.equals(subclass, CABIN_CLASS_FIRST_CLASS)) {
            return "First";
        } else if(StringUtils.equals(subclass, CABIN_CLASS_BUSINESS_CLASS)) {
            return "Business";
        } else if(StringUtils.equals(subclass, CABIN_CLASS_PEY_CLASS)) {
            return "Premium Economy";
        } else if(StringUtils.equals(subclass, CABIN_CLASS_ECON_CLASS)) {
            return "Economy";
        } else {
            return subclass;
        }
    }
    
    /**
     * Convert Language type
     * en-HK => en_HK
     * @param language
     * @return
     */
    public static String convertLanguage(String language) {
    	 if (StringUtils.isEmpty(language)) {  
         return null;  
       }
    	 
    	 return StringUtils.replace(language, LANGUAGE_SPLITTER, LANGUAGE_UNDERLINE);  
		}
    
  	/**
     * has stop error or not
     * 
     * @param errors
     * @return
     */
    public static boolean hasOlciStopError(List<ErrorInfo> errors) {
        return !CollectionUtils.isEmpty(errors) && errors.stream()
                .anyMatch(error -> error !=null && OLCIConstants.CPR_ERROR_INFO_TYPE_S.equalsIgnoreCase(error.getType().getType()));
    }
}
