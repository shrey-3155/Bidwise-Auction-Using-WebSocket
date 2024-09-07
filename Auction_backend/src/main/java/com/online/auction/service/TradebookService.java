package com.online.auction.service;

import com.online.auction.dto.InvoiceDTO;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

import java.util.List;

/**
 * Service interface for managing tradebook-related operations.
 * <p>
 * This interface provides methods for retrieving trade records for a user and generating invoices for auctions.
 * </p>
 */
public interface TradebookService {
    /**
     * Retrieves all trade records for a specific user.
     * <p>
     * This method returns a list of {@link TradebookDTO} objects containing the trade details for the user identified by the provided {@link User} object.
     * </p>
     *
     * @param user The {@link User} whose trade records are to be retrieved.
     * @return A list of {@link TradebookDTO} objects containing the user's trade records.
     * @throws ServiceException If there is an error retrieving the trade records or if the user cannot be found.
     */
    List<TradebookDTO> getAllTradesByUser(User user) throws ServiceException;

    /**
     * Retrieves the invoice details for a specific auction.
     * <p>
     * This method returns an {@link InvoiceDTO} object containing the invoice details for the auction identified by the provided auction ID.
     * </p>
     *
     * @param auctionId The ID of the auction for which the invoice is to be retrieved.
     * @return An {@link InvoiceDTO} object containing the invoice details for the specified auction.
     * @throws ServiceException If there is an error retrieving the invoice or if the auction cannot be found.
     */
    InvoiceDTO getInvoiceByAuctionId(int auctionId) throws ServiceException;

}
