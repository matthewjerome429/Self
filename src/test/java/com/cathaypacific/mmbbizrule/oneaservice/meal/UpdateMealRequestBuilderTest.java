package com.cathaypacific.mmbbizrule.oneaservice.meal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UpdateMealRequestBuilderTest {
	
	@InjectMocks
	private MealRequestBuilder mealRequestBuilder;
	
	private List<AddMealDetailDTO> addMealDetailDTOs = new ArrayList<>();

	@Before
	public void before(){
		/** initialize request **/
		addMealDetailDTOs = Lists.newArrayList();
		AddMealDetailDTO addMealDetailDTO = new AddMealDetailDTO();
		addMealDetailDTO.setSegmentId("1");
		addMealDetailDTO.setCompanyId("CX");
		addMealDetailDTO.setPaxMealDetails(Lists.newArrayList());
		
		MealRequestDetailDTO mealRequestDetailDTO = new MealRequestDetailDTO();
		mealRequestDetailDTO.setMealCode("DBML");
		mealRequestDetailDTO.setPassengerId("1");
		mealRequestDetailDTO.setQuantity(1);
		addMealDetailDTO.getPaxMealDetails().add(mealRequestDetailDTO);
		
		addMealDetailDTOs.add(addMealDetailDTO);
	}
	
	@Test
	public void buildRequestTest() throws ExpectedException{
		PNRAddMultiElements result = mealRequestBuilder.buildRequest("ABCDEF", addMealDetailDTOs, null);

		assertNotNull(result);
		assertEquals("ABCDEF", result.getReservationInfo().getReservation().getControlNumber());
		
		List<DataElementsIndiv> dataElementsIndivs = result.getDataElementsMaster().getDataElementsIndiv();
		assertEquals("SSR",dataElementsIndivs.get(0).getElementManagementData().getSegmentName());
		assertEquals("PT",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(0).getQualifier());
		assertEquals("1",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(0).getNumber());
		assertEquals("ST",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(1).getQualifier());
		assertEquals("1",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(1).getNumber());
		assertEquals("CX",dataElementsIndivs.get(0).getServiceRequest().getSsr().getCompanyId());
		assertEquals("DBML",dataElementsIndivs.get(0).getServiceRequest().getSsr().getType());
		assertEquals(1,dataElementsIndivs.get(0).getServiceRequest().getSsr().getQuantity().intValue());
	}
}
