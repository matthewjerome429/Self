package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RetrievePnrSeat {
	
	private RetrievePnrSeatDetail seatDetail;
	
	private List<RetrievePnrSeatDetail> extraSeats;
	
	private RetrievePnrSeatPreference preference;
	
	private BigInteger qulifierId;
	
	public RetrievePnrSeatDetail getSeatDetail() {
		return seatDetail;
	}
	public RetrievePnrSeatDetail findSeatDetail() {
		if(seatDetail == null){
			seatDetail = new RetrievePnrSeatDetail();
		}
		return seatDetail;
	}
	public void setSeatDetail(RetrievePnrSeatDetail seatDetail) {
		this.seatDetail = seatDetail;
	}
	public RetrievePnrSeatPreference getPreference() {
		return preference;
	}
	public RetrievePnrSeatPreference findPreference() {
		if(preference == null){
			preference = new RetrievePnrSeatPreference();
		}
		return preference;
	}
	public void setPreference(RetrievePnrSeatPreference preference) {
		this.preference = preference;
	}
	public BigInteger getQulifierId() {
		return qulifierId;
	}
	public void setQulifierId(BigInteger qulifierId) {
		this.qulifierId = qulifierId;
	}
	public List<RetrievePnrSeatDetail> getExtraSeats() {
		return extraSeats;
	}
	public List<RetrievePnrSeatDetail> findExtraSeats() {
		if(extraSeats == null){
			extraSeats = new ArrayList<>();
		}
		return extraSeats;
	}
	public void setExtraSeats(List<RetrievePnrSeatDetail> extraSeats) {
		this.extraSeats = extraSeats;
	}
	
}
