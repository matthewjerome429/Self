package com.cathaypacific.mmbbizrule.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.constant.FlightStatusEnum;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.AddressDetails;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.CheckInChannel;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Contact;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.EmergencyContact;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FqtvInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FrequentFlyerInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Journey;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Passenger;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.TravelDocument;




public class JourneyUtil {
	
	public static final String CARRIER_CX = "CX";

	public static final String CARRIER_KA = "KA";
	
	public static final String FQTV_USAGE = "FQTV";
	
	public static final String ASIA_MILES_TIER = "AM";
	
	public static final String SSR_SK_INHIBIT = "I";

	public static final String CHECKIN_ACCEPTED_STATUS = "CAC";
	
	public static final String CHECKIN_STANDBY_STATUS = "CST";
	
	public static final String FLIGHT_CANCELLED_STATUS = "DFX";
	
	public static final String EXIT_SEAT_SUITABLE_INDICATOR = "EXS";

	public static final String PRIMARY_TRAVEL_DOC_GROUP_CODE = "PT";

	public static final String SECONDARY_TRAVEL_DOC_GROUP_CODE = "ST";
	
	public static final String COUNTRY_CODE_US = "US";
	/** High priority comment status code **/
	public static final String COMMENT_STATUS_HIGH_PRIORITY = "H";
	/** Comment free text - DCHK **/
	public static final String COMMENT_FREE_TEXT_DCHK = "DCHK";
	/** Regulatory Status - Allow **/
	public static final String REGULATORY_STATUS_ALLOW = "Y";
	/** Regulatory Status - Not Allow **/
	public static final String REGULATORY_STATUS_NOT_ALLOW = "N";
	/** SSR SK rule BP inhibited **/
	public static final String BP_INHIBIT = "I";
	/** SSR SK rule BP allowed **/
	public static final String BP_ALLOW = "A";
	
	/**  Claimed lounge FLAC **/
	public static final String LOUNGE_FLAC = "FLAC";
	/** Claimed lounge BLAC **/
	public static final String LOUNGE_BLAC = "BLAC";
	
	public static final String REGULATORY_STATUS_ADC = "ADC";
	
	
	private JourneyUtil() {
		
	}
	/**
	 * Get SK lounge entitlement(claimed lounge pass)
	 * FLAC>BLAC
	 * @param skList
	 * @return
	 */
	public static String getSKLounge(List<String> skList){
		String result = null;
		if(!CollectionUtils.isEmpty(skList)){
			for (String sk:skList) {
				if(LOUNGE_FLAC.equals(sk)){
					result = sk;
					break;
				}
				if(LOUNGE_BLAC.equals(sk)){
					result = sk;
				}
			}
		}
		return result;
	}
	/**
	 * Get display primary travel document for flight
	 * 
	 * @param flightDID
	 * @return
	 */
	public static TravelDocument getDisplayPrimaryTravelDocument(@NotNull Passenger passenger, String flightDID) {
		return getDisplayTravelDocument(passenger, flightDID, PRIMARY_TRAVEL_DOC_GROUP_CODE,
				Flight::getPrimaryTravelDocuments, Passenger::getPrimaryTravelDocuments);
	}

	/**
	 * Get display secondary travel document for flight
	 * 
	 * @param flightDID
	 * @return
	 */
	public static TravelDocument getDisplaySecondaryTravelDocument(@NotNull Passenger passenger, String flightDID) {
		return getDisplayTravelDocument(passenger, flightDID, SECONDARY_TRAVEL_DOC_GROUP_CODE,
				Flight::getSecondaryTravelDocuments, Passenger::getSecondaryTravelDocuments);
	}
	
	/**
	 * get display ktn travel document for flight
	 * @param passenger
	 * @param flightDID
	 * @return
	 */
	public static TravelDocument getDisplayKtnTravelDocument(@NotNull Passenger passenger, String flightDID) {
		TravelDocument result = null;
		List<Flight> flights = getAvailableFlights(passenger);
		if (flights.isEmpty() || flightDID == null) {
			return result;
		}
		Flight flight =
				flights.stream().filter(f -> flightDID.equals(f.getProductIdentifierDID())).findFirst().orElse(null);
		if (flight == null) {
			return result;
		}
		
		if(null != flight.getKtnTravelDocument() && !StringUtils.isEmpty(flight.getKtnTravelDocument().getNumber())){
			result = flight.getKtnTravelDocument();
		} else if(null != passenger && null != passenger.getKtnTravelDocument() && !StringUtils.isEmpty(passenger.getKtnTravelDocument().getNumber())){
			result = passenger.getKtnTravelDocument();
		}
		
		return result;
	}

