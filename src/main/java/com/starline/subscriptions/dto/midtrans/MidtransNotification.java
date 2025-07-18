package com.starline.subscriptions.dto.midtrans;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/24/2024 12:13 AM
@Last Modified 6/24/2024 12:13 AM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MidtransNotification {


    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("transaction_status")
    private String transactionStatus;

    @JsonProperty("fraud_status")
    private String fraudStatus;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("gross_amount")
    private String grossAmount;

    @JsonProperty("signature_key")
    private String signatureKey;
}
