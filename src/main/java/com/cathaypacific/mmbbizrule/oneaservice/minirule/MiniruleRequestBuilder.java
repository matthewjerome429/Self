package com.cathaypacific.mmbbizrule.oneaservice.minirule;

import org.springframework.stereotype.Service;

import com.cathaypacific.mmbbizrule.constant.MiniruleConstant;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.*;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.MiniRuleGetFromRec.GroupRecords;

@Service
public class MiniruleRequestBuilder {
	private ObjectFactory objFactory = new ObjectFactory();
	
	public MiniRuleGetFromRec buildMiniruleRequest(String rloc) {
		MiniRuleGetFromRec miniRuleGetFromRec = objFactory.createMiniRuleGetFromRec();
		
		GroupRecords groupRecords = objFactory.createMiniRuleGetFromRecGroupRecords();
		ItemReferencesAndVersionsType itemReferencesAndVersionsType = objFactory.createItemReferencesAndVersionsType();
		itemReferencesAndVersionsType.setReferenceType(MiniruleConstant.PNR);
		itemReferencesAndVersionsType.setUniqueReference(rloc);
		groupRecords.setRecordID(itemReferencesAndVersionsType);
		miniRuleGetFromRec.getGroupRecords().add(groupRecords);

		return miniRuleGetFromRec;
	}
	
}