	/**
	 * Judge flight is cancelled.
	 * @param flight
	 * @return
	 */
   public static boolean isCancelledForFlight(Flight flight){
	   
	   if(FlightStatusEnum.CANCELLED.equals(FlightStatusEnum.valueIn(flight.getRtfsStatus()))){
		   return true;
	   }
	   return false;
   }
   
   
	/**
	 * Judge flight is Flown.
	 * @param flight
	 * @return
	 */
  public static boolean isFlownForFlight(Flight flight){
	   if(FlightStatusEnum.FLOWN.equals(FlightStatusEnum.valueIn(flight.getRtfsStatus()))){
		   return true;
	   }
	   return false;
  }
   
	/**
	 * Indicate whether it is MICE booking without seat
	 * 
	 * @param journey
	 * @return
	 */
	public static boolean isMiceNoSeatAssign(Passenger passenger) {
		if(OneAConstants.CUSTOMER_LEVEL_SERVICE_SSR_CODE_GRMA.equals(passenger.getMiceDetails().getSsrCode())){
			return false;
		}else if(OneAConstants.PAX_TYPE_INFANT.equals(passenger.getType())){
			return false;
		}else if(passenger.getFlights().stream().anyMatch(f -> (!f.isFerryFlight() && f.getSeat()== null) || (!f.isFerryFlight() && f.getSeat().getSeatNum() == null))){
			return true;
		}
		return false;
	}
  
   
	/**
	 * Get display travel document. <br>
	 * It's common logic for both primary and secondary.
	 * 
	 * @return
	 */
	private static TravelDocument getDisplayTravelDocument(@NotNull Passenger passenger,
			@NotNull String flightDID, @NotNull String travelDocGroupCode,
			@NotNull Function<Flight, List<TravelDocument>> flightTravelDocumentsGetter,
			@NotNull Function<Passenger, List<TravelDocument>> passengerTravelDocumentsGetter) {
		TravelDocument result = null;
		List<Flight> flights = getAvailableFlights(passenger);
		if (flights.isEmpty() || flightDID == null) {
			return result;
		}
		Flight flight =
				flights.stream().filter(f -> flightDID.equals(f.getProductIdentifierDID())).findFirst().orElse(null);
		if (flight == null) {
			return result;
		}
		Map<String, List<String>> travelDocTypeMap = flight.getTravelDocTypeMap();
		List<String> travelDocTypeList = null;
		if (travelDocTypeMap != null && travelDocTypeMap.containsKey(travelDocGroupCode)) {
			travelDocTypeList = travelDocTypeMap.get(travelDocGroupCode);
		}
		
		List<TravelDocument> productTravelDocuments = flightTravelDocumentsGetter.apply(flight);
		result = getFirstAvailableTravelDocument(productTravelDocuments, travelDocTypeList);
		if (result != null) {
			return result;
		}

		String flightCompany = getCompanyPrimeCXKA(flight);
		List<TravelDocument> customerTravelDocuments = passengerTravelDocumentsGetter.apply(passenger).stream().filter(
				td -> flightCompany.equals(td.getMarketingCompany())).collect(Collectors.toList());
		result = getFirstAvailableTravelDocument(customerTravelDocuments, travelDocTypeList);
		return result;
	}

	/**
	 * Get first available travel document from list. <br>
	 * 
	 * @return
	 */
	public static TravelDocument getFirstAvailableTravelDocument(@NotNull List<TravelDocument> travelDocuments,
			List<String> travelDocTypeList) {
		TravelDocument result = null;
		if (!CollectionUtils.isEmpty(travelDocuments)) {
			// If available travel document type list exists, filter out
			// unavailable type.
			if (travelDocTypeList != null) {
				result = travelDocuments.stream().filter(
						td -> travelDocTypeList.contains(td.getType())).findFirst().orElse(null);
			} else {
				result = travelDocuments.stream().findFirst().orElse(null);
			}
		}
		return result;
	}
	
	/**
	 * Get display emergency contact
	 * 
	 * @return
	 */
	public static EmergencyContact getDisplayEmergencyContact(@NotNull Passenger passenger) {
		Set<String> flightMarkingCompanySet = new HashSet<>();
		Set<String> flightOperatingCompanySet = new HashSet<>();
		EmergencyContact firstAvailableEmergencyContact = null;
		List<Flight> flights = getAvailableFlights(passenger);
		for (Flight flight : flights) {
			if (!StringUtils.isEmpty(flight.getMarketingCompany())) {
				flightMarkingCompanySet.add(flight.getMarketingCompany());
			}
			if (!StringUtils.isEmpty(flight.getOperateCompany())) {
				flightOperatingCompanySet.add(flight.getOperateCompany());
			}
			
			if (flight.getEmergencyContact() != null) {
				firstAvailableEmergencyContact = flight.getEmergencyContact();
				break;
			}
		}
		
		if (firstAvailableEmergencyContact == null && passenger.getEmergencyContacts() != null) {
			firstAvailableEmergencyContact = passenger.getEmergencyContacts().stream().filter(
					tempEC -> flightMarkingCompanySet.contains(tempEC.getMarketingCompany())
			).findFirst().orElseGet(() -> passenger.getEmergencyContacts().stream().filter(
					tempEC -> flightOperatingCompanySet.contains(tempEC.getMarketingCompany())
			).findFirst().orElse(null));
		}

		return firstAvailableEmergencyContact;
	}

