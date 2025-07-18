package com.starline.subscriptions.exceptions;

public class InvalidMobilePhoneFormatException extends ApiException {

    public InvalidMobilePhoneFormatException() {
        super("Invalid mobile phone format");
        this.httpCode = 400;
    }
}
