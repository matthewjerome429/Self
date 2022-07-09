package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SeatSelectionDTO {
	
	/** eligibility to select seat */
	@ApiModelProperty("if the passenger is eligible to do seat selection")
	private Boolean eligible;
	
	/** eligibility to select special seat */
	@ApiModelProperty("eligibility of special seats")
	private SpecialSeatEligibilityDTO specialSeatEligibility;
	
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
	
	/** asr FOC */
	@ApiModelProperty("if the passenger can choose ASR seat for free")
	private boolean asrFOC;
	
	/** selected asr FOC */
	@ApiModelProperty("if the selected ASR is free. if true, the selected ASR is free and pax can only change to seats of the same type")
	private boolean selectedAsrFOC;

	public SeatSelectionDTO() {
		this.eligible = false;
		this.specialSeatEligibility = new SpecialSeatEligibilityDTO();
		this.xlFOC = false;
	}
	
	public Boolean isEligible() {
		return eligible;
	}

	public void setEligible(Boolean eligible) {
		this.eligible = eligible;
	}

	public SpecialSeatEligibilityDTO getSpecialSeatEligibility() {
		return specialSeatEligibility;
	}
	
	public SpecialSeatEligibilityDTO findSpecialSeatEligibility() {
		if(specialSeatEligibility == null){
			specialSeatEligibility = new SpecialSeatEligibilityDTO();
		}
		return specialSeatEligibility;
	}

	public void setSpecialSeatEligibility(SpecialSeatEligibilityDTO specialSeatEligibility) {
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

	public boolean isSelectedAsrFOC() {
		return selectedAsrFOC;
	}

	public void setSelectedAsrFOC(boolean selectedAsrFOC) {
		this.selectedAsrFOC = selectedAsrFOC;
	}

}
