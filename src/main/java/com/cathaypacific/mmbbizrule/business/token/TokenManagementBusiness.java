package com.cathaypacific.mmbbizrule.business.token;

import com.cathaypacific.mmbbizrule.dto.request.session.cacheclear.CacheClearRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.session.cacheclear.CacheClearResultDTO;

public interface TokenManagementBusiness {
	
	/**
	 * Get expiration time of token
	 * @param mmbToken
	 */
	public Integer getExpirationTime();

	/**
	 * delete the token 
	 * @param mmbToken
	 */
	public boolean delete(String mmbToken);
	
	/**
	 * delete All Token level Cache 
	 * @param mmbToken
	 */
	public boolean deleteCache(String mmbToken);
	
	/**
	 * clear cache token level Cache 
	 * @param mmbToken
	 * @param request
	 * @return CacheClearResultDTO
	 */
	public CacheClearResultDTO clearCache(String mmbToken, CacheClearRequestDTO request);
}
