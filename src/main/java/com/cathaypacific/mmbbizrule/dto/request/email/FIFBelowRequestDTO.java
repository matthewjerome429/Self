package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer the notification fields per passenger for 15below
 *
 * @author fangfang.zhang
 */
public class FIFBelowRequestDTO implements Serializable {

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = 4922993000269868388L;

	/** the rloc. */
	@ApiModelProperty(value = "rloc", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;

	/** the languageCode. */
	@ApiModelProperty(value = "language code", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String languageCode;

	/** the paxInfos. */
	@ApiModelProperty(value = "paxInfos", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxInfo> paxInfos;

	/** the segments. */
	@ApiModelProperty(value = "segments", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<SegmentDTO> segments;

	/** the paxSegments. */
	@ApiModelProperty(value = "paxSegments", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxSegmentDTO> paxSegments;

	/** the paxContacts. */
	@ApiModelProperty(value = "paxContacts", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxContactDTO> paxContacts;

	/** the templateType. */
	@ApiModelProperty(value = "templateType", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private TemplateTypeEnum templateType;

	/** the remarks. */
	@ApiModelProperty(value = "remarks", required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<RemarkDTO> remarks;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public List<PaxInfo> getPaxInfos() {
		return paxInfos;
	}

	public void setPaxInfos(List<PaxInfo> paxInfos) {
		this.paxInfos = paxInfos;
	}

	public List<PaxSegmentDTO> getPaxSegments() {
		return paxSegments;
	}

	public void setPaxSegments(List<PaxSegmentDTO> paxSegments) {
		this.paxSegments = paxSegments;
	}
	
	public List<PaxSegmentDTO> findPaxSegments() {
		if (CollectionUtils.isEmpty(this.paxSegments)) {
			this.paxSegments = Lists.newArrayList();
		}
		return this.paxSegments;
	}
	
	public List<SegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentDTO> segments) {
		this.segments = segments;
	}

	public List<PaxInfo> findPaxInfos() {
		if (CollectionUtils.isEmpty(this.paxInfos)) {
			this.paxInfos = Lists.newArrayList();
		}
		return this.paxInfos;
	}

	public List<SegmentDTO> findSegments() {
		if (CollectionUtils.isEmpty(this.segments)) {
			this.segments = Lists.newArrayList();
		}
		return this.segments;
	}

	public List<PaxContactDTO> findPaxContacts() {
		if (CollectionUtils.isEmpty(paxContacts)) {
			return Lists.newArrayList();
		}
		return paxContacts;
	}

	public List<PaxContactDTO> getPaxContacts() {
		return paxContacts;
	}

	public void setPaxContacts(List<PaxContactDTO> paxContacts) {
		this.paxContacts = paxContacts;
	}

	public TemplateTypeEnum getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateTypeEnum templateType) {
		this.templateType = templateType;
	}

	public List<RemarkDTO> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<RemarkDTO> remarks) {
		this.remarks = remarks;
	}

	public List<RemarkDTO> findRemarks() {
		if (CollectionUtils.isEmpty(this.remarks)) {
			this.remarks = Lists.newArrayList();
		}
		return this.remarks;
	}

	@Override
	public String toString() {
		return "FIFBelowRequestDTO [rloc=" + rloc + ", languageCode=" + languageCode + ", paxInfos=" + paxInfos
				+ ", segments=" + segments + ", paxContacts=" + paxContacts + ", templateType=" + templateType + "]";
	}
}
