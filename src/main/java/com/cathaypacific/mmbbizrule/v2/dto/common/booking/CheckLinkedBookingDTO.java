package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

public class CheckLinkedBookingDTO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7504151974304134045L;

    private boolean needNameChecking;
    
    private BookingDTOV2 booking;

    public boolean isNeedNameChecking() {
        return needNameChecking;
    }

    public void setNeedNameChecking(boolean needNameChecking) {
        this.needNameChecking = needNameChecking;
    }

    public BookingDTOV2 getBooking() {
        return booking;
    }

    public void setBooking(BookingDTOV2 booking) {
        this.booking = booking;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
