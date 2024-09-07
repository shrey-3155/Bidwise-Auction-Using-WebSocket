package com.online.auction.repository;

import com.online.auction.model.AuctionBidDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link AuctionBidDetails} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link AuctionBidDetails} entities, with the primary key of type {@code Integer}.
 * </p>
 */
@Repository
public interface AuctionBidDetailRepository extends JpaRepository<AuctionBidDetails, Integer> {
    /**
     * Retrieves the highest bid for a specific item based on the bid amount.
     * <p>
     * This method uses a native SQL query to find the bid with the highest amount for a given item ID.
     * The result is ordered in descending order by bid amount, and only the top record is returned.
     * </p>
     *
     * @param itemId The ID of the item for which the highest bid is to be retrieved.
     * @return The {@link AuctionBidDetails} entity representing the highest bid for the specified item,
     * or {@code null} if no bids are found for the item.
     */
    @Query(value = "SELECT * FROM auction_bid_detail WHERE item_id = :itemId ORDER BY bid_amount DESC LIMIT 1", nativeQuery = true)
    AuctionBidDetails findTopByItemIdOrderByBidAmountDesc(@Param("itemId") int itemId);
}
