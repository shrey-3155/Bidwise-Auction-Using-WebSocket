package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

/**
 * Service interface for managing membership-related operations.
 * <p>
 * This interface provides methods for upgrading to a premium membership, checking premium status, and canceling a premium membership.
 * </p>
 */
public interface MembershipService {
    /**
     * Upgrades the specified user to a premium membership.
     * <p>
     * This method processes the upgrade request for the user identified by the provided email address.
     * If the upgrade is successful, a confirmation message or identifier is returned.
     * </p>
     *
     * @param email The email address of the user to be upgraded to premium.
     * @return A confirmation message or identifier related to the upgrade process.
     * @throws ServiceException If there is an error during the upgrade process or if the user cannot be found.
     */
    String upgradeToPremium(String email) throws ServiceException;

    /**
     * Checks if the specified user has a premium membership.
     * <p>
     * This method verifies the membership status of the user identified by the provided {@link User} object.
     * </p>
     *
     * @param user The {@link User} object representing the user whose membership status is to be checked.
     * @return {@code true} if the user has a premium membership; {@code false} otherwise.
     * @throws ServiceException If there is an error while checking the membership status.
     */
    Boolean isPremium(User user) throws ServiceException;

    /**
     * Cancels the premium membership of the specified user.
     * <p>
     * This method processes the cancellation request for the user identified by the provided email address.
     * </p>
     *
     * @param email The email address of the user whose premium membership is to be canceled.
     * @throws ServiceException If there is an error during the cancellation process or if the user cannot be found.
     */
    void cancelPremium(String email) throws ServiceException;

}
