package com.online.auction.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemCategoryRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.online.auction.constant.AuctionConstants.*;

/**
 * Service implementation for handling item-related operations.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final AuctionListingRepository auctionListingRepository;
    private final Cloudinary cloudinary;

    /**
     * Adds a new item to the auction.
     *
     * @param itemDto The item details to add.
     * @param file    The image file associated with the item.
     * @param user    The authenticated user adding the item.
     * @return A success message indicating the item was listed successfully.
     * @throws ServiceException If there is an error during the addition of the item.
     */
    @Override
    public String addItem(ItemDTO itemDto, MultipartFile file, User user) throws ServiceException {
        log.debug("Attempting to add a new item: {}", itemDto);

        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByItemCategoryName(itemDto.getCategoryName());
        if (itemCategory.isEmpty()) {
            log.warn("Item category '{}' is not present", itemDto.getCategoryName());
            throw new ServiceException(HttpStatus.BAD_REQUEST, ITEM_CATEGORY_NOT_FOUND);
        }

        if (itemDto.getMinBidAmount() < 0) {
            log.warn("Minimum bid amount is negative: {}", itemDto.getMinBidAmount());
            throw new ServiceException(HttpStatus.BAD_REQUEST, NEGATIVE_BID_AMOUNT);
        }

        if (itemDto.getItemName() == null || itemDto.getItemName().isEmpty()) {
            log.warn("Item name is missing in the provided item data: {}", itemDto);
            throw new ServiceException(HttpStatus.BAD_REQUEST, EMPTY_ITEM_NAME);
        }

        String imageUrl = uploadImageToCloudinary(file);
        itemDto.setItemPhoto(imageUrl);

        var item = Item.builder()
                .item_name(itemDto.getItemName())
                .item_maker(itemDto.getItemMaker())
                .description(itemDto.getDescription())
                .min_bid_amount(itemDto.getMinBidAmount())
                .price_paid(itemDto.getPricePaid())
                .currency(itemDto.getCurrency())
                .item_photo(itemDto.getItemPhoto())
                .item_condition(itemDto.getItemCondition())
                .itemcategory(itemCategory.get())
                .sellerId(user)
                .build();

        log.debug("Built item entity: {}", item);

        Item savedItem = itemRepository.save(item);
        log.info("Item saved successfully: {}", savedItem);

        var auction = Auction.builder()
                .startTime(itemDto.getStartTime())
                .endTime(itemDto.getEndTime())
                .items(savedItem)
                .isOpen(true)
                .sellerId(user)
                .build();

        log.debug("Built auction entity: {}", auction);

        Auction savedAuction = auctionListingRepository.save(auction);
        log.info("Auction saved successfully: {}", savedAuction);

        String successMessage = "Item listed successfully for Auction";
        log.info(successMessage);
        return successMessage;
    }

    /**
     * Uploads an image file to Cloudinary and returns its URL.
     *
     * @param file The image file to upload.
     * @return The URL of the uploaded image.
     * @throws ServiceException If there is an error during the upload process.
     */
    private String uploadImageToCloudinary(MultipartFile file) throws ServiceException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, IMAGE_UPLOAD_FAILED);
        }
    }

    /**
     * Retrieves all items listed by the authenticated user.
     *
     * @param user The authenticated user whose items are to be retrieved.
     * @return A list of item data transfer objects representing the user's items.
     */
    @Override
    public List<ItemDTO> getAllItemsByUser(User user) {
        log.debug("Fetching items for user: {}", user.getEmail());
        List<Item> items = itemRepository.findBySellerId(user);
        List<ItemDTO> itemDTOs = items.stream().map(this::convertToItemDTO).collect(Collectors.toList());

        log.debug("Fetched {} items for user: {}", items.size(), user.getEmail());

        return itemDTOs;
    }

    /**
     * Converts an Item entity to an ItemDTO object.
     *
     * @param item The item entity to convert.
     * @return The corresponding item data transfer object.
     */
    private ItemDTO convertToItemDTO(Item item) {
        Auction auction = auctionListingRepository.findByItems(item).orElse(null);
        LocalDateTime endTime = auction != null ? auction.getEndTime() : null;
        boolean isAuctionEnded = endTime != null && endTime.isBefore(LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(AMERICAN_TIME_ZONE)).toLocalDateTime()); // Check if auction has ended
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItem_name())
                .itemMaker(item.getItem_maker())
                .description(item.getDescription())
                .minBidAmount(item.getMin_bid_amount())
                .pricePaid(item.getPrice_paid())
                .currency(item.getCurrency())
                .itemPhoto(item.getItem_photo())
                .itemCondition(item.getItem_condition())
                .categoryName(item.getItemcategory().getItemCategoryName())
                .startTime(auction != null ? auction.getStartTime() : null)
                .endTime(auction != null ? auction.getEndTime() : null)
                .soldPrice(item.getSelling_amount())
                .isAuctionEnded(isAuctionEnded)
                .build();
    }

    /**
     * Deletes an item listed by the authenticated user.
     *
     * @param itemId The ID of the item to be deleted.
     * @param user   The authenticated user requesting the deletion.
     * @throws ServiceException If the item is not found or if the user is not authorized to delete the item.
     */
    public void deleteItem(int itemId, User user) throws ServiceException {
        log.debug("Deleting item with ID: {} for user: {}", itemId, user.getEmail());
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, ITEM_NOT_FOUND));

        if (item.getSellerId().getUserId() != user.getUserId()) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, USER_NOT_AUTHORIZED);
        }
        log.info("Deleted Auction for ID: {} for user: {}", itemId, user.getEmail());
        auctionListingRepository.deleteByItems(item);
        log.info("Deleted Item for ID: {} for user: {}:", itemId, user.getEmail());
        itemRepository.delete(item);
        log.info("Deleted item with ID: {} for user: {}", itemId, user.getEmail());
    }

    /**
     * Retrieves items by item ID.
     *
     * @param itemId the item ID to search for
     * @return a list of item data transfer objects
     * @throws ServiceException if an error occurs while retrieving the items
     */
    @Override
    public List<ItemDTO> findItemsByItemId(Integer itemId) throws ServiceException {
        log.debug("Fetching items by itemId");

        List<Item> items = itemRepository.findByItemId(itemId);
        List<ItemDTO> itemDTOs = items.stream().map(this::convertToItemDTO).collect(Collectors.toList());

        log.debug("Fetched {} items by itemId", items.size());

        return itemDTOs;
    }
}
