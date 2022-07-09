/*
 * package com.cathaypacific.mmbbizrule.business.impl; import static
 * org.mockito.Matchers.anyObject; import static org.mockito.Mockito.when;
 * 
 * import java.text.SimpleDateFormat; import java.util.ArrayList; import
 * java.util.Calendar; import java.util.Date; import java.util.List; import
 * java.util.concurrent.Future;
 * 
 * import org.junit.Assert; import org.junit.Before; import org.junit.Test;
 * import org.junit.runner.RunWith; import org.mockito.InjectMocks; import
 * org.mockito.Mock; import org.mockito.runners.MockitoJUnitRunner; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.AsyncResult;
 * 
 * import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum; import
 * com.cathaypacific.mbcommon.exception.BusinessBaseException; import
 * com.cathaypacific.mbcommon.model.login.LoginInfo; import
 * com.cathaypacific.mbcommon.token.MbTokenCacheRepository; import
 * com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.
 * RetrieveProfileService; import
 * com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService; import
 * com.cathaypacific.mmbbizrule.dto.response.bookingsummary.
 * BookingSummaryResponseDTO; import
 * com.cathaypacific.mmbbizrule.handler.BookingSummaryHelper; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.Booking; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
 * import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
 * import com.cathaypacific.mmbbizrule.model.booking.detail.Email; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.Passenger; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.Segment; import
 * com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus; import
 * com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummary; import
 * com.cathaypacific.mmbbizrule.model.booking.summary.
 * BookingSummaryIntegrationBean; import
 * com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
 * import com.cathaypacific.mmbbizrule.model.booking.summary.
 * FlightBookingSummaryConvertBean; import
 * com.cathaypacific.mmbbizrule.model.booking.summary.
 * OjBookingSummaryConvertBean; import
 * com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary; import
 * com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo; import
 * com.cathaypacific.mmbbizrule.oneaservice.pnr.model.
 * RetrievePnrBookingCerateInfo; import
 * com.cathaypacific.mmbbizrule.service.EodsBookingSummaryService; import
 * com.cathaypacific.mmbbizrule.service.OneABookingSummaryService; import
 * com.cathaypacific.mmbbizrule.service.TempLinkedBookingSummaryService;
 * 
 * @RunWith(MockitoJUnitRunner.class) public class
 * BookingSummaryBusinessImplTest {
 * 
 * @InjectMocks BookingSummaryBusinessImpl bookingSummaryBusinessImpl;
 * 
 * @Mock private BookingSummaryHelper bookingSummaryHelper;
 * 
 * private String lastRloc; LoginInfo loginInfo; Booking booking; String
 * mmbToken; String requirePageSize;
 * 
 * @Before public void setUp() throws BusinessBaseException { lastRloc =
 * "LE5XOE"; mmbToken = "123456"; requirePageSize="4"; loginInfo=new
 * LoginInfo(); loginInfo.setMmbToken("qwertyu"); loginInfo.setUserType("RU");
 * booking =new Booking(); booking.setOneARloc("SGT7P4"); Date date = new
 * Date(); Calendar calendar = Calendar.getInstance(); calendar.setTime(date);
 * calendar.add(Calendar.MONTH, 1); calendar.getTime(); SimpleDateFormat sf =
 * new SimpleDateFormat("yyyy-MM-dd HH:mm"); sf.format(calendar.getTime());
 * RetrievePnrBookingCerateInfo bookingCreateInfo = new
 * RetrievePnrBookingCerateInfo(); bookingCreateInfo.setRpOfficeId("AKLHF2105");
 * bookingCreateInfo.setCreateTime(sf.format(calendar.getTime()).toString());
 * bookingCreateInfo.setCreateDate("191118");
 * booking.setBookingCreateInfo(bookingCreateInfo);
 * booking.setRedemptionBooking(false); booking.setHasFqtu(false);
 * 
 * //passengers List<Passenger> passengers = new ArrayList<>(); Passenger
 * passenger = new Passenger(); passenger.setFamilyName("CHEUNG");
 * passenger.setGivenName("SAU YI WINNIE"); passenger.setParentId("1");
 * passenger.setTitle("ms"); passenger.setPassengerId("1");
 * passenger.setLoginMember(true); //contactInfo ContactInfo contactInfo = new
 * ContactInfo(); List<Email> emails =new ArrayList<>(); Email email = new
 * Email(); email.setEmailAddress("fangfang_zhang@cathaypacific.com");
 * email.setType("CT"); emails.add(email); contactInfo.setEmail(email);
 * contactInfo.setNotificationEmails(emails);
 * passenger.setContactInfo(contactInfo); passengers.add(passenger);
 * booking.setPassengers(passengers);
 * 
 * //segments List<Segment> segments = new ArrayList<>(); Segment segment=new
 * Segment(); segment.setSegmentID("1"); DepartureArrivalTime departureTime =new
 * DepartureArrivalTime();
 * 
 * departureTime.setRtfsActualTime(sf.format(calendar.getTime()).toString());
 * departureTime.setTimeZoneOffset("+0800");
 * segment.setDepartureTime(departureTime);
 * segment.setArrivalTime(departureTime); segment.setCabinClass("J");
 * segment.setOriginPort("HKG"); segment.setDestPort("TPE");
 * segment.setOperateCompany("CX"); segment.setMarketCabinClass("J");
 * segment.setSubClass("U"); segment.setOperateSegmentNumber("564");
 * segments.add(segment); booking.setSegments(segments);
 * 
 * //passengerSegments List<PassengerSegment> passengerSegments = new
 * ArrayList<>(); PassengerSegment passengerSegment=new PassengerSegment();
 * passengerSegment.setSegmentId("1"); passengerSegment.setPassengerId("1");
 * FQTVInfo fQTVInfo = new FQTVInfo(); fQTVInfo.setCompanyId("CX");
 * fQTVInfo.setTierLevel("MPO"); fQTVInfo.setMembershipNumber("1910026122");
 * 
 * passengerSegment.setFqtvInfo(fQTVInfo); SeatSelection seatSelection = new
 * SeatSelection(); seatSelection.setEligible(true);
 * seatSelection.setXlFOC(false);
 * passengerSegment.setSeatSelection(seatSelection);
 * passengerSegments.add(passengerSegment);
 * booking.setPassengerSegments(passengerSegments);
 * 
 * } // @Test // public void test() throws BusinessBaseException { // String
 * memberId="1"; // String mmbToken="123456"; // String lastRloc="RI4567"; //
 * String requirePageSize="4"; // Date departureTimePassed = new Date(); //
 * departureTimePassed.setTime(System.currentTimeMillis() + 87000000l); //
 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 * // List<BookingSummary> bookings =new ArrayList<>(); // BookingSummary
 * booking=new BookingSummary(); // booking.setDisplayOnly(false); //
 * List<ErrorInfo> errorInfos = new ArrayList<ErrorInfo>(); // List<Summary>
 * segments=new ArrayList<>(); // SegmentSummary segment=new SegmentSummary();
 * // segment.setOriginPort("Q"); // segment.setDestPort("W"); //
 * segment.setMarketCompany("CX"); // segment.setMarketSegmentNumber("123"); //
 * SegmentStatus segmentStatus=new SegmentStatus(); //
 * segmentStatus.setStatus(FlightStatusEnum.CANCELLED); //
 * segment.setSegmentStatus(segmentStatus); // DepartureArrivalTime
 * departureTime = new DepartureArrivalTime(); //
 * departureTime.setTimeZoneOffset("+0800"); //
 * departureTime.setRtfsEstimatedTime(simpleDateFormat.format(
 * departureTimePassed)); //
 * departureTime.setPnrTime(simpleDateFormat.format(departureTimePassed)); //
 * departureTime.setRtfsActualTime(simpleDateFormat.format(departureTimePassed))
 * ; // departureTime.setRtfsScheduledTime(simpleDateFormat.format(
 * departureTimePassed)); // departureTime.setTimeZoneOffset("+800"); //
 * segment.setDepartureTime(departureTime); // segments.add(segment); //
 * booking.setSummarys(segments); // bookings.add(booking); // //
 * doNothing().when(mbTokenCacheRepository).add(Matchers.any(),Matchers.any(),
 * Matchers.any(),Matchers.any()); //
 * when(bookingListBuildService.getBookingList(memberId, mmbToken,
 * errorInfos)).thenReturn(bookings); // // LoginInfo loginInfo = new
 * LoginInfo(); // loginInfo.setMemberId(memberId); // BookingSummaryResponseDTO
 * bookingSummaryResponseDTO=bookingSummaryBusinessImpl.getMemberBookings(
 * loginInfo, mmbToken, lastRloc, requirePageSize, new BookingBuildRequired());
 * // // Assert.assertEquals(1, bookingSummaryResponseDTO.getBookingCount()); //
 * Assert.assertTrue(CollectionUtils.isEmpty(bookingSummaryResponseDTO.
 * getBookings())); // }
 * 
 * @Test public void convertToFlightBookingSummaryDTO_test() throws
 * BusinessBaseException {
 * 
 * BookingSummaryIntegrationBean response = new BookingSummaryIntegrationBean();
 * 
 * List<SegmentSummary> details = new ArrayList<>(); SegmentSummary
 * segmentSummary = new SegmentSummary(); segmentSummary.setSegmentId("1");
 * DepartureArrivalTime departureTime =new DepartureArrivalTime(); Date date =
 * new Date(); Calendar calendar = Calendar.getInstance();
 * calendar.setTime(date); calendar.add(Calendar.MONTH, 1); calendar.getTime();
 * SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 * sf.format(calendar.getTime());
 * departureTime.setRtfsActualTime(sf.format(calendar.getTime()).toString());
 * departureTime.setTimeZoneOffset("+0800"); //
 * segmentSummary.setDepartureTime(departureTime); //
 * segmentSummary.setArrivalTime(departureTime);
 * segmentSummary.setCabinClass("J"); segmentSummary.setOriginPort("HKG");
 * segmentSummary.setDestPort("TPE"); segmentSummary.setOperateCompany("CX");
 * segmentSummary.setSubClass("U");
 * segmentSummary.setOperateSegmentNumber("564"); SegmentStatus segmentStatus =
 * new SegmentStatus(); segmentStatus.setStatus(FlightStatusEnum.STANDBY);
 * segmentSummary.setSegmentStatus(segmentStatus); details.add(segmentSummary);
 * 
 * 
 * 
 * 
 * FlightBookingSummary flightBookingSummary = new FlightBookingSummary();
 * flightBookingSummary.setRloc("SGT7P4");
 * flightBookingSummary.setOnHoldBooking(true);
 * flightBookingSummary.setEncryptedRloc("1223456");
 * flightBookingSummary.setFlightOnly(true);
 * flightBookingSummary.setDetails(details);
 * 
 * 
 * 
 * 
 * 
 * List<BookingSummary> bookingSummarys= new ArrayList<>();
 * 
 * BookingSummary bookingSummary = new BookingSummary();
 * bookingSummary.setBookingStatus("F"); bookingSummary.setRloc("SGT7P4");
 * bookingSummary.setBookingType("Flight");
 * bookingSummary.setFlightSummary(flightBookingSummary);
 * bookingSummarys.add(bookingSummary);
 * 
 * 
 * response.setBookings(bookingSummarys);
 * 
 * when(bookingSummaryHelper.getMemberBookings(anyObject(),
 * anyObject())).thenReturn(response);
 * 
 * BookingSummaryResponseDTO
 * bookingSummaryResponseDTO=bookingSummaryBusinessImpl.getMemberBookings(
 * loginInfo, lastRloc, requirePageSize, new BookingBuildRequired());
 * 
 * Assert.assertEquals("1223456",bookingSummaryResponseDTO.getBookings().get(0).
 * getFlightSummary().getEncryptedRloc()); Assert.assertEquals(true,
 * bookingSummaryResponseDTO.getBookings().get(0).getFlightSummary().
 * isOnHoldBooking()); } }
 */