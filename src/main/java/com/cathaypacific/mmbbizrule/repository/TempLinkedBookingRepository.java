package com.cathaypacific.mmbbizrule.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.enums.booking.BookingSources;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;

@Repository
public class TempLinkedBookingRepository {
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	/**
	 * add the external booking to the temp list
	 * @return
	 */
	public void  addExternalBooking(String rloc, String familyName, String givenName,String primaryPassengerId,String mmbToken){
		
		TempLinkedBooking tempAssociatedBooking =  new TempLinkedBooking();
		tempAssociatedBooking.setRloc( rloc);
		tempAssociatedBooking.setFamilyName(familyName);
		tempAssociatedBooking.setGivenName(givenName);
		tempAssociatedBooking.setPrimaryPassengerId(primaryPassengerId);
		tempAssociatedBooking.setBookingSources(BookingSources.EXTERNAL_LINK);
		this.addLinkedBookingToCache(tempAssociatedBooking, mmbToken);
	}
	 
	/**
	 * add EODS booking to the temp list
	 * 
	 * @param oneARloc
	 * @param familyName
	 * @param givenName
	 * @param primaryPassengerId
	 * @param mmbToken
	 */
	public void addEodsBooking(String oneARloc, String familyName, String givenName, String primaryPassengerId, String mmbToken){
		TempLinkedBooking tempAssociatedBooking = new TempLinkedBooking();
		tempAssociatedBooking.setRloc(oneARloc);
		tempAssociatedBooking.setFamilyName(familyName);
		tempAssociatedBooking.setGivenName(givenName);
		tempAssociatedBooking.setPrimaryPassengerId(primaryPassengerId);
		tempAssociatedBooking.setBookingSources(BookingSources.ADD_EODS_BOOKING_TEMP_LINK);
		this.addLinkedBookingToCache(tempAssociatedBooking, mmbToken);
	}
	
	/**
	 * enrol Ru account and add current booking to the temp list
	 * 
	 * @param oneARloc
	 * @param familyName
	 * @param givenName
	 * @param primaryPassengerId
	 * @param mmbToken
	 */
	public void addRuEnrolBooking(String oneARloc, String familyName, String givenName, String primaryPassengerId, String mmbToken){
		TempLinkedBooking tempAssociatedBooking = new TempLinkedBooking();
		tempAssociatedBooking.setRloc(oneARloc);
		tempAssociatedBooking.setFamilyName(familyName);
		tempAssociatedBooking.setGivenName(givenName);
		tempAssociatedBooking.setPrimaryPassengerId(primaryPassengerId);
		tempAssociatedBooking.setBookingSources(BookingSources.RU_ENROL);
		this.addLinkedBookingToCache(tempAssociatedBooking, mmbToken);
	}
	
	/**
	 * add update FFP booking to the temp list
	 * 
	 * @param oneARloc
	 * @param familyName
	 * @param givenName
	 * @param primaryPassengerId
	 * @param mmbToken
	 */
	public void addUpdateFfpBooking(String oneARloc, String familyName, String givenName, String primaryPassengerId, String mmbToken){
		TempLinkedBooking tempAssociatedBooking =  new TempLinkedBooking();
		tempAssociatedBooking.setRloc(oneARloc);
		tempAssociatedBooking.setFamilyName(familyName);
		tempAssociatedBooking.setGivenName(givenName);
		tempAssociatedBooking.setPrimaryPassengerId(primaryPassengerId);
		tempAssociatedBooking.setBookingSources(BookingSources.EXTERNAL_LINK);
		this.addLinkedBookingToCache(tempAssociatedBooking, mmbToken);
	}
	
	/**
	 * add to linked booking list
	 * @param tempAssociatedBooking
	 * @param mmbToken
	 */
	public void addLinkedBookingToCache(TempLinkedBooking tempAssociatedBooking, String mmbToken){
		@SuppressWarnings("unchecked")
		List<TempLinkedBooking> cachedBookings = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.LIKEND_BOOKING, null, ArrayList.class);
		cachedBookings = cachedBookings == null ? new ArrayList<>() : cachedBookings;
		//check exit
		if(cachedBookings.stream().noneMatch(tempBooking->Objects.equals(tempBooking.getRloc(), tempAssociatedBooking.getRloc()))){
			cachedBookings.add(tempAssociatedBooking);
			mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.LIKEND_BOOKING, null, cachedBookings);		
			addRlocToValidFlightRlocs(tempAssociatedBooking.getRloc(), mmbToken);
		}
		
	}
	
	/**
	 * add to linked booking list
	 * @param tempAssociatedBooking
	 * @param mmbToken
	 */
	public void addLinkedBookingsToCache(List<TempLinkedBooking> tempAssociatedBookings, String mmbToken){
		if (CollectionUtils.isEmpty(tempAssociatedBookings)) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<TempLinkedBooking> cachedBookings = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.LIKEND_BOOKING, null, ArrayList.class);
		cachedBookings = cachedBookings == null ? new ArrayList<>() : cachedBookings;
		List<String> rlocs = new ArrayList<>();
		
		for (TempLinkedBooking tempAssociatedBooking : tempAssociatedBookings) {
			//check exit
			if(cachedBookings.stream().noneMatch(tempBooking->Objects.equals(tempBooking.getRloc(), tempAssociatedBooking.getRloc()))){
				cachedBookings.add(tempAssociatedBooking);
				rlocs.add(tempAssociatedBooking.getRloc());
			}
		}
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.LIKEND_BOOKING, null, cachedBookings);		
		addRlocsToValidFlightRlocs(rlocs, mmbToken);
	}
	
	/**
	 * add rloc into valid_flight_rlocs in redis
	 * @param rloc
	 * @param mmbToken
	 */
	private void addRlocToValidFlightRlocs(String rloc, String mmbToken) {
		@SuppressWarnings("unchecked")
		ArrayList<String> cachedRlocs = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, ArrayList.class);
		cachedRlocs = cachedRlocs == null ?  new ArrayList<>() : cachedRlocs;
		cachedRlocs.add(rloc);
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, cachedRlocs);
	}
	
	/**
	 * add rloc into valid_flight_rlocs in redis
	 * @param rloc
	 * @param mmbToken
	 */
	private void addRlocsToValidFlightRlocs(List<String> rlocs, String mmbToken) {
		if (CollectionUtils.isEmpty(rlocs)) {
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> cachedRlocs = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, ArrayList.class);
		cachedRlocs = cachedRlocs == null ?  new ArrayList<>() : cachedRlocs;
		
		for (String rloc : rlocs) {
			if (!cachedRlocs.contains(rloc)) {
				cachedRlocs.add(rloc);
			}
		}
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, cachedRlocs);
	}
	
	/**
	 * get Linked bookings 
	 * @param mmbToken
	 * @return
	 */
	public List<TempLinkedBooking> getLinkedBookings(String mmbToken){
		@SuppressWarnings("unchecked")
		List<TempLinkedBooking> cachedBookings = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.LIKEND_BOOKING, null, ArrayList.class);
		 	
		return cachedBookings != null ? cachedBookings:Collections.emptyList();
	}
	
}
