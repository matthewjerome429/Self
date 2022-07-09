package com.cathaypacific.mmbbizrule;

import org.apache.log4j.Logger;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc: TODO
 * @author: fengfeng.jiang
 * @date: Dec 8, 2017 3:30:42 PM
 * @version: V1.0
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {
	protected Logger logger = Logger.getLogger(BaseTest.class);
	//protected MockMvc mockMvc = null;
	//@Autowired
	//private WebApplicationContext wac;
	
	//@Before
	public void setup() {
		//this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	/**
	 * 
	* @Desc: 发送请求获取数据（默认POST方式）
	* @param reqUri 请求URL
	* @param reqParam 请求参数
	* @throws Exception
	* @return String
	* @author: fengfeng.jiang
	* @date: Dec 8, 2017 3:28:02 PM
	 */
	/*protected String sendAndReceive(String reqUri,String reqParam) throws Exception{
		return sendAndReceive(reqUri, reqParam, "POST");
	}
	
	protected String sendAndReceive(String reqUri,String reqParam, String method) throws Exception{
		String rspStr = null;
		logger.info("============请求地址：" + reqUri + "============");
		logger.info("============请求参数：" + reqParam + "============");
		logger.info("============请求方式：" + method + "============");
		
		RequestBuilder request = null;
		if("POST".equals(method)){
			request = MockMvcRequestBuilders.post(reqUri).contentType(MediaType.APPLICATION_JSON_UTF8).header("SESSIONNO", "").content(reqParam) ;
		}else if("GET".equals(method)){
			request = MockMvcRequestBuilders.get(reqUri).contentType(MediaType.APPLICATION_FORM_URLENCODED).header("SESSIONNO", "").content(reqParam) ;
		}
		
		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		int status = mvcResult.getResponse().getStatus();  
		rspStr = mvcResult.getResponse().getContentAsString();
		
		logger.info("============响应状态码：" + status + "============");
		logger.info("============响应数据：" + rspStr + "============");
		
		return rspStr;
	}*/
}
