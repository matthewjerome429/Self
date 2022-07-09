package com.cathaypacific.mmbbizrule.oneaservice.convertOARloc.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cathaypacific.mmbbizrule.BaseTest;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service.impl.ConvertOARlocServiceImpl;
import com.cathaypacific.eodsconsumer.util.MarshallerFactory;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc: GDS RLOC Login test
 * @author: fengfeng.jiang
 * @date: Dec 8, 2017 12:50:12 PM
 * @version: V1.0
 * 
 */

@RunWith(MockitoJUnitRunner.class)
public class ConvertOARlocServiceImplTest extends BaseTest{
	@Mock
	private OneAWSClient oneAWSClient;
	@InjectMocks
	private ConvertOARlocServiceImpl convertOARlocService;
	
	@Before
	public void setUp() throws Exception{
	}
	  
	@Test
	public void test_getRlocByGDSRloc() throws Exception {
		Resource resource = new ClassPathResource("xml/RetrieveByOARlocReply.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller =MarshallerFactory.getMarshaller(PNRRetrieveByOARlocReply.class);
		PNRRetrieveByOARlocReply pnrRetrieveByOARlocReply = (PNRRetrieveByOARlocReply) marshaller.unmarshal(new StreamSource(is));
		when(oneAWSClient.getRlocByGDSRloc( anyObject())).thenReturn(pnrRetrieveByOARlocReply);
		
		String rloc = convertOARlocService.getRlocByGDSRloc("6RCLXK");
		
		assertNotNull(rloc);
		assertEquals("JU6AKW",rloc);
	}
}
