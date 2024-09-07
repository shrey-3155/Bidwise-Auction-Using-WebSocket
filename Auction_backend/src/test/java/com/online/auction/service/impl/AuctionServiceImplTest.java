package com.online.auction.service.impl;


import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.dto.SuggestedItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.City;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.ItemCondition;
import com.online.auction.model.User;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.TestConstants.AUCTION_CITY_NAME;
import static com.online.auction.constant.TestConstants.AUCTION_USD_CURRENCY;
import static com.online.auction.constant.TestConstants.AUCTION_ITEM_DESCRIPTION;
import static com.online.auction.constant.TestConstants.AUCTION_ITEM_MAKER;
import static com.online.auction.constant.TestConstants.AUCTION_PHOTO_URL;
import static com.online.auction.constant.TestConstants.AUCTION_PRICE_PAID;
import static com.online.auction.constant.TestConstants.AUCTION_RECORD_NOT_FOUND_MSG;
import static com.online.auction.constant.TestConstants.BID_AMOUNT_ONE_HUNDRED;
import static com.online.auction.constant.TestConstants.END_TIME;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.ITEM_NAME_1;
import static com.online.auction.constant.TestConstants.PAINTING_ITEM_CATEGORY;
import static com.online.auction.constant.TestConstants.START_TIME;
import static com.online.auction.constant.TestConstants.TEST_AUCTION;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;



class AuctionServiceImplTest {
    @Mock
    private AuctionListingRepository auctionListingRepository;

    @Mock
    private AuctionBidDetailRepository auctionBidDetailRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuctionDetailsTest() throws ServiceException {
        Item item = new Item();
        item.setItemId(INTEGER_ONE);

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setAuctionId(String.valueOf(INTEGER_ONE));
        auctionDTO.setItemId(String.valueOf(INTEGER_ONE));
        auctionDTO.setSellerId(String.valueOf(INTEGER_ONE));

        User user = new User();
        user.setUserId(INTEGER_ONE);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setSellerId(user);
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.of(auction));

        AuctionDTO result = auctionService.getAuctionDetails(INTEGER_ONE);

