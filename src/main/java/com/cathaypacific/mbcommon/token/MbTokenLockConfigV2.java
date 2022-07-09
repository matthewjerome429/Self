package com.cathaypacific.mbcommon.token;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;

public class MbTokenLockConfigV2 {

	private static final String REDIS_SEPARATOR = ":";

	private static final String TOKEN_LOCK_PREFIX = "MMB_LOCK" + REDIS_SEPARATOR + "TOKEN";

	/**
	 * By default, a thread can hold a lock at most 30 seconds.
	 */
	private static final long DEFAULT_LEASE_MILLIS = 30000;

	/**
	 * By default, a thread blocks at most 30 seconds to try acquiring lock.
	 */
	private static final long DEFAULT_WAIT_MILLIS = 30000;

	RedissonClient redisson;
	
	String mbToken;

	String key;
	
	MbTokenLockTypeV2 type = MbTokenLockTypeV2.LOCK;

	long leaseMillis = DEFAULT_LEASE_MILLIS;

	long waitMillis = DEFAULT_WAIT_MILLIS;

	public MbTokenLockConfigV2(RedissonClient redisson, String mbToken, TokenLockKeyEnum tokenLockKey) {
		Objects.requireNonNull(redisson);
		Objects.requireNonNull(mbToken);
		Objects.requireNonNull(tokenLockKey);
		this.redisson = redisson;
		this.mbToken = mbToken;
		this.key = TOKEN_LOCK_PREFIX + REDIS_SEPARATOR + mbToken + REDIS_SEPARATOR + tokenLockKey.getKey();
	}

	/**
	 * Add lock additional key in Redis.
	 *
	 * @param additionalKey
	 * @return
	 */
	public void addAdditionalKey(String additionalKey) {
		key = StringUtils.isEmpty(additionalKey) ? key : key + "_" + additionalKey;
	}

	public MbTokenLockTypeV2 getType() {
		return type;
	}

	public void setType(MbTokenLockTypeV2 type) {
		this.type = type;
	}

	public long getLeaseMillis() {
		return leaseMillis;
	}

	public void setLeaseMillis(long leaseMillis) {
		this.leaseMillis = leaseMillis;
	}

	public long getWaitMillis() {
		return waitMillis;
	}

	public void setWaitMillis(long waitMillis) {
		this.waitMillis = waitMillis;
	}
	
}
