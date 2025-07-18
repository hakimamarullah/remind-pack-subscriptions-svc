package com.starline.subscriptions.service;

import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.subscriptions.HasActiveSubscription;
import com.starline.subscriptions.dto.subscriptions.SubscriptionInfo;

import java.util.List;

public interface SubscriptionService {

    ApiResponse<List<SubscriptionInfo>> getSubscriptionInfoByUserId(Long userId);

    ApiResponse<HasActiveSubscription> checkHasActiveSubscription(Long userId);
}
