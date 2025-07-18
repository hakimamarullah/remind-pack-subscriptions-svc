package com.starline.subscriptions.service;

import com.midtrans.httpclient.error.MidtransError;
import com.starline.subscriptions.dto.midtrans.SnapAPIRequest;

public interface SnapAPIService {

    String createSnapUrl(SnapAPIRequest payload) throws MidtransError;
}
