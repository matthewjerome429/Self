package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfoHandler;
import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheHandler;
import com.cathaypacific.mbcommon.applicationcache.ApplicationCacheRepository;
import com.cathaypacific.mbcommon.cache.ApplicationLevelCacheConfig;
import com.cathaypacific.mbcommon.cache.CacheManagerConfig;
import com.cathaypacific.mbcommon.cache.TokenLevelCacheConfig;
import com.cathaypacific.mbcommon.handler.exception.GlobalExceptionBaseHandler;
import com.cathaypacific.mbcommon.handler.log.CXMDCServletFilter;
import com.cathaypacific.mbcommon.handler.log.RequestBodyLogger;
import com.cathaypacific.mbcommon.handler.log.ResponseBodyLogger;
import com.cathaypacific.mbcommon.httpclient.BookEligibilityRestTemplateConfig;
import com.cathaypacific.mbcommon.httpclient.MessageHubRestTemplateConfig;
import com.cathaypacific.mbcommon.httpclient.OLCIRestTemplateConfig;
import com.cathaypacific.mbcommon.httpclient.RTFSRestTemplateConfig;
import com.cathaypacific.mbcommon.httpclient.RestTemplateConfig;
import com.cathaypacific.mbcommon.thread.ExecutorConfig;
import com.cathaypacific.mbcommon.token.CreateTokenFilter;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.web.RequestHeaderParaCheckFilter;
import com.cathaypacific.mmbbizrule.config.cache.CacheTimeOutConfig;

@Configuration
public class OLSSApplicationConfig {
	
	@Autowired
	private CacheTimeOutConfig cacheTimeOutConfig;

	/** ---------------token config start------------ */
	@Bean
	public CreateTokenFilter createTokenFilter() {
		return new CreateTokenFilter();
	}

	/** ---------------token config end----------- */
	

	/** ---------------MDC config start------------ */
	@Bean
	public CXMDCServletFilter cxMDCServletFilter() {
		return new CXMDCServletFilter();
	}

	/** ---------------MDC config end------------ */
	
	/** ---------------header checking start------------ */
	@Bean
	public RequestHeaderParaCheckFilter requestHeaderParaCheckFilter() {
		return new RequestHeaderParaCheckFilter();
	}
	/** ---------------header checking  end----------- */
	
	/** --------------- GlobalExceptionHandler start------------ */
	@Bean
	public GlobalExceptionBaseHandler globalExceptionHandler(){
		return new GlobalExceptionBaseHandler();
	}
	
	/** ---------------GlobalExceptionHandler end------------ */
	
	/** ---------------self determine aop start------------ */
	//check login info, must add this if want to use annotation checkLoginInfo 
	@Bean
	public CheckLoginInfoHandler checkLoginInfoHandler(){
		return new CheckLoginInfoHandler();
	}
	
	//check login info, must add this if want to use annotation tokenLevelCache 
	@Bean
	public TokenLevelCacheHandler tokenLevelCacheHandler(){
		return new TokenLevelCacheHandler();
	}
	
	/** ---------------self determine aop end------------ */
	
	/** ---------------log config start------------ */

	// request body logger
	@Bean()
	@ConditionalOnProperty(name = "http.api.log", havingValue = "true")
	public RequestBodyLogger requestBodyLogger() {
		return new RequestBodyLogger();
	}

	// request body logger
	@Bean
	@ConditionalOnProperty(name = "http.api.log", havingValue = "true")
	public ResponseBodyLogger responseBodyLogger() {
		return new ResponseBodyLogger();
	}
	/** ---------------log config end------------ */
	// request body logger
	
	/** ---------------thread config start------------ */
	@Bean
	public ExecutorConfig getExecutorConfig() {
		return new ExecutorConfig();
	}
	/** ---------------thread config end------------ */
	
	/** ---------------http client config start------------ */
	@Bean()
	public RestTemplateConfig restTemplateConfig(){
		return new RestTemplateConfig();
	}
	
