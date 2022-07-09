package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbOfficialReceiptDownloadHistory;

@Repository
public interface TbOfficialReceiptDownloadHistoryDAO extends JpaRepository<TbOfficialReceiptDownloadHistory, Integer> {

}
