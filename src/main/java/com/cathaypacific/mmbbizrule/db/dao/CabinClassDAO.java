package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.db.model.CabinClassPK;

public interface CabinClassDAO extends JpaRepository<CabinClass,CabinClassPK>{
	
	@Query(nativeQuery=true, value = "SELECT DISTINCT(basic_class) FROM tb_cabin_class WHERE app_code = :appCode and description IN ('First Class','Business Class','Premium Economy class')")
	public List<String> findCabinClassByDescriptionAndAppCode(@Param("appCode") String appCode);

	public List<CabinClass> findByAppCode(String appCode);
	
	public List<CabinClass> findByAppCodeAndBasicClassAndSubclass(String appCode, String basicClass, String subclass);
	
	@Query(nativeQuery=true, value = "SELECT DISTINCT(description) FROM tb_cabin_class WHERE app_code = :appCode and subclass = :subclass")
	public List<String> findDescriptionBySubclass(@Param("appCode") String appCode, @Param("subclass") String subclass);
	
}
