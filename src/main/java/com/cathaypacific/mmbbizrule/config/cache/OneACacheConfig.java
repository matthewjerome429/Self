package com.cathaypacific.mmbbizrule.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cathaypacific.mmbbizrule.oneaservice.cache.AirFlightInfoCacheComponent;
import com.cathaypacific.mmbbizrule.oneaservice.cache.OneAWSClientDecorator;
import com.cathaypacific.mmbbizrule.oneaservice.cache.PNRCacheComponent;
import com.cathaypacific.mmbbizrule.oneaservice.cache.TicketProcessCacheComponent;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Configuration
public class OneACacheConfig {

	@Value("${cahce.onea.pnr}")
	private boolean pnrCache;
	@Value("${cahce.onea.flightinfo}")
	private boolean flightinfoCache;
	@Value("${cahce.onea.ticketprocess}")
	private boolean ticketProcessCache;

	
	@Autowired
	private AirFlightInfoCacheComponent airFlightInfoCacheComponent;
	
	@Autowired
	private PNRCacheComponent pnrCacheComponent;
	@Autowired
	private TicketProcessCacheComponent ticketProcessCacheComponent;
	
	@Bean(name = "mmbOneAWSClient")
	public OneAWSClient getMMBOneAWSClient(@Qualifier("oneAWSClient") OneAWSClient oneAWSClient) {

		OneAWSClient mmbOneAWSClient = oneAWSClient;
		if (flightinfoCache) {
			airFlightInfoCacheComponent.setOneAWSClient(mmbOneAWSClient);
			mmbOneAWSClient = airFlightInfoCacheComponent;
		}
		if (ticketProcessCache) {
			ticketProcessCacheComponent.setOneAWSClient(mmbOneAWSClient);
			mmbOneAWSClient = ticketProcessCacheComponent;
		}
		if (pnrCache) {
			pnrCacheComponent.setOneAWSClient(mmbOneAWSClient);
			mmbOneAWSClient = pnrCacheComponent;
		}
		// return OneAWSClientDecorator instead of mmbOneAWSClient because spring aop will check @Cacheable twice and stored two same recodes to redis if use mmbOneAWSClient, 
		OneAWSClientDecorator  oneAWSClientDecorator = new OneAWSClientDecorator();
		oneAWSClientDecorator.setOneAWSClient(mmbOneAWSClient);
		return oneAWSClientDecorator;
	}
}
