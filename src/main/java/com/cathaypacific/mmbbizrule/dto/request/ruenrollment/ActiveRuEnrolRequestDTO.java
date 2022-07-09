package com.cathaypacific.mmbbizrule.dto.request.ruenrollment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

import io.swagger.annotations.ApiModelProperty;

public class ActiveRuEnrolRequestDTO {
	
	@ApiModelProperty(required = true, value = "passenger id")
	@Pattern(regexp = "^[0-9]+$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String passengerId;
	
	@ApiModelProperty(required = true, value = "email address")
	@Pattern(regexp = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$", groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String email;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private ActiveRuEnrolNameDTO name;
	
	@Valid
	private ActiveRuEnrolLoginDetailsDTO loginDetails;
	
	@ApiModelProperty(required = false, value = "the preferred origin")
	private String preferredOrigin;
	
	@ApiModelProperty(required = false, value = "whether agree the promotion consent")
	private boolean promotionConsent;
	
	@ApiModelProperty(required = false, value = "whether need to add the booking to new RU account after the enrollment")
	private boolean addBooking;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ActiveRuEnrolNameDTO getName() {
		return name;
	}

	public void setName(ActiveRuEnrolNameDTO name) {
		this.name = name;
	}

	public ActiveRuEnrolLoginDetailsDTO getLoginDetails() {
		return loginDetails;
	}

	public void setLoginDetails(ActiveRuEnrolLoginDetailsDTO loginDetails) {
		this.loginDetails = loginDetails;
	}

	public String getPreferredOrigin() {
		return preferredOrigin;
	}

	public void setPreferredOrigin(String preferredOrigin) {
		this.preferredOrigin = preferredOrigin;
	}

	public boolean isPromotionConsent() {
		return promotionConsent;
	}

	public void setPromotionConsent(boolean promotionConsent) {
		this.promotionConsent = promotionConsent;
	}

	public boolean isAddBooking() {
		return addBooking;
	}

	public void setAddBooking(boolean addBooking) {
		this.addBooking = addBooking;
	}
}
