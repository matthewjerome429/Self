package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;

public class BtuResponseDTO implements Serializable {

	private static final long serialVersionUID = 7098909895260986325L;

	private String btuId;

	private List<BaggageAllowanceDTO> baggageAllowance;

	public String getBtuId() {
		return btuId;
	}

	public void setBtuId(String btuId) {
		this.btuId = btuId;
	}

	public List<BaggageAllowanceDTO> getBaggageAllowance() {
		return baggageAllowance;
	}

	public void setBaggageAllowance(List<BaggageAllowanceDTO> baggageAllowance) {
		this.baggageAllowance = baggageAllowance;
	}

}
