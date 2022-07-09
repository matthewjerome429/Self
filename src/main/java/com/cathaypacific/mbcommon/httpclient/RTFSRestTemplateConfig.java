package com.cathaypacific.mbcommon.httpclient;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import java.util.List;

public class RTFSRestTemplateConfig extends RestTemplateConfig {

    /** timeout In Milliseconds **/
    @Value("${endpoint.path.rtfs.timeoutInMillionSeconds}")
    private Integer timeoutInMillionSeconds;

    /** timeout In Milliseconds **/
    @Value("${http.client.cookie.disable:false}")
    private boolean disableCookie;

    /** disable SSL check flag **/
    @Value("${http.client.ssl.disable:false}")
    private boolean disableSSLCheck;

    @Value("#{'${restTemplate.disableLogBody.contentType}'.split(',')}")
    private List<String> disbaledLogBodyContentTypes;

    /** maxTotalConnections **/
    @Value("${http.connection.maxTotalConnections}")
    private Integer maxTotalConnections;

    /** defaultMaxConnectionsPerHost **/
    @Value("${http.connection.defaultMaxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;


    @Override
    protected ClientHttpRequestFactory clientHttpRequestFactory(){

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setMaxConnTotal(maxTotalConnections);
        httpClientBuilder.setMaxConnPerRoute(defaultMaxConnectionsPerHost);


        if (disableSSLCheck) {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
                SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
                httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            } catch (Exception e) {
                throw new RuntimeException("SLL config initialize failed.",e);
            }

        }

        if (disableCookie) {
            httpClientBuilder.disableCookieManagement();
        }

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());

        httpRequestFactory.setReadTimeout(timeoutInMillionSeconds);
        httpRequestFactory.setConnectTimeout(timeoutInMillionSeconds);

        return new BufferingClientHttpRequestFactory(httpRequestFactory);
    }
}
