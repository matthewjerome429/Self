package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.db.dao.TbFeedbackDAO;
import com.cathaypacific.mmbbizrule.db.model.TbFeedback;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
@RunWith(MockitoJUnitRunner.class)
public class SaveFeedbackServiceImplTest {
	@InjectMocks
	SaveFeedbackServiceImpl saveFeedbackServiceImpl;
	@Mock
	private TbFeedbackDAO tbFeedbackDAO;
	@Test
	public void test() {
		SaveFeedbackRequestDTO requestDTO=new SaveFeedbackRequestDTO();
		TbFeedback tbFeedback = new TbFeedback();
		String rloc="MN542";
		String eticket="123";
		requestDTO.setMemberId("1");
		requestDTO.setPage("JKL");
		requestDTO.setMcOption("IO");
		requestDTO.setComment("hello");
		when(tbFeedbackDAO.save(tbFeedback)).thenReturn(tbFeedback);
		try {
			saveFeedbackServiceImpl.saveFeedback(requestDTO, rloc, eticket);
		} catch (Exception e) {
			// TODO: handle exception
			
		}
	}

}
