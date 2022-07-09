package com.cathaypacific.mmbbizrule.cxservice.mlc.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


/**
 * Stores part of the User session details.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3987424390101857522L;
	/**
	 * Identifies the session ID.
	 */
	private Integer sessionID;
	/**
	 * Identifies the member id.
	 */
	private String memID;
	/**
	 * Identifies the user type.
	 */
	private String userType;
	/**
	 * Identifies the preLink.
	 */
	private String preLink;
	/**
	 * indicates user login.
	 */
	private String isJustLogin;

	/**
	 * Returns the session ID.
	 * 
	 * @return int session ID
	 */
	public final int getSessionID() {
		return this.sessionID;
	}

	/**
	 * Sets the mlcSession.
	 * 
	 * @param mlcSession
	 *            the new value of the mlcSession property
	 */
	public final void setSessionID(final int mlcSession) {
		this.sessionID = mlcSession;
	}

	/**
	 * Returns the Membership ID of the MLC Session.
	 * 
	 * @return memID
	 */
	public final String getMemID() {
		return this.memID;
	}

	/**
	 * Sets the memID.
	 * 
	 * @param memberID
	 *            the new value of the memID property
	 */
	public final void setMemID(final String memberID) {
		this.memID = memberID;
	}

	/**
	 * Returns the Membership ID of the MLC Session.
	 * 
	 * @return userType
	 */
	public final String getUserType() {
		return this.userType;
	}

	/**
	 * Sets the member type.
	 * 
	 * @param memberType
	 *            the new value of the memberType property
	 */
	public final void setUserType(final String memberType) {
		this.userType = memberType;
	}

	/**
	 * Returns the PreLink Indicator of the member.
	 * 
	 * @return preLink
	 */
	public final String getPreLink() {
		return this.preLink;
	}

	/**
	 * sets the memberPreLink.
	 * 
	 * @param memberPreLink
	 *            the new value of the preLink property
	 */
	public final void setPreLink(final String memberPreLink) {
		this.preLink = memberPreLink;
	}

	/**
	 * Returns the just login status of the member.
	 * 
	 * @return isJustLogin
	 */
	public final String getIsJustLogin() {
		return this.isJustLogin;
	}

	/**
	 * sets the value of justLogin.
	 * 
	 * @param justLogin
	 *            the new value of the justLogin property
	 */
	public final void setIsJustLogin(final String justLogin) {
		this.isJustLogin = justLogin;
	}
}
