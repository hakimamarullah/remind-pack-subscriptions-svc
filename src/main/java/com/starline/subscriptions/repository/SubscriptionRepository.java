package com.starline.subscriptions.repository;

import com.starline.subscriptions.model.Subscription;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    List<Subscription> getSubscriptionByUserIdAndStatusIn(Long userId, List<SubscriptionStatus> status);

    int countByUserIdAndStatus(Long userId, SubscriptionStatus status);

    int countByUserIdAndStatusIn(Long userId, List<SubscriptionStatus> status);

    int countByPlanId(Long planId);

    @Modifying
    @Query("""
            update Subscription s set s.status = 'EXPIRED',
            s.version = s.version + 1,
            s.updatedDate = current_timestamp
            where s.expiryDate < :cutOffDate AND s.status = 'ACTIVE'
     """)
    int expireSubscriptions(LocalDate cutOffDate);
}
