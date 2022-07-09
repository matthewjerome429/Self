package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class Lounge implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -4517640394937832574L;

	/** Lounge Name */
	private String name;

	/** Lounge Access Class */
	private String accessClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessClass() {
		return accessClass;
	}

	public void setAccessClass(String accessClass) {
		this.accessClass = accessClass;
	}
}
