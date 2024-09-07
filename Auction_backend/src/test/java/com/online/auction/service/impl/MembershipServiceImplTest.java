package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static com.online.auction.constant.TestConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private MembershipServiceImpl membershipServiceImpl;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPremium(false);
    }

    @Test
    public void testUpgradeToPremium_Success() throws ServiceException {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        String result = membershipServiceImpl.upgradeToPremium(TEST_EMAIL);

        assertEquals(TEST_EMAIL, result);
        assertEquals(true, user.isPremium());
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).save(user);
        verify(accountService, times(1)).addFunds(user.getUserId(), 100);
    }

    @Test
    public void testUpgradeToPremium_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            membershipServiceImpl.upgradeToPremium(TEST_EMAIL);
        });

        assertEquals(USER_NOT_FOUND, exception.getErrorMessage());
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void isPremiumWhenUserIsNotPremiumTest() throws ServiceException {
        user.setPremium(false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Boolean isPremium = membershipServiceImpl.isPremium(user);

        assertFalse(isPremium);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void isPremiumWhenUserNotFoundTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            membershipServiceImpl.isPremium(user);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(USER_NOT_FOUND, exception.getErrorMessage());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void cancelPremiumSuccessTest() throws ServiceException {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPremium(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        membershipServiceImpl.cancelPremium(email);

        // Assert
        assertFalse(user.isPremium());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void cancelPremiumUserNotFoundTest() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            membershipServiceImpl.cancelPremium(email);
        });

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}
