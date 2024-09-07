package com.online.auction.service.impl;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfileUserExistsTest() throws ServiceException {
        UserProfileDTO userProfile = new UserProfileDTO();
        when(profileRepository.findUserProfileByUserId(1)).thenReturn(userProfile);

        UserProfileDTO result = profileService.getUserProfile(1);

        assertNotNull(result);
        verify(profileRepository, times(1)).findUserProfileByUserId(1);
    }

    @Test
    void getUserProfileUserNotFoundTest() {
        when(profileRepository.findUserProfileByUserId(1)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.getUserProfile(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).findUserProfileByUserId(1);
    }

    @Test
    void countUserParticipatedAuctionsAuctionsFoundTest() throws ServiceException {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(5L);

        long result = profileService.countUserParticipatedAuctions(1);

        assertEquals(5L, result);
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }


    @Test
    void countUserParticipatedAuctionsNoAuctionsFoundTest() throws ServiceException {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(0L);

        long result = profileService.countUserParticipatedAuctions(1);

        assertEquals(0L, result);
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }

    @Test
    void countUserParticipatedAuctionsNegativeCountTest() {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(-1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.countUserParticipatedAuctions(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }

    @Test
    void getBidStatsStatsFoundTest() throws ServiceException {
        // Mock data for the bid statistics, assuming month is 1-based (January = 1, February = 2, etc.)
        List<BidStatsDTO> bidStats = new ArrayList<>();
        bidStats.add(new BidStatsDTO(1, 5, 2)); // January: 5 total participated auctions, 2 won auctions
        bidStats.add(new BidStatsDTO(2, 3, 1)); // February: 3 total participated auctions, 1 won auction

        // Mock the repository method call
        when(profileRepository.findBidStatsByUserIdForCurrentYear(1)).thenReturn(bidStats);

        // Call the service method
        List<BidStatsDTO> result = profileService.getBidStats(1);

        // Assertions
        assertEquals(12, result.size(), "The result should contain stats for all 12 months"); // Ensure stats for all 12 months are present

        // January checks
        BidStatsDTO januaryStats = result.get(0); // January is the first month in the list
        assertEquals(2, januaryStats.getTotalParticipatedAuctions(), "January total participated auctions should be 5");
        assertEquals(5, januaryStats.getWonAuctions(), "January won auctions should be 2");

        // February checks
        BidStatsDTO februaryStats = result.get(1); // February is the second month in the list
        assertEquals(1, februaryStats.getTotalParticipatedAuctions(), "February total participated auctions should be 3");
        assertEquals(3, februaryStats.getWonAuctions(), "February won auctions should be 1");

        // Check for other months (ensure they are initialized correctly)
        for (int i = 2; i < result.size(); i++) { // Starting from March (index 2)
            BidStatsDTO monthStats = result.get(i);
            assertEquals(0, monthStats.getTotalParticipatedAuctions(), "Other months should have zero total participated auctions");
            assertEquals(0, monthStats.getWonAuctions(), "Other months should have zero won auctions");
        }

        // Verify repository interaction
        verify(profileRepository, times(1)).findBidStatsByUserIdForCurrentYear(1);
    }


    @Test
    void getCategoryBidStatsStatsFoundTest() throws ServiceException {
        List<CategoryBidStatsDTO> categoryBidStats = new ArrayList<>();
        categoryBidStats.add(new CategoryBidStatsDTO("Electronics", 10));
        when(profileRepository.findCategoryBidStatsByUserId(1)).thenReturn(categoryBidStats);

        List<CategoryBidStatsDTO> result = profileService.getCategoryBidStats(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
        assertEquals(10, result.get(0).getBidCount());
        verify(profileRepository, times(1)).findCategoryBidStatsByUserId(1);
    }

    @Test
    void getCategoryBidStatsNoStatsFoundTest() {
        when(profileRepository.findCategoryBidStatsByUserId(1)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.getCategoryBidStats(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).findCategoryBidStatsByUserId(1);
    }

}
