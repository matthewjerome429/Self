package com.cathaypacific.mbcommon.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.cathaypacific.mbcommon.applicationcache.ApplicationCacheRepository;

public class ApplicationLevelCacheConfig {
	
	@Value("${applicationCache.maxInactiveIntervalInSeconds}")
	private Integer applicationCahcheDurationSeconds;
	
	public ApplicationCacheRepository buildApplicationCacheRepository(RedisTemplate<String, Object> redisTemplate) {
		ApplicationCacheRepository applicationCacheRepository = new ApplicationCacheRepository(redisTemplate);
		applicationCacheRepository.setMaxInactiveIntervalInSeconds(applicationCahcheDurationSeconds);
		return applicationCacheRepository;
	}
	
}
