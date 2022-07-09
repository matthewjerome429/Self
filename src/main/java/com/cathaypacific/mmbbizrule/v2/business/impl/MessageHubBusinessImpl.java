package com.cathaypacific.mmbbizrule.v2.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.MessageHubConfig;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain.ActionDTO;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain.MetaSBR;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request.EventsRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.EventsDto;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.EventsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.GetEventResponseDto;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.PassengerSegmentEvent;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.service.MessageHubService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.v2.business.MessageHubBusiness;

@Service
public class MessageHubBusinessImpl implements MessageHubBusiness {

    private static LogAgent logger = LogAgent.getLogAgent(MessageHubBusinessImpl.class);

    @Autowired
    private MessageHubService messageHubService;

    @Autowired
    private MessageHubConfig messageHubConfig;

    private static final String SBR = "SBR";
    private static final String EMPTY_VALUE = "";
    private static final String EVENT_TYPE_FQTV = "FQTV";

    @Override
    public EventsResponseDTO getEvents(EventsRequestDTO requestDTO) throws Exception {
        EventsResponseDTO response = new EventsResponseDTO();
        // request checking according to jekins configure
        // we will only consume request when the event type is congifured in jekins
        List<String> validEventTypes = buildValidEventType(requestDTO);
        if(CollectionUtils.isEmpty(validEventTypes)) {
            logger.warn(String.format("[Message Hub] can not find valid event type from jekins, jekins event type %s", messageHubConfig.getEnabledEventType()));
            return response;
        }
        // call pax com message hub to get events
        GetEventResponseDto eventResponseDto = messageHubService.getEventResponse(requestDTO.getRloc(),
                validEventTypes);
        if(null != eventResponseDto) {
            // build passenger segment events according to message hub events response
            List<PassengerSegmentEvent> groupedEvents = buildPassengerSegmentEvents(validEventTypes, eventResponseDto);
            // set grouped events to response
            response.setEvents(groupedEvents);
        }
        return response;
    }

    /**
     * build passenger segment events according to message hub response
     * 
     * @param events
     * @param validEventTypes
     * @param eventResponseDto
     * @return
     */
    private List<PassengerSegmentEvent> buildPassengerSegmentEvents(List<String> validEventTypes,
            GetEventResponseDto eventResponseDto) {
        // get valid events from message hub
        List<EventsDto> validEventsDTO = abstractValidEventsFromMessageHub(validEventTypes, eventResponseDto);

        // parse event and meta info to passenger segment event
        List<PassengerSegmentEvent> passengerSegmentEvents = parseValidEventToPassengerSegmentEvent(validEventsDTO);

        // sort passenger segment event according to event creeate time
        List<PassengerSegmentEvent> sortedPassengerSegmentEvents = sortPassengerSegmentEvents(passengerSegmentEvents);

        // groupFQTVPassengerSegmentEvent(sortedPassengerSegmentEvents)
        // groupDOCSPassengerSegmentEvent(sortedPassengerSegmentEvents)
        // groupUNTKPassengerSegmentEvent(sortedPassengerSegmentEvents)
        // summary all event to return if needed
        return groupFQTVPassengerSegmentEvent(sortedPassengerSegmentEvents);
    }

    /**
     * @param sortedPassengerSegmentEvents
     * @return
     */
    private List<PassengerSegmentEvent> groupFQTVPassengerSegmentEvent(
            List<PassengerSegmentEvent> sortedPassengerSegmentEvents) {
        List<PassengerSegmentEvent> groupedFqtvEvents = new ArrayList<>();
        List<PassengerSegmentEvent> fqtvEvents = sortedPassengerSegmentEvents.stream()
                .filter(pse -> EVENT_TYPE_FQTV.equalsIgnoreCase(pse.getEventType())).collect(Collectors.toList());
        // fqtv group by pt
        // tempPassengerId used to record first event id
        // if the event does not has valid message
        // we will not return to bff
        List<String> tempPassengerId = new ArrayList<>();
        for (PassengerSegmentEvent fqtvEvent : fqtvEvents) {
            // not contains means first hint
            if (!tempPassengerId.contains(fqtvEvent.getPassengerId())) {
                tempPassengerId.add(fqtvEvent.getPassengerId());
                if(fqtvEvent.hasValidMessage()) {
                    groupedFqtvEvents.add(fqtvEvent);
                }
            }
        }
        return groupedFqtvEvents;
    }

