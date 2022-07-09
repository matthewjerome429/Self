package com.cathaypacific.mmbbizrule.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormAddressRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormGuardianInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormPortInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormSegmentRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.model.umnreform.MultiLineOTRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormPortInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormSegmentRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.service.UMNREFormRemarkService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.google.common.collect.Lists;

@Service
public class UMNREFormRemarkServiceImpl implements UMNREFormRemarkService {
	
	/**
	 * Length of each group of info
	 * If the remark format is changed, please update the length here.
	 */
	private static final int UMNR_EFORM_REMARK_SEGMENT_LENGTH = 3;
	private static final int UMNR_EFORM_REMARK_PORT_INFO_LENGTH = 8;
	
	/**
	 * Common format
	 */
	private static final String UMNR_EFORM_REMARK_PREFIX = "MMB UMForm";
	private static final String UMNR_EFORM_REGEX_PRECENTAGE = "%";
	private static final String UMNR_EFORM_REMARK_POSTFIX = UMNR_EFORM_REGEX_PRECENTAGE + "END!" + UMNR_EFORM_REGEX_PRECENTAGE;
	private static final String UMNR_EFORM_REMARK_POSTFIX_STRING_FORMAT = UMNR_EFORM_REGEX_PRECENTAGE + UMNR_EFORM_REMARK_POSTFIX + UMNR_EFORM_REGEX_PRECENTAGE;
	private static final String UMNR_EFORM_REGEX_SPLITER = "\\/";
	
	/**
	 * UMNR EFORM SPLIT REGEX
	 * Input: RM MMB UMForm/P1/07/M/(36/F Happy (01)) Building)/(Happy road)
	 * Output: ["RM MMB UMForm", "P1", "07", "M", "(36/F (01)) Happy Building)", "(Happy road)"]
	 */
	private static final String UMNR_EFORM_SPLIT_REMARK_REGEX = 
			UMNR_EFORM_REGEX_SPLITER								// Split on "/"
			+ "(?="													// Followed by
				+ "(?:"												// Start a non-capture group
					+ "[^" + UMNR_EFORM_REGEX_PRECENTAGE + "]*"		// 0 or more non-bracket characters
					+  UMNR_EFORM_REGEX_PRECENTAGE					// 1 open-bracket
					+ "[^" + UMNR_EFORM_REGEX_PRECENTAGE + "]*"		// 0 or more non-bracket characters
					+ UMNR_EFORM_REGEX_PRECENTAGE					// 1 close-bracket
				+ ")*"												// 0 or more repetition of non-capture group
				+ "[^" + UMNR_EFORM_REGEX_PRECENTAGE + "]*"			// Finally 0 or more non-bracket characters
				+ "$"												// Till the end
			+ ")";
	
	private static final String AIRPORT_CODE_REGEX = "\\b[a-zA-Z]{3}\\b";
	private static final String FLIGHT_NUMBER_REGEX = "^[a-zA-Z].*[0-9]$";

	/**
	 * Free text format used to convert model to free text
	 */
	// Full UMNR Free text format: MMB UMForm/<Pax info><Flight Info>/%END!%
	private static final String UMNR_EFORM_FULL_FREETEXT_FORMAT = UMNR_EFORM_REMARK_PREFIX + "/%s%s/" + UMNR_EFORM_REMARK_POSTFIX_STRING_FORMAT;
	
	// <Pax info>
	// <Pax association (Px)>/<UMNR Age (always double digit)>/<UMNR Gender (M/F)>/<UMNR address>/<UMNR guardian>
	private static final String UMNR_EFORM_PAX_FREETEXT_FORMAT = "P%s/%s/%s/%s/%s";
	
	// <Flight Info>
	// <Flight No.>/<Flight Date DDMMYYYY><Port info>
	private static final String UMNR_EFORM_FLIGHT_INFO_FREETEXT_FORMAT = "/%s/%s%s";
	
	// <Port info>
	// <Airport Code>/<Port guardian>
	private static final String UMNR_EFORM_PORT_INFO_FREETEXT_FORMAT = "/%s/%s";
	
	// <Parent Guardian Name>/<Guardian Relationship>/<Parent Guardian Phone>/<Parent Guardian Address>
	private static final String UMNR_EFORM_GUARDIAN_RELATIONSHIP_INFO_FREETEXT_FORMAT = "%s/%%%s%%/%s-%s/%s";
	// <Parent Guardian Name>/<Parent Guardian Phone>/<Parent Guardian Address>
	private static final String UMNR_EFORM_GUARDIAN_INFO_FREETEXT_FORMAT = "%s/%s-%s/%s";
	
