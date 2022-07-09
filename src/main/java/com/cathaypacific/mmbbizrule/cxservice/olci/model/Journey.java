package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mmbbizrule.util.JourneyUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Journey implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -4141751949680591869L;

	/**
	 * journey Id
	 */
	private String journeyId;

	/** passengers */
	private List<Passenger> passengers;
	
	/** The Indicate: all primary flights of this journey is  between Check in window Flight Close Time.*/
	private boolean betweenChekinFlightCloseTime;
	
	@JsonIgnore
	private boolean displayOnly;
	
	@JsonIgnore
	public String getLoginPaxCprRloc() {
		if (!CollectionUtils.isEmpty(this.passengers)) {
			Passenger pax = this.passengers.stream().filter(Passenger::getLoginPax).findFirst().orElse(null);
			if (pax != null) {
				return pax.getRloc();
			}
		}
		return null;
	}

	@JsonIgnore
	public String getLoginPaxDisplayRloc() {
		if (!CollectionUtils.isEmpty(this.passengers)) {
			Passenger pax = this.passengers.stream().filter(Passenger::getLoginPax).findFirst().orElse(null);
			if (pax != null) {
				return StringUtils.isEmpty(pax.getDisplayRloc()) ? pax.getRloc() : pax.getDisplayRloc();
			}
		}
		return null;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}
	

	public boolean isBetweenChekinFlightCloseTime() {
		return betweenChekinFlightCloseTime;
	}

	public void setBetweenChekinFlightCloseTime(boolean betweenChekinFlightCloseTime) {
		this.betweenChekinFlightCloseTime = betweenChekinFlightCloseTime;
	}

	/**
	 * This method for quick get available passengers for do checkin.
	 * @return
	 */
	@JsonIgnore
	public List<Passenger> getCurrentAvailableChinkInPassengers() {
		List<Passenger> result;
		if (passengers == null) {
			result = null;
		} else if (passengers.isEmpty()) {
			result = new ArrayList<>();
		} else {
			result = passengers.stream().filter(pax -> !JourneyUtil.isDisplayOnly(pax)).collect(Collectors.toList());
		}
		return result;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}
	
	
}
