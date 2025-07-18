package com.starline.subscriptions.controllers;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/23/2024 3:53 PM
@Last Modified 6/23/2024 3:53 PM
Version 1.0
*/

import com.starline.subscriptions.annotations.LogRequestResponse;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.midtrans.MidtransNotification;
import com.starline.subscriptions.service.MidtransWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/midtrans")
@RequiredArgsConstructor
@Slf4j
@LogRequestResponse
public class MidtransWebhook {

    private final MidtransWebhookService webhookService;


    @PostMapping("/notification")
    public ResponseEntity<ApiResponse<String>> handleNotification(@RequestBody MidtransNotification payload) {
        return webhookService.handleNotification(payload).toResponseEntity();
    }
}
