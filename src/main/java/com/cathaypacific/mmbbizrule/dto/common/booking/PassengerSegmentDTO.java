package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.LoungeAccessDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.MemberAward;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PassengerSegmentDTO implements Serializable{

	private static final long serialVersionUID = 5651199418183824576L;
	
	private String passengerId;
	
	private String segmentId;
	
	private String seatPreference="";

	private boolean specialPreference;
	
	private SeatDTO seat;
	
	private List<SeatDTO> extraSeats;

	private BaggageAllowanceDTO baggageAllowance;

	private MealDetailDTO meal;

	private MemberAward memberAward;
	
	private TravelDocsDTO travelDoc;
	
	private FQTVInfoDTO FQTVInfo;
 
	@JsonIgnore
	private String eticket;
		
	/** seat selection */
	private SeatSelectionDTO seatSelection;
	
	private MealSelectionDTO mealSelection;
	
	private String pickUpNumber;
	
	/** no e-ticket */
	private Boolean hasEticket;
	
	private boolean checkedIn;

	private boolean hasCheckedBaggage;
	
	private Boolean isTransit;
	
	private String departureGate;

	private List<SpecialService> specialServices;
	
	@Deprecated
	private ClaimedLoungeDTO claimedLounge;
	
	private List<LoungeAccessDTO> LoungeAccess;
	
	private AirportUpgradeInfoDTO airportUpgradeInfo;
	
	private UpgradeProgressStatus upgradeProgressStatus;
	
	/** reverse checkin carrier **/
	private String reverseCheckinCarrier;
	
	private boolean isCxKaEt;
	
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
	
	public SeatDTO getSeat() {
		return seat;
	}

	public void setSeat(SeatDTO seat) {
		this.seat = seat;
	}

	public BaggageAllowanceDTO getBaggageAllowance() {
		return baggageAllowance;
	}
	
	public BaggageAllowanceDTO findBaggageAllowance() {
		if(baggageAllowance == null){
			baggageAllowance = new BaggageAllowanceDTO();
		}
		return baggageAllowance;
	}

	public void setBaggageAllowance(BaggageAllowanceDTO baggageAllowance) {
		this.baggageAllowance = baggageAllowance;
	}

	public MealDetailDTO getMeal() {
		return meal;
	}

	public void setMeal(MealDetailDTO meal) {
		this.meal = meal;
	}

	public MemberAward getMemberAward() {
		return memberAward;
	}

	public void setMemberAward(MemberAward memberAward) {
		this.memberAward = memberAward;
	}

	public FQTVInfoDTO getFQTVInfo() {
		return FQTVInfo;
	}
	
	public FQTVInfoDTO findFQTVInfo() {
		if(FQTVInfo == null) {
			FQTVInfo = new FQTVInfoDTO();
		}
		return FQTVInfo;
	}

	public void setFQTVInfo(FQTVInfoDTO fQTVInfo) {
		FQTVInfo = fQTVInfo;
	}

	public List<SeatDTO> getExtraSeats() {
		return extraSeats;
	}

	public void setExtraSeats(List<SeatDTO> extraSeats) {
		this.extraSeats = extraSeats;
	}

	public TravelDocsDTO getTravelDoc() {
		return travelDoc;
	}
	
	public TravelDocsDTO findTravelDoc() {
		if(travelDoc == null) {
			travelDoc = new TravelDocsDTO();
		}
		return travelDoc;
	}

	public void setTravelDoc(TravelDocsDTO travelDoc) {
		this.travelDoc = travelDoc;
	}
	
	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public SeatSelectionDTO getSeatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(SeatSelectionDTO seatSelection) {
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

	public MealSelectionDTO getMealSelection() {
		return mealSelection;
	}

	public void setMealSelection(MealSelectionDTO mealSelection) {
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

	public ClaimedLoungeDTO getClaimedLounge() {
		return claimedLounge;
	}

	public void setClaimedLounge(ClaimedLoungeDTO claimedLounge) {
		this.claimedLounge = claimedLounge;
	}

	public AirportUpgradeInfoDTO getAirportUpgradeInfo() {
		return airportUpgradeInfo;
	}

	public void setAirportUpgradeInfo(AirportUpgradeInfoDTO airportUpgradeInfo) {
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

	public List<LoungeAccessDTO> getLoungeAccess() {
		return LoungeAccess;
	}

	public void setLoungeAccess(List<LoungeAccessDTO> loungeAccess) {
		LoungeAccess = loungeAccess;
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

}
