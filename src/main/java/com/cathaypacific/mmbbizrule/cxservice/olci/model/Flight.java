package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import com.cathaypacific.mbcommon.utils.DateUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Flight implements Serializable {
	
	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 2484008084933232782L;

	/** Cabin Class */
    private String cabinClass;
    
    /** booking Class */
    private String subClass;
    
    /** travel Cabin Class **/
    private String travelCabinClass;
    
    /** original flight Number */
    private String operateFlightNumber;
    
    /** code share flight Number */
    private String marketFlightNumber;
    
    /** original flight Provider */
    private String operateCompany;
    
    /** code share flight Provider */
    private String marketingCompany;

	/** RTFS isProvisional indicator */
	private boolean provisional = false;
    
    /** all kinds of departure time */
    private DepartureArrivalTime departureTime;

    /** boarding time */
    private String boardingTime;
    
    /** Departure Port code */
    private String originPort;
    
    /** all kinds of arrival time */
    private DepartureArrivalTime arrivalTime;
    
    /** CPR Destination Port*/
    private String destPort;
    
    /** CPR Destination Port for first leg */
    private String firstLegDest;
    
    /** Seat Num*/
    private Seat seat;
    
    /** Temporary seat, not yet allocated */
    private Seat tempSeat;

    /** seat Preference */
    private String seatPreference;
    
    /** Temporary seat preference, not yet allocated */
    private String tempSeatPreference;
    
    /** Unique flight id, DID*/
    private String productIdentifierDID;
    
    /** Relate TO Infant's Sector DID, this filed only for Adult who has Infant.*/
    private String productIdentifierJID;
    
    /** Flight ID of the next sector */
    private String productIdentifierOID;

    /** Primary Travel Documents In productLevel*/
    private List<TravelDocument> primaryTravelDocuments;
    
    /** Secondary Travel Documents In productLevel*/
    private List<TravelDocument> secondaryTravelDocuments;
    
    /** KTN Travel Documents In productLevel*/
    private TravelDocument ktnTravelDocument;
    
    /** Departure gate **/
    private String departureGate;
    
    /** Security number **/
    private String securityNumber;
    
    /** Associated E-Ticket number */
    private String assocETicketNumber;
    
    /** conjunction E-Ticket number, if sectors more than 5*/
    private String conjunctionETicketNumber;
    
    /** Fqtv details **/
    private List<FqtvInfo> productLevelFqtvInfo;

    /** Acceptance status **/
    private String acceptanceStatus;
    
    /** RI Check **/
    private RevenueIntegrityStatus revenueIntegrityStatus;
    
    /** Indicator to mark the air port display only flights */
    private boolean portDisplayOnly = false;
    
    /** Indicator to mark the flight inhibited */
    private boolean inhibitedFlight = false;
    
    /** Inhibited flight corresponding error code */
    private String inhibitErrorCode = "";
    
    /** The booking status of the flight, possible value: HK */
    private String bookingStatus;
    
    /** List of Flight indicator */
    private List<AttributeDetails> indicatorList;

	/** List of Flight status */
	private List<FlightStatusInfo> flightStatusList;

	/** Travel Document version. */
	private int travelDocVersion;

	/** Required Travel Document Groups */
	private List<TravelDocGroup> travelDocGroupList;

	/** Travel Document group and types mapping */
	private Map<String, List<String>> travelDocTypeMap;

    /** CPR destination address */
    private AddressDetails destinationAddress;
    
    /** CPR transit */
    private Boolean transit;
    
    private String rtfsStatus;
    /** Emergency Contact */
    private EmergencyContact emergencyContact;
    
    /** CPR country of residence */
    private String countryOfResidence;
    
    /** flight info in this set is always unmasked */
    private Set<String> unmaskInfos;
    
    /** ssr list, serviceType = S, actionCode= MCF */
    private List<String> ssrList;
    
    /** sk list,serviceType = K, actionCode= MCF */
    private List<String> skList;
    
    /** Board Flag */
    private boolean boarded;

    /** DCHK **/
    private boolean dchk;

    /** CM Lounge **/
    private Lounge cmLounge;
    
    /** Check-in Channel **/
    private CheckInChannel checkInChannel;
    
    /** Indicate whether there is any checked baggage **/
    private boolean hasCheckedBaggage;
    
    /** Indicate the flight is prime flight */
    private boolean prime;
    
    /** Origin Country Code **/
    private String originCountryCode;
    
    /** Destination Country Code **/
    private String destCountryCode;
    
    /** Allow Self Print Boarding Pass */
    private boolean allowSPBP;
    
    /** Allow Mobile Boarding Pass */
    private boolean allowMBP;
    
    /** Comment list **/
    private List<Comment> commentList;
    
    /** Regulatory Status List */
    private List<RegulatoryStatus> regulatoryStatusList;
    
    /** Indicate whether it is ferry flight **/
    private boolean ferryFlight;
    
	/** Indicate ferry flight operator**/
    private String operatorKey;
    
    /** Boarding pass code **/
    private String boardingPassCode;
    
    /**Indicate TSA for bp**/
    private boolean allowTSA;
    
    /**Indicate fast track access for bp**/
    private boolean allowFastTrack;
    
    private boolean displayOnly;
    
	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	public String getSubClass() {
		return subClass;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}

	public String getOperateFlightNumber() {
		return operateFlightNumber;
	}

	public void setOperateFlightNumber(String operateFlightNumber) {
		this.operateFlightNumber = operateFlightNumber;
	}

	public String getMarketFlightNumber() {
		return marketFlightNumber;
	}

	public void setMarketFlightNumber(String marketFlightNumber) {
		this.marketFlightNumber = marketFlightNumber;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}

	public boolean isProvisional() {
		return provisional;
	}

	public void setProvisional(boolean provisional) {
		this.provisional = provisional;
	}

	public DepartureArrivalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DepartureArrivalTime departureTime) {
		this.departureTime = departureTime;
	}

	public String getBoardingTime() {
		return boardingTime;
	}

	public void setBoardingTime(String boardingTime) {
		DateFormat dateFormat = DateUtil.getDateFormat("yyyy-MM-dd HH:mm");
		if (boardingTime == null) {
			return;
		}
		try {
			dateFormat.parse(boardingTime);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Boarding time doesn't match format.", e);
		}
		this.boardingTime = boardingTime;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public DepartureArrivalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DepartureArrivalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getFirstLegDest() {
		return firstLegDest;
	}

	public void setFirstLegDest(String firstLegDest) {
		this.firstLegDest = firstLegDest;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Seat getTempSeat() {
		return tempSeat;
	}

	public void setTempSeat(Seat tempSeat) {
		this.tempSeat = tempSeat;
	}

	public String getSeatPreference() {
		return seatPreference;
	}

	public void setSeatPreference(String seatPreference) {
		this.seatPreference = seatPreference;
	}

	public String getTempSeatPreference() {
		return tempSeatPreference;
	}

	public void setTempSeatPreference(String tempSeatPreference) {
		this.tempSeatPreference = tempSeatPreference;
	}

	public String getProductIdentifierDID() {
		return productIdentifierDID;
	}

	public void setProductIdentifierDID(String productIdentifierDID) {
		this.productIdentifierDID = productIdentifierDID;
	}

	public String getProductIdentifierJID() {
		return productIdentifierJID;
	}

	public void setProductIdentifierJID(String productIdentifierJID) {
		this.productIdentifierJID = productIdentifierJID;
	}

	public String getProductIdentifierOID() {
		return productIdentifierOID;
	}

	public void setProductIdentifierOID(String productIdentifierOID) {
		this.productIdentifierOID = productIdentifierOID;
	}

	public List<TravelDocument> getPrimaryTravelDocuments() {
		if (primaryTravelDocuments == null) {
			primaryTravelDocuments = new ArrayList<>();
		}
		return primaryTravelDocuments;
	}

	public void setPrimaryTravelDocuments(List<TravelDocument> primaryTravelDocuments) {
		this.primaryTravelDocuments = primaryTravelDocuments;
	}

	public List<TravelDocument> getSecondaryTravelDocuments() {
		if (secondaryTravelDocuments == null) {
			secondaryTravelDocuments = new ArrayList<>();
		}
		return secondaryTravelDocuments;
	}

	public void setSecondaryTravelDocuments(List<TravelDocument> secondaryTravelDocuments) {
		this.secondaryTravelDocuments = secondaryTravelDocuments;
	}

	public String getDepartureGate() {
		return departureGate;
	}

	public void setDepartureGate(String departureGate) {
		this.departureGate = departureGate;
	}

	public String getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}

	public List<FqtvInfo> getProductLevelFqtvInfo() {
		return productLevelFqtvInfo;
	}

	public void setProductLevelFqtvInfo(List<FqtvInfo> productLevelFqtvInfo) {
		this.productLevelFqtvInfo = productLevelFqtvInfo;
	}

	public String getAssocETicketNumber() {
		return assocETicketNumber;
	}

	public void setAssocETicketNumber(String assocETicketNumber) {
		this.assocETicketNumber = assocETicketNumber;
	}


	public String getAcceptanceStatus() {
		return acceptanceStatus;
	}

	public void setAcceptanceStatus(String acceptanceStatus) {
		this.acceptanceStatus = acceptanceStatus;
	}

	public RevenueIntegrityStatus getRevenueIntegrityStatus() {
		return revenueIntegrityStatus;
	}

	public void setRevenueIntegrityStatus(RevenueIntegrityStatus revenueIntegrityStatus) {
		this.revenueIntegrityStatus = revenueIntegrityStatus;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public List<AttributeDetails> getIndicatorList() {
		return indicatorList;
	}

	public void setIndicatorList(List<AttributeDetails> indicatorList) {
		this.indicatorList = indicatorList;
	}

	public List<FlightStatusInfo> getFlightStatusList() {
		return flightStatusList;
	}

	public void setFlightStatusList(List<FlightStatusInfo> flightStatusList) {
		this.flightStatusList = flightStatusList;
	}

	public AddressDetails getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(AddressDetails destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public Boolean getTransit() {
		return transit;
	}

	public void setTransit(Boolean transit) {
		this.transit = transit;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public int getTravelDocVersion() {
		return travelDocVersion;
	}

	public void setTravelDocVersion(int travelDocVersion) {
		this.travelDocVersion = travelDocVersion;
	}

	public List<TravelDocGroup> getTravelDocGroupList() {
		return travelDocGroupList;
	}

	public void setTravelDocGroupList(List<TravelDocGroup> travelDocGroupList) {
		this.travelDocGroupList = travelDocGroupList;
	}

	public Map<String, List<String>> getTravelDocTypeMap() {
		return travelDocTypeMap;
	}

	public void setTravelDocTypeMap(Map<String, List<String>> travelDocTypeMap) {
		this.travelDocTypeMap = travelDocTypeMap;
	}

	public EmergencyContact getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(EmergencyContact emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public Set<String> getUnmaskInfos() {
		if (unmaskInfos == null) {
			unmaskInfos = new HashSet<>();
		}
		return unmaskInfos;
	}

	public void setUnmaskInfos(Set<String> unmaskInfoSet) {
		this.unmaskInfos = unmaskInfoSet;
	}

	public List<String> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<String> ssrList) {
		this.ssrList = ssrList;
	}

	public List<String> getSkList() {
		return skList;
	}

	public void setSkList(List<String> skList) {
		this.skList = skList;
	}

	public boolean isPortDisplayOnly() {
		return portDisplayOnly;
	}

	public void setPortDisplayOnly(boolean portDisplayOnly) {
		this.portDisplayOnly = portDisplayOnly;
	}

	public boolean isInhibitedFlight() {
		return inhibitedFlight;
	}

	public void setInhibitedFlight(boolean inhibitedFlight) {
		this.inhibitedFlight = inhibitedFlight;
	}
	
	public String getInhibitErrorCode() {
		return inhibitErrorCode;
	}

	public void setInhibitErrorCode(String inhibitErrorCode) {
		this.inhibitErrorCode = inhibitErrorCode;
	}
	
	public boolean isBoarded() {
		return boarded;
	}

	public void setBoarded(boolean boarded) {
		this.boarded = boarded;
	}

	public boolean isDchk() {
		return dchk;
	}

	public void setDchk(boolean dchk) {
		this.dchk = dchk;
	}
	
	public Lounge getCmLounge() {
		return cmLounge;
	}
	
	public void setCmLounge(Lounge cmLounge) {
		this.cmLounge = cmLounge;
	}
	
	public CheckInChannel getCheckInChannel() {
		return checkInChannel;
	}

	public void setCheckInChannel(CheckInChannel checkInChannel) {
		this.checkInChannel = checkInChannel;
	}

	public boolean isHasCheckedBaggage() {
		return hasCheckedBaggage;
	}

	public void setHasCheckedBaggage(boolean hasCheckedBaggage) {
		this.hasCheckedBaggage = hasCheckedBaggage;
	}

	public boolean isPrime() {
		return prime;
	}

	public void setPrime(boolean prime) {
		this.prime = prime;
	}

	public String getOriginCountryCode() {
		return originCountryCode;
	}

	public void setOriginCountryCode(String originCountryCode) {
		this.originCountryCode = originCountryCode;
	}

	public String getDestCountryCode() {
		return destCountryCode;
	}

	public void setDestCountryCode(String destCountryCode) {
		this.destCountryCode = destCountryCode;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public boolean isAllowSPBP() {
		return allowSPBP;
	}

	public void setAllowSPBP(boolean allowSPBP) {
		this.allowSPBP = allowSPBP;
	}

	public boolean isAllowMBP() {
		return allowMBP;
	}

	public void setAllowMBP(boolean allowMBP) {
		this.allowMBP = allowMBP;
	}

	public List<RegulatoryStatus> getRegulatoryStatusList() {
		return regulatoryStatusList;
	}

	public void setRegulatoryStatusList(List<RegulatoryStatus> regulatoryStatusList) {
		this.regulatoryStatusList = regulatoryStatusList;
	}


	public boolean isFerryFlight() {
		return ferryFlight;
	}

	public void setFerryFlight(boolean ferryFlight) {
		this.ferryFlight = ferryFlight;
	}

	public String getOperatorKey() {
		return operatorKey;
	}

	public void setOperatorKey(String operatorKey) {
		this.operatorKey = operatorKey;
	}
	
	public String getConjunctionETicketNumber() {
		return conjunctionETicketNumber;
	}
	public void setConjunctionETicketNumber(String conjunctionETicketNumber) {
		this.conjunctionETicketNumber = conjunctionETicketNumber;
	}
	public String getBoardingPassCode() {
		return boardingPassCode;
	}

	public void setBoardingPassCode(String boardingPassCode) {
		this.boardingPassCode = boardingPassCode;
	}

	public String getRtfsStatus() {
		return rtfsStatus;
	}

	public void setRtfsStatus(String rtfsStatus) {
		this.rtfsStatus = rtfsStatus;
	}

	public boolean isAllowTSA() {
		return allowTSA;
	}

	public void setAllowTSA(boolean allowTSA) {
		this.allowTSA = allowTSA;
	}

	public boolean isAllowFastTrack() {
		return allowFastTrack;
	}

	public void setAllowFastTrack(boolean allowFastTrack) {
		this.allowFastTrack = allowFastTrack;
	}

	public TravelDocument getKtnTravelDocument() {
		return ktnTravelDocument;
	}

	public void setKtnTravelDocument(TravelDocument ktnTravelDocument) {
		this.ktnTravelDocument = ktnTravelDocument;
	}

	public String getTravelCabinClass() {
		return travelCabinClass;
	}

	public void setTravelCabinClass(String travelCabinClass) {
		this.travelCabinClass = travelCabinClass;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}
	
}
