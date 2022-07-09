package com.cathaypacific.mmbbizrule.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageHubConfig {

    @Value("${olss.messagehub.enabled.event.type}")
    private String enabledEventType;
    @Value("${olss.messagehub.retrieve.events.endpoint}")
    private String retrieveEventsUrl;
    @Value("${olss.messagehub.apikey}")
    private String messageHubApikey;

    public List<String> getEnabledEventType() {
        return !StringUtils.isEmpty(enabledEventType) ? Arrays.asList(enabledEventType.split(",")) : Collections.emptyList();
    }

    public String getRetrieveEventsUrl() {
        return retrieveEventsUrl;
    }

    public String getMessageHubApikey() {
        return messageHubApikey;
    }

}
