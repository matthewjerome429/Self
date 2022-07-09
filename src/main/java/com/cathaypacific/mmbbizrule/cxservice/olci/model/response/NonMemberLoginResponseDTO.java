package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import com.cathaypacific.mmbbizrule.cxservice.olci.model.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.JourneyDTO;

import java.util.List;

public class NonMemberLoginResponseDTO extends BaseResponseDTO {

    private List<JourneyDTO> journeys;

    public List<JourneyDTO> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<JourneyDTO> journeys) {
        this.journeys = journeys;
    }
}
