package com.cathaypacific.mmbbizrule.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;

public class EmailUtil {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(EmailUtil.class);

	private static final String CPA_SIMPLIFIED_CHINESE = "sc_CN";
	private static final String ISO_SIMPLIFIED_CHINESE = "zh_CN";
	
	private static final Pattern INTEGER_PATTERN = Pattern.compile("^(?<integer>\\d+)(?<fraction>\\.0+)?$");

	private EmailUtil() {

	}
	
	/**
	 * Capitalize first letter of each word, and transform the rest of the word to lower case.
	 * 
	 * @param text
	 * @return
	 */
    public static String formatTextToTitleCase(String text) {
    	if (StringUtils.isEmpty(text)) {
    		return text;
    	}
    	
    	String[] words = text.split("\\s");
    	UnaryOperator<String> formatWordFunc = word -> {
    		if (word.length() == 0) {
    			return word;
    		} else if (word.length() == 1) {
        		return word.toUpperCase();
        	} else {
        		return word.substring(0, 1).toUpperCase() + word.substring(1, word.length()).toLowerCase();
        	}
    	};
    	
    	return Stream.of(words).map(formatWordFunc).collect(Collectors.joining(" "));
    }
    
    public static Locale getISOLocale(String cpaLocale) {
    	if (StringUtils.isEmpty(cpaLocale)) {
    		return null;
    	}
    	
    	String isoLocale;
    	if (CPA_SIMPLIFIED_CHINESE.equals(cpaLocale)) {
    		isoLocale = ISO_SIMPLIFIED_CHINESE;
    	} else {
    		isoLocale = cpaLocale;
    	}
    	
    	return StringUtils.parseLocaleString(isoLocale);
    }
    
	/**
	 * Convert price number to localized format.
	 * 
	 * @param priceNumber
	 * @return
	 */
	public static String localizePriceNumber(String priceNumber, String locale) {
		Locale isoLocale = getISOLocale(locale);
		
		boolean isInteger = false;
		Matcher matcher = INTEGER_PATTERN.matcher(priceNumber);
		isInteger = matcher.matches();

		NumberFormat numberFormat = NumberFormat.getNumberInstance(isoLocale);
		if (isInteger) {
			numberFormat.setMaximumFractionDigits(0);
		} else {
			numberFormat.setMinimumFractionDigits(2);
		}

		try {
			BigDecimal decimal = new BigDecimal(priceNumber);
			priceNumber = numberFormat.format(decimal);
			return priceNumber;
		} catch (Exception e) {
			LOGGER.warn("Failed to format price number", e);
			return priceNumber;
		}
	}
}
