package com.cathaypacific.mmbbizrule.v2.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails.PassengerDetailsResponseDTOV2;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to add passenger info of PNR
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:19:37 PM
 * @version V1.0
 */
public interface UpdatePassengerServiceV2 {

	/**
	 * add passenger info
	 * @param request
	 * @param pnrBooking
	 * @param booking
	 * @param ffpTransferStatus
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	public RetrievePnrBooking updatePassenger(UpdatePassengerDetailsRequestDTOV2 request,RetrievePnrBooking pnrBooking,
			Booking booking, TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus, PassengerDetailsResponseDTOV2 response) throws BusinessBaseException;
	
	class TransferStatus{
		
		private boolean needTransfer;
		
		private boolean anyTransferFailed;

		public boolean isNeedTransfer() {
			return needTransfer;
		}

		public void setNeedTransfer(boolean needTransfer) {
			this.needTransfer = needTransfer;
		}

		public boolean isAnyTransferFailed() {
			return anyTransferFailed;
		}

		public void setAnyTransferFailed(boolean anyTransferFailed) {
			this.anyTransferFailed = anyTransferFailed;
		}
	}
}
