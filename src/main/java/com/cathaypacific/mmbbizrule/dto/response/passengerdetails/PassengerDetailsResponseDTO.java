package com.cathaypacific.mmbbizrule.dto.response.passengerdetails;

import java.io.Serializable;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;

import io.swagger.annotations.ApiModelProperty;

public class PassengerDetailsResponseDTO  extends BaseResponseDTO implements Serializable{
	
	private static final long serialVersionUID = -5598132353727356851L;
	
	private FlightBookingDTO booking;
	
	@ApiModelProperty("The trasnfer information")
	private ProductLevelTransferDTO productLevelTransfer;

	public FlightBookingDTO getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTO booking) {
		this.booking = booking;
	}

	public ProductLevelTransferDTO getProductLevelTransfer() {
		return productLevelTransfer;
	}
	
	public ProductLevelTransferDTO findProductLevelTransfer() {
		if (productLevelTransfer == null) {
			productLevelTransfer = new ProductLevelTransferDTO();
		}
		return productLevelTransfer;
	}

	public void setProductLevelTransfer(ProductLevelTransferDTO productLevelTransfer) {
		this.productLevelTransfer = productLevelTransfer;
	}
	
}
