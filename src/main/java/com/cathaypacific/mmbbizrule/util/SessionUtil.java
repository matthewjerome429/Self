package com.cathaypacific.mmbbizrule.util;

import com.cathaypacific.mbcommon.loging.LogAgent;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class SessionUtil {

    private static LogAgent logger = LogAgent.getLogAgent(SessionUtil.class);

    private static final String OLCI_JSESSIONID = "OLCI_JSESSIONID";
    
    public static String getCookie(ResponseEntity responseEntity){
        Assert.notNull(responseEntity,"ResponseEntity is null");
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        Assert.notEmpty(cookies,"not cookie");
        return cookies.get(0);
    }
    
    public static String getCookies(ResponseEntity responseEntity){
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        if(cookies != null) {
        	return String.join(";", cookies.stream().map(cookie -> cookie.split(";")[0]).collect(Collectors.toList()));
        }
        return "";
    }
}
