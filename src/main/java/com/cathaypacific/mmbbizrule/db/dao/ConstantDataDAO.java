package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.db.model.ConstantDataKey;

public interface ConstantDataDAO extends CrudRepository<ConstantData,ConstantDataKey>{
	
	public List<ConstantData> findByAppCodeAndType(String appCode,String type);
	
}
