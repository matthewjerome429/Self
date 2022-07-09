package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbCardPaymentMapping;
import com.cathaypacific.mmbbizrule.db.model.TbCardPaymentMappingPK;

@Repository
public interface TbCardPaymentMappingDAO extends CrudRepository<TbCardPaymentMapping, TbCardPaymentMappingPK> {
	
	public TbCardPaymentMapping findByAppCodeAndCardType(String appCode, String cardType);
	
}
