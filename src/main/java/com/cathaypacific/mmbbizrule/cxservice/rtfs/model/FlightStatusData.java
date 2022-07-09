package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightStatusData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1403976536386490732L;
	/** The operating flight number. */
	private Flight operatingFlight;
	/** The origin. */
	private String origin;
	/** The destination. */
	private String destination;

	/** The depart scheduled. */
	private Date departScheduled;
	/** The arrival scheduled. */
	private Date arrivalScheduled;

	/** The sectors. */
	private List<SectorDTO> sectors;

	/**
	 * Gets the operating flight number.
	 *
	 * @return the operating flight number
	 */
	public final Flight getOperatingFlight() {
		return operatingFlight;
	}

	/**
	 * Sets the operating flight number.
	 *
	 * @param aOperFlNmbr
	 *            the new operating flight number
	 */
	public final void setOperatingFlight(final Flight aOperFlNmbr) {
		this.operatingFlight = aOperFlNmbr;
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
	 * Gets the sectors.
	 *
	 * @return the sectors
	 */
	public List<SectorDTO> getSectors() {
		if (sectors == null) {
			sectors = new ArrayList<>();
		}
		return sectors;
	}

	/**
	 * Sets the sectors.
	 *
	 * @param inSectors
	 *            the new sectors
	 */
	public void setSectors(final List<SectorDTO> inSectors) {
		this.sectors=inSectors;
	}
}
