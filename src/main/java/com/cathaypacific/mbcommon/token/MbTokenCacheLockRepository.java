package com.cathaypacific.mbcommon.token;

import org.springframework.beans.factory.annotation.Autowired;

import com.cathaypacific.mbcommon.loging.LogAgent;

public class MbTokenCacheLockRepository {
	private static final LogAgent LOGGER = LogAgent.getLogAgent(MbTokenCacheLockRepository.class);
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private MbTokenLockRepository mbTokenLockRepository;
	
	public MbTokenCacheLockRepository() {
	}
	
	/**
	 * get obj from  redis.
	 * @param mmbToken
	 * @param cacheKey
	 * @param lockKey
	 * @param rloc
	 * @param clazz
	 * @return
	 */
	public <T> T get(String mbToken, TokenCacheKeyEnum cacheKey, TokenLockKeyEnum lockKey, String rloc, Class<T> clazz) {
		MbTokenLock lock = new MbTokenLock(mbTokenLockRepository, mbToken, lockKey, rloc);
		try {
			LOGGER.info(String.format("To lock [%s] , before get() rloc:[%s]", cacheKey.getKey(), rloc));
			lock.lock();
			LOGGER.info(String.format("Locked [%s] , before get() rloc:[%s]", cacheKey.getKey(), rloc));
			return (T) mbTokenCacheRepository.get(mbToken, cacheKey, rloc, clazz);
		} catch (Exception e) {
			LOGGER.info(String.format("To lock failed [%s] , before get() rloc:[%s]", cacheKey.getKey(), rloc));
			return null;
		} finally {
			lock.unlock();
			LOGGER.info(String.format("Unlocked [%s] , after get() rloc:[%s]", cacheKey.getKey(), rloc));
		}
	}
	
	/**
	 * remove key from redis.
	 * @param mbToken
	 * @param cacheKey
	 * @param lockKey
	 * @param rloc
	 */
	public void delete(String mbToken, TokenCacheKeyEnum cacheKey, TokenLockKeyEnum lockKey, String rloc) {
		MbTokenLock lock = new MbTokenLock(mbTokenLockRepository, mbToken, lockKey, rloc);
		try {
			LOGGER.info(String.format("To lock [%s] , before delete() rloc:[%s]", cacheKey.getKey(), rloc));
			lock.lock();
			LOGGER.info(String.format("Locked [%s] , before delete() rloc:[%s]", cacheKey.getKey(), rloc));
			mbTokenCacheRepository.delete(mbToken, cacheKey, rloc);
		} catch (Exception e) {
			LOGGER.info(String.format("To lock failed [%s] , before delete() rloc:[%s]", cacheKey.getKey(), rloc));
		} finally {
			lock.unlock();
			LOGGER.info(String.format("Unlocked [%s] , after delete() rloc:[%s]", cacheKey.getKey(), rloc));
		}
	}
	
	
	/**
	 * add to redis.
	 * @param mmbToken
	 * @param cacheKey
	 * @param lockKey
	 * @param rloc
	 * @param value
	 */
	public void add(String mbToken, TokenCacheKeyEnum cacheKey, TokenLockKeyEnum lockKey, String rloc, Object value){
		MbTokenLock lock = new MbTokenLock(mbTokenLockRepository, mbToken, lockKey, rloc);
		try {
			LOGGER.info(String.format("To lock [%s] , before add() rloc:[%s]", cacheKey.getKey(), rloc));
			lock.lock();
			LOGGER.info(String.format("Locked [%s] , before add() rloc:[%s]", cacheKey.getKey(), rloc));
			mbTokenCacheRepository.add(mbToken, cacheKey, rloc, value);
		} catch (Exception e) {
			LOGGER.info(String.format("To lock failed [%s] , before add() rloc:[%s]", cacheKey.getKey(), rloc));
		} finally {
			lock.unlock();
			LOGGER.info(String.format("Unlocked [%s] , after add() rloc:[%s]", cacheKey.getKey(), rloc));
		}
		
	}
}
