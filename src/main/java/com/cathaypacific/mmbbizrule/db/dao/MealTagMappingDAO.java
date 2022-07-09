package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModel;
import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModelKey;

public interface MealTagMappingDAO extends JpaRepository<MealTagMappingModel, MealTagMappingModelKey> {

	public List<MealTagMappingModel> findByAppCode(String appCode);

}
