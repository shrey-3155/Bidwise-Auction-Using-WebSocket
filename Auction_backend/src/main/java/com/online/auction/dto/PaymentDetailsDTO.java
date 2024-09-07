package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentDetailsDTO {
    private String email;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String name;
}
