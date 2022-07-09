package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

public class AddLinkedBookingDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -758413733164779731L;
    private boolean linkSuccess;

    public boolean isLinkSuccess() {
        return linkSuccess;
    }

    public void setLinkSuccess(boolean linkSuccess) {
        this.linkSuccess = linkSuccess;
    }

}
