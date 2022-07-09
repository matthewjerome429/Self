package com.cathaypacific.mmbbizrule.cxservice.dprebook.service;

import com.cathaypacific.mmbbizrule.cxservice.dprebook.model.request.EncryptDeepLinkRequest;
import com.cathaypacific.mmbbizrule.cxservice.dprebook.model.response.EncryptDeepLinkResponse;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to dp_rebook
 * @author fangfang.zhang
 * @date Feb 16, 2019 5:19:37 PM
 * @version V1.0
 */
public interface DpRebookService {
	
	/**
	 * 
	 * @Description update seat
	 * @param
	 * @return RetrievePnrBooking
	 * @author fangfang.zhang
	 * @param map 
	 */
	public EncryptDeepLinkResponse getRescheduleDeeplink(EncryptDeepLinkRequest requestDTO);
	
}
