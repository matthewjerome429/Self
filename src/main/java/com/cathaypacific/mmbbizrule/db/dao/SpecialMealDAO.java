package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.SpecialMealKey;
import com.cathaypacific.mmbbizrule.db.model.SpecialMealModel;

public interface SpecialMealDAO extends CrudRepository<SpecialMealModel, SpecialMealKey> {

	/*
	 * Fix: OLSSMMB-13416
	 * This is to query meal type with wildcard support that the wildcard has the lowest priority.
	 * Main logic: Select and sort all records. Then only take the first row of each meal type group which is enabled.
	 */
	@Query(nativeQuery=true, value="SELECT f.meal_type FROM (" +
			" SELECT r.* FROM (" + 
			" SELECT s.*, (@row_number\\:=@row_number + 1) AS row_number " + 
			" FROM tb_special_meal_rule AS s, (SELECT @row_number\\:=0) r " + 
			" WHERE (s.carrier_code = :carrierCode OR s.carrier_code = '*') " + 
			" AND (s.cabin_class = :cabinClass OR s.cabin_class = '*') " + 
			" AND (s.origin = :origin OR s.origin = '*') " + 
			" AND (s.destination = :destination OR s.destination = '*') " + 
			" AND s.app_code = :appCode " + 
			" ORDER BY meal_type DESC, carrier_code DESC, cabin_class DESC, origin DESC, destination DESC) AS r " + 
			" GROUP BY r.meal_type " + 
			" HAVING r.enable = TRUE) AS f")
	public List<String> getMealList(@Param("appCode") String appCode, @Param("carrierCode") String carrierCode,
			@Param("cabinClass") String cabinClass, @Param("origin") String origin, @Param("destination") String destination);
	
	@Query(nativeQuery=true, value="SELECT f.meal_type as type, f.consent as consent FROM (" +
			" SELECT r.* FROM (" + 
			" SELECT s.*, (@row_number\\:=@row_number + 1) AS row_number " + 
			" FROM tb_special_meal_rule AS s, (SELECT @row_number\\:=0) r " + 
			" WHERE (s.carrier_code = :carrierCode OR s.carrier_code = '*') " + 
			" AND (s.cabin_class = :cabinClass OR s.cabin_class = '*') " + 
			" AND (s.origin = :origin OR s.origin = '*') " + 
			" AND (s.destination = :destination OR s.destination = '*') " + 
			" AND s.app_code = :appCode " + 
			" ORDER BY meal_type DESC, carrier_code DESC, cabin_class DESC, origin DESC, destination DESC) AS r " + 
			" GROUP BY r.meal_type " + 
			" HAVING r.enable = TRUE) AS f")
	public List<Object[]> getMeals(@Param("appCode") String appCode, @Param("carrierCode") String carrierCode,
			@Param("cabinClass") String cabinClass, @Param("origin") String origin, @Param("destination") String destination);

	@Query(nativeQuery=true, value="SELECT DISTINCT meal_type FROM tb_special_meal_rule WHERE app_code = :appCode")
	public List<String> findMealListByAppCode(@Param("appCode") String appCode);
}
