package org.sid.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
//@RefreshScope <- old not needed with ConfigurationProperties
public class ConsulConfigRestController {

//    old way with injection

//    @Value("${token.accessTokenTimeout}")
//    private long accessTokenTimeout;
//    @Value("${token.refreshTokenTimeout}")
//    private long refreshTokenTimeout;
//    @GetMapping("/myConfig")
//    public Map<String, Object> myConfig() {
//        return Map.of("accessTokenTimeout",accessTokenTimeout, "refreshTokenTimeout",refreshTokenTimeout);
//    }
    // new way with ConfigurationProperties
    @Autowired
    private MyConsulConfig myConsulConfig;

    @GetMapping("/myConfig")
    public MyConsulConfig myConfig() {
        return myConsulConfig;
    }

}
