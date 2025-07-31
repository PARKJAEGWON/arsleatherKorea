package com.groo.kmw.global.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom.toss.payments")
@Getter
@Setter
public class TossPaymentsConfig {
    private String clientKey;
    private String secretKey;
}