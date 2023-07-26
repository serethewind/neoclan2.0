package com.neoclan.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailDetails {
    private String recipient;
    private String message;
    private String subject;
    private String attachment;
}
