package com.online.auction.repository;

import com.online.auction.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Token} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link Token} entities, with the primary key of type {@code Integer}.
 * It includes methods to retrieve valid tokens for a user and find a token by its value.
 * </p>
 */
public interface TokenRepository extends JpaRepository<Token, Integer> {
    /**
     * Finds all valid tokens associated with a specific user.
     * <p>
     * This method retrieves tokens for a user that are neither expired nor revoked.
     * The tokens are fetched using an inner join with the {@link com.online.auction.model.User} entity based on the user's ID.
     * </p>
     *
     * @param id The ID of the user for whom valid tokens are to be retrieved.
     * @return A list of {@link Token} that are valid for the specified user.
     */
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);

    /**
     * Finds a token by its value.
     * <p>
     * This method retrieves a token based on its string representation.
     * </p>
     *
     * @param token The value of the token to be retrieved.
     * @return An {@link Optional} containing the {@link Token} if it exists, or empty if not found.
     */
    Optional<Token> findByToken(String token);
}
