package com.neoclan.notificationservice.repository;

import com.neoclan.notificationservice.model.EmailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailEntity, String> {
}
