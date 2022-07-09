package com.cathaypacific.mmbbizrule.util;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.util.HttpResponse.ResponseStatus;
import com.google.gson.Gson;


@Service
public class HttpClientService {

	private static LogAgent logger = LogAgent.getLogAgent(HttpClientService.class);

	@Autowired
	private RestTemplate restTemplate;


	public RestTemplate getRestTemplate(){
		return  restTemplate;
	}
	
	public <T> T get(String url, Class<T> responseType)  {
		return restTemplate.getForObject(url, responseType);
	}

	public String postJson(String url, String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		HttpEntity<String> entity = new HttpEntity<>(json, headers);
		
		/*logger.info("Invoke http service call: " + url + " with parameters: " + entity);*/
		return restTemplate.postForObject(url, entity, String.class);
	}
	
	public String postJson(String url, String json,HttpHeaders headers) {
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		HttpEntity<String> entity = new HttpEntity<>(json, headers);
		
		/*logger.info("Invoke http service call: " + url + " with parameters: " + entity);*/
		return restTemplate.postForObject(url, entity, String.class);
	}
	
	/**
	 * GetMethod to get a DTO object by Map params
	 * 
	 * @param url:
	 *            http://localhost:8080/test/action01
	 * @param paramMappings:
	 *            Map<String,String> entry, for key and value
	 * @return
	 */
	public <T> HttpResponse<T> getForObject(Class<T> clazz, String url,Map<String, String> header , Map<String, String> paramMappings) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (paramMappings != null && paramMappings.size() > 0) {
			sb.append("?");
			for (Entry<String, String> e : paramMappings.entrySet()) {
				sb.append(e.getKey()).append("={").append(e.getKey()).append("}&");
			}
		}
		HttpEntity<String> httpEntity = prepareEntityHeader(header);
		return doRequest(clazz, sb.toString(), httpEntity, HttpMethod.GET, paramMappings);
	}
	
	/**
	 * DeleteMethod to get a DTO object by Map params
	 * 
	 * @param url:
	 *            http://localhost:8080/test/action01
	 * @param paramMappings:
	 *            Map<String,String> entry, for key and value
	 * @return
	 */
	public <T> HttpResponse<T> deleteForObject(Class<T> clazz, String url,Map<String, String> header , Map<String, String> paramMappings) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (paramMappings != null && paramMappings.size() > 0) {
			sb.append("?");
			for (Entry<String, String> e : paramMappings.entrySet()) {
				sb.append(e.getKey()).append("={").append(e.getKey()).append("}&");
			}
		}
		HttpEntity<String> httpEntity = prepareEntityHeader(header);
		return doRequest(clazz, sb.toString(), httpEntity, HttpMethod.DELETE, paramMappings);
	}
	
	private <T> HttpResponse<T> doRequest(Class<T> clazz, String url, HttpEntity<?> header, HttpMethod method, Map<String, String> paramMappings, Object... paramList) {
		HttpResponse<T> res = new HttpResponse<>();
		try {
			ResponseEntity<String> response = null;
			if (paramMappings == null) {
				URI uri = restTemplate.getUriTemplateHandler().expand(url, paramList);
				res.setRequestUrl(uri.toString());
				response = restTemplate.exchange(url, method, header, String.class, paramList);
			} else {
				URI uri = restTemplate.getUriTemplateHandler().expand(url, paramMappings);
				res.setRequestUrl(uri.toString());
				response = restTemplate.exchange(url, method, header, String.class, paramMappings);
			}
			Gson gson = new Gson();
			res.setValue(gson.fromJson(response.getBody(), clazz));
			Long endTime = System.currentTimeMillis();
			int index = res.getRequestUrl().indexOf('/', 10);
			/*logger.info("HttpClientService.doRequest",  String.valueOf( endTime - startTime), "Http call for: " + res.getRequestUrl().substring(0, index));
			logger.info("Response Body : " + response.getBody());*/

			if (response.getStatusCode().is2xxSuccessful()) {
				res.setStatus(ResponseStatus.SUCCESS_200);
				res.setOriginalBody(response.hasBody() ? response.getBody() : "");
			} else if (response.getStatusCode().is4xxClientError()) {
				if (HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
					res.setStatus(ResponseStatus.URL_ERROR_404);
				} else {
					res.setStatus(ResponseStatus.CLIENT_ERROR_400_405);
				}
				res.setErrorMsg(response.getBody());
			} else if (response.getStatusCode().is5xxServerError()) {
				res.setStatus(ResponseStatus.SERVER_ERROR_500_501_504);
				res.setErrorMsg(response.getBody());
			}
		} catch (ResourceAccessException e) {
			res.setStatus(ResponseStatus.CONNECT_ERROR);
			res.setErrorMsg("[" + e.getClass().getName() + "]" + e.getMessage());
		} catch (Throwable t) {
			res.setStatus(ResponseStatus.SYSTEM_ERROR);
			res.setErrorMsg("[" + t.getClass().getName() + "]" + t.getMessage());
		}

		if (!ResponseStatus.SUCCESS_200.equals(res.getStatus())) {
			logger.error("Http request error! URL:" + res.getRequestUrl() + " Msg:" + res.getErrorMsg(), res.getStatus().toString());
		}

		return res;
	}
	
	private HttpEntity<String> prepareEntityHeader(Map<String, String> headerMap) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (headerMap != null && headerMap.size() > 0) {
			for (Entry<String, String> e : headerMap.entrySet()) {
				httpHeaders.set(e.getKey(), e.getValue());
			}
		}
		return new HttpEntity<>(httpHeaders);
	}
	
	
}
