package com.neoclan.identitymanagement.communicationConfig;

import com.neoclan.identitymanagement.dto.communication.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.neoclan.exchange.name}")
    private String notificationExchange;

    @Value("${rabbitmq.login.email.routing.key}")
    private String loginEmailRoutingKey;
    @Value("${rabbitmq.registration.email.routing.key}")
    private String emailRegistrationRoutingKey;

    @Value("${rabbitmq.credit.email.routing.key}")
    private String creditEmailRoutingKey;

    @Value("${rabbitmq.debit.email.routing.key}")
    private String debitEmailRoutingKey;

    public void sendCreditEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, creditEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendDebitEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, debitEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendRegistrationEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, emailRegistrationRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendLoginEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, loginEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
}
