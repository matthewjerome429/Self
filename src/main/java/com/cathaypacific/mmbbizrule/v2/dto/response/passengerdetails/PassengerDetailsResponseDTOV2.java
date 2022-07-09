package com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails;

import java.io.Serializable;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck.RegCheckCprJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.passengerdetails.ProductLevelTransferDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;

import io.swagger.annotations.ApiModelProperty;

public class PassengerDetailsResponseDTOV2  extends BaseResponseDTO implements Serializable{
	
	private static final long serialVersionUID = -5598132353727356851L;
	
	private FlightBookingDTOV2 booking;
	
	private RegCheckCprJourneyDTO regulatoryCheck;
	
	@ApiModelProperty("The trasnfer information")
	private ProductLevelTransferDTO productLevelTransfer;

	public FlightBookingDTOV2 getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTOV2 booking) {
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

	public RegCheckCprJourneyDTO getRegulatoryCheck() {
		return regulatoryCheck;
	}

	public void setRegulatoryCheck(RegCheckCprJourneyDTO regulatoryCheck) {
		this.regulatoryCheck = regulatoryCheck;
	}
}
