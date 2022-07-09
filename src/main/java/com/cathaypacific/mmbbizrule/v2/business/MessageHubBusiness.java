package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request.EventsRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.EventsResponseDTO;

public interface MessageHubBusiness {

    /**
     * get events from message hub
     * 
     * @param loginInfo
     * @param requestDTO
     * @return
     * @throws BusinessBaseException
     */
    public EventsResponseDTO getEvents(EventsRequestDTO requestDTO) throws Exception;

}
