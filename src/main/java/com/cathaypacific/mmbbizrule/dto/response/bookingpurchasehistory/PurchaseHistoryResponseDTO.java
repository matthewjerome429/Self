package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import io.swagger.annotations.ApiModelProperty;

public class PurchaseHistoryResponseDTO extends BaseResponseDTO{
	
	private static final long serialVersionUID = 7826881879774774340L;

	@ApiModelProperty(value = "passenger info list", required = true)
	private List<PassengerInfoDTO> passengers;
	@ApiModelProperty(value = "segment info list", required = true)
	private List<SegmentInfoDTO> segments;
	@ApiModelProperty(value = "purchase history list grouped by date", required = true)
	private List<DailyHistoryDTO> purchaseHistory;

	public List<PassengerInfoDTO> getPassengers() {
		return passengers;
	}
	
	public List<PassengerInfoDTO> findPassengers() {
		if (passengers == null) {
			passengers = new ArrayList<>();
		}
		return passengers;
	}

	public void setPassengers(List<PassengerInfoDTO> passengers) {
		this.passengers = passengers;
	}

	public List<SegmentInfoDTO> getSegments() {
		return segments;
	}
	
	public List<SegmentInfoDTO> findSegments() {
		if(segments == null) {
			segments = new ArrayList<>();
		}
		return segments;
	}

	public void setSegments(List<SegmentInfoDTO> segments) {
		this.segments = segments;
	}

	public List<DailyHistoryDTO> getPurchaseHistory() {
		return purchaseHistory;
	}
	
	public List<DailyHistoryDTO> findPurchaseHistory() {
		if(purchaseHistory == null) {
			purchaseHistory = new ArrayList<>();
		}
		return purchaseHistory;
	}

	public void setPurchaseHistory(List<DailyHistoryDTO> purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}
}
