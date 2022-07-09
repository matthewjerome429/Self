package com.cathaypacific.mmbbizrule.business.token.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.token.TokenLockKeyEnum;
import com.cathaypacific.mmbbizrule.business.token.TokenManagementBusiness;
import com.cathaypacific.mmbbizrule.dto.request.session.cacheclear.CacheClearRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.session.cacheclear.CacheClearResultDTO;

@Service
public class TokenManagementBusinessImpl implements TokenManagementBusiness {

	@Value("${token.maxInactiveIntervalInSeconds}")
	private Integer tokenDurationSeconds;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;
	
	@Override
	public Integer getExpirationTime() {
		return tokenDurationSeconds;
	}

	@Override
	public boolean delete(String mmbToken) {
		
		mbTokenCacheRepository.delete(mmbToken);
		//return true if no exception
		return true;

	}

	@Override
	public boolean deleteCache(String mmbToken) {
		mbTokenCacheRepository.deleteAllCache(mmbToken);
		return true;
	}

	@Override
	public CacheClearResultDTO clearCache(String mmbToken, CacheClearRequestDTO request) {
		CacheClearResultDTO result = new CacheClearResultDTO();

		if (request.isClearPnr()) {
			mbTokenCacheRepository.delete(mmbToken, TokenCacheKeyEnum.PNR, request.getRloc());
			result.setPnrCleared(true);
		}
		
		if (request.isClearTempSeat()) {
			mbTokenCacheLockRepository.delete(mmbToken, TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, request.getRloc());
			result.setTempSeatCleared(true);
		}
		return result;
	}
}
