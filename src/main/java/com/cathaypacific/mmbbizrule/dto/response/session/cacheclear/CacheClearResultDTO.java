package com.cathaypacific.mmbbizrule.dto.response.session.cacheclear;

public class CacheClearResultDTO {
	/** PNR cache is cleared */
	private boolean pnrCleared;
	/** All temporary seat is cleared */
	private boolean tempSeatCleared;

	public boolean isPnrCleared() {
		return pnrCleared;
	}

	public void setPnrCleared(boolean pnrCleared) {
		this.pnrCleared = pnrCleared;
	}

	public boolean isTempSeatCleared() {
		return tempSeatCleared;
	}

	public void setTempSeatCleared(boolean tempSeatCleared) {
		this.tempSeatCleared = tempSeatCleared;
	}
	
}
