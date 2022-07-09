package com.cathaypacific.mmbbizrule.dto.response.specialservice;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.dto.common.booking.FQTVInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.MealSelectionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SeatSelectionDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.MemberAward;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RequestAssistancePassengerSegmentDTO implements Serializable {

	private static final long serialVersionUID = -3617904903720897386L;

	private String passengerId;
	
	private String segmentId;

	private MemberAward memberAward;
	
	private FQTVInfoDTO FQTVInfo;
 
	@JsonIgnore
	private String eticket;
		
	/** no e-ticket */
	private Boolean hasEticket;
	
	private Boolean checkedIn;

	private List<SpecialService> specialServices;
	
	private Boolean hasMobilityAssist;

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

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public Boolean getHasEticket() {
		return hasEticket;
	}

	public void setHasEticket(Boolean hasEticket) {
		this.hasEticket = hasEticket;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public List<SpecialService> getSpecialServices() {
		return specialServices;
	}

	public void setSpecialServices(List<SpecialService> specialServices) {
		this.specialServices = specialServices;
	}

	public Boolean getHasMobilityAssist() {
		return hasMobilityAssist;
	}

	public void setHasMobilityAssist(Boolean hasMobilityAssist) {
		this.hasMobilityAssist = hasMobilityAssist;
	}

}