	// <Address>
	// %<Parent Guardian Room Unit Building Floor>%/%<Parent Guardian Street Address>%/%<Parent Guardian City>%/<Parent Guardian Country>
	private static final String UMNR_EFORM_ADDRESS_FREETEXT_FORMAT = "%%%s%%/%%%s%%/%%%s%%/%s";
	
	private static LogAgent logger = LogAgent.getLogAgent(UMNREFormRemarkServiceImpl.class);
	
	/**
	 * Build UMNREFormRemark models based on remark list in RetrievePnrBooking
	 * @return a list of UMNREFormRemark
	 */
	public List<UMNREFormRemark> buildUMNREFormRemark(RetrievePnrBooking retrievePnrBooking) {
		// Retrieve remarks and convert them to remark model
		List<MultiLineOTRemark> otRemarks = BookingBuildUtil.retrieveMultilineRemarks(
			retrievePnrBooking.getRemarkList(), UMNR_EFORM_REMARK_PREFIX, UMNR_EFORM_REMARK_POSTFIX
		);
		List<UMNREFormRemark> umnreFormRemarks = Lists.newArrayList();
		for (MultiLineOTRemark otRemark: otRemarks) {
			try {
				UMNREFormRemark umnrEFormRemark = parseUMNREFormRemark(otRemark.getRemark(), otRemark.getOtQualifierNumber());
				if (umnrEFormRemark != null) {
					umnreFormRemarks.add(umnrEFormRemark);
				}
			} catch (Exception exception) {
				logger.error(
					String.format(
						"Error occured when parse UMNR EForm freetext to remark model. This remark will be ignored: %s",
						otRemark.getRemark()
					), exception
				);
			}
		}
		
		return umnreFormRemarks;
	}
	
	/**
	 * Parse UMNREFormRemark from string to model for one
	 * @param remark
	 * @return UMNREFormRemark
	 */
	private UMNREFormRemark parseUMNREFormRemark(String remark, List<String> otNumberList) {
		String[] splitedRemarks = remark.split(UMNR_EFORM_SPLIT_REMARK_REGEX);
		
		// This is not an UMNR EForm remark
		if (!StringUtils.equalsIgnoreCase(splitedRemarks[0].trim(), UMNR_EFORM_REMARK_PREFIX) ||
			!StringUtils.equalsIgnoreCase(splitedRemarks[splitedRemarks.length - 1].trim(), UMNR_EFORM_REMARK_POSTFIX)) {
			return null;
		}
		
		return buildUMNREFormRemark(splitedRemarks, otNumberList);
	}
	
	/**
	 * @param item
	 * @return true if the string item is a flight number (e.g. CX250)
	 */
	private boolean isFlightNumberRemark(String item) {
		return item.matches(FLIGHT_NUMBER_REGEX);
	}
	
	
	/**
	 * @param item
	 * @return true if the string item is a port code (e.g. HKG)
	 */
	private boolean isPortInfoRemark(String item) {
		return item.matches(AIRPORT_CODE_REGEX);
	}
	
	/**
	 * Build UMNREFormRemark from a list of splited remarks
	 * @param splitedRemarks
	 * @return UMNREFormRemark
	 */
	private UMNREFormRemark buildUMNREFormRemark(String[] splitedRemarks, List<String> otNumberList) {
		removeOpenCloseBrackets(splitedRemarks);
		
		// Parse remark into UMNREFormRemark
		UMNREFormRemark umnrEFormRemark = new UMNREFormRemark();
		for(int i = 0; i < splitedRemarks.length; i++) {
			String splitedItem = splitedRemarks[i];
			
			if (i == 1) {
				umnrEFormRemark.setPassengerId(splitedItem);
			} else if (i == 2) {
				umnrEFormRemark.setAge(splitedItem);
			} else if (i == 3) {
				umnrEFormRemark.setGender(splitedItem);
			} else if (i == 4) {
				umnrEFormRemark.getAddress().setBuilding(splitedItem);
			} else if (i == 5) {
				umnrEFormRemark.getAddress().setStreet(splitedItem);
			} else if (i == 6) {
				umnrEFormRemark.getAddress().setCity(splitedItem);
			} else if (i == 7) {
				umnrEFormRemark.getAddress().setCountryCode(splitedItem);
			} else if (i == 8) {
				umnrEFormRemark.getParentInfo().setName(splitedItem);
			} else if (i == 9) {
				umnrEFormRemark.getParentInfo().setPhoneNumber(FreeTextUtil.decodeNumber(splitedItem));
			} else if (i == 10) {
				umnrEFormRemark.getParentInfo().getAddress().setBuilding(splitedItem);
			} else if (i == 11) {
				umnrEFormRemark.getParentInfo().getAddress().setStreet(splitedItem);
			} else if (i == 12) {
				umnrEFormRemark.getParentInfo().getAddress().setCity(splitedItem);
			} else if (i == 13) {
				umnrEFormRemark.getParentInfo().getAddress().setCountryCode(splitedItem);
			} else if (i == 14) {
				umnrEFormRemark.setSegments(buildUMNREFormSegmentRemarks(splitedRemarks, i));
				break;
			}
		}
		umnrEFormRemark.setOtQualifierList(otNumberList);
		return umnrEFormRemark;
	}
	
