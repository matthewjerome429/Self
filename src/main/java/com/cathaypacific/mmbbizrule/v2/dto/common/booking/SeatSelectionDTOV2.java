package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelectionIneligibleReason;

import io.swagger.annotations.ApiModelProperty;

public class SeatSelectionDTOV2 {
	
	/** eligibility to select seat */
	@ApiModelProperty("if the passenger is eligible to do seat selection")
	private Boolean eligible;
	
	/** eligibility to select special seat */
	@ApiModelProperty("eligibility of special seats")
	private SpecialSeatEligibilityDTOV2 specialSeatEligibility;
	
	/** ssr/sk disability list */
	@ApiModelProperty("the SSR/SK which makes the passenger ineligible to do seat selection")
	private List<String> disabilities;
	
	/** extra legroom seat free of charge */
	@ApiModelProperty("if the passenger can choose extra legroom seat for free")
	private Boolean xlFOC;
	
	/** the reason why extra legroom seat is free of charge */
	@ApiModelProperty("the reason why the passenger can choose extra legroom seat for free")
	private String xlFOCReason;
	
	/** is low RBD */
	@ApiModelProperty("if is low RBD")
	private Boolean lowRBD;
	
	/** is paid ASR */
	private boolean isPaidASR;

	/** eligibility to select seat preference */
	private boolean isSeatPreferenceEligible;
	
	/** asr FOC */
	@ApiModelProperty("if the passenger can choose ASR seat for free")
	private boolean asrFOC;
	
	private SeatSelectionIneligibleReason ineligibleReason;

	public SeatSelectionDTOV2() {
		this.eligible = false;
		this.specialSeatEligibility = new SpecialSeatEligibilityDTOV2();
		this.xlFOC = false;
	}
	
	public Boolean isEligible() {
		return eligible;
	}

	public void setEligible(Boolean eligible) {
		this.eligible = eligible;
	}

	public SpecialSeatEligibilityDTOV2 getSpecialSeatEligibility() {
		return specialSeatEligibility;
	}
	
	public SpecialSeatEligibilityDTOV2 findSpecialSeatEligibility() {
		if(specialSeatEligibility == null){
			specialSeatEligibility = new SpecialSeatEligibilityDTOV2();
		}
		return specialSeatEligibility;
	}

	public void setSpecialSeatEligibility(SpecialSeatEligibilityDTOV2 specialSeatEligibility) {
		this.specialSeatEligibility = specialSeatEligibility;
	}

	public List<String> getDisabilities() {
		return disabilities;
	}

	public List<String> findDisabilities() {
		if(disabilities == null){
			disabilities = new ArrayList<>();
		}
		return disabilities;
	}
	
	public void setDisabilities(List<String> disabilities) {
		this.disabilities = disabilities;
	}

	public Boolean isXlFOC() {
		return xlFOC;
	}

	public void setXlFOC(Boolean xlFOC) {
		this.xlFOC = xlFOC;
	}

	public String getXlFOCReason() {
		return xlFOCReason;
	}

	public void setXlFOCReason(String xlFOCReason) {
		this.xlFOCReason = xlFOCReason;
	}

	public Boolean isLowRBD() {
		return lowRBD;
	}

	public void setLowRBD(Boolean lowRBD) {
		this.lowRBD = lowRBD;
	}

	public boolean isPaidASR() {
		return isPaidASR;
	}

	public void setPaidASR(boolean isPaidASR) {
		this.isPaidASR = isPaidASR;
	}

	public boolean isAsrFOC() {
		return asrFOC;
	}

	public void setAsrFOC(boolean asrFOC) {
		this.asrFOC = asrFOC;
	}

    public SeatSelectionIneligibleReason getIneligibleReason() {
        return ineligibleReason;
    }

    public void setIneligibleReason(SeatSelectionIneligibleReason ineligibleReason) {
        this.ineligibleReason = ineligibleReason;
    }


	public boolean getIsSeatPreferenceEligible() {
		return this.isSeatPreferenceEligible;
	}

	public void setIsSeatPreferenceEligible(boolean isSeatPreferenceEligible) {
		this.isSeatPreferenceEligible = isSeatPreferenceEligible;
	}
}
