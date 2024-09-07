package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.ITEM_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.USER_NOT_PRESENT_MSG;
import static com.online.auction.constant.TestConstants.AUCTION_BID_LESS_THAN_HIGHEST_BID_ERROR_MSG;
import static com.online.auction.constant.TestConstants.BID_AMOUNT_ONE_HUNDRED;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.INTEGER_TWO_HUNDRED;
import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BidServiceImplTest {

    @Mock
    private AuctionBidDetailRepository auctionBidDetailRepository;

    @Mock
    private AuctionListingRepository auctionListingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @SneakyThrows
    void processBidSuccessTest() {
        Auction auction = new Auction();
        Item item = new Item();
        User user = new User();

        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE))
                .thenReturn(Optional.of(auction));
        when(itemRepository.findById(INTEGER_ONE))
                .thenReturn(Optional.of(item));
        when(userRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(user));

        bidService.processBid(BID_AMOUNT_ONE_HUNDRED, String.valueOf(INTEGER_ONE), TEST_EMAIL);

        verify(auctionBidDetailRepository, times(1))
                .save(any(AuctionBidDetails.class));
    }

    @Test
    void processBid_whenBidAmountLowerThanHighestBid() {
        AuctionBidDetails auctionBidDetails = new AuctionBidDetails();
        auctionBidDetails.setBid_amount(INTEGER_TWO_HUNDRED);

        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(auctionBidDetails);

        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE))
                .thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            bidService.processBid(BID_AMOUNT_ONE_HUNDRED, String.valueOf(INTEGER_ONE), TEST_EMAIL);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_BID_LESS_THAN_HIGHEST_BID_ERROR_MSG, exception.getErrorMessage());

        verify(auctionBidDetailRepository, never())
                .save(any(AuctionBidDetails.class));
    }

    @Test
    void processBid_auctionNotFound() {

        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE))
                .thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            bidService.processBid(BID_AMOUNT_ONE_HUNDRED, String.valueOf(INTEGER_ONE), TEST_EMAIL);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_NOT_FOUND_MSG, exception.getErrorMessage());

        verify(auctionBidDetailRepository, never())
                .save(any(AuctionBidDetails.class));
    }

    @Test
    void processBidItemNotFoundTest() {

        Auction auction = new Auction();

        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE))
                .thenReturn(Optional.of(auction));
        when(itemRepository.findById(INTEGER_ONE))
                .thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            bidService.processBid(BID_AMOUNT_ONE_HUNDRED, String.valueOf(INTEGER_ONE), TEST_EMAIL);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ITEM_NOT_FOUND_MSG, exception.getErrorMessage());

        verify(auctionBidDetailRepository, never())
                .save(any(AuctionBidDetails.class));
    }

    @Test
    void processBid_userNotFound() {
        Auction auction = new Auction();
        Item item = new Item();

        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE))
                .thenReturn(Optional.of(auction));
        when(itemRepository.findById(INTEGER_ONE))
                .thenReturn(Optional.of(item));
        when(userRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            bidService.processBid(BID_AMOUNT_ONE_HUNDRED, String.valueOf(INTEGER_ONE), TEST_EMAIL);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(USER_NOT_PRESENT_MSG, exception.getErrorMessage());

        verify(auctionBidDetailRepository, never())
                .save(any(AuctionBidDetails.class));
    }
}