package com.starline.subscriptions.dto.midtrans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SnapAPIRequest {

    @JsonProperty("customer_details")
    private CustomerDetails customerDetails;

    @JsonProperty("transaction_details")
    private TransactionDetails transactionDetails;

    @JsonProperty("item_details")
    private List<MidtransItemDetails> itemDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerDetails {
        private String phone;
    }

    @Data
    @Builder(toBuilder = true)
    public static class TransactionDetails {

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("gross_amount")
        private String grossAmount;
    }
}
