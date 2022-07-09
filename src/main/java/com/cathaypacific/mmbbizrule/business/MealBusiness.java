package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.meal.updateMeal.UpdateMealResponseDTO;

public interface MealBusiness {

	public UpdateMealResponseDTO updateMeal(UpdateMealRequestDTO requestDto, LoginInfo loginInfo) throws BusinessBaseException;

}
