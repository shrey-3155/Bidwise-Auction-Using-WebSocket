package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidStatsDTO {
    private int month;
    private long wonAuctions;
    private long totalParticipatedAuctions;
}
