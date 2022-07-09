package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mmbbizrule.db.model.TbPortFlight;
import com.cathaypacific.mmbbizrule.db.model.TbPortFlightKey;

@Repository
public interface TbPortFlightDAO extends JpaRepository<TbPortFlight, TbPortFlightKey> {

	List<TbPortFlight> findByAppCode(String appCode);
	
}