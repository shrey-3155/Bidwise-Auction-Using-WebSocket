package com.online.auction.dto;

import com.online.auction.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private String city;
    private String timezone;
    private boolean isPremium;
}
