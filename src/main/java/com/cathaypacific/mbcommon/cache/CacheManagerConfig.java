package com.cathaypacific.mbcommon.cache;

import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.cathaypacific.mbcommon.utils.EncodeUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheManagerConfig extends CachingConfigurerSupport {

	public CacheManager buildCacheManager(RedisTemplate<String, ?> redisTemplate,
			Map<String, Long> cacheTimeoutExpiresMap, Long defaultExpiration) {
		RedisCacheManager rcManager = new RedisCacheManager(redisTemplate);
		rcManager.afterPropertiesSet();
		rcManager.setExpires(cacheTimeoutExpiresMap);
		rcManager.setDefaultExpiration(defaultExpiration);
		rcManager.setUsePrefix(true);
		rcManager.setCachePrefix(new MMBRedisCachePrefix());
		return rcManager;
	}

    public RedisTemplate<String, Object> buildRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
	/**
	 * over write spring keyGenerator with string to resolve rides key messy
	 * code.
	 */
	@Override
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(".");
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append("-");
				sb.append(EncodeUtil.encoderByMd5UTF8(obj));
			}
			return EncodeUtil.encoderByMd5UTF8(sb.toString());
		};
	}

	/**
	 * This KeyGenerator used for the cache which need share in different
	 * microservice, the key use class simple name instead of full name.
	 * 
	 * @return
	 */
	public KeyGenerator shareKeyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getSimpleName());
			sb.append(".");
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append("-");
				sb.append(EncodeUtil.encoderByMd5UTF8(obj));
			}
			return sb.toString();
		};
	}

	class MMBRedisCachePrefix implements RedisCachePrefix {
		private final RedisSerializer<String> serializer = new StringRedisSerializer();
		private static final String DELIMITER = ":";

		@Override
		public byte[] prefix(String cacheName) {
			return serializer.serialize("MMB_CACHE".concat(DELIMITER).concat(cacheName).concat(DELIMITER));
		}
	}
	
}
