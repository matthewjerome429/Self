package com.cathaypacific.mmbbizrule.db.dao;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.cathaypacific.mmbbizrule.db.model.MealOptionKey;
import com.cathaypacific.mmbbizrule.db.model.MealOptionModel;

public interface MealIneligibilityDAO extends Repository<MealOptionModel, MealOptionKey> {

	public Optional<MealOptionModel> findOne(MealOptionKey key);

}
