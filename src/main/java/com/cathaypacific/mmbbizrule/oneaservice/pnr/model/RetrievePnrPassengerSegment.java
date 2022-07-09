package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.ArrayList;
import java.util.List;

public class RetrievePnrPassengerSegment {

	private List<RetrievePnrEticket> etickets;

	private String passengerId;

	private String segmentId;

	private RetrievePnrSeat seat;

	private List<RetrievePnrBaggage> baggages;
	
	private RetrievePnrMeal meal;

	/** The primary travel documents. */
	private List<RetrievePnrTravelDoc> PriTravelDocs;
	
	/** The secondary travel documents. */
	private List<RetrievePnrTravelDoc> SecTravelDocs;
	
	/** The other travel documents.*/
	private List<RetrievePnrTravelDoc> othTravelDocs;
	
	/** The list of KTN.*/
	private List<RetrievePnrTravelDoc> ktns;
	
	/** The list of redress.*/
	private List<RetrievePnrTravelDoc> redresses;
	
	private List<RetrievePnrFFPInfo> fqtvInfos;
	
	private List<RetrievePnrFFPInfo> fqtrInfos;

	/** The destination address. */
	private List<RetrievePnrAddressDetails> desAddresses;
	
	/** The residence address. */
	private List<RetrievePnrAddressDetails> resAddresses;
	
	private List<RetrievePnrLoungeInfo> claimedLounges;
	
	private List<RetrievePnrLoungeInfo> purchasedLounges;
	
	/** The train pick up number */
	private String pickUpNumber;
	
	private RetrievePnrAirportUpgradeInfo airportUpgradeInfo;
	
	private RetrievePnrUpgradeProcessInfo upgradeProcessInfo;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public List<RetrievePnrEticket> getEtickets() {
		if(etickets == null){
			etickets = new ArrayList<>();
		}
		return etickets;
	}

	public void setEtickets(List<RetrievePnrEticket> etickets) {
		this.etickets = etickets;
	}

	public RetrievePnrSeat getSeat() {
		return seat;
	}
	
	public RetrievePnrSeat findSeat() {
		if(seat == null) {
			seat = new RetrievePnrSeat();
		}
		return seat;
	}

	public void setSeat(RetrievePnrSeat seat) {
		this.seat = seat;
	}
	
	public List<RetrievePnrFFPInfo> getFQTRInfos() {
		if(fqtrInfos == null){
			fqtrInfos = new ArrayList<>();
		}
		return fqtrInfos;
	}

	public void setFQTRInfos(List<RetrievePnrFFPInfo> fQTRInfos) {
		fqtrInfos = fQTRInfos;
	}

	public List<RetrievePnrFFPInfo> getFQTVInfos() {
		if(fqtvInfos == null){
			fqtvInfos = new ArrayList<>();
		}
		return fqtvInfos;
	}

	public void setFQTVInfos(List<RetrievePnrFFPInfo> fQTVInfos) {
		fqtvInfos = fQTVInfos;
	}
	
	public List<RetrievePnrTravelDoc> getPriTravelDocs() {
		if(PriTravelDocs == null) {
			PriTravelDocs = new ArrayList<>();
		}
		return PriTravelDocs;
	}
	
	public void setPriTravelDocs(List<RetrievePnrTravelDoc> priTravelDocs) {
		PriTravelDocs = priTravelDocs;
	}
	
	public List<RetrievePnrTravelDoc> getSecTravelDocs() {
		if(SecTravelDocs == null) {
			SecTravelDocs = new ArrayList<>();
		}
		return SecTravelDocs;
	}
	
	public void setSecTravelDocs(List<RetrievePnrTravelDoc> secTravelDocs) {
		SecTravelDocs = secTravelDocs;
	}

	public List<RetrievePnrTravelDoc> getKtns() {
		if(ktns == null) {
			ktns = new ArrayList<>();
		}
		return ktns;
	}

	public void setKtns(List<RetrievePnrTravelDoc> ktns) {
		this.ktns = ktns;
	}

	public List<RetrievePnrTravelDoc> getRedresses() {
		if(redresses == null) {
			redresses = new ArrayList<>();
		}
		return redresses;
	}