	@Bean
	@Primary
	public RestTemplate httpClientConfig(RestTemplateBuilder builder,RestTemplateConfig restTemplateConfig){
		return restTemplateConfig.buildRestTemplate(builder);
	}
	
	@Bean()
	public OLCIRestTemplateConfig olciRestTemplateConfig(){
		return new OLCIRestTemplateConfig();
	}
	
	@Bean("OLCIRestTemplate")
	public RestTemplate olciHttpClientConfig(RestTemplateBuilder builder, OLCIRestTemplateConfig olciRestTemplateConfig){
		return olciRestTemplateConfig.buildRestTemplate(builder);
	}

    @Bean()
    public BookEligibilityRestTemplateConfig bookEligibilityTemplateConfig() {
        return new BookEligibilityRestTemplateConfig();
    }

    @Bean("BookEligibilityRestTemplate")
    public RestTemplate bookEligibilityHttpClientConfig(RestTemplateBuilder builder,
            BookEligibilityRestTemplateConfig bookEligibilityRestTemplateConfig) {
        return bookEligibilityRestTemplateConfig.buildRestTemplate(builder);
    }

	@Bean()
	public RTFSRestTemplateConfig rtfsRestTemplateConfig(){
		return new RTFSRestTemplateConfig();
	}

	@Bean("RTFSRestTemplate")
	public RestTemplate rtfsHttpClientConfig(RestTemplateBuilder builder, RTFSRestTemplateConfig rtfsRestTemplateConfig){
		return rtfsRestTemplateConfig.buildRestTemplate(builder);
	}
	
    @Bean()
    public MessageHubRestTemplateConfig messageHubTemplateConfig() {
        return new MessageHubRestTemplateConfig();
    }

    @Bean("MessageHubRestTemplate")
    public RestTemplate messageHubHttpClientConfig(RestTemplateBuilder builder,
            MessageHubRestTemplateConfig messageHubRestTemplateConfig) {
        return messageHubRestTemplateConfig.buildRestTemplate(builder);
    }
	/** ---------------http client config end------------- */
	
	
	/** ---------------cache config start------------ */
	@Bean
	public CacheManagerConfig cacheManagerConfig() {
		return new CacheManagerConfig();
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, CacheManagerConfig cacheManagerConfig) {
		return cacheManagerConfig.buildRedisTemplate(redisConnectionFactory);
	}
	
	@Bean
	public CacheManager cacheManager(RedisTemplate<String, ?> redisTemplate, CacheManagerConfig cacheManagerConfig) {
		return cacheManagerConfig.buildCacheManager(redisTemplate, cacheTimeOutConfig.getCacheTimeoutExpiresMap(), cacheTimeOutConfig.getDefaultExpiration());
	}
	
	@Bean
	public KeyGenerator keyGenerator(CacheManagerConfig cacheManagerConfig) {
		return cacheManagerConfig.keyGenerator();
	}
	
	@Bean(name = "shareKeyGenerator")
	public KeyGenerator shareKeyGenerator(CacheManagerConfig cacheManagerConfig) {
		return cacheManagerConfig.shareKeyGenerator();
	}
	
	@Bean
	public TokenLevelCacheConfig tokenLevelCacheConfig() {
		return new TokenLevelCacheConfig();
	}
	
	@Bean
	public MbTokenCacheRepository mbTokenCacheConfig(RedisTemplate<String, Object> redisTemplate, TokenLevelCacheConfig tokenLevelCacheConfig) {
		return tokenLevelCacheConfig.buildMbTokenCacheRepository(redisTemplate);
	}
	
	@Bean
	public ApplicationLevelCacheConfig applicationLevelCacheConfig() {
		return new ApplicationLevelCacheConfig();
	}
	
	@Bean
	public ApplicationCacheRepository applicationCacheConfig(RedisTemplate<String, Object> redisTemplate, ApplicationLevelCacheConfig applicationLevelCacheConfig) {
		return applicationLevelCacheConfig.buildApplicationCacheRepository(redisTemplate);
	}
	/** ---------------cache config end------------ */
	
}
