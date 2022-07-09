package com.cathaypacific.mmbbizrule.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.seatmap.service.SeatMapService;
import com.cathaypacific.mmbbizrule.dto.request.seatmap.SeatMapRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.seatmap.SegmentInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.AswrInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.ExtraSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.GridDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.GridRowDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.RetrieveSeatMapDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatCharacteristicDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatDetailsDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatMapDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.AddSeatService;
import com.cathaypacific.mmbbizrule.service.UpdateSeatService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info
 * @author fengfeng.jiang
 * @date Jan 29, 2018 5:42:46 PM
 * @version V1.0
 */
@Service
public class UpdateSeatServiceImpl implements UpdateSeatService{

	@Autowired
	AddSeatService addSeatService;
	
	@Autowired
	DeletePnrService deletePnrService;
	
	@Autowired
	private SeatMapService seatMapService;
	
	private static String CONFIRMED = "HK|KK|KL";
	
	@Override
	public RetrievePnrBooking updateSeat(UpdateSeatRequestDTO requestDTO, LoginInfo loginInfo, Booking booking) throws BusinessBaseException{
		//1.check if add is needed
		boolean needAdd = checkNeedAdd(requestDTO);
		
		//2.check seats & build delete OTs & build XLMR info
		Session session = null;
		List<XlwrInfo> xlwrInfos = new ArrayList<>();//XLWR info
		List<RemarkInfo> remarkInfos = new ArrayList<>();//remark info
		List<AswrInfo> aswrInfos = new ArrayList<>();//ASWR info
		Map<String, List<String>> deleteMap = installDeleteMap(loginInfo, requestDTO, booking, xlwrInfos, remarkInfos, aswrInfos);
		
		//3.check whether all seats are available
		if(deleteMap.containsKey(MMBBizruleConstants.UPDATE_SEAT_REJECTED_INELIGIBLE)) {
			throw new UnexpectedException("Ineligible to update the seat.", new ErrorInfo(ErrorCodeEnum.ERR_INELIGIBLE_TO_UPDATE_SEAT));
		}
		
		RetrievePnrBooking resBooking = null;
		if(!deleteMap.isEmpty()) {
			if(needAdd) {
				session = new Session();//set begin transaction
			}
			Map<String, List<String>> map = buildDeleteOtMap(deleteMap);
			resBooking = deletePnrService.deletePnr(requestDTO.getRloc(), map, session);
			
			session = resBooking.getSession();
			session.setStatus(SessionStatus.END.getStatus());//set end transaction
		}
		
		//4.add seat
		if(needAdd) {
			//check whether need to add seat back for the over-deleted seats
			processOverDeletedSeat(requestDTO, booking);
			return addSeatService.addSeat(requestDTO, session, xlwrInfos, remarkInfos, aswrInfos);
		} else {
			return resBooking;
		}
	}
	
	/**
	 * build OT map for 1A delete
	 * @param deleteMap
	 * @return map
	 */
	private Map<String, List<String>> buildDeleteOtMap(Map<String, List<String>> deleteMap) {
		Map<String, List<String>> map = new HashMap<>();
		List<String> values = new ArrayList<>();
		for(Entry<String, List<String>> entry : deleteMap.entrySet()) {
			values.addAll(entry.getValue());
		}
		map.put(OneAConstants.OT_QUALIFIER, values);
		return map;
	}

	/**
	 * check if add is needed
	 * @param requestDTO
	 * @return
	 */
	public boolean checkNeedAdd(UpdateSeatRequestDTO requestDTO) {
		boolean needAdd =false;
		if(!CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())){
			for(PaxSeatDetail details: requestDTO.getPaxSeatDetails()) {
				String seatNo = details.getSeatNo();
				String preference = details.getSeatPreference();
				
				if(!StringUtils.isEmpty(seatNo) || !StringUtils.isEmpty(preference)) {
					needAdd = true;
				}
			}
		}
		
