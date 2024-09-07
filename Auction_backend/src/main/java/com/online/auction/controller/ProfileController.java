package com.online.auction.controller;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.PROFILE;

/**
 * Controller for managing user profile-related operations, including fetching user details, counting participated auctions, and retrieving bid statistics.
 */
@RestController
@RequestMapping(API_VERSION_V1 + PROFILE)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Retrieves the profile details of the authenticated user.
     *
     * @param user The authenticated user whose profile details are to be fetched.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the user's profile details.
     * @throws ServiceException If there is an error retrieving the user's profile details.
     */
    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<UserProfileDTO>> getUserProfile(
            @AuthenticationPrincipal User user) throws ServiceException {
        UserProfileDTO userProfile = profileService.getUserProfile(user.getUserId());
        SuccessResponse<UserProfileDTO> response = new SuccessResponse<>(200, HttpStatus.OK, userProfile);
        return ResponseEntity.ok(response);
    }

    /**
     * Counts the number of auctions the authenticated user has participated in.
     *
     * @param user The authenticated user whose auction participation count is to be retrieved.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the count of auctions the user has participated in.
     * @throws ServiceException If there is an error retrieving the auction count.
     */
    @GetMapping("/auctionsParticipated")
    public ResponseEntity<SuccessResponse<Long>> countUserParticipatedAuctions(
            @AuthenticationPrincipal User user) throws ServiceException {
        long auctionCount = profileService.countUserParticipatedAuctions(user.getUserId());
        SuccessResponse<Long> response = new SuccessResponse<>(200, HttpStatus.OK, auctionCount);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the bid statistics for the authenticated user.
     *
     * @param user The authenticated user whose bid statistics are to be fetched.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the user's bid statistics.
     * @throws ServiceException If there is an error retrieving the bid statistics.
     */
    @GetMapping("/bidStats")
    public ResponseEntity<SuccessResponse<List<BidStatsDTO>>> getBidStats(
            @AuthenticationPrincipal User user) throws ServiceException {
        List<BidStatsDTO> bidStats = profileService.getBidStats(user.getUserId());
        SuccessResponse<List<BidStatsDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, bidStats);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the category-wise bid statistics for the authenticated user.
     *
     * @param user The authenticated user whose category-wise bid statistics are to be fetched.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the user's category-wise bid statistics.
     * @throws ServiceException If there is an error retrieving the category-wise bid statistics.
     */
    @GetMapping("/categoryBidStats")
    public ResponseEntity<SuccessResponse<List<CategoryBidStatsDTO>>> getCategoryBidStats(
            @AuthenticationPrincipal User user) throws ServiceException {
        List<CategoryBidStatsDTO> categoryBidStats = profileService.getCategoryBidStats(user.getUserId());
        SuccessResponse<List<CategoryBidStatsDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, categoryBidStats);
        return ResponseEntity.ok(response);
    }
}
