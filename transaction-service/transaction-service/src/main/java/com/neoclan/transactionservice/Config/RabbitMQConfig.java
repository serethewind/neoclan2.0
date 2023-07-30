package com.neoclan.transactionservice.Config;

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
    private String transactionExchange;

    @Value("${rabbitmq.credit.email.queue.name}")
    private String creditEmailQueue;

    @Value("${rabbitmq.debit.email.queue.name}")
    private String debitEmailQueue;

    @Value("${rabbitmq.credit.email.routing.key}")
    private String creditEmailRoutingKey;

    @Value("${rabbitmq.debit.email.routing.key}")
    private String debitEmailRoutingKey;

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(transactionExchange);
    }

    @Bean
    public Queue creditEmailQueue() {
        return new Queue(creditEmailQueue);
    }

    @Bean
    public Queue debitEmailQueue() {
        return new Queue(debitEmailQueue);
    }

    @Bean
    public Binding creditEmailBinding() {
        return BindingBuilder.bind(creditEmailQueue())
                .to(transactionExchange())
                .with(creditEmailRoutingKey);
    }

    @Bean
    public Binding debitEmailBinding() {
        return BindingBuilder.bind(debitEmailQueue())
                .to(transactionExchange())
                .with(debitEmailRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
