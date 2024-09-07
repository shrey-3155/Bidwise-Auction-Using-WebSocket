package com.online.auction.dto;


import com.online.auction.model.ItemCategory;
import com.online.auction.model.ItemCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItemsDTO {
    private int itemId;
    private String itemName;
    private LocalDateTime auctionStartTime;
    private String itemPhoto;
    private String itemMaker;
    private String description;
    private double minBidAmount;
    private double price_paid;
    private String currency;
    private ItemCondition item_condition;
    private ItemCategory itemcategory;
    private LocalDateTime auctionEndTime;
    private String cityName;
}
