package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CabinDimensionsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4120497150754786777L;

	private DimensionDTO standard;

	private DimensionDTO small;

	public DimensionDTO getStandard() {
		return standard;
	}

	public void setStandard(DimensionDTO standard) {
		this.standard = standard;
	}

	public DimensionDTO getSmall() {
		return small;
	}

	public void setSmall(DimensionDTO small) {
		this.small = small;
	}

}
