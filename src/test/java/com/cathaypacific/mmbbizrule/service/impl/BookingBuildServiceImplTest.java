package com.cathaypacific.mmbbizrule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.cathaypacific.olciconsumer.model.response.db.TravelDocList;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocOD;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.aep.service.AEPService;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.MemberAwardResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.service.MemberAwardService;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerCheckInInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerInfoType;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PersonalInfoType;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeService;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.FlightStatusService;
import com.cathaypacific.mmbbizrule.db.dao.ASREnableCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.RedemptionSubclassCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.RedemptionTPOSCheckDAO;
import com.cathaypacific.mmbbizrule.db.dao.SeatRuleDao;
import com.cathaypacific.mmbbizrule.db.dao.SpecialServiceDAO;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TBSsrSkMappingDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbFlightHaulDAO;
import com.cathaypacific.mmbbizrule.db.model.AsrEnableCheck;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassCheck;
import com.cathaypacific.mmbbizrule.db.model.RedemptionTPOSCheck;
import com.cathaypacific.mmbbizrule.db.model.SeatRuleModel;
import com.cathaypacific.mmbbizrule.db.model.SpecialServiceModel;
import com.cathaypacific.mmbbizrule.db.model.StatusManagementModel;
import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMapping;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.BookingBuildValidationHelpr;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.profile.Contact;
import com.cathaypacific.mmbbizrule.model.profile.ProfileEmail;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePhoneInfo;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAddressDetails;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBaggage;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDob;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmrContactInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFa;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFe;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeat;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatPreference;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessBaggageAllowance;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightDate;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService;
import com.cathaypacific.mmbbizrule.service.SeatRuleService;
import com.cathaypacific.mmbbizrule.service.UMNREFormBuildService;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper.Encoding;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@RunWith(MockitoJUnitRunner.class)
public class BookingBuildServiceImplTest {

	@InjectMocks
	private BookingBuildServiceImpl bookingBuildServiceImpl;

	@Mock
	private TicketProcessInvokeService ticketProcessInvokeService;

	@Mock
	private BizRuleConfig bizRuleConfig;

	@Mock
	private BookingStatusConfig bookingStatusCOnfig;

	@Mock
	private TbTravelDocListCacheHelper travelDocListCacheHelper;

	@Mock
	private EncryptionHelper encryptionHelper;

	@Mock
	private BookingStatusDAO bookingStatusDAO;

	@Mock
	private AirportTimeZoneService airportTimeZoneService;

	@Mock
	private RetrieveProfileService retrieveProfileService;

	@Mock
	private TbTravelDocOdCacheHelper tbTravelDocOdCacheHelper;

	@Mock
	private RedemptionTPOSCheckDAO redemptionTPOSCheckDAO;
	
	@Mock
	private SeatRuleDao seatRuleDao;

	@Mock
	private RedemptionSubclassCheckDAO redemptionSubclassCheckDAO;

	@Mock
	private ASREnableCheckDAO asrEnableCheckDAO;

	@Mock
	private AirFlightInfoService airFlightInfoService;

	@Mock
	private MemberAwardService memberAwardService;

	@Mock
	private CabinClassDAO cabinClassDAO;

	@Mock
	private AEMService aemService;

	@Mock
	private FlightStatusService flightStatusService;

	@Mock
	private OLCIConfig olciConfig;

	@Mock
	private ConstantDataDAO constantDataDAO;

	@Mock
	private TBSsrSkMappingDAO tBSsrSkMappingDao;
	
	@Mock
	private OLCIServiceV2 olciServiceV2;
	@Mock
	private PnrCprMergeHelper pnrCprMergeHelper;
	@Mock
	private SeatRuleService seatRuleService;

	@Mock
	private BookingStatusConfig bookingStatusConfig;

	@Mock
	private AEPService aepService;

	@Mock
	private BaggageAllowanceBuildService baggageAllowanceBuildService;

	@Mock
	private NationalityCodeService nationalityCodeService;
	
	@Mock
	private StatusManagementDAO statusManagementDAO;

	@Mock
	private SpecialServiceDAO specialServiceDAO;
	
	@Mock
	private BookingBuildHelper bookingBuildHelper;
	
	@Mock
	private BookingBuildValidationHelpr bookingBuildValidationHelpr;
	
	@Mock
	private UMNREFormBuildService umnreFormBuildService;
	
	@Mock
	private TbFlightHaulDAO tbFlightHaulDAO;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Mock
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	@Mock
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;
	
	@Mock
    private BookEligibilityConfig bookEligibilityConfig;
	
	private RetrievePnrBooking pnrBooking;

	private LoginInfo loginInfo;

	private RetrievePnrPassenger passenger;
	private RetrievePnrPassenger passenger2;
	private RetrievePnrPassenger passenger3;
	private RetrievePnrSeat seat;

	private BookingBuildRequired required;

