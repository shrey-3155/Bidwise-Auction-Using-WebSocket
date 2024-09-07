package com.online.auction.controller;

import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.ITEM;

/**
 * Controller for managing items in the auction system.
 * Provides endpoints to add, retrieve, and delete items.
 */
@RestController
@RequestMapping(API_VERSION_V1 + ITEM)
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Adds a new item to the auction.
     *
     * @param itemDTO The item details to add.
     * @param files   The image files associated with the item.
     * @param user    The authenticated user adding the item.
     * @return A ResponseEntity containing a success response with the result of the add operation.
     * @throws ServiceException If there is an error during the addition of the item.
     */
    @PostMapping("/additem")
    public ResponseEntity<SuccessResponse<String>> addItem(
            @RequestPart("itemDTO") ItemDTO itemDTO,
            @RequestPart("file") MultipartFile files,
            @AuthenticationPrincipal User user
    ) throws ServiceException {
        String addResponseItem = itemService.addItem(itemDTO, files, user);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, addResponseItem);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all items listed by the authenticated user.
     *
     * @param user The authenticated user whose items are to be retrieved.
     * @return A ResponseEntity containing a success response with the list of items.
     * @throws ServiceException If there is an error during retrieval of items.
     */
    @GetMapping("/getitems")
    public ResponseEntity<SuccessResponse<List<ItemDTO>>> getAllItems(@AuthenticationPrincipal User user) throws ServiceException {
        List<ItemDTO> items = itemService.getAllItemsByUser(user);
        SuccessResponse<List<ItemDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, items);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes an item listed by the authenticated user.
     *
     * @param itemId The ID of the item to be deleted.
     * @param user   The authenticated user requesting the deletion.
     * @return A ResponseEntity containing a success response indicating the deletion status.
     * @throws ServiceException If there is an error during deletion of the item.
     */
    @DeleteMapping("/deleteItemListed")
    public ResponseEntity<SuccessResponse<String>> deleteItem(
            @RequestParam("itemId") int itemId,
            @AuthenticationPrincipal User user
    ) throws ServiceException {
        itemService.deleteItem(itemId, user);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, "Item deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves items by their item ID.
     *
     * @param itemId The ID of the item to retrieve.
     * @return A ResponseEntity containing a success response with the list of items matching the ID.
     * @throws ServiceException If there is an error during retrieval of the items.
     */
    @GetMapping("/itemsById")
    public ResponseEntity<SuccessResponse<List<ItemDTO>>> getItemsByItemId(
            @RequestParam("itemId") Integer itemId
    ) throws ServiceException {
        List<ItemDTO> items = itemService.findItemsByItemId(itemId);
        SuccessResponse<List<ItemDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, items);
        return ResponseEntity.ok(response);
    }

}
