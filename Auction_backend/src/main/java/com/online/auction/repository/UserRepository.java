package com.online.auction.repository;

import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link User} entities, with the primary key of type {@code Integer}.
 * It includes methods to find users by their email address and reset token.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Finds a user by their email address.
     * <p>
     * This method retrieves a user entity based on the provided email address.
     * </p>
     *
     * @param email The email address of the user to be retrieved.
     * @return An {@link Optional} containing the {@link User} if found, or empty if no user with the provided email exists.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their password reset token.
     * <p>
     * This method retrieves a user entity based on the provided reset token, which is typically used
     * for password recovery processes.
     * </p>
     *
     * @param resetToken The reset token associated with the user.
     * @return An {@link Optional} containing the {@link User} if found, or empty if no user with the provided reset token exists.
     */
    Optional<User> findByResetToken(String resetToken);
}
