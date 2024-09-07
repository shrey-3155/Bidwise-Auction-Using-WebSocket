package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private String itemName;
    private String sellerName;
    private String sellerEmail;
    private String itemCategory;
    private LocalDateTime dateOfAuction;
    private LocalDateTime dateOfInvoice;
    private Double pricePaid;
}