package com.cathaypacific.mmbbizrule.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormGuardianInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormSegmentDTO;
import com.cathaypacific.mmbbizrule.service.UMNREFormPDFService;
import com.cathaypacific.mmbbizrule.util.PDFUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;


@Service
public class UMNREFormPDFServiceImpl implements UMNREFormPDFService{
	
	private static final String ACRO_FIELD_BOOKING_REFERENCE = "bookingReference";
	
	private static final String ACRO_FIELD_PARENT_NAME = "parentName";
	private static final String ACRO_FIELD_PARENT_ADDRESS_1 = "parentAddress1";
	private static final String ACRO_FIELD_PARENT_ADDRESS_2 = "parentAddress2";
	private static final String ACRO_FIELD_PARENT_TEL = "parentTel";

	private static final String ACRO_FIELD_UM_FAMILY_NAME = "umFamilyName";
	private static final String ACRO_FIELD_UM_GIVEN_NAME = "umGivenName";
	private static final String ACRO_FIELD_UM_AGE = "umAge";
	private static final String ACRO_FIELD_UM_GENDER = "umGender";
	private static final String ACRO_FIELD_UM_PERMANENT_ADDRESS = "umPermanentAddress";
	private static final String ACRO_FIELD_UM_PARENTAL_LOCKED = "umParentalLocked";
	
	private static final String ACRO_FIELD_FLIGHT_NUMBER = "flightNum%s";
	private static final String ACRO_FIELD_FLIGHT_DATE = "flightDate%s";
	private static final String ACRO_FIELD_FLIGHT_ORIGIN = "originPort%s";
	private static final String ACRO_FIELD_FLIGHT_DESTINATION = "destinationPort%s";
	private static final String ACRO_FIELD_PERSON_SEEING_OFF_NAME = "personSeeingOffAtDeparture_name";
	private static final String ACRO_FIELD_PERSON_SEEING_OFF_RELATIONSHIP = "personSeeingOffAtDeparture_relationship";
	private static final String ACRO_FIELD_PERSON_SEEING_OFF_ADDRESS = "personSeeingOffAtDeparture_address";
	private static final String ACRO_FIELD_PERSON_SEEING_OFF_TEL = "personSeeingOffAtDeparture_tel";
	private static final String ACRO_FIELD_PERSON_MEETING_ON_NAME = "personMeetingOnArrival_name";
	private static final String ACRO_FIELD_PERSON_MEETING_ON_RELATIONSHIP = "personMeetingOnArrival_relationship";
	private static final String ACRO_FIELD_PERSON_MEETING_ON_ADDRESS = "personMeetingOnArrival_address";
	private static final String ACRO_FIELD_PERSON_MEETING_ON_TEL = "personMeetingOnArrival_tel";
	
	private static final String ACRO_FIELD_TEL_FORMAT = "(%s) %s";

	private static LogAgent logger = LogAgent.getLogAgent(UMNREFormPDFServiceImpl.class);
	
	private static final Gson GSON = new Gson();

	@Override
	public OutputStream buildPDF(InputStream pdfTemplate, String oneARloc, List<UMNREFormSegmentDTO> umnrSegments, UMNREFormPassengerDTO umnrPassenger, UMNREFormJourneyDTO umnrJourney) throws ParseException, IOException {
		// Build PDF data model
		Map<String, Object> pdfDataModel = buildPDFDataModel(oneARloc, umnrSegments, umnrPassenger, umnrJourney);
		logger.info(String.format("UMNR EForm PDF will be generated with data %s", GSON.toJson(pdfDataModel)));

		// Fill in PDF template
		return PDFUtil.fillInPdfTemplate(pdfTemplate, pdfDataModel);
	}
	
