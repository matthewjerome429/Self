package com.cathaypacific.mbcommon.constants;

public class CacheNamesConstants {
	
	private CacheNamesConstants() {
		
	}

	public static final String TIMEZONE = "TimeZoneInformationCache";
	
	public static final String FLIGHTSTATUS = "FlightStatusNumberCache";
	
	public static final String COUNTRYCODE_TWO_THREE_MAP = "CountryCodeTwoCodeThreeCodeMapCache"; 
		
	public static final String AIR_FLIGHT_INFO = "AirFlightInfo"; 
	
	public static final String TICKET_PROCESS = "TicketProcess";
	/** 1A Inv_AdvancedGetFlightData cache name  */
	public static final String ADVANCED_FLIGHT_DATA = "Inv_AdvancedGetFlightData";
	
	public static final String AEP_BAGGAGE = "AEPBaggage";
	
	public static final String DP_ELIGIBILITY_PNR = "DPEligibilityPnr";
	
	/** AEM cache start */
	public static final String COUNTRYCODE = "AEMCountrycodeCache";
	
	public static final String COUNTRYNAME = "AEMCountryNameCache";
	
	public static final String CITYCODE = "AEMCityCodeCache";
	
	public static final String CITYAIRPORTS = "AEMCityAirportsCache";
	
	public static final String AIRPORTCITY = "AEMAirportCityCache";
	
	public static final String AIRPORTDETAIL = "AEMAirportdetailsCache";
	
	public static final String UMNR_EFORM_PDF_TEMPLATE = "AEMUMNREFormPDFTemplateCache";
	
	public static final String AIRPORTCODEBYNAME = "AEMAirportCodeByNameCache";
	
	public static final String COUNTRY_SELECTOR = "AEMCountrySelector";
	/** AEM cache end */
	
	public static final String SEAT_MAP = "SeatMapCache";
	
	public static final String TOKEN_BELOW = "FIFBelowCache";
	
	public static final String REDEMPTION_SUBCLASS = "RedemptionSubclassCache";
	
	/**
	 * DAO Cache
	 */
	public static final String TB_OPEN_CLOSE_TIME = "TbOpenCloseTime";
	public static final String TB_TRAVEL_DOC_DISPLAY = "TbTravelDocDisplay";
	public static final String TB_TRAVEL_DOC_LIST = "TbTravelDocList";
	public static final String TB_TRAVEL_DOC_OD = "TbTravelDocOd";
	public static final String TB_PORT_FLIGHT = "TbPortFlight";
	public static final String TB_TRAVEL_DOC_NAT_COI_CHECK = "TbTravelDocNatCoiCheck";

	/**
	 * Call olci db cache
	 */
	public static final String OPEN_CLOSE_TIME = "OpenCloseTime";
	public static final String TRAVEL_DOC_DISPLAY = "TravelDocDisplay";
	public static final String TRAVEL_DOC_LIST = "TravelDocList";
	public static final String TRAVEL_DOC_OD = "TravelDocOd";
	public static final String PORT_FLIGHT = "PortFlight";
	public static final String TRAVEL_DOC_NAT_COI_CHECK = "TravelDocNatCoiCheck";
}
