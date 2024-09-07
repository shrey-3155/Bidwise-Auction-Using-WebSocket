package com.online.auction.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for sending email notifications.
 * <p>
 * This class provides functionality to send simple email messages using Spring's {@link JavaMailSender}.
 * It includes methods for constructing and sending email messages with specified recipients, subjects, and bodies.
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailUtils {

    private final JavaMailSender javaMailSender;

    /**
     * Sends an email with the given details.
     * <p>
     * This method constructs a {@link SimpleMailMessage} with the provided recipient email address, subject, and body,
     * and sends it using the configured {@link JavaMailSender}.
     * </p>
     *
     * @param toEmail The recipient's email address.
     * @param subject The subject of the email.
     * @param body    The body content of the email.
     */
    public void sendEmail(String toEmail, String subject, String body) {
        log.info("All information for mail received");
        SimpleMailMessage message = new SimpleMailMessage();
        log.info("Auction Bidwise email set");
        message.setFrom("auctionbidwise@gmail.com");
        log.info("Recepient email set");
        message.setTo(toEmail);
        log.info("Body of email set");
        message.setText(body);
        log.info("Subject of email set");
        message.setSubject(subject);
        log.info("Mail successfully sent");
        javaMailSender.send(message);
    }

}
