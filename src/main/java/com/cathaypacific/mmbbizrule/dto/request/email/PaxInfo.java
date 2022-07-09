package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer the passenger Info fields for 15below
 *
 * @author fangfang.zhang
 */
public class PaxInfo implements Serializable {

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = -3191303665490099469L;

	/** the passenger id. */
	@ApiModelProperty(value = "paxSeq", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String paxSeq;

	/** the title. */
	private String title;

	/** the firstName. */
	private String firstName;

	/** the lastName. */
	private String lastName;

	/** the ffpInfo. */
	private FFPInfoDTO ffpInfo;

	public String getPaxSeq() {
		return paxSeq;
	}

	public void setPaxSeq(String paxSeq) {
		this.paxSeq = paxSeq;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public FFPInfoDTO getFfpInfo() {
		return ffpInfo;
	}

	public void setFfpInfo(FFPInfoDTO ffpInfo) {
		this.ffpInfo = ffpInfo;
	}

	@Override
	public String toString() {
		return "PaxInfoDTO [Title=" + title + ", paxFirstName=" + firstName + ", FFPInfoDTO=" + ffpInfo + "]";
	}

}