	@Before
	public void setUp() throws UnexpectedException, ParseException, SoapFaultException {
		loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("TEST");
		loginInfo.setLoginRloc("TEST");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("123456");
		loginInfo.setMmbToken("654321");
		when(bookEligibilityConfig.isEnablePreselectedMeal()).thenReturn(false);
		when(nationalityCodeService.findThreeCountryCodeByTwoCountryCode("JP")).thenReturn("JPN");
		when(nationalityCodeService.findThreeCountryCodeByTwoCountryCode("IN")).thenReturn("IND");
		when(nationalityCodeService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CHN");
		List<SeatRuleModel> seatRule=new ArrayList<>();
		List<String> purchaseASRInhibitSubclassList = Arrays.asList("Y", "B", "H", "K", "M", "L", "V");
		seatRule.addAll(purchaseASRInhibitSubclassList.stream().map(subclass -> {
			SeatRuleModel seatRuleModel = new SeatRuleModel();
			seatRuleModel.setValue(subclass);
			return seatRuleModel;
		}).collect(Collectors.toList()));
		when(seatRuleDao.findAllByAppCodeAndAsrFOC(MMBConstants.APP_CODE,TBConstants.ASR_FOC_YES)).thenReturn(seatRule);
		pnrBooking = new RetrievePnrBooking();
		RetrievePnrSegment segment = new RetrievePnrSegment();

		Date departureTimePassed = new Date();
		departureTimePassed.setTime(System.currentTimeMillis() + 87000000l);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("ddMMYY");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HHmm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		List<RetrievePnrDataElements> ssrLists = new ArrayList<>();
		List<RetrievePnrFe> feList =new ArrayList<>();
		RetrievePnrDataElements ssrList = new RetrievePnrDataElements();
		ssrList.setPassengerId("1");
		ssrList.setSegmentId("1");
		ssrList.setType("OBAG");
		ssrList.setStatus("HK");
		ssrList.setCompanyId("CX");
		ssrList.setFreeText("TTL111KG111PC");
		ssrLists.add(ssrList);
		RetrievePnrDataElements retrievePnrSsrSk = new RetrievePnrDataElements();
		retrievePnrSsrSk.setFreeText("M55MZWP/FO/FLT/MOB");
		retrievePnrSsrSk.setType("XLGR");
		retrievePnrSsrSk.setPassengerId("1");
		retrievePnrSsrSk.setSegmentName("SK");
		retrievePnrSsrSk.setSegmentId("1");
		RetrievePnrDataElements retrievePnrSsrSk1 = new RetrievePnrDataElements();
		retrievePnrSsrSk1.setFreeText("9K/FO/EACH/MOB");
		retrievePnrSsrSk1.setType("BAGW");
		retrievePnrSsrSk1.setPassengerId("1");
		retrievePnrSsrSk1.setSegmentName("SK");
		retrievePnrSsrSk1.setSegmentId("1");
		retrievePnrSsrSk1.setStatus("HK");
		retrievePnrSsrSk1.setCompanyId("CX");
		retrievePnrSsrSk1.setQulifierId(new BigInteger("1"));
		RetrievePnrDataElements retrievePnrSsrSk2 = new RetrievePnrDataElements();
		retrievePnrSsrSk2.setFreeText("9K/FO/TOTL/MOB");
		retrievePnrSsrSk2.setType("BAGW");
		retrievePnrSsrSk2.setPassengerId("1");
		retrievePnrSsrSk2.setSegmentName("SK");
		retrievePnrSsrSk2.setSegmentId("1");
		retrievePnrSsrSk2.setStatus("HK");
		retrievePnrSsrSk2.setCompanyId("CX");
		retrievePnrSsrSk2.setQulifierId(new BigInteger("2"));
		RetrievePnrDataElements retrievePnrSsrSk3 = new RetrievePnrDataElements();
		retrievePnrSsrSk3.setFreeText("10K/FO/TOTL/MOB");
		retrievePnrSsrSk3.setType("BAGW");
		retrievePnrSsrSk3.setPassengerId("1");
		retrievePnrSsrSk3.setSegmentName("SK");
		retrievePnrSsrSk3.setSegmentId("1");
		retrievePnrSsrSk3.setStatus("HK");
		retrievePnrSsrSk3.setCompanyId("CX");
		retrievePnrSsrSk3.setQulifierId(new BigInteger("3"));
		RetrievePnrDataElements retrievePnrSsrSk4 = new RetrievePnrDataElements();
		retrievePnrSsrSk4.setFreeText("10K/FO/TOTL/MOB");
		retrievePnrSsrSk4.setType("BAGW");
		retrievePnrSsrSk4.setPassengerId("2");
		retrievePnrSsrSk4.setSegmentName("SK");
		retrievePnrSsrSk4.setSegmentId("1");
		retrievePnrSsrSk4.setStatus("HK");
		retrievePnrSsrSk4.setCompanyId("CX");
		retrievePnrSsrSk4.setQulifierId(new BigInteger("3"));
		skList.add(retrievePnrSsrSk);
		skList.add(retrievePnrSsrSk1);
		skList.add(retrievePnrSsrSk2);
		skList.add(retrievePnrSsrSk3);
		skList.add(retrievePnrSsrSk4);
		pnrBooking.setSkList(skList);
		pnrBooking.setSsrList(ssrLists);
		RetrievePnrFe retrievePnrFe =new RetrievePnrFe();
		retrievePnrFe.setLongFreeText("PAX WAIVECXCHB5 - VLD CX/KA NONEND.REF/RBK RTE FEEAPPLYADDONCXR RESTR");
		feList.add(retrievePnrFe);
		pnrBooking.setFeList(feList);
		RetrievePnrDepartureArrivalTime departureTime = new RetrievePnrDepartureArrivalTime();
		departureTime.setTimeZoneOffset("+0800");
		departureTime.setRtfsEstimatedTime(simpleDateFormat.format(departureTimePassed));
		departureTime.setPnrTime(simpleDateFormat.format(departureTimePassed));
		segment.setDepartureTime(departureTime);
		segment.setSegmentID("1");
		segment.setAirCraftType("TRN");
		segment.setPnrOperateCompany("CX");
		segment.setPnrOperateSegmentNumber("520");
		segment.setMarketCompany("CX");
		segment.setMarketSegmentNumber("520");
		segment.setOriginPort("HK");
		segment.setDestPort("LON");
		segment.setSubClass("Z");
		List<String> statusList = new ArrayList<>();
		statusList.add("HK");
		statusList.add("CC");
		segment.setStatus(statusList);
		List<RetrievePnrSegment> segments = new ArrayList<>();
		segments.add(segment);
		pnrBooking.setSegments(segments);
		passenger = new RetrievePnrPassenger();
		List<RetrievePnrTravelDoc> priTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc priTravelDoc = new RetrievePnrTravelDoc();
		List<RetrievePnrTravelDoc> secTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc secTravelDoc = new RetrievePnrTravelDoc();
		List<RetrievePnrTravelDoc> othTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc othTravelDoc = new RetrievePnrTravelDoc();
		RetrievePnrDob kidDob = new RetrievePnrDob();

		kidDob.setBirthDateDay("04");
		kidDob.setBirthDateMonth("07");
		kidDob.setBirthDateYear("2018");

		BigInteger b1 = new BigInteger("123456789");
		priTravelDoc.setQualifierId(b1);
		priTravelDoc.setBirthDateDay("04");
		priTravelDoc.setBirthDateMonth("04");
		priTravelDoc.setBirthDateYear("2018");
		priTravelDoc.setGender("M");
		priTravelDoc.setTravelDocumentType("PT");

		BigInteger b2 = new BigInteger("123456789");
		secTravelDoc.setQualifierId(b2);
		secTravelDoc.setBirthDateDay("04");
		secTravelDoc.setBirthDateMonth("05");
		secTravelDoc.setBirthDateYear("2018");
		secTravelDoc.setGender("M");
		secTravelDoc.setTravelDocumentType("PT");
		secTravelDocs.add(secTravelDoc);

		BigInteger b3 = new BigInteger("123456789");
		othTravelDoc.setQualifierId(b3);
		othTravelDoc.setBirthDateDay("04");
		othTravelDoc.setBirthDateMonth("06");
		othTravelDoc.setBirthDateYear("2018");
		othTravelDoc.setGender("M");
		othTravelDoc.setTravelDocumentType("PT");
		othTravelDocs.add(othTravelDoc);
		List<RetrievePnrContactPhone> pnrContactPhones = new ArrayList<>();
		RetrievePnrContactPhone pnrContactPhone = new RetrievePnrContactPhone();
		pnrContactPhone.setCompanyId("XC");
		pnrContactPhone.setType("APM");
		// pnrContactPhone.setOlssContact(true);
		pnrContactPhones.add(pnrContactPhone);
		priTravelDocs.add(priTravelDoc);
		passenger.setPassengerID("1");
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TEST");
		passenger.setPassengerType("ADT");
		List<RetrievePnrTravelDoc> redresses = new ArrayList<>();
		RetrievePnrTravelDoc redresse = new RetrievePnrTravelDoc();
		redresse.setTravelDocumentNumber("741");
		redresses.add(redresse);
		passenger.setRedresses(redresses);
		passenger.setPriTravelDocs(priTravelDocs);
		List<RetrievePnrTravelDoc> ktns = new ArrayList<>();
		RetrievePnrTravelDoc ktn = new RetrievePnrTravelDoc();
		ktn.setTravelDocumentNumber("147");
		ktns.add(ktn);
		passenger.setKtns(ktns);
		passenger.setSecTravelDocs(secTravelDocs);
		passenger.setOthTravelDocs(othTravelDocs);
		passenger.setDob(kidDob);
		passenger.setLoginMember(true);
		List<RetrievePnrEmrContactInfo> emrContactInfos = new ArrayList<>();
		RetrievePnrEmrContactInfo emrContactInfo = new RetrievePnrEmrContactInfo();
		RetrievePnrEmrContactInfo emrContactInfo1 = new RetrievePnrEmrContactInfo();
		emrContactInfo.setQualifierId(b1);
		emrContactInfo1.setQualifierId(b1);
		emrContactInfos.add(emrContactInfo);
		emrContactInfos.add(emrContactInfo1);
		passenger.setEmrContactInfos(emrContactInfos);
		passenger.setContactPhones(pnrContactPhones);
		passenger.setPrimaryPassenger(true);
		List<RetrievePnrFFPInfo> fQTVInfos = new ArrayList<>();
		RetrievePnrFFPInfo fQTVInfo = new RetrievePnrFFPInfo();
		fQTVInfo.setCompanyId("CX");
		fQTVInfo.setTierLevel("GG");
		fQTVInfos.add(fQTVInfo);
		List<RetrievePnrFFPInfo> fQTUInfos = new ArrayList<>();
		RetrievePnrFFPInfo fQTUInfo = new RetrievePnrFFPInfo();
		fQTUInfo.setCompanyId("CX");

		fQTUInfos.add(fQTUInfo);
		passenger2 = new RetrievePnrPassenger();
		passenger2.setPassengerID("2");
		passenger2.setParentId("1");
		passenger2.setFamilyName("TEST");
		passenger2.setGivenName("SAM");
		passenger2.setPassengerType("CHDF");
		passenger2.setFQTVInfos(fQTVInfos);

		passenger2.setLoginMember(false);
		passenger3 = new RetrievePnrPassenger();
		List<RetrievePnrEmail> pnrEmails = new ArrayList<>();
		RetrievePnrEmail pnrEmail = new RetrievePnrEmail();
		pnrEmail.setCompanyId("CX");
		pnrEmail.setType("CTCE");
		pnrEmail.setOlssContact(true);
		pnrEmails.add(pnrEmail);
		passenger3.setPassengerID("2");
		passenger3.setParentId("1");
		passenger3.setFamilyName("TEST");
		passenger3.setGivenName("SAME");
		passenger3.setPassengerType("CHD");
		passenger3.setLoginMember(false);
		passenger3.setEmails(pnrEmails);
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		passengers.add(passenger);
		// passengers.add(passenger2);
		passengers.add(passenger3);
		pnrBooking.setPassengers(passengers);
		List<RetrievePnrFa> faLists = new ArrayList<>();
		RetrievePnrFa faList = new RetrievePnrFa();
		faList.setPassengerId("1");
		faList.setLongFreeText("DTCX");
		faLists.add(faList);
		pnrBooking.setFaList(faLists);

		List<TravelDocList> travelDocLists = new ArrayList<>();
		TravelDocList travelDocList = new TravelDocList();
		travelDocList.setTravelDocCode("PT");
		travelDocList.setTravelDocType("PT");
		travelDocLists.add(travelDocList);

		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		RetrievePnrBaggage baggage = new RetrievePnrBaggage();
		baggage.setWeightAmount(new BigInteger("111"));
		passengerSegment.setBaggages(Arrays.asList(baggage));
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegment.setPriTravelDocs(priTravelDocs);
		passengerSegment.setSecTravelDocs(secTravelDocs);
		passengerSegment.setOthTravelDocs(othTravelDocs);
		passengerSegment.setFQTVInfos(fQTVInfos);
		RetrievePnrMeal meal = new RetrievePnrMeal();
		meal.setCompanyId("CX");
		meal.setMealCode("meal");
		meal.setQuantity(1);
		meal.setStatus("start");
		passengerSegment.setMeal(meal);

		List<RetrievePnrEticket> etickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber("eticket");
		etickets.add(retrievePnrEticket);
		passengerSegment.setEtickets(etickets);
		seat = new RetrievePnrSeat();
		RetrievePnrSeatDetail seatDetail = new RetrievePnrSeatDetail();
		seatDetail.setSeatNo("044E");
		seatDetail.setIndicator("KK");
		List<String> seatCharacteristics = new ArrayList<>();
		seatCharacteristics.add("LL");
		seatDetail.setSeatCharacteristics(seatCharacteristics);
		seat.setSeatDetail(seatDetail);
		passengerSegment.setSeat(seat);

		RetrievePnrPassengerSegment passengerSegment2 = new RetrievePnrPassengerSegment();
		passengerSegment2.setPassengerId("2");
		passengerSegment2.setSegmentId("1");
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		passengerSegments.add(passengerSegment);
		passengerSegments.add(passengerSegment2);
		pnrBooking.setPassengerSegments(passengerSegments);

		List<TravelDocOD> travelDocOds = new ArrayList<>();
		TravelDocOD travelDocOd = new TravelDocOD() ;
		travelDocOd.setAppCode("MMB");
		travelDocOd.setOrigin("**");
		travelDocOd.setDestination("HK");
		travelDocOds.add(travelDocOd);
		when(travelDocListCacheHelper.findAll()).thenReturn(travelDocLists);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		memberprofile.setContact(contact);
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(tbTravelDocOdCacheHelper.findByAppCodeInAndStartDateBefore(anyObject(),anyObject())).thenReturn(travelDocOds);
		when(travelDocListCacheHelper.findVersionByTypeGroupByCode(anyObject()))
				.thenReturn(new ArrayList<>());
		when(travelDocListCacheHelper.findVersionByTypeGroupByCode(anyObject()))
				.thenReturn(new ArrayList<>());
		when(encryptionHelper.encryptMessage(pnrBooking.getOneARloc(), Encoding.BASE64, MMBBizruleConstants.IBE_KEY))
				.thenReturn("ENCRYPTEDRLOC");
		when(bookingStatusDAO.findStatusCodeByAppCodeAndActionIn(anyObject(), anyObject())).thenReturn(statusList);
		
		when(asrEnableCheckDAO.getASREnableCheck(anyString(), anyString(), anyString())).thenReturn(null);
		when(statusManagementDAO.findByAppCode(anyObject())).thenReturn(new ArrayList<>());
		when(specialServiceDAO.findAllByAppCodeAndAction(anyObject(),anyObject())).thenReturn(new ArrayList<>());


		List<BookingStatus> buBookingStatusList = new ArrayList<>();
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		buBookingStatusList.add(bookingStatus);
		when(bookingBuildHelper.getFirstAvailableStatus(anyObject())).thenReturn(bookingStatus);
		when(bookingStatusDAO.findAvailableStatus(anyObject())).thenReturn(buBookingStatusList);
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HK")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("LON")).thenReturn("+1000");
		// when(tbTravelDocOdDAO.findByStartDateBefore(anyObject(),
		// anyObject())).thenReturn(new ArrayList<>());
		when(cabinClassDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(new ArrayList<>());

		when(aemService.getCountryCodeByPortCode("LON")).thenReturn("CNN");
		when(aemService.getCountryCodeByPortCode("HK")).thenReturn("HK");
		AirFlightInfoBean airFlightInfoBean = new AirFlightInfoBean();
		airFlightInfoBean.setCarrierCode("CX");
		airFlightInfoBean.setFlightNumber("520");
		airFlightInfoBean.setStopOverFlight(true);
		List<String> stops = new ArrayList<>();
		stops.add("1");
		airFlightInfoBean.setStops(stops);
		BigDecimal b4 = new BigDecimal("123456789");
		airFlightInfoBean.setNumberOfStops(b4);
//		doNothing().when(flightStatusService).populateFlightDetailTime(Matchers.any(Booking.class));
		when(olciConfig.getCheckInWindowUpper()).thenReturn(172800000l);
		List<ConstantData> constantDatas = new ArrayList<>();
		ConstantData constantData = new ConstantData();
		constantData.setValue("TITLE");
		constantDatas.add(0, constantData);
		List<TBSsrSkMapping> tbSsrSkMappings = new ArrayList<>();
		TBSsrSkMapping tBSsrSkMapping = new TBSsrSkMapping();
		tBSsrSkMapping.setSsrSkCode("CK");
		TBSsrSkMapping tBSsrSkMapping2 = new TBSsrSkMapping();
		tBSsrSkMapping.setSsrSkCode("DOCA");
		tbSsrSkMappings.add(tBSsrSkMapping);
		tbSsrSkMappings.add(tBSsrSkMapping2);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB)).thenReturn(constantDatas);
		when(tBSsrSkMappingDao.findByAppCodeAndSeat(any(),any())).thenReturn(tbSsrSkMappings);
		when(airFlightInfoService.getAirFlightInfo(segment.getDepartureTime().getTime(), segment.getOriginPort(),
				segment.getDestPort(), segment.getMarketCompany(), segment.getMarketSegmentNumber(), ""))
						.thenReturn(airFlightInfoBean);
		List<PassengerCheckInInfo> passengerCheckInInfos = new ArrayList<>();
		PassengerCheckInInfo passengerCheckInInfo = new PassengerCheckInInfo();
		passengerCheckInInfo.setCarrierCode("CX");
		passengerCheckInInfo.setFltNo("520");
		passengerCheckInInfo.setFlightDate(sdf.parse(sdf.format(departureTimePassed)));
		List<PassengerInfoType> passengerInfoTypes = new ArrayList<>();
		PassengerInfoType passengerInfoType = new PassengerInfoType();
		passengerInfoType.setCheckInIndicator("Y");
		passengerInfoType.setSeatNum("055E");
		PersonalInfoType personalInfoType = new PersonalInfoType();
		personalInfoType.setFamilyName("TEST");
		personalInfoType.setFirstName("TEST");
		passengerInfoType.setPersonalInfo(personalInfoType);

		PassengerInfoType passengerInfoType2 = new PassengerInfoType();
		passengerInfoType2.setCheckInIndicator("N");
		passengerInfoType2.setSeatNum("055E");
		PersonalInfoType personalInfoType2 = new PersonalInfoType();
		personalInfoType2.setFamilyName("TEST");
		personalInfoType2.setFirstName("SAM");
		passengerInfoType2.setPersonalInfo(personalInfoType2);

		List<RetrievePnrTicket> ticketList = new ArrayList<>();
		RetrievePnrTicket retrievePnrTicket = new RetrievePnrTicket();
		retrievePnrTicket.setIndicator("XL");
		retrievePnrTicket.setOfficeId("HK0001");
		retrievePnrTicket.setDate("211018");
		ticketList.add(retrievePnrTicket);
		pnrBooking.setTicketList(ticketList);

		List<String> subClasses = new ArrayList<>();
		subClasses.add("Z");
		subClasses.add("U");
		List<String> issuingOffice = new ArrayList<>();
		issuingOffice.add("HK0001");
		issuingOffice.add("GG");
		List<String> confirmedList = new ArrayList<>();
		confirmedList.add("HK");
		confirmedList.add("CC");

		when(bizRuleConfig.getRedemptionSegmentSubclass()).thenReturn(subClasses);
		when(bizRuleConfig.getRedemptionIssuingOffice()).thenReturn(issuingOffice);
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(issuingOffice);
		when(bizRuleConfig.getAmTierLevel()).thenReturn(issuingOffice);
		MemberAwardResponse response = new MemberAwardResponse();
		response.setTotalBasicAwardMiles(1);
		response.setTotalClubPoints(2);
		response.setTotalTierBonusMiles(2);
		when(memberAwardService.getMemberAward(any())).thenReturn(response);
		when(bookingStatusCOnfig.getConfirmedList()).thenReturn(confirmedList);
		when(redemptionTPOSCheckDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(null);
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(anyString(), anyString())).thenReturn(null);
		when(seatRuleService.getEligibleRBDForSeatSelection()).thenReturn(Arrays.asList("B", "E", "H", "K", "L", "M", "N", "O", "Q", "R", "S", "T", "V", "W", "Y"));
		passengerInfoTypes.add(passengerInfoType);
		passengerInfoTypes.add(passengerInfoType2);
		passengerCheckInInfo.setPassengerDetails(passengerInfoTypes);
		passengerCheckInInfos.add(passengerCheckInInfo);
		when(olciServiceV2.asyncRetrieveCPRBooking(anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyBoolean()))
				.thenReturn(CompletableFuture.completedFuture(null));
		
		ReflectionTestUtils.setField(bookingBuildServiceImpl, "shortCompareSize", 4);
		when(bookingStatusConfig.getConfirmedList()).thenReturn(confirmedList);
		TicketProcessInfo ticketProcessInfo = new TicketProcessInfo();
		List<TicketProcessDocGroup> infoGroups = new ArrayList<>();
		TicketProcessDocGroup infoGroup = new TicketProcessDocGroup();
		List<TicketProcessDetailGroup> detailInfos = new ArrayList<>();
		TicketProcessDetailGroup detailInfo = new TicketProcessDetailGroup();
		detailInfo.setEticket("eticket");
		List<TicketProcessCouponGroup> couponInfos = new ArrayList<>();
		TicketProcessCouponGroup couponInfo = new TicketProcessCouponGroup();
		List<TicketProcessBaggageAllowance> baggageAllowances = new ArrayList<>();
		TicketProcessBaggageAllowance baggageAllowance = new TicketProcessBaggageAllowance();
		baggageAllowance.setNumber(b1);
		baggageAllowance.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		baggageAllowances.add(baggageAllowance);
		couponInfo.setBaggageAllowances(baggageAllowances);
		List<TicketProcessFlightInfo> flightInfos = new ArrayList<>();
		TicketProcessFlightInfo flightInfo = new TicketProcessFlightInfo();
		flightInfo.setFlightNumber("520");
		flightInfo.setBoardPoint("HK");
		flightInfo.setOffpoint("LON");
		TicketProcessFlightDate flightDate = new TicketProcessFlightDate();
		flightDate.setDepartureTime(simpleDateFormat2.format(departureTimePassed));
		flightDate.setDepartureDate(simpleDateFormat1.format(departureTimePassed));
		flightInfo.setFlightDate(flightDate);
		flightInfo.setMarketingCompany("CX");
		flightInfos.add(flightInfo);
		couponInfo.setFlightInfos(flightInfos);
		couponInfos.add(couponInfo);
		detailInfo.setCouponGroups(couponInfos);
		detailInfos.add(detailInfo);
		infoGroup.setDetailInfos(detailInfos);
		infoGroups.add(infoGroup);
		ticketProcessInfo.setDocGroups(infoGroups);
		when(ticketProcessInvokeService.getTicketProcessInfo(anyString(), anyObject())).thenReturn(ticketProcessInfo);

		required = new BookingBuildRequired();
		required.setMiceBookingCheck(false);
		
		when(mbTokenCacheRepository.get(anyString(), anyObject(), anyString(), anyObject())).thenReturn(null);
		when(mbTokenCacheLockRepository.get(anyString(), anyObject(), anyObject(), anyString(), anyObject())).thenReturn(null);
		
		when(linkTempBookingRepository.getLinkedBookings(anyString())).thenReturn(Collections.emptyList());
	}

