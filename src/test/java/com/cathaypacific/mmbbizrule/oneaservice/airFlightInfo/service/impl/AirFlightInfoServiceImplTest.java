package com.cathaypacific.mmbbizrule.oneaservice.airFlightInfo.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cathaypacific.eodsconsumer.util.MarshallerFactory;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.BaseTest;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.cxservice.aep.service.AEPService;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.impl.RetrieveProfileServiceImpl;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.Impl.AirportTimeZoneServiceImpl;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.repository.FlightStatusRepository;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.impl.FlightStatusServiceImpl;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.TBSsrSkMappingDAO;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.BookingBuildValidationHelpr;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.impl.AirFlightInfoServiceImpl;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.impl.PnrInvokeServiceImpl;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.SeatRuleService;
import com.cathaypacific.mmbbizrule.service.impl.BookingBuildServiceImpl;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

/**
 * Created by shane.tian.xia on 1/2/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AirFlightInfoServiceImplTest extends BaseTest {

	@Mock
	private OneAWSClient oneAWSClient;

    @InjectMocks
    private AirFlightInfoServiceImpl airFlightInfoService;

    @Mock
    private PnrInvokeServiceImpl pnrInvokeService;

    @Mock
    private AirportTimeZoneServiceImpl airportTimeZoneService;

    @InjectMocks
    private BookingBuildServiceImpl bookingBuildService;
    
	@Mock
	private TicketProcessInvokeService ticketProcessInvokeService;
    
    @Mock
    private RetrieveProfileServiceImpl retrieveProfileService;
    
    @Mock
    private BookingStatusDAO bookingStatusDAO;

    @Mock
	private TBSsrSkMappingDAO tBSsrSkMappingDao;
    
    @Mock
    private AirFlightInfoServiceImpl airFlightInfoServiceImpl;

    @Mock
    private FlightStatusServiceImpl flightStatusService;
    
    @Mock
    private AEMService aemService;

    @Mock
    private FlightStatusRepository flightStatusRepository;

    @Mock
    private ConstantDataDAO constantDataDAO;

    @Mock
    private CabinClassDAO cabinClassDAO;
    
    @Mock
    private TbTravelDocOdCacheHelper tbTravelDocOdCacheHelper;
    
    @Mock
	private PaxNameIdentificationService paxNameIdentificationService;
    
    @Mock
	private BizRuleConfig bizRuleConfig;
    
    @Mock
	private EncryptionHelper encryptionHelper;
    
    @Mock
    private OLCIConfig olciConfig;
    
    @Mock
	private SeatRuleService seatRuleService;
	
	@Mock
	private AEPService aepService;
	
	@Mock
	private BaggageAllowanceBuildService baggageAllowanceBuildService;
	
	@Mock
	private BookingBuildValidationHelpr bookingBuildValidationHelpr;
	
	@Mock
	private BookingBuildHelper bookingBuildHelper;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Mock
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;
	
	@Mock
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	@Mock
	private TbTravelDocListCacheHelper travelDocListCacheHelper;
    
    private String rloc;
    private String familyName;
    private String givenName;
    private RetrievePnrBooking retrievePnrBooking;
    private List<String> actions;
    private AirFlightInfoBean airFlightInfoBean;
    private String departTime;
    private String boardLocationId;
    private String OffLocationId;
    private String marketingCompany;
    private String flightNumber;
    private  LoginInfo loginInfo;
    private BookingBuildRequired required;
    
    @Before
    public void setUp() throws Exception{
    	int limithours =72;
		Field limithoursField = BookingBuildServiceImpl.class.getDeclaredField("limithours");
		limithoursField.setAccessible(true);
		limithoursField.set(bookingBuildService, limithours);

        departTime = "2018-01-05 01:00";
        boardLocationId = "HKG";
        OffLocationId = "JFK";
        marketingCompany = "CX";
        flightNumber = "888";
        Resource resource = new ClassPathResource("xml/AirFlightInfoReply.xml");
        InputStream is = resource.getInputStream();
        Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(AirFlightInfoReply.class);
        AirFlightInfoReply airFlightInfoReply = (AirFlightInfoReply) marshaller.unmarshal(new StreamSource(is));
        when(oneAWSClient.findFlightInfo(anyObject())).thenReturn(airFlightInfoReply);
        when(linkTempBookingRepository.getLinkedBookings(anyString())).thenReturn(Collections.emptyList());
        
        required = new BookingBuildRequired();
    }

    @Test
    public void testAirFlightInfo() throws Exception{
        AirFlightInfoBean result = airFlightInfoService.getAirFlightInfo(departTime,boardLocationId,OffLocationId,marketingCompany,flightNumber,"");
        assertNotNull(result);
    }

    private void prepareTestData(){
        rloc = "OY3Q7J";
        familyName = "WONG";
        givenName = "WEN";
        retrievePnrBooking = new RetrievePnrBooking();
        RetrievePnrPassenger passenger = new RetrievePnrPassenger();
        passenger.setPassengerID("1");
        passenger.setPassengerType("ADT");
        passenger.setFamilyName(familyName);
        passenger.setGivenName(givenName);
        List<RetrievePnrPassenger> retrievePnrPassengerArrayList = new ArrayList<RetrievePnrPassenger>();
        retrievePnrPassengerArrayList.add(passenger);
        retrievePnrBooking.setPassengers(retrievePnrPassengerArrayList);
        List<RetrievePnrSegment> retrievePnrSegmentList = new ArrayList<RetrievePnrSegment>();
        RetrievePnrSegment segment = new RetrievePnrSegment();
        segment.setSegmentID("2");
        segment.setOriginPort("HKG");
        segment.setDestPort("JFK");
        RetrievePnrDepartureArrivalTime departureTime = new RetrievePnrDepartureArrivalTime();
        departureTime.setPnrTime("2018-01-15 01:00");
        segment.setDepartureTime(departureTime);
        RetrievePnrDepartureArrivalTime arrivalTime = new RetrievePnrDepartureArrivalTime();
        arrivalTime.setPnrTime("2018-01-05 06:05");
        segment.setArrivalTime(arrivalTime);
        segment.setMarketSegmentNumber("888");
        segment.setMarketCompany("CX");
        segment.setSubClass("R");
        List<String> statusList = new ArrayList<String>();
        statusList.add("HK");
        segment.setStatus(statusList);
        segment.setOriginTerminal("1");
        segment.setDestTerminal("8");
        segment.setAirCraftType("77W");
        retrievePnrSegmentList.add(segment);
        retrievePnrBooking.setSegments(retrievePnrSegmentList);
        List<RetrievePnrPassengerSegment> retrievePnrPassengerSegmentList = new ArrayList<RetrievePnrPassengerSegment>();
        RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
        List<RetrievePnrEticket> eticketList = new ArrayList<RetrievePnrEticket>();
        RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
        retrievePnrEticket.setTicketNumber("1602361869667");
        eticketList.add(retrievePnrEticket);
        passengerSegment.setEtickets(eticketList);
        passengerSegment.setPassengerId("1");
        passengerSegment.setSegmentId("2");
        List<RetrievePnrTravelDoc> travelDocList = new ArrayList<RetrievePnrTravelDoc>();
        RetrievePnrTravelDoc travelDoc = new RetrievePnrTravelDoc();
        travelDoc.setFamilyName(familyName);
        travelDoc.setGivenName(givenName);
        travelDoc.setTravelDocumentType("P");
        travelDoc.setTravelDocumentNumber("201701141");
        travelDoc.setCountryOfIssuance("HKG");
        travelDoc.setNationality("HKG");
        travelDoc.setExpiryDateYear("2020");
        travelDoc.setExpiryDateMonth("02");
        travelDoc.setExpiryDateDay("02");
        travelDoc.setBirthDateYear("1988");
        travelDoc.setBirthDateMonth("09");
        travelDoc.setBirthDateDay("10");
        travelDoc.setCompanyId("CX");
        travelDoc.setGender("F");
        travelDoc.setInfant(false);
        travelDocList.add(travelDoc);
        retrievePnrPassengerSegmentList.add(passengerSegment);
        retrievePnrBooking.setPassengerSegments(retrievePnrPassengerSegmentList);
        retrievePnrBooking.setOneARloc(rloc);

        actions = new ArrayList<>();
        actions.add("HK");
        actions.add("HL");
        actions.add("HN");
        actions.add("KK");
        actions.add("KL");
        actions.add("NN");
        actions.add("RR");
        actions.add("TK");
        actions.add("TL");
        actions.add("TN");
        actions.add("UN");
        actions.add("UU");

        List<String> stops = new ArrayList<>();
        stops.add("YVR");
        airFlightInfoBean = new AirFlightInfoBean();
        airFlightInfoBean.setNumberOfStops(new BigDecimal(1));
        airFlightInfoBean.setTotalDuration("1805");
        airFlightInfoBean.setStops(stops);
        airFlightInfoBean.setStopOverFlight(true);
        
		 loginInfo = new LoginInfo();
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		loginInfo.setMemberId("1234567");
		loginInfo.setMmbToken("testtoken");
    }

    @Test
    public void test_wholePrecessForAirFlightInfo() throws Exception{
        prepareTestData();
        doNothing().when(paxNameIdentificationService).primaryPaxIdentificationForMember(Matchers.any(), Matchers.any());
        doNothing().when(bookingBuildValidationHelpr).removeAllFqtvForIDBooking(Matchers.any());
        when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
        when(bookingStatusDAO.findStatusCodeByAppCodeAndActionIn(anyString(),anyListOf(String.class))).thenReturn(actions);
        when(airportTimeZoneService.getAirPortTimeZoneOffset("HKG")).thenReturn("+0800");
        when(airportTimeZoneService.getAirPortTimeZoneOffset("JFK")).thenReturn("-0500");
        when(airportTimeZoneService.getAirPortTimeZoneOffset("YVR")).thenReturn("-0800");
        when(airportTimeZoneService.getAirPortTimeZoneOffset("SHA")).thenReturn("+0800");
        when(airportTimeZoneService.getAirPortTimeZoneOffset("TPE")).thenReturn("+0800");
        when(airFlightInfoServiceImpl.getAirFlightInfo(anyString(),anyString(),anyString(),anyString(),anyString(), anyString())).thenReturn(airFlightInfoBean);
        when(encryptionHelper.encryptMessage(anyString(), anyObject(), anyString())).thenReturn("");
        when(olciConfig.getCheckInWindowUpper()).thenReturn(172800000l);
        when(seatRuleService.getEligibleRBDForSeatSelection()).thenReturn(null);
        when(mbTokenCacheRepository.get(anyString(), anyObject(), anyString(), anyObject())).thenReturn(null);
        when(mbTokenCacheLockRepository.get(anyString(), anyObject(), anyObject(), anyString(), anyObject())).thenReturn(null);
       try {
	    	   bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required);
		} catch (ExpectedException e) {
			assertNotNull(e.getErrorInfo().getErrorCode().equals(ErrorCodeEnum.ERR_ALL_FLIGHT_FLOWN_BEFOR_LIMIT_TIME));
		}
      
    }
}
