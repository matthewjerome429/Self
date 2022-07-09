package com.cathaypacific.mmbbizrule.cxservice.memberaward.service;

import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.MemberAwardRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.MemberAwardResponse;

public interface MemberAwardService {
	/**
	 * 
	* @Description get member award
	* @param
	* @return MemberAwardResponse
	* @author haiwei.jia
	 */
	public MemberAwardResponse getMemberAward(MemberAwardRequest request );
}
