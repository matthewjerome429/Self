package com.cathaypacific.mmbbizrule.cxservice.mlc.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents an UserSession object from MLC.
 */
@XmlRootElement(name = "MLCSession")
public class MLCSession implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4777659381017731436L;
	/**
	 * Identifies token status.
	 */
	private Integer statusCode;
	/**
	 * Identifies user sub session object.
	 */
	private Session session;

	/**
	 * Returns the UserSubSession object.
	 * 
	 * @return UserSubSession MLCSubSession.
	 */
	public final Session getSession() {
		return this.session;
	}

	/**
	 * Sets the value of session.
	 * 
	 * session sess
	 *            the UserSubSession object.
	 */
	public final void setSession(final Session session) {
		this.session = session;
	}

	/**
	 * Returns the status.
	 * @return
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the value of login status received from MLC.
	 * @param statusCode
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}
