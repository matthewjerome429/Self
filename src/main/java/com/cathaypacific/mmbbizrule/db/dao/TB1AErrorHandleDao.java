package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.TB1AErrorHandle;
import com.cathaypacific.mmbbizrule.db.model.TB1AErrorHandleKey;

public interface TB1AErrorHandleDao extends CrudRepository<TB1AErrorHandle, TB1AErrorHandleKey>{
	
	/**
	 * Get mostly matched record, the sort priority
	 * @param appCode
	 * @param actionCode
	 * @param oneAWsCall
	 * @param errorCode
	 * @return
	 */
	@Query(name="findMostMatchedErrorHandle",nativeQuery=true)
	public TB1AErrorHandle findMostMatchedErrorHandle(String appCode,String actionCode, String oneAWsCall,String errorCode);
}
