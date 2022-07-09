package com.cathaypacific.mmbbizrule.cxservice.rtfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class FlightStatusResponseDTO implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 5165883697918294136L;

	/** The last update GMT. */
	private Date lastUpdateGMT;
	/** The result. */
	private List<FlightStatusData> result;
	/** The errors. */
	private List<ErrorInfo> errors;

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public final List<ErrorInfo> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errs
	 *            the errors to set
	 */
	public final void setErrors(final List<ErrorInfo> errs) {
		this.errors = errs;
	}

	/**
	 * Gets the last update GMT.
	 *
	 * @return the lastUpdateGMT
	 */
	public final Date getLastUpdateGMT() {
		if (lastUpdateGMT != null) {
			return new Date(lastUpdateGMT.getTime());
		} else {
			return null;
		}
	}

	/**
	 * Sets the last update GMT.
	 *
	 * @param aLastUpdGMT
	 *            the lastUpdateGMT to set
	 */
	public final void setLastUpdateGMT(final Date aLastUpdGMT) {
		if (aLastUpdGMT != null) {
			this.lastUpdateGMT = new Date(aLastUpdGMT.getTime());
		}
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public final List<FlightStatusData> getResult() {
		if (result == null) {
			result = new ArrayList<>();
		}
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result
	 *            the new flight status data values
	 */
	public final void setResult(final List<FlightStatusData> result) {
		this.result = result;
	}
}
