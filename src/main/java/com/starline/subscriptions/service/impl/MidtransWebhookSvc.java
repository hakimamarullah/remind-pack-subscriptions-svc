package com.starline.subscriptions.service.impl;

import com.starline.subscriptions.config.MidtransProps;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.midtrans.MidtransNotification;
import com.starline.subscriptions.model.PaymentHistory;
import com.starline.subscriptions.model.Plan;
import com.starline.subscriptions.model.Subscription;
import com.starline.subscriptions.model.enums.PaymentStatus;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import com.starline.subscriptions.repository.PaymentHistRepository;
import com.starline.subscriptions.repository.SubscriptionRepository;
import com.starline.subscriptions.service.MidtransWebhookService;
import com.starline.subscriptions.utils.HashUtils;
import com.starline.subscriptions.utils.SubscriptionsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidtransWebhookSvc implements MidtransWebhookService {

    private final MidtransProps midtransProps;

    private final PaymentHistRepository paymentHistRepository;

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    @Override
    public ApiResponse<String> handleNotification(MidtransNotification payload) {
        log.info("[HANDLING MIDTRANS NOTIFICATION FOR ORDER] {} Status: {}", payload.getOrderId(), payload.getTransactionStatus());
        if (!isValidSignature(payload)) {
            return ApiResponse.setResponse(ApiResponse.FAILED, "Invalid signature", 403);
        }
        Optional<Subscription> subscription = subscriptionRepository.findById(payload.getOrderId());
        if (subscription.isEmpty()) {
            return ApiResponse.setResponse(ApiResponse.FAILED, "Subscription not found", 404);
        }

        Optional<PaymentHistory> paymentHistory = paymentHistRepository.findByReferenceId(payload.getOrderId());
        if (paymentHistory.isEmpty()) {
            return ApiResponse.setResponse(ApiResponse.FAILED, "Payment history not found", 404);
        }

        if (isPaymentSuccessful(payload.getTransactionStatus(), payload.getFraudStatus())) {
            Subscription sub = subscription.get();
            sub.setStatus(SubscriptionStatus.ACTIVE);
            sub.setEffectiveDate(LocalDateTime.now());

            Plan plan = sub.getPlan();
            sub.setExpiryDate(SubscriptionsUtil.calculateExpiryDate(sub.getEffectiveDate(), plan.getValidity()));
            subscriptionRepository.save(sub);


            var paymentHist = paymentHistory.get();
            paymentHist.setPaymentStatus(PaymentStatus.SUCCESS.name());
            paymentHist.setStatusCode(payload.getStatusCode());
            paymentHist.setPartnerTrxId(payload.getTransactionId());
            paymentHist.setPaymentDate(sub.getEffectiveDate());
            paymentHist.setPaymentType(paymentHist.getPaymentType());

            paymentHistRepository.save(paymentHist);

        } else if (!"pending".equalsIgnoreCase(payload.getTransactionStatus())) {
            var paymentHist = paymentHistory.get();
            paymentHist.setPaymentStatus(PaymentStatus.FAILED.name());
            paymentHist.setStatusCode(payload.getStatusCode());
            paymentHist.setPartnerTrxId(payload.getTransactionId());
            paymentHist.setPaymentDate(LocalDateTime.now());
            paymentHist.setPaymentType(paymentHist.getPaymentType());
            paymentHist.setReason(payload.getTransactionStatus());

            paymentHistRepository.save(paymentHist);
            subscriptionRepository.deleteById(subscription.get().getId());

        }
        log.info("[END HANDLING MIDTRANS NOTIFICATION FOR ORDER] {} Status: {}", payload.getOrderId(), payload.getTransactionStatus());
        return ApiResponse.setDefaultSuccess();
    }



    public boolean isValidSignature(MidtransNotification notification) {
        String orderId = notification.getOrderId();
        String statusCode = notification.getStatusCode();
        String grossAmount = notification.getGrossAmount();
        String serverKey = midtransProps.getServerKey();
        String signature = HashUtils.sha512Hex(orderId + statusCode + grossAmount + serverKey);

        return signature.equals(notification.getSignatureKey());
    }

    private boolean isPaymentSuccessful(String transactionStatus, String fraudStatus) {
        return ("capture".equals(transactionStatus) && "accept".equals(fraudStatus)) || "settlement".equals(transactionStatus);
    }

}
