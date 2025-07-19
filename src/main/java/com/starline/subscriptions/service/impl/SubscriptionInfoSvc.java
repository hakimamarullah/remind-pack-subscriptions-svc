package com.starline.subscriptions.service.impl;

import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.subscriptions.HasActiveSubscription;
import com.starline.subscriptions.dto.subscriptions.SubscriptionInfo;
import com.starline.subscriptions.model.Subscription;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import com.starline.subscriptions.repository.PaymentHistRepository;
import com.starline.subscriptions.repository.SubscriptionRepository;
import com.starline.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionInfoSvc implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentHistRepository paymentHistRepository;

    @Override
    public ApiResponse<List<SubscriptionInfo>> getSubscriptionInfoByUserId(Long userId) {
        List<Subscription> subscriptionInfoList = subscriptionRepository.getSubscriptionByUserIdAndStatusIn(userId,
                List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PENDING));

        String paymentUrl = subscriptionInfoList.stream()
                .filter(it -> it.getStatus() == SubscriptionStatus.PENDING)
                .findFirst()
                .map(Subscription::getId)
                .map(paymentHistRepository::getPaymentUrlByReferenceId)
                .orElse(null);
        return ApiResponse.setResponse(toSubscriptionInfoList(subscriptionInfoList, paymentUrl), 200);
    }

    @Override
    public ApiResponse<HasActiveSubscription> checkHasActiveSubscription(Long userId) {
        boolean hasActiveSubscription = subscriptionRepository.countByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE) > 0;
        return ApiResponse.setResponse(new HasActiveSubscription(hasActiveSubscription), 200);
    }

    private List<SubscriptionInfo> toSubscriptionInfoList(List<Subscription> subscriptionInfoList, String paymentUrl) {
        List<SubscriptionInfo> results = new ArrayList<>();

        for (Subscription sub : subscriptionInfoList) {
            var plan = sub.getPlan();
            var item = SubscriptionInfo.builder()
                    .id(sub.getId())
                    .status(sub.getStatus())
                    .effectiveDate(sub.getEffectiveDate())
                    .expiryDate(sub.getExpiryDate())
                    .planDescription(plan.getDescription())
                    .planName(plan.getName())
                    .planDescription(plan.getDescription())
                    .planCycle(plan.getValidity().name())
                    .build();
            if (item.getStatus() == SubscriptionStatus.PENDING) {
                item.setPaymentUrl(paymentUrl);
            }
            results.add(item);
        }

        return results;

    }
}
