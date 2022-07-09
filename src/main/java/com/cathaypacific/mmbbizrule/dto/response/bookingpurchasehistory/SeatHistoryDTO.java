package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SeatHistoryDTO implements Serializable{

	private static final long serialVersionUID = 1591121454018831974L;
	@ApiModelProperty(value = "seat details", required = true)
	private List<SeatDetailDTO> seats;
	@ApiModelProperty(value = "fare info (tax included)", required = true)
	private PriceDTO fare;
	@ApiModelProperty(value = "tax info")
	private PriceDTO tax;

	public List<SeatDetailDTO> getSeats() {
		return seats;
	}
	
	public List<SeatDetailDTO> findSeats() {
		if (seats == null) {
			seats = new ArrayList<>();
		}
		return seats;
	}

	public void setSeats(List<SeatDetailDTO> seats) {
		this.seats = seats;
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
