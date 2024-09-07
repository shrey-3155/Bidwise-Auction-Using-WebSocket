package com.online.auction.controller;

import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.online.auction.constant.AuctionConstants.ACCOUNT;
import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;

/**
 * REST controller for managing account-related operations.
 */
@RestController
@RequestMapping(API_VERSION_V1 + ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Retrieves the account balance for the authenticated user.
     *
     * @param user The authenticated user.
     * @return A ResponseEntity containing the account balance wrapped in a SuccessResponse.
     * @throws ServiceException If there is an error retrieving the account balance.
     */
    @GetMapping("/balance")
    public ResponseEntity<SuccessResponse<Double>> getAccountBalance(
            @AuthenticationPrincipal User user) throws ServiceException {
        double balance = accountService.getAccountBalance(user.getUserId());
        SuccessResponse<Double> response = new SuccessResponse<>(200, HttpStatus.OK, balance);
        return ResponseEntity.ok(response);

    }

    /**
     * Adds funds to the account of the authenticated user.
     *
     * @param user   The authenticated user.
     * @param amount The amount to add to the account.
     * @return A ResponseEntity containing a success message wrapped in a SuccessResponse.
     * @throws ServiceException If there is an error adding funds to the account.
     */
    @PostMapping("/addFunds")
    public ResponseEntity<SuccessResponse<String>> addFunds(
            @AuthenticationPrincipal User user,
            @RequestParam("amount") float amount) throws ServiceException {
        accountService.addFunds(user.getUserId(), amount);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, "Funds added successfully");
        return ResponseEntity.ok(response);
    }
}
