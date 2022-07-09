package com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;

public interface ConvertOARlocService {
	/**
	 * 
	* @Description get corresponding 1A RLOC by gds rloc
	* @param
	* @return String
	* @author fengfeng.jiang
	 * @throws ExpectedException 
	 */
	public String getRlocByGDSRloc(String gdsRloc) throws BusinessBaseException;
}
