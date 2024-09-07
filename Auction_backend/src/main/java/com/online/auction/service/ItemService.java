package com.online.auction.service;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing items in the auction system.
 * <p>
 * This interface provides methods for adding, retrieving, and deleting auction items.
 * </p>
 */
public interface ItemService {
    /**
     * Adds a new item to the auction system.
     * <p>
     * This method processes the provided item details and the associated file (such as an image),
     * and associates the item with the specified user.
     * </p>
     *
     * @param itemDTO The data transfer object containing details of the item to be added.
     * @param file    The file associated with the item, such as an image.
     * @param user    The user adding the item.
     * @return A unique identifier or other relevant information about the added item.
     * @throws ServiceException If an error occurs while adding the item, such as validation errors
     *                          or issues with the file.
     */
    String addItem(ItemDTO itemDTO, MultipartFile file, User user) throws ServiceException;

    /**
     * Retrieves all items associated with a specific user.
     * <p>
     * This method returns a list of items that belong to the specified user.
     * </p>
     *
     * @param user The user whose items are to be retrieved.
     * @return A list of data transfer objects representing the items belonging to the user.
     * @throws ServiceException If an error occurs while retrieving the items, such as database
     *                          access issues or user-related problems.
     */
    List<ItemDTO> getAllItemsByUser(User user) throws ServiceException;

    /**
     * Deletes an item from the auction system.
     * <p>
     * This method removes the specified item and ensures that the item belongs to the provided user.
     * </p>
     *
     * @param itemId The unique identifier of the item to be deleted.
     * @param user   The user requesting the deletion of the item.
     * @throws ServiceException If an error occurs while deleting the item, such as the item not
     *                          existing or issues with user permissions.
     */
    void deleteItem(int itemId, User user) throws ServiceException;

    /**
     * Finds items based on their unique identifier.
     * <p>
     * This method retrieves a list of items that match the specified item ID.
     * </p>
     *
     * @param itemId The unique identifier of the item(s) to be found.
     * @return A list of data transfer objects representing the items with the specified ID.
     * @throws ServiceException If an error occurs while finding the items, such as database access
     *                          issues or invalid item ID.
     */
    List<ItemDTO> findItemsByItemId(Integer itemId) throws ServiceException;
}
