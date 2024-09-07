package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import com.online.auction.utils.EmailUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTests {

    private static final String EXPECTED_SUBJECT = "Password Reset Request";
    private static final String EXPECTED_BODY = "To reset your password, click the link below:\nhttp://172.17.3.242/reset-password?token=";


    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtils emailUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendPasswordResetLink_ShouldSendMail() throws ServiceException {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setResetToken(UUID.randomUUID().toString());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        passwordResetService.sendPasswordResetLink(email);

        verify(emailUtils, times(1)).sendEmail(eq(email), eq(EXPECTED_SUBJECT), eq(EXPECTED_BODY + user.getResetToken()));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void sendPasswordResetLink_UserNotFound_ShouldThrowException() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            passwordResetService.sendPasswordResetLink(email);
        });
    }


    @Test
    void resetPassword_ShouldUpdatePassword() throws ServiceException {
        String token = "resetToken";
        String newPassword = "newPassword";
        User user = new User();
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        passwordResetService.resetPassword(token, newPassword);

        verify(userRepository, times(1)).save(user);
        assert user.getPassword().equals("encodedPassword");
        assert user.getResetToken() == null;
    }

    @Test
    void resetPassword_InvalidToken_ShouldThrowException() {
        String token = "invalidToken";
        when(userRepository.findByResetToken(token)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            passwordResetService.resetPassword(token, "newPassword");
        });
    }
}
