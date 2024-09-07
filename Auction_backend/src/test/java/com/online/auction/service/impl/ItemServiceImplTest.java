package com.online.auction.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.ItemCondition;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemCategoryRepository;
import com.online.auction.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.online.auction.constant.TestConstants.END_TIME;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.PASSWORD;
import static com.online.auction.constant.TestConstants.START_TIME;
import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemCategoryRepository itemCategoryRepository;

    @Mock
    private AuctionListingRepository auctionListingRepository;

    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private Uploader uploader;
    @Mock
    private Cloudinary cloudinary;

    private ItemDTO itemDto;
    private User user;
    private MultipartFile mockMultipartFile;
    private ItemCondition itemCondition;
    @BeforeEach
    void setUp() {
        itemDto = new ItemDTO();
        itemDto.setItemName("Antique Vase");
        itemDto.setItemMaker("Unknown");
        itemDto.setDescription("A beautiful antique vase.");
        itemDto.setMinBidAmount(100);
        itemDto.setPricePaid(50);
        itemDto.setCurrency("USD");
        itemDto.setItemPhoto("http://image.url");
        itemDto.setItemCondition(itemCondition);
        itemDto.setCategoryName("Paintings");
        itemDto.setStartTime(START_TIME);
        itemDto.setEndTime(END_TIME);
        mockMultipartFile = mock(MultipartFile.class);
        user = new User();
        user.setUserId(INTEGER_ONE);
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);
        user.setUserId(1);

    }

    @Test
    void addItemSuccessfulTest() throws ServiceException, IOException {
        // Mocking the item category repository
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(INTEGER_ONE);
        itemCategory.setItemCategoryName("Paintings");
        when(itemCategoryRepository.findByItemCategoryName(anyString())).thenReturn(Optional.of(itemCategory));
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());
        when(auctionListingRepository.save(any(Auction.class))).thenReturn(new Auction());
        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://image.url");
        Map<String, String> temp = new HashMap<>();
        when(uploader.upload( mockMultipartFile.getBytes(), temp)).thenReturn(uploadResult);
        String response = itemService.addItem(itemDto, mockMultipartFile, user);
        assertNotNull(response);
        assertEquals("Item listed successfully for Auction", response);
        verify(itemCategoryRepository).findByItemCategoryName(itemDto.getCategoryName());
        verify(itemRepository).save(any(Item.class));
        verify(auctionListingRepository).save(any(Auction.class));
    }


    @Test
    void addItemFailureDueToMissingCategoryTest() {
        when(itemCategoryRepository.findByItemCategoryName(anyString())).thenReturn(Optional.empty());
        ServiceException exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto, mockMultipartFile,user));
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals("Item category is not present", exception.getErrorMessage());
        verify(itemCategoryRepository).findByItemCategoryName(itemDto.getCategoryName());
        verify(itemRepository, never()).save(any(Item.class));
        verify(auctionListingRepository, never()).save(any(Auction.class));
    }

    @Test
    void addItemWithInvalidDataTest() {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("Valid Category");
        when(itemCategoryRepository.findByItemCategoryName(anyString())).thenReturn(Optional.of(itemCategory));
        itemDto.setItemName(null);
        ServiceException exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto,mockMultipartFile, user));
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals("Item name is required", exception.getErrorMessage());
        itemDto.setItemName("Antique Vase");
        itemDto.setMinBidAmount(-10);
        exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto,mockMultipartFile, user));
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals("Minimum bid amount must be positive", exception.getErrorMessage());
        verify(itemRepository, never()).save(any(Item.class));
        verify(auctionListingRepository, never()).save(any(Auction.class));
    }

    @Test
    void getAllItemsByUserTest() {
        // Mocking repository to return items for a specific user
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("Paintings");

        Item item1 = Item.builder()
                .itemId(1)
                .item_name("Item 1")
                .sellerId(user)
                .itemcategory(itemCategory)
                .build();

        Item item2 = Item.builder()
                .itemId(2)
                .item_name("Item 221")
                .sellerId(user)
                .itemcategory(itemCategory)
                .build();
        when(itemRepository.findBySellerId(any(User.class))).thenReturn(Arrays.asList(item1, item2));
        List<ItemDTO> items = itemService.getAllItemsByUser(user);
        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getItemName());
        assertEquals("Item 221", items.get(1).getItemName());
    }

    @Test
    void deleteItemSuccessfulTest() throws ServiceException {
        // Mocking repository to return an item for deletion
        Item item = new Item();
        item.setItemId(1);
        item.setSellerId(user);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        // Call the service method
        itemService.deleteItem(1, user);

        // Verify repository interactions
        verify(itemRepository).findById(1);
        verify(auctionListingRepository).deleteByItems(item);
        verify(itemRepository).delete(item);
    }

    @Test
    void deleteItemUnauthorizedTest() {
        // Mocking repository to return an item for deletion
        Item item = new Item();
        item.setItemId(1);
        item.setSellerId(new User()); // Different user

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        // Call the service method and assert ServiceException
        assertThrows(ServiceException.class, () -> itemService.deleteItem(1, user));

        // Verify repository interactions
        verify(itemRepository).findById(1);
        verify(auctionListingRepository, never()).deleteByItems(any(Item.class));
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    void deleteItemNotFoundTest() {
        // Mocking repository to return empty for item deletion
        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        // Call the service method and assert ServiceException
        assertThrows(ServiceException.class, () -> itemService.deleteItem(1, user));

        // Verify repository interactions
        verify(itemRepository).findById(1);
        verify(auctionListingRepository, never()).deleteByItems(any(Item.class));
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    void findItemsByItemIdItemsFoundTest() throws ServiceException {
        Integer itemId = 1;
        List<Item> items = new ArrayList<>();
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(1);
        itemCategory.setItemCategoryName("Electronics");
        User seller = new User();
        seller.setUserId(1);
        Item item = Item.builder()
                .itemId(itemId)
                .item_name("Test Item")
                .item_maker("Test Maker")
                .description("Test Description")
                .min_bid_amount(100.0)
                .price_paid(150.0)
                .currency("USD")
                .item_photo("test_photo.jpg")
                .item_condition(itemCondition)
                .itemcategory(itemCategory)
                .sellerId(seller)
                .build();
        items.add(item);
        when(itemRepository.findByItemId(itemId)).thenReturn(items);
        when(auctionListingRepository.findByItems(any(Item.class))).thenReturn(Optional.of(new Auction()));

        List<ItemDTO> result = itemService.findItemsByItemId(itemId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getItemName());
        verify(itemRepository, times(1)).findByItemId(itemId);
    }

    @Test
    void findItemsByItemIdNoItemsFoundTest() throws ServiceException {
        Integer itemId = 1;
        when(itemRepository.findByItemId(itemId)).thenReturn(new ArrayList<>());

        List<ItemDTO> result = itemService.findItemsByItemId(itemId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).findByItemId(itemId);
    }

}