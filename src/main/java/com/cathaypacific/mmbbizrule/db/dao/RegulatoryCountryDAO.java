package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.RegulatoryCountryMapping;
import com.cathaypacific.mmbbizrule.db.model.RegulatoryCountryMappingKey;

public interface RegulatoryCountryDAO extends CrudRepository<RegulatoryCountryMapping, RegulatoryCountryMappingKey>{

	@Query(nativeQuery=true, value="select * FROM tb_regulatory_country_mapping "
			+ "where :appCode like  REPLACE(app_code, '*', '%') and reg_type = :type and country_code in (:countries)")
	public List<RegulatoryCountryMapping> getByAppCodeAndTypeAndCountries(@Param("appCode") String appCode, @Param("type") String type, @Param("countries") List<String> countries);	
	
	@Query(nativeQuery=true, value="select * FROM tb_regulatory_country_mapping "
			+ "where :appCode like  REPLACE(app_code, '*', '%') and reg_type in (:types)")
	public List<RegulatoryCountryMapping> getByAppCodeAndTypes(@Param("appCode") String appCode, @Param("types") List<String> types);	
}
