package com.online.auction.repository;

import com.online.auction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Account} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link Account} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
    /**
     * Retrieves an {@link Account} entity by its associated user ID.
     * <p>
     * This method uses a custom query to find an account based on the given user ID.
     * </p>
     *
     * @param userId The ID of the user whose account is to be retrieved.
     * @return The {@link Account} entity associated with the given user ID, or {@code null} if no account is found.
     */
    Account findByUserId(Integer userId);
}
