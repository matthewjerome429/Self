package com.cathaypacific.mmbbizrule.cxservice.messagehub.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.config.MessageHubConfig;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.client.MessageHubClient;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request.GetEventRequestDto;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.GetEventResponseDto;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.service.MessageHubService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.gson.Gson;

@Service
public class MessageHubServiceImpl implements MessageHubService {

    private static LogAgent logger = LogAgent.getLogAgent(MessageHubServiceImpl.class);
    @Autowired
    private PnrInvokeService pnrInvokeService;
    @Autowired
    private MessageHubClient messageHubClient;
    @Autowired
    private MessageHubConfig messageHubConfig;

    private static final Gson gson = new Gson();

    @Override
    @LogPerformance(message = "Time required to retrieve message hub events")
    @TokenLevelCacheable(name = TokenCacheKeyEnum.MESSAGEHUB_EVENTS)
    public GetEventResponseDto getEventResponse(String rloc, List<String> eventTypes) throws Exception {
        // retrieve pnr
        PNRReply pnrReply = null;
        String eventResponseDtoStr = null;
        try {
            pnrReply = pnrInvokeService.retrievePnrReplyByOneARloc(rloc);
            eventResponseDtoStr = messageHubClient.postRetrieveEventsJson(messageHubConfig.getRetrieveEventsUrl(),
                    gson.toJson(buildGetEventsRequest(rloc, eventTypes, pnrReply)));
        } catch (BusinessBaseException e) {
            logger.error(String.format("[Message Hub] retrieve PNR by rloc: %s failed while cancelling booking", rloc),
                    ErrorCodeEnum.ERR_PAXCOM_MESSAGEHUB_EVENTS_EXCEPTION.getCode(), e);
            return null;
        } catch (Exception e) {
            logger.error(String.format("[Message Hub] retrieve events from message hub failed for rloc %s, events %s", rloc,
                    gson.toJson(eventTypes)), ErrorCodeEnum.ERR_PAXCOM_MESSAGEHUB_EVENTS_EXCEPTION.getCode(), e);
            return null;
        }
        // check response
        GetEventResponseDto response = gson.fromJson(eventResponseDtoStr, GetEventResponseDto.class);
        if (null == response) {
            logger.error(String.format("[Message Hub] can not retrieve anything from messhub events for rloc %s, events %s", rloc,
                    gson.toJson(eventTypes)), ErrorCodeEnum.ERR_PAXCOM_MESSAGEHUB_EVENTS_EXCEPTION.getCode());
            return null;
        }
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            logger.error(
                    String.format("[Message Hub] message hub return error when get events, error %s ",
                            gson.toJson(response.getErrors())),
                    ErrorCodeEnum.ERR_PAXCOM_MESSAGEHUB_EVENTS_EXCEPTION.getCode());
            return null;
        }
        return response;
    }

    /**
     * build get events request for message hub call
     * 
     * @param requestDTO
     * @param pnrReply
     * @return
     */
    private GetEventRequestDto buildGetEventsRequest(String rloc, List<String> eventTypes, PNRReply pnrReply) {
        GetEventRequestDto eventRequestDto = new GetEventRequestDto();
        eventRequestDto.setJobId(MMBUtil.getCurrentMMBToken());
        eventRequestDto.setrLoc(rloc);
        eventRequestDto.setEvtTypes(eventTypes);
        eventRequestDto.setPnrCreatedAt(retrievePnrCreatedTime(rloc, pnrReply));
        return eventRequestDto;
    }

    /**
     * @param requestDTO
     * @param pnrReply
     * @return
     */
    private String retrievePnrCreatedTime(String rloc, PNRReply pnrReply) {
        String dateTime = "";
        if (null == pnrReply || null == pnrReply.getSecurityInformation()
                || null == pnrReply.getSecurityInformation().getSecondRpInformation().getCreationDate()
                || null == pnrReply.getSecurityInformation().getSecondRpInformation().getCreationTime()) {
            logger.warn(String.format("can not retrieve pnr create time from pnr, rloc %s", rloc));
            return dateTime;
        }
        return DateUtil.convertDateFormat(
                pnrReply.getSecurityInformation().getSecondRpInformation().getCreationDate()
                        + pnrReply.getSecurityInformation().getSecondRpInformation().getCreationTime(),
                DateUtil.DATE_PATTERN_DDMMYYHHMM, DateUtil.DATETIME_FORMAT_XML);
    }

}
