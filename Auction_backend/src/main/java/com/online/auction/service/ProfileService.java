package com.online.auction.service;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;

import java.util.List;

/**
 * Service interface for managing user profile-related operations.
 * <p>
 * This interface provides methods for retrieving user profiles, counting participated auctions, and fetching bid statistics.
 * </p>
 */
public interface ProfileService {
    /**
     * Retrieves the profile details for a specific user.
     * <p>
     * This method returns a {@link UserProfileDTO} object containing the profile information for the user identified by the provided user ID.
     * </p>
     *
     * @param userId The ID of the user whose profile is to be retrieved.
     * @return A {@link UserProfileDTO} object containing the user's profile details.
     * @throws ServiceException If there is an error retrieving the user profile or if the user cannot be found.
     */
    UserProfileDTO getUserProfile(Integer userId) throws ServiceException;

    /**
     * Counts the number of auctions participated in by a specific user.
     * <p>
     * This method returns the count of distinct auctions where the user identified by the provided user ID has placed bids.
     * </p>
     *
     * @param userId The ID of the user whose auction participation count is to be retrieved.
     * @return The number of distinct auctions the user has participated in.
     * @throws ServiceException If there is an error counting the user's participated auctions or if the user cannot be found.
     */
    long countUserParticipatedAuctions(Integer userId) throws ServiceException;


    /**
     * Retrieves bid statistics for a specific user.
     * <p>
     * This method returns a list of {@link BidStatsDTO} objects containing bid statistics for the user identified by the provided user ID.
     * The statistics include information on the number of won and lost bids grouped by month for the current year.
     * </p>
     *
     * @param userId The ID of the user whose bid statistics are to be retrieved.
     * @return A list of {@link BidStatsDTO} objects containing the user's bid statistics for the current year.
     * @throws ServiceException If there is an error retrieving the bid statistics or if the user cannot be found.
     */
    List<BidStatsDTO> getBidStats(Integer userId) throws ServiceException;

    /**
     * Retrieves category-based bid statistics for a specific user.
     * <p>
     * This method returns a list of {@link CategoryBidStatsDTO} objects containing bid statistics grouped by item category for the user
     * identified by the provided user ID.
     * </p>
     *
     * @param userId The ID of the user whose category-based bid statistics are to be retrieved.
     * @return A list of {@link CategoryBidStatsDTO} objects containing the user's bid statistics grouped by item category.
     * @throws ServiceException If there is an error retrieving the category-based bid statistics or if the user cannot be found.
     */
    List<CategoryBidStatsDTO> getCategoryBidStats(Integer userId) throws ServiceException;

}
