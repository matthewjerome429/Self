package com.cathaypacific.mmbbizrule.db.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbConsentRecord;

@Repository
public interface ConsentRecordDAOV2 extends JpaRepository<TbConsentRecord, Integer> {

	public List<TbConsentRecord> findByIdIn(Collection<Integer> ids);
	
}
