package com.cathaypacific.mmbbizrule.cxservice.olci.service;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.cathaypacific.olciconsumer.model.request.boardingpass.SendEmailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendSMSRequestDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.BoardingPassResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendEmailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendSMSResponseDTO;

public interface SentBPService {

	public BoardingPassResponseDTO getEligibleBPPaxs(String journeyId, List<String> requiredUcis, boolean spbp, String rloc);
	
	public Future<SendEmailResponseDTO> asyncSendEmail(SendEmailRequestDTO requestDTO, String rloc);
	
	public SendEmailResponseDTO sendEmail(SendEmailRequestDTO requestDTO, String rloc);
	
	public SendSMSResponseDTO sendSms(SendSMSRequestDTO requestDTO, String rloc);
	
	public Future<SendSMSResponseDTO> asyncSendSms(SendSMSRequestDTO requestDTO, String rloc);
	
	public ResponseEntity<Resource> getAppleWalletBP(String local,String applePassNumber, String rloc);

}
