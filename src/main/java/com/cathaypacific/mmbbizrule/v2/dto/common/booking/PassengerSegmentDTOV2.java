package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.MemberAward;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class PassengerSegmentDTOV2 implements Serializable{

	private static final long serialVersionUID = 5651199418183824576L;
	
	private String passengerId;
	
	private String segmentId;
	@ApiModelProperty(value = "the preference of seat.", required = false)
	private String seatPreference="";
	
	@ApiModelProperty(value = "if true the phone has special preference.", required = false)
	private boolean specialPreference;
	
	@ApiModelProperty(value = "the passenger has seat.", required = false)
	private SeatDTOV2 seat;
	
	@ApiModelProperty(value = "the passenger has extra seats.", required = false)
	private List<SeatDTOV2> extraSeats;

	@ApiModelProperty(value = "the passenger has meal.", required = false)
	private MealDetailDTOV2 meal;

	private MemberAward memberAward;
	
	/** The country of residence info */
	private String countryOfResidence;
	
	private TravelDocsDTOV2 travelDoc;
	
	private FQTVInfoDTOV2 FQTVInfo;

	@JsonIgnore
	private String eticket;
		
	/** seat selection */
	private SeatSelectionDTOV2 seatSelection;
	
	private MealSelectionDTOV2 mealSelection;
	
	private String pickUpNumber;
	
	/** no e-ticket */
	@ApiModelProperty(value = "if false the passenger do not have eticket.", required = false)
	private Boolean hasEticket;

	@ApiModelProperty(value = "if ture the passenger has checked baggage.", required = false)
	private boolean hasCheckedBaggage;
	
	@ApiModelProperty(value = "if ture the passenger is transit.", required = false)
	private Boolean isTransit;
	
	@ApiModelProperty(value = "the passenger departure gate of the current flight.", required = false)
	private String departureGate;

	private List<SpecialService> specialServices;
	
	@Deprecated
	private ClaimedLoungeDTO claimedLounge;
	
	private List<LoungeAccessDTOV2> loungeAccess;
	
	private AirportUpgradeInfoDTOV2 airportUpgradeInfo;
	
	private UpgradeProgressStatus upgradeProgressStatus;
	
	/** reverse checkin carrier **/
	@ApiModelProperty(value = "the carrier of reverse checkin.", required = false)
	private String reverseCheckinCarrier;
	
	@ApiModelProperty(value = "if true the passenger's eticket belongs to cx/ka.", required = false)
	private boolean isCxKaEt;
	
	/** Serial number of Apple Wallet boarding pass */
	private String cprApplePassNumber;
	
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
	
	public SeatDTOV2 getSeat() {
		return seat;
	}

	public void setSeat(SeatDTOV2 seat) {
		this.seat = seat;
	}

	public MealDetailDTOV2 getMeal() {
		return meal;
	}

	public void setMeal(MealDetailDTOV2 meal) {
		this.meal = meal;
	}

	public MemberAward getMemberAward() {
		return memberAward;
	}

	public void setMemberAward(MemberAward memberAward) {
		this.memberAward = memberAward;
	}

	public FQTVInfoDTOV2 getFQTVInfo() {
		return FQTVInfo;
	}
	
	public FQTVInfoDTOV2 findFQTVInfo() {
		if(FQTVInfo == null) {
			FQTVInfo = new FQTVInfoDTOV2();
		}
		return FQTVInfo;
	}

	public void setFQTVInfo(FQTVInfoDTOV2 fQTVInfo) {
		FQTVInfo = fQTVInfo;
	}

	public List<SeatDTOV2> getExtraSeats() {
		return extraSeats;
	}

	public void setExtraSeats(List<SeatDTOV2> extraSeats) {
		this.extraSeats = extraSeats;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public TravelDocsDTOV2 getTravelDoc() {
		return travelDoc;
	}
	
	public TravelDocsDTOV2 findTravelDoc() {
		if(travelDoc == null) {
			travelDoc = new TravelDocsDTOV2();
		}
		return travelDoc;
	}

	public void setTravelDoc(TravelDocsDTOV2 travelDoc) {
		this.travelDoc = travelDoc;
	}
	
	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public SeatSelectionDTOV2 getSeatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(SeatSelectionDTOV2 seatSelection) {
		this.seatSelection = seatSelection;
	}

	public String getSeatPreference() {
		return seatPreference;
	}

	public void setSeatPreference(String seatPreference) {
		this.seatPreference = seatPreference;
	}

	public String getPickUpNumber() {
		return pickUpNumber;
	}

	public void setPickUpNumber(String pickUpNumber) {
		this.pickUpNumber = pickUpNumber;
	}

	public MealSelectionDTOV2 getMealSelection() {
		return mealSelection;
	}

	public void setMealSelection(MealSelectionDTOV2 mealSelection) {
		this.mealSelection = mealSelection;
	}

	public Boolean isHasEticket() {
		return hasEticket;
	}

	public void setHasEticket(Boolean noEticket) {
		this.hasEticket = noEticket;
	}

	public boolean isSpecialPreference() {
		return specialPreference;
	}

	public void setSpecialPreference(boolean specialPreference) {
		this.specialPreference = specialPreference;
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

	public ClaimedLoungeDTO getClaimedLounge() {
		return claimedLounge;
	}

	public void setClaimedLounge(ClaimedLoungeDTO claimedLounge) {
		this.claimedLounge = claimedLounge;
	}

	public AirportUpgradeInfoDTOV2 getAirportUpgradeInfo() {
		return airportUpgradeInfo;
	}

	public void setAirportUpgradeInfo(AirportUpgradeInfoDTOV2 airportUpgradeInfo) {
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

	public List<LoungeAccessDTOV2> getLoungeAccess() {
		return loungeAccess;
	}

	public void setLoungeAccess(List<LoungeAccessDTOV2> loungeAccess) {
		this.loungeAccess = loungeAccess;
	}

	public boolean isCxKaEt() {
		return isCxKaEt;
	}

	public void setCxKaEt(boolean isCxKaEt) {
		this.isCxKaEt = isCxKaEt;
	}
	
	public void repopulateTravelDocumentGender(String gender) {
		if(travelDoc == null || StringUtils.isEmpty(gender)) {
			return;
		}
		
		if(travelDoc.getPriTravelDoc() != null) {
			travelDoc.getPriTravelDoc().setGender(gender);
		}
		if(travelDoc.getSecTravelDoc() != null) {
			travelDoc.getSecTravelDoc().setGender(gender);
		}
	}

	public String getCprApplePassNumber() {
		return cprApplePassNumber;
	}

	public void setCprApplePassNumber(String cprApplePassNumber) {
		this.cprApplePassNumber = cprApplePassNumber;
	}

}
