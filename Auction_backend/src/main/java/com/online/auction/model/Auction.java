package com.online.auction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auctionId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    @Column(name = "start_time", columnDefinition = "DATETIME")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private Item items;

    private boolean isOpen;

    @Column(name = "end_time", columnDefinition = "DATETIME")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;
}
