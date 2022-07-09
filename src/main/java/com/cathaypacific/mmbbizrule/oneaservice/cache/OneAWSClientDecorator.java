package com.cathaypacific.mmbbizrule.oneaservice.cache;

import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.AirFlightInfo;
import com.cathaypacific.oneaconsumer.model.request.iflirq_15_2_1a.InvAdvancedGetFlightData;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.PNRRetrieveByOARloc;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.PNRSearch;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.PNRChangeElement;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.PNRCancel;
import com.cathaypacific.oneaconsumer.model.request.smpreq_14_2_1a.AirRetrieveSeatMap;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketProcessEDocAirportControl;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.MiniRuleGetFromRec;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.cryptic_07_3_1a.CommandCrypticReply;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
import com.cathaypacific.oneaconsumer.model.response.iflirr_15_2_1a.InvAdvancedGetFlightDataReply;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.PNRSearchReply;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.smpreq_14_2_1a.AirRetrieveSeatMapReply;
import com.cathaypacific.oneaconsumer.model.response.tacres_17_1_1a.TicketProcessEDocAirportControlReply;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

public class OneAWSClientDecorator implements OneAWSClient {

	protected OneAWSClient oneAWSClient;
	
	@Override
	public PNRRetrieveByOARlocReply getRlocByGDSRloc(PNRRetrieveByOARloc request) throws SoapFaultException {
		return oneAWSClient.getRlocByGDSRloc(request);
	}

	@Override
	public PNRSearchReply getPnrViews(PNRSearch request) throws SoapFaultException {
		return oneAWSClient.getPnrViews(request);
	}

	@Override
	public AirRetrieveSeatMapReply retrieveSeatMap(AirRetrieveSeatMap request) throws SoapFaultException {
		return oneAWSClient.retrieveSeatMap(request);
	}

	@Override
	public PNRReply retrievePnr(PNRRetrieve request) throws SoapFaultException {
		return oneAWSClient.retrievePnr(request);
	}
	
	@Override
	public PNRReply retrievePnr(PNRRetrieve request, Session session) throws SoapFaultException {
		return oneAWSClient.retrievePnr(request, session);
	}

	@Override
	public AirFlightInfoReply findFlightInfo(AirFlightInfo request) throws SoapFaultException {
		return oneAWSClient.findFlightInfo(request);
	}

	@Override
	public TicketProcessEDocReply ticketprocess(TicketProcessEDoc request, Session session) throws SoapFaultException {
		return oneAWSClient.ticketprocess(request, session);
	}

	@Override
	public OneaResponse<PNRReply> addMultiElements(PNRAddMultiElements request, Session session)
			throws SoapFaultException {
		return oneAWSClient.addMultiElements(request, session);
	}

	@Override
	public OneaResponse<PNRReply> deleteMultiElements(PNRCancel request, Session session) throws SoapFaultException {
		return oneAWSClient.deleteMultiElements(request, session);
	}

	public void setOneAWSClient(OneAWSClient oneAWSClient) {
		this.oneAWSClient = oneAWSClient;
	}

	@Override
	public InvAdvancedGetFlightDataReply getInvAdvancedGetFlightData(InvAdvancedGetFlightData request)
			throws SoapFaultException {
		return oneAWSClient.getInvAdvancedGetFlightData(request);
	}

	@Override
	public CommandCrypticReply sendTicketEmail(CommandCryptic request, Session session) throws SoapFaultException {
		return oneAWSClient.sendTicketEmail(request, session);
	}

	@Override
	public Session openSessionOnly(String oneARloc) throws SoapFaultException {
		return oneAWSClient.openSessionOnly(oneARloc);
	}

	@Override
	public OneaResponse<PNRReply> changePnrElement(PNRChangeElement request, Session session, String rloc) throws SoapFaultException {
		return oneAWSClient.changePnrElement(request, session, rloc);
	}

	@Override
	public Session endSessionOnlyWithCxmbRf(Session session) throws SoapFaultException {
		return oneAWSClient.endSessionOnlyWithCxmbRf(session);
	}

	@Override
	public TicketProcessEDocAirportControlReply ticketprocessControl(TicketProcessEDocAirportControl request,
			Session session) throws SoapFaultException {
		return oneAWSClient.ticketprocessControl(request, session);
	}

	@Override
	public MiniRuleGetFromRecReply getMiniRuleGetFromRec(MiniRuleGetFromRec request, Session session)
			throws SoapFaultException {
		return oneAWSClient.getMiniRuleGetFromRec(request, session);
	}

}
