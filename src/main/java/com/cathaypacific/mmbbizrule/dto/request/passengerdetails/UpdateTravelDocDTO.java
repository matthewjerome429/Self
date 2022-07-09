package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.annotation.IsValidDate;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.COR;
import com.cathaypacific.mmbbizrule.validator.group.DOCO;
import com.cathaypacific.mmbbizrule.validator.group.DOCS;

import io.swagger.annotations.ApiModelProperty;

public class UpdateTravelDocDTO extends Updated {
	
	private static final long serialVersionUID = 349586695850203472L;
	
	/** The familyName. */
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = { DOCS.class })
	@Pattern(regexp = "^[A-Za-z ]{1,70}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
    private String familyName;
    
    /** The givenName. */
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = { DOCS.class })
	@Pattern(regexp = "^[A-Za-z ]{1,70}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
    private String givenName;
    
    /** The travelDocumentType. */
	@NotEmpty(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = {DOCS.class, DOCO.class})
	private String travelDocumentType;
	
    /** The travelDocumentNumber. */
	@NotNull(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = {DOCS.class, DOCO.class})
	@Pattern(regexp = "^[A-Za-z0-9]{1,25}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class, DOCO.class})
    private String travelDocumentNumber;
    
    /** The countryOfIssuance */
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = { DOCS.class, DOCO.class })
    private String countryOfIssuance;
    
	/** The countryOfResidence */
	@NotEmpty(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = {COR.class})
	private String countryOfResidence;
	
    /** The nationality. */
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = { DOCS.class })
    private String nationality;
	
    /** date of expire, format: yyyy-mm-dd */
    @IsValidDate(comparable = true, future = true, nullable = false, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
    private String dateOfExpire;

	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE, groups = { DOCS.class })
	@Pattern(regexp = "^(M|F)$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
	private String gender;//gender
	
	@IsValidDate(comparable = true, nullable = false, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
	private String dateOfBirth;//yyyy-MM-dd
	
	@ApiModelProperty("the segmentId of the segment which the travel doc value copys from(since front-end has toggle on function, the value in this travel doc may come from the travel doc of other segments)")
	@Pattern(regexp = "^[0-9]*$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE, groups = {DOCS.class})
	private String copyFrom;
	
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getTravelDocumentType() {
		return travelDocumentType;
	}

	public void setTravelDocumentType(String travelDocumentType) {
		this.travelDocumentType = travelDocumentType;
	}

	public String getTravelDocumentNumber() {
		return travelDocumentNumber;
	}

	public void setTravelDocumentNumber(String travelDocumentNumber) {
		this.travelDocumentNumber = travelDocumentNumber;
	}

	public String getCountryOfIssuance() {
		return countryOfIssuance;
	}

	public void setCountryOfIssuance(String countryOfIssuance) {
		this.countryOfIssuance = countryOfIssuance;
	}
	
	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}
	
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDateOfExpire() {
		return dateOfExpire;
	}

	public void setDateOfExpire(String dateOfExpire) {
		this.dateOfExpire = dateOfExpire;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCopyFrom() {
		return copyFrom;
	}

	public void setCopyFrom(String copyFrom) {
		this.copyFrom = copyFrom;
	}
}
