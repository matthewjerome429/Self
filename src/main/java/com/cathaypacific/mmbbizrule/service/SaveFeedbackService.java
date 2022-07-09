package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;

public interface SaveFeedbackService {

	/**
	 * 
	* @Description save feedback
	* @param requestDTO
	* @return boolean
	* @author haiwei.jia
	 */
	public void saveFeedback(SaveFeedbackRequestDTO requestDTO, String rloc, String eticket);
}
