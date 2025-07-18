package com.starline.subscriptions.exceptions;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String message) {
        super(message);
        this.httpCode = 404;
    }
}
