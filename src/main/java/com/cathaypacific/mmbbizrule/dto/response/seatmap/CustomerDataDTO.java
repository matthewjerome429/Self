package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class CustomerDataDTO {
	
	private List<String> passengerIds;
	
	private List<SeatPriceDTO> seatPrices;

	public List<String> getPassengerIds() {
		return passengerIds;
	}

	public void setPassengerIds(List<String> passengerIds) {
		this.passengerIds = passengerIds;
	}

	public List<SeatPriceDTO> getSeatPrices() {
		return seatPrices;
	}

	public void setSeatPrices(List<SeatPriceDTO> seatPrices) {
		this.seatPrices = seatPrices;
	}
	
}
