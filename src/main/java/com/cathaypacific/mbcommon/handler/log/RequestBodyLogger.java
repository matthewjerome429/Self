package com.cathaypacific.mbcommon.handler.log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.MaskMessageUtil;

@ControllerAdvice
public class RequestBodyLogger extends RequestBodyAdviceAdapter {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(RequestBodyLogger.class);
	
	//private Gson gson = new Gson();
	
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		RequestMapping isHTTPRequest = methodParameter.getMethodAnnotation(RequestMapping.class);
		return isHTTPRequest != null;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		byte[] body = IOUtils.toByteArray(inputMessage.getBody());
		SimpleHttpInputMessage simpleHttpInputMessage = new SimpleHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(body));
		String strBody = new String(body, "UTF-8");
		LOGGER.httpReqInfo(parameter.getMethod().getName(), "------ Request Body Original of type " + targetType.getTypeName() + " for parameter " + parameter.getParameterName() + " ------", MaskMessageUtil.maskPin(strBody));
		return simpleHttpInputMessage;
	}

	/*@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		LOGGER.httpReqInfo(parameter.getMethod().getName(), "------ Request Body Object converted successfully ------", gson.toJson(body));
		return body;
	}*/

	class SimpleHttpInputMessage implements HttpInputMessage {
		private InputStream inputStream;
		private HttpHeaders httpHeaders;

		SimpleHttpInputMessage(HttpHeaders httpHeaders, InputStream inputStream) {
			this.inputStream = inputStream;
			this.httpHeaders = httpHeaders;
		}

		@Override
		public HttpHeaders getHeaders() {
			return httpHeaders;
		}

		@Override
		public InputStream getBody() throws IOException {
			return inputStream;
		}
	};
}
