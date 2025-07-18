package com.starline.subscriptions.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public enum PaymentStatus {

    PENDING, SUCCESS, FAILED;

    @JsonCreator
    public static PaymentStatus getType(String type) {
        if (Objects.isNull(type)) {
            return null;
        }
        for (PaymentStatus requestType : values()) {
            if (requestType.name().equalsIgnoreCase(type)) {
                return requestType;
            }
        }
        return null;
    }
}
