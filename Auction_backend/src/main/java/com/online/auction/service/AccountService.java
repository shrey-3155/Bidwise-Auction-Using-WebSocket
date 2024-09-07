package com.online.auction.service;

import com.online.auction.exception.ServiceException;

/**
 * Service interface for managing user accounts.
 * <p>
 * This interface defines methods for interacting with user account balances,
 * including retrieving the current balance and adding funds to the account.
 * </p>
 */
public interface AccountService {
    /**
     * Retrieves the current balance of the specified user's account.
     * <p>
     * This method fetches the account balance for a user identified by their unique user ID.
     * </p>
     *
     * @param userId The unique identifier of the user whose account balance is to be retrieved.
     * @return The current balance of the user's account.
     * @throws ServiceException If an error occurs while retrieving the account balance.
     */
    double getAccountBalance(Integer userId) throws ServiceException;

    /**
     * Adds funds to the specified user's account.
     * <p>
     * This method updates the account balance for a user identified by their unique user ID
     * by adding the specified amount of funds.
     * </p>
     *
     * @param userId The unique identifier of the user to whom the funds will be added.
     * @param amount The amount of funds to be added to the user's account.
     * @throws ServiceException If an error occurs while adding funds to the account.
     */
    void addFunds(Integer userId, float amount) throws ServiceException;
}