	/** Build a list of UMNREFormSegmentRemark recursively by specifying starting index
	 * @param splitedRemarks
	 * @param startingIndex
	 * @return a list of UMNREFormSegmentRemark
	 */
	private List<UMNREFormSegmentRemark> buildUMNREFormSegmentRemarks(String[] splitedRemarks, int startingIndex) {
		List<UMNREFormSegmentRemark> umnrEFormSegmentRemarks = Lists.newArrayList();
		
		int totalLength = UMNR_EFORM_REMARK_SEGMENT_LENGTH;	// Number of item in segment remark
		int maxIndex = startingIndex + (totalLength - 1);
		
		UMNREFormSegmentRemark umnrEFormSegmentRemark = new UMNREFormSegmentRemark();
		umnrEFormSegmentRemarks.add(umnrEFormSegmentRemark);
		
		// Put splited remark value to UMNREFormSegmentRemark
		int count = 0;
		for (int i = startingIndex; i <= maxIndex; i++) {
			switch(count){
			case 0:
				umnrEFormSegmentRemark.setFlightNumber(splitedRemarks[i]);
				break;
			case 1:
				umnrEFormSegmentRemark.setFlightDate(splitedRemarks[i]);
				break;
			case 2:
				umnrEFormSegmentRemark.setPortInfo(buildUMNREFormPortInfoRemark(splitedRemarks, i));
				break;
			default:
				break;
			}
			count++;
		}

		// Check the next part. If it is segment info, then proceed it recursively.
		int nextIndex = ((umnrEFormSegmentRemark.getPortInfo().size() * UMNR_EFORM_REMARK_PORT_INFO_LENGTH - 1) + maxIndex) + 1;
		if (splitedRemarks.length > nextIndex + 1 && isFlightNumberRemark(splitedRemarks[nextIndex])) {
			umnrEFormSegmentRemarks.addAll(buildUMNREFormSegmentRemarks(splitedRemarks, nextIndex));
		}
		
		return umnrEFormSegmentRemarks;
	}
	
	/**
	 * Build a list of UMNREFormPortInfoRemark by specifying correct starting index
	 * @param splitedRemarks
	 * @param startingIndex
	 * @return A list of UMNREFormPortInfoRemark
	 */
	private List<UMNREFormPortInfoRemark> buildUMNREFormPortInfoRemark(String[] splitedRemarks, int startingIndex) {
		List<UMNREFormPortInfoRemark> umnrEFormPortInfoRemarks = Lists.newArrayList();
		
		int totalLength = UMNR_EFORM_REMARK_PORT_INFO_LENGTH;	// Number of item in port info remark
		int maxIndex = startingIndex + totalLength - 1;
		
		UMNREFormPortInfoRemark umnrEFormPortInfoRemark = new UMNREFormPortInfoRemark();
		umnrEFormPortInfoRemarks.add(umnrEFormPortInfoRemark);
		
		// Put splited remark value to UMNREFormPortInfoRemark
		int count = 0;
		for (int i = startingIndex; i <= maxIndex; i++) {
			
			switch(count){
			case 0:
				umnrEFormPortInfoRemark.setAirportCode(splitedRemarks[i]);
				break;
			case 1:
				umnrEFormPortInfoRemark.getGuardianInfo().setName(splitedRemarks[i]);
				break;
			case 2:
				umnrEFormPortInfoRemark.getGuardianInfo().setRelationship(splitedRemarks[i]);
				break;
			case 3:
				umnrEFormPortInfoRemark.getGuardianInfo().setPhoneNumber(FreeTextUtil.decodeNumber(splitedRemarks[i]));
				break;
			case 4:
				umnrEFormPortInfoRemark.getGuardianInfo().getAddress().setBuilding(splitedRemarks[i]);
				break;
			case 5:
				umnrEFormPortInfoRemark.getGuardianInfo().getAddress().setStreet(splitedRemarks[i]);
				break;
			case 6:
				umnrEFormPortInfoRemark.getGuardianInfo().getAddress().setCity(splitedRemarks[i]);
				break;
			case 7:
				umnrEFormPortInfoRemark.getGuardianInfo().getAddress().setCountryCode(splitedRemarks[i]);
				break;
			default:
				break;
			}

			count++;
		}
		
		// Check the next part. If it is port info, then proceed it recursively.
		int nextIndex = maxIndex + 1;
		if (splitedRemarks.length > nextIndex + 1 && isPortInfoRemark(splitedRemarks[nextIndex])) {
			umnrEFormPortInfoRemarks.addAll(buildUMNREFormPortInfoRemark(splitedRemarks, nextIndex));
		}
		
		return umnrEFormPortInfoRemarks;
	}
	
