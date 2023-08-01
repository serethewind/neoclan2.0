package com.neoclan.notificationservice.config.communication;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.registration.email.queue.name}")
    private String emailRegistrationQueue;

    @Value("${rabbitmq.login.email.queue.name}")
    private String loginEmailQueue;
    @Value("${rabbitmq.credit.email.queue.name}")
    private String creditEmailQueue;
    @Value("${rabbitmq.debit.email.queue.name}")
    private String debitEmailQueue;

//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    @Value("${rabbitmq.json.routing.key}")
//    private String routingJsonKey;

    @Bean
    public Queue emailRegistrationQueue() {
        return new Queue(emailRegistrationQueue);
    }

    @Bean
    public Queue emailLoginQueue(){
        return new Queue(loginEmailQueue);
    }
    @Bean
    public Queue emailCreditQueue(){
        return new Queue(creditEmailQueue);
    }
    @Bean
    public Queue emailDebitQueue(){
        return new Queue(debitEmailQueue);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
// amqpTemplate is an interface that is implemented by rabbit template. it supports json message
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
//
//    @Bean
//    public Queue queue() {
//        return new Queue(queue);
//    }
//
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(exchange);
//    }
//
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue())
//                .to(exchange())
//                .with(routingKey);
//    }
//
//    @Bean
//    public Binding jsonBinding(){
//        return BindingBuilder.bind(jsonQueue())
//                .to(exchange())
//                .with(routingJsonKey);
//    }

}
