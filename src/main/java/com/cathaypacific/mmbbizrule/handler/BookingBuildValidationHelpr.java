package com.cathaypacific.mmbbizrule.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;

@Component
public class BookingBuildValidationHelpr {

	private static LogAgent logger = LogAgent.getLogAgent(BookingBuildValidationHelpr.class);
	
	@Autowired
	private DeletePnrService deletePnrService;

	/**
	 * remove fqtv for id booking async
	 * @param pnrBooking
	 * @throws BusinessBaseException
	 */
	public void removeAllFqtvForIDBooking(RetrievePnrBooking pnrBooking){
		if(!pnrBooking.isIDBooking()){
			return;
		}
		 Map<String, List<String>> fqtvMap = getFqtvOtMap(pnrBooking);
		 if(fqtvMap!=null && !fqtvMap.isEmpty()){
			 logger.warn("Found FQTV(s) in ID booking, will remove it.");
			 //remove fqtv Directly and assume 1A delete succell
			 removeFqtvFromPnrDirectly(pnrBooking);
			 //async to delete fqtv
			 deletePnrService.asyncDeletePnrWithoutParser(pnrBooking.getOneARloc(), fqtvMap, null);
		 }
			
	}
	/**
	 * remove fqtv info from pnr
	 * @param pnrBooking
	 */
	private void removeFqtvFromPnrDirectly(RetrievePnrBooking pnrBooking){
		//check passenger level fqtv
		Optional.ofNullable(pnrBooking.getPassengers()).orElseGet(Collections::emptyList).stream().forEach(p -> p.setFQTVInfos(null));
		//check passenger segment level fqtv
		Optional.ofNullable(pnrBooking.getPassengerSegments()).orElseGet(Collections::emptyList).stream().forEach(ps -> ps.setFQTVInfos(null));
		//check ssr
		List<RetrievePnrDataElements> ssrList = Optional.ofNullable(pnrBooking.getSsrList()).orElseGet(Collections::emptyList);
		//ssrList.iterator().forEachRemaining(action);
		Iterator<RetrievePnrDataElements> ssrIterator = ssrList.iterator();
		while(ssrIterator.hasNext()){
			RetrievePnrDataElements ssr = ssrIterator.next();
			if(OneAConstants.SSR_TYPE_FQTV.equals(ssr.getType())){
				ssrIterator.remove();
			}
		}
	}
	
	/**
	 * get map of OT numbers which need to be deleted in 1A
	 * @param pnrBooking
	 * @return Map<String, List<String>>
	 */
	private Map<String, List<String>> getFqtvOtMap(RetrievePnrBooking pnrBooking) {
		List<RetrievePnrFFPInfo> fqtvs = new ArrayList<>();
		Optional.ofNullable(pnrBooking.getPassengers()).orElseGet(Collections::emptyList).stream().forEach(p -> fqtvs.addAll(p.getFQTVInfos()));
		Optional.ofNullable(pnrBooking.getPassengerSegments()).orElseGet(Collections::emptyList).stream().forEach(ps -> fqtvs.addAll(ps.getFQTVInfos()));
		List<String> otNumbers = fqtvs.stream().map(fqtv -> String.valueOf(fqtv.getQualifierId())).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(otNumbers)) {
			return null;
		}
		
		logger.debug(String.format("FQTV OT numbers need to deleted: %s", otNumbers.toString()));
		Map<String, List<String>> map = new HashMap<>();
		map.put(OneAConstants.OT_QUALIFIER, otNumbers);
		return map;		
	}

}
