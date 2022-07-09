package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.applicationcache.ApplicationCacheKeyEnum;
import com.cathaypacific.mbcommon.applicationcache.ApplicationCacheRepository;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.TokenDataBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.dto.response.tokendata.store.StoreTokenDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.tokendata.transfer.TransferTokenDataResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import org.springframework.util.StringUtils;

@Service
public class TokenDataBusinessImpl implements TokenDataBusiness{
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private ApplicationCacheRepository applicationCacheRepository;
	
	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;

	@Override
	public StoreTokenDataResponseDTO storeTokenData(String mmbToken, String rloc) {
		// generate unique key
		String uniqueKey = UUID.randomUUID().toString().replace("-", "");
		
		// store mask info
		storeMaskInfo(rloc, uniqueKey);
		
		// store member temporarily linked booking info
		storeTempLinkedBookingInfo(uniqueKey, mmbToken);

		// store the OLCI session cookie
		storeOLCISessionCookie(rloc, uniqueKey, mmbToken);
		
		// store 1A RLOC
		storeOneARloc(uniqueKey, rloc);
		
		StoreTokenDataResponseDTO responseDTO = new StoreTokenDataResponseDTO();
		responseDTO.setKey(uniqueKey);
		return responseDTO;
	}

	@Override
	public TransferTokenDataResponseDTO transferTokenData(String mmbToken, String cacheKey) {
		// get 1A RLOC from stored data
		String oneARloc = getOneARlocFromStoredData(cacheKey);
		
		// transfer mask info
		transferMaskInfo(mmbToken, oneARloc, cacheKey);
		
		// transfer member temporarily linked booking info
		transferTempLinkedBookingInfo(cacheKey, mmbToken);

		// transfer OLCI session cookie to the new Token
		transferOLCISessionCookie(cacheKey, oneARloc, mmbToken);
		
		// delete the previous cache
		applicationCacheRepository.delete(cacheKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey());
		
		TransferTokenDataResponseDTO responseDTO = new TransferTokenDataResponseDTO();
		responseDTO.setSuccess(true);
		return responseDTO;
	}

	/**
	 * get 1A rloc from stored data
	 * @param cacheKey
	 * @return String
	 */
	private String getOneARlocFromStoredData(String cacheKey) {
		return applicationCacheRepository.get(cacheKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), MMBBizruleConstants.APPLICATION_TOKEN_DATA_CACHE_KEY_ONE_A_RLOC, String.class);
	}

	/**
	 * transfer mask info from application cache to current token cache
	 * @param mmbToken
	 * @param rloc
	 * @param cacheKey
	 */
	private void transferMaskInfo(String mmbToken, String rloc, String cacheKey) {
		// get maskInfo stored in redis using the cacheKey
		@SuppressWarnings("unchecked")
		List<MaskInfo> maskInfos = applicationCacheRepository.get(cacheKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.MASK_INFO.getKey(), ArrayList.class);
		
		//transfer the maskInfo to current token
		if (!CollectionUtils.isEmpty(maskInfos)) {
			mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.MASK_INFO, rloc, maskInfos);
		}
	}   
	
	/**
	 * transfer member temporarily linked booking info from application cache to current token cache
	 * @param cacheKey
	 * @param mmbToken
	 */
	private void transferTempLinkedBookingInfo(String cacheKey, String mmbToken) {
		// get temporarily linked bookings stored in redis using the cacheKey
		@SuppressWarnings("unchecked")
		List<TempLinkedBooking> tempLinkedBookings = applicationCacheRepository.get(cacheKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.LIKEND_BOOKING.getKey(), ArrayList.class);
		
		// transfer temporarily linked bookings to current token
		if (!CollectionUtils.isEmpty(tempLinkedBookings)) {
			tempLinkedBookingRepository.addLinkedBookingsToCache(tempLinkedBookings, mmbToken);
		}
	}

	/**
	 * transfer olci session cookie info from application cache to current token cache
	 * @param cacheKey
	 * @param mmbToken
	 */
	private void transferOLCISessionCookie(String cacheKey, String rloc, String mmbToken) {
		// get the olci session cookie from the application cache repository
		@SuppressWarnings("unchecked")
		String olciSessionCookie = applicationCacheRepository.get(cacheKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.OLCI_SESSION_COOKIE.getKey(), String.class);

		// transfer temporarily linked bookings to current token
		if (!StringUtils.isEmpty(olciSessionCookie)) {
			mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.OLCI_SESSION_COOKIE, rloc, olciSessionCookie);
		}
	}
	

	/**
	 * store mask info
	 * @param rloc
	 * @param uniqueKey
	 */
	private void storeMaskInfo(String rloc, String uniqueKey) {
		// get mask info of current token
		@SuppressWarnings("unchecked")
		List<MaskInfo> maskInfos = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.MASK_INFO, rloc, ArrayList.class);
		
		// store token data in application cache level
		applicationCacheRepository.add(uniqueKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.MASK_INFO.getKey(), maskInfos);
	}
	
	/**
	 * store member temporarily linked booking info
	 * @param uniqueKey
	 * @param mmbToken
	 */
	private void storeTempLinkedBookingInfo(String uniqueKey, String mmbToken) {
		// get linked booking from token cache
		List<TempLinkedBooking> tempLinkedBookings =  tempLinkedBookingRepository.getLinkedBookings(mmbToken);

		if (!CollectionUtils.isEmpty(tempLinkedBookings)) {
			// store linked booking to application cache
			applicationCacheRepository.add(uniqueKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.LIKEND_BOOKING.getKey(), tempLinkedBookings);
		}
	}


	/**
	 * store member temporarily linked OLCI session cookie
	 * @param rloc
	 * @param uniqueKey
	 * @param mmbToken
	 */
	private void storeOLCISessionCookie(String rloc, String uniqueKey, String mmbToken) {
		// get the stored sessionOLCI session cookie from the mbTokenCache
		String olciSessionCookie = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.OLCI_SESSION_COOKIE, rloc, String.class);

		if (!StringUtils.isEmpty(olciSessionCookie)) {
			// store linked booking to application cache
			applicationCacheRepository.add(uniqueKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), TokenCacheKeyEnum.OLCI_SESSION_COOKIE.getKey(), olciSessionCookie);
		}
	}

	/**
	 * store 1A RLOC
	 * @param uniqueKey
	 */
	private void storeOneARloc(String uniqueKey, String oneARloc) {
		// store token data in application cache level
		applicationCacheRepository.add(uniqueKey, ApplicationCacheKeyEnum.TOKEN_DATA.getKey(), MMBBizruleConstants.APPLICATION_TOKEN_DATA_CACHE_KEY_ONE_A_RLOC, oneARloc);

	}
}
