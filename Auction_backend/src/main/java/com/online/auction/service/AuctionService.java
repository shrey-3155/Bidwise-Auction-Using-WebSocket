package com.online.auction.service;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.dto.SuggestedItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

import java.util.List;

/**
 * Service interface for managing auction-related operations.
 * <p>
 * This interface provides methods for retrieving auction details, processing auction states,
 * and fetching auction listings for users.
 * </p>
 */
public interface AuctionService {
    /**
     * Retrieves the details of an auction based on the item ID.
     * <p>
     * This method fetches the details of an auction associated with a specific item.
     * </p>
     *
     * @param itemId The unique identifier of the item for which auction details are to be retrieved.
     * @return An {@link AuctionDTO} object containing details of the auction.
     * @throws ServiceException If an error occurs while retrieving the auction details.
     */
    AuctionDTO getAuctionDetails(int itemId) throws ServiceException;

    /**
     * Processes the post-auction state for a specific item.
     * <p>
     * This method handles the state changes or updates required after an auction has concluded
     * for the specified item.
     * </p>
     *
     * @param itemId The unique identifier of the item for which the post-auction state is to be processed.
     * @return A boolean indicating whether the post-auction processing was successful.
     * @throws ServiceException If an error occurs while processing the post-auction state.
     */
    boolean processPostAuctionState(int itemId) throws ServiceException;

    /**
     * Retrieves a list of upcoming auctions.
     * <p>
     * This method fetches a list of auctions that are scheduled to take place in the future.
     * </p>
     *
     * @return A list of {@link AuctionItemsDTO} objects representing upcoming auctions.
     */
    List<AuctionItemsDTO> getUpcomingAuctions();

    /**
     * Retrieves a list of auctions for an existing user based on the seller ID.
     * <p>
     * This method fetches a list of auctions associated with a specific user who is a seller.
     * </p>
     *
     * @param sellerId The unique identifier of the seller whose auctions are to be retrieved.
     * @return A list of {@link AuctionItemsDTO} objects representing auctions for the specified seller.
     * @throws ServiceException If an error occurs while retrieving the auctions for the seller.
     */
    List<AuctionItemsDTO> getAuctionsForExistingUser(int sellerId) throws ServiceException;

    List<SuggestedItemDTO> getSuggestedItems(User user) throws ServiceException;

}
