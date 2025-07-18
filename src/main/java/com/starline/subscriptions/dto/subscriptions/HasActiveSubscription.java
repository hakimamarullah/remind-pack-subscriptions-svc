package com.starline.subscriptions.dto.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HasActiveSubscription {

    private boolean isActiveSubscription;
}
