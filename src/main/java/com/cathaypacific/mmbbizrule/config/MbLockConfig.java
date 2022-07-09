package com.cathaypacific.mmbbizrule.config;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.cathaypacific.mbcommon.cache.TokenLevelCacheConfig;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.MbTokenLockRepository;

@Configuration
public class MbLockConfig {

	public class RedisConfig {
		private String host;
		private String port;
		private int database;
		private String password;
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public int getDatabase() {
			return database;
		}
		public void setDatabase(int database) {
			this.database = database;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisConfig redisConfig() {
		return new RedisConfig();
	}
	
	@Bean
	public MbTokenCacheLockRepository mbTokenCacheLock() {
		return new MbTokenCacheLockRepository();
	}
	
	@Bean
	public MbTokenLockRepository mbTokenLockRepository(RedisTemplate<String, Object> redisTemplate, TokenLevelCacheConfig tokenLevelCacheConfig) {
		return new MbTokenLockRepository(redisTemplate);
	}

	@Bean
	public RedissonClient redisson(RedisConfig redisConfig) {
		Config config = new Config();
		SingleServerConfig singleServerConfig = 
				config.useSingleServer()
				.setAddress(String.format("redis://%s:%s", redisConfig.getHost(), redisConfig.getPort()))
				.setDatabase(redisConfig.getDatabase());
		
		if (!StringUtils.isEmpty(redisConfig.getPassword())) {
			singleServerConfig.setPassword(redisConfig.getPassword());
		}

		return Redisson.create(config);
	}
	
}
