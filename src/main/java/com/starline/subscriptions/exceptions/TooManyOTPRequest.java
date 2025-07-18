package com.starline.subscriptions.exceptions;

public class TooManyOTPRequest extends ApiException {

    public TooManyOTPRequest() {
        super("Too many OTP request");
        this.httpCode = 429;
    }
}
