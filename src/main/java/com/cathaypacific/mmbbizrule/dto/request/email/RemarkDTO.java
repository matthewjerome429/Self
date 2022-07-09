package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;

/**
 * Transfer the Remark fields for 15below
 *
 * @author fangfang.zhang
 */
public class RemarkDTO implements Serializable {

	/**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = -3168077932738838840L;
	/** the type. */
	private RemarkTypeEnum type;

	/** the remark. */
	private String remark;

	public RemarkDTO() {
	}

	public RemarkDTO(RemarkTypeEnum type, String remark) {
		this.type = type;
		this.remark = remark;
	}

	public RemarkTypeEnum getType() {
		return type;
	}

	public void setType(RemarkTypeEnum type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "RemarksDTO [type=" + type + ", remark=" + remark + "]";
	}

}
