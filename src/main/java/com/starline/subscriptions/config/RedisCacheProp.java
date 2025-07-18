package com.starline.subscriptions.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "redis.cache")
@Component
@Getter
@Setter
public class RedisCacheProp {

    private String host = "localhost";
    private int port = 6379;

    private long subscriptionTtlHours = 3L;
    private long planTtlHours = 8L;
}
