package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

@RunWith(MockitoJUnitRunner.class)
public class RequestDateSerializerTest {
	
	@InjectMocks
	RequestDateSerializer requestDateSerializer;
	
	@Mock
	DateUtil dateUtil;
	
	@Mock
	Type paramType;
	
	@Test
	public void test() {
		Date paramT=null;
		Type paramType=null; 		
		JsonSerializationContext paramJsonSerializationContext=null;	
		JsonElement element=requestDateSerializer.serialize(paramT, paramType, paramJsonSerializationContext);
		Assert.assertEquals("null", element.toString());
	}
	
	@Test
	public void test1() throws ParseException {		
		JsonSerializationContext paramJsonSerializationContext=null;
		String date="2018-03-20";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date paramT=sdf.parse(date);
		JsonElement element=requestDateSerializer.serialize(paramT, paramType, paramJsonSerializationContext);
		Assert.assertEquals("\"2018-03-20\"", element.toString());
	}
	
}
