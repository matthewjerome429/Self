package com.cathaypacific.mmbbizrule.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PNREligibilityRequest;

@Service
public class BookEligibilityHttpClientService {

    @Autowired
    @Qualifier("BookEligibilityRestTemplate")
    private RestTemplate bookEligibilityRestTemplate;

    private static final String API_KEY = "apikey";

    @Autowired
    private BookEligibilityConfig bookEligibilityConfig;

    public String postPnrEligibilityJson(String url, PNREligibilityRequest pnrEligibilityRequest) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, bookEligibilityConfig.getFacadeApikey());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        headers.set(MMBBizruleConstants.MEAL_ELIGIBILITY_JOBID, pnrEligibilityRequest.getJobId());
        HttpEntity<String> entity = new HttpEntity<>(pnrEligibilityRequest.getPnrReply(), headers);
        return bookEligibilityRestTemplate.postForObject(url, entity, String.class);
    }

    public String postMealDetailsJson(String url, String json) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, bookEligibilityConfig.getFacadeApikey());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return bookEligibilityRestTemplate.postForObject(url, entity, String.class);
    }

    public String postMealDescriptionJson(String url, String json) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, bookEligibilityConfig.getFacadeApikey());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return bookEligibilityRestTemplate.postForObject(url, entity, String.class);
    }
}
