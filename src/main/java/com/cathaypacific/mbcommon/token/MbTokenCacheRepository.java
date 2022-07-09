package com.cathaypacific.mbcommon.token;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class MbTokenCacheRepository {
	
	private static final String REDIS_SEPARATOR=":";
	
	private static final String TOKEN_CACHE_PREFIX="MMB_CACHE"+REDIS_SEPARATOR+"TOKEN";
	
	private int maxInactiveIntervalInSeconds = 1800;
	
	private RedisTemplate<String, Object> redisTemplate;
	
	public MbTokenCacheRepository(RedisTemplate<String, Object> redisTemplate){
		if(redisTemplate==null){
			throw new NullPointerException("RedisTemplate must be not null.");
		}
		this.redisTemplate = redisTemplate;
	}
   
	/**
	 * add to redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	public void add(String mbToken,TokenCacheKeyEnum key,String additionalKey,Object value){
		redisTemplate.opsForHash().put(generateTokenKey(mbToken),generateHashKey(key, additionalKey), value);
		refreshExpire(mbToken);
	}
	
	/**
	 * remove key from redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	public void delete(String mbToken,TokenCacheKeyEnum key,String additionalKey){
		redisTemplate.opsForHash().delete(generateTokenKey(mbToken),generateHashKey(key, additionalKey));
	}
	
	/**
	 * remove tabel from redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	public void delete(String mbToken){
		redisTemplate.delete(generateTokenKey(mbToken));
	}
	
	/**
	 * remove tabel from redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	public void deleteKeys(String mbToken, Object... keys) {
		if (keys != null && keys.length > 0) {
			redisTemplate.opsForHash().delete(generateTokenKey(mbToken), keys);
		}

	}
	/**
	 * Delete all cache, the keys in token start with "cache"
	 * @param mmbToken
	 */
	public void deleteAllCache(String mmbToken){
		Set<String> cacheKeys = strKeys(mmbToken).stream().filter(key->key.startsWith("cache")).collect(Collectors.toSet());
		if(!CollectionUtils.isEmpty(cacheKeys)){
			deleteKeys(mmbToken, cacheKeys.toArray());
		}
		
	}
	/**
	 * get obj from  redis.
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @param vaule
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String mbToken, TokenCacheKeyEnum key, String additionalKey, Class<T> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Result clazz type must be not null.");
		}
		if (mbToken == null) {
			throw new NullPointerException("mbToken must be not null.");
		}
		if (key == null) {
			throw new NullPointerException("key must be not null.");
		}
		return (T) redisTemplate.opsForHash().get(generateTokenKey(mbToken),generateHashKey(key, additionalKey));
	}
	
	/**
	 * Check if the key stored in token
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @return
	 */
	public boolean hasTokenKey(String mbToken, TokenCacheKeyEnum key, String additionalKey) {
		if (mbToken == null) {
			throw new NullPointerException("mbToken must be not null.");
		}
		if (key == null) {
			throw new NullPointerException("key must be not null.");
		}
		return redisTemplate.opsForHash().hasKey(generateTokenKey(mbToken), generateHashKey(key, additionalKey));
	}
	
	/**
	 * check the token exist, only check token instead of all
	 * 
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @return
	 */
	public boolean hasToken(String mbToken) {

		return redisTemplate.hasKey(generateTokenKey(mbToken));
	}
	
	/**
	 *  Get all keys(String) of the token
	 * @param mbToken
	 * @return
	 */
	public Set<Object> keys(String mbToken) {

		return redisTemplate.opsForHash().keys(generateTokenKey(mbToken));
	}
	
	/**
	 *  Get all keys(String) of the token
	 * @param mbToken
	 * @return
	 */
	public Set<String> strKeys(String mbToken) {

		return Optional.ofNullable(keys(mbToken)).orElseGet(Collections::emptySet).stream().map(obj->(String)obj).collect(Collectors.toSet());
	}
	/**
	 * refreshes the expiry time for all cache of this token
	 * @param mbToken
	 */
	public void refreshExpire(String mbToken){
		 
		redisTemplate.expire(generateTokenKey(mbToken), maxInactiveIntervalInSeconds, TimeUnit.SECONDS);
	}



	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}
	
	/**
	 * Generate the unique key for token  
	 * @param mbToken
	 * @return
	 */
	private String generateTokenKey(String mbToken) {
		return TOKEN_CACHE_PREFIX + REDIS_SEPARATOR + mbToken;
	}
	
	/**
	 * Generate hash key for token  
	 * @param mbToken
	 * @return
	 */
	private String generateHashKey(TokenCacheKeyEnum key, String additionalKey) {
		return  StringUtils.isEmpty(additionalKey) ? key.getKey():key.getKey()+"_"+additionalKey;
	}
}
