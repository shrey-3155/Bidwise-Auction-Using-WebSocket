package com.online.auction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.sql.Blob;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @ManyToOne
    @JoinColumn(name = "item_category_id", referencedColumnName = "itemCategoryId")
    private ItemCategory itemcategory;

    private String description;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User sellerId;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyerId;

    @Column(nullable = true)
    private double min_bid_amount;

    @Column(nullable = true)
    private Double selling_amount;

    private String item_photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition")
    private ItemCondition item_condition;

    @Column(name = "item_name")
    private String item_name;

    private String item_maker;
    private double price_paid;

    private String currency;

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + this.itemId +
                "itemName==" + this.item_name +
                ", itemCategory=" + (this.itemcategory != null ? this.itemcategory : "None") +
                ", description='" + this.description + '\'' +
                ", seller=" + (this.sellerId != null ? this.sellerId.getFirstName() + " " + this.sellerId.getLastName() : "None") +
                ", buyer=" + (this.buyerId != null ? this.buyerId.getFirstName() + " " + this.buyerId.getLastName() : "None") +
                ", minBidAmount=" + this.min_bid_amount +
                ", sellingAmount=" + this.selling_amount +
                ", itemPhoto='" + this.item_photo + '\'' +
                ", itemCondition=" + this.item_condition +
                '}';
    }
}
