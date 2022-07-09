package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.AdditionalOperatorInfoKey;
import com.cathaypacific.mmbbizrule.db.model.AdditionalOperatorInfoModel;

public interface AdditionalOperatorInfoDAO extends CrudRepository<AdditionalOperatorInfoModel, AdditionalOperatorInfoKey> {

}
