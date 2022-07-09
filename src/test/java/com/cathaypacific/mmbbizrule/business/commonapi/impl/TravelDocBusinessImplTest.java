package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cathaypacific.mmbbizrule.db.service.TbTravelDocNatCoiCheckCacheHelper;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocNatCoiMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TBTravelDocNatCoiMappingDTO;

@RunWith(MockitoJUnitRunner.class)
public class TravelDocBusinessImplTest {
	@InjectMocks
	private TravelDocBusinessImpl travelDocBusinessImpl;
	@Mock
	private TbTravelDocListCacheHelper tbTravelDocListCacheHelper;

	@Mock
	private TbTravelDocNatCoiCheckCacheHelper travelDocNatCoiCheckCacheHelper;
	
	@Test
	public void test() {
				List<TravelDocNatCoiMapping>  travelDocNatCoiMappings=new ArrayList<>();
		TravelDocNatCoiMapping travelDocNatCoiMapping1=new TravelDocNatCoiMapping();
		travelDocNatCoiMapping1.setCoi("1");
		travelDocNatCoiMapping1.setNat("N");
		travelDocNatCoiMapping1.setDocType("3");
		travelDocNatCoiMapping1.setReminder("M");
		travelDocNatCoiMappings.add(travelDocNatCoiMapping1);
		when(travelDocNatCoiCheckCacheHelper.findDocCoisByAppCode(MMBConstants.APP_CODE)).thenReturn(travelDocNatCoiMappings);
		List<TBTravelDocNatCoiMappingDTO> tBTravelDocNatCoiMappingDTO=travelDocBusinessImpl.getNATAndCOI();
		Assert.assertEquals("1", tBTravelDocNatCoiMappingDTO.get(0).getCoi());
		Assert.assertEquals("N", tBTravelDocNatCoiMappingDTO.get(0).getNat());
		Assert.assertEquals("3", tBTravelDocNatCoiMappingDTO.get(0).getDocType());
		Assert.assertEquals("M", tBTravelDocNatCoiMappingDTO.get(0).getReminder());

	}
	@Test
	public void test1() {
		Integer version=1;
		List<String> usablePrimaryTavelDocs=new ArrayList<>();
		usablePrimaryTavelDocs.add("1");
		List<String> usableSecondaryTavelDocs=new ArrayList<>();
		usableSecondaryTavelDocs.add("M");
		when(tbTravelDocListCacheHelper.findTravelDocCodeByVersion(version,Arrays.asList(TBConstants.TRAVEL_DOC_PRIMARY))).thenReturn(usablePrimaryTavelDocs);
		when(tbTravelDocListCacheHelper.findTravelDocCodeByVersion(version,Arrays.asList(TBConstants.TRAVEL_DOC_SECONDARY))).thenReturn(usableSecondaryTavelDocs);
		travelDocBusinessImpl.getTravedocTypesByApiVersion(version);
		Assert.assertEquals("1", usablePrimaryTavelDocs.get(0));
		Assert.assertEquals("M", usableSecondaryTavelDocs.get(0));
	}
}
