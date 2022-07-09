package com.cathaypacific.mmbbizrule.cxservice.messagehub.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.config.MessageHubConfig;

@Service
public class MessageHubClient {

    @Autowired
    @Qualifier("MessageHubRestTemplate")
    private RestTemplate messageHubRestTemplate;

    private static final String API_KEY = "apikey";

    @Autowired
    private MessageHubConfig messageHubConfigConfig;

    /**
     * get events from message hub
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String postRetrieveEventsJson(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, messageHubConfigConfig.getMessageHubApikey());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return messageHubRestTemplate.postForObject(url, entity, String.class);
    }

}
