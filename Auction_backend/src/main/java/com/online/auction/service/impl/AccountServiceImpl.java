package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.repository.AccountRepository;
import com.online.auction.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AccountService interface.
 * Provides methods to manage account operations such as retrieving the account balance and adding funds.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    /**
     * Retrieves the account balance for a given user ID.
     *
     * @param userId the ID of the user whose account balance is to be retrieved
     * @return the account balance
     * @throws ServiceException if the account is not found for the given user ID
     */
    @Override
    public double getAccountBalance(Integer userId) throws ServiceException {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Account not found for user id: " + userId);
        }
        return account.getFunds();
    }

    /**
     * Adds funds to the account for a given user ID.
     *
     * @param userId the ID of the user to whom funds are to be added
     * @param amount the amount of funds to be added
     * @throws ServiceException if the account is not found for the given user ID
     */
    @Override
    public void addFunds(Integer userId, float amount) throws ServiceException {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Account not found for user id: " + userId);
        }

        account.setFunds(account.getFunds() + amount);
        accountRepository.save(account);
    }
}