	/**
	 * get dispaly DestinationAddresses
	 * 
	 * @return
	 */
	public static AddressDetails getDisplayDestinationAddress(@NotNull Passenger passenger) {
		Set<String> flightMarkingCompanySet = new HashSet<>();
		Set<String> flightOperatingCompanySet = new HashSet<>();
		AddressDetails firstAvailableDestinationAddress = null;
		List<Flight> flights = getAvailableFlights(passenger);
		for (Flight flight : flights) {
			if (!StringUtils.isEmpty(flight.getMarketingCompany())) {
				flightMarkingCompanySet.add(flight.getMarketingCompany());
			}
			if (!StringUtils.isEmpty(flight.getOperateCompany())) {
				flightOperatingCompanySet.add(flight.getOperateCompany());
			}
			
			if (flight.getDestinationAddress() != null) {
				firstAvailableDestinationAddress = flight.getDestinationAddress();
				break;
			}
		}

		if (firstAvailableDestinationAddress == null && passenger.getDestinationAddresses() != null) {
			firstAvailableDestinationAddress = passenger.getDestinationAddresses().stream().filter(
					tempEC -> flightMarkingCompanySet.contains(tempEC.getMarketingCompany())
			).findFirst().orElseGet(() -> passenger.getDestinationAddresses().stream().filter(
					tempEC -> flightOperatingCompanySet.contains(tempEC.getMarketingCompany())
			).findFirst().orElse(null));
		}

		return firstAvailableDestinationAddress;
	}
	
	/**
	 * If ITI indicator exists in any flight, passenger's transit is true.
	 * 
	 */
	public static boolean getDisplayTransit(@NotNull Passenger passenger) {
		List<Flight> flights = getAvailableFlights(passenger);
		return flights.stream().anyMatch(f -> BooleanUtils.isTrue(f.getTransit()));
	}

	/**
	 * Collect flight carriers of this passenger.<br>
	 * Only CX, KA will be collected.
	 * 
	 * @param flights
	 * @return carriers of contact to be updated.
	 */
	public static List<String> collectDistinctCarriers(@NotNull Passenger passenger) {
		List<Flight> flights = getAvailableFlights(passenger);
		List<String> carrierList = flights.stream().map(JourneyUtil::getCompanyPrimeCXKA).filter(
				carrier -> CARRIER_CX.equals(carrier) || CARRIER_KA.equals(carrier)
		).distinct().collect(Collectors.toList());

		return Collections.unmodifiableList(carrierList);
	}

	/**
	 * Get appeared carriers from flights. And flight DID belong to every
	 * carrier.
	 * 
	 * @param flights
	 * @return carriers of contact to be updated.
	 */
	public static Map<String, List<String>> mapCarrierFlight(@NotNull Passenger passenger) {
		Map<String, List<String>> carrierFlightMap = new HashMap<>();
		List<Flight> flights = getAvailableFlights(passenger);
		for (Flight flight : flights) {
			String carrier = getCompanyPrimeCXKA(flight);
			if (StringUtils.isEmpty(carrier) || StringUtils.isEmpty(flight.getProductIdentifierDID())) {
				continue;
			}

			List<String> flightIdList = carrierFlightMap.get(carrier);
			if (flightIdList == null) {
				flightIdList = new ArrayList<>();
				carrierFlightMap.put(carrier, flightIdList);
			}
			flightIdList.add(flight.getProductIdentifierDID());
		}

		return Collections.unmodifiableMap(carrierFlightMap);
	}

	/**
	 * Retrieve carrier of contact from passenger to display.
	 * 
	 * @param passenger
	 * @return
	 */
	public static String retrieveCarrierForDisplay(@NotNull Passenger passenger) {
		List<Flight> flights = getAvailableFlights(passenger);

		String carrier = null;
		
		Set<String> carrierSet = flights.stream().map(JourneyUtil::getCompanyPrimeCXKA).collect(Collectors.toSet());
		if (carrierSet.stream().anyMatch(CARRIER_CX::equals)) {
			carrier = CARRIER_CX;
		} else if (carrierSet.stream().anyMatch(CARRIER_KA::equals)) {
			carrier = CARRIER_KA;
		}

		return carrier;
	} 
	
