package com.online.auction.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class SuccessResponse<T> {
    private LocalDateTime timestamp;
    private int httpCode;
    private HttpStatus httpStatus;
    private T data;

    public SuccessResponse(int httpCode, HttpStatus httpStatus, T data) {
        this.timestamp = LocalDateTime.now();
        this.httpCode = httpCode;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