	public void setRedresses(List<RetrievePnrTravelDoc> redresses) {
		this.redresses = redresses;
	}

	public List<RetrievePnrAddressDetails> getDesAddresses() {
		if(desAddresses == null){
			desAddresses = new ArrayList<>();
		}
		return desAddresses;
	}

	public void setDesAddresses(List<RetrievePnrAddressDetails> desAddresses) {
		this.desAddresses = desAddresses;
	}

	public List<RetrievePnrAddressDetails> getResAddresses() {
		if(resAddresses == null){
			resAddresses = new ArrayList<>();
		}
		return resAddresses;
	}

	public void setResAddresses(List<RetrievePnrAddressDetails> resAddresseses) {
		this.resAddresses = resAddresseses;
	}

	/*
	public void addMeal(RetrievePnrMeal meal){
		if(meals == null){
			meals = new ArrayList<>();
		}
		meals.add(meal);
	}
	 */
 

	public RetrievePnrMeal getMeal() {
		return meal;
	}

	public List<RetrievePnrLoungeInfo> getClaimedLounges() {
		return claimedLounges;
	}

	public void setClaimedLounges(List<RetrievePnrLoungeInfo> claimedLounges) {
		this.claimedLounges = claimedLounges;
	}
	
	public void addClaimedLounge(RetrievePnrLoungeInfo claimedLounge) {
		if (claimedLounge == null) {
			return;
		}
		if (this.claimedLounges == null) {
			this.claimedLounges = new ArrayList<>();
		}
		claimedLounges.add(claimedLounge);
	}

	public List<RetrievePnrBaggage> getBaggages() {
		return baggages;
	}

	public void setBaggages(List<RetrievePnrBaggage> baggages) {
		this.baggages = baggages;
	}
	
	public void addBaggage(RetrievePnrBaggage baggage) {
		if (baggage == null) {
			return;
		}
		if (this.baggages == null) {
			this.baggages = new ArrayList<>();
		}
		baggages.add(baggage);
	}


	public void setMeal(RetrievePnrMeal meal) {
		this.meal = meal;
	}

	public List<RetrievePnrTravelDoc> getOthTravelDocs() {
		if(othTravelDocs == null) {
			othTravelDocs = new ArrayList<>();
		}
		return othTravelDocs;
	}

	public void setOthTravelDocs(List<RetrievePnrTravelDoc> othTravelDocs) {
		this.othTravelDocs = othTravelDocs;
	}

	public String getPickUpNumber() {
		return pickUpNumber;
	}

	public void setPickUpNumber(String pickUpNumber) {
		this.pickUpNumber = pickUpNumber;
	}

	public RetrievePnrAirportUpgradeInfo getAirportUpgradeInfo() {
		return airportUpgradeInfo;
	}
	
	public RetrievePnrAirportUpgradeInfo findAirportUpgradeInfo() {
		if(airportUpgradeInfo == null) {
			airportUpgradeInfo = new RetrievePnrAirportUpgradeInfo();
		}
		return airportUpgradeInfo;
	}

	public void setAirportUpgradeInfo(RetrievePnrAirportUpgradeInfo airportUpgradeInfo) {
		this.airportUpgradeInfo = airportUpgradeInfo;
	}

	public RetrievePnrUpgradeProcessInfo getUpgradeProcessInfo() {
		return upgradeProcessInfo;
	}

	public void setUpgradeProcessInfo(RetrievePnrUpgradeProcessInfo upgradeProcessInfo) {
		this.upgradeProcessInfo = upgradeProcessInfo;
	}

	public List<RetrievePnrLoungeInfo> getPurchasedLounges() {
		return purchasedLounges;
	}

	public void setPurchasedLounges(List<RetrievePnrLoungeInfo> purchasedLounges) {
		this.purchasedLounges = purchasedLounges;
	}
	
	public void addPurchasedLounge(RetrievePnrLoungeInfo purchasedLounge) {
		if (purchasedLounge == null) {
			return;
		}
		if (this.purchasedLounges == null) {
			this.purchasedLounges = new ArrayList<>();
		}
		purchasedLounges.add(purchasedLounge);
	}
	
}
