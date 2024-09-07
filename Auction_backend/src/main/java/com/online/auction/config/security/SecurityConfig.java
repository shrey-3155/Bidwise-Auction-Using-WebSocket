package com.online.auction.config.security;

import com.online.auction.filter.JwtAuthenticationFilter;
import com.online.auction.handler.CustomAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.online.auction.model.Role.ADMIN;
import static com.online.auction.model.Role.USER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration class for setting up web security in the application.
 * <p>
 * This configuration class enables web security, sets up authentication, and configures authorization rules for different endpoints.
 * It also defines the behavior for handling exceptions, user logout, and JWT authentication.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/api/v1/user/register", "/api/v1/user/authenticate", "/api/v1/user/refresh-token", "/api/v1/user/forgot-password", "/api/v1/user/reset-password", "/gs-guide-websocket", "/api/v1/locale/countries", "/api/v1/locale/cities", "/api/v1/auction/upcoming"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configures the security filter chain for the application.
     * <p>
     * This method configures HTTP security to:
     * - Disable CSRF protection.
     * - Permit access to specific white-listed URLs without authentication.
     * - Require authentication for other URLs, with role-based access control.
     * - Use stateless session management.
     * - Set up a custom JWT authentication filter before the default username/password authentication filter.
     * - Handle access denied exceptions with a custom handler.
     * - Configure logout behavior including clearing the security context on successful logout.
     * </p>
     *
     * @param http The {@link HttpSecurity} object used to configure security settings.
     * @return A {@link SecurityFilterChain} object representing the configured security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/user/**").hasAnyRole(ADMIN.name(), USER.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(new CustomAccessDeniedException()))
                .logout(logout ->
                        logout.logoutUrl("/api/v1/user/logout")
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;
        return http.build();
    }
}
