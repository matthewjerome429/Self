package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PassengerSegment {
	
	private ETicket eticket;
	
	private String passengerId;

	private String segmentId;

	private SeatDetail seat;
	/** in OLCI flow, seat retrieved from CPR or PNR may be overridden by the tempSeat, this is the original seat */
	private SeatDetail originalSeat;
	
	/**
	 * In OLCI flow, seat from PNR.
	 */
	private SeatDetail pnrSeat;
	
	/**
	 * In OLCI flow, seat from CPR.
	 */
	private SeatDetail cprSeat;
	
	private SeatPreference preference;
		
	private BigInteger seatQulifierId;
	
	private List<SeatDetail> extraSeats;

	private BaggageAllowance baggageAllowance;

	private MealDetail meal;
	
	private MemberAward memberAward;
	
    /** The country of residence */
    private String countryOfResidence;

	private TravelDoc priTravelDoc;
	
	private TravelDoc secTravelDoc;
	
	private FQTVInfo fqtvInfo;
	
	private FQTVInfo fqtrInfo;
	// MMB seat selection
	private SeatSelection mmbSeatSelection;
	// OLCI seat selection
	private SeatSelection olciSeatSelection;
	
	private MealSelection mealSelection;
	
	private String pickUpNumber;
	
	private boolean checkedIn;

	private boolean hasCheckedBaggage;
	
	private Boolean isTransit;
	
	private String departureGate;

	private List<SpecialService> specialServices;
	
	private ClaimedLounge claimedLounge;
	
	private PurchasedLounge purchasedLounge;
	
	private AirportUpgradeInfo airportUpgradeInfo;
	
	private UpgradeProgressStatus upgradeProgressStatus;
	
	/** reverse checkin carrier **/
	private String reverseCheckinCarrier;
	
	/**--------------------Cpr information start----------------------*/
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;

	/** Relate TO Adult's Sector DID, this filed only for infant. */
	private String cprProductIdentifierJID;
	
	/** Serial number of Apple Wallet boarding pass */
	private String cprApplePassNumber;
	
	/** check if enableASRForRedemptionBooking */
	private boolean enableASRForRedemptionBooking;
	
	/** inhibit change seat from olci response passenger level **/
	private boolean inhibitChangeSeat;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public SeatDetail getSeat() {
		return seat;
	}
	
	public SeatDetail findSeat() {
		if(seat == null){
			seat =new SeatDetail();
		}
		return seat;
	}

	public void setSeat(SeatDetail seat) {
		this.seat = seat;
	}

	public SeatDetail getOriginalSeat() {
		return originalSeat;
	}

	public void setOriginalSeat(SeatDetail originalSeat) {
		this.originalSeat = originalSeat;
	}

	public SeatDetail getPnrSeat() {
		return pnrSeat;
	}

	public void setPnrSeat(SeatDetail pnrSeat) {
		this.pnrSeat = pnrSeat;
	}

	public SeatDetail getCprSeat() {
		return cprSeat;
	}

	public void setCprSeat(SeatDetail cprSeat) {
		this.cprSeat = cprSeat;
	}

	public BaggageAllowance getBaggageAllowance() {
		return baggageAllowance;
	}
	
	public BaggageAllowance findBaggageAllowance() {
		if(baggageAllowance == null){
			baggageAllowance = new BaggageAllowance();
		}
		return baggageAllowance;
	}

	public void setBaggageAllowance(BaggageAllowance baggageAllowance) {
		this.baggageAllowance = baggageAllowance;
	}

	public MealDetail getMeal() {
		return meal;
	}

	public void setMeal(MealDetail meal) {
		this.meal = meal;
	}

	public MemberAward getMemberAward() {
		if(memberAward == null){
			memberAward = new MemberAward();
		}
		return memberAward;
	}

	public void setMemberAward(MemberAward memberAward) {
		this.memberAward = memberAward;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public TravelDoc getPriTravelDoc() {
		return priTravelDoc;
	}
	
	public TravelDoc findPriTravelDoc() {
		if(priTravelDoc == null) {
			priTravelDoc = new TravelDoc();
		}
		return priTravelDoc;
	}

	public void setPriTravelDoc(TravelDoc priTravelDoc) {
		this.priTravelDoc = priTravelDoc;
	}

	public TravelDoc getSecTravelDoc() {
		return secTravelDoc;
	}
	
	public TravelDoc findSecTravelDoc() {
		if(secTravelDoc == null) {
			secTravelDoc = new TravelDoc();
		}
		return secTravelDoc;
	}

	public void setSecTravelDoc(TravelDoc secTravelDoc) {
		this.secTravelDoc = secTravelDoc;
	}

	public FQTVInfo getFqtvInfo() {
		if(fqtvInfo == null) {
			fqtvInfo = new FQTVInfo();
		}
		return fqtvInfo;
	}

	public void setFqtvInfo(FQTVInfo fqtvInfo) {
		this.fqtvInfo = fqtvInfo;
	}
	
	public FQTVInfo getFqtrInfo() {
		if(fqtrInfo == null) {
			fqtrInfo = new FQTVInfo();
		}
		return fqtrInfo;
	}

	public void setFqtrInfo(FQTVInfo fqtrInfo) {
		this.fqtrInfo = fqtrInfo;
	}

	public FQTVInfo findFqtrInfo() {
		if(fqtrInfo == null) {
			fqtrInfo = new FQTVInfo();
		}
		return fqtrInfo;
	}
	
	public List<SeatDetail> getExtraSeats() {
		return extraSeats;
	}
	
	public List<SeatDetail> findExtraSeats() {
		if(extraSeats == null){
			extraSeats = new ArrayList<>();
		}
		return extraSeats;
	}

	public void setExtraSeats(List<SeatDetail> extraSeats) {
		this.extraSeats = extraSeats;
	}

	public SeatPreference getPreference() {
		return preference;
	}
	
	public SeatPreference findPreference() {
		if(preference == null){
			preference = new SeatPreference();
		}
		return preference;
	}

	public void setPreference(SeatPreference preference) {
		this.preference = preference;
	}

	public SeatSelection getMmbSeatSelection() {
		return mmbSeatSelection;
	}

	public void setMmbSeatSelection(SeatSelection mmbSeatSelection) {
		this.mmbSeatSelection = mmbSeatSelection;
	}

	public SeatSelection getOlciSeatSelection() {
		return olciSeatSelection;
	}

	public void setOlciSeatSelection(SeatSelection olciSeatSelection) {
		this.olciSeatSelection = olciSeatSelection;
	}

	public String getPickUpNumber() {
		return pickUpNumber;
	}

	public void setPickUpNumber(String pickUpNumber) {
		this.pickUpNumber = pickUpNumber;
	}

	public MealSelection getMealSelection() {
		return mealSelection;
	}
	
	public MealSelection findMealSelection() {
	    if(this.mealSelection == null) {
	        this.mealSelection = new MealSelection();
	    }
	    return this.mealSelection;
    }
    
	public void setMealSelection(MealSelection mealSelection) {
		this.mealSelection = mealSelection;
	}

	public BigInteger getSeatQulifierId() {
		return seatQulifierId;
	}

	public void setSeatQulifierId(BigInteger seatQulifierId) {
		this.seatQulifierId = seatQulifierId;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public boolean isHasCheckedBaggage() {
		return hasCheckedBaggage;
	}

	public void setHasCheckedBaggage(boolean hasCheckedBaggage) {
		this.hasCheckedBaggage = hasCheckedBaggage;
	}

	public List<SpecialService> getSpecialServices() {
		return specialServices;
	}

	public void setSpecialServices(List<SpecialService> specialServices) {
		this.specialServices = specialServices;
	}

	public Boolean getIsTransit() {
		return isTransit;
	}

	public void setIsTransit(Boolean isTransit) {
		this.isTransit = isTransit;
	}

	public String getDepartureGate() {
		return departureGate;
	}

	public void setDepartureGate(String departureGate) {
		this.departureGate = departureGate;
	}

	public String getEticketOriginatorId() {
		return this.eticket == null ? null : this.eticket.getOriginatorId();
	}
	
	public void setEticketOriginatorId(String originatorId) {
		findEticket().setOriginatorId(originatorId);
	}
	
	public ETicket getEticket() {
		return eticket;
	}

	public ETicket findEticket() {
		if(this.eticket == null) {
			this.eticket = new ETicket();
		}
		return this.eticket;
	}

	public void setEticket(ETicket eticket) {
		this.eticket = eticket;
	}
	
	public String getEticketNumber() {
		return this.eticket == null ? null : this.eticket.getNumber();
	}
	
	public void setEticketNumber(String eticketNumber) {
		findEticket().setNumber(eticketNumber);
	}
	@JsonIgnore
	public List<String> getEticketCouponStatus() {
		if(this.eticket == null || CollectionUtils.isEmpty(this.eticket.getCouponInfos())) {
			return Collections.emptyList();
		} else {
			return this.eticket.getCouponInfos().stream().map(TicketProcessCouponInfo::getCpnStatus).distinct().collect(Collectors.toList());
		}
	}
	@JsonIgnore
	public String getEticketPaxType() {
		if(eticket == null) {
			return null;
		}
		return eticket.getPassengerType();
	}
	@JsonIgnore
	public Date getTicketIssueDateByTimezone(String timezoneOffset) {
		if(eticket == null || StringUtils.isEmpty(eticket.getDate())) {
			return null;
		}
		Date ticketIssueDate = null;
		try {
			if(StringUtils.isNotEmpty(timezoneOffset)) {
				return DateUtil.getStrToGMTDate(ETicket.TIME_FORMAT, timezoneOffset, eticket.getDate());
			} else {
				return DateUtil.getStrToDate(ETicket.TIME_FORMAT, eticket.getDate());				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ticketIssueDate;
	}
	@JsonIgnore
	public Date getTicketProductDateByTimeZone(String timezoneOffset) {
		if(eticket == null || StringUtils.isEmpty(eticket.getDate())) {
			return null;
		}
		Date productDepartureDate = null;
		try {
			if(StringUtils.isNotEmpty(timezoneOffset)) {
				return DateUtil.getStrToGMTDate(DateUtil.DATE_PATTERN_DDMMYY, timezoneOffset, eticket.getProductDepartureDate());
			} else {
				return DateUtil.getStrToDate(DateUtil.DATE_PATTERN_DDMMYY, eticket.getProductDepartureDate());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return productDepartureDate;
	}

	public ClaimedLounge getClaimedLounge() {
		return claimedLounge;
	}

	public void setClaimedLounge(ClaimedLounge claimedLounge) {
		this.claimedLounge = claimedLounge;
	}

	public AirportUpgradeInfo getAirportUpgradeInfo() {
		return airportUpgradeInfo;
	}

	public void setAirportUpgradeInfo(AirportUpgradeInfo airportUpgradeInfo) {
		this.airportUpgradeInfo = airportUpgradeInfo;
	}

	public UpgradeProgressStatus getUpgradeProgressStatus() {
		return upgradeProgressStatus;
	}

	public void setUpgradeProgressStatus(UpgradeProgressStatus upgradeProgressStatus) {
		this.upgradeProgressStatus = upgradeProgressStatus;
	}

	public String getReverseCheckinCarrier() {
		return reverseCheckinCarrier;
	}

	public void setReverseCheckinCarrier(String reverseCheckinCarrier) {
		this.reverseCheckinCarrier = reverseCheckinCarrier;
	}

	public PurchasedLounge getPurchasedLounge() {
		return purchasedLounge;
	}

	public void setPurchasedLounge(PurchasedLounge purchasedLounge) {
		this.purchasedLounge = purchasedLounge;
	}

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}

	public String getCprProductIdentifierDID() {
		return cprProductIdentifierDID;
	}

	public void setCprProductIdentifierDID(String cprProductIdentifierDID) {
		this.cprProductIdentifierDID = cprProductIdentifierDID;
	}

	public String getCprProductIdentifierJID() {
		return cprProductIdentifierJID;
	}

	public void setCprProductIdentifierJID(String cprProductIdentifierJID) {
		this.cprProductIdentifierJID = cprProductIdentifierJID;
	}

	public String getCprApplePassNumber() {
		return cprApplePassNumber;
	}

	public void setCprApplePassNumber(String cprApplePassNumber) {
		this.cprApplePassNumber = cprApplePassNumber;
	}

	public boolean isEnableASRForRedemptionBooking() {
		return enableASRForRedemptionBooking;
	}

	public void setEnableASRForRedemptionBooking(boolean enableASRForRedemptionBooking) {
		this.enableASRForRedemptionBooking = enableASRForRedemptionBooking;
	}

    public boolean isInhibitChangeSeat() {
        return inhibitChangeSeat;
    }

    public void setInhibitChangeSeat(boolean inhibitChangeSeat) {
        this.inhibitChangeSeat = inhibitChangeSeat;
    }
	
}