	@Test
	public void test_BookingBuild1() throws BusinessBaseException, ParseException {
		pnrBooking.getPassengers().add(passenger2);
		pnrBooking.getPassengers().add(passenger3);
		pnrBooking.getSegments().get(0).setAirCraftType("BUSS");
		AirFlightInfoBean airFlightInfoBean = new AirFlightInfoBean();
		airFlightInfoBean.setCarrierCode("CX");
		airFlightInfoBean.setFlightNumber("520");
		when(airFlightInfoService.getAirFlightInfo(anyObject(), anyObject(), anyObject(), anyObject(), anyObject(),
				anyString())).thenReturn(airFlightInfoBean);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertFalse(booking.getPassengerSegments().get(1).isCheckedIn());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
		

	}

	@Test
	public void test_BookingBuild2() throws BusinessBaseException, ParseException {
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);	
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
	}

	@Test
	public void test_BookingBuild3() throws BusinessBaseException, ParseException {
		pnrBooking.getSegments().get(0).setPnrOperateCompany("KA");
		pnrBooking.getSegments().get(0).getStatus().add("L");

		RetrievePnrSeatPreference preference = new RetrievePnrSeatPreference();
		List<RetrievePnrSeatDetail> extraSeats = new ArrayList<>();
		RetrievePnrSeatDetail extraSeat = new RetrievePnrSeatDetail();
		extraSeat.setSeatNo("123");
		extraSeat.setPaid(true);
		extraSeat.setSeatCharacteristics(pnrBooking.getSegments().get(0).getStatus());
		extraSeat.setStatus("start");
		extraSeats.add(extraSeat);
		preference.setPreferenceCode("good");
		preference.setSpeicalPreference(true);
		preference.setStatus("start");
		seat.setPreference(preference);
		seat.setExtraSeats(extraSeats);
		pnrBooking.getPassengerSegments().get(0).setSeat(seat);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		pnrStatusList.add("L");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);	
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
	}

	@Test
	public void test_BookingBuild4() throws BusinessBaseException, ParseException {
		loginInfo.setLoginType("R");
		pnrBooking.getSkList().get(0).setType("ADTK");
		pnrBooking.getSegments().get(0).setPnrOperateCompany("KA");
		List<RetrievePnrRemark> remarkList = new ArrayList<>();
		RetrievePnrRemark remark = new RetrievePnrRemark();
		remark.setFreeText("STATUS:ON HOLD");
		remarkList.add(remark);
		pnrBooking.setRemarkList(remarkList);
		List<RetrievePnrAddressDetails> desAddresses = new ArrayList<>();
		RetrievePnrAddressDetails desAddresse = new RetrievePnrAddressDetails();
		desAddresse.setCity("HK");
		desAddresse.setStateCode("HH");
		desAddresse.setStreet("CN");
		desAddresse.setZipCode("1");
		desAddresses.add(desAddresse);
		pnrBooking.getPassengerSegments().get(0).setDesAddresses(desAddresses);
		pnrBooking.getTicketList().get(0).setIndicator("XT");
		List<String> confirmedList = new ArrayList<>();
		confirmedList.add("HK");
		confirmedList.add("CC");
		when(bizRuleConfig.getTopTier()).thenReturn(confirmedList);
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("KA");
		redemptionSubclassCheck.setSubclass("Z");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(MMBConstants.APP_CODE, "KA"))
				.thenReturn(redemptionSubclassChecks);		
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.isCanIssueTicket());
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());	
	}

	@Test
	public void test_BookingBuildIssueTicketDBMatch() throws BusinessBaseException, ParseException {
		loginInfo.setLoginType("R");
		List<RedemptionTPOSCheck> redemptionChecks = new ArrayList<>();
		RedemptionTPOSCheck redemptionCheck = new RedemptionTPOSCheck();
		redemptionCheck.setType("TPOS");
		redemptionCheck.setValue("LP1");
		redemptionChecks.add(redemptionCheck);
		RetrievePnrDataElements retrievePnrSsrSk = new RetrievePnrDataElements();
		retrievePnrSsrSk.setFreeText("LP1");
		retrievePnrSsrSk.setType("TPOS");
		retrievePnrSsrSk.setPassengerId("1");
		retrievePnrSsrSk.setSegmentId("1");
		pnrBooking.getSkList().add(retrievePnrSsrSk);
		when(redemptionTPOSCheckDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(redemptionChecks);
		when(redemptionTPOSCheckDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.TPOS))
				.thenReturn(redemptionChecks);
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("CX");
		redemptionSubclassCheck.setSubclass("Z");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(MMBConstants.APP_CODE, "CX"))
				.thenReturn(redemptionSubclassChecks);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
		.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.isCanIssueTicket());
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
	}

	@Test
	public void test_BookingBuildIssueTicketDBNotMatch() throws BusinessBaseException, ParseException {
		List<RedemptionTPOSCheck> redemptionChecks = new ArrayList<>();
		RedemptionTPOSCheck redemptionCheck = new RedemptionTPOSCheck();
		redemptionCheck.setType("TPOS");
		redemptionCheck.setValue("LP2");
		redemptionChecks.add(redemptionCheck);
		RetrievePnrDataElements retrievePnrSsrSk = new RetrievePnrDataElements();
		retrievePnrSsrSk.setFreeText("LP1");
		retrievePnrSsrSk.setType("TPOS");
		retrievePnrSsrSk.setPassengerId("1");
		retrievePnrSsrSk.setSegmentId("1");
		pnrBooking.getSkList().add(retrievePnrSsrSk);
		when(redemptionTPOSCheckDAO.count()).thenReturn(1l);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(redemptionTPOSCheckDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.TPOS))
				.thenReturn(redemptionChecks);		
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.isCanIssueTicket());
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
	}

	@Test
	public void test_BookingBuild5() throws BusinessBaseException, ParseException {
		pnrBooking.getPassengers().add(passenger2);
		pnrBooking.getPassengers().add(passenger3);
		passenger.getContactPhones().get(0).setType("CTCM");
		passenger.getContactPhones().get(0).setCompanyId("CX");
		passenger.getContactPhones().get(0).setOlssContact(true);
		pnrBooking.getPassengers().add(passenger);
		pnrBooking.getSegments().get(0).setAirCraftType("BUSS");
		AirFlightInfoBean airFlightInfoBean = new AirFlightInfoBean();
		airFlightInfoBean.setCarrierCode("CX");
		airFlightInfoBean.setFlightNumber("520");
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(airFlightInfoService.getAirFlightInfo(anyObject(), anyObject(), anyObject(), anyObject(), anyObject(),
				anyString())).thenReturn(airFlightInfoBean);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getSegments().get(0).isWithinNinetyMins());
			assertFalse(booking.getSegments().get(0).isWithinTwentyFourHrs());
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertFalse(booking.getPassengerSegments().get(1).isCheckedIn());
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());		
	}

	@Test(expected = ExpectedException.class)
	public void test_IncompleteRedemptionBookingRlocLogin() throws BusinessBaseException {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_RLOC);
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("CX");
		redemptionSubclassCheck.setSubclass("Z");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		List<RetrievePnrFFPInfo> fQTRInfos = new ArrayList<>();
		RetrievePnrFFPInfo fqtrInfo = new RetrievePnrFFPInfo();
		fqtrInfo.setCompanyId("1");
		fQTRInfos.add(fqtrInfo);
		fqtrInfo.setFfpCompany("CX");
		pnrBooking.getPassengerSegments().get(0).setFQTRInfos(fQTRInfos);
		pnrBooking.getPassengerSegments().get(0).setEtickets(null);
		doNothing().when(bookingBuildValidationHelpr).removeAllFqtvForIDBooking(Matchers.any());
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompanyAndSubclass(MMBConstants.APP_CODE, "CX", "Z"))
				.thenReturn(redemptionSubclassChecks);
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HKG")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("LON")).thenReturn("+1200");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("JFK")).thenReturn("-0500");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("YVR")).thenReturn("-0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("SHA")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("TPE")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HK")).thenReturn("+0800");
		
		bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);		
	}

	@Test
	public void test_IncompleteRedemptionBookingMemberLogin() throws BusinessBaseException {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("CX");
		redemptionSubclassCheck.setSubclass("Z");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		List<RetrievePnrFFPInfo> fQTRInfos = new ArrayList<>();
		RetrievePnrFFPInfo fqtrInfo = new RetrievePnrFFPInfo();
		fqtrInfo.setCompanyId("1");
		fqtrInfo.setFfpMembershipNumber("1");
		fqtrInfo.setFfpCompany("1");
		fQTRInfos.add(fqtrInfo);
		pnrBooking.getPassengerSegments().get(0).setFQTRInfos(fQTRInfos);
		pnrBooking.getPassengerSegments().get(0).setEtickets(null);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(MMBConstants.APP_CODE, "CX"))
				.thenReturn(redemptionSubclassChecks);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
	}

	@Test
	public void test_IncompleteRedemptionBookingEticketLogin() throws BusinessBaseException {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_ETICKET);
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("CX");
		redemptionSubclassCheck.setSubclass("Z");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		List<RetrievePnrFFPInfo> fQTRInfos = new ArrayList<>();
		RetrievePnrFFPInfo fqtrInfo = new RetrievePnrFFPInfo();
		fqtrInfo.setCompanyId("1");
		fqtrInfo.setFfpMembershipNumber("1");
		fqtrInfo.setFfpCompany("1");
		fQTRInfos.add(fqtrInfo);
		pnrBooking.getPassengerSegments().get(0).setFQTRInfos(fQTRInfos);
		pnrBooking.getPassengerSegments().get(0).setEtickets(null);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(MMBConstants.APP_CODE, "CX"))
				.thenReturn(redemptionSubclassChecks);	
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
		.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());		
	}

	@Test
	public void test_BookingASREnable() throws BusinessBaseException, ParseException {
		RetrievePnrBookingCerateInfo createInfo = new RetrievePnrBookingCerateInfo();
		pnrBooking.setBookingCreateInfo(createInfo);
		createInfo.setRpOfficeId("OFFICEID");
		pnrBooking.getSegments().get(0).setSubClass("X");

		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("KA");
		redemptionSubclassCheck.setSubclass("X");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(anyString(), anyString()))
				.thenReturn(redemptionSubclassChecks);
		AsrEnableCheck asrEnableCheck = new AsrEnableCheck();
		asrEnableCheck.setAppCode("MMB");
		asrEnableCheck.setOfficeId("OFFICEID");
		asrEnableCheck.setSeatSelection(true);
		asrEnableCheck.setTpos("*");
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, "OFFICEID", null))
				.thenReturn(Arrays.asList(asrEnableCheck));	
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertFalse(booking.isRedemptionBooking());
			assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isEligible()));
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());		
	}
	
	

	@Test
	public void test_BaggageAllowance() throws BusinessBaseException, ParseException {
		pnrBooking.getSegments().get(0).setSubClass("X");
		RetrievePnrBookingCerateInfo createInfo = new RetrievePnrBookingCerateInfo();
		pnrBooking.setBookingCreateInfo(createInfo);
		createInfo.setRpOfficeId("OFFICEID");
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("KA");
		redemptionSubclassCheck.setSubclass("X");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(anyString(), anyString()))
				.thenReturn(redemptionSubclassChecks);
		AsrEnableCheck asrEnableCheck = new AsrEnableCheck();
		asrEnableCheck.setAppCode("MMB");
		asrEnableCheck.setOfficeId("OFFICEID");
		asrEnableCheck.setSeatSelection(true);
		asrEnableCheck.setTpos("*");
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		when(asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, "OFFICEID", null))
				.thenReturn(Arrays.asList(asrEnableCheck));		
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertFalse(booking.isRedemptionBooking());
			assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isEligible()));
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
			// standard baggage
			Assert.assertEquals(new BigInteger("123456789"), booking.getPassengerSegments().get(0).getBaggageAllowance()
					.getCheckInBaggage().getStandardBaggage().getAmount());
			Assert.assertEquals(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT, booking.getPassengerSegments().get(0)
					.getBaggageAllowance().getCheckInBaggage().getStandardBaggage().getUnit());

			// waiver baggage
			Assert.assertEquals(new BigInteger("0"), booking.getPassengerSegments().get(0).getBaggageAllowance()
					.findCheckInBaggage().findWaiverBaggage().findAmount());
			Assert.assertEquals(null, booking.getPassengerSegments().get(0)
					.getBaggageAllowance().getCheckInBaggage().getWaiverBaggage().getUnit());

			// purchase baggage
			Assert.assertEquals(null, booking.getPassengerSegments().get(0).findBaggageAllowance()
					.findCheckInBaggage().getPurchasedBaggages());		
	}

	@Test
	public void test_BaggageAllowance1() throws BusinessBaseException, ParseException {
		pnrBooking.getSegments().get(0).setSubClass("X");
		RetrievePnrBookingCerateInfo createInfo = new RetrievePnrBookingCerateInfo();
		pnrBooking.setBookingCreateInfo(createInfo);
		createInfo.setRpOfficeId("OFFICEID");
		pnrBooking.getSsrList().get(0).setType("CBAG");
		;
		List<RedemptionSubclassCheck> redemptionSubclassChecks = new ArrayList<>();
		RedemptionSubclassCheck redemptionSubclassCheck = new RedemptionSubclassCheck();
		redemptionSubclassCheck.setAppCode("MMB");
		redemptionSubclassCheck.setOperateCompany("KA");
		redemptionSubclassCheck.setSubclass("X");
		redemptionSubclassChecks.add(redemptionSubclassCheck);
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		when(redemptionSubclassCheckDAO.findByAppCodeAndOperateCompany(anyString(), anyString()))
				.thenReturn(redemptionSubclassChecks);
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);
		AsrEnableCheck asrEnableCheck = new AsrEnableCheck();
		asrEnableCheck.setAppCode("MMB");
		asrEnableCheck.setOfficeId("OFFICEID");
		asrEnableCheck.setSeatSelection(true);
		asrEnableCheck.setTpos("*");
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(asrEnableCheckDAO.getASREnableCheck(MMBConstants.APP_CODE, "OFFICEID", null))
				.thenReturn(Arrays.asList(asrEnableCheck));		
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
		.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(booking.getPassengerSegments().get(0).isCheckedIn());
			assertFalse(booking.getPassengerSegments().get(0).getSeat().isFromDCS());
			assertFalse(booking.isRedemptionBooking());
			assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isEligible()));
			assertTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
			assertTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());
			// standard baggage
			Assert.assertEquals(new BigInteger("123456789"), booking.getPassengerSegments().get(0).getBaggageAllowance()
					.getCheckInBaggage().getStandardBaggage().getAmount());
			Assert.assertEquals(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT, booking.getPassengerSegments().get(0)
					.getBaggageAllowance().getCheckInBaggage().getStandardBaggage().getUnit());

			// waiver baggage
			Assert.assertEquals(null, booking.getPassengerSegments().get(0).getBaggageAllowance()
					.getCheckInBaggage().findWaiverBaggage().getAmount());
			Assert.assertEquals(null, booking.getPassengerSegments().get(0)
					.getBaggageAllowance().getCheckInBaggage().getWaiverBaggage().getUnit());

			// shared waiver baggage
			Assert.assertEquals(0, booking.getPassengerSegments().get(1).findBaggageAllowance()
					.findCheckInBaggage().findSharedWaiverBaggages().size());
			Assert.assertEquals(0, booking.getPassengerSegments().get(1).getBaggageAllowance()
					.getCheckInBaggage().getSharedWaiverBaggages().size());
			Assert.assertEquals(0,
					booking.getPassengerSegments().get(1).getBaggageAllowance().getCheckInBaggage()
							.getSharedWaiverBaggages().size());

			Assert.assertEquals(0, booking.getPassengerSegments().get(1).getBaggageAllowance()
					.getCheckInBaggage().getSharedWaiverBaggages().size());
			Assert.assertEquals(0, booking.getPassengerSegments().get(1).getBaggageAllowance()
					.getCheckInBaggage().getSharedWaiverBaggages().size());
			Assert.assertEquals(0,
					booking.getPassengerSegments().get(1).getBaggageAllowance().getCheckInBaggage()
							.getSharedWaiverBaggages().size());		
	}

	@Test
	public void test_CountryOfResidenceAndGenderDOB() throws BusinessBaseException {
		loginInfo.setLoginType("R");
		RetrievePnrBooking retrievePnrBooking = new RetrievePnrBooking();

		RetrievePnrPassenger retrievePnrPassenger = new RetrievePnrPassenger();

		retrievePnrPassenger.setPassengerID("1");
		retrievePnrPassenger.setFamilyName("TEST");
		retrievePnrPassenger.setGivenName("TEST");
		retrievePnrPassenger.setPassengerType("ADT");

		retrievePnrPassenger.setLoginMember(true);
		retrievePnrBooking.setPassengers(new ArrayList<>());
		retrievePnrBooking.getPassengers().add(retrievePnrPassenger);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date departureTimePassed1 = new Date();
		departureTimePassed1.setTime(System.currentTimeMillis() + 87000000l);
		Date departureTimePassed2 = new Date();
		departureTimePassed2.setTime(System.currentTimeMillis() + 87100000l);

		RetrievePnrSegment segment1 = new RetrievePnrSegment();
		RetrievePnrDepartureArrivalTime departureTime1 = new RetrievePnrDepartureArrivalTime();
		departureTime1.setTimeZoneOffset("+0800");
		departureTime1.setRtfsScheduledTime(simpleDateFormat.format(departureTimePassed1));
		departureTime1.setRtfsEstimatedTime(simpleDateFormat.format(departureTimePassed1));
		departureTime1.setPnrTime(simpleDateFormat.format(departureTimePassed1));
		segment1.setDepartureTime(departureTime1);
		segment1.setSegmentID("1");
		segment1.setAirCraftType("TRN");
		segment1.setPnrOperateCompany("CX");
		segment1.setPnrOperateSegmentNumber("520");
		segment1.setMarketCompany("CX");
		segment1.setMarketSegmentNumber("520");
		segment1.setOriginPort("HK");
		segment1.setDestPort("LON");
		segment1.setSubClass("Z");
		List<String> statusList1 = new ArrayList<>();
		statusList1.add("HK");
		statusList1.add("CC");
		segment1.setStatus(statusList1);

		RetrievePnrSegment segment2 = new RetrievePnrSegment();
		RetrievePnrDepartureArrivalTime departureTime2 = new RetrievePnrDepartureArrivalTime();
		departureTime2.setTimeZoneOffset("+0800");
		departureTime2.setRtfsScheduledTime(simpleDateFormat.format(departureTimePassed2));
		departureTime2.setRtfsEstimatedTime(simpleDateFormat.format(departureTimePassed2));
		departureTime2.setPnrTime(simpleDateFormat.format(departureTimePassed2));
		segment2.setDepartureTime(departureTime2);
		segment2.setSegmentID("2");
		segment2.setAirCraftType("TRN");
		segment2.setPnrOperateCompany("CX");
		segment2.setPnrOperateSegmentNumber("520");
		segment2.setMarketCompany("CX");
		segment2.setMarketSegmentNumber("520");
		segment2.setOriginPort("HK");
		segment2.setDestPort("LON");
		segment2.setSubClass("Z");
		List<String> statusList2 = new ArrayList<>();
		statusList2.add("HK");
		statusList2.add("CC");
		segment2.setStatus(statusList2);

		List<RetrievePnrSegment> segments = new ArrayList<>();
		segments.add(segment1);
		segments.add(segment2);
		retrievePnrBooking.setSegments(segments);

		List<RetrievePnrTravelDoc> priTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc priTravelDoc1 = new RetrievePnrTravelDoc();
		RetrievePnrTravelDoc priTravelDoc2 = new RetrievePnrTravelDoc();

		BigInteger b1 = new BigInteger("1");
		priTravelDoc1.setQualifierId(b1);
		priTravelDoc1.setBirthDateDay("01");
		priTravelDoc1.setBirthDateMonth("01");
		priTravelDoc1.setBirthDateYear("2018");
		priTravelDoc1.setGender("M");
		priTravelDoc1.setTravelDocumentType("PT");

		BigInteger b2 = new BigInteger("2");
		priTravelDoc2.setQualifierId(b2);
		priTravelDoc2.setBirthDateDay("02");
		priTravelDoc2.setBirthDateMonth("01");
		priTravelDoc2.setBirthDateYear("2018");
		priTravelDoc2.setGender("M");
		priTravelDoc2.setTravelDocumentType("PT");

		priTravelDocs.add(priTravelDoc1);
		priTravelDocs.add(priTravelDoc2);

		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment1 = new RetrievePnrPassengerSegment();

		passengerSegment1.setPassengerId("1");
		passengerSegment1.setSegmentId("1");
		passengerSegment1.setPriTravelDocs(priTravelDocs);
		List<RetrievePnrAddressDetails> resAddresses1 = new ArrayList<>();
		RetrievePnrAddressDetails resAddress1 = new RetrievePnrAddressDetails();
		resAddress1.setCountry("JP");
		resAddress1.setQualifierId(new BigInteger("1"));
		RetrievePnrAddressDetails resAddress2 = new RetrievePnrAddressDetails();
		resAddress2.setCountry("CN");
		resAddress2.setQualifierId(new BigInteger("2"));
		resAddresses1.add(resAddress1);
		resAddresses1.add(resAddress2);
		passengerSegment1.setResAddresses(resAddresses1);

		RetrievePnrPassengerSegment passengerSegment2 = new RetrievePnrPassengerSegment();
		passengerSegment2.setPassengerId("1");
		passengerSegment2.setSegmentId("2");
		List<RetrievePnrAddressDetails> resAddresses2 = new ArrayList<>();
		RetrievePnrAddressDetails resAddress3 = new RetrievePnrAddressDetails();
		resAddress3.setCountry("IN");
		resAddress3.setQualifierId(new BigInteger("3"));
		resAddresses2.add(resAddress3);
		passengerSegment2.setResAddresses(resAddresses2);

		passengerSegments.add(passengerSegment1);
		passengerSegments.add(passengerSegment2);
		retrievePnrBooking.setPassengerSegments(passengerSegments);	
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(retrievePnrBooking, loginInfo, required);
//			assertTrue(booking.getPassengerSegments().get(0).getSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(0).getMmbSeatSelection().isPaidASR());
//			assertTrue(booking.getPassengerSegments().get(1).getSeatSelection().isAsrFOC());
			assertFalse(booking.getPassengerSegments().get(1).getMmbSeatSelection().isPaidASR());		
	}
	
	@Test
	public void test_AsrExlSeat1() throws BusinessBaseException, ParseException {
		
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment retrievePnrPassengerSegment = new RetrievePnrPassengerSegment();

		RetrievePnrSeat seat = new RetrievePnrSeat();
		RetrievePnrSeatDetail seatDetail = new RetrievePnrSeatDetail();
		List<String> seatCharacteristics = new ArrayList<>();
		seatCharacteristics.add("CH");
		seatDetail.setSeatCharacteristics(seatCharacteristics);
		seat.setSeatDetail(seatDetail);	

		retrievePnrPassengerSegment.setPassengerId("1");
		retrievePnrPassengerSegment.setSegmentId("1");
		retrievePnrPassengerSegment.setSeat(seat);
		passengerSegments.add(retrievePnrPassengerSegment);
		
		loginInfo.setLoginType("R");
		pnrBooking.getSegments().get(0).setSubClass("Y");
		pnrBooking.setPassengerSegments(passengerSegments);
		List<CabinClass> cabinClasses = new ArrayList<>();
		CabinClass cabinClass = new CabinClass();
		cabinClass.setSubclass("Y");
		cabinClass.setBasicClass("Y");
		cabinClasses.add(cabinClass);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(cabinClassDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(cabinClasses);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
			.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).findSeat().isAsrSeat()));
			assertFalse(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).findSeat().isExlSeat()));
	}
	@Test
	public void test_AsrExlSeat() throws BusinessBaseException, ParseException {
		
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment retrievePnrPassengerSegment = new RetrievePnrPassengerSegment();

		RetrievePnrSeat seat = new RetrievePnrSeat();
		RetrievePnrSeatDetail seatDetail = new RetrievePnrSeatDetail();
		List<String> seatCharacteristics = new ArrayList<>();
		seatCharacteristics.add("CH");
		seatDetail.setSeatCharacteristics(seatCharacteristics);
		seat.setSeatDetail(seatDetail);	

		retrievePnrPassengerSegment.setPassengerId("1");
		retrievePnrPassengerSegment.setSegmentId("1");
		retrievePnrPassengerSegment.setSeat(seat);
		passengerSegments.add(retrievePnrPassengerSegment);
		
		loginInfo.setLoginType("R");
		pnrBooking.getSegments().get(0).setSubClass("Y");
		pnrBooking.setPassengerSegments(passengerSegments);
		List<CabinClass> cabinClasses = new ArrayList<>();
		CabinClass cabinClass = new CabinClass();
		cabinClass.setSubclass("Y");
		cabinClass.setBasicClass("Y");
		cabinClasses.add(cabinClass);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(cabinClassDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(cabinClasses);
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
		seatCharacteristics.add("L");
			Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
			assertFalse(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).findSeat().isAsrSeat()));
			assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).findSeat().isExlSeat()));
	
	
	}
	
	@Test
	public void test_SeatSelection() throws BusinessBaseException, ParseException {
		loginInfo.setLoginType("R");
		RetrievePnrBooking retrievePnrBooking = new RetrievePnrBooking();
		
		RetrievePnrDataElements dataElements = new RetrievePnrDataElements();
		dataElements.setType("XLWR");
		dataElements.setPassengerId("1");
		retrievePnrBooking.findSkList().add(dataElements);

		RetrievePnrPassenger retrievePnrPassenger1 = new RetrievePnrPassenger();

		retrievePnrPassenger1.setPassengerID("1");
		retrievePnrPassenger1.setFamilyName("TEST");
		retrievePnrPassenger1.setGivenName("ONE");
		retrievePnrPassenger1.setPassengerType("ADT");

		retrievePnrBooking.setPassengers(new ArrayList<>());
		retrievePnrBooking.getPassengers().add(retrievePnrPassenger1);
		
		RetrievePnrPassenger retrievePnrPassenger2 = new RetrievePnrPassenger();

		retrievePnrPassenger2.setPassengerID("2");
		retrievePnrPassenger2.setFamilyName("TEST");
		retrievePnrPassenger2.setGivenName("TWO");
		retrievePnrPassenger2.setPassengerType("ADT");

		retrievePnrBooking.getPassengers().add(retrievePnrPassenger2);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date departureTimePassed1 = new Date();
		departureTimePassed1.setTime(System.currentTimeMillis() + 87000000l);
		Date departureTimePassed2 = new Date();
		departureTimePassed2.setTime(System.currentTimeMillis() + 87100000l);

		RetrievePnrSegment segment1 = new RetrievePnrSegment();
		RetrievePnrDepartureArrivalTime departureTime1 = new RetrievePnrDepartureArrivalTime();
		departureTime1.setTimeZoneOffset("+0800");
		departureTime1.setRtfsScheduledTime(simpleDateFormat.format(departureTimePassed1));
		departureTime1.setRtfsEstimatedTime(simpleDateFormat.format(departureTimePassed1));
		departureTime1.setPnrTime(simpleDateFormat.format(departureTimePassed1));
		segment1.setDepartureTime(departureTime1);
		segment1.setSegmentID("1");
		segment1.setAirCraftType("TRN");
		segment1.setPnrOperateCompany("CX");
		segment1.setPnrOperateSegmentNumber("520");
		segment1.setMarketCompany("CX");
		segment1.setMarketSegmentNumber("520");
		segment1.setOriginPort("HK");
		segment1.setDestPort("LON");
		segment1.setSubClass("Y");
		List<String> statusList1 = new ArrayList<>();
		statusList1.add("HK");
		statusList1.add("CC");
		segment1.setStatus(statusList1);

		RetrievePnrSegment segment2 = new RetrievePnrSegment();
		RetrievePnrDepartureArrivalTime departureTime2 = new RetrievePnrDepartureArrivalTime();
		departureTime2.setTimeZoneOffset("+0800");
		departureTime2.setRtfsScheduledTime(simpleDateFormat.format(departureTimePassed2));
		departureTime2.setRtfsEstimatedTime(simpleDateFormat.format(departureTimePassed2));
		departureTime2.setPnrTime(simpleDateFormat.format(departureTimePassed2));
		segment2.setDepartureTime(departureTime2);
		segment2.setSegmentID("2");
		segment2.setAirCraftType("TRN");
		segment2.setPnrOperateCompany("CX");
		segment2.setPnrOperateSegmentNumber("520");
		segment2.setMarketCompany("CX");
		segment2.setMarketSegmentNumber("520");
		segment2.setOriginPort("HK");
		segment2.setDestPort("LON");
		segment2.setSubClass("O");
		List<String> statusList2 = new ArrayList<>();
		statusList2.add("HK");
		statusList2.add("CC");
		segment2.setStatus(statusList2);

		List<RetrievePnrSegment> segments = new ArrayList<>();
		segments.add(segment1);
		segments.add(segment2);
		retrievePnrBooking.setSegments(segments);
		List<RetrievePnrFe> feList =new ArrayList<>();
		RetrievePnrFe retrievePnrFe=new RetrievePnrFe();
		retrievePnrFe.setLongFreeText("PAX WAIVECXCHB5 - VLD CX/KA NONEND.REF/RBK RTE FEEAPPLYADDONCXR RESTR");
		feList.add(retrievePnrFe);
		retrievePnrBooking.setFeList(feList);
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		
		RetrievePnrPassengerSegment passengerSegment1 = new RetrievePnrPassengerSegment();
		passengerSegment1.setPassengerId("1");
		passengerSegment1.setSegmentId("1");
		passengerSegment1.setEtickets(new ArrayList<>());
		RetrievePnrEticket eticket1 = new RetrievePnrEticket();
		eticket1.setTicketNumber("0432364343644");
		passengerSegment1.getEtickets().add(eticket1);

		RetrievePnrPassengerSegment passengerSegment2 = new RetrievePnrPassengerSegment();
		passengerSegment2.setPassengerId("2");
		passengerSegment2.setSegmentId("1");
		passengerSegment2.setEtickets(new ArrayList<>());
		RetrievePnrEticket eticket2 = new RetrievePnrEticket();
		eticket2.setTicketNumber("1612364343644");
		passengerSegment2.getEtickets().add(eticket2);
		
		RetrievePnrPassengerSegment passengerSegment3 = new RetrievePnrPassengerSegment();
		passengerSegment3.setPassengerId("1");
		passengerSegment3.setSegmentId("2");
		passengerSegment3.setEtickets(new ArrayList<>());
		RetrievePnrEticket eticket3 = new RetrievePnrEticket();
		eticket3.setTicketNumber("1602364343643");
		passengerSegment3.getEtickets().add(eticket3);

		RetrievePnrPassengerSegment passengerSegment4 = new RetrievePnrPassengerSegment();
		passengerSegment4.setPassengerId("2");
		passengerSegment4.setSegmentId("2");
		passengerSegment4.setEtickets(new ArrayList<>());
		RetrievePnrEticket eticket4 = new RetrievePnrEticket();
		eticket4.setTicketNumber("1602364343642");
		passengerSegment4.getEtickets().add(eticket4);

		passengerSegments.add(passengerSegment1);
		passengerSegments.add(passengerSegment2);
		passengerSegments.add(passengerSegment3);
		passengerSegments.add(passengerSegment4);
		retrievePnrBooking.setPassengerSegments(passengerSegments);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HKG")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("LON")).thenReturn("+1200");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("JFK")).thenReturn("-0500");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("YVR")).thenReturn("-0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("SHA")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("TPE")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HK")).thenReturn("+0800");
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
		Booking booking = bookingBuildServiceImpl.buildBooking(retrievePnrBooking, loginInfo, required);
		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isEligible()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getSeatSelection().getSpecialSeatEligibility().getExlSeat()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getSeatSelection().isXlFOC()));
		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(0).getMmbSeatSelection().isAsrFOC()));

		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(1).getMmbSeatSelection().isEligible()));

