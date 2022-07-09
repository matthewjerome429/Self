package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrSegment {

    /** original flight Number */
	private String pnrOperateSegmentNumber;
    
    /** original flight Provider */
	private String pnrOperateCompany;
		
	private String segmentID;
	
	/** The origin. */
	private String originPort;
	
	/** The destination. */
	private String destPort;
	
	/** The detailed departure time. */
	private RetrievePnrDepartureArrivalTime departureTime;
	
	/** The detailed arrival time. */
	private RetrievePnrDepartureArrivalTime arrivalTime;

	/** code share flight Number */
	private String marketSegmentNumber;

	/** code share flight Provider */
	private String marketCompany;

	/** The Sub Class in booking of flight */
	private String subClass;
	
	/** The Sub Class in booking of market */
	private String marketSubClass;
	
	private List<String> status;

	private String originTerminal;

	private String destTerminal;

	private String airCraftType;
	
	private String trainReminder;
	
	private RetrievePnrRebookInfo rebookInfo;
	
	private String lineNumber;

	private RetrievePnrFFPInfo fqtu;
	
	private RetrievePnrUpgradeInfo upgradeInfo;
	public String getSegmentID() {
		return segmentID;
	}

	public void setSegmentID(String segmentID) {
		this.segmentID = segmentID;
	}

	public RetrievePnrDepartureArrivalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(RetrievePnrDepartureArrivalTime departureTime) {
		this.departureTime = departureTime;
	}

	public RetrievePnrDepartureArrivalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(RetrievePnrDepartureArrivalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getMarketSegmentNumber() {
		return marketSegmentNumber;
	}

	public void setMarketSegmentNumber(String marketSegmentNumber) {
		this.marketSegmentNumber = marketSegmentNumber;
	}

	public String getMarketCompany() {
		return marketCompany;
	}

	public void setMarketCompany(String marketCompany) {
		this.marketCompany = marketCompany;
	}


	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getSubClass() {
		return subClass;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}

	public String getOriginTerminal() {
		return originTerminal;
	}

	public void setOriginTerminal(String originTerminal) {
		this.originTerminal = originTerminal;
	}

	public String getDestTerminal() {
		return destTerminal;
	}

	public void setDestTerminal(String destTerminal) {
		this.destTerminal = destTerminal;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public String getAirCraftType() {
		return airCraftType;
	}

	public void setAirCraftType(String airCraftType) {
		this.airCraftType = airCraftType;
	}

	public String getTrainReminder() {
		return trainReminder;
	}

	public void setTrainReminder(String trainReminder) {
		this.trainReminder = trainReminder;
	}


	public String getPnrOperateSegmentNumber() {
		return pnrOperateSegmentNumber;
	}

	public void setPnrOperateSegmentNumber(String pnrOperateSegmentNumber) {
		this.pnrOperateSegmentNumber = pnrOperateSegmentNumber;
	}

	public String getPnrOperateCompany() {
		return pnrOperateCompany;
	}

	public void setPnrOperateCompany(String pnrOperateCompany) {
		this.pnrOperateCompany = pnrOperateCompany;
	}

	public String getMarketSubClass() {
		return marketSubClass;
	}

	public void setMarketSubClass(String marketSubClass) {
		this.marketSubClass = marketSubClass;
	}
	
	public RetrievePnrRebookInfo getRebookInfo() {
		return rebookInfo;
	}

	public void setRebookInfo(RetrievePnrRebookInfo rebookInfo) {
		this.rebookInfo = rebookInfo;
	}

	public RetrievePnrFFPInfo getFqtu() {
		return fqtu;
	}

	public void setFqtu(RetrievePnrFFPInfo fqtu) {
		this.fqtu = fqtu;
	}

	public RetrievePnrUpgradeInfo getUpgradeInfo() {
		return upgradeInfo;
	}

	public void setUpgradeInfo(RetrievePnrUpgradeInfo upgradeInfo) {
		this.upgradeInfo = upgradeInfo;
	}
	
	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public String getOriginalSubClass() {
		return this.upgradeInfo == null ? null:this.upgradeInfo.getFromSubClass();
	}

}