	/**
	 * Retrieve contact from passenger to display.
	 * 
	 * @param passenger
	 * @return retrieved contact.
	 */
	public static <C extends Contact> C retrieveContactForDisplay(@NotNull Passenger passenger, List<C> contacts) {
		if (contacts == null) {
			return null;
		}
		
		String preferredCarrier = retrieveCarrierForDisplay(passenger);
		if (preferredCarrier == null) {
			return null;
		}
		String alternativeCarrier;
		if (CARRIER_CX.equals(preferredCarrier)) {
			alternativeCarrier = CARRIER_KA;
		} else {
			alternativeCarrier = CARRIER_CX;
		}
		
		C contact;
		/*
		 *  Find OLCI version contact at first. 
		 *  If no OLCI version contact exists, use normal contact.
		 */
		List<C> olciVerContacts = contacts.stream().filter(C::isOlciVersion).collect(Collectors.toList());
		if (!olciVerContacts.isEmpty()) {			
			contact = filterContactByCarrier(olciVerContacts, preferredCarrier, alternativeCarrier);
		} else {
			contact = filterContactByCarrier(contacts, preferredCarrier, alternativeCarrier);
		}
		
		return contact;
	}

	/**
	 * Filter contact by specified carrier.
	 * 
	 * @param contactStream
	 * @param preferredCarrier
	 * @param alternativeCarrier
	 * @return
	 */
	private static <C extends Contact> C filterContactByCarrier(@NotNull List<C> contacts, String preferredCarrier,
			String alternativeCarrier) {
		C contact;

		/* Find preferred carrier. */
		contact = contacts.stream().filter(

				c -> preferredCarrier.equals(c.getMarketingCompany())

		).findFirst().orElse(null);

		/*
		 * If preferred carrier is not found, Find alternative carrier instead.
		 */
		if (contact == null) {
			contact = contacts.stream().filter(

					c -> alternativeCarrier.equals(c.getMarketingCompany())

			).findFirst().orElse(null);
		}

		return contact;
	}
	
	/**
	 * Retrieve all OLCI version contacts.
	 * 
	 * @param contacts
	 * @return
	 */
	@NotNull
	public static <C extends Contact> List<C> retrieveAllOlciVersionContact(List<C> contacts) {
		if (contacts == null) {
			return Collections.emptyList();
		}

		return contacts.stream().filter(C::isOlciVersion).collect(Collectors.toList());
	}
    
	/**
	 * Get company of flight.<br>
	 * If marketing company is cx, return marketing company. If not
	 * If operate company is cx, return operate company. If not
	 * If marketing company is ka, return marketing company. If not
	 * If operate company is ka, return operate company. If not
	 * If marketing company exists, return marketing company. If not, return
	 * operate company.
	 * 
	 * @param flight
	 * @return
	 * @exception IllegalArgumentException
	 *                when neither marketing nor operate company can be found.
	 */
	public static String getCompanyPrimeCXKA(@NotNull Flight flight) {
		if (CARRIER_CX.equals(flight.getMarketingCompany())) {
			return flight.getMarketingCompany();
		} else if (CARRIER_CX.equals(flight.getOperateCompany())) {
			return flight.getOperateCompany();
		} else if (CARRIER_KA.equals(flight.getMarketingCompany())) {
			return flight.getMarketingCompany();
		} else if (CARRIER_KA.equals(flight.getOperateCompany())) {
			return flight.getOperateCompany();
		} else if (!StringUtils.isEmpty(flight.getMarketingCompany())) {
			return flight.getMarketingCompany();
		} else if (!StringUtils.isEmpty(flight.getOperateCompany())) {
			return flight.getOperateCompany();
		} else {
			throw new IllegalArgumentException("Flight's company is not found.");
		}
	}

	/**
	 * Get flight number of flight.<br>
	 * If flight marketing company is cx, return marketing flight number. If not
	 * If flight operate company is cx, return operate flight number. If not
	 * If flight marketing company is ka, return marketing flight number. If not
	 * If flight operate company is ka, return operate flight number. If not
	 * If marketing flight number exists, return marketing flight number. If
	 * not, return operate flight number.
	 * 
	 * @param flight
	 * @return
	 * @exception IllegalArgumentException
	 *                when neither marketing nor operate flight number can be
	 *                found.
	 */
	public static String getFlightNumberPrimeCXKA(@NotNull Flight flight) {
		if (CARRIER_CX.equals(flight.getMarketingCompany())) {
			return flight.getMarketFlightNumber();
		} else if (CARRIER_CX.equals(flight.getOperateCompany())) {
			return flight.getOperateFlightNumber();
		} else if (CARRIER_KA.equals(flight.getMarketingCompany())) {
			return flight.getMarketFlightNumber();
		} else if (CARRIER_KA.equals(flight.getOperateCompany())) {
			return flight.getOperateFlightNumber();
		} else if (!StringUtils.isEmpty(flight.getMarketFlightNumber())) {
			return flight.getMarketFlightNumber();
		} else if (!StringUtils.isEmpty(flight.getOperateFlightNumber())) {
			return flight.getOperateFlightNumber();
		} else {
			throw new IllegalArgumentException("Flight's flight number is not found.");
		}
	}

