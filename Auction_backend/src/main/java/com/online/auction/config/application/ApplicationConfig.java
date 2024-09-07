package com.online.auction.config.application;

import com.online.auction.audit.ApplicationAuditAware;
import com.online.auction.handler.CustomAccessDeniedException;
import com.online.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Boot configuration class for setting up security and auditing components.
 * <p>
 * This class is responsible for configuring various beans related to security and auditing,
 * including password encoding, user authentication, and auditor-aware components.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     * Provides a {@link PasswordEncoder} bean for encoding passwords using BCrypt.
     *
     * @return A {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a {@link UserDetailsService} bean for loading user-specific data.
     * <p>
     * This service loads user details by email, and throws a {@link UsernameNotFoundException}
     * if the user is not found in the repository.
     * </p>
     *
     * @return A {@link UserDetailsService} instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Provides an {@link AuthenticationProvider} bean for handling authentication requests.
     * <p>
     * This provider uses {@link DaoAuthenticationProvider} to perform authentication
     * by setting the user details service and password encoder.
     * </p>
     *
     * @return A {@link DaoAuthenticationProvider} instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides an {@link AuditorAware} bean for auditing purposes.
     * <p>
     * This bean is responsible for returning the current auditor (user) ID for auditing
     * purposes. It uses {@link ApplicationAuditAware} to retrieve the current user's ID.
     * </p>
     *
     * @return An {@link ApplicationAuditAware} instance.
     */
    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new ApplicationAuditAware();
    }

    /**
     * Provides an {@link AuthenticationManager} bean for managing authentication requests.
     * <p>
     * This manager is configured using the provided {@link AuthenticationConfiguration}.
     * </p>
     *
     * @param config The {@link AuthenticationConfiguration} to configure the manager.
     * @return An {@link AuthenticationManager} instance.
     * @throws Exception If an error occurs during the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a {@link CustomAccessDeniedException} bean for handling access denied errors.
     * <p>
     * This custom exception is used to provide specific handling for cases where access
     * is denied.
     * </p>
     *
     * @return A {@link CustomAccessDeniedException} instance.
     */
    @Bean
    public CustomAccessDeniedException customAccessDeniedException() {
        return new CustomAccessDeniedException();
    }
}
