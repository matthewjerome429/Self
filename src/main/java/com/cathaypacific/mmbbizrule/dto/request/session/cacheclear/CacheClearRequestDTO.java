package com.cathaypacific.mmbbizrule.dto.request.session.cacheclear;

public class CacheClearRequestDTO {
	/** 1A RLOC of the booking */
	private String rloc;
	/** whether should clear PNR cache */
	private boolean clearPnr;
	/** whether should clear TempSeat cache */
	private boolean clearTempSeat;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public boolean isClearPnr() {
		return clearPnr;
	}

	public void setClearPnr(boolean clearPnr) {
		this.clearPnr = clearPnr;
	}

	public boolean isClearTempSeat() {
		return clearTempSeat;
	}

	public void setClearTempSeat(boolean clearTempSeat) {
		this.clearTempSeat = clearTempSeat;
	}

}
