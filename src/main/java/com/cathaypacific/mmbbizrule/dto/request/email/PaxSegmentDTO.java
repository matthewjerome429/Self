package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer the flight segment fields for 15below
 *
 * @author fangfang.zhang
 */
public class PaxSegmentDTO implements Serializable{

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = -175720883771676263L;

	/** the passengerSeq. */
	@ApiModelProperty(value = "paxSeq", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String paxSeq;

	/** the segmentSeq. */
	@ApiModelProperty(value = "segmentSeq", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String segmentSeq;
	
	/** the bookingClass. */
    private String bookingClass;
    
    private List<RemarkDTO> remarks;

	public String getPaxSeq() {
		return paxSeq;
	}

	public void setPaxSeq(String paxSeq) {
		this.paxSeq = paxSeq;
	}

	public String getSegmentSeq() {
		return segmentSeq;
	}

	public void setSegmentSeq(String segmentSeq) {
		this.segmentSeq = segmentSeq;
	}

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
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

}
