package com.online.auction.repository;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link User} entities and related profile data.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link User} entities, with the primary key of type {@code Integer}.
 * It includes methods to retrieve user profile details, count user participation in auctions,
 * and get bid statistics categorized by item categories.
 * </p>
 */
public interface ProfileRepository extends JpaRepository<User, Integer> {

    /**
     * Finds user profile details by user ID.
     * <p>
     * This method retrieves user profile information including user ID, first name, last name, email,
     * city name, and country name, based on the provided user ID.
     * </p>
     *
     * @param userId The ID of the user whose profile information is to be retrieved.
     * @return A {@link UserProfileDTO} containing the user's profile details.
     */
    @Query("SELECT new com.online.auction.dto.UserProfileDTO(u.userId, u.firstName, u.lastName, u.email, c.cityName, co.countryName) " +
            "FROM User u " +
            "JOIN u.city c " +
            "JOIN c.country co " +
            "WHERE u.userId = :userId")
    UserProfileDTO findUserProfileByUserId(Integer userId);

    /**
     * Counts the number of distinct auctions participated in by a user.
     * <p>
     * This method calculates the number of unique auctions where the specified user has placed bids.
     * </p>
     *
     * @param userId The ID of the user whose auction participation is to be counted.
     * @return The number of distinct auctions the user has participated in.
     */
    @Query("SELECT COUNT(DISTINCT abd.auctionId) " +
            "FROM AuctionBidDetails abd " +
            "WHERE abd.bidderId.userId = :userId")
    long countUserParticipatedAuctions(Integer userId);

    /**
     * Finds bid statistics by user ID for the current year.
     * <p>
     * This method retrieves bid statistics grouped by month for the current year, including the number of
     * bids won and lost by the specified user.
     * </p>
     *
     * @param userId The ID of the user for whom bid statistics are to be retrieved.
     * @return A list of {@link BidStatsDTO} containing the bid statistics for each month of the current year.
     */
    @Query("SELECT new com.online.auction.dto.BidStatsDTO(FUNCTION('MONTH', abd.bidTime), " +
            "SUM(CASE WHEN abd.isWon = true THEN 1 ELSE 0 END), " +  // wonAuctions
            "COUNT(DISTINCT abd.auctionId)) " +  // totalParticipatedAuctions
            "FROM AuctionBidDetails abd " +
            "WHERE abd.bidderId.userId = :userId " +
            "AND FUNCTION('YEAR', abd.bidTime) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', abd.bidTime)")
    List<BidStatsDTO> findBidStatsByUserIdForCurrentYear(Integer userId);

    /**
     * Finds bid statistics categorized by item categories for a user.
     * <p>
     * This method retrieves the number of bids placed by the specified user for each item category.
     * The results are grouped by item category.
     * </p>
     *
     * @param userId The ID of the user for whom category-based bid statistics are to be retrieved.
     * @return A list of {@link CategoryBidStatsDTO} containing the bid counts for each item category.
     */
    @Query("SELECT new com.online.auction.dto.CategoryBidStatsDTO(ic.itemCategoryName, COALESCE(COUNT(abd.auction_bid_details_id), 0)) " +
            "FROM ItemCategory ic " +
            "LEFT JOIN Item i ON ic.itemCategoryId = i.itemcategory.itemCategoryId " +
            "LEFT JOIN AuctionBidDetails abd ON i.itemId = abd.itemId.itemId AND abd.bidderId.userId = :userId " +
            "GROUP BY ic.itemCategoryName")
    List<CategoryBidStatsDTO> findCategoryBidStatsByUserId(Integer userId);
}
