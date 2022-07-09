package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.TbFlightHaul;
import com.cathaypacific.mmbbizrule.db.model.TbFlightHaulKey;

public interface TbFlightHaulDAO extends JpaRepository<TbFlightHaul, TbFlightHaulKey>{
	
	@Query(nativeQuery = true, value = "select haul_type from tb_flight_haul t1 where :appCode like REPLACE(t1.app_code, '*', '%')"
			+ " and (t1.apt_code_one = :aptCodeOne and t1.apt_code_two = :aptCodeTwo or t1.apt_code_one = :aptCodeTwo and t1.apt_code_two = :aptCodeOne)"
			+ " and (t1.opt_cx = :optCx and :optCx = 'Y' or t1.opt_ka = :optKa and :optKa = 'Y') LIMIT 0,1")
	public String findHaulType(@Param("appCode") String appCode,@Param("aptCodeOne") String aptCodeOne, @Param("aptCodeTwo") String aptCodeTwo, @Param("optCx") String optCx, @Param("optKa") String optKa);

}
