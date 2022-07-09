package com.cathaypacific.mmbbizrule.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by zilong.bu on 3/8/2017.
 */
@Configuration
public class SwaggerFoxConfig {
	
	@Value("#{'${app.code.list}'.split(',')}")
	private List<String> appCodeList;
	
	@Value("#{'${app.access.channel.list}'.split(',')}")
	private List<String> accessChannelList;
	
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/.*")).paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .globalOperationParameters(globalOperationParameters())
                .apiInfo(apiInfo());
    }
    /**
     * Add global parameters
     * @return
     */
    private List<Parameter> globalOperationParameters() {
    	
        List<Parameter> pars = new ArrayList<>();
    	//add appcode
        ParameterBuilder appCodePar = new ParameterBuilder();
        appCodePar.name(MMBConstants.HEADER_KEY_APP_CODE).description("e.g.:MMB,OLCI").modelRef(new ModelRef("string")).parameterType("header").allowableValues(new AllowableListValues(appCodeList, "string")).required(false).defaultValue("MMB").build();
        pars.add(appCodePar.build());
        ParameterBuilder accessChannelPar = new ParameterBuilder();
        //add accessChannel
        accessChannelPar.name(MMBConstants.HEADER_KEY_ACCESS_CHANNEL).description("e.g.：WEB,MOB,VERA").modelRef(new ModelRef("string")).parameterType("header").allowableValues(new AllowableListValues(accessChannelList, "string")).required(false).defaultValue("").build();
        pars.add(accessChannelPar.build());
        //Accept-Language
        ParameterBuilder acceptLanguagePar = new ParameterBuilder();
        acceptLanguagePar.name(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE).description("Accept-Language：e.g. en_HK").modelRef(new ModelRef("string")).parameterType("header").defaultValue("en_HK").required(false).build();
        pars.add(acceptLanguagePar.build());
        
        return pars;
 
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MMBPnr API with Swagger")
                .description("MMB API with Swagger")
                .termsOfServiceUrl("http://www.cathaypacific.com")
                .license("Copyright ©  Cathay Pacific Airways Limited")
                .build();
    }
    
}
