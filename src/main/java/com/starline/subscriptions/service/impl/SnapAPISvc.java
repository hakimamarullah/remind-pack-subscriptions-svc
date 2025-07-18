package com.starline.subscriptions.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import com.starline.subscriptions.dto.midtrans.SnapAPIRequest;
import com.starline.subscriptions.service.SnapAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapAPISvc implements SnapAPIService {

    private final MidtransSnapApi midtransSnapApi;

    private final ObjectMapper mapper;

    @Override
    public String createSnapUrl(SnapAPIRequest payload) throws MidtransError {
        Map<String, Object> request = mapper.convertValue(payload, new TypeReference<>() {
        });
        return midtransSnapApi.createTransactionRedirectUrl(request);
    }
}
