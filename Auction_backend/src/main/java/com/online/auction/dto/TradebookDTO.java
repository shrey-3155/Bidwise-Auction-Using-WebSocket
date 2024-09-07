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
public class TradebookDTO {
    private String nameOfItem;
    private Double soldPrice;
    private Double userHighestBid;
    private LocalDateTime dateOfAuction;
    private boolean isAuctionWon;
    private int AuctionId;
}