	/**
	 * Remove open and close bracket for each of string items
	 * @param splitedItems
	 */
	private void removeOpenCloseBrackets(String[] splitedItems) {
		for(int i = 0; i < splitedItems.length; i++) {
			String item = splitedItems[i];
			splitedItems[i] = item.replaceAll("^" + UMNR_EFORM_REGEX_PRECENTAGE, "").replaceAll(UMNR_EFORM_REGEX_PRECENTAGE + "$", "");
		}
	}
	
	/**
	 * build freeText for saving UMNR form
	 * @param requestDTO
	 * @return
	 */
	public String buildUMNREFormRMFreeText(UmnrFormUpdateRequestDTO request) {
		return String.format(
			UMNR_EFORM_FULL_FREETEXT_FORMAT,
			buildPassengerInfoFreeText(request),
			buildFlightInfoFreeText(request.getSegments())
		);
	}
	
	private String buildPassengerInfoFreeText(UmnrFormUpdateRequestDTO request) {
		return String.format(
			UMNR_EFORM_PAX_FREETEXT_FORMAT,
			request.getPassengerId(),
			request.getDoubleDigitAge(),
			request.getGender(),
			buildAddressFreeText(request.getAddress()),
			buildGuardianFreeText(request.getParentInfo())
		);
	}
	
	private String buildGuardianFreeText(UmnrFormGuardianInfoRemarkDTO guardianInfo) {
		if (StringUtils.isEmpty(guardianInfo.getRelationship())) {
			return String.format(
				UMNR_EFORM_GUARDIAN_INFO_FREETEXT_FORMAT,
				guardianInfo.getName(),
				FreeTextUtil.encodeNumber(guardianInfo.getPhoneInfo().getPhoneCountryNumber()),
				FreeTextUtil.encodeNumber(guardianInfo.getPhoneInfo().getPhoneNo()),
				buildAddressFreeText(guardianInfo.getAddress())
			);
		} else {
			return String.format(
				UMNR_EFORM_GUARDIAN_RELATIONSHIP_INFO_FREETEXT_FORMAT,
				guardianInfo.getName(),
				guardianInfo.getRelationship(),
				FreeTextUtil.encodeNumber(guardianInfo.getPhoneInfo().getPhoneCountryNumber()),
				FreeTextUtil.encodeNumber(guardianInfo.getPhoneInfo().getPhoneNo()),
				buildAddressFreeText(guardianInfo.getAddress())
			);
		}
		
	}
	
	private String buildAddressFreeText(UmnrFormAddressRemarkDTO address) {
		return String.format(
			UMNR_EFORM_ADDRESS_FREETEXT_FORMAT,
			address.getBuilding(),
			address.getStreet(),
			address.getCity(),
			address.getCountryCode()
		);
	}
	
	private String buildFlightInfoFreeText(List<UmnrFormSegmentRemarkDTO> segments) {
		if (CollectionUtils.isEmpty(segments)) {
			logger.warn("Flight info is null when build UMNR remark free text. The flight info will be ignored.");
			return StringUtils.EMPTY;
		}
		
		StringBuilder freeText = new StringBuilder();
		for(UmnrFormSegmentRemarkDTO segment: segments) {
			freeText.append(
				String.format(
					UMNR_EFORM_FLIGHT_INFO_FREETEXT_FORMAT,
					segment.getFlightNumber(),
					segment.getFlightDate(),
					buildPortInfoFreeText(segment.getPortInfo())
				)
			);
		}

		return freeText.toString();
	}
	
	private String buildPortInfoFreeText(List<UmnrFormPortInfoRemarkDTO> portInfos) {
		if (CollectionUtils.isEmpty(portInfos)) {
			logger.warn("Port info is null when build UMNR remark free text. The port info will be ignored.");
			return StringUtils.EMPTY;
		}
		
		StringBuilder freeText = new StringBuilder();
		for(UmnrFormPortInfoRemarkDTO portInfo: portInfos) {
			freeText.append(
				String.format(
					UMNR_EFORM_PORT_INFO_FREETEXT_FORMAT,
					portInfo.getAirportCode(),
					buildGuardianFreeText(portInfo.getGuardianInfo())
				)
			);
		}
		
		return freeText.toString();
	}

}
