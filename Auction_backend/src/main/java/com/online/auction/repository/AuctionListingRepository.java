package com.online.auction.repository;

import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Auction} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link Auction} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface AuctionListingRepository extends JpaRepository<Auction, Integer> {
    /**
     * Finds an auction by its associated {@link Item}.
     * <p>
     * This method retrieves an {@link Auction} entity that is associated with the specified {@link Item}.
     * </p>
     *
     * @param items The {@link Item} for which to find the associated auction.
     * @return An {@link Optional} containing the {@link Auction} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<Auction> findByItems(Item items);

    /**
     * Deletes auctions associated with the specified {@link Item}.
     * <p>
     * This method removes all {@link Auction} entities that are associated with the given {@link Item}.
     * </p>
     *
     * @param item The {@link Item} whose associated auctions are to be deleted.
     */
    @Transactional
    void deleteByItems(Item item);

    /**
     * Finds an auction by the ID of its associated {@link Item}.
     * <p>
     * This method retrieves an {@link Auction} entity that is associated with the specified {@link Item} ID.
     * </p>
     *
     * @param itemId The ID of the {@link Item} for which to find the associated auction.
     * @return An {@link Optional} containing the {@link Auction} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<Auction> findByItems_ItemId(int itemId);

    /**
     * Finds all auctions that are upcoming or currently open.
     * <p>
     * This method retrieves a list of {@link Auction} entities where the auction's start time is in the future,
     * or if the auction is currently open (i.e., `isOpen` is true and the end time is in the future).
     * </p>
     *
     * @param currentTime The current time used to filter auctions based on their start and end times.
     * @return A list of {@link Auction} entities that are upcoming or currently open.
     */
    @Query("SELECT a FROM Auction a WHERE (a.startTime > :currentTime OR (a.isOpen = true AND a.endTime > :currentTime))")
    List<Auction> findUpcomingAndCurrentAuctions(LocalDateTime currentTime);

    @Query("SELECT DISTINCT i.itemcategory.itemCategoryId FROM AuctionBidDetails abd " +
            "JOIN abd.itemId i WHERE abd.bidderId.userId = :userId")
    List<Integer> findDistinctCategoryIdsByUserId(@Param("userId") int userId);

}