		return needAdd;
	}
	
	/**
	 * check if add is needed
	 * @param requestDTO
	 * @return
	 */
	public boolean checkSeats(UpdateSeatRequestDTO requestDTO) {
		boolean needAdd =false;
		if(!CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())){
			for(PaxSeatDetail details: requestDTO.getPaxSeatDetails()) {
				String seatNo = details.getSeatNo();
				String preference = details.getSeatPreference();
				
				if(!StringUtils.isEmpty(seatNo) || !StringUtils.isEmpty(preference)) {
					needAdd = true;
				}
			}
		}
		
		return needAdd;
	}
	
	/**
	 * 
	* @Description
	* @param
	* @return void
	* @author fengfeng.jiang
	 * @param aswrInfos 
	 * @throws BusinessBaseException 
	 */
	private Map<String, List<String>> installDeleteMap(LoginInfo loginInfo, UpdateSeatRequestDTO requestDTO, Booking booking, List<XlwrInfo> xlwrInfos, List<RemarkInfo> remarkInfos, List<AswrInfo> aswrInfos) throws BusinessBaseException{
		Map<String, List<String>> otListMap = new HashMap<>();
		List<String> qulifierList = new ArrayList<>();
		List<BigInteger> invalidOts = booking.getInvalidOts();
		if(!CollectionUtils.isEmpty(invalidOts)) {
			for(BigInteger ot:invalidOts) {
				qulifierList.add(ot.toString());
			}
		}
	
		//retrieve seat map
		String currentSegmentId = requestDTO.getSegmentId();
		Segment segment = booking.getSegments().stream().filter(tempSegment -> requestDTO.getSegmentId().equals(tempSegment.getSegmentID())).findFirst().get();
		// if there is seat number in the requestDTO to be updated
		boolean needToUpdateSeatNo = false;
		// check if there is seat number in the requestDTO
		if(!CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())) {
			needToUpdateSeatNo = requestDTO.getPaxSeatDetails().stream().allMatch(detail -> !StringUtils.isEmpty(detail.getSeatNo()));
		}
		// if there is seat number in the requestDTO to be updated, retrieve the seat map
		RetrieveSeatMapDTO seatMapDTO = needToUpdateSeatNo ? retrieveSeatMap(segment, loginInfo.getMmbToken(), booking.getOneARloc()) : null;

		Optional.ofNullable(requestDTO.getPaxSeatDetails()).orElseGet(Collections::emptyList).forEach(seatDetail->{
			String currentPassengerId = seatDetail.getPassengerID();
			//build seat OT number to delete
			PassengerSegment passengerSegment = booking.getPassengerSegments().stream()
					.filter(tempPassengerSegment -> currentSegmentId.equals(tempPassengerSegment.getSegmentId())
							&& currentPassengerId.equals(tempPassengerSegment.getPassengerId()))
					.findFirst().orElse(null);
			if(passengerSegment != null) {
				BigInteger qulifierId = passengerSegment.getSeatQulifierId();
				if(qulifierId != null){
					qulifierList.add(qulifierId.toString());
					
				}
			}
			
			//check whether update seatNo
			if(StringUtils.isEmpty(seatDetail.getSeatNo())) return;
			
			//check seat type and eligibility
			checkSeatTypeAndEligibility(requestDTO, seatMapDTO, otListMap, booking);
			
			if(passengerSegment == null) return;
			String companyId = getCompanyIdBySt(booking.getSegments(), currentSegmentId);
			List<RetrievePnrDataElements> skList = booking.getSkList();

			//check if need to update ASWR
			if(passengerSegment.isEnableASRForRedemptionBooking()
					&& seatDetail.isAsrSeat()
					&& passengerSegment.getMmbSeatSelection() != null
					&& !hasIncludedSKList(booking.getSkList(), OneAConstants.SK_TYPE_ASWR, currentSegmentId, currentPassengerId)
					&& passengerSegment.getMmbSeatSelection().isAsrFOC()) {
				buildASWR(skList, aswrInfos, currentPassengerId, currentSegmentId, companyId);
			}
			//check whether user is top tier member
			if(!isTopTier(passengerSegment)) {
				//check whether user change seat from paid extra leg room seat to normal seat
				SeatDetail seatInfo = passengerSegment.getSeat();
				if(seatInfo != null && BooleanUtils.isTrue(seatInfo.isExlSeat()) && BooleanUtils.isTrue(seatInfo.isPaid()) && !StringUtils.isEmpty(seatInfo.getStatus()) && CONFIRMED.contains(seatInfo.getStatus()) && !seatDetail.isExlSeat()) {
					buildRemark(remarkInfos, currentPassengerId, currentSegmentId);
				}
				return;
			}
			
			//build SK XLWR OT number to delete
			if(skList == null) {
				if(seatDetail.isExlSeat()) {
					buildXLMR(xlwrInfos, currentPassengerId, currentSegmentId, companyId);
				}
				return;
			}
			
			boolean needAddXLMR = true;
			List<PassengerSegment> passengerSegments = booking.getPassengerSegments().stream().filter(tempPassengerSegment -> currentPassengerId.equals(tempPassengerSegment.getPassengerId())).collect(Collectors.toList());
			for(RetrievePnrDataElements sk:skList) {
				if(!OneAConstants.SK_TYPE_XLWR.equals(sk.getType())) continue;
				String passengerId = sk.getPassengerId();
				String segmentId = sk.getSegmentId();
				
				if(currentPassengerId.equals(passengerId)) {
					if(StringUtils.isEmpty(segmentId)) {
						/** if SK XLWR element has been found on customer level **/
						//1、remove it from customer level
						qulifierList.add(sk.getQulifierId().toString());
						
						//2、if user select extra leg room seat, add SK XLWR to product level
						if(seatDetail.isExlSeat() && !hasProductLevelXlmr(skList, currentPassengerId, currentSegmentId)) {
							buildXLMR(xlwrInfos, currentPassengerId, currentSegmentId, companyId);
						}
						
						//3、update the element to product level of all left upcoming sectors if have no product level XLWR
						for(PassengerSegment tempPassengerSegment:passengerSegments) {
							String tempSegmentId = tempPassengerSegment.getSegmentId();
							if(!currentSegmentId.equals(tempSegmentId) && !hasProductLevelXlmr(skList, currentPassengerId, tempSegmentId)) {
								Segment tempSegment = booking.getSegments().stream().filter(segment1 -> tempSegmentId.equals(segment1.getSegmentID())).findFirst().get();
								String airCraftType = tempSegment.getAirCraftType();
								if(!OneAConstants.EQUIPMENT_TRN.equals(airCraftType) && 
										!OneAConstants.EQUIPMENT_LCH.equals(airCraftType) && 
										!OneAConstants.EQUIPMENT_BUS.equals(airCraftType) &&
										!tempSegment.isFlown() &&
										FlightStatusEnum.CANCELLED.equals(tempSegment.getSegmentStatus().getStatus())
										) {
									buildXLMR(xlwrInfos, currentPassengerId, tempSegmentId, companyId);
								}
							}
						}
						needAddXLMR = false;
					}else if(segmentId.equals(currentSegmentId)) {
						if (!seatDetail.isExlSeat()) {
							// if user has re-selected normal seat instead of free EXL seat for top tier
							// member, remove the SK XLWR element from product level upon save
							qulifierList.add(sk.getQulifierId().toString());
						}
						needAddXLMR = false;
					}
				}
			}
			
			if(seatDetail.isExlSeat() && needAddXLMR) {
				buildXLMR(xlwrInfos, currentPassengerId, currentSegmentId, companyId);
			}
		});
		
		if(!CollectionUtils.isEmpty(qulifierList)) {
			otListMap.put(CommonConstants.MAP_SEAT_KEY, new ArrayList<String>(new HashSet<String>(qulifierList)));
		}
		return otListMap;
	}

	private boolean hasIncludedSKList(List<RetrievePnrDataElements> skList, String skType, String segmentId, String passengerId) {
		if (CollectionUtils.isEmpty(skList) || skType == null || segmentId == null || passengerId == null) {
			return false;
		}
		return skList.stream().anyMatch(sk -> sk != null && skType.equals(sk.getType()) && segmentId.equals(sk.getSegmentId()) && passengerId.equals(sk.getPassengerId()));
	}
	
	/**
	 * build ASWR
	 * @param skList
	 * @param aswrInfos
	 * @param currentPassengerId
	 * @param currentSegmentId
	 * @param companyId
	 */
	private void buildASWR(List<RetrievePnrDataElements> skList, List<AswrInfo> aswrInfos, String currentPassengerId,
			String currentSegmentId, String companyId) {
		// check whether the aswrInfos is null or not
		if (aswrInfos != null) {
			// check whether there exists a ASWR for the specific passengerId under the specific segment
			if (Optional.ofNullable(aswrInfos).orElse(new ArrayList<AswrInfo>()).stream()
					.anyMatch(info -> info != null && info.getSegmentId().equals(currentSegmentId) && info.getPassengerId().equals(currentPassengerId))) {
				return;
			}
			AswrInfo aswrInfo = new AswrInfo();
			aswrInfo.setCompanyId(companyId);
			aswrInfo.setPassengerId(currentPassengerId);
			aswrInfo.setSegmentId(currentSegmentId);
			aswrInfos.add(aswrInfo);
		}
	}

	/**
	 * 
	 * @param requestDTO
	 * @param passengerId
	 * @param segmentId
	 */
	private void buildXLMR(List<XlwrInfo> xlwrInfos, String passengerId, String segmentId, String companyId) {
		XlwrInfo xlmrInfo = new XlwrInfo();
		xlmrInfo.setPassengerId(passengerId);
		xlmrInfo.setSegmentId(segmentId);
		xlmrInfo.setCompanyId(companyId);
		xlwrInfos.add(xlmrInfo);
	}
	
	/**
	 * 
	 * @param requestDTO
	 * @param passengerId
	 * @param segmentId
	 */
	private void buildRemark(List<RemarkInfo> remarkInfos, String passengerId, String segmentId) {
		RemarkInfo remarkInfo = new RemarkInfo();
		remarkInfo.setPassengerId(passengerId);
		remarkInfo.setSegmentId(segmentId);
		remarkInfos.add(remarkInfo);
	}
	
	private boolean hasProductLevelXlmr(List<RetrievePnrDataElements> skList, String passengerId, String segmentId) {
		for(RetrievePnrDataElements sk:skList) {
			String tempPassengerId = sk.getPassengerId();
			String tempSegmentId = sk.getSegmentId();
			if(passengerId.equals(tempPassengerId) && segmentId.equals(tempSegmentId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if a passenger is top tier member
	 * 
	 * @param pnrFQTVInfo
	 * @param fqtvInfo
	 */
	private boolean isTopTier(PassengerSegment passengerSegment) {
		FQTVInfo pnrFQTVInfo = passengerSegment.getFqtvInfo();
		if(pnrFQTVInfo != null){
			Boolean isTopTier = pnrFQTVInfo.isTopTier();
			return isTopTier == null ? false : isTopTier;
		}
		return false;
	}
	
	/**
	 * retrieve seat map
	 * @param segment
	 * @return
	 */
	private RetrieveSeatMapDTO retrieveSeatMap(Segment segment, String mmbToken, String rloc) {
		String departureDate = DateUtil.convertDateFormat(segment.getDepartureTime().getPnrTime(), DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATE_PATTERN_DDMMYY);
		String originAirportCode = segment.getOriginPort();
		String destinationAirportCode = segment.getDestPort();
		String operatingCompany = segment.getOperateCompany();
		String flightNum = segment.getOperateSegmentNumber();
		String bookingClass = segment.getSubClass();
		
		SeatMapRequestDTO requestDTO = new SeatMapRequestDTO();
		requestDTO.setRloc(rloc);
		SegmentInfo segmentInfo = new SegmentInfo();
		segmentInfo.setBookingClass(bookingClass);
		segmentInfo.setDepartureDate(departureDate);
		segmentInfo.setDestinationAirportCode(destinationAirportCode);
		segmentInfo.setFlightNum(flightNum);
		segmentInfo.setMarketingCompany(operatingCompany);
		segmentInfo.setOriginAirportCode(originAirportCode);
		requestDTO.setSegmentInfo(segmentInfo);
		
        return seatMapService.retrieveSeatMap(mmbToken, requestDTO);
	}
	
	/**
	 * check if the seat is EXL/ASR seat, and if it is already occupied or ineligible to select
	 * @param requestDTO
	 * @param seatMapDTO
	 * @param booking 
	 */
	private void checkSeatTypeAndEligibility(UpdateSeatRequestDTO requestDTO, RetrieveSeatMapDTO seatMapDTO, Map<String, List<String>> otListMap, Booking booking) {
		List<String> ineligibleList = new ArrayList<>();
		//build seat OT number to delete
		Optional.ofNullable(requestDTO.getPaxSeatDetails()).orElseGet(Collections::emptyList).forEach(detail->{
			String seatNo = detail.getSeatNo();
			int rowNumber = -1;
			String columnCode = null;
			Pattern pattern = Pattern.compile("(\\d+)([A-Z]+)");
			Matcher matcher = pattern.matcher(seatNo);
			if(matcher.matches()) {
				rowNumber = Integer.parseInt(matcher.group(1));
				columnCode = matcher.group(2);
			}
			BigDecimal seatPrice = getSeatPriceAmount(rowNumber, columnCode, detail.getPassengerID(), seatMapDTO);
			if(rowNumber != -1 && !StringUtils.isEmpty(columnCode) && seatMapDTO != null && seatMapDTO.getSeatMap() != null) {
				SeatMapDTO seatMap = seatMapDTO.getSeatMap().get(0);
				List<GridRowDTO> rows = seatMap.getRows();
				for(GridRowDTO row:rows) {
					int rowNum = row.getRowNum();
					if(rowNumber != rowNum) continue;
					
					List<GridDTO> grids = row.getGrids();
					for(GridDTO grid:grids) {
						String colCode = grid.getColumn();
						if(!columnCode.equals(colCode)) continue;
						
						SeatDetailsDTO seatDetail = grid.getSeatDetails();
						if(seatDetail == null) continue;
						if(seatDetail != null) {
							SeatCharacteristicDTO characteristic = seatDetail.getSeatCharacteristic();
							if(characteristic != null) {
								boolean exlSeat = characteristic.isIsLegSpace();
								detail.setExlSeat(exlSeat);
								detail.setAsrSeat(characteristic.isIsAsrSeat());
							}
						}
					}
				}
			}
			checkUpdateEligibility(requestDTO, booking, ineligibleList, detail, seatNo, seatPrice);
		});
		
		if(!ineligibleList.isEmpty()) {
			otListMap.put(MMBBizruleConstants.UPDATE_SEAT_REJECTED_INELIGIBLE, ineligibleList);
		}
	}

	/**
	 * check  pax eligibility for seat update
	 * @param requestDTO
	 * @param booking
	 * @param ineligibleList
	 * @param detail
	 * @param seatNo
	 * @param seatPrice
	 */
	private void checkUpdateEligibility(UpdateSeatRequestDTO requestDTO, Booking booking, List<String> ineligibleList,
			PaxSeatDetail detail, String seatNo, BigDecimal seatPrice) {
		// TODO add AEP validation since it is not necessary now because when AEP timeout, pax can still do free seat selection
		PassengerSegment passengerSegment = booking == null ? null : booking.getPassengerSegments().stream()
				.filter(tempPassengerSegment -> requestDTO.getSegmentId().equals(tempPassengerSegment.getSegmentId())
						&& detail.getPassengerID().equals(tempPassengerSegment.getPassengerId()))
				.findFirst().orElse(null);
		
		if(passengerSegment == null || passengerSegment.getMmbSeatSelection() == null) {
			ineligibleList.add(seatNo);
			return;
		}
		SeatSelection seatSelection = passengerSegment.getMmbSeatSelection();
		// if pax is ineligible to update seat
		if(!BooleanUtils.isTrue(seatSelection.isEligible()) 
				|| (detail.isExlSeat() && (seatSelection.getSpecialSeatEligibility() == null || !BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getExlSeat())))
				|| (detail.isAsrSeat() && (seatSelection.getSpecialSeatEligibility() == null || !BooleanUtils.isTrue(seatSelection.getSpecialSeatEligibility().getAsrSeat())))) {
			ineligibleList.add(seatNo);
			return;
		} 
		
		//TODO ignore seat check from seat map because MMB only check pnr rule in current design. defect OLSSMMB-16637, need future check this handling with BA
		//boolean isTopTier = passengerSegment.getFqtvInfo() != null && BooleanUtils.isTrue(passengerSegment.getFqtvInfo().isTopTier());		
		//checkEligibilityOfTheSeat(ineligibleList, detail, seatNo, seatPrice, seatSelection, isTopTier, passengerSegment.getSeat());
	}

	/**
	 * check if the pax is eligible to update the seat
	 * @param ineligibleList
	 * @param detail
	 * @param seatNo
	 * @param seatPrice
	 * @param seatSelection
	 * @param isTopTier
	 * @param seatDetail 
	 * @param exlFOC
	 */
	private void checkEligibilityOfTheSeat(List<String> ineligibleList, PaxSeatDetail detail, String seatNo,
			BigDecimal seatPrice, SeatSelection seatSelection, boolean isTopTier, SeatDetail seatDetail) {
		if ((detail.isExlSeat()
				&& (!BooleanUtils.isTrue(seatSelection.isXlFOC()) || (seatPrice != null && seatPrice.compareTo(BigDecimal.ZERO) != 0))
				&& (seatDetail == null || !BooleanUtils.isTrue(seatDetail.isExlSeat()) || !BooleanUtils.isTrue(seatDetail.isPaid()))) // to update EXL seat, pax must be exlFOC and seat price must be 0 or pax must have a paid EXL seat
				|| (detail.isAsrSeat()
						&& (!seatSelection.isAsrFOC() || (seatPrice != null && seatPrice.compareTo(BigDecimal.ZERO) != 0))
						&& (seatDetail == null || !BooleanUtils.isTrue(seatDetail.isPaid()))) // to update ASR seat, pax must be asrFOC and seat price must be 0 or pax must have a paid seat
				|| (!detail.isExlSeat() && !detail.isAsrSeat() && BooleanUtils.isTrue(seatSelection.isLowRBD() && !seatSelection.isAsrFOC())
						&& !isTopTier)) { // to update other normal seat, pax must be high RBD or asrFOC
			ineligibleList.add(seatNo);
		} 
	}
	
	/**
	 * get seat price from seatMap
	 * @param rowNumber
	 * @param columnCode
	 * @param string 
	 * @param seatMapDTO
	 * @return BigDecimal
	 */
	private BigDecimal getSeatPriceAmount(int rowNumber, String columnCode, String passengerId, RetrieveSeatMapDTO seatMapDTO) {
		if (rowNumber < 0 || StringUtils.isEmpty(columnCode) || StringUtils.isEmpty(passengerId) || seatMapDTO == null
				|| CollectionUtils.isEmpty(seatMapDTO.getSeatMap()) || seatMapDTO.getSeatMap().get(0) == null
				|| seatMapDTO.getSeatMap().get(0).getCustomerDatas() == null) {
			return null;
		}
		
		SeatMapDTO seatMap = seatMapDTO.getSeatMap().get(0);
		// get seat price
		return seatMap.getCustomerDatas().stream()
				// filter the cusData associated to current passenger
				.filter(cusData -> cusData != null && !CollectionUtils.isEmpty(cusData.getPassengerIds())
						&& cusData.getPassengerIds().contains(passengerId)
						&& !CollectionUtils.isEmpty(cusData.getSeatPrices()))
				// get price of current seat from seatPrices
				.map(cusData -> cusData.getSeatPrices().stream()
						// filter the seatPrice of current seat
						.filter(seatPrice -> seatPrice != null && seatPrice.getPrice() != null
								&& seatPrice.getPrice().getAmount() != null
								&& !CollectionUtils.isEmpty(seatPrice.getRowDetails())
								// match the seat by row number and column number
								&& seatPrice.getRowDetails().stream()
										.anyMatch(row -> row != null && row.getRowNumber() != null
												&& !CollectionUtils.isEmpty(row.getColumns())
												&& row.getRowNumber().equals(new BigInteger(String.valueOf(rowNumber)))
												&& row.getColumns().contains(columnCode)))
						.map(seatPrice -> seatPrice.getPrice().getAmount()).findFirst().orElse(null))
				.filter(amount -> amount != null)
				.findFirst().orElse(null);			
	}

	/**
	 * 
	 * @Description get company id by segment id
	 * @param
	 * @return boolean
	 * @author fengfeng.jiang
	 */
	private String getCompanyIdBySt(List<Segment> segments, String segmentId) {
		String companyId = "";
		if(segments != null) {
			for(Segment segment:segments) {
				if(segmentId.equals(segment.getSegmentID())) {
					companyId = segment.getMarketCompany();
					break;
				}
			}
		}
		
		return companyId;
	}
	
	/**
	 * when seat is over-deleted, add seat back
	 * @param requestDTO
	 * @param booking
	 */
	private void processOverDeletedSeat(UpdateSeatRequestDTO requestDTO, Booking booking) {
		String currentSegmentId = requestDTO.getSegmentId();
		
		List<PaxSeatDetail> paxSeatDetailList = new ArrayList<>();
		Optional.ofNullable(requestDTO.getPaxSeatDetails()).orElseGet(Collections::emptyList).forEach(seatDetail->{
			String currentPassengerId = seatDetail.getPassengerID();
			PassengerSegment passengerSegment = booking.getPassengerSegments().stream().filter(tempPassengerSegment -> currentSegmentId.equals(tempPassengerSegment.getSegmentId()) && currentPassengerId.equals(tempPassengerSegment.getPassengerId())).findFirst().orElse(null);
			if(passengerSegment != null) {
				//check whether other passenger share the same RQST but not updated
				List<PassengerSegment> psList = getAddBackList(requestDTO, booking, passengerSegment);
				for(PassengerSegment ps:psList) {
					PaxSeatDetail paxSeatDetail = new PaxSeatDetail();
					paxSeatDetail.setSeatNo(ps.getSeat().getSeatNo());
					paxSeatDetail.setPassengerID(ps.getPassengerId());
					
					// populate the extra seats
					if(!CollectionUtils.isEmpty(ps.getExtraSeats())) {
						ps.getExtraSeats().stream().filter(extraSeat -> !StringUtils.isEmpty(extraSeat.getSeatNo()) && !StringUtils.isEmpty(extraSeat.getCrossRef()))
						.forEach(extraSeat -> {
							ExtraSeatDetail extraSeatDetail = new ExtraSeatDetail();
							extraSeatDetail.setSeatNo(extraSeat.getSeatNo());
							extraSeatDetail.setPassengerId(extraSeat.getCrossRef());
							paxSeatDetail.findExtraSeats().add(extraSeatDetail);
						});
					}
					
					paxSeatDetailList.add(paxSeatDetail);
				}
			}
		});
		
		requestDTO.getPaxSeatDetails().addAll(paxSeatDetailList);
	}

	/**
	 * check whether other passenger share the same RQST but not updated
	 * 
	 * @param requestDTO
	 * @param booking
	 * @param qulifierId
	 * @return
	 */
	private List<PassengerSegment> getAddBackList(UpdateSeatRequestDTO requestDTO, Booking booking, PassengerSegment passengerSegment) {
		List<PassengerSegment> psList = new ArrayList<PassengerSegment>();
		
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
		if(CollectionUtils.isEmpty(passengerSegments)) {
			return psList;
		}
		
		List<String> updatedPts = requestDTO.getPaxSeatDetails().stream().map(seatDetail -> seatDetail.getPassengerID()).collect(Collectors.toList());
		String pt = passengerSegment.getPassengerId();
		String st = passengerSegment.getSegmentId();
		BigInteger qulifierId = passengerSegment.getSeatQulifierId();
		if(qulifierId == null) {
			return psList;
		}
		
		for(PassengerSegment ps:passengerSegments) {
			String tempPt = ps.getPassengerId();
			String tempSt = ps.getSegmentId();
			String tempQulifierId = ps.getSeatQulifierId()==null?null:ps.getSeatQulifierId().toString();
			
			if(st.equals(tempSt)) {
				if(pt.equals(tempPt) || updatedPts.contains(tempPt) || !qulifierId.toString().equals(tempQulifierId)) {
					continue;
				}
				
				psList.add(ps);
			}
		}
		
		return psList;
	}
}
