package com.cathaypacific.mmbbizrule.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormSegmentDTO;

public interface UMNREFormPDFService {

	/**
	 * Build PDF file by specified passenger and journey
	 * @param pdfTemplate - input stream of pdfTemplate. You should close the input stream after calling.
	 * @param umnrSegments - all segments of the booking
	 * @param umnrPassenger - specified passenger to build PDF file
	 * @param umnrJourney - specified journey to build PDF file
	 * @return Output stream
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public OutputStream buildPDF(InputStream pdfTemplate, String oneARloc, List<UMNREFormSegmentDTO> umnrSegments, UMNREFormPassengerDTO umnrPassenger, UMNREFormJourneyDTO umnrJourney) throws ParseException, IOException;

}
