package com.starline.subscriptions.dto.subscriptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starline.subscriptions.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SubscriptionInfo {

    private String id;
    private String planName;
    private String planDescription;
    private SubscriptionStatus status;

    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDateTime effectiveDate;

    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDateTime expiryDate;

    private String paymentUrl;
}
