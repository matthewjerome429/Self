package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CabinBaggageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 871092507937402303L;

	private CabinBaggageWeightDTO weight;

	private CabinBaggagePieceDTO piece;
	
	private CabinSmallItemDTO smallItem;
	
	private CabinDimensionsDTO dimensions;

	public CabinBaggageWeightDTO getWeight() {
		return weight;
	}

	public void setWeight(CabinBaggageWeightDTO weight) {
		this.weight = weight;
	}

	public CabinBaggagePieceDTO getPiece() {
		return piece;
	}

	public void setPiece(CabinBaggagePieceDTO piece) {
		this.piece = piece;
	}

	public CabinSmallItemDTO getSmallItem() {
		return smallItem;
	}

	public void setSmallItem(CabinSmallItemDTO smallItem) {
		this.smallItem = smallItem;
	}

	public CabinDimensionsDTO getDimensions() {
		return dimensions;
	}

	public void setDimensions(CabinDimensionsDTO dimensions) {
		this.dimensions = dimensions;
	}

}
