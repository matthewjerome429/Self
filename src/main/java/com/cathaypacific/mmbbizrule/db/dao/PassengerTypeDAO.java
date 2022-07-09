package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.PassengerType;
import com.cathaypacific.mmbbizrule.db.model.PassengerTypeKey;

public interface PassengerTypeDAO extends CrudRepository<PassengerType, PassengerTypeKey>{
	
	@Query(nativeQuery=true, value="select passenger_type FROM tb_passenger_type as a "
			+ "where a.app_code = :appCode AND a.pnr_passenger_type = :pnrPassengerType")
	public String getPaxType(@Param("appCode") String appCode, @Param("pnrPassengerType") String pnrPassengerType);
}
