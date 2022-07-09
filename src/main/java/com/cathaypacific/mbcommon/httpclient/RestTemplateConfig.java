package com.cathaypacific.mbcommon.httpclient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.MaskMessageUtil;
/**
 * The config of OLSS application, will override restTemplate.
 * Below is the environment variables:</br>
 * http.connection.timeoutInMillionSeconds</br>
 * @author zilong.bu
 *
 */
public class RestTemplateConfig {

	/** timeout In Milliseconds **/
	@Value("${http.connection.timeoutInMillionSeconds}")
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
	
	public RestTemplate buildRestTemplate(RestTemplateBuilder builder){
		RestTemplate restTemplate = builder.build(); 
		addMessageConverterWithNewMediaType(restTemplate);
		restTemplate.setRequestFactory(clientHttpRequestFactory());
		restTemplate.getInterceptors().add(new RestInvokeHeaderInterceptor());
		restTemplate.getInterceptors().add(new RequestResponseLoggingInterceptor());
		return restTemplate;
	}
	
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
	
	/**
	 *  the interceptor to add header for rest client call.
	 * @author zilong.bu
	 *
	 */
	private class RestInvokeHeaderInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			HttpHeaders headers = request.getHeaders();
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			if(ra != null){
				ServletRequestAttributes sra = (ServletRequestAttributes) ra;
				HttpServletRequest webRequest = sra.getRequest();
				//headers.add(HttpHeaders.COOKIE, webRequest.getHeader(HttpHeaders.COOKIE))
				//headers.add("User-Session-Id", webRequest.getHeader("User-Session-Id"));
				if(StringUtils.isEmpty(headers.getFirst("Access-Channel"))){
					headers.add("Access-Channel", webRequest.getHeader("Access-Channel"));
				}
			}
			return execution.execute(request, body);
		}
	}
	/**
	 *  the interceptor to add header for rest client call.
	 * @author zilong.bu
	 *
	 */
	private class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

		private final LogAgent logger = LogAgent.getLogAgent(RequestResponseLoggingInterceptor.class);

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			String requestBody = new String(body, "UTF-8");
			logger.info(
				request.getMethod().name(),
				MaskMessageUtil.maskPin(requestBody),
				request.getHeaders().toString(),
				body.length,
				"External call request:" + request.getURI()
			);
			
			ClientHttpResponse response = execution.execute(request, body);
			
			if (isLogWithoutBody(response)) {
				MediaType contentType = response.getHeaders().getContentType();
				logger.info(
					request.getMethod().name(),
					StringUtils.EMPTY,
					response.getHeaders().toString(),
					-1,
					String.format(
						"External call response: %s " +
						"The response content type is %s. " +
						"According to the config RESTTEMPLATE_DISABLE_BODY_LOG_CONTENT_TYPE, this response body and its size won't be logged.",
						request.getURI(),
						contentType.toString()
					)
				);
			} else {
				String responseStr = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
				logger.info(
					request.getMethod().name(),
					responseStr,
					response.getHeaders().toString(),
					responseStr.getBytes().length,
					"External call response:" + request.getURI()
				);
			}
			return response;
		}
		
		private boolean isLogWithoutBody(ClientHttpResponse response) {
			if (
				disbaledLogBodyContentTypes != null
				&& !disbaledLogBodyContentTypes.isEmpty()
				&& !StringUtils.isEmpty(disbaledLogBodyContentTypes.get(0))
				&& response != null
				&& response.getHeaders() != null
			) {
				MediaType contentType = response.getHeaders().getContentType();
				if (contentType != null && disbaledLogBodyContentTypes.contains(contentType.toString())) {
					logger.debug("MediaType is " + contentType.toString());
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * add new HttpMessageConverter with new MediaType eg. application/octet-stream
	 * 
	 * @param restTemplate
	 */
	private void addMessageConverterWithNewMediaType(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
		boolean mediaTypeExist = false;
		for(HttpMessageConverter<?> httpMessageConverter : httpMessageConverters) {
			if(httpMessageConverter instanceof MappingJackson2HttpMessageConverter 
					&& httpMessageConverter.getSupportedMediaTypes().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				mediaTypeExist = true;
				break;
			}
		}
		if(!mediaTypeExist) {
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
			converter.setSupportedMediaTypes(mediaTypes);	
			restTemplate.getMessageConverters().add(converter);
		}
	}

}
