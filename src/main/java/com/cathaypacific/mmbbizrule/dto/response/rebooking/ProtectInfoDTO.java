package com.cathaypacific.mmbbizrule.dto.response.rebooking;

import java.io.Serializable;
import java.util.List;

public class ProtectInfoDTO implements Serializable {

	private static final long serialVersionUID = -3941838436416991018L;

	private List<ProtectFlightInfoDTO> cancelledFlightInfos;
	
	private List<ProtectFlightInfoDTO> confirmedFlightInfos;
	
	private List<ProtectFlightInfoDTO> atciCancelledFlightInfos;

	public List<ProtectFlightInfoDTO> getCancelledFlightInfos() {
		return cancelledFlightInfos;
	}

	public void setCancelledFlightInfos(List<ProtectFlightInfoDTO> cancelledFlightInfos) {
		this.cancelledFlightInfos = cancelledFlightInfos;
	}

	public List<ProtectFlightInfoDTO> getConfirmedFlightInfos() {
		return confirmedFlightInfos;
	}

	public void setConfirmedFlightInfos(List<ProtectFlightInfoDTO> confirmedFlightInfos) {
		this.confirmedFlightInfos = confirmedFlightInfos;
	}

	public List<ProtectFlightInfoDTO> getAtciCancelledFlightInfos() {
		return atciCancelledFlightInfos;
	}

	public void setAtciCancelledFlightInfos(List<ProtectFlightInfoDTO> atciCancelledFlightInfos) {
		this.atciCancelledFlightInfos = atciCancelledFlightInfos;
	}
	
}
