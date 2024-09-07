package com.online.auction.service;

import com.online.auction.exception.ServiceException;

/**
 * Service interface for managing bids in the auction system.
 * <p>
 * This interface provides methods for processing bids placed by users on auction items.
 * </p>
 */
public interface BidService {
    /**
     * Processes a bid placed by a user on a specific item.
     * <p>
     * This method handles the logic for submitting a bid, including validating the bid amount,
     * ensuring the item ID is correct, and associating the bid with the user's email address.
     * </p>
     *
     * @param bidAmount The amount of the bid being placed.
     * @param itemId    The unique identifier of the item being bid on.
     * @param userEmail The email address of the user placing the bid.
     * @throws ServiceException If an error occurs while processing the bid, such as validation failures
     *                          or issues with the auction or item.
     */
    void processBid(String bidAmount, String itemId, String userEmail) throws ServiceException;
}
