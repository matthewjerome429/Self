package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommunication.model.request.testmail.TestEmailRequest;
import com.cathaypacific.mbcommunication.webservice.service.client.TestEmailClient;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;
import com.cathaypacific.mmbbizrule.cxservice.eods.service.EODSBookingService;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.TicketRefundRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.ibe.service.IBEService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.request.smpreq_14_2_1a.AirRetrieveSeatMap;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.smpreq_14_2_1a.AirRetrieveSeatMapReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/* REMOVE TEST CODE BEFORE GO LIVE R5.0
@Api(tags = {" Temp For Test"} , description= "just for test, will delete before go live")
@RestController
@RequestMapping(path = "/v1/common")
*/
public class TempForTestController {
	
	private static LogAgent logger = LogAgent.getLogAgent(TempForTestController.class);
	
	@Autowired
	private EODSBookingService eodsBookingServiceImpl;
	
	@Autowired
	private OneAWSClient oneAWSClient;
	
	@Autowired
	private IBEService ibeService;
	
	@Autowired
	private TestEmailClient testMailClient;
	
	@GetMapping("/eods/{memberId}")
	@ApiOperation(value = "Retrieve traveldoc types by api version.", produces = "application/json")
	public  List<EODSBooking> getTravedocTypes(@PathVariable String memberId) throws UnexpectedException {

		return eodsBookingServiceImpl.getBookingList(memberId);
	}

	@GetMapping("/retrieveSeatMap")
	@ApiOperation(value = "Retrieve seat map", response = AirRetrieveSeatMapReply.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "departureDate", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "originAirportCode", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "destinationAirportCode", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "marketingCompany", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "flightNum", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "bookingClass", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "rloc", value = "", required = true, dataType = "string", paramType = "query", defaultValue = ""),
	})
	public  AirRetrieveSeatMapReply retrieveSeatMap(@ApiIgnore @Validated SeatMapRequestDTO requestDTO) throws BusinessBaseException {
		AirRetrieveSeatMap request = new RetrieveSeatMapRequestBuilder().build(requestDTO.getDepartureDate(), requestDTO.getOriginAirportCode(), requestDTO.getDestinationAirportCode(), requestDTO.getMarketingCompany(), requestDTO.getFlightNum(), requestDTO.getBookingClass(), requestDTO.getRloc());
		return oneAWSClient.retrieveSeatMap(request);
	}

	@GetMapping("/pnr/{rloc}")
	@ApiOperation(value = "Retrieve pnr", response = AirRetrieveSeatMapReply.class, produces = "application/json")
	public  PNRReply retrievePnr(@PathVariable String rloc) throws BusinessBaseException {
		PNRRetrieve request = new PnrRequestBuilder().buildRlocRequest(rloc);
		return oneAWSClient.retrievePnr(request);
	}
	
	@GetMapping("/sockethanguptest/{sleepSeconeds}")
	@ApiOperation(value = "socket hangup test", response = AirRetrieveSeatMapReply.class, produces = "application/json")
	public  String socketHangUpTest(@PathVariable int sleepSeconeds) throws  InterruptedException {
		logger.info(System.currentTimeMillis()+": strat socket hangup test, sleep seconeds:"+sleepSeconeds);
		Thread.sleep(sleepSeconeds*1000l);
		logger.info(System.currentTimeMillis()+": end socket hangup test, sleep seconeds:"+sleepSeconeds);
		return ""+sleepSeconeds;
	}
	
	@PostMapping("/ibe/ticketrefund")
	@ApiOperation(value = "ticket refund through IBE", produces = "application/json")
	public boolean ticketRefund(@RequestBody TicketRefundRequestDTO request) throws UnexpectedException {
		return ibeService.submitTicketRefund(request);
	}
	
	@PostMapping("/mail/{locale}")
	@ApiOperation(value = "send E-mail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "locale", value = "", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "toAddress", value = "", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "fromAddress", value = "", required = false, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "subject", value = "", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "body", value = "", required = true, dataType = "string", paramType = "form", defaultValue = "")
	})
	public boolean sendMail(@PathVariable String locale, @RequestHeader String toAddress,
			@RequestHeader(required = false) String fromAddress, @RequestHeader String subject, String body) {
		TestEmailRequest request = new TestEmailRequest();
		request.setLocale(locale);
		request.setToAddress(toAddress);
		request.setFromAddress(fromAddress);
		request.setSubject(subject);
		request.setBody(body);
		return testMailClient.sendEmail(request);
	}
	
}
