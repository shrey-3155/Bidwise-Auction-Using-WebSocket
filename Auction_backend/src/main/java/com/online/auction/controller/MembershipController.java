package com.online.auction.controller;

import com.online.auction.dto.PaymentDetailsDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.MEMBERSHIP;

/**
 * Controller for managing membership-related operations, such as upgrading to premium, checking premium status, and canceling premium subscriptions.
 */
@RestController
@RequestMapping(API_VERSION_V1 + MEMBERSHIP)
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    /**
     * Upgrades a user to a premium membership.
     *
     * @param paymentDetails Contains the details necessary for upgrading to premium, including the user's email.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} indicating the success of the upgrade operation.
     * @throws ServiceException If there is an error during the upgrade process.
     */
    @PostMapping("/upgrade-to-premium")
    public ResponseEntity<SuccessResponse<String>> upgradeToPremium(@RequestBody PaymentDetailsDTO paymentDetails) throws ServiceException {
        membershipService.upgradeToPremium(paymentDetails.getEmail());
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, "User upgraded to premium successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the premium status of the authenticated user.
     *
     * @param user The authenticated user whose premium status is to be checked.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with a boolean indicating whether the user has a premium membership.
     * @throws ServiceException If there is an error retrieving the user's premium status.
     */
    @GetMapping("/checkPremium")
    public ResponseEntity<SuccessResponse<Boolean>> getPremiumStatus(@AuthenticationPrincipal User user) throws ServiceException {
        Boolean isPremium = membershipService.isPremium(user);
        SuccessResponse<Boolean> successResponse = new SuccessResponse<>(200, HttpStatus.OK, isPremium);
        return ResponseEntity.ok(successResponse);
    }

    /**
     * Cancels the premium subscription for the authenticated user.
     *
     * @param user The authenticated user whose premium subscription is to be canceled.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} indicating the success of the cancellation operation.
     * @throws ServiceException If there is an error during the cancellation process.
     */
    @PostMapping("/cancelPremium")
    public ResponseEntity<SuccessResponse<String>> cancelPremium(@AuthenticationPrincipal User user) throws ServiceException {
        membershipService.cancelPremium(user.getEmail());
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, "Subscription canceled successfully");
        return ResponseEntity.ok(response);
    }
}