    /**
     * sort passenger segment event
     * 
     * @param passengerSegmentEvents
     * @return
     */
    private List<PassengerSegmentEvent> sortPassengerSegmentEvents(List<PassengerSegmentEvent> passengerSegmentEvents) {
        return passengerSegmentEvents.stream().sorted((e2, e1) -> {
            if (StringUtils.isEmpty(e1.getEventCreateTime())) {
                return 1;
            } else if (StringUtils.isEmpty(e2.getEventCreateTime())) {
                return -1;
            } else {
                Date eventCreateTime1 = null;
                Date eventCreateTime2 = null;
                try {
                    eventCreateTime1 = DateUtil.getStrToDate(DateUtil.DATETIME_FORMAT_XML, e1.getEventCreateTime());
                } catch (ParseException e) {
                    logger.warn(String.format("[Message Hub] failed to conver string %s to date", e1.getEventCreateTime()));
                    return 1;
                }
                try {
                    eventCreateTime2 = DateUtil.getStrToDate(DateUtil.DATETIME_FORMAT_XML, e2.getEventCreateTime());
                } catch (ParseException e) {
                    logger.warn(String.format("[Message Hub] failed to conver string %s to date", e2.getEventCreateTime()));
                    return -1;
                }
                return eventCreateTime1.compareTo(eventCreateTime2);
            }

        }).collect(Collectors.toList());
    }

    /**
     * parse event to passenger segment event
     * 
     * @param validEventsDTO
     */
    private List<PassengerSegmentEvent> parseValidEventToPassengerSegmentEvent(List<EventsDto> validEventsDTO) {
        List<PassengerSegmentEvent> passengerSegmentEvents = new ArrayList<>();
        for (EventsDto eventsDto : validEventsDTO) {
            // for day1 validMessage means have no collection under event
            boolean hasValidMessage = CollectionUtils.isEmpty(eventsDto.getCollections());
            if (null != eventsDto.getEvent()) {
                MetaSBR meta = eventsDto.getEvent().getMeta();
                if (null != meta && CollectionUtils.isNotEmpty(meta.getActions())) {
                    for (ActionDTO action : meta.getActions()) {
                        // parse passenger and segment
                        PassengerSegmentEvent passengerSegmentEvent = new PassengerSegmentEvent();
                        String passengerId = null == action.getTravellerInfo() ? EMPTY_VALUE
                                : String.valueOf(action.getTravellerInfo().getNumber());
                        String passengerType = null == action.getTravellerInfo() ? EMPTY_VALUE
                                : action.getTravellerInfo().getType();
                        String segmentId = null == action.getFlight() ? EMPTY_VALUE
                                : String.valueOf(action.getFlight().getNumber());
                        passengerSegmentEvent.setPassengerType(passengerType);
                        if (PnrResponseParser.PASSENGER_TYPE_INF.equalsIgnoreCase(passengerType)) {
                            passengerId = passengerId + PnrResponseParser.PASSENGER_INFANT_ID_SUFFIX;
                        }
                        passengerSegmentEvent.setPassengerId(passengerId);
                        passengerSegmentEvent.setSegmentId(segmentId);
                        passengerSegmentEvent.setEventId(eventsDto.getEvent().getEvtId());
                        passengerSegmentEvent.setValidMessage(hasValidMessage);
                        passengerSegmentEvent.setEventCreateTime(eventsDto.getEvent().getCreatedAt());
                        passengerSegmentEvent.setEventType(eventsDto.getEvent().getEvtType());
                        passengerSegmentEvents.add(passengerSegmentEvent);
                    }
                }
            }
        }
        return passengerSegmentEvents;
    }

    /**
     * get valid events from message hub
     * 
     * @param validEventTypes
     * @param eventResponseDto
     * @param validEventsDTO
     * @return
     */
    private List<EventsDto> abstractValidEventsFromMessageHub(List<String> validEventTypes,
            GetEventResponseDto eventResponseDto) {
        List<EventsDto> validEventsDTO = new ArrayList<>();
        // abstract valid events from message hub response
        // condition:
        // srcType is SBR
        // eventType mapped with request
        if (CollectionUtils.isNotEmpty(eventResponseDto.getEvents())) {
            for (EventsDto eventsDto : eventResponseDto.getEvents()) {
                if (null != eventsDto && null != eventsDto.getEvent()) {
                    if (validEventTypes.contains(eventsDto.getEvent().getEvtType())) {
                        if (SBR.equalsIgnoreCase(eventsDto.getEvent().getSrcType())) {
                            // add to target event
                            validEventsDTO.add(eventsDto);
                        } else {
                            logger.warn(
                                    String.format("[Message Hub] ignore the invalid srcType %s", eventsDto.getEvent().getSrcType()));
                        }
                    } else {
                        logger.warn(
                                String.format("[Message Hub] ignore the invalid eventType %s", eventsDto.getEvent().getEvtType()));
                    }
                }
            }
        }
        return validEventsDTO;
    }

    /**
     * filter out invalid event type according to olss.messagehub.enabled.event.type
     * 
     * @param requestDTO
     * @return
     */
    private List<String> buildValidEventType(EventsRequestDTO requestDTO) {
        return requestDTO.getEventTypes().stream().filter(evetType -> {
            return messageHubConfig.getEnabledEventType().contains(evetType);
        }).collect(Collectors.toList());
    }

}
