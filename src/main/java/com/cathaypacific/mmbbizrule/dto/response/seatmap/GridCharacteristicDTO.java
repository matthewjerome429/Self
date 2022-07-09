
package com.cathaypacific.mmbbizrule.dto.response.seatmap;


public class GridCharacteristicDTO {

    private boolean windowSeat;
    private boolean centerSeat;
    private boolean aisleSeat;
    
	public boolean isWindowSeat() {
		return windowSeat;
	}
	public void setWindowSeat(boolean windowSeat) {
		this.windowSeat = windowSeat;
	}
	public boolean isCenterSeat() {
		return centerSeat;
	}
	public void setCenterSeat(boolean centerSeat) {
		this.centerSeat = centerSeat;
	}
	public boolean isAisleSeat() {
		return aisleSeat;
	}
	public void setAisleSeat(boolean aisleSeat) {
		this.aisleSeat = aisleSeat;
	}
}