//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(2).getSeatSelection().isEligible()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(2).getSeatSelection().getSpecialSeatEligibility().getExlSeat()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(2).getSeatSelection().isXlFOC()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(2).getSeatSelection().isAsrFOC()));

//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(3).getSeatSelection().isEligible()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(3).getSeatSelection().getSpecialSeatEligibility().getExlSeat()));
//		assertFalse(BooleanUtils.isTrue(booking.getPassengerSegments().get(3).getSeatSelection().isXlFOC()));
//		assertTrue(BooleanUtils.isTrue(booking.getPassengerSegments().get(3).getSeatSelection().isAsrFOC()));	
	}
	@Test
	public void test_isBookingOnHold() throws BusinessBaseException, ParseException {
		List<RetrievePnrRemark> remarkList = new ArrayList<>();
		RetrievePnrRemark remark=new RetrievePnrRemark();
		remark.setFreeText("aaddSTATUS:ON HOLD");
		pnrBooking.setRemarkList(remarkList);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HKG")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("LON")).thenReturn("+1200");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("JFK")).thenReturn("-0500");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("YVR")).thenReturn("-0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("SHA")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("TPE")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HK")).thenReturn("+0800");
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
		.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
		Booking booking = bookingBuildServiceImpl.buildBooking(pnrBooking, loginInfo, required);
		assertFalse(booking.isBookingOnhold());		
	}
	
	@Test
	public void test_SpecialServiceUMNR() throws BusinessBaseException, ParseException {
		ProfilePersonInfo memberprofile = new ProfilePersonInfo();
		Contact contact = new Contact();
		ProfileEmail email = new ProfileEmail();
		RetrievePnrBooking pnrBookingForUMNR = pnrBooking;
		RetrievePnrDataElements ssrList = new RetrievePnrDataElements();
		ssrList.setPassengerId("1");
		ssrList.setSegmentId("1");
		ssrList.setType("UMNR");
		ssrList.setStatus("HK");
		ssrList.setCompanyId("CX");
		ssrList.setFreeText("TTL111KG111PC");
		pnrBookingForUMNR.getSsrList().add(ssrList);
		email.setEmail("123@qq.com");
		contact.setEmail(email);
		ProfilePhoneInfo phoneInfo = new ProfilePhoneInfo();
		phoneInfo.setPhoneNo("1-123456");
		contact.setPhoneInfo(phoneInfo);
		memberprofile.setContact(contact);
		List<String> pnrStatusList = new ArrayList<>();
		pnrStatusList.add("HK");
		pnrStatusList.add("CC");
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setStatusCode("HK");
		List<SpecialServiceModel> specialServiceModels = new ArrayList<>();
		SpecialServiceModel specialServiceModel = new SpecialServiceModel();
		specialServiceModel.setReminderCode("OBAG");
		specialServiceModels.add(specialServiceModel);
		specialServiceModel.setReminderCode("UMNR");
		specialServiceModels.add(specialServiceModel);
		List<StatusManagementModel> statusManagementModels = new ArrayList<>();
		StatusManagementModel statusManagementModel = new StatusManagementModel();
		statusManagementModel.setStatusCode("HK");
		statusManagementModels.add(statusManagementModel);
		
		when(retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken()))
				.thenReturn(memberprofile);	
		when(bookingBuildHelper.getFirstAvailableStatus(pnrStatusList))
				.thenReturn(bookingStatus).thenReturn(bookingStatus).thenReturn(bookingStatus);
		when(umnreFormBuildService.hasEFormRemark("1", pnrBookingForUMNR)).thenReturn(true);
		when(specialServiceDAO.findAllByAppCodeAndAction(anyObject(),anyObject())).thenReturn(specialServiceModels);
		when(statusManagementDAO.findAllByAppCodeAndType(anyObject(),anyObject())).thenReturn(statusManagementModels);
		
		Booking booking = bookingBuildServiceImpl.buildBooking(pnrBookingForUMNR, loginInfo, required);
		assertEquals("UMNR", booking.getPassengerSegments().get(0).getSpecialServices().get(0).getCode());
		assertEquals("withUMForm", booking.getPassengerSegments().get(0).getSpecialServices().get(0).getAdditionalStatus());
	}
}
