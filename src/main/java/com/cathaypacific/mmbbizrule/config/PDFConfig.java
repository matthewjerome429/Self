package com.cathaypacific.mmbbizrule.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.cathaypacific.mmbbizrule.util.PDFUtil;

@Configuration
public class PDFConfig {

	@Value("${pdfbox.fontcache.path}")
	private String pdfBoxFontCachePath;
	
	@PostConstruct
	public void init() {
		PDFUtil.setPDFBoxCache(pdfBoxFontCachePath);
	}
}
