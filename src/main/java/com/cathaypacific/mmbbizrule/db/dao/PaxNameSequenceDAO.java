package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.PaxNameSequence;
import com.cathaypacific.mmbbizrule.db.model.PaxNameSequenceKey;

@Repository
public interface PaxNameSequenceDAO extends JpaRepository<PaxNameSequence, PaxNameSequenceKey> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM tb_pax_name_sequence "
			+ "WHERE (app_code = :appCode OR app_code = '*') AND (locale = :locale OR locale = '*') "
			+ "ORDER BY app_code DESC, locale DESC LIMIT 0,1")
	public PaxNameSequence findPaxNameSequenceByAppCodeAndLocale(@Param("appCode") String appCode, @Param("locale") String locale);
	
}
