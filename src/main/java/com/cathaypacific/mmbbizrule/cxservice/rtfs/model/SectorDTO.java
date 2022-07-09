package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SectorDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8680410679027127761L;

	/** The code share flight numbers. */
	private List<Flight> codeShareFlights;
	/** codeShareNature. */
	private CodeShareNature codeShareNature;
	/** The origin. */
	private String origin;
	/** The destination. */
	private String destination;
	/** The next update. */
	private Date nextUpdate;
	/** The is provisional. */
	private Boolean isProvisional;
	/** The depart scheduled. */
	private Date departScheduled;
	/** The depart estimated. */
	private Date departEstimated;
	/** The depart actual. */
	private Date departActual;
	/** The arrival scheduled. */
	private Date arrivalScheduled;
	/** The arrival estimated. */
	private Date arrivalEstimated;
	/** The arrival actual. */
	private Date arrivalActual;
	/** The flight status. */
	private String flightStatus;
	/** The flight scenario ID. */
	private Integer scenarioID;
	/** The advice to passengers. */
	private List<AdviceToPassengers> adviceToPassengers;
	/** Sequence id **/
	private Integer sequenceId;
	/** The facilities to flights. */
	private List<Facilities> facilities;
	/** the gateNumber for flight **/
	private String gateNumber;
	
	/**
	 * Gets scenario ID.
	 * 
	 * @return the scenarioID
	 */
	public Integer getScenarioID() {
		return scenarioID;
	}

	/**
	 * Sets scenario ID.
	 * 
	 * @param scenarioID
	 *            the scenarioID to set
	 */
	public void setScenarioID(Integer scenarioID) {
		this.scenarioID = scenarioID;
	}

	/**
	 * Gets the code share flight numbers.
	 *
	 * @return the code share flight numbers
	 */
	public final List<Flight> getCodeShareFlights() {
		return codeShareFlights;
	}

	/**
	 * Sets the code share flight numbers.
	 *
	 * @param aCodeShareFlNmbrs
	 *            the new code share flight numbers
	 */
	public final void setCodeShareFlights(final List<Flight> aCodeShareFlNmbrs) {
		if (codeShareFlights == null) {
			codeShareFlights = new ArrayList<>();
		}
		this.codeShareFlights = aCodeShareFlNmbrs;
	}

	/**
	 * Adds the codeShareFlight.
	 *
	 * @param inCodeShareFlights
	 *            the in destination
	 */
	public void addCodeShareFlights(final Flight inCodeShareFlights) {
		this.codeShareFlights.add(inCodeShareFlights);
	}

	/**
	 * Gets the code share nature.
	 *
	 * @return the codeShareNature
	 */
	public CodeShareNature getCodeShareNature() {
		return codeShareNature;
	}

	/**
	 * Sets the code share nature.
	 *
	 * @param inCodeShareNature
	 *            the codeShareNature to set
	 */
	public void setCodeShareNature(final CodeShareNature inCodeShareNature) {
		this.codeShareNature = inCodeShareNature;
	}

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public final String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 *
	 * @param aOrigin
	 *            the new origin
	 */
	public final void setOrigin(final String aOrigin) {
		this.origin = aOrigin;
	}

	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public final String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param aDest
	 *            the new destination
	 */
	public final void setDestination(final String aDest) {
		this.destination = aDest;
	}

	/**
	 * Gets the next update.
	 *
	 * @return the nextUpdate
	 */
	public final Date getNextUpdate() {
		if (nextUpdate == null) {
			return null;
		} else {
			return new Date(nextUpdate.getTime());
		}
	}

	/**
	 * Sets the next update.
	 *
	 * @param inNextUpdate
	 *            the nextUpdate to set
	 */
	public final void setNextUpdate(final Date inNextUpdate) {
		this.nextUpdate = inNextUpdate;
	}

	/**
	 * Sets the checks if is provisional.
	 *
	 * @param isaProvisional
	 *            the isProvisional to set
	 */
	public void setIsProvisional(final Boolean isaProvisional) {
		this.isProvisional = isaProvisional;
	}

	/**
	 * Gets the checks if is provisional.
	 *
	 * @return the isProvisional
	 */
	public Boolean getIsProvisional() {
		return isProvisional;
	}

	/**
	 * Gets the depart estimated.
	 *
	 * @return the departEstimated
	 */
	public final Date getDepartEstimated() {
		if (departEstimated == null) {
			return null;
		} else {
			return new Date(departEstimated.getTime());
		}
	}

	/**
	 * Sets the depart estimated.
	 *
	 * @param inDepartEstimated
	 *            the departEstimated to set
	 */
	public final void setDepartEstimated(final Date inDepartEstimated) {
		this.departEstimated = inDepartEstimated;
	}

	/**
	 * Gets the arrival estimated.
	 *
	 * @return the arrivalEstimated
	 */
	public final Date getArrivalEstimated() {
		if (arrivalEstimated == null) {
			return null;
		} else {
			return new Date(arrivalEstimated.getTime());
		}
	}

	/**
	 * Sets the arrival estimated.
	 *
	 * @param inArrivalEstimated
	 *            the arrivalEstimated to set
	 */
	public final void setArrivalEstimated(final Date inArrivalEstimated) {
		this.arrivalEstimated = inArrivalEstimated;
	}

	/**
	 * Gets the depart scheduled.
	 *
	 * @return the depart scheduled
	 */
	public final Date getDepartScheduled() {
		if (departScheduled == null) {
			return null;
		} else {
			return new Date(departScheduled.getTime());
		}
	}

	/**
	 * Sets the depart scheduled.
	 *
	 * @param aDepSch
	 *            the new depart scheduled
	 */
	public final void setDepartScheduled(final Date aDepSch) {
		if (aDepSch != null) {
			this.departScheduled = new Date(aDepSch.getTime());
		}
	}

	/**
	 * Gets the depart actual.
	 *
	 * @return the depart actual
	 */
	public final Date getDepartActual() {
		if (departActual == null) {
			return null;
		} else {
			return new Date(departActual.getTime());
		}
	}

	/**
	 * Sets the depart actual.
	 *
	 * @param aDepAct
	 *            the new depart actual
	 */
	public final void setDepartActual(final Date aDepAct) {
		if (aDepAct != null) {
			this.departActual = new Date(aDepAct.getTime());
		}
	}

	/**
	 * Gets the arrival scheduled.
	 *
	 * @return the arrival scheduled
	 */
	public final Date getArrivalScheduled() {
		if (arrivalScheduled == null) {
			return null;
		} else {
			return new Date(arrivalScheduled.getTime());
		}
	}

	/**
	 * Sets the arrival scheduled.
	 *
	 * @param aArrSch
	 *            the new arrival scheduled
	 */
	public final void setArrivalScheduled(final Date aArrSch) {
		if (aArrSch != null) {
			this.arrivalScheduled = new Date(aArrSch.getTime());
		}
	}

	/**
	 * Gets the arrival actual.
	 *
	 * @return the arrival actual
	 */
	public final Date getArrivalActual() {
		if (arrivalActual == null) {
			return null;
		} else {
			return new Date(arrivalActual.getTime());
		}
	}

	/**
	 * Sets the arrival actual.
	 *
	 * @param aArrAct
	 *            the new arrival actual
	 */
	public final void setArrivalActual(final Date aArrAct) {
		if (aArrAct != null) {
			this.arrivalActual = new Date(aArrAct.getTime());
		}
	}

	/**
	 * Gets the flight status.
	 *
	 * @return the flight status
	 */
	public final String getFlightStatus() {
		return flightStatus;
	}

	/**
	 * Sets the flight status.
	 *
	 * @param aFlightStatus
	 *            the new flight status
	 */
	public final void setFlightStatus(final String aFlightStatus) {
		this.flightStatus = aFlightStatus;
	}

	/**
	 * Gets the advice to passengers.
	 *
	 * @return the advice to passengers
	 */
	public final List<AdviceToPassengers> getAdviceToPassengers() {
		if (adviceToPassengers == null) {
			adviceToPassengers = new ArrayList<>();
		}
		return adviceToPassengers;
	}

	/**
	 * Sets the advice to passengers.
	 *
	 * @param aAdvToPassengers
	 *            the new advice to passengers
	 */
	public final void setAdviceToPassengers(final List<AdviceToPassengers> aAdvToPassengers) {
		this.adviceToPassengers = aAdvToPassengers;
	}

	/**
	 * Gets the sequence id
	 * 
	 * @return the sequence id
	 */
	public Integer getSequenceId() {
		return sequenceId;
	}

	/**
	 * Set the sequence id
	 * 
	 * @param the
	 *            sequence id
	 */
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the facilities
	 * 
	 * @return the facilities
	 */
	public List<Facilities> getFacilities() {
		return facilities;
	}

	/**
	 * Set the facilities
	 * 
	 * @param the
	 *            facilities
	 */
	public void setFacilities(List<Facilities> facilities) {
		this.facilities = facilities;
	}


	/**
	 *
	 * @return gateNumber
	 */
	public String getGateNumber() {
		return gateNumber;
	}

	/**
	 *
	 * @param gateNumber
	 */
	public void setGateNumber(String gateNumber) {
		this.gateNumber = gateNumber;
	}
}
