package com.neoclan.notificationservice.service;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.dto.Response;

public interface EmailService {
    Response sendSimpleMessage(EmailDetails emailDetails);

    Response sendMessageWithAttachment(EmailDetails emailDetails);
}
