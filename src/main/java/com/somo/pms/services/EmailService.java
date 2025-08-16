package com.somo.pms.services;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

public interface EmailService {
    void sendEmailWithToken(String userEmail, String link, String projectName, LocalDateTime expired) throws MessagingException;
}
