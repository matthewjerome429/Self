package com.cathaypacific.mmbbizrule.dto.response.passengerdetails;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class TransferDetailDTO implements Serializable{

	private static final long serialVersionUID = 2594021549006349737L;

	@ApiModelProperty("Whether need to be transfered")
	private Boolean needTransfer;
	
	@ApiModelProperty("Whether there's any sector failed to transfer the info")
	private Boolean anySectorFail;

	public Boolean getNeedTransfer() {
		return needTransfer;
	}

	public void setNeedTransfer(Boolean needTransfer) {
		this.needTransfer = needTransfer;
	}

	public Boolean getAnySectorFail() {
		return anySectorFail;
	}

	public void setAnySectorFail(Boolean anySectorFail) {
		this.anySectorFail = anySectorFail;
	}
}
