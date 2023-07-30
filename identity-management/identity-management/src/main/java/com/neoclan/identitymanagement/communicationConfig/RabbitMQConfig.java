package com.neoclan.identitymanagement.communicationConfig;


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

    @Value("${rabbitmq.neoclan.exchange.name}")
    private String notificationExchange;

    @Value("${rabbitmq.registration.email.queue.name}")
    private String registrationEmailQueue;

    @Value("${rabbitmq.login.email.queue.name}")
    private String loginEmailQueue;

    @Value("${rabbitmq.registration.email.routing.key}")
    private String registrationEmailRoutingKey;

    @Value("${rabbitmq.login.email.routing.key}")
    private String loginEmailRoutingKey;


    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchange);
    }

    @Bean
    public Queue registrationEmailQueue() {
        return new Queue(registrationEmailQueue);
    }

    public Queue loginEmailQueue() {
        return new Queue(loginEmailQueue);
    }

    @Bean
    public Binding registrationEmailBinding() {
        return BindingBuilder.bind(registrationEmailQueue())
                .to(notificationExchange())
                .with(registrationEmailRoutingKey);
    }

    @Bean
    public Binding loginEmailBinding() {
        return BindingBuilder.bind(loginEmailQueue())
                .to(notificationExchange())
                .with(loginEmailRoutingKey);
    }

    //rabbit template for sending json messages
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    //amqpTemplate is an interface that is implemented by rabbit template. it supports json message
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
