package com.starline.subscriptions.resources;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WhatsAppTrackingInfoMessage {

    private final MessageSource messageSource;

    @Builder(toBuilder = true)
    public static class TrackingStartParams {
        private String trackingNumber;
        private String currentCheckpoint;
        private String timestamp;
        private String courierName;

        Object[] toArray() {
            return new Object[]{trim(courierName), trim(trackingNumber), trim(currentCheckpoint), trim(timestamp)};
        }
    }

    @Builder(toBuilder = true)
    public static class TrackingUpdateParams {
        private String trackingNumber;
        private String currentCheckpoint;
        private String previousCheckpoint;
        private String previousCheckpointTime;
        private String currentCheckpointTime;
        private String courierName;


        Object[] toArray() {
            return new Object[]{trim(courierName), trim(trackingNumber), trim(currentCheckpoint),
                    trim(currentCheckpointTime), trim(previousCheckpoint), trim(previousCheckpointTime)};
        }
    }

    public String getTrackingStartMessage(TrackingStartParams params) {
        return messageSource.getMessage("package.tracking.started",
                params.toArray(),
                LocaleContextHolder.getLocale());
    }

    public String getTrackingUpdateMessage(TrackingUpdateParams params) {
        return messageSource.getMessage("package.status.update",
                params.toArray(),
                LocaleContextHolder.getLocale());
    }

    private static String trim(String str) {
        return Objects.toString(str, "N/A").trim();
    }
}
