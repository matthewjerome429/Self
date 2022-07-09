package com.cathaypacific.mmbbizrule.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cathaypacific.mbcommon.loging.ApiPerformanceLogInterceptor;
import com.cathaypacific.mbcommon.token.RefreshTokenInterceptor;
import com.cathaypacific.olciconsumer.session.RefreshSessionInterceptor;

@Configuration
@ImportResource(value = "classpath:config/logging-interceptor.xml")
public class
WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public RefreshTokenInterceptor refreshTokenInterceptor() {
		return new RefreshTokenInterceptor();
	}
	
	@Bean
	public RefreshSessionInterceptor refreshSessionInterceptor() {
		return new RefreshSessionInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiPerformanceLogInterceptor());
		//registry.addInterceptor(new CreateTokenInterceptor()).addPathPatterns("/v1/booking/byrloc", "/v1/booking/byeticket", "/v1/verifytoken");
		
		registry.addInterceptor(refreshTokenInterceptor()).addPathPatterns("/v1/**", "/v2/**").excludePathPatterns(
				"/v1/booking/byrloc", 
				"/v1/booking/byeticket", 
				"/v1/booking/byEncryptedLink",
				"/v1/booking/byPNR",
				"/v1/verifytoken",
				"/v1/test/**",
				"/v1/common/**",
				"/v1/rebooking/**",
				"/v2/booking/byrloc", 
				"/v2/booking/byeticket", 
				"/v2/booking/byEncryptedLink",
				"/v2/booking/byPNR",
				"/v2/verifytoken",
				"/v2/test/**",
				"/v2/common/**",
				"/v2/rebooking/**"
				);
		
		registry.addInterceptor(refreshSessionInterceptor()).addPathPatterns("/v1/**", "/v2/**").excludePathPatterns(
				"/v1/booking/byrloc", 
				"/v1/booking/byeticket", 
				"/v1/booking/byEncryptedLink",
				"/v1/booking/byPNR",
				"/v1/verifytoken",
				"/v1/test/**",
				"/v1/common/**",
				"/v1/rebooking/**",
				"/v2/booking/byrloc", 
				"/v2/booking/byeticket", 
				"/v2/booking/byEncryptedLink",
				"/v2/booking/byPNR",
				"/v2/verifytoken",
				"/v2/test/**",
				"/v2/common/**",
				"/v2/rebooking/**",
				"/v2/messagehub/**"
				);
	}
}
