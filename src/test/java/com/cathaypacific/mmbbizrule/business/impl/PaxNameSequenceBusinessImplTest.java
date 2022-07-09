package com.cathaypacific.mmbbizrule.business.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mmbbizrule.business.commonapi.impl.PaxNameSequenceBusinessImpl;
import com.cathaypacific.mmbbizrule.constant.PaxNameSequenceEnum;
import com.cathaypacific.mmbbizrule.db.dao.PaxNameSequenceDAO;
import com.cathaypacific.mmbbizrule.db.model.PaxNameSequence;

@RunWith(MockitoJUnitRunner.class)
public class PaxNameSequenceBusinessImplTest {
	
	@InjectMocks
	PaxNameSequenceBusinessImpl paxNameSequenceBusinessImpl;
	
	@Mock
	PaxNameSequenceDAO paxNameSequenceDAO;
	
	@Test
	public void test() {
		String locale="AU";
		String appCode="MMB";
		PaxNameSequence paxNameSequence=new PaxNameSequence();
		paxNameSequence.setAppCode("MMB");
		paxNameSequence.setFamilyNameSequence(1);
		paxNameSequence.setGivenNameSequence(2);
		paxNameSequence.setDispalyModelNameBy(PaxNameSequenceEnum.LF);
		when(paxNameSequenceDAO.findPaxNameSequenceByAppCodeAndLocale(appCode, locale)).thenReturn(paxNameSequence);
		paxNameSequenceBusinessImpl.findPaxNameSequence(appCode,locale);
		assertEquals("MMB",paxNameSequence.getAppCode());
		assertEquals("1",paxNameSequence.getFamilyNameSequence().toString());
		assertEquals("2",paxNameSequence.getGivenNameSequence().toString());
		assertEquals("LastNameAndFirstName",paxNameSequence.getDispalyModelNameBy().getCode());

	}
}
