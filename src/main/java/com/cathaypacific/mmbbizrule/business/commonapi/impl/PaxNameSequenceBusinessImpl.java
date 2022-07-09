package com.cathaypacific.mmbbizrule.business.commonapi.impl;



import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cathaypacific.mmbbizrule.business.commonapi.PaxNameSequenceBusiness;
import com.cathaypacific.mmbbizrule.db.dao.PaxNameSequenceDAO;
import com.cathaypacific.mmbbizrule.db.model.PaxNameSequence;
import com.cathaypacific.mmbbizrule.dto.response.namesequence.PaxNameSequenceDTO;


@Service
public class PaxNameSequenceBusinessImpl implements PaxNameSequenceBusiness{
	@Autowired
	private PaxNameSequenceDAO paxNameSequenceDAO;
	@Override
	public PaxNameSequenceDTO findPaxNameSequence(String appCode,String locale) {

		PaxNameSequenceDTO paxNameSequenceDTO = new PaxNameSequenceDTO();
		PaxNameSequence paxNameSequence=null;
		
		if (StringUtils.isNotEmpty(locale)) {
			locale = locale.toUpperCase();
			paxNameSequence=paxNameSequenceDAO.findPaxNameSequenceByAppCodeAndLocale(appCode, locale);
			paxNameSequenceDTO.setFamilyNameSequence(paxNameSequence.getFamilyNameSequence());
			paxNameSequenceDTO.setGivenNameSequence(paxNameSequence.getGivenNameSequence());
			paxNameSequenceDTO.setDispalyModelNameBy(paxNameSequence.getDispalyModelNameBy().getCode());
			return paxNameSequenceDTO;
		}
		return paxNameSequenceDTO;
	}
}


