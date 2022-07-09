package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer the passenger contact fields for 15below
 *
 * @author fangfang.zhang
 */
public class PaxContactDTO implements Serializable {

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = -765252387641924072L;

	/** the contactType. */
	@ApiModelProperty(value = "contactType", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private ContactTypeEnum contactType;

	/** the contactDetail. */
	@ApiModelProperty(value = "contactDetail", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String contactDetail;

	public ContactTypeEnum getContactType() {
		return contactType;
	}

	public void setContactType(ContactTypeEnum contactType) {
		this.contactType = contactType;
	}

	public String getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}

	@Override
	public String toString() {
		return "PaxContactDTO [contactType=" + contactType + ", contactDetail=" + contactDetail + "]";
	}

}
