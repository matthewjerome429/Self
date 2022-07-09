package com.cathaypacific.mbcommon.token;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;

public class MbTokenLockV2 {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(MbTokenLockV2.class);

	private final RedissonClient redisson;

	private final MbTokenLockTypeV2 type;

	private final String key;

	private final long leaseMillis;

	private final long waitMillis;

	private RLock acquiredLock;

	public static MbTokenLockV2 create(MbTokenLockConfigV2 config) {
		return new MbTokenLockV2(config);
	}

	private MbTokenLockV2(MbTokenLockConfigV2 config) {
		Objects.requireNonNull(config);

		this.redisson = config.redisson;
		this.type = config.type;
		this.key = config.key;
		this.leaseMillis = config.leaseMillis;
		this.waitMillis = config.waitMillis;
	}

	public boolean isLocked() {
		return this.acquiredLock != null;
	}

	private boolean tryLock(RLock lock, long waitMills, long leaseMillis) throws UnexpectedException {
		boolean isLocked = false;
		try {
			LOGGER.debug(String.format("Thread is acquiring %s (%s).", this.type.getName(), this.key));
			isLocked = lock.tryLock(waitMills, leaseMillis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.warn(String.format("Thread is interrupted when wait for %s (%s).", this.type.getName(), this.key));
		}
		if (isLocked) {
			this.acquiredLock = lock;
			LOGGER.debug(String.format("Thread acquired %s (%s).", this.type.getName(), this.key));
		} else {
			throw new UnexpectedException(String.format("Thread cannot acquire %s (%s).", this.type.getName(), this.key),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		return isLocked;
	}

	private boolean tryLock(long waitMills, long leaseMillis) throws UnexpectedException {
		if (isLocked()) {
			throw new UnsupportedOperationException("It has been locked and cannot be locked again.");
		}

		RLock lock;
		RReadWriteLock rwLock;
		switch (this.type) {
		case LOCK:
			lock = redisson.getLock(key);
			break;

		case READ_LOCK:
			rwLock = redisson.getReadWriteLock(key);
			lock = rwLock.readLock();
			break;

		case WRITE_LOCK:
			rwLock = redisson.getReadWriteLock(key);
			lock = rwLock.writeLock();
			break;

		default:
			LOGGER.warn(String.format("Unknown lock type %s", type.getName()));
			lock = redisson.getLock(key);
		}

		return tryLock(lock, waitMills, leaseMillis);
	}

	/**
	 * Try to acquire lock during wait time.<br>
	 * If lock is acquired, TTL is lease time.
	 * 
	 * @return  true if lock is acquired, otherwise false.
	 * @throws UnexpectedException
	 */
	public boolean tryLock() throws UnexpectedException {
		return tryLock(this.waitMillis, this.leaseMillis);
	}
	
	/**
	 * Keep acquiring lock until acquired, fail if not acquired during wait time.
	 * If lock is acquired, TTL is lease time.
	 * 
	 * @throws UnexpectedException
	 */
	public void lock() throws UnexpectedException {
		
		if (!tryLock()) {
			throw new UnexpectedException("Thread cannot acquire lock",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}

	/**
	 * Release acquired lock.
	 */
	public void unlock() {
		if (!isLocked()) {
			throw new UnsupportedOperationException("It has been unlocked or not been locked yet.");
		}

		this.acquiredLock.unlock();
		this.acquiredLock = null;
		LOGGER.debug(String.format("Thread released %s (%s).", this.type.getName(), key));
	}

}
