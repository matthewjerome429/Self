package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.cathaypacific.mmbbizrule.util.RTFSHttpClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.FlightStatusConfig;
import com.cathaypacific.mmbbizrule.constant.FlightStatusConstants;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(MockitoJUnitRunner.class)
public class FlightStatusRepositoryTest {
	@InjectMocks
	FlightStatusRepository flightStatusRepository;
	@Mock
	private FlightStatusConfig flightStatusConfig;
	@Mock
	private HttpClientService httpClientService;

	@Mock
	private RTFSHttpClientService rtfsHttpClientService;
	
	static {
		REQUEST_GSON = new GsonBuilder().registerTypeAdapter(Date.class, new RequestDateSerializer()).create();
		RESPONSE_GSON = new GsonBuilder().registerTypeAdapter(Date.class, new ResponseDateDeserializer()).create();
	}
	private static final Gson REQUEST_GSON;
	
	private FlightStatusRequestDTO flightStatusRequest;
	@SuppressWarnings("unused")
	private static final  Gson RESPONSE_GSON;
	
	@Before
	public void setUp() throws ParseException{
		flightStatusRequest=new FlightStatusRequestDTO();
		String company="XC";
		String flightNumber="520"; 
		String travelTime="2018-3-25 08:00";
		
		flightStatusRequest.setCarrierCode(company);
		flightStatusRequest.setFlightNumber(flightNumber);
		flightStatusRequest.setTravelDate(DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, travelTime));
		flightStatusRequest.setDepartureArrival(FlightStatusConstants.DEPARTURE);
		
	}
	@Test
	public void test() {
		String company="XC";
		String flightNumber="520"; 
		String travelTime="2018-3-25";
		 Throwable t = null; 
		try{
		flightStatusRepository.findByFlightNumber(company, flightNumber, travelTime);
		}catch(Exception ex){
			t=ex;
			 assertNotNull(t);  
			    assertTrue(t instanceof IllegalArgumentException);  
			    assertTrue(t.getMessage().contains("Flight time doesn't match format.")); 
		}
	}
	@Test
	public void test2() {
		String company="XC";
		String flightNumber="520"; 
		String travelTime="2018-3-25 08:00";
		String json ="{\"result\":[{\"origin\":\"\",\"destination\":\"HKG\"}],\"errors\":[{\"errorCode\":\"1931\",\"type\":\"V\",\"passengerId\":\"1\"}]}";
		String url="www.baidu.com";
		when(flightStatusConfig.getFlightStatusByFlightNumberUrl()).thenReturn(url);
		when(httpClientService.postJson(flightStatusConfig.getFlightStatusByFlightNumberUrl(),
				REQUEST_GSON.toJson(flightStatusRequest))).thenReturn(json);							
		
		List<FlightStatusData> flightStatusData=flightStatusRepository.findByFlightNumber(company, flightNumber, travelTime);
		Assert.assertNull(flightStatusData);
	}
	@Test
	public void test3() {
		String company="XC";
		String flightNumber="520"; 
		String travelTime="2018-3-25 08:00";
		String json ="";
		String url="www.baidu.com";
		when(flightStatusConfig.getFlightStatusByFlightNumberUrl()).thenReturn(url);
		when(httpClientService.postJson(flightStatusConfig.getFlightStatusByFlightNumberUrl(),
				REQUEST_GSON.toJson(flightStatusRequest))).thenReturn(json);							
		
		List<FlightStatusData> flightStatusData=flightStatusRepository.findByFlightNumber(company, flightNumber, travelTime);
		Assert.assertNull(flightStatusData);
	}
	@Test
	public void test1() throws IOException {
		String company="XC";
		String flightNumber="520"; 
		String travelTime="2018-3-25 08:00";
		String json ="{\"result\":[{\"origin\":\"HKG\",\"destination\":\"HKG\"}]}";
		String url="www.baidu.com";
		when(flightStatusConfig.getFlightStatusByFlightNumberUrl()).thenReturn(url);
		when(rtfsHttpClientService.postJson(flightStatusConfig.getFlightStatusByFlightNumberUrl(),
				REQUEST_GSON.toJson(flightStatusRequest))).thenReturn(json);

		List<FlightStatusData> flightStatusData=flightStatusRepository.findByFlightNumber(company, flightNumber, travelTime);
		Assert.assertEquals("HKG", flightStatusData.get(0).getOrigin());
		Assert.assertEquals("HKG", flightStatusData.get(0).getDestination());
	}
	
}
