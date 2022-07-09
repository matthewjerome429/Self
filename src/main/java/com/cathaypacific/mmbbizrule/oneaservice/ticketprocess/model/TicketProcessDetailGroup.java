package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

import java.util.ArrayList;
import java.util.List;

public class TicketProcessDetailGroup {

	private String eticket;
	
	private List<TicketProcessCouponGroup> couponGroups;

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public List<TicketProcessCouponGroup> getCouponGroups() {
		return couponGroups;
	}
	
	public List<TicketProcessCouponGroup> findCouponGroups() {
		if(couponGroups == null){
			couponGroups = new ArrayList<>();
		}
		return couponGroups;
	}

	public void setCouponGroups(List<TicketProcessCouponGroup> couponGroups) {
		this.couponGroups = couponGroups;
	}

}
