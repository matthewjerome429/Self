package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mmbbizrule.dto.response.tokendata.store.StoreTokenDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.tokendata.transfer.TransferTokenDataResponseDTO;

public interface TokenDataBusiness {
	
	/**
	 * store the token data in redis and return a key to retrieve the data
	 * @param rloc 
	 * @param mmbToken 
	 * @return StoreTokenDataResponseDTO
	 */
	public StoreTokenDataResponseDTO storeTokenData(String mmbToken, String rloc);
	
	/**
	 * transfer token data to current token
	 */
	public TransferTokenDataResponseDTO transferTokenData(String mmbToken, String cacheKey);
}
