package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassCheck;
import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassCheckKey;

public interface RedemptionSubclassCheckDAO extends CrudRepository<RedemptionSubclassCheck,RedemptionSubclassCheckKey>{

    @Query(nativeQuery = true, value = "select * FROM tb_redemption_subclass_check where :appCode like replace(app_code,'*','%')")
    public List<RedemptionSubclassCheck> findByAppCode(@Param("appCode") String appCode);
    
    @Query(nativeQuery = true, value = "select * FROM tb_redemption_subclass_check where :appCode like replace(app_code,'*','%') and operate_company=:operateCompany")
    public List<RedemptionSubclassCheck> findByAppCodeAndOperateCompany(@Param("appCode") String appCode, @Param("operateCompany") String operateCompany);
    
    @Query(nativeQuery = true, value = "select * FROM tb_redemption_subclass_check where :appCode like replace(app_code,'*','%') and operate_company=:operateCompany and subclass=:subclass")
    public List<RedemptionSubclassCheck> findByAppCodeAndOperateCompanyAndSubclass(@Param("appCode") String appCode, @Param("operateCompany") String operateCompany, @Param("subclass") String subclass);
	
}
