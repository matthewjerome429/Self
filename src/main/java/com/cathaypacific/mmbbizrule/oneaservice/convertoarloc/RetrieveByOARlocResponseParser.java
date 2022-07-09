package com.cathaypacific.mmbbizrule.oneaservice.convertoarloc;

import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply.PnrList;

public class RetrieveByOARlocResponseParser {
	/**
	 * logger Factory
	 */
	//private static final Logger LOGGER = Logger.getLogger(RetrieveByOARlocResponseParser.class)
	private static final LogAgent LOGGER = LogAgent.getLogAgent(RetrieveByOARlocResponseParser.class);
	
	/**
	 * 
	* @Description find One A rloc from 1A response
	* @param
	* @return String
	* @author fengfeng.jiang
	 */
	public String findOneARloc(PNRRetrieveByOARlocReply retrieveByOARlocReply,String inputRloc) {
		String resultRloc = null;
		if (retrieveByOARlocReply == null ) {
			return resultRloc;
		}
		
		//LOGGER.info("findOneARloc", retrieveByOARlocReply.toString(), 0, "Soap response from 1A")
		
		String gdsRloc="";
		String gdsCompany ="";
		for (PnrList pnr : retrieveByOARlocReply.getPnrList()) {

			if (inputRloc.equals(pnr.getExternalReservation().getReservation().getControlNumber())
					&& pnr.getPnrDetails() != null) {
				if (pnr.getPnrDetails().getAttributeDetails().stream()
						.anyMatch(type -> OneAConstants.ATTRIBUTETYPE_OAP.equals(type.getAttributeType()))) {
					resultRloc = pnr.getAmadeusReservation().getReservation().getControlNumber();
					gdsRloc = pnr.getExternalReservation().getReservation().getControlNumber();
					gdsCompany = pnr.getExternalReservation().getReservation().getCompanyId();
					break;
				} else if (pnr.getPnrDetails().getAttributeDetails().stream().anyMatch(
						type -> OneAConstants.ATTRIBUTETYPE_CSP.equals(type.getAttributeType()) || OneAConstants.ATTRIBUTETYPE_SGP.equals(type.getAttributeType()))) {
					resultRloc = pnr.getAmadeusReservation().getReservation().getControlNumber();
					gdsRloc = pnr.getExternalReservation().getReservation().getControlNumber();
					gdsCompany = pnr.getExternalReservation().getReservation().getCompanyId();
				}
			}
		}
		if(!StringUtils.isEmpty(resultRloc)){
			LOGGER.info(String.format("Received 1A rloc by GDS rloc,  1A rloc=%s,GDS rloc=%s,Company ID=%s", resultRloc,gdsRloc,gdsCompany));
		}
		return resultRloc;
	}
}
