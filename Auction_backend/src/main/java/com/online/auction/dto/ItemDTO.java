package com.online.auction.dto;

import com.online.auction.model.ItemCondition;
import com.online.auction.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private int itemId;
    private String itemName;
    private String itemMaker;
    private String description;
    private double minBidAmount;
    private double pricePaid;
    private String currency;
    private String itemPhoto;
    private ItemCondition itemCondition;
    private String categoryName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double soldPrice;
    private boolean isAuctionEnded; // New field for auction status

}
