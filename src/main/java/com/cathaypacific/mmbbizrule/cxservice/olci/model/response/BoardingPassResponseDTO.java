package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class BoardingPassResponseDTO extends BaseResponseDTO{
	
		private static final long serialVersionUID = 5073082310003998247L;
		
		private List<String> eligibleBoardingPassPassengerUcis;
		
		public BoardingPassResponseDTO() {
			super();
		}
		
		public BoardingPassResponseDTO(List<ErrorInfo> errorInfos, List<String> eligibleBoardingPassPassengerUcis) {
			super();
			this.addAllErrors(errorInfos);
			this.eligibleBoardingPassPassengerUcis = eligibleBoardingPassPassengerUcis;
		}
		
		public BoardingPassResponseDTO(ErrorInfo errorInfo, List<String> eligibleBoardingPassPassengerUcis) {
			super();
			this.addError(errorInfo);
			this.eligibleBoardingPassPassengerUcis = eligibleBoardingPassPassengerUcis;
		}

		public List<String> getEligibleBoardingPassPassengerUcis() {
			return eligibleBoardingPassPassengerUcis;
		}

		public void setEligibleBoardingPassPassengerUcis(List<String> eligibleBoardingPassPassengerUcis) {
			this.eligibleBoardingPassPassengerUcis = eligibleBoardingPassPassengerUcis;
		}
		
}
