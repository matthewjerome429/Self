package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.ArrayList;
import java.util.List;

public class SeatSelection {
	
	/** eligibility to select seat */
	private Boolean eligible;
	
	private SeatSelectionIneligibleReason ineligibleReason;
	
	/** eligibility to select special seat */
	private SpecialSeatEligibility specialSeatEligibility;
	
	/** ssr/sk disability list */
	private List<String> disabilities;
	
	/** extra legroom seat free of charge */
	private Boolean xlFOC;
	
	/** the reason why extra legroom seat is free of charge */
	private String xlFOCReason;
	
	/** is low RBD */
	private Boolean lowRBD;
	
	/** is paid ASR */
	private boolean paidASR;
	
	/** asr FOC */
	private boolean asrFOC;
	
	/** the selected asr is FOC, e.g. paid by miles (determined by SK ASMR), can only change to the seats of same type freely, other seats are blocked */
	private boolean selectedAsrFOC;

	/** the flight is eligible for displaying or submitting seat preference form*/
	private boolean isSeatPreferenceEligible;

	public SeatSelection() {
		this.eligible = false;
		this.specialSeatEligibility = new SpecialSeatEligibility();
		this.xlFOC = false;
		this.isSeatPreferenceEligible = true;
	}
	
	public Boolean isEligible() {
		return eligible;
	}

	public void setEligible(Boolean eligible) {
		this.eligible = eligible;
	}

	public SeatSelectionIneligibleReason getIneligibleReason() {
		return ineligibleReason;
	}

	public void setIneligibleReason(SeatSelectionIneligibleReason ineligibleReason) {
		this.ineligibleReason = ineligibleReason;
	}

	public SpecialSeatEligibility getSpecialSeatEligibility() {
		return specialSeatEligibility;
	}

	public void setSpecialSeatEligibility(SpecialSeatEligibility specialSeatEligibility) {
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
		return paidASR;
	}

	public void setPaidASR(boolean paidASR) {
		this.paidASR = paidASR;
	}

	public boolean isAsrFOC() {
		return asrFOC;
	}

	public void setAsrFOC(boolean asrFOC) {
		this.asrFOC = asrFOC;
	}

	public boolean isSelectedAsrFOC() {
		return selectedAsrFOC;
	}

	public void setSelectedAsrFOC(boolean selectedAsrFOC) {
		this.selectedAsrFOC = selectedAsrFOC;
	}

	public boolean getIsSeatPreferenceEligible() {
		return this.isSeatPreferenceEligible;
	}

	public void setIsSeatPreferenceEligible(boolean isSeatPreferenceEligible) {
		this.isSeatPreferenceEligible = isSeatPreferenceEligible;
	}
}
