package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModel;
import com.cathaypacific.mmbbizrule.dto.response.tagging.TaggingDTO;
import com.cathaypacific.mmbbizrule.service.TaggingService;
@RunWith(MockitoJUnitRunner.class)
public class TaggingBusinessImplTest {
	@InjectMocks
	private TaggingBusinessImpl taggingBusinessImpl;
	@Mock
	TaggingService taggingService;
	@Test
	public void test() {
		List<MealTagMappingModel> mealTagMappingModels=new ArrayList<>();
		MealTagMappingModel mealTagMappingModel=new MealTagMappingModel();
		mealTagMappingModel.setMealType("M");
		mealTagMappingModel.setTaggingName("MEAL");
		mealTagMappingModels.add(mealTagMappingModel);
		when(taggingService.findMealTagMapping()).thenReturn(mealTagMappingModels);
		TaggingDTO taggingDTO=taggingBusinessImpl.retrieveTagMapping();
		Assert.assertEquals("M", taggingDTO.getMealTagMappings().get(0).getMealType());
		Assert.assertEquals("MEAL", taggingDTO.getMealTagMappings().get(0).getTaggingName());
	}

}