	/**
	 * Get Passenger's member tier. Product-level FQTV info is used if it
	 * exists, otherwise customer-level FQTV info is used.</br>
	 * will return TierLevel if carrier is cx, else return priorityCode.
	 * @param passenger
	 * @param flight
	 * @return
	 */
	public static String getMemberPriorityTier(Passenger passenger, Flight flight) {
		FqtvInfo targetFqtvInfo = null;

		if (flight != null) {
			if (flight.getProductLevelFqtvInfo() != null) {
				targetFqtvInfo = getFqtvInfo(flight.getProductLevelFqtvInfo(), null);
			}
			if (null == targetFqtvInfo && passenger.getCustomerLevelFqtvInfo() != null) {
				targetFqtvInfo = getFqtvInfo(passenger.getCustomerLevelFqtvInfo(), flight.getMarketingCompany());
			}
			if (null == targetFqtvInfo && passenger.getCustomerLevelFqtvInfo() != null) {
				targetFqtvInfo = getFqtvInfo(passenger.getCustomerLevelFqtvInfo(), flight.getOperateCompany());
			}
		}
		if (targetFqtvInfo != null && targetFqtvInfo.getFrequentFlyerInfo() != null) {
			return getMemberPriorityTierByFqtvInfo(targetFqtvInfo);
		}
		return null;
	}
	
	/**
	 * @param targetFqtvInfo
	 * @return
	 */
	public static String getMemberPriorityTierByFqtvInfo(FqtvInfo targetFqtvInfo) {
		String result;
		if (CARRIER_CX.equals(targetFqtvInfo.getFrequentFlyerInfo().getCarrier())) {
			result = targetFqtvInfo.getFrequentFlyerInfo().getTierLevel();
		} else {
			result =  targetFqtvInfo.getFrequentFlyerInfo().getPriorityCode();
		}
		return result == null ? "" : result;
	}
	
	/**
	 * Get the FQTV info.
	 * 
	 * @param fqtvInfos
	 * @param flightOptCompany - null if no filtering is needed
	 * @return
	 */
	public static FqtvInfo getFqtvInfo(List<FqtvInfo> fqtvInfos, String flightOptCompany){
		FqtvInfo nonCXFqtvInfo = null;
		for (FqtvInfo fqtvInfo : fqtvInfos) {
			if (null != fqtvInfo && FQTV_USAGE.equals(fqtvInfo.getFqtvUsage())
					&& (StringUtils.isEmpty(flightOptCompany) || flightOptCompany.equals(fqtvInfo.getFqtvCarrier()))) {
				
				FrequentFlyerInfo frequentFlyerInfo = fqtvInfo.getFrequentFlyerInfo();
				if (null != frequentFlyerInfo && null != frequentFlyerInfo.getCarrier()
						&& null != frequentFlyerInfo.getNumber()) {

					if (CARRIER_CX.equals(frequentFlyerInfo.getCarrier())) {
						return fqtvInfo;
					} else {
						nonCXFqtvInfo = fqtvInfo;
					}
				}
			}
		}
		return nonCXFqtvInfo;
	}
	
	/**
	 * Judge the passenger whether can do check in
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean isDisplayOnly(Passenger passenger) {
		boolean passengerInhibitionFlag = false;
		if (passenger == null) {
			return passengerInhibitionFlag;
		}
		if (passenger.getSsrSkMappingRule() != null
				&& SSR_SK_INHIBIT.equals(passenger.getSsrSkMappingRule().getCheckInValue())) {
			passengerInhibitionFlag = true;
		}
		boolean flightInhibitionFlag = false;
		List<Flight> flights = getAvailableFlights(passenger);
		if (CollectionUtils.isEmpty(flights) || hasAnyInvalidEticketForCXKAFlight(flights) || hasAnyNeedPaymentSeat(passenger)) {
			flightInhibitionFlag = true;
		}
		return passengerInhibitionFlag || flightInhibitionFlag;
	}
	/**
	 * check the check in rule of SSR SK
	 * @param passengers
	 * @return
	 */
	public static boolean inhibitCheckinBySsrSkRule(Passenger passengers){
		boolean result = false;
		if(passengers == null || passengers.getSsrSkMappingRule() == null){
			return result;
		}
		return SSR_SK_INHIBIT.equals(passengers.getSsrSkMappingRule().getCheckInValue());
	}
	/**
	 * Judge the flight whether can do check in
	 * @param passenger
	 * @return
	 */
	public static boolean isDisplayOnly(Flight flight){
		boolean result= false;
		if(flight == null){
			return result;
		}
		return BooleanUtils.isTrue(flight.isPortDisplayOnly());
	}
	
