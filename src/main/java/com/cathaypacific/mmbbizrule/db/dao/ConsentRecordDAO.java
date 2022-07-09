package com.cathaypacific.mmbbizrule.db.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbConsentLogInfo;

@Repository
public interface ConsentRecordDAO extends JpaRepository<TbConsentLogInfo, Date> {

}
