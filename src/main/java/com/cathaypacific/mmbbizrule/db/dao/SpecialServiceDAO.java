package com.cathaypacific.mmbbizrule.db.dao;

import com.cathaypacific.mmbbizrule.db.model.SpecialServiceKey;
import com.cathaypacific.mmbbizrule.db.model.SpecialServiceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SpecialServiceDAO extends CrudRepository<SpecialServiceModel,SpecialServiceKey> {

     @Query(value = "SELECT t.* FROM tb_reminder_code t WHERE t.app_code=:appCode AND t.action = :FUNCTION_NAME ORDER BY t.reminder_code", nativeQuery = true)
     List<SpecialServiceModel> findAllByAppCodeAndAction(@Param("appCode") String appCode,@Param("FUNCTION_NAME") String functionName);

}
