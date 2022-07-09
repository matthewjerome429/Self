package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.v2.dto.response.meal.updateMeal.UpdateMealResponseDTOV2;

public interface MealBusinessV2 {

	public UpdateMealResponseDTOV2 updateMeal(UpdateMealRequestDTO requestDto, LoginInfo loginInfo) throws BusinessBaseException;
	
}
