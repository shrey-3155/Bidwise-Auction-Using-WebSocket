package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomErrorResponseDTO {
    private final String timestamp;
    private final int httpCode;
    private final String httpStatus;
    private final String errorMessage;
}
