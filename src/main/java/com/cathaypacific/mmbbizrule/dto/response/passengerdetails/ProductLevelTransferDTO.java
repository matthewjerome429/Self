package com.cathaypacific.mmbbizrule.dto.response.passengerdetails;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ProductLevelTransferDTO implements Serializable {
	
	private static final long serialVersionUID = -7215148070709130160L;
	
	@ApiModelProperty("The trasnfer information of FFP")
	private TransferDetailDTO ffp;
	
	@ApiModelProperty("The trasnfer information of travel document")
	private TransferDetailDTO travelDoc;

	public TransferDetailDTO getFfp() {
		return ffp;
	}
	
	public TransferDetailDTO findFfp() {
		if (ffp == null) {
			ffp = new TransferDetailDTO();
		}
		return ffp;
	}

	public void setFfp(TransferDetailDTO ffp) {
		this.ffp = ffp;
	}

	public TransferDetailDTO getTravelDoc() {
		return travelDoc;
	}
	
	public TransferDetailDTO findTravelDoc() {
		if (travelDoc == null) {
			travelDoc = new TransferDetailDTO();
		}
		return travelDoc;
	}

	public void setTravelDoc(TransferDetailDTO travelDoc) {
		this.travelDoc = travelDoc;
	}
}
