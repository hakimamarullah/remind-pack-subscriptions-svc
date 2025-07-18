package com.starline.subscriptions.service;

import com.midtrans.httpclient.error.MidtransError;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.payment.CreateOrderRequest;
import com.starline.subscriptions.dto.payment.OrderSummary;
import com.starline.subscriptions.dto.payment.PaymentInfo;

public interface OrderService {

    ApiResponse<OrderSummary> getPlanOrderSummary(Long planId);

    ApiResponse<PaymentInfo> createOrder(CreateOrderRequest request) throws MidtransError;
}
