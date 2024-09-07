package com.online.auction.config.application;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfctfgi4g",
                "api_key", "774726643355859",
                "api_secret", "4IG5R5BaIt3hJxkUmKqzlRn9Kig"));
    }
}