	/**
	 * check if the flight need ri check
	 * @param flight
	 * @return
	 */
	public static boolean isNeedRevenueIntegrityCheck(Flight flight){
		
		return null != flight.getRevenueIntegrityStatus() && (OneAConstants.STATUS_INFORMATION_F.equals(flight.getRevenueIntegrityStatus().getStopoverStatus())
				|| OneAConstants.STATUS_INFORMATION_R.equals(flight.getRevenueIntegrityStatus().getStopoverStatus())
				|| OneAConstants.STATUS_INFORMATION_F.equals(flight.getRevenueIntegrityStatus().getSequenceStatus())
				|| OneAConstants.STATUS_INFORMATION_R.equals(flight.getRevenueIntegrityStatus().getSequenceStatus()));
	}
	
	/**
	 * check the flight if have valid e-ticket
	 * @param passenger
	 * @return
	 */
	public static boolean hasAnyInvalidEticketForCXKAFlight(List<Flight> flights) {
		boolean result = false;
		if (flights == null || flights.isEmpty()) {
			return result;
		}
		return flights.stream().anyMatch(
				flt -> (CARRIER_CX.equals(flt.getOperateCompany()) || CARRIER_KA.equals(flt.getOperateCompany()))
						&& !isDisplayOnly(flt) && StringUtils.isEmpty(flt.getAssocETicketNumber()));
	}
	
