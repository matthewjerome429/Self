package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import com.cathaypacific.mmbbizrule.cxservice.olci.model.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.JourneyDTO;

import java.util.List;

public class CancelCheckInResponseDTO extends BaseResponseDTO {

    private JourneyDTO journey;

    private boolean success ;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JourneyDTO getJourney() {
        return journey;
    }

    public void setJourney(JourneyDTO journey) {
        this.journey = journey;
    }
}
