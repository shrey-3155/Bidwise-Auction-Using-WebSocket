package com.online.auction.repository;

import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link AuctionBidDetails} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link AuctionBidDetails} entities, with the primary key of type {@code Integer}.
 * It includes methods to retrieve bid details by user and by auction ID with specific conditions.
 * </p>
 */
public interface TradebookRepository extends JpaRepository<AuctionBidDetails, Integer> {
    /**
     * Finds all bid details for a specific user where the bid amount is the maximum for each auction.
     * <p>
     * This method retrieves all bid details where the bid amount is the highest for a given auction and
     * the bidder is the specified user. It ensures that the user's highest bid for each auction is returned.
     * </p>
     *
     * @param user The user whose bids are to be retrieved.
     * @return A list of {@link AuctionBidDetails} where the user has placed the highest bid for each auction.
     */
    @Query("SELECT abd FROM AuctionBidDetails abd WHERE abd.bidderId = :user AND abd.bid_amount = (SELECT MAX(bid.bid_amount) FROM AuctionBidDetails bid WHERE bid.auctionId = abd.auctionId AND bid.bidderId = :user)")
    List<AuctionBidDetails> findAllByUser(User user);

    /**
     * Finds the winning bid details for a specific auction.
     * <p>
     * This method retrieves the bid details for a given auction where the bid is marked as won.
     * </p>
     *
     * @param auctionId The ID of the auction for which the winning bid details are to be retrieved.
     * @return An {@link Optional} containing the {@link AuctionBidDetails} if a winning bid exists, or empty if no such bid is found.
     */
    @Query("SELECT abd FROM AuctionBidDetails abd WHERE abd.auctionId.auctionId = :auctionId AND abd.isWon = true")
    Optional<AuctionBidDetails> findByAuctionIdAndIsWonTrue(int auctionId);

}
