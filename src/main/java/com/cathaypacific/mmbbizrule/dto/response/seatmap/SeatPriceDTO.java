package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class SeatPriceDTO {

	private PriceDTO price;
	
	private List<RowDetailDTO> rowDetails;

	public PriceDTO getPrice() {
		return price;
	}

	public void setPrice(PriceDTO price) {
		this.price = price;
	}

	public List<RowDetailDTO> getRowDetails() {
		return rowDetails;
	}

	public void setRowDetails(List<RowDetailDTO> rowDetails) {
		this.rowDetails = rowDetails;
	}
}
