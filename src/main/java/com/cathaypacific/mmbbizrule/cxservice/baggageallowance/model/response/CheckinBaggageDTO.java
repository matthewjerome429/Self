package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CheckinBaggageDTO implements Serializable {

	private static final long serialVersionUID = 517638513018542416L;

	private CheckinBaggageWeightDTO weight;

	private CheckinBaggagePieceDTO piece;

	private DimensionDTO dimension;

	public CheckinBaggageWeightDTO getWeight() {
		return weight;
	}

	public void setWeight(CheckinBaggageWeightDTO weight) {
		this.weight = weight;
	}

	public CheckinBaggagePieceDTO getPiece() {
		return piece;
	}

	public void setPiece(CheckinBaggagePieceDTO piece) {
		this.piece = piece;
	}

	public DimensionDTO getDimension() {
		return dimension;
	}

	public void setDimension(DimensionDTO dimension) {
		this.dimension = dimension;
	}

}
