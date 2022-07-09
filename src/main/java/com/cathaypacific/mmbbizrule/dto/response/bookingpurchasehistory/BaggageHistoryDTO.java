package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class BaggageHistoryDTO implements Serializable{
	
	private static final long serialVersionUID = 3823007620250356512L;
	@ApiModelProperty(value = "baggage info details", required = true)
	private List<BaggageInfoDTO> baggageInfos;
	@ApiModelProperty(value = "fare info (tax included)", required = true)
	private PriceDTO fare;
	@ApiModelProperty(value = "tax info")
	private PriceDTO tax;

	public List<BaggageInfoDTO> getBaggageInfos() {
		return baggageInfos;
	}
	
	public List<BaggageInfoDTO> findBaggageInfos() {
		if(baggageInfos == null) {
			baggageInfos = new ArrayList<>();
		}
		return baggageInfos;
	}

	public void setBaggageInfos(List<BaggageInfoDTO> baggageInfos) {
		this.baggageInfos = baggageInfos;
	}

	public PriceDTO getFare() {
		return fare;
	}
	
	public PriceDTO findFare() {
		if(fare == null) {
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
		if(tax == null) {
			tax = new PriceDTO();
		}
		return tax;
	}

	public void setTax(PriceDTO tax) {
		this.tax = tax;
	}
}
