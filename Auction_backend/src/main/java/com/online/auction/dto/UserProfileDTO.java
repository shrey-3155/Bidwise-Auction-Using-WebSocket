package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String cityName;
    private String countryName;
}
