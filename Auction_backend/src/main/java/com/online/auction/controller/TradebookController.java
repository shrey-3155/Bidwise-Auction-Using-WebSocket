package com.online.auction.controller;

import com.online.auction.dto.InvoiceDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.TradebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.USER;

/**
 * Controller for managing tradebook-related operations, including fetching trades and retrieving invoices.
 */
@RestController
@RequestMapping(API_VERSION_V1 + USER)
@RequiredArgsConstructor
public class TradebookController {

    private final TradebookService tradebookService;

    /**
     * Get auction participated or won for user.
     *
     * @param user The authenticated user getting the tradebook.
     * @return A ResponseEntity containing a success response with the result of the get operation.
     * @throws ServiceException If there is an error during the addition of the item.
     */
    @GetMapping("/getTradebook")
    public ResponseEntity<SuccessResponse<List<TradebookDTO>>> getAllItems(@AuthenticationPrincipal User user) throws ServiceException {
        List<TradebookDTO> items = tradebookService.getAllTradesByUser(user);
        SuccessResponse<List<TradebookDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, items);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the invoice for a specific auction.
     *
     * @param auctionId The ID of the auction for which the invoice is requested.
     * @param user      The authenticated user making the request.
     * @return A ResponseEntity containing a SuccessResponse with the InvoiceDTO.
     * @throws ServiceException If an error occurs while retrieving the invoice.
     */
    @GetMapping("/getInvoice")
    public ResponseEntity<SuccessResponse<InvoiceDTO>> getInvoice(@RequestParam("auctionId") int auctionId, @AuthenticationPrincipal User user) throws ServiceException {
        InvoiceDTO invoice = tradebookService.getInvoiceByAuctionId(auctionId);
        SuccessResponse<InvoiceDTO> response = new SuccessResponse<>(200, HttpStatus.OK, invoice);
        return ResponseEntity.ok(response);
    }
}
