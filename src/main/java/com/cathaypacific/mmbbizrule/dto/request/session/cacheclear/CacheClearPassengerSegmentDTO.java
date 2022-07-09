package com.cathaypacific.mmbbizrule.dto.request.session.cacheclear;

public class CacheClearPassengerSegmentDTO {
	/** passenger id */
	private String passengerId;
	/** segment id */
	private String segmentId;
	/** whether should clear temporary seat cache */
	private boolean clearTempSeat;

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

	public boolean isClearTempSeat() {
		return clearTempSeat;
	}

	public void setClearTempSeat(boolean clearTempSeat) {
		this.clearTempSeat = clearTempSeat;
	}
	
}
