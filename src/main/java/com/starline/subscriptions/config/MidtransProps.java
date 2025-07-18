package com.starline.subscriptions.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "midtrans")
@Setter
@Getter
public class MidtransProps {

    private String clientKey;
    private String serverKey;
    private boolean isProduction;
    private String snapUrl;
}
