package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.NonAirSegment;
import com.cathaypacific.mmbbizrule.db.model.NonAirSegmentKey;

	public interface NonAirSegmentDao extends JpaRepository<NonAirSegment, NonAirSegmentKey> {
		
		@Query(nativeQuery = true, value="select type from tb_nonair_segment t1 where "
				+ " :appCode like  REPLACE(t1.app_code, '*', '%')"
				+ " and :origin like REPLACE(t1.origin, '*', '%')"
                + " and :destination like REPLACE(t1.destination, '*', '%')"
                + " and  :airlineCode like REPLACE(t1.airline_code, '*', '%')"
                + " ORDER BY t1.ORIGIN DESC,t1.destination DESC,t1.airline_code DESC LIMIT 0,1")
		public String findNonAirSegmentType(@Param("appCode") String appCode,@Param("origin") String origin, @Param("destination") String destination, @Param("airlineCode") String airlineCode);

		@Query(nativeQuery = true, value="select type from tb_nonair_segment t1 where "
				+ " :appCode like  REPLACE(t1.app_code, '*', '%')"
				+ " and :origin like REPLACE(t1.origin, '*', '%')"
                + " and :destination like REPLACE(t1.destination, '*', '%')"
                + " and  :airlineCode like REPLACE(t1.airline_code, '*', '%')"
                + " ORDER BY t1.ORIGIN DESC,t1.destination DESC,t1.airline_code DESC LIMIT 0,1")
		@Cacheable(cacheNames="tb:tb_nonair_segment",keyGenerator = "shareKeyGenerator")
		public String findNonAirSegmentTypeShareKeyGeneratorCache(@Param("appCode") String appCode,@Param("origin") String origin, @Param("destination") String destination, @Param("airlineCode") String airlineCode);

		@Query(nativeQuery = true, value="select type from tb_nonair_segment t1 where "
				+ " :appCode like  REPLACE(t1.app_code, '*', '%')"
				+ " and :origin like REPLACE(t1.origin, '*', '%')"
                + " and :destination like REPLACE(t1.destination, '*', '%')"
                + " and  :airlineCode like REPLACE(t1.airline_code, '*', '%')"
                + " ORDER BY t1.ORIGIN DESC,t1.destination DESC,t1.airline_code DESC LIMIT 0,1")
		@Cacheable(cacheNames="tb:tb_nonair_segment",keyGenerator = "shareKeyGenerator")
		public String findNonAirSegmentTypeCache(@Param("appCode") String appCode,@Param("origin") String origin, @Param("destination") String destination, @Param("airlineCode") String airlineCode);

	}
