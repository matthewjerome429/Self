package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbOfficeIdMapping;
import com.cathaypacific.mmbbizrule.db.model.TbOfficeIdMappingKey;

@Repository
public interface TbOfficeIdMappingDAO extends JpaRepository<TbOfficeIdMapping, TbOfficeIdMappingKey> {
	
	@Query(nativeQuery = true, value="SELECT EXISTS(SELECT * FROM tb_officeid_mapping WHERE :value LIKE REPLACE(value, '*', '%') AND app_code=:appCode AND type=:type)")
	public int existByAppCodeAndTypeLikeValue(@Param("appCode") String appCode, @Param("type") String type, @Param("value") String value);
	
}
