package com.starline.subscriptions.service;

import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.midtrans.MidtransNotification;

public interface MidtransWebhookService {

    ApiResponse<String> handleNotification(MidtransNotification payload);
}
