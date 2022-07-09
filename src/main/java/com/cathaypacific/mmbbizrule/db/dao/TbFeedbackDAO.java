package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.TbFeedback;
import com.cathaypacific.mmbbizrule.db.model.TbFeedbackKey;

public interface TbFeedbackDAO extends CrudRepository<TbFeedback, TbFeedbackKey>{
	
}
