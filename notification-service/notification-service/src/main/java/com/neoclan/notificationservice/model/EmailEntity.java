package com.neoclan.notificationservice.model;


import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")
public class EmailEntity {
    @Id
    private String id;
    private String message;
    private String subject;
    private String attachment;
    @CreatedDate
    private LocalDateTime created;
}
