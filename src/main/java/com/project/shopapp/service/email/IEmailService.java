package com.project.shopapp.service.email;

public interface IEmailService {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);
}
