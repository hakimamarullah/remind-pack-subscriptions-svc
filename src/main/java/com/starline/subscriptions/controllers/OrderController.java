package com.starline.subscriptions.controllers;

import com.midtrans.httpclient.error.MidtransError;
import com.starline.subscriptions.annotations.LogRequestResponse;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.payment.CreateOrderRequest;
import com.starline.subscriptions.dto.payment.OrderSummary;
import com.starline.subscriptions.dto.payment.PaymentInfo;
import com.starline.subscriptions.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@LogRequestResponse
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "/{planId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<OrderSummary>> getPlaOrderSummary(@PathVariable Long planId) {
        return orderService.getPlanOrderSummary(planId).toResponseEntity();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PaymentInfo>> createOrder(@RequestBody @Valid CreateOrderRequest request) throws MidtransError {
        return orderService.createOrder(request).toResponseEntity();
    }
}
