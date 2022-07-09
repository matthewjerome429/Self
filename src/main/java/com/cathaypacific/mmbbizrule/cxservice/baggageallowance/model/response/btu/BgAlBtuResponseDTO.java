package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu;

import java.io.Serializable;
import java.util.List;

public class BgAlBtuResponseDTO implements Serializable {

	private static final long serialVersionUID = 3785118169196541180L;

	private List<BtuResponseDTO> btu;

	public List<BtuResponseDTO> getBtu() {
		return btu;
	}

	public void setBtu(List<BtuResponseDTO> btu) {
		this.btu = btu;
	}

}
