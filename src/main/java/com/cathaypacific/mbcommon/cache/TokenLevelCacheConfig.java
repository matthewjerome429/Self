package com.cathaypacific.mbcommon.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;

public class TokenLevelCacheConfig {
	
	@Value("${token.maxInactiveIntervalInSeconds}")
	private Integer tokenDurationSeconds;
	
	public MbTokenCacheRepository buildMbTokenCacheRepository(RedisTemplate<String, Object> redisTemplate) {
		MbTokenCacheRepository mbTokenCacheRepository = new MbTokenCacheRepository(redisTemplate);
		mbTokenCacheRepository.setMaxInactiveIntervalInSeconds(tokenDurationSeconds);
		return mbTokenCacheRepository;
	}
	
}
