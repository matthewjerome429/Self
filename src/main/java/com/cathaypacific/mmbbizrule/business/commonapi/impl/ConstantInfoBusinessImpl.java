package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.business.commonapi.ConstantInfoBusiness;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
@Service
public class ConstantInfoBusinessImpl implements ConstantInfoBusiness {

	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Override
	public List<String> getTitleList() {
		 
		return constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
	}

}
