package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;

public class SeatDetail implements Cloneable{
	/** seat number */
	private String seatNo;

	/** seat status */
	private String paymentStatus;
	
	/** if it is a extra legroom seat  */
	private Boolean exlSeat;
	
	/** if it is a regular seat */
	private Boolean asrSeat;
	
	/**if the seat is a window seat */
	private Boolean windowSeat;
	
	/** if is an aisle seat */
	private Boolean aisleSeat;
	
	private Boolean paid;
	
	/** seat status */
	private String status;
	
	private BigInteger qulifierId;
	
	private RetrievePnrPaymentInfo paymentInfo;
	
	private boolean isFromDCS;
	
	private String crossRef;
	
	/** if it is a temporary seat stored in cache; this is for the OLCI checking in case*/
	private boolean tempSeat;
	
	public SeatDetail() {
		this.paid = false;
	}
	
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	public Boolean isExlSeat() {
		return exlSeat;
	}
	public void setExlSeat(Boolean exlSeat) {
		this.exlSeat = exlSeat;
	}
	public Boolean isAsrSeat() {
		return asrSeat;
	}

	public void setAsrSeat(Boolean asrSeat) {
		this.asrSeat = asrSeat;
	}

	public Boolean isWindowSeat() {
		return windowSeat;
	}

	public void setWindowSeat(Boolean windowSeat) {
		this.windowSeat = windowSeat;
	}

	public Boolean isAisleSeat() {
		return aisleSeat;
	}

	public void setAisleSeat(Boolean aisleSeat) {
		this.aisleSeat = aisleSeat;
	}

	public Boolean isPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public BigInteger getQulifierId() {
		return qulifierId;
	}

	public void setQulifierId(BigInteger qulifierId) {
		this.qulifierId = qulifierId;
	}

	public boolean isFromDCS() {
		return isFromDCS;
	}

	public void setFromDCS(boolean isFromDCS) {
		this.isFromDCS = isFromDCS;
	}

	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getCrossRef() {
		return crossRef;
	}

	public void setCrossRef(String crossRef) {
		this.crossRef = crossRef;
	}

	public boolean isTempSeat() {
		return tempSeat;
	}

	public void setTempSeat(boolean tempSeat) {
		this.tempSeat = tempSeat;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object object =  super.clone();
		RetrievePnrPaymentInfo pi = ((SeatDetail)object).getPaymentInfo();
		if (pi != null) {
			((SeatDetail)object).setPaymentInfo((RetrievePnrPaymentInfo)pi.clone());
		}
		return object;
	}
	
}