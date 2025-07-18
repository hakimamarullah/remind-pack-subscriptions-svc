package com.starline.subscriptions.utils;

import com.starline.subscriptions.model.enums.PlanValidity;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubscriptionsUtil {

    private SubscriptionsUtil() {
    }

    public static String generateId() {
        String todayDatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomDigitsSuffix = RandomStringUtils.secureStrong().nextAlphanumeric(3).toUpperCase();
        String randomDigitsPrefix = RandomStringUtils.secureStrong().nextAlphanumeric(2).toUpperCase();
        return randomDigitsPrefix + todayDatetime + randomDigitsSuffix;
    }

    public static LocalDateTime calculateExpiryDate(LocalDateTime effectiveDate, PlanValidity planValidity) {
        return switch (planValidity) {
            case WEEKLY -> effectiveDate.plusDays(7);
            case MONTHLY -> effectiveDate.plusMonths(1);
            case YEARLY -> effectiveDate.plusYears(1);
        };
    }

    public static String getValidityDisplay(PlanValidity planValidity) {
        return switch (planValidity) {
            case WEEKLY -> "7 Days";
            case MONTHLY -> "Month";
            case YEARLY -> "Year";
        };
    }


}
