
package com.cathaypacific.mmbbizrule.dto.response.seatmap;


public class SeatDetailsDTO {

    private String seatOccupationStatus;
    private SeatCharacteristicDTO seatCharacteristic;

    public String getSeatOccupationStatus() {
        return seatOccupationStatus;
    }

    public void setSeatOccupationStatus(String seatOccupationStatus) {
        this.seatOccupationStatus = seatOccupationStatus;
    }

    public SeatCharacteristicDTO getSeatCharacteristic() {
        return seatCharacteristic;
    }

    public void setSeatCharacteristic(SeatCharacteristicDTO seatCharacteristic) {
        this.seatCharacteristic = seatCharacteristic;
    }

}