	/**
	 * Build PDF data to a map with key-value pair format where key is arco field name of PDF file
	 * @param umnrSegments - all segments built by UMNREFormBuildService
	 * @param umnrPassenger - passenger that you want to generate the PDF. Built by UMNREFormBuildService.
	 * @param umnrJourney - journey that you want to generate the PDF. Built by UMNREFormBuildService.
	 * @return map with key-value pair format where key is arco field name of PDF file
	 */
	private Map<String, Object> buildPDFDataModel(String oneARloc, List<UMNREFormSegmentDTO> umnrSegments, UMNREFormPassengerDTO umnrPassenger, UMNREFormJourneyDTO umnrJourney) {
		Map<String, Object> pdfModel = Maps.newHashMap();
		
		// Booking reference
		pdfModel.put(ACRO_FIELD_BOOKING_REFERENCE, oneARloc);
		
		// Personal information
		pdfModel.put(ACRO_FIELD_UM_FAMILY_NAME, umnrPassenger.getFamilyName());
		pdfModel.put(ACRO_FIELD_UM_GIVEN_NAME, umnrPassenger.getGivenName());
		pdfModel.put(ACRO_FIELD_UM_AGE, umnrPassenger.getAge());
		pdfModel.put(ACRO_FIELD_UM_GENDER, umnrPassenger.getGender());
		pdfModel.put(ACRO_FIELD_UM_PARENTAL_LOCKED, umnrPassenger.isParentalLock());
		
		UMNREFormGuardianInfoDTO parentInfo = umnrPassenger.getParentInfo();
		pdfModel.put(ACRO_FIELD_PARENT_NAME, parentInfo.getName());
		pdfModel.put(ACRO_FIELD_PARENT_ADDRESS_1, parentInfo.getAddress().toBuildStreetString());
		pdfModel.put(ACRO_FIELD_PARENT_ADDRESS_2, parentInfo.getAddress().toCityCountryString());
		if (StringUtils.isNotEmpty(parentInfo.getPhoneCountryNumber()) && StringUtils.isNotEmpty(parentInfo.getPhoneNumber())) {
			pdfModel.put(
				ACRO_FIELD_PARENT_TEL,
				String.format(
					ACRO_FIELD_TEL_FORMAT,
					parentInfo.getPhoneCountryNumber(),
					parentInfo.getPhoneNumber()
				)
			);
		}
		pdfModel.put(
			ACRO_FIELD_UM_PERMANENT_ADDRESS,
			(umnrPassenger.getPermanentAddress() != null) ? umnrPassenger.getPermanentAddress().toString() : ""
		);
		
		// Person seeing off
		UMNREFormGuardianInfoDTO personSeeingOff = umnrJourney.getPersonSeeingOffDeparture();
		pdfModel.put(ACRO_FIELD_PERSON_SEEING_OFF_NAME, personSeeingOff.getName());
		pdfModel.put(ACRO_FIELD_PERSON_SEEING_OFF_RELATIONSHIP, personSeeingOff.getRelationship());
		pdfModel.put(ACRO_FIELD_PERSON_SEEING_OFF_ADDRESS, (personSeeingOff.getAddress() != null) ? personSeeingOff.getAddress().toString() : "");
		if (StringUtils.isNotEmpty(personSeeingOff.getPhoneCountryNumber()) && StringUtils.isNotEmpty(personSeeingOff.getPhoneNumber())) {
			pdfModel.put(
				ACRO_FIELD_PERSON_SEEING_OFF_TEL,
				String.format(
					ACRO_FIELD_TEL_FORMAT,
					personSeeingOff.getPhoneCountryNumber(), personSeeingOff.getPhoneNumber()
				)
			);
		}
		
		// Person seeing on
		UMNREFormGuardianInfoDTO personMeetingAt = umnrJourney.getPersonMeetingArrival();
		pdfModel.put(ACRO_FIELD_PERSON_MEETING_ON_NAME, personMeetingAt.getName());
		pdfModel.put(ACRO_FIELD_PERSON_MEETING_ON_RELATIONSHIP, personMeetingAt.getRelationship());
		pdfModel.put(ACRO_FIELD_PERSON_MEETING_ON_ADDRESS, (personMeetingAt.getAddress() != null) ? personMeetingAt.getAddress().toString() : "");
		if (StringUtils.isNotEmpty(personMeetingAt.getPhoneCountryNumber()) && StringUtils.isNotEmpty(personMeetingAt.getPhoneNumber())) {
			pdfModel.put(
				ACRO_FIELD_PERSON_MEETING_ON_TEL,
				String.format(
					ACRO_FIELD_TEL_FORMAT,
					personMeetingAt.getPhoneCountryNumber(),
					personMeetingAt.getPhoneNumber()
				)
			);
		}
		
		// By each segment
		List<String> segmentIds = umnrJourney.getSegmentIds();
		for(int i = 0; i < segmentIds.size(); i++) {
			String segmentId = segmentIds.get(i);
			
			UMNREFormSegmentDTO umnrSegment = umnrSegments.stream().filter(
				segment -> StringUtils.equalsIgnoreCase(segment.getSegmentId(), segmentId)
			).findFirst().orElse(null);
			if (umnrSegment == null) {
				logger.warn(
					String.format(
						"The segment id %s cannot be found in umnrSegments built from passenger id %s. The segment will be ignored.",
						segmentId,
						umnrPassenger.getPassengerId()
					)
				);
				continue;
			}
			
			int acroFieldIndex = i + 1;
			pdfModel.put(String.format(ACRO_FIELD_FLIGHT_ORIGIN, acroFieldIndex), umnrSegment.getOriginPort());
			pdfModel.put(String.format(ACRO_FIELD_FLIGHT_DESTINATION, acroFieldIndex), umnrSegment.getDestPort());
			pdfModel.put(
				String.format(ACRO_FIELD_FLIGHT_NUMBER, acroFieldIndex),
				umnrSegment.getMarketingCompany() + umnrSegment.getMarketingSegmentNumber());
			pdfModel.put(
				String.format(ACRO_FIELD_FLIGHT_DATE, acroFieldIndex),
				DateUtil.convertDateFormat(
					umnrSegment.getDepartureTime(),
					DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
					DateUtil.DATE_PATTERN_DDMMMYYYY_WITH_SPACE
				)
			);
		}

		return pdfModel;
	}
}
