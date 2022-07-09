package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import static org.junit.Assert.*;
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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

@RunWith(MockitoJUnitRunner.class)
public class ResponseDateDeserializerTest {
	
	@InjectMocks
	ResponseDateDeserializer responseDateDeserializer;
	
	@Mock
	DateUtil dateUtil;
	
	@Test
	public void test() {
		String date="2018-03-25";
		JsonElement paramJsonElement=new JsonPrimitive(date);
		Type paramType=null;
		Throwable t = null;
		JsonDeserializationContext paramJsonDeserializationContext=null;
		try{
			responseDateDeserializer.deserialize(paramJsonElement, paramType, paramJsonDeserializationContext);
		}catch(Exception ex){
			t=ex;
			assertNotNull(t);  
			assertTrue(t instanceof JsonParseException);  
			assertTrue(t.getMessage().contains("Cannot parse date in flight status response.")); 
		}
	}
	
	@Test
	public void test1() {
		JsonElement paramJsonElement=null;
		Type paramType=null;
		JsonDeserializationContext paramJsonDeserializationContext=null;
		responseDateDeserializer.deserialize(paramJsonElement, paramType, paramJsonDeserializationContext);
	}
	
	@Test
	public void test2() throws ParseException {
		String date="2018-03-20 19:20:31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonElement paramJsonElement=new JsonPrimitive(date);
		
		Type paramType=null;
		JsonDeserializationContext paramJsonDeserializationContext=null;
		
		Date result =responseDateDeserializer.deserialize(paramJsonElement, paramType, paramJsonDeserializationContext);
		Assert.assertEquals("2018-03-20 19:20:31", sdf.format(result));
	}
	
}
