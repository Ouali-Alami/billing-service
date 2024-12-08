package org.sid.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.vault.config.VaultConfigTemplate;

import java.util.Map;

//@RefreshScope //<- old not needed with ConfigurationProperties
// consul cmd to create the pair key,value in consul:
//  consul kv put /config/billing-service/token/accessTokenTimeout 3600
//  warning you cannot create a key with '.' in cmd but via the ui you can
@RestController
public class ConsulConfigRestController {

//    old way with injection

//    @Value("${accessTokenTimeout}")
//    private long accessTokenTimeout;
//    @Value("${refreshTokenTimeout}")
//    private long refreshTokenTimeout;
//    @GetMapping("/myConfig")
//
//    public Map<String, Object> myConfig() {
//        return Map.of("accessTokenTimeout",accessTokenTimeout, "refreshTokenTimeout",refreshTokenTimeout);
//    }
    // new way with ConfigurationProperties
    // not working collision between spring config and consul interpretation of url
    // warning . are replace by /
    private final MyConsulConfig myConsulConfig;
    private final MyVaultConfig myVaultConfig;

    // Injection par constructeur
    @Autowired
    public ConsulConfigRestController(MyConsulConfig myConsulConfig, MyVaultConfig myVaultConfig) {
        this.myConsulConfig = myConsulConfig;
        this.myVaultConfig = myVaultConfig;
    }

    @GetMapping("/myConfig")
    public Map<String, Object> myConfig() {
        return Map.of("myConsulConfig",myConsulConfig, "myVaultConfig",myVaultConfig);
    }

}
