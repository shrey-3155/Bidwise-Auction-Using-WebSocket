package com.online.auction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auction_bid_detail")
public class AuctionBidDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auction_bid_details_id;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auctionId;


    @ManyToOne
    @JoinColumn(name = "bidder_id", nullable = false)
    private User bidderId;


    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item itemId;

    private double bid_amount;

    @Column(name = "bidTime", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bidTime;

    private boolean isWon;
}
