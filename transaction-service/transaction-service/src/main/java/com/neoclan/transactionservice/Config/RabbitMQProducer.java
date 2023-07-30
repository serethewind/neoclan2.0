package com.neoclan.transactionservice.Config;

import com.neoclan.transactionservice.dto.communication.EmailDetails;
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
    private String transactionExchange;

    @Value("${rabbitmq.credit.email.routing.key}")
    private String creditEmailRoutingKey;

    @Value("${rabbitmq.debit.email.routing.key}")
    private String debitEmailRoutingKey;

    public void sendCreditEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(transactionExchange, creditEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendDebitEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(transactionExchange, debitEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
}
