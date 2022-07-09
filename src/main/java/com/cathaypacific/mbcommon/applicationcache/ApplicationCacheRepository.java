package com.cathaypacific.mbcommon.applicationcache;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

public class ApplicationCacheRepository {
	
	private static final String REDIS_SEPARATOR=":";
	
	private static final String APPLICATION_CACHE_PREFIX="MMB_CACHE";
	
	private int maxInactiveIntervalInSeconds = 300;
	
	private RedisTemplate<String, Object> redisTemplate;
	
	public ApplicationCacheRepository(RedisTemplate<String, Object> redisTemplate){
		if(redisTemplate==null){
			throw new NullPointerException("RedisTemplate must be not null.");
		}
		this.redisTemplate = redisTemplate;
	}
   
	/**
	 * add to redis.
	 * @param mbToken
	 * @param uniqueKey
	 * @param additionalKey
	 * @param vaule
	 */
	public void add(String uniqueKey,String cacheKey, String hashKey, Object value){
		redisTemplate.opsForHash().put(generateCacheKey(cacheKey, uniqueKey), hashKey, value);
		refreshExpire(cacheKey, uniqueKey);
	}
	
	/**
	 * remove key from redis.
	 * @param uniqueKey
	 * @param cacheKey
	 * @param hashKey
	 */
	public void delete(String uniqueKey, String cacheKey, String hashKey){
		redisTemplate.opsForHash().delete(generateCacheKey(cacheKey, uniqueKey), hashKey);
	}
	
	/**
	 * remove table from redis.
	 * @param uniqueKey
	 * @param cacheKey
	 */
	public void delete(String uniqueKey, String cacheKey){
		redisTemplate.delete(generateCacheKey(cacheKey, uniqueKey));
	}
	
	/**
	 * get obj from  redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String uniqueKey, String cacheKey, String hashKey, Class<T> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Result clazz type must be not null.");
		}
		if (uniqueKey == null) {
			throw new NullPointerException("uniqueKey must be not null.");
		}
		if (cacheKey == null) {
			throw new NullPointerException("cacheKey must be not null.");
		}
		if (hashKey == null) {
			throw new NullPointerException("hashKey must be not null.");
		}
		return (T) redisTemplate.opsForHash().get(generateCacheKey(cacheKey, uniqueKey), hashKey);
	}
	
	/**
	 * refreshes the expiry time for all cache of this token
	 * @param cacheKey
	 * @param uniqueKey
	 */
	public void refreshExpire(String cacheKey, String uniqueKey){
		 
		redisTemplate.expire(generateCacheKey(cacheKey, uniqueKey), maxInactiveIntervalInSeconds, TimeUnit.SECONDS);
	}



	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}
	
	/**
	 * Generate the key
	 * @param cacheKey
	 * @param uniqueKey
	 * @return String
	 */
	private String generateCacheKey(String cacheKey, String uniqueKey) {
		return APPLICATION_CACHE_PREFIX + REDIS_SEPARATOR + cacheKey + REDIS_SEPARATOR + uniqueKey;
	}
}
