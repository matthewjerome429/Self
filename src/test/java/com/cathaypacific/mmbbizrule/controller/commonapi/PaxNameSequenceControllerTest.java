package com.cathaypacific.mmbbizrule.controller.commonapi;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mmbbizrule.business.commonapi.PaxNameSequenceBusiness;
import com.cathaypacific.mmbbizrule.dto.response.namesequence.PaxNameSequenceDTO;

@RunWith(MockitoJUnitRunner.class)
public class PaxNameSequenceControllerTest {
	
	@InjectMocks
	private PaxNameSequenceController paxNameSequenceController;
	
	@Mock
	private PaxNameSequenceBusiness paxNameSequenceBusiness;
	
	@Test
	public void test() {
		String locale="AU";
		String appCode="MMB";
		PaxNameSequenceDTO paxNameSequence=new PaxNameSequenceDTO();
		paxNameSequence.setFamilyNameSequence(1);
		paxNameSequence.setGivenNameSequence(2);
		when(paxNameSequenceBusiness.findPaxNameSequence(appCode,locale)).thenReturn(paxNameSequence);		
		paxNameSequenceController.findPaxNameSequence(appCode,locale);
		assertEquals("1",paxNameSequence.getFamilyNameSequence().toString());
		assertEquals("2",paxNameSequence.getGivenNameSequence().toString());
	}
	
}
