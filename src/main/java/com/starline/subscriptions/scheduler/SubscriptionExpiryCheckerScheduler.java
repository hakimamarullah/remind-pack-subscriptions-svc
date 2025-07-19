package com.starline.subscriptions.scheduler;

import com.starline.subscriptions.repository.SubscriptionRepository;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiryCheckerScheduler {

    private final SubscriptionRepository subscriptionRepository;

    @WithSpan
    @Scheduled(cron = "${cron.check-expired-subscription-status:0 0 0 * * *}")
    @Transactional
    public void checkExpiredSubscriptionStatus() {
        LocalDate cutOffDate = LocalDate.now();
        log.info("Check expired subscription status at {}", cutOffDate);
        int count = subscriptionRepository.expireSubscriptions(cutOffDate);
        log.info("Expired {} subscriptions", count);
    }
}
