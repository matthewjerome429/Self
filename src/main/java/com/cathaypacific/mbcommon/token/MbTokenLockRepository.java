package com.cathaypacific.mbcommon.token;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

@Deprecated
public class MbTokenLockRepository {

	private static final String REDIS_SEPARATOR = ":";

	private static final String TOKEN_LOCK_PREFIX = "MMB_LOCK" + REDIS_SEPARATOR + "TOKEN";

	private RedisTemplate<String, Object> redisTemplate;

	public MbTokenLockRepository(RedisTemplate<String, Object> redisTemplate) {
		if (redisTemplate == null) {
			throw new NullPointerException("RedisTemplate must be not null.");
		}
		this.redisTemplate = redisTemplate;
	}

	/**
	 * Add lock value if lock is free.<br>
	 * Free lock might be not existing key or expired lock value.
	 * 
	 * @param mbToken
	 * @param tokenLockKey
	 * @param additionalKey
	 * @param expireMillis
	 * @return locked value
	 */
	public String lock(String mbToken, TokenLockKeyEnum tokenLockKey, String additionalKey, int expireMillis) {
	
		String key = generateLockKey(mbToken, tokenLockKey, additionalKey);
		
		long expireTimeMillis = System.currentTimeMillis() + expireMillis;
		String newValue = String.valueOf(expireTimeMillis);
		
		/*
		 * Set value when key doesn't exist (Redis SETNX)
		 */
		if (redisTemplate.opsForValue().setIfAbsent(key, newValue)) {

			/*
			 * If value can be successfully set, the key does not exist.
			 * The set value is time stamp of lock expiry time.
			 */
			
			// Set live time of key. To make key not deleted by Redis before its lock expiry time, 
			// live time is double of key expireMillis.
			redisTemplate.expire(key, expireMillis * 2L, TimeUnit.MILLISECONDS);
			
			return newValue;
		}
		
		/*
		 * Judgment lock expiration. Prevent the original thread being abnormal without unlock operation to avoid deadlock.
		 * 
		 * If the lock expires, it is free as not existing.
		 */
		String currentValue = (String) redisTemplate.opsForValue().get(key);
		if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// Set new value and get the old value atomically (Redis GETSET)
			String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, newValue);
			
			/*
			 * Suppose more than one threads come in at the same time because the key is occupied and the lock expires.
			 * 
			 * Given the current value of "get" before is A and the lock time has expired. All threads try to set value B.
			 * Then the "getAndSet" inside will only be executed only once. After that, the old value will be changed to B.
			 * The old value obtained by only one thread will be A, and the value obtained by other threads will be B.
			 * 
			 * The thread that obtains old value (from "getAndSet") equals current value (from "get") is 
			 * the one obtains lock successfully.
			 */
			if (oldValue != null && oldValue.equals(currentValue)) {
				return newValue;
			}
		}
		
		return null;
	}

	/**
	 * Remove locked value if lock is not changed.<br>
	 * If lock has be expired and acquired by other thread, do nothing.
	 * 
	 * @param mbToken
	 * @param tokenLockKey
	 * @param additionalKey
	 * @param value locked value
	 */
	public void unlock(String mbToken, TokenLockKeyEnum tokenLockKey, String additionalKey, String value) {
		
		String key = generateLockKey(mbToken, tokenLockKey, additionalKey);
		
		/*
		 * If locked value is current value, indicating the lock is still held by current thread, then remove it.
		 */
		String currentValue = (String) redisTemplate.opsForValue().get(key);
		if (currentValue != null && currentValue.equals(value)) {
			redisTemplate.delete(key);
		}
	}
	
	/**
	 * Generate lock key for token.
	 * 
	 * @param mbToken
	 * @param key
	 * @param additionalKey
	 * @return
	 */
	private String generateLockKey(String mbToken, TokenLockKeyEnum tokenLockKey, String additionalKey) {
		String key = TOKEN_LOCK_PREFIX + REDIS_SEPARATOR + mbToken + REDIS_SEPARATOR + tokenLockKey.getKey();
		return StringUtils.isEmpty(additionalKey) ? key : key + "_" + additionalKey;
	}
}
