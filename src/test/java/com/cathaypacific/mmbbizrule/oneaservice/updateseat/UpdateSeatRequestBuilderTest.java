package com.cathaypacific.mmbbizrule.oneaservice.updateseat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSeatRequestBuilderTest {
	
	private UpdateSeatRequestDTO request;	
	
	private List<PaxSeatDetail> detailList;

	@Before
	public void setUp(){
		/** initialize request **/
		detailList = new ArrayList<>();
		PaxSeatDetail details = new PaxSeatDetail();
		details.setPassengerID("3");
		details.setSeatNo("61A");
		detailList.add(details);
		
		request = new UpdateSeatRequestDTO();
		request.setRloc("RGIJBH");
		request.setSegmentId("1");
		request.setPaxSeatDetails(detailList);
	}
	
	@Test
	public void buildRequestTest(){
		UpdateSeatRequestBuilder builder = new UpdateSeatRequestBuilder();
		PNRAddMultiElements result = null;
		result = builder.buildRequest(request, null, new ArrayList<XlwrInfo>(), null, null);
		
		assertNotNull(result);
		assertEquals("RGIJBH",result.getReservationInfo().getReservation().getControlNumber());
		
		List<DataElementsIndiv>  dataElementsIndivs = result.getDataElementsMaster().getDataElementsIndiv();
		assertEquals("STR",dataElementsIndivs.get(0).getElementManagementData().getSegmentName());
		assertEquals("61A",dataElementsIndivs.get(0).getSeatGroup().getSeatRequest().getSpecial().get(0).getData());
		assertEquals("PT",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(0).getQualifier());
		assertEquals("3",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(0).getNumber());
		assertEquals("ST",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(1).getQualifier());
		assertEquals("1",dataElementsIndivs.get(0).getReferenceForDataElement().getReference().get(1).getNumber());
		
		assertEquals("RF",dataElementsIndivs.get(1).getElementManagementData().getSegmentName());
		assertEquals("3",dataElementsIndivs.get(1).getFreetextData().getFreetextDetail().getSubjectQualifier());
		assertEquals("P22",dataElementsIndivs.get(1).getFreetextData().getFreetextDetail().getType());
		assertEquals("CXMB",dataElementsIndivs.get(1).getFreetextData().getLongFreetext());
	}
	
}
