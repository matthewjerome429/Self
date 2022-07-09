package com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class BookingCancelCheckResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 8579872738068576208L;

	/** The reasons of block cancel */
	private List<ErrorInfo> blockReason;
	
	/** the reasons of cannot refund */
	private List<ErrorInfo> cantRefundReason;
	
	/** the flow  of cancel booking */
	private CancelFlow suggestedCancelflow;
	
	/** redemption booking identifier*/
	private boolean redemptionBooking;
	
	/** Any passenger checked in */
	private boolean hasCheckedInPassenger;
	
	/** Available to cancel booking */
	private boolean canCancel = false;
	
	/** Available to refund */
	private boolean canRefund = false;
	
	/** BOH booking identifier*/
	private boolean isBohBooking;
	
	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public List<ErrorInfo> getBlockReason() {
		return blockReason;
	}

	public void setBlockReason(List<ErrorInfo> blockReason) {
		this.blockReason = blockReason;
	}
	
	public void addBlocReason(ErrorInfo errorInfo){
		if(blockReason == null){
			blockReason = new ArrayList<>();
		}
		blockReason.add(errorInfo);
	}

	public boolean isCanRefund() {
		return canRefund;
	}

	public void setCanRefund(boolean canRefund) {
		this.canRefund = canRefund;
	}

	public List<ErrorInfo> getCantRefundReason() {
		return cantRefundReason;
	}

	public void setCantRefundReason(List<ErrorInfo> cantRefundReason) {
		this.cantRefundReason = cantRefundReason;
	}
	
	public void addCantRefundReason(ErrorInfo errorInfo){
		if(cantRefundReason == null){
			cantRefundReason = new ArrayList<>();
		}
		cantRefundReason.add(errorInfo);
	}

	public CancelFlow getSuggestedCancelflow() {
		return suggestedCancelflow;
	}

	public void setSuggestedCancelflow(CancelFlow suggestedCancelflow) {
		this.suggestedCancelflow = suggestedCancelflow;
	}

	public boolean isRedemptionBooking() {
		return redemptionBooking;
	}

	public void setRedemptionBooking(boolean redemptionBooking) {
		this.redemptionBooking = redemptionBooking;
	}

	public boolean isHasCheckedInPassenger() {
		return hasCheckedInPassenger;
	}

	public void setHasCheckedInPassenger(boolean hasCheckedInPassenger) {
		this.hasCheckedInPassenger = hasCheckedInPassenger;
	}

	public boolean isBohBooking() {
		return isBohBooking;
	}

	public void setBohBooking(boolean isBohBooking) {
		this.isBohBooking = isBohBooking;
	}

}