        assertEquals(auctionDTO, result);
    }

    @Test
    void getAuctionDetailsWhenAuctionNotPresentTest() {
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.getAuctionDetails(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_NOT_FOUND_MSG, exception.getErrorMessage());
    }

    @Test
    @SneakyThrows
    public void processPostAuctionStateSuccessTest() {
        // Setup test data
        User bidder = new User();
        bidder.setUserId(INTEGER_ONE);

        User seller = new User();
        seller.setUserId(INTEGER_TWO);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setSellerId(seller);

        AuctionBidDetails auctionBidDetails = new AuctionBidDetails();
        auctionBidDetails.setWon(false);
        auctionBidDetails.setBidderId(bidder);
        auctionBidDetails.setBid_amount(100.0);
        auctionBidDetails.setItemId(item);

        Auction auction = new Auction();
        auction.setOpen(true);
        auction.setItems(item);

        Account bidderAccount = new Account();
        bidderAccount.setUserId(INTEGER_ONE);
        bidderAccount.setFunds(200.0);

        Account sellerAccount = new Account();
        sellerAccount.setUserId(INTEGER_TWO);
        sellerAccount.setFunds(50.0);

        // Mocking repository calls
        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(auctionBidDetails);
        when(itemRepository.findById(INTEGER_ONE)).thenReturn(Optional.of(item));
        when(accountRepository.findByUserId(INTEGER_ONE)).thenReturn(bidderAccount);
        when(accountRepository.findByUserId(INTEGER_TWO)).thenReturn(sellerAccount);
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.of(auction));

        // Method under test
        boolean result = auctionService.processPostAuctionState(INTEGER_ONE);

        // Assertions
        assertTrue(result);
        assertTrue(auctionBidDetails.isWon());
        verify(auctionBidDetailRepository, times(1)).save(auctionBidDetails);
        verify(itemRepository, times(1)).save(item);
        verify(accountRepository, times(1)).save(bidderAccount);
        verify(accountRepository, times(1)).save(sellerAccount);
        verify(auctionListingRepository, times(1)).save(auction);
        assertEquals(100.0, item.getSelling_amount(), 0.01);
        assertEquals(bidder, item.getBuyerId());
        assertEquals(150.0, sellerAccount.getFunds(), 0.01);  // Updated amount after adding the bid amount
        assertEquals(100.0, bidderAccount.getFunds(), 0.01);  // Updated amount after deducting the bid amount
    }



    @Test
    public void processPostAuctionStateWhenStateAlreadyUpdatedTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);
        AuctionBidDetails auctionBidDetails = new AuctionBidDetails();
        auctionBidDetails.setWon(true);
        auctionBidDetails.setBidderId(user);

        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(auctionBidDetails);
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.processPostAuctionState(INTEGER_ONE);
        });

        assertEquals(HttpStatus.OK.value(), exception.getStatusCode());
    }

    @Test
    public void processPostAuctionStateNoBidDetailsTest() {
        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.processPostAuctionState(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_RECORD_NOT_FOUND_MSG, exception.getErrorMessage());
    }

    @Test
    void getUpcomingAuctionsTest() {
        List<Auction> auctions = new ArrayList<>();
        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setItem_name(ITEM_NAME_1);
        item.setItem_photo(AUCTION_PHOTO_URL);
        item.setItem_maker(AUCTION_ITEM_MAKER);
        item.setDescription(AUCTION_ITEM_DESCRIPTION);
        item.setMin_bid_amount(100);
        item.setPrice_paid(200);
        item.setCurrency(AUCTION_USD_CURRENCY);
        item.setItem_condition(ItemCondition.NEW);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName(PAINTING_ITEM_CATEGORY);

        User user = new User();
        user.setUserId(INTEGER_ONE);


        City city = new City();
        city.setCityName(AUCTION_CITY_NAME);

        user.setCity(city);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setSellerId(user);
        auction.setStartTime(START_TIME);
        auction.setEndTime(END_TIME);
        auctions.add(auction);

        when(auctionListingRepository.findUpcomingAndCurrentAuctions(any(LocalDateTime.class))).thenReturn(auctions);

        List<AuctionItemsDTO> result = auctionService.getUpcomingAuctions();

        assertEquals(1, result.size());
        assertEquals(ITEM_NAME_1, result.get(0).getItemName());
    }

    @Test
    void getItemsForExistingUserTest() throws ServiceException {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setItem_name(TEST_AUCTION);
        item.setItem_photo(AUCTION_PHOTO_URL);
        item.setItem_maker(AUCTION_ITEM_MAKER);
        item.setDescription(AUCTION_ITEM_DESCRIPTION);
        item.setMin_bid_amount(Double.parseDouble(BID_AMOUNT_ONE_HUNDRED));
        item.setPrice_paid(Double.parseDouble(AUCTION_PRICE_PAID));
        item.setCurrency(AUCTION_USD_CURRENCY);
        item.setItem_condition(ItemCondition.NEW);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName(PAINTING_ITEM_CATEGORY);

        List<Item> items = new ArrayList<>();
        items.add(item);

        User auctionUser = new User();
        auctionUser.setUserId(INTEGER_ONE);

        City city = new City();
        city.setCityName(AUCTION_CITY_NAME);
        auctionUser.setCity(city);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setSellerId(auctionUser);
        auction.setStartTime(START_TIME);
        auction.setEndTime(END_TIME);

        when(itemRepository.findUpcomingAndCurrentItemsExcludingUserItems(any(LocalDateTime.class), eq(user))).thenReturn(items);
        when(auctionListingRepository.findByItems_ItemId(item.getItemId())).thenReturn(Optional.of(auction));

        List<AuctionItemsDTO> result = auctionService.getAuctionsForExistingUser(INTEGER_ONE);

        assertEquals(1, result.size());
        assertEquals(TEST_AUCTION, result.get(0).getItemName());
    }

    @Test
    void getItemsForExistingUserWhenAuctionNotPresentTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findUpcomingAndCurrentItemsExcludingUserItems(any(LocalDateTime.class), eq(user))).thenReturn(items);
        when(auctionListingRepository.findByItems_ItemId(item.getItemId())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.getAuctionsForExistingUser(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_NOT_FOUND_MSG, exception.getErrorMessage());
    }



    @Test
    void getSuggestedItemsSuccessTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setItem_name(TEST_AUCTION);
        item.setItem_photo(AUCTION_PHOTO_URL);
        item.setSellerId(new User());

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setEndTime(LocalDateTime.now().plusDays(1));
        auction.setSellerId(user);

        City city = new City();
        city.setCityName(AUCTION_CITY_NAME);
        user.setCity(city);

        List<Integer> categoryIds = List.of(INTEGER_ONE, INTEGER_TWO);
        List<Item> items = List.of(item);

        when(auctionListingRepository.findDistinctCategoryIdsByUserId(INTEGER_ONE)).thenReturn(categoryIds);
        when(itemRepository.findItemsNotBidByUserInCategories(INTEGER_ONE, categoryIds)).thenReturn(items);
        when(auctionListingRepository.findByItems(item)).thenReturn(Optional.of(auction));

        List<SuggestedItemDTO> result = auctionService.getSuggestedItems(user);

        assertEquals(1, result.size());
        SuggestedItemDTO suggestedItem = result.get(0);
        assertEquals(String.valueOf(INTEGER_ONE), suggestedItem.getAuctionId());
        assertEquals(TEST_AUCTION, suggestedItem.getItemName());
        assertEquals(AUCTION_PHOTO_URL, suggestedItem.getItemPhoto());
        assertEquals(AUCTION_CITY_NAME, suggestedItem.getCityName());
    }

    @Test
    void getSuggestedItemsNoCategoriesFoundTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        when(auctionListingRepository.findDistinctCategoryIdsByUserId(INTEGER_ONE)).thenReturn(new ArrayList<>());

        List<SuggestedItemDTO> result = auctionService.getSuggestedItems(user);

        assertTrue(result.isEmpty());
        verify(auctionListingRepository, times(1)).findDistinctCategoryIdsByUserId(INTEGER_ONE);
        verify(itemRepository, never()).findItemsNotBidByUserInCategories(anyInt(), anyList());
    }

    @Test
    void getSuggestedItemsAuctionNotFoundTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        User differentSeller = new User();
        differentSeller.setUserId(INTEGER_TWO);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setSellerId(differentSeller); // Ensure seller is different

        List<Integer> categoryIds = List.of(INTEGER_ONE);
        List<Item> items = List.of(item);

        when(auctionListingRepository.findDistinctCategoryIdsByUserId(INTEGER_ONE)).thenReturn(categoryIds);
        when(itemRepository.findItemsNotBidByUserInCategories(INTEGER_ONE, categoryIds)).thenReturn(items);
        when(auctionListingRepository.findByItems(item)).thenReturn(Optional.empty());

        List<SuggestedItemDTO> result = auctionService.getSuggestedItems(user);

        assertTrue(result.isEmpty());
        verify(auctionListingRepository, times(1)).findDistinctCategoryIdsByUserId(INTEGER_ONE);
        verify(itemRepository, times(1)).findItemsNotBidByUserInCategories(INTEGER_ONE, categoryIds);
        verify(auctionListingRepository, times(1)).findByItems(item);
    }

    @Test
    void getSuggestedItemsExcludeEndedAuctionsTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        User differentSeller = new User();
        differentSeller.setUserId(INTEGER_TWO);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setSellerId(differentSeller);

        List<Integer> categoryIds = List.of(INTEGER_ONE);
        List<Item> items = List.of(item);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setSellerId(differentSeller);
        auction.setEndTime(LocalDateTime.now().minusDays(1)); // Auction ended yesterday

        when(auctionListingRepository.findDistinctCategoryIdsByUserId(INTEGER_ONE)).thenReturn(categoryIds);
        when(itemRepository.findItemsNotBidByUserInCategories(INTEGER_ONE, categoryIds)).thenReturn(items);
        when(auctionListingRepository.findByItems(item)).thenReturn(Optional.of(auction));

        List<SuggestedItemDTO> result = auctionService.getSuggestedItems(user);

        assertTrue(result.isEmpty());
        verify(auctionListingRepository, times(1)).findDistinctCategoryIdsByUserId(INTEGER_ONE);
        verify(itemRepository, times(1)).findItemsNotBidByUserInCategories(INTEGER_ONE, categoryIds);
        verify(auctionListingRepository, times(1)).findByItems(item);
    }


}