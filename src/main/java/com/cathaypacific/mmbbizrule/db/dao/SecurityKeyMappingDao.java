package com.cathaypacific.mmbbizrule.db.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cathaypacific.mmbbizrule.db.model.SecurityKeyMapping;
import com.cathaypacific.mmbbizrule.db.model.SecurityKeyMappingKey;

public interface SecurityKeyMappingDao extends JpaRepository<SecurityKeyMapping, SecurityKeyMappingKey> {

	Optional<SecurityKeyMapping> findOneByAppCodeAndChannelName(String appCode, String channelName);
}
