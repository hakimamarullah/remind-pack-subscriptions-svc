package com.starline.subscriptions.repository;

import com.starline.subscriptions.model.Subscription;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    List<Subscription> getSubscriptionByUserIdAndStatusIn(Long userId, List<SubscriptionStatus> status);

    int countByUserIdAndStatus(Long userId, SubscriptionStatus status);

    int countByUserIdAndStatusIn(Long userId, List<SubscriptionStatus> status);

    int countByPlanId(Long planId);
}
