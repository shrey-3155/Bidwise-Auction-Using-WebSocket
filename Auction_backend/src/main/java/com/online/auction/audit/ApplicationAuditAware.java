package com.online.auction.audit;

import com.online.auction.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Provides information about the current auditor (user) for auditing purposes.
 * <p>
 * This implementation of {@link AuditorAware} is used to retrieve the current
 * authenticated user's ID. It checks the security context to determine if the user
 * is authenticated and not anonymous. If these conditions are met, it returns
 * the user's ID as the current auditor; otherwise, it returns an empty {@link Optional}.
 * </p>
 * <p>
 * This class is used to support auditing functionality by providing the ID of the
 * user performing the action being audited, which can be useful for tracking changes
 * and maintaining an audit trail in the application.
 * </p>
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getUserId());
    }
}
