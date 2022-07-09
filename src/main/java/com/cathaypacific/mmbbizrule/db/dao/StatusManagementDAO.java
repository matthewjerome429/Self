package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.StatusManagementKey;
import com.cathaypacific.mmbbizrule.db.model.StatusManagementModel;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusManagementDAO extends CrudRepository<StatusManagementModel, StatusManagementKey>{
	
	@Query(nativeQuery=true, value="select * from tb_status_management where "
			+ " app_code = ?1 and type = ?2 and status_code = ?4" 
			+ " and (value = ?3 or value = '*')"
			+ " order by value desc limit 1")
	public StatusManagementModel findMostMatchedStatus(String appCode, String type, String value, String statusCode);

	@Query(nativeQuery=true, value="select * from tb_status_management WHERE app_code = :appCode AND TYPE =:type_value")
	List<StatusManagementModel> findAllByAppCodeAndType(@Param("appCode") String appCode,@Param("type_value") String type_value);

	public List<StatusManagementModel> findByAppCode(String appCode);
}
