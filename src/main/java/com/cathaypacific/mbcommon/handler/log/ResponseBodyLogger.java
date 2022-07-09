package com.cathaypacific.mbcommon.handler.log;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ResponseBodyLogger implements ResponseBodyAdvice<Object> {
	
	private final ObjectMapper om;
	
	public ResponseBodyLogger() {
		om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
	}
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		RequestMapping isHTTPRequest = returnType.getMethodAnnotation(RequestMapping.class);
		return isHTTPRequest != null;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body != null) {
			String jsonBody;
			try {
				jsonBody = om.writeValueAsString(body);				
			} catch (JsonProcessingException e) {
				jsonBody = null;
			}
			
			if (jsonBody == null) {
				jsonBody = body.toString();
			}
			MDC.put(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY, jsonBody);
		}
		return body;
	}
}
