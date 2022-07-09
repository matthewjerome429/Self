package com.cathaypacific.mmbbizrule.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mmbbizrule.config.BizRuleConfig;

@Component
public class LocaleHelper {
	
	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	public boolean localeConsentPageRequired(String acceptLanguage) {
		if(StringUtils.isEmpty(acceptLanguage)) {
			return false;
		}
		
		return bizRuleConfig.getConsentPageRequiredLocale().stream().anyMatch(locale -> acceptLanguage.startsWith(locale));
	}

}
