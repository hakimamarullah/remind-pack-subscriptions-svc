package com.starline.subscriptions.config;


import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MidtransConfig {

    private final MidtransProps midtransProps;

    @Bean
    public Config getSnapConfig() {
       return Config.builder()
               .setServerKey(midtransProps.getServerKey())
               .setClientKey(midtransProps.getClientKey())
               .setIsProduction(midtransProps.isProduction())
               .build();
    }

    @Bean
    public MidtransSnapApi getSnapApi(Config config) {
        return new ConfigFactory(config).getSnapApi();
    }
}
