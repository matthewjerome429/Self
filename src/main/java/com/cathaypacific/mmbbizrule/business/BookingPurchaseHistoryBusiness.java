package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PurchaseHistoryResponseDTO;

public interface BookingPurchaseHistoryBusiness {
	
	public PurchaseHistoryResponseDTO retrieveHistroy(String oneARloc) throws BusinessBaseException;
}
