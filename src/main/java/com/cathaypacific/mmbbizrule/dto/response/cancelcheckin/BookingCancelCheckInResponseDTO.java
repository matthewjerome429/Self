package com.cathaypacific.mmbbizrule.dto.response.cancelcheckin;


import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class BookingCancelCheckInResponseDTO extends BaseResponseDTO {

    private boolean success ;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
