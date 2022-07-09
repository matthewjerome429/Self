package com.cathaypacific.mbcommon.token;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;

@Deprecated
public class MbTokenLock {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(MbTokenLock.class);
	
	/**
	 * Use OpenShift timeout as default value.
	 * By default, a thread should not hold a lock over 30 seconds.
	 */
	private static final int DEFAULT_EXPIRE_MILLIS = 30000;
	
	/**
	 * By default, a thread retry to acquire lock again after 1 second.
	 */
	private static final int DEFAULT_RETRY_MILLIS = 1000;
	
	/**
	 * Use OpenShift timeout as default value.
	 * By default, a thread block 30 seconds to try acquiring lock.
	 */
	private static final int DEFAULT_TIMEOUT_MILLIS = 30000;

	private MbTokenLockRepository mbTokenLockRepository;

	private String mbToken;
	
	private TokenLockKeyEnum tokenLockKey;
	
	private String additionalKey;
	
	private int expireMillis;
	
	private int retryMillis;

	private String lockedValue;

	public MbTokenLock(MbTokenLockRepository mbTokenLockRepository, String mbToken, TokenLockKeyEnum tokenLockKey,
			String additionalKey, int expireMillis, int retryMillis) {
		
		this.mbTokenLockRepository = mbTokenLockRepository;
		this.mbToken = mbToken;
		this.tokenLockKey = tokenLockKey;
		this.additionalKey = additionalKey;
		this.expireMillis = expireMillis;
		this.retryMillis = retryMillis;
	}

	public MbTokenLock(MbTokenLockRepository mbTokenLockRepository, String mbToken, TokenLockKeyEnum tokenLockKey,
			String additionalKey) {

		this(mbTokenLockRepository, mbToken, tokenLockKey, additionalKey, DEFAULT_EXPIRE_MILLIS, DEFAULT_RETRY_MILLIS);
	}
	
	/**
	 * Try to acquire lock once.
	 * 
	 * @return true if lock is acquired, otherwise false.
	 */
	public boolean tryLock() {
		
		if (lockedValue != null) {
			throw new UnsupportedOperationException("It has been locked.");
		}
		
		this.lockedValue = this.mbTokenLockRepository.lock(mbToken, tokenLockKey, additionalKey, expireMillis);
		return lockedValue != null;
	}

	/**
	 * Try to acquire lock, and retry during specified time interval.
	 * 
	 * @param millis retry time interval.
	 * @return  true if lock is acquired, otherwise false.
	 * @throws UnexpectedException
	 */
	public boolean tryLock(int millis) throws UnexpectedException {
		
		long timeoutMillis = System.currentTimeMillis() + millis;
		
		while (!tryLock()) {
			if (timeoutMillis < System.currentTimeMillis()) {
				LOGGER.warn("Thread cannot get lock before timeout.");
				return false;
			}
			
			try {
				LOGGER.info("Block thread " + retryMillis + " milliseconds to retry acquire lock");
				Thread.sleep(retryMillis);
			} catch (InterruptedException e) {
				throw new UnexpectedException("Thread is interrupted when wait for lock",
						new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
			}
		}
		
		return true;
	}
	
	/**
	 * Keep acquiring lock until acquired, fail if not acquired before timeout.
	 * 
	 * @throws UnexpectedException
	 */
	public void lock() throws UnexpectedException {
		
		if (!tryLock(DEFAULT_TIMEOUT_MILLIS)) {
			throw new UnexpectedException("Thread cannot get lock",
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
	}

	/**
	 * Release acquired lock.
	 */
	public void unlock() {
		
		if (lockedValue == null) {
			throw new UnsupportedOperationException("It has been unlocked or not been locked yet.");
		}

		this.mbTokenLockRepository.unlock(mbToken, tokenLockKey, additionalKey, lockedValue);
		lockedValue = null;
	}

}
