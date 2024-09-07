package com.online.auction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    private int userId;

    private double funds;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
}
