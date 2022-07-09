package com.cathaypacific.mmbbizrule.oneaservice.pnr;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.eodsconsumer.util.MarshallerFactory;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.NonAirSegmentDao;
import com.cathaypacific.mmbbizrule.db.dao.PassengerTypeDAO;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbSsrTypeDAO;
import com.cathaypacific.mmbbizrule.db.model.StatusManagementModel;
import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModel;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;


@RunWith(MockitoJUnitRunner.class)
public class PnrResponseParserTest {
	@InjectMocks
	private PnrResponseParser pnrResponseParser;
	@Mock
	private BizRuleConfig bizRuleConfig;
	@Mock
	private StatusManagementDAO statusManagementDAO;
	@Mock
	private NonAirSegmentDao nonAirSegmentDao;
	@Mock
	private TbSsrTypeDAO tbSsrTypeDAO;
	@Mock
	private PassengerTypeDAO passengerTypeDAO;
	@Mock
	ConstantDataDAO constantDataDAO;
	@Mock
    BookEligibilityConfig bookEligibilityConfig;
	
	@Before
	public void setUp() throws SecurityException, NoSuchMethodException {
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(new StatusManagementModel());
		
		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);
		
		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);
		
		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);
		
		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);
		
		List<String> mealTypes = new ArrayList<>(Arrays.asList("VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML".split(",")));

		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);
		when(passengerTypeDAO.getPaxType(PnrResponseParser.APP_CODE, "ADT")).thenReturn("ADT");
		when(passengerTypeDAO.getPaxType(PnrResponseParser.APP_CODE, "INF")).thenReturn("INF");
		when(passengerTypeDAO.getPaxType(PnrResponseParser.APP_CODE, "INS")).thenReturn("INS");

	}
	
	@Test
	public void paserResponseTest() throws Exception {
	    when(bookEligibilityConfig.isEnablePreselectedMeal()).thenReturn(false);
		Resource resource = new ClassPathResource("xml/PNRReply_PnrResponseParser.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		RetrievePnrBooking retrievePnrBooking = pnrResponseParser.paserResponse(pnrReply);
		List<RetrievePnrPassenger> passengers = retrievePnrBooking.getPassengers();
		List<RetrievePnrPassengerSegment> passengerSegments = retrievePnrBooking.getPassengerSegments();
		List<RetrievePnrSegment> segments = retrievePnrBooking.getSegments();
		
		Assert.assertTrue(passengers.size() == 3);
		Assert.assertTrue(segments.size() == 2);
		Assert.assertTrue(passengerSegments.size() == 6);
		
		for(int i = 0; i < segments.size(); i++) {
			RetrievePnrSegment segment = segments.get(i);
			if(i == 0) {
				Assert.assertEquals("TRN", segment.getAirCraftType());
				Assert.assertEquals("HK/ TO CHECK-IN VISIT CHECK-IN.ACCESRAIL.COM OR USE SELF SERVICE MACHINE AT TRAIN STATION WITHIN 72 HOURS BEFORE DEPARTURE HK/ PSGR INFO TO PRINTOUT SEE WWW.BAHN.DE/RAILANDFLY", segment.getTrainReminder());
			} else if(i == 1) {
				Assert.assertEquals("77W", segment.getAirCraftType());
				Assert.assertNull(segment.getTrainReminder());
			}
		}
		
		for(int i = 0; i < passengers.size(); i++) {
			RetrievePnrPassenger passenger = passengers.get(i);
			
			if(i == 0) {
				//familyName & givenName & passengerID & parentId & passengerType
				Assert.assertEquals("WANG", passenger.getFamilyName());
				Assert.assertEquals("EXST", passenger.getGivenName());
				Assert.assertNull(passenger.getParentId());
				Assert.assertEquals("3", passenger.getPassengerID());
				Assert.assertEquals("ADT", passenger.getPassengerType());
				
				//travelDocs
				List<RetrievePnrTravelDoc> travelDocs = passenger.getPriTravelDocs();
				Assert.assertTrue(travelDocs.size() == 1);
				RetrievePnrTravelDoc travelDoc = travelDocs.get(0);
				Assert.assertEquals("10", travelDoc.getBirthDateDay());
				Assert.assertEquals("09", travelDoc.getBirthDateMonth());
				Assert.assertEquals("1988", travelDoc.getBirthDateYear());
				Assert.assertEquals("CX", travelDoc.getCompanyId());
				Assert.assertEquals("HKG", travelDoc.getCountryOfIssuance());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateMonth());
				Assert.assertEquals("2120", travelDoc.getExpiryDateYear());
				Assert.assertEquals("WANG", travelDoc.getFamilyName());
				Assert.assertEquals("F", travelDoc.getGender());
				Assert.assertEquals("JI", travelDoc.getGivenName());
				Assert.assertFalse(travelDoc.isInfant());
				Assert.assertEquals("HKG", travelDoc.getNationality());
				Assert.assertEquals("201701141", travelDoc.getTravelDocumentNumber());
				Assert.assertEquals("P", travelDoc.getTravelDocumentType());
				Assert.assertEquals("DOCS", travelDoc.getSsrType());
			} else if(i == 2) {
				//familyName & givenName & passengerID & parentId & passengerType
				Assert.assertEquals("WANG", passenger.getFamilyName());
				Assert.assertEquals("BOB", passenger.getGivenName());
				Assert.assertEquals("1", passenger.getParentId());
				Assert.assertEquals("1I", passenger.getPassengerID());
				Assert.assertEquals("INF", passenger.getPassengerType());
				
				//KTN
				Assert.assertTrue(passenger.getKtns().size() == 1);
				RetrievePnrTravelDoc ktn = passenger.getKtns().get(0);
				Assert.assertNotNull(ktn);
				Assert.assertEquals("USA", ktn.getCountryOfIssuance());
				Assert.assertEquals("03", ktn.getExpiryDateDay());
				Assert.assertEquals("05", ktn.getExpiryDateMonth());
				Assert.assertEquals("1994", ktn.getExpiryDateYear());
				Assert.assertEquals("NEWYORK USA", ktn.getExtraFreeText());
				Assert.assertEquals("NEWYORK USA", ktn.getNationality());
				Assert.assertEquals("10", ktn.getQualifierId().toString());
				Assert.assertEquals("DOCO", ktn.getSsrType());
				Assert.assertEquals("135701701", ktn.getTravelDocumentNumber());
				Assert.assertEquals("K", ktn.getTravelDocumentType());
				Assert.assertTrue(ktn.isInfant());
			} else if(i == 1) {
				//familyName & givenName & passengerID & parentId & passengerType
				Assert.assertEquals("WANG", passenger.getFamilyName());
				Assert.assertEquals("RONG MRS", passenger.getGivenName());
				Assert.assertNull(passenger.getParentId());
				Assert.assertEquals("1", passenger.getPassengerID());
				Assert.assertEquals("ADT", passenger.getPassengerType());
				
				//FQTV
				List<RetrievePnrFFPInfo> FQTVInfos = passenger.getFQTVInfos();
				Assert.assertTrue(FQTVInfos.size() == 2);
				RetrievePnrFFPInfo FQTVInfo = FQTVInfos.get(0);
				Assert.assertEquals("CX", FQTVInfo.getCompanyId());
				Assert.assertFalse(FQTVInfo.isTopTier());
				Assert.assertEquals("1920354571", FQTVInfo.getFfpMembershipNumber());
				Assert.assertEquals("GR", FQTVInfo.getTierLevel());
				
				//email && contactMobile
				Assert.assertEquals("wangrong@accenture.com", passenger.getEmails().get(0).getEmail());
				Assert.assertEquals("8617602103083", passenger.getContactPhones().get(0).getPhoneNumber());
				
				//redress
				Assert.assertTrue(passenger.getRedresses().size() == 1);
				RetrievePnrTravelDoc redress = passenger.getRedresses().get(0);
				Assert.assertNotNull(redress);
				Assert.assertEquals("1234567", redress.getTravelDocumentNumber());
				Assert.assertEquals("R", redress.getTravelDocumentType());
				Assert.assertFalse(redress.isInfant());
			}
			
		}
		
		for(int i = 0; i < passengerSegments.size(); i++) {
			RetrievePnrPassengerSegment passengerSegment = passengerSegments.get(i);
			//passengerSegment 
			List<RetrievePnrTravelDoc> travelDocs = passengerSegment.getPriTravelDocs();
			if(i == 0 || i == 1 || i == 4) {
				Assert.assertTrue(CollectionUtils.isEmpty(travelDocs));
			}
			if(i == 3) {
				//travelDocs
				Assert.assertTrue(travelDocs.size() == 1);
				RetrievePnrTravelDoc travelDoc = travelDocs.get(0);
				Assert.assertEquals("10", travelDoc.getBirthDateDay());
				Assert.assertEquals("09", travelDoc.getBirthDateMonth());
				Assert.assertEquals("1988", travelDoc.getBirthDateYear());
				Assert.assertEquals("CX", travelDoc.getCompanyId());
				Assert.assertEquals("HKG", travelDoc.getCountryOfIssuance());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateMonth());
				Assert.assertEquals("2120", travelDoc.getExpiryDateYear());
				Assert.assertEquals("WANG", travelDoc.getFamilyName());
				Assert.assertEquals("F", travelDoc.getGender());
				Assert.assertEquals("RONG", travelDoc.getGivenName());
				Assert.assertFalse(travelDoc.isInfant());
				Assert.assertEquals("HKG", travelDoc.getNationality());
				Assert.assertEquals("201701141", travelDoc.getTravelDocumentNumber());
				Assert.assertEquals("P", travelDoc.getTravelDocumentType());
				Assert.assertEquals("DOCS", travelDoc.getSsrType());
				
				//FQTV
				RetrievePnrFFPInfo FQTVInfo = passengerSegment.getFQTVInfos().get(0);
				Assert.assertEquals("CX", FQTVInfo.getCompanyId());
				Assert.assertFalse(FQTVInfo.isTopTier());
				Assert.assertEquals("1920354571", FQTVInfo.getFfpMembershipNumber());
				Assert.assertEquals("GR", FQTVInfo.getTierLevel());
				
				//pick up number
				Assert.assertEquals("928691683",passengerSegment.getPickUpNumber());
			} else if(i == 2) {
				//travelDocs
				Assert.assertTrue(travelDocs.size() == 1);
				RetrievePnrTravelDoc travelDoc = travelDocs.get(0);
				Assert.assertEquals("10", travelDoc.getBirthDateDay());
				Assert.assertEquals("09", travelDoc.getBirthDateMonth());
				Assert.assertEquals("1988", travelDoc.getBirthDateYear());
				Assert.assertEquals("CX", travelDoc.getCompanyId());
				Assert.assertEquals("HKG", travelDoc.getCountryOfIssuance());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateMonth());
				Assert.assertEquals("2120", travelDoc.getExpiryDateYear());
				Assert.assertEquals("WANG", travelDoc.getFamilyName());
				Assert.assertEquals("F", travelDoc.getGender());
				Assert.assertEquals("JI", travelDoc.getGivenName());
				Assert.assertFalse(travelDoc.isInfant());
				Assert.assertEquals("HKG", travelDoc.getNationality());
				Assert.assertEquals("201701141", travelDoc.getTravelDocumentNumber());
				Assert.assertEquals("P", travelDoc.getTravelDocumentType());
				Assert.assertEquals("DOCS", travelDoc.getSsrType());
			} else if(i == 5) {
				//travelDocs
				Assert.assertTrue(travelDocs.size() == 1);
				RetrievePnrTravelDoc travelDoc = travelDocs.get(0);
				Assert.assertEquals("10", travelDoc.getBirthDateDay());
				Assert.assertEquals("09", travelDoc.getBirthDateMonth());
				Assert.assertEquals("1988", travelDoc.getBirthDateYear());
				Assert.assertEquals("CX", travelDoc.getCompanyId());
				Assert.assertEquals("HKG", travelDoc.getCountryOfIssuance());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateDay());
				Assert.assertEquals("02", travelDoc.getExpiryDateMonth());
				Assert.assertEquals("2120", travelDoc.getExpiryDateYear());
				Assert.assertEquals("WANG", travelDoc.getFamilyName());
				Assert.assertEquals("F", travelDoc.getGender());
				Assert.assertEquals("BOB", travelDoc.getGivenName());
				Assert.assertTrue(travelDoc.isInfant());
				Assert.assertEquals("HKG", travelDoc.getNationality());
				Assert.assertEquals("201701141", travelDoc.getTravelDocumentNumber());
				Assert.assertEquals("P", travelDoc.getTravelDocumentType());
				Assert.assertEquals("DOCS", travelDoc.getSsrType());
			}
			
			//check seat
			if("1".equals(passengerSegment.getPassengerId()) && "1".equals(passengerSegment.getSegmentId())) {
				Assert.assertEquals("42C", passengerSegment.getSeat().getSeatDetail().getSeatNo());
			}
			
			//check seat preference
			if("3".equals(passengerSegment.getPassengerId()) && "1".equals(passengerSegment.getSegmentId())) {
				Assert.assertEquals(null, passengerSegment.getSeat().getPreference().getPreferenceCode());
			}
		}
		
		List<RetrievePnrRemark> remarkList = retrievePnrBooking.getRemarkList();
		Assert.assertEquals("RM", remarkList.get(0).getType());
		Assert.assertTrue(remarkList.get(0).getFreeText().indexOf("NOTIFY PASSENGER PRIOR TO TICKET PURCHASE & CHECK-IN") > -1);
		List<RetrievePnrTicket> ticketList = retrievePnrBooking.getTicketList();
		Assert.assertEquals("121217", ticketList.get(0).getDate());
		Assert.assertEquals("HKGCX0300", ticketList.get(0).getOfficeId());
		Assert.assertEquals("OK", ticketList.get(0).getIndicator());
		Assert.assertEquals(null, ticketList.get(0).getTime());
		
		//Spnr 
		List<RetrievePnrDataElements> skList = retrievePnrBooking.getSkList();
		Assert.assertEquals("SK", skList.get(0).getSegmentName());
		Assert.assertEquals("SPNR", skList.get(0).getType());
		Assert.assertEquals("83", skList.get(0).getQulifierId().toString());
		Assert.assertEquals("M55MZWP/FO/FLT/MOB", skList.get(0).getFreeText());
		Assert.assertEquals("M55MZWP", retrievePnrBooking.getBookingPackageInfo().getSpnr());
		
		//Corporate booking
		Assert.assertTrue(BooleanUtils.isTrue(retrievePnrBooking.isCorporateBooking()));
	}
	@Test
	public void paserResponseTest3() throws IOException{
		Resource resource = new ClassPathResource("xml/PNRReply_PnrResponseParser.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		
		List<OneAError> allErrors=PnrResponseParser.getAllErrors(pnrReply);
		for(int i=0;i<allErrors.size();i++){
			if(i==0){
				Assert.assertEquals("23951", allErrors.get(i).getErrorCode());
			}
			if(i==1){
				Assert.assertEquals("A", allErrors.get(i).getErrorCode());
			}
			if(i==2){
				Assert.assertEquals("G",allErrors.get(i).getErrorCode());
			}
			if(i==3){
				Assert.assertEquals("J", allErrors.get(i).getErrorCode());
			}
			if(i==4){
				Assert.assertEquals("K", allErrors.get(i).getErrorCode());
			}
			if(i==5){
				Assert.assertEquals("L", allErrors.get(i).getErrorCode());
			}
			if(i==6){
				Assert.assertEquals("K", allErrors.get(i).getErrorCode());
			}
			if(i==7){
				Assert.assertEquals("L", allErrors.get(i).getErrorCode());
			}
		}
	}
	@Test
	public void paserResponseTest1() throws Exception {
		String segmentId="1";
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment= new RetrievePnrPassengerSegment();
		passengerSegment.setSegmentId("1");
		passengerSegments.add(passengerSegment);
		List<RetrievePnrPassengerSegment> RetrievePnrPassengerSegment = PnrResponseParser.getPassengerSegmentBySegmentId(passengerSegments, segmentId);
		Assert.assertEquals("1", RetrievePnrPassengerSegment.get(0).getSegmentId());
	}
	@Test
	public void paserResponseTestEmptyEquipment() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRReply_PnrResponseParser_non_air_without_equipment.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");
		
		
		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);
		
		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);
		
		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);
		
		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);
		
		List<String> mealTypes = new ArrayList<>(Arrays.asList("VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML".split(",")));
		
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);

		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);

		RetrievePnrSegment pnrSeg = pnrBooking.getSegments().stream().filter(st->st.getSegmentID().equals("3")).findFirst().orElse(null);
		Assert.assertEquals("LCH", pnrSeg.getAirCraftType());
		
	}
	
	@Test
	public void paserResponseTest_pax_type_test() throws Exception {
		/*
		 * test data MB8FHV
				PAX8:ADT(with INF)
				PAX5:ADT + SSR INFT
				PAX2:INS + SSR INFT
				PAX3:ADT(with INF) + SSR INFT
				PAX6:CHD + SSR CHLD
				PAX7:ADT + SSR CHLD
				PAX1:INS + SSR CHLD
		 */
		Resource resource = new ClassPathResource("xml/PNRRepley_MB8FHV_passenger_type_test.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");
			
		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);
		
		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);
		
		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);
		
		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);
		
		List<String> mealTypes = new ArrayList<>(Arrays.asList("VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML".split(",")));
		
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);

		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);
		
		List<RetrievePnrPassenger> pnrPaxList = pnrBooking.getPassengers();

		// pax 8 should identify to ADT
		Assert.assertEquals("ADT", pnrPaxList.stream().filter(pax -> "8".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
		// pax 8's baby should identify to INF
		Assert.assertEquals("INF", pnrPaxList.stream().filter(pax -> "8".equals(pax.getParentId())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
		
		// pax 5 should identify to INF
		Assert.assertEquals("ADT", pnrPaxList.stream().filter(pax -> "5".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
		
		// pax 2  should identify to INS
		Assert.assertEquals("INS", pnrPaxList.stream().filter(pax -> "2".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());

		// pax 3 should identify to ADT
		Assert.assertEquals("ADT", pnrPaxList.stream().filter(pax -> "3".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
		// pax 3's baby should identify to INF
		Assert.assertEquals("INF", pnrPaxList.stream().filter(pax -> "3".equals(pax.getParentId())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());

		// pax 6 should identify to CHD
		Assert.assertEquals("CHD", pnrPaxList.stream().filter(pax -> "6".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
		// pax 7 should identify to CHD
		Assert.assertEquals("CHD", pnrPaxList.stream().filter(pax -> "7".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());

		// pax 1 should identify to INS
		Assert.assertEquals("INS", pnrPaxList.stream().filter(pax -> "1".equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger()).getPassengerType());
	
	}
	
	
	@Test
	public void paser_ResponseTest_invalid_segment_N() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRReply_Invalid_N_Sengment.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");
		
		
		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);
		
		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);
		
		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);
		
		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);
		
		List<String> mealTypes = new ArrayList<>(Arrays.asList("VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML".split(",")));
		
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);
		
		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);
		
		Assert.assertEquals(1, pnrBooking.getSegments().size());
	}
	
	@Test
	public void paser_ResponseTest_staff_without_paxid() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRRepley_MIYPGV_Staff_Without_Paxid.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString())).thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");
		
		
		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);
		
		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);
		
		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);
		
		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);
		
		List<String> mealTypes = new ArrayList<>(Arrays.asList("VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML".split(",")));
		
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);

		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);
		
		Assert.assertTrue(pnrBooking.getPassengers().get(0).getStaffDetail().getType().equals(StaffBookingType.DONATION_WINNER));
		Assert.assertTrue(pnrBooking.getPassengers().get(1).getStaffDetail().getType().equals(StaffBookingType.DONATION_WINNER));
	}
	
	@Test
	public void paser_ResponseTest_protection_rm_parse() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRReply_Protection_RM_parse_test");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));

		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");

		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);

		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);

		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);

		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);

		List<String> mealTypes = new ArrayList<>(Arrays.asList(
				"VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML"
						.split(",")));

		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);

		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);

		RetrievePnrSegment cancelledSegment = pnrBooking.getSegments().stream()
				.filter(seg -> seg.getStatus().contains("UN")).findFirst().orElse(null);
		Assert.assertNotNull(cancelledSegment);
		RetrievePnrRebookInfo rebookInfo = cancelledSegment.getRebookInfo();

		Assert.assertTrue(BooleanUtils.isTrue(rebookInfo.isRebooked()));
		Assert.assertEquals(1, rebookInfo.getNewBookedSegmentIds().size());
		Assert.assertEquals("3", rebookInfo.getNewBookedSegmentIds().get(0));
	}
	
	@Test
	public void paser_ResponseTest_SSRINFT() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRRepley_SSR_Infat.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));

		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		when(statusManagementDAO.findMostMatchedStatus(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(new StatusManagementModel());
		when(nonAirSegmentDao.findNonAirSegmentType(MMBConstants.APP_CODE, "ZIY", "HKG", "3A")).thenReturn("LCH");

		List<TbSsrTypeModel> avaliableTravelDocModels = new ArrayList<>();
		TbSsrTypeModel tbSsrTypeModel1 = new TbSsrTypeModel();
		TbSsrTypeModel tbSsrTypeModel2 = new TbSsrTypeModel();
		tbSsrTypeModel1.setValue("DOCS");
		tbSsrTypeModel2.setValue("DOCO");
		avaliableTravelDocModels.add(tbSsrTypeModel1);
		avaliableTravelDocModels.add(tbSsrTypeModel2);

		List<TbSsrTypeModel> emrContactModels = new ArrayList<>();
		TbSsrTypeModel emrContactModel = new TbSsrTypeModel();
		emrContactModel.setValue("PCTC");
		emrContactModels.add(emrContactModel);

		List<TbSsrTypeModel> destinationAddrModels = new ArrayList<>();
		TbSsrTypeModel destinationAddrModel = new TbSsrTypeModel();
		destinationAddrModel.setValue("DOCA");
		destinationAddrModels.add(destinationAddrModel);

		List<TbSsrTypeModel> residenceAddrModels = new ArrayList<>();
		TbSsrTypeModel residenceAddrModel = new TbSsrTypeModel();
		residenceAddrModel.setValue("DOCA");
		residenceAddrModels.add(residenceAddrModel);

		List<String> mealTypes = new ArrayList<>(Arrays.asList(
				"VGML,VLML,VOML,VJML,RVML,AVML,FPML,KSML,MOML,HNML,NLML,DBML,BLML,LPML,GFML,HFML,LFML,LCML,LSML,BBML,CHML,SFML,PRML,SPML"
						.split(",")));

		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD,
				TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(avaliableTravelDocModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC))
				.thenReturn(emrContactModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA))
				.thenReturn(destinationAddrModels);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA))
				.thenReturn(residenceAddrModels);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);

		RetrievePnrBooking pnrBooking = pnrResponseParser.paserResponse(pnrReply);

 
		Assert.assertTrue(pnrBooking.getPassengers().stream().anyMatch(pax->"INF".equals(pax.getPassengerType())));
	}
}
