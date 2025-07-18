package com.starline.subscriptions.service.impl;

import com.midtrans.httpclient.error.MidtransError;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.midtrans.MidtransItemDetails;
import com.starline.subscriptions.dto.midtrans.SnapAPIRequest;
import com.starline.subscriptions.dto.payment.CreateOrderRequest;
import com.starline.subscriptions.dto.payment.OrderSummary;
import com.starline.subscriptions.dto.payment.PaymentInfo;
import com.starline.subscriptions.dto.proxy.UserInfo;
import com.starline.subscriptions.exceptions.DataNotFoundException;
import com.starline.subscriptions.feign.UserProxySvc;
import com.starline.subscriptions.model.PaymentHistory;
import com.starline.subscriptions.model.Plan;
import com.starline.subscriptions.model.Subscription;
import com.starline.subscriptions.model.enums.PaymentStatus;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import com.starline.subscriptions.repository.PaymentHistRepository;
import com.starline.subscriptions.repository.PlanRepository;
import com.starline.subscriptions.repository.SubscriptionRepository;
import com.starline.subscriptions.service.OrderService;
import com.starline.subscriptions.service.SnapAPIService;
import com.starline.subscriptions.utils.CurrencyUtil;
import com.starline.subscriptions.utils.SubscriptionsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidtransOrderSvc implements OrderService {

    private final PlanRepository planRepository;

    private final UserProxySvc userProxySvc;

    private final SnapAPIService snapAPIService;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentHistRepository paymentHistRepository;

    @Value("${tax.rate:0.12}")
    private double taxRate;

    @Override
    public ApiResponse<OrderSummary> getPlanOrderSummary(Long planId) {
        Plan plan = planRepository.findByIdAndEnabledTrue(planId)
                .orElseThrow(() -> new DataNotFoundException("Plan not found"));

        int planPrice = plan.getPrice();
        int taxTotal = (int) Math.round(planPrice * taxRate);
        int grandTotal = planPrice + taxTotal;
        String taxRateDisplay = String.format("%.0f%%", taxRate * 100);

        OrderSummary orderSummary = OrderSummary.builder()
                .planId(plan.getId())
                .planName(plan.getName())
                .planDescription(plan.getDescription())
                .planPrice(CurrencyUtil.formatIDR(planPrice))
                .taxTotal(CurrencyUtil.formatIDR(taxTotal))
                .taxRate(taxRateDisplay)
                .grandTotal(CurrencyUtil.formatIDR(grandTotal))
                .build();
        return ApiResponse.setSuccess(orderSummary);
    }

    @Transactional
    @Override
    public ApiResponse<PaymentInfo> createOrder(CreateOrderRequest request) throws MidtransError {
        // Check Eligibility
        ApiResponse<Void> isEligibleForPlan = isEligibleForPlan(request);
        if (isEligibleForPlan.isNot2xxSuccessful()) {
            return ApiResponse.setResponse(null, isEligibleForPlan.getMessage(), isEligibleForPlan.getCode());
        }

        // Setup Customer Details
        SnapAPIRequest.CustomerDetails customerDetails = new SnapAPIRequest.CustomerDetails();
        ApiResponse<UserInfo> userInfoResponse = userProxySvc.getUserInfoById(request.getUserId());
        Optional.ofNullable(userInfoResponse.getData())
                .ifPresent(user -> customerDetails.setPhone(user.getMobilePhone()));


        Plan plan = planRepository.findByIdAndEnabledTrue(request.getPlanId())
                .orElseThrow(() -> new DataNotFoundException("Plan not found"));
        if (Boolean.FALSE.equals(plan.getEnabled())) {
            return ApiResponse.setResponse(null, "Plan is not available", 400);
        }

        Subscription subscription = new Subscription();
        subscription.setId(SubscriptionsUtil.generateId())
                .setStatus(SubscriptionStatus.PENDING)
                .setPlan(plan)
                .setUserId(request.getUserId());

        // Free plan
        if (plan.getPrice() == 0) {
            subscription.setEffectiveDate(LocalDateTime.now());
            subscription.setExpiryDate(SubscriptionsUtil.calculateExpiryDate(subscription.getEffectiveDate(), plan.getValidity()));
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscriptionRepository.save(subscription);
            return ApiResponse.setResponse(new PaymentInfo(), 201);
        }

        // Setup Transaction Details
        int planPrice = plan.getPrice();
        int taxTotal = (int) Math.round(planPrice * taxRate);
        int grandTotal = planPrice + taxTotal;
        SnapAPIRequest.TransactionDetails transactionDetails = SnapAPIRequest.TransactionDetails.builder()
                .orderId(subscription.getId())
                .grossAmount(String.valueOf(grandTotal))
                .build();

        // Setup Item Details
        MidtransItemDetails itemPlan = new MidtransItemDetails();
        int priceAfterTax = planPrice + taxTotal;
        itemPlan.setId(String.valueOf(plan.getId()))
                .setPrice(priceAfterTax)
                .setQuantity(1)
                .setName(plan.getName())
                .setBrand("RemindPack");

        SnapAPIRequest snapAPIRequest = SnapAPIRequest.builder()
                .customerDetails(customerDetails)
                .transactionDetails(transactionDetails)
                .itemDetails(List.of(itemPlan))
                .build();

        String snapUrl = snapAPIService.createSnapUrl(snapAPIRequest);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setPaymentStatus(PaymentStatus.PENDING.name())
                .setAmount(String.valueOf(grandTotal))
                .setReferenceId(subscription.getId())
                .setPaymentUrl(snapUrl);

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .snapUrl(snapUrl)
                .build();
        subscriptionRepository.save(subscription);
        paymentHistRepository.save(paymentHistory);
        return ApiResponse.setResponse(paymentInfo, 201);
    }


    private boolean isLimitExceeded(int currentSubCountByPlan, Integer planLimit) {
        if (Objects.isNull(planLimit)) {
            return false;
        }

        return currentSubCountByPlan >= planLimit;
    }

    private ApiResponse<Void> isEligibleForPlan(CreateOrderRequest request) {
        int countActiveAndPending = subscriptionRepository.countByUserIdAndStatusIn(request.getUserId(),
                List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PENDING));
        if (countActiveAndPending > 0) {
            return ApiResponse.setResponse(null, "You have an active or pending subscription. Please complete payment or wait until your subscription is expired", 400);
        }

        // Check Limit
        int countByPlanId = subscriptionRepository.countByPlanId(request.getPlanId());
        Integer planLimit = planRepository.getLimitById(request.getPlanId()).orElse(null);
        if (isLimitExceeded(countByPlanId, planLimit)) {
            return ApiResponse.setResponse(null, "You have reached the maximum number of subscriptions for this plan", 400);
        }
        return ApiResponse.setDefaultSuccess();
    }
}
