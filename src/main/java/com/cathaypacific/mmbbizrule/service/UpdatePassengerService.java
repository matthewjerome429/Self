package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to add passenger info of PNR
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:19:37 PM
 * @version V1.0
 */
public interface UpdatePassengerService {

	/**
	 * add passenger info
	 * @param request
	 * @param pnrBooking
	 * @param booking
	 * @param ffpTransferStatus
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	public RetrievePnrBooking updatePassenger(UpdatePassengerDetailsRequestDTO request,RetrievePnrBooking pnrBooking, Booking booking, TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus) throws BusinessBaseException;
	
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
