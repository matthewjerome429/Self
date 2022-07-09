package com.cathaypacific.mmbbizrule.cxservice.eods;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.eodsconsumer.model.request.bookingsummary_v1.BookingSummaryRQ;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
@RunWith(MockitoJUnitRunner.class)
public class EODSRequestBuilderTest {
	@InjectMocks
	EODSRequestBuilder eODSRequestBuilder;
	@Test
	public void test()throws UnexpectedException {
		String memberId="196185";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		BookingSummaryRQ RQ=eODSRequestBuilder.buildRequest(memberId);
		Assert.assertEquals(196185, RQ.getCLSID());
		Assert.assertEquals("2999-11-30T10:00:00", RQ.getStdend());
		Assert.assertEquals(df.format(new Timestamp(System.currentTimeMillis()- 4 * 24 * 60 * 60 * 1000)), RQ.getStdstart());
	}
	@Test
	public void test1()throws UnexpectedException {
		String memberId=null;
		 Throwable t = null; 
		 try{
			 eODSRequestBuilder.buildRequest(memberId);
			}catch(Exception ex){
				t=ex;
				 assertNotNull(t);  
				    assertTrue(t instanceof UnexpectedException);  
				    assertTrue(t.getMessage().contains("Build EODS booking summary request body failed.")); 
			}
	}

}
