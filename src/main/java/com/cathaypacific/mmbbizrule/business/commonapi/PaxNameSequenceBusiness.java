package com.cathaypacific.mmbbizrule.business.commonapi;


import com.cathaypacific.mmbbizrule.dto.response.namesequence.PaxNameSequenceDTO;


public interface PaxNameSequenceBusiness {
	public PaxNameSequenceDTO findPaxNameSequence(String appCode,String locale);
}
