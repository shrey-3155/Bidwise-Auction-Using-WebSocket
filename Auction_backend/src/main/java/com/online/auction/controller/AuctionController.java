package com.online.auction.controller;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.dto.SuggestedItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.AUCTION_MAPPING;

/**
 * Controller for handling auction-related operations.
 * Provides endpoints for retrieving auction details, processing post-auction updates,
 * fetching upcoming auctions, and retrieving items for existing users.
 */
@RestController
@AllArgsConstructor
@RequestMapping(API_VERSION_V1 + AUCTION_MAPPING)
public class AuctionController {
    private final AuctionService auctionService;

    /**
     * Retrieves the details of an auction for a given item ID.
     *
     * @param itemId The ID of the item for which auction details are to be retrieved.
     * @return A ResponseEntity containing a SuccessResponse with the auction details.
     * @throws ServiceException If there is an error retrieving the auction details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<AuctionDTO>> auctionDetails(@PathVariable(value = "id") Integer itemId) throws ServiceException {
        AuctionDTO auction = auctionService.getAuctionDetails(itemId);
        SuccessResponse<AuctionDTO> successResponse = new SuccessResponse<>(200, HttpStatus.OK, auction);
        return ResponseEntity.ok(successResponse);
    }

    /**
     * Processes post-auction updates for a given item ID.
     *
     * @param itemId The ID of the item for which post-auction updates are to be processed.
     * @return A ResponseEntity containing a SuccessResponse indicating whether the update was successful.
     * @throws ServiceException If there is an error processing the post-auction updates.
     */
    @GetMapping("/postAuction/{itemId}")
    public ResponseEntity<SuccessResponse<Boolean>> postAuctionUpdate(@PathVariable(value = "itemId") Integer itemId) throws ServiceException {
        boolean isSuccess = auctionService.processPostAuctionState(itemId);
        SuccessResponse<Boolean> successResponse = new SuccessResponse<>(200, HttpStatus.OK, isSuccess);
        return ResponseEntity.ok(successResponse);
    }

    /**
     * Retrieves a list of upcoming auctions.
     *
     * @return A list of AuctionItemsDTO representing the upcoming auctions.
     * @throws ServiceException If there is an error retrieving the upcoming auctions.
     */
    @GetMapping("/upcoming")
    public List<AuctionItemsDTO> getUpcomingAuctions() throws ServiceException {
        return auctionService.getUpcomingAuctions();
    }

    /**
     * Retrieves a list of auction items for the currently authenticated user.
     *
     * @param user The authenticated user for whom auction items are to be retrieved.
     * @return A ResponseEntity containing a list of AuctionItemsDTO representing the user's auction items.
     * @throws ServiceException If there is an error retrieving the auction items.
     */
    @GetMapping("/getAuction")
    public ResponseEntity<List<AuctionItemsDTO>> getItemsForExistingUser(@AuthenticationPrincipal User user
    ) throws ServiceException {
        List<AuctionItemsDTO> items = auctionService.getAuctionsForExistingUser(user.getUserId());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/suggested")
    public ResponseEntity<SuccessResponse<List<SuggestedItemDTO>>> getSuggestedItems(@AuthenticationPrincipal
                                                                                     User user) throws ServiceException {
        List<SuggestedItemDTO> suggestedItems = auctionService.getSuggestedItems(user);
        SuccessResponse<List<SuggestedItemDTO>> successResponse = new SuccessResponse<>(200, HttpStatus.OK, suggestedItems);
        return ResponseEntity.ok(successResponse);
    }

}
