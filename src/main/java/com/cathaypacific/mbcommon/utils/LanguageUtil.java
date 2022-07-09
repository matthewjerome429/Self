package com.cathaypacific.mbcommon.utils;

import java.util.Locale;

import org.springframework.util.StringUtils;

public class LanguageUtil {
	
	private LanguageUtil() {
		
	}
	
	public static String getDefaultLanguage() {
		Locale locale = getDefaultLocale();
		return locale.toString();
	}
	
	public static Locale getDefaultLocale() {
		return new Locale("en", "HK");
	}
	
	public static String getLanguageByName(String name) {
		if(StringUtils.isEmpty(name)) {
			return getDefaultLanguage();
		} else {
			return name.replace("-", "_");
		}
	}
	
}
