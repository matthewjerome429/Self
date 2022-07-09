package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;

public class Facilities implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = -8010902248238116829L;

	/** The name. */
	private String name;

	/** The remarks. */
	private String remarks;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the remarks.
	 *
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Sets the remarks.
	 *
	 * @param remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
