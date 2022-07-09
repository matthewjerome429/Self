package com.cathaypacific.mmbbizrule.oneaservice.handler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.db.dao.TB1AErrorHandleDao;
import com.cathaypacific.mmbbizrule.db.model.TB1AErrorHandle;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;

@RunWith(MockitoJUnitRunner.class)
public class OneAErrorHandlerTest {
	@InjectMocks
	OneAErrorHandler oneAErrorHandler;
	@Mock
	TB1AErrorHandleDao errorHandleDao;
	
	@Mock
	BizRuleConfig bizRuleConfig;
	
	List<OneAError> oneAErrorCodeList =new ArrayList<>();
	OneAError oneAError =new OneAError();
	List<OneAError> oneAErrorCodeList1 =new ArrayList<>();
	OneAError oneAError1 =new OneAError();
	List<OneAError> oneAErrorCodeList2 =new ArrayList<>();
	OneAError oneAError2 =new OneAError();
	List<String> appCode =new ArrayList<>();
	List<String> oneAErrorWithCategory =new ArrayList<>();
	List<String> oneAWsCallCode =new ArrayList<>();
	String AErrorHandle ;
	String actionCode;
	String actionCode1;
	
	@Before
	public void setUp(){
		appCode.add("MMB");
		appCode.add("*");
		actionCode="*";
		oneAWsCallCode.add("T");
		oneAWsCallCode.add("V");
		AErrorHandle="E";
		oneAError.setErrorCode("24607");
		oneAError.setErrorCategory("Category");
		oneAError.setErrorMessage("Message");
		oneAError1.setErrorCode("*");
		oneAErrorCodeList.add(oneAError);
		oneAErrorCodeList1.add(oneAError1);
		oneAErrorWithCategory.add("K");
		oneAErrorWithCategory.add("G");
		oneAErrorWithCategory.add("L");
		oneAErrorWithCategory.add("M");
		when(bizRuleConfig.getIgnoreCategoryFromOneAError()).thenReturn(oneAErrorWithCategory);			
	}
	@Rule
	public ExpectedException thrown= ExpectedException.none();
	@Test
	public void test_parseOneAErrorCode1()throws BusinessBaseException {
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		db1AErrorHandle.setAppCode(appCode.get(0));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("24607");
		db1AErrorHandle.setOneAErrHandle("E");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(0));
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);			
		List<ErrorInfo> ParsedErrorHandle=oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
	    Assert.assertEquals("E", ParsedErrorHandle.get(0).getType().getType());
		Assert.assertEquals("E21T24607", ParsedErrorHandle.get(0).getErrorCode());
	}
	@Test
	public void test_parseOneAErrorCode2()throws BusinessBaseException {
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		db1AErrorHandle.setAppCode(appCode.get(1));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("*");
		db1AErrorHandle.setOneAErrHandle("L");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(1));
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);					
		List<ErrorInfo> ParsedErrorHandle=oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		Assert.assertEquals("L", ParsedErrorHandle.get(0).getType().getType());
		Assert.assertEquals("E21V*", ParsedErrorHandle.get(0).getErrorCode());
	}
	@Test
	public void test_parseOneAErrorCode3()throws BusinessBaseException {
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		db1AErrorHandle.setAppCode(appCode.get(1));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("*");
		db1AErrorHandle.setOneAErrHandle("B");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(1));
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);						
		List<ErrorInfo> ParsedErrorHandle=oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		Assert.assertEquals(0, ParsedErrorHandle.size());
	}
	@Test
	public void test_parseOneAErrorCode4()throws BusinessBaseException {
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		db1AErrorHandle.setAppCode(appCode.get(1));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("*");
		db1AErrorHandle.setOneAErrHandle("S");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(1));
		 Throwable t = null; 
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);							
		try{
			oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		}catch(Exception ex){
			t=ex;
			 assertNotNull(t);  
			    assertTrue(t instanceof com.cathaypacific.mbcommon.exception.ExpectedException);  
			    assertTrue(t.getMessage().contains("Find 1A Stop(type:S) error code:E21V*")); 
		}
		 
	}
	@Test
	public void test_parseOneAErrorCode5()throws BusinessBaseException {
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		db1AErrorHandle.setAppCode(appCode.get(1));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("*");
		db1AErrorHandle.setOneAErrHandle("D");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(1));
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);					
		List<ErrorInfo> ParsedErrorHandle=oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		Assert.assertEquals("E", ParsedErrorHandle.get(0).getType().getType());
		Assert.assertEquals("E21V*", ParsedErrorHandle.get(0).getErrorCode());
	}
		
	@Test
public void parseOneAErrorCode_test()throws BusinessBaseException {
		
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		 Throwable t = null; 
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);						
		try{
			oneAErrorHandler.getParsedErrorHandleByErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		}catch(Exception ex){
			t=ex;
			assertNotNull(t);  
		    assertTrue(t instanceof UnexpectedException);  
		    assertTrue(t.getMessage().contains("Cannot find the handling/default handling of 1A error code:appCode:null,oneAWsCallCode:null,actionCode:*,oneAErrorCode:*")); 
		}
		
	}
	@Test
        public void parseOneAErrorCode_test1()throws BusinessBaseException {
		
		TB1AErrorHandle db1AErrorHandle=new TB1AErrorHandle();
		Throwable t = null;
		db1AErrorHandle.setAppCode(appCode.get(1));
		db1AErrorHandle.setOneAErrHandle(AErrorHandle);
		db1AErrorHandle.setAction(actionCode);
		db1AErrorHandle.setOneAErrCode("*");
		db1AErrorHandle.setOneAErrHandle("L");
		db1AErrorHandle.setOneAWsCall(oneAWsCallCode.get(1));
		when(errorHandleDao.findMostMatchedErrorHandle(db1AErrorHandle.getAppCode(),actionCode, db1AErrorHandle.getOneAWsCall(),db1AErrorHandle.getOneAErrCode())).thenReturn(db1AErrorHandle);					
		try{
			oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList1, db1AErrorHandle.getAppCode(), actionCode, db1AErrorHandle.getOneAWsCall());
		}catch(Exception ex){
			t=ex;
			assertNotNull(t);  
		    assertTrue(t instanceof OneAErrorsException);  
		    assertTrue(t.getMessage().contains("Received 1A D|L error code."));
		}	
	}

}
