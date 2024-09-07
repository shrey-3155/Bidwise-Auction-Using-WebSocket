package com.online.auction.service.impl;

import com.online.auction.dto.InvoiceDTO;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.TradebookRepository;
import com.online.auction.service.TradebookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.online.auction.constant.AuctionConstants.NO_TRADES_FOUND;

/**
 * Service implementation for handling tradebook-related operations.
 * This service provides methods for retrieving user trades and generating invoices.
 */
@Service
@AllArgsConstructor
@Slf4j
public class TradebookServiceImpl implements TradebookService {
    private final TradebookRepository tradebookRepository;

    /**
     * Retrieves all trades associated with the given user.
     *
     * @param user The user whose trades are to be fetched.
     * @return A list of TradebookDTOs representing the user's trades.
     * @throws ServiceException If there is an error retrieving the trades or if no trades are found.
     */
    @Override
    public List<TradebookDTO> getAllTradesByUser(User user) throws ServiceException {
        log.debug("Fetching items for user: {}", user.getEmail());
        List<AuctionBidDetails> bids;
        bids = tradebookRepository.findAllByUser(user);


        if (bids == null || bids.isEmpty()) {
            log.error("No trades found for user: {}", user.getEmail());
            throw new ServiceException(HttpStatus.NOT_FOUND, NO_TRADES_FOUND);
        }
        return bids.stream()
                .map(bid -> TradebookDTO.builder()
                        .nameOfItem(bid.getItemId().getItem_name())
                        .soldPrice(bid.getItemId().getSelling_amount())
                        .userHighestBid(bid.getBid_amount())
                        .dateOfAuction(bid.getAuctionId().getEndTime())
                        .isAuctionWon(bid.isWon())
                        .AuctionId(bid.getAuctionId().getAuctionId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the invoice details for a given auction ID.
     *
     * @param auctionId The ID of the auction.
     * @return An InvoiceDTO containing the invoice details.
     * @throws ServiceException If there is no winning bid for the given auction ID.
     */
    @Override
    public InvoiceDTO getInvoiceByAuctionId(int auctionId) throws ServiceException {
        log.debug("Fetching invoice for Auction id: {}", auctionId);
        AuctionBidDetails bidDetails = tradebookRepository.findByAuctionIdAndIsWonTrue(auctionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "No winning bid found for the given auction ID"));

        log.debug("Found winning bids for Auction id: {}", auctionId);
        Auction auction = bidDetails.getAuctionId();
        Item item = bidDetails.getItemId();
        User seller = item.getSellerId();

        log.debug("Returning invoice details for Auction id: {}", auctionId);
        return InvoiceDTO.builder()
                .itemName(item.getItem_name())
                .sellerName(seller.getFirstName() + " " + seller.getLastName())
                .sellerEmail(seller.getEmail())
                .itemCategory(item.getItemcategory().getItemCategoryName())
                .dateOfAuction(auction.getEndTime())
                .dateOfInvoice(LocalDateTime.now())
                .pricePaid(bidDetails.getBid_amount())
                .build();
    }
}
