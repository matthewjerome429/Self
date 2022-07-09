package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModel;
import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModelPK;

@Repository
public interface TbSsrTypeDAO extends JpaRepository<TbSsrTypeModel, TbSsrTypeModelPK> {
	
	List<TbSsrTypeModel> findByAppCodeAndTypeAndAction(String appCode, String type, String action);
	
	List<TbSsrTypeModel> findDistinctTbSsrTypeModelByAppCodeAndType(String appCode,String type);
	
	List<TbSsrTypeModel> findByAppCodeAndType(String appCode, String type);
	
	@Query(nativeQuery=true, value="select distinct value from tb_ssr_type where app_code = :appCode AND type = 'ML'")
	List<String> findMealCodeListByAppCode(@Param("appCode") String appCode);
}
