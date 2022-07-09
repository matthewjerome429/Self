package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.enums.PurchaseProductTypeEnum;

public class TicketProcessCouponGroup {

	private List<TicketProcessFlightInfo> flightInfos;
	
	private List<TicketProcessBaggageAllowance> baggageAllowances;
	
	private PurchaseProductTypeEnum purchaseProductType;
	
	private List<TicketProcessCouponInfo> couponInfos;
	
	private TicketProcessPricingInfo pricingInfo;

	public List<TicketProcessFlightInfo> getFlightInfos() {
		return flightInfos;
	}
	
	public List<TicketProcessFlightInfo> findFlightInfos() {
		if(flightInfos == null){
			flightInfos = new ArrayList<>();
		}
		return flightInfos;
	}

	public void setFlightInfos(List<TicketProcessFlightInfo> flightInfos) {
		this.flightInfos = flightInfos;
	}

	public List<TicketProcessBaggageAllowance> getBaggageAllowances() {
		return baggageAllowances;
	}
	
	public List<TicketProcessBaggageAllowance> findBaggageAllowances() {
		if(baggageAllowances == null){
			baggageAllowances = new ArrayList<>();
		}
		return baggageAllowances;
	}

	public void setBaggageAllowances(List<TicketProcessBaggageAllowance> baggageAllowances) {
		this.baggageAllowances = baggageAllowances;
	}

	public PurchaseProductTypeEnum getPurchaseProductType() {
		return purchaseProductType;
	}

	public void setPurchaseProductType(PurchaseProductTypeEnum purchaseProductType) {
		this.purchaseProductType = purchaseProductType;
	}

	public List<TicketProcessCouponInfo> getCouponInfos() {
		return couponInfos;
	}
	
	public List<TicketProcessCouponInfo> findCouponInfos() {
		if(couponInfos == null){
			couponInfos = new ArrayList<>();
		}
		return couponInfos;
	}

	public void setCouponInfos(List<TicketProcessCouponInfo> couponInfos) {
		this.couponInfos = couponInfos;
	}

	public TicketProcessPricingInfo getPricingInfo() {
		return pricingInfo;
	}

	public void setPricingInfo(TicketProcessPricingInfo pricingInfo) {
		this.pricingInfo = pricingInfo;
	}
	
}
