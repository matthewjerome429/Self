package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.Repository;

import com.cathaypacific.mmbbizrule.db.model.SeatRuleKey;
import com.cathaypacific.mmbbizrule.db.model.SeatRuleModel;

public interface SeatRuleDao extends Repository<SeatRuleModel, SeatRuleKey> {

	public Stream<SeatRuleModel> findAllByAppCodeAndTypeAndSeatSelect(String appCode, String type, String seatSelect);
	
	public List<SeatRuleModel> findAllByAppCodeAndAsrFOC(String appCode,String asrFOC);
	
	public List<SeatRuleModel> findAllByAppCodeAndTypeAndLowRBD(String appCode, String type, String lowRBD);
}
