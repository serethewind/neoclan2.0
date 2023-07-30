package com.neoclan.notificationservice.config.communication;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.registration.email.queue.name}")
    public void sendRegistrationEmailNotification(EmailDetails emailDetails) {
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    @RabbitListener(queues = "${rabbitmq.login.email.queue.name}")
    public void sendLoginEmailNotification(EmailDetails emailDetails) {
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    @RabbitListener(queues = "${rabbitmq.credit.email.queue.name}")
    public void sendCreditEmailNotification(EmailDetails emailDetails){
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
    @RabbitListener(queues = "${rabbitmq.debit.email.queue.name}")
    public void sendDebitEmailNotification(EmailDetails emailDetails){
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
}
