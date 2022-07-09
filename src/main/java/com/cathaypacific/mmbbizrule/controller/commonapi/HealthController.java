package com.cathaypacific.mmbbizrule.controller.commonapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class HealthController {

    @RequestMapping("/_health")
    public String health() {
        return "OK";
    }

}

