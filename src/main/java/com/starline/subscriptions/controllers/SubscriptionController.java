package com.starline.subscriptions.controllers;

import com.starline.subscriptions.annotations.LogRequestResponse;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.subscriptions.HasActiveSubscription;
import com.starline.subscriptions.dto.subscriptions.SubscriptionInfo;
import com.starline.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@LogRequestResponse
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<SubscriptionInfo>>> getSubscriptionInfoByUserId(@PathVariable Long userId) {
        return subscriptionService.getSubscriptionInfoByUserId(userId).toResponseEntity();
    }

    @GetMapping(value = "/users/{userId}/has-active-subscription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<HasActiveSubscription>> checkHasActiveSubscription(@PathVariable Long userId) {
        return subscriptionService.checkHasActiveSubscription(userId).toResponseEntity();
    }
}
