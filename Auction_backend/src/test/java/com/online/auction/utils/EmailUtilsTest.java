package com.online.auction.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import static com.online.auction.constant.TestConstants.EMAIL_BODY;
import static com.online.auction.constant.TestConstants.RECEPIENT_EMAIL;
import static com.online.auction.constant.TestConstants.EMAIL_SUBJECT;


public class EmailUtilsTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailUtils emailUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void name() {
    }

    @Test
    void sendEmail_Success() {
        // Act
        emailUtils.sendEmail(RECEPIENT_EMAIL, EMAIL_SUBJECT, EMAIL_BODY);

        // Capture the SimpleMailMessage that was passed to the javaMailSender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        // Assert
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals("auctionbidwise@gmail.com", capturedMessage.getFrom());
        assertEquals(RECEPIENT_EMAIL, Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals(EMAIL_SUBJECT, capturedMessage.getSubject());
        assertEquals(EMAIL_BODY, capturedMessage.getText());
    }
}
