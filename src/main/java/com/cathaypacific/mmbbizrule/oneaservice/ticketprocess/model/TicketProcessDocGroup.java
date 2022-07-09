package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

import java.util.ArrayList;
import java.util.List;

public class TicketProcessDocGroup {

	private List<TicketProcessRloc> rlocs;
	
	private List<TicketProcessDetailGroup> detailGroups;

	private TicketProcessOriginator originatorInfo;
	
	private TicketProcessPaxInfo paxInfo;
	
	private TicketProcessFareDetail fareInfo;
	
	private TicketProcessProductInfo productInfo;
	
	private TicketProcessFareDetail taxInfo;
	
	private TicketProcessSysProvider sysProvider;

	public List<TicketProcessRloc> getRlocs() {
		return rlocs;
	}
	
	public List<TicketProcessRloc> findRlocs() {
		if(rlocs == null){
			rlocs = new ArrayList<>();
		}
		return rlocs;
	}

	public void setRlocs(List<TicketProcessRloc> rlocs) {
		this.rlocs = rlocs;
	}

	public List<TicketProcessDetailGroup> getDetailInfos() {
		return detailGroups;
	}

	public List<TicketProcessDetailGroup> findDetailInfos() {
		if(detailGroups == null){
			detailGroups = new ArrayList<>();
		}
		return detailGroups;
	}

	public void setDetailInfos(List<TicketProcessDetailGroup> detailInfos) {
		this.detailGroups = detailInfos;
	}

	public TicketProcessOriginator getOriginatorInfo() {
		return originatorInfo;
	}

	public void setOriginatorInfo(TicketProcessOriginator originatorInfo) {
		this.originatorInfo = originatorInfo;
	}

	public TicketProcessPaxInfo getPaxInfo() {
		return paxInfo;
	}

	public void setPaxInfo(TicketProcessPaxInfo paxInfo) {
		this.paxInfo = paxInfo;
	}

	public TicketProcessFareDetail getFareInfo() {
		return fareInfo;
	}

	public void setFareInfo(TicketProcessFareDetail fareInfo) {
		this.fareInfo = fareInfo;
	}

	public TicketProcessProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(TicketProcessProductInfo productInfo) {
		this.productInfo = productInfo;
	}

	public TicketProcessFareDetail getTaxInfo() {
		return taxInfo;
	}

	public void setTaxInfo(TicketProcessFareDetail taxInfo) {
		this.taxInfo = taxInfo;
	}
	
	public String getOriginatorId() {
		if (originatorInfo != null && originatorInfo.getOriginIdentification() != null) {
			return originatorInfo.getOriginIdentification().getOriginatorId();
		}
		return null;
	}

	public TicketProcessSysProvider getSysProvider() {
		return sysProvider;
	}

	public void setSysProvider(TicketProcessSysProvider sysProvider) {
		this.sysProvider = sysProvider;
	}
	
}
