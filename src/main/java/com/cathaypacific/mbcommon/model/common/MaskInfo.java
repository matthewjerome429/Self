package com.cathaypacific.mbcommon.model.common;

import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;

/**
 * The model of mask info for mmb
 * @author zilong.bu
 *
 */
public class MaskInfo {

	/** field name */
	private MaskFieldName fieldName;
	
	/** passenger id of the mask field */
	private String passengerId;
	
	/** segments id of the mask field */
	private String segmentId;
	
	/** the original value of the field */
	private String  originalValue;
	
	/** the masked value of the field */
	private String maskedValue;
	
	/** edited in Current session, should not mask the value if this field true and originalValue same as request value.</br>
	 * if originalValue not same as request value, it is mean pnr updated in other client, should mask the value also.
	 * */
	private boolean editedInCurrentSession;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getMaskedValue() {
		return maskedValue;
	}

	public void setMaskedValue(String maskedValue) {
		this.maskedValue = maskedValue;
	}

	public boolean isEditedInCurrentSession() {
		return editedInCurrentSession;
	}

	public void setEditedInCurrentSession(boolean editedInCurrentSession) {
		this.editedInCurrentSession = editedInCurrentSession;
	}

	public MaskFieldName getFieldName() {
		return fieldName;
	}

	public void setFieldName(MaskFieldName fieldName) {
		this.fieldName = fieldName;
	}

}
