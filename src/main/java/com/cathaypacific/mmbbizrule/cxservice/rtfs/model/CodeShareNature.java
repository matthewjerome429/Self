package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;

public class CodeShareNature implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 4812923712223444404L;

	/** code share nature. */
	private String nature;

	/** leaseFromCarrierCode. */
	private String leaseFromCarrierCode;

	/** The lease to carrier code. */
	private String leaseToCarrierCode;

	/**
	 * Gets the nature.
	 *
	 * @return the nature
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * Sets the nature.
	 *
	 * @param inNature
	 *            the nature to set
	 */
	public void setNature(final String inNature) {
		this.nature = inNature;
	}

	/**
	 * Gets the lease from carrier code.
	 *
	 * @return the leaseFromCarrierCode
	 */
	public String getLeaseFromCarrierCode() {
		return leaseFromCarrierCode;
	}

	/**
	 * Sets the lease from carrier code.
	 *
	 * @param inLeaseFromCarrierCode
	 *            the leaseFromCarrierCode to set
	 */
	public void setLeaseFromCarrierCode(final String inLeaseFromCarrierCode) {
		this.leaseFromCarrierCode = inLeaseFromCarrierCode;
	}

	/**
	 * Gets the lease to carrier code.
	 *
	 * @return the leaseToCarrierCode
	 */
	public String getLeaseToCarrierCode() {
		return leaseToCarrierCode;
	}

	/**
	 * Sets the lease to carrier code.
	 *
	 * @param inLeaseToCarrierCode
	 *            the leaseToCarrierCode to set
	 */
	public void setLeaseToCarrierCode(final String inLeaseToCarrierCode) {
		this.leaseToCarrierCode = inLeaseToCarrierCode;
	}

}
