package com.cathaypacific.mmbbizrule.cxservice.olci.service.impl;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.SentBPService;
import com.cathaypacific.olciconsumer.model.request.boardingpass.BoardingPassRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendEmailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendSMSRequestDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.BoardingPassResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendEmailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendSMSResponseDTO;
import com.cathaypacific.olciconsumer.service.client.OlciClient;

@Service
public class SentBPServiceImpl implements SentBPService {
	
	private static LogAgent logger = LogAgent.getLogAgent(SentBPServiceImpl.class);
	
	@Autowired
	private OlciClient olciClient;
	
	@Override
	public BoardingPassResponseDTO getEligibleBPPaxs(String journeyId, List<String> requiredUcis, boolean spbp, String rloc) {
		if (StringUtils.isEmpty(journeyId) || CollectionUtils.isEmpty(requiredUcis)) {
			return null;
		}

		BoardingPassRequestDTO boardingPassRequest = new BoardingPassRequestDTO();
		boardingPassRequest.setJourneyId(journeyId);
		boardingPassRequest.setUcis(requiredUcis);
		boardingPassRequest.setSpbp(spbp);
		try {
			ResponseEntity<BoardingPassResponseDTO> responseEntity = olciClient.getEligibleBPPaxs(boardingPassRequest, rloc, null);
			return responseEntity.getBody();
		} catch(Exception e) {
			logger.error(String.format("Journey[%s] get eligible send boarding pass passengers failure when trying to call OLCI", journeyId), e);
		}
		
		return null;
	}
	
	@Async
	@Override
	public Future<SendEmailResponseDTO> asyncSendEmail(SendEmailRequestDTO requestDTO, String rloc) {
		return new AsyncResult<>(sendEmail(requestDTO, rloc));
	}
	
	@Override
	public SendEmailResponseDTO sendEmail(SendEmailRequestDTO requestDTO, String rloc) {
		try {
			ResponseEntity<SendEmailResponseDTO> responseEntity = olciClient.sendEmailWithOLCI(requestDTO, rloc, null);
			return responseEntity.getBody();
		} catch(Exception e) {
			logger.error(String.format("Journey[%s] send boarding pass email failure when trying to call OLCI",
					requestDTO != null ? requestDTO.getJourneyId() : null), e);
		}
		
		return null;
	}
	
	@Async
	@Override
	public Future<SendSMSResponseDTO> asyncSendSms(SendSMSRequestDTO requestDTO, String rloc) {
		return new AsyncResult<>(sendSms(requestDTO, rloc));
	}
	
	@Override
	public SendSMSResponseDTO sendSms(SendSMSRequestDTO requestDTO, String rloc) {
		try {
			ResponseEntity<SendSMSResponseDTO> responseEntity = olciClient.sendSMSWithOLCI(requestDTO, rloc, null);
			return responseEntity.getBody();
		} catch(Exception e) {
			logger.error(String.format("Journey[%s] send boarding pass sms failure when trying to call OLCI",
					requestDTO != null ? requestDTO.getJourneyId() : null), e);
		}
		
		return null;
	}
	
	@Override
	public ResponseEntity<Resource> getAppleWalletBP(String local, String applePassNumber, String rloc) {
		try {
			return olciClient.getAppleWalletBP(local, applePassNumber, rloc, null);
		} catch(Exception e) {
			logger.error(String.format("ApplePassNumber[%s] send boarding pass sms failure when trying to call OLCI",applePassNumber), e);
		}
		
		return null;
	}

}
