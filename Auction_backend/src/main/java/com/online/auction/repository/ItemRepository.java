package com.online.auction.repository;

import com.online.auction.model.Item;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Item} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link Item} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * Finds all items associated with a specific seller.
     * <p>
     * This method retrieves a list of {@link Item} entities where the {@code sellerId} matches the specified {@link User}.
     * </p>
     *
     * @param sellerId The {@link User} representing the seller of the items.
     * @return A list of {@link Item} entities associated with the specified seller.
     */
    List<Item> findBySellerId(User sellerId);

    /**
     * Finds an item by its ID.
     * <p>
     * This method retrieves an {@link Item} entity with the specified ID.
     * </p>
     *
     * @param id The ID of the item to be found.
     * @return An {@link Optional} containing the {@link Item} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<Item> findById(Integer id);

    /**
     * Finds upcoming and current items excluding those listed by a specific seller.
     * <p>
     * This method retrieves a list of {@link Item} entities that are associated with active or upcoming auctions,
     * excluding items listed by the specified seller.
     * </p>
     *
     * @param currentTime The current time to be used for filtering auctions.
     * @param seller      The {@link User} representing the seller whose items should be excluded.
     * @return A list of {@link Item} entities that are part of upcoming or current auctions, excluding those by the specified seller.
     */
    @Query("SELECT i FROM Item i JOIN Auction a ON i.itemId = a.items.itemId WHERE (a.startTime > :currentTime OR (a.isOpen = true AND a.endTime > :currentTime)) AND i.sellerId != :seller")
    List<Item> findUpcomingAndCurrentItemsExcludingUserItems(LocalDateTime currentTime, User seller);

    /**
     * Finds items by their item ID.
     * <p>
     * This method retrieves a list of {@link Item} entities with the specified item ID.
     * </p>
     *
     * @param itemId The ID of the item to be found.
     * @return A list of {@link Item} entities with the specified item ID.
     */
    List<Item> findByItemId(Integer itemId);

    @Query("SELECT i FROM Item i WHERE i.itemcategory.itemCategoryId IN :categoryIds " +
            "AND i.itemId NOT IN (SELECT abd.itemId.itemId FROM AuctionBidDetails abd WHERE abd.bidderId.userId = :userId)")
    List<Item> findItemsNotBidByUserInCategories(@Param("userId") int userId, @Param("categoryIds") List<Integer> categoryIds);
}
