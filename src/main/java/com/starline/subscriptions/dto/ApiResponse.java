package com.starline.subscriptions.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {

    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";
    @Builder.Default
    private Integer code = 200;
    private String message;
    private T data;



    public static <U> ApiResponse<U> setResponse(U data, String message, int code) {
        return ApiResponse.<U>builder()
                .code(code)
                .data(data)
                .message(message)
                .build();
    }

    public static ApiResponse<Void> setSuccessWithMessage(String message) {
        return setResponse(null, message, 200);
    }

    public static <U> ApiResponse<U> setDefaultSuccess() {
        return setResponse(null, SUCCESS, 200);
    }

    public static <U> ApiResponse<U> setResponse(U data, int code) {
        return setResponse(data, SUCCESS, code);
    }

    public static <U> ApiResponse<U> setSuccess(U data) {
        return setResponse(data, SUCCESS, 200);
    }

    public static <U> ApiResponse<PageWrapper<U>> setPagedResponse(Page<U> page) {
        return setPagedResponse(page, 200);
    }

    public static <U> ApiResponse<PageWrapper<U>> setPagedResponse(Page<U> page, int code) {
        var message = code < 200 || code >= 300 ? FAILED : SUCCESS;
        return setResponse(PageWrapper.of(page), message, code);
    }




    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.code);
    }

    @JsonIgnore
    public ResponseEntity<ApiResponse<T>> toResponseEntity() { return ResponseEntity.status(getHttpStatus()).body(this); }

    @JsonIgnore
    public boolean isNot2xxSuccessful() {
        return this.code < 200 || this.code >= 300;
    }
}

