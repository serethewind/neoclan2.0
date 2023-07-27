package com.neoclan.notificationservice.service;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.dto.Response;

public interface EmailService {
    String sendSimpleMessage(EmailDetails emailDetails);

    String sendMessageWithAttachment(EmailDetails emailDetails);
}