	/**
	 * Judge the flight whether can do check in
	 * @param passenger
	 * @return
	 */
	public static boolean hasAnyNeedPaymentSeat(Passenger passenger) {
		if (passenger == null || passenger.getFlights() == null) {
			return false;
		}
		
		if (passenger.getCustomerLevelFqtvInfo() != null) {
			boolean isCustomerLevelTopTier = passenger.getCustomerLevelFqtvInfo().stream()
					.anyMatch(fqtv -> fqtv.isTopTierMember());
			if(isCustomerLevelTopTier){
				return false;
			}
		}
		for (Flight flight : getAvailableFlights(passenger)) {
			if(!CARRIER_CX.equals(flight.getOperateCompany()) && !CARRIER_KA.equals(flight.getOperateCompany())){
				continue;
			}
			boolean upaidFlight = flight.getSeat() != null && flight.getSeat().isChargeable()
					&& !flight.getSeat().isPaid();
			if(upaidFlight){
				boolean isFlightLevelTopTier = false;
				if (flight.getProductLevelFqtvInfo() != null) {
					isFlightLevelTopTier = flight.getProductLevelFqtvInfo().stream()
							.anyMatch(fqtv -> fqtv.isTopTierMember());
				}
				if(!isFlightLevelTopTier){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 *  return true if any flight check in accepted
	 * @param flights
	 * @return
	 */
	public static boolean isAnyCheckInAccepted(Passenger passenger){
		List<Flight> flights = getAvailableFlights(passenger);
		return flights.stream().anyMatch(flt -> CHECKIN_ACCEPTED_STATUS.equals(flt.getAcceptanceStatus()));
	}
	
	/**
	 *  return true if all flight check in accepted
	 * @param flights
	 * @return
	 */
	public static boolean isAllCheckInAccepted(Passenger passenger){
		List<Flight> flights = getAvailableFlights(passenger);
		return flights.stream().allMatch(flt -> CHECKIN_ACCEPTED_STATUS.equals(flt.getAcceptanceStatus()));
	}
	
	public static boolean hasNonADCRegulatoryStatusWithBPNotAllowed(Passenger passenger){
		 return passenger.getFlights().stream()
			.filter(f -> !CollectionUtils.isEmpty(f.getRegulatoryStatusList()))
			.flatMap(f -> f.getRegulatoryStatusList().stream())
			.anyMatch(r -> r!=null && !REGULATORY_STATUS_ADC.equals(r.getRegSysType()) && REGULATORY_STATUS_NOT_ALLOW.equals(r.getAllowBP()));
	}
	
	/**
	 * return true if there is at least one accepted flight and at least one
	 * non-accepted flight
	 * 
	 * @param flights
	 * @return
	 */
	public static boolean isCheckInPartiallyAccepted(Passenger passenger) {
		List<Flight> flights = getAvailableFlights(passenger);
		return isAnyCheckInAccepted(passenger)
				&& flights.stream().anyMatch(flt -> !CHECKIN_ACCEPTED_STATUS.equals(flt.getAcceptanceStatus()));
	}
	
	/**
	 *  return true if any flight boarded
	 * @param flights
	 * @return
	 */
	public static boolean isBoarded(Passenger passenger){
		List<Flight> flights = getAvailableFlights(passenger);
		return flights.stream().anyMatch(flt -> BooleanUtils.isTrue(flt.isBoarded()));
	}

	/**
	 * return true if any flight is stand by
	 * 
	 * @param flights
	 * @return
	 */
	public static boolean isStandBy(Passenger passenger) {
		List<Flight> flights = getAvailableFlights(passenger);
		return flights.stream().anyMatch(flt -> CHECKIN_STANDBY_STATUS.equals(flt.getAcceptanceStatus()));
	}
	
	/**
	 * Get all flights of passenger, ignoring all flags.
	 * 
	 * @param passenger
	 * @return
	 */
	@NotNull
	public static List<Flight> getAllFlights(Passenger passenger) {
		if (passenger == null || passenger.getFlights() == null) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(passenger.getFlights());
	}

	/**
	 * Get flights of passenger by specifying condition to filter flight.
	 * 
	 * @param passenger
	 * @param flightPredicate
	 * @return
	 */
	@NotNull
	public static List<Flight> getFilteredFlights(Passenger passenger, Predicate<Flight> flightPredicate) {
		if (flightPredicate == null) {
			throw new IllegalArgumentException("Flight predicate cannot be null.");
		}

		if (passenger == null || passenger.getFlights() == null) {
			return Collections.emptyList();
		}

		List<Flight> filteredFlights =
				passenger.getFlights().stream().filter(flightPredicate).collect(Collectors.toList());

		return Collections.unmodifiableList(filteredFlights);
	}

	/**
	 * Get all check in available flights of passenger.
	 * 
	 * @param passenger
	 * @return
	 */
	@NotNull
	public static List<Flight> getAvailableFlights(Passenger passenger) {
		return getFilteredFlights(passenger, flight -> !isDisplayOnly(flight));
	}

	/**
	 * Get display only flights of passenger.
	 * 
	 * @param passenger
	 * @return
	 */
	@NotNull
	public static List<Flight> getDisplayOnlyFlights(Passenger passenger) {
		return getFilteredFlights(passenger, JourneyUtil::isDisplayOnly);
	}
	
	/**
	 * Check whether flight is exit seat suitable already.
	 * 
	 * @param flight
	 * @return
	 */
	public static boolean isExitSeatSuitable(Flight flight) {
		if (flight.getIndicatorList() == null) {
			return false;
		}
		return flight.getIndicatorList().stream().anyMatch(
				indicator -> EXIT_SEAT_SUITABLE_INDICATOR.equals(indicator.getAttributeType()));
	}
	
	/**
	 * Check whether the passenger is allowed to cancel check-in.
	 * Passenger is not allowed to cancel check-in if he/she checked in from other channel or has checked baggage.
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean isCancelCheckInAllowed(Passenger passenger) {
		// Not allow cancel check-in if the passenger is inhibited for checkin by SSR rules
		if (passenger == null || inhibitCheckinBySsrSkRule(passenger)) {
			return false;
		}
		List<Flight> acceptedFlightList = passenger.getFlights().stream()
				.filter(f -> !isDisplayOnly(f) && CHECKIN_ACCEPTED_STATUS.equals(f.getAcceptanceStatus()))
				.collect(Collectors.toList());

		return !acceptedFlightList.isEmpty() && acceptedFlightList.stream()
				.allMatch(f -> (f.getCheckInChannel() != null && f.getCheckInChannel() == CheckInChannel.WEB)
						&& !f.isHasCheckedBaggage());
	}
	
	/**
	 * Check whether the passenger is allowed to cancel check-in.
	 * Passenger is not allowed to cancel check-in if he/she checked in from other channel or has checked baggage.
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean isCMCheckIn(Passenger passenger) {
		// Not allow cancel check-in if the passenger is inhibited for checkin by SSR rules
		return passenger.getFlights().stream().anyMatch(f ->f.getCheckInChannel() != null && f.getCheckInChannel().equals(CheckInChannel.JFE));

	}

	/**
	 * Check whether MBP is allowed.
	 * 
	 * @param passengerList
	 * @return
	 */
	public static boolean isMBPAllowed(Passenger passenger) {

		// Return false the passenger not checked in yet.
		if (!isAnyCheckInAccepted(passenger)) {
			return false;
		}
		List<Flight> availableFlights = new ArrayList<>();
		// Get the available flights before ferry.
		for (Flight flight : JourneyUtil.getAvailableFlights(passenger)) {
			if (!flight.isFerryFlight()) {
				availableFlights.add(flight);
			} else {
				break;
			}
		}
		// return false if any flight(Available only)
		if (availableFlights.stream().allMatch(Flight::isAllowMBP)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether SPBP is allowed.
	 * 
	 * @param passengerList
	 * @return
	 */
	public static boolean isSPBPAllowed(Passenger passenger) {
		// Return false the passenger not checked in yet.
		if (!isAnyCheckInAccepted(passenger)) {
			return false;
		}
		List<Flight> availableFlights = new ArrayList<>();
		// Get the available flights before ferry.
		for (Flight flight : JourneyUtil.getAvailableFlights(passenger)) {
			if (!flight.isFerryFlight()) {
				availableFlights.add(flight);
			} else {
				break;
			}
		}
		// return false if any flight(Available only) 
		if (availableFlights.stream().allMatch(Flight::isAllowSPBP)) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether the Flight is US Flight with check-in time >= 24 hours
	 * before departure time
	 * 
	 * @param flight
	 * @return
	 */
	public static boolean isUSFlightWithInhibitedBPCheckInTime(Flight flight) {
		if (COUNTRY_CODE_US.equals(flight.getOriginCountryCode())
				|| COUNTRY_CODE_US.equals(flight.getDestCountryCode())) {

			String departureDateTimeStr = flight.getDepartureTime().getTime();
			LocalDateTime departureDateTime = LocalDateTime.parse(departureDateTimeStr,
					DateTimeFormatter.ofPattern(DepartureArrivalTime.TIME_FORMAT));

			ZoneOffset offset = ZoneOffset.of(flight.getDepartureTime().getTimeZoneOffset());
			long hours = Instant.now().until(departureDateTime.toInstant(offset), ChronoUnit.HOURS);
			// Check if the hour difference is more than 24 hours
			if (hours >= 24) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Indicate whether the passenger has associated infant.
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean hasAsscoiatedInfant(Passenger passenger) {
		return passenger.getFlights().stream()
				.anyMatch(f -> f.getProductIdentifierJID() != null && !f.getProductIdentifierJID().isEmpty());
	}
	
	/**
	 * Indicate whether the passenger has regulatory status with BP not allowed
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean hasRegulatoryStatusWithBPNotAllowed(Passenger passenger) {
		return passenger.getFlights().stream()
				.filter(f -> f.getRegulatoryStatusList() != null && !f.getRegulatoryStatusList().isEmpty())
				.flatMap(f -> f.getRegulatoryStatusList().stream())
				.anyMatch(r -> r != null && REGULATORY_STATUS_NOT_ALLOW.equals(r.getAllowBP()));
	}
	
	/**
	 * Indicate whether the passenger has SSR with BP inhibited
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean hasSSRWithBPNotAllowed(Passenger passenger) {
		return passenger.getSsrSkMappingRule() != null
				&& BP_INHIBIT.equals(passenger.getSsrSkMappingRule().getBpValue());
	}
	
	/**
	 * Indicate whether the passenger has any high priority comments associated (Except DCHK)
	 * 
	 * @param passenger
	 * @return
	 */
	public static boolean hasHighPriorityComments(Passenger passenger) {
		return passenger.isInhibitBPByComment();
	}

	/**
	 * Check the type whether in primary travel doc type.
	 * 
	 * @param type
	 * @param cprConfig
	 * @return
	 */
	public static boolean isInactiveBookingStatus(final String status, final String[] inactiveBookingStatus) {
		boolean isInactiveBookingStatus = false;
		for (int i = 0; i < inactiveBookingStatus.length; i++) {
			if (inactiveBookingStatus[i].equals(status)) {
				isInactiveBookingStatus = true;
				break;
			}
		}
		return isInactiveBookingStatus;
	}
	
	/**
	 * Indicate whether it is staff booking
	 * 
	 * @param journey
	 * @return
	 */
	public static boolean isStaffBooking(Journey journey) {
		return journey.getPassengers().stream().anyMatch(p -> p.getLoginPax() && p.getStaffDetails() != null);
	}
	
	/**
	 * find Infant Parent
	 * 
	 * @param passengers
	 * @param infant
	 * @return
	 */
	public static Passenger findInfantParent(List<Passenger> passengers, Passenger infant) {
		List<Flight> infantFlights = JourneyUtil.getAvailableFlights(infant);
		Flight infantFlight = infantFlights.get(0);
		for (Passenger passenger : passengers) {
			List<Flight> flights = JourneyUtil.getAvailableFlights(passenger);
			for (Flight flight : flights) {
				String parentJID = flight.getProductIdentifierJID();
				if (parentJID != null && parentJID.equals(infantFlight.getProductIdentifierDID())) {
					return passenger;
				}
			}
		}
		return null;
	}
	
	public static boolean hasNonCXKAFlight(Passenger passenger) {
		boolean result = false;
		if (passenger.getFlights() == null || passenger.getFlights().isEmpty()) {
			return result;
		}
		return passenger.getFlights().stream().anyMatch(
				flt -> (!CARRIER_CX.equals(flt.getOperateCompany()) && !CARRIER_KA.equals(flt.getOperateCompany())));
	}
	
}
