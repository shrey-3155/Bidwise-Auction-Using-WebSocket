package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.AccountService;
import com.online.auction.service.MembershipService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.USER_NOT_PRESENT_MSG;

/**
 * Service implementation for handling the membership related operations.
 */
@Service
@AllArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {
    private final UserRepository userRepository;
    private final AccountService accountService;

    /**
     * Upgrades a user to premium status.
     *
     * @param email the user email to upgrade
     * @return the email of the upgraded user
     * @throws ServiceException if the user is not found
     */
    public String upgradeToPremium(String email) throws ServiceException {
        log.info("Upgrading user to premium");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, USER_NOT_PRESENT_MSG));
        user.setPremium(true);
        userRepository.save(user);

        accountService.addFunds(user.getUserId(), 100);

        return email;
    }

    /**
     * Checks if the given user has a premium account.
     *
     * @param user The user object containing the email to be checked.
     * @return {@code true} if the user has a premium account, {@code false} otherwise.
     * @throws ServiceException if the user is not found in the database.
     */
    @Override
    public Boolean isPremium(User user) throws ServiceException {
        log.info("Checking if the user is Premium for : {}", user);
        Optional<User> userDbOptional = userRepository.findByEmail(user.getEmail());
        if (userDbOptional.isEmpty()) {
            log.error("User not Found for the given details: {}", user);
            throw new ServiceException(HttpStatus.BAD_REQUEST, USER_NOT_PRESENT_MSG);
        }
        log.info("The user premium status is:{}", userDbOptional.get().isPremium());
        return userDbOptional.get().isPremium();
    }

    @Override
    public void cancelPremium(String email) throws ServiceException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, USER_NOT_PRESENT_MSG));

        user.setPremium(false);
        userRepository.save(user);
    }
}
