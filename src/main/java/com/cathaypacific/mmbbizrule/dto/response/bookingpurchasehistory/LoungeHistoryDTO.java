package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class LoungeHistoryDTO implements Serializable {

	private static final long serialVersionUID = -3710232246255458395L;

	@ApiModelProperty(value = "lounge info details", required = true)
	private List<LoungeInfoDTO> loungeInfos;

	@ApiModelProperty(value = "fare info (tax included)", required = true)
	private PriceDTO fare;

	@ApiModelProperty(value = "tax info")
	private PriceDTO tax;

	public List<LoungeInfoDTO> getLoungeInfos() {
		return loungeInfos;
	}

	public void setLoungeInfos(List<LoungeInfoDTO> loungeInfos) {
		this.loungeInfos = loungeInfos;
	}

	public List<LoungeInfoDTO> findLoungeInfos() {
		if (loungeInfos == null) {
			loungeInfos = new ArrayList<>();
		}
		return loungeInfos;
	}

	public PriceDTO getFare() {
		return fare;
	}

	public PriceDTO findFare() {
		if (fare == null) {
			fare = new PriceDTO();
		}
		return fare;
	}

	public void setFare(PriceDTO fare) {
		this.fare = fare;
	}

	public PriceDTO getTax() {
		return tax;
	}

	public PriceDTO findTax() {
		if (tax == null) {
			tax = new PriceDTO();
		}
		return tax;
	}

	public void setTax(PriceDTO tax) {
		this.tax = tax;
	}

}
