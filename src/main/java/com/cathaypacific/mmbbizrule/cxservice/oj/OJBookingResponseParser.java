package com.cathaypacific.mmbbizrule.cxservice.oj;

import static java.util.stream.Collectors.toList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OjConstants;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Address;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Amenities;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Amenity;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.ContactDetails;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Description;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Descriptions;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Event;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.EventDescription;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.EventGuest;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Flights;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Guest;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.GuestDetails;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.GuestName;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Hotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.HotelAirport;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Name;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.NameInput;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.OJBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Option;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Package;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.PackageDescriptions;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.PackageHotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Product;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.ProductFlights;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Room;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.RoomInfo;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.RoomOption;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.SmokingPreference;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Tickets;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJAdress;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJContactDetails;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEvent;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventDescription;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventGuest;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJFlightBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelAmenity;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelDescription;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelRoom;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelRoomOption;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJName;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJRoomGuestDetail;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;

@Service
public class OJBookingResponseParser {
	
	private static LogAgent logger = LogAgent.getLogAgent(OJBookingResponseParser.class);
	
	@Autowired
	private AirportTimeZoneService airportTimeZoneService;
	
	@Autowired
	private AEMService aemService;
	
	public OJBooking paserResponse(OJBookingResponseDTO response) {
		if(response == null || CollectionUtils.isEmpty(response.getProduct())) {
			return null;
		}
		List<Product> products = response.getProduct();
		
		Date now = DateUtil.getGMTTime();

		OJBooking ojBooking = null;
		
		//flightBooking
		OJFlightBooking ojFlightBooking = null;
		
		//hotelBooking
		OJHotelBooking ojHotelBooking = null;
		List<OJHotel> ojHotels = new ArrayList<>();
		
		//eventBooking
		OJEventBooking ojEventBooking = null;
		List<OJEvent> ojEvents = new ArrayList<>();
		
		String packageBookingDate = null;
		
		for(Product product : products) {
			if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equalsIgnoreCase(product.getType())) {
				Hotel hotel = product.getHotel();
				if(hotel == null) {
					continue;
				}
				buildHotelDetails(product, ojHotels, now);
			}  else if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equalsIgnoreCase(product.getType())) {
				ojFlightBooking = buildProductFlight(product.getFlight());
			}  else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equalsIgnoreCase(product.getType())) {
			
				Package pk = product.getPackage();
				if(pk == null) {
					continue;
				}
				
				packageBookingDate = product.getBookingDate();
				
				//Flight
				Flights flights = pk.getFlights();
				ojFlightBooking = buildFlight(flights);
				
				//Hotel
				PackageHotel hotel = pk.getHotel();
				if(hotel != null) {
					buildPackageHotelDetails(hotel, ojHotels, now, product.getBookingStatus());
				}
			} else if(MMBBizruleConstants.BOOKING_TYPE_EVENT.equalsIgnoreCase(product.getType())) {
				buildEventDetails(ojEvents, product);
			}
		}
		
		if(!CollectionUtils.isEmpty(ojHotels)) {
			ojHotelBooking = new OJHotelBooking();
			ojHotelBooking.setDetails(groupOjHotelsRoom(ojHotels));
		}
		if(!CollectionUtils.isEmpty(ojEvents)) {
			ojEventBooking = new OJEventBooking();
			ojEventBooking.setDetails(ojEvents);
		}
		if(ojFlightBooking != null && ojHotelBooking == null && ojEventBooking == null) {
			ojBooking = new OJBooking(MMBBizruleConstants.BOOKING_TYPE_FLIGHT);
			ojBooking.setBookingStatus(response.getBookingStatus());
			ojBooking.setFlightBooking(ojFlightBooking);
			ojBooking.setBookingReference(ojFlightBooking.getBookingReference());
			ojBooking.setContactDetails(buildContactDetails(response));
		}else if(ojHotelBooking != null && ojFlightBooking == null && ojEventBooking == null) {
			ojBooking = new OJBooking(MMBBizruleConstants.BOOKING_TYPE_HOTEL);
			ojBooking.setBookingStatus(response.getBookingStatus());
			ojBooking.setBookingReference(response.getSuperPNRID());
			ojBooking.setContactDetails(buildContactDetails(response));
			
			ojBooking.setHotelBooking(ojHotelBooking);
			String startDate = response.getStartDate();
			String endDate = response.getEndDate();
			if(startDate != null && endDate != null) {
				try {
					Date sDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, startDate);
					Date eDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, endDate);
					//TODO compare using timeZone or not
					ojHotelBooking.setIsStayed(now.compareTo(sDate) > 0 && now.compareTo(eDate) < 0);
					ojHotelBooking.setIsCompleted(now.compareTo(eDate) > 0);
				} catch (ParseException e) {
					logger.error("Hotel Booking start/end Date convet error", e);
				}							
			}
		} else if(ojFlightBooking == null && ojHotelBooking == null && ojEventBooking == null) {
			//do nothing
		} else {
			ojBooking = new OJBooking(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
			ojBooking.setBookingStatus(response.getBookingStatus());
			ojBooking.setBookingReference(response.getSuperPNRID());
			ojBooking.setBookingDate(packageBookingDate);
			ojBooking.setContactDetails(buildContactDetails(response));
			
			ojBooking.setFlightBooking(ojFlightBooking);
			ojBooking.setHotelBooking(ojHotelBooking);
			ojBooking.setEventBooking(ojEventBooking);
		}
		buildNameInput(ojBooking, response);
		return ojBooking;
	}
	
	/**
	 * build nameInput
	 * @param ojBooking 
	 * @param response
	 * @return NameInput
	 */
	private void buildNameInput(OJBooking ojBooking, OJBookingResponseDTO response) {
		if(ojBooking == null 
				|| response == null
				|| response.getNameInput() == null) {
			return;
		}
		
		NameInput nameInput = response.getNameInput();
		ojBooking.setNameInput(nameInput);
		
		if(StringUtils.isEmpty(nameInput.getTitle())) {
			buildTitleFromGuestDetails(nameInput, ojBooking);
		}
	}

	/**
	 * build input name title from hotel/event guestDetails
	 * @param nameInput
	 * @param ojBooking 
	 */
	private void buildTitleFromGuestDetails(NameInput nameInput, OJBooking ojBooking) {
		String firstNameInput = nameInput.getFirstNameInput();
		String lastNameInput = nameInput.getLastNameInput();
		if(firstNameInput == null || lastNameInput == null) {
			return;
		}
		
		String title = getTitleFromHotel(firstNameInput, lastNameInput, ojBooking.getHotelBooking());
		if(StringUtils.isEmpty(title)) {
			title = getTitleFromEvent(firstNameInput, lastNameInput, ojBooking.getEventBooking());
		}
		nameInput.setTitle(title);
	}

	/**
	 * get title from event guest names match
	 * @param firstNameInput
	 * @param lastNameInput
	 * @param eventBooking
	 * @return String
	 */
	private String getTitleFromEvent(String firstNameInput, String lastNameInput, OJEventBooking eventBooking) {
		if(eventBooking == null || CollectionUtils.isEmpty(eventBooking.getDetails())) {
			return null;
		}
		
		String title = null;
		boolean foundTitle = false;
		
		List<OJEvent> events = eventBooking.getDetails();
		for(OJEvent event : events) {
			if(foundTitle) {
				break;
			}
			List<OJEventGuest> guests = event.getGuests();
			if(CollectionUtils.isEmpty(guests)) {
				continue;
			}
			for(OJEventGuest guest : guests) {
				firstNameInput = StringUtils.deleteWhitespace(firstNameInput);
				lastNameInput = StringUtils.deleteWhitespace(lastNameInput);
				if(firstNameInput.equalsIgnoreCase(StringUtils.deleteWhitespace(guest.getGivenName())) 
						&& lastNameInput.equalsIgnoreCase(StringUtils.deleteWhitespace(guest.getSurName()))) {
					title = guest.getPrefix();
					foundTitle = true;
					break;
				}
			}
		}

		return title;
	}

	/**
	 * get title from hotel guest names match
	 * @param firstNameInput
	 * @param lastNameInput
	 * @param hotelBooking
	 * @return String
	 */
	private String getTitleFromHotel(String firstNameInput, String lastNameInput, OJHotelBooking hotelBooking) {
		if(hotelBooking == null || CollectionUtils.isEmpty(hotelBooking.getDetails())) {
			return null;
		}
		
		String title = null;
		boolean foundTitle = false;
		
		List<OJHotel> hotels = hotelBooking.getDetails();
		for(OJHotel hotel : hotels) {
			if(foundTitle) {
				break;
			}
			List<OJHotelRoom> rooms = hotel.getRooms();
			if(CollectionUtils.isEmpty(rooms)) {
				continue;
			}
			for(OJHotelRoom room :rooms) {
				if(foundTitle) {
					break;
				}
				List<OJHotelRoomOption> roomOptions = room.getRoomOptions();
				if(CollectionUtils.isEmpty(roomOptions)) {
					continue;
				}
				for(OJHotelRoomOption roomOption : roomOptions) {
					if(foundTitle) {
						break;
					}
					List<OJRoomGuestDetail> guests = roomOption.getGuestDetails();
					if(CollectionUtils.isEmpty(guests)) {
						continue;
					}
					for(OJRoomGuestDetail guest : guests) {
						firstNameInput = StringUtils.deleteWhitespace(firstNameInput);
						lastNameInput = StringUtils.deleteWhitespace(lastNameInput);
						if(firstNameInput.equalsIgnoreCase(StringUtils.deleteWhitespace(guest.getGivenName())) 
								&& lastNameInput.equalsIgnoreCase(StringUtils.deleteWhitespace(guest.getSurname()))) {
							title = guest.getPrefix();
							foundTitle = true;
							break;
						}
					}
				}	
			}
		}

		return title;
	}

	/**
	 * 
	 * @param ProductFlights
	 * @return
	 */
	private OJFlightBooking buildProductFlight(ProductFlights flight) {
		OJFlightBooking ojFlightBooking = null;

		if (CollectionUtils.isEmpty(flight.getFlightDetails()) && StringUtils.isEmpty(flight.getBookingReference())) {
			return ojFlightBooking;
		}

		ojFlightBooking = new OJFlightBooking();
		ojFlightBooking.setBookingReference(flight.getBookingReference());

		return ojFlightBooking;
	}

	/**
	 * @param flights
	 * @return
	 */
	private OJFlightBooking buildFlight(Flights flights) {
		if(CollectionUtils.isEmpty(flights.getFlight())) {
			return null;
		}
		
		OJFlightBooking ojFlightBooking = null;
		
		//rloc(bookingReference)
		String bookingReference = null;
		for(Flight flight : flights.getFlight()) {
			if(CollectionUtils.isEmpty(flight.getOptions())) {
				continue;
			}
			for(Option option : flight.getOptions()) {
				if(option != null && StringUtils.isNotEmpty(option.getBookingReference())) {
					bookingReference = option.getBookingReference();
					break;
				}
			}
			if(StringUtils.isNotEmpty(bookingReference)) {
				break;
			}
		}
		if(bookingReference != null) {
			ojFlightBooking = new OJFlightBooking();
			ojFlightBooking.setBookingReference(bookingReference);
		}
		
		return ojFlightBooking;
	}
	
	/**
	 * @param hotel
	 * @param ojHotels
	 * @param now
	 * @param bookingStatus 
	 * @param timeZoneMap 
	 */
	private void buildPackageHotelDetails(PackageHotel hotel, List<OJHotel> ojHotels, Date now, String bookingStatus) {
		OJHotel ojHotel = new OJHotel();
		ojHotel.setBookingReference(hotel.getBookingReference());
		ojHotel.setId(MMBBizruleConstants.BOOKING_TYPE_HOTEL + ojHotel.getBookingReference());
		ojHotel.setName(hotel.getName());
		ojHotel.setUrlPath(hotel.getuRLPath());
		ojHotel.setBookingStatus(bookingStatus);
		
		ojHotel.setIsStayed(false);
		ojHotel.setIsCompleted(false);
		
		//airport name && timezoneOffset
		if(hotel.getAirports() != null && !CollectionUtils.isEmpty(hotel.getAirports().getAirports())) {
			buildHotelTimezone(hotel.getAirports().getAirports(), ojHotel);
		}
		
		//room
		List<RoomInfo> roomInfo = hotel.getRoomInfo();
		if(!CollectionUtils.isEmpty(roomInfo) && !CollectionUtils.isEmpty(roomInfo.get(0).getRoomList())) {
			buildHotelRooms(roomInfo.get(0).getRoomList(), ojHotel, now);
		}
		
		try {
			if(checkoutFourDaysAgo(ojHotel.getOutDate(), now)) {
				return;
			}
		} catch(Exception e) {
			logger.warn("hotelBooking[bookingReference: %s] checkOutDate compare with now failure", hotel.getBookingReference(), e);
		}
		
		//address
		ojHotel.setAdress(buildHotelAddress(hotel.getAddress()));
		
		//description
		PackageDescriptions description = hotel.getDescriptions();
		if(description != null && !CollectionUtils.isEmpty(description.getDescription())) {
			buildHotelDescription(ojHotel, description.getDescription().toArray(new Description[description.getDescription().size()]));
		}
		
		//amenity
		ojHotel.setAmenities(buildHotelAmenities(hotel.getAmenities()));
		
		ojHotels.add(ojHotel);
	}
	
	/**
	 * @param product
	 * @param ojHotels
	 * @param now
	 * @param timeZoneMap 
	 */
	private void buildHotelDetails(Product product, List<OJHotel> ojHotels, Date now) {
		Hotel hotel = product.getHotel();
		if(hotel == null) {
			return;
		}
		
		OJHotel ojHotel = new OJHotel();
		ojHotel.setBookingReference(hotel.getBookingReference());
		ojHotel.setId(MMBBizruleConstants.BOOKING_TYPE_HOTEL + ojHotel.getBookingReference());
		ojHotel.setName(hotel.getName());
		ojHotel.setUrlPath(hotel.getURLPath());
		ojHotel.setBookingStatus(product.getBookingStatus());
		
		ojHotel.setIsStayed(false);
		ojHotel.setIsCompleted(false);
		
		//airport name && timezoneOffset
		if(hotel.getAirports() != null && !CollectionUtils.isEmpty(hotel.getAirports().getAirports())) {
			buildHotelTimezone(hotel.getAirports().getAirports(), ojHotel);
		}
		
		ojHotel.setBookingDate(product.getBookingDate());
		
		//room
		RoomInfo roomInfo = hotel.getRoomInfo();
		if(roomInfo != null && !CollectionUtils.isEmpty(roomInfo.getRoomList())) {
			buildHotelRooms(roomInfo.getRoomList(), ojHotel, now);
		}
		
		//TODO this fliter will be used for hubPage
		/*try {
			if(checkoutFourDaysAgo(ojHotel.getOutDate(), now)) {
				return;
			}			
		} catch(Exception e) {
			logger.warn("hotelBooking[bookingReference: %s] checkOutDate compare with now failure", hotel.getBookingReference(), e);
		}*/
		
		//address
		ojHotel.setAdress(buildHotelAddress(hotel.getAddress()));
		
		//description
		Descriptions description = hotel.getDescriptions();
		if(description != null && description.getDescription() != null) {
			buildHotelDescription(ojHotel, description.getDescription());
		}
		
		//amenity
		ojHotel.setAmenities(buildHotelAmenities(hotel.getAmenities()));
		
		ojHotels.add(ojHotel);
	}

	/**
	 * airport name && timezoneOffset
	 * @param timeZoneMap
	 * @param hotelAirports
	 * @param ojHotel
	 */
	private void buildHotelTimezone(List<HotelAirport> hotelAirports, OJHotel ojHotel) {
		ojHotel.setAirportName(hotelAirports.get(0).getName());
		String airportCode = aemService.retrieveAirportCodeByName(ojHotel.getAirportName());
		try{
			if(StringUtils.isNotEmpty(airportCode)) {
				ojHotel.setTimeZoneOffset(airportTimeZoneService.getAirPortTimeZoneOffset(airportCode));				
			}
		}catch(Exception e){
			logger.warn(String.format("Cannot find available timezone for airPort:%s", airportCode), e);
		}
	}
	
	/**
	 * Group OjHotel rooms by name, checkInDate, checkInTime, checkOutDate, checkOutTime
	 * @param ojHotels
	 * @return
	 */
	private List<OJHotel> groupOjHotelsRoom(List<OJHotel> ojHotels) {
		// group rooms by hotel
		Map<String, List<OJHotel>> groupedResult = ojHotels.stream()
				.collect(Collectors.groupingBy(OJHotel::getWrapperHash, toList()));
		
		// get map of ojHotel status 
		Map<String, String> statusMap = getHotelStatusMap(ojHotels);
		
		// distinct by name, checkInDate, checkInTime, checkOutDate, checkOutTime
		ojHotels = ojHotels.stream().map(WrapperOJHotel::new).distinct().map(WrapperOJHotel::unwrap).collect(toList());
		
		// flat map and assign rooms back to OJHotel
		ojHotels.forEach(h->h.setRooms(groupedResult.get(h.getWrapperHash())
				.stream().flatMap(th->th.getRooms().stream())
				.collect(Collectors.toList())));
		
		// reset hotel status
		ojHotels.forEach(h -> h.setBookingStatus(statusMap.get(h.getWrapperHash())));
		
		return ojHotels;
	}
	
	/**
	 * get map of ojHotel status 
	 * @param ojHotels
	 * @return
	 */
	private Map<String, String> getHotelStatusMap(List<OJHotel> ojHotels) {
		Map<String, String> statusMap = new HashMap<>();
		for(OJHotel ojHotel : ojHotels) {
			if(ojHotel == null || ojHotel.getBookingStatus() == null) {
				continue;
			}
			String key = ojHotel.getWrapperHash();
			String value = statusMap.get(key);
			
			String status = ojHotel.getBookingStatus();
			if(value == null
					|| (OjConstants.BOOKED.equals(status) && OjConstants.CANCELLED.equals(value))) {
				statusMap.put(key, status);
			}
		}
		return statusMap;
	}

	/**
	 * Wrapper class for OJHotel
	 */
	class WrapperOJHotel {
		
		private OJHotel e;

		public WrapperOJHotel(OJHotel e) {
			this.e = e;
		}

		public OJHotel unwrap() {
			return this.e;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WrapperOJHotel that = (WrapperOJHotel) o;
			return Objects.equals(e.getWrapperHash(), that.e.getWrapperHash());
		}

		@Override
		public int hashCode() {
			return Objects.hash(e.getWrapperHash());
		}
		
	}

	/**
	 * @param ojEvents
	 * @param product
	 * @param timeZoneMap 
	 */
	private void buildEventDetails(List<OJEvent> ojEvents, Product product) {
		if(product == null || product.getEvent() == null) {
			return;
		}
		Event event = product.getEvent();
		
		OJEvent ojEvent = new OJEvent();
		ojEvent.setId(MMBBizruleConstants.BOOKING_TYPE_EVENT + event.getBookingReference());
		ojEvent.setReference(event.getReference());
		ojEvent.setBookingStatus(product.getBookingStatus());
		ojEvent.setBookingReference(event.getBookingReference());
		
		if(event.getDetails() != null) {
			ojEvent.setName(event.getDetails().getName());
			if(event.getDetails().getAirPort() != null) {
				ojEvent.setAirportCode(event.getDetails().getAirPort().getIata());				
			}
			if(event.getDetails().getDestination() != null) {
				ojEvent.setCityName(event.getDetails().getDestination().getName());				
			}
		}
		
		try{
			if(StringUtils.isNotEmpty(ojEvent.getAirportCode())) {
				ojEvent.setTimeZoneOffset(airportTimeZoneService.getAirPortTimeZoneOffset(ojEvent.getAirportCode()));
			}
		}catch (Exception e){
			logger.warn(String.format("Cannot find available timezone for airPort:%s", ojEvent.getAirportCode()), e);
		}
		//time
		Tickets ticket = event.getTickets();
		try {
			Date date = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMMSS, ticket.getDate() + " " + ticket.getTime());
			if(StringUtils.isNotEmpty(ojEvent.getTimeZoneOffset())) {
				ojEvent.setOrderDate(DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMMSS, ticket.getDate() + " " + ticket.getTime(), ojEvent.getTimeZoneOffset()));				
			} else {
				ojEvent.setOrderDate(date);
			}
			ojEvent.setDate(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_YYYY_MM_DD, date));
			ojEvent.setTime(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HH_MM, date));
			buildOjEventBookingStatus(ojEvent);
		} catch (ParseException e) {
			logger.error("EventBooking ticket Date/time convet error", e);
		}
		
		//Description
		EventDescription description = event.getDescription();
		if(description != null) {
			OJEventDescription ojEventDescription = new OJEventDescription();
			ojEventDescription.setShortName(description.getEventName());
			ojEventDescription.setFullName(description.getFull());
			ojEventDescription.setInclusion(description.getInclusion());
			ojEventDescription.setOperatingDetails(description.getOperatingDetails());
			ojEventDescription.setAdditionalInstructions(description.getAdditionalInstructions());
			ojEventDescription.setSmall(description.getSmall());
			ojEventDescription.setEssential(description.getEssential());
			ojEvent.setDescription(ojEventDescription);
		}
		
		//Guest
		if(event.getGuestDetails() != null) {
			List<EventGuest> guests = event.getGuestDetails().getGuests();
			if(!CollectionUtils.isEmpty(guests)) {
				List<OJEventGuest> ojEventGuests = new ArrayList<>();
				for(EventGuest guest : guests) {
					OJEventGuest ojEventGuest = new OJEventGuest();
					ojEventGuest.setType(guest.getType());
					ojEventGuest.setRph(guest.getRph());
					ojEventGuest.setTicketCategory(guest.getTicketCategory());
					Name name = guest.getName();
					if(name != null) {
						ojEventGuest.setGivenName(name.getGivenName());
						ojEventGuest.setSurName(name.getSurName());
						ojEventGuest.setPrefix(name.getPrefix());
					}
					ojEventGuests.add(ojEventGuest);
				}
				ojEvent.setGuests(ojEventGuests);
			}
		}
		
		ojEvents.add(ojEvent);
	}
	
	/**
	 * @Description check if the booking is checked out 4 days ago
	 * @param checkoutDate
	 * @param now
	 * @return
	 */
	private boolean checkoutFourDaysAgo(Date checkoutDate, Date now) {
		long day = (now.getTime() - checkoutDate.getTime())/(24*60*60*1000);
		return day > 4;
	}
	
	/**
	 * @param rooms
	 * @param ojHotel
	 * @param now
	 */
	private void buildHotelRooms(List<Room> rooms, OJHotel ojHotel, Date now) {
		List<OJHotelRoom> ojHotelrooms = new ArrayList<>();
		for(int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if(room == null) {
				continue;
			}
			OJHotelRoom ojRoom = new OJHotelRoom();
			ojRoom.setDuration(room.getDuration());
			ojRoom.setAdults(room.getAdults());
			ojRoom.setChildren(room.getChildren());
			ojRoom.setInfants(room.getInfants());
			ojRoom.setIsCompleted(false);
			ojRoom.setIsStayed(false);
			ojRoom.setBookingStatus(ojHotel.getBookingStatus());
			
			String checkInDateStr = room.getCheckInDate();
			String checkOutDateStr = room.getCheckOutDate();
			if(checkInDateStr != null && checkOutDateStr != null) {
				try {
					Date checkInDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, checkInDateStr);
					ojRoom.setCheckInDate(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_YYYY_MM_DD, checkInDate));
					ojRoom.setCheckInDay(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_EEE, checkInDate));
					ojRoom.setCheckInTime(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HH_MM, checkInDate));
					
					Date checkOutDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, checkOutDateStr);
					ojRoom.setCheckOutDate(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_YYYY_MM_DD, checkOutDate));
					ojRoom.setCheckOutDay(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_EEE, checkOutDate));
					ojRoom.setCheckOutTime(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HH_MM, checkOutDate));
					
					String timeZoneOffset = ojHotel.getTimeZoneOffset();
					Date checkInGMTDate = null;
					Date checkOutGMTDate = null;
					if(StringUtils.isNotEmpty(timeZoneOffset)) {
						checkInGMTDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, checkInDateStr, timeZoneOffset);
						checkOutGMTDate = DateUtil.getStrToDate(OjConstants.TIME_FORMAT, checkOutDateStr, timeZoneOffset);
					} else {
						//TODO: use localtime without timezone
						checkInGMTDate = checkInDate;
						checkOutGMTDate = checkOutDate;
					}

					ojRoom.setIsStayed(now.compareTo(checkInGMTDate) > 0 && now.compareTo(checkOutGMTDate) < 0);
					ojRoom.setIsCompleted(now.compareTo(checkOutGMTDate) > 0);
					
					if(i == 0) {
						ojHotel.setCheckInDate(ojRoom.getCheckInDate());
						ojHotel.setOrderDate(checkInGMTDate);
						ojHotel.setCheckInTime(ojRoom.getCheckInTime());
						ojHotel.setIsInCheckInTime(isInCheckInTime(ojRoom.getCheckInDate(), timeZoneOffset));
						ojHotel.setCheckOutDate(ojRoom.getCheckOutDate());
						ojHotel.setOutDate(checkOutGMTDate);
						ojHotel.setCheckOutTime(ojRoom.getCheckOutTime());
						ojHotel.setIsCompleted(ojRoom.isCompleted());
						ojHotel.setIsStayed(ojRoom.isStayed());
						//set ojHotelBooking status
						buildOjHotelBookingStatus(ojHotel);
					}
					
				} catch (ParseException e) {
					logger.warn("room checkIn/Out Date convet error", e);
				}
			}
			
			//roomOptions
			List<RoomOption> roomOptions = room.getOption();
			if(!CollectionUtils.isEmpty(roomOptions)) {
				buildHotelRoomOption(ojRoom, roomOptions);
			}
			
			ojHotelrooms.add(ojRoom);
		}
		if(!CollectionUtils.isEmpty(ojHotelrooms)) {
			ojHotel.setRooms(ojHotelrooms);
		}
	}

	/**
	 * set OJHotel booking status
	 * @param ojHotel
	 */
	private void buildOjHotelBookingStatus(OJHotel ojHotel) {
		if(OjConstants.BOOKED.equals(ojHotel.getBookingStatus()) && isBookingCompleted(ojHotel.getOutDate())) {
			ojHotel.setBookingStatus(OjConstants.COMPLETED);
		}
	}
	
	/**
	 * set OJEvent booking status
	 * @param ojHotel
	 */
	private void buildOjEventBookingStatus(OJEvent ojEvent) {
		if(OjConstants.BOOKED.equals(ojEvent.getBookingStatus()) && isBookingCompleted(ojEvent.getOrderDate())) {
			ojEvent.setBookingStatus(OjConstants.COMPLETED);
		}
	}

	/**
	 * @param checkOutDate
	 * @return Boolean
	 */
	private boolean isBookingCompleted(Date date) {
		if(date == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		return date.before(cal.getTime());
	}

	/**
	 * @param ojRoom
	 * @param roomOptions
	 */
	private void buildHotelRoomOption(OJHotelRoom ojRoom, List<RoomOption> roomOptions) {
		List<OJHotelRoomOption> ojHotelRoomOptions = new ArrayList<>();
		for(RoomOption roomOption : roomOptions) {
			if(roomOption == null) {
				continue;
			}
			OJHotelRoomOption roomOptionDTO = new OJHotelRoomOption();
			roomOptionDTO.setSelectedBedType(roomOption.getSelectedBedType());
			roomOptionDTO.setCode(roomOption.getCode());
			roomOptionDTO.setName(roomOption.getName());
			roomOptionDTO.setDescription(roomOption.getDescription());
			
			//smokingPreference
			SmokingPreference smokingPreference = roomOption.getSmokingPreference();
			roomOptionDTO.setSmokingPreference(smokingPreference != null ? smokingPreference.getPreference() : null);
			
			//guest detail
			GuestDetails guestDetails = roomOption.getGuestDetails();
			if(guestDetails != null && !CollectionUtils.isEmpty(guestDetails.getGuest())) {
				List<Guest> guests = guestDetails.getGuest();
				List<OJRoomGuestDetail> ojRoomGuestDetails = new ArrayList<>();
				for(Guest guest : guests) {
					if(guest == null || guest.getName() == null) {
						continue;
					}
					OJRoomGuestDetail ojRoomGuestDetail = new OJRoomGuestDetail();
					GuestName guestName = guest.getName();
					ojRoomGuestDetail.setGivenName(guestName.getGivenName());
					ojRoomGuestDetail.setMiddleName(guestName.getMiddleName());
					ojRoomGuestDetail.setPaxType(guestName.getPaxType());
					ojRoomGuestDetail.setPrefix(guestName.getPrefix());
					ojRoomGuestDetail.setSurname(guestName.getSurname());
					ojRoomGuestDetails.add(ojRoomGuestDetail);
				}
				roomOptionDTO.setGuestDetails(ojRoomGuestDetails);
			}
			
			ojHotelRoomOptions.add(roomOptionDTO);
		}
		ojRoom.setRoomOptions(ojHotelRoomOptions);
	}
	
	/**
	 * @param address
	 * @return
	 */
	private OJAdress buildHotelAddress(Address address) {
		if(address == null) {
			return null;
		}
		OJAdress ojAdress = new OJAdress();
		ojAdress.setCompressed(address.getCompressed());
		
		if(!CollectionUtils.isEmpty(address.getCityNameInfos())) {	
			ojAdress.setCityName(address.getCityNameInfos().get(0).getCityName());
		}
		
		return ojAdress;
	}
	
	/**
	 * @param ojHotel
	 * @param descriptions
	 */
	private void buildHotelDescription(OJHotel ojHotel, Description... descriptions) {
		List<OJHotelDescription> ojDescriptions = new ArrayList<>();
		for(Description description : descriptions) {
			OJHotelDescription ojHotelDescription = new OJHotelDescription();
			ojHotelDescription.setCode(description.getCode());
			ojHotelDescription.setText(description.getText());
			ojDescriptions.add(ojHotelDescription);
		}
		ojHotel.setDescriptions(ojDescriptions);
	}
	
	/**
	 * @param amenities
	 * @return
	 */
	private List<OJHotelAmenity> buildHotelAmenities(Amenities amenities) {
		List<OJHotelAmenity> ojHotelAmenitys = null;
		if(amenities != null && !CollectionUtils.isEmpty(amenities.getAmenity())) {
			ojHotelAmenitys = new ArrayList<>();
			for(Amenity amenity : amenities.getAmenity()) {
				if(amenity != null) {
					OJHotelAmenity ojHotelAmenity = new OJHotelAmenity();
					ojHotelAmenity.setCode(amenity.getCode());
					ojHotelAmenitys.add(ojHotelAmenity);
				}
			}	
		}
		return ojHotelAmenitys;
	}
	
	/**
	 * @param response
	 * @return
	 */
	private OJContactDetails buildContactDetails(OJBookingResponseDTO response) {
		ContactDetails contactDetails = response.getContactDetails();
		if(contactDetails == null) {
			return null;
		}
		
		OJContactDetails ojContactDetails = new OJContactDetails();
		
		//name
		Name name = contactDetails.getName();
		if(name != null) {
			OJName ojName = new OJName();
			ojName.setGivenName(name.getGivenName());
			ojName.setSurName(name.getSurName());
			ojName.setPrefix(name.getPrefix());
			ojContactDetails.setName(ojName);
		}
		
		return ojContactDetails;
	}
	
	/**
	 * check whether pass checkInTime. True means it has already passed checIn time
	 * If there is something wrong, return true and don't show it in the frontend.
	 * @param checkInTime
	 * @return
	 */
	private Boolean isInCheckInTime(String checkInDate, String timeZoneOffset) {
		int result = 1;
		try {
			Date checkInDateTemp;
			if (!StringUtils.isEmpty(timeZoneOffset)) {				
				checkInDateTemp = DateUtil.getStrToGMTDate(DateUtil.DATE_PATTERN_YYYY_MM_DD, timeZoneOffset, checkInDate);
			}else {
				//TODO: use localtime without timezone
				checkInDateTemp = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD, checkInDate);
			}
			Date now = DateUtil.getGMTTime();
			result = DateUtil.compareDate(checkInDateTemp, now, Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			logger.error("Paser segment departureTime failure for orderDate", e);
		}
		return result > 0 || result == 0;
	}
}