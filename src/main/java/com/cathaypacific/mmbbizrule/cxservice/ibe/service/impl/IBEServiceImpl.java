package com.cathaypacific.mmbbizrule.cxservice.ibe.service.impl;

import java.net.URI;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.request.CancelBookingEligibilityRequest;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response.CancelBookingEligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.TicketRefundRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.response.TicketRefundResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.service.IBEService;

@Service
public class IBEServiceImpl implements IBEService {
	
	@Value("${endpoint.path.ibe.ticketrefund}")
	private String ticketRefundUrl;
	
	@Value("${endpoint.path.ibe.cancelbooking.eligibility.check}")
	private String cancelBookingEligibilityCheckUri;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean submitTicketRefund(TicketRefundRequestDTO request) throws UnexpectedException {
		ResponseEntity<TicketRefundResponseDTO> responseEntity = restTemplate.postForEntity(ticketRefundUrl, request, TicketRefundResponseDTO.class);			
		checkTicketRefundResponse(responseEntity.getBody());
		return true;
	}
	
	@Override
	public CancelBookingEligibilityResponse checkCancelBookingEligibility(CancelBookingEligibilityRequest cancelBookingEligibilityRequest) {
		URI uri = UriComponentsBuilder.fromHttpUrl(cancelBookingEligibilityCheckUri).build().toUri();
		RequestEntity<CancelBookingEligibilityRequest> requestEntity = RequestEntity.post(uri).body(cancelBookingEligibilityRequest);
		ResponseEntity<CancelBookingEligibilityResponse> responseEntity = restTemplate.exchange(requestEntity, CancelBookingEligibilityResponse.class);
		return responseEntity.getBody();
	}
	
	private void checkTicketRefundResponse(TicketRefundResponseDTO response) throws UnexpectedException {
		if(response == null || CollectionUtils.isNotEmpty(response.getErrors())) {
			throw new UnexpectedException("ticket refund through IBE, response is null or contains any errors", new ErrorInfo(ErrorCodeEnum.ERR_IBE_TICKET_REFUND_FAIL));
		}
	}

}
