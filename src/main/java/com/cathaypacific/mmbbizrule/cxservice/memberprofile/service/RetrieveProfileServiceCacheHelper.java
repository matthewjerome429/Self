package com.cathaypacific.mmbbizrule.cxservice.memberprofile.service;

import java.net.URI;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.ClsAPIRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.socialaccount.ClsSocialAccountRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.v2.TravelDocumentRetrievalRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.ClsApiResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PreferenceRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.Profile;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.ClsSocialAccountResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2.TravelDocumentRetrievalResponse;
import com.google.gson.Gson;

@Service
public class RetrieveProfileServiceCacheHelper {

	private static LogAgent logger = LogAgent.getLogAgent(RetrieveProfileServiceCacheHelper.class);
	
	@Value("${cx.memberprofile.endpoint.path.summary}")
	private String endpointPathSummary;

	@Value("${cx.memberprofile.endpoint.path.details}")
	private String endpointPathDetails;
	
	@Value("${cx.memberprofile.endpoint.path.details.v2}")
	private String endpointPathDetailsV2;
	
	@Value("${cx.memberprofile.endpoint.path.socialaccount}")
	private String endpointPathSocialAccount;

	@Value("${member.services.api.key}")
	private String apiKey;
	
	@Value("${member.services.api.key.v2}")
	private String apiKeyV2;

	@Autowired
	private RestTemplate restTemplate;

	@TokenLevelCacheable(name = TokenCacheKeyEnum.PROFILE_SUMMARY)
	public Profile retrieveMemberProfileSummary(ClsAPIRequest summaryInfoRequest) {
		URI uri = UriComponentsBuilder.fromHttpUrl(endpointPathSummary).build().toUri();

		RequestEntity<ClsAPIRequest> requestEntity = RequestEntity.post(uri).header("apiKey", apiKey)
				.header("Content-Type", "application/json").body(summaryInfoRequest);
        // OLSS-7868 (A) [Login] - INC000001815680 - Not to throw error on non-member login during CLS outage
        ResponseEntity<ClsApiResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, ClsApiResponse.class);
        } catch (Exception e) {
            logger.error(
                    String.format("Retrieve memeber Profile Summary failed, ClsAPIRequest: %s,CLS connection fail:%s",
                            summaryInfoRequest.toString(), e.getMessage()),
                    e);
            return null;
        }
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return responseEntity.getBody().getProfile();
		} else {
			return null;
		}
	}

	@TokenLevelCacheable(name = TokenCacheKeyEnum.PROFILE_PREFERENCE)
	public PreferenceRecord retrieveMemberProfileDetails(ClsAPIRequest summaryInfoRequest) {
		URI uri = UriComponentsBuilder.fromHttpUrl(endpointPathDetails).build().toUri();

		RequestEntity<ClsAPIRequest> requestEntity = RequestEntity.post(uri).header("apiKey", apiKey)
				.header("Content-Type", "application/json").body(summaryInfoRequest);
        // OLSS-7868 (A) [Login] - INC000001815680 - Not to throw error on non-member login during CLS outage
        ResponseEntity<ClsApiResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, ClsApiResponse.class);
        } catch (Exception e) {
            logger.error(
                    String.format("Retrieve memeber Profile Details failed, ClsAPIRequest: %s,CLS connection fail:%s",
                            summaryInfoRequest.toString(), e.getMessage()),
                    e);
            return null;
        }
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return responseEntity.getBody().getPreferenceRecord();
		} else {
			return null;
		}
	}
	
	@TokenLevelCacheable(name = TokenCacheKeyEnum.PROFILE_SOCIAL_ACCOUNT)
	public ClsSocialAccountResponse retrieveSocialAccountDetails(ClsSocialAccountRequest socialAccountRequest) {
		URI uri = UriComponentsBuilder.fromHttpUrl(endpointPathSocialAccount).build().toUri();

		RequestEntity<ClsSocialAccountRequest> requestEntity = RequestEntity.post(uri).header("apiKey", apiKey)
				.header("Content-Type", "application/json").body(socialAccountRequest);
		// TODO remove the log
		Gson gson = new Gson();
		logger.info(String.format("Temporary log for test - post request url: %s, header: %s, body: %s", requestEntity.getUrl().toString(), requestEntity.getHeaders().toString(), gson.toJson(requestEntity.getBody())));
        // OLSS-7868 (A) [Login] - INC000001815680 - Not to throw error on non-member login during CLS outage
        ResponseEntity<ClsSocialAccountResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, ClsSocialAccountResponse.class);
        } catch (Exception e) {
            logger.error(String.format(
                    "Retrieve Social Account Details failed, ClsSocialAccountRequest: %s,CLS connection fail:%s",
                    gson.toJson(socialAccountRequest), e.getMessage()), e);
            return null;
        }
		logger.info(String.format("Temporary log for test - post response status: %s, body: %s", responseEntity.getStatusCode().toString(), gson.toJson(responseEntity.getBody())));
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return responseEntity.getBody();
		} else {
			return null;
		}
	}

	@TokenLevelCacheable(name = TokenCacheKeyEnum.PROFILE_PREFERENCE_V2)
	public TravelDocumentRetrievalResponse retrieveMemberProfileSummaryV2(TravelDocumentRetrievalRequest request) {
		URI uri = UriComponentsBuilder.fromHttpUrl(endpointPathDetailsV2).build().toUri();

		RequestEntity<TravelDocumentRetrievalRequest> requestEntity = RequestEntity.post(uri).header("X-Consumer-ID", MMBUtil.getCurrentAppCode()).header("X-Consumer-Username", MMBUtil.getCurrentAppCode())
				.header("Content-Type", "application/json").header("apiKey", apiKeyV2).header("trace_id", request.getCorrelationId()).body(request);
        // OLSS-7868 (A) [Login] - INC000001815680 - Not to throw error on non-member login during CLS outage
        ResponseEntity<TravelDocumentRetrievalResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, TravelDocumentRetrievalResponse.class);
        } catch (Exception e) {
            logger.error(String.format(
                    "Retrieve Member Profile Summary V2 failed, TravelDocumentRetrievalRequest.memberIdOrUsername: %s,CLS connection fail:%s",
                    request.getMemberIdOrUsername(), e.getMessage()), e);
            return null;
        }
		if (responseEntity.getStatusCode().equals(HttpStatus.OK) && Objects.equals(responseEntity.getBody().getStatusCode(), "00")) {
			return responseEntity.getBody();
		} else {
			return null;
		}
	}
}
