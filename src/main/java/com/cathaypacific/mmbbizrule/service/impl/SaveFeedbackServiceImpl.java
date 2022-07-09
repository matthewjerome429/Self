package com.cathaypacific.mmbbizrule.service.impl;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.TbFeedbackDAO;
import com.cathaypacific.mmbbizrule.db.model.TbFeedback;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
import com.cathaypacific.mmbbizrule.service.SaveFeedbackService;

@Service
public class SaveFeedbackServiceImpl implements SaveFeedbackService{

	@Autowired
	private TbFeedbackDAO tbFeedbackDAO;
	
	@Override
	public void saveFeedback(SaveFeedbackRequestDTO requestDTO, String rloc, String eticket) {
		
		if(requestDTO == null){
			return;
		}
	
		TbFeedback tbFeedback = new TbFeedback();
		tbFeedback.setRloc(rloc);
		tbFeedback.setEticket(eticket);
		tbFeedback.setAppCode(MMBConstants.APP_CODE);
		tbFeedback.setMemberId(requestDTO.getMemberId());
		tbFeedback.setPage(requestDTO.getPage());
		tbFeedback.setStarRating(requestDTO.getStarRating());
		tbFeedback.setMcOption(requestDTO.getMcOption());
		tbFeedback.setComment(requestDTO.getComment());
		tbFeedback.setLastUpdateSource(TBConstants.UPDATE_SOURCE);
		tbFeedback.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));

		tbFeedbackDAO.save(tbFeedback);
	}
}
