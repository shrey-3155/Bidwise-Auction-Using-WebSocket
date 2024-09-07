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
public class SuggestedItemDTO {
    private String auctionId;
    private String itemId;
    private String itemName;
    private String itemPhoto;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String cityName;
}
