package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDTO {
    private String auctionId;
    private boolean isOpen;
    private LocalDateTime startTime;
    private String sellerId;
    private LocalDateTime endTime;
    private String itemId;
}
