package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu;

import java.io.Serializable;
import java.util.List;

public class BgAlBtuRequestDTO implements Serializable {

	private static final long serialVersionUID = 6991957046548905453L;

	private List<BtuRequestDTO> btu;

	public List<BtuRequestDTO> getBtu() {
		return btu;
	}

	public void setBtu(List<BtuRequestDTO> btu) {
		this.btu = btu;
	}

}
