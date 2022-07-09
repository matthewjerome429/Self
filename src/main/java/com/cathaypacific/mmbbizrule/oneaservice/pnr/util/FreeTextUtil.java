package com.cathaypacific.mmbbizrule.oneaservice.pnr.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class FreeTextUtil {
	
	/**
	 * format of RM protection freeText
	 * PROTECTION: PASSENGER [Pax Surname + "/" + Given Name] ACCEPT CHANGE [Date/Time] FOR [Flight no. with TK status]/[departure date] via MMB
	 */
	public static final String REBOOK_PROTECTION_REMARK_FORMAT = "PROTECTION: PASSENGER %s/%s ACCEPT CHANGE %s FOR %s/%s VIA %s";
	
	//regex of eticket freetext
	private static final String E_TICKET_FREE_TEXT_REGEX = "(((?<passengerType>PAX|INF) (?<number>\\d{3}-\\d{10})(?<range>-\\d+){0,1}){0,1}\\/){0,1}((?<unknown1>(ET[\\da-zA-Z]{0,4}){0,1}|ET([a-zA-Z]*){0,1}){0,1}\\/){0,1}(((?<currency>[a-zA-Z]{3})(?<amount>([\\d]+(\\.{0,1}\\d*){0,1}))){0,1}\\/){0,1}((?<date>\\d{2}[a-zA-Z]{3}\\d{2}){0,1}\\/){0,1}((?<officeId>[a-zA-Z][\\da-zA-Z-]+){0,1}\\/){0,1}((?<unknown2>[\\d]+){0,1}\\/){0,1}";
	private static final String E_TICKET_FREE_TEXT_PASSENGER_TYPE_GROUP = "passengerType";
	private static final String E_TICKET_FREE_TEXT_NUMBER_GROUP = "number";
	private static final String E_TICKET_FREE_TEXT_RANGE_GROUP = "range";
	private static final String E_TICKET_FREE_TEXT_DATE_GROUP = "date";
	private static final String E_TICKET_FREE_TEXT_CURRENCY_GROUP = "currency";
	private static final String E_TICKET_FREE_TEXT_AMOUNT_GROUP = "amount";
	private static final String E_TICKET_FREE_TEXT_OFFICE_ID_GROUP = "officeId";
	private static final Pattern E_TICKET_FREE_TEXT_PATTERN = Pattern.compile(E_TICKET_FREE_TEXT_REGEX);
	//regex of emergency contact freetext
	private static final String EMR_CONTACT_FREE_TEXT_REGEX = "(?<contactName>[a-z A-Z]{0,64})/(?<countryCode>([A-Z]{2,3})|)(?<phoneNumber>[0-9]{0,25}).*";
	private static final String EMR_CONTACT_FREE_TEXT_CONTACT_NAME_GROUP = "contactName";
	private static final String EMR_CONTACT_FREE_TEXT_COUNTRY_CODE_GROUP = "countryCode";
	private static final String EMR_CONTACT_FREE_TEXT_PHONE_NUMBER_GROUP = "phoneNumber";
	private static final Pattern EMR_CONTACT_FREE_TEXT_PATTERN = Pattern.compile(EMR_CONTACT_FREE_TEXT_REGEX);
	//regex of destination address freetext
	private static final String ADDRESS_DETAILS_FREE_TEXT_REGEX = "(?<type>[A-Z]*)/(?<country>[A-Z]*|)(/|)(?<streetAddress>[a-z A-Z0-9]*|)(/|)(?<city>[a-z A-Z0-9]*|)(/|)(?<state>[A-Z]*|)(/|)(?<zipCode>[a-z A-Z0-9]*|)(?<infIndicator>(/I){1}|)";
	private static final String ADDRESS_DETAILS_FREE_TEXT_TYPE_GROUP = "type";
	private static final String ADDRESS_DETAILS_FREE_TEXT_COUNTRY_GROUP = "country";
	private static final String ADDRESS_DETAILS_FREE_TEXT_STREET_ADDRESS_GROUP = "streetAddress";
	private static final String ADDRESS_DETAILS_FREE_TEXT_CITY_GROUP = "city";
	private static final String ADDRESS_DETAILS_FREE_TEXT_STATE_GROUP = "state";
	private static final String ADDRESS_DETAILS_FREE_TEXT_ZIP_CODE_GROUP = "zipCode";
	private static final String ADDRESS_DETAILS_FREE_TEXT_INF_INDICATOR_GROUP = "infIndicator";
	private static final Pattern ADDRESS_DETAILS_FREE_TEXT_PATTERN = Pattern.compile(ADDRESS_DETAILS_FREE_TEXT_REGEX);
	
	//regex of baggage string
	private static final String BAGGAGE_STRING_REGEX = "(?<amount>[0-9]+)(?<unit>K|P)";
	private static final String BAGGAGE_STRING_AMOUNT_GROUP = "amount";
	private static final String BAGGAGE_STRING_UNIT_GROUP = "unit";
	private static final Pattern BAGGAGE_STRING_AMOUNT_PATTERN = Pattern.compile(BAGGAGE_STRING_REGEX);
	
	//regex of purchased baggage free text
	private static final String PURCHASED_BAGGAGE_FREE_TEXT_REGEX = "TTL(?<weightAmount>[0-9]*)KG(?<pieceAmount>[0-9]*)PC.*";
	private static final String PURCHASED_BAGGAGE_FREE_WEIGHT_AMOUNT_GROUP = "weightAmount";
	private static final String PURCHASED_BAGGAGE_FREE_PIECE_AMOUNT_GROUP = "pieceAmount";
	private static final Pattern PURCHASED_BAGGAGE_FREE_TEXT_PATTERN = Pattern.compile(PURCHASED_BAGGAGE_FREE_TEXT_REGEX);
	
	//regex of BCODE free text
	private static final String BCODE_FREE_TEXT_REGEX = ".*BCODE[0-9A-Y]{7}Z.*";
	private static final Pattern BCODE_FREE_TEXT_PATTERN = Pattern.compile(BCODE_FREE_TEXT_REGEX);

	//regex of reBook NOTIFLY free text
	private static final String REBOOK_NOTIFLY_FREE_TEXT_REGEX = "NOTIFLY:\\s*(?<cancelledFlights>[a-z A-Z0-9]*)\\s*FLIGHT\\s*PROTECTION\\s*EMAIL\\s*SENT\\s*AT\\s*(?<date>[0-9]{8})/(?<time>[0-9]{4})/(?<email>(\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14})|)/(?<origin>[A-Z]{3})-(?<destination>[A-Z]{3})/(?<newBookFlights>[a-z A-Z0-9]*)/(?<bookDate>[a-z A-Z0-9]*)/.*";
	private static final String REBOOK_NOTIFLY_CANCELLED_FLGIHTS = "cancelledFlights";
	private static final String REBOOK_NOTIFLY_CANCELLED_FLGIHTS_ORIGIN = "origin";
	private static final String REBOOK_NOTIFLY_CANCELLED_FLGIHTS_DESTINATION = "destination";
	private static final String REBOOK_NOTIFLY_CANCELLED_NEW_BOOK_FLIGHTS = "newBookFlights";
	private static final String REBOOK_NOTIFLY_CANCELLED_NEW_BOOK_DATES = "bookDate";
	private static final Pattern REBOOK_NOTIFLY_FREE_TEXT_PATTERN = Pattern.compile(REBOOK_NOTIFLY_FREE_TEXT_REGEX);
	private static final String CANCELLED_FLIGHTS_REGEX = "(?<flightCode>[A-Z]{2}\\d{3,4}?)\\s*(?<flightTime>\\d{1,2}[A-Z]{3})";
	private static final String NEW_BOOKING_FLIGHTS_REGEX = "(?<flightCode>[A-Z]{2}\\d{3,4})";
	private static final String REBOOKING_NOTIFLY_FLIGHTS_FLIGHT_CODE = "flightCode";
	private static final String REBOOKING_NOTIFLY_FLIGHTS_FLIGHT_TIME = "flightTime";
	private static final Pattern CANCELLED_FLIGHTS_PATTERN = Pattern.compile(CANCELLED_FLIGHTS_REGEX);
	private static final Pattern NEW_BOOKING_FLIGHTS_PATTERN = Pattern.compile(NEW_BOOKING_FLIGHTS_REGEX);
	
	//regex of reBook ACCEPT free text
	private static final String REBOOK_ACCEPT_FREE_TEXT_REGEX = "PROTECTION: PASSENGER (?<familyName>[a-z A-Z0-9]*)/(?<givenName>[a-z A-Z0-9]*) ACCEPT CHANGE (?<date>[a-z A-Z0-9]*)/(?<time>[: a-z A-Z0-9]*) FOR (?<acceptedFlights>[a-z A-Z0-9]*)/(?<departureDate>[a-z A-Z0-9]*) VIA (?<app>[a-z A-Z0-9]*)";
	private static final String REBOOK_ACCEPT_FAMILY_NAME = "familyName";
	private static final String REBOOK_ACCEPT_GIVEN_NAME = "givenName";
	private static final String REBOOK_ACCEPT_FLIGHTS = "acceptedFlights";
	private static final Pattern REBOOK_ACCEPT_FREE_TEXT_PATTERN = Pattern.compile(REBOOK_ACCEPT_FREE_TEXT_REGEX);
	
	//regex of eticket freetext/
	/**
	 * format of PAYMENT_FREE_TEXT
	 * PAX 160-2367041334/ETCX/HKD8905/28AUG18/HKGCX0300/77491982
	 */
	private static final String PAYMENT_FREE_TEXT_REGEX = ".*(?<ticket>([0-9]{3})-([0-9]{10}))/(DT(CX|KA)/){1}(?<currency>\\D+)(?<amount>(\\d+(\\.\\d+)?))/(?<date>.*)/(?<officeId>.*)/.*";
	private static final String PAYMENT_FREE_TEXT_CURRENCY = "currency";
	private static final String PAYMENT_FREE_TEXT_AMOUNT = "amount";
	private static final String PAYMENT_FREE_TEXT_DATE = "date";
	private static final String PAYMENT_FREE_TEXT_OFFICEID = "officeId";
	private static final String PAYMENT_FREE_TEXT_TICKET = "ticket";
	private static final Pattern PAYMENT_FREE_TEXT_PATTERN = Pattern.compile(PAYMENT_FREE_TEXT_REGEX);
	/**
	 * format of PAYMENT_FREE_TEXT_WITHOUT_PAYMENT
	 * PAX 160-4552184133/DTCX/28AUG18/HKGCX0ERS/13010454
	 */
	private static final String PAYMENT_FREE_TEXT_REGEX_WITHOUT_PAYMENT = ".*(?<ticket>([0-9]{3})-([0-9]{10}))/(DT(CX|KA)/){1}(?<date>.*)/(?<officeId>.*)/.*";
	private static final Pattern PAYMENT_FREE_TEXT_PATTERN_WITHOUT_PAYMENT = Pattern.compile(PAYMENT_FREE_TEXT_REGEX_WITHOUT_PAYMENT);
	
	//regex of SK XLMR MCO freetext
	private static final String SK_XLMR_MCO_FREE_TEXT_REGEX = "MCO (?<eticket>([0-9]{3})-([0-9]{10})).*";
	private static final Pattern SK_XLMR_MCO_FREE_TEXT_PATTERN = Pattern.compile(SK_XLMR_MCO_FREE_TEXT_REGEX);

	//regex of Dynamic Waiver freetext /**eg:PAX WAIVECXCHB5 - VLD CX/KA NONEND.REF/RBK RTE FEEAPPLYADDONCXR RESTR*/
    private static final String DW_FREE_TEXT_REGEX = "PAX WAIVE(?:CX|KA)(?<dwcode>[0-9a-zA-Z]*)";
    private static final String DW_FREE_TEXT_DWCODE_GROUP = "dwcode";

	//regex of staff os string
	private static final String STAFF_OS_STRING_REGEX = "(?<type>[a-zA-Z]{2})[a-zA-Z0-9]{0,4}/.*";
	private static final String  STAFF_OS_STRING_TYPE = "type";
	private static final Pattern STAFF_OS_STRING_PATTERN = Pattern.compile(STAFF_OS_STRING_REGEX);
	private static final String STAFF_OS_ERN = "ERN";
    
	
	//regex of staff os string
	private static final String STAFF_STFD_STRING_REGEX = "([a-zA-Z]+)-(?<priority>\\d{1,2})[a-zA-Z]+/[a-zA-Z0-9-/]+";
	private static final Pattern STAFF_STFD_STRING_PATTERN = Pattern.compile(STAFF_STFD_STRING_REGEX);
	private static final String STAFF_STFD_STRING_PRIORITY = "priority";
	
	
	//regex of BOH RM string
	private static final String BOH_RM_STRING_REGEX = "STATUS:ON HOLD(/FEE(?<amount>[0-9.]+)( ){0,}(?<currency>[a-zA-Z]+){0,3}.*){0,1}/.*";
	private static final Pattern BOH_RM_STRING_PATTERN = Pattern.compile(BOH_RM_STRING_REGEX);
	private static final String BOH_RM_STRING_PATTERN_AMOUNT = "amount";
	private static final String BOH_RM_STRING_PATTERN_CURRENCY = "currency";
	
	//regex of Mbr Entitlement
	private static final String ME_RM_STRING_REGEX = "(\\bMBR ENTITLEMENT BKUG REQUESTED BY MMB TO ID *\\b)([a-jA-J]*)";
	private static final Pattern ME_RM_STRING_PATTERN = Pattern.compile(ME_RM_STRING_REGEX);
	private static final List<String> DIGIT_LETTER_MAPPING = Collections.unmodifiableList(Arrays.asList("j", "a", "b", "c", "d", "e", "f", "g", "h", "i"));
	
	//OS SITE
	private static final String SITE_OS_STRING_REGEX = "BOOKING SITE (?<country>[a-zA-Z]{2})(-(?<company>[a-zA-Z]{2})){0,1}.*";
	private static final Pattern SITE_OS_STRING_PATTERN = Pattern.compile(SITE_OS_STRING_REGEX);
	private static final String SITE_OS_STRING_COUNTRY = "country";
	private static final String SITE_OS_STRING_COMPANY = "company";
	
	//SK CUST
	private static final String SK_CUST_FREETEXT_REGEX = "(?<ffp>\\d*):.*";
	private static final Pattern SK_CUST_FREETEXT_PATTERN = Pattern.compile(SK_CUST_FREETEXT_REGEX);
	private static final String  SK_CUST_FREETEXT_FFP = "ffp";
	
	// UMNR SSR Age
	private static final String SSR_UMNR_FREETEXT_AGE = "(\\d+)\\D*$";
	
	// UMNR OSI Freetext
	private static final String OSI_UMNR_FREETEXT_REGEX = "UMNR/(?<portCode>[a-zA-Z]{3})/(?<name>.*)/(?<phoneNumber>.*)";
	private static final String OSI_UMNR_FREETEXT_PORT_CODE = "portCode";
	private static final String OSI_UMNR_FREETEXT_NAME = "name";
	private static final String OSI_UMNR_FREETEXT_PHONE_NUMBER = "phoneNumber";
	
	// SSR INFT
	private static final String SSR_INFT_FREETEXT_REGEX = "(?<familyName>[a-zA-Z]+)/(?<givenName>[a-zA-Z ]+) (?<birthDate>[0-9]{2}[a-zA-Z]{3}[0-9]{2})";
	private static final String SSR_INFT_FAMILY_NAME = "familyName";
	private static final String SSR_INFT_GIVEN_NAME = "givenName";
	private static final String SSR_INFT_BIRTH_DATE = "birthDate";
	private static final Pattern SSR_INFT_FREETEXT_PATTERN = Pattern.compile(SSR_INFT_FREETEXT_REGEX);
	
	// Occupy INFT(INS) ssr  Freetext KEY
	private static final String SSR_INFT_OCCUPY_KEYWORD = "OCCUPY";
	
	// FP freeText
	private static final String FP_FREETEXT_NO_STORED_CARD_REGEX = "(PAX |INF ){0,1}(?<cardIndicator>(CC(?<cardType>[a-zA-Z]*)))(/(?<cardNumber>[0-9X]*))(/(?<expireDate>[0-9]{4})[\\da-zA-Z-]*)(/(?<currency>[a-zA-Z]{3})(?<amount>(\\d+(\\.\\d+)?))){0,1}(/(?<approvalCode>.*)){0,1}";
	private static final String FP_FREETEXT_NO_STORED_CARD_INDICATOR = "cardIndicator";
	private static final String FP_FREETEXT_NO_STORED_CARD_TYPE = "cardType";
	private static final String FP_FREETEXT_NO_STORED_CARD_NUMBER = "cardNumber";
	private static final String FP_FREETEXT_NO_STORED_CARD_EXPIRY_DATE = "expireDate";
	private static final String FP_FREETEXT_NO_STORED_CARD_CURRENCY = "currency";
	private static final String FP_FREETEXT_NO_STORED_CARD_AMOUNT = "amount";
	private static final String FP_FREETEXT_NO_STORED_CARD_APPROVAL_CODE = "approvalCode";
	private static final Pattern FP_FREETEXT_NO_STORED_CARD_PATTERN = Pattern.compile(FP_FREETEXT_NO_STORED_CARD_REGEX);
	
	private static final String FP_FREETEXT_STORED_CARD_REGEX = ".*(?<cardIndicator>IBE|CASH)\\*(?<cardType>[A-Z]*)(?<approvalCode>[0-9]*)\\*(?<cardNumber>[0-9]*)";
	private static final String FP_FREETEXT_STORED_CARD_INDICATOR = "cardIndicator";
	private static final String FP_FREETEXT_STORED_CARD_TYPE = "cardType";
	private static final String FP_FREETEXT_STORED_CARD_APPROVAL_CODE = "approvalCode";
	private static final String FP_FREETEXT_STORED_CARD_NUMBER_FIREST_6_DIGIT = "cardNumber";
	private static final Pattern FP_FREETEXT_STORED_CARD_PATTERN = Pattern.compile(FP_FREETEXT_STORED_CARD_REGEX);
	
	/**
	 * SPNR SK freetext sample: "5UL6YEQ/FO/FLT/MOB".
	 * */
	private static final String SK_SPNR_FREETEXT_REGEX = "^(\\w+)/(\\w+)/(\\w+)/(\\w+)$";
	private static final Pattern SK_SPNR_FREETEXT_PATTERN = Pattern.compile(SK_SPNR_FREETEXT_REGEX);
	
	/**
     * preselect meal freetext sample: "SSR SPML PJ2E01D02A03 CX888 HKGYVR".
     * */
    private static final String PRESELCTED_MEAL_FREETEXT_REGEX = "[PSU][FJWY][0-9][A-Z0-9]{3}";
    private static final Pattern PRESELCTED_MEAL_FREETEXT_PATTERN = Pattern.compile(PRESELCTED_MEAL_FREETEXT_REGEX);
	
	private FreeTextUtil(){
		
	}
	/**
	 * Get ffp from free text.
	 * @param freeText
	 * @return ffp
	 */
	public static String getFfpFromCustDFreeText(String freeText){
		if(StringUtils.isEmpty(freeText)){
			return null;
		}
		Matcher matcher = SK_CUST_FREETEXT_PATTERN.matcher(freeText);
		if (matcher.matches()) {
			return matcher.group(SK_CUST_FREETEXT_FFP);
		} else {
			return null;
		}
	}
	
	/**
	 * Parse OSI booking site info
	 * 
	 * @param freeText
	 * @return String[2], index 0:country 1:company
	 */
	public static String[] parseOSISiteInfo(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		
		Matcher matcher = SITE_OS_STRING_PATTERN.matcher(freeText);
		if (matcher.matches()) {
			String[] result = new String[2];
			result[0] = matcher.group(SITE_OS_STRING_COUNTRY);
			result[1] = matcher.group(SITE_OS_STRING_COMPANY);
			return result;
		}
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
	/**
	 * Parser On hold remark free text 
	 * @param freeText
	 * @return String[2], index 0:amount 1:currency
	 */
	public static String[] parserOnHoldText(String freeText) {

		if (StringUtils.isEmpty(freeText)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		Matcher matcher = BOH_RM_STRING_PATTERN.matcher(freeText);
		if (matcher.matches()) {
			String[] result = new String[2];
			result[0] = matcher.group(BOH_RM_STRING_PATTERN_AMOUNT);
			result[1] = matcher.group(BOH_RM_STRING_PATTERN_CURRENCY);
			return result;
		}
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
	/**
	 * parser Mbr Entitlement Text
	 * @param freeText
	 * @return entitlementId
	 */
	public static String parserMbrEntitlementText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return null;
		}
		Matcher matcher = ME_RM_STRING_PATTERN.matcher(freeText);
		if(matcher.find()){
			return decodeNumber(matcher.group(2).trim());
		}
		return null;
	}
	
	/**
	 * Replace "a-j" with "0-9" to decrypt number 
	 * Use case: decrypt EntitlementId, decrypt phone number, etc.
	 * @param encodedNumber
	 * @return
	 */
	public static String decodeNumber(String encodedNumber) {
		if(StringUtils.isEmpty(encodedNumber)) {
			return StringUtils.EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		
		char[] idChars = encodedNumber.toCharArray();
		for(char idChar : idChars) {
			int decodedDigit = DIGIT_LETTER_MAPPING.indexOf(String.valueOf(idChar).toLowerCase());
			if (decodedDigit > -1) {
				sb.append(decodedDigit);
			} else {
				sb.append(String.valueOf(idChar));
			}
		}

		return sb.toString();
	}
	
	/**
	 * Replace "0-9" with "a-j" to encode number and avoid 1A auto mask
	 * Use case: encode entitlementId, encrypt phone number, etc. 
	 * 
	 * @param numberStr
	 * @return
	 */
	public static String encodeNumber(String numberStr) {
		if(StringUtils.isEmpty(numberStr)) {
			return StringUtils.EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		
		char[] idChars = numberStr.toCharArray();
		for(char idChar : idChars) {
			String encodedDigit = null;
			try {
				encodedDigit = DIGIT_LETTER_MAPPING.get(Integer.parseInt(String.valueOf(idChar)));
				sb.append(encodedDigit);
			} catch (Exception e) {
				sb.append(String.valueOf(idChar));
			}
		}

		return sb.toString();
	}
	
	/**
	 * parser staff OS info from text
	 * @param freeText
	 * @return String[], index 0:type
	 */
	public static String[] getStaffOSInfoFromFreeText(String freeText) {

		if(StringUtils.isEmpty(freeText)){
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		
		Matcher matcher = STAFF_OS_STRING_PATTERN.matcher(freeText);
		// if no match, return empty array
		
		if (matcher.matches()) {
			String[] result = new String[1];
			result[0] = matcher.group(STAFF_OS_STRING_TYPE);
			return result;
		} else {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}
	/**
	 * parser staff OS info from text
	 * @param freeText
	 * @return String[], index 0:priority
	 */
	public static String[] getStaffSKInfoFromFreeText(String freeText) {

		if(StringUtils.isEmpty(freeText)){
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		
		Matcher matcher = STAFF_STFD_STRING_PATTERN.matcher(freeText);
		// if no match, return empty array
		
		if (matcher.matches()) {
			String[] result = new String[1];
			result[0] = matcher.group(STAFF_STFD_STRING_PRIORITY);
			return result;
		} else {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}
	/**
	 * Parser the payment info from free text  
	 * @param freeText
	 * @return String[], index 0:currency; 1:amount; 2:date(yyMMMdd); 3:office id; 4:ticket
	 */
	public static String[] getPaymentInfoFromFreeText(String freeText) {

		if(StringUtils.isEmpty(freeText)){
			return null;
		}
		
		Matcher matcher = PAYMENT_FREE_TEXT_PATTERN.matcher(freeText);
		// if no match, return empty array5
		
		if (!matcher.matches()) {
			matcher = PAYMENT_FREE_TEXT_PATTERN_WITHOUT_PAYMENT.matcher(freeText);
			return matcher.matches() ? getPaymentInfoFromDifferentFreeText(matcher, false) : null;
		} else {
			return getPaymentInfoFromDifferentFreeText(matcher, true);
		}
	}
	
	/**
	 * @param matcher
	 * @param haveAmount
	 * @return String[]
	 */
	private static String[] getPaymentInfoFromDifferentFreeText(Matcher matcher, boolean haveAmount) {
		String[] paymentInfo = new String[5];
		if(haveAmount) {
			// currency on index 0
			paymentInfo[0] = matcher.group(PAYMENT_FREE_TEXT_CURRENCY);
			// amount on index 0
			paymentInfo[1] = matcher.group(PAYMENT_FREE_TEXT_AMOUNT);			
		}
		paymentInfo[2] = matcher.group(PAYMENT_FREE_TEXT_DATE);
		paymentInfo[3] = matcher.group(PAYMENT_FREE_TEXT_OFFICEID);
		paymentInfo[4] = matcher.group(PAYMENT_FREE_TEXT_TICKET).replace("-", "");
		return paymentInfo;
	}
	
	/**
	 * Parse e-ticket information from freeText
	 * 
	 * @param freeText
	 * @return Object[]
	 * index -	0: list of e-Ticket numbers; 
	 * 			1: date(ddMMMyy);
	 * 			2: passengerType;
	 * 			3: currency;
	 * 			4: amount;
	 */
	public static Object[] getETicketInfoFromFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return null;
		}
		
		// The REGEX of ET must be end with "/" to explicitly indicate the end
		if (!freeText.endsWith("/")) {
			freeText += "/";
		}
		
		Matcher matcher = E_TICKET_FREE_TEXT_PATTERN.matcher(freeText);
		if(matcher.matches()) {
			String number = matcher.group(E_TICKET_FREE_TEXT_NUMBER_GROUP);
			String range = matcher.group(E_TICKET_FREE_TEXT_RANGE_GROUP);
			List<String> eTickets = getETicketNumbers(number, range);
			String date = matcher.group(E_TICKET_FREE_TEXT_DATE_GROUP);
			String passengerType = matcher.group(E_TICKET_FREE_TEXT_PASSENGER_TYPE_GROUP);
			String currency = matcher.group(E_TICKET_FREE_TEXT_CURRENCY_GROUP);
			String amount = matcher.group(E_TICKET_FREE_TEXT_AMOUNT_GROUP);
			String officeId = matcher.group(E_TICKET_FREE_TEXT_OFFICE_ID_GROUP);
			return new Object[]{eTickets, date, passengerType, currency, amount, officeId};
		}
		
		return null;
	}

	/**
	 * get list of e-ticket numbers
	 * format sample: 
	 * in: number:160-2367076693, range:-95
	 * out: 1602367076693, 1602367076694, 1602367076695
	 * 
	 * @param number
	 * @param range
	 * @return
	 */
	private static List<String> getETicketNumbers(String number, String rangeEnd) {
		List<String> eTickets = new ArrayList<>();
		String firstETicket = number.replace("-", "");
		
		if(!StringUtils.isEmpty(rangeEnd)) {
			rangeEnd = rangeEnd.replace("-", "");
		}
		
		//if rang is empty(which means there is only one e ticket), add it into list directly.
		if(StringUtils.isEmpty(rangeEnd)){
			eTickets.add(firstETicket);
		} 
		/** 
		 * 	if range is not empty, which means there are multiple e-tickets, parse and return all tickets. 
		 *  sample freeText: 160-5972462899-01, this sample contains three e-tickets:
		 *  1605972462899, 1605972462900 and 1605972462901
		 */
		else{
			int rangeLength = rangeEnd.length();
			long eticketPrefix = Long.parseLong(firstETicket.substring(0, firstETicket.length() - rangeLength));	// This is the prefix of eticket e.g. 12345678901XX-XX, then this is 12345678901

			// End of the range number
			rangeEnd = rangeEnd.replace("-", "");		// This is the end of the range e.g. XXXXXXXXXXX98-01, then this is 01
			long rangeEndInt = Long.parseLong(rangeEnd);
			
			// Beginning of the range number
			String rangeStart = firstETicket.substring(firstETicket.length() - rangeLength);	// This is the start of the range e.g. XXXXXXXXXXX98-01, then this is 98
			long rangeStartInt = Long.parseLong(rangeStart);
			
			// Beginning of the eticket
			long eticketStartInt = Long.parseLong(eticketPrefix + rangeStart);
			
			// Fix of OLSSMMB-14486 - support the case that rangeEnd is less than rangeStart e.g. XXXXXXXXXXX98-01
			// End of the eticket
			long eticketEndInt;
			if (rangeEndInt < rangeStartInt) {	
				eticketEndInt = Long.parseLong(("" + (eticketPrefix + 1)) + rangeEnd);
			} 
			// support the normal case that rangeStart is less than rangeEnd e.g. XXXXXXXXXXX98-99
			else {
				eticketEndInt = Long.parseLong(eticketPrefix + rangeEnd);
			}
			
			// Calculate Eticket
			while (eticketStartInt <= eticketEndInt) {
				String eTickerNum = String.format("%013d", eticketStartInt);	// Fix of OLSSMMB-13758 - E-ticket length must be 13. Fill in leading zero until 13 digits.
				eTickets.add(eTickerNum);
				eticketStartInt++;
			}
		}
		
		return eTickets;
	}
	/**
	 * 
	* @Description get e ticket number from eticket freetext
	* @param freeText
	* @return List<String>
	* @author haiwei.jia
	 */
	public static List<String> getETicketNumberFromEticketFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return new ArrayList<>();
		}
		
		// The REGEX of ET must be end with "/" to explicitly indicate the end
		if (!freeText.endsWith("/")) {
			freeText += "/";
		}
		
		Matcher matcher = E_TICKET_FREE_TEXT_PATTERN.matcher(freeText);
		List<String> eTickets = new ArrayList<>();
		
		//if no match, return empty list
		if (!matcher.matches()) {
			return new ArrayList<>();	
		} 
		//for the time being,one freetext can contain at most two e ticket
		else {
			String number = matcher.group(E_TICKET_FREE_TEXT_NUMBER_GROUP);
			String range = matcher.group(E_TICKET_FREE_TEXT_RANGE_GROUP);
			eTickets.addAll(getETicketNumbers(number, range));
		}
		
		return eTickets;
	}
	
	/**
	 * 
	* @Description get passenger type from eticket freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getPassengerTypeFromEticketFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = E_TICKET_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(E_TICKET_FREE_TEXT_PASSENGER_TYPE_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get contact name from emergency contact freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getContactNameFromEmrContactFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = EMR_CONTACT_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(EMR_CONTACT_FREE_TEXT_CONTACT_NAME_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get country code from emergency contact freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getCountryCodeFromEmrContactFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = EMR_CONTACT_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(EMR_CONTACT_FREE_TEXT_COUNTRY_CODE_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get phone number from emergency contact freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getPhoneNumberFromEmrContactFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = EMR_CONTACT_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(EMR_CONTACT_FREE_TEXT_PHONE_NUMBER_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get free text type from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getFreeTextTypeFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_TYPE_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get country from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getCountryFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_COUNTRY_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get street name from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getStreetNameFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_STREET_ADDRESS_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get city from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getCityFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_CITY_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get state code from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getStateCodeFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_STATE_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get zip code from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getZipCodeFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_ZIP_CODE_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get infant indicator from destination address freetext
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getInfIndicatorFromAddressDetailsFreeText(String freeText) {
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = ADDRESS_DETAILS_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";	
		} 
		else {
			return matcher.group(ADDRESS_DETAILS_FREE_TEXT_INF_INDICATOR_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get amount from baggage string, e.g. "10K"/"2P"
	* @param baggageStr
	* @return BigInteger
	* @author haiwei.jia
	 */
	public static BigInteger getAmountFromBaggageStr(String baggageStr){
		if (StringUtils.isEmpty(baggageStr)) {
			return null;
		}
		
		Matcher matcher = BAGGAGE_STRING_AMOUNT_PATTERN.matcher(baggageStr);
		
		//if no match, return 0
		if (!matcher.matches()) {
			return null;	
		} 
		else {
			return new BigInteger(matcher.group(BAGGAGE_STRING_AMOUNT_GROUP));
		}
	}
	
	/**
	 * 
	* @Description get unit from baggage string, e.g. "10K"/"2P"
	* @param baggageStr
	* @return BigInteger
	* @author haiwei.jia
	 */
	public static String getUnitFromBaggageStr(String baggageStr){
		if (StringUtils.isEmpty(baggageStr)) {
			return "";
		}
		
		Matcher matcher = BAGGAGE_STRING_AMOUNT_PATTERN.matcher(baggageStr);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";
		} 
		else {
			return matcher.group(BAGGAGE_STRING_UNIT_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get weight amount from purchased baggage free text
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getWeightAmountFromPurchasedBaggageFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = PURCHASED_BAGGAGE_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";
		} 
		else {
			return matcher.group(PURCHASED_BAGGAGE_FREE_WEIGHT_AMOUNT_GROUP);
		}
	}
	
	/**
	 * 
	* @Description get piece amount from purchased baggage free text
	* @param freeText
	* @return String
	* @author haiwei.jia
	 */
	public static String getPieceAmountFromPurchasedBaggageFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		
		Matcher matcher = PURCHASED_BAGGAGE_FREE_TEXT_PATTERN.matcher(freeText);
		
		//if no match, return ""
		if (!matcher.matches()) {
			return "";
		} 
		else {
			return matcher.group(PURCHASED_BAGGAGE_FREE_PIECE_AMOUNT_GROUP);
		}
	}
	
	/**
	 * check if the freeText is freeText with BCODE
	 * @param freeText
	 * @return
	 */
	public static boolean isBCODEFreetext(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return false;
		}	
		Matcher matcher = BCODE_FREE_TEXT_PATTERN.matcher(freeText);
		
		//return match result
		return matcher.matches();
	}
	
	
	/**
	 * Parse re-book NOTIFLY info from freeText
	 * @param freeText FORMAT:
	 * 	"NOTIFLY: CX5728 12MAY CX882 12MAY FLIGHT CANCEL PROTECT EMAIL SENT AT 20180508/0358/SCOTT.WH.LEE@GMAIL.COM/KUL-LAX/KA734 CX880/10MAR 11MAR/LAIHA LAM"
	 * @return String[], index 0:cancelledFlights[format "KA734 CX880"], 1:newBookFlights [format "KA734 CX880"]
	 */
	public static String[] getRebookNotiflyInfoFromFreeText(String freeText) {
		if(StringUtils.isEmpty(freeText)) {
			return null;
		}
		
		Matcher matcher = REBOOK_NOTIFLY_FREE_TEXT_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] rebookNotiflyInfo = new String[2];
		
		//cancelled segment information
		String cancelledFlightInfo = matcher.group(REBOOK_NOTIFLY_CANCELLED_FLGIHTS);
		rebookNotiflyInfo[0] = getRebookCancelledStNumbers(cancelledFlightInfo);
		
		//new segment information 
		String newBookFlightInfo = matcher.group(REBOOK_NOTIFLY_CANCELLED_NEW_BOOK_FLIGHTS);
		rebookNotiflyInfo[1] = getNewBookingStNumbers(newBookFlightInfo);

		return rebookNotiflyInfo;
	}
	
	/**
	 * Parse re-book NOTIFLY detail info from freeText
	 * @param freeText FORMAT:
	 * 	"NOTIFLY: CX5728 12MAY CX882 12MAY FLIGHT CANCEL PROTECT EMAIL SENT AT 20180508/0358/SCOTT.WH.LEE@GMAIL.COM/KUL-LAX/KA734 CX880/10MAR 11MAR/LAIHA LAM"
	 * @return String[], 
	 * 	index 0:cancelledFlights[format "KA734 CX880"], 
	 * 	index 1:cancelled flight dates[format "12MAY 12MAY"], 
	 * 	index 2:cancelled flight origin port[format "KUL"], 
	 *  index 3:cancelled flight destination port[format "LAX"], 
	 *  index 4:new book flights[format "KA734 CX880"],
	 *  index 5:new booking flight dates[format "12MAY 12MAY"]
	 */
	public static String[] getRebookNotiflyDetailInfoFromFreeText(String freeText) {
		if(StringUtils.isEmpty(freeText)) {
			return null;
		}
		
		Matcher matcher = REBOOK_NOTIFLY_FREE_TEXT_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] rebookNotiflyInfo = new String[6];
		
		String cancelledFlightInfo = matcher.group(REBOOK_NOTIFLY_CANCELLED_FLGIHTS);
		//cancelled segment company information
		rebookNotiflyInfo[0] = getRebookCancelledStNumbers(cancelledFlightInfo);
		//cancelled segment date information
		rebookNotiflyInfo[1] = getRebookCancelledStDates(cancelledFlightInfo);
		//cancelled segment origin
		rebookNotiflyInfo[2] = matcher.group(REBOOK_NOTIFLY_CANCELLED_FLGIHTS_ORIGIN);
		//cancelled segment destination
		rebookNotiflyInfo[3] = matcher.group(REBOOK_NOTIFLY_CANCELLED_FLGIHTS_DESTINATION);
		//new segment information 
		String newBookFlightInfo = matcher.group(REBOOK_NOTIFLY_CANCELLED_NEW_BOOK_FLIGHTS);
		rebookNotiflyInfo[4] = getNewBookingStNumbers(newBookFlightInfo);
		//new segment date information
		rebookNotiflyInfo[5] = matcher.group(REBOOK_NOTIFLY_CANCELLED_NEW_BOOK_DATES);
		return rebookNotiflyInfo;
	}

	/**
	 * get new booking segment numbers(company+flightNumber)
	 * @param newBookFlightIn fo format:"CX5728 CX882KA216".
	 * @return String format:"CX5728 CX882 KA216"
	 */
	private static String getNewBookingStNumbers(String newBookFlightInfo) {
		if (StringUtils.isEmpty(newBookFlightInfo)) {
			return null;
		}
		
		Matcher matcher = NEW_BOOKING_FLIGHTS_PATTERN.matcher(newBookFlightInfo);
		StringBuilder sb = new StringBuilder();
		while(matcher.find()) {
			sb.append(" ");
			sb.append(matcher.group(0));
		}
		return sb.toString().trim();
	}
	
	/**
	 * get cancelled segment numbers(company+flightNumber)
	 * @param cancelledFlightInfo, format:"CX5728 12MAY CX88212MAY".
	 * @return String, format:"CX5728 CX882".
	 */
	private static String getRebookCancelledStNumbers(String cancelledFlightInfo) {
		if (StringUtils.isEmpty(cancelledFlightInfo)) {
			return null;
		}
		
		Matcher matcher = CANCELLED_FLIGHTS_PATTERN.matcher(cancelledFlightInfo);
		StringBuilder sb = new StringBuilder();
		while(matcher.find()) {
			sb.append(" ");
			sb.append(matcher.group(REBOOKING_NOTIFLY_FLIGHTS_FLIGHT_CODE));
		}
		return sb.toString().trim();
	}
	
	/**
	 * get cancelled segment dates
	 * @param cancelledFlightInfo, format:"CX5728 12MAY CX882 12MAY".
	 * @return String, format:"12MAY 12MAY".
	 */
	private static String getRebookCancelledStDates(String cancelledFlightInfo) {
		if (StringUtils.isEmpty(cancelledFlightInfo)) {
			return null;
		}
		
		Matcher matcher = CANCELLED_FLIGHTS_PATTERN.matcher(cancelledFlightInfo);
		StringBuilder sb = new StringBuilder();
		while(matcher.find()) {
			sb.append(" ");
			sb.append(matcher.group(REBOOKING_NOTIFLY_FLIGHTS_FLIGHT_TIME));
		}
		return sb.toString().trim();
	}
	
	/**
	 * Parse re-book Accept Protection info from freeText
	 * @param freeText, format:
	 * 	"PROTECTION: PASSENGER CHAN/JOHN ACCEPT CHANGE 25 JUL 2018/04:07 FOR CX450 CX455/10 AUG 11 AUG VIA MMB"
	 * @return String[], index 0:acceptFamilyName, 1:acceptGivenName, 2:acceptFlights(CX450 CX455)
	 */
	public static String[] getRebookAcceptInfoFromFreeText(String freeText) {
		if(StringUtils.isEmpty(freeText)) {
			return null;
		}
		
		Matcher matcher = REBOOK_ACCEPT_FREE_TEXT_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] rebookProtectionInfo = new String[3];
		// accept family name
		rebookProtectionInfo[0] = matcher.group(REBOOK_ACCEPT_FAMILY_NAME);;
		// accept given name 
		rebookProtectionInfo[1] = matcher.group(REBOOK_ACCEPT_GIVEN_NAME);
		// accept flights
		rebookProtectionInfo[2] = matcher.group(REBOOK_ACCEPT_FLIGHTS);

		return rebookProtectionInfo;
	}
	
	/**
	 * 
	* @Description check if is XLMR MCO by freeText
	* @param freeText
	* @return String
	* @author jiajian.guo
	 */
	public static boolean isXLMRMCO(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return false;
		}
		
		Matcher matcher = SK_XLMR_MCO_FREE_TEXT_PATTERN.matcher(freeText);
		return matcher.matches();
	}

	public static String getDwCodeFromFreeText(String freeText){
		if (StringUtils.isEmpty(freeText)) {
			return "";
		}
		Matcher matcher = Pattern.compile(DW_FREE_TEXT_REGEX).matcher(freeText);
        //if no match, return ""
		if (!matcher.find()) {
			return "";
		}
		else {
			return matcher.group(DW_FREE_TEXT_DWCODE_GROUP);
		}
	}
	
	public static String getUMNRAge(String freeText) {
		String age = null;
		if (StringUtils.isNotEmpty(freeText)) {
			Pattern pattern = Pattern.compile(SSR_UMNR_FREETEXT_AGE);
			Matcher matcher = pattern.matcher(freeText);
			if (matcher.find()){
				age = matcher.group(1);
			}
		}
		return age;
	}
	
	public static String parsePortCodeFromOSIUMNRFreeText(String freeText) {
		String portCode = null;
		if (StringUtils.isNotEmpty(freeText)) {
			Pattern pattern = Pattern.compile(OSI_UMNR_FREETEXT_REGEX);
			Matcher matcher = pattern.matcher(freeText);
			if (matcher.find()){
				portCode = matcher.group(OSI_UMNR_FREETEXT_PORT_CODE);
			}
		}
		return portCode;
	}
	
	public static String getNameFromOSIFreeText(String freeText) {
		String name = null;
		if (StringUtils.isNotEmpty(freeText)) {
			Pattern pattern = Pattern.compile(OSI_UMNR_FREETEXT_REGEX);
			Matcher matcher = pattern.matcher(freeText);
			if (matcher.find()){
				name = matcher.group(OSI_UMNR_FREETEXT_NAME);
			}
		}
		return name;
	}
	
	public static String getPhoneNumberFromOSIFreeText(String freeText) {
		String portCode = null;
		if (StringUtils.isNotEmpty(freeText)) {
			Pattern pattern = Pattern.compile(OSI_UMNR_FREETEXT_REGEX);
			Matcher matcher = pattern.matcher(freeText);
			if (matcher.find()){
				portCode = matcher.group(OSI_UMNR_FREETEXT_PHONE_NUMBER);
			}
		}
		return portCode;
	}
	
	/**
	 * split freeText cause the max length is 105 and use 5 as buffer 
	 * @param freeText
	 * @param length
	 * @return
	 */
	public static String[] splitFreeTextBySize(String freeText, int length) {
        char[] charString = freeText.toCharArray();
        StringBuilder result = new StringBuilder();
        List<String> resultList = new ArrayList<>();
        int index = 0;
        for (char c: charString){
            index += String.valueOf(c).getBytes().length;
            if (index > length){
                resultList.add(result.toString());
                result.delete(0,index-1);
                index = 1;
                result.append(c);
            }else {
                result.append(c);
            }
        }
        resultList.add(result.toString());
        return resultList.toArray(new String[resultList.size()]);
    }
		
	/**
	 * get FamilyName From Ssr Infant FreeText
	 * @param freeText
	 * @return
	 */
	public static String getFamilyNameFromSsrInfantFreeText(String freeText){
		String familyName = null;
		if(StringUtils.isNotEmpty(freeText)){
			Matcher matcher = SSR_INFT_FREETEXT_PATTERN.matcher(freeText);
			if (matcher.find()){
				familyName = matcher.group(SSR_INFT_FAMILY_NAME);
			}
		}
		return familyName;
	}
	
	/**
	 * get GivenName From Ssr Infant FreeText
	 * @param freeText
	 * @return
	 */
	public static String getGivenNameFromSsrInfantFreeText(String freeText){
		String givenName = null;
		if(StringUtils.isNotEmpty(freeText)){
			Matcher matcher = SSR_INFT_FREETEXT_PATTERN.matcher(freeText);
			if(matcher.find()){
				givenName = matcher.group(SSR_INFT_GIVEN_NAME).trim();
			}
		}
		return givenName;
	}
	
	/**
	 * get BirthDate From Ssr Infant FreeText
	 * @param freeText
	 * @return
	 */
	public static String getBirthDateFromSsrInfantFreeText(String freeText){
		String birthDate = null;
		if(StringUtils.isNotEmpty(freeText)){
			Matcher matcher = SSR_INFT_FREETEXT_PATTERN.matcher(freeText);
			if(matcher.find()){
				birthDate = matcher.group(SSR_INFT_BIRTH_DATE);
			}
		}
		return birthDate;
	}
	/**
	 * 
	 * @param freeText
	 * @return
	 */
	public static boolean isOccupyInfant(String freeText) {
		return StringUtils.containsIgnoreCase(freeText, SSR_INFT_OCCUPY_KEYWORD);
	}
    
	/**
	 * Get FP (Credit card/debit card/APM/Cash) information
	 * Sample: CCVI/4444333322221111/1020/HKD8977/N417
	 * String[] index:
	 * 0 - credit card indicator
	 * 1 - card type
	 * 2 - card number
	 * 3 - expiry date(mmyy)
	 * 4 - currency
	 * 5 - amount
	 * 6 - approval code or transaction no
	 * 
	 * @param freeText
	 * @return
	 */
	public static String[] getFpNoStoredCardInfo(String freeText) {
		if(StringUtils.isEmpty(freeText)){
			return null;
		}
		
		Matcher matcher = FP_FREETEXT_NO_STORED_CARD_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] fpNoStoredCardInfo = new String[7];
		fpNoStoredCardInfo[0] = matcher.group(FP_FREETEXT_NO_STORED_CARD_INDICATOR);
		fpNoStoredCardInfo[1] = matcher.group(FP_FREETEXT_NO_STORED_CARD_TYPE);
		fpNoStoredCardInfo[2] = matcher.group(FP_FREETEXT_NO_STORED_CARD_NUMBER);
		fpNoStoredCardInfo[3] = matcher.group(FP_FREETEXT_NO_STORED_CARD_EXPIRY_DATE);
		fpNoStoredCardInfo[4] = matcher.group(FP_FREETEXT_NO_STORED_CARD_CURRENCY);
		fpNoStoredCardInfo[5] = matcher.group(FP_FREETEXT_NO_STORED_CARD_AMOUNT);
		fpNoStoredCardInfo[6] = matcher.group(FP_FREETEXT_NO_STORED_CARD_APPROVAL_CODE);
		
		return fpNoStoredCardInfo;
	}
	
	/**
	 * Get FP Stored card information
	 * Sample: IBE*VI446656*427113
	 * String[] index:
	 * 0 - credit card indicator
	 * 1 - card type
	 * 2 - approval code (alpha numeric)
	 * 3 - credit card 1st 6 digit
	 * 
	 * @param freeText
	 * @return
	 */
	public static String[] getFpStoredCardInfo(String freeText) {
		if(StringUtils.isEmpty(freeText)){
			return null;
		}
		
		Matcher matcher = FP_FREETEXT_STORED_CARD_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] fpNoStoredCardInfo = new String[4];
		fpNoStoredCardInfo[0] = matcher.group(FP_FREETEXT_STORED_CARD_INDICATOR);
		fpNoStoredCardInfo[1] = matcher.group(FP_FREETEXT_STORED_CARD_TYPE);
		fpNoStoredCardInfo[2] = matcher.group(FP_FREETEXT_STORED_CARD_APPROVAL_CODE);
		fpNoStoredCardInfo[3] = matcher.group(FP_FREETEXT_STORED_CARD_NUMBER_FIREST_6_DIGIT);
		
		return fpNoStoredCardInfo;
	}
	
	/**
	 * Get spnr informations from freeText
	 * Sample: 5UL6YEQ/FO/FLT/MOB
	 * String[] index:
	 * 0 - spnr
	 * 1 - FO
	 * 2 - FLT
	 * 3 - MOB
	 * 
	 * @param freeText
	 * @return
	 */
	public static String[] getSpnrInfo(String freeText) {
		if(StringUtils.isEmpty(freeText)){
			return null;
		}
		
		Matcher matcher = SK_SPNR_FREETEXT_PATTERN.matcher(freeText);
		if(!matcher.matches()) {
			return null;
		}
		
		String[] spnrInfo = new String[4];
		spnrInfo[0] = matcher.group(1);
		spnrInfo[1] = matcher.group(2);
		spnrInfo[2] = matcher.group(3);
		spnrInfo[3] = matcher.group(4);
		
		return spnrInfo;
	}
	
	 /**
	  * parse staff id from free texts
	  *  <longFreetext>ERN 111111H</longFreetext>
	  * @param freeTexts
	  * @return 111111H
	  */
    public static String getStaffIdFromFreeText(List<String> freeTexts) {
        if(CollectionUtils.isEmpty(freeTexts)) {
            return StringUtils.EMPTY;
        }
        for(String freeText : freeTexts) {
            if(StringUtils.isNotEmpty(freeText) && freeText.startsWith(STAFF_OS_ERN)) {
                String[] staffIds = freeText.split(STAFF_OS_ERN);
                if(null != staffIds && StringUtils.isNotEmpty(staffIds[1])) {
                    return StringUtils.trim(staffIds[1]);
                }
            }
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * parse preselected meal code
     *  <longFreetext>PJ2E01D02A03 CX888 HKGYVR</longFreetext>
     * @param freeText
     * @return PJ2E01
     */
    public static String getPreSelectedMealFromFreeText(String freeText) {
        if (!StringUtils.isEmpty(freeText)) {
            Matcher matcher = PRESELCTED_MEAL_FREETEXT_PATTERN.matcher(freeText);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return StringUtils.EMPTY;
    }
    
}
