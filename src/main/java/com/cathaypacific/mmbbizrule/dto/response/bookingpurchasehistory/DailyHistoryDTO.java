package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class DailyHistoryDTO implements Serializable{

	private static final long serialVersionUID = -873671047892842881L;
	@ApiModelProperty(value = "purchase history of seat")
	private List<SeatHistoryDTO> seatHistory;
	@ApiModelProperty(value = "purchase history of baggage")
	private List<BaggageHistoryDTO> baggageHistory;
	@ApiModelProperty(value = "purchase history of lounge")
	private List<LoungeHistoryDTO> loungeHistory;
	/** format: YYYY-MM-DD */
	@ApiModelProperty(value = "purchase date, format: YYYY-MM-DD", required = true)
	private String purchaseDate;

	public List<SeatHistoryDTO> getSeatHistory() {
		return seatHistory;
	}
	
	public List<SeatHistoryDTO> findSeatHistory() {
		if (seatHistory == null) {
			seatHistory = new ArrayList<>();
		}
		return seatHistory;
	}

	public void setSeatHistory(List<SeatHistoryDTO> seatHistory) {
		this.seatHistory = seatHistory;
	}

	public List<BaggageHistoryDTO> getBaggageHistory() {
		return baggageHistory;
	}

	public List<BaggageHistoryDTO> findBaggageHistory() {
		if (baggageHistory == null) {
			baggageHistory = new ArrayList<>();
		}
		return baggageHistory;
	}

	public void setBaggageHistory(List<BaggageHistoryDTO> baggageHistory) {
		this.baggageHistory = baggageHistory;
	}
	
	public List<LoungeHistoryDTO> getLoungeHistory() {
		return loungeHistory;
	}

	public void setLoungeHistory(List<LoungeHistoryDTO> loungeHistory) {
		this.loungeHistory = loungeHistory;
	}
	
	public List<LoungeHistoryDTO> findLoungeHistory() {
		if (loungeHistory == null) {
			loungeHistory = new ArrayList<>();
		}
		return loungeHistory;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
