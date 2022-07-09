package com.cathaypacific.mbcommon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;

public class MaskMessageUtil {
	
	//regex of pin parameter in request
	private static final String PIN_PARAMETER_REGEX = "\"pin\"(\\s*):(\\s*)\"(?<pin>.*?)(?<!\\\\)\"";
	private static final String PIN_VALUE_GROUP = "pin";
	private static final Pattern PIN_PARAMETER_PATTERN = Pattern.compile(PIN_PARAMETER_REGEX);
	
	private static final char PIN_MASK_CHAR = '*';
	
	/**
	 * Empty constructor.
	 */
	private MaskMessageUtil() {
		
	}
	
	/**
	 * mask pin value in the message
	 * @param requestBody
	 * @return String
	 */
	public static String maskPin(String message) {
		if (StringUtils.isEmpty(message)) {
			return message;
		}
		
		StringBuilder requestBuilder = new StringBuilder(message);
		Matcher matcher = PIN_PARAMETER_PATTERN.matcher(requestBuilder);
		while (matcher.find()) {
			// replace the pin value with "*"
			IntStream.range(matcher.start(PIN_VALUE_GROUP), matcher.end(PIN_VALUE_GROUP))
					.forEach(index -> requestBuilder.setCharAt(index, PIN_MASK_CHAR));
		}
		return requestBuilder.toString();
	}

}
