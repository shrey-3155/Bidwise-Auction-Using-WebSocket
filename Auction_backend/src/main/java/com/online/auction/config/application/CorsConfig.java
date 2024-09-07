package com.online.auction.config.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) in the application.
 * <p>
 * This class configures CORS to allow cross-origin requests to the application's endpoints.
 * It defines which origins are allowed to make requests and which HTTP methods are permitted.
 * </p>
 */
@Configuration
public class CorsConfig {
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    /**
     * Provides a {@link WebMvcConfigurer} bean that configures CORS settings for the application.
     * <p>
     * This method configures CORS mappings to allow requests from origins specified in the
     * {@code cors.allowed.origins} property and permits all HTTP methods.
     * </p>
     *
     * @return A {@link WebMvcConfigurer} instance that sets up the CORS mappings.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("*");
            }
        };
    }
}
