package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelectionIneligibleReason;

import io.swagger.annotations.ApiModelProperty;

public class SeatSelectionCustomizedInfoDTO {
	
	/** eligibility to select seat */
	@ApiModelProperty("if the passenger is eligible to do seat selection")
	private Boolean eligible;
	
	/** ineligible reason */
	@ApiModelProperty("the reason why pax is ineligible to do seat selection")
	private SeatSelectionIneligibleReason ineligibleReason;
	
	/** eligibility to select special seat */
	@ApiModelProperty("eligibility of special seats")
	private SpecialSeatEligibilityCustomizedInfoDTO specialSeatEligibility;
	
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

	public SeatSelectionCustomizedInfoDTO() {
		this.eligible = false;
		this.specialSeatEligibility = new SpecialSeatEligibilityCustomizedInfoDTO();
		this.xlFOC = false;
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

	public SpecialSeatEligibilityCustomizedInfoDTO getSpecialSeatEligibility() {
		return specialSeatEligibility;
	}
	
	public SpecialSeatEligibilityCustomizedInfoDTO findSpecialSeatEligibility() {
		if(specialSeatEligibility == null){
			specialSeatEligibility = new SpecialSeatEligibilityCustomizedInfoDTO();
		}
		return specialSeatEligibility;
	}

	public void setSpecialSeatEligibility(SpecialSeatEligibilityCustomizedInfoDTO specialSeatEligibility) {
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
