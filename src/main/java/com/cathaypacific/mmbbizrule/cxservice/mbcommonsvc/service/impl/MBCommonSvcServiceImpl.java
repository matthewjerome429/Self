package com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.model.response.PhoneNumberValidationResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.service.MBCommonSvcService;

@Service
public class MBCommonSvcServiceImpl implements MBCommonSvcService {
	
	@Value("${mb.endpoint.mbcommonsvc.phonenumber.validation}")
	private String phonenumberValidationUri;

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public PhoneNumberValidationResponseDTO validatePhoneNumber(String phoneCountryNumber, String phonenNumber,
			ContactType contactType) {
		URI uri = UriComponentsBuilder.fromHttpUrl(phonenumberValidationUri)
				.queryParam("countryCode", phoneCountryNumber).queryParam("phoneNumber", phonenNumber)
				.queryParam("contactType", contactType).build().toUri();

		RequestEntity<Void> requestEntity = RequestEntity.get(uri)
				.header(MMBConstants.HEADER_KEY_MMB_TOKEN_ID, MMBUtil.getCurrentMMBToken()).build();
	
		ResponseEntity<PhoneNumberValidationResponseDTO> responseEntity = restTemplate.exchange(requestEntity,
				PhoneNumberValidationResponseDTO.class);

		return responseEntity.getBody();
	}

}
