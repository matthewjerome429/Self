package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.AsrEnableCheck;
import com.cathaypacific.mmbbizrule.db.model.AsrEnableCheckKey;

public interface ASREnableCheckDAO extends CrudRepository<AsrEnableCheck, AsrEnableCheckKey>{

	@Query(nativeQuery=true, value="select * FROM tb_redemption_asr_check as a "
			+ "where (a.office_id = :officeId or a.office_id = '*') AND (a.tpos in ( :tpos ) or a.tpos = '*') "
			+ "AND a.app_code = :appCode AND a.seat_selection = '1' "
			+ "order by a.tpos, a.office_id desc")
	public List<AsrEnableCheck> getASREnableCheck(@Param("appCode") String appCode, @Param("officeId") String officeId, @Param("tpos") String tpos);	

	@Query(nativeQuery=true, value="select * FROM tb_redemption_asr_check as a "
			+ "where a.office_id = :officeId "
			+ "AND a.app_code = :appCode AND a.seat_selection = '1' "
			+ "order by a.office_id desc")
	public List<AsrEnableCheck> getASREnableCheckByOfficeId(@Param("appCode") String appCode, @Param("officeId") String officeId);
}
