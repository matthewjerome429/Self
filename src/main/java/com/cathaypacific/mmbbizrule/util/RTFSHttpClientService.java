package com.cathaypacific.mmbbizrule.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class RTFSHttpClientService {

    @Autowired
    @Qualifier("RTFSRestTemplate")
    private RestTemplate restTemplate;

    public String postJson(String url, String json) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        String str = restTemplate.postForObject(url, entity, String.class);

        return restTemplate.postForObject(url, entity, String.class);
    }
